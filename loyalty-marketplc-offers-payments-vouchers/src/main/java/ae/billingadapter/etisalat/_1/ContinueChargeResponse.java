
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for continueChargeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="continueChargeResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ContinueChargingResponse" type="{http://www.etisalat.billingadapter.ae/1.0}ContinueChargingResponse" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "continueChargeResponse", propOrder = {
    "continueChargingResponse"
})
public class ContinueChargeResponse {

    @XmlElement(name = "ContinueChargingResponse", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected ContinueChargingResponse continueChargingResponse;

    /**
     * Gets the value of the continueChargingResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ContinueChargingResponse }
     *     
     */
    public ContinueChargingResponse getContinueChargingResponse() {
        return continueChargingResponse;
    }

    /**
     * Sets the value of the continueChargingResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContinueChargingResponse }
     *     
     */
    public void setContinueChargingResponse(ContinueChargingResponse value) {
        this.continueChargingResponse = value;
    }

}
