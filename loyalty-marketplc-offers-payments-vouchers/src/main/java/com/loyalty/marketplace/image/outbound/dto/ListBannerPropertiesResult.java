package com.loyalty.marketplace.image.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ListBannerPropertiesResult {

	private String id;
	
	private String programCode;
	
	private Integer topBannerLimit;
	
	private Integer middleBannerLimit;
	
	private Integer bottomBannerLimit;
	
	private boolean includeRedeemedOffers;
	
	private Integer personalizeBannerCount;
	
	private Integer fixedBannerCount;
	
}
