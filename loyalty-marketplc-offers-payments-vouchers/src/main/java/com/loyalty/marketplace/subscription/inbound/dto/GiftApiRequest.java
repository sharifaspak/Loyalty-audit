package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.promocode.inbound.dto.PromoCodeDetails;

import lombok.Data;
@Data
public class GiftApiRequest {
	private List<String> accountNumber;
	private PromoCodeDetails promoCodeDetails;
	private String subscriptionCatalogId;
}
