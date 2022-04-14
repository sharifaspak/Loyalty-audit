
package ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MiscPaymentRequest }
     * 
     */
    public MiscPaymentRequest createMiscPaymentRequest() {
        return new MiscPaymentRequest();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader }
     * 
     */
    public MiscPaymentRequest.DataHeader createMiscPaymentRequestDataHeader() {
        return new MiscPaymentRequest.DataHeader();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.MiscPaymentDetails }
     * 
     */
    public MiscPaymentRequest.DataHeader.MiscPaymentDetails createMiscPaymentRequestDataHeaderMiscPaymentDetails() {
        return new MiscPaymentRequest.DataHeader.MiscPaymentDetails();
    }

    /**
     * Create an instance of {@link AdditionalInfo }
     * 
     */
    public AdditionalInfo createAdditionalInfo() {
        return new AdditionalInfo();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.ChequeDetails }
     * 
     */
    public MiscPaymentRequest.DataHeader.ChequeDetails createMiscPaymentRequestDataHeaderChequeDetails() {
        return new MiscPaymentRequest.DataHeader.ChequeDetails();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.CardDetails }
     * 
     */
    public MiscPaymentRequest.DataHeader.CardDetails createMiscPaymentRequestDataHeaderCardDetails() {
        return new MiscPaymentRequest.DataHeader.CardDetails();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.LoyaltyDetails }
     * 
     */
    public MiscPaymentRequest.DataHeader.LoyaltyDetails createMiscPaymentRequestDataHeaderLoyaltyDetails() {
        return new MiscPaymentRequest.DataHeader.LoyaltyDetails();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.CashierShiftDetails }
     * 
     */
    public MiscPaymentRequest.DataHeader.CashierShiftDetails createMiscPaymentRequestDataHeaderCashierShiftDetails() {
        return new MiscPaymentRequest.DataHeader.CashierShiftDetails();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.OtherPaymentTypes }
     * 
     */
    public MiscPaymentRequest.DataHeader.OtherPaymentTypes createMiscPaymentRequestDataHeaderOtherPaymentTypes() {
        return new MiscPaymentRequest.DataHeader.OtherPaymentTypes();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.FranchiseePayment }
     * 
     */
    public MiscPaymentRequest.DataHeader.FranchiseePayment createMiscPaymentRequestDataHeaderFranchiseePayment() {
        return new MiscPaymentRequest.DataHeader.FranchiseePayment();
    }

    /**
     * Create an instance of {@link MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination }
     * 
     */
    public MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination createMiscPaymentRequestDataHeaderMiscPaymentDetailsCardDenomination() {
        return new MiscPaymentRequest.DataHeader.MiscPaymentDetails.CardDenomination();
    }

}
