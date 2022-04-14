package com.loyalty.marketplace.offers.promocode.outbound.database.entity;


import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.outbound.database.entity.CustomerType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "PromoCode")
public class PromoCode {
	
	@Id
	private String id;
	@Field("ProgramCode")
	private String program;
	@Field("Code")
	private String promoCode;
	@Field("Description")
	private String description;
	@Field("OfferId")
	private List<String> offerIds;
	@Field("PromoValue")
	private int value;
	@Field("PromoLevel")
	private PromoLevels promoLevel;
	@Field("PromoType")
	private PromoTypes promoType;
	@Field("Rule_id")
	private PromoRules ruleId;
	@Field("StartDate")
	private Date startDate;
	@Field("EndDate")
	private Date endDate;
	@Field("TotalCount")
	private int totalCount;
	@Field("RedeemedCount")
	private int redeemedCount;
	@Field("Duration")
	private int duration;
	@Field("DenominationAmount")
	private List<Integer> denominationAmount;
	@Field("OfferType")
	private String offerType;
	@Field("Subscriptions")
	private List<String> subcscriptions;
	@Field("UserRedeemedAccountLimit")
	private int userRedeemedAccountLimit;
	@Field("CustomerType")
	private List<CustomerType> customerType;
	@Field("AccountNumber")
	private List<String> accountNumber;
	@Field("Partner")
	private String partner;
	@Field("PartnerRef")
	private String partnerRef;
	@Field("CreatedDate")
	private Date dtCreated;
	@Field("CreatedUser")
	private String usrCreated;
	@Field("UpdatedDate")
	private Date dtUpdated;
	@Field("UpdatedUser")
	private String usrUpdated;

}
