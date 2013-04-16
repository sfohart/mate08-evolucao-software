package br.ufba.dcc.disciplinas.mate08.view.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mining.challenge.android.bugreport.model.Bug;
import br.ufba.dcc.disciplinas.mate08.mahout.classifier.BugClassifier;
import br.ufba.dcc.disciplinas.mate08.qualifier.BugClassifierQualifier;
import br.ufba.dcc.disciplinas.mate08.view.components.LazyOwnedBugDataModel;

@Named
@ConversationScoped
public class TesteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6903752123217243426L;

	@Inject
	private LazyOwnedBugDataModel lazyDataModel;
	
	@Inject
	@BugClassifierQualifier
	private BugClassifier classifier;
	
	private Bug selectedBug;
	
	@PostConstruct
	public void init() throws ParseException, IOException {
		int[] topKValues = {3,5,10};
		
		for (int topK : topKValues) {
			for (int i = 0; i < 100; i++) {
				classifier.trainClassifier(topK);
			}
		}
	}
	
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
