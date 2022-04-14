package com.loyalty.marketplace.offers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionDetailsDomain {
	
	private Boolean subscPromo;
    private String subscriptionCatalogId;

}