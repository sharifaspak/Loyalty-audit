package com.loyalty.marketplace.gifting.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class LimitsResponse {

	private Double senderDayLimit;
	
	private Double senderWeekLimit;
	
	private Double senderMonthLimit;
	
	private Double receiverDayLimit;
	
	private Double receiverWeekLimit;
	
	private Double receiverMonthLimit;
	
}
