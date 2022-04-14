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
public class DenominationRange {
	
	private String lowValue;
	private String highValue;

}
