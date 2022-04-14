package com.loyalty.marketplace.offers.stores.domain.model;

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
public class StoreContactPersonDomain {

	private String emailId;
    private String mobileNumber;
	private String firstName;
	private String lastName;
	private String faxNumber;
	private String userName;
	private String password;
	
}
