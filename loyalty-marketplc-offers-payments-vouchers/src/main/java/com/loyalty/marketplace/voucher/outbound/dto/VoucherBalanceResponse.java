package com.loyalty.marketplace.voucher.outbound.dto;

import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VoucherBalanceResponse extends ResultResponse{

	public VoucherBalanceResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	private String voucherBalance;

}
