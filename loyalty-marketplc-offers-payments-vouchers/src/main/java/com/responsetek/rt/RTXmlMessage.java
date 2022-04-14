
package com.responsetek.rt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RTXmlMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RTXmlMessage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rt-client-id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="rt-config-id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="invitation" type="{http://rt.responsetek.com/}RTInvitation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RTXmlMessage", propOrder = {
    "rtClientId",
    "rtConfigId",
    "username",
    "password",
    "invitation"
})
public class RTXmlMessage {

    @XmlElement(name = "rt-client-id", required = true, nillable = true)
    protected String rtClientId;
    @XmlElement(name = "rt-config-id", required = true, nillable = true)
    protected String rtConfigId;
    @XmlElement(required = true, nillable = true)
    protected String username;
    @XmlElement(required = true, nillable = true)
    protected String password;
    @XmlElement(nillable = true)
    protected List<RTInvitation> invitation;

    /**
     * Gets the value of the rtClientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRtClientId() {
        return rtClientId;
    }

    /**
     * Sets the value of the rtClientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRtClientId(String value) {
        this.rtClientId = value;
    }

    /**
     * Gets the value of the rtConfigId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRtConfigId() {
        return rtConfigId;
    }

    /**
     * Sets the value of the rtConfigId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRtConfigId(String value) {
        this.rtConfigId = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the invitation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invitation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvitation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RTInvitation }
     * 
     * 
     */
    public List<RTInvitation> getInvitation() {
        if (invitation == null) {
            invitation = new ArrayList<RTInvitation>();
        }
        return this.invitation;
    }

}
