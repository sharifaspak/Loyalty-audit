package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

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
public class PurchasePaymentMethodDto {

	@NotEmpty(message="{validation.PurchasePaymentMethodDto.purchaseItem.notEmpty.msg}")
	private String purchaseItem;
	private List<String> paymentMethods;
}
