
package ae.etisalat.middleware.serviceordermgmt_cud.ypayserviceorderrequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.applicationheader.ApplicationHeader;
import ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd}ApplicationHeader"/&gt;
 *         &lt;element name="DataHeader"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="MSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="TypeOfSubscription" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "applicationHeader",
    "dataHeader"
})
@XmlRootElement(name = "YPAYServiceOrderRequest")
public class YPAYServiceOrderRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected YPAYServiceOrderRequest.DataHeader dataHeader;

    /**
     * Gets the value of the applicationHeader property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationHeader }
     *     
     */
    public ApplicationHeader getApplicationHeader() {
        return applicationHeader;
    }

    /**
     * Sets the value of the applicationHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationHeader }
     *     
     */
    public void setApplicationHeader(ApplicationHeader value) {
        this.applicationHeader = value;
    }

    /**
     * Gets the value of the dataHeader property.
     * 
     * @return
     *     possible object is
     *     {@link YPAYServiceOrderRequest.DataHeader }
     *     
     */
    public YPAYServiceOrderRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link YPAYServiceOrderRequest.DataHeader }
     *     
     */
    public void setDataHeader(YPAYServiceOrderRequest.DataHeader value) {
        this.dataHeader = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="MSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="TypeOfSubscription" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    @XmlType(name = "", propOrder = {
        "msisdn",
        "typeOfSubscription",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "MSISDN", required = true)
        protected String msisdn;
        @XmlElement(name = "TypeOfSubscription", required = true)
        protected String typeOfSubscription;
        @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
        protected List<AdditionalInfo> additionalInfo;

        /**
         * Gets the value of the msisdn property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMSISDN() {
            return msisdn;
        }

        /**
         * Sets the value of the msisdn property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMSISDN(String value) {
            this.msisdn = value;
        }

        /**
         * Gets the value of the typeOfSubscription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTypeOfSubscription() {
            return typeOfSubscription;
        }

        /**
         * Sets the value of the typeOfSubscription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTypeOfSubscription(String value) {
            this.typeOfSubscription = value;
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

}
