package br.ufba.dcc.disciplinas.mate08;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import mining.challenge.android.bugreport.model.AndroidBugs;
import mining.challenge.android.bugreport.model.Bug;

public class Teste {

	/**
	 * @param args
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws JAXBException {
		
		//lendo o xml com os dados
		
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("android_platform_bugs.xml");
		JAXBContext context = JAXBContext.newInstance("mining.challenge.android.bugreport.model");
		
		Unmarshaller unmarshaller = context.createUnmarshaller();
		JAXBElement<AndroidBugs> root = unmarshaller.unmarshal(new StreamSource(is), AndroidBugs.class);
		
		AndroidBugs androidBugs = root.getValue();
		
		//criando preferências
		if (androidBugs.getBug() != null && ! androidBugs.getBug().isEmpty()) {
			for (Bug bug : androidBugs.getBug()) {
				bug.getBugId();
				bug.getOwner();				
				bug.getStars();
			}
		}
		
	}

}
