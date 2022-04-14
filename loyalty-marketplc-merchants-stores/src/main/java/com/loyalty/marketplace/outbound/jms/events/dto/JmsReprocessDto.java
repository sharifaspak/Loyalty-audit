package com.loyalty.marketplace.outbound.jms.events.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JmsReprocessDto {

	private List<String> correlationIds;
	
}
