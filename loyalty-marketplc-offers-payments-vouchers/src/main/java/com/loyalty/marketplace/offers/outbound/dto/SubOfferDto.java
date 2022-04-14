package com.loyalty.marketplace.offers.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SubOfferDto {

	private String subOfferId;
	private String subOfferTitleEn;
	private String subOfferTitleAr;
	private String subOfferDescEn;
	private String subOfferDescAr;
	private Double oldCost; 
	private Integer oldPointValue;
	private Double newCost; 
	private Integer newPointValue;
		
}
