package com.loyalty.marketplace.welcomegift.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WelcomeGiftDetails {

	private String accountNumber;
	private String giftType;
	private String giftValue;
	private String startDate;
	private String endDate;
	private String offerTitleEn;
	private String offerTitleAr;
	private String offerId;
	private String subscriptionCatalogId;
}
