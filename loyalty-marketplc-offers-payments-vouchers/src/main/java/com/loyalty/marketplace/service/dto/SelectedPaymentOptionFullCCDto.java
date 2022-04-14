package com.loyalty.marketplace.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedPaymentOptionFullCCDto {
	 private String paymentType;
	 private String dirhamValue;
}
