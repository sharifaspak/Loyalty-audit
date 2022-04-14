
package ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest;

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
 *                   &lt;element name="PaidTotalAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                   &lt;element name="PaymentModeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="IsCancelled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CancellationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CancellationReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CancellationRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LinkPaymentTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CashierName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="Currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MiscPaymentDetails" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                             &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="BidderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;element name="CardDenomination" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="Denomination" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                       &lt;element name="NoOfUnit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                       &lt;element name="DiscountPercentage" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                                       &lt;element name="DiscountedAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                                       &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ChequeDetails" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ChequeNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ChequeDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *                             &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="Emirate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CardDetails" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="CardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CardSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="CardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="InstallmentFlag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                             &lt;element name="CardExpiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="TerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="MerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *                             &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="EPGTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element name="LoyaltyNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="LoyaltyPoints" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *                             &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CashierShiftDetails" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="CashierUsername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CashierShiftStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CashierShiftEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CashierLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="FranchiseePayment" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="FranchiseeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlRootElement(name = "MiscPaymentRequest")
public class MiscPaymentRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected MiscPaymentRequest.DataHeader dataHeader;

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
     *     {@link MiscPaymentRequest.DataHeader }
     *     
     */
    public MiscPaymentRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link MiscPaymentRequest.DataHeader }
     *     
     */
    public void setDataHeader(MiscPaymentRequest.DataHeader value) {
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
     *         &lt;element name="PaidTotalAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *         &lt;element name="PaymentModeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="IsCancelled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CancellationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CancellationReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CancellationRemarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LinkPaymentTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CashierName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="Currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MiscPaymentDetails" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="BidderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;element name="CardDenomination" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="Denomination" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                             &lt;element name="NoOfUnit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                             &lt;element name="DiscountPercentage" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                             &lt;element name="DiscountedAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ChequeDetails" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ChequeNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ChequeDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
     *                   &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="Emirate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CardDetails" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="CardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CardSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="CardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="InstallmentFlag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *                   &lt;element name="CardExpiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="TerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="MerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
     *                   &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="EPGTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element name="LoyaltyNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="LoyaltyPoints" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
     *                   &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CashierShiftDetails" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="CashierUsername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CashierShiftStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CashierShiftEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CashierLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="FranchiseePayment" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="FranchiseeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "paidTotalAmount",
        "paymentModeCode",
        "isCancelled",
        "cancellationDate",
        "cancellationReason",
        "cancellationRemarks",
        "linkPaymentTransactionCode",
        "cashierName",
        "bankCode",
        "currency",
        "paymentType",
        "externalTransactionCode",
        "channelTransactionDate",
        "remarks",
        "channelType",
        "collectionLocationCode",
        "collectionLocationName",
        "collectionRegionCode",
        "miscPaymentDetails",
        "chequeDetails",
        "cardDetails",
        "loyaltyDetails",
        "cashierShiftDetails",
        "otherPaymentTypes",
        "franchiseePayment",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "PaidTotalAmount")
        protected Double paidTotalAmount;
        @XmlElement(name = "PaymentModeCode")
        protected String paymentModeCode;
        @XmlElement(name = "IsCancelled")
        protected String isCancelled;
        @XmlElement(name = "CancellationDate")
        protected String cancellationDate;
        @XmlElement(name = "CancellationReason")
        protected String cancellationReason;
        @XmlElement(name = "CancellationRemarks")
        protected String cancellationRemarks;
        @XmlElement(name = "LinkPaymentTransactionCode")
        protected String linkPaymentTransactionCode;
        @XmlElement(name = "CashierName", required = true)
        protected String cashierName;
        @XmlElement(name = "BankCode")
        protected String bankCode;
        @XmlElement(name = "Currency")
        protected String currency;
        @XmlElement(name = "PaymentType", required = true)
        protected String paymentType;
        @XmlElement(name = "ExternalTransactionCode", required = true)
        protected String externalTransactionCode;
        @XmlElement(name = "ChannelTransactionDate", required = true)
        protected String channelTransactionDate;
        @XmlElement(name = "Remarks")
        protected String remarks;
        @XmlElement(name = "ChannelType")
        protected String channelType;
        @XmlElement(name = "CollectionLocationCode")
        protected String collectionLocationCode;
        @XmlElement(name = "CollectionLocationName")
        protected String collectionLocationName;
        @XmlElement(name = "CollectionRegionCode")
        protected String collectionRegionCode;
        @XmlElement(name = "MiscPaymentDetails", required = true)
        protected List<MiscPaymentRequest.DataHeader.MiscPaymentDetails> miscPaymentDetails;
        @XmlElement(name = "ChequeDetails")
        protected MiscPaymentRequest.DataHeader.ChequeDetails chequeDetails;
        @XmlElement(name = "CardDetails")
        protected MiscPaymentRequest.DataHeader.CardDetails cardDetails;
        @XmlElement(name = "LoyaltyDetails")
        protected MiscPaymentRequest.DataHeader.LoyaltyDetails loyaltyDetails;
        @XmlElement(name = "CashierShiftDetails")
        protected MiscPaymentRequest.DataHeader.CashierShiftDetails cashierShiftDetails;
        @XmlElement(name = "OtherPaymentTypes")
        protected MiscPaymentRequest.DataHeader.OtherPaymentTypes otherPaymentTypes;
        @XmlElement(name = "FranchiseePayment")
        protected MiscPaymentRequest.DataHeader.FranchiseePayment franchiseePayment;
        @XmlElement(name = "AdditionalInfo")
        protected List<AdditionalInfo> additionalInfo;

        /**
         * Gets the value of the paidTotalAmount property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getPaidTotalAmount() {
            return paidTotalAmount;
        }

        /**
         * Sets the value of the paidTotalAmount property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setPaidTotalAmount(Double value) {
            this.paidTotalAmount = value;
        }

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
         * Gets the value of the isCancelled property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsCancelled() {
            return isCancelled;
        }

        /**
         * Sets the value of the isCancelled property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsCancelled(String value) {
            this.isCancelled = value;
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
         * Gets the value of the linkPaymentTransactionCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLinkPaymentTransactionCode() {
            return linkPaymentTransactionCode;
        }

        /**
         * Sets the value of the linkPaymentTransactionCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLinkPaymentTransactionCode(String value) {
            this.linkPaymentTransactionCode = value;
        }

        /**
         * Gets the value of the cashierName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCashierName() {
            return cashierName;
        }

        /**
         * Sets the value of the cashierName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCashierName(String value) {
            this.cashierName = value;
        }

        /**
         * Gets the value of the bankCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBankCode() {
            return bankCode;
        }

        /**
         * Sets the value of the bankCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBankCode(String value) {
            this.bankCode = value;
        }

        /**
         * Gets the value of the currency property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCurrency() {
            return currency;
        }

        /**
         * Sets the value of the currency property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCurrency(String value) {
            this.currency = value;
        }

        /**
         * Gets the value of the paymentType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaymentType() {
            return paymentType;
        }

        /**
         * Sets the value of the paymentType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaymentType(String value) {
            this.paymentType = value;
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
         * Gets the value of the miscPaymentDetails property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the miscPaymentDetails property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMiscPaymentDetails().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MiscPaymentRequest.DataHeader.MiscPaymentDetails }
         * 
         * 
         */
        public List<MiscPaymentRequest.DataHeader.MiscPaymentDetails> getMiscPaymentDetails() {
            if (miscPaymentDetails == null) {
                miscPaymentDetails = new ArrayList<MiscPaymentRequest.DataHeader.MiscPaymentDetails>();
            }
            return this.miscPaymentDetails;
        }

        /**
         * Gets the value of the chequeDetails property.
         * 
         * @return
         *     possible object is
         *     {@link MiscPaymentRequest.DataHeader.ChequeDetails }
         *     
         */
        public MiscPaymentRequest.DataHeader.ChequeDetails getChequeDetails() {
            return chequeDetails;
        }

        /**
         * Sets the value of the chequeDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link MiscPaymentRequest.DataHeader.ChequeDetails }
         *     
         */
        public void setChequeDetails(MiscPaymentRequest.DataHeader.ChequeDetails value) {
            this.chequeDetails = value;
        }

        /**
         * Gets the value of the cardDetails property.
         * 
         * @return
         *     possible object is
         *     {@link MiscPaymentRequest.DataHeader.CardDetails }
         *     
         */
        public MiscPaymentRequest.DataHeader.CardDetails getCardDetails() {
            return cardDetails;
        }

        /**
         * Sets the value of the cardDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link MiscPaymentRequest.DataHeader.CardDetails }
         *     
         */
        public void setCardDetails(MiscPaymentRequest.DataHeader.CardDetails value) {
            this.cardDetails = value;
        }

        /**
         * Gets the value of the loyaltyDetails property.
         * 
         * @return
         *     possible object is
         *     {@link MiscPaymentRequest.DataHeader.LoyaltyDetails }
         *     
         */
        public MiscPaymentRequest.DataHeader.LoyaltyDetails getLoyaltyDetails() {
            return loyaltyDetails;
        }

        /**
         * Sets the value of the loyaltyDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link MiscPaymentRequest.DataHeader.LoyaltyDetails }
         *     
         */
        public void setLoyaltyDetails(MiscPaymentRequest.DataHeader.LoyaltyDetails value) {
            this.loyaltyDetails = value;
        }

        /**
         * Gets the value of the cashierShiftDetails property.
         * 
         * @return
         *     possible object is
         *     {@link MiscPaymentRequest.DataHeader.CashierShiftDetails }
         *     
         */
        public MiscPaymentRequest.DataHeader.CashierShiftDetails getCashierShiftDetails() {
            return cashierShiftDetails;
        }

        /**
         * Sets the value of the cashierShiftDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link MiscPaymentRequest.DataHeader.CashierShiftDetails }
         *     
         */
        public void setCashierShiftDetails(MiscPaymentRequest.DataHeader.CashierShiftDetails value) {
            this.cashierShiftDetails = value;
        }

        /**
         * Gets the value of the otherPaymentTypes property.
         * 
         * @return
         *     possible object is
         *     {@link MiscPaymentRequest.DataHeader.OtherPaymentTypes }
         *     
         */
        public MiscPaymentRequest.DataHeader.OtherPaymentTypes getOtherPaymentTypes() {
            return otherPaymentTypes;
        }

        /**
         * Sets the value of the otherPaymentTypes property.
         * 
         * @param value
         *     allowed object is
         *     {@link MiscPaymentRequest.DataHeader.OtherPaymentTypes }
         *     
         */
        public void setOtherPaymentTypes(MiscPaymentRequest.DataHeader.OtherPaymentTypes value) {
            this.otherPaymentTypes = value;
        }

        /**
         * Gets the value of the franchiseePayment property.
         * 
         * @return
         *     possible object is
         *     {@link MiscPaymentRequest.DataHeader.FranchiseePayment }
         *     
         */
        public MiscPaymentRequest.DataHeader.FranchiseePayment getFranchiseePayment() {
            return franchiseePayment;
        }

        /**
         * Sets the value of the franchiseePayment property.
         * 
         * @param value
         *     allowed object is
         *     {@link MiscPaymentRequest.DataHeader.FranchiseePayment }
         *     
         */
        public void setFranchiseePayment(MiscPaymentRequest.DataHeader.FranchiseePayment value) {
            this.franchiseePayment = value;
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
         *         &lt;element name="CardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CardSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="CardToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="InstallmentFlag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
         *         &lt;element name="CardExpiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="TerminalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="MerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
         *         &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="EPGTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "cardNumber",
            "cardType",
            "cardSubType",
            "cardToken",
            "authorizationCode",
            "installmentFlag",
            "cardExpiryDate",
            "terminalId",
            "merchantId",
            "bankID",
            "bankCode",
            "bankName",
            "epgTransactionID",
            "additionalInfo"
        })
        public static class CardDetails {

            @XmlElement(name = "CardNumber")
            protected String cardNumber;
            @XmlElement(name = "CardType")
            protected String cardType;
            @XmlElement(name = "CardSubType", required = true)
            protected String cardSubType;
            @XmlElement(name = "CardToken")
            protected String cardToken;
            @XmlElement(name = "AuthorizationCode", required = true)
            protected String authorizationCode;
            @XmlElement(name = "InstallmentFlag")
            protected Integer installmentFlag;
            @XmlElement(name = "CardExpiryDate")
            protected String cardExpiryDate;
            @XmlElement(name = "TerminalId")
            protected String terminalId;
            @XmlElement(name = "MerchantId")
            protected String merchantId;
            @XmlElement(name = "BankID")
            protected Long bankID;
            @XmlElement(name = "BankCode")
            protected String bankCode;
            @XmlElement(name = "BankName")
            protected String bankName;
            @XmlElement(name = "EPGTransactionID")
            protected String epgTransactionID;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the cardNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCardNumber() {
                return cardNumber;
            }

            /**
             * Sets the value of the cardNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCardNumber(String value) {
                this.cardNumber = value;
            }

            /**
             * Gets the value of the cardType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCardType() {
                return cardType;
            }

            /**
             * Sets the value of the cardType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCardType(String value) {
                this.cardType = value;
            }

            /**
             * Gets the value of the cardSubType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCardSubType() {
                return cardSubType;
            }

            /**
             * Sets the value of the cardSubType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCardSubType(String value) {
                this.cardSubType = value;
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
             * Gets the value of the installmentFlag property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getInstallmentFlag() {
                return installmentFlag;
            }

            /**
             * Sets the value of the installmentFlag property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setInstallmentFlag(Integer value) {
                this.installmentFlag = value;
            }

            /**
             * Gets the value of the cardExpiryDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCardExpiryDate() {
                return cardExpiryDate;
            }

            /**
             * Sets the value of the cardExpiryDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCardExpiryDate(String value) {
                this.cardExpiryDate = value;
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
             * Gets the value of the bankID property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getBankID() {
                return bankID;
            }

            /**
             * Sets the value of the bankID property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setBankID(Long value) {
                this.bankID = value;
            }

            /**
             * Gets the value of the bankCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBankCode() {
                return bankCode;
            }

            /**
             * Sets the value of the bankCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBankCode(String value) {
                this.bankCode = value;
            }

            /**
             * Gets the value of the bankName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBankName() {
                return bankName;
            }

            /**
             * Sets the value of the bankName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBankName(String value) {
                this.bankName = value;
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
         *         &lt;element name="CashierUsername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CashierShiftStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CashierShiftEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CashierLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "cashierUsername",
            "cashierShiftStartDate",
            "cashierShiftEndDate",
            "cashierLocation",
            "additionalInfo"
        })
        public static class CashierShiftDetails {

            @XmlElement(name = "CashierUsername")
            protected String cashierUsername;
            @XmlElement(name = "CashierShiftStartDate")
            protected String cashierShiftStartDate;
            @XmlElement(name = "CashierShiftEndDate")
            protected String cashierShiftEndDate;
            @XmlElement(name = "CashierLocation")
            protected String cashierLocation;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the cashierUsername property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCashierUsername() {
                return cashierUsername;
            }

            /**
             * Sets the value of the cashierUsername property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCashierUsername(String value) {
                this.cashierUsername = value;
            }

            /**
             * Gets the value of the cashierShiftStartDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCashierShiftStartDate() {
                return cashierShiftStartDate;
            }

            /**
             * Sets the value of the cashierShiftStartDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCashierShiftStartDate(String value) {
                this.cashierShiftStartDate = value;
            }

            /**
             * Gets the value of the cashierShiftEndDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCashierShiftEndDate() {
                return cashierShiftEndDate;
            }

            /**
             * Sets the value of the cashierShiftEndDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCashierShiftEndDate(String value) {
                this.cashierShiftEndDate = value;
            }

            /**
             * Gets the value of the cashierLocation property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCashierLocation() {
                return cashierLocation;
            }

            /**
             * Sets the value of the cashierLocation property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCashierLocation(String value) {
                this.cashierLocation = value;
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
         *         &lt;element name="ChequeNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ChequeDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
         *         &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="Emirate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "chequeNumber",
            "chequeDate",
            "bankID",
            "bankCode",
            "bankName",
            "regionCode",
            "emirate",
            "additionalInfo"
        })
        public static class ChequeDetails {

            @XmlElement(name = "ChequeNumber", required = true)
            protected String chequeNumber;
            @XmlElement(name = "ChequeDate", required = true)
            protected String chequeDate;
            @XmlElement(name = "BankID")
            protected Long bankID;
            @XmlElement(name = "BankCode")
            protected String bankCode;
            @XmlElement(name = "BankName")
            protected String bankName;
            @XmlElement(name = "RegionCode", required = true)
            protected String regionCode;
            @XmlElement(name = "Emirate")
            protected String emirate;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the chequeNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getChequeNumber() {
                return chequeNumber;
            }

            /**
             * Sets the value of the chequeNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setChequeNumber(String value) {
                this.chequeNumber = value;
            }

            /**
             * Gets the value of the chequeDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getChequeDate() {
                return chequeDate;
            }

            /**
             * Sets the value of the chequeDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setChequeDate(String value) {
                this.chequeDate = value;
            }

            /**
             * Gets the value of the bankID property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getBankID() {
                return bankID;
            }

            /**
             * Sets the value of the bankID property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setBankID(Long value) {
                this.bankID = value;
            }

            /**
             * Gets the value of the bankCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBankCode() {
                return bankCode;
            }

            /**
             * Sets the value of the bankCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBankCode(String value) {
                this.bankCode = value;
            }

            /**
             * Gets the value of the bankName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBankName() {
                return bankName;
            }

            /**
             * Sets the value of the bankName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBankName(String value) {
                this.bankName = value;
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
             * Gets the value of the emirate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getEmirate() {
                return emirate;
            }

            /**
             * Sets the value of the emirate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setEmirate(String value) {
                this.emirate = value;
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
         *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="FranchiseeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
         *         &lt;element name="LoyaltyNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="LoyaltyPoints" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
         *         &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "loyaltyNumber",
            "loyaltyPoints",
            "conversionRate",
            "additionalInfo"
        })
        public static class LoyaltyDetails {

            @XmlElement(name = "LoyaltyNumber", required = true)
            protected String loyaltyNumber;
            @XmlElement(name = "LoyaltyPoints")
            protected long loyaltyPoints;
            @XmlElement(name = "ConversionRate")
            protected double conversionRate;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the loyaltyNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLoyaltyNumber() {
                return loyaltyNumber;
            }

            /**
             * Sets the value of the loyaltyNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLoyaltyNumber(String value) {
                this.loyaltyNumber = value;
            }

            /**
             * Gets the value of the loyaltyPoints property.
             * 
             */
            public long getLoyaltyPoints() {
                return loyaltyPoints;
            }

            /**
             * Sets the value of the loyaltyPoints property.
             * 
             */
            public void setLoyaltyPoints(long value) {
                this.loyaltyPoints = value;
            }

            /**
             * Gets the value of the conversionRate property.
             * 
             */
            public double getConversionRate() {
                return conversionRate;
            }

            /**
             * Sets the value of the conversionRate property.
             * 
             */
            public void setConversionRate(double value) {
                this.conversionRate = value;
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
         *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="MiscPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="BidderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
         *         &lt;element name="CardDenomination" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="Denomination" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                   &lt;element name="NoOfUnit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                   &lt;element name="DiscountPercentage" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *                   &lt;element name="DiscountedAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "amount",
            "referenceNumber",
            "miscPurposeCode",
            "bidderName",
            "additionalInfo",
            "cardDenomination"
        })
        public static class MiscPaymentDetails {

            @XmlElement(name = "Amount")
            protected double amount;
            @XmlElement(name = "ReferenceNumber")
            protected String referenceNumber;
            @XmlElement(name = "MiscPurposeCode", required = true)
            protected String miscPurposeCode;
            @XmlElement(name = "BidderName")
            protected String bidderName;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;
            @XmlElement(name = "CardDenomination")
            protected MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination cardDenomination;

            /**
             * Gets the value of the amount property.
             * 
             */
            public double getAmount() {
                return amount;
            }

            /**
             * Sets the value of the amount property.
             * 
             */
            public void setAmount(double value) {
                this.amount = value;
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
             * Gets the value of the bidderName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBidderName() {
                return bidderName;
            }

            /**
             * Sets the value of the bidderName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBidderName(String value) {
                this.bidderName = value;
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
             * Gets the value of the cardDenomination property.
             * 
             * @return
             *     possible object is
             *     {@link MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination }
             *     
             */
            public MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination getCardDenomination() {
                return cardDenomination;
            }

            /**
             * Sets the value of the cardDenomination property.
             * 
             * @param value
             *     allowed object is
             *     {@link MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination }
             *     
             */
            public void setCardDenomination(MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination value) {
                this.cardDenomination = value;
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
             *         &lt;element name="Denomination" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *         &lt;element name="NoOfUnit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *         &lt;element name="DiscountPercentage" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
             *         &lt;element name="DiscountedAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
             *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
                "denomination",
                "noOfUnit",
                "discountPercentage",
                "discountedAmount",
                "additionalInfo"
            })
            public static class CardDenomination {

                @XmlElement(name = "Denomination", required = true)
                protected String denomination;
                @XmlElement(name = "NoOfUnit", required = true)
                protected String noOfUnit;
                @XmlElement(name = "DiscountPercentage")
                protected double discountPercentage;
                @XmlElement(name = "DiscountedAmount")
                protected double discountedAmount;
                @XmlElement(name = "AdditionalInfo")
                protected List<AdditionalInfo> additionalInfo;

                /**
                 * Gets the value of the denomination property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDenomination() {
                    return denomination;
                }

                /**
                 * Sets the value of the denomination property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDenomination(String value) {
                    this.denomination = value;
                }

                /**
                 * Gets the value of the noOfUnit property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNoOfUnit() {
                    return noOfUnit;
                }

                /**
                 * Sets the value of the noOfUnit property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNoOfUnit(String value) {
                    this.noOfUnit = value;
                }

                /**
                 * Gets the value of the discountPercentage property.
                 * 
                 */
                public double getDiscountPercentage() {
                    return discountPercentage;
                }

                /**
                 * Sets the value of the discountPercentage property.
                 * 
                 */
                public void setDiscountPercentage(double value) {
                    this.discountPercentage = value;
                }

                /**
                 * Gets the value of the discountedAmount property.
                 * 
                 */
                public double getDiscountedAmount() {
                    return discountedAmount;
                }

                /**
                 * Sets the value of the discountedAmount property.
                 * 
                 */
                public void setDiscountedAmount(double value) {
                    this.discountedAmount = value;
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
         *         &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "paymentType",
            "additionalInfo"
        })
        public static class OtherPaymentTypes {

            @XmlElement(name = "PaymentType")
            protected String paymentType;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the paymentType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentType() {
                return paymentType;
            }

            /**
             * Sets the value of the paymentType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentType(String value) {
                this.paymentType = value;
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
