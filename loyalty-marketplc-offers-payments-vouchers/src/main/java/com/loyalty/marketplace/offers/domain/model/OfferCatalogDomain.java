package com.loyalty.marketplace.offers.domain.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.domain.model.CategoryDomain;
import com.loyalty.marketplace.domain.model.DenominationDomain;
import com.loyalty.marketplace.domain.model.PaymentMethodDomain;
import com.loyalty.marketplace.domain.model.UploadedFileContentDomain;
import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.gifting.outbound.database.entity.Gifts;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftRepository;
import com.loyalty.marketplace.helper.MarketplaceFileHelper;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersListConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.BirthdayDurationInfoDto;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.EligibleOfferHelperDto;
import com.loyalty.marketplace.offers.helper.dto.ExceptionInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.OfferListAndCount;
import com.loyalty.marketplace.offers.helper.dto.OfferReferences;
import com.loyalty.marketplace.offers.helper.dto.PromotionalGiftHelper;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCountDto;
import com.loyalty.marketplace.offers.inbound.dto.PromotionalGiftRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.merchants.domain.model.MerchantDomain;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferList;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffersContextAware;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.dto.CreateOfferStatusDto;
import com.loyalty.marketplace.offers.outbound.dto.EligibleOffersResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogShortResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.PaymentMethodResponse;
import com.loyalty.marketplace.offers.outbound.dto.PromotionalGiftResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.RestaurantDto;
import com.loyalty.marketplace.offers.outbound.dto.RestaurantResultResponse;
import com.loyalty.marketplace.offers.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.offers.utils.FilterValues;
import com.loyalty.marketplace.offers.utils.MapValues;
import com.loyalty.marketplace.offers.utils.OfferValidator;
import com.loyalty.marketplace.offers.utils.Predicates;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Requests;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileContent;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileInfo;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileContentRepository;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileInfoRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.dto.ApiError;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.subscription.outbound.dto.HbFileDto;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MarketplaceValidator;
import com.loyalty.marketplace.utils.Utils;
import com.mongodb.MongoException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@Component
public class OfferCatalogDomain {

	private static final Logger LOG = LoggerFactory.getLogger(OfferCatalogDomain.class);
	private static final String GET_ALL_RESTAURANTS = "getAllRestaurants";

	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	OffersHelper helper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	BirthdayGiftTrackerDomain birthdayGiftTrackerDomain;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	EventHandler eventHandler;
		
	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	@Value(OffersConfigurationConstants.ADMIN)
	@Getter(AccessLevel.NONE)
	protected String adminRole;
	
	@Value(OffersConfigurationConstants.OFFER_UPADTE_ROLES)
	@Getter(AccessLevel.NONE)
	protected List<String> roles;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	EligibleOfferList eligibleOfferList;
	
	@Autowired
	private SubscriptionManagementController subscriptionManagementController;
	
	@Autowired
	EligibleOffersContextAware eligibleOffersContextAware;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	GiftRepository giftRepository;
	
	@Autowired
	UploadedFileInfoRepository uploadedFileInfoRepository;
	
	@Autowired
	UploadedFileContentRepository uploadedFileContentRepository;
	
	@Autowired
	OfferRepository offerRepository; 
	
	@Autowired
	MarketplaceFileHelper marketplaceFileHelper;
	
	@Autowired
	UploadedFileContentDomain uploadedFileContentDomain;
	
	@Autowired
	OffersHelper offersHelper;
	
	private String id;
	private String offerId;
	private String programCode;
	private String offerCode;
	private OfferTypeDomain offerType;
	private OfferDetailsDomain offer;
	private BrandDescriptionDomain brandDescription;
	private TermsConditionsDomain termsAndConditions;
	private TagsDomain tags;
	private WhatYouGetDomain whatYouGet;
	private OfferDateDomain offerDates;
	private Integer trendingRank;
	private String status;
	private List<String> availableInPortals;
	private String newOffer;
	private GiftInfoDomain giftInfo;
	private String isDod;
	private String isFeatured;
	private OfferValuesDomain offerValues;
	private Integer discountPerc;
	private Double estSavings; 
	private String sharing;
	private Integer sharingBonus;
	private Double vatPercentage;
	private List<OfferLimitDomain> limit;
	private String partnerCode;
	private MerchantDomain merchant;
	private List<StoreDomain> offerStores;
	private CategoryDomain category;
	private CategoryDomain subCategory;
	private String dynamicDenomination;
	private String groupedFlag;
	private DynamicDenominationValueDomain dynamicDenominationValue;
	private Integer incrementalValue;
	private ListValuesDomain customerTypes;
	private ListValuesDomain customerSegments;
	private List<DenominationDomain> denominations;
	private List<String> rules;
	private String provisioningChannel;
	private boolean proratedBundle;
	private ProvisioningAttributesDomain provisioningAttributes;
	private List<SubOfferDomain> subOffer;
	private Double earnMultiplier;
	private AccrualDetailsDomain accrualDetails;
	private ActivityCodeDomain activityCode;
	private String isBirthdayGift;
	private Integer staticRating;
	private OfferRatingDomain offerRating;
	private VoucherInfoDomain voucherInfo;
	private FreeOfferDomain freeOffers;
	private SubscriptionDetailsDomain subscriptionDetails;
	private List<PaymentMethodDomain> paymentMethods;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	private RestaurantDomain restaurant;
	
	public OfferCatalogDomain(OfferCatalogBuilder offer) {
		
		this.id = offer.id;
		this.programCode = offer.programCode;
		this.offerId = offer.offerId;
		this.offerCode = offer.offerCode;
		this.offerType = offer.offerType;
		this.offer = offer.offer;
		this.brandDescription = offer.brandDescription;
		this.termsAndConditions = offer.termsAndConditions;
		this.tags = offer.tags;
		this.whatYouGet = offer.whatYouGet;
		this.offerDates = offer.offerDates;
		this.trendingRank = offer.trendingRank;
		this.status = offer.status;
		this.availableInPortals = offer.availableInPortals;
		this.newOffer = offer.newOffer;
		this.giftInfo = offer.giftInfo;
		this.isDod = offer.isDod;
		this.isFeatured = offer.isFeatured;
		this.offerValues = offer.offerValues;
		this.discountPerc = offer.discountPerc;
		this.estSavings = offer.estSavings;
		this.sharing = offer.sharing;
		this.sharingBonus = offer.sharingBonus;
		this.vatPercentage = offer.vatPercentage;
		this.limit = offer.limit;
		this.partnerCode = offer.partnerCode;
		this.merchant = offer.merchant;
		this.offerStores = offer.offerStores;
		this.category = offer.category;
		this.subCategory = offer.subCategory;
		this.dynamicDenomination = offer.dynamicDenomination;
		this.groupedFlag = offer.groupedFlag;
		this.dynamicDenominationValue = offer.dynamicDenominationValue;
		this.incrementalValue = offer.incrementalValue;
		this.customerTypes = offer.customerTypes;
		this.customerSegments = offer.customerSegments;
		this.denominations = offer.denominations;
		this.rules = offer.rules;
		this.provisioningChannel = offer.provisioningChannel;
		this.proratedBundle = offer.proratedBundle;
		this.provisioningAttributes = offer.provisioningAttributes;
		this.subOffer = offer.subOffer;
		this.earnMultiplier = offer.earnMultiplier;
		this.accrualDetails = offer.accrualDetails;
		this.activityCode = offer.activityCode;
		this.isBirthdayGift = offer.isBirthdayGift;
		this.staticRating = offer.staticRating;
		this.offerRating = offer.offerRating;
		this.voucherInfo = offer.voucherInfo;
		this.freeOffers = offer.freeOffers;
		this.subscriptionDetails = offer.subscriptionDetails;
		this.paymentMethods = offer.paymentMethods;
		this.restaurant = offer.restaurant;
		this.createdDate = offer.createdDate;
		this.createdUser = offer.createdUser;
		this.updatedDate = offer.updatedDate;
		this.updatedUser = offer.updatedUser;
	}
	
	public static class OfferCatalogBuilder {

		private String id;
		private String offerId;
		private String programCode;
		private String offerCode;
		private OfferTypeDomain offerType;
		private OfferDetailsDomain offer;
		private BrandDescriptionDomain brandDescription;
		private TermsConditionsDomain termsAndConditions;
		private TagsDomain tags;
		private WhatYouGetDomain whatYouGet;
		private OfferDateDomain offerDates;
		private Integer trendingRank;
		private String status;
		private List<String> availableInPortals;
		private String newOffer;
		private GiftInfoDomain giftInfo;
		private String isDod;
		private String isFeatured;
		private OfferValuesDomain offerValues;
		private Integer discountPerc;
		private Double estSavings; 
		private String sharing;
		private Integer sharingBonus;
		private Double vatPercentage;
		private List<OfferLimitDomain> limit;
		private String partnerCode;
		private MerchantDomain merchant;
		private List<StoreDomain> offerStores;
		private CategoryDomain category;
		private CategoryDomain subCategory;
		private String dynamicDenomination;
		private String groupedFlag;
		private DynamicDenominationValueDomain dynamicDenominationValue;
		private Integer incrementalValue;
		private ListValuesDomain customerTypes;
		private ListValuesDomain customerSegments;
		private List<DenominationDomain> denominations;
		private List<String> rules;
		private String provisioningChannel;
		private boolean proratedBundle;
		private ProvisioningAttributesDomain provisioningAttributes;
		private List<SubOfferDomain> subOffer;
		private Double earnMultiplier;
		private AccrualDetailsDomain accrualDetails;
		private ActivityCodeDomain activityCode;
		private String isBirthdayGift;
		private Integer staticRating;
		private OfferRatingDomain offerRating;
		private VoucherInfoDomain voucherInfo;
		private FreeOfferDomain freeOffers;
		private SubscriptionDetailsDomain subscriptionDetails;
		private List<PaymentMethodDomain> paymentMethods;
		private RestaurantDomain restaurant;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;
		
		public OfferCatalogBuilder(String offerCode, OfferTypeDomain offerType, 
				CategoryDomain category, CategoryDomain subCategory, String partnerCode, MerchantDomain merchant,
				List<StoreDomain> offerStores) {
		
			this.offerCode = offerCode;
			this.offerType = offerType;
			this.category = category;
			this.subCategory = subCategory;
			this.partnerCode = partnerCode;
			this.merchant = merchant;
			this.offerStores = offerStores;
		}
		
		public OfferCatalogBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public OfferCatalogBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public OfferCatalogBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}
		
		public OfferCatalogBuilder offer(OfferDetailsDomain offer) {
			this.offer = offer;
			return this;
		}
		
		public OfferCatalogBuilder brandDescription(BrandDescriptionDomain brandDescription) {
			this.brandDescription = brandDescription;
			return this;
		}
		
		public OfferCatalogBuilder tAndC(TermsConditionsDomain termsAndConditions) {
			this.termsAndConditions = termsAndConditions;
			return this;
		}
		
		public OfferCatalogBuilder tags(TagsDomain tags) {
			this.tags = tags;
			return this;
		}
		
		public OfferCatalogBuilder whatYouGet(WhatYouGetDomain whatYouGet) {
			this.whatYouGet = whatYouGet;
			return this;
		}
		
		public OfferCatalogBuilder offerDates(OfferDateDomain offerDates) {
			this.offerDates = offerDates;
			return this;
		}
		
		public OfferCatalogBuilder trendingRank(Integer trendingRank) {
			this.trendingRank = trendingRank;
			return this;
		}
		
		public OfferCatalogBuilder status(String status) {
			this.status = status;
			return this;
		}
		
		public OfferCatalogBuilder availableInPortals(List<String> availableInPortals) {
			this.availableInPortals = availableInPortals;
			return this;
		}
		
		public OfferCatalogBuilder newOffer(String newOffer) {
			this.newOffer = newOffer;
			return this;
		}
		
		public OfferCatalogBuilder giftInfo(GiftInfoDomain giftInfo) {
			this.giftInfo = giftInfo;
			return this;
		}
		
		public OfferCatalogBuilder isDod(String isDod) {
			this.isDod = isDod;
			return this;
		}
		
		public OfferCatalogBuilder isFeatured(String isFeatured) {
			this.isFeatured = isFeatured;
			return this;
		}
		
		public OfferCatalogBuilder offerValues(OfferValuesDomain offerValues) {
			this.offerValues = offerValues;
			return this;
		}
		
		public OfferCatalogBuilder discountPerc(Integer discountPerc) {
			this.discountPerc = discountPerc;
			return this;
		}
		
		public OfferCatalogBuilder estSavings(Double estSavings) {
			this.estSavings = estSavings;
			return this;
		}
		
		public OfferCatalogBuilder sharing(String sharing) {
			this.sharing = sharing;
			return this;
		}
				
		public OfferCatalogBuilder sharingBonus(Integer sharingBonus) {
			this.sharingBonus = sharingBonus;
			return this;
		}
		
		public OfferCatalogBuilder offerRating(OfferRatingDomain offerRating) {
			this.offerRating = offerRating;
			return this;
		}
		
		public OfferCatalogBuilder vatPercentage(Double vatPercentage) {
			this.vatPercentage = vatPercentage;
			return this;
		}
		
		public OfferCatalogBuilder limit(List<OfferLimitDomain> limit) {
			this.limit = limit;
			return this;
		}
		
		public OfferCatalogBuilder dynamicDenomination(String dynamicDenomination) {
			this.dynamicDenomination = dynamicDenomination; 
			
			return this;
		}

		public OfferCatalogBuilder groupedFlag(String groupedFlag) {
			this.groupedFlag = groupedFlag;
			return this;
		}
		
		public OfferCatalogBuilder dynamicDenominationValue(DynamicDenominationValueDomain dynamicDenominationValue) {
			this.dynamicDenominationValue = dynamicDenominationValue;
			return this;
		}
		
		public OfferCatalogBuilder incrementalValue(Integer incrementalValue) {
			this.incrementalValue = incrementalValue;
			return this;
		}
		
		public OfferCatalogBuilder customerTypes(ListValuesDomain customerTypes) {
			this.customerTypes = customerTypes;
			return this;
		}
		
		public OfferCatalogBuilder customerSegments(ListValuesDomain customerSegments) {
			this.customerSegments = customerSegments;
			return this;
		}
		
		public OfferCatalogBuilder denominations(List<DenominationDomain> denominations) {
			this.denominations = denominations;
			return this;
		}
		
		public OfferCatalogBuilder rules(List<String> rules) {
			this.rules = rules;
			return this;
		}
		
		public OfferCatalogBuilder provisioningChannel(String provisioningChannel) {
			this.provisioningChannel = provisioningChannel;
			return this;
		}
		
		public OfferCatalogBuilder proratedBundle(Boolean proratedBundle) {
			this.proratedBundle = proratedBundle;
			return this;
		}
		
		public OfferCatalogBuilder provisioningAttributes(ProvisioningAttributesDomain provisioningAttributes) {
			this.provisioningAttributes = provisioningAttributes;
			return this;
		}
		
		public OfferCatalogBuilder subOffer(List<SubOfferDomain> subOffer) {
			
			this.subOffer = subOffer;
			return this;
		}
		
		public OfferCatalogBuilder earnMultiplier(Double earnMultiplier) {
			
			this.earnMultiplier = earnMultiplier;
			return this;
		}
		
		public OfferCatalogBuilder accrualDetails(AccrualDetailsDomain accrualDetails) {
			
			this.accrualDetails = accrualDetails;
			return this;
		}
				
		public OfferCatalogBuilder activityCode(ActivityCodeDomain activityCode) {
			
			this.activityCode = activityCode;
			return this;
		}
		
		public OfferCatalogBuilder isBirthdayGift(String isBirthdayGift) {
			
			this.isBirthdayGift = isBirthdayGift;
			return this;
		}
		
		public OfferCatalogBuilder staticRating(Integer staticRating) {
			
			this.staticRating = staticRating;
			return this;
		}
		
		public OfferCatalogBuilder voucherInfo(VoucherInfoDomain voucherInfo) {
			
			this.voucherInfo = voucherInfo;
			return this;
		}
		
		public OfferCatalogBuilder freeOffers(FreeOfferDomain freeOffers) {
			
			this.freeOffers = freeOffers;
			return this;
		}
		
		public OfferCatalogBuilder subscriptionDetails(SubscriptionDetailsDomain subscriptionDetails) {
			
			this.subscriptionDetails = subscriptionDetails;
			return this;
		}
		
		public OfferCatalogBuilder paymentMethods(List<PaymentMethodDomain> paymentMethods) {
			this.paymentMethods = paymentMethods;
			return this;
		}
		
		public OfferCatalogBuilder restaurants(RestaurantDomain restaurant) {
			this.restaurant = restaurant;
			return this;
		}
		
		public OfferCatalogBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		
		public OfferCatalogBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public OfferCatalogBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public OfferCatalogBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public OfferCatalogDomain build() {
			return new OfferCatalogDomain(this);
		}

	}
	
	/**
	 * Domain method to save/update offer to repository
	 * @param offerDomain
	 * @param action
	 * @param fetchedOffer
	 * @param header
	 * @return saved/updated offer
	 * @throws MarketplaceException
	 */
	public OfferCatalog saveUpdateOffer(OfferCatalogDomain offerDomain, String action, OfferCatalog fetchedOffer, Headers header) throws MarketplaceException {
		
		try {
			
			LOG.info("offerDomain on entry into saveUpdateOffer:{}", offerDomain);
			OfferCatalog offerCatalog = modelMapper.map(offerDomain, OfferCatalog.class);
			
			LOG.info("offerCatalog:{}", offerCatalog);
			Category cat = modelMapper.map(offerDomain.getCategory(),Category.class);
			offerCatalog.setCategory(cat);
			LOG.info("cat:{}", cat);
			Category subCat = modelMapper.map(offerDomain.getSubCategory(),Category.class);
			offerCatalog.setSubCategory(subCat);
			LOG.info("subCat:{}", subCat);
			offerCatalog.setGiftInfo(ProcessValues.getGiftInfoEntity(offerDomain.getGiftInfo()));
			
			OfferCatalog savedOffer = repositoryHelper.saveOffer(offerCatalog);
			
			LOG.info("savedOffer:{}", savedOffer);
			if(action.equals(OfferConstants.INSERT_ACTION.get())) {
				
//				auditService.insertDataAudit(OffersDBConstants.OFFER_CATALOG, savedOffer, OffersRequestMappingConstants.CREATE_OFFER, header.getExternalTransactionId(), header.getUserName());
			
			} else if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.OFFER_CATALOG, savedOffer, OffersRequestMappingConstants.UPDATE_OFFER, fetchedOffer, header.getExternalTransactionId(), header.getUserName());
			}
			return savedOffer;
			
		} catch (MongoException mongoException) {
			
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_OFFER_REPOSITORY_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferErrorCodes.OFFER_CREATION_FAILED);
		
		}  catch (Exception e) {
			
			OfferExceptionCodes exception = action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())
					? OfferExceptionCodes.SAVE_OFFER_DOMAIN_RUNTIME_EXCEPTION
					: OfferExceptionCodes.OFFER_PURCHASE_HISTORY_UPDATION_RUNTIME_EXCEPTION; 
		
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_OFFER_REPOSITORY_METHOD.get(),
					e.getClass() + e.getMessage(), exception);
		
		}
		
	}

	/**
	 * Domain method to update offer status
	 * @param fetchedOffer
	 * @param status
	 * @param userName
	 * @param externalTransactionId
	 * @return updated offer
	 * @throws MarketplaceException
	 */
	public OfferCatalog updateOfferStatus(OfferCatalog fetchedOffer, String status, String userName,
			String externalTransactionId) throws MarketplaceException {
		
		try {
			
			Gson gson = new Gson();
			OfferCatalog originalOffer = gson.fromJson(gson.toJson(fetchedOffer), OfferCatalog.class);
			
			if (OfferConstants.ACTIVE_STATUS.get().equalsIgnoreCase(status)) {
				
				fetchedOffer.setStatus(OfferConstants.ACTIVE_STATUS.get());
				
			} else if(OfferConstants.OFFER_DEFAULT_STATUS.get().equalsIgnoreCase(status)) {
				
				fetchedOffer.setStatus(OfferConstants.OFFER_DEFAULT_STATUS.get());
			}
			
			fetchedOffer.setUpdatedUser(userName);
			fetchedOffer.setUpdatedDate(new Date());
			OfferCatalog savedOffer = repositoryHelper.saveOffer(fetchedOffer);
			auditService.updateDataAudit(OffersDBConstants.OFFER_CATALOG, savedOffer, OffersRequestMappingConstants.UPDATE_OFFER, originalOffer,  externalTransactionId, userName);
			return savedOffer;
			
		} catch (MongoException mongoException) {
		
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_OFFER_STATUS_REPOSITORY_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
		
		} catch (Exception e) {
		
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_OFFER_STATUS_REPOSITORY_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.UPDATE_OFFER_STATUS_REPOSITORY_RUNTIME_EXCEPTION);
		
		}
		
	}
	
	/**
	 * Domain method to validate and save offer
	 * @param offerCatalogRequest
	 * @param header
	 * @return status after saving offer
	 */
	public ResultResponse validateAndSaveOffer(OfferCatalogDto offerCatalogRequest, Headers header){
		
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		
		try {
			
			header.setProgram(programManagement.getProgramCode(header.getProgram()));
			
			if(OfferValidator.validateDto(offerCatalogRequest, validator, resultResponse)
			&& OfferValidator.validateOfferCatalog(offerCatalogRequest, resultResponse)) {
				
				OfferCatalog offerDetails = repositoryHelper.findByOfferCode(offerCatalogRequest.getOfferCode());
				
				offerCatalogRequest.setAction(OfferConstants.INSERT_ACTION.get());
				
				OfferReferences offerReference = new OfferReferences();
				offerReference.setHeader(header);
		         		
				if(Responses.setResponseAfterConditionCheck(ObjectUtils.isEmpty(offerDetails), OfferErrorCodes.OFFER_EXISTS, resultResponse)
				&& helper.validateAndGetActivityCodeForCreate(offerCatalogRequest, resultResponse, offerReference)) {
				     
					offerReference.setSize(repositoryHelper.getOfferSize());
					OfferCatalogDomain offerDomain = DomainConfiguration.getOfferDomain(header, offerDetails, offerCatalogRequest, offerReference);
					
					if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerDomain), OfferErrorCodes.CANNOT_CREATE_OFFER_WITH_OFFER_TYPE, resultResponse)) {
						
						offerDetails = saveUpdateOffer(offerDomain, OfferConstants.INSERT_ACTION.get(), null, header);
						
						if(Checks.checkNoErrors(resultResponse)
						&& !ObjectUtils.isEmpty(offerDetails)) {
									
							Responses.setResultStatus(resultResponse, new CreateOfferStatusDto(offerDetails.getOfferId()));
						}
						
					}	
					
				}
				
			}
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.VALIDATE_AND_SAVE_OFFER.get(), null, e, OfferErrorCodes.OFFER_CREATION_FAILED, null,
					OfferErrorCodes.OFFER_CREATION_FAILED,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.VALIDATE_AND_SAVE_OFFER.get(), e, null, OfferErrorCodes.OFFER_CREATION_FAILED,
					OfferExceptionCodes.SAVE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
				
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.OFFER_CREATION_FAILED, ProcessValues.getCreateUpdateOfferSuccessCodeValue(offerCatalogRequest.getOfferTypeId(), offerCatalogRequest.getProvisioningChannel(), offerCatalogRequest.getAction()));
		return resultResponse;
		
	}
			
	/**
	 * Domain method to validate and update offer
	 * @param offerCatalogRequest
	 * @param offerId
	 * @param header
	 * @param userRole
	 * @return status after updating offer
	 */
	public ResultResponse validateAndUpdateOffer(OfferCatalogDto offerCatalogRequest, String offerIdentifier, 
			Headers header, String userRole, OfferCountDto offerCountDto){
		
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		String log = Logs.logForVariable(OffersRequestMappingConstants.OFFER_ID, offerIdentifier);
	    LOG.info(log);
	    
		try {
			 
             if(Responses.setResponseAfterConditionCheck(!StringUtils.isEmpty(offerCatalogRequest.getAction()), OfferErrorCodes.EMPTY_ACTION, resultResponse)
             && OfferValidator.validateOfferUpdateAction(offerCatalogRequest.getAction(), resultResponse)
			 && Responses.setResponseAfterConditionCheck(Utils.checkRolesExists(userRole, roles), OfferErrorCodes.UNAUTHORIZED_ROLE, resultResponse)) {
            	 
            	 boolean isAdminRole = Utils.checkRoleExists(userRole, adminRole);
            	 
            	 if(Utilities.presentInList(OffersListConstants.ACTIVATE_DEACTIVATE_ACTION_LIST, offerCatalogRequest.getAction())
            	 && Responses.setResponseAfterConditionCheck(isAdminRole, OfferErrorCodes.UNAUTHORIZED_ROLE, resultResponse)) {
            	    
            		 String offerStatus = (OfferConstants.ACTIVATE_ACTION.get().equalsIgnoreCase(offerCatalogRequest.getAction()))
								?(OfferConstants.ACTIVE_STATUS.get())
								:(OfferConstants.INACTIVE_STATUS.get());
				
            		 return changeOfferStatus(offerIdentifier, offerStatus, header, resultResponse, offerCountDto);
            		 
            	 } else if(StringUtils.equalsIgnoreCase(OfferConstants.UPDATE_ACTION.get(), offerCatalogRequest.getAction())) {
                   
            		 updateOffer(header, offerIdentifier, resultResponse, offerCatalogRequest, offerCountDto);
            		 
            	 } 
            	 
            }
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.VALIDATE_AND_UPDATE_OFFER.get(),
                    e.getClassName()+ OfferConstants.COMMA_OPERATOR.get()+e.getMethodName() + OfferConstants.MESSAGE_SEPARATOR.get()
                    +e.getDetailMessage(), OfferErrorCodes.OFFER_UPDATION_FAILED).printMessage());
			resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg()+OfferConstants.REFER_LOGS.get());
			resultResponse.setResult(OfferErrorCodes.OFFER_UPDATION_FAILED.getId(),
					OfferErrorCodes.OFFER_UPDATION_FAILED.getMsg());
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.VALIDATE_AND_UPDATE_OFFER.get(),
					e.getClass() + e.getMessage(),OfferExceptionCodes.UPDATE_OFFER_DOMAIN_RUNTIME_EXCEPTION).printMessage());
			resultResponse.setErrorAPIResponse(OfferExceptionCodes.UPDATE_OFFER_DOMAIN_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.UPDATE_OFFER_DOMAIN_RUNTIME_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
			resultResponse.setResult(OfferErrorCodes.OFFER_UPDATION_FAILED.getId(),
					OfferErrorCodes.OFFER_UPDATION_FAILED.getMsg());
			
		}

		Responses.setResponse(resultResponse, OfferErrorCodes.OFFER_UPDATION_FAILED, ProcessValues.getCreateUpdateOfferSuccessCodeValue(offerCatalogRequest.getOfferTypeId(), offerCatalogRequest.getProvisioningChannel(), offerCatalogRequest.getAction()));
		return resultResponse;
	}
	
	/**
	 * Domain method to update offer
	 * @param header
	 * @param offerIdentifier 
	 * @param resultResponse
	 * @param offerCatalogRequest
	 * @throws MarketplaceException
	 * @throws ParseException
	 */
	private void updateOffer(Headers header, String offerIdentifier, ResultResponse resultResponse, OfferCatalogDto offerCatalogRequest, 
			OfferCountDto offerCountDto) throws MarketplaceException, ParseException {
		
		if (MarketplaceValidator.validateDto(offerCatalogRequest, validator, resultResponse)
      	 && OfferValidator.validateOfferCatalog(offerCatalogRequest, resultResponse)) {
      							
  			 OfferCatalog fetchedOffer = repositoryHelper.findByOfferId(offerIdentifier);
  			 OfferReferences offerReference = new OfferReferences();
			 offerReference.setHeader(header);
			 
			 if(!ObjectUtils.isEmpty(fetchedOffer)) {
				 
				 ProcessValues.setUpdateParameters(fetchedOffer, offerCatalogRequest);
			 }
			 
  			 if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(fetchedOffer), OfferErrorCodes.OFFER_NOT_AVAIABLE_UPDATE, resultResponse)
  			 && OfferValidator.validateUpdateRequest(offerCatalogRequest, fetchedOffer, resultResponse)
             && helper.validateAndUpdateEarnMultiplerForUpdate(offerCatalogRequest, fetchedOffer, resultResponse, offerReference, offerCountDto)) {
  				 
  				 OfferCatalogDomain offerDomainToUpdate = DomainConfiguration.getOfferDomain(header, fetchedOffer, offerCatalogRequest, offerReference);
  				 saveUpdateOffer(offerDomainToUpdate, OfferConstants.UPDATE_ACTION.get(), fetchedOffer, header);
  			 }
      			 
      	}
		
	}

	/**
	 * Domain method to get all offers for administrator
	 * @param header
	 * @param pageLimit 
	 * @param page 
	 * @param status 
	 * @param offerType 
	 * @return list of filtered offers for administartor
	 */
	public OfferCatalogShortResultResponse getAllOffersForAdministrator(Headers header, Integer page, Integer pageLimit, String offerType, String status) {
		
		OfferCatalogShortResultResponse offerCatalogResultResponse = new OfferCatalogShortResultResponse(header.getExternalTransactionId());
				
		try {
			
			if(Responses.setResponseAfterConditionCheck(Checks.checkPageNumberAndPageLimitCombination(page, pageLimit), OfferErrorCodes.PAGE_LIMIT_NUMBER_TOGETHER, offerCatalogResultResponse)
			&& Checks.checkPageNumberAndPageLimitValid(page, pageLimit, offerCatalogResultResponse)
			&& Responses.setResponseAfterConditionCheck(Checks.checkValidOfferStatus(status), OfferErrorCodes.INVALID_STATUS, offerCatalogResultResponse)) {
				
				if(StringUtils.equalsIgnoreCase(status, OfferConstants.ALL.get())) {
					status = null;
				}
				
				OfferListAndCount offerListAndCount = repositoryHelper.findAllAdminOffers(page, pageLimit, offerType, status, offerCatalogResultResponse, header);
				
				if (Checks.checkNoErrors(offerCatalogResultResponse)
				 && Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerListAndCount), OfferErrorCodes.NO_OFFERS_TO_DISPLAY, offerCatalogResultResponse)
				 && Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offerListAndCount.getOfferList()), OfferErrorCodes.NO_OFFERS_TO_DISPLAY, offerCatalogResultResponse)) {
					
					offerCatalogResultResponse.setTotalRecordCount(offerListAndCount.getCount());
					offerCatalogResultResponse.setOfferList(helper.getConvertedOfferShortResponseList(offerListAndCount.getOfferList()));
				}
				
			} 
			
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ADMIN_OFFERS_DOMAIN.get(), null, e, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, null,
					OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ADMIN_OFFERS_DOMAIN.get(), e, null, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					OfferExceptionCodes.GET_ADMIN_OFFERS_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
		} 
			
		Responses.setResponse(offerCatalogResultResponse, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, OfferSuccessCodes.OFFERS_LISTED_SUCCESSFULLY);
		return offerCatalogResultResponse;
	}
	
	/**
	 * Domain method to get all offers for a partner
	 * @param header
	 * @param partnerCode
	 * @param limit
	 * @param page 
	 * @param status 
	 * @param offerType 
	 * @return list of filtered offers for partner
	 */
	public OfferCatalogShortResultResponse getAllOffersForPartner(Headers header, String partnerCode, Integer page, Integer pageLimit, String offerType, String status){
		
		OfferCatalogShortResultResponse offerCatalogResultResponse = new OfferCatalogShortResultResponse(header.getExternalTransactionId());
		
		try {
			
			if(Responses.setResponseAfterConditionCheck(Checks.checkPageNumberAndPageLimitCombination(page, pageLimit), OfferErrorCodes.PAGE_LIMIT_NUMBER_TOGETHER, offerCatalogResultResponse)
			&& Checks.checkPageNumberAndPageLimitValid(page, pageLimit, offerCatalogResultResponse)
			&& Responses.setResponseAfterConditionCheck(Checks.checkValidOfferStatus(status), OfferErrorCodes.INVALID_STATUS, offerCatalogResultResponse)
			&& Responses.setResponseAfterConditionCheck(fetchServiceValues.checkPartnerExists(partnerCode, header), OfferErrorCodes.INVALID_PARTNER_CODE, offerCatalogResultResponse)) {
				
				if(StringUtils.equalsIgnoreCase(status, OfferConstants.ALL.get())) {
					status = null;
				}
				
				//Loyalty as a service.
				OfferListAndCount offerListAndCount = repositoryHelper.findOffersByPartnerCode(partnerCode, page, pageLimit, offerType, status, offerCatalogResultResponse, header);
				
				if (Checks.checkNoErrors(offerCatalogResultResponse)
				 && Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerListAndCount), OfferErrorCodes.NO_OFFERS_FOR_PARTNER_TO_DISPLAY, offerCatalogResultResponse)
				 && Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offerListAndCount.getOfferList()), OfferErrorCodes.NO_OFFERS_FOR_PARTNER_TO_DISPLAY, offerCatalogResultResponse)) {
					
					offerCatalogResultResponse.setTotalRecordCount(offerListAndCount.getCount());
					offerCatalogResultResponse.setOfferList(helper.getConvertedOfferShortResponseList(offerListAndCount.getOfferList()));
				}
				
			}

			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_PARTNER_OFFERS_DOMAIN.get(), null, e, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, null,
					OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_PARTNER_OFFERS_DOMAIN.get(), e, null, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					OfferExceptionCodes.GET_PARTNER_OFFERS_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
				
		}
		
		Responses.setResponse(offerCatalogResultResponse, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, OfferSuccessCodes.OFFERS_LISTED_FOR_PARTNER_SUCCESSFULLY);
		return offerCatalogResultResponse;
	}
	
	/**
	 * Loyalty as a service.
	 * Domain method to get all offers for a merchant
	 * @param header
	 * @param merchantCode
	 * @param pageLimit 
	 * @param page 
	 * @param status 
	 * @param offerType 
	 * @return list of filtered offers for merchant
	 */
	public OfferCatalogShortResultResponse getAllOffersForMerchant(Headers header, String merchantCode, Integer page, Integer pageLimit, String offerType, String status){
		
		//Loyalty as a service.
		OfferCatalogShortResultResponse offerCatalogResultResponse = new OfferCatalogShortResultResponse(header.getExternalTransactionId());
		
		try {
			
			if(Responses.setResponseAfterConditionCheck(Checks.checkPageNumberAndPageLimitCombination(page, pageLimit), OfferErrorCodes.PAGE_LIMIT_NUMBER_TOGETHER, offerCatalogResultResponse)
			&& Checks.checkPageNumberAndPageLimitValid(page, pageLimit, offerCatalogResultResponse)
			&& Responses.setResponseAfterConditionCheck(Checks.checkValidOfferStatus(status), OfferErrorCodes.INVALID_STATUS, offerCatalogResultResponse)) {
				
				//Merchant fetchedMerchant = repositoryHelper.getMerchant(merchantCode);
				Merchant fetchedMerchant = repositoryHelper.getMerchantByProgramCode(merchantCode, header);
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(fetchedMerchant), OfferErrorCodes.INVALID_MERCHANT_CODE, offerCatalogResultResponse)) {
					
					if(StringUtils.equalsIgnoreCase(status, OfferConstants.ALL.get())) {
						status = null;
					}
					
					//Loyalty as a service.
					OfferListAndCount offerListAndCount = repositoryHelper.findOfferByMerchant(fetchedMerchant, page, pageLimit, offerType, status, offerCatalogResultResponse, header);
					
					if (Checks.checkNoErrors(offerCatalogResultResponse)
					 && Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerListAndCount), OfferErrorCodes.NO_OFFERS_FOR_MERCHANT_TO_DISPLAY, offerCatalogResultResponse)
					 && Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offerListAndCount.getOfferList()), OfferErrorCodes.NO_OFFERS_FOR_MERCHANT_TO_DISPLAY, offerCatalogResultResponse)) {
						
						offerCatalogResultResponse.setTotalRecordCount(offerListAndCount.getCount());
						offerCatalogResultResponse.setOfferList(helper.getConvertedOfferShortResponseList(offerListAndCount.getOfferList()));
					}
					
				}
				
				
			}
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_MERCHANT_OFFERS_DOMAIN.get(), null, e, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, null,
					OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_MERCHANT_OFFERS_DOMAIN.get(), e, null, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					OfferExceptionCodes.GET_MERCHANT_OFFERS_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
				
		}
		
		Responses.setResponse(offerCatalogResultResponse, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, OfferSuccessCodes.OFFERS_LISTED_FOR_MERCHANT_SUCCESSFULLY);
		return offerCatalogResultResponse;
	}
	
	/**
	 * Domain method to get all offers for detailed offer for administarator/ partner/ merchant
	 * @param header
	 * @param offerId
	 * @return detailed offer
	 */
	public OfferCatalogResultResponse getDetailedOfferPortal(Headers header, String offerId){
		
		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(header.getExternalTransactionId());
		List<OfferCatalogResultResponseDto> offerCatalogList = null;

		try {
			
			//Loyalty as a service.
			//OfferCatalog offerCatalog = repositoryHelper.findByOfferId(offerId);
			OfferCatalog offerCatalog = repositoryHelper.findByOfferIdAndProgramCode(offerId, header);
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerCatalog), OfferErrorCodes.OFFER_NOT_AVAILABLE, offerCatalogResultResponse)) {
				//false for not including offer level payment methods for selecting eligible payment methods
				offerCatalogList = helper.getConvertedOfferResponseList(Arrays.asList(offerCatalog), header.getChannelId(), header, false);
			
			
			}
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_DETAIL_OFFER_DOMAIN.get(), null, e, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, null,
					OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_DETAIL_OFFER_DOMAIN.get(), e, null, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED,
					OfferExceptionCodes.GET_DETAIL_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
				
		}  
		
		offerCatalogResultResponse.setOfferCatalogs(offerCatalogList);
		Responses.setResponse(offerCatalogResultResponse, OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED, OfferSuccessCodes.SPECIFIC_OFFER_PORTAL_LISTED_SUCCESSFULLY);
		return offerCatalogResultResponse;
		
	}

	/**
	 * Domain method to change method status
	 * @param offerId
	 * @param status
	 * @param userName
	 * @param externalTransactionId
	 * @param resultResponse
	 * @return status after changing offer status
	 */
	public ResultResponse changeOfferStatus(String offerIdentifier, String status, Headers header, ResultResponse resultResponse, OfferCountDto offerCountDto) {
		
		Responses.setResponseAfterConditionCheck(OfferConstants.ACTIVE_STATUS.get().equalsIgnoreCase(status)
				|| OfferConstants.OFFER_DEFAULT_STATUS.get().equalsIgnoreCase(status), OfferErrorCodes.INVALID_OFFER_STATUS, resultResponse);
		
		OfferSuccessCodes succesResult = status.equalsIgnoreCase(OfferConstants.OFFER_DEFAULT_STATUS.get())
				? OfferSuccessCodes.OFFER_DEACTIVATED
				: OfferSuccessCodes.OFFER_ACTIVATED; 
		
		try {
			
			if(Checks.checkNoErrors(resultResponse)) {
				
				OfferCatalog fetchedOffer = repositoryHelper.findByOfferId(offerIdentifier);
				offerCountDto.setFetchedOffer(fetchedOffer);

				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(fetchedOffer), OfferErrorCodes.OFFER_NOT_AVAIABLE_UPDATE, resultResponse)) {
					
					OfferErrorCodes errorCode= status.equalsIgnoreCase(OfferConstants.OFFER_DEFAULT_STATUS.get())
							? OfferErrorCodes.OFFER_DEACTIVATED_ALREADY
							: OfferErrorCodes.OFFER_ACTIVATED_ALREADY;
					
					if (Responses.setResponseAfterConditionCheck(!fetchedOffer.getStatus().equalsIgnoreCase(status), errorCode, resultResponse)) {

						updateOfferStatus(fetchedOffer, status,	header.getUserName(), header.getExternalTransactionId());
			
					} 
					
				}
				
			}
				
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.CHANGE_OFFER_STATUS.get(),
                    e.getClassName()+ OfferConstants.COMMA_OPERATOR.get()+e.getMethodName() + OfferConstants.MESSAGE_SEPARATOR.get()
                    +e.getDetailMessage(), OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED).printMessage());
			resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg()+OfferConstants.REFER_LOGS.get());
			
			resultResponse.setResult(OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED.getId(),
					OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED.getMsg());
			
		} catch (Exception e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.CHANGE_OFFER_STATUS.get(),
					e.getClass() + e.getMessage(),OfferExceptionCodes.UPDATE_OFFER_DOMAIN_RUNTIME_EXCEPTION).printMessage());
			resultResponse.addErrorAPIResponse(OfferExceptionCodes.UPDATE_OFFER_DOMAIN_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.UPDATE_OFFER_DOMAIN_RUNTIME_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
			resultResponse.setResult(OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED.getId(),
					OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED.getMsg());
			
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED, succesResult);
		return resultResponse;
	}

	/**
	 * Domain method to get payment methods common for member and purchase item
	 * @param purchaseItem
	 * @param accountNumber
	 * @param header
	 * @return list of eligible payment methods 
	 */
	public PaymentMethodResponse getEligiblePaymentMethods(String purchaseItem, String accountNumber,
			Headers header) {
		
		PaymentMethodResponse paymentMethodResponse = new PaymentMethodResponse(header.getExternalTransactionId());
		List<String> eligiblePaymentMethods = null;
		
		try {
			
            OfferCatalog offerCatalog = null;
			PurchasePaymentMethod purchasePaymentMethod = repositoryHelper.getPaymentMethodsForPurchaseItem(purchaseItem);
			List<PaymentMethod> itemPaymentMethods = null;
			
			if(ObjectUtils.isEmpty(purchasePaymentMethod)) {
				
				offerCatalog = repositoryHelper.findByOfferId(purchaseItem);
				if(!ObjectUtils.isEmpty(offerCatalog) && !ObjectUtils.isEmpty(offerCatalog.getOfferType())) {
					
					//true for including offer level payment methods for selecting eligible payment methods
					itemPaymentMethods = helper.getPaymentMethods(offerCatalog.getOfferType().getOfferTypeId(), offerCatalog.getPaymentMethods(), true);
					LOG.info("itemPaymentMethods from offer : {}", itemPaymentMethods);
				}
			
			} else {
				
				itemPaymentMethods = purchasePaymentMethod.getPaymentMethods();
				LOG.info("itemPaymentMethods from PurchasePaymentMethod : {}", itemPaymentMethods);
			}
			
			if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(itemPaymentMethods), OfferErrorCodes.PURCHASE_PAYMENT_METHOD_RECORD_UNAVAILABLE, paymentMethodResponse)) {
				
				GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, paymentMethodResponse, header);
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(memberDetails), OfferErrorCodes.MEMBER_NOT_AVAILABLE, paymentMethodResponse)
				&& Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(memberDetails.getEligiblePaymentMethod()), OfferErrorCodes.CUSTOMER_PAYMENT_METHOD_NOT_SET, paymentMethodResponse)) {
					
				    eligiblePaymentMethods = MapValues.mapEligiblePaymentMethods(itemPaymentMethods, Predicates.presentInMemberPaymentMethod(memberDetails.getEligiblePaymentMethod()));
					
				    LOG.info("eligiblePaymentMethods after final mapping : {}", eligiblePaymentMethods);
				}	
					
			}
			
			if(Checks.checkNoErrors(paymentMethodResponse)) {
				
				Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(eligiblePaymentMethods), OfferErrorCodes.NO_PAYMENT_METHODS_TO_DISPLAY, paymentMethodResponse);
			}
		
		} 
		catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(paymentMethodResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_PAYMENT_METHODS.get(), null, e, OfferErrorCodes.LISTING_PAYMENT_METHODS_FAILED, null,
					OfferErrorCodes.LISTING_PAYMENT_METHODS_FAILED,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), accountNumber, header.getUserName())));
			
			
		} 
		catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(paymentMethodResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_PAYMENT_METHODS.get(), e, null, OfferErrorCodes.LISTING_PAYMENT_METHODS_FAILED,
					OfferExceptionCodes.GET_ELIGIBLE_PAYMENT_METHODS_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), accountNumber, header.getUserName())));
				
		}
		
		paymentMethodResponse.setPaymentMethods(eligiblePaymentMethods);
		Responses.setResponse(paymentMethodResponse, OfferErrorCodes.LISTING_PAYMENT_METHODS_FAILED, OfferSuccessCodes.PAYMENT_METHODS_LISTED_SUCCESSFULLY);
		return paymentMethodResponse;
	}
	
	/**
	 * Domain method to get eligible offers list for method
	 * @param eligibleOffersRequest
	 * @param headers
	 * @return list of eligible offers
	 */
	public OfferCatalogResultResponse getEligibleOfferList(EligibleOffersFiltersRequest eligibleOffersRequest, Headers headers) {
		
		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(headers.getExternalTransactionId());
		boolean isMember = !StringUtils.isEmpty(eligibleOffersRequest.getAccountNumber());
		
		OfferErrorCodes errorResult = isMember  
				? OfferErrorCodes.LISTING_OFFERS_MEMBER_FAILED
				: OfferErrorCodes.LISTING_OFFERS_ELIGIBLE_FAILED;
		
		OfferSuccessCodes successResult = isMember 
				? OfferSuccessCodes.OFFERS_LISTED_FOR_MEMBER_SUCCESSFULLY
				: OfferSuccessCodes.ELIGIBLE_OFFERS_LISTED_SUCCESSFULLY;
		
		try {
			
			if(OfferValidator.validateEligibleOfferFilterRequest(eligibleOffersRequest, offerCatalogResultResponse)) {
				
				EligibilityInfo eligibilityInfo = new EligibilityInfo();
				ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, headers, null, isMember);
				
				if(isMember) {
					
					eligibilityInfo.setAccountNumber(eligibleOffersRequest.getAccountNumber());
					helper.setAllAccountDetails(eligibilityInfo, offerCatalogResultResponse);
					
				}
				
				if(Checks.checkNoErrors(offerCatalogResultResponse)) {
					
					repositoryHelper.getEligibleOffersList(eligibleOffersRequest, eligibilityInfo, headers.getChannelId());
					
					if(Checks.checkNoErrors(offerCatalogResultResponse)
					&& Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(eligibilityInfo.getOfferList()), OfferErrorCodes.NO_OFFERS_FOR_MEMBER_TO_DISPLAY, offerCatalogResultResponse)) {
					
					  offerCatalogResultResponse.setTotalRecordCount(eligibilityInfo.getRecordCount());
					  helper.setCounterDetails(eligibilityInfo);
					  offerCatalogResultResponse.setOfferCatalogs(helper.getEligibleOfferList(eligibilityInfo, offerCatalogResultResponse, true, true));
					  offerCatalogResultResponse.setOfferCatalogs(ProcessValues.sortOffers(offerCatalogResultResponse.getOfferCatalogs(), eligibleOffersRequest));
					  
					}
					
				}
				  
			}
			
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_OFFERS_LIST.get(), null, e, errorResult, null,
					errorResult, exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), 
							eligibleOffersRequest.getAccountNumber(), headers.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_OFFERS_LIST.get(), e, null, errorResult,
					OfferExceptionCodes.GET_LIST_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), 
							eligibleOffersRequest.getAccountNumber(), headers.getUserName())));
			
		}
		
		Responses.setResponse(offerCatalogResultResponse, errorResult, successResult);
		return offerCatalogResultResponse;
	}

//	/**
//	 * Domain method to get detailed eligible offer for a member
//	 * @param accountNumber
//	 * @param header
//	 * @param offerIdentifier
//	 * @return detailed eligible offer
//	 */
//	public OfferCatalogResultResponse listDetailedOfferForMember(String accountNumber, Headers header, String offerIdentifier) {
//		
//		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(header.getExternalTransactionId());
//		boolean isMember = !StringUtils.isEmpty(accountNumber);
//		
//		OfferErrorCodes errorResult = isMember
//				? OfferErrorCodes.FETCHING_DETAILED_OFFER_MEMBER_FAILED
//				: OfferErrorCodes.FETCHING_DETAILED_ELIGIBLE_OFFER_FAILED;
//		
//		OfferSuccessCodes successResult = isMember
//				? OfferSuccessCodes.DETAILED_MEMBER_OFFER_DISPLAYED_SUCCESSFULLY
//				: OfferSuccessCodes.DETAILED_ELIGIBLE_OFFER_DISPLAYED_SUCCESSFULLY;
//		
//		List<OfferCatalogResultResponseDto> offerCatalogList = null;
//		
//		try {
//              OfferCatalog offerCatalog = repositoryHelper.findByOfferId(offerIdentifier);
//			    
//			  if(Checks.checkActiveOfferDetails(offerCatalog, header.getChannelId(), offerCatalogResultResponse, false)) {
//				  
//				  List<ConversionRate> conversionRateList = helper.getConversionRateListForOfferList(Arrays.asList(offerCatalog), header.getChannelId());
//				  List<PurchasePaymentMethod> purchasePaymentMethodList = helper.getPurchasePaymentMethodForOfferList(Arrays.asList(offerCatalog));
//				  List<MarketplaceImage> imageList = helper.getImageUrlListForOfferList(Arrays.asList(offerCatalog));
//				   
//				  EligibilityInfo eligibilityInfo = null;
//				 
//				  if(isMember) {
//					  
//					  eligibilityInfo = new EligibilityInfo();
//					  helper.getMemberDetailsForDetailedEligibleOffer(eligibilityInfo, isMember, offerCatalog, header, accountNumber, offerCatalogResultResponse);
//					  eligibilityInfo.setConversionRateList(conversionRateList);
//				  }
//				  
//				  if(Checks.checkNoErrors(offerCatalogResultResponse)) {
//					  
//					  offerCatalogList = new ArrayList<>(1);
//					  OfferCatalogResultResponseDto offerCatalogDto = helper.getOfferResponse(offerCatalog, purchasePaymentMethodList, conversionRateList, imageList);
//					  
//					  boolean subscribed = isMember
//							  && !ObjectUtils.isEmpty(eligibilityInfo)
//					          && !ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())
//					          && eligibilityInfo.getMemberDetails().isSubscribed();
//					  
//					  ProcessValues.setFreeOfferResponse(subscribed, offerCatalog, offerCatalogDto, conversionRateList);
//					 
//					  if(isMember) {
//						
//						  helper.setDetailedOfferResponseForMember(offerCatalogDto, offerCatalog, eligibilityInfo, offerCatalogResultResponse);
//						  
//					  }
//					  offerCatalogList.add(offerCatalogDto);
//				  } 
//				  
//			 }
//              
//        
//		} catch (MarketplaceException e) {
// 			
// 			e.printStackTrace();
// 			
// 			Responses.setResponseAfterException(offerCatalogResultResponse, 
// 					new ExceptionInfo(this.getClass().getSimpleName(), 
// 					OfferConstants.LIST_SPECIFIC_OFFER_FOR_MEMBER.get(), null, e, errorResult, null,
// 					errorResult));
// 			
// 			
// 		} catch (Exception e) {
// 			
// 			e.printStackTrace();
// 			
// 			Responses.setResponseAfterException(offerCatalogResultResponse, 
// 					new ExceptionInfo(this.getClass().getSimpleName(), 
// 					OfferConstants.LIST_SPECIFIC_OFFER_FOR_MEMBER.get(), e, null, errorResult,
// 					OfferExceptionCodes.GET_DETAIL_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null));
// 				
// 		}
//		
//		Responses.setResponse(offerCatalogResultResponse, errorResult, successResult);
//		offerCatalogResultResponse.setOfferCatalogs(offerCatalogList);
//		return offerCatalogResultResponse;
//	}
	
	/**
	 * Domain method to get detailed eligible offer for a member
	 * @param accountNumber
	 * @param header
	 * @param offerIdentifier
	 * @return detailed eligible offer
	 */
	public OfferCatalogResultResponse listSpecificOfferForMember(String accountNumber, Headers header, String offerIdentifier) {
		
		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(header.getExternalTransactionId());
		boolean isMember = !StringUtils.isEmpty(accountNumber);
		
		OfferErrorCodes errorResult = isMember
				? OfferErrorCodes.FETCHING_DETAILED_OFFER_MEMBER_FAILED
				: OfferErrorCodes.FETCHING_DETAILED_ELIGIBLE_OFFER_FAILED;
		
		OfferSuccessCodes successResult = isMember
				? OfferSuccessCodes.DETAILED_MEMBER_OFFER_DISPLAYED_SUCCESSFULLY
				: OfferSuccessCodes.DETAILED_ELIGIBLE_OFFER_DISPLAYED_SUCCESSFULLY;
		
		List<OfferCatalogResultResponseDto> offerCatalogList = null;
		
		try {
              OfferCatalog offerCatalog = repositoryHelper.findByOfferId(offerIdentifier);
			    
			  if(Checks.checkActiveOfferDetails(offerCatalog, header.getChannelId(), offerCatalogResultResponse, false)) {
				  
				  List<ConversionRate> conversionRateList = helper.getConversionRateListForOfferList(Arrays.asList(offerCatalog), header.getChannelId());
				  conversionRateList = FilterValues.filterConversionRateList(conversionRateList, Predicates.applicableRateForPartnerListAndChannelId(Arrays.asList(offerCatalog.getPartnerCode()), header.getChannelId()));
				  List<PurchasePaymentMethod> purchasePaymentMethodList = helper.getPurchasePaymentMethodForOfferList(Arrays.asList(offerCatalog));
				  List<MarketplaceImage> imageList = helper.getImageUrlListForOfferList(Arrays.asList(offerCatalog));
				   
				  EligibilityInfo eligibilityInfo = null;
				 
				  if(isMember) {
					  
					  eligibilityInfo = new EligibilityInfo();
					  helper.getMemberDetailsForEligibleOfferDetail(eligibilityInfo, isMember, offerCatalog, header, accountNumber, offerCatalogResultResponse);
					  eligibilityInfo.setConversionRateList(conversionRateList);
				  }
				  
				  if(Checks.checkNoErrors(offerCatalogResultResponse)) {
					  
					  offerCatalogList = new ArrayList<>(1);
					//true for including offer level payment methods for selecting eligible payment methods
					  OfferCatalogResultResponseDto offerCatalogDto = helper.getOfferResponse(offerCatalog, purchasePaymentMethodList, conversionRateList, imageList, true);
					  
					  ProcessValues.setFreeOfferResponse(offerCatalogDto, eligibilityInfo, true, conversionRateList);
					  
					  if(isMember) {
						
						  helper.setDetailedEligibleOfferResponse(offerCatalogDto, offerCatalog, eligibilityInfo, offerCatalogResultResponse);
						  
					  }
					  offerCatalogList.add(offerCatalogDto);
				  } 
				  
			 }
              
        
		} catch (MarketplaceException e) {
 			
 			e.printStackTrace();
 			
 			Responses.setResponseAfterException(offerCatalogResultResponse, 
 					new ExceptionInfo(this.getClass().getSimpleName(), 
 					OfferConstants.LIST_SPECIFIC_OFFER_FOR_MEMBER.get(), null, e, errorResult, null,
 					errorResult, exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(),
 							accountNumber, header.getUserName())));
 			
 			
 		} catch (Exception e) {
 			
 			e.printStackTrace();
 			
 			Responses.setResponseAfterException(offerCatalogResultResponse, 
 					new ExceptionInfo(this.getClass().getSimpleName(), 
 					OfferConstants.LIST_SPECIFIC_OFFER_FOR_MEMBER.get(), e, null, errorResult,
 					OfferExceptionCodes.GET_DETAIL_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
 					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), 
 							accountNumber, header.getUserName())));
 				
 		}
		
		Responses.setResponse(offerCatalogResultResponse, errorResult, successResult);
		offerCatalogResultResponse.setOfferCatalogs(offerCatalogList);
		return offerCatalogResultResponse;
	}
	
	/**
	 * Domain method to get all merchant codes for a specific offer type
	 * @param offerType
	 * @return list of merchant codes for iput offer type
	 */
	public List<String> getAllMerchantCodesForOffer(String offerType) {
		
		List<String> merchantIdList = null;
		
		try {
			
			OfferType offerTypeFromDb = repositoryHelper.getOfferTypeByDescription(offerType);
			List<OfferCatalog> offerCatalogs = repositoryHelper.getOffersByOfferTypeAndMerchant(offerTypeFromDb.getOfferTypeId(), OfferConstants.ACTIVE_STATUS.get());
					
			if (offerCatalogs != null && !offerCatalogs.isEmpty()) {
				merchantIdList = new ArrayList<>(offerCatalogs.size());
				for (final OfferCatalog offerCatalog : offerCatalogs) {
					String offerCatalogDto = modelMapper.map(offerCatalog.getMerchant().getId(), String.class);				
					merchantIdList.add(offerCatalogDto);
				}
			}
			
		} catch (Exception e) {
			LOG.error(new MarketplaceException(this.getClass().toString(),
					OfferConstants.GET_ALL_MERCHANTCODES_FOR_OFFER_METHOD.get(), e.getClass() + e.getMessage(),
					OfferExceptionCodes.GET_MERCHANT_IDS_OFFERS_DOMAIN_RUNTIME_EXCEPTION).printMessage());
		}
		return merchantIdList;
	}

	/**
	 * Domain method to set offer rating
	 * @param fetchedOffer
	 * @param savedOfferRating
	 * @param headers
	 * @throws MarketplaceException
	 */
	public void setOfferRating(OfferCatalog fetchedOffer, OfferRating savedOfferRating, Headers headers) throws MarketplaceException {
		
		try {
			
			if(!ObjectUtils.isEmpty(fetchedOffer)
			&& ObjectUtils.isEmpty(fetchedOffer.getOfferRating())
			&& !ObjectUtils.isEmpty(savedOfferRating)) {
				
				Gson gson = new Gson();
				OfferCatalog originalOffer = gson.fromJson(gson.toJson(fetchedOffer), OfferCatalog.class);
				fetchedOffer.setOfferRating(savedOfferRating);
				fetchedOffer.setUpdatedUser(headers.getUserName());
				fetchedOffer.setUpdatedDate(new Date());
				OfferCatalog savedOffer = repositoryHelper.saveOffer(fetchedOffer);
				auditService.updateDataAudit(OffersDBConstants.OFFER_CATALOG, savedOffer, OffersRequestMappingConstants.UPDATE_OFFER, originalOffer,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
		} catch (MongoException mongoException) {
		
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_OFFER_RATING_REPOSITORY_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
		
		} catch (Exception e) {
		
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_OFFER_RATING_REPOSITORY_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.UPDATE_OFFER_RATING_REPOSITORY_RUNTIME_EXCEPTION);
		
		}
		
	}

	/**
	 * Domain method to send birthday gift alerts
	 * @param header
	 * @return status after sending birthday gift alert
	 */
	public ResultResponse sendBirthdayGiftAlerts(Headers header) {
		
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		try {
			
			BirthdayInfo birthdayInfo = repositoryHelper.findBirthdayInfo();
			Integer daysToAdd = !ObjectUtils.isEmpty(birthdayInfo) ? birthdayInfo.getThresholdMinusValue() : 0;
			Integer plusDays = !ObjectUtils.isEmpty(birthdayInfo) ? birthdayInfo.getThresholdPlusValue() : 0;
			Date requiredDob = Utilities.getDateFromSpecificDate(daysToAdd, new Date());
			BirthdayDurationInfoDto birthdayDurationInfoDto = ProcessValues.getBirthdayDurationInfoForAlerts(birthdayInfo);
			List<BirthdayAccountsDto> accountInfoDtoList = repositoryHelper.getBirthdayAccountsInfo(daysToAdd);
			
			if (!CollectionUtils.isEmpty(accountInfoDtoList)) {
				
				List<String> accountNumberList = MapValues.getAllAccountNumbersFromBirthdayAccountList(accountInfoDtoList);
				List<String> membershipCodeList = MapValues.getAllMembershipCodeFromBirthdayAccountList(accountInfoDtoList);
				List<BirthdayGiftTracker> birthdayTrackerList = repositoryHelper.getBirthdayTrackerListForAccountList(accountNumberList, membershipCodeList);
				List<PurchaseHistory> purchaseRecords = repositoryHelper.findBirthdayOfferPurchaseInDuration(accountNumberList, membershipCodeList, birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate());
				
				for (BirthdayAccountsDto birthdayAccountsDto : accountInfoDtoList) {
					
					birthdayAccountsDto.setDob(ProcessValues.setUtcTimeandCurrentYear(birthdayAccountsDto.getDob()));
					
					if(DateUtils.isSameDay(requiredDob, birthdayAccountsDto.getDob())) {
						
						BirthdayGiftTracker birthdayGiftTracker = FilterValues.findAnyBirthdayTrackerInList(birthdayTrackerList, Predicates.sameAccountNumberAndMembershipCodeInTracker(birthdayAccountsDto.getAccountNumber(), birthdayAccountsDto.getMembershipCode()));
						birthdayDurationInfoDto.setStartDate(Utilities.setTimeInDate(new Date(), OfferConstants.FROM_DATE_TIME.get()));
						birthdayDurationInfoDto.setDob(birthdayAccountsDto.getDob());	
						birthdayDurationInfoDto.setLastYearDob(Utilities.getDateFromSpecificDateDurationYear(-1, birthdayDurationInfoDto.getDob()));
						birthdayDurationInfoDto.setEndDate(Utilities.getDateFromSpecificDate(daysToAdd+plusDays, new Date()));
						
						if(Checks.checkBirthdayEligibilityForAlerts(birthdayDurationInfoDto, birthdayGiftTracker, 
							resultResponse, purchaseRecords, 
							birthdayAccountsDto.getAccountNumber(), birthdayAccountsDto.getMembershipCode())) {
								
							eventHandler.publishSms(Requests.createSMSRequestForBirthdayAlert(birthdayAccountsDto, header));
							//eventHandler.publishEmail(Requests.createEmailRequestForBirthdayAlert(birthdayAccountsDto));
							eventHandler.publishPushNotification(Requests.createPushNotificationRequestForBirthdayAlert(birthdayAccountsDto, header.getExternalTransactionId()));
							
						}
						
						Responses.removeAllErrors(resultResponse);

					}
									
				}
				
				Responses.removeAllErrors(resultResponse);
			}

			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			String log = Logs.logForException(this.getClass().getName(), OfferConstants.SEND_BIRTHDAY_GIFTS_METHOD.get(), 
					e.getClassName(), e.getMessage(), OfferExceptionCodes.BIRTHDAY_NOTIFICATION_EXCEPTION);
			LOG.error(log);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			String log = Logs.logForException(this.getClass().getName(), OfferConstants.SEND_BIRTHDAY_GIFTS_METHOD.get(), 
					null, e.getMessage(), OfferExceptionCodes.BIRTHDAY_NOTIFICATION_EXCEPTION);
			LOG.error(log);
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED, OfferSuccessCodes.BATCH_PROCESS_FOR_BIRTHDAY_OFFERS_SUCCESSFULLY);
		return resultResponse;

	}
	
//	/**
//	 * Domain method to get all eligible birthday offers
//	 * @param accountNumber
//	 * @param headers
//	 * @return list of eligible birthday offers
//	 */
//	public OfferCatalogResultResponse getEligibleBirthdayOffers(String accountNumber, Headers headers) {
//		
//		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(headers.getExternalTransactionId());
//        List<OfferCatalogResultResponseDto> offerCatalogList = null;
//        OfferErrorCodes errorResult = OfferErrorCodes.LISTING_BIRTHDAYGIFT_OFFERS_ACCOUNT_FAILED;
//		OfferSuccessCodes successResult = OfferSuccessCodes.BIRTHDAYGIFT_OFFERS_LISTED_FOR_ACCOUNT_SUCCESSFULLY;
//		
//        try {
//        		headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
//        	    List<OfferCatalog> fetchedOffers = repositoryHelper.listBirthdayOffers(headers.getChannelId());
//			    FilterValues.filterOfferList(fetchedOffers, Predicates.activeMerchantAndStore());
//			    
//				if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(fetchedOffers), OfferErrorCodes.NO_BIRTHDAY_OFFERS_FILTERED, offerCatalogResultResponse)) {
//					
//					  EligibilityInfo eligibilityInfo = new EligibilityInfo();
//					  ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, headers, fetchedOffers, true);
//					  eligibilityInfo.setBirthdayInfoRequired(true);
//					  
//					  if(helper.checkMembershipEligbilityForOffers(eligibilityInfo, accountNumber, offerCatalogResultResponse)) {
//						
//						  BirthdayGiftTracker birthdayGiftTracker = repositoryHelper.getBirthdayGiftTrackerForCurrentAccount(accountNumber, eligibilityInfo.getMemberDetails().getMembershipCode());
//						  
//						  if(Checks.checkBirthdayEligibility(eligibilityInfo.getBirthdayDurationInfoDto(), birthdayGiftTracker, 
//								  offerCatalogResultResponse, eligibilityInfo.getPurchaseHistoryList(), 
//								  eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode())) {
//							  
//							  ProcessValues.filterEligibleOffersForMember(eligibilityInfo, fetchedOffers, offerCatalogResultResponse);
//							  
//							  eligibilityInfo.setOfferList(fetchedOffers);
//							  offerCatalogList = helper.getEligibleOfferList(eligibilityInfo, offerCatalogResultResponse, false);
//							  if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offerCatalogList), OfferErrorCodes.NO_BIRTHDAY_OFFERS_FILTERED, offerCatalogResultResponse)) {
//								  
//								  BirthdayGiftTrackerDomain giftTrackerDomain = DomainConfiguration.getBirthdayConfigurationDomain(birthdayGiftTracker, eligibilityInfo.getMemberDetails(), headers);
//								  birthdayGiftTrackerDomain.saveUpdateBirthdayTracker(giftTrackerDomain, birthdayGiftTracker, ProcessValues.getAction(giftTrackerDomain), headers);
//								  
//							  }
//							  
//						  }
//					       
//					  }
//					  
//				} 
//			
//
//		} catch (MarketplaceException e) {
//			
//			e.printStackTrace();
//			
//			Responses.setResponseAfterException(offerCatalogResultResponse, 
//					new ExceptionInfo(this.getClass().toString(), 
//					OfferConstants.GET_ELIGIBLE_BIRTHDAY_OFFER_LIST.get(), null, e, errorResult, null,
//					errorResult));
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//			Responses.setResponseAfterException(offerCatalogResultResponse, 
//					new ExceptionInfo(this.getClass().toString(), 
//					OfferConstants.GET_ELIGIBLE_BIRTHDAY_OFFER_LIST.get(), e, null, errorResult,
//					OfferExceptionCodes.GET_LIST_BIRTHDAY_OFFER_DOMAIN_RUNTIME_EXCEPTION, null));
//			
//		}
//		
//		Responses.setResponse(offerCatalogResultResponse, errorResult, successResult);
//		offerCatalogResultResponse.setOfferCatalogs(offerCatalogList);
//		return offerCatalogResultResponse;
//	}
	
	/**
	 * Domain method to get all eligible birthday offers
	 * @param accountNumber
	 * @param headers
	 * @return list of eligible birthday offers
	 */
	public OfferCatalogResultResponse getAllEligibleBirthdayOffers(String accountNumber, Headers headers) {
		
		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(headers.getExternalTransactionId());
        List<OfferCatalogResultResponseDto> offerCatalogList = null;
        OfferErrorCodes errorResult = OfferErrorCodes.LISTING_BIRTHDAYGIFT_OFFERS_ACCOUNT_FAILED;
		
        try {
        		headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
        	    List<OfferCatalog> fetchedOffers = repositoryHelper.listBirthdayOffers(headers.getChannelId());
			    FilterValues.filterOfferList(fetchedOffers, Predicates.activeMerchantAndStore());
			    
				if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(fetchedOffers), OfferErrorCodes.NO_BIRTHDAY_OFFERS_FILTERED, offerCatalogResultResponse)) {
					
					  EligibilityInfo eligibilityInfo = new EligibilityInfo();
					  ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, headers, fetchedOffers, true);
					  eligibilityInfo.setBirthdayInfoRequired(true);
					  
					  if(helper.checkMemberEligbilityForOffers(eligibilityInfo, accountNumber, offerCatalogResultResponse)) {
						
						  BirthdayGiftTracker birthdayGiftTracker = repositoryHelper.getBirthdayGiftTrackerForCurrentAccount(accountNumber, eligibilityInfo.getMemberDetails().getMembershipCode());
						  
						  if(Checks.checkBirthdayEligibility(eligibilityInfo.getBirthdayDurationInfoDto(), birthdayGiftTracker, 
								  offerCatalogResultResponse, eligibilityInfo.getPurchaseHistoryList(), 
								  eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode())) {
							  
							  ProcessValues.filterEligibleOffers(eligibilityInfo, fetchedOffers, offerCatalogResultResponse);
							  
							  offerCatalogList = helper.listOfEligibleOffers(eligibilityInfo, offerCatalogResultResponse, false, false);
							  if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offerCatalogList), OfferErrorCodes.NO_BIRTHDAY_OFFERS_FILTERED, offerCatalogResultResponse)) {
								  
								  BirthdayGiftTrackerDomain giftTrackerDomain = DomainConfiguration.getBirthdayConfigurationDomain(birthdayGiftTracker, eligibilityInfo.getMemberDetails(), headers);
								  birthdayGiftTrackerDomain.saveUpdateBirthdayTracker(giftTrackerDomain, birthdayGiftTracker, ProcessValues.getAction(giftTrackerDomain), headers);
								  
							  }
							  
						  }
					       
					  }
					  
				} 
			

		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_BIRTHDAY_OFFER_LIST.get(), null, e, errorResult, null,
					errorResult, exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(),
							accountNumber, headers.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_BIRTHDAY_OFFER_LIST.get(), e, null, errorResult,
					OfferExceptionCodes.GET_LIST_BIRTHDAY_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), 
							accountNumber, headers.getUserName())));
			
		}
		
		Responses.setResponse(offerCatalogResultResponse, errorResult, OfferSuccessCodes.BIRTHDAYGIFT_OFFERS_LISTED_FOR_ACCOUNT_SUCCESSFULLY);
		offerCatalogResultResponse.setOfferCatalogs(offerCatalogList);
		return offerCatalogResultResponse;
	}
	
//	/**
//	 * 
//	 * @param eligibleOffersParameters
//	 * @param headers
//	 * @return list of filtered eligible offers
//	 */
//	public OfferCatalogResultResponse listEligibleOffersForMembers(EligibleOffersFiltersRequest eligibleOffersRequest,
//			Headers headers) {
//		
//		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(headers.getExternalTransactionId());
//		boolean isMember = !StringUtils.isEmpty(eligibleOffersRequest.getAccountNumber());
//		
//		OfferErrorCodes errorResult = isMember  
//				? OfferErrorCodes.LISTING_OFFERS_MEMBER_FAILED
//				: OfferErrorCodes.LISTING_OFFERS_ELIGIBLE_FAILED;
//		
//		OfferSuccessCodes successResult = isMember 
//				? OfferSuccessCodes.OFFERS_LISTED_FOR_MEMBER_SUCCESSFULLY
//				: OfferSuccessCodes.ELIGIBLE_OFFERS_LISTED_SUCCESSFULLY;
//		
//		try {
//			
//			if(OfferValidator.validateEligibleOfferFilterRequest(eligibleOffersRequest, offerCatalogResultResponse)) {
//				
//				EligibleOfferHelperDto eligibilityInfo = new EligibleOfferHelperDto();
//				eligibilityInfo.setHeaders(headers);
//				eligibilityInfo.setMember(isMember);
//				
//				if(isMember) {
//					
//					eligibilityInfo.setAccountNumber(eligibleOffersRequest.getAccountNumber());
//					helper.setAccountDetailsForEligibleOffers(eligibilityInfo, offerCatalogResultResponse);
//					
//				}
//				
//				if(Checks.checkNoErrors(offerCatalogResultResponse)) {
//
//					eligibilityInfo.setOfferList(eligibleOffersContextAware.fetchEligibleOfferList());
//					
//					helper.getFilteredEligibleOfferResponse(eligibleOffersRequest, eligibilityInfo, 
//							offerCatalogResultResponse);	
//					
//				}
//				
//			}
//			
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//			Responses.setResponseAfterException(offerCatalogResultResponse, 
//					new ExceptionInfo(this.getClass().toString(), 
//					OfferConstants.LIST_ALL_ELIGIBLE_OFFER.get(), e, null, errorResult,
//					OfferExceptionCodes.GET_LIST_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null));
//			
//		}
//		
//		Responses.setResponse(offerCatalogResultResponse, errorResult, successResult);
//		return offerCatalogResultResponse;
//
//	}
	
	/**
	 * 
	 * @param eligibleOffersParameters
	 * @param headers
	 * @return list of filtered eligible offers
	 */
	public OfferCatalogResultResponse listEligibleOffers(EligibleOffersFiltersRequest eligibleOffersRequest,
			Headers headers) {
		
		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(headers.getExternalTransactionId());
		boolean isMember = !StringUtils.isEmpty(eligibleOffersRequest.getAccountNumber());
		
		OfferErrorCodes errorResult = isMember  
				? OfferErrorCodes.LISTING_OFFERS_MEMBER_FAILED
				: OfferErrorCodes.LISTING_OFFERS_ELIGIBLE_FAILED;
		
		OfferSuccessCodes successResult = isMember 
				? OfferSuccessCodes.OFFERS_LISTED_FOR_MEMBER_SUCCESSFULLY
				: OfferSuccessCodes.ELIGIBLE_OFFERS_LISTED_SUCCESSFULLY;
		
		try {
			
			if(OfferValidator.validateEligibleOfferFilterRequest(eligibleOffersRequest, offerCatalogResultResponse)) {
				
				EligibleOfferHelperDto eligibilityInfo = new EligibleOfferHelperDto();
				eligibilityInfo.setHeaders(headers);
				eligibilityInfo.setMember(isMember);
				
				if(isMember) {
					
					eligibilityInfo.setAccountNumber(eligibleOffersRequest.getAccountNumber());
					helper.setAccountDetailsForEligibleOffers(eligibilityInfo, offerCatalogResultResponse);
					
				}
				
				if(Checks.checkNoErrors(offerCatalogResultResponse)) {

					eligibilityInfo.setOfferList(eligibleOffersContextAware.fetchEligibleOfferList());
					
					helper.filterAndGetEligibleOfferResponse(eligibleOffersRequest, eligibilityInfo, 
							offerCatalogResultResponse, true);	
					
				}
				
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.LIST_ALL_ELIGIBLE_OFFER.get(), e, null, errorResult,
					OfferExceptionCodes.GET_LIST_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), 
							eligibleOffersRequest.getAccountNumber(), headers.getUserName())));
			
		}
		
		Responses.setResponse(offerCatalogResultResponse, errorResult, successResult);
		return offerCatalogResultResponse;

	}

	/**
	 * 
	 * @param headers
	 * @return response after setting all the eligible offers to the collection
	 */
	@Async(OffersConfigurationConstants.THREAD_POOL_TASK_EXECUTOR)
	public EligibleOffersResultResponse getAndSaveEligibleOffers(Headers headers) {
		
		EligibleOffersResultResponse resultResponse = new EligibleOffersResultResponse(headers.getExternalTransactionId());
				
		try {
			    repositoryHelper.removeAllEligibleOffers();
			    EligibleOfferHelperDto eligibilityInfo = new EligibleOfferHelperDto();
				eligibilityInfo.setHeaders(headers);
				helper.populateEligibleOffers(eligibilityInfo);
				resultResponse.setEligibleOfferList(eligibilityInfo.getOfferList());
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_AND_SAVE_ELIGIBLE_OFFERS.get(), e, null, OfferErrorCodes.ELIGIBLE_OFFERS_CONFIGURATION_FAILED,
					OfferExceptionCodes.CONFIGURE_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), 
							null, headers.getUserName())));
			
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.ELIGIBLE_OFFERS_CONFIGURATION_FAILED, OfferSuccessCodes.ELIGIBLE_OFFERS_CONFIGURATION_SUCCESSFULL);
		return resultResponse;
	}
	

	/**
	 * 
	 * @param headers
	 * @return list of eligible offers in singleton class
	 */
	public EligibleOffersResultResponse getEligibleOfferListInSingletonClass(Headers headers) {
		
		EligibleOffersResultResponse resultResponse = new EligibleOffersResultResponse(headers.getExternalTransactionId());
		resultResponse.setEligibleOfferList(eligibleOfferList.getOfferList());
		resultResponse.setTotalRecordCount(!CollectionUtils.isEmpty(resultResponse.getEligibleOfferList()) ? resultResponse.getEligibleOfferList().size(): 0);
		Responses.setResponse(resultResponse, OfferErrorCodes.SINGLETON_RECORDS_RETRIEVAL_FAILED, OfferSuccessCodes.SINGLETON_RECORDS_RETRIEVED_SUCCESSFULLY);
		return resultResponse;
	}
	
	/**
	 * 
	 * @param promotionalGiftRequest
	 * @param headers
	 * @return response after giving promotional gift
	 */
	public PromotionalGiftResultResponse givePromotionalGift(PromotionalGiftRequestDto promotionalGiftRequest, Headers headers) {
		
		PromotionalGiftResultResponse promotionalGiftResultResponse = new PromotionalGiftResultResponse(headers.getExternalTransactionId());
		OfferErrorCodes errorResult = OfferErrorCodes.PROMOTIONAL_GIFT_FAILED;
		OfferSuccessCodes successResult = OfferSuccessCodes.PROMOTIONAL_GIFT_SUCCESS;
		
		try {
			
			if(MarketplaceValidator.validateDto(promotionalGiftRequest, validator, promotionalGiftResultResponse)) {
				
				Gifts giftsForCatalog = giftRepository.findByPromotionalGiftIdAndIsActive(promotionalGiftRequest.getPromotionalGiftId(),true);
				if(!ObjectUtils.isEmpty(giftsForCatalog)) {
					
					OfferGiftValues offerGiftValues = !CollectionUtils.isEmpty(giftsForCatalog.getOfferValues())
							? giftsForCatalog.getOfferValues().get(0)
							: null;
					
						if(Checks.checkNoErrors(promotionalGiftResultResponse)) {
							
							PromotionalGiftHelper promotionalGiftHelper = new  PromotionalGiftHelper();
							promotionalGiftHelper.setSubscription(!ObjectUtils.isEmpty(giftsForCatalog.getGiftDetails())
									&& !StringUtils.isEmpty(giftsForCatalog.getGiftDetails().getSubscriptionCatalogId()));
							promotionalGiftHelper.setOffer(!ObjectUtils.isEmpty(offerGiftValues)
									&& !StringUtils.isEmpty(offerGiftValues.getOfferId()));
						
							if(promotionalGiftHelper.isSubscription()) {
								
								CompletableFuture<PurchaseResultResponse> subscriptionAsynch = CompletableFuture
										.supplyAsync(() -> helper.giftPromotionalSubscription(promotionalGiftHelper, promotionalGiftRequest, headers, 
												!ObjectUtils.isEmpty(giftsForCatalog.getGiftDetails())
												? giftsForCatalog.getGiftDetails().getSubscriptionCatalogId()
												: null));
								promotionalGiftHelper.setSubscriptionResponse(subscriptionAsynch.get());
								promotionalGiftHelper.setSubscriptionGifted(Checks.checkSuccessfulSubscription(promotionalGiftHelper.getSubscriptionResponse()));
							}
							
							if(promotionalGiftHelper.isOffer()) {
								CompletableFuture<PurchaseResultResponse> voucherAsynch = CompletableFuture
									.supplyAsync(() -> helper.giftPromotionalOffer(promotionalGiftRequest, headers, offerGiftValues));
								promotionalGiftHelper.setVoucherResponse(voucherAsynch.get());
								promotionalGiftHelper.setVoucherGifted(Checks.checkNoErrors(promotionalGiftHelper.getVoucherResponse()));
								
							} 
							
							ProcessValues.setPromotionalGiftResponse(promotionalGiftHelper, promotionalGiftResultResponse);
							LOG.info("{}", promotionalGiftHelper);
							
							errorResult = ProcessValues.getErrorCodeForPromotionalGift(promotionalGiftHelper);
							successResult = ProcessValues.getSuccessCodeForPromotionalGift(promotionalGiftHelper);
							ProcessValues.addErrorsForPromotionalGift(promotionalGiftHelper, promotionalGiftResultResponse);
						}
					}
				
			}
			
		}  catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(promotionalGiftResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GIVE_PROMOTIONAL_GIFT_DOMAIN_METHOD.get(), e, null, OfferErrorCodes.PROMOTIONAL_GIFT_FAILED,
					OfferExceptionCodes.GET_PROMOTIONAL_GIFT_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), null, headers.getUserName())));
			
		}
		
		Responses.setResponse(promotionalGiftResultResponse, errorResult, successResult);
		return promotionalGiftResultResponse;
	}	
	
	public ResultResponse processOfferCatalogContent(String fileSource, Headers headers, ResultResponse resultResponse) {
		
		try {	
			LOG.info("Inside processOfferCatalogContent");
			List<UploadedFileInfo> uploadedFileInfoList = uploadedFileInfoRepository.findByProcessingStatusAndFileSource("Uploaded", fileSource);
			marketplaceFileHelper.changeStateToProcessing(uploadedFileInfoList);
			if(!ObjectUtils.isEmpty(uploadedFileInfoList)) {
				for(UploadedFileInfo uploadedFileInfo: uploadedFileInfoList) {
					List<UploadedFileContent> uploadedFileContentList = uploadedFileContentRepository.findByUploadedFileInfoId(uploadedFileInfo.getId());
					if(!ObjectUtils.isEmpty(uploadedFileContentList)) {
						List<HbFileDto> hbFileDtoList =  iterateOfferCatalogContent(uploadedFileInfo, headers, fileSource, uploadedFileContentList, resultResponse);
						marketplaceFileHelper.updateStatusAndRecordCountInFileInfo(uploadedFileInfo, uploadedFileContentList.size());
						marketplaceFileHelper.createHandbackFileAndUpdateInfoForOfferCatalog(hbFileDtoList, uploadedFileInfo, fileSource);
						marketplaceFileHelper.emailReport(uploadedFileInfo);
						resultResponse.setResult(OfferSuccessCodes.FILE_PROCESSED_SUCCESSFULLY.getId(),
								OfferSuccessCodes.FILE_PROCESSED_SUCCESSFULLY.getMsg());
						
					} else {
						resultResponse.addErrorAPIResponse(Integer.parseInt(OfferErrorCodes.FILE_IS_EMPTY.getId()),
								OfferErrorCodes.FILE_IS_EMPTY.getMsg());
						resultResponse.setResult(OfferErrorCodes.FILE_IS_EMPTY.getId(),
								OfferErrorCodes.FILE_IS_EMPTY.getMsg());
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Inside processOfferCatalogContent catch block");
			resultResponse.addErrorAPIResponse(Integer.parseInt(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getId()),
					OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getId(), OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
		}
		return resultResponse;
	}

	
	public List<HbFileDto> iterateOfferCatalogContent(UploadedFileInfo uploadedFileInfo, Headers headers, String fileSource, List<UploadedFileContent> uploadedFileContentList,
			ResultResponse resultResponse) throws MarketplaceException {
		UploadedFileContent savedUploadedAccrualFileContent = new UploadedFileContent();
		List<HbFileDto> hbFileDtoList = new ArrayList<>();
		for(UploadedFileContent uploadedFileContent: uploadedFileContentList) {
			try {
				savedUploadedAccrualFileContent = populateFileContent(uploadedFileContent, resultResponse, headers);
			} catch(Exception ex) {
				LOG.info("Inside iterateOfferCatalogContent catch block");
				savedUploadedAccrualFileContent = marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getId()), 
						OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg()), headers);
			}
			hbFileDtoList.add(marketplaceFileHelper.populateHBFile(uploadedFileInfo, savedUploadedAccrualFileContent));
		}
		return hbFileDtoList;
	}
	
	public UploadedFileContent populateFileContent(UploadedFileContent uploadedFileContent, ResultResponse resultResponse, Headers headers){
		if(!ObjectUtils.isEmpty(uploadedFileContent)) {
			try {
				String contentString = uploadedFileContent.getContentString();
				if(!StringUtils.isEmpty(contentString)) {
					String[] catalogItems = contentString.split(",",-1);
					String offerId = catalogItems[0].trim();
					String extRefNum = catalogItems[1].trim();
					if(StringUtils.isEmpty(extRefNum)) {
						return marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.EXTERNAL_REFERENCE_NUMBER_EMPTY.getId()), 
								OfferErrorCodes.EXTERNAL_REFERENCE_NUMBER_EMPTY.getMsg()), headers);
					}
					else if(marketplaceFileHelper.checkForDuplicateExternalReferenceNumber(extRefNum, uploadedFileContent)) {
						return marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.EXTERNAL_REFERENCE_NUMBER_DUPLICATE.getId()), 
								OfferErrorCodes.EXTERNAL_REFERENCE_NUMBER_DUPLICATE.getMsg()), headers);
					}
					else if(StringUtils.isEmpty(offerId)) {
						return marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.OFFERID_CANNOT_BE_EMPTY.getId()), 
								OfferErrorCodes.OFFERID_CANNOT_BE_EMPTY.getMsg()), headers);
					} else {
						OfferCatalog offerCatalog = offerRepository.findByOfferId(offerId);
						if(!ObjectUtils.isEmpty(offerCatalog)) {
							updateOfferCatalog(offerCatalog, catalogItems, resultResponse, headers);
							return marketplaceFileHelper.updateSuccessFileContent(uploadedFileContent, headers);
						} else {
							return marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.OFFER_NOT_AVAIABLE_UPDATE.getId()), 
									OfferErrorCodes.OFFER_NOT_AVAIABLE_UPDATE.getMsg()), headers);
						}
					}
				} else { 
					return marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.NO_DATA_TO_UPDATE.getId()), 
							OfferErrorCodes.NO_DATA_TO_UPDATE.getMsg()), headers);
				}
			} catch (MarketplaceException e) {
				LOG.info("Inside populateFileContent MarketplaceException catch block");
				e.printStackTrace();
				return marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getId()), 
						OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg()), headers);
				
			} catch(Exception e) {
				LOG.info("Inside populateFileContent catch block");
				e.printStackTrace();
				return marketplaceFileHelper.updateFailedFileContent(uploadedFileContent, new ApiError(Integer.parseInt(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getId()), 
						OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg()), headers);
			}
		}
		return uploadedFileContent;
	}
	
	public void updateOfferCatalog(OfferCatalog offerCatalog, String[] splittedContent, ResultResponse resultResponse, Headers headers) throws ParseException, MarketplaceException {
		
		try {
			Gson gson = new Gson();
			OfferCatalog originalOffer = gson.fromJson(gson.toJson(offerCatalog), OfferCatalog.class);
			offersHelper.populateOfferDetails(offerCatalog, splittedContent);
			offersHelper.populateOtherOfferCatalogDetails(offerCatalog, splittedContent);
			offerCatalog.setUpdatedDate(new Date());
			offerCatalog.setUpdatedUser(headers.getUserName());			
			OfferCatalog savedOffer = repositoryHelper.saveOffer(offerCatalog);
			auditService.updateDataAudit(OffersDBConstants.OFFER_CATALOG, savedOffer, OffersRequestMappingConstants.UPDATE_OFFER, originalOffer,  headers.getExternalTransactionId(), headers.getUserName());
		} catch(Exception e) {
			LOG.info("Inside updateOfferCatalog catch block");
			resultResponse.addErrorAPIResponse(Integer.parseInt(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getId()),
					OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getId(), OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			throw new MarketplaceException(this.getClass().toString(), "updateOfferCatalog",
					e.getClass() + e.getMessage(), OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION);
		}
	}
	
	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_MERCHANT_OFFER_COUNT_UPDATE)
	public void updateOfferCountInMerchant(OfferCatalogDto offerCatalogRequest,OfferCountDto offerCountDto) {
		try {
			LOG.info("Entering updateOfferCountInMerchant Method");
		
			if(Utilities.presentInList(OffersListConstants.ACTIVATE_DEACTIVATE_ACTION_LIST, offerCatalogRequest.getAction())) {
				Merchant merchant = repositoryHelper.getMerchant(offerCountDto.getFetchedOffer().getMerchant().getMerchantCode());
				if(offerCatalogRequest.getAction().equalsIgnoreCase(OfferConstants.ACTIVATE_ACTION.get())) {
					LOG.info("activate call values : merchant:{},action:{},true",merchant,OfferConstants.ACTIVATE_ACTION.get());
					helper.updateOfferCount(merchant,OfferConstants.ACTIVATE_ACTION.get(),true);
				} else if(offerCatalogRequest.getAction().equalsIgnoreCase(OfferConstants.DEACTIVATE_ACTION.get())) {
					LOG.info("deactivate call values : merchant:{},action:{},false",merchant,OfferConstants.DEACTIVATE_ACTION.get());
					helper.updateOfferCount(merchant, OfferConstants.DEACTIVATE_ACTION.get(), false);
				}
			} else if(offerCatalogRequest.getAction().equalsIgnoreCase(OfferConstants.UPDATE_ACTION.get()) && offerCountDto.isMerchantCodeNotEqual()) {
				Merchant existingMerchant = repositoryHelper.getMerchant(offerCountDto.getExistingMerchantCode());
				Merchant updatedMerchant = repositoryHelper.getMerchant(offerCountDto.getUpdatedMerchantCode());
				LOG.info("update call values : merchant:{},action:{},true",existingMerchant,OfferConstants.UPDATE_ACTION.get());
				helper.updateOfferCount(existingMerchant,OfferConstants.UPDATE_ACTION.get(),false);
				LOG.info("update call values : merchant:{},action:{},true",updatedMerchant,OfferConstants.UPDATE_ACTION.get());
				helper.updateOfferCount(updatedMerchant,OfferConstants.UPDATE_ACTION.get(),true);
				
			}
			LOG.info("Exiting updateOfferCountInMerchant Method");
		} catch(Exception e) {
			e.printStackTrace();
			exceptionLogService.saveExceptionsToExceptionLogs(e,null,null,null);
		}
	}

	/***
	 * 
	 * @param headers
	 * @return list of all restaurants
	 */
	public RestaurantResultResponse getAllRestaurants(Headers headers) {
		
		RestaurantResultResponse restaurantResultResponse = new RestaurantResultResponse(headers.getExternalTransactionId());
        List<RestaurantDto> restaurantList = null;
        OfferErrorCodes errorResult = OfferErrorCodes.LISTING_RESTAURANTS_FAILED;
		
        try {
        		headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
        		restaurantList = fetchServiceValues.fetchRestaurantList(headers, restaurantResultResponse);
        		
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(restaurantResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					GET_ALL_RESTAURANTS, null, e, errorResult, null,
					errorResult, exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(),
							null, headers.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(restaurantResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					GET_ALL_RESTAURANTS, e, null, errorResult,
					OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), 
							null, headers.getUserName())));
			
		}
		
		Responses.setResponse(restaurantResultResponse, errorResult, OfferSuccessCodes.RESTAURANTS_FETCHED_SUCCESSFULLY);
		restaurantResultResponse.setRestaurants(restaurantList);
		return restaurantResultResponse;

	}
	
}
