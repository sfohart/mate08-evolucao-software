package br.ufba.dcc.disciplinas.mate08.mahout.classifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import mining.challenge.android.bugreport.model.Bug;
import mining.challenge.android.bugreport.model.Developer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.ModelDissector;
import org.apache.mahout.ep.State;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.Vectors;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.LuceneTextValueEncoder;

import br.ufba.dcc.disciplinas.mate08.lucene.indexer.BugIndexer;
import br.ufba.dcc.disciplinas.mate08.model.ScoredItem;
import br.ufba.dcc.disciplinas.mate08.qualifier.BugClassifierQualifier;
import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.qualifier.ConfigurationValue;
import br.ufba.dcc.disciplinas.mate08.qualifier.DeveloperQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;
import br.ufba.dcc.disciplinas.mate08.repository.DeveloperRepository;

import com.google.common.collect.Lists;

/**
 * 
 * Mahout In Action, página 292 do PDF.
 * 
 * @author leandro.ferreira
 *
 */
@Dependent
@BugClassifierQualifier
public class BugClassifier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2517748248513800802L;
	
	private AdaptiveLogisticRegression learningAlgorithm;
	private Analyzer analyzer;
	
	@Inject
	@ConfigurationValue
	private String stopWordsListPath;
	
	@Inject
	private Logger logger;
	
	@Inject
	@ConfigurationValue(required=true)
	private Boolean testMinPosition;
	
	
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
	@ConfigurationValue(required=true)
	private Integer typeProbes;
	
	@Inject
	@ConfigurationValue(required=true)
	private Integer priorityProbes;
	
	@Inject
	@ConfigurationValue(value="learningAlgorithm.interval")
	private Integer learningInterval;
	
	@Inject
	@ConfigurationValue(value="learningAlgorithm.averagingWindow")
	private Integer averagingWindow;
	
	
	private Map<String, FeatureVectorEncoder> encoderMap;
	private Dictionary ownerGroups;
	
	@PostConstruct
	public void init() throws URISyntaxException, IOException {		
		
		URL stopWordFilePath = getClass().getClassLoader().getResource(stopWordsListPath);
		
		FileReader stopWordsReader = new FileReader(new File(stopWordFilePath.toURI()));
		
		this.analyzer = new StandardAnalyzer(Version.LUCENE_36, stopWordsReader);
		
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
		this.encoderMap.put(BugIndexer.TYPE_FIELD, new AdaptiveWordValueEncoder(BugIndexer.TYPE_FIELD));
		
		LuceneTextValueEncoder descriptionEncoder = new LuceneTextValueEncoder(BugIndexer.DESCRIPTION_FIELD);
		descriptionEncoder.setAnalyzer(analyzer);	
		this.encoderMap.put(BugIndexer.DESCRIPTION_FIELD, descriptionEncoder);
		
		LuceneTextValueEncoder titleEncoder = new LuceneTextValueEncoder(BugIndexer.TITLE_FIELD);
		titleEncoder.setAnalyzer(analyzer);	
		this.encoderMap.put(BugIndexer.TITLE_FIELD, titleEncoder);
		
		LuceneTextValueEncoder componentEncoder = new LuceneTextValueEncoder(BugIndexer.COMPONENT_FIELD);
		componentEncoder.setAnalyzer(analyzer);
		this.encoderMap.put(BugIndexer.COMPONENT_FIELD, componentEncoder);
	}
	
	protected int getCardinality() {
		int cardinality = 0;
		
		cardinality += titleProbes;
		cardinality += descriptionProbes;
		cardinality += componentProbes;
		cardinality += typeProbes;
		cardinality += priorityProbes;
		
		
		return cardinality;
	}
	
	
	private Vector classify(Bug bug) {
		CrossFoldLearner learner =
				learningAlgorithm.getBest().getPayload().getLearner();
		
		Vector vector = encodeFeatureVector(bug);
		Vector resultScores = learner.classifyFull(vector);
		
		return resultScores;
	}
	
	public List<ScoredItem<Developer>> classifyTopK(Bug bug, int limit) {
		Vector vector = classify(bug);
		Vector topKScores = Vectors.topKElements(limit, vector);
		
		
		PriorityQueue<ScoredItem<Developer>> priorityQueue = new PriorityQueue<ScoredItem<Developer>>();
		
		
		Iterator<Vector.Element> iterator = topKScores.iterateNonZero();
		while (iterator.hasNext()) {
			Vector.Element element = iterator.next();
			String emailDeveloper = ownerGroups.values().get(element.index());
			Developer developer = developerRepository.findByEmail(emailDeveloper);
			
			priorityQueue.add(new ScoredItem<Developer>(developer, element.get()));
			
			while (priorityQueue.size() > limit) {
				priorityQueue.poll();
			}
		}
		
		List<ScoredItem<Developer>> list = Lists.newArrayList(priorityQueue);
		Collections.sort(list, Collections.reverseOrder());
		
		return list;
	}
	
	public Developer classifyBest(Bug bug) {
		Vector vector = classify(bug);
		int developerIndex = vector.maxValueIndex();
		
		String emailDeveloper = ownerGroups.values().get(developerIndex);
		
		Developer developer = developerRepository.findByEmail(emailDeveloper);
		return developer;
	}
	
	public void trainClassifier(int topK) throws ParseException, IOException {		
		ownerGroups = new Dictionary();
		
		//todos os bugs já atribuidos a algum desenvolvedor para usar na fase de treinamento
		List<Bug> bugs = bugRepository.findAllBugsToExperiment(3, 9L);
		Collections.shuffle(bugs);
		for (Bug bug : bugs) {
			ownerGroups.intern(bug.getOwner().getEmail());
		}
		
		int numCategories = ownerGroups.values().size();
		this.learningAlgorithm = new AdaptiveLogisticRegression(numCategories, getCardinality(), new L1());
		this.learningAlgorithm.setInterval(learningInterval);
		
		int trainSize = (int) Math.rint(bugs.size() * 0.8);
		
		List<Bug> trainList = bugs.subList(0, trainSize);
		List<Bug> testList = bugs.subList(trainSize, bugs.size());
		
		if (trainList != null && ! trainList.isEmpty()) {
			//embaralhando a coleção
			Collections.shuffle(trainList);
			
			for (Bug bug : trainList) {
				int actual = ownerGroups.intern(bug.getOwner().getEmail());
				
				//codificando o bug num vetor de termos
				Vector vector = encodeFeatureVector(bug);
				
				//treinando o classificador
				learningAlgorithm.train(actual, vector);				
				
				//melhor resultado encontrado pelo classificador
				State<AdaptiveLogisticRegression.Wrapper, CrossFoldLearner> best =
						learningAlgorithm.getBest();				
			}
		}
		
		learningAlgorithm.close();
		
		//dissecTest(ownerGroups, testList);
		analyzeTest(ownerGroups, testList, topK);
		
	}

	private void dissecTest(Dictionary ownerGroups, List<Bug> testList) {
		ModelDissector dissector = new ModelDissector();
		CrossFoldLearner learner =
				learningAlgorithm.getBest().getPayload().getLearner();
		
		Map<String, Set<Integer>> traceDictionary = new HashMap<String, Set<Integer>>();
		for (String key : encoderMap.keySet()) {
			FeatureVectorEncoder encoder = encoderMap.get(key);
			encoder.setTraceDictionary(traceDictionary);
			encoderMap.put(key, encoder);
		}
		
		for (Bug bug : testList) {
			traceDictionary.clear();
			Vector v = encodeFeatureVector(bug);
			dissector.update(v, traceDictionary, learner);
		}
		
		List<ModelDissector.Weight> weights = dissector.summary(100);
		for (ModelDissector.Weight w : weights) {
			System.out.printf("%s\t%.1f\n", w.getFeature(), w.getWeight());
		}
	}
	
	
	/**
	 * @param ownerGroups
	 * @param testList
	 * @throws IOException 
	 */
	private void analyzeTest(Dictionary ownerGroups, List<Bug> testList, int topK) throws IOException {
		File summarizeFile = new File(System.getProperty("java.io.tmpdir"), "summarize-statistic.txt");
		FileWriter summarizeWriter = new FileWriter(summarizeFile, true);
		
		File kMinFile = new File(System.getProperty("java.io.tmpdir"), "kmin-statistic.txt");
		FileWriter kMinWriter = new FileWriter(kMinFile, true);
		
		
		if (testMinPosition) {
			System.out.println(kMinFile.getAbsolutePath());
		} else {
			System.out.println(summarizeFile.getAbsolutePath());			
		}
		
		int topKCorrectlyClassified = 0;
		int topKIncorrectlyClassified = 0;
		
		
		continueTest: for (Bug bug : testList) {
			int minPositionInList = Integer.MAX_VALUE;
			
			List<ScoredItem<Developer>> topKList = classifyTopK(bug, topK);
			String correctLabel = bug.getOwner().getEmail();
			
			for (int i = 0; i < topKList.size(); i++) {
				Developer developer = topKList.get(i).getItem();
				
				if (correctLabel.equals(developer.getEmail())) {
					
					if (testMinPosition) {
						if (i + 1 < minPositionInList) {
							minPositionInList = i + 1;
							break;
						}						
					} else {						
						topKCorrectlyClassified++;
						continue continueTest;
					}
															
					
				}					
			}
			
			if (! testMinPosition) {
				topKIncorrectlyClassified++;
			} else {
				kMinWriter.append(String.format("%d,%d\n",bug.getId(), minPositionInList));
			}
		}
		
		/*continueTest: for (Bug bug : testList) {			
			Vector input = encodeFeatureVector(bug);
			Vector resultScores = learner.classifyFull(input);
			Vector topKScores = Vectors.topKElements(topK, resultScores);
			
			Iterator<Vector.Element> iterator = topKScores.iterateNonZero();
			
			String correctLabel = bug.getOwner().getEmail();
			String classifiedLabel = ownerGroups.values().get(topKScores.maxValueIndex());
			
			while (iterator.hasNext()) {
				Vector.Element element = iterator.next();
				String topKValue = ownerGroups.values().get(element.index());
				
				if (topKValue.equals(correctLabel)) {
					topKCorrectlyClassified++;					
					continue continueTest;
				} 
			}
			
			topKIncorrectlyClassified++;
			
			
		}*/
		
		
		if (! testMinPosition) {
			int topKTotalClassified = topKCorrectlyClassified + topKIncorrectlyClassified;
			double topKPercentageCorrect = (double) 100 * topKCorrectlyClassified / topKTotalClassified;
			double topKPercentageIncorrect = (double) 100 * topKIncorrectlyClassified / topKTotalClassified;
			
			NumberFormat decimalFormatter = new DecimalFormat("0.0####", DecimalFormatSymbols.getInstance(Locale.US));
			
			String log = String.format("%d,%s,%s\n",
					topK,	    		 
					decimalFormatter.format(topKPercentageCorrect), 
					decimalFormatter.format(topKPercentageIncorrect)
					);
			
			System.out.println(log);
			summarizeWriter.append(log);
		} 
	    
	    summarizeWriter.close();
	    kMinWriter.close();
	}
	
	private Vector encodeFeatureVector(Bug bug) {		
		
		Vector vector = new RandomAccessSparseVector(getCardinality());
		
		FeatureVectorEncoder titleEncoder  =  encoderMap.get(BugIndexer.TITLE_FIELD);
		String title = bug.getTitle() != null ? bug.getTitle() : "";
		titleEncoder.addToVector(title, vector);
		
		FeatureVectorEncoder descriptionEncoder =  encoderMap.get(BugIndexer.DESCRIPTION_FIELD);
		String description = bug.getDescription() != null ? bug.getDescription() : "";
		descriptionEncoder.addToVector(description, 2.0, vector);
		
		FeatureVectorEncoder componentEncoder  =  encoderMap.get(BugIndexer.COMPONENT_FIELD);
		String component = bug.getComponent() != null? bug.getComponent() : "";
		componentEncoder.addToVector(component, vector);
		
		FeatureVectorEncoder priorityEncoder  =  encoderMap.get(BugIndexer.PRIORITY_FIELD);
		String priority =  bug.getPriority() != null? bug.getPriority() : "";
		priorityEncoder.addToVector(priority, vector);
		
		FeatureVectorEncoder typeEncoder  =  encoderMap.get(BugIndexer.TYPE_FIELD);
		String type = bug.getTypeBug() != null ? bug.getTypeBug() : "";
		typeEncoder.addToVector(type, vector);
		
		return vector;
	}

}
