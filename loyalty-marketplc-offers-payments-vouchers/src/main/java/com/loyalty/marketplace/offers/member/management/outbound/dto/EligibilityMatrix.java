package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class EligibilityMatrix {
	@Field(value = "EligibleFeature")
	private List<EligibileSections> eligibleFeature;
	@Field(value = "PaymentMethod")
	private List<PaymentMethods> paymentMethod;
	public List<EligibileSections> getEligibleFeature() {
		return eligibleFeature;
	}
	public void setEligibleFeature(List<EligibileSections> eligibleFeature) {
		this.eligibleFeature = eligibleFeature;
	}
	public List<PaymentMethods> getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(List<PaymentMethods> paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
}
