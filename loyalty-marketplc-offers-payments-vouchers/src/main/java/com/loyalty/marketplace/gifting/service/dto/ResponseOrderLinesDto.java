package com.loyalty.marketplace.gifting.service.dto;

import java.util.List;

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
	private String activityCode;// renamed
	private String productName;
	@JsonIgnore
	private String productDescription;
	@JsonIgnore
	private Double amount;// renamed
	@JsonInclude(Include.NON_DEFAULT)
	@JsonIgnore
	private int quantity;
	@JsonIgnore
	private String pointsEngineOrderLineNumber;
	private String transactionId;
	private String transactionRefId;
	@JsonInclude(Include.NON_DEFAULT)
	private String transactionStatus;
	private String reason;
	@JsonIgnore
	private Integer code;
	// child
	private List<ChainedActivityEventDto> chainedActivities;

}
