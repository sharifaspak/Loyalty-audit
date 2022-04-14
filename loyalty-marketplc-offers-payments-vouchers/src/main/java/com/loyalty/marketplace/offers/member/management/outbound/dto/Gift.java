package com.loyalty.marketplace.offers.member.management.outbound.dto;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class Gift {

	@Field("GiftType")
	private String giftType;
	@Field("GiftId")
	private String giftId;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
	@Field("FreeDuration")
	private Integer freeDuration;
}
