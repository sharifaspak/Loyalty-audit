package com.loyalty.marketplace.payment.outbound.dto;

public class CreatePaymentResponse {
	
	private CommonApiStatus apiStatus;
	
	private CreatePaymentResponseResult result;

	public CommonApiStatus getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(CommonApiStatus apiStatus) {
		this.apiStatus = apiStatus;
	}

	public CreatePaymentResponseResult getResult() {
		return result;
	}

	public void setResult(CreatePaymentResponseResult result) {
		this.result = result;
	}
	
	

}
