package br.ufba.dcc.disciplinas.mate08.view.managedbean;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

import br.ufba.dcc.disciplinas.mate08.mahout.vectorizer.BugVectorizer;

@Named
@ConversationScoped
public class VectorizeIndexBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6279655534546197421L;
	
	@Inject
	private BugVectorizer bugVectorizer;
	
	public void createVector(ActionEvent actionEvent) throws Exception {
		bugVectorizer.vectorizeBugIndex();
		FacesContext.getCurrentInstance().addMessage("Vetorização concluída", new FacesMessage("Vetorização concluída com sucesso"));
	}


}
