
package ae.etisalat.middleware.bwtemplate.webservicename;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.2.4
 * 2019-11-11T17:04:11.996+04:00
 * Generated source version: 3.2.4
 */

@WebFault(name = "AckMessage", targetNamespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/AckMessage.xsd")
public class PaymentFault extends Exception {

    private ae.etisalat.middleware.sharedresources.common.ackmessage.AckMessage ackMessage;

    public PaymentFault() {
        super();
    }

    public PaymentFault(String message) {
        super(message);
    }

    public PaymentFault(String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public PaymentFault(String message, ae.etisalat.middleware.sharedresources.common.ackmessage.AckMessage ackMessage) {
        super(message);
        this.ackMessage = ackMessage;
    }

    public PaymentFault(String message, ae.etisalat.middleware.sharedresources.common.ackmessage.AckMessage ackMessage, java.lang.Throwable cause) {
        super(message, cause);
        this.ackMessage = ackMessage;
    }

    public ae.etisalat.middleware.sharedresources.common.ackmessage.AckMessage getFaultInfo() {
        return this.ackMessage;
    }
}
