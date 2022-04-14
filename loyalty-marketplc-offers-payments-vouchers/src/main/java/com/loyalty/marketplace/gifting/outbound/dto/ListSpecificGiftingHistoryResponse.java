package com.loyalty.marketplace.gifting.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class ListSpecificGiftingHistoryResponse extends ResultResponse {

	public ListSpecificGiftingHistoryResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	private ListSpecificGiftingHistoryResult giftHistoryResult;

}
