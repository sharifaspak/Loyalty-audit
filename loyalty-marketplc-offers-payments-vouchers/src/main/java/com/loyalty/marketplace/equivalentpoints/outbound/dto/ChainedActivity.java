package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ChainedActivity {
	
	private String partnerActivityId;
	private Integer sequence;
	
}
