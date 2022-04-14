package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = OffersDBConstants.MEMBER_OFFER_COUNTERS)
public class MemberOfferCounts {
	
	@Id
	private String id;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("OfferId")
	private String offerId;

	@Field("DailyCount")
	private Integer dailyCount;
	
	@Field("WeeklyCount")
	private Integer weeklyCount;
	
	@Field("MonthlyCount")
	private Integer monthlyCount;
	
	@Field("AnnualCount")
	private Integer annualCount;
	
	@Field("TotalCount")
	private Integer totalCount;
	
	@Field("LastPurchased")
	private Date lastPurchased;
	
	@Field("DenominationCount")
	private List<DenominationCount> denominationCount;

}
