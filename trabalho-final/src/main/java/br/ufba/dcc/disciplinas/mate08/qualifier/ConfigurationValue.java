package br.ufba.dcc.disciplinas.mate08.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Define método ou variável que deve ser injetada com valor lido de algum recurso arbitrário
 * como por exemplo  um arquivo <code>properties</code>
 * @author leandro.ferreira
 *
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface ConfigurationValue {

	//chave a ser procurada
	@Nonbinding
	String value() default "";
	
	//define se o valor deve ser definido
	boolean required() default true;
	
}
