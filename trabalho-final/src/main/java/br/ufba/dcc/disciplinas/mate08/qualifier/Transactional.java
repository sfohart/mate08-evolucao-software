package br.ufba.dcc.disciplinas.mate08.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@Target({ElementType.METHOD, ElementType.TYPE})
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

}
