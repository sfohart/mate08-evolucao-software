package br.ufba.dcc.disciplinas.mate08.view;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import mining.challenge.android.bugreport.model.Bug;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;

@Dependent
public class LazyBugDataModel extends LazyDataModel<Bug> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1473031061591313522L;

	@Inject 
	@BugQualifier
	private BugRepository repository;

	@Override
	public List<Bug> load(
			int first, 
			int pageSize,
			String sortField, 
			SortOrder sortOrder, 
			Map<String, String> filters) {
		
		List<Bug> data = repository.findAll(first, pageSize);
		
        //rowCount  
        int dataSize = repository.countAll().intValue();  
        this.setRowCount(dataSize);
        
        return data;
	}

}
