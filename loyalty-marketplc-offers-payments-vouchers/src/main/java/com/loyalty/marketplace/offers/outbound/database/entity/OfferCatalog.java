package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

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
@Document(collection = OffersDBConstants.OFFER_CATALOG)
public class OfferCatalog {

	@Id
	private String id;
	@Field("OfferId")
	private String offerId;
	@Field("ProgramCode")
	private String programCode;
	@Field("OfferCode")
	private String offerCode;
	@DBRef
	@Field("OfferType")
	private OfferType offerType;
	@Field("Offer")
	private OfferDetails offer;
	@Field("BrandDescription")
	private BrandDescription brandDescription;
	@Field("TAndC")
	private TermsConditions termsAndConditions;
	@Field("Tags")
	private Tags tags;
	@Field("WhatYouGet")
	private WhatYouGet whatYouGet;
	@Field("OfferDate")
	private OfferDate offerDates;
	@Field("TrendingRank")
	private Integer trendingRank;
	@Field("Status")
	private String status;
	@Field("AvailableInPortal")
	private List<String> availableInPortals;
	@Field("NewOffer")
	private String newOffer;
	@Field("GiftInfo")
	private GiftInfo giftInfo;
	@Field("IsDod")
	private String isDod;
	@Field("IsFeatured")
	private String isFeatured;
	@Field("OfferValues")
	private OfferValues offerValues;
	@Field("DiscountPerc")
	private Integer discountPerc;
	@Field("ESTSavings")
	private Double estSavings; 
	@Field("Sharing")
	private String sharing;
	@Field("SharingBonus")
	private Integer sharingBonus;
	@Field("VatPercentage")
	private Double vatPercentage;
	@Field("OfferLimit")
	private List<OfferLimit> limit;
	@Field("PartnerCode")
	private String partnerCode;
	@DBRef
	@Field("Merchant")
	private Merchant merchant;
	@DBRef
	@Field("OfferStores")
	private List<Store> offerStores;
	@DBRef
	@Field("Category")
	private Category category;
	@DBRef
	@Field("SubCategory")
	private Category subCategory;
	@Field("DynamicDenomination")
	private String dynamicDenomination;
	@Field("GroupedFlag")
	private String groupedFlag;
	@Field("DynamicDenominationValue")
	private DynamicDenominationValue dynamicDenominationValue;
	@Field("IncrementalValue")
	private Integer incrementalValue;
	@Field("CustomerType")
	private ListValues customerTypes;
	@Field("CustomerSegments")
	private ListValues customerSegments;
	@DBRef
	@Field("Denominations")
	private List<Denomination> denominations;
	@Field("Rules")
	private List<String> rules;
	@Field("ProvisioningChannel")
	private String provisioningChannel;
	@Field("ProvisioningAttributes")
	private ProvisioningAttributes provisioningAttributes;
	@Field("SubOffer")
	private List<SubOffer> subOffer;
	@Field("EarnMultiplier")
	private Double earnMultiplier;
	@Field("AccrualDetails")
	private AccrualDetails accrualDetails;
	@Field("ActivityCode")
	private ActivityCode activityCode;
	@Field("VoucherInfo")
	private VoucherInfo voucherInfo;
	@Field("ProratedBundle")
	private boolean proratedBundle;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("IsBirthdayGift")
	private String isBirthdayGift;
	@Field("StaticRating")
	private Integer staticRating;
	@DBRef
	@Field("OfferRating")
	private OfferRating offerRating;
	@Field("FreeOffers")
	private FreeOffers freeOffers;
	@Field("SubscriptionDetails")
	private SubscriptionDetails subscriptionDetails;
	@Field("PaymentMethods")
	private List<PaymentMethod> paymentMethods;
	@Field("Restaurants")
	private Restaurant restaurant;
	
}
