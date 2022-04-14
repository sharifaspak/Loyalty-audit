package com.loyalty.marketplace.voucher.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetails {

	@Field("CardNumber")
	private String cardNumber;

	@Field("CardType")
	private String cardType;

	@Field("CardSubType")
	private String cardSubType;

	@Field("CardToken")
	private String cardToken;

	@Field("CardExpiryDate")
	private String cardExpiryDate;

	@Field("AuthorizationCode")
	private String authorizationCode;

	@Field("SpentAmount")
	private Double spentAmount;	

}
