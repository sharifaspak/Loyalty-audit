package com.loyalty.marketplace.gifting.outbound.dto;

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
public class ListGoldTransactionResponse extends ResultResponse {

	public ListGoldTransactionResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	private ListGoldTransactionResult goldTransaction;
	
}
