package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import javax.validation.constraints.NotEmpty;

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
public class RateType {
	
	@NotEmpty
	private String name;
	private String description;
	
}
