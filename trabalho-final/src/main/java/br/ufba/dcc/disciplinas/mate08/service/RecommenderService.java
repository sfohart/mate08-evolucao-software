package br.ufba.dcc.disciplinas.mate08.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import br.ufba.dcc.disciplinas.mate08.mahout.datamodel.ResourceDataModel;


public class RecommenderService {

	@Inject 
	private ResourceDataModel dataModel;
	
	public void getRecommendations() {
		ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(dataModel); 
		
		ItemBasedRecommender recommender =         
		        new GenericItemBasedRecommender(dataModel, itemSimilarity); 
		
		
	}
	
	
}
