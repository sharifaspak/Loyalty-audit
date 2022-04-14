
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for startChargeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="startChargeResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StartChargingResponse" type="{http://www.etisalat.billingadapter.ae/1.0}StartChargingResponse" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "startChargeResponse", propOrder = {
    "startChargingResponse"
})
public class StartChargeResponse {

    @XmlElement(name = "StartChargingResponse", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected StartChargingResponse startChargingResponse;

    /**
     * Gets the value of the startChargingResponse property.
     * 
     * @return
     *     possible object is
     *     {@link StartChargingResponse }
     *     
     */
    public StartChargingResponse getStartChargingResponse() {
        return startChargingResponse;
    }

    /**
     * Sets the value of the startChargingResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link StartChargingResponse }
     *     
     */
    public void setStartChargingResponse(StartChargingResponse value) {
        this.startChargingResponse = value;
    }

}
