package br.ufba.dcc.disciplinas.mate08.view.components;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;

import mining.challenge.android.bugreport.model.Bug;

import org.primefaces.model.SortOrder;

@Dependent
public class LazyNotOwnedBugDataModel extends LazyBugDataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6007146345072421096L;

	@Override
	public List<Bug> load(
			int first, 
			int pageSize,
			String sortField, 
			SortOrder sortOrder, 
			Map<String, String> filters) {
		
		List<Bug> data = repository.findAllNotOwnedBugs(first, pageSize);
		
        //rowCount  
        int dataSize = repository.countAllNotOwnedBugs().intValue();  
        this.setRowCount(dataSize);
        
        setPageSize(pageSize);
        
        
        return data;
	}

}
