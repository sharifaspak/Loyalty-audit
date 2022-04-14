package com.loyalty.marketplace.offers.decision.manager.outbound.dto;

import java.util.List;

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
public class RuleResult {
	
	private String accountNumber;
	private boolean eligibility;
	private String reason;
	private List<String> customerSegments;

}
