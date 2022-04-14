package com.loyalty.marketplace.payment.inbound.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Accenture
 *
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RollbackAccrualOrRedemptionDto {

	private String transactionRefId;
	private String externalReferenceNumber;
	private String orderId;
	private String orderItemTransactionId;
	private String agentName;
}
