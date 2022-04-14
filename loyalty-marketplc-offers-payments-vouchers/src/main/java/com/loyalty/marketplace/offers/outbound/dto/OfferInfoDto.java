package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

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
public class OfferInfoDto {
	
	private String offerId;
	private String offerTypeId;
	private String offerTypeDescriptionEn;
	private String offerTypeDescriptionAr;
	private String offerTitleEn;
	private String offerTitleAr;
	private Integer pointsValue;
	private Integer dirhamValue;
	private List<SubOfferDto> subOffer;
}
