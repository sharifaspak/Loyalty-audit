package com.loyalty.marketplace.banners.outbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthorizationResponse {
	
	private String token;
	private String responseCode;
	private String responseMsg;

}
