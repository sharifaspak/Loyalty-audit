package com.loyalty.marketplace.offers.decision.manager.inbound.dto;

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
@NoArgsConstructor
@ToString
public class CustomerSegmentDMRequestDto {
	
	List<MemberDetails> memberDetailsList;
	
	
}
