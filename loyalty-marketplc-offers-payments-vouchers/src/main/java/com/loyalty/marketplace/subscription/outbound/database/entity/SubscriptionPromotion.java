package com.loyalty.marketplace.subscription.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "SubscriptionPromotion")
@Setter
@Getter
@ToString
public class SubscriptionPromotion {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
	@Field("Validity")
	private String validity;
	@Field("StartDate")
	private Date startDate;
	@Field("EndDate")
	private Date endDate;	
	@Field("DiscountPercentage")
	private String discountPercentage;
	@Field("FlatRate")
	private String flatRate;	
	@Field("ChargeabilityType")
	private String chargeabilityType;	
	@Field("DiscountMonths")
	private int discountMonths;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	
}
