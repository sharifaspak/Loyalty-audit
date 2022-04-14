package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OfferTypeDto {

	String offerTypeId;
	@NotEmpty(message="{validation.OfferTypeDto.descriptionEn.notEmpty.msg}")
	private String descriptionEn;
	private String descriptionAr;
	private List<String> paymentMethods;
}
