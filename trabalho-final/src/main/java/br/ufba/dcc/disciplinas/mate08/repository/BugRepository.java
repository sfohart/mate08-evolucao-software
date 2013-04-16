package br.ufba.dcc.disciplinas.mate08.repository;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import mining.challenge.android.bugreport.model.Bug;

public interface BugRepository extends BaseRepository<Long, Bug>, Serializable {
	
	public List<Bug> findAllReleasedOwnedBugs();	
	public List<Bug> findAllNotOwnedBugs();
	
	
	public List<Bug> findAllBugsToExperiment(Integer numLastMonths, Long minTotalBugsPerDeveloper) throws ParseException;
	public List<Bug> findAllBugsToExperiment(Integer numLastMonths, Long minTotalBugsPerDeveloper, Integer startAt, Integer offset) ;
	
	public Long countAllReleasedOwnedBugs();
	public Long countAllNotOwnedBugs();
	
	public List<Bug> findAllReleasedOwnedBugs(Integer startAt, Integer offset);
	public List<Bug> findAllNotOwnedBugs(Integer startAt, Integer offset);

}
