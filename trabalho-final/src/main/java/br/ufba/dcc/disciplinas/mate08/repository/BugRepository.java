package br.ufba.dcc.disciplinas.mate08.repository;

import java.io.Serializable;
import java.util.List;

import mining.challenge.android.bugreport.model.Bug;

public interface BugRepository extends BaseRepository<Long, Bug>, Serializable {
	
	public List<Bug> findAllOwnedBugs();	
	public List<Bug> findAllNotOwnedBugs();
	public Long countAllOwnedBugs();
	public Long countAllNotOwnedBugs();
	public List<Bug> findAllOwnedBugs(Integer startAt, Integer offset);
	public List<Bug> findAllNotOwnedBugs(Integer startAt, Integer offset);

}
