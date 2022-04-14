package com.loyalty.marketplace.gifting.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ListGiftingLimitsResult {
	
	private String id;
	
	private String programCode;
	
	private String giftType;
	
	private Double fee;
	
	private LimitsResponse accountLimits;
	
	private LimitsResponse membershipLimits;
	
}
