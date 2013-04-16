package br.ufba.dcc.disciplinas.mate08.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mining.challenge.android.bugreport.model.Developer;
import br.ufba.dcc.disciplinas.mate08.qualifier.DeveloperQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.DeveloperRepository;

@DeveloperQualifier
public class DeveloperRepositoryImpl extends BaseRepositoryImpl<Integer,Developer>
implements DeveloperRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4997037257406128764L;
	
	@Override
	public Developer findByEmail(String email) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Developer> criteriaQuery = criteriaBuilder.createQuery(Developer.class);
		Root<Developer> root = criteriaQuery.from(Developer.class);
		
		
		Predicate ownerPredicate = criteriaBuilder.equal(root.get("email"), email);
		criteriaQuery.where(ownerPredicate);
		
		criteriaQuery.select(root);
		
		TypedQuery<Developer> query = getEntityManager().createQuery(criteriaQuery);
		Developer result = query.getSingleResult();
		
		return result;
	}
	
	@Override
	public List<Developer> findByEmail(List<String> emails) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Developer> criteriaQuery = criteriaBuilder.createQuery(Developer.class);
		Root<Developer> root = criteriaQuery.from(Developer.class);
		
		
		Predicate ownerPredicate = criteriaBuilder.in(root.get("email")).in(emails);
		criteriaQuery.where(ownerPredicate);
		
		criteriaQuery.select(root);
		
		TypedQuery<Developer> query = getEntityManager().createQuery(criteriaQuery);
		List<Developer> result = query.getResultList();
		
		return result;
	}

}
