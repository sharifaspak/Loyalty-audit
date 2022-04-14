package com.loyalty.marketplace.voucher.domain;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class PurchaseDetailsDomain {

	@Autowired
	ModelMapper modelMapper;

	private String cardNumber;
	private String cardType;
	private String cardSubType;
	private String cardToken;
	private String cardExpiryDate;
	private String authorizationCode;
	private Double spentAmount;

	public String getCardNumber() {
		return cardNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public String getCardSubType() {
		return cardSubType;
	}

	public String getCardToken() {
		return cardToken;
	}

	public String getCardExpiryDate() {
		return cardExpiryDate;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public Double getSpentAmount() {
		return spentAmount;
	}

	public PurchaseDetailsDomain() {

	}

	public PurchaseDetailsDomain(PurchaseDetailsBuilder purchaseDetailsBuilder) {
		this.cardNumber = purchaseDetailsBuilder.cardNumber;
		this.cardType = purchaseDetailsBuilder.cardType;
		this.cardSubType = purchaseDetailsBuilder.cardSubType;
		this.cardToken = purchaseDetailsBuilder.cardToken;
		this.cardExpiryDate = purchaseDetailsBuilder.cardExpiryDate;
		this.authorizationCode = purchaseDetailsBuilder.authorizationCode;
		this.spentAmount = purchaseDetailsBuilder.spentAmount;
	}

	public static class PurchaseDetailsBuilder {
		private String cardNumber;
		private String cardType;
		private String cardSubType;
		private String cardToken;
		private String cardExpiryDate;
		private String authorizationCode;
		private Double spentAmount;

		public PurchaseDetailsBuilder(String cardNumber, String cardType, String cardSubType, String cardToken,
				String cardExpiryDate, String authorizationCode, Double spentAmount) {
			super();
			this.cardNumber = cardNumber;
			this.cardType = cardType;
			this.cardSubType = cardSubType;
			this.cardToken = cardToken;
			this.cardExpiryDate = cardExpiryDate;
			this.authorizationCode = authorizationCode;
			this.spentAmount = spentAmount;
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

		public PurchaseDetailsDomain build() {
			return new PurchaseDetailsDomain(this);
		}

	}

}
