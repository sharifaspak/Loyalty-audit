
package ae.etisalat.middleware.serviceordermgmt_cud.unsubscribeservicesrequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo;


/**
 * <p>Java class for DataHeaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataHeaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PackageID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TargetSystem" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeactivateAll" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RetainIFR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CRBTUnsubscriptionDetails" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="APartyMSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ToneId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PackName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="LanguageID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataHeaderType", propOrder = {
    "msisdn",
    "packageID",
    "targetSystem",
    "serviceID",
    "deactivateAll",
    "retainIFR",
    "crbtUnsubscriptionDetails",
    "additionalInfo"
})
public class DataHeaderType {

    @XmlElement(name = "MSISDN", required = true)
    protected String msisdn;
    @XmlElement(name = "PackageID")
    protected String packageID;
    @XmlElement(name = "TargetSystem", required = true)
    protected String targetSystem;
    @XmlElement(name = "ServiceID")
    protected String serviceID;
    @XmlElement(name = "DeactivateAll")
    protected String deactivateAll;
    @XmlElement(name = "RetainIFR")
    protected String retainIFR;
    @XmlElement(name = "CRBTUnsubscriptionDetails")
    protected List<DataHeaderType.CRBTUnsubscriptionDetails> crbtUnsubscriptionDetails;
    @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
    protected List<AdditionalInfo> additionalInfo;

    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSISDN() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSISDN(String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the packageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageID() {
        return packageID;
    }

    /**
     * Sets the value of the packageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageID(String value) {
        this.packageID = value;
    }

    /**
     * Gets the value of the targetSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetSystem() {
        return targetSystem;
    }

    /**
     * Sets the value of the targetSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetSystem(String value) {
        this.targetSystem = value;
    }

    /**
     * Gets the value of the serviceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceID() {
        return serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceID(String value) {
        this.serviceID = value;
    }

    /**
     * Gets the value of the deactivateAll property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeactivateAll() {
        return deactivateAll;
    }

    /**
     * Sets the value of the deactivateAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeactivateAll(String value) {
        this.deactivateAll = value;
    }

    /**
     * Gets the value of the retainIFR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetainIFR() {
        return retainIFR;
    }

    /**
     * Sets the value of the retainIFR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetainIFR(String value) {
        this.retainIFR = value;
    }

    /**
     * Gets the value of the crbtUnsubscriptionDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the crbtUnsubscriptionDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCRBTUnsubscriptionDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataHeaderType.CRBTUnsubscriptionDetails }
     * 
     * 
     */
    public List<DataHeaderType.CRBTUnsubscriptionDetails> getCRBTUnsubscriptionDetails() {
        if (crbtUnsubscriptionDetails == null) {
            crbtUnsubscriptionDetails = new ArrayList<DataHeaderType.CRBTUnsubscriptionDetails>();
        }
        return this.crbtUnsubscriptionDetails;
    }

    /**
     * Gets the value of the additionalInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalInfo }
     * 
     * 
     */
    public List<AdditionalInfo> getAdditionalInfo() {
        if (additionalInfo == null) {
            additionalInfo = new ArrayList<AdditionalInfo>();
        }
        return this.additionalInfo;
    }


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
     *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="APartyMSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ToneId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PackName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="LanguageID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
        "serviceID",
        "aPartyMSISDN",
        "toneId",
        "packName",
        "priority",
        "languageID"
    })
    public static class CRBTUnsubscriptionDetails {

        @XmlElement(name = "ServiceID")
        protected String serviceID;
        @XmlElement(name = "APartyMSISDN", required = true)
        protected String aPartyMSISDN;
        @XmlElement(name = "ToneId")
        protected String toneId;
        @XmlElement(name = "PackName", required = true)
        protected String packName;
        @XmlElement(name = "Priority", required = true)
        protected String priority;
        @XmlElement(name = "LanguageID", required = true)
        protected String languageID;

        /**
         * Gets the value of the serviceID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getServiceID() {
            return serviceID;
        }

        /**
         * Sets the value of the serviceID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setServiceID(String value) {
            this.serviceID = value;
        }

        /**
         * Gets the value of the aPartyMSISDN property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAPartyMSISDN() {
            return aPartyMSISDN;
        }

        /**
         * Sets the value of the aPartyMSISDN property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAPartyMSISDN(String value) {
            this.aPartyMSISDN = value;
        }

        /**
         * Gets the value of the toneId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getToneId() {
            return toneId;
        }

        /**
         * Sets the value of the toneId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setToneId(String value) {
            this.toneId = value;
        }

        /**
         * Gets the value of the packName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPackName() {
            return packName;
        }

        /**
         * Sets the value of the packName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPackName(String value) {
            this.packName = value;
        }

        /**
         * Gets the value of the priority property.
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
         * Sets the value of the priority property.
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
         * Gets the value of the languageID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLanguageID() {
            return languageID;
        }

        /**
         * Sets the value of the languageID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLanguageID(String value) {
            this.languageID = value;
        }

    }

}
