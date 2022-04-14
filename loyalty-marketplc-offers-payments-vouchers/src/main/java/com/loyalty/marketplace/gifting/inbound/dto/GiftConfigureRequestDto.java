package com.loyalty.marketplace.gifting.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GiftConfigureRequestDto {

	@NotBlank(message = "{validation.giftRequestDto.giftType.notBlank.msg}")
	private String giftType;
	@Min(value=1, message="{validation.giftRequestDto.minPointValue.min.msg}")
	private Integer minPointValue;
	@Min(value=1, message="{validation.giftRequestDto.maxPointValue.min.msg}")
	private Integer maxPointValue;
	private String subscriptionCatalogId;
	private String promotionalGiftId;
	private String channelId;
	@Valid
	private List<OfferValueDto> offerValues;
}	
