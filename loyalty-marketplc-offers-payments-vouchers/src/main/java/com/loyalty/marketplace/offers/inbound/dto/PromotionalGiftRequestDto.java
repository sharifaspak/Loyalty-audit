package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PromotionalGiftRequestDto {
	
	@NotEmpty(message = "{validation.promotionalGiftRequestDto.promotionalGiftId.notEmpty.msg}")
	private String promotionalGiftId;
	@NotEmpty(message = "{validation.accountNumber.accountNumber.notEmpty.msg}")
	private String accountNumber;
}
