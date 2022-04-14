package com.loyalty.marketplace.equivalentpoints.inbound.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeaderDto {

	private String program;
	private String channelId;
	private String userName;
	private String role;
	private String systemId;
	private String externalTransactionId;
	private String token;
}
