package com.loyalty.marketplace.offers.outbound.service.dto;

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
public class IncludeMemberDetails {
	
	private boolean includeEligibilityMatrix;
	private boolean includePaymentMethods;
	private boolean includeSubscriptionInfo;
	private boolean includePointsBankInfo;
	private boolean includeMemberActivityInfo;
	private boolean includeCustomerInterestInfo;
	private boolean includeReferralBonusAccount;
	private boolean includeLinkedAccount;

}
