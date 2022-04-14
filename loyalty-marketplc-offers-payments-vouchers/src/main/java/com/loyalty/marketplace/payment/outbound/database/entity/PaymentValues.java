package com.loyalty.marketplace.payment.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PaymentValues {

	@Field("PointValue")
	private Integer pointsValue;
	@Field("Cost")
	private Double cost;
}
