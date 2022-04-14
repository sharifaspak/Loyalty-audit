package com.loyalty.marketplace.payment.outbound.dto;

import java.io.Serializable;

public class PaymentGatewayResponseResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected int responseCode;
    protected String responseDescription;
    protected String responseParameters;
    protected String uniqueId;
    protected String paymentPortal;
    protected String transactionId;     
    protected String merchantAccount;	
    protected String amount;
    protected String approvalCode;
    protected String balance;
    protected String accountNumber;	
    protected String accountToken;	
    protected String fees;
    protected String orderID;
    protected String accountExpiry;
    protected String paymentURL;
    protected String cardToken;
    protected String cardNumber;
    protected String cardBrand;
    protected String recurrenceID;
    
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getResponseParameters() {
		return responseParameters;
	}
	public void setResponseParameters(String responseParameters) {
		this.responseParameters = responseParameters;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getPaymentPortal() {
		return paymentPortal;
	}
	public void setPaymentPortal(String paymentPortal) {
		this.paymentPortal = paymentPortal;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getMerchantAccount() {
		return merchantAccount;
	}
	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getApprovalCode() {
		return approvalCode;
	}
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountToken() {
		return accountToken;
	}
	public void setAccountToken(String accountToken) {
		this.accountToken = accountToken;
	}
	public String getFees() {
		return fees;
	}
	public void setFees(String fees) {
		this.fees = fees;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getAccountExpiry() {
		return accountExpiry;
	}
	public void setAccountExpiry(String accountExpiry) {
		this.accountExpiry = accountExpiry;
	}
	public String getPaymentURL() {
		return paymentURL;
	}
	public void setPaymentURL(String paymentURL) {
		this.paymentURL = paymentURL;
	}
	
	public String getCardToken() {
		return cardToken;
	}
	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardBrand() {
		return cardBrand;
	}
	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}
	public String getRecurrenceID() {
		return recurrenceID;
	}
	public void setRecurrenceID(String recurrenceID) {
		this.recurrenceID = recurrenceID;
	}
}
