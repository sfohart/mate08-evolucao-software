//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.6 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: PM.02.27 às 04:03:15 PM GMT-03:00 
//


package mining.challenge.android.bugreport.model;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mining.challenge.android.bugreport.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OpenedDate_QNAME = new QName("", "openedDate");
    private final static QName _Status_QNAME = new QName("", "status");
    private final static QName _ClosedOn_QNAME = new QName("", "closedOn");
    private final static QName _ReportedBy_QNAME = new QName("", "reportedBy");
    private final static QName _Type_QNAME = new QName("", "type");
    private final static QName _Author_QNAME = new QName("", "author");
    private final static QName _Title_QNAME = new QName("", "title");
    private final static QName _What_QNAME = new QName("", "what");
    private final static QName _Bugid_QNAME = new QName("", "bugid");
    private final static QName _Component_QNAME = new QName("", "component");
    private final static QName _Stars_QNAME = new QName("", "stars");
    private final static QName _Priority_QNAME = new QName("", "priority");
    private final static QName _Description_QNAME = new QName("", "description");
    private final static QName _When_QNAME = new QName("", "when");
    private final static QName _Owner_QNAME = new QName("", "owner");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mining.challenge.android.bugreport.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AndroidBugs }
     * 
     */
    public AndroidBugs createAndroidBugs() {
        return new AndroidBugs();
    }

    /**
     * Create an instance of {@link Bug }
     * 
     */
    public Bug createBug() {
        return new Bug();
    }

    /**
     * Create an instance of {@link Comment }
     * 
     */
    public Comment createComment() {
        return new Comment();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "openedDate")
    public JAXBElement<String> createOpenedDate(String value) {
        return new JAXBElement<String>(_OpenedDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "status")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createStatus(String value) {
        return new JAXBElement<String>(_Status_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "closedOn")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createClosedOn(String value) {
        return new JAXBElement<String>(_ClosedOn_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reportedBy")
    public JAXBElement<String> createReportedBy(String value) {
        return new JAXBElement<String>(_ReportedBy_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "type")
    public JAXBElement<String> createType(String value) {
        return new JAXBElement<String>(_Type_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "author")
    public JAXBElement<String> createAuthor(String value) {
        return new JAXBElement<String>(_Author_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "what")
    public JAXBElement<String> createWhat(String value) {
        return new JAXBElement<String>(_What_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "bugid")
    public JAXBElement<BigInteger> createBugid(BigInteger value) {
        return new JAXBElement<BigInteger>(_Bugid_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "component")
    public JAXBElement<String> createComponent(String value) {
        return new JAXBElement<String>(_Component_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "stars")
    public JAXBElement<BigInteger> createStars(BigInteger value) {
        return new JAXBElement<BigInteger>(_Stars_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "priority")
    public JAXBElement<String> createPriority(String value) {
        return new JAXBElement<String>(_Priority_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "when")
    public JAXBElement<String> createWhen(String value) {
        return new JAXBElement<String>(_When_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "owner")
    public JAXBElement<String> createOwner(String value) {
        return new JAXBElement<String>(_Owner_QNAME, String.class, null, value);
    }

}
