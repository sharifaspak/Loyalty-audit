
package ae.billingadapter.etisalat._1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="contentId" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="contentName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="contentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="channel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="subscriptionType" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="barCode" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="contentProviderId" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="revenuePercent" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="additionalParams" type="{http://www.etisalat.billingadapter.ae/1.0}ExtraPrams" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionInfo", propOrder = {
    "contentId",
    "contentName",
    "contentType",
    "channel",
    "transactionId",
    "subscriptionType",
    "barCode",
    "contentProviderId",
    "revenuePercent",
    "additionalParams"
})
public class AdditionInfo {

    @XmlElement(required = true)
    protected BigInteger contentId;
    @XmlElement(required = true)
    protected String contentName;
    @XmlElement(required = false)
    protected String contentType;
    @XmlElement(required = false)
    protected String channel;
    @XmlElement(required = true)
    protected BigInteger transactionId;
    @XmlElement(required = true)
    protected BigInteger subscriptionType;
    @XmlElement(required = false)
    protected BigInteger barCode;
    @XmlElement(required = true)
    protected BigInteger contentProviderId;
    @XmlElement(required = false)
    protected BigInteger revenuePercent;
    protected List<ExtraPrams> additionalParams;

    /**
     * Gets the value of the contentId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getContentId() {
        return contentId;
    }

    /**
     * Sets the value of the contentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setContentId(BigInteger value) {
        this.contentId = value;
    }

    /**
     * Gets the value of the contentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentName() {
        return contentName;
    }

    /**
     * Sets the value of the contentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentName(String value) {
        this.contentName = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *      {@link String }
     *     
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentType(String value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannel(String value) {
        this.channel = value;
    }

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTransactionId(BigInteger value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the subscriptionType property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * Sets the value of the subscriptionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSubscriptionType(BigInteger value) {
        this.subscriptionType = value;
    }

    /**
     * Gets the value of the barCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     *     
     */
    public BigInteger getBarCode() {
        return barCode;
    }

    /**
     * Sets the value of the barCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     *     
     */
    public void setBarCode(BigInteger value) {
        this.barCode = value;
    }

    /**
     * Gets the value of the contentProviderId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getContentProviderId() {
        return contentProviderId;
    }

    /**
     * Sets the value of the contentProviderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setContentProviderId(BigInteger value) {
        this.contentProviderId = value;
    }

    /**
     * Gets the value of the revenuePercent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     *     
     */
    public BigInteger getRevenuePercent() {
        return revenuePercent;
    }

    /**
     * Sets the value of the revenuePercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     *     
     */
    public void setRevenuePercent(BigInteger value) {
        this.revenuePercent = value;
    }

    /**
     * Gets the value of the additionalParams property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalParams property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalParams().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtraPrams }
     * 
     * 
     */
    public List<ExtraPrams> getAdditionalParams() {
        if (additionalParams == null) {
            additionalParams = new ArrayList<ExtraPrams>();
        }
        return this.additionalParams;
    }

}
