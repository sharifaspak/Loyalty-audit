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
public class PurchasePaymentMethodRequestDto {
	@Valid
	@NotEmpty(message="{validation.PurchasePaymentMethodRequestDto.purchasePaymentMethods.notEmpty.msg}")
	private List<PurchasePaymentMethodDto> purchasePaymentMethods;

}
