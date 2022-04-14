
package ae.etisalat.middleware.serviceordermgmt_cud.subscribeservicesrequest;

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
 *                   &lt;element name="Msisdn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="PackageID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="TargetSystem" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="Duration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CRBTSubscriptionDetails" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="APartyMSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ToneID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="PackName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="TonePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="CPNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="ToneName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="LanguageID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="PaymentMode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
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
@XmlRootElement(name = "SubscribeServicesRequest")
public class SubscribeServicesRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected SubscribeServicesRequest.DataHeader dataHeader;

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
     *     {@link SubscribeServicesRequest.DataHeader }
     *     
     */
    public SubscribeServicesRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscribeServicesRequest.DataHeader }
     *     
     */
    public void setDataHeader(SubscribeServicesRequest.DataHeader value) {
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
     *         &lt;element name="Msisdn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="PackageID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="TargetSystem" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="Duration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CRBTSubscriptionDetails" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="APartyMSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ToneID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="PackName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="TonePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="CPNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="ToneName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="LanguageID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="PaymentMode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
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
        "packageID",
        "targetSystem",
        "duration",
        "serviceID",
        "crbtSubscriptionDetails",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "Msisdn", required = true)
        protected String msisdn;
        @XmlElement(name = "PackageID")
        protected List<String> packageID;
        @XmlElement(name = "TargetSystem", required = true)
        protected String targetSystem;
        @XmlElement(name = "Duration")
        protected String duration;
        @XmlElement(name = "ServiceID")
        protected String serviceID;
        @XmlElement(name = "CRBTSubscriptionDetails")
        protected List<SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails> crbtSubscriptionDetails;
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
        public String getMsisdn() {
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
        public void setMsisdn(String value) {
            this.msisdn = value;
        }

        /**
         * Gets the value of the packageID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the packageID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPackageID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPackageID() {
            if (packageID == null) {
                packageID = new ArrayList<String>();
            }
            return this.packageID;
        }

        /**
         * Gets the value of the targetSystem property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTargetSystem() {
            return targetSystem;
        }

        /**
         * Sets the value of the targetSystem property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTargetSystem(String value) {
            this.targetSystem = value;
        }

        /**
         * Gets the value of the duration property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDuration() {
            return duration;
        }

        /**
         * Sets the value of the duration property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDuration(String value) {
            this.duration = value;
        }

        /**
         * Gets the value of the serviceID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getServiceID() {
            return serviceID;
        }

        /**
         * Sets the value of the serviceID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setServiceID(String value) {
            this.serviceID = value;
        }

        /**
         * Gets the value of the crbtSubscriptionDetails property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the crbtSubscriptionDetails property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCRBTSubscriptionDetails().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails }
         * 
         * 
         */
        public List<SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails> getCRBTSubscriptionDetails() {
            if (crbtSubscriptionDetails == null) {
                crbtSubscriptionDetails = new ArrayList<SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails>();
            }
            return this.crbtSubscriptionDetails;
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
         *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="APartyMSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ToneID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="PackName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="TonePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="CPNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ToneName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="LanguageID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="PaymentMode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
            "serviceID",
            "aPartyMSISDN",
            "toneID",
            "packName",
            "tonePath",
            "cpname",
            "toneName",
            "priority",
            "languageID",
            "paymentMode",
			"activityId"
        })
        public static class CRBTSubscriptionDetails {

            @XmlElement(name = "ServiceID")
            protected String serviceID;
            @XmlElement(name = "APartyMSISDN", required = true)
            protected String aPartyMSISDN;
            @XmlElement(name = "ToneID")
            protected String toneID;
            @XmlElement(name = "PackName", required = true)
            protected String packName;
            @XmlElement(name = "TonePath")
            protected String tonePath;
            @XmlElement(name = "CPNAME")
            protected String cpname;
            @XmlElement(name = "ToneName")
            protected String toneName;
            @XmlElement(name = "Priority", required = true)
            protected String priority;
            @XmlElement(name = "LanguageID", required = true)
            protected String languageID;
            @XmlElement(name = "PaymentMode", required = true)
            protected String paymentMode;
			@XmlElement(name = "ActivityId", required = true)
            protected String activityId;

            /**
             * Gets the value of the serviceID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getServiceID() {
                return serviceID;
            }

            /**
             * Sets the value of the serviceID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setServiceID(String value) {
                this.serviceID = value;
            }

            /**
             * Gets the value of the aPartyMSISDN property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAPartyMSISDN() {
                return aPartyMSISDN;
            }

            /**
             * Sets the value of the aPartyMSISDN property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAPartyMSISDN(String value) {
                this.aPartyMSISDN = value;
            }

            /**
             * Gets the value of the toneID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getToneID() {
                return toneID;
            }

            /**
             * Sets the value of the toneID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setToneID(String value) {
                this.toneID = value;
            }

            /**
             * Gets the value of the packName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPackName() {
                return packName;
            }

            /**
             * Sets the value of the packName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPackName(String value) {
                this.packName = value;
            }

            /**
             * Gets the value of the tonePath property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTonePath() {
                return tonePath;
            }

            /**
             * Sets the value of the tonePath property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTonePath(String value) {
                this.tonePath = value;
            }

            /**
             * Gets the value of the cpname property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCPNAME() {
                return cpname;
            }

            /**
             * Sets the value of the cpname property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCPNAME(String value) {
                this.cpname = value;
            }

            /**
             * Gets the value of the toneName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getToneName() {
                return toneName;
            }

            /**
             * Sets the value of the toneName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setToneName(String value) {
                this.toneName = value;
            }

            /**
             * Gets the value of the priority property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPriority() {
                return priority;
            }

            /**
             * Sets the value of the priority property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPriority(String value) {
                this.priority = value;
            }

            /**
             * Gets the value of the languageID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLanguageID() {
                return languageID;
            }

            /**
             * Sets the value of the languageID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLanguageID(String value) {
                this.languageID = value;
            }

            /**
             * Gets the value of the paymentMode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentMode() {
                return paymentMode;
            }

            /**
             * Sets the value of the paymentMode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentMode(String value) {
                this.paymentMode = value;
            }
			
			public String getActivityId() {
				return activityId;
			}

			public void setActivityId(String activityId) {
				this.activityId = activityId;
			}

        }

    }

}
