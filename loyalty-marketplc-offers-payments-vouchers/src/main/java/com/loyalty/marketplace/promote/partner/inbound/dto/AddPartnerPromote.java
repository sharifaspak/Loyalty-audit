package com.loyalty.marketplace.promote.partner.inbound.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddPartnerPromote {

	@NotEmpty(message = "PartnerCode should not be empty or null")
	private String partnerCode;
	
	@NotEmpty(message = "accountNumber should not be empty or null")
	private String accountNumber;

	@NotEmpty(message = "contactName should not be empty or null")
	private String contactName;
	
	@NotEmpty(message = "contactNumber should not be empty or null")
	private String contactNumber;
	
	@NotEmpty(message = "contactEmail should not be empty or null")
	@Email
	private String contactEmail;
	
	private String dob;
	
	private String nationality;
	
	private String language;
}
