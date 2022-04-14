package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ValidDates {

	Date validStartDate;
	Date validEndDate;
}
