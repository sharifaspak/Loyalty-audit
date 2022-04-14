package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionTitleDto {

	@NotNull(message = "{validation.subscriptionTitle.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionTitle.notEmpty.msg}")
	private String subscriptionTitleEn;
	
	@NotNull(message = "{validation.subscriptionTitle.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionTitle.notEmpty.msg}")
	private String subscriptionTitleAr;
}