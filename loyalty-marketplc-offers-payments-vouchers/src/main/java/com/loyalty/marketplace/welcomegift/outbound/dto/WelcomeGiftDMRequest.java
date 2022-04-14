package com.loyalty.marketplace.welcomegift.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WelcomeGiftDMRequest {

	private String accountNumber;
	private String customerType;
	private String channelId;
}
