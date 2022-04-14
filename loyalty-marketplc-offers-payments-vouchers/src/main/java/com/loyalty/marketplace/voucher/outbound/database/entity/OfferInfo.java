package com.loyalty.marketplace.voucher.outbound.database.entity;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OfferInfo {
	
	@NotEmpty(message="Offer Id cannot be empty")
	@Field("OfferId")
	private String offer;
	
	@Field("SubOfferId")
	private String subOffer;
	
	/*
	 * @Field("DenominationId") private String denomination;
	 */

}
