package br.ufba.dcc.disciplinas.mate08.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import br.ufba.dcc.disciplinas.mate08.repository.BaseRepository;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BaseRepositoryImpl<ID extends Serializable, E extends Serializable>
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
	
	
		
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}



	public List<E> findAll(){
		return findAll(null);
	}
	
	public List<E> findAll(String orderBy){
		TypedQuery<E> query = null;
		if (orderBy == null || "".equals(orderBy)) {
			query = createSelectAllQuery();
		} else {
			query = createSelectAllQuery(orderBy);
		}
		
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
	
	private TypedQuery<E> createSelectAllQuery(String orderBy) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		
		Root<E> root = criteriaQuery.from(entityClass);
		CriteriaQuery<E> select = criteriaQuery.select(root);
		select.orderBy(criteriaBuilder.asc(root.get(orderBy)));
		
		TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
		return query;		
	}
	
	
	public List<E> findAll(Integer startAt, Integer offset, String orderBy) {
		TypedQuery<E> query = null;
		if (orderBy == null || "".equals(orderBy)) {
			query = createSelectAllQuery();
		} else {
			query = createSelectAllQuery(orderBy);
		}
		
		query
			.setMaxResults(offset)
			.setFirstResult(startAt);
		
		List<E> result = query.getResultList();
		
		return result;
	}
	
	public List<E> findAll(Integer startAt, Integer offset) {
		return findAll(startAt, offset, null);
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
