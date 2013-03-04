package br.ufba.dcc.disciplinas.mate08.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import mining.challenge.android.bugreport.model.AndroidBugs;
import mining.challenge.android.bugreport.model.Bug;
import br.ufba.dcc.disciplinas.mate08.model.DeveloperEntity;
import br.ufba.dcc.disciplinas.mate08.qualifier.Developer;
import br.ufba.dcc.disciplinas.mate08.repository.DeveloperRepository;


@Named
@ViewScoped
public class TesteBean {

	private List<DeveloperEntity> developerList;
	
	@Inject 
	@Developer
	private DeveloperRepository repository;
	
	public DeveloperRepository getRepository() {
		return repository;
	}
	
	@PostConstruct
	public void init() {
		Map<String, DeveloperEntity> map = new HashMap<>();
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("android_platform_bugs.xml");
			JAXBContext context = JAXBContext.newInstance("mining.challenge.android.bugreport.model");
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<AndroidBugs> root = unmarshaller.unmarshal(new StreamSource(is), AndroidBugs.class);
			
			AndroidBugs androidBugs = root.getValue();
			
			//criando preferências
			
			
			if (androidBugs.getBug() != null && ! androidBugs.getBug().isEmpty()) {
				for (Bug bug : androidBugs.getBug()) {
					if (! "null".equals(bug.getOwner())) {
						DeveloperEntity developer = map.get(bug.getOwner());
						if (developer == null) {
							developer = new DeveloperEntity();
							developer.setEmail(bug.getOwner());
							
							getRepository().save(developer);
							
							map.put(bug.getOwner(), developer);
						}
						
						List<Bug> bugs = developer.getBugs();
						bugs.add(bug);
						
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		this.developerList = new ArrayList<>(map.values());		
	}
	
	public List<DeveloperEntity> getDeveloperList() {
		return developerList;
	}
	
	
	
}
