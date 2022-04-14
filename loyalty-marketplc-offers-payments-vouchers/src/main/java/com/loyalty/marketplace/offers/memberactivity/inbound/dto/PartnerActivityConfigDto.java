package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ChainedActivity;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ThresholdCap;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.TierBonus;

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
public class PartnerActivityConfigDto {

	private Double baseRate;
    private List<CustomerTypeDto> customerType;
    private List<ChainedActivity> chainedActivity;
    private List<TierBonus> tierBonus;
    private List<ThresholdCap> thresholdCap;

}
