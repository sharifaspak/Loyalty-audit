package com.loyalty.marketplace.offers.merchants.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@AllArgsConstructor
public class MerchantContactPersonDomain {
	
	private String emailId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String faxNumber;
	private String userName;
	private String password;

}
