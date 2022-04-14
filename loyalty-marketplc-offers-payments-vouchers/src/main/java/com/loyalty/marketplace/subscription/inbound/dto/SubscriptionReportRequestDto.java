package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionReportRequestDto {

	private List<String> subscriptionChannel;
	private List<String> subscriptionCatalogId;
	private String dateType;
	private String toDate;
	private String fromDate;
}
