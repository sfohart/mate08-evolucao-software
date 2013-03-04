package br.ufba.dcc.disciplinas.mate08.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.ufba.dcc.disciplinas.mate08.model.BaseEntity;
import br.ufba.dcc.disciplinas.mate08.repository.BaseRepository;

@Dependent
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BaseRepositoryImpl<ID extends Serializable, E extends BaseEntity<ID>>
	implements BaseRepository<ID,E> {

	@Inject
	private EntityManager entityManager;
	
	private Class<ID> idClass;
	private Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseRepositoryImpl() {
		idClass = (Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	
	
	public List<E> findAll(){
		TypedQuery<E> query = createSelectAllQuery();
		List<E> result = query.getResultList();
		
		return result;
	}

	private TypedQuery<E> createSelectAllQuery() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		
		Root<E> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		
		TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
		return query;
	}
	
	public List<E> findAll(Integer startAt, Integer offset) {
		TypedQuery<E> query = createSelectAllQuery();
		
		query
			.setMaxResults(offset)
			.setFirstResult(startAt);
		
		List<E> result = query.getResultList();
		
		return result;
	}
	
	public E findById(ID id) {
		return entityManager.find(entityClass, id);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(E entity) {
		entityManager.persist(entity);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(E entity) {
		entityManager.remove(entity);
	}



	@Override
	public Long countAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(entityClass)));
		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}
	
}
