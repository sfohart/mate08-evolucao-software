package br.ufba.dcc.disciplinas.mate08.repository;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<ID extends Serializable, E extends Serializable> {

	public Long countAll();
	public List<E> findAll();	
	public List<E> findAll(String orderBy);
	public List<E> findAll(Integer startAt, Integer offset, String orderBy);
	
	public E findById(ID id);
	public void save(E entity) throws Exception;
	public void delete(E entity)  throws Exception;
	
}
