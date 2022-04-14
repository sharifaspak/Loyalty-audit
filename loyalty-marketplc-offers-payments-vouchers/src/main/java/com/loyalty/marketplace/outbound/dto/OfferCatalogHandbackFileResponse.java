package com.loyalty.marketplace.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class OfferCatalogHandbackFileResponse extends ResultResponse {
	
	private List<OfferCatalogHandbackFileResponseDto> offerCatalogHandbackFileResponseList;
	
	public OfferCatalogHandbackFileResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
}
