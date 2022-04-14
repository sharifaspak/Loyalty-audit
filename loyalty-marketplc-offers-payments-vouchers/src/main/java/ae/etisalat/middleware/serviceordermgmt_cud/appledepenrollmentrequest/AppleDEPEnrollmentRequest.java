
package ae.etisalat.middleware.serviceordermgmt_cud.appledepenrollmentrequest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ae.etisalat.middleware.sharedresources.common.applicationheader.ApplicationHeader;


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
 *                   &lt;element name="DepResellerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="RequestContext"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ShipTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="LangCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="TimeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Orders" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="OrderDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="PoNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="Deliveries" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="DeliveryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                       &lt;element name="ShipDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                       &lt;element name="Devices" maxOccurs="unbounded"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;sequence&gt;
 *                                                 &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                                 &lt;element name="AssetTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                 &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                                               &lt;/sequence&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                       &lt;element name="AdditionalInfo" maxOccurs="unbounded" minOccurs="0"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;sequence&gt;
 *                                                 &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                                 &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                               &lt;/sequence&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="AdditionalInfo" maxOccurs="unbounded" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                       &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
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
@XmlRootElement(name = "AppleDEPEnrollmentRequest")
public class AppleDEPEnrollmentRequest {

    @XmlElement(name = "ApplicationHeader", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/ApplicationHeader.xsd", required = true)
    protected ApplicationHeader applicationHeader;
    @XmlElement(name = "DataHeader", required = true)
    protected AppleDEPEnrollmentRequest.DataHeader dataHeader;

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
     *     {@link AppleDEPEnrollmentRequest.DataHeader }
     *     
     */
    public AppleDEPEnrollmentRequest.DataHeader getDataHeader() {
        return dataHeader;
    }

    /**
     * Sets the value of the dataHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppleDEPEnrollmentRequest.DataHeader }
     *     
     */
    public void setDataHeader(AppleDEPEnrollmentRequest.DataHeader value) {
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
     *         &lt;element name="DepResellerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="RequestContext"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ShipTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="LangCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="TimeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Orders" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="OrderDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="PoNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="Deliveries" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="DeliveryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                             &lt;element name="ShipDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                             &lt;element name="Devices" maxOccurs="unbounded"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                                       &lt;element name="AssetTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                                     &lt;/sequence&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="AdditionalInfo" maxOccurs="unbounded" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                                       &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                                     &lt;/sequence&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="AdditionalInfo" maxOccurs="unbounded" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                             &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
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
        "depResellerID",
        "requestContext",
        "orders",
        "additionalInfo"
    })
    public static class DataHeader {

        @XmlElement(name = "DepResellerID", required = true)
        protected String depResellerID;
        @XmlElement(name = "RequestContext", required = true)
        protected AppleDEPEnrollmentRequest.DataHeader.RequestContext requestContext;
        @XmlElement(name = "Orders", required = true)
        protected List<AppleDEPEnrollmentRequest.DataHeader.Orders> orders;
        @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
        protected List<ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo> additionalInfo;

        /**
         * Gets the value of the depResellerID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDepResellerID() {
            return depResellerID;
        }

        /**
         * Sets the value of the depResellerID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDepResellerID(String value) {
            this.depResellerID = value;
        }

        /**
         * Gets the value of the requestContext property.
         * 
         * @return
         *     possible object is
         *     {@link AppleDEPEnrollmentRequest.DataHeader.RequestContext }
         *     
         */
        public AppleDEPEnrollmentRequest.DataHeader.RequestContext getRequestContext() {
            return requestContext;
        }

        /**
         * Sets the value of the requestContext property.
         * 
         * @param value
         *     allowed object is
         *     {@link AppleDEPEnrollmentRequest.DataHeader.RequestContext }
         *     
         */
        public void setRequestContext(AppleDEPEnrollmentRequest.DataHeader.RequestContext value) {
            this.requestContext = value;
        }

        /**
         * Gets the value of the orders property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the orders property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getOrders().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AppleDEPEnrollmentRequest.DataHeader.Orders }
         * 
         * 
         */
        public List<AppleDEPEnrollmentRequest.DataHeader.Orders> getOrders() {
            if (orders == null) {
                orders = new ArrayList<AppleDEPEnrollmentRequest.DataHeader.Orders>();
            }
            return this.orders;
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
         * {@link ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo }
         * 
         * 
         */
        public List<ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo> getAdditionalInfo() {
            if (additionalInfo == null) {
                additionalInfo = new ArrayList<ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo>();
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
         *         &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="OrderDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="PoNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="Deliveries" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="DeliveryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                   &lt;element name="ShipDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                   &lt;element name="Devices" maxOccurs="unbounded"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                             &lt;element name="AssetTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
         *                           &lt;/sequence&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="AdditionalInfo" maxOccurs="unbounded" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                             &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                           &lt;/sequence&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="AdditionalInfo" maxOccurs="unbounded" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                   &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
            "orderNumber",
            "orderDate",
            "orderType",
            "customerID",
            "poNumber",
            "deliveries",
            "additionalInfo"
        })
        public static class Orders {

            @XmlElement(name = "OrderNumber", required = true)
            protected String orderNumber;
            @XmlElement(name = "OrderDate", required = true)
            protected String orderDate;
            @XmlElement(name = "OrderType", required = true)
            protected String orderType;
            @XmlElement(name = "CustomerID", required = true)
            protected String customerID;
            @XmlElement(name = "PoNumber", required = true)
            protected String poNumber;
            @XmlElement(name = "Deliveries", required = true)
            protected List<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries> deliveries;
            @XmlElement(name = "AdditionalInfo")
            protected List<AppleDEPEnrollmentRequest.DataHeader.Orders.AdditionalInfo> additionalInfo;

            /**
             * Gets the value of the orderNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOrderNumber() {
                return orderNumber;
            }

            /**
             * Sets the value of the orderNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOrderNumber(String value) {
                this.orderNumber = value;
            }

            /**
             * Gets the value of the orderDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOrderDate() {
                return orderDate;
            }

            /**
             * Sets the value of the orderDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOrderDate(String value) {
                this.orderDate = value;
            }

            /**
             * Gets the value of the orderType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOrderType() {
                return orderType;
            }

            /**
             * Sets the value of the orderType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOrderType(String value) {
                this.orderType = value;
            }

            /**
             * Gets the value of the customerID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCustomerID() {
                return customerID;
            }

            /**
             * Sets the value of the customerID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCustomerID(String value) {
                this.customerID = value;
            }

            /**
             * Gets the value of the poNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPoNumber() {
                return poNumber;
            }

            /**
             * Sets the value of the poNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPoNumber(String value) {
                this.poNumber = value;
            }

            /**
             * Gets the value of the deliveries property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the deliveries property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getDeliveries().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries }
             * 
             * 
             */
            public List<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries> getDeliveries() {
                if (deliveries == null) {
                    deliveries = new ArrayList<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries>();
                }
                return this.deliveries;
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
             * {@link AppleDEPEnrollmentRequest.DataHeader.Orders.AdditionalInfo }
             * 
             * 
             */
            public List<AppleDEPEnrollmentRequest.DataHeader.Orders.AdditionalInfo> getAdditionalInfo() {
                if (additionalInfo == null) {
                    additionalInfo = new ArrayList<AppleDEPEnrollmentRequest.DataHeader.Orders.AdditionalInfo>();
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
             *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
                "name",
                "value"
            })
            public static class AdditionalInfo {

                @XmlElement(name = "Name", required = true)
                protected String name;
                @XmlElement(name = "Value", required = true)
                protected String value;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

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
             *         &lt;element name="DeliveryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *         &lt;element name="ShipDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *         &lt;element name="Devices" maxOccurs="unbounded"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *                   &lt;element name="AssetTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element ref="{http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd}AdditionalInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
             *                 &lt;/sequence&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="AdditionalInfo" maxOccurs="unbounded" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *                   &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
                "deliveryNumber",
                "shipDate",
                "devices",
                "additionalInfo"
            })
            public static class Deliveries {

                @XmlElement(name = "DeliveryNumber", required = true)
                protected String deliveryNumber;
                @XmlElement(name = "ShipDate", required = true)
                protected String shipDate;
                @XmlElement(name = "Devices", required = true)
                protected List<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.Devices> devices;
                @XmlElement(name = "AdditionalInfo")
                protected List<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.AdditionalInfo> additionalInfo;

                /**
                 * Gets the value of the deliveryNumber property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDeliveryNumber() {
                    return deliveryNumber;
                }

                /**
                 * Sets the value of the deliveryNumber property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDeliveryNumber(String value) {
                    this.deliveryNumber = value;
                }

                /**
                 * Gets the value of the shipDate property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getShipDate() {
                    return shipDate;
                }

                /**
                 * Sets the value of the shipDate property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setShipDate(String value) {
                    this.shipDate = value;
                }

                /**
                 * Gets the value of the devices property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the devices property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getDevices().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.Devices }
                 * 
                 * 
                 */
                public List<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.Devices> getDevices() {
                    if (devices == null) {
                        devices = new ArrayList<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.Devices>();
                    }
                    return this.devices;
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
                 * {@link AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.AdditionalInfo }
                 * 
                 * 
                 */
                public List<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.AdditionalInfo> getAdditionalInfo() {
                    if (additionalInfo == null) {
                        additionalInfo = new ArrayList<AppleDEPEnrollmentRequest.DataHeader.Orders.Deliveries.AdditionalInfo>();
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
                 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
                 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
                    "name",
                    "value"
                })
                public static class AdditionalInfo {

                    @XmlElement(name = "Name", required = true)
                    protected String name;
                    @XmlElement(name = "Value", required = true)
                    protected String value;

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

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
                 *         &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
                 *         &lt;element name="AssetTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
                    "deviceId",
                    "assetTag",
                    "additionalInfo"
                })
                public static class Devices {

                    @XmlElement(name = "DeviceId", required = true)
                    protected String deviceId;
                    @XmlElement(name = "AssetTag")
                    protected String assetTag;
                    @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
                    protected List<ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo> additionalInfo;

                    /**
                     * Gets the value of the deviceId property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDeviceId() {
                        return deviceId;
                    }

                    /**
                     * Sets the value of the deviceId property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDeviceId(String value) {
                        this.deviceId = value;
                    }

                    /**
                     * Gets the value of the assetTag property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAssetTag() {
                        return assetTag;
                    }

                    /**
                     * Sets the value of the assetTag property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAssetTag(String value) {
                        this.assetTag = value;
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
                     * {@link ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo }
                     * 
                     * 
                     */
                    public List<ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo> getAdditionalInfo() {
                        if (additionalInfo == null) {
                            additionalInfo = new ArrayList<ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo>();
                        }
                        return this.additionalInfo;
                    }

                }

            }

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
         *         &lt;element name="ShipTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="LangCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="TimeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
            "shipTo",
            "langCode",
            "timeZone"
        })
        public static class RequestContext {

            @XmlElement(name = "ShipTo")
            protected String shipTo;
            @XmlElement(name = "LangCode")
            protected String langCode;
            @XmlElement(name = "TimeZone")
            protected String timeZone;

            /**
             * Gets the value of the shipTo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getShipTo() {
                return shipTo;
            }

            /**
             * Sets the value of the shipTo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setShipTo(String value) {
                this.shipTo = value;
            }

            /**
             * Gets the value of the langCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLangCode() {
                return langCode;
            }

            /**
             * Sets the value of the langCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLangCode(String value) {
                this.langCode = value;
            }

            /**
             * Gets the value of the timeZone property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTimeZone() {
                return timeZone;
            }

            /**
             * Sets the value of the timeZone property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTimeZone(String value) {
                this.timeZone = value;
            }

        }

    }

}
