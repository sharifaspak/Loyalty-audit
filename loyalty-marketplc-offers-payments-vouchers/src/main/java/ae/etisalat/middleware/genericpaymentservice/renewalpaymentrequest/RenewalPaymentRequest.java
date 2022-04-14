
package ae.etisalat.middleware.genericpaymentservice.renewalpaymentrequest;

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
 *                   &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AccountHolderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ProductGroupCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ProductCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AmountReceived" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="PaymentMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="CashierName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="Currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CurrencyRate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *                   &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AdviceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="BankAdviceRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlRootElement(name = "RenewalPaymentRequest")
public class RenewalPaymentRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected RenewalPaymentRequest.DataHeader dataHeader;

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
     *     {@link RenewalPaymentRequest.DataHeader }
     *     
     */
    public RenewalPaymentRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link RenewalPaymentRequest.DataHeader }
     *     
     */
    public void setDataHeader(RenewalPaymentRequest.DataHeader value) {
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
     *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AccountHolderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ProductGroupCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ProductCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AmountReceived" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="PaymentMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ChannelTransactionDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ExternalTransactionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="CashierName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="CollectionLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionLocationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CollectionRegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="Currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CurrencyRate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
     *         &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="Remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AdviceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="BankAdviceRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     *                   &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "accountNumber",
        "accountHolderName",
        "productGroupCode",
        "productCode",
        "amountReceived",
        "orderType",
        "paymentMode",
        "channelTransactionDate",
        "externalTransactionCode",
        "cashierName",
        "collectionLocationCode",
        "collectionLocationName",
        "collectionRegionCode",
        "currency",
        "currencyRate",
        "paymentType",
        "channelType",
        "bankCode",
        "remarks",
        "chequeDetails",
        "cardDetails",
        "loyaltyDetails",
        "bankAdviceDetails",
        "otherPaymentTypes",
        "franchiseePayment",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "AccountNumber", required = true)
        protected String accountNumber;
        @XmlElement(name = "AccountHolderName")
        protected String accountHolderName;
        @XmlElement(name = "ProductGroupCode", required = true)
        protected String productGroupCode;
        @XmlElement(name = "ProductCode", required = true)
        protected String productCode;
        @XmlElement(name = "AmountReceived")
        protected double amountReceived;
        @XmlElement(name = "OrderType", required = true)
        protected String orderType;
        @XmlElement(name = "PaymentMode")
        protected String paymentMode;
        @XmlElement(name = "ChannelTransactionDate")
        protected String channelTransactionDate;
        @XmlElement(name = "ExternalTransactionCode", required = true)
        protected String externalTransactionCode;
        @XmlElement(name = "CashierName", required = true)
        protected String cashierName;
        @XmlElement(name = "CollectionLocationCode")
        protected String collectionLocationCode;
        @XmlElement(name = "CollectionLocationName")
        protected String collectionLocationName;
        @XmlElement(name = "CollectionRegionCode")
        protected String collectionRegionCode;
        @XmlElement(name = "Currency")
        protected String currency;
        @XmlElement(name = "CurrencyRate")
        protected Double currencyRate;
        @XmlElement(name = "PaymentType", required = true)
        protected String paymentType;
        @XmlElement(name = "ChannelType")
        protected String channelType;
        @XmlElement(name = "BankCode")
        protected String bankCode;
        @XmlElement(name = "Remarks")
        protected String remarks;
        @XmlElement(name = "ChequeDetails")
        protected RenewalPaymentRequest.DataHeader.ChequeDetails chequeDetails;
        @XmlElement(name = "CardDetails")
        protected RenewalPaymentRequest.DataHeader.CardDetails cardDetails;
        @XmlElement(name = "LoyaltyDetails")
        protected RenewalPaymentRequest.DataHeader.LoyaltyDetails loyaltyDetails;
        @XmlElement(name = "BankAdviceDetails")
        protected RenewalPaymentRequest.DataHeader.BankAdviceDetails bankAdviceDetails;
        @XmlElement(name = "OtherPaymentTypes")
        protected RenewalPaymentRequest.DataHeader.OtherPaymentTypes otherPaymentTypes;
        @XmlElement(name = "FranchiseePayment")
        protected RenewalPaymentRequest.DataHeader.FranchiseePayment franchiseePayment;
        @XmlElement(name = "AdditionalInfo")
        protected List<AdditionalInfo> additionalInfo;

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
         * Gets the value of the accountHolderName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountHolderName() {
            return accountHolderName;
        }

        /**
         * Sets the value of the accountHolderName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountHolderName(String value) {
            this.accountHolderName = value;
        }

        /**
         * Gets the value of the productGroupCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProductGroupCode() {
            return productGroupCode;
        }

        /**
         * Sets the value of the productGroupCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProductGroupCode(String value) {
            this.productGroupCode = value;
        }

        /**
         * Gets the value of the productCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProductCode() {
            return productCode;
        }

        /**
         * Sets the value of the productCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProductCode(String value) {
            this.productCode = value;
        }

        /**
         * Gets the value of the amountReceived property.
         * 
         */
        public double getAmountReceived() {
            return amountReceived;
        }

        /**
         * Sets the value of the amountReceived property.
         * 
         */
        public void setAmountReceived(double value) {
            this.amountReceived = value;
        }

        /**
         * Gets the value of the orderType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOrderType() {
            return orderType;
        }

        /**
         * Sets the value of the orderType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOrderType(String value) {
            this.orderType = value;
        }

        /**
         * Gets the value of the paymentMode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaymentMode() {
            return paymentMode;
        }

        /**
         * Sets the value of the paymentMode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaymentMode(String value) {
            this.paymentMode = value;
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
         * Gets the value of the currencyRate property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getCurrencyRate() {
            return currencyRate;
        }

        /**
         * Sets the value of the currencyRate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setCurrencyRate(Double value) {
            this.currencyRate = value;
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
         * Gets the value of the chequeDetails property.
         * 
         * @return
         *     possible object is
         *     {@link RenewalPaymentRequest.DataHeader.ChequeDetails }
         *     
         */
        public RenewalPaymentRequest.DataHeader.ChequeDetails getChequeDetails() {
            return chequeDetails;
        }

        /**
         * Sets the value of the chequeDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link RenewalPaymentRequest.DataHeader.ChequeDetails }
         *     
         */
        public void setChequeDetails(RenewalPaymentRequest.DataHeader.ChequeDetails value) {
            this.chequeDetails = value;
        }

        /**
         * Gets the value of the cardDetails property.
         * 
         * @return
         *     possible object is
         *     {@link RenewalPaymentRequest.DataHeader.CardDetails }
         *     
         */
        public RenewalPaymentRequest.DataHeader.CardDetails getCardDetails() {
            return cardDetails;
        }

        /**
         * Sets the value of the cardDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link RenewalPaymentRequest.DataHeader.CardDetails }
         *     
         */
        public void setCardDetails(RenewalPaymentRequest.DataHeader.CardDetails value) {
            this.cardDetails = value;
        }

        /**
         * Gets the value of the loyaltyDetails property.
         * 
         * @return
         *     possible object is
         *     {@link RenewalPaymentRequest.DataHeader.LoyaltyDetails }
         *     
         */
        public RenewalPaymentRequest.DataHeader.LoyaltyDetails getLoyaltyDetails() {
            return loyaltyDetails;
        }

        /**
         * Sets the value of the loyaltyDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link RenewalPaymentRequest.DataHeader.LoyaltyDetails }
         *     
         */
        public void setLoyaltyDetails(RenewalPaymentRequest.DataHeader.LoyaltyDetails value) {
            this.loyaltyDetails = value;
        }

        /**
         * Gets the value of the bankAdviceDetails property.
         * 
         * @return
         *     possible object is
         *     {@link RenewalPaymentRequest.DataHeader.BankAdviceDetails }
         *     
         */
        public RenewalPaymentRequest.DataHeader.BankAdviceDetails getBankAdviceDetails() {
            return bankAdviceDetails;
        }

        /**
         * Sets the value of the bankAdviceDetails property.
         * 
         * @param value
         *     allowed object is
         *     {@link RenewalPaymentRequest.DataHeader.BankAdviceDetails }
         *     
         */
        public void setBankAdviceDetails(RenewalPaymentRequest.DataHeader.BankAdviceDetails value) {
            this.bankAdviceDetails = value;
        }

        /**
         * Gets the value of the otherPaymentTypes property.
         * 
         * @return
         *     possible object is
         *     {@link RenewalPaymentRequest.DataHeader.OtherPaymentTypes }
         *     
         */
        public RenewalPaymentRequest.DataHeader.OtherPaymentTypes getOtherPaymentTypes() {
            return otherPaymentTypes;
        }

        /**
         * Sets the value of the otherPaymentTypes property.
         * 
         * @param value
         *     allowed object is
         *     {@link RenewalPaymentRequest.DataHeader.OtherPaymentTypes }
         *     
         */
        public void setOtherPaymentTypes(RenewalPaymentRequest.DataHeader.OtherPaymentTypes value) {
            this.otherPaymentTypes = value;
        }

        /**
         * Gets the value of the franchiseePayment property.
         * 
         * @return
         *     possible object is
         *     {@link RenewalPaymentRequest.DataHeader.FranchiseePayment }
         *     
         */
        public RenewalPaymentRequest.DataHeader.FranchiseePayment getFranchiseePayment() {
            return franchiseePayment;
        }

        /**
         * Sets the value of the franchiseePayment property.
         * 
         * @param value
         *     allowed object is
         *     {@link RenewalPaymentRequest.DataHeader.FranchiseePayment }
         *     
         */
        public void setFranchiseePayment(RenewalPaymentRequest.DataHeader.FranchiseePayment value) {
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
         *         &lt;element name="ConversionRate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="BankID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AdviceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="BankAdviceRefNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "additionalInfo"
        })
        public static class BankAdviceDetails {

            @XmlElement(name = "ConversionRate", required = true)
            protected String conversionRate;
            @XmlElement(name = "BankID")
            protected String bankID;
            @XmlElement(name = "BankCode")
            protected String bankCode;
            @XmlElement(name = "BankName")
            protected String bankName;
            @XmlElement(name = "AdviceDate")
            protected String adviceDate;
            @XmlElement(name = "BankAdviceRefNo")
            protected String bankAdviceRefNo;
            @XmlElement(name = "AdditionalInfo")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the conversionRate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getConversionRate() {
                return conversionRate;
            }

            /**
             * Sets the value of the conversionRate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setConversionRate(String value) {
                this.conversionRate = value;
            }

            /**
             * Gets the value of the bankID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBankID() {
                return bankID;
            }

            /**
             * Sets the value of the bankID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBankID(String value) {
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
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
         *         &lt;element name="PaymentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element ref="{http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
