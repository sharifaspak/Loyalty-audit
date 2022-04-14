package com.loyalty.marketplace.voucher.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BurnCashVoucher {
	
	@NotEmpty(message = "{validation.MambaBurnRequestObject.voucherCode.notEmpty.msg}")
	private String voucherCode;	
	@NotNull(message = "{validation.MambaBurnRequestObject.amountToDeduct.notEmpty.msg}")
	private Double amountToDeduct;
	private String orderId;
}
