package com.loyalty.marketplace.offers.promocode.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PartnerPromoCodeDetails extends ResultResponse {

	public PartnerPromoCodeDetails(String externalTransactionId) {
		super(externalTransactionId);
	}
	private List<String> promoDetails;
}
