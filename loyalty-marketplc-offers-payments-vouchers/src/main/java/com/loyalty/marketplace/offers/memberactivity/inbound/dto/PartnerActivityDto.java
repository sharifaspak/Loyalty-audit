package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import java.util.Date;
import java.util.List;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityTypes;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ChainedActivity;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.PartnerActivityCode;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.RateType;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ThresholdCap;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.TierBonus;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;

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
public class PartnerActivityDto {

	private ProgramActivityWithIdDto loyaltyActivity;
	private PartnerActivityCode activityCode;
	private Double baseRate;
	private Date startDate;
	private Date endDate;
	private boolean tierPointsFlag;
	private Double tierPointRate;
	private EventTypeCatalogueDTO activityTypeCatalogue;
	private List<NumberTypeDto> numberType;
	private Long pointsExpiryPeriod;
	private ActivityTypes activityType;
	private RateType rateType;
	private List<CustomerTypeDto> customerType;
	private List<TierBonus> tierBonus;
	private List<ChainedActivity> chainedActivity;
	private List<ThresholdCap> thresholdCap;


}
