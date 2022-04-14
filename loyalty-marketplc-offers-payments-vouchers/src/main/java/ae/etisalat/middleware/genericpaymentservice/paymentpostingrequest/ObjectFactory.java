
package ae.etisalat.middleware.genericpaymentservice.paymentpostingrequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.genericpaymentservice.paymentpostingrequest package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.genericpaymentservice.paymentpostingrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaymentPostingRequest }
     * 
     */
    public PaymentPostingRequest createPaymentPostingRequest() {
        return new PaymentPostingRequest();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader }
     * 
     */
    public PaymentPostingRequest.DataHeader createPaymentPostingRequestDataHeader() {
        return new PaymentPostingRequest.DataHeader();
    }

    /**
     * Create an instance of {@link AdditionalInfo }
     * 
     */
    public AdditionalInfo createAdditionalInfo() {
        return new AdditionalInfo();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.PaymentDetails }
     * 
     */
    public PaymentPostingRequest.DataHeader.PaymentDetails createPaymentPostingRequestDataHeaderPaymentDetails() {
        return new PaymentPostingRequest.DataHeader.PaymentDetails();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.ChequeDetails }
     * 
     */
    public PaymentPostingRequest.DataHeader.ChequeDetails createPaymentPostingRequestDataHeaderChequeDetails() {
        return new PaymentPostingRequest.DataHeader.ChequeDetails();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.CardDetails }
     * 
     */
    public PaymentPostingRequest.DataHeader.CardDetails createPaymentPostingRequestDataHeaderCardDetails() {
        return new PaymentPostingRequest.DataHeader.CardDetails();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.LoyaltyDetails }
     * 
     */
    public PaymentPostingRequest.DataHeader.LoyaltyDetails createPaymentPostingRequestDataHeaderLoyaltyDetails() {
        return new PaymentPostingRequest.DataHeader.LoyaltyDetails();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.BankAdviceDetails }
     * 
     */
    public PaymentPostingRequest.DataHeader.BankAdviceDetails createPaymentPostingRequestDataHeaderBankAdviceDetails() {
        return new PaymentPostingRequest.DataHeader.BankAdviceDetails();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.OtherPaymentTypes }
     * 
     */
    public PaymentPostingRequest.DataHeader.OtherPaymentTypes createPaymentPostingRequestDataHeaderOtherPaymentTypes() {
        return new PaymentPostingRequest.DataHeader.OtherPaymentTypes();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.FranchiseePayment }
     * 
     */
    public PaymentPostingRequest.DataHeader.FranchiseePayment createPaymentPostingRequestDataHeaderFranchiseePayment() {
        return new PaymentPostingRequest.DataHeader.FranchiseePayment();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.WalletPayment }
     * 
     */
    public PaymentPostingRequest.DataHeader.WalletPayment createPaymentPostingRequestDataHeaderWalletPayment() {
        return new PaymentPostingRequest.DataHeader.WalletPayment();
    }

    /**
     * Create an instance of {@link PaymentPostingRequest.DataHeader.CashPayment }
     * 
     */
    public PaymentPostingRequest.DataHeader.CashPayment createPaymentPostingRequestDataHeaderCashPayment() {
        return new PaymentPostingRequest.DataHeader.CashPayment();
    }

}
