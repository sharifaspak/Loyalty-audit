package com.loyalty.marketplace.stores.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class ContactPersonResponseDto {

	private String emailId;	
	private String firstName;	
	private String lastName;
	private String mobileNumber;
	private String faxNumber;
	private String userName;
	private Integer pin;
	
}
