package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class OfferRatingDto {

	@NotBlank(message = "{validation.OfferRatingDto.offerId.notBlank.msg}")
	private String offerId;
	
	@NotBlank(message = "{validation.OfferRatingDto.accountNumber.notBlank.msg}")
	private String accountNumber;
	
	@NotNull(message = "{validation.OfferRatingDto.rating.notBlank.msg}")
	@Min(value=1, message="{validation.OfferRatingDto.rating.min.msg}")
	@Max(value=5, message="{validation.OfferRatingDto.rating.max.msg}")
	private Integer rating;
	
	private String comment;
	
}
