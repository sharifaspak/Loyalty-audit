package com.loyalty.marketplace.offers.promocode.inbound.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionManagerResponseDto {

	@JsonProperty("promoEligibility")
	private boolean promoEligibilityStatus;
	@JsonProperty("reason")
	private String reason;
	@JsonIgnore
	private Integer errorCode;
	@JsonIgnore
	private String errorReason;
}

