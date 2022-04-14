
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for refundImmediate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="refundImmediate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RefundImmediateRequest" type="{http://www.etisalat.billingadapter.ae/1.0}RefundImmediatelyRequest" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refundImmediate", propOrder = {
    "refundImmediateRequest"
})
public class RefundImmediate {

    @XmlElement(name = "RefundImmediateRequest", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected RefundImmediatelyRequest refundImmediateRequest;

    /**
     * Gets the value of the refundImmediateRequest property.
     * 
     * @return
     *     possible object is
     *     {@link RefundImmediatelyRequest }
     *     
     */
    public RefundImmediatelyRequest getRefundImmediateRequest() {
        return refundImmediateRequest;
    }

    /**
     * Sets the value of the refundImmediateRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefundImmediatelyRequest }
     *     
     */
    public void setRefundImmediateRequest(RefundImmediatelyRequest value) {
        this.refundImmediateRequest = value;
    }

}
