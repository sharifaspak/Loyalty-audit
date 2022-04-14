package com.loyalty.marketplace.offers.promocode.constants.codes;

public enum PromoCodeConstants {
	
	INSTANCE("Instance"),
	UMBRELLA("Umbrella"),
	INSTANCE_PROMOTION("Instance Promotion"),
	UMBRELLA_PROMOTION("Umbrella Promotion"),
	;

	private final String constant;

	PromoCodeConstants(String constant) {
		this.constant = constant;
	}

	public String get() {
		return this.constant;
	}
}
