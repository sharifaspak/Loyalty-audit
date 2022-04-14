package com.loyalty.marketplace.subscription.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.subscription.domain.MerchantsList;
import com.loyalty.marketplace.subscription.inbound.dto.SubscribedOfferTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionMarketDescLineDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionMarketTitleLineDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionSubTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionTitleHeaderDto;
import com.loyalty.marketplace.subscription.inbound.dto.TermsAndConditionsDto;
import com.loyalty.marketplace.subscription.outbound.dto.CuisinesResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "SubscriptionCatalog")
@Setter
@Getter
@ToString
public class SubscriptionCatalog {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("Title")
	private SubscriptionTitle subscriptionTitle;
	@Field("Description")
	private SubscriptionDescription subscriptionDescription;
	@Field("TermsAndConditions")
	private TermsAndConditionsDto termsAndConditions;
	@Field("SubscriptionTitleHeader")
	private SubscriptionTitleHeaderDto subscriptionTitleHeader;
	@Field("SubscriptionSubTitle")
	private SubscriptionSubTitleDto subscriptionSubTitle;
	@Field("SubscribedOfferTitle")
	private SubscribedOfferTitleDto subscribedOfferTitle;
	@Field("SubscriptionMarketTitleLine")
	private SubscriptionMarketTitleLineDto subscriptionMarketTitleLine;
	@Field("SubscriptionMarketDescLine")
	private SubscriptionMarketDescLineDto subscriptionMarketDescLine;
	@Field("ImageUrl")
	private String imageUrl;
	@Field("MerchantsList")
	private List<MerchantsList> merchantsList;
	@Field("CuisinesList")
	private List<CuisinesResponseDto> cuisinesList;
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
	@Field("ChargeabilityType")
	private String chargeabilityType;
	@Field("PaymentMethods")
	private List<SubscriptionPaymentMethod> paymentMethods;
	@Field("CustomerSegments")
	private List<String> customerSegments;
	@Field("EligibleChannels")
	private List<String> eligibleChannels;
	@Field("LinkedOffers")
	private List<Title> linkedOffers;
	@Field("FreeOffer")
	private List<FreeOffer> freeOffer;
	@Field("Benefits")
	private List<CatalogBenefits> benefits;
	@Field("PackageType")
	private String packageType;
	@Field("SubscriptionSegment")
	private String subscriptionSegment;
	@Field("Logo")
	private String subscriptionLogo;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	
	
}
