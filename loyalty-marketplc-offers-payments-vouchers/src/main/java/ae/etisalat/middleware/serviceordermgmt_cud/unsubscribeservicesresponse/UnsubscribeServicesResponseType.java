
package ae.etisalat.middleware.serviceordermgmt_cud.unsubscribeservicesresponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.ackmessage.AckMessage;


/**
 * <p>Java class for UnsubscribeServicesResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnsubscribeServicesResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/ServiceOrderMgmt_CUD/UnsubscribeServicesResponse.xsd}ResponseData" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/AckMessage.xsd}AckMessage"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnsubscribeServicesResponseType", propOrder = {
    "responseData",
    "ackMessage"
})
public class UnsubscribeServicesResponseType {

    @XmlElement(name = "ResponseData")
    protected ResponseDataType responseData;
    @XmlElement(name = "AckMessage", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/AckMessage.xsd", required = true)
    protected AckMessage ackMessage;

    /**
     * Gets the value of the responseData property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseDataType }
     *     
     */
    public ResponseDataType getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseDataType }
     *     
     */
    public void setResponseData(ResponseDataType value) {
        this.responseData = value;
    }

    /**
     * Gets the value of the ackMessage property.
     * 
     * @return
     *     possible object is
     *     {@link AckMessage }
     *     
     */
    public AckMessage getAckMessage() {
        return ackMessage;
    }

    /**
     * Sets the value of the ackMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link AckMessage }
     *     
     */
    public void setAckMessage(AckMessage value) {
        this.ackMessage = value;
    }

}
