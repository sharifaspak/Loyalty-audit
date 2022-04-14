package com.loyalty.marketplace.payment.outbound.dto;

public class PaymentFinalizationResponse {
	
	private CommonApiStatus apiStatus;
	
	private PaymentFinalizationResponseResult result;

	public CommonApiStatus getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(CommonApiStatus apiStatus) {
		this.apiStatus = apiStatus;
	}

	public PaymentFinalizationResponseResult getResult() {
		return result;
	}

	public void setResult(PaymentFinalizationResponseResult result) {
		this.result = result;
	}
	
}
