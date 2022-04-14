
package ae.etisalat.middleware.serviceordermgmt_cud.subscribeservicesrequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.serviceordermgmt_cud.subscribeservicesrequest package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.serviceordermgmt_cud.subscribeservicesrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubscribeServicesRequest }
     * 
     */
    public SubscribeServicesRequest createSubscribeServicesRequest() {
        return new SubscribeServicesRequest();
    }

    /**
     * Create an instance of {@link SubscribeServicesRequest.DataHeader }
     * 
     */
    public SubscribeServicesRequest.DataHeader createSubscribeServicesRequestDataHeader() {
        return new SubscribeServicesRequest.DataHeader();
    }

    /**
     * Create an instance of {@link SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails }
     * 
     */
    public SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails createSubscribeServicesRequestDataHeaderCRBTSubscriptionDetails() {
        return new SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails();
    }

}
