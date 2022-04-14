
package ae.etisalat.middleware.serviceordermgmt_cud.unsubscribeservicesresponse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.serviceordermgmt_cud.unsubscribeservicesresponse package. 
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

    private final static QName _ResponseData_QNAME = new QName("http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/UnsubscribeServicesResponse.xsd", "ResponseData");
    private final static QName _UnsubscribeServicesResponse_QNAME = new QName("http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/UnsubscribeServicesResponse.xsd", "UnsubscribeServicesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.serviceordermgmt_cud.unsubscribeservicesresponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseDataType }
     * 
     */
    public ResponseDataType createResponseDataType() {
        return new ResponseDataType();
    }

    /**
     * Create an instance of {@link UnsubscribeServicesResponseType }
     * 
     */
    public UnsubscribeServicesResponseType createUnsubscribeServicesResponseType() {
        return new UnsubscribeServicesResponseType();
    }

    /**
     * Create an instance of {@link ResponseDataType.Details }
     * 
     */
    public ResponseDataType.Details createResponseDataTypeDetails() {
        return new ResponseDataType.Details();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/UnsubscribeServicesResponse.xsd", name = "ResponseData")
    public JAXBElement<ResponseDataType> createResponseData(ResponseDataType value) {
        return new JAXBElement<ResponseDataType>(_ResponseData_QNAME, ResponseDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnsubscribeServicesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/UnsubscribeServicesResponse.xsd", name = "UnsubscribeServicesResponse")
    public JAXBElement<UnsubscribeServicesResponseType> createUnsubscribeServicesResponse(UnsubscribeServicesResponseType value) {
        return new JAXBElement<UnsubscribeServicesResponseType>(_UnsubscribeServicesResponse_QNAME, UnsubscribeServicesResponseType.class, null, value);
    }

}
