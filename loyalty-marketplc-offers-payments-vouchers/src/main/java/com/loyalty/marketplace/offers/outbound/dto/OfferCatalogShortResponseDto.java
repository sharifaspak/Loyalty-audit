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
public class OfferCatalogShortResponseDto {
	
	private String offerId;
	private String offerCode;
	private String offerTypeId;
	private String typeDescriptionEn;
	private String typeDescriptionAr;
	private String offerTitleEn;
	private String offerTitleAr;
	private String offerTitleDescriptionEn;
	private String offerTitleDescriptionAr;
	private String status;
	private List<String> availableInPortals;
	private String merchantCode;
		
}
