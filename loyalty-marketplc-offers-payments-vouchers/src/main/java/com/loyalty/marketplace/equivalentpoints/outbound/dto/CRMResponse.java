package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CRMResponse {
	
	private long accountNumber; 
	private String firstName;
	private String lastName;
	private String customerTier;
	private String customerType;
	private String nationality;
}
