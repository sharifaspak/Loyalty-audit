package com.loyalty.marketplace.offers.outbound.dto;

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
@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class PromotionalGiftResultResponse extends ResultResponse{
	
	private PromotionalGiftResponseDto promotionalGiftResponseDto;
	
	public PromotionalGiftResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

}
