package br.ufba.dcc.disciplinas.mate08.producer;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LoggerProducer {
	
	@Produces
	public Logger produceLogger(InjectionPoint injectionPoint) {
		Logger logger = Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getSimpleName());
		
		return logger;
	}
	

}
