package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import lombok.Data;

@Data
public class GiftDetails {
	List<SubscriptionCatalogResponseDto> subscriptionCatalog;
}
