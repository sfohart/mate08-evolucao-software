//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.6 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: PM.02.27 às 04:03:15 PM GMT-03:00 
//


package mining.challenge.android.bugreport.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}bugid"/>
 *         &lt;element ref="{}title"/>
 *         &lt;element ref="{}status"/>
 *         &lt;element ref="{}owner"/>
 *         &lt;element ref="{}closedOn"/>
 *         &lt;element ref="{}type"/>
 *         &lt;element ref="{}priority"/>
 *         &lt;element ref="{}component"/>
 *         &lt;element ref="{}stars"/>
 *         &lt;element ref="{}reportedBy"/>
 *         &lt;element ref="{}openedDate"/>
 *         &lt;element ref="{}description"/>
 *         &lt;element ref="{}comment" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bugid",
    "title",
    "status",
    "owner",
    "closedOn",
    "type",
    "priority",
    "component",
    "stars",
    "reportedBy",
    "openedDate",
    "description",
    "comment"
})
@XmlRootElement(name = "bug")
@Entity
public class Bug implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1269937626883938309L;
	
	@Id
	@Column(name="id")	
	@XmlElement(required = true)
    protected BigInteger bugid;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String status;
    @XmlElement(required = true)
    protected String owner;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String closedOn;
    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true)
    protected String priority;
    @XmlElement(required = true)
    protected String component;
    @XmlElement(required = true)
    protected BigInteger stars;
    @XmlElement(required = true)
    protected String reportedBy;
    @XmlElement(required = true)
    protected String openedDate;
    @XmlElement(required = true)
    protected String description;
    
    @OneToMany
    protected List<Comment> comment;

    /**
     * Obtém o valor da propriedade bugid.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBugid() {
        return bugid;
    }

    /**
     * Define o valor da propriedade bugid.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBugid(BigInteger value) {
        this.bugid = value;
    }

    /**
     * Obtém o valor da propriedade title.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Define o valor da propriedade title.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Obtém o valor da propriedade status.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o valor da propriedade status.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Obtém o valor da propriedade owner.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Define o valor da propriedade owner.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Obtém o valor da propriedade closedOn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosedOn() {
        return closedOn;
    }

    /**
     * Define o valor da propriedade closedOn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosedOn(String value) {
        this.closedOn = value;
    }

    /**
     * Obtém o valor da propriedade type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Define o valor da propriedade type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtém o valor da propriedade priority.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Define o valor da propriedade priority.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Obtém o valor da propriedade component.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponent() {
        return component;
    }

    /**
     * Define o valor da propriedade component.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponent(String value) {
        this.component = value;
    }

    /**
     * Obtém o valor da propriedade stars.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStars() {
        return stars;
    }

    /**
     * Define o valor da propriedade stars.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStars(BigInteger value) {
        this.stars = value;
    }

    /**
     * Obtém o valor da propriedade reportedBy.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportedBy() {
        return reportedBy;
    }

    /**
     * Define o valor da propriedade reportedBy.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportedBy(String value) {
        this.reportedBy = value;
    }

    /**
     * Obtém o valor da propriedade openedDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenedDate() {
        return openedDate;
    }

    /**
     * Define o valor da propriedade openedDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenedDate(String value) {
        this.openedDate = value;
    }

    /**
     * Obtém o valor da propriedade description.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Define o valor da propriedade description.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Comment }
     * 
     * 
     */
    public List<Comment> getComment() {
        if (comment == null) {
            comment = new ArrayList<Comment>();
        }
        return this.comment;
    }

}
