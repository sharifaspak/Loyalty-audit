package com.loyalty.marketplace.subscription.outbound.database.entity;

import java.util.Date;
import java.util.List;

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
public class SubscriptionPayment {

	@Id
	private String id;
	@Field("SubscriptionId")
	private String subscriptionId;
	@Field("PaymentMethodDetails")
	private List<PaymentMethodDetails> paymentMethodDetails;	
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	
	
}
