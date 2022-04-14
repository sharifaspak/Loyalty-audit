package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
public class TransactionsResultResponse extends ResultResponse{
	
	private List<TransactionListDto> transactionList;
	
	public TransactionsResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	
}
