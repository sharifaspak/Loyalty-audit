package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscribedOfferTitleDto {
	@NotNull(message = "{validation.subscribedOfferTitle.notNull.msg}")
	@NotEmpty(message = "{validation.subscribedOfferTitle.notEmpty.msg}")
	private String subscribedOfferTitleEn;
	
	@NotNull(message = "{validation.subscribedOfferTitle.notNull.msg}")
	@NotEmpty(message = "{validation.subscribedOfferTitle.notEmpty.msg}")
	private String subscribedOfferTitleAr;
}
