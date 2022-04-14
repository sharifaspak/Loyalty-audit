package com.loyalty.marketplace.subscription.outbound.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "SubscriptionCatalog")
@Setter
@Getter
@ToString
public class CatalogBenefits {

	@Field("EligibleOfferType")
	private Title eligibleOfferType;
	
	@Field("EligibleOfferCategory")
	private List<OfferCategory> eligibleOfferCategory;
}
