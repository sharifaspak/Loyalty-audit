package com.loyalty.marketplace.offers.member.management.inbound.dto;

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
public class MemberDetailRequestDto {
	
	 private String accountNumber;
	 private String documentId ;
	 private String membershipCode; 
	 private String emailId; 
	 private String loyaltyId; 
	 private String atgUserName;
	 private boolean includeCrmInfo; 
	 private boolean includeEligiblePaymentMethods ;
	 private boolean includeEligibilityMatrix; 
	 private boolean includeLinkedAccount; 
	 private boolean includeAccountInterest;
	 private boolean includeCancelledAccount;
	 private boolean includeLoginInfo;
	 private boolean callCustomerInterest; 
     private boolean callSubscription; 
	 private boolean callMemberActivity; 
	 private boolean callPointsBank; 
	 private boolean includeReferralBonusAccount;

}
