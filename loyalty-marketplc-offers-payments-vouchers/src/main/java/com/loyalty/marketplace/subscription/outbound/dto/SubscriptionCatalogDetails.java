package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.Date;
import java.util.List;

import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.subscription.domain.Benefits;
import com.loyalty.marketplace.subscription.domain.MerchantsList;
import com.loyalty.marketplace.subscription.inbound.dto.SubscribedOfferTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionDescriptionDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionMarketDescLineDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionMarketTitleLineDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionSubTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionTitleHeaderDto;
import com.loyalty.marketplace.subscription.inbound.dto.TermsAndConditionsDto;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionDescription;
import com.loyalty.marketplace.subscription.outbound.database.entity.Title;

import lombok.Data;

@Data
public class SubscriptionCatalogDetails {
	private String promoTypeDescription;
	
	
	
	
	private String id;
	private SubscriptionTitleDto subscriptionTitle;
	private SubscriptionDescriptionDto subscriptionDescription;
	private TermsAndConditionsDto termsAndConditions;
	private SubscriptionTitleHeaderDto subscriptionTitleHeader;
	private SubscriptionSubTitleDto subscriptionSubTitle;
	private SubscribedOfferTitleDto subscribedOfferTitle;
	private SubscriptionMarketTitleLineDto subscriptionMarketTitleLine;
	private SubscriptionMarketDescLineDto subscriptionMarketDescLine;
	private String imageUrl;
	private List<MerchantsList> merchantsList;
	private List<CuisinesResponseDto> cuisinesList;
	private PointsValue loyaltyPointsValue;
	private Cost loyaltyCost;
	private Integer freeDuration;
	private Integer validityPeriod;
	private Date startDate;
	private Date endDate;
	private String status;
	private String chargeabilityType;
	private List<PaymentMethodDto> paymentMethods;
	private List<String> customerSegments;
	private List<String> eligibleChannels;
	private List<Title> linkedOffers;
	private List<Benefits> benefits;
	private String packageType;
	private String subscriptionSegment;
	private String subscriptionLogo;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	private String programCode;

}
