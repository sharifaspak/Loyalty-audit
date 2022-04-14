package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

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
public class ActivityCode {
	
	
	@Field("AccrualCode")
	private MarketplaceActivity accrualActivityCode;
	@Field("RedemptionCode")
	private MarketplaceActivity redemptionActivityCode;
	@Field("PointsAccrualCode")
	private MarketplaceActivity pointsAccrualActivityCode;

}
