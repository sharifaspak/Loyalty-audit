package com.loyalty.marketplace.welcomegift.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WelcomeGiftRequestDto {
	
	private String accountNumber;
	private String customerType;
	private String channelId;
	private String membershipCode;
	private String cardType;
	private String subscriptionCatalogId;
	private boolean isBogoBulk;
	private String giftType;
	private String giftId;
	private Integer freeDuration;
}
