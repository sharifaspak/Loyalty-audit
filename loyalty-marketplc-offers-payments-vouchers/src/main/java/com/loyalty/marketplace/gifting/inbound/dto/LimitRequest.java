package com.loyalty.marketplace.gifting.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LimitRequest {

	private Double senderDayLimit;
	
	private Double senderWeekLimit;
	
	private Double senderMonthLimit;
	
	private Double receiverDayLimit;
	
	private Double receiverWeekLimit;
	
	private Double receiverMonthLimit;
	
}
