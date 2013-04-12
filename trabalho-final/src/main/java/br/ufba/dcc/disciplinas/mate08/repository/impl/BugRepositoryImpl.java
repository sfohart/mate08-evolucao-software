package br.ufba.dcc.disciplinas.mate08.repository.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import mining.challenge.android.bugreport.model.Bug;
import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;

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
	
	public List<Bug> findAllBugsToExperiment() throws ParseException {
		return findAllBugsToExperiment(null,null);
	}
	
	public List<Bug> findAllBugsToExperiment(Integer startAt, Integer offset) throws ParseException {
		
		StringBuffer query = new StringBuffer();
		
		//todos os bugs que tem status "Released"
		query.append("SELECT bug FROM Bug bug JOIN FETCH bug.owner WHERE bug.status = :status  ");
		//e cujos desenvolvedores...
		query.append("AND bug.owner IN ( ");
				//corrigiram algum bug nos últimos 3 meses
				query.append("SELECT DISTINCT dev FROM Developer dev JOIN dev.ownedBugs bug WHERE (bug.openedDate BETWEEN :start AND :end) AND bug.status = :status ");
				//e tenham corrigido (durante o projeto inteiro) pelo menos 9 bugs
				query.append("AND size(dev.ownedBugs) >= :minTotal ");
		query.append(") ");
		
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		TypedQuery<Bug> typedQuery = getEntityManager().createQuery(query.toString(), Bug.class);
		typedQuery.setParameter("minTotal", 9);
		typedQuery.setParameter("status", "Released");
		typedQuery.setParameter("start", dateFormat.parse("2011-06-22 19:29:33"), TemporalType.TIMESTAMP);
		typedQuery.setParameter("end", dateFormat.parse("2011-09-22 19:29:33"), TemporalType.TIMESTAMP);
		
		if (startAt != null && offset != null) {
			typedQuery
				.setMaxResults(offset)
				.setFirstResult(startAt);
		}
		
		List<Bug> result = typedQuery.getResultList();
		
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
