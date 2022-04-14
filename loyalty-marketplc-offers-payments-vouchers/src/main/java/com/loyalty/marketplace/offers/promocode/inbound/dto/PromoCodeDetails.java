package com.loyalty.marketplace.offers.promocode.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PromoCodeDetails {

	private String promoCode;
	private String promoCodeDescription;
	private int value;
	private String promoCodeType;
	private String promoCodeLevel;
	private String startDate;
	private String endDate;
	private int promoCodeDuration;
	private int promoCodeTotalCount;
	private List<Integer> promoDenominationAmount;
	private int promoUserRedeemCountLimit;
	private boolean isWelcomeGift;
}
