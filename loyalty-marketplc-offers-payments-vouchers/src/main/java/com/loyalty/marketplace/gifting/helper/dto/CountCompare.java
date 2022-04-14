package com.loyalty.marketplace.gifting.helper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CountCompare {

	private Double sentDayCount;
	
	private Double sentWeekCount;
	
	private Double sentMonthCount;
	
	private Double receivedDayCount;
	
	private Double receivedWeekCount;
	
	private Double receivedMonthCount;
	
}
