package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.loyalty.marketplace.offers.outbound.dto.RestaurantDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OfferCatalogDto {
	
	@NotBlank(message = "{validation.offerCatalogDto.offerCode.notBlank.msg}")
	private String offerCode;
	
	@NotBlank(message = "{validation.offerCatalogDto.offerTypeId.notBlank.msg}")
	private String offerTypeId;
	
	@Size(max=25, message = "{validation.offerCatalogDto.offerLabelEn.size.msg}")
	private String offerLabelEn;
	
	@Size(max=25, message = "{validation.offerCatalogDto.offerLabelAr.size.msg}")
	private String offerLabelAr;
	
	@NotEmpty(message = "{validation.offerCatalogDto.offerTitleEn.notEmpty.msg}")
	private String offerTitleEn;
	
	@NotEmpty(message = "{validation.offerCatalogDto.offerTitleAr.notEmpty.msg}")
	private String offerTitleAr;
	
	@NotEmpty(message = "{validation.offerCatalogDto.offerTitleDescriptionEn.notEmpty.msg}")
	private String offerTitleDescriptionEn;
	
	@NotEmpty(message = "{validation.offerCatalogDto.offerTitleDescriptionAr.notEmpty.msg}")
	private String offerTitleDescriptionAr;
	
	private String offerDetailMobileEn;
	private String offerDetailMobileAr;
	private String offerDetailWebEn;
	private String offerDetailWebAr;
	
	@NotEmpty(message = "{validation.offerCatalogDto.brandDescriptionEn.notEmpty.msg}")
	private String brandDescriptionEn;
	
	@NotEmpty(message = "{validation.offerCatalogDto.brandDescriptionAr.notEmpty.msg}")
	private String brandDescriptionAr;
	
	@NotEmpty(message = "{validation.offerCatalogDto.termsAndConditionsEn.notEmpty.msg}")
	private String termsAndConditionsEn;
	
	@NotEmpty(message = "{validation.offerCatalogDto.termsAndConditionsAr.notEmpty.msg}")
	private String termsAndConditionsAr;
	
	private String additionalTermsAndConditionsEn;
	private String additionalTermsAndConditionsAr;
	private String tagsEn;
	private String tagsAr;
	
	@NotEmpty(message = "{validation.offerCatalogDto.whatYouGetEn.notEmpty.msg}")
	private String whatYouGetEn;
	
	@NotEmpty(message = "{validation.offerCatalogDto.whatYouGetAr.notEmpty.msg}")
	private String whatYouGetAr;
	
	@NotEmpty(message = "{validation.offerCatalogDto.offerStartDate.notEmpty.msg}")
	private String offerStartDate;
	private String offerEndDate;
	
	@Min(value=1, message="{validation.offerCatalogDto.trendingRank.min.msg}")
	private Integer trendingRank;
	private String status;
	
	private List<String> availableInPortals;
	private String newOffer;
	
	private String isGift;
	private List<String> giftChannels;
	private List<String> giftSubCardTypes;
	
	private String isDod;
	private String isFeatured;
	
	@Min(value=0, message="{validation.offerCatalogDto.pointsValue.min.msg}")
	private Integer pointsValue;
	
	@NotNull(message = "{validation.offerCatalogDto.cost.notNull.msg}")
	private Double cost;
	
	@Min(value=1, message="{validation.offerCatalogDto.discountPerc.min.msg}")
	private Integer discountPerc;
	private Double estSavings;
	
	private List<LimitDto> limit;
	
	private String sharing;
	@Min(value=1, message="{validation.offerCatalogDto.sharingBonus.min.msg}")
	private Integer sharingBonus;
	
	private Double vatPercentage;
	
	private String partnerCode;
	@NotEmpty(message = "{validation.offerCatalogDto.merchantCode.notEmpty.msg}")
	private String merchantCode;
	@NotEmpty(message = "{validation.offerCatalogDto.storeCodes.notEmpty.msg}")
	private List<String> storeCodes;
	
	@NotEmpty(message = "{validation.offerCatalogDto.categoryId.notEmpty.msg}")
	private String categoryId;
	@NotEmpty(message = "{validation.offerCatalogDto.subCategoryId.notEmpty.msg}")
	private String subCategoryId;
	
	private String groupedFlag;
	
	private String dynamicDenomination;
	@Min(value=1, message="{validation.offerCatalogDto.minDenomination.min.msg}")
	private Integer minDenomination;
	@Min(value=1, message="{validation.offerCatalogDto.maxDenomination.min.msg}")
	private Integer maxDenomination;
	@Min(value=1, message="{validation.offerCatalogDto.incrementalValue.min.msg}")
	private Integer incrementalValue;
		
	private ListValuesDto customerTypes;
	private ListValuesDto customerSegments;
	
	private List<String> denominations;
	private List<String> rules;
	
	private String provisioningChannel;
	private boolean proratedBundle;
	private String ratePlanCode;
	private String rtfProductCode;
	private String rtfProductType;
	private String vasCode;
	private String vasActionId;
	@Min(value=1, message="{validation.offerCatalogDto.promotionalPeriod.min.msg}")
	private Integer promotionalPeriod;
	private String feature;
	private String serviceId;
	private String activityId;
	private String packName;
	private String action;
	
	@Valid
	private List<SubOfferDto> subOffer;
	
	private Double earnMultiplier;
	private Double pointsEarnMultiplier;
	private List<String> accrualPaymentMethods;
	private String accrualId;
	private String accrualActivityCode;
	private String accrualCodeDescriptionEn;
	private String accrualCodeDescriptionAr;
	private String redemptionId;
	private String redemptionActivityCode;
	private String redemptionCodeDescriptionEn;
	private String redemptionCodeDescriptionAr;
	
	private String isBirthdayGift;
	
	@Min(value=1, message="{validation.offerCatalogDto.staticRating.min.msg}")
	@Max(value=5, message="{validation.offerCatalogDto.staticRating.max.msg}")
	private Integer staticRating;
	private String offerRating;
	
	@Min(value=1, message="{validation.offerCatalogDto.voucherExpiryPeriod.min.msg}")
	private Integer voucherExpiryPeriod;
	private String voucherAction;
	@Min(value=0, message="{validation.offerCatalogDto.voucherAmount.min.msg}")
	private Double voucherAmount;
	private String voucherExpiryDate;
	private String voucherRedeemType;
	private String partnerRedeemURL ;
	private String instructionsToRedeemTitleEn;
	private String instructionsToRedeemTitleAr;
	private String instructionsToRedeemEn;
	private String instructionsToRedeemAr;
	private boolean isMamba;
	private boolean isPromotionalGift;
	private boolean isPointsRedemptionGift;
	private Boolean subscPromo;
	private String subscriptionCatalogId;
	private List<String> paymentMethods;
	private String pointsAccrualId;
	private String pointsAccrualActivityCode;
	private String pointsAccrualCodeDescriptionEn;
	private String pointsAccrualCodeDescriptionAr;
	
	private RestaurantDto restaurant;
    
}
