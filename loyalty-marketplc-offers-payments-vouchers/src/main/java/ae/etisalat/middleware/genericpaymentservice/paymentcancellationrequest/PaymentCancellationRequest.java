
package ae.etisalat.middleware.genericpaymentservice.paymentcancellationrequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.applicationheader.ApplicationHeader;


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
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd}ApplicationHeader"/&gt;
 *         &lt;element name="DataHeader"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="PaymentModeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RequestXML" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FranchiseePayment" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="FranchiseeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CancelDetails" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="LinkExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="LinkCODETransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CancellationDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="CancellationReason" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="CancellationRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CardDetails" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="AuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element name="EPGTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element name="TerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element name="MerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element name="CardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="LoyaltyDetails" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="LoyaltyTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="WalletDetails" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="WalletTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="OtherPaymentTypes" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="OptTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "applicationHeader",
    "dataHeader"
})
@XmlRootElement(name = "PaymentCancellationRequest")
public class PaymentCancellationRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected PaymentCancellationRequest.DataHeader dataHeader;

    /**
     * Gets the value of the applicationHeader property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationHeader }
     *     
     */
    public ApplicationHeader getApplicationHeader() {
        return applicationHeader;
    }

    /**
     * Sets the value of the applicationHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationHeader }
     *     
     */
    public void setApplicationHeader(ApplicationHeader value) {
        this.applicationHeader = value;
    }

    /**
     * Gets the value of the dataHeader property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentCancellationRequest.DataHeader }
     *     
     */
    public PaymentCancellationRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentCancellationRequest.DataHeader }
     *     
     */
    public void setDataHeader(PaymentCancellationRequest.DataHeader value) {
        this.dataHeader = value;
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
     *         &lt;element name="PaymentModeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RequestXML" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FranchiseePayment" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="FranchiseeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CancelDetails" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="LinkExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="LinkCODETransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CancellationDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="CancellationReason" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="CancellationRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CardDetails" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="AuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element name="EPGTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element name="TerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element name="MerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element name="CardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="LoyaltyDetails" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="LoyaltyTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="WalletDetails" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="WalletTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="OtherPaymentTypes" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="OptTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "paymentModeCode",
        "channelTransactionDate",
        "collectionLocationCode",
        "collectionLocationName",
        "collectionRegionCode",
        "channelType",
        "requestXML",
        "franchiseePayment",
        "cancelDetails",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "PaymentModeCode", required = true)
        protected String paymentModeCode;
        @XmlElement(name = "ChannelTransactionDate", required = true)
        protected String channelTransactionDate;
        @XmlElement(name = "CollectionLocationCode")
        protected String collectionLocationCode;
        @XmlElement(name = "CollectionLocationName")
        protected String collectionLocationName;
        @XmlElement(name = "CollectionRegionCode")
        protected String collectionRegionCode;
        @XmlElement(name = "ChannelType")
        protected String channelType;
        @XmlElement(name = "RequestXML")
        protected String requestXML;
        @XmlElement(name = "FranchiseePayment")
        protected PaymentCancellationRequest.DataHeader.FranchiseePayment franchiseePayment;
        @XmlElement(name = "CancelDetails")
        protected PaymentCancellationRequest.DataHeader.CancelDetails cancelDetails;
        @XmlElement(name = "AdditionalInfo")
        protected List<AdditionalInfo> additionalInfo;

        /**
         * Gets the value of the paymentModeCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaymentModeCode() {
            return paymentModeCode;
        }

        /**
         * Sets the value of the paymentModeCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaymentModeCode(String value) {
            this.paymentModeCode = value;
        }

        /**
         * Gets the value of the channelTransactionDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getChannelTransactionDate() {
            return channelTransactionDate;
        }

        /**
         * Sets the value of the channelTransactionDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setChannelTransactionDate(String value) {
            this.channelTransactionDate = value;
        }

        /**
         * Gets the value of the collectionLocationCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCollectionLocationCode() {
            return collectionLocationCode;
        }

        /**
         * Sets the value of the collectionLocationCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCollectionLocationCode(String value) {
            this.collectionLocationCode = value;
        }

        /**
         * Gets the value of the collectionLocationName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCollectionLocationName() {
            return collectionLocationName;
        }

        /**
         * Sets the value of the collectionLocationName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCollectionLocationName(String value) {
            this.collectionLocationName = value;
        }

        /**
         * Gets the value of the collectionRegionCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCollectionRegionCode() {
            return collectionRegionCode;
        }

        /**
         * Sets the value of the collectionRegionCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCollectionRegionCode(String value) {
            this.collectionRegionCode = value;
        }

        /**
         * Gets the value of the channelType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getChannelType() {
            return channelType;
        }

        /**
         * Sets the value of the channelType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setChannelType(String value) {
            this.channelType = value;
        }

        /**
         * Gets the value of the requestXML property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRequestXML() {
            return requestXML;
        }

        /**
         * Sets the value of the requestXML property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRequestXML(String value) {
            this.requestXML = value;
        }

        /**
         * Gets the value of the franchiseePayment property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentCancellationRequest.DataHeader.FranchiseePayment }
         *     
         */
        public PaymentCancellationRequest.DataHeader.FranchiseePayment getFranchiseePayment() {
            return franchiseePayment;
        }

        /**
         * Sets the value of the franchiseePayment property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentCancellationRequest.DataHeader.FranchiseePayment }
         *     
         */
        public void setFranchiseePayment(PaymentCancellationRequest.DataHeader.FranchiseePayment value) {
            this.franchiseePayment = value;
        }

        /**
         * Gets the value of the cancelDetails property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentCancellationRequest.DataHeader.CancelDetails }
         *     
         */
        public PaymentCancellationRequest.DataHeader.CancelDetails getCancelDetails() {
            return cancelDetails;
        }

        /**
         * Sets the value of the cancelDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentCancellationRequest.DataHeader.CancelDetails }
         *     
         */
        public void setCancelDetails(PaymentCancellationRequest.DataHeader.CancelDetails value) {
            this.cancelDetails = value;
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
         *         &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="LinkExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="LinkCODETransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CancellationDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="CancellationReason" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="CancellationRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CardDetails" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="AuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="EPGTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="TerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="MerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="CardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="LoyaltyDetails" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="LoyaltyTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="WalletDetails" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="WalletTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="OtherPaymentTypes" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="OptTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "transactionType",
            "externalTransactionCode",
            "orderNumber",
            "linkExternalTransactionCode",
            "linkCODETransactionCode",
            "cancellationDate",
            "cancellationReason",
            "cancellationRemarks",
            "remarks",
            "cardDetails",
            "loyaltyDetails",
            "walletDetails",
            "otherPaymentTypes",
            "additionalInfo"
        })
        public static class CancelDetails {

            @XmlElement(name = "TransactionType", required = true)
            protected String transactionType;
            @XmlElement(name = "ExternalTransactionCode", required = true)
            protected String externalTransactionCode;
            @XmlElement(name = "OrderNumber")
            protected String orderNumber;
            @XmlElement(name = "LinkExternalTransactionCode")
            protected String linkExternalTransactionCode;
            @XmlElement(name = "LinkCODETransactionCode")
            protected String linkCODETransactionCode;
            @XmlElement(name = "CancellationDate", required = true)
            protected String cancellationDate;
            @XmlElement(name = "CancellationReason", required = true)
            protected String cancellationReason;
            @XmlElement(name = "CancellationRemarks")
            protected String cancellationRemarks;
            @XmlElement(name = "Remarks")
            protected String remarks;
            @XmlElement(name = "CardDetails")
            protected PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails cardDetails;
            @XmlElement(name = "LoyaltyDetails")
            protected PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails loyaltyDetails;
            @XmlElement(name = "WalletDetails")
            protected PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails walletDetails;
            @XmlElement(name = "OtherPaymentTypes")
            protected PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes otherPaymentTypes;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the transactionType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTransactionType() {
                return transactionType;
            }

            /**
             * Sets the value of the transactionType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTransactionType(String value) {
                this.transactionType = value;
            }

            /**
             * Gets the value of the externalTransactionCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getExternalTransactionCode() {
                return externalTransactionCode;
            }

            /**
             * Sets the value of the externalTransactionCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setExternalTransactionCode(String value) {
                this.externalTransactionCode = value;
            }

            /**
             * Gets the value of the orderNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOrderNumber() {
                return orderNumber;
            }

            /**
             * Sets the value of the orderNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOrderNumber(String value) {
                this.orderNumber = value;
            }

            /**
             * Gets the value of the linkExternalTransactionCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLinkExternalTransactionCode() {
                return linkExternalTransactionCode;
            }

            /**
             * Sets the value of the linkExternalTransactionCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLinkExternalTransactionCode(String value) {
                this.linkExternalTransactionCode = value;
            }

            /**
             * Gets the value of the linkCODETransactionCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLinkCODETransactionCode() {
                return linkCODETransactionCode;
            }

            /**
             * Sets the value of the linkCODETransactionCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLinkCODETransactionCode(String value) {
                this.linkCODETransactionCode = value;
            }

            /**
             * Gets the value of the cancellationDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCancellationDate() {
                return cancellationDate;
            }

            /**
             * Sets the value of the cancellationDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCancellationDate(String value) {
                this.cancellationDate = value;
            }

            /**
             * Gets the value of the cancellationReason property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCancellationReason() {
                return cancellationReason;
            }

            /**
             * Sets the value of the cancellationReason property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCancellationReason(String value) {
                this.cancellationReason = value;
            }

            /**
             * Gets the value of the cancellationRemarks property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCancellationRemarks() {
                return cancellationRemarks;
            }

            /**
             * Sets the value of the cancellationRemarks property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCancellationRemarks(String value) {
                this.cancellationRemarks = value;
            }

            /**
             * Gets the value of the remarks property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRemarks() {
                return remarks;
            }

            /**
             * Sets the value of the remarks property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRemarks(String value) {
                this.remarks = value;
            }

            /**
             * Gets the value of the cardDetails property.
             * 
             * @return
             *     possible object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails }
             *     
             */
            public PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails getCardDetails() {
                return cardDetails;
            }

            /**
             * Sets the value of the cardDetails property.
             * 
             * @param value
             *     allowed object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails }
             *     
             */
            public void setCardDetails(PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails value) {
                this.cardDetails = value;
            }

            /**
             * Gets the value of the loyaltyDetails property.
             * 
             * @return
             *     possible object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails }
             *     
             */
            public PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails getLoyaltyDetails() {
                return loyaltyDetails;
            }

            /**
             * Sets the value of the loyaltyDetails property.
             * 
             * @param value
             *     allowed object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails }
             *     
             */
            public void setLoyaltyDetails(PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails value) {
                this.loyaltyDetails = value;
            }

            /**
             * Gets the value of the walletDetails property.
             * 
             * @return
             *     possible object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails }
             *     
             */
            public PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails getWalletDetails() {
                return walletDetails;
            }

            /**
             * Sets the value of the walletDetails property.
             * 
             * @param value
             *     allowed object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails }
             *     
             */
            public void setWalletDetails(PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails value) {
                this.walletDetails = value;
            }

            /**
             * Gets the value of the otherPaymentTypes property.
             * 
             * @return
             *     possible object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes }
             *     
             */
            public PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes getOtherPaymentTypes() {
                return otherPaymentTypes;
            }

            /**
             * Sets the value of the otherPaymentTypes property.
             * 
             * @param value
             *     allowed object is
             *     {@link PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes }
             *     
             */
            public void setOtherPaymentTypes(PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes value) {
                this.otherPaymentTypes = value;
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
             *         &lt;element name="AuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="EPGTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="TerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="MerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="CardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
                "authorizationCode",
                "epgTransactionID",
                "terminalId",
                "merchantId",
                "cardToken",
                "additionalInfo"
            })
            public static class CardDetails {

                @XmlElement(name = "AuthorizationCode")
                protected String authorizationCode;
                @XmlElement(name = "EPGTransactionID")
                protected String epgTransactionID;
                @XmlElement(name = "TerminalId")
                protected String terminalId;
                @XmlElement(name = "MerchantId")
                protected String merchantId;
                @XmlElement(name = "CardToken")
                protected String cardToken;
                @XmlElement(name = "AdditionalInfo")
                protected List<AdditionalInfo> additionalInfo;

                /**
                 * Gets the value of the authorizationCode property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAuthorizationCode() {
                    return authorizationCode;
                }

                /**
                 * Sets the value of the authorizationCode property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAuthorizationCode(String value) {
                    this.authorizationCode = value;
                }

                /**
                 * Gets the value of the epgTransactionID property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getEPGTransactionID() {
                    return epgTransactionID;
                }

                /**
                 * Sets the value of the epgTransactionID property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setEPGTransactionID(String value) {
                    this.epgTransactionID = value;
                }

                /**
                 * Gets the value of the terminalId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getTerminalId() {
                    return terminalId;
                }

                /**
                 * Sets the value of the terminalId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setTerminalId(String value) {
                    this.terminalId = value;
                }

                /**
                 * Gets the value of the merchantId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getMerchantId() {
                    return merchantId;
                }

                /**
                 * Sets the value of the merchantId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setMerchantId(String value) {
                    this.merchantId = value;
                }

                /**
                 * Gets the value of the cardToken property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCardToken() {
                    return cardToken;
                }

                /**
                 * Sets the value of the cardToken property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCardToken(String value) {
                    this.cardToken = value;
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
             *         &lt;element name="LoyaltyTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
                "loyaltyTransactionID",
                "additionalInfo"
            })
            public static class LoyaltyDetails {

                @XmlElement(name = "LoyaltyTransactionID")
                protected String loyaltyTransactionID;
                @XmlElement(name = "AdditionalInfo")
                protected List<AdditionalInfo> additionalInfo;

                /**
                 * Gets the value of the loyaltyTransactionID property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getLoyaltyTransactionID() {
                    return loyaltyTransactionID;
                }

                /**
                 * Sets the value of the loyaltyTransactionID property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setLoyaltyTransactionID(String value) {
                    this.loyaltyTransactionID = value;
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
             *         &lt;element name="OptTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
                "optTransactionID",
                "additionalInfo"
            })
            public static class OtherPaymentTypes {

                @XmlElement(name = "OptTransactionID")
                protected String optTransactionID;
                @XmlElement(name = "AdditionalInfo")
                protected List<AdditionalInfo> additionalInfo;

                /**
                 * Gets the value of the optTransactionID property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getOptTransactionID() {
                    return optTransactionID;
                }

                /**
                 * Sets the value of the optTransactionID property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setOptTransactionID(String value) {
                    this.optTransactionID = value;
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
             *         &lt;element name="WalletTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
                "walletTransactionID",
                "additionalInfo"
            })
            public static class WalletDetails {

                @XmlElement(name = "WalletTransactionID")
                protected String walletTransactionID;
                @XmlElement(name = "AdditionalInfo")
                protected List<AdditionalInfo> additionalInfo;

                /**
                 * Gets the value of the walletTransactionID property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getWalletTransactionID() {
                    return walletTransactionID;
                }

                /**
                 * Sets the value of the walletTransactionID property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setWalletTransactionID(String value) {
                    this.walletTransactionID = value;
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
         *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="FranchiseeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "userId",
            "franchiseeCode",
            "miscPurposeCode",
            "additionalInfo"
        })
        public static class FranchiseePayment {

            @XmlElement(name = "UserId", required = true)
            protected String userId;
            @XmlElement(name = "FranchiseeCode", required = true)
            protected String franchiseeCode;
            @XmlElement(name = "MiscPurposeCode", required = true)
            protected String miscPurposeCode;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the userId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUserId() {
                return userId;
            }

            /**
             * Sets the value of the userId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUserId(String value) {
                this.userId = value;
            }

            /**
             * Gets the value of the franchiseeCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFranchiseeCode() {
                return franchiseeCode;
            }

            /**
             * Sets the value of the franchiseeCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFranchiseeCode(String value) {
                this.franchiseeCode = value;
            }

            /**
             * Gets the value of the miscPurposeCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMiscPurposeCode() {
                return miscPurposeCode;
            }

            /**
             * Sets the value of the miscPurposeCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMiscPurposeCode(String value) {
                this.miscPurposeCode = value;
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

}
