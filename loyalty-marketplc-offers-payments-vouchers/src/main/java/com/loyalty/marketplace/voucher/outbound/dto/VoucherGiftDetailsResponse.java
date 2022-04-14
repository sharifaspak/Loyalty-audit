package com.loyalty.marketplace.voucher.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.gifting.outbound.dto.GiftingHistoryResult;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class VoucherGiftDetailsResponse extends ResultResponse{

	public VoucherGiftDetailsResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	private GiftingHistoryResult voucherGiftResult;

}
