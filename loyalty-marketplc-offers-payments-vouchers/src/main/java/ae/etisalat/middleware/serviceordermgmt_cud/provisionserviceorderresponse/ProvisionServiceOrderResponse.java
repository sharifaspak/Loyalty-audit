
package ae.etisalat.middleware.serviceordermgmt_cud.provisionserviceorderresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.ackmessage.AckMessage;
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
 *         &lt;element name="ResponseData" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ServiceOrder" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ServiceOrderID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="ServiceAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="AccountInformation" maxOccurs="unbounded" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element name="InternetUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                       &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *         &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/AckMessage.xsd}AckMessage"/&gt;
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
    "responseData",
    "ackMessage"
})
@XmlRootElement(name = "ProvisionServiceOrderResponse")
public class ProvisionServiceOrderResponse {

    @XmlElement(name = "ResponseData")
    protected ProvisionServiceOrderResponse.ResponseData responseData;
    @XmlElement(name = "AckMessage", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/AckMessage.xsd", required = true)
    protected AckMessage ackMessage;

    /**
     * Gets the value of the responseData property.
     * 
     * @return
     *     possible object is
     *     {@link ProvisionServiceOrderResponse.ResponseData }
     *     
     */
    public ProvisionServiceOrderResponse.ResponseData getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProvisionServiceOrderResponse.ResponseData }
     *     
     */
    public void setResponseData(ProvisionServiceOrderResponse.ResponseData value) {
        this.responseData = value;
    }

    /**
     * Gets the value of the ackMessage property.
     * 
     * @return
     *     possible object is
     *     {@link AckMessage }
     *     
     */
    public AckMessage getAckMessage() {
        return ackMessage;
    }

    /**
     * Sets the value of the ackMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link AckMessage }
     *     
     */
    public void setAckMessage(AckMessage value) {
        this.ackMessage = value;
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
     *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ServiceOrder" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ServiceOrderID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="ServiceAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="AccountInformation" maxOccurs="unbounded" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element name="InternetUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                             &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "transactionID",
        "serviceOrder",
        "additionalInfo"
    })
    public static class ResponseData {

        @XmlElement(name = "TransactionID")
        protected String transactionID;
        @XmlElement(name = "ServiceOrder")
        protected List<ProvisionServiceOrderResponse.ResponseData.ServiceOrder> serviceOrder;
        @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
        protected List<AdditionalInfo> additionalInfo;

        /**
         * Gets the value of the transactionID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTransactionID() {
            return transactionID;
        }

        /**
         * Sets the value of the transactionID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTransactionID(String value) {
            this.transactionID = value;
        }

        /**
         * Gets the value of the serviceOrder property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the serviceOrder property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getServiceOrder().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProvisionServiceOrderResponse.ResponseData.ServiceOrder }
         * 
         * 
         */
        public List<ProvisionServiceOrderResponse.ResponseData.ServiceOrder> getServiceOrder() {
            if (serviceOrder == null) {
                serviceOrder = new ArrayList<ProvisionServiceOrderResponse.ResponseData.ServiceOrder>();
            }
            return this.serviceOrder;
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
         *         &lt;element name="ServiceOrderID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="ServiceAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="AccountInformation" maxOccurs="unbounded" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="InternetUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                   &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
            "serviceOrderID",
            "serviceType",
            "serviceID",
            "serviceAction",
            "accountInformation",
            "additionalInfo"
        })
        public static class ServiceOrder {

            @XmlElement(name = "ServiceOrderID")
            protected String serviceOrderID;
            @XmlElement(name = "ServiceType")
            protected String serviceType;
            @XmlElement(name = "ServiceID")
            protected String serviceID;
            @XmlElement(name = "ServiceAction")
            protected String serviceAction;
            @XmlElement(name = "AccountInformation")
            protected List<ProvisionServiceOrderResponse.ResponseData.ServiceOrder.AccountInformation> accountInformation;
            @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
            protected List<AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the serviceOrderID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getServiceOrderID() {
                return serviceOrderID;
            }

            /**
             * Sets the value of the serviceOrderID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setServiceOrderID(String value) {
                this.serviceOrderID = value;
            }

            /**
             * Gets the value of the serviceType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getServiceType() {
                return serviceType;
            }

            /**
             * Sets the value of the serviceType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setServiceType(String value) {
                this.serviceType = value;
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
             * Gets the value of the serviceAction property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getServiceAction() {
                return serviceAction;
            }

            /**
             * Sets the value of the serviceAction property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setServiceAction(String value) {
                this.serviceAction = value;
            }

            /**
             * Gets the value of the accountInformation property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the accountInformation property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAccountInformation().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ProvisionServiceOrderResponse.ResponseData.ServiceOrder.AccountInformation }
             * 
             * 
             */
            public List<ProvisionServiceOrderResponse.ResponseData.ServiceOrder.AccountInformation> getAccountInformation() {
                if (accountInformation == null) {
                    accountInformation = new ArrayList<ProvisionServiceOrderResponse.ResponseData.ServiceOrder.AccountInformation>();
                }
                return this.accountInformation;
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
             *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="InternetUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
                "accountNumber",
                "accountID",
                "internetUserName",
                "status",
                "additionalInfo"
            })
            public static class AccountInformation {

                @XmlElement(name = "AccountNumber")
                protected String accountNumber;
                @XmlElement(name = "AccountID")
                protected String accountID;
                @XmlElement(name = "InternetUserName")
                protected String internetUserName;
                @XmlElement(name = "Status")
                protected String status;
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
                 * Gets the value of the accountID property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAccountID() {
                    return accountID;
                }

                /**
                 * Sets the value of the accountID property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAccountID(String value) {
                    this.accountID = value;
                }

                /**
                 * Gets the value of the internetUserName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getInternetUserName() {
                    return internetUserName;
                }

                /**
                 * Sets the value of the internetUserName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setInternetUserName(String value) {
                    this.internetUserName = value;
                }

                /**
                 * Gets the value of the status property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getStatus() {
                    return status;
                }

                /**
                 * Sets the value of the status property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setStatus(String value) {
                    this.status = value;
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

    }

}
