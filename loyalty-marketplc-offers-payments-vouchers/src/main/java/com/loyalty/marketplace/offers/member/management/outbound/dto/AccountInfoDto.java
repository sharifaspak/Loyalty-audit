package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loyalty.marketplace.subscription.outbound.dto.BenefitsResponseDto;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfoDto {

	private String channelId;
	private String accountNumber;
	private String accountId;
	private List<String> customerType;
	private String loyaltyId;
	private String deviceId;
	private String language;
	private String uilanguage;
	private String mobileNumber;
	private String accountAuthority;
	private Date accountStartDate;
	private String email;
	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private String nationality;
	private Date dob;
	private boolean isBlocked;
	private String blockReason;
	private String documentId;
	private String partyId;
	private boolean firstAccessFlag;
	private boolean isPrimary;
	private boolean ageEligibleFlag;
	
	private String accountStatus;
	private String numberType;
	private String atgUserName;
	private String subscribtionStatus;
	private double lifetimeSavings;
	private int totalPoints;
	private Date nextExpiryDate;
	private int nextExpiryPoints;
	private int ceasedPoints;
	private int sharingBonusCounterRemaining;
	private boolean welcomegiftEligibleFlag;
	private List<WelcomeGift> welcomeGifts;
	private String referralCode;
	private Date loginDate;
	
	private ReferralBonusDetails referralBonusAccount;
	
	private List<InterestDetails> interest;
	private List<EligibileSections> eligibleFeatures;
	private List<EligibileSections> nonEligibleFeatures;
	private List<PaymentMethods> eligiblePaymentMethod;
	private CRMResponse crmInfo;
	private EmailVerificationDto emailVerification;
	private List<CobrandedCardDto> cobrandedCardDetails;
	private List<BenefitsResponseDto> benefits;
}
