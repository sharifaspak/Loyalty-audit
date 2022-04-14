package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionMarketDescLineDto {
	@NotNull(message = "{validation.subscriptionMarketDescLine.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionMarketDescLine.notEmpty.msg}")
	private String subscriptionMarketDescLineEn;
	
	@NotNull(message = "{validation.subscriptionMarketDescLine.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionMarketDescLine.notEmpty.msg}")
	private String subscriptionMarketDescLineAr;

}
