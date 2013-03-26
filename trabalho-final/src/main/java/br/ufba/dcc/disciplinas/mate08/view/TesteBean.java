package br.ufba.dcc.disciplinas.mate08.view;

import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import mining.challenge.android.bugreport.model.AndroidBugs;


@Named
@ViewScoped
public class TesteBean {

	@Inject
	private LazyBugDataModel lazyDataModel;
	
	@Inject
	private EntityManager entityManager;
	
	
	@PostConstruct
	public void init() {
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("android_platform_bugs.xml");
			JAXBContext context = JAXBContext.newInstance("mining.challenge.android.bugreport.model");
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AndroidBugs> root = unmarshaller.unmarshal(new StreamSource(is), AndroidBugs.class);
			
			AndroidBugs androidBugs = root.getValue();
			
			entityManager.persist(androidBugs);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return;
	}
	
	
	public LazyBugDataModel getLazyDataModel() {
		return lazyDataModel;
	}
	
}
