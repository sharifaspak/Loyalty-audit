package com.loyalty.marketplace.banners.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationRequest {
	
	private String username;
	private String password;
	private String channel;

}
