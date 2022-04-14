package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OfferCountDto {
	private boolean isMerchantCodeNotEqual;
	private String existingMerchantCode;
	private String updatedMerchantCode;
	private OfferCatalog fetchedOffer;

}
