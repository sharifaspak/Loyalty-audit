package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BirthdayAccountsDto {

	@Field(value = "AccountNumber")	
	private String accountNumber;
	@Field(value = "EmailId")
	private String email;
	@Field(value = "Language")
	private String language;
	@Field(value = "UiLanguage")
	private String uiLanguage;
	@Field(value = "MembershipCode")
	private String membershipCode;
	@Field(value = "FirstName")
	private String firstName;
	@Field(value = "LastName")
	private String lastName;
	@Field(value = "DOB")
	private Date dob;
	@Field(value = "Status.Code")
	private String status;
}
