package br.ufba.dcc.disciplinas.mate08.repository.impl;

import mining.challenge.android.bugreport.model.Developer;
import br.ufba.dcc.disciplinas.mate08.qualifier.DeveloperQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.DeveloperRepository;

@DeveloperQualifier
public class DeveloperRepositoryImpl extends BaseRepositoryImpl<Integer,Developer>
implements DeveloperRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4997037257406128764L;

}
