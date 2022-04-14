package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

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
public class RefundTransactionResponse extends ResultResponse{
	
	private List<RefundTransactionDto> refundTransactionList;
	
	public RefundTransactionResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
}
