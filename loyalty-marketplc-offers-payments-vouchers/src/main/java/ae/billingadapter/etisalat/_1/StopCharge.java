
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for stopCharge complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stopCharge"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StopChargRequest" type="{http://www.etisalat.billingadapter.ae/1.0}StopChargingRequest" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stopCharge", propOrder = {
    "stopChargRequest"
})
public class StopCharge {

    @XmlElement(name = "StopChargRequest", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected StopChargingRequest stopChargRequest;

    /**
     * Gets the value of the stopChargRequest property.
     * 
     * @return
     *     possible object is
     *     {@link StopChargingRequest }
     *     
     */
    public StopChargingRequest getStopChargRequest() {
        return stopChargRequest;
    }

    /**
     * Sets the value of the stopChargRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link StopChargingRequest }
     *     
     */
    public void setStopChargRequest(StopChargingRequest value) {
        this.stopChargRequest = value;
    }

}
