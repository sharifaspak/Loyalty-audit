
package ae.billingadapter.etisalat._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for refundImmediateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="refundImmediateResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RefundImmediatelyResponse" type="{http://www.etisalat.billingadapter.ae/1.0}RefundImmediatelyResponse" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refundImmediateResponse", propOrder = {
    "refundImmediatelyResponse"
})
public class RefundImmediateResponse {

    @XmlElement(name = "RefundImmediatelyResponse", namespace = "http://www.etisalat.billingadapter.ae/1.0")
    protected RefundImmediatelyResponse refundImmediatelyResponse;

    /**
     * Gets the value of the refundImmediatelyResponse property.
     * 
     * @return
     *     possible object is
     *     {@link RefundImmediatelyResponse }
     *     
     */
    public RefundImmediatelyResponse getRefundImmediatelyResponse() {
        return refundImmediatelyResponse;
    }

    /**
     * Sets the value of the refundImmediatelyResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefundImmediatelyResponse }
     *     
     */
    public void setRefundImmediatelyResponse(RefundImmediatelyResponse value) {
        this.refundImmediatelyResponse = value;
    }

}
