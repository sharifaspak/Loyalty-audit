package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PartnerActivityDto {

	private ProgramActivityWithIdDto loyaltyActivity;
	private PartnerActivityCode activityCode;
	private Double baseRate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date startDate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private Date endDate;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private boolean tierPointsFlag;
	private Double tierPointRate;
	private EventTypeCatalogueDTO activityTypeCatalogue;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<NumberTypeDto> numberType;
	private Long pointsExpiryPeriod;
	private ActivityTypes activityType;
	private RateType rateType;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<CustomerTypeDto> customerType;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<TierBonus> tierBonus;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ChainedActivity> chainedActivity;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ThresholdCap> thresholdCap;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Boolean enableForGift;

}
