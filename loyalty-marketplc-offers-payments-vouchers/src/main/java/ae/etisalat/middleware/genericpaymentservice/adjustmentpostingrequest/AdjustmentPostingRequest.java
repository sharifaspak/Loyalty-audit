
package ae.etisalat.middleware.genericpaymentservice.adjustmentpostingrequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.applicationheader.ApplicationHeader;
import ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo;


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
 *                   &lt;element name="RequestedUserName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AdjustmentDetails" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *                             &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AdjAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                             &lt;element name="VATAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element name="VATRatio" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element name="AdjType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="AdjTransType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="AdjChargeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="AdjRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AdjReasonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="InvoiceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="TopUpCompleted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="DueAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element name="GLAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CRMSubRequestNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="ComplaintReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CustomerContactNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="DOPActivityType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="DOPSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="RatePlanCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AdjPeriodFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AdjPeriodTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlRootElement(name = "AdjustmentPostingRequest")
public class AdjustmentPostingRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected AdjustmentPostingRequest.DataHeader dataHeader;

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
     *     {@link AdjustmentPostingRequest.DataHeader }
     *     
     */
    public AdjustmentPostingRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdjustmentPostingRequest.DataHeader }
     *     
     */
    public void setDataHeader(AdjustmentPostingRequest.DataHeader value) {
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
     *         &lt;element name="RequestedUserName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AdjustmentDetails" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
     *                   &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AdjAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                   &lt;element name="VATAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element name="VATRatio" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element name="AdjType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="AdjTransType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="AdjChargeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="AdjRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AdjReasonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="InvoiceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="TopUpCompleted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="DueAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element name="GLAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CRMSubRequestNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="ComplaintReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CustomerContactNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="DOPActivityType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="DOPSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="RatePlanCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AdjPeriodFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AdjPeriodTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    @XmlType(name = "", propOrder = {
        "requestedUserName",
        "externalTransactionCode",
        "channelTransactionDate",
        "collectionLocationCode",
        "collectionLocationName",
        "collectionRegionCode",
        "adjustmentDetails",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "RequestedUserName", required = true)
        protected String requestedUserName;
        @XmlElement(name = "ExternalTransactionCode", required = true)
        protected String externalTransactionCode;
        @XmlElement(name = "ChannelTransactionDate", required = true)
        protected String channelTransactionDate;
        @XmlElement(name = "CollectionLocationCode")
        protected String collectionLocationCode;
        @XmlElement(name = "CollectionLocationName")
        protected String collectionLocationName;
        @XmlElement(name = "CollectionRegionCode")
        protected String collectionRegionCode;
        @XmlElement(name = "AdjustmentDetails", required = true)
        protected List<AdjustmentPostingRequest.DataHeader.AdjustmentDetails> adjustmentDetails;
        @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
        protected List<AdditionalInfo> additionalInfo;

        /**
         * Gets the value of the requestedUserName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRequestedUserName() {
            return requestedUserName;
        }

        /**
         * Sets the value of the requestedUserName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRequestedUserName(String value) {
            this.requestedUserName = value;
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
         * Gets the value of the adjustmentDetails property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the adjustmentDetails property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAdjustmentDetails().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AdjustmentPostingRequest.DataHeader.AdjustmentDetails }
         * 
         * 
         */
        public List<AdjustmentPostingRequest.DataHeader.AdjustmentDetails> getAdjustmentDetails() {
            if (adjustmentDetails == null) {
                adjustmentDetails = new ArrayList<AdjustmentPostingRequest.DataHeader.AdjustmentDetails>();
            }
            return this.adjustmentDetails;
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
         *         &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
         *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AdjAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *         &lt;element name="VATAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element name="VATRatio" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element name="AdjType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="AdjTransType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="AdjChargeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="AdjRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AdjReasonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="InvoiceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="TopUpCompleted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="DueAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element name="GLAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CRMSubRequestNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ComplaintReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CustomerContactNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="DOPActivityType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="DOPSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="RatePlanCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AdjPeriodFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AdjPeriodTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        @XmlType(name = "", propOrder = {
            "accountID",
            "accountNumber",
            "accountType",
            "regionCode",
            "adjAmount",
            "vatAmount",
            "vatRatio",
            "adjType",
            "adjTransType",
            "adjChargeCode",
            "referenceNumber",
            "adjRemarks",
            "adjReasonCode",
            "invoiceNumber",
            "topUpCompleted",
            "dueAmount",
            "glAccountNo",
            "crmSubRequestNo",
            "complaintReferenceNo",
            "customerContactNo",
            "dopActivityType",
            "dopSubType",
            "ratePlanCode",
            "adjPeriodFrom",
            "adjPeriodTo",
            "additionalInfo"
        })
        public static class AdjustmentDetails {

            @XmlElement(name = "AccountID")
            protected Long accountID;
            @XmlElement(name = "AccountNumber")
            protected String accountNumber;
            @XmlElement(name = "AccountType")
            protected String accountType;
            @XmlElement(name = "RegionCode")
            protected String regionCode;
            @XmlElement(name = "AdjAmount")
            protected double adjAmount;
            @XmlElement(name = "VATAmount")
            protected Double vatAmount;
            @XmlElement(name = "VATRatio")
            protected Double vatRatio;
            @XmlElement(name = "AdjType", required = true)
            protected String adjType;
            @XmlElement(name = "AdjTransType", required = true)
            protected String adjTransType;
            @XmlElement(name = "AdjChargeCode", required = true)
            protected String adjChargeCode;
            @XmlElement(name = "ReferenceNumber", required = true)
            protected String referenceNumber;
            @XmlElement(name = "AdjRemarks")
            protected String adjRemarks;
            @XmlElement(name = "AdjReasonCode")
            protected String adjReasonCode;
            @XmlElement(name = "InvoiceNumber")
            protected String invoiceNumber;
            @XmlElement(name = "TopUpCompleted")
            protected String topUpCompleted;
            @XmlElement(name = "DueAmount")
            protected Double dueAmount;
            @XmlElement(name = "GLAccountNo")
            protected String glAccountNo;
            @XmlElement(name = "CRMSubRequestNo")
            protected String crmSubRequestNo;
            @XmlElement(name = "ComplaintReferenceNo")
            protected String complaintReferenceNo;
            @XmlElement(name = "CustomerContactNo")
            protected String customerContactNo;
            @XmlElement(name = "DOPActivityType")
            protected String dopActivityType;
            @XmlElement(name = "DOPSubType")
            protected String dopSubType;
            @XmlElement(name = "RatePlanCode")
            protected String ratePlanCode;
            @XmlElement(name = "AdjPeriodFrom")
            protected String adjPeriodFrom;
            @XmlElement(name = "AdjPeriodTo")
            protected String adjPeriodTo;
            @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the accountID property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getAccountID() {
                return accountID;
            }

            /**
             * Sets the value of the accountID property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setAccountID(Long value) {
                this.accountID = value;
            }

            /**
             * Gets the value of the accountNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAccountNumber() {
                return accountNumber;
            }

            /**
             * Sets the value of the accountNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAccountNumber(String value) {
                this.accountNumber = value;
            }

            /**
             * Gets the value of the accountType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAccountType() {
                return accountType;
            }

            /**
             * Sets the value of the accountType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAccountType(String value) {
                this.accountType = value;
            }

            /**
             * Gets the value of the regionCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRegionCode() {
                return regionCode;
            }

            /**
             * Sets the value of the regionCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRegionCode(String value) {
                this.regionCode = value;
            }

            /**
             * Gets the value of the adjAmount property.
             * 
             */
            public double getAdjAmount() {
                return adjAmount;
            }

            /**
             * Sets the value of the adjAmount property.
             * 
             */
            public void setAdjAmount(double value) {
                this.adjAmount = value;
            }

            /**
             * Gets the value of the vatAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getVATAmount() {
                return vatAmount;
            }

            /**
             * Sets the value of the vatAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setVATAmount(Double value) {
                this.vatAmount = value;
            }

            /**
             * Gets the value of the vatRatio property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getVATRatio() {
                return vatRatio;
            }

            /**
             * Sets the value of the vatRatio property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setVATRatio(Double value) {
                this.vatRatio = value;
            }

            /**
             * Gets the value of the adjType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdjType() {
                return adjType;
            }

            /**
             * Sets the value of the adjType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdjType(String value) {
                this.adjType = value;
            }

            /**
             * Gets the value of the adjTransType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdjTransType() {
                return adjTransType;
            }

            /**
             * Sets the value of the adjTransType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdjTransType(String value) {
                this.adjTransType = value;
            }

            /**
             * Gets the value of the adjChargeCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdjChargeCode() {
                return adjChargeCode;
            }

            /**
             * Sets the value of the adjChargeCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdjChargeCode(String value) {
                this.adjChargeCode = value;
            }

            /**
             * Gets the value of the referenceNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getReferenceNumber() {
                return referenceNumber;
            }

            /**
             * Sets the value of the referenceNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setReferenceNumber(String value) {
                this.referenceNumber = value;
            }

            /**
             * Gets the value of the adjRemarks property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdjRemarks() {
                return adjRemarks;
            }

            /**
             * Sets the value of the adjRemarks property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdjRemarks(String value) {
                this.adjRemarks = value;
            }

            /**
             * Gets the value of the adjReasonCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdjReasonCode() {
                return adjReasonCode;
            }

            /**
             * Sets the value of the adjReasonCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdjReasonCode(String value) {
                this.adjReasonCode = value;
            }

            /**
             * Gets the value of the invoiceNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getInvoiceNumber() {
                return invoiceNumber;
            }

            /**
             * Sets the value of the invoiceNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInvoiceNumber(String value) {
                this.invoiceNumber = value;
            }

            /**
             * Gets the value of the topUpCompleted property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTopUpCompleted() {
                return topUpCompleted;
            }

            /**
             * Sets the value of the topUpCompleted property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTopUpCompleted(String value) {
                this.topUpCompleted = value;
            }

            /**
             * Gets the value of the dueAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getDueAmount() {
                return dueAmount;
            }

            /**
             * Sets the value of the dueAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setDueAmount(Double value) {
                this.dueAmount = value;
            }

            /**
             * Gets the value of the glAccountNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getGLAccountNo() {
                return glAccountNo;
            }

            /**
             * Sets the value of the glAccountNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setGLAccountNo(String value) {
                this.glAccountNo = value;
            }

            /**
             * Gets the value of the crmSubRequestNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCRMSubRequestNo() {
                return crmSubRequestNo;
            }

            /**
             * Sets the value of the crmSubRequestNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCRMSubRequestNo(String value) {
                this.crmSubRequestNo = value;
            }

            /**
             * Gets the value of the complaintReferenceNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getComplaintReferenceNo() {
                return complaintReferenceNo;
            }

            /**
             * Sets the value of the complaintReferenceNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setComplaintReferenceNo(String value) {
                this.complaintReferenceNo = value;
            }

            /**
             * Gets the value of the customerContactNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCustomerContactNo() {
                return customerContactNo;
            }

            /**
             * Sets the value of the customerContactNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCustomerContactNo(String value) {
                this.customerContactNo = value;
            }

            /**
             * Gets the value of the dopActivityType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDOPActivityType() {
                return dopActivityType;
            }

            /**
             * Sets the value of the dopActivityType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDOPActivityType(String value) {
                this.dopActivityType = value;
            }

            /**
             * Gets the value of the dopSubType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDOPSubType() {
                return dopSubType;
            }

            /**
             * Sets the value of the dopSubType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDOPSubType(String value) {
                this.dopSubType = value;
            }

            /**
             * Gets the value of the ratePlanCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRatePlanCode() {
                return ratePlanCode;
            }

            /**
             * Sets the value of the ratePlanCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRatePlanCode(String value) {
                this.ratePlanCode = value;
            }

            /**
             * Gets the value of the adjPeriodFrom property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdjPeriodFrom() {
                return adjPeriodFrom;
            }

            /**
             * Sets the value of the adjPeriodFrom property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdjPeriodFrom(String value) {
                this.adjPeriodFrom = value;
            }

            /**
             * Gets the value of the adjPeriodTo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdjPeriodTo() {
                return adjPeriodTo;
            }

            /**
             * Sets the value of the adjPeriodTo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdjPeriodTo(String value) {
                this.adjPeriodTo = value;
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
