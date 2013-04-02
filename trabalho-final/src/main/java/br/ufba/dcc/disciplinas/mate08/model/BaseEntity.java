package br.ufba.dcc.disciplinas.mate08.model;

import java.io.Serializable;

public interface BaseEntity<ID extends Serializable> extends Serializable {

	public ID getId();	
	public void setId(ID id);

}
