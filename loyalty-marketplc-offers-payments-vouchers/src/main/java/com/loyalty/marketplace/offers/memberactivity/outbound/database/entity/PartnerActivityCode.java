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
public class PartnerActivityCode {
	
	@NotEmpty
	private String code;
	private ActivityDescription description;
	
}
