package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethods {

	private String paymentMethodId;
	private String description;

}
