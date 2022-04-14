
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for stopChargeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stopChargeResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StopChargingResponse" type="{http://www.etisalat.billingadapter.ae/1.0}StopChargingResponse" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stopChargeResponse", propOrder = {
    "stopChargingResponse"
})
public class StopChargeResponse {

    @XmlElement(name = "StopChargingResponse", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected StopChargingResponse stopChargingResponse;

    /**
     * Gets the value of the stopChargingResponse property.
     * 
     * @return
     *     possible object is
     *     {@link StopChargingResponse }
     *     
     */
    public StopChargingResponse getStopChargingResponse() {
        return stopChargingResponse;
    }

    /**
     * Sets the value of the stopChargingResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link StopChargingResponse }
     *     
     */
    public void setStopChargingResponse(StopChargingResponse value) {
        this.stopChargingResponse = value;
    }

}
