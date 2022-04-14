package com.loyalty.marketplace.subscription.crm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLineItems {
	
	
	private String clientReferenceId;
	private String subRequestId;
	private String statusCode;
	private String statusDesc;
	private String accountId;
	private String accountNumber;
	private String subscriptionId;
	private ResponseAdditionalInfo responseAdditionalInfo; 

}
