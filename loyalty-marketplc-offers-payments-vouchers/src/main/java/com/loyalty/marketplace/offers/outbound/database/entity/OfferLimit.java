package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

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
public class OfferLimit {
	
	@Field("CustomerSegment")
	private String customerSegment;
	@Field("CouponQuantity")
	private Integer couponQuantity;
	@Field("DownloadLimit")
	private Integer downloadLimit;
	@Field("OfferDailyLimit")
	private Integer dailyLimit;
	@Field("OfferWeeklyLimit")
	private Integer weeklyLimit;
	@Field("OfferMonthlyLimit")
	private Integer monthlyLimit;
	@Field("OfferAnnualLimit")
	private Integer annualLimit;
	@Field("OfferDenominationLimit")
	private List<DenominationLimit> denominationLimit;
	@Field("AccountDailyLimit")
	private Integer accountDailyLimit;
	@Field("AccountWeeklyLimit")
	private Integer accountWeeklyLimit;
	@Field("AccountMonthlyLimit")
	private Integer accountMonthlyLimit;
	@Field("AccountAnnualLimit")
	private Integer accountAnnualLimit;
	@Field("AccountTotalLimit")
	private Integer accountTotalLimit;
	@Field("AccountDenominationLimit")
	private List<DenominationLimit> accountDenominationLimit;
	@Field("MemberDailyLimit")
	private Integer memberDailyLimit;
	@Field("MemberWeeklyLimit")
	private Integer memberWeeklyLimit;
	@Field("MemberMonthlyLimit")
	private Integer memberMonthlyLimit;
	@Field("MemberAnnualLimit")
	private Integer memberAnnualLimit;
	@Field("MemberTotalLimit")
	private Integer memberTotalLimit;
	@Field("MemberDenominationLimit")
	private List<DenominationLimit> memberDenominationLimit;
	
}
