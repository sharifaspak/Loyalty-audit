package com.loyalty.marketplace.subscription.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FreeOffer {
	
	@Field("Id")
	private String id;
	@Field("OfferType")
	private String offerType;
	@Field("SubOfferId")
	private String subOfferId;
	@Field("Denomination")
	private Integer denomination;
	@Field("CouponQuantity")
	private Integer couponQuantity;
}
