package com.loyalty.marketplace.gifting.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class OfferValueResponseDto {
	
	private String offerId;
	private String offerType;
	private String subOfferId;
	private String denomination;
	private Integer couponQuantity;

}
