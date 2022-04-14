package com.loyalty.marketplace.offers.domain.model;

import java.util.List;

import com.loyalty.marketplace.domain.model.PaymentMethodDomain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class AccrualDetailsDomain {
	
	private Double pointsEarnMultiplier;
	private List<PaymentMethodDomain> accrualPaymentMethods;

}
