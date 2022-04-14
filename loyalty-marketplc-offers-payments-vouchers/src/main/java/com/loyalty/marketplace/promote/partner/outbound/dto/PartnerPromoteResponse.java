package com.loyalty.marketplace.promote.partner.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerPromoteResponse {


	private String accountNumber;
	
	private String partnerCode;
	
	private String ContactNumber;

	private String ContactEmail;
	
	private String Message;
	
	private String dob;
	
	private String nationality;
	
	private String language;
}
