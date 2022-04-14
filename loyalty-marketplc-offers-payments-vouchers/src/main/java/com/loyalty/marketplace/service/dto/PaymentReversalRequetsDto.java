package com.loyalty.marketplace.service.dto;

import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReversalRequetsDto {

	private PurchaseRequestDto purchaseRequestDto;
	private String accountType;
	private String transactionDescription;
}
