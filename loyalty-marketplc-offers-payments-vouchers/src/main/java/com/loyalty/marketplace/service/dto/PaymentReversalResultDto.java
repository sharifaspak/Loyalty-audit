package com.loyalty.marketplace.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentReversalResultDto {
	private String responseCode;
	private String responseMsg;
	private String transactionId;
	private String msisdn;
	private String epgRerversalCode;
	private String epgRerversalDesc;
	private String epgRefundCode;
	private String epgRefundMsg;
	private String errorMessage;
	private Boolean smsNotificationTrigger;
}