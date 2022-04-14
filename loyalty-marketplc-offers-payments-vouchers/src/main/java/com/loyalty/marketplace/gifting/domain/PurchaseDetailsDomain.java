package com.loyalty.marketplace.gifting.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class PurchaseDetailsDomain {

	private String cardNumber;
	private String cardType;
	private String cardSubType;
	private String cardToken;
	private String cardExpiryDate;
	private String authorizationCode;
	private Double spentAmount;
	private String epgTransactionId;
	private String uiLanguage;

	public PurchaseDetailsDomain(PurchaseDetailsBuilder purchaseDetailsBuilder) {
		this.cardNumber = purchaseDetailsBuilder.cardNumber;
		this.cardType = purchaseDetailsBuilder.cardType;
		this.cardSubType = purchaseDetailsBuilder.cardSubType;
		this.cardToken = purchaseDetailsBuilder.cardToken;
		this.cardExpiryDate = purchaseDetailsBuilder.cardExpiryDate;
		this.authorizationCode = purchaseDetailsBuilder.authorizationCode;
		this.spentAmount = purchaseDetailsBuilder.spentAmount;
		this.epgTransactionId =purchaseDetailsBuilder.epgTransactionId;
		this.uiLanguage =purchaseDetailsBuilder.uiLanguage;
	}

	public static class PurchaseDetailsBuilder {
		
		private String cardNumber;
		private String cardType;
		private String cardSubType;
		private String cardToken;
		private String cardExpiryDate;
		private String authorizationCode;
		private Double spentAmount;
		private String epgTransactionId;
		private String uiLanguage;

		public PurchaseDetailsBuilder(String cardNumber, String cardType, String cardSubType, String cardToken,
				String cardExpiryDate, String authorizationCode, Double spentAmount,String epgTransactionId,String uiLanguage ) {
			super();
			this.cardNumber = cardNumber;
			this.cardType = cardType;
			this.cardSubType = cardSubType;
			this.cardToken = cardToken;
			this.cardExpiryDate = cardExpiryDate;
			this.authorizationCode = authorizationCode;
			this.spentAmount = spentAmount;
			this.epgTransactionId =epgTransactionId;
			this.uiLanguage = uiLanguage;
		}

		public PurchaseDetailsBuilder cardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
			return this;
		}

		public PurchaseDetailsBuilder cardType(String cardType) {
			this.cardType = cardType;
			return this;
		}

		public PurchaseDetailsBuilder cardSubType(String cardSubType) {
			this.cardSubType = cardSubType;
			return this;
		}

		public PurchaseDetailsBuilder cardToken(String cardToken) {
			this.cardToken = cardToken;
			return this;
		}

		public PurchaseDetailsBuilder cardExpiryDate(String cardExpiryDate) {
			this.cardExpiryDate = cardExpiryDate;
			return this;
		}

		public PurchaseDetailsBuilder authorizationCode(String authorizationCode) {
			this.authorizationCode = authorizationCode;
			return this;
		}

		public PurchaseDetailsBuilder spentAmount(Double spentAmount) {
			this.spentAmount = spentAmount;
			return this;
		}
		
		public PurchaseDetailsBuilder epgTransactionId(String epgTransactionId) {
			this.epgTransactionId = epgTransactionId;
			return this;
		}
		
		public PurchaseDetailsBuilder uiLanguage(String uiLanguage) {
			this.uiLanguage = uiLanguage;
			return this;
		}

		public PurchaseDetailsDomain build() {
			return new PurchaseDetailsDomain(this);
		}

	}

}
