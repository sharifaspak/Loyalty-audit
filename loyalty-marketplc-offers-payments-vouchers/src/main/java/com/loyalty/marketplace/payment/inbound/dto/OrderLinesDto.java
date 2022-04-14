package com.loyalty.marketplace.payment.inbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderLinesDto {

	private String orderLineNumber;
	private String activityCode;// renamed
	private String productName;
	private String productDescription;
	private Double amount;// renamed
	@JsonInclude(Include.NON_DEFAULT)
	private int quantity;
	private String pointsEngineOrderLineNumber;
	private String transactionId;
	private String transactionRefId;

}
