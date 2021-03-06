package com.responsetek.rt;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.4
 * 2021-06-28T11:31:45.443+05:30
 * Generated source version: 3.2.4
 *
 */
@WebServiceClient(name = "RTInvite",
                  wsdlLocation = "file:/C:/Etisalat/workspace/FoodSubscription/loyalty-marketplc/loyalty-marketplc-offers-payments-vouchers/src/main/resources/wsdl/RtInvite.xml",
                  targetNamespace = "http://rt.responsetek.com/")
public class RTInvite extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://rt.responsetek.com/", "RTInvite");
    public final static QName RTInviteHttpPost = new QName("http://rt.responsetek.com/", "RTInviteHttpPost");
    public final static QName RTInviteSoap = new QName("http://rt.responsetek.com/", "RTInviteSoap");
    public final static QName RTInviteSoap12 = new QName("http://rt.responsetek.com/", "RTInviteSoap12");
    public final static QName RTInviteHttpGet = new QName("http://rt.responsetek.com/", "RTInviteHttpGet");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Etisalat/workspace/FoodSubscription/loyalty-marketplc/loyalty-marketplc-offers-payments-vouchers/src/main/resources/wsdl/RtInvite.xml");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RTInvite.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Etisalat/workspace/FoodSubscription/loyalty-marketplc/loyalty-marketplc-offers-payments-vouchers/src/main/resources/wsdl/RtInvite.xml");
        }
        WSDL_LOCATION = url;
    }

    public RTInvite(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RTInvite(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RTInvite() {
        super(WSDL_LOCATION, SERVICE);
    }





    /**
     *
     * @return
     *     returns RTInviteHttpPost
     */
    @WebEndpoint(name = "RTInviteHttpPost")
    public RTInviteHttpPost getRTInviteHttpPost() {
        return super.getPort(RTInviteHttpPost, RTInviteHttpPost.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RTInviteHttpPost
     */
    @WebEndpoint(name = "RTInviteHttpPost")
    public RTInviteHttpPost getRTInviteHttpPost(WebServiceFeature... features) {
        return super.getPort(RTInviteHttpPost, RTInviteHttpPost.class, features);
    }


    /**
     *
     * @return
     *     returns RTInviteSoap
     */
    @WebEndpoint(name = "RTInviteSoap")
    public RTInviteSoap getRTInviteSoap() {
        return super.getPort(RTInviteSoap, RTInviteSoap.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RTInviteSoap
     */
    @WebEndpoint(name = "RTInviteSoap")
    public RTInviteSoap getRTInviteSoap(WebServiceFeature... features) {
        return super.getPort(RTInviteSoap, RTInviteSoap.class, features);
    }


    /**
     *
     * @return
     *     returns RTInviteSoap
     */
    @WebEndpoint(name = "RTInviteSoap12")
    public RTInviteSoap getRTInviteSoap12() {
        return super.getPort(RTInviteSoap12, RTInviteSoap.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RTInviteSoap
     */
    @WebEndpoint(name = "RTInviteSoap12")
    public RTInviteSoap getRTInviteSoap12(WebServiceFeature... features) {
        return super.getPort(RTInviteSoap12, RTInviteSoap.class, features);
    }


    /**
     *
     * @return
     *     returns RTInviteHttpGet
     */
    @WebEndpoint(name = "RTInviteHttpGet")
    public RTInviteHttpGet getRTInviteHttpGet() {
        return super.getPort(RTInviteHttpGet, RTInviteHttpGet.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RTInviteHttpGet
     */
    @WebEndpoint(name = "RTInviteHttpGet")
    public RTInviteHttpGet getRTInviteHttpGet(WebServiceFeature... features) {
        return super.getPort(RTInviteHttpGet, RTInviteHttpGet.class, features);
    }

}
