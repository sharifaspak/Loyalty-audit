package com.loyalty.marketplace.payment.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;




@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPaymentRequest", propOrder = {
    "Customer",
    "Language",
    "Amount",
    "Channel",
    "Currency",
    "OrderID",
    "OrderInfo",
    "OrderName",
    "ReturnPath",
    "TransactionHint",
    "Password",
    "Store",
    "Terminal",
    "transactionId",
    "TotalPayment",
    "TransactionType",
    "MobileNumber"

})
public class GetPaymentRequest {
	
		@XmlElement(required = true)	    
	    protected String customerName;
	    @XmlElement(required = true)
	    protected String language;
	    @XmlElement(required = true)
	    protected Double amount;
	    @XmlElement(required = true)
	    protected String channel;
	    @XmlElement(required = true)
	    protected String currency;
	    @XmlElement(required = true)
	    protected String orderId;
	    @XmlElement(required = true)
	    protected String orderInfo;
	    @XmlElement(required = true)
	    protected String orderName;
	    @XmlElement(required = true)
	    protected String returnPath;
	    @XmlElement(required = true)
	    protected String transactionHint;
	    @XmlElement(required = true)
	    protected String password;
	    @XmlElement(required = true)
	    protected String store;
	    @XmlElement(required = true)
	    protected String terminal;
	    @XmlElement(required = true)
	    protected String transactionId;
	    @XmlElement(required = true)
	    protected String TotalPayment;
	    @XmlElement(required = true)
	    protected String TransactionType;
	    @XmlElement(required = true)
	    protected String MobileNumber;
	    
	    
	    
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getOrderInfo() {
			return orderInfo;
		}
		public void setOrderInfo(String orderInfo) {
			this.orderInfo = orderInfo;
		}
		public String getOrderName() {
			return orderName;
		}
		public void setOrderName(String orderName) {
			this.orderName = orderName;
		}
		public String getReturnPath() {
			return returnPath;
		}
		public void setReturnPath(String returnPath) {
			this.returnPath = returnPath;
		}
		public String getTransactionHint() {
			return transactionHint;
		}
		public void setTransactionHint(String transactionHint) {
			this.transactionHint = transactionHint;
		}
	    
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getStore() {
			return store;
		}
		public void setStore(String store) {
			this.store = store;
		}
		public String getTerminal() {
			return terminal;
		}
		public void setTerminal(String terminal) {
			this.terminal = terminal;
		}
		public String getTransactionId() {
			return transactionId;
		}
		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}
		public String getTotalPayment() {
			return TotalPayment;
		}
		public void setTotalPayment(String totalPayment) {
			TotalPayment = totalPayment;
		}
		public String getTransactionType() {
			return TransactionType;
		}
		public void setTransactionType(String transactionType) {
			TransactionType = transactionType;
		}
		public String getMobileNumber() {
			return MobileNumber;
		}
		public void setMobileNumber(String mobileNumber) {
			MobileNumber = mobileNumber;
		}
}
