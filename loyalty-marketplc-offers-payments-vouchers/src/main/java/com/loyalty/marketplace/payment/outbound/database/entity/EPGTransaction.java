package com.loyalty.marketplace.payment.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "EPGTransaction")
public class EPGTransaction {

	@Id
	private String id;	
	
	@Field("OrderId")
	private String orderId;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("Amount")
	private String amount;
	@Field("PurchaseItem")
	private String purchaseItem;
	@Field("SubscriptionId")
	private String subscriptionId;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
	@Field("EPGStatus")
	private String epgStatus;
	@Field("MasterEPGTransactionId")
	private String masterEPGTransactionId;
	@Field("ResponseCode")
	private String responseCode;
	@Field("ResponseClass")
	private String responseClass;
	@Field("ResponseDescription")
	private String responseDescription;
	@Field("ResponseClassDescription")
	private String responseClassDescription;
	@Field("Language")
	private String language;
	@Field("EPGTransactionId")
	private String epgTransactionId;
	@Field("ApprovalCode")
	private String approvalCode;
	@Field("Account")
	private String account;
	@Field("Balance")
	private String balance;
	@Field("Fees")
	private String fees;
	@Field("CardNumber")
	private String cardNumber;
	@Field("Payer")
	private String payer;
	@Field("CardToken")
	private String cardToken;
	@Field("CardBrand")
	private String cardBrand;
	@Field("CardType")
	private String cardType;
	@Field("UniqueId")
	private String uniqueId;
	@Field("PaymentType")
	private String paymentType;
	@Field("ProgramCode")
	private String programCode;
	@Field("ExternalTransactionId")
	private String externalTransactionId;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("UpdatedDate")
	private Date dateLastUpdated;
	
	
}
