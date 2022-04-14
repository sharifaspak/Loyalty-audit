package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

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
public class ActivityTypes {
	
	@NotNull(message = "{validation.partner.activity.null.activityType.name}")
	private ActivityName name;
	private ActivityDescription description;
	
}
