package com.loyalty.marketplace.payment.inbound.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreatePaymentRequest {
	
	private String channelId;
	
	@NotNull(message = "createPaymentInfo is mandatory")
	private CreatePaymentRequestObj createPaymentInfo;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public CreatePaymentRequestObj getCreatePaymentInfo() {
		return createPaymentInfo;
	}

	public void setCreatePaymentInfo(CreatePaymentRequestObj createPaymentInfo) {
		this.createPaymentInfo = createPaymentInfo;
	}
	
}
