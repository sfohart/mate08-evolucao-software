package br.ufba.dcc.disciplinas.mate08.persistence.basic;

import java.util.List;

public interface GenericRepository<E,ID> {
	
	public List<E> findAll();
	
	public E findById(ID id);
	
	public void save(E entity);
	
	public void delete(E entity);
	
}
