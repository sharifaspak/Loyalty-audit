package com.loyalty.marketplace.offers.member.management.outbound.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class CRMResponse {
	
	private String accountNumber; 
	private String firstName;
	private String lastName;
	private String customerTier;
	private String customerType;
	private String nationality;
}
