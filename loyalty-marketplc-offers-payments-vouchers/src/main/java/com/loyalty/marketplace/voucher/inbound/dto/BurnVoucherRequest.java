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
public class BurnVoucherRequest {
	
	@NotEmpty(message = "{validation.BurnVoucherRequest.voucherCode.notEmpty.msg}")
	private List<String> voucherCodes;	
	private String invoiceId;
	private String remarks;	
	private Integer storePin;
	private String partnerCode;
	private String merchantCode;
}
