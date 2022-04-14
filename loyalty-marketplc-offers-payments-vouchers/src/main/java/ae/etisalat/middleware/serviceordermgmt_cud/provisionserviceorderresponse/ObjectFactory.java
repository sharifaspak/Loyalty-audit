
package ae.etisalat.middleware.serviceordermgmt_cud.provisionserviceorderresponse;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ae.etisalat.middleware.serviceordermgmt_cud.provisionserviceorderresponse package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ae.etisalat.middleware.serviceordermgmt_cud.provisionserviceorderresponse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderResponse }
     * 
     */
    public ProvisionServiceOrderResponse createProvisionServiceOrderResponse() {
        return new ProvisionServiceOrderResponse();
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderResponse.ResponseData }
     * 
     */
    public ProvisionServiceOrderResponse.ResponseData createProvisionServiceOrderResponseResponseData() {
        return new ProvisionServiceOrderResponse.ResponseData();
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderResponse.ResponseData.ServiceOrder }
     * 
     */
    public ProvisionServiceOrderResponse.ResponseData.ServiceOrder createProvisionServiceOrderResponseResponseDataServiceOrder() {
        return new ProvisionServiceOrderResponse.ResponseData.ServiceOrder();
    }

    /**
     * Create an instance of {@link ProvisionServiceOrderResponse.ResponseData.ServiceOrder.AccountInformation }
     * 
     */
    public ProvisionServiceOrderResponse.ResponseData.ServiceOrder.AccountInformation createProvisionServiceOrderResponseResponseDataServiceOrderAccountInformation() {
        return new ProvisionServiceOrderResponse.ResponseData.ServiceOrder.AccountInformation();
    }

}
