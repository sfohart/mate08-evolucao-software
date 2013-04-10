package br.ufba.dcc.disciplinas.mate08.mahout.classifier;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import mining.challenge.android.bugreport.model.Bug;
import mining.challenge.android.bugreport.model.Developer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.ep.State;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.LuceneTextValueEncoder;

import br.ufba.dcc.disciplinas.mate08.lucene.indexer.BugIndexer;
import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.qualifier.ConfigurationValue;
import br.ufba.dcc.disciplinas.mate08.qualifier.DeveloperQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;
import br.ufba.dcc.disciplinas.mate08.repository.DeveloperRepository;

/**
 * 
 * Mahout In Action, página 292 do PDF.
 * 
 * @author leandro.ferreira
 *
 */
public class BugClassifier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2517748248513800802L;
	
	
	private AdaptiveLogisticRegression learningAlgorithm;
	private Analyzer analyzer;
	
	@Inject
	@DeveloperQualifier
	private DeveloperRepository developerRepository;
	
	@Inject
	@BugQualifier
	private BugRepository bugRepository;
	
	@Inject
	@ConfigurationValue(required=true)
	private Integer descriptionProbes;
	
	@Inject
	@ConfigurationValue(required=true)
	private Integer titleProbes;
	
	@Inject
	@ConfigurationValue(required=true)
	private Integer componentProbes;
	
	@Inject
	@ConfigurationValue(value="learningAlgorithm.interval")
	private Integer learningInterval;
	
	@Inject
	@ConfigurationValue(value="learningAlgorithm.averagingWindow")
	private Integer averagingWindow;
	
	
	private Map<String, FeatureVectorEncoder> encoderMap;
	
	@PostConstruct
	public void init() throws URISyntaxException, IOException {		
		this.analyzer = new StandardAnalyzer(Version.LUCENE_36);
		
		initEncoderMap();		
	}

	/**
	 * 
	 */
	private void initEncoderMap() {
		this.encoderMap = new HashMap<String, FeatureVectorEncoder>();
		
		this.encoderMap.put(BugIndexer.OWNER_FIELD, new AdaptiveWordValueEncoder(BugIndexer.OWNER_FIELD));
		this.encoderMap.put(BugIndexer.REPORTED_BY_FIELD, new AdaptiveWordValueEncoder(BugIndexer.REPORTED_BY_FIELD));
		this.encoderMap.put(BugIndexer.STATUS_FIELD, new AdaptiveWordValueEncoder(BugIndexer.STATUS_FIELD));
		this.encoderMap.put(BugIndexer.PRIORITY_FIELD, new AdaptiveWordValueEncoder(BugIndexer.PRIORITY_FIELD));
		
		LuceneTextValueEncoder descriptionEncoder = new LuceneTextValueEncoder(BugIndexer.DESCRIPTION_FIELD);
		descriptionEncoder.setAnalyzer(analyzer);	
		descriptionEncoder.setProbes(descriptionProbes);
		this.encoderMap.put(BugIndexer.DESCRIPTION_FIELD, descriptionEncoder);
		
		LuceneTextValueEncoder titleEncoder = new LuceneTextValueEncoder(BugIndexer.TITLE_FIELD);
		titleEncoder.setAnalyzer(analyzer);	
		titleEncoder.setProbes(titleProbes);
		this.encoderMap.put(BugIndexer.TITLE_FIELD, titleEncoder);
		
		LuceneTextValueEncoder componentEncoder = new LuceneTextValueEncoder(BugIndexer.COMPONENT_FIELD);
		componentEncoder.setAnalyzer(analyzer);
		componentEncoder.setProbes(componentProbes);
		this.encoderMap.put(BugIndexer.COMPONENT_FIELD, componentEncoder);
	}
	
	private Integer getCardinality() {
		return descriptionProbes + titleProbes + componentProbes;
	}
	
	public void test() {
		int categoryCount = developerRepository.countAll().intValue();
		
		this.learningAlgorithm = new AdaptiveLogisticRegression(categoryCount, getCardinality(), new L1());
		this.learningAlgorithm.setInterval(learningInterval);
		this.learningAlgorithm.setAveragingWindow(averagingWindow);
		
		Dictionary ownerGroups = new Dictionary();
		
		//todos os bugs já atribuidos a algum desenvolvedor para usar na fase de treinamento
		List<Bug> bugs = bugRepository.findAllOwnedBugs();
		
		if (bugs != null && ! bugs.isEmpty()) {
			//embaralhando a coleção
			Collections.shuffle(bugs);
			
			//construindo um dicionário de "categorias", que correspondem aos emails dos desenvolvedores
			List<Developer> developers = developerRepository.findAll();
			if (developers != null && ! developers.isEmpty()) {
				for (Developer developer : developers) {
					ownerGroups.intern(developer.getEmail());
				}
			}
			
			int k = 0;
			for (Bug bug : bugs) {
				int actual = ownerGroups.intern(bug.getOwner().getEmail());
				
				//codificando o bug num vetor de termos
				Vector vector = encodeFeatureVector(bug, actual);
				
				//treinando o classificador
				learningAlgorithm.train(actual, vector);				
				k++;
				
				//melhor resultado encontrado pelo classificador
				State<AdaptiveLogisticRegression.Wrapper, CrossFoldLearner> best =
						learningAlgorithm.getBest();
				
				if (best != null && (k % learningInterval != 0)) {
					CrossFoldLearner model = best.getPayload().getLearner();
					double averageCorrect = model.percentCorrect();
					double averageLL = model.logLikelihood();
					System.out.printf("%d\t%.3f\t%.2f\t\n",
							k, averageLL, averageCorrect * 100);
				}
			}
		}
		 
		
		
	}

	private Vector encodeFeatureVector(Bug bug, int actual) {		
		LuceneTextValueEncoder descriptionEncoder = (LuceneTextValueEncoder) encoderMap.get(BugIndexer.DESCRIPTION_FIELD);
		Vector description = encode(bug.getDescription(), descriptionEncoder, descriptionProbes);
		
		
		return description;
	}

	/**
	 * 
	 * @see https://raw.github.com/MaineC/sofia/44137f1d76be7c1bb1bd7e745dba23670184f687/sof-trainer/sofia/src/main/java/de/isabeldrostfromm/sof/mahout/StandardVectoriser.java
	 * 
	 * @param text
	 * @param encoder
	 * @param probes
	 * @return
	 */
	private Vector encode(String text, LuceneTextValueEncoder encoder, int probes) {
		encoder.addText(text);
		
		Vector vector = new RandomAccessSparseVector(probes);
		encoder.flush(1, vector);
		
		return vector;
	}
	
	
	private Vector encode(int probes, String text, AdaptiveWordValueEncoder encoder) {
		Vector vector = new SequentialAccessSparseVector(probes);		
		
		encoder.setProbes(probes);
		encoder.addToVector(text, vector);
		
		return vector;
	}
	
}
