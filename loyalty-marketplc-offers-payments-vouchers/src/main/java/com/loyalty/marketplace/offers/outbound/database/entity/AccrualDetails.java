package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class AccrualDetails {
	
	@Field("PointsEarnMultiplier")
	private Double pointsEarnMultiplier;
	
	@Field("AccrualPaymentMethods")
	private List<PaymentMethod> accrualPaymentMethods;
	
}
