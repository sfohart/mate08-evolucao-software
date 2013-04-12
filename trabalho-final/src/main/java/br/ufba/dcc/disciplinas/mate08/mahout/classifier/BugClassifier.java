package br.ufba.dcc.disciplinas.mate08.mahout.classifier;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import mining.challenge.android.bugreport.model.Bug;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.ep.State;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.Vectors;
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
	private Logger logger;
	
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
	
	public void test() throws ParseException {		
		Dictionary ownerGroups = new Dictionary();
		
		//todos os bugs já atribuidos a algum desenvolvedor para usar na fase de treinamento
		List<Bug> bugs = bugRepository.findAllBugsToExperiment();
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
		CrossFoldLearner learner =
				learningAlgorithm.getBest().getPayload().getLearner();
		
		//ResultAnalyzer resultAnalyzer = new ResultAnalyzer(ownerGroups.values(), "unknow");
		int topKCorrectlyClassified = 0;
		int topKIncorrectlyClassified = 0;
		int bestCorrectlyClassified = 0;
		int bestIncorrectlyClassified = 0;
		
		continueTest: for (Bug bug : testList) {
			Vector input = encodeFeatureVector(bug);
			Vector resultScores = learner.classifyFull(input);
			Vector topKScores = Vectors.topKElements(10, resultScores);
			
			/*System.out.println("BugId #" + bug.getId() + "\t- " + bug.getTitle());
			System.out.println("Owner: " + bug.getOwner().getEmail());
			System.out.println("Recommended: ");*/
			
			Iterator<Vector.Element> iterator = topKScores.iterateNonZero();
			
			String correctLabel = bug.getOwner().getEmail();
			String classifiedLabel = ownerGroups.values().get(topKScores.maxValueIndex());
			
			if (correctLabel.equals(classifiedLabel)) {
				bestCorrectlyClassified++;
			} else {
				bestIncorrectlyClassified++;
			}
			
			/*ClassifierResult result = new ClassifierResult(classifiedLabel, topKScores.get(topKScores.maxValueIndex())); 
			resultAnalyzer.addInstance(correctLabel, result);*/
			
			
			while (iterator.hasNext()) {
				Vector.Element element = iterator.next();
				String topKValue = ownerGroups.values().get(element.index());
				
				if (topKValue.equals(correctLabel)) {
					topKCorrectlyClassified++;					
					continue continueTest;
				} 
				//System.out.println("\trank " + element.get() + "\t- " + topKValue);
			}
			
			topKIncorrectlyClassified++;
			
			
		}
		
		StringBuilder returnString = new StringBuilder();
	    
		returnString.append("===================================================================\n");
	    returnString.append("Summary (top 10)\n");
	    returnString.append("-------------------------------------------------------\n");
	    
	    int topKTotalClassified = topKCorrectlyClassified + topKIncorrectlyClassified;
	    double topKPercentageCorrect = (double) 100 * topKCorrectlyClassified / topKTotalClassified;
	    double topKPercentageIncorrect = (double) 100 * topKIncorrectlyClassified / topKTotalClassified;
	    
	    NumberFormat decimalFormatter = new DecimalFormat("0.####");
	    
	    returnString.append(StringUtils.rightPad("Top K Correctly Classified Instances", 40)).append(": ").append(
	    		StringUtils.leftPad(Integer.toString(topKCorrectlyClassified), 10)).append('\t').append(
	    				StringUtils.leftPad(decimalFormatter.format(topKPercentageCorrect), 10)).append("%\n");
	    returnString.append(StringUtils.rightPad("Top K Incorrectly Classified Instances", 40)).append(": ").append(
	    		StringUtils.leftPad(Integer.toString(topKIncorrectlyClassified), 10)).append('\t').append(
	    				StringUtils.leftPad(decimalFormatter.format(topKPercentageIncorrect), 10)).append("%\n");
	    returnString.append(StringUtils.rightPad("Total Classified Instances", 40)).append(": ").append(
	    		StringUtils.leftPad(Integer.toString(topKTotalClassified), 10)).append('\n');
	    returnString.append('\n');

	    System.out.println(returnString.toString());
	    returnString = new StringBuilder();
	    
	    returnString.append("===================================================================\n");
	    returnString.append("Summary (best)\n");
	    returnString.append("-------------------------------------------------------\n");
	    
	    int bestTotalClassified = bestCorrectlyClassified + bestIncorrectlyClassified;
	    double bestPercentageCorrect = (double) 100 * bestCorrectlyClassified / bestTotalClassified;
	    double bestPercentageIncorrect = (double) 100 * bestIncorrectlyClassified / bestTotalClassified;
	    
	    returnString.append(StringUtils.rightPad("Correctly Classified Instances", 40)).append(": ").append(
	    		StringUtils.leftPad(Integer.toString(bestCorrectlyClassified), 10)).append('\t').append(
	    				StringUtils.leftPad(decimalFormatter.format(bestPercentageCorrect), 10)).append("%\n");
	    returnString.append(StringUtils.rightPad("Incorrectly Classified Instances", 40)).append(": ").append(
	    		StringUtils.leftPad(Integer.toString(bestIncorrectlyClassified), 10)).append('\t').append(
	    				StringUtils.leftPad(decimalFormatter.format(bestPercentageIncorrect), 10)).append("%\n");
	    returnString.append(StringUtils.rightPad("Total Classified Instances", 40)).append(": ").append(
	    		StringUtils.leftPad(Integer.toString(bestTotalClassified), 10)).append('\n');
	    
	    System.out.println(returnString.toString());
	    
	    //System.out.println(resultAnalyzer.toString());
		
		
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
