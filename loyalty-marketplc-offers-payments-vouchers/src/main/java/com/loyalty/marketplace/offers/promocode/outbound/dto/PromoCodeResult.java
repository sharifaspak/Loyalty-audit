package com.loyalty.marketplace.offers.promocode.outbound.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PromoCodeResult {
	
	private String promoCode;
	private List<String> accountNumber;
	private Date startDate;
	private Date endDate;
	private int totalCount;
	private int redeemedCount;
	private int usageCount;
	private String offerId;
	private String offerType;
	private String subscriptionCatalogId;
	private int usageRedeemedCount;
	private List<String> customerType;
	private List<Integer> denominationAmount;
}
