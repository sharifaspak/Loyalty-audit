package com.loyalty.marketplace.gifting.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GiftUpdateRequestDto {

	@Valid
	@NotEmpty(message = "{validation.giftUpdateRequestDto.offerValues.notEmpty.msg}")
	private List<OfferValueDto> offerValues;
}	
