package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.Min;

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
public class SubOfferDto {

	private String subOfferId;
	private String subOfferTitleEn;
	private String subOfferTitleAr;
	private String subOfferDescEn;
	private String subOfferDescAr;
	private Double oldCost;
	@Min(value=0, message="{validation.subOfferDto.oldPointValue.min.msg}")
	private Integer oldPointValue;
	private Double newCost;
	@Min(value=0, message="{validation.subOfferDto.newPointValue.min.msg}")
	private Integer newPointValue;
		
}
