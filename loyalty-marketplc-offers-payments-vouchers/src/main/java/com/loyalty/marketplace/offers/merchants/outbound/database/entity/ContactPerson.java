package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

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
public class ContactPerson {

	@Field("EmailId")
	private String emailId;	
	
	@Field("Firstname")
	private String firstName;	
	
	@Field("LastName")
	private String lastName;
	
	@Field("Mobile")
	private String mobileNumber;
	
	@Field("Fax")
	private String faxNumber;
	
	@Field("UserName")
	private String userName;
	
}
