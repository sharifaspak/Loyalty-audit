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
public class VoucherBalanceRequestDto {

	@NotNull(message = "{validation.voucherBalanceRequestDto.crfrTransId.notEmpty.msg}")
	private String crfrTransId;
	@NotEmpty(message = "{validation.voucherBalanceRequestDto.cardNumber.notEmpty.msg}")
	private String cardNumber;
	@NotEmpty(message = "{validation.voucherRequestDto.accountNumber.notEmpty.msg}")
	private String accountNumber;
	
	private int amount;
	@NotEmpty(message = "{validation.voucherRequestDto.UUID.notEmpty.msg}")
	private String uuid;
}
