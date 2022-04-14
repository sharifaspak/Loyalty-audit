package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.RedemptionRate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ListRedemptionRate {
	
	private List<RedemptionRate> redemptionRateList;
	
}
