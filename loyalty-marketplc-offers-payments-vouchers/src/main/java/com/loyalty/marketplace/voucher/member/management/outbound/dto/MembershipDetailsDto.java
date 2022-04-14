package com.loyalty.marketplace.voucher.member.management.outbound.dto;

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
public class MembershipDetailsDto {

	private String primaryAccount;
	private String tierLevel;
    private String tierLevelInArabic;
	private Integer totalTierPoints;
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
	private Integer totalPoints;
	private Date nextExpiryDate;
	private int nextExpiryPoints;
	private boolean coBrandedCard;
	private List<MembershipNote> membershipNotes;
	private List<String> top3Account;

}
