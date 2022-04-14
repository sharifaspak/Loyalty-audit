package com.loyalty.marketplace.offers.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OfferCatalogResultResponseDto {
	
	private String id;
	private String programCode;
	private String offerId;
	private String offerCode;
	private String offerTypeId;
	private String typeDescriptionEn;
	private String typeDescriptionAr;
	
	private List<PaymentMethodDto> paymentMethods;
	
	private String offerLabelEn;
	private String offerLabelAr;
	private String offerTitleEn;
	private String offerTitleAr;
	private String offerTitleDescriptionEn;
	private String offerTitleDescriptionAr;
	private String offerDetailMobileEn;
	private String offerDetailMobileAr;
	private String offerDetailWebEn;
	private String offerDetailWebAr;
	
	private String brandDescriptionEn;
	private String brandDescriptionAr;
	
	private String termsAndConditionsEn;
	private String termsAndConditionsAr;
	private String additionalTermsAndConditionsEn;
	private String additionalTermsAndConditionsAr;
	
	private String tagsEn;
	private String tagsAr;
	
	private String whatYouGetEn;
	private String whatYouGetAr;
	
	private Date offerStartDate;
	private Date offerEndDate;
	
	private Integer trendingRank;
	private String status;
	
	private List<String> availableInPortals;
	
	private String newOffer; 
	private String isDod;
	private String isFeatured;
	private String isBirthdayGift;
	private String groupedFlag;
	
	private String isGift; 
	private List<String> giftChannels;
	private List<String> giftSubCardTypes;
	
	private Integer originalPointsValue;
	private Integer pointsValue;
	private Double originalCost;
	private Double cost; 
	private Integer discountPerc;
	private Double estSavings;
	private Double vatPercentage;
	
	private String sharing; 
	private Integer sharingBonus;
		
	private List<LimitResponseDto> offerLimit;
	
	private String partnerCode;
	private String merchantCode;
	private String merchantNameEn;
	private String merchantNameAr;	
	private List<StoreDto> offerStores;
	
	private String categoryId;
	private String categoryNameEn;
	private String categoryNameAr;
	private String subCategoryId;
	private String subCategoryNameEn;
	private String subCategoryNameAr;
	
	private String dynamicDenomination; 
	private Integer minDenomination;
	private Integer maxDenomination;
	private Integer incrementalValue;
	
	private List<String> eligibleCustomerTypes;
	private List<String> exclusionCustomerTypes;
	private List<String> eligibleCustomerSegments;
	private List<String> exclusionCustomerSegments;
	
	private List<DenominationDto> denominations;
	
	private Date voucherExpiryDate;
	private Integer voucherExpiryPeriod;
	private Double voucherAmount;
	private String voucherAction; 
	private String voucherRedeemType;
    private String partnerRedeemURL ;
    private String instructionsToRedeemTitleEn;
    private String instructionsToRedeemTitleAr;
    private String instructionsToRedeemEn;
    private String instructionsToRedeemAr;
    
	private List<String> rules;
	
	private List<SubOfferDto> subOffer;
	
	private Double earnMultiplier;
	private Double pointsEarnMultiplier;
	private List<PaymentMethodDto> accrualPaymentMethods;
	private String accActivityId;
	private String accActivityCd;
	private String accActivityCodeDescriptionEn;
	private String accActivityCodeDescriptionAr;
	private String redActivityId;
	private String redActivityCd;
	private String redActivityCodeDescriptionEn;
	private String redActivityCodeDescriptionAr;
	private String pointsAcrId;
	private String pointsAcrActivityCode;
	private String pointsAcrCodeDescriptionEn;
	private String pointsAcrCodeDescriptionAr;
	
	private String provisioningChannel;
	private boolean proratedBundle;
	private String ratePlanCode;
	private String rtfProductCode;
	private String rtfProductType;
	private String vasCode;
	private String vasActionId;
	private Integer promotionalPeriod;
	private String feature;
	private String serviceId;
	private String activityIdRbt;
	private String packName;
	
	private Eligibility eligibility;
	
	private String offerRating;
	private Integer staticRating;
	private Double averageRating;
	private List<MemberRatingDto> memberRatings;
	
	private String birthdayGiftAvailed;
	private Integer soldCount;
	private Integer quantityLeft;
	
	private List<ImageUrlDto> imageUrlList; 
	
	private boolean isMamba;
	private boolean isPromotionalGift;
	private boolean isPointsRedemptionGift;

	private Boolean subscPromo;
	private String subscriptionCatalogId;
	
	private RestaurantDto restaurant;

		
}
