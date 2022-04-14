package com.loyalty.marketplace.offers.inbound.dto;

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
public class PaymentMethodDto {

	private String paymentMethodId;
	
	@NotEmpty(message="{validation.PaymentMethodDto.description.notEmpty.msg}")
	private String description;
	

}
