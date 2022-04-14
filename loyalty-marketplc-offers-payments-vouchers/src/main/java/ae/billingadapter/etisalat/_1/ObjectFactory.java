
package ae.billingadapter.etisalat._1;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.billingadapter.etisalat._1 package. 
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

    private final static QName _ChargeImmediate_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "chargeImmediate");
    private final static QName _ChargeImmediateResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "chargeImmediateResponse");
    private final static QName _ContinueCharge_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "continueCharge");
    private final static QName _ContinueChargeResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "continueChargeResponse");
    private final static QName _GetIP_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "getIP");
    private final static QName _GetIPResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "getIPResponse");
    private final static QName _GetUserName_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "getUserName");
    private final static QName _GetUserNameResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "getUserNameResponse");
    private final static QName _IsAmountInRange_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInRange");
    private final static QName _IsAmountInRangeResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInRangeResponse");
    private final static QName _IsAmountInRangeSpecialCase_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInRangeSpecialCase");
    private final static QName _IsAmountInRangeSpecialCaseResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInRangeSpecialCaseResponse");
    private final static QName _IsAmountInUserRange_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInUserRange");
    private final static QName _IsAmountInUserRangeResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInUserRangeResponse");
    private final static QName _IsAmountInUserRangeSpecialCase_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInUserRangeSpecialCase");
    private final static QName _IsAmountInUserRangeSpecialCaseResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "isAmountInUserRangeSpecialCaseResponse");
    private final static QName _RefundImmediate_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "refundImmediate");
    private final static QName _RefundImmediateResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "refundImmediateResponse");
    private final static QName _StartCharge_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "startCharge");
    private final static QName _StartChargeResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "startChargeResponse");
    private final static QName _StopCharge_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "stopCharge");
    private final static QName _StopChargeResponse_QNAME = new QName("http://www.etisalat.billingadapter.ae/1.0", "stopChargeResponse");
    private final static QName _AdditionInfoContentType_QNAME = new QName("", "contentType");
    private final static QName _AdditionInfoChannel_QNAME = new QName("", "channel");
    private final static QName _AdditionInfoBarCode_QNAME = new QName("", "barCode");
    private final static QName _AdditionInfoRevenuePercent_QNAME = new QName("", "revenuePercent");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.billingadapter.etisalat._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ChargeImmediate }
     * 
     */
    public ChargeImmediate createChargeImmediate() {
        return new ChargeImmediate();
    }

    /**
     * Create an instance of {@link ChargeImmediateResponse }
     * 
     */
    public ChargeImmediateResponse createChargeImmediateResponse() {
        return new ChargeImmediateResponse();
    }

    /**
     * Create an instance of {@link ContinueCharge }
     * 
     */
    public ContinueCharge createContinueCharge() {
        return new ContinueCharge();
    }

    /**
     * Create an instance of {@link ContinueChargeResponse }
     * 
     */
    public ContinueChargeResponse createContinueChargeResponse() {
        return new ContinueChargeResponse();
    }

    /**
     * Create an instance of {@link GetIP }
     * 
     */
    public GetIP createGetIP() {
        return new GetIP();
    }

    /**
     * Create an instance of {@link GetIPResponse }
     * 
     */
    public GetIPResponse createGetIPResponse() {
        return new GetIPResponse();
    }

    /**
     * Create an instance of {@link GetUserName }
     * 
     */
    public GetUserName createGetUserName() {
        return new GetUserName();
    }

    /**
     * Create an instance of {@link GetUserNameResponse }
     * 
     */
    public GetUserNameResponse createGetUserNameResponse() {
        return new GetUserNameResponse();
    }

    /**
     * Create an instance of {@link IsAmountInRange }
     * 
     */
    public IsAmountInRange createIsAmountInRange() {
        return new IsAmountInRange();
    }

    /**
     * Create an instance of {@link IsAmountInRangeResponse }
     * 
     */
    public IsAmountInRangeResponse createIsAmountInRangeResponse() {
        return new IsAmountInRangeResponse();
    }

    /**
     * Create an instance of {@link IsAmountInRangeSpecialCase }
     * 
     */
    public IsAmountInRangeSpecialCase createIsAmountInRangeSpecialCase() {
        return new IsAmountInRangeSpecialCase();
    }

    /**
     * Create an instance of {@link IsAmountInRangeSpecialCaseResponse }
     * 
     */
    public IsAmountInRangeSpecialCaseResponse createIsAmountInRangeSpecialCaseResponse() {
        return new IsAmountInRangeSpecialCaseResponse();
    }

    /**
     * Create an instance of {@link IsAmountInUserRange }
     * 
     */
    public IsAmountInUserRange createIsAmountInUserRange() {
        return new IsAmountInUserRange();
    }

    /**
     * Create an instance of {@link IsAmountInUserRangeResponse }
     * 
     */
    public IsAmountInUserRangeResponse createIsAmountInUserRangeResponse() {
        return new IsAmountInUserRangeResponse();
    }

    /**
     * Create an instance of {@link IsAmountInUserRangeSpecialCase }
     * 
     */
    public IsAmountInUserRangeSpecialCase createIsAmountInUserRangeSpecialCase() {
        return new IsAmountInUserRangeSpecialCase();
    }

    /**
     * Create an instance of {@link IsAmountInUserRangeSpecialCaseResponse }
     * 
     */
    public IsAmountInUserRangeSpecialCaseResponse createIsAmountInUserRangeSpecialCaseResponse() {
        return new IsAmountInUserRangeSpecialCaseResponse();
    }

    /**
     * Create an instance of {@link RefundImmediate }
     * 
     */
    public RefundImmediate createRefundImmediate() {
        return new RefundImmediate();
    }

    /**
     * Create an instance of {@link RefundImmediateResponse }
     * 
     */
    public RefundImmediateResponse createRefundImmediateResponse() {
        return new RefundImmediateResponse();
    }

    /**
     * Create an instance of {@link StartCharge }
     * 
     */
    public StartCharge createStartCharge() {
        return new StartCharge();
    }

    /**
     * Create an instance of {@link StartChargeResponse }
     * 
     */
    public StartChargeResponse createStartChargeResponse() {
        return new StartChargeResponse();
    }

    /**
     * Create an instance of {@link StopCharge }
     * 
     */
    public StopCharge createStopCharge() {
        return new StopCharge();
    }

    /**
     * Create an instance of {@link StopChargeResponse }
     * 
     */
    public StopChargeResponse createStopChargeResponse() {
        return new StopChargeResponse();
    }

    /**
     * Create an instance of {@link ChargeImmediateRequest }
     * 
     */
    public ChargeImmediateRequest createChargeImmediateRequest() {
        return new ChargeImmediateRequest();
    }

    /**
     * Create an instance of {@link AdditionInfo }
     * 
     */
    public AdditionInfo createAdditionInfo() {
        return new AdditionInfo();
    }

    /**
     * Create an instance of {@link ExtraPrams }
     * 
     */
    public ExtraPrams createExtraPrams() {
        return new ExtraPrams();
    }

    /**
     * Create an instance of {@link ChargeImmediatelyResponse }
     * 
     */
    public ChargeImmediatelyResponse createChargeImmediatelyResponse() {
        return new ChargeImmediatelyResponse();
    }

    /**
     * Create an instance of {@link AdditionResponse }
     * 
     */
    public AdditionResponse createAdditionResponse() {
        return new AdditionResponse();
    }

    /**
     * Create an instance of {@link ContinueChargeRequest }
     * 
     */
    public ContinueChargeRequest createContinueChargeRequest() {
        return new ContinueChargeRequest();
    }

    /**
     * Create an instance of {@link ContinueChargingResponse }
     * 
     */
    public ContinueChargingResponse createContinueChargingResponse() {
        return new ContinueChargingResponse();
    }

    /**
     * Create an instance of {@link StopChargingRequest }
     * 
     */
    public StopChargingRequest createStopChargingRequest() {
        return new StopChargingRequest();
    }

    /**
     * Create an instance of {@link StopChargingResponse }
     * 
     */
    public StopChargingResponse createStopChargingResponse() {
        return new StopChargingResponse();
    }

    /**
     * Create an instance of {@link StartChargingRequest }
     * 
     */
    public StartChargingRequest createStartChargingRequest() {
        return new StartChargingRequest();
    }

    /**
     * Create an instance of {@link StartChargingResponse }
     * 
     */
    public StartChargingResponse createStartChargingResponse() {
        return new StartChargingResponse();
    }

    /**
     * Create an instance of {@link RefundImmediatelyRequest }
     * 
     */
    public RefundImmediatelyRequest createRefundImmediatelyRequest() {
        return new RefundImmediatelyRequest();
    }

    /**
     * Create an instance of {@link RefundImmediatelyResponse }
     * 
     */
    public RefundImmediatelyResponse createRefundImmediatelyResponse() {
        return new RefundImmediatelyResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeImmediate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "chargeImmediate")
    public JAXBElement<ChargeImmediate> createChargeImmediate(ChargeImmediate value) {
        return new JAXBElement<ChargeImmediate>(_ChargeImmediate_QNAME, ChargeImmediate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeImmediateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "chargeImmediateResponse")
    public JAXBElement<ChargeImmediateResponse> createChargeImmediateResponse(ChargeImmediateResponse value) {
        return new JAXBElement<ChargeImmediateResponse>(_ChargeImmediateResponse_QNAME, ChargeImmediateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContinueCharge }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "continueCharge")
    public JAXBElement<ContinueCharge> createContinueCharge(ContinueCharge value) {
        return new JAXBElement<ContinueCharge>(_ContinueCharge_QNAME, ContinueCharge.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContinueChargeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "continueChargeResponse")
    public JAXBElement<ContinueChargeResponse> createContinueChargeResponse(ContinueChargeResponse value) {
        return new JAXBElement<ContinueChargeResponse>(_ContinueChargeResponse_QNAME, ContinueChargeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "getIP")
    public JAXBElement<GetIP> createGetIP(GetIP value) {
        return new JAXBElement<GetIP>(_GetIP_QNAME, GetIP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIPResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "getIPResponse")
    public JAXBElement<GetIPResponse> createGetIPResponse(GetIPResponse value) {
        return new JAXBElement<GetIPResponse>(_GetIPResponse_QNAME, GetIPResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "getUserName")
    public JAXBElement<GetUserName> createGetUserName(GetUserName value) {
        return new JAXBElement<GetUserName>(_GetUserName_QNAME, GetUserName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "getUserNameResponse")
    public JAXBElement<GetUserNameResponse> createGetUserNameResponse(GetUserNameResponse value) {
        return new JAXBElement<GetUserNameResponse>(_GetUserNameResponse_QNAME, GetUserNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInRange }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInRange")
    public JAXBElement<IsAmountInRange> createIsAmountInRange(IsAmountInRange value) {
        return new JAXBElement<IsAmountInRange>(_IsAmountInRange_QNAME, IsAmountInRange.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInRangeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInRangeResponse")
    public JAXBElement<IsAmountInRangeResponse> createIsAmountInRangeResponse(IsAmountInRangeResponse value) {
        return new JAXBElement<IsAmountInRangeResponse>(_IsAmountInRangeResponse_QNAME, IsAmountInRangeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInRangeSpecialCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInRangeSpecialCase")
    public JAXBElement<IsAmountInRangeSpecialCase> createIsAmountInRangeSpecialCase(IsAmountInRangeSpecialCase value) {
        return new JAXBElement<IsAmountInRangeSpecialCase>(_IsAmountInRangeSpecialCase_QNAME, IsAmountInRangeSpecialCase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInRangeSpecialCaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInRangeSpecialCaseResponse")
    public JAXBElement<IsAmountInRangeSpecialCaseResponse> createIsAmountInRangeSpecialCaseResponse(IsAmountInRangeSpecialCaseResponse value) {
        return new JAXBElement<IsAmountInRangeSpecialCaseResponse>(_IsAmountInRangeSpecialCaseResponse_QNAME, IsAmountInRangeSpecialCaseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInUserRange }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInUserRange")
    public JAXBElement<IsAmountInUserRange> createIsAmountInUserRange(IsAmountInUserRange value) {
        return new JAXBElement<IsAmountInUserRange>(_IsAmountInUserRange_QNAME, IsAmountInUserRange.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInUserRangeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInUserRangeResponse")
    public JAXBElement<IsAmountInUserRangeResponse> createIsAmountInUserRangeResponse(IsAmountInUserRangeResponse value) {
        return new JAXBElement<IsAmountInUserRangeResponse>(_IsAmountInUserRangeResponse_QNAME, IsAmountInUserRangeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInUserRangeSpecialCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInUserRangeSpecialCase")
    public JAXBElement<IsAmountInUserRangeSpecialCase> createIsAmountInUserRangeSpecialCase(IsAmountInUserRangeSpecialCase value) {
        return new JAXBElement<IsAmountInUserRangeSpecialCase>(_IsAmountInUserRangeSpecialCase_QNAME, IsAmountInUserRangeSpecialCase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAmountInUserRangeSpecialCaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "isAmountInUserRangeSpecialCaseResponse")
    public JAXBElement<IsAmountInUserRangeSpecialCaseResponse> createIsAmountInUserRangeSpecialCaseResponse(IsAmountInUserRangeSpecialCaseResponse value) {
        return new JAXBElement<IsAmountInUserRangeSpecialCaseResponse>(_IsAmountInUserRangeSpecialCaseResponse_QNAME, IsAmountInUserRangeSpecialCaseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundImmediate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "refundImmediate")
    public JAXBElement<RefundImmediate> createRefundImmediate(RefundImmediate value) {
        return new JAXBElement<RefundImmediate>(_RefundImmediate_QNAME, RefundImmediate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundImmediateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "refundImmediateResponse")
    public JAXBElement<RefundImmediateResponse> createRefundImmediateResponse(RefundImmediateResponse value) {
        return new JAXBElement<RefundImmediateResponse>(_RefundImmediateResponse_QNAME, RefundImmediateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartCharge }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "startCharge")
    public JAXBElement<StartCharge> createStartCharge(StartCharge value) {
        return new JAXBElement<StartCharge>(_StartCharge_QNAME, StartCharge.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartChargeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "startChargeResponse")
    public JAXBElement<StartChargeResponse> createStartChargeResponse(StartChargeResponse value) {
        return new JAXBElement<StartChargeResponse>(_StartChargeResponse_QNAME, StartChargeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopCharge }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "stopCharge")
    public JAXBElement<StopCharge> createStopCharge(StopCharge value) {
        return new JAXBElement<StopCharge>(_StopCharge_QNAME, StopCharge.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopChargeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.billingadapter.ae/1.0", name = "stopChargeResponse")
    public JAXBElement<StopChargeResponse> createStopChargeResponse(StopChargeResponse value) {
        return new JAXBElement<StopChargeResponse>(_StopChargeResponse_QNAME, StopChargeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "contentType", scope = AdditionInfo.class)
    public JAXBElement<String> createAdditionInfoContentType(String value) {
        return new JAXBElement<String>(_AdditionInfoContentType_QNAME, String.class, AdditionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "channel", scope = AdditionInfo.class)
    public JAXBElement<String> createAdditionInfoChannel(String value) {
        return new JAXBElement<String>(_AdditionInfoChannel_QNAME, String.class, AdditionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "barCode", scope = AdditionInfo.class)
    public JAXBElement<BigInteger> createAdditionInfoBarCode(BigInteger value) {
        return new JAXBElement<BigInteger>(_AdditionInfoBarCode_QNAME, BigInteger.class, AdditionInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "revenuePercent", scope = AdditionInfo.class)
    public JAXBElement<BigInteger> createAdditionInfoRevenuePercent(BigInteger value) {
        return new JAXBElement<BigInteger>(_AdditionInfoRevenuePercent_QNAME, BigInteger.class, AdditionInfo.class, value);
    }

}
