package com.loyalty.marketplace.subscription.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "Subscription")
@Setter
@Getter
@ToString
public class Subscription {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("SubscriptionCatalog")
	private String subscriptionCatalogId;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("PromoCode")
	private String promoCode;
	@Field("Cost")
	private Double cost;
	@Field("PointsValue")
	private Integer pointsValue;
	@Field("FreeDuration")
	private Integer freeDuration;
	@Field("ValidityPeriod")
	private Integer validityPeriod;
	@Field("StartDate")
	private Date startDate;
	@Field("EndDate")
	private Date endDate;
	@Field("Status")
	private String status;
	@Field("PaymentMethod")
	private String paymentMethod;
	@Field("SubscriptionMethod")
	private String subscriptionMethod;
	@Field("SubscriptionSegment")
	private String subscriptionSegment;
	@Field("SubscriptionChannel")
	private String subscriptionChannel;
	@Field("UnSubscriptionChannel")
	private String unSubscriptionChannel;
	@Field("UnSubscriptionDate")
	private Date unSubscriptionDate;
	@Field("EnrollmentChannel")
	private String enrollmentChannel;
	@Field("CardType")
	private String cardType;
	@Field("PackageId")
	private String phoneyTunesPackageId;
	@Field("TransactionId")
	private String transactionId;
	@Field("LastChargedAmount")
	private Double lastChargedAmount;
	@Field("LastChargedDate")
	private Date lastChargedDate;
	@Field("NextRenewalDate")
	private Date nextRenewalDate;
	@Field("CancellationReason")
	private String cancellationReason;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	
	
}
