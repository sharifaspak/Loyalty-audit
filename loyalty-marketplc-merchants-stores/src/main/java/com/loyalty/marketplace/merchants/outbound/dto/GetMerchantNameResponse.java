package com.loyalty.marketplace.merchants.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class GetMerchantNameResponse extends ResultResponse {

	GetMerchantNameResult merchantName;

	public GetMerchantNameResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
}
