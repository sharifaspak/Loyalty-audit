
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for startCharge complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="startCharge"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StartChargeRequest" type="{http://www.etisalat.billingadapter.ae/1.0}StartChargingRequest" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "startCharge", propOrder = {
    "startChargeRequest"
})
public class StartCharge {

    @XmlElement(name = "StartChargeRequest", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected StartChargingRequest startChargeRequest;

    /**
     * Gets the value of the startChargeRequest property.
     * 
     * @return
     *     possible object is
     *     {@link StartChargingRequest }
     *     
     */
    public StartChargingRequest getStartChargeRequest() {
        return startChargeRequest;
    }

    /**
     * Sets the value of the startChargeRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link StartChargingRequest }
     *     
     */
    public void setStartChargeRequest(StartChargingRequest value) {
        this.startChargeRequest = value;
    }

}
