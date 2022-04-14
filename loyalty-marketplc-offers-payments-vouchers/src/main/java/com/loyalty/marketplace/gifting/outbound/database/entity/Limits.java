package com.loyalty.marketplace.gifting.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Limits {

	@Field("SenderDayLimit")
	private Double senderDayLimit;
	
	@Field("SenderWeekLimit")
	private Double senderWeekLimit;
	
	@Field("SenderMonthLimit")
	private Double senderMonthLimit;
	
	@Field("ReceiverDayLimit")
	private Double receiverDayLimit;
	
	@Field("ReceiverWeekLimit")
	private Double receiverWeekLimit;
	
	@Field("ReceiverMonthLimit")
	private Double receiverMonthLimit;
	
}
