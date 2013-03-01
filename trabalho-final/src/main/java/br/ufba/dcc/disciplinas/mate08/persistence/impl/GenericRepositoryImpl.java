package br.ufba.dcc.disciplinas.mate08.persistence.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.ufba.dcc.disciplinas.mate08.persistence.basic.GenericRepository;


public abstract class GenericRepositoryImpl<E,ID> implements GenericRepository<E,ID> {

	@Inject
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<E> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E findById(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E save(E entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(E entity) {
		// TODO Auto-generated method stub
	}

	
}
