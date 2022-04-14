package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

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
public class PartnerActivityResponseDto {

	private String description;
	private String activityCode;
	private String activityId;

}
