package com.loyalty.marketplace.offers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCodeDomain {
	
	private MarketplaceActivityDomain accrualActivityCode;
	private MarketplaceActivityDomain redemptionActivityCode;
	private MarketplaceActivityDomain pointsAccrualActivityCode;

}
