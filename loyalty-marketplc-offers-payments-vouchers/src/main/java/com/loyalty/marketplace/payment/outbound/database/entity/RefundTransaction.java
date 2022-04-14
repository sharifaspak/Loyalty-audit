package com.loyalty.marketplace.payment.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.constants.MarketplaceDBConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = MarketplaceDBConstants.REFUND_TRANSACTIONS)
public class RefundTransaction {

	@Id
	private String id;	
	
	@Field("TransactionId")
	private String transactionId;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("Amount")
	private String amount;
	@Field("EPGTransactionId")
	private String epgTransactionId;
	@Field("RefundStatus")
	private String refundStatus;
	@Field("ResponseMsg")
	private String responseMsg;
	@Field("ResponseCode")
	private String responseCode;
	@Field("EPGReversalDesc")
	private String epgRerversalDesc;
	@Field("EPGReversalCode")
	private String epgRerversalCode;
	@Field("EPGRefundCode")
	private String epgRefundCode;
	@Field("EPGRefundMsg")
	private String epgRefundMsg;
	@Field("FailureReason")
	private String errorMessage;
	@Field("IsNotificationTrigger")
	private Boolean isNotificationTrigger;
	@Field("CardNumber")
	private String cardNumber;
	@Field("CardToken")
	private String cardToken;
	@Field("CardSubType")
	private String cardSubType;
	@Field("CardType")
	private String cardType;
	@Field("AuthorizationCode")
	private String authorizationCode;
	@Field("OrderId")
	private String orderId;
	@Field("Msisdn")
	private String msisdn;
	@Field("Language")
	private String language;
	@Field("SelectedPaymentItem")
	private String selectedPaymentItem;
	@Field("SelectedPaymentOption")
	private String selectedPaymentOption;
	@Field("DirhamValue")
	private String dirhamValue;
//	@Field("IsFreeDelivery")
//	private Boolean isFreeDelivery;
	@Field("PointsValue")
	private String pointsValue;
	@Field("OfferId")
	private String offerId;
	@Field("VoucherDenomination")
	private String voucherDenomination;
	@Field("AccountType")
	private String accountType;
	@Field("Quantity")
	private Integer quantity;
	@Field("ProgramCode")
	private String programCode;
	@Field("ExternalTransactionId")
	private String externalTransactionId;
	@Field("PartialTransactionId")
	private String partialTransactionId;
	@Field("ReprocessedFlag")
	private boolean reprocessedFlag;
	@Field("TransactionDescription")
	private String transactionDescription;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("UpdatedDate")
	private Date dateLastUpdated;
	
}
