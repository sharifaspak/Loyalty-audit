package com.loyalty.marketplace.offers.promocode.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class ValidPromoCodeDetails extends ResultResponse {

	public ValidPromoCodeDetails(String externalTransactionId) {
		super(externalTransactionId);
	}
	private PromoDetails promoDetails;
}
