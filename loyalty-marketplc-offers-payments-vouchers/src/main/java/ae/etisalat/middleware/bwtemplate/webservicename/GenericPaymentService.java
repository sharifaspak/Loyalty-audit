package ae.etisalat.middleware.bwtemplate.webservicename;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.2.4
 * 2019-11-11T17:04:12.088+04:00
 * Generated source version: 3.2.4
 *
 */
@WebService(targetNamespace = "http://www.etisalat.ae/Middleware/BWTemplate/WebserviceName", name = "GenericPaymentService")
@XmlSeeAlso({ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.paymentpostingrequest.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.paymentcancellationrequest.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.renewalpaymentresponse.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.renewalpaymentrequest.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.adjustmentpostingrequest.ObjectFactory.class, ae.etisalat.middleware.sharedresources.common.ackmessage.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.paymentcancellationresponse.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.adjustmentpostingresponse.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.miscpaymentresponse.ObjectFactory.class, ae.etisalat.middleware.sharedresources.common.applicationheader.ObjectFactory.class, ae.etisalat.middleware.sharedresources.common.common.ObjectFactory.class, ae.etisalat.middleware.genericpaymentservice.paymentpostingresponse.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface GenericPaymentService {

    @WebMethod(operationName = "OpPaymentCancellation", action = "/Middleware/GenericPaymentService/PaymentCancellation")
    @WebResult(name = "PaymentCancellationResponse", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationResponse.xsd", partName = "PaymentCancellationResponse")
    public ae.etisalat.middleware.genericpaymentservice.paymentcancellationresponse.PaymentCancellationResponse opPaymentCancellation(
        @WebParam(partName = "PaymentCancellationRequest", name = "PaymentCancellationRequest", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentCancellationRequest.xsd")
        ae.etisalat.middleware.genericpaymentservice.paymentcancellationrequest.PaymentCancellationRequest paymentCancellationRequest
    ) throws PaymentFault;

    @WebMethod(action = "/Middleware/GenericPaymentService/PaymentPosting")
    @WebResult(name = "PaymentPostingResponse", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingResponse.xsd", partName = "PaymentPostingResponse")
    public ae.etisalat.middleware.genericpaymentservice.paymentpostingresponse.PaymentPostingResponse opPaymentPosting(
        @WebParam(partName = "PaymentPostingRequest", name = "PaymentPostingRequest", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/PaymentPostingRequest.xsd")
        ae.etisalat.middleware.genericpaymentservice.paymentpostingrequest.PaymentPostingRequest paymentPostingRequest
    ) throws PaymentFault;

    @WebMethod(action = "/Middleware/GenericPaymentService/MiscPayment")
    @WebResult(name = "MiscPaymentResponse", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentResponse.xsd", partName = "MiscPaymentResponse")
    public ae.etisalat.middleware.genericpaymentservice.miscpaymentresponse.MiscPaymentResponse opMiscPayment(
        @WebParam(partName = "MiscPaymentRequest", name = "MiscPaymentRequest", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/MiscPaymentRequest.xsd")
        ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest.MiscPaymentRequest miscPaymentRequest
    ) throws PaymentFault;

    @WebMethod(action = "/Middleware/GenericPaymentService/RenewalPayment")
    @WebResult(name = "RenewalPaymentResponse", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentResponse.xsd", partName = "RenewalPaymentResponse")
    public ae.etisalat.middleware.genericpaymentservice.renewalpaymentresponse.RenewalPaymentResponse opRenewalPayment(
        @WebParam(partName = "RenewalPaymentRequest", name = "RenewalPaymentRequest", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/RenewalPaymentRequest.xsd")
        ae.etisalat.middleware.genericpaymentservice.renewalpaymentrequest.RenewalPaymentRequest renewalPaymentRequest
    ) throws PaymentFault;

    @WebMethod(operationName = "OpAdjustmentPosting", action = "/Middleware/GenericPaymentService/AdjustmentPosting")
    @WebResult(name = "AdjustmentPostingResponse", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/AdjustmentPostingResponse.xsd", partName = "AdjustmentPostingResponse")
    public ae.etisalat.middleware.genericpaymentservice.adjustmentpostingresponse.AdjustmentPostingResponse opAdjustmentPosting(
        @WebParam(partName = "AdjustmentPostingRequest", name = "AdjustmentPostingRequest", targetNamespace = "http://www.etisalat.ae/Middleware/GenericPaymentService/AdjustmentPostingRequest.xsd")
        ae.etisalat.middleware.genericpaymentservice.adjustmentpostingrequest.AdjustmentPostingRequest adjustmentPostingRequest
    ) throws PaymentFault;
}
