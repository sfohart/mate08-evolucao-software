package br.ufba.dcc.disciplinas.mate08.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

	
	public List<Bug> findAllOwnedBugs() {
		return findAllOwnedBugs(null, null);
	}
	
	public List<Bug> findAllNotOwnedBugs() {
		return findAllNotOwnedBugs(null, null);
	}
	
	@Override
	public Long countAllOwnedBugs() {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Bug> root = criteriaQuery.from(Bug.class);
				
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Bug.class)));
		
		Predicate ownerPredicate = criteriaBuilder.isNotNull(root.get("owner"));
		criteriaQuery.where(ownerPredicate);
		
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
	
	@Override
	public List<Bug> findAllOwnedBugs(Integer startAt, Integer offset) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Bug> criteriaQuery = criteriaBuilder.createQuery(Bug.class);
		
		Root<Bug> root = criteriaQuery.from(Bug.class);
		CriteriaQuery<Bug> select = criteriaQuery.select(root);
		select.orderBy(criteriaBuilder.asc(root.get("openedDate")));
		
		Predicate ownerPredicate = criteriaBuilder.isNotNull(root.get("owner"));
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

	

}
