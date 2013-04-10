package br.ufba.dcc.disciplinas.mate08.lucene.indexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import mining.challenge.android.bugreport.model.Bug;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.qualifier.ConfigurationValue;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;

@Dependent
public class BugIndexer implements Serializable {
	
	public static final String OWNER_FIELD = "owner";

	public static final String REPORTED_BY_FIELD = "reportedBy";

	public static final String PRIORITY_FIELD = "priority";

	public static final String TYPE_FIELD = "type";

	public static final String STATUS_FIELD = "status";

	public static final String COMPONENT_FIELD = "component";

	public static final String BUG_ID_FIELD = "bugId";

	public static final String DESCRIPTION_FIELD = "description";

	public static final String TITLE_FIELD = "title";

	/**
	 * 
	 */
	private static final long serialVersionUID = -7807047534286650931L;

	@Inject
	private Logger logger;

	@Inject
	@ConfigurationValue
	private String indexDir;
	
	@Inject
	@ConfigurationValue
	private String stopWordsListPath;
	
	@Inject
	@BugQualifier
	private BugRepository bugRepository;

	private IndexWriter indexWriter;

	@PostConstruct
	private void init() throws IOException, URISyntaxException {
		if (indexWriter == null) {
			String path = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath(indexDir);
			
			
			URL stopWordFilePath = getClass().getClassLoader().getResource(stopWordsListPath);
			
			FileReader stopWordsReader = new FileReader(new File(stopWordFilePath.toURI()));
	
			Directory dir = FSDirectory.open(new File(path));
	
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36, stopWordsReader);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
					Version.LUCENE_36, analyzer);
			
			indexWriter = new IndexWriter(dir, indexWriterConfig);
		}
		
	}

	public int index() throws Exception {		
		List<Bug> bugList = bugRepository.findAll();
		Integer numDocs  = 0;
		
		try {
		
			if (bugList != null && ! bugList.isEmpty()) {
				for (Bug bug : bugList) {
					indexBug(bug);
				}
			}
			
			numDocs = indexWriter.numDocs();
		} finally {
			indexWriter.close();
		}
		
		return numDocs;
	}

	/**
	 * @param bug
	 * @throws Exception
	 * @throws IOException
	 */
	private void indexBug(Bug bug) throws Exception, IOException {
		logger.info("Indexando bug #" + bug.getId() + " - " + bug.getTitle());
		Document document = getDocument(bug);
		indexWriter.addDocument(document);
	}
	
	private Document getDocument(Bug bug) {
		Document document = new Document();
		
		String ownerEmail = bug.getOwner() != null ? bug.getOwner().getEmail() : "";
		String reportedByEmail = bug.getReportedBy() != null ? bug.getReportedBy().getEmail() : ""; 
		
		Field bugIdField = new Field(BUG_ID_FIELD, bug.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
		Field titleField = new Field(TITLE_FIELD, bug.getTitle(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field descriptionField = new Field(DESCRIPTION_FIELD, bug.getDescription(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field componentField = new Field(COMPONENT_FIELD, bug.getComponent(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field statusField = new Field(STATUS_FIELD, bug.getStatus(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field typeField = new Field(TYPE_FIELD, bug.getTypeBug(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field priorityField = new Field(PRIORITY_FIELD, bug.getPriority(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		
		Field ownerField = new Field(OWNER_FIELD, ownerEmail, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field reportedByField = new Field(REPORTED_BY_FIELD, reportedByEmail, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
		
		/*
		 * Para o Lucene 4.2
		Field titleField = new TextField(TITLE_FIELD, bug.getTitle(), Field.Store.YES);
		Field descriptionField = new TextField(DESCRIPTION_FIELD, bug.getTitle(), Field.Store.YES);
		Field bugIdField = new LongField(BUG_ID_FIELD, bug.getId(), Field.Store.YES);
		*/
		
		document.add(bugIdField);
		document.add(titleField);
		document.add(descriptionField);
		document.add(componentField);
		document.add(statusField);
		document.add(typeField);
		document.add(priorityField);
		document.add(ownerField);
		document.add(reportedByField);
		
		return document;
	}

	

}
