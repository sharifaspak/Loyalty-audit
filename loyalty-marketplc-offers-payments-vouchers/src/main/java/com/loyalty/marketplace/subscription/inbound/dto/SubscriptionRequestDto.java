package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionRequestDto {
	
	private String id;
	
	@NotNull(message = "{validation.subscriptionCatalogId.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionCatalogId.notEmpty.msg}")
	private String subscriptionCatalogId;
	
	@NotNull(message = "{validation.accountNumber.notNull.msg}")
	@NotEmpty(message = "{validation.accountNumber.notEmpty.msg}")
	private String accountNumber;
	
	private String membershipCode;
	
	private String promoCode;
	
	private String startDate;
	
	@NotNull(message = "{validation.paymentMethod.notNull.msg}")
	@NotEmpty(message = "{validation.paymentMethod.notEmpty.msg}")
	private String paymentMethod;
	
	private String subscriptionMethod;
	
	private String cardType;
	
	private int freeSubscriptionDays;
	
}
