package com.loyalty.marketplace.payment.inbound.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PaymentRequestDto {
	private String accountNumber;
	private String membershipCode;
	private String authorizationCode;
	private int pointsValue;
	private double cost;
	private String atgUsername;
	private String cardNumber;
	private String cardType;
	private String cardToken;
	private String cardSubType;
	private String cardExpiryDate;
	private String extTransactionId;
	private String epgTransactionId;
	private String language;
	private String offerId;
	private String paymentType;
	private String paymentItem;
	private String paymentMethod;
	private int couponQuantity;
	private String merchantName;
	private String merchantCode;
	private String barcodeId;
	private String subOfferId;
	private String denominationId;
	private String voucherAction;
	private String partnerCode;
	private int voucherExpiryPeriod;
	private String channelId;
	private String activityCode;
	private String uuid;

}
