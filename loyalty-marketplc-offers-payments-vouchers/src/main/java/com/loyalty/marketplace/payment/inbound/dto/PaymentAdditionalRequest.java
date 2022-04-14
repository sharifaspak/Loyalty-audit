package com.loyalty.marketplace.payment.inbound.dto;

import com.loyalty.marketplace.offers.helper.dto.Headers;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentAdditionalRequest {

	private String channelId; 
	private String activityCode;
	private	String uuid; 
	private boolean isPaymentRequired;
	private Headers header;
}
