package com.loyalty.marketplace.subscription.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "SubscriptionPayment")
@Setter
@Getter
@ToString
public class CCDetails {

	@Field("cardNumber")
	private String cardNumber;
	@Field("SubType")
	private String subType;	
}
