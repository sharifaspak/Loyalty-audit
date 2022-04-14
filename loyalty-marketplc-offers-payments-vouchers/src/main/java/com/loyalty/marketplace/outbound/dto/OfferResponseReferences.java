package com.loyalty.marketplace.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class OfferResponseReferences {

	private List<MarketplaceImage> imageList;
	private List<PurchaseHistory> purchaseHistoryList;
	private List<ConversionRate> conversionList;
	
}
