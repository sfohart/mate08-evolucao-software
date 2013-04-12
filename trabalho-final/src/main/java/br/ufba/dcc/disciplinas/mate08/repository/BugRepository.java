package br.ufba.dcc.disciplinas.mate08.repository;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import mining.challenge.android.bugreport.model.Bug;

public interface BugRepository extends BaseRepository<Long, Bug>, Serializable {
	
	public List<Bug> findAllReleasedOwnedBugs();	
	public List<Bug> findAllNotOwnedBugs();
	
	
	public List<Bug> findAllBugsToExperiment() throws ParseException;
	public List<Bug> findAllBugsToExperiment(Integer startAt, Integer offset) throws ParseException;
	
	public Long countAllReleasedOwnedBugs();
	public Long countAllNotOwnedBugs();
	
	public List<Bug> findAllReleasedOwnedBugs(Integer startAt, Integer offset);
	public List<Bug> findAllNotOwnedBugs(Integer startAt, Integer offset);

}
