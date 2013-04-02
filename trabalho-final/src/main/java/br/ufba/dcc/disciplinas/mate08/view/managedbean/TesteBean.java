package br.ufba.dcc.disciplinas.mate08.view.managedbean;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufba.dcc.disciplinas.mate08.lucene.indexer.BugIndexer;
import br.ufba.dcc.disciplinas.mate08.view.components.LazyOwnedBugDataModel;

import mining.challenge.android.bugreport.model.Bug;

@Named
@ConversationScoped
public class TesteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6903752123217243426L;

	@Inject
	private LazyOwnedBugDataModel lazyDataModel;
	
	
	private Bug selectedBug;
	
	public Bug getSelectedBug() {
		return selectedBug;
	}
	
	public void setSelectedBug(Bug selectedBug) {
		this.selectedBug = selectedBug;
	}
	
	

	public LazyOwnedBugDataModel getLazyDataModel() {
		return lazyDataModel;
	}

}
