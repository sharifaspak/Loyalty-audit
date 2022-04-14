package com.loyalty.marketplace.voucher.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class HandbackFileDto {
	
	private String accountNumber;
	private String membershipCode;
	private String loyaltyId;
	private String externalTransactionId;
	private String denomination;
	private String status;
    private String error;
}
