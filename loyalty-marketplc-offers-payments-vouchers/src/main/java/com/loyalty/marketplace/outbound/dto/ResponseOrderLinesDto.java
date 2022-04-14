package com.loyalty.marketplace.outbound.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseOrderLinesDto {

	private String orderLineNumber;
	private String activityCode;//renamed
	private String productName;
	private String productDescription;
	private Double amount;//renamed
	@JsonInclude(Include.NON_DEFAULT)
	private int quantity;
	private String pointsEngineOrderLineNumber;
	private String transactionId;
	private String transactionRefId;
	@JsonInclude(Include.NON_DEFAULT)
	private String transactionStatus;
	private String reason;
	@JsonIgnore
	private Integer code;

}
