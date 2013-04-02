package br.ufba.dcc.disciplinas.mate08.view;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;

import mining.challenge.android.bugreport.model.Bug;

import org.primefaces.model.SortOrder;


@Dependent
public class LazyOwnedBugDataModel extends LazyBugDataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1473031061591313522L;

	@Override
	public List<Bug> load(
			int first, 
			int pageSize,
			String sortField, 
			SortOrder sortOrder, 
			Map<String, String> filters) {
		
		List<Bug> data = repository.findAllOwnedBugs(first, pageSize);
		
        //rowCount  
        int dataSize = repository.countAllOwnedBugs().intValue();  
        this.setRowCount(dataSize);
        
        setPageSize(pageSize);
        
        
        return data;
	}

}
