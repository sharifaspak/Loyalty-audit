package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.Date;
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
public class GetMemberResponse {
	
	private String accountId;
	private String accountNumber;
	private String membershipCode;
	
	private String channelId;
	private String emailVerificationStatus;
	private String tierLevelName;
    private String firstName;
	private String lastName;
	private String gender;
	private String nationality;
	private String numberType;
	private String accountStatus;
	private String email;
	
    	private Integer totalAccountPoints;
    	private Integer totalTierPoints;
        
    	private boolean isTop3Account;
    	private boolean hasCoBranded;
    	private boolean ageEligibleFlag;
    	private boolean isFirstAccess;
	private boolean isSubscribed;
	private boolean isPrimaryAccount;
	
	private List<String> customerType;
	private List<String> customerSegment;
	private List<CobrandedCardDto> cobrandedCardDetails;
    	private List<EligibileSections> eligibleFeatures;
	private List<EligibileSections> nonEligibleFeatures;
	private List<PaymentMethods> eligiblePaymentMethod;
	
	private Date dob;
	private Date accountStartDate;
	
	private String referralAccountNumber;
	private String referralBonusCode;
	
	private String language;
	private String uiLanguage;
	

	private String partyId;
	private Date lastLoginDate;
	private List<OfferBenefit> benefits;
	
}

