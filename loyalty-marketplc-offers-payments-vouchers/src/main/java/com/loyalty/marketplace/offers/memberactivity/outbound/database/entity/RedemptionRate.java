package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RedemptionRate {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private DenominationRange denominationRange;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Double equivalentPoint;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Double rate;

}
