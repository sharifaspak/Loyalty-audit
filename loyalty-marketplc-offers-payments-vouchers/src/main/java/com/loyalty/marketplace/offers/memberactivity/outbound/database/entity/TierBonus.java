package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class TierBonus {

	@NotEmpty(message = "{validation.partner.activity.null.tier.bonus.customerTier}")
	private String customerTier;
	@NotNull(message = "{validation.partner.activity.null.tier.bonus.tierBonusRate}")
	private Double tierBonusRate;
	@NotNull(message = "{validation.partner.activity.null.tier.bonus.expiryInMonth}")
	private Integer expiryInMonth;
	@NotEmpty(message = "{validation.partner.activity.null.tier.bonus.rateType}")
	private String rateType;

}
