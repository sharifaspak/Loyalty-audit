package com.loyalty.marketplace.voucher.outbound.dto;

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
public class VoucherReconciliationDtoResponse extends ResultResponse {
	public VoucherReconciliationDtoResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

}
