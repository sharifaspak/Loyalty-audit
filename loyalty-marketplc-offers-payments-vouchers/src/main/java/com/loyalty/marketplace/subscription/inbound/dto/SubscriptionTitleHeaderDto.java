package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionTitleHeaderDto {
	@NotNull(message = "{validation.subscriptionTitleHeader.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionTitleHeader.notEmpty.msg}")
	private String subscriptionTitleHeaderEn;
	
	@NotNull(message = "{validation.subscriptionTitleHeader.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionTitleHeader.notEmpty.msg}")
	private String subscriptionTitleHeaderAr;
}
