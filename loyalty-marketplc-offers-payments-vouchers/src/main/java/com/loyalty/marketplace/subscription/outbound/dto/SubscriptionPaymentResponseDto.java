package com.loyalty.marketplace.subscription.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionPaymentResponseDto {
	private String paymentMethodId;
	private String paymentMethod;
	private String cardNumber;
	private String subType;
}
