package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
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
@JsonInclude(Include.NON_NULL)
public class OfferCatalogShortResultResponse extends ResultResponse{
	
	private Integer totalRecordCount;
	private List<OfferCatalogShortResponseDto> offerList;

	public OfferCatalogShortResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

}