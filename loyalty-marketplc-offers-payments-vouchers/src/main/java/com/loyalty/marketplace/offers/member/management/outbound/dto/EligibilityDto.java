package com.loyalty.marketplace.offers.member.management.outbound.dto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EligibilityDto {
	private String customerType;
	private EligibilityMatrixDto eligibility;

}
