package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TermsAndConditionsDto {
	@NotNull(message = "{validation.termsAndConditions.notNull.msg}")
	@NotEmpty(message = "{validation.termsAndConditions.notEmpty.msg}")
	private String termsAndConditionsEn;
	
	@NotNull(message = "{validation.termsAndConditions.notNull.msg}")
	@NotEmpty(message = "{validation.termsAndConditions.notEmpty.msg}")
	private String termsAndConditionsAr;
}
