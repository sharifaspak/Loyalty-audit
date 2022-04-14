package com.loyalty.marketplace.gifting.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftValues {
	
	@Field("MinPointValue")
	private Integer minPointValue;
	@Field("MaxPointValue")
	private Integer maxPointValue;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
	@Field("PromotionalGiftId")
	private String promotionalGiftId;
	@Field("ChannelId")
	private String channelId;

}
