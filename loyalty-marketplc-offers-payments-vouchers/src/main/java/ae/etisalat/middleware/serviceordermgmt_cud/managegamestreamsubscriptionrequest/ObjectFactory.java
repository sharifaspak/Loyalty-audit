
package ae.etisalat.middleware.serviceordermgmt_cud.managegamestreamsubscriptionrequest;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.serviceordermgmt_cud.managegamestreamsubscriptionrequest package. 
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

    private final static QName _DataHeader_QNAME = new QName("http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/ManageGameStreamSubscriptionRequest.xsd", "DataHeader");
    private final static QName _ManageGameStreamSubscriptionRequest_QNAME = new QName("http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/ManageGameStreamSubscriptionRequest.xsd", "ManageGameStreamSubscriptionRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.serviceordermgmt_cud.managegamestreamsubscriptionrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataHeaderType }
     * 
     */
    public DataHeaderType createDataHeaderType() {
        return new DataHeaderType();
    }

    /**
     * Create an instance of {@link ManageGameStreamSubscriptionRequestType }
     * 
     */
    public ManageGameStreamSubscriptionRequestType createManageGameStreamSubscriptionRequestType() {
        return new ManageGameStreamSubscriptionRequestType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataHeaderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/ManageGameStreamSubscriptionRequest.xsd", name = "DataHeader")
    public JAXBElement<DataHeaderType> createDataHeader(DataHeaderType value) {
        return new JAXBElement<DataHeaderType>(_DataHeader_QNAME, DataHeaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageGameStreamSubscriptionRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/ManageGameStreamSubscriptionRequest.xsd", name = "ManageGameStreamSubscriptionRequest")
    public JAXBElement<ManageGameStreamSubscriptionRequestType> createManageGameStreamSubscriptionRequest(ManageGameStreamSubscriptionRequestType value) {
        return new JAXBElement<ManageGameStreamSubscriptionRequestType>(_ManageGameStreamSubscriptionRequest_QNAME, ManageGameStreamSubscriptionRequestType.class, null, value);
    }

}
