package br.ufba.dcc.disciplinas.mate08.repository.impl;

import java.math.BigInteger;

import mining.challenge.android.bugreport.model.Bug;
import br.ufba.dcc.disciplinas.mate08.qualifier.BugQualifier;
import br.ufba.dcc.disciplinas.mate08.repository.BugRepository;

@BugQualifier
public class BugRepositoryImpl extends BaseRepositoryImpl<BigInteger, Bug>
implements BugRepository {


}
