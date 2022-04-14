package com.loyalty.marketplace.gifting.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class PurchaseDetailsResponse {

	private String cardNumber;
	
	private String cardType;
	
	private String cardSubType;
	
	private String cardToken;
	
	private String cardExpiryDate;
	
	private String authorizationCode;
	
	private Double spentAmount;
	
	private String epgTransactionId;
	
	private String uiLanguage;
	
	private String paymentTransactionNo;
	
	private Date paymentTransactionDate;	
	
	private String extRefNo;

}
