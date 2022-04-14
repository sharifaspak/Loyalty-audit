package com.loyalty.marketplace.offers.stores.outbound.database.entity;

import org.springframework.data.annotation.Transient;
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
 
	@Field("MobileNumber")
	private String mobileNumber;
 
	@Field("FirstName")
	private String firstName;
 
	@Field("LastName")
	private String lastName;
    
	@Field("Fax")
    private String faxNumber;     
    
	@Field("UserName")
    private String userName;
    
	@Field("Pin")
    private Integer pin;
    
    @Transient
    private String password;
    
}
