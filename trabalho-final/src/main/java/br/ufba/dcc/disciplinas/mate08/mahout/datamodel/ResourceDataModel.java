package br.ufba.dcc.disciplinas.mate08.mahout.datamodel;

import java.util.Collection;

import javax.annotation.PostConstruct;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import br.ufba.dcc.disciplinas.mate08.qualifier.ConfigurationValue;

/**
 * 
 * @see http://www.searchworkings.org/blog/-/blogs/23944
 * 
 * @author leandro.ferreira
 *
 */
public class ResourceDataModel implements DataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5035912759201720174L;
	
	@Inject
	@ConfigurationValue("hibernate.connection.username")
	private String databaseUsername;

	
	@Inject
	@ConfigurationValue("hibernate.connection.password")
	private String databasePassword;
	
	@Inject
	@ConfigurationValue("hibernate.connection.url")
	private String databaseUrl;
	
	@Inject
	@ConfigurationValue("hibernate.connection.driver_class")
	private String databaseDriverClassName;

	private PostgreSQLBooleanPrefJDBCDataModel delegate;
	
	private BasicDataSource dataSource;
	
	@PostConstruct
	public void init() {
		dataSource = new BasicDataSource();
		dataSource.setPassword(databasePassword);
		dataSource.setUsername(databaseUsername);
		dataSource.setUrl(databaseUrl);
		dataSource.setDriverClassName(databaseDriverClassName);
		
		delegate = new PostgreSQLBooleanPrefJDBCDataModel(
				dataSource, 
				"preferences", 
				"developer_id", 
				"bug_id", 
				"opened_date");
	}

	public void setPreference(long userID, long itemID, float value)
			throws TasteException {
		delegate.setPreference(userID, itemID, value);
	}

	public boolean hasPreferenceValues() {
		return delegate.hasPreferenceValues();
	}

	public float getMaxPreference() {
		return delegate.getMaxPreference();
	}

	public float getMinPreference() {
		return delegate.getMinPreference();
	}

	public DataSource getDataSource() {
		return delegate.getDataSource();
	}

	public String getPreferenceTable() {
		return delegate.getPreferenceTable();
	}

	public String getUserIDColumn() {
		return delegate.getUserIDColumn();
	}

	public String getItemIDColumn() {
		return delegate.getItemIDColumn();
	}

	public String getPreferenceColumn() {
		return delegate.getPreferenceColumn();
	}

	public LongPrimitiveIterator getUserIDs() throws TasteException {
		return delegate.getUserIDs();
	}

	public PreferenceArray getPreferencesFromUser(long userID)
			throws TasteException {
		return delegate.getPreferencesFromUser(userID);
	}

	public FastByIDMap<PreferenceArray> exportWithPrefs() throws TasteException {
		return delegate.exportWithPrefs();
	}

	public FastByIDMap<FastIDSet> exportWithIDsOnly() throws TasteException {
		return delegate.exportWithIDsOnly();
	}

	public FastIDSet getItemIDsFromUser(long userID) throws TasteException {
		return delegate.getItemIDsFromUser(userID);
	}

	public Float getPreferenceValue(long userID, long itemID)
			throws TasteException {
		return delegate.getPreferenceValue(userID, itemID);
	}

	public Long getPreferenceTime(long userID, long itemID)
			throws TasteException {
		return delegate.getPreferenceTime(userID, itemID);
	}

	public LongPrimitiveIterator getItemIDs() throws TasteException {
		return delegate.getItemIDs();
	}

	public PreferenceArray getPreferencesForItem(long itemID)
			throws TasteException {
		return delegate.getPreferencesForItem(itemID);
	}

	public int getNumItems() throws TasteException {
		return delegate.getNumItems();
	}

	public int getNumUsers() throws TasteException {
		return delegate.getNumUsers();
	}

	public int getNumUsersWithPreferenceFor(long itemID) throws TasteException {
		return delegate.getNumUsersWithPreferenceFor(itemID);
	}

	public int getNumUsersWithPreferenceFor(long itemID1, long itemID2)
			throws TasteException {
		return delegate.getNumUsersWithPreferenceFor(itemID1, itemID2);
	}

	public void removePreference(long userID, long itemID)
			throws TasteException {
		delegate.removePreference(userID, itemID);
	}

	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		delegate.refresh(alreadyRefreshed);
	}
	
	
	
}
