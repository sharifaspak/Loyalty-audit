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
public class BirthdayInfoResultResponse extends ResultResponse{
	
	private BirthdayInfoResponseDto birthdayInfoResponseDto;
	
	public BirthdayInfoResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public BirthdayInfoResponseDto getBirthdayInfoResponseDto() {
		return birthdayInfoResponseDto;
	}

	public void setBirthdayInfoResponseDto(BirthdayInfoResponseDto birthdayInfoResponseDto) {
		this.birthdayInfoResponseDto = birthdayInfoResponseDto;
	}

	@Override
	public String toString() {
		return "BirthdayInfoResultResponse [birthdayInfoResponseDto=" + birthdayInfoResponseDto + "]";
	}

	

	
}
