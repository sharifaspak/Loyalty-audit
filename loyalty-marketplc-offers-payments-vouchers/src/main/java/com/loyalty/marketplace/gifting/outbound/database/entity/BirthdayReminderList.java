package com.loyalty.marketplace.gifting.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class BirthdayReminderList {

	@Field("AccountNumber")
	private String accountNumber;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("FirstName")
	private String firstName;
	
	@Field("LastName")
	private String lastName;
	
	@Field("RemindPrior")
	private String remindPrior;
	
	@Field("Status")
	private String status;
	
}
