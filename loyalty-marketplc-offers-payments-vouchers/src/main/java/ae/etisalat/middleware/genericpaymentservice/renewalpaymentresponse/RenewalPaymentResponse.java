
package ae.etisalat.middleware.genericpaymentservice.renewalpaymentresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.ackmessage.AckMessage;


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
 *         &lt;element name="ResponseData" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentResponse.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/AckMessage.xsd}AckMessage"/&gt;
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
    "responseData",
    "ackMessage"
})
@XmlRootElement(name = "RenewalPaymentResponse")
public class RenewalPaymentResponse {

    @XmlElement(name = "ResponseData")
    protected RenewalPaymentResponse.ResponseData responseData;
    @XmlElement(name = "AckMessage", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/AckMessage.xsd", required = true)
    protected AckMessage ackMessage;

    /**
     * Gets the value of the responseData property.
     * 
     * @return
     *     possible object is
     *     {@link RenewalPaymentResponse.ResponseData }
     *     
     */
    public RenewalPaymentResponse.ResponseData getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link RenewalPaymentResponse.ResponseData }
     *     
     */
    public void setResponseData(RenewalPaymentResponse.ResponseData value) {
        this.responseData = value;
    }

    /**
     * Gets the value of the ackMessage property.
     * 
     * @return
     *     possible object is
     *     {@link AckMessage }
     *     
     */
    public AckMessage getAckMessage() {
        return ackMessage;
    }

    /**
     * Sets the value of the ackMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link AckMessage }
     *     
     */
    public void setAckMessage(AckMessage value) {
        this.ackMessage = value;
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
     *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentResponse.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "transactionID",
        "additionalInfo"
    })
    public static class ResponseData {

        @XmlElement(name = "TransactionID")
        protected String transactionID;
        @XmlElement(name = "AdditionalInfo")
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

    }

}
