package com.loyalty.marketplace.gifting.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GiftingLimitRequest {

	private String giftType;

	private Double fee;

	private LimitRequest accountLimits;
	
	private LimitRequest membershipLimits;
	
}
