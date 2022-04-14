package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfoDto {

	private String channelId;
	private String accountNumber;
	private List<String> customerType;
	private String loyaltyId;
	private long accountId;
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

	private String accountStatus;
	private String numberType;
	private String atgUserName;
	private String subscribtionStatus;
	private double lifetimeSavings;
	private int totalPoints;
	private Date nextExpiryDate;
	private int nextExpiryPoints;
	private int ceasedPoints;
	private List<InterestDetails> interest;
	private List<EligibileSections> eligibleFeatures;
	private List<EligibileSections> nonEligibleFeatures;
	private List<PaymentMethods> eligiblePaymentMethod;
	private CRMResponse crmInfo;
	private EmailVerificationDto emailVerification;
	private int sharingBonusCounterRemaining;
}
