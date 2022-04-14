package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

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
@ToString
@Document(collection = OffersDBConstants.OFFER_COUNTER)
public class OfferCounter {
	
	@Id
	private String id;
	
	@Field("OfferId")
	private String offerId;
	
	@Field("Rules")
	private List<String> rules;
	
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
	
	@Field("MemberOfferCount")
	private List<MemberOfferCount> memberOfferCount;
	
	@Field("AccountOfferCount")
	private List<AccountOfferCount> accountOfferCount;
	
}
