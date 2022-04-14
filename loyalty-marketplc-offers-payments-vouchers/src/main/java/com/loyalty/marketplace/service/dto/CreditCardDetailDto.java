package com.loyalty.marketplace.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDetailDto {
	 private String authorizationCode;
	 private String orderId;
	 private String cardToken;
	 private String cardSubType;
	 private String epgTransactionId;
	 private String cardType;
	 private String cardNumber;
}
