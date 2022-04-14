package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.offers.inbound.dto.DenominationDto;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class DenominationResultResponse extends ResultResponse{
	
	private List<DenominationDto> denominations;
	
	public DenominationResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

}