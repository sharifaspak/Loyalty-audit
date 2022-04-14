
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for continueCharge complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="continueCharge"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ContinueChargeRequest" type="{http://www.etisalat.billingadapter.ae/1.0}ContinueChargeRequest" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "continueCharge", propOrder = {
    "continueChargeRequest"
})
public class ContinueCharge {

    @XmlElement(name = "ContinueChargeRequest", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected ContinueChargeRequest continueChargeRequest;

    /**
     * Gets the value of the continueChargeRequest property.
     * 
     * @return
     *     possible object is
     *     {@link ContinueChargeRequest }
     *     
     */
    public ContinueChargeRequest getContinueChargeRequest() {
        return continueChargeRequest;
    }

    /**
     * Sets the value of the continueChargeRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContinueChargeRequest }
     *     
     */
    public void setContinueChargeRequest(ContinueChargeRequest value) {
        this.continueChargeRequest = value;
    }

}
