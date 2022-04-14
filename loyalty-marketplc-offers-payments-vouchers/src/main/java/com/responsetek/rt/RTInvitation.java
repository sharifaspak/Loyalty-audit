
package com.responsetek.rt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RTInvitation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RTInvitation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="collection-point" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="invite-type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="invite-tstamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="invite-lang" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="custom-fields" type="{http://rt.responsetek.com/}RTCustom" minOccurs="0"/&gt;
 *         &lt;element name="consumer" type="{http://rt.responsetek.com/}RTConsumer"/&gt;
 *         &lt;element name="primary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="secondary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="tertiary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="quaternary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="quinary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="senary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="septenary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="octonary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="nonary-hierarchy" type="{http://rt.responsetek.com/}RTHierarchyList" minOccurs="0"/&gt;
 *         &lt;element name="extended-custom-fields" type="{http://rt.responsetek.com/}RTDynamicCustom" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RTInvitation", propOrder = {
    "collectionPoint",
    "inviteType",
    "inviteTstamp",
    "inviteLang",
    "customFields",
    "consumer",
    "primaryHierarchy",
    "secondaryHierarchy",
    "tertiaryHierarchy",
    "quaternaryHierarchy",
    "quinaryHierarchy",
    "senaryHierarchy",
    "septenaryHierarchy",
    "octonaryHierarchy",
    "nonaryHierarchy",
    "extendedCustomFields"
})
public class RTInvitation {

    @XmlElement(name = "collection-point", required = true, nillable = true)
    protected String collectionPoint;
    @XmlElement(name = "invite-type", required = true, nillable = true)
    protected String inviteType;
    @XmlElement(name = "invite-tstamp")
    protected String inviteTstamp;
    @XmlElement(name = "invite-lang")
    protected String inviteLang;
    @XmlElement(name = "custom-fields")
    protected RTCustom customFields;
    @XmlElement(required = true, nillable = true)
    protected RTConsumer consumer;
    @XmlElement(name = "primary-hierarchy")
    protected RTHierarchyList primaryHierarchy;
    @XmlElement(name = "secondary-hierarchy")
    protected RTHierarchyList secondaryHierarchy;
    @XmlElement(name = "tertiary-hierarchy")
    protected RTHierarchyList tertiaryHierarchy;
    @XmlElement(name = "quaternary-hierarchy")
    protected RTHierarchyList quaternaryHierarchy;
    @XmlElement(name = "quinary-hierarchy")
    protected RTHierarchyList quinaryHierarchy;
    @XmlElement(name = "senary-hierarchy")
    protected RTHierarchyList senaryHierarchy;
    @XmlElement(name = "septenary-hierarchy")
    protected RTHierarchyList septenaryHierarchy;
    @XmlElement(name = "octonary-hierarchy")
    protected RTHierarchyList octonaryHierarchy;
    @XmlElement(name = "nonary-hierarchy")
    protected RTHierarchyList nonaryHierarchy;
    @XmlElement(name = "extended-custom-fields")
    protected List<RTDynamicCustom> extendedCustomFields;

    /**
     * Gets the value of the collectionPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollectionPoint() {
        return collectionPoint;
    }

    /**
     * Sets the value of the collectionPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionPoint(String value) {
        this.collectionPoint = value;
    }

    /**
     * Gets the value of the inviteType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInviteType() {
        return inviteType;
    }

    /**
     * Sets the value of the inviteType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInviteType(String value) {
        this.inviteType = value;
    }

    /**
     * Gets the value of the inviteTstamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInviteTstamp() {
        return inviteTstamp;
    }

    /**
     * Sets the value of the inviteTstamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInviteTstamp(String value) {
        this.inviteTstamp = value;
    }

    /**
     * Gets the value of the inviteLang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInviteLang() {
        return inviteLang;
    }

    /**
     * Sets the value of the inviteLang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInviteLang(String value) {
        this.inviteLang = value;
    }

    /**
     * Gets the value of the customFields property.
     * 
     * @return
     *     possible object is
     *     {@link RTCustom }
     *     
     */
    public RTCustom getCustomFields() {
        return customFields;
    }

    /**
     * Sets the value of the customFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTCustom }
     *     
     */
    public void setCustomFields(RTCustom value) {
        this.customFields = value;
    }

    /**
     * Gets the value of the consumer property.
     * 
     * @return
     *     possible object is
     *     {@link RTConsumer }
     *     
     */
    public RTConsumer getConsumer() {
        return consumer;
    }

    /**
     * Sets the value of the consumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTConsumer }
     *     
     */
    public void setConsumer(RTConsumer value) {
        this.consumer = value;
    }

    /**
     * Gets the value of the primaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getPrimaryHierarchy() {
        return primaryHierarchy;
    }

    /**
     * Sets the value of the primaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setPrimaryHierarchy(RTHierarchyList value) {
        this.primaryHierarchy = value;
    }

    /**
     * Gets the value of the secondaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getSecondaryHierarchy() {
        return secondaryHierarchy;
    }

    /**
     * Sets the value of the secondaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setSecondaryHierarchy(RTHierarchyList value) {
        this.secondaryHierarchy = value;
    }

    /**
     * Gets the value of the tertiaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getTertiaryHierarchy() {
        return tertiaryHierarchy;
    }

    /**
     * Sets the value of the tertiaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setTertiaryHierarchy(RTHierarchyList value) {
        this.tertiaryHierarchy = value;
    }

    /**
     * Gets the value of the quaternaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getQuaternaryHierarchy() {
        return quaternaryHierarchy;
    }

    /**
     * Sets the value of the quaternaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setQuaternaryHierarchy(RTHierarchyList value) {
        this.quaternaryHierarchy = value;
    }

    /**
     * Gets the value of the quinaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getQuinaryHierarchy() {
        return quinaryHierarchy;
    }

    /**
     * Sets the value of the quinaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setQuinaryHierarchy(RTHierarchyList value) {
        this.quinaryHierarchy = value;
    }

    /**
     * Gets the value of the senaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getSenaryHierarchy() {
        return senaryHierarchy;
    }

    /**
     * Sets the value of the senaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setSenaryHierarchy(RTHierarchyList value) {
        this.senaryHierarchy = value;
    }

    /**
     * Gets the value of the septenaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getSeptenaryHierarchy() {
        return septenaryHierarchy;
    }

    /**
     * Sets the value of the septenaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setSeptenaryHierarchy(RTHierarchyList value) {
        this.septenaryHierarchy = value;
    }

    /**
     * Gets the value of the octonaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getOctonaryHierarchy() {
        return octonaryHierarchy;
    }

    /**
     * Sets the value of the octonaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setOctonaryHierarchy(RTHierarchyList value) {
        this.octonaryHierarchy = value;
    }

    /**
     * Gets the value of the nonaryHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link RTHierarchyList }
     *     
     */
    public RTHierarchyList getNonaryHierarchy() {
        return nonaryHierarchy;
    }

    /**
     * Sets the value of the nonaryHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RTHierarchyList }
     *     
     */
    public void setNonaryHierarchy(RTHierarchyList value) {
        this.nonaryHierarchy = value;
    }

    /**
     * Gets the value of the extendedCustomFields property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extendedCustomFields property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtendedCustomFields().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RTDynamicCustom }
     * 
     * 
     */
    public List<RTDynamicCustom> getExtendedCustomFields() {
        if (extendedCustomFields == null) {
            extendedCustomFields = new ArrayList<RTDynamicCustom>();
        }
        return this.extendedCustomFields;
    }

}
