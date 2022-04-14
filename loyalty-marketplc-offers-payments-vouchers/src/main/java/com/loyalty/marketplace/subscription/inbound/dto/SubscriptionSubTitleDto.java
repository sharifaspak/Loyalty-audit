package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionSubTitleDto {
	@NotNull(message = "{validation.subscriptionSubTitle.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionSubTitle.notEmpty.msg}")
	private String subscriptionSubTitleEn;
	
	@NotNull(message = "{validation.subscriptionSubTitle.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionSubTitle.notEmpty.msg}")
	private String subscriptionSubTitleAr;
}
