package com.loyalty.marketplace.subscription.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PaymentMethods {

	private String paymentMethodId;
	private String description;
}
