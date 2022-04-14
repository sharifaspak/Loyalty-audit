package com.loyalty.marketplace.subscription.inbound.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CCDetails {
	
	private String cardNumber;
	private String subType;
	private String authorizationCode;
	private String cardToken;

}
