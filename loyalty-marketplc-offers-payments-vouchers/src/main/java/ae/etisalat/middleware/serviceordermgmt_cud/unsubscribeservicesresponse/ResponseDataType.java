
package ae.etisalat.middleware.serviceordermgmt_cud.unsubscribeservicesresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo;


/**
 * <p>Java class for ResponseDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Details" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="BillingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PurchaseID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PurchaseIsDone" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                   &lt;element name="RecurrentPurchaseID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="UnlimitedTicketUsed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                   &lt;element name="TicketConsumed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "ResponseDataType", propOrder = {
    "transactionID",
    "details",
    "additionalInfo"
})
public class ResponseDataType {

    @XmlElement(name = "TransactionID")
    protected String transactionID;
    @XmlElement(name = "Details")
    protected List<ResponseDataType.Details> details;
    @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
    protected List<AdditionalInfo> additionalInfo;

    /**
     * Gets the value of the transactionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Sets the value of the transactionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionID(String value) {
        this.transactionID = value;
    }

    /**
     * Gets the value of the details property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the details property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResponseDataType.Details }
     * 
     * 
     */
    public List<ResponseDataType.Details> getDetails() {
        if (details == null) {
            details = new ArrayList<ResponseDataType.Details>();
        }
        return this.details;
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
     *         &lt;element name="BillingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PurchaseID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PurchaseIsDone" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *         &lt;element name="RecurrentPurchaseID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="UnlimitedTicketUsed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *         &lt;element name="TicketConsumed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
        "billingID",
        "purchaseID",
        "purchaseIsDone",
        "recurrentPurchaseID",
        "statusCode",
        "unlimitedTicketUsed",
        "ticketConsumed"
    })
    public static class Details {

        @XmlElement(name = "BillingID")
        protected String billingID;
        @XmlElement(name = "PurchaseID")
        protected String purchaseID;
        @XmlElement(name = "PurchaseIsDone")
        protected Boolean purchaseIsDone;
        @XmlElement(name = "RecurrentPurchaseID")
        protected String recurrentPurchaseID;
        @XmlElement(name = "StatusCode")
        protected String statusCode;
        @XmlElement(name = "UnlimitedTicketUsed")
        protected Boolean unlimitedTicketUsed;
        @XmlElement(name = "TicketConsumed")
        protected Boolean ticketConsumed;

        /**
         * Gets the value of the billingID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBillingID() {
            return billingID;
        }

        /**
         * Sets the value of the billingID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBillingID(String value) {
            this.billingID = value;
        }

        /**
         * Gets the value of the purchaseID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPurchaseID() {
            return purchaseID;
        }

        /**
         * Sets the value of the purchaseID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPurchaseID(String value) {
            this.purchaseID = value;
        }

        /**
         * Gets the value of the purchaseIsDone property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isPurchaseIsDone() {
            return purchaseIsDone;
        }

        /**
         * Sets the value of the purchaseIsDone property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setPurchaseIsDone(Boolean value) {
            this.purchaseIsDone = value;
        }

        /**
         * Gets the value of the recurrentPurchaseID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRecurrentPurchaseID() {
            return recurrentPurchaseID;
        }

        /**
         * Sets the value of the recurrentPurchaseID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRecurrentPurchaseID(String value) {
            this.recurrentPurchaseID = value;
        }

        /**
         * Gets the value of the statusCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatusCode() {
            return statusCode;
        }

        /**
         * Sets the value of the statusCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatusCode(String value) {
            this.statusCode = value;
        }

        /**
         * Gets the value of the unlimitedTicketUsed property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isUnlimitedTicketUsed() {
            return unlimitedTicketUsed;
        }

        /**
         * Sets the value of the unlimitedTicketUsed property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setUnlimitedTicketUsed(Boolean value) {
            this.unlimitedTicketUsed = value;
        }

        /**
         * Gets the value of the ticketConsumed property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isTicketConsumed() {
            return ticketConsumed;
        }

        /**
         * Sets the value of the ticketConsumed property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setTicketConsumed(Boolean value) {
            this.ticketConsumed = value;
        }

    }

}
