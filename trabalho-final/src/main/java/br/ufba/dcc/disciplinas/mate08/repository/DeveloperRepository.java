package br.ufba.dcc.disciplinas.mate08.repository;

import java.io.Serializable;
import java.util.List;

import mining.challenge.android.bugreport.model.Developer;

public interface DeveloperRepository extends BaseRepository<Integer, Developer>, Serializable {

	public abstract List<Developer> findByEmail(List<String> emails);

	public abstract Developer findByEmail(String email);

}
