package br.ufba.dcc.disciplinas.mate08.persistence.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.ufba.dcc.disciplinas.mate08.persistence.basic.GenericRepository;


public abstract class GenericRepositoryImpl<E,ID> implements GenericRepository<E,ID> {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Produces
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	private Class<E> entityClass;
	private Class<ID> pkClass;
	
	@SuppressWarnings("unchecked")
	public GenericRepositoryImpl() {
		entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		pkClass = (Class<ID>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	

	@Override
	public List<E> findAll() {
		 CriteriaBuilder cb = entityManager.getCriteriaBuilder(); //Step 1
         CriteriaQuery<E> cqry = cb.createQuery(entityClass);

         //Interesting stuff happens here
         Root<E> root = cqry.from(entityClass); //Step 2
         cqry.select(root); 
         
         //Boilerplate code
         TypedQuery<E> qry = entityManager.createQuery(cqry); 
         List<E> results = qry.getResultList(); 
         
         return results;
	}

	@Override
	public E findById(ID id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public void save(E entity) {		
		entityManager.persist(entity);
	}

	@Override
	public void delete(E entity) {
		// TODO Auto-generated method stub
	}

	
}
