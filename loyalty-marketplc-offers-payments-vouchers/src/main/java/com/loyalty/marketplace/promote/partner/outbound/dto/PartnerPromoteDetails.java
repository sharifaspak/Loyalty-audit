package com.loyalty.marketplace.promote.partner.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerPromoteDetails {
	
	private String accountNumber;
	
	private String partnerCode;
	
	private String contactNumber;

	private String contactEmail;
	
	private String contactName;
	
	private String dob;
	
	private String nationality;
	
	private String language;
	
}
