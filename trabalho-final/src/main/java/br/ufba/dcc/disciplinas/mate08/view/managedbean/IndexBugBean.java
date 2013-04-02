package br.ufba.dcc.disciplinas.mate08.view.managedbean;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufba.dcc.disciplinas.mate08.lucene.indexer.BugIndexer;

@Named
@ConversationScoped
public class IndexBugBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5265051895367163472L;
	
	@Inject
	private BugIndexer bugIndexer;
	
	public void createIndex(ActionEvent actionEvent) throws Exception {
		Integer numDocs = bugIndexer.index();
		bugIndexer.close();
		FacesContext.getCurrentInstance().addMessage("Indexação concluída", new FacesMessage(numDocs + " bug(s) indexado(s)."));
	}

}
