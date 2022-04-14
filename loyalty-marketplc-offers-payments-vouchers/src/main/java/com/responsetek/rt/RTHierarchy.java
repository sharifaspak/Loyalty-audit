
package com.responsetek.rt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RTHierarchy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RTHierarchy"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="level" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="accountable-user-name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="accountable-user-first-name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="accountable-user-last-name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="accountable-user-email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RTHierarchy", propOrder = {
    "name",
    "level",
    "id",
    "accountableUserName",
    "accountableUserFirstName",
    "accountableUserLastName",
    "accountableUserEmail"
})
public class RTHierarchy {

    protected String name;
    @XmlElement(required = true, nillable = true)
    protected String level;
    @XmlElement(required = true, nillable = true)
    protected String id;
    @XmlElement(name = "accountable-user-name")
    protected String accountableUserName;
    @XmlElement(name = "accountable-user-first-name")
    protected String accountableUserFirstName;
    @XmlElement(name = "accountable-user-last-name")
    protected String accountableUserLastName;
    @XmlElement(name = "accountable-user-email")
    protected String accountableUserEmail;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel(String value) {
        this.level = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the accountableUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountableUserName() {
        return accountableUserName;
    }

    /**
     * Sets the value of the accountableUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountableUserName(String value) {
        this.accountableUserName = value;
    }

    /**
     * Gets the value of the accountableUserFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountableUserFirstName() {
        return accountableUserFirstName;
    }

    /**
     * Sets the value of the accountableUserFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountableUserFirstName(String value) {
        this.accountableUserFirstName = value;
    }

    /**
     * Gets the value of the accountableUserLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountableUserLastName() {
        return accountableUserLastName;
    }

    /**
     * Sets the value of the accountableUserLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountableUserLastName(String value) {
        this.accountableUserLastName = value;
    }

    /**
     * Gets the value of the accountableUserEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountableUserEmail() {
        return accountableUserEmail;
    }

    /**
     * Sets the value of the accountableUserEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountableUserEmail(String value) {
        this.accountableUserEmail = value;
    }

}
