package com.loyalty.marketplace.offers.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
@JsonInclude(Include.NON_NULL)
public class PurchaseResultResponse extends ResultResponse{
	
	private PurchaseResponseDto purchaseResponseDto;
	
	public PurchaseResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public PurchaseResponseDto getPurchaseResponseDto() {
		return purchaseResponseDto;
	}

	public void setPurchaseResponseDto(PurchaseResponseDto purchaseResponseDto) {
		this.purchaseResponseDto = purchaseResponseDto;
	}

	@Override
	public String toString() {
		return "PurchaseResultResponse [purchaseResponseDto=" + purchaseResponseDto + "]";
	}

	
}
