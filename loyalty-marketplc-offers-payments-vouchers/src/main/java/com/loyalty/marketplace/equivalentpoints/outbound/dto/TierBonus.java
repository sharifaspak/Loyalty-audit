package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TierBonus {

	private String customerTier;
	private Double tierBonusRate;
	private Integer expiryInMonth;
	private String rateType;

}
