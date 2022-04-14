package com.loyalty.marketplace.subscription.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "Subscription")
@Setter
@Getter
@ToString
public class SubscriptionShort {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("Status")
	private String status;
	@Field("LastChargedAmount")
	private Double lastChargedAmount;
	@Field("LastChargedDate")
	private Date lastChargedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	
	
}
