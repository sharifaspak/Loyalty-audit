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
public class BurnVoucher {
	
	private String transactionRefId;
	private String voucherCode;
	private String burnId;
	private String rollBackId;
	private Double rollBackAmount;
}
