package com.loyalty.marketplace.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedPaymentOptionPartialPointCCDto {
	 private String paymentType;
	 private String dirhamValue;
	 private String pointsValue;
	 private String partialTransactionId;
}
