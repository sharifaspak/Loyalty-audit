package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class CuisinesResultResponse extends ResultResponse {
	
	List<CuisinesResponseDto> cuisines;
	
	public CuisinesResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<CuisinesResponseDto> getCuisines() {
		return cuisines;
	}

	public void setCuisines(List<CuisinesResponseDto> cuisines) {
		this.cuisines = cuisines;
	}

}
