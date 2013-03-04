package br.ufba.dcc.disciplinas.mate08.listeners;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.ufba.dcc.disciplinas.mate08.persistence.impl.DeveloperRepositoryImpl;

/**
 * Application Lifecycle Listener implementation class LoadBugReportDataListener
 *
 */
public class LoadBugReportDataListener implements ServletContextListener {

	
	
    /**
     * Default constructor. 
     */
    public LoadBugReportDataListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
