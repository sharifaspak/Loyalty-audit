package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PaymentMethodRequestDto {
	@Valid
	@NotEmpty(message="{validation.PaymentMethodRequestDto.paymentMethods.notEmpty.msg}")
	private List<PaymentMethodDto> paymentMethods;
}
