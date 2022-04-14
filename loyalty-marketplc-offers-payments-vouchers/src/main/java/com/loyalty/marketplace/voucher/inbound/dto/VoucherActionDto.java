package com.loyalty.marketplace.voucher.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VoucherActionDto {
	@NotEmpty(message = "{validation.VoucherActionDto.id.notEmpty.msg}")
	private String id;		
	private String action;	
	@NotEmpty(message = "{validation.VoucherActionDto.redemptionMethod.notEmpty.msg}")
	private String redemptionMethod;
	@NotEmpty(message = "{validation.VoucherActionDto.label.notEmpty.msg}")
	private String label;

}
