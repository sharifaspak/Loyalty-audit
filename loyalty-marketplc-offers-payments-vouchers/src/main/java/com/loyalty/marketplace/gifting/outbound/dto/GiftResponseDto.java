package com.loyalty.marketplace.gifting.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class GiftResponseDto {

	private String id;
	private String giftType;
	private String promotionalGiftId;
	private Integer minPointValue;
	private Integer maxPointValue;
	private String subscriptionCatalogId;
	private String channelId;
	private List<OfferValueResponseDto> offerValues;
}	
