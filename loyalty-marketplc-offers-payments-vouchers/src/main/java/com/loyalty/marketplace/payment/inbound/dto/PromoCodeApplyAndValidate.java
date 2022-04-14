package com.loyalty.marketplace.payment.inbound.dto;

import com.loyalty.marketplace.offers.promocode.outbound.dto.ValidPromoCodeDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromoCodeApplyAndValidate {
	private PromoCodeApply promoCodeApply;
	private ValidPromoCodeDetails validatePromoCodeResponse;
}
