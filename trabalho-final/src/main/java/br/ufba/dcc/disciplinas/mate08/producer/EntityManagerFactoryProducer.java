package br.ufba.dcc.disciplinas.mate08.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author leandro.ferreira
 *
 *
 * @see https://www.42lines.net/2011/11/21/adding-jpahibernate-into-the-cdi-and-wicket-mix/
 */
public class EntityManagerFactoryProducer {
	
	private static final String PERSISTENCE_UNIT = "mate08";
	
	@Produces
	@ApplicationScoped
	public EntityManagerFactory create() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}
	
	public void destroy(@Disposes EntityManagerFactory factory) {
		factory.close();
	}

}
