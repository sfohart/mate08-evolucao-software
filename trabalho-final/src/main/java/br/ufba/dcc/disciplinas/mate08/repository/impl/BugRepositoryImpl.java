package br.ufba.dcc.disciplinas.mate08.repository.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mining.challenge.android.bugreport.model.Bug;
import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;

import com.ibm.icu.util.Calendar;

@BugQualifier
public class BugRepositoryImpl extends BaseRepositoryImpl<Long, Bug>
implements BugRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1789992132917763163L;

	
	public List<Bug> findAllReleasedOwnedBugs() {
		return findAllReleasedOwnedBugs(null, null);
	}
	
	public List<Bug> findAllNotOwnedBugs() {
		return findAllNotOwnedBugs(null, null);
	}
	
	@Override
	public Long countAllReleasedOwnedBugs() {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Bug> root = criteriaQuery.from(Bug.class);
				
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Bug.class)));
		
		Predicate ownerPredicate = criteriaBuilder.isNotNull(root.get("owner"));
		Predicate releasedPredicate = criteriaBuilder.equal(root.get("status"), "Released");		
		Predicate predicate = criteriaBuilder.and(ownerPredicate,releasedPredicate);
		
		criteriaQuery.where(predicate);
		
		return getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}
	
	@Override
	public Long countAllNotOwnedBugs() {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Bug> root = criteriaQuery.from(Bug.class);
				
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Bug.class)));
		
		Predicate ownerPredicate = criteriaBuilder.isNull(root.get("owner"));
		criteriaQuery.where(ownerPredicate);
		
		return getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}
	
	@Override
	public List<Bug> findAll() {

		return super.findAll();
	}
	
	
	@Override
	public List<Bug> findAllNotOwnedBugs(Integer startAt, Integer offset) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Bug> criteriaQuery = criteriaBuilder.createQuery(Bug.class);
		
		Root<Bug> root = criteriaQuery.from(Bug.class);
		
		CriteriaQuery<Bug> select = criteriaQuery.select(root);
		select.orderBy(criteriaBuilder.asc(root.get("openedDate")));
		
		Predicate ownerPredicate = criteriaBuilder.isNull(root.get("owner"));
		criteriaQuery.where(ownerPredicate);
		
		TypedQuery<Bug> query = getEntityManager().createQuery(criteriaQuery);
		
		if (startAt != null && offset != null) {
			query
				.setMaxResults(offset)
				.setFirstResult(startAt);
		}
		
		List<Bug> result = query.getResultList();
		
		return result;
		
	}
	
	public List<Bug> findAllBugsToExperiment(Integer numLastMonths, Long minTotalBugsPerDeveloper) {
		return findAllBugsToExperiment(numLastMonths, minTotalBugsPerDeveloper ,null,null);
	}
	
	public List<Bug> findAllBugsToExperiment(Integer numLastMonths, Long minTotalBugsPerDeveloper, Integer startAt, Integer offset)  {
		
		
		TypedQuery<Date> maxOpenedDateQuery = getEntityManager().createQuery("SELECT MAX(bug.openedDate) FROM Bug bug", Date.class);
		Date maxOpenedDate = maxOpenedDateQuery.getSingleResult();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(maxOpenedDate);
		
		calendar.add(Calendar.DAY_OF_YEAR, - 30 * numLastMonths);
		
		//Timestamp minOpenedDate = new Timestamp(calendar.getTimeInMillis());
		Date minOpenedDate = calendar.getTime();
		
		StringBuffer buffer = new StringBuffer();
		
		/*//todos os bugs que tem status "Released"
		buffer.append("SELECT bug FROM Bug bug JOIN FETCH bug.owner WHERE bug.status = :status  ");
		//e cujos desenvolvedores...
		buffer.append("AND bug.owner IN ( ");
				//corrigiram algum bug nos últimos 3 meses
				buffer.append("SELECT DISTINCT dev FROM Developer dev JOIN dev.ownedBugs bug WHERE (bug.openedDate BETWEEN :start AND :end) AND bug.status = :status ");
				//e tenham corrigido (durante o projeto inteiro) pelo menos 9 bugs
				buffer.append("AND size(bug) >= :minTotal ");
		buffer.append(") ");*/
		
		buffer.append("select * from bug where owner_id in ( ");
		buffer.append("	select dev from ( ");
		buffer.append("		select  ");
		buffer.append("			distinct dev.id as dev, ");
		buffer.append("			( ");
		buffer.append("				select  ");
		buffer.append("					count(*)  ");
		buffer.append("				from  ");
		buffer.append("					bug  ");
		buffer.append("				where  ");
		buffer.append("					owner_id = dev.id ");
		buffer.append("					and opened_date between :start and :end ");
		buffer.append("			) as totalLastThreeMonths ");
		buffer.append("		from  ");
		buffer.append("			bug  ");
		buffer.append("			join developer dev on bug.owner_id = dev.id ");
		buffer.append("		where 	 ");
		buffer.append("			owner_id is not null ");
		buffer.append("			and status = :status 		 ");
		buffer.append("		order by totalLastThreeMonths desc, dev asc ");
		buffer.append(") as dados ");
		buffer.append("	where totalLastThreeMonths >= :minTotalBugs ");
		buffer.append(") ");
		
		
		//TypedQuery<Bug> query = getEntityManager().createQuery(buffer.toString(), Bug.class);
		Query query = getEntityManager().createNativeQuery(buffer.toString(), Bug.class);
		query.setParameter("minTotalBugs", minTotalBugsPerDeveloper);
		query.setParameter("status", "Released");
		query.setParameter("start", minOpenedDate, TemporalType.TIMESTAMP);
		query.setParameter("end", maxOpenedDate, TemporalType.TIMESTAMP);
		
		if (startAt != null && offset != null) {
			query
				.setMaxResults(offset)
				.setFirstResult(startAt);
		}
		
		List<Bug> result = (List<Bug>) query.getResultList();
		
		return result;
		
	}
	
	@Override
	public List<Bug> findAllReleasedOwnedBugs(Integer startAt, Integer offset) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Bug> criteriaQuery = criteriaBuilder.createQuery(Bug.class);
		
		Root<Bug> root = criteriaQuery.from(Bug.class);
		CriteriaQuery<Bug> select = criteriaQuery.select(root);
		select.orderBy(criteriaBuilder.asc(root.get("openedDate")));
		
		Predicate ownerPredicate = criteriaBuilder.isNotNull(root.get("owner"));
		Predicate releasedPredicate = criteriaBuilder.equal(root.get("status"), "Released");		
		Predicate predicate = criteriaBuilder.and(ownerPredicate,releasedPredicate);
		
		criteriaQuery.where(predicate);
		
		TypedQuery<Bug> query = getEntityManager().createQuery(criteriaQuery);
		
		if (startAt != null && offset != null) {
			query
				.setMaxResults(offset)
				.setFirstResult(startAt);
		}
		
		List<Bug> result = query.getResultList();
		
		return result;
		
	}

	

}
