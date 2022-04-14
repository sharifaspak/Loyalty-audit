package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class MerchantBillingRate {

	private String id;
	private Double rate;
    private Date startDate;
    private Date endDate;
	private String rateType;
    private String currency;
    
}
