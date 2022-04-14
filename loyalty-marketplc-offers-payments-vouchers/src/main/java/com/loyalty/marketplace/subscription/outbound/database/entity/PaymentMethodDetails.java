package com.loyalty.marketplace.subscription.outbound.database.entity;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "SubscriptionPayment")
@Setter
@Getter
@ToString
public class PaymentMethodDetails {

	@Id
	@Field("PaymentMethodId")
	private String paymentMethodId;
	@Field("PaymentMethod")
	private String paymentMethod;
	@Field("MasterEPGTransactionId")
	private String masterEPGTransactionId;
	@Field("EpgTransactionID")
	private String epgTransactionID;
	@Field("CCDetails")
	private CCDetails ccDetails;
	@Field("Status")
	private String status;
	@Field("InactiveDate")
	private Date inactiveDate;
	
}
