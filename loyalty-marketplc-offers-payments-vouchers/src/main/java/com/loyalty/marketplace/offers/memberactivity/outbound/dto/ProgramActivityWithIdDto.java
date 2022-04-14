package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import javax.validation.constraints.NotEmpty;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityName;

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
public class ProgramActivityWithIdDto {

	@NotEmpty(message = "{validation.partner.activity.null.activityId}")
	private String activityId;
	private ActivityName activityName;
	private ActivityDescription activityDescription;

}
