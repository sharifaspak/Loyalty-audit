package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionPromotionRequestDto {
	
	private String id;
	
	@NotNull(message = "{validation.subscriptionCatalogId.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionCatalogId.notEmpty.msg}")
	private String subscriptionCatalogId;
	
	@NotNull(message = "{validation.validityPeriod.notNull.msg}")
	@NotEmpty(message = "{validation.validityPeriod.notEmpty.msg}")
	private String validity;
	
	private String startDate;
	
	@NotNull(message = "{validation.endDate.notNull.msg}")
	@NotEmpty(message = "{validation.endDate.notEmpty.msg}")
	private String endDate;
	
	private String discountPercentage;
	
	private String flatRate;
	
	private int discountMonths;
	
}
