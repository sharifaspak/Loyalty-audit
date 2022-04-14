package com.loyalty.marketplace.inbound.dto;

import lombok.Data;

@Data
public class BogoBulkCSVRequest {

	private String contentString;
	private String accountNumber;
	private String membershipCode;
	private String externalReferenceNumber;
	private boolean isValidContent;
}
