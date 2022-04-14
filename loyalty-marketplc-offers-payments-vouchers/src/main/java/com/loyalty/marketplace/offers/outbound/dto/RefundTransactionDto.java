package com.loyalty.marketplace.offers.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class RefundTransactionDto {

	private String id;
	private String transactionId;
	private String programCode;
	private String accountNumber;
	private String amount;
	private String epgTransactionId;
	private String refundStatus;
	private String responseMsg;
	private String responseCode;
	private String epgRerversalDesc;
	private String epgRerversalCode;
	private String epgRefundCode;
	private String epgRefundMsg;
	private String errorMessage;
	private boolean isNotificationTrigger;
	private String cardNumber;
	private String cardToken;
	private String cardSubType;
	private String cardType;
	private String authorizationCode;
	private String orderId;
	private String msisdn;
	private String language;
	private String selectedPaymentItem;
	private String selectedPaymentOption;
	private String dirhamValue;
	private String pointsValue;
	private boolean isFreeDelivery;
	private String offerId;
	private String voucherDenomination;
	private String accountType;
	private Integer quantity;
	private String externalTransactionId;
	private String partialTransactionId;
	private boolean reprocessedFlag;
	private String transactionDescription;
	private Date createdDate;
	private String createdUser;
	private String updatedUser;
	private Date dateLastUpdated;
}
