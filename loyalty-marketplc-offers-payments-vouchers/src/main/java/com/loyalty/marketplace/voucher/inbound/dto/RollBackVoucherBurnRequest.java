package com.loyalty.marketplace.voucher.inbound.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RollBackVoucherBurnRequest {
	
	private String voucherCode;
	private String transactionId;
}
