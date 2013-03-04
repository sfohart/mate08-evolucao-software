package br.ufba.dcc.disciplinas.mate08.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class BaseModel<ID extends Serializable> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7945670440130889562L;

	@Id
	@GeneratedValue
	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

}
