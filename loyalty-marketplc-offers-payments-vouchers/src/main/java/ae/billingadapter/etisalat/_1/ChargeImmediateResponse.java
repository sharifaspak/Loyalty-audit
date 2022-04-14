
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for chargeImmediateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="chargeImmediateResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ChargeImmediatelyResponse" type="{http://www.etisalat.billingadapter.ae/1.0}ChargeImmediatelyResponse" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chargeImmediateResponse", propOrder = {
    "chargeImmediatelyResponse"
})
public class ChargeImmediateResponse {

    @XmlElement(name = "ChargeImmediatelyResponse", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected ChargeImmediatelyResponse chargeImmediatelyResponse;

    /**
     * Gets the value of the chargeImmediatelyResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ChargeImmediatelyResponse }
     *     
     */
    public ChargeImmediatelyResponse getChargeImmediatelyResponse() {
        return chargeImmediatelyResponse;
    }

    /**
     * Sets the value of the chargeImmediatelyResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChargeImmediatelyResponse }
     *     
     */
    public void setChargeImmediatelyResponse(ChargeImmediatelyResponse value) {
        this.chargeImmediatelyResponse = value;
    }

}
