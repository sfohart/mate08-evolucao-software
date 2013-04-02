package br.ufba.dcc.disciplinas.mate08.producer;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import br.ufba.dcc.disciplinas.mate08.qualifier.ConfigurationValue;
import br.ufba.dcc.disciplinas.mate08.resolver.PropertyResolver;

/**
 *  
 * Produz campos anotados com {@link ConfigurationValue}. Respons�vel por dar suporte a convers�o entre
 * tipos (principalmente de String para qualquer outro tipo requerido pelo usu�rio)
 * 
 * Esses produtores n�o deve estar interessado em que os campos s�o lidos. � o {@link PropertyResolver}
 * quem � respons�vel pela carga da configura��o
 *  
 * @author leandro.ferreira
 *
 */
public class ConfigurationValueProducer {
	
	@Inject
	private PropertyResolver propertyResolver;
	
	/**
	 * M�todo produtor princial. Tenta encontrar um valor de propriedade utilizando as seguintes chaves:
	 * 
	 * <ol>
	 * 		<li>Propriedade <code>key</code> da anota��o {@link ConfigurationValue} (se n�o encontrou 
	 * 		chave alguma, mas o campo est� definido como obrigat�rio, retorna null</li>
	 * 
	 * 		<li>Nomes de campos de classe totalmente qualificados, por exemplo, 
	 * 		<code>eu.awaketech.MyBean.myField</code>. Se o valor for nulo, tente a �ltima cartada</li>
	 * 
	 * 		<li>Nome do campo de classe, por exemplo, <code>myField</code> do exemplo acima. Se o valor
	 * 		for nulo, n�o h� o que se fazer, retorne null.</li>
	 * </ol>
	 * 
	 * @param ip
	 * @return
	 */
	@Produces
    @ConfigurationValue
    public String getStringConfigValue(InjectionPoint ip) {

        String fqn = ip.getMember().getDeclaringClass().getName() + "." + ip.getMember().getName();

        // Trying with explicit key defined on the field
        String key = ip.getAnnotated().getAnnotation(ConfigurationValue.class).value();
        boolean isKeyDefined = !key.trim().isEmpty();

        boolean valueRequired = ip.getAnnotated().getAnnotation(ConfigurationValue.class).required();

        if (isKeyDefined) {
            return propertyResolver.getValue(key);
        }

        // Falling back to fully-qualified field name resolving.
        key = fqn;
        String value = propertyResolver.getValue(fqn);

        // No luck... so perhaps just the field name?
        if (value == null) {
            key = ip.getMember().getName();
            value = propertyResolver.getValue(key);
        }

        // No can do - no value found but you've said it's required.
        if (value == null && valueRequired) {
            throw new IllegalStateException("Nenhum valor definido para o campo: " + fqn + " mas o campo foi marcado como obrigat�rio.");
        }

        return value;
    }
	
	@Produces
    @ConfigurationValue
    public Double getDoubleConfigValue(InjectionPoint ip) {
        String value = getStringConfigValue(ip);

        return (value != null) ? Double.valueOf(value) : null;
    }
	
	@Produces
    @ConfigurationValue
    public Integer getIntegerConfigValue(InjectionPoint ip) {
        String value = getStringConfigValue(ip);

        return (value != null) ? Integer.valueOf(value) : null;
    }

}
