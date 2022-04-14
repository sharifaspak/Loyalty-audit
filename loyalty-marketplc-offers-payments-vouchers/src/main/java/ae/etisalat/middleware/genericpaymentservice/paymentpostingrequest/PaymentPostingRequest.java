
package ae.etisalat.middleware.genericpaymentservice.paymentpostingrequest;

import java.math.BigDecimal;
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
 *                   &lt;element name="PaidTotalAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
 *                   &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CurrencyRate" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PaymentAccepted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="BulkPayment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PaymentProcessed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SMSNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="EmailNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PushNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PaymentDetails"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="AccountId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *                             &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="PaymentCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="PaidAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                             &lt;element name="PartyId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *                             &lt;element name="InvoiceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="DueAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element name="CreditStatusID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *                             &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="AccountStatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="PaymentPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="TopupCompleted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="GLCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element name="ChequeAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element name="CardAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element name="LoyaltyTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="LoyaltyAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="BankAdviceDetails" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                             &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *                             &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AdviceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankAdviceRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankAdviceAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element name="OptAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element name="OptReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="OptTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="WalletPayment" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="WalletNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="WalletSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="WalletAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                             &lt;element name="WalletTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CashPayment" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="CashAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlRootElement(name = "PaymentPostingRequest")
public class PaymentPostingRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected PaymentPostingRequest.DataHeader dataHeader;

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
     *     {@link PaymentPostingRequest.DataHeader }
     *     
     */
    public PaymentPostingRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentPostingRequest.DataHeader }
     *     
     */
    public void setDataHeader(PaymentPostingRequest.DataHeader value) {
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
     *         &lt;element name="PaidTotalAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
     *         &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CurrencyRate" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PaymentAccepted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="BulkPayment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PaymentProcessed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SMSNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="EmailNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PushNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PaymentDetails"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="AccountId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
     *                   &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="PaymentCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="PaidAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                   &lt;element name="PartyId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
     *                   &lt;element name="InvoiceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="DueAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element name="CreditStatusID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
     *                   &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="AccountStatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="PaymentPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="TopupCompleted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="GLCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element name="ChequeAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element name="CardAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element name="LoyaltyTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="LoyaltyAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="BankAdviceDetails" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                   &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
     *                   &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AdviceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankAdviceRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankAdviceAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element name="OptAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element name="OptReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="OptTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="WalletPayment" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="WalletNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="WalletSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="WalletAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *                   &lt;element name="WalletTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CashPayment" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="CashAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "remarks",
        "channelType",
        "collectionLocationCode",
        "collectionLocationName",
        "collectionRegionCode",
        "currencyRate",
        "externalTransactionCode",
        "channelTransactionDate",
        "referenceNo",
        "paymentAccepted",
        "bulkPayment",
        "paymentProcessed",
        "smsNotification",
        "emailNotification",
        "pushNotification",
        "paymentDetails",
        "chequeDetails",
        "cardDetails",
        "loyaltyDetails",
        "bankAdviceDetails",
        "otherPaymentTypes",
        "franchiseePayment",
        "walletPayment",
        "cashPayment",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "PaidTotalAmount")
        protected String paidTotalAmount;
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
        @XmlElement(name = "CurrencyRate")
        protected BigDecimal currencyRate;
        @XmlElement(name = "ExternalTransactionCode", required = true)
        protected String externalTransactionCode;
        @XmlElement(name = "ChannelTransactionDate", required = true)
        protected String channelTransactionDate;
        @XmlElement(name = "ReferenceNo")
        protected String referenceNo;
        @XmlElement(name = "PaymentAccepted")
        protected String paymentAccepted;
        @XmlElement(name = "BulkPayment")
        protected String bulkPayment;
        @XmlElement(name = "PaymentProcessed")
        protected String paymentProcessed;
        @XmlElement(name = "SMSNotification")
        protected String smsNotification;
        @XmlElement(name = "EmailNotification")
        protected String emailNotification;
        @XmlElement(name = "PushNotification")
        protected String pushNotification;
        @XmlElement(name = "PaymentDetails", required = true)
        protected PaymentPostingRequest.DataHeader.PaymentDetails paymentDetails;
        @XmlElement(name = "ChequeDetails")
        protected PaymentPostingRequest.DataHeader.ChequeDetails chequeDetails;
        @XmlElement(name = "CardDetails")
        protected PaymentPostingRequest.DataHeader.CardDetails cardDetails;
        @XmlElement(name = "LoyaltyDetails")
        protected PaymentPostingRequest.DataHeader.LoyaltyDetails loyaltyDetails;
        @XmlElement(name = "BankAdviceDetails")
        protected PaymentPostingRequest.DataHeader.BankAdviceDetails bankAdviceDetails;
        @XmlElement(name = "OtherPaymentTypes")
        protected PaymentPostingRequest.DataHeader.OtherPaymentTypes otherPaymentTypes;
        @XmlElement(name = "FranchiseePayment")
        protected PaymentPostingRequest.DataHeader.FranchiseePayment franchiseePayment;
        @XmlElement(name = "WalletPayment")
        protected PaymentPostingRequest.DataHeader.WalletPayment walletPayment;
        @XmlElement(name = "CashPayment")
        protected PaymentPostingRequest.DataHeader.CashPayment cashPayment;
        @XmlElement(name = "AdditionalInfo")
        protected List<AdditionalInfo> additionalInfo;

        /**
         * Gets the value of the paidTotalAmount property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaidTotalAmount() {
            return paidTotalAmount;
        }

        /**
         * Sets the value of the paidTotalAmount property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaidTotalAmount(String value) {
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
         * Gets the value of the currencyRate property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCurrencyRate() {
            return currencyRate;
        }

        /**
         * Sets the value of the currencyRate property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCurrencyRate(BigDecimal value) {
            this.currencyRate = value;
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
         * Gets the value of the referenceNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenceNo() {
            return referenceNo;
        }

        /**
         * Sets the value of the referenceNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenceNo(String value) {
            this.referenceNo = value;
        }

        /**
         * Gets the value of the paymentAccepted property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaymentAccepted() {
            return paymentAccepted;
        }

        /**
         * Sets the value of the paymentAccepted property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaymentAccepted(String value) {
            this.paymentAccepted = value;
        }

        /**
         * Gets the value of the bulkPayment property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBulkPayment() {
            return bulkPayment;
        }

        /**
         * Sets the value of the bulkPayment property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBulkPayment(String value) {
            this.bulkPayment = value;
        }

        /**
         * Gets the value of the paymentProcessed property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaymentProcessed() {
            return paymentProcessed;
        }

        /**
         * Sets the value of the paymentProcessed property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaymentProcessed(String value) {
            this.paymentProcessed = value;
        }

        /**
         * Gets the value of the smsNotification property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSMSNotification() {
            return smsNotification;
        }

        /**
         * Sets the value of the smsNotification property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSMSNotification(String value) {
            this.smsNotification = value;
        }

        /**
         * Gets the value of the emailNotification property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmailNotification() {
            return emailNotification;
        }

        /**
         * Sets the value of the emailNotification property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmailNotification(String value) {
            this.emailNotification = value;
        }

        /**
         * Gets the value of the pushNotification property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPushNotification() {
            return pushNotification;
        }

        /**
         * Sets the value of the pushNotification property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPushNotification(String value) {
            this.pushNotification = value;
        }

        /**
         * Gets the value of the paymentDetails property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.PaymentDetails }
         *     
         */
        public PaymentPostingRequest.DataHeader.PaymentDetails getPaymentDetails() {
            return paymentDetails;
        }

        /**
         * Sets the value of the paymentDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.PaymentDetails }
         *     
         */
        public void setPaymentDetails(PaymentPostingRequest.DataHeader.PaymentDetails value) {
            this.paymentDetails = value;
        }

        /**
         * Gets the value of the chequeDetails property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.ChequeDetails }
         *     
         */
        public PaymentPostingRequest.DataHeader.ChequeDetails getChequeDetails() {
            return chequeDetails;
        }

        /**
         * Sets the value of the chequeDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.ChequeDetails }
         *     
         */
        public void setChequeDetails(PaymentPostingRequest.DataHeader.ChequeDetails value) {
            this.chequeDetails = value;
        }

        /**
         * Gets the value of the cardDetails property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.CardDetails }
         *     
         */
        public PaymentPostingRequest.DataHeader.CardDetails getCardDetails() {
            return cardDetails;
        }

        /**
         * Sets the value of the cardDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.CardDetails }
         *     
         */
        public void setCardDetails(PaymentPostingRequest.DataHeader.CardDetails value) {
            this.cardDetails = value;
        }

        /**
         * Gets the value of the loyaltyDetails property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.LoyaltyDetails }
         *     
         */
        public PaymentPostingRequest.DataHeader.LoyaltyDetails getLoyaltyDetails() {
            return loyaltyDetails;
        }

        /**
         * Sets the value of the loyaltyDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.LoyaltyDetails }
         *     
         */
        public void setLoyaltyDetails(PaymentPostingRequest.DataHeader.LoyaltyDetails value) {
            this.loyaltyDetails = value;
        }

        /**
         * Gets the value of the bankAdviceDetails property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.BankAdviceDetails }
         *     
         */
        public PaymentPostingRequest.DataHeader.BankAdviceDetails getBankAdviceDetails() {
            return bankAdviceDetails;
        }

        /**
         * Sets the value of the bankAdviceDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.BankAdviceDetails }
         *     
         */
        public void setBankAdviceDetails(PaymentPostingRequest.DataHeader.BankAdviceDetails value) {
            this.bankAdviceDetails = value;
        }

        /**
         * Gets the value of the otherPaymentTypes property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.OtherPaymentTypes }
         *     
         */
        public PaymentPostingRequest.DataHeader.OtherPaymentTypes getOtherPaymentTypes() {
            return otherPaymentTypes;
        }

        /**
         * Sets the value of the otherPaymentTypes property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.OtherPaymentTypes }
         *     
         */
        public void setOtherPaymentTypes(PaymentPostingRequest.DataHeader.OtherPaymentTypes value) {
            this.otherPaymentTypes = value;
        }

        /**
         * Gets the value of the franchiseePayment property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.FranchiseePayment }
         *     
         */
        public PaymentPostingRequest.DataHeader.FranchiseePayment getFranchiseePayment() {
            return franchiseePayment;
        }

        /**
         * Sets the value of the franchiseePayment property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.FranchiseePayment }
         *     
         */
        public void setFranchiseePayment(PaymentPostingRequest.DataHeader.FranchiseePayment value) {
            this.franchiseePayment = value;
        }

        /**
         * Gets the value of the walletPayment property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.WalletPayment }
         *     
         */
        public PaymentPostingRequest.DataHeader.WalletPayment getWalletPayment() {
            return walletPayment;
        }

        /**
         * Sets the value of the walletPayment property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.WalletPayment }
         *     
         */
        public void setWalletPayment(PaymentPostingRequest.DataHeader.WalletPayment value) {
            this.walletPayment = value;
        }

        /**
         * Gets the value of the cashPayment property.
         * 
         * @return
         *     possible object is
         *     {@link PaymentPostingRequest.DataHeader.CashPayment }
         *     
         */
        public PaymentPostingRequest.DataHeader.CashPayment getCashPayment() {
            return cashPayment;
        }

        /**
         * Sets the value of the cashPayment property.
         * 
         * @param value
         *     allowed object is
         *     {@link PaymentPostingRequest.DataHeader.CashPayment }
         *     
         */
        public void setCashPayment(PaymentPostingRequest.DataHeader.CashPayment value) {
            this.cashPayment = value;
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
         *         &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *         &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
         *         &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AdviceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankAdviceRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankAdviceAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "conversionRate",
            "bankID",
            "bankCode",
            "bankName",
            "adviceDate",
            "bankAdviceRefNo",
            "bankAdviceAmount",
            "additionalInfo"
        })
        public static class BankAdviceDetails {

            @XmlElement(name = "ConversionRate")
            protected double conversionRate;
            @XmlElement(name = "BankID")
            protected Long bankID;
            @XmlElement(name = "BankCode")
            protected String bankCode;
            @XmlElement(name = "BankName")
            protected String bankName;
            @XmlElement(name = "AdviceDate")
            protected String adviceDate;
            @XmlElement(name = "BankAdviceRefNo")
            protected String bankAdviceRefNo;
            @XmlElement(name = "BankAdviceAmount")
            protected Double bankAdviceAmount;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

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
             * Gets the value of the adviceDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdviceDate() {
                return adviceDate;
            }

            /**
             * Sets the value of the adviceDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdviceDate(String value) {
                this.adviceDate = value;
            }

            /**
             * Gets the value of the bankAdviceRefNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBankAdviceRefNo() {
                return bankAdviceRefNo;
            }

            /**
             * Sets the value of the bankAdviceRefNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBankAdviceRefNo(String value) {
                this.bankAdviceRefNo = value;
            }

            /**
             * Gets the value of the bankAdviceAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getBankAdviceAmount() {
                return bankAdviceAmount;
            }

            /**
             * Sets the value of the bankAdviceAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setBankAdviceAmount(Double value) {
                this.bankAdviceAmount = value;
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
         *         &lt;element name="CardAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "cardAmount",
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
            @XmlElement(name = "CardAmount")
            protected Double cardAmount;
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
             * Gets the value of the cardAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCardAmount() {
                return cardAmount;
            }

            /**
             * Sets the value of the cardAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCardAmount(Double value) {
                this.cardAmount = value;
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
         *         &lt;element name="CashAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "cashAmount",
            "additionalInfo"
        })
        public static class CashPayment {

            @XmlElement(name = "CashAmount")
            protected Double cashAmount;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the cashAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCashAmount() {
                return cashAmount;
            }

            /**
             * Sets the value of the cashAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCashAmount(Double value) {
                this.cashAmount = value;
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
         *         &lt;element name="ChequeAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "chequeAmount",
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
            @XmlElement(name = "ChequeAmount")
            protected Double chequeAmount;
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
             * Gets the value of the chequeAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getChequeAmount() {
                return chequeAmount;
            }

            /**
             * Sets the value of the chequeAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setChequeAmount(Double value) {
                this.chequeAmount = value;
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
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
         *         &lt;element name="LoyaltyTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="LoyaltyAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "loyaltyTransactionID",
            "loyaltyAmount",
            "additionalInfo"
        })
        public static class LoyaltyDetails {

            @XmlElement(name = "LoyaltyNumber", required = true)
            protected String loyaltyNumber;
            @XmlElement(name = "LoyaltyPoints")
            protected long loyaltyPoints;
            @XmlElement(name = "ConversionRate")
            protected double conversionRate;
            @XmlElement(name = "LoyaltyTransactionID")
            protected String loyaltyTransactionID;
            @XmlElement(name = "LoyaltyAmount")
            protected Double loyaltyAmount;
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
             * Gets the value of the loyaltyAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getLoyaltyAmount() {
                return loyaltyAmount;
            }

            /**
             * Sets the value of the loyaltyAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setLoyaltyAmount(Double value) {
                this.loyaltyAmount = value;
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
         *         &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="OptAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element name="OptReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="OptTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "optAmount",
            "optReferenceNo",
            "optTransactionID",
            "additionalInfo"
        })
        public static class OtherPaymentTypes {

            @XmlElement(name = "PaymentType")
            protected String paymentType;
            @XmlElement(name = "OptAmount")
            protected Double optAmount;
            @XmlElement(name = "OptReferenceNo")
            protected String optReferenceNo;
            @XmlElement(name = "OptTransactionID")
            protected String optTransactionID;
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
             * Gets the value of the optAmount property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getOptAmount() {
                return optAmount;
            }

            /**
             * Sets the value of the optAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setOptAmount(Double value) {
                this.optAmount = value;
            }

            /**
             * Gets the value of the optReferenceNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOptReferenceNo() {
                return optReferenceNo;
            }

            /**
             * Sets the value of the optReferenceNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOptReferenceNo(String value) {
                this.optReferenceNo = value;
            }

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
         *         &lt;element name="AccountId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
         *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="PaymentCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="PaidAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *         &lt;element name="PartyId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
         *         &lt;element name="InvoiceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="DueAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
         *         &lt;element name="CreditStatusID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
         *         &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="AccountStatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="PaymentPurposeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="TopupCompleted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="GLCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "accountId",
            "accountNumber",
            "accountType",
            "paymentCategory",
            "paidAmount",
            "partyId",
            "invoiceNumber",
            "remarks",
            "regionCode",
            "dueAmount",
            "creditStatusID",
            "transactionType",
            "accountStatusCode",
            "paymentPurposeCode",
            "referenceNumber",
            "topupCompleted",
            "glCode",
            "additionalInfo"
        })
        public static class PaymentDetails {

            @XmlElement(name = "AccountId")
            protected Long accountId;
            @XmlElement(name = "AccountNumber")
            protected String accountNumber;
            @XmlElement(name = "AccountType")
            protected String accountType;
            @XmlElement(name = "PaymentCategory")
            protected String paymentCategory;
            @XmlElement(name = "PaidAmount")
            protected double paidAmount;
            @XmlElement(name = "PartyId")
            protected Long partyId;
            @XmlElement(name = "InvoiceNumber")
            protected String invoiceNumber;
            @XmlElement(name = "Remarks")
            protected String remarks;
            @XmlElement(name = "RegionCode")
            protected String regionCode;
            @XmlElement(name = "DueAmount")
            protected Double dueAmount;
            @XmlElement(name = "CreditStatusID")
            protected Long creditStatusID;
            @XmlElement(name = "TransactionType", required = true)
            protected String transactionType;
            @XmlElement(name = "AccountStatusCode")
            protected String accountStatusCode;
            @XmlElement(name = "PaymentPurposeCode")
            protected String paymentPurposeCode;
            @XmlElement(name = "ReferenceNumber")
            protected String referenceNumber;
            @XmlElement(name = "TopupCompleted")
            protected String topupCompleted;
            @XmlElement(name = "GLCode")
            protected String glCode;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the accountId property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getAccountId() {
                return accountId;
            }

            /**
             * Sets the value of the accountId property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setAccountId(Long value) {
                this.accountId = value;
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
             * Gets the value of the paymentCategory property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentCategory() {
                return paymentCategory;
            }

            /**
             * Sets the value of the paymentCategory property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentCategory(String value) {
                this.paymentCategory = value;
            }

            /**
             * Gets the value of the paidAmount property.
             * 
             */
            public double getPaidAmount() {
                return paidAmount;
            }

            /**
             * Sets the value of the paidAmount property.
             * 
             */
            public void setPaidAmount(double value) {
                this.paidAmount = value;
            }

            /**
             * Gets the value of the partyId property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getPartyId() {
                return partyId;
            }

            /**
             * Sets the value of the partyId property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setPartyId(Long value) {
                this.partyId = value;
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
             * Gets the value of the creditStatusID property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getCreditStatusID() {
                return creditStatusID;
            }

            /**
             * Sets the value of the creditStatusID property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setCreditStatusID(Long value) {
                this.creditStatusID = value;
            }

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
             * Gets the value of the accountStatusCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAccountStatusCode() {
                return accountStatusCode;
            }

            /**
             * Sets the value of the accountStatusCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAccountStatusCode(String value) {
                this.accountStatusCode = value;
            }

            /**
             * Gets the value of the paymentPurposeCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentPurposeCode() {
                return paymentPurposeCode;
            }

            /**
             * Sets the value of the paymentPurposeCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentPurposeCode(String value) {
                this.paymentPurposeCode = value;
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
             * Gets the value of the topupCompleted property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTopupCompleted() {
                return topupCompleted;
            }

            /**
             * Sets the value of the topupCompleted property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTopupCompleted(String value) {
                this.topupCompleted = value;
            }

            /**
             * Gets the value of the glCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getGLCode() {
                return glCode;
            }

            /**
             * Sets the value of the glCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setGLCode(String value) {
                this.glCode = value;
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
         *         &lt;element name="WalletNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="WalletSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="WalletAmount" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
         *         &lt;element name="WalletTransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "walletNumber",
            "walletSubType",
            "walletAmount",
            "walletTransactionID",
            "additionalInfo"
        })
        public static class WalletPayment {

            @XmlElement(name = "WalletNumber", required = true)
            protected String walletNumber;
            @XmlElement(name = "WalletSubType", required = true)
            protected String walletSubType;
            @XmlElement(name = "WalletAmount")
            protected double walletAmount;
            @XmlElement(name = "WalletTransactionID")
            protected String walletTransactionID;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the walletNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getWalletNumber() {
                return walletNumber;
            }

            /**
             * Sets the value of the walletNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setWalletNumber(String value) {
                this.walletNumber = value;
            }

            /**
             * Gets the value of the walletSubType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getWalletSubType() {
                return walletSubType;
            }

            /**
             * Sets the value of the walletSubType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setWalletSubType(String value) {
                this.walletSubType = value;
            }

            /**
             * Gets the value of the walletAmount property.
             * 
             */
            public double getWalletAmount() {
                return walletAmount;
            }

            /**
             * Sets the value of the walletAmount property.
             * 
             */
            public void setWalletAmount(double value) {
                this.walletAmount = value;
            }

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

}
