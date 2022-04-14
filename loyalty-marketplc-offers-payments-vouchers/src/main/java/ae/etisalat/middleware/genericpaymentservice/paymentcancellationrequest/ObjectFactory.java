
package ae.etisalat.middleware.genericpaymentservice.paymentcancellationrequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.genericpaymentservice.paymentcancellationrequest package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.genericpaymentservice.paymentcancellationrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest }
     * 
     */
    public PaymentCancellationRequest createPaymentCancellationRequest() {
        return new PaymentCancellationRequest();
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest.DataHeader }
     * 
     */
    public PaymentCancellationRequest.DataHeader createPaymentCancellationRequestDataHeader() {
        return new PaymentCancellationRequest.DataHeader();
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest.DataHeader.CancelDetails }
     * 
     */
    public PaymentCancellationRequest.DataHeader.CancelDetails createPaymentCancellationRequestDataHeaderCancelDetails() {
        return new PaymentCancellationRequest.DataHeader.CancelDetails();
    }

    /**
     * Create an instance of {@link AdditionalInfo }
     * 
     */
    public AdditionalInfo createAdditionalInfo() {
        return new AdditionalInfo();
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest.DataHeader.FranchiseePayment }
     * 
     */
    public PaymentCancellationRequest.DataHeader.FranchiseePayment createPaymentCancellationRequestDataHeaderFranchiseePayment() {
        return new PaymentCancellationRequest.DataHeader.FranchiseePayment();
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails }
     * 
     */
    public PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails createPaymentCancellationRequestDataHeaderCancelDetailsCardDetails() {
        return new PaymentCancellationRequest.DataHeader.CancelDetails.CardDetails();
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails }
     * 
     */
    public PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails createPaymentCancellationRequestDataHeaderCancelDetailsLoyaltyDetails() {
        return new PaymentCancellationRequest.DataHeader.CancelDetails.LoyaltyDetails();
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails }
     * 
     */
    public PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails createPaymentCancellationRequestDataHeaderCancelDetailsWalletDetails() {
        return new PaymentCancellationRequest.DataHeader.CancelDetails.WalletDetails();
    }

    /**
     * Create an instance of {@link PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes }
     * 
     */
    public PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes createPaymentCancellationRequestDataHeaderCancelDetailsOtherPaymentTypes() {
        return new PaymentCancellationRequest.DataHeader.CancelDetails.OtherPaymentTypes();
    }

}
