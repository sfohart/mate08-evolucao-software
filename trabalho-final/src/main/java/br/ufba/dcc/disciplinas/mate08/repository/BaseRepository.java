package br.ufba.dcc.disciplinas.mate08.repository;

import java.io.Serializable;
import java.util.List;

import br.ufba.dcc.disciplinas.mate08.model.BaseEntity;

public interface BaseRepository<ID extends Serializable, E extends BaseEntity<ID>> {

	public Long countAll();
	public List<E> findAll();	
	public List<E> findAll(Integer startAt, Integer offset);
	
	public E findById(ID id);
	public void save(E entity);
	public void delete(E entity);
	
}
