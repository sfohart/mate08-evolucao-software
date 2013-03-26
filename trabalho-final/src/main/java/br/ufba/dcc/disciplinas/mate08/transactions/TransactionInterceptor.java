package br.ufba.dcc.disciplinas.mate08.transactions;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


@Transactional
@Interceptor
public class TransactionInterceptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 175350563624660404L;
	
	@Inject
	private EntityManager entityManager;
	
	@AroundInvoke
	public Object manageTransaction(InvocationContext context) throws Exception { 
		EntityTransaction transaction = entityManager.getTransaction();
		try {  
            if (!transaction.isActive()) {  
                transaction.begin();  
            }  
  
            return context.proceed();  
  
        } catch (Exception e) {  
            System.out.println("Chamando transação no método:" + e);  
            if (transaction != null) {  
                transaction.rollback();  
            }  
  
            throw e;  
  
        } finally {  
            if (transaction != null && transaction.isActive()) {  
                transaction.commit();  
            }  
        } 
		
	}

}
