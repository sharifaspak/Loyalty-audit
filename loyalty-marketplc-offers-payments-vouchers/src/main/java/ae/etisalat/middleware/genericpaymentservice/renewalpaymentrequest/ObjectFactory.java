
package ae.etisalat.middleware.genericpaymentservice.renewalpaymentrequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.genericpaymentservice.renewalpaymentrequest package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.genericpaymentservice.renewalpaymentrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest }
     * 
     */
    public RenewalPaymentRequest createRenewalPaymentRequest() {
        return new RenewalPaymentRequest();
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest.DataHeader }
     * 
     */
    public RenewalPaymentRequest.DataHeader createRenewalPaymentRequestDataHeader() {
        return new RenewalPaymentRequest.DataHeader();
    }

    /**
     * Create an instance of {@link AdditionalInfo }
     * 
     */
    public AdditionalInfo createAdditionalInfo() {
        return new AdditionalInfo();
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest.DataHeader.ChequeDetails }
     * 
     */
    public RenewalPaymentRequest.DataHeader.ChequeDetails createRenewalPaymentRequestDataHeaderChequeDetails() {
        return new RenewalPaymentRequest.DataHeader.ChequeDetails();
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest.DataHeader.CardDetails }
     * 
     */
    public RenewalPaymentRequest.DataHeader.CardDetails createRenewalPaymentRequestDataHeaderCardDetails() {
        return new RenewalPaymentRequest.DataHeader.CardDetails();
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest.DataHeader.LoyaltyDetails }
     * 
     */
    public RenewalPaymentRequest.DataHeader.LoyaltyDetails createRenewalPaymentRequestDataHeaderLoyaltyDetails() {
        return new RenewalPaymentRequest.DataHeader.LoyaltyDetails();
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest.DataHeader.BankAdviceDetails }
     * 
     */
    public RenewalPaymentRequest.DataHeader.BankAdviceDetails createRenewalPaymentRequestDataHeaderBankAdviceDetails() {
        return new RenewalPaymentRequest.DataHeader.BankAdviceDetails();
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest.DataHeader.OtherPaymentTypes }
     * 
     */
    public RenewalPaymentRequest.DataHeader.OtherPaymentTypes createRenewalPaymentRequestDataHeaderOtherPaymentTypes() {
        return new RenewalPaymentRequest.DataHeader.OtherPaymentTypes();
    }

    /**
     * Create an instance of {@link RenewalPaymentRequest.DataHeader.FranchiseePayment }
     * 
     */
    public RenewalPaymentRequest.DataHeader.FranchiseePayment createRenewalPaymentRequestDataHeaderFranchiseePayment() {
        return new RenewalPaymentRequest.DataHeader.FranchiseePayment();
    }

}
