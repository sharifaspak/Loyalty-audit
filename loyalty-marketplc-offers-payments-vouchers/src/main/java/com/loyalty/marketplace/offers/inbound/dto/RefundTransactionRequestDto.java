package com.loyalty.marketplace.offers.inbound.dto;

import lombok.Data;

@Data
public class RefundTransactionRequestDto {

	private String fromDate;
	private String toDate;
	private String status;

}
