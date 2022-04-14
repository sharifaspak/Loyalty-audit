package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionMarketTitleLineDto {
	@NotNull(message = "{validation.subscriptionMarketTitleLine.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionMarketTitleLine.notEmpty.msg}")
	private String subscriptionMarketTitleLineEn;
	
	@NotNull(message = "{validation.subscriptionMarketTitleLine.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionMarketTitleLine.notEmpty.msg}")
	private String subscriptionMarketTitleLineAr;
}
