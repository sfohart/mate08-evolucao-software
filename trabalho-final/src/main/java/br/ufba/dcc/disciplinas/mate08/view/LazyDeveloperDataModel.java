package br.ufba.dcc.disciplinas.mate08.view;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.ufba.dcc.disciplinas.mate08.model.DeveloperEntity;
import br.ufba.dcc.disciplinas.mate08.qualifier.Developer;
import br.ufba.dcc.disciplinas.mate08.repository.DeveloperRepository;

@Dependent
public class LazyDeveloperDataModel extends LazyDataModel<DeveloperEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1473031061591313522L;

	@Inject
	@Developer
	private DeveloperRepository repository;

	@Override
	public List<DeveloperEntity> load(
			int first, 
			int pageSize,
			String sortField, 
			SortOrder sortOrder, 
			Map<String, String> filters) {
		
		List<DeveloperEntity> data = repository.findAll(first, pageSize);
		
        //rowCount  
        int dataSize = repository.countAll().intValue();  
        this.setRowCount(dataSize);
        
        return data;
	}

}
