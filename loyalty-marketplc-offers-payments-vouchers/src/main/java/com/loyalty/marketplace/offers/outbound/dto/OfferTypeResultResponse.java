package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
public class OfferTypeResultResponse extends ResultResponse{
	
	private List<OfferTypesDto> offerTypes;
	
	public OfferTypeResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<OfferTypesDto> getOfferTypes() {
		return offerTypes;
	}

	public void setOfferTypes(List<OfferTypesDto> offerTypes) {
		this.offerTypes = offerTypes;
	}
	
	@Override
	public String toString() {
		return "OfferTypeResultResponse [offerTypes=" + offerTypes + "]";
	}

}