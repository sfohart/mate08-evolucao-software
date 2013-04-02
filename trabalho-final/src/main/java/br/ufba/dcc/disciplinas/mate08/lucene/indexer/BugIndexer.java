package br.ufba.dcc.disciplinas.mate08.lucene.indexer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import mining.challenge.android.bugreport.model.Bug;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
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
	@BugQualifier
	private BugRepository bugRepository;

	private IndexWriter indexWriter;

	@PostConstruct
	private void init() throws IOException {
		String path = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath(indexDir);

		Directory dir = FSDirectory.open(new File(path));

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
				Version.LUCENE_42, analyzer);
		
		indexWriter = new IndexWriter(dir, indexWriterConfig);
		
	}

	public int index() throws Exception {		
		List<Bug> bugList = bugRepository.findAll();
		
		if (bugList != null && ! bugList.isEmpty()) {
			for (Bug bug : bugList) {
				indexBug(bug);
			}
		}
	
		return indexWriter.numDocs();
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
		
		Field titleField = new TextField("title", bug.getTitle(), Field.Store.YES);
		Field descriptionField = new TextField("description", bug.getTitle(), Field.Store.YES);
		
		document.add(titleField);
		document.add(descriptionField);
		
		return document;
	}

	public void close() throws IOException {
		indexWriter.close();
	}

	
	

}
