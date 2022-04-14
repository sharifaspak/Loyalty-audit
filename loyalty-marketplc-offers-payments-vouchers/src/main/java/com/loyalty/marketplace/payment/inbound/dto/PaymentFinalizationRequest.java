package com.loyalty.marketplace.payment.inbound.dto;

public class PaymentFinalizationRequest {

	private String token;
	
	private String transactionID;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	
}
