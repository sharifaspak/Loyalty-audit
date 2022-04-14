
package com.responsetek.rt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EnqueueInvitesResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "enqueueInvitesResult"
})
@XmlRootElement(name = "EnqueueInvitesResponse")
public class EnqueueInvitesResponse {

    @XmlElement(name = "EnqueueInvitesResult")
    protected String enqueueInvitesResult;

    /**
     * Gets the value of the enqueueInvitesResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnqueueInvitesResult() {
        return enqueueInvitesResult;
    }

    /**
     * Sets the value of the enqueueInvitesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnqueueInvitesResult(String value) {
        this.enqueueInvitesResult = value;
    }

}
