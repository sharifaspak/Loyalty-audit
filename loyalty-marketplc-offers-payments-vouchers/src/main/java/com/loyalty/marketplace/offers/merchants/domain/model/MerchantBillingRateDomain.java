package com.loyalty.marketplace.offers.merchants.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@AllArgsConstructor
public class MerchantBillingRateDomain {
	
	private String id;
	private Double rate;
	private Date startDate;
	private Date endDate;
	private String rateType;
	private String currency;

}
