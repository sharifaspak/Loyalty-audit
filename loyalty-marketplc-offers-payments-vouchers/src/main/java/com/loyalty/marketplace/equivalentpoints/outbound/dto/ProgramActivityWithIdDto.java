package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramActivityWithIdDto {

	private String activityId;
	private ActivityName activityName;
	private ActivityDescription activityDescription;

}
