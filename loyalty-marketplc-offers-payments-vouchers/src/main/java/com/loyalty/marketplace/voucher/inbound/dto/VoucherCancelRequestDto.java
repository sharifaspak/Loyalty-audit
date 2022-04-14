package com.loyalty.marketplace.voucher.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VoucherCancelRequestDto {
	
	@NotNull(message = "{validation.voucherRequestDto.voucherPurchaseId.notEmpty.msg}")
	private String purchaseId;
	@NotEmpty(message = "{validation.voucherRequestDto.voucherAction.notEmpty.msg}")
	private String action;
	private String voucherCode;
	private String actionReason;

}
