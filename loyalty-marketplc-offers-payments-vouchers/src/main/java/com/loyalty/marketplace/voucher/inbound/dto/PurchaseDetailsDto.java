package com.loyalty.marketplace.voucher.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PurchaseDetailsDto {

	private String cardNumber;
	private String cardType;
	private String cardSubType;
	private String cardToken;
	private String cardExpiryDate;
	private String authorizationCode;
	private Double spentAmount;
}
