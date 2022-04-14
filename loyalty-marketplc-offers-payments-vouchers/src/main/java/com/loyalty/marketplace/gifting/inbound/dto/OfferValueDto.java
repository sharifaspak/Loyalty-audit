package com.loyalty.marketplace.gifting.inbound.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OfferValueDto {
	
	@NotBlank(message = "{validation.offerValueDto.offerId.notBlank.msg}")
	private String offerId;
	@NotBlank(message = "{validation.offerValueDto.offerType.notBlank.msg}")
	private String offerType;
	private String subOfferId;
	@Min(value=1, message="{validation.offerValueDto.denomination.min.msg}")
	private Integer denomination;
	@Min(value=1, message="{validation.offerValueDto.couponQuantity.min.msg}")
	private Integer couponQuantity;
	@NotBlank(message = "{validation.offerValueDto.action.notBlank.msg}")
	private String action;
	
}
