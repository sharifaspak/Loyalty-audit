package com.loyalty.marketplace.offers.domain.model;

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
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RedeemInstructionsDomain {
	
	private String instructionsToRedeemEn;
	private String instructionsToRedeemAr;

}
