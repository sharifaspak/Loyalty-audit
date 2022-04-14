package com.loyalty.marketplace.voucher.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VoucherTransferRequest {
	
	@NotEmpty(message = "{validation.VoucherTransferRequest.agentName.notEmpty.msg}")
	private String agentName;
	@NotEmpty(message = "{validation.VoucherTransferRequest.targetAccountNumber.notEmpty.msg}")
	private String targetAccountNumber;
	@NotEmpty(message = "{validation.VoucherTransferRequest.voucherCode.notEmpty.msg}")
	private String voucherCode;
	private String customerType;
	private boolean isGift;
	private String isExternalUser;
	private String email;

}
