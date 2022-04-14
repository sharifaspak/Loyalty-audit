package com.loyalty.marketplace.voucher.inbound.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BurnCashVoucherRequest {	
	@NotEmpty(message = "{validation.MambaBurnVoucherRequest.mambaBurnRequestObject.notEmpty.msg}")
	private List<BurnCashVoucher> burnCashVoucher;	
}
