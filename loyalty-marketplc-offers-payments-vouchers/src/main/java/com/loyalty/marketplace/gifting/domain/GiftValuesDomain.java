package com.loyalty.marketplace.gifting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GiftValuesDomain {
	
	private Integer minPointValue;
	private Integer maxPointValue;
	private String subscriptionCatalogId;
	private String promotionalGiftId;
	private String channelId;
}
