package com.loyalty.marketplace.gifting.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferGiftValues {
	
	@Field("OfferId")
	private String offerId;
	@Field("OfferType")
	private String offerType;
	@Field("SubOfferId")
	private String subOfferId;
	@Field("Denomination")
	private Integer denomination;
	@Field("CouponQuantity")
	private Integer couponQuantity;

}
