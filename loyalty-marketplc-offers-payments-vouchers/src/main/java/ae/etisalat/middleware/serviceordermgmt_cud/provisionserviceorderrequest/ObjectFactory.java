
package ae.etisalat.middleware.serviceordermgmt_cud.provisionserviceorderrequest;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.serviceordermgmt_cud.provisionserviceorderrequest package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.serviceordermgmt_cud.provisionserviceorderrequest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderRequest }
     * 
     */
    public ProvisionServiceOrderRequest createProvisionServiceOrderRequest() {
        return new ProvisionServiceOrderRequest();
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderRequest.DataHeader }
     * 
     */
    public ProvisionServiceOrderRequest.DataHeader createProvisionServiceOrderRequestDataHeader() {
        return new ProvisionServiceOrderRequest.DataHeader();
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderRequest.DataHeader.ServiceOrder }
     * 
     */
    public ProvisionServiceOrderRequest.DataHeader.ServiceOrder createProvisionServiceOrderRequestDataHeaderServiceOrder() {
        return new ProvisionServiceOrderRequest.DataHeader.ServiceOrder();
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderRequest.DataHeader.ServiceOrder.AccountInformation }
     * 
     */
    public ProvisionServiceOrderRequest.DataHeader.ServiceOrder.AccountInformation createProvisionServiceOrderRequestDataHeaderServiceOrderAccountInformation() {
        return new ProvisionServiceOrderRequest.DataHeader.ServiceOrder.AccountInformation();
    }

}
