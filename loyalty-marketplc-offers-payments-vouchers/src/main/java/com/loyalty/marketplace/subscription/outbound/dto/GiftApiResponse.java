package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoDetails;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class GiftApiResponse extends ResultResponse {

	public GiftApiResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	private PromoDetails promoDetails;
	
	private List<GiftDetails> giftDetails;

}
