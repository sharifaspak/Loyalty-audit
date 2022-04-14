package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriptionCatalogInputDto {
	
	private String id;
	private SubscriptionTitleDto subscriptionTitle;
	private SubscriptionDescriptionDto subscriptionDescription;
	private TermsAndConditionsDto termsAndConditions;
	private SubscriptionTitleHeaderDto subscriptionTitleHeader;
	private SubscriptionSubTitleDto subscriptionSubTitle;
	private SubscribedOfferTitleDto subscribedOfferTitle;
	private SubscriptionMarketTitleLineDto subscriptionMarketTitleLine;
	private SubscriptionMarketDescLineDto subscriptionMarketDescLine;
	
	@NotNull(message= "{validation.imageUrl.nonNull.msg}")
	@NotEmpty(message = "{validation.imageUrl.nonEmpty.msg}")
	private String imageUrl;
	private List<String> merchantsList;
	private List<String> cuisinesList;
	
	
	@NotNull(message = "{validation.cost.notNull.msg}")
	@Min(value=0, message="{validation.cost.min.msg}")
	private Double cost;
	
	private Integer pointsValue;
	
	@Min(value=0, message="{validation.freeDuration.min.msg}")
	private Integer freeDuration;
	
	@NotNull(message = "{validation.validityPeriod.notNull.msg}")
	@Min(value=0, message="{validation.validityPeriod.min.msg}")
	private Integer validityPeriod;
	
	private String startDate;
	
	@NotNull(message = "{validation.endDate.notNull.msg}")
	private String endDate;
	
	@NotNull(message="{validation.status.notNull.msg}")
	@NotEmpty(message = "{validation.status.notEmpty.msg}")
	private String status;
	
	@NotNull(message = "{validation.chargeabilityType.notNull.msg}")
	@NotEmpty(message = "{validation.chargeabilityType.notEmpty.msg}")
	private String chargeabilityType;
	
	private List<String> paymentMethods;
	
	@NotNull(message = "{validation.customerSegment.notNull.msg}")
	@NotEmpty(message = "{validation.customerSegment.notEmpty.msg}")
	private List<String> customerSegments;
	
	private List<String> eligibleChannels;
		
	private List<String> linkedOffers;
	
	private List<SubscriptionBenefitsRequest> subscriptionBenefits;
	
	private String packageType;
	
	@NotNull(message = "{validation.subscriptionSegment.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionSegment.notEmpty.msg}")
	private String subscriptionSegment;
	
		
}
