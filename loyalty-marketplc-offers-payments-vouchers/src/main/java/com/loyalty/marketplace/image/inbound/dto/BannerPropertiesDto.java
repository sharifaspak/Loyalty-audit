package com.loyalty.marketplace.image.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BannerPropertiesDto {

	private Integer topBannerLimit;
	
	private Integer middleBannerLimit;
	
	private Integer bottomBannerLimit;
	
	private String includeRedeemedOffers;
	
	private Integer personalizeBannerCount;
	
	private Integer fixedBannerCount;
	
}
