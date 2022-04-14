package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.MembershipNote;

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
public class MembershipDetailsDto {
	  
	 private String primaryAccount;
     private String tierLevel;
     private String tierLevelInArabic;
     private int totalTierPoints; 
	 private String membershipCode;
     private String atgUsername;
     private Date startDate;
	 private Date endDate;
     private String status;
     private boolean flagBlocked;
     private String blockedReason;
     private boolean firstAccessFlag;
     private int pointsTonextTierLevel;
     private double lifetimeSavings;
     private int totalPoints;
     private Date nextExpiryDate;
     private int nextExpiryPoints;
     private boolean coBrandedCard;
     private List<MembershipNote> membershipNotes;
     private List<String> top3Account;
	    
}
