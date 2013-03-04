package br.ufba.dcc.disciplinas.mate08.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import mining.challenge.android.bugreport.model.Bug;

@Table(name="developer")
@Entity
public class DeveloperModel extends BaseModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391175927035588862L;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	private List<Bug> bugs;
	
	public DeveloperModel() {
		bugs = new ArrayList<>();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Bug> getBugs() {
		return bugs;
	}
	
	public void setBugs(List<Bug> bugs) {
		this.bugs = bugs;
	}

}
