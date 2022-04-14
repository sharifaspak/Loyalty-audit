package com.loyalty.marketplace.payment.inbound.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PaymentPostingTIBCODto {
	
	private String paidTotalAmount; 
	private String paymentType; 
	private long accountId;
	private String accountNmbr; 
	private String transactionType; 
	private String referenceNmbr;
	private String cardNmbr;
	private String cardToken;
	private String cardExpiryDt; 
	private String authorizationCode; 
	private String paidAmount; 
	private String epgTransactionId;
	private String cardSubType;
	private String membershipCode; 
	private String loyaltyPoints;

}
