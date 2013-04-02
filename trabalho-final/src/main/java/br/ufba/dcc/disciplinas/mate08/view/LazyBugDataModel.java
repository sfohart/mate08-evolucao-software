package br.ufba.dcc.disciplinas.mate08.view;

import javax.inject.Inject;

import mining.challenge.android.bugreport.model.Bug;

import org.primefaces.model.LazyDataModel;

import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;

public abstract class LazyBugDataModel extends LazyDataModel<Bug> {

	@Inject
	@BugQualifier
	protected BugRepository repository;

	public LazyBugDataModel() {
		super();
	}

	@Override
	public void setRowIndex(int rowIndex) {
	    /*
	     * The following is in ancestor (LazyDataModel):
	     * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
	     */
	    if (rowIndex == -1 || getPageSize() == 0) {
	        super.setRowIndex(-1);
	    }
	    else {
	        super.setRowIndex(rowIndex % getPageSize());
	    }      
	}

	@Override
	public Bug getRowData(String rowKey) {
		Long id = Long.valueOf(rowKey);
		Bug bug = repository.findById(id);
		return bug;
	}

	@Override
	public Object getRowKey(Bug bug) {		
		return bug.getId();
	}

}