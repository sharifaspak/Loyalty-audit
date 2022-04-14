package com.loyalty.marketplace.payment.outbound.dto;

public class PaymentGatewayResponse {

	private CommonApiStatus commonApiStatus;
	
	private PaymentGatewayResponseResult result;
	
	public CommonApiStatus getCommonApiStatus() {
		return commonApiStatus;
	}
	public void setCommonApiStatus(CommonApiStatus commonApiStatus) {
		this.commonApiStatus = commonApiStatus;
	}
	public PaymentGatewayResponseResult getResult() {
		return result;
	}
	public void setResult(PaymentGatewayResponseResult result) {
		this.result = result;
	}
	
	
}
