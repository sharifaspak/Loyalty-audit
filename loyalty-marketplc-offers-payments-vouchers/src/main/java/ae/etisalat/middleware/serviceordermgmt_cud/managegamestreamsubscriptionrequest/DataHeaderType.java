
package ae.etisalat.middleware.serviceordermgmt_cud.managegamestreamsubscriptionrequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo;


/**
 * <p>Java class for DataHeaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataHeaderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IPTVNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RenewalFlag" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RateplanID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataHeaderType", propOrder = {
    "accountNumber",
    "iptvNumber",
    "renewalFlag",
    "orderID",
    "rateplanID",
    "timeStamp",
    "additionalInfo"
})
public class DataHeaderType {

    @XmlElement(name = "AccountNumber", required = true)
    protected String accountNumber;
    @XmlElement(name = "IPTVNumber", required = true)
    protected String iptvNumber;
    @XmlElement(name = "RenewalFlag", required = true)
    protected String renewalFlag;
    @XmlElement(name = "OrderID", required = true)
    protected String orderID;
    @XmlElement(name = "RateplanID", required = true)
    protected String rateplanID;
    @XmlElement(name = "TimeStamp", required = true)
    protected String timeStamp;
    @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
    protected List<AdditionalInfo> additionalInfo;

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the iptvNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPTVNumber() {
        return iptvNumber;
    }

    /**
     * Sets the value of the iptvNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPTVNumber(String value) {
        this.iptvNumber = value;
    }

    /**
     * Gets the value of the renewalFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRenewalFlag() {
        return renewalFlag;
    }

    /**
     * Sets the value of the renewalFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRenewalFlag(String value) {
        this.renewalFlag = value;
    }

    /**
     * Gets the value of the orderID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderID() {
        return orderID;
    }

    /**
     * Sets the value of the orderID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderID(String value) {
        this.orderID = value;
    }

    /**
     * Gets the value of the rateplanID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRateplanID() {
        return rateplanID;
    }

    /**
     * Sets the value of the rateplanID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRateplanID(String value) {
        this.rateplanID = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeStamp(String value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the additionalInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalInfo }
     * 
     * 
     */
    public List<AdditionalInfo> getAdditionalInfo() {
        if (additionalInfo == null) {
            additionalInfo = new ArrayList<AdditionalInfo>();
        }
        return this.additionalInfo;
    }

}
