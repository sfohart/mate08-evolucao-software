package br.ufba.dcc.disciplinas.mate08.mahout.vectorizer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.lucene.Driver;

import br.ufba.dcc.disciplinas.mate08.lucene.indexer.BugIndexer;
import br.ufba.dcc.disciplinas.mate08.qualifier.ConfigurationValue;


/**
 * @see http://www.philippeadjiman.com/blog/2010/12/30/how-to-easily-build-and-observe-tf-idf-weight-vectors-with-lucene-and-mahout/
 * 
 * @author leandro.ferreira
 *
 */
@Dependent
public class BugVectorizer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300519990750179620L;
	
	@Inject
	@ConfigurationValue
	private String indexDir;
	
	@Inject
	@ConfigurationValue
	private String mahoutDir;
	
	@Inject
	@ConfigurationValue
	private String dictionaryPath;
	
	private File dictionaryFile;
	
	@Inject
	@ConfigurationValue
	private String vectorPath;
	
	@Inject
	@ConfigurationValue
	private Long maxDocs;
	
	@Inject
	@ConfigurationValue
	private Double  maxPercentErrorDocs;
	
	@Inject
	@ConfigurationValue
	private Double norm; 
	
	private File vectorFile;
	
	private Driver luceneDriver;
	
	
	@PostConstruct
	public void init() throws Exception {
		String indexFullPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath(indexDir);
		
		String mahoutFullPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath(mahoutDir);
				
		dictionaryFile = new File(mahoutFullPath, dictionaryPath);
		vectorFile = new File(mahoutFullPath, vectorPath);
		
		luceneDriver = new Driver();
		
		luceneDriver.setLuceneDir(indexFullPath);
		luceneDriver.setDictOut(dictionaryFile.getPath());
		luceneDriver.setOutFile(vectorFile.getPath());
		luceneDriver.setIdField(BugIndexer.BUG_ID_FIELD);
		luceneDriver.setField(BugIndexer.DESCRIPTION_FIELD);
		luceneDriver.setMaxDocs(maxDocs);
		luceneDriver.setNorm(norm);
		
		luceneDriver.setMaxPercentErrorDocs(maxPercentErrorDocs);		
		
	}
	
	public void vectorizeBugIndex() throws IOException {
		luceneDriver.dumpVectors();
		//vectorizeBugTest();
	}
	
	
	protected void vectorizeBugTest() throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		
		Path path = new Path(vectorFile.getPath());
		 
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
		LongWritable key = new LongWritable();
		VectorWritable value = new VectorWritable();
		
		HashMap<Integer, String> dictionaryMap = new HashMap<Integer, String>();
		
		while (reader.next(key, value)) {
			NamedVector namedVector = (NamedVector)value.get();
			RandomAccessSparseVector vect = (RandomAccessSparseVector)namedVector.getDelegate();
		 
			dictionaryMap.put(Integer.parseInt(key.toString()), value.toString());
			
			for( Element  e : vect ){
				System.out.println("Token: " + dictionaryMap.get(e.index()) + ", TF-IDF weight: " + e.get()) ;
			}
		}
		reader.close();
	}

}
