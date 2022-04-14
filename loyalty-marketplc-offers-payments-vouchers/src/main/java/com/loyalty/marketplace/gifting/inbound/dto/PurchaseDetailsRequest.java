package com.loyalty.marketplace.gifting.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PurchaseDetailsRequest {

	private String cardNumber;
	private String cardType;
	private String cardSubType;
	private String cardToken;
	private String cardExpiryDate;
	private String authorizationCode;	
	private Double spentAmount;
	private String epgTransactionId;
	private String uiLanguage;

}
