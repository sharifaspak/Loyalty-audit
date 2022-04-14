package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class SubscriptionResponseDto {
	
	private String id;
	private String programCode;
	private String subscriptionCatalogId;
	private String accountNumber;
	private String membershipCode;
	private String promoCode;
	private Double cost;
	private Integer pointsValue;
	private Integer freeDuration;
	private Integer validityPeriod;
	private Date startDate;
	private Date endDate;
	private String status;
	private String paymentMethod;
	private SubscriptionPaymentResponseDto paymentDetails;
	private String subscriptionMethod;
	private String subscriptionSegment;
	private String subscriptionChannel;
	private String unSubscriptionChannel;
	private Date unSubscriptionDate;
	private String enrollmentChannel;
	private String cardType;
	private String phoneyTunesPackageId;
	private String transactionId;
	private Double lastChargedAmount;	
	private Date lastChargedDate;	
	private Date nextRenewalDate;
	private Boolean cobrandFlag;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	
}
