package br.ufba.dcc.disciplinas.mate08.view.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mining.challenge.android.bugreport.model.Bug;
import mining.challenge.android.bugreport.model.Developer;
import br.ufba.dcc.disciplinas.mate08.mahout.classifier.BugClassifier;
import br.ufba.dcc.disciplinas.mate08.model.ScoredItem;
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
		/*int[] topKValues = {1,3,5,7,10};
		
		for (int topK : topKValues) {
			for (int i = 0; i < 150; i++) {
				classifier.trainClassifier(topK);
			}
		}*/
		
		/*for (int i = 0; i < 150; i++) {
			classifier.trainClassifier(10);
		}*/
	}
	
	public Bug getSelectedBug() {
		return selectedBug;
	}
	
	
	public List<ScoredItem<Developer>> getTopKDevelopers() {
		List<ScoredItem<Developer>> list = null;
		
		if (classifier != null && getSelectedBug() != null) {
			list = classifier.classifyTopK(getSelectedBug(), 10);
		}
		
		return list;
	}
	
	public void setSelectedBug(Bug selectedBug) {
		this.selectedBug = selectedBug;
	}

	public LazyOwnedBugDataModel getLazyDataModel() {
		return lazyDataModel;
	}

}
