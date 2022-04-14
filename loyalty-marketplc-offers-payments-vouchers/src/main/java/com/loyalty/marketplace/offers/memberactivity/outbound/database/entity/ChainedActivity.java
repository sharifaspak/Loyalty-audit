package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

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
public class ChainedActivity {
	
	private String partnerActivityId;
	private Integer sequence;
	
}
