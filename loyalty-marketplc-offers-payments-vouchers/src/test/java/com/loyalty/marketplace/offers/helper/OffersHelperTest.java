package com.loyalty.marketplace.offers.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.gifting.domain.GoldCertificateDomain;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.image.outbound.database.entity.MerchantOfferImage;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.domain.model.BirthdayGiftTrackerDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCounterDomain;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.helper.dto.AmountInfo;
import com.loyalty.marketplace.offers.helper.dto.AmountPoints;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.OfferReferences;
import com.loyalty.marketplace.offers.helper.dto.TimeLimits;
import com.loyalty.marketplace.offers.inbound.dto.DenominationLimitDto;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersRequest;
import com.loyalty.marketplace.offers.inbound.dto.LimitDto;
import com.loyalty.marketplace.offers.inbound.dto.ListValuesDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.member.management.outbound.dto.PaymentMethods;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityConfigDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityName;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.ActivityCode;
import com.loyalty.marketplace.offers.outbound.database.entity.ActivityCodeDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.AddTAndC;
import com.loyalty.marketplace.offers.outbound.database.entity.AdditionalDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.BrandDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.DynamicDenominationValue;
import com.loyalty.marketplace.offers.outbound.database.entity.GiftInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.ListValues;
import com.loyalty.marketplace.offers.outbound.database.entity.MarketplaceActivity;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDate;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetailMobile;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetailWeb;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLabel;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTitle;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTitleDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTypeDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferValues;
import com.loyalty.marketplace.offers.outbound.database.entity.ProvisioningAttributes;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.TAndC;
import com.loyalty.marketplace.offers.outbound.database.entity.Tags;
import com.loyalty.marketplace.offers.outbound.database.entity.TermsConditions;
import com.loyalty.marketplace.offers.outbound.database.entity.VoucherInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.WhatYouGet;
import com.loyalty.marketplace.offers.outbound.dto.LimitResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.StoreDto;
import com.loyalty.marketplace.offers.promocode.domain.PromoCodeDomain;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.StoreAddress;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.StoreDescription;
import com.loyalty.marketplace.offers.utils.Requests;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.DenominationDescription;
import com.loyalty.marketplace.outbound.database.entity.DenominationValue;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApply;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes=OffersHelper.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OffersHelperTest {
	
	
	@Mock
	ModelMapper modelMapper;
	
	@Mock
	RepositoryHelper repositoryHelper;
	
	@Mock
	FetchServiceValues fetchServiceValues;
	
	@Mock
	EventHandler eventHandler;
	
	@Mock
	PaymentService paymentService;
	
	@Mock
	AuditService auditService;
	
	@Mock
	PurchaseDomain purchaseDomain;
	
	@Mock
	OfferCounterDomain offerCounterDomain;
	
	@Mock
	GoldCertificateDomain goldCertificateDomain;
	
	@Mock 
	BirthdayGiftTrackerDomain birthdayGiftTrackerDomain;
	
	@Mock
	PromoCodeDomain promoCodeDomain;
	
	@Value(OffersConfigurationConstants.OFFER_COUNTER_FLAG_ENABLED)
	private boolean offerCounterFlagEnabled;
	
	@InjectMocks
	OffersHelper helper = new OffersHelper();
	
	private String program;
	private OfferCatalogDto offerCatalogDto;
	private List<String> storeCodes;
	private List<String> eligibleCustomerTypes;
	private List<String> exclusionTypes;
	private List<String> denominations;
	private List<String> rules;
	private List<SubOfferDto> subOffer;
	private SubOfferDto subOfferDto;
	private OfferCatalog offerCatalog;
	private OfferType offerType;
	private OfferTypeDescription offerDescription;
	private PaymentMethod paymentMethod;
	private List<PaymentMethod> paymentMethods;
	private SimpleDateFormat simpleDateFormat;
	private Date dtCreated;
	private Date dtUpdated;
	private OfferDetails offer;
	private OfferLabel offerLabel;
	private OfferTitle offerTitle;
	private OfferTitleDescription offerTitleDescription;
	private OfferDetailMobile offerMobile;
	private OfferDetailWeb offerWeb;
	private BrandDescription brandDescription;
	private TermsConditions termsAndConditions;
	private TAndC termsAndCondition;
	private AddTAndC addTermsAndConditions;
	private Tags tags;
	private WhatYouGet whatYouGet;
	private OfferDate offerDates;
	private Date offerStartDate;
	private Date offerEndDate;
	private OfferValues offerValues;
	private Merchant merchant;
	private MerchantName merchantName;
	private Barcode barcodeType;
	private List<Store> offerStores;
	private Store store;
	private StoreAddress address;
	private StoreDescription description;
	private List<ContactPerson> contactPersons;
	private ContactPerson contactPerson;
	private Category category;
	private CategoryName categoryName;
	private Category subCategory;
	private DynamicDenominationValue dynamicDenominationValue;
	private ListValues listValues;
	private List<Denomination> denomination;
	private Denomination denominatn;
	private ProvisioningAttributes provisioningAttributes;
	private StoreDto storeDto;
	private List<String> storeCoordinates;
	private List<StoreDto> storeDtoList;
	private List<String> availableInPortals;
	private ListValuesDto listValuesDto;
	private ListValuesDto segmentListValuesDto;
	private OfferLimit offerLimit;
		
	private EligibleOffersRequest eligibleOffersRequest;
	private ResultResponse resultResponse;
	private String userName;
	private String externalTransactionId;
	private String token;
	private String accountNumber;
	private String channelId;
	private String purchaseItem;
	private Headers header;
	private OfferReferences offerReference;
	private List<OfferCatalog> offerList;
	private GetMemberResponse memberDetails;
	private PurchasePaymentMethod purchasePaymentMethod;
	private EligibilityInfo eligibilityInfo;
	private OfferCatalogResultResponseDto offerCatalogResultResponseDto; 
 	private List<OfferCatalogResultResponseDto> offerResponseList;
 	private List<Category> categoryList;
 	private List<ParentChlidCustomer> customerTypeList;
 	List<ProgramActivityWithIdDto> programActivityList;
 	private List<MarketplaceImage> imageList;
 	private List<PurchasePaymentMethod> purchasePaymentmethodList;
 	private List<ConversionRate> conversionRateList;
 	private PurchaseRequestDto purchaseRequest;
 	private PromoCodeApply promocodeApply;
 	private List<OfferCounter> offerCounterList;
 	private OfferCounter offerCounter;
 	private PurchaseHistory purchaseHistory;
 	private PurchaseHistory savedPurchaseHistory;
 	private PurchaseResultResponse purchaseResultResponse;
 	private BirthdayInfo birthdayInfo;
 	private TimeLimits timeLimit;
 	private BirthdayGiftTracker birthdayGiftTracker;
 	private PaymentResponse paymentResponse;
 	
	@Before
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
        program = "Smiles";
		userName="userName";
		externalTransactionId="externalTransactionId";
		token = "token";
		accountNumber="accountNumber";
		channelId="SWEB";
		purchaseItem="purchaseItem";
		
		header = new Headers(program, "", externalTransactionId, userName, "", "",
				channelId, "", "", token, "");
		
		offerCatalogDto = new OfferCatalogDto();
		offerCatalogDto.setOfferCode("offerCode");
		offerCatalogDto.setOfferTypeId(OffersConfigurationConstants.discountOfferType);
		offerCatalogDto.setOfferLabelEn("offerLabelEn");
		offerCatalogDto.setOfferLabelAr("offerLabelAr");
		offerCatalogDto.setOfferTitleEn("offerTitleEn");
		offerCatalogDto.setOfferTitleAr("offerTitleAr");
		offerCatalogDto.setOfferTitleDescriptionEn("offerTitleDescriptionEn");
		offerCatalogDto.setOfferTitleDescriptionAr("offerTitleDescriptionAr");
		offerCatalogDto.setOfferDetailMobileEn("offerDetailMobileEn");
		offerCatalogDto.setOfferDetailMobileAr("offerDetailMobileAr");
		offerCatalogDto.setOfferDetailWebEn("offerDetailWebEn");
		offerCatalogDto.setOfferDetailWebAr("offerDetailWebAr");
		offerCatalogDto.setBrandDescriptionEn("brandDescriptionEn");
		offerCatalogDto.setBrandDescriptionAr("brandDescriptionAr");
		offerCatalogDto.setTermsAndConditionsEn("termsAndConditionsEn");
		offerCatalogDto.setTermsAndConditionsAr("termsAndConditionsAr");
		offerCatalogDto.setAdditionalTermsAndConditionsEn("additionalTermsAndConditionsEn");
		offerCatalogDto.setAdditionalTermsAndConditionsAr("additionalTermsAndConditionsAr");
		offerCatalogDto.setTagsEn("tagsEn");
		offerCatalogDto.setTagsAr("tagsAr");
		offerCatalogDto.setWhatYouGetEn("whatYouGetEn");
		offerCatalogDto.setWhatYouGetAr("whatYouGetAr");
		offerCatalogDto.setOfferStartDate("2030-12-23 17:33:55");
		offerCatalogDto.setOfferEndDate("2034-12-29 17:33:55");
		offerCatalogDto.setTrendingRank(1);
		offerCatalogDto.setStatus(OfferConstants.INACTIVE_STATUS.get());
		availableInPortals = new ArrayList<>();
		availableInPortals.add("SWEB");
		offerCatalogDto.setAvailableInPortals(availableInPortals);
		offerCatalogDto.setNewOffer(OfferConstants.FLAG_SET.get());
		offerCatalogDto.setIsGift(OfferConstants.FLAG_SET.get());
		offerCatalogDto.setIsDod(OfferConstants.FLAG_SET.get());
		offerCatalogDto.setIsFeatured(OfferConstants.FLAG_SET.get());
		offerCatalogDto.setPointsValue(240);
		offerCatalogDto.setCost(78.0);
		offerCatalogDto.setDiscountPerc(13);
		offerCatalogDto.setEstSavings(14.0);
		List<LimitResponseDto> limitList = new ArrayList<>(1);
		LimitResponseDto limitDto = new LimitResponseDto();
		List<DenominationLimitDto> denominationLimitList =  new ArrayList<>(1);
		DenominationLimitDto denominationLimit = new DenominationLimitDto();
		denominationLimit.setDenomination(1);
		denominationLimit.setDailyLimit(1);
		denominationLimit.setWeeklyLimit(1);
		denominationLimit.setMonthlyLimit(1);
		denominationLimit.setAnnualLimit(1);
		denominationLimit.setTotalLimit(1);
		denominationLimitList.add(denominationLimit);
		limitDto.setCouponQuantity(1);
		limitDto.setDailyLimit(1);
		limitDto.setWeeklyLimit(1);
		limitDto.setMonthlyLimit(1);
		limitDto.setAnnualLimit(1);
		limitDto.setDownloadLimit(1);
		limitDto.setDenominationLimit(denominationLimitList);
		limitDto.setAccountDailyLimit(1);
		limitDto.setAccountWeeklyLimit(1);
		limitDto.setAccountMonthlyLimit(1);
		limitDto.setAccountAnnualLimit(1);
		limitDto.setAccountTotalLimit(1);
		limitDto.setAccountDenominationLimit(denominationLimitList);
		limitDto.setMemberDailyLimit(1);
		limitDto.setMemberWeeklyLimit(1);
		limitDto.setMemberMonthlyLimit(1);
		limitDto.setMemberAnnualLimit(1);
		limitDto.setMemberTotalLimit(1);
		limitDto.setMemberDenominationLimit(denominationLimitList);
		limitList.add(limitDto);
		LimitDto offerLimitDto = new LimitDto();
		offerLimitDto.setCustomerSegment("Special");
		offerLimitDto.setDailyLimit(2);
		limitList.add(offerLimitDto);
		offerCatalogDto.setLimit(limitList);
		offerCatalogDto.setSharing(OfferConstants.FLAG_SET.get());
		offerCatalogDto.setSharingBonus(13);
		offerCatalogDto.setVatPercentage(12.5);
		offerCatalogDto.setVoucherExpiryDate("2070-12-23 17:33:55");
		offerCatalogDto.setMerchantCode("Dev2");
		storeCodes = new ArrayList<>();
		storeCodes.add("storeCode");
		offerCatalogDto.setStoreCodes(storeCodes);
		offerCatalogDto.setCategoryId("1");
		offerCatalogDto.setSubCategoryId("4");
		offerCatalogDto.setDynamicDenomination(OfferConstants.FLAG_NOT_SET.get());
		offerCatalogDto.setGroupedFlag(OfferConstants.FLAG_SET.get());
		offerCatalogDto.setMinDenomination(1);
		offerCatalogDto.setMaxDenomination(45);
		offerCatalogDto.setIncrementalValue(2);
		eligibleCustomerTypes = new ArrayList<>();
		eligibleCustomerTypes.add("Prepaid");
		eligibleCustomerTypes.add("Gold");
		exclusionTypes = new ArrayList<>();
		exclusionTypes.add("Hybrid");
		listValuesDto = new ListValuesDto();
		listValuesDto.setEligibleTypes(eligibleCustomerTypes);
		listValuesDto.setExclusionTypes(exclusionTypes);
		offerCatalogDto.setCustomerTypes(listValuesDto);
		segmentListValuesDto = new ListValuesDto();
		List<String> eligibleSegmentValues = new ArrayList<>(1);
		eligibleSegmentValues.add("Special");
		List<String> exclusionSegmentValues = new ArrayList<>(1);
		exclusionSegmentValues.add("Standard");
		segmentListValuesDto.setEligibleTypes(eligibleSegmentValues);
		segmentListValuesDto.setExclusionTypes(exclusionSegmentValues);
		offerCatalogDto.setCustomerSegments(segmentListValuesDto);
		denominations = new ArrayList<>();
		denominations.add("1");
		offerCatalogDto.setDenominations(denominations);
		offerCatalogDto.setVoucherAction("2");
		rules = new ArrayList<>();
		rules.add("CinemaOffer");
		offerCatalogDto.setRules(rules);
		offerCatalogDto.setProvisioningChannel("provisioningChannel");
		offerCatalogDto.setRatePlanCode("ratePlanCode");
		offerCatalogDto.setRtfProductCode("rtfProductCode");
		offerCatalogDto.setRtfProductType("rtfProductType");
		offerCatalogDto.setVasCode("vasCode");
		offerCatalogDto.setVasActionId("vasActionId");
		offerCatalogDto.setPromotionalPeriod(2);
		offerCatalogDto.setPackName("packName");
		offerCatalogDto.setFeature("feature");
		offerCatalogDto.setActivityId("actionId");
		offerCatalogDto.setServiceId("serviceId");
		offerCatalogDto.setEarnMultiplier(1.0);
		offerCatalogDto.setAccrualId("accId");
		offerCatalogDto.setAccrualActivityCode("accrualActivityCode");
		offerCatalogDto.setAccrualCodeDescriptionEn("accrualActivityCodeDescriptionEn");
		offerCatalogDto.setAccrualCodeDescriptionAr("accrualActivityCodeDescriptionAr");
		offerCatalogDto.setRedemptionId("redId");
		offerCatalogDto.setRedemptionActivityCode("redemptionActivityCode");
		offerCatalogDto.setRedemptionCodeDescriptionEn("redemptionActivityCodeDescriptionEn");
		offerCatalogDto.setRedemptionCodeDescriptionAr("redemptionActivityCodeDescriptionAr");
		offerCatalogDto.setAction(OfferConstants.INSERT_ACTION.get());
		
		subOfferDto = new SubOfferDto();
		subOfferDto.setSubOfferId("subOfferId");
		subOfferDto.setSubOfferTitleEn("subOfferTitleEn");
		subOfferDto.setSubOfferTitleAr("subOfferTitleAr");
		subOfferDto.setSubOfferDescEn("subOfferDescEn");
		subOfferDto.setSubOfferDescAr("subOfferDescAr");
		subOfferDto.setOldCost(1.0);
		subOfferDto.setOldPointValue(0);
		subOfferDto.setNewCost(2.0);
		subOfferDto.setNewPointValue(0);
		subOffer = new ArrayList<>();		
		subOffer.add(subOfferDto);
		offerCatalogDto.setSubOffer(subOffer);
		
		offerCatalog = new OfferCatalog();
		offerCatalog.setOfferId("OF_A155_4");
		offerCatalog.setProgramCode("Smiles");
		offerCatalog.setOfferCode("A155");		
		offerType = new OfferType();
		offerType.setOfferTypeId("2");
		offerDescription = new OfferTypeDescription();
		offerDescription.setTypeDescriptionEn("typeDescriptionEn");
		offerDescription.setTypeDescriptionAr("typeDescriptionAr");
		offerType.setOfferDescription(offerDescription);
		paymentMethod = new PaymentMethod();
		paymentMethod.setPaymentMethodId("1");
		paymentMethod.setDescription("methodDescription1");
		paymentMethod.setUsrCreated("");
		paymentMethod.setUsrUpdated("");
		String pattern = "yyyy-MM-dd HH:MM:ss";
		simpleDateFormat = new SimpleDateFormat(pattern);
		dtCreated = simpleDateFormat.parse("2018-12-23 17:33:55");
		dtUpdated = simpleDateFormat.parse("2018-12-23 17:38:55");
		paymentMethod.setDtCreated(dtCreated);
		paymentMethod.setDtUpdated(dtUpdated);
		paymentMethods = new ArrayList<>();
		paymentMethods.add(paymentMethod);
		offerType.setUsrCreated("John");
		offerType.setUsrUpdated("John");
		offerType.setDtCreated(dtCreated);
		offerType.setDtUpdated(dtUpdated);
		offerCatalog.setOfferType(offerType);
		offer = new OfferDetails();
		offerLabel = new OfferLabel();
		offerLabel.setOfferLabelEn("offerLabelEn");
		offerLabel.setOfferLabelAr("offerLabelAr");
		offerTitle = new OfferTitle();
		offerTitle.setOfferTitleEn("offerTitleEn");
		offerTitle.setOfferTitleAr("offerTitleAr");
		offerTitleDescription = new OfferTitleDescription();
		offerTitleDescription.setOfferTitleDescriptionEn("offerTitleDescriptionEn");
		offerTitleDescription.setOfferTitleDescriptionAr("offerTitleDescriptionAr");
		offerMobile = new OfferDetailMobile();
		offerMobile.setOfferDetailMobileEn("offerDetailMobileEn");
		offerMobile.setOfferDetailMobileAr("offerDetailMobileAr");
		offerWeb = new OfferDetailWeb();
		offerWeb.setOfferDetailWebEn("offerDetailWebEn");
		offerWeb.setOfferDetailWebAr("offerDetailWebAr");
		offer.setOfferLabel(offerLabel);
		offer.setOfferTitle(offerTitle);
		offer.setOfferTitleDescription(offerTitleDescription);
		offer.setOfferMobile(offerMobile);
		offer.setOfferWeb(offerWeb);
		offerCatalog.setOffer(offer);
		brandDescription = new BrandDescription();
		brandDescription.setBrandDescriptionEn("brandDescriptionEn");
		brandDescription.setBrandDescriptionAr("brandDescriptionAr");
		offerCatalog.setBrandDescription(brandDescription);
		termsAndConditions = new TermsConditions();
		termsAndCondition = new TAndC();
		termsAndCondition.setTermsAndConditionsEn("termsAndConditionsEn");
		termsAndCondition.setTermsAndConditionsAr("termsAndConditionsAr");
		addTermsAndConditions = new AddTAndC();
		addTermsAndConditions.setAdditionalTermsAndConditionsEn("additionalTermsAndConditionsEn");
		addTermsAndConditions.setAdditionalTermsAndConditionsAr("additionalTermsAndConditionsAr");
		termsAndConditions.setTermsAndConditions(termsAndCondition);
		termsAndConditions.setAddTermsAndConditions(addTermsAndConditions);
		offerCatalog.setTermsAndConditions(termsAndConditions);
		tags = new Tags();
		tags.setTagsEn("tagsEn");
		tags.setTagsAr("tagsAr");
		offerCatalog.setTags(tags);
		whatYouGet = new WhatYouGet();
		whatYouGet.setWhatYouGetEn("whatYouGetEn");
		whatYouGet.setWhatYouGetAr("whatYouGetAr");
		offerCatalog.setWhatYouGet(whatYouGet);
		offerDates = new OfferDate();
		offerStartDate = simpleDateFormat.parse("2040-12-23 17:33:55");
		offerEndDate = simpleDateFormat.parse("2050-12-25 17:38:55");
		offerDates.setOfferStartDate(offerStartDate);
		offerDates.setOfferEndDate(offerEndDate);
		offerCatalog.setOfferDates(offerDates);
		offerCatalog.setTrendingRank(1);
		offerCatalog.setStatus(OfferConstants.OFFER_DEFAULT_STATUS.get());
		offerCatalog.setAvailableInPortals(availableInPortals);
		offerCatalog.setNewOffer("newOffer");
		offerCatalog.setGiftInfo(new GiftInfo("", new ArrayList<>(), null));
		offerCatalog.setIsDod("IsDod");
		offerCatalog.setIsFeatured("isFeatured");
		offerValues = new OfferValues();
		offerValues.setPointsValue(2);
		offerValues.setCost(240.0);
		offerCatalog.setOfferValues(offerValues);
		offerCatalog.setDiscountPerc(10);
		offerCatalog.setEstSavings(3.8);
		offerCatalog.setSharing(OfferConstants.FLAG_SET.get());
		offerCatalog.setSharingBonus(12);
		offerCatalog.setVatPercentage(10.2);
		offerLimit = new OfferLimit();
		offerLimit.setCustomerSegment("Special");
		offerLimit.setDownloadLimit(10);
		offerLimit.setCouponQuantity(50);
		offerLimit.setDailyLimit(7);
		offerLimit.setWeeklyLimit(15);
		offerLimit.setMonthlyLimit(20);
		offerLimit.setAccountDailyLimit(10);
		offerLimit.setAccountWeeklyLimit(15);
		offerLimit.setAccountMonthlyLimit(30);
		
		List<OfferLimit> offerLimitList = new ArrayList<>(1);
		offerLimitList.add(offerLimit);
		offerCatalog.setLimit(offerLimitList);
		
		offerCatalog.setPartnerCode("Dev23");
		merchant = new Merchant();
		merchant.setId("MerchantId");
		merchant.setMerchantCode("merchantCode");
		merchantName = new MerchantName();
		merchantName.setMerchantNameEn("merchantNameEn");
		merchantName.setMerchantNameAr("merchantNameAr");
		merchant.setMerchantName(merchantName);
		merchant.setPartner("partner");
		barcodeType = new Barcode();
		barcodeType.setId("id");
		barcodeType.setName("name");
		barcodeType.setDescription("description");
		barcodeType.setUsrCreated("usrCreated");
		barcodeType.setUsrUpdated("usrUpdated");
		barcodeType.setDtCreated(dtCreated);
		barcodeType.setDtUpdated(dtUpdated);
		merchant.setBarcodeType(barcodeType);
		merchant.setStatus(OfferConstants.ACTIVE_STATUS.get());
		offerCatalog.setMerchant(merchant);
		store = new Store();
		store.setId("1");
		store.setProgramCode("programCode");
		store.setStoreCode("storeCode");
		description = new StoreDescription();
		description.setStoreDescriptionEn("storeDescriptionEn");
		description.setStoreDescriptionAr("storeDescriptionAr");
		store.setDescription(description);
		address = new StoreAddress();
		address.setAddressEn("addressEn");
		address.setAddressAr("addressAr");
		store.setAddress(address);
		store.setLatitude("latitude");
		store.setLongitude("longitude");
		store.setMerchantCode("merchantCode");
		contactPerson = new ContactPerson();
		contactPerson.setMobileNumber("mobileNumber");
		contactPerson.setEmailId("emailId");
		contactPerson.setFirstName("firstName");
		contactPerson.setLastName("lastName");
		contactPersons = new ArrayList<>();
		contactPersons.add(contactPerson);
		store.setContactPersons(contactPersons);
		store.setStatus(OfferConstants.ACTIVE_STATUS.get());
		store.setUsrCreated("usrCreated");
		store.setUsrUpdated("usrUpdated");
		store.setDtCreated(dtCreated);
		store.setDtUpdated(dtUpdated);
		offerStores = new ArrayList<>();
		offerStores.add(store);
		offerCatalog.setOfferStores(offerStores);
		category = new Category();
		category.setCategoryId("categoryId");
		categoryName = new CategoryName();
		categoryName.setCategoryNameEn("categoryNameEn");
		categoryName.setCategoryNameAr("categoryNameAr");
		category.setCategoryName(categoryName);
		category.setUsrCreated("usrCreated");
		category.setUsrUpdated("usrUpdated");
		category.setDtCreated(dtCreated);
		category.setDtUpdated(dtUpdated);
		offerCatalog.setCategory(category);
		subCategory = new Category();
		subCategory.setCategoryId("SubCategoryId");
		offerCatalog.setSubCategory(subCategory);
		offerCatalog.setDynamicDenomination("dynamicDenomination");
		offerCatalog.setGroupedFlag("groupedFlag");
		dynamicDenominationValue = new DynamicDenominationValue();
		dynamicDenominationValue.setMinDenomination(1);
		dynamicDenominationValue.setMaxDenomination(2);
		offerCatalog.setDynamicDenominationValue(dynamicDenominationValue);
		offerCatalog.setIncrementalValue(5);
		listValues = new ListValues();
		listValues.setEligibleTypes(eligibleCustomerTypes);
		listValues.setExclusionTypes(exclusionTypes);
		offerCatalog.setCustomerTypes(listValues);
		denomination = new ArrayList<>();
		denominatn = new Denomination();
		denominatn.setDenominationId("1");
		DenominationDescription denominationDescription = new DenominationDescription();
		denominationDescription.setDenominationDescriptionEn("En");
		denominationDescription.setDenominationDescriptionEn("Ar");
		denominatn.setDenominationDescription(denominationDescription);
		DenominationValue denominationValue = new DenominationValue();
		denominationValue.setDirhamValue(1);
		denominationValue.setPointValue(240);
		denominatn.setDenominationValue(denominationValue);
		denomination.add(denominatn);
		offerCatalog.setDenominations(denomination);
		offerCatalog.setRules(rules);
		VoucherInfo voucherInfo = new VoucherInfo();
		voucherInfo.setVoucherAction("2");
		voucherInfo.setVoucherAmount(0.0);
		voucherInfo.setVoucherExpiryPeriod(0);
		voucherInfo.setVoucherExpiryDate(new Date());
		offerCatalog.setVoucherInfo(voucherInfo);
		offerCatalog.setProvisioningChannel("provisioningChannel");
		provisioningAttributes = new ProvisioningAttributes();
		provisioningAttributes.setRatePlanCode("ratePlanCode");
		provisioningAttributes.setRtfProductCode("rtfProductCode");
		provisioningAttributes.setRtfProductType("rtfProductType");
		provisioningAttributes.setVasCode("vasCode");
		provisioningAttributes.setVasActionId("vasActionId");
		offerCatalog.setProvisioningAttributes(provisioningAttributes);
		offerCatalog.setEarnMultiplier(2.0);
		ActivityCode activityCode = new ActivityCode();
		MarketplaceActivity redemptionCode = new MarketplaceActivity();
		redemptionCode.setActivityId("redemptionActivityId");
		redemptionCode.setActivityCode("redemptionActivityCode");
		ActivityCodeDescription activityCodeDescription = new ActivityCodeDescription();
		activityCodeDescription.setActivityCodeDescriptionEn("english");
		activityCodeDescription.setActivityCodeDescriptionAr("arabic");
		redemptionCode.setActivityCodeDescription(activityCodeDescription);
		MarketplaceActivity accrualCode = new MarketplaceActivity();
		accrualCode.setActivityId("accrualActivityId");
		accrualCode.setActivityCode("accrualActivityCode");
		accrualCode.setActivityCodeDescription(activityCodeDescription);
		activityCode.setRedemptionActivityCode(redemptionCode);
		activityCode.setAccrualActivityCode(accrualCode);
		offerCatalog.setActivityCode(activityCode);
		storeDto = new StoreDto();
		storeDto.setStoreCode("storeCode");
		storeCoordinates = new ArrayList<>();
		storeCoordinates.add("4566");
		storeCoordinates.add("6567");
		storeDto.setStoreCoordinates(storeCoordinates);
		storeDto.setStoreDescriptionEn("storeDescriptionEn");
		storeDto.setStoreDescriptionAr("storeDescriptionAr");
		storeDtoList = new ArrayList<>();
		storeDtoList.add(storeDto);
		
		eligibleOffersRequest = new EligibleOffersRequest();
		//eligibleOffersRequest.setFilterFlag("filterFlag");
		eligibleOffersRequest.setAccountNumber("accountNumber");
//		eligibleOffersRequest.setCategoryId("categoryId");
//		eligibleOffersRequest.setSubCategoryId("subCategoryId");
//		eligibleOffersRequest.setKeywords("keywords");
//		eligibleOffersRequest.setMerchantCode("merchantCode");
		
		offerReference = new OfferReferences();
		offerReference.setCategory(category);
		offerReference.setSubCategory(subCategory);
		offerReference.setOfferType(offerType);
		offerReference.setMerchant(merchant);
		offerReference.setStore(offerStores);
		offerReference.setDenominations(denomination);
		offerReference.setHeader(header);
		offerReference.setSize(0);
		
		offerList = new ArrayList<>(1);
		offerList.add(offerCatalog);
		
		List<PaymentMethods> paymentMethodList = new ArrayList<>(1);
		PaymentMethods paymentMethodM = new PaymentMethods();
		paymentMethodM.setPaymentMethodId(paymentMethods.get(0).getPaymentMethodId());
		paymentMethodM.setDescription(paymentMethods.get(0).getDescription());
		paymentMethodList.add(paymentMethodM);
		
		memberDetails = new GetMemberResponse();
		memberDetails.setAccountNumber(accountNumber);
		memberDetails.setMembershipCode("membershipCode");
		memberDetails.setEligiblePaymentMethod(paymentMethodList);
		memberDetails.setCustomerType(eligibleCustomerTypes);
		memberDetails.setSubscribed(true);
		memberDetails.setDob(new Date());
		memberDetails.setCustomerSegment(eligibleSegmentValues);
		memberDetails.setReferralAccountNumber(accountNumber);
		memberDetails.setReferralBonusCode("referralBonusCode");
		
		DenominationCount denominationCount = new DenominationCount();
		denominationCount.setDenomination(1);
		denominationCount.setDailyCount(0);
		denominationCount.setWeeklyCount(0);
		denominationCount.setMonthlyCount(0);
		denominationCount.setAnnualCount(0);
		denominationCount.setTotalCount(0);
		denominationCount.setLastPurchased(new Date());
		List<DenominationCount> denominationCountList = new ArrayList<>(1);
		denominationCountList.add(denominationCount);
		
		AccountOfferCount accountOfferCount = new AccountOfferCount();
		accountOfferCount.setDailyCount(0);
		accountOfferCount.setWeeklyCount(0);
		accountOfferCount.setMonthlyCount(0);
		accountOfferCount.setAnnualCount(0);
		accountOfferCount.setTotalCount(0);
		accountOfferCount.setAccountNumber(memberDetails.getAccountNumber());
		accountOfferCount.setMembershipCode(memberDetails.getMembershipCode());
		accountOfferCount.setDenominationCount(denominationCountList);
		List<AccountOfferCount> accountOfferCounterList = new ArrayList<>(1);
		accountOfferCounterList.add(accountOfferCount);
		
		MemberOfferCount memberOfferCount = new MemberOfferCount();
		memberOfferCount.setDailyCount(0);
		memberOfferCount.setWeeklyCount(0);
		memberOfferCount.setMonthlyCount(0);
		memberOfferCount.setAnnualCount(0);
		memberOfferCount.setTotalCount(0);
		memberOfferCount.setMembershipCode(memberDetails.getMembershipCode());
		memberOfferCount.setDenominationCount(denominationCountList);
		List<MemberOfferCount> memberOfferCounterList = new ArrayList<>(1);
		memberOfferCounterList.add(memberOfferCount);
		
		offerCounter = new OfferCounter();
		offerCounter.setOfferId(offerCatalog.getOfferId());
		offerCounter.setDailyCount(0);
		offerCounter.setWeeklyCount(0);
		offerCounter.setMonthlyCount(0);
		offerCounter.setAnnualCount(0);
		offerCounter.setTotalCount(0);
		offerCounter.setLastPurchased(new Date());
		offerCounter.setDenominationCount(denominationCountList);
		offerCounter.setAccountOfferCount(accountOfferCounterList);
		offerCounter.setMemberOfferCount(memberOfferCounterList);
		
		offerCounterList = new ArrayList<>(1);
		offerCounterList.add(offerCounter);
		
		eligibilityInfo = new EligibilityInfo();
		eligibilityInfo.setMemberDetails(memberDetails);
		eligibilityInfo.setOfferCounterList(offerCounterList);
		eligibilityInfo.setOfferList(offerList);
		eligibilityInfo.setHeaders(header);
		eligibilityInfo.setOffer(offerCatalog);
		AdditionalDetails additionalDetails = new AdditionalDetails();
		eligibilityInfo.setAdditionalDetails(additionalDetails);
		AmountInfo amountInfo = new AmountInfo();
		amountInfo.setOfferCost(0.0);
		amountInfo.setOfferPoints(0);
		amountInfo.setPurchaseAmount(0.0);
		eligibilityInfo.setAmountInfo(amountInfo);
		eligibilityInfo.setOfferCounters(offerCounter);
		
		purchasePaymentMethod = new PurchasePaymentMethod();
		purchasePaymentMethod.setPurchaseItem(purchaseItem);
		purchasePaymentMethod.setPaymentMethods(paymentMethods);
		
		offerCatalogResultResponseDto = new OfferCatalogResultResponseDto();
		offerCatalogResultResponseDto.setOfferId(offerCatalog.getOfferId());
		offerCatalogResultResponseDto.setOfferCode("offerCode");
		offerCatalogResultResponseDto.setOfferTypeId(OffersConfigurationConstants.discountOfferType);
		offerCatalogResultResponseDto.setOfferLabelEn("offerLabelEn");
		offerCatalogResultResponseDto.setOfferLabelAr("offerLabelAr");
		offerCatalogResultResponseDto.setOfferTitleEn("offerTitleEn");
		offerCatalogResultResponseDto.setOfferTitleAr("offerTitleAr");
		offerCatalogResultResponseDto.setOfferTitleDescriptionEn("offerTitleDescriptionEn");
		offerCatalogResultResponseDto.setOfferTitleDescriptionAr("offerTitleDescriptionAr");
		offerCatalogResultResponseDto.setOfferDetailMobileEn("offerDetailMobileEn");
		offerCatalogResultResponseDto.setOfferDetailMobileAr("offerDetailMobileAr");
		offerCatalogResultResponseDto.setOfferDetailWebEn("offerDetailWebEn");
		offerCatalogResultResponseDto.setOfferDetailWebAr("offerDetailWebAr");
		offerCatalogResultResponseDto.setBrandDescriptionEn("brandDescriptionEn");
		offerCatalogResultResponseDto.setBrandDescriptionAr("brandDescriptionAr");
		offerCatalogResultResponseDto.setTermsAndConditionsEn("termsAndConditionsEn");
		offerCatalogResultResponseDto.setTermsAndConditionsAr("termsAndConditionsAr");
		offerCatalogResultResponseDto.setAdditionalTermsAndConditionsEn("additionalTermsAndConditionsEn");
		offerCatalogResultResponseDto.setAdditionalTermsAndConditionsAr("additionalTermsAndConditionsAr");
		offerCatalogResultResponseDto.setTagsEn("tagsEn");
		offerCatalogResultResponseDto.setTagsAr("tagsAr");
		offerCatalogResultResponseDto.setWhatYouGetEn("whatYouGetEn");
		offerCatalogResultResponseDto.setWhatYouGetAr("whatYouGetAr");
		offerCatalogResultResponseDto.setOfferStartDate(new Date());
		offerCatalogResultResponseDto.setOfferEndDate(new Date());
		offerCatalogResultResponseDto.setTrendingRank(1);
		offerCatalogResultResponseDto.setStatus(OfferConstants.INACTIVE_STATUS.get());
		offerCatalogResultResponseDto.setAvailableInPortals(availableInPortals);
		offerCatalogResultResponseDto.setNewOffer(OfferConstants.FLAG_SET.get());
		offerCatalogResultResponseDto.setIsGift(OfferConstants.FLAG_SET.get());
		offerCatalogResultResponseDto.setIsDod(OfferConstants.FLAG_SET.get());
		offerCatalogResultResponseDto.setIsFeatured(OfferConstants.FLAG_SET.get());
		offerCatalogResultResponseDto.setPointsValue(240);
		offerCatalogResultResponseDto.setCost(78.0);
		offerCatalogResultResponseDto.setDiscountPerc(13);
		offerCatalogResultResponseDto.setEstSavings(14.0);
		offerCatalogResultResponseDto.setOfferLimit(limitList);
		offerCatalogResultResponseDto.setPartnerCode("partnerCode");
		offerCatalogResultResponseDto.setMerchantCode("merchantCode");
		//Full merchant details
		//Set stores
		offerCatalogResultResponseDto.setCategoryId("1");
		//Full category details
		offerCatalogResultResponseDto.setSubCategoryId("4");
		//full subcategory details
		offerCatalogResultResponseDto.setDynamicDenomination(OfferConstants.FLAG_SET.get());
		offerCatalogResultResponseDto.setGroupedFlag(OfferConstants.FLAG_SET.get());
		offerCatalogResultResponseDto.setMinDenomination(1);
		offerCatalogResultResponseDto.setMaxDenomination(45);
		offerCatalogResultResponseDto.setIncrementalValue(2);
		offerCatalogResultResponseDto.setEligibleCustomerTypes(eligibleCustomerTypes);
		offerCatalogResultResponseDto.setExclusionCustomerTypes(exclusionTypes);
		offerCatalogResultResponseDto.setEligibleCustomerSegments(eligibleCustomerTypes);
		offerCatalogResultResponseDto.setExclusionCustomerSegments(exclusionSegmentValues);
		//denominations
		offerCatalogResultResponseDto.setDenominations(null);
		offerCatalogResultResponseDto.setVoucherAction("2");
		offerCatalogResultResponseDto.setRules(rules);
		offerCatalogResultResponseDto.setProvisioningChannel("provisioningChannel");
		offerCatalogResultResponseDto.setRatePlanCode("ratePlanCode");
		offerCatalogResultResponseDto.setRtfProductCode("rtfProductCode");
		offerCatalogResultResponseDto.setRtfProductType("rtfProductType");
		offerCatalogResultResponseDto.setVasCode("vasCode");
		offerCatalogResultResponseDto.setVasActionId("vasActionId");
		offerCatalogResultResponseDto.setPromotionalPeriod(2);
		offerCatalogResultResponseDto.setPackName("packName");
		offerCatalogResultResponseDto.setFeature("feature");
		offerCatalogResultResponseDto.setActivityIdRbt("activityIdRbt");
		offerCatalogResultResponseDto.setServiceId("serviceId");
		offerCatalogResultResponseDto.setEarnMultiplier(1.0);
		offerCatalogResultResponseDto.setAccActivityId("accId");
		offerCatalogResultResponseDto.setAccActivityCd("accrualActivityCode");
		offerCatalogResultResponseDto.setAccActivityCodeDescriptionEn("accrualActivityCodeDescriptionEn");
		offerCatalogResultResponseDto.setAccActivityCodeDescriptionEn("accrualActivityCodeDescriptionAr");
		offerCatalogResultResponseDto.setRedActivityId("redId");
		offerCatalogResultResponseDto.setRedActivityCd("redemptionActivityCode");
		offerCatalogResultResponseDto.setRedActivityCodeDescriptionEn("redemptionActivityCodeDescriptionEn");
		offerCatalogResultResponseDto.setRedActivityCodeDescriptionEn("redemptionActivityCodeDescriptionAr");
		//SubOffer
		
		offerResponseList = new ArrayList<>(1);
		offerResponseList.add(offerCatalogResultResponseDto);
		
		categoryList = new ArrayList<>(1);
		categoryList.add(category);
		categoryList.add(subCategory);
		
		customerTypeList = new ArrayList<>();
		customerTypeList.add(new ParentChlidCustomer(null, "Prepaid"));
		customerTypeList.add(new ParentChlidCustomer(null, "Gold"));
		customerTypeList.add(new ParentChlidCustomer("Gold", "Hybrid"));
		
		ActivityName activityName = new ActivityName();
		activityName.setEnglish(OfferConstants.MARKETPLACE_PROGRAM_ACTIVITY.get());
		activityName.setArabic(OfferConstants.MARKETPLACE_PROGRAM_ACTIVITY.get());
		ActivityDescription activityDescription = new ActivityDescription();
		activityDescription.setEnglish("english");
		activityDescription.setArabic("arabic");
		ProgramActivityWithIdDto programActivityDto = new ProgramActivityWithIdDto();
		programActivityDto.setActivityName(activityName);
		programActivityDto.setActivityId("activityId");
		programActivityDto.setActivityDescription(activityDescription);
		programActivityList = new ArrayList<>(1);
		programActivityList.add(programActivityDto);
		
		MarketplaceImage marketplaceImage = new MarketplaceImage();
		marketplaceImage.setImageUrl("");
		MerchantOfferImage merchantOfferImage = new MerchantOfferImage();
		merchantOfferImage.setAvailableInChannel("");
		merchantOfferImage.setImageType("");
		marketplaceImage.setMerchantOfferImage(merchantOfferImage);
		imageList = new ArrayList<>(1);
		imageList.add(marketplaceImage);
		
		PurchasePaymentMethod purchasePaymentMethod = new PurchasePaymentMethod();
		purchasePaymentMethod.setPurchaseItem(OfferConstants.CASHVOUCHER.get());
		purchasePaymentMethod.setPaymentMethods(paymentMethods);
		purchasePaymentmethodList = new ArrayList<>(1);
		purchasePaymentmethodList.add(purchasePaymentMethod);
		
		ConversionRate conversionRate = new ConversionRate();
		conversionRate.setPartnerCode(OfferConstants.SMILES.get());
		conversionRate.setProductItem(OfferConstants.CASH_VOUCHER_PRODUCT.get());
		conversionRate.setLowValue(0.0);
		conversionRate.setHighValue(100000.0);
		conversionRate.setCoefficientA(0.17);
		conversionRate.setCoefficientB(-0.05);
		conversionRate.setValuePerPoint(0.08);
		conversionRateList = new ArrayList<>(1);
		conversionRateList.add(conversionRate);
		
		purchaseRequest = new PurchaseRequestDto();
		purchaseRequest.setSelectedPaymentItem(OfferConstants.CASHVOUCHER.get());
		purchaseRequest.setSelectedOption(OfferConstants.FULLCREDITCARD.get());
		purchaseRequest.setCouponQuantity(1);
		purchaseRequest.setSpentAmount(1.0);
		purchaseRequest.setSpentPoints(1);
		purchaseRequest.setPromoCode("promoCode");
		purchaseRequest.setOfferId(offerCatalog.getOfferId());
		purchaseRequest.setAccountNumber(accountNumber);
		
		promocodeApply = new PromoCodeApply();
		promocodeApply.setCost(2.0);
		promocodeApply.setPointsValue(2);
		promocodeApply.setStatus(0);
		
		List<String> voucherCodeList = new ArrayList<>(1);
		voucherCodeList.add("voucherCodes");
		savedPurchaseHistory = new PurchaseHistory();
		savedPurchaseHistory.setId("id");
		savedPurchaseHistory.setOfferId(offerCatalog.getOfferId());
		savedPurchaseHistory.setVoucherCode(voucherCodeList);
		
		purchaseHistory = new PurchaseHistory();
		purchaseHistory.setOfferId(offerCatalog.getOfferId());
		
		
		birthdayInfo = new BirthdayInfo();
		birthdayInfo.setThresholdMinusValue(5);
		birthdayInfo.setThresholdPlusValue(5);
		
		timeLimit = new TimeLimits();
		timeLimit.setDailyCount(0);
		timeLimit.setWeeklyCount(0);
		timeLimit.setMonthlyCount(0);
		timeLimit.setAnnualCount(0);
		timeLimit.setLastPurchased(new Date());
		
		birthdayGiftTracker = new BirthdayGiftTracker();
		birthdayGiftTracker.setLastViewedDate(Utilities.getDateFromSpecificDateDurationYear(-3, new Date()));
		
		paymentResponse = new PaymentResponse();
		
		resultResponse = new ResultResponse(externalTransactionId);
		purchaseResultResponse = new PurchaseResultResponse(externalTransactionId);
	
	}
	
	@Test
	public void testValidateAndGetActivityCodeForCreate() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.CASH_VOUCHER.get());
		when(repositoryHelper.getOfferType(Mockito.any())).thenReturn(offerType);
		when(repositoryHelper.getCategoryListByIdList(Mockito.any()))
			.thenReturn(categoryList);
		when(repositoryHelper.getMerchant(Mockito.any())).thenReturn(merchant);
		when(repositoryHelper.getStoreList(Mockito.any(), Mockito.any())).thenReturn(offerStores);
		when(repositoryHelper.getDenominationList(Mockito.any())).thenReturn(denomination);
		Mockito.doReturn(customerTypeList).when(fetchServiceValues).getAllCustomerTypes(Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		Mockito.doReturn(programActivityList).when(fetchServiceValues).getAllProgramActivity(Mockito.any(Headers.class), Mockito.any(ResultResponse.class));
		boolean status = helper.validateAndGetActivityCodeForCreate(offerCatalogDto, resultResponse, offerReference);
		assertTrue(status);	
	}
	
	@Test
	public void testValidateAndUpdateEarnMultiplerForUpdate() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.CASH_VOUCHER.get());
		offerCatalogDto.setEarnMultiplier(5.4);
		Requests.getUpdatePartnerActivityRequest(offerCatalogDto.getEarnMultiplier());
		when(repositoryHelper.getCategoryListByIdList(Mockito.any()))
			.thenReturn(categoryList);
		when(repositoryHelper.getMerchant(Mockito.any())).thenReturn(merchant);
		when(repositoryHelper.getStoreList(Mockito.any(), Mockito.any())).thenReturn(offerStores);
		when(repositoryHelper.getDenominationList(Mockito.any())).thenReturn(denomination);
		Mockito.doReturn(customerTypeList).when(fetchServiceValues).getAllCustomerTypes(Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		Mockito.doReturn(programActivityList).when(fetchServiceValues).getAllProgramActivity(Mockito.any(Headers.class), Mockito.any(ResultResponse.class));
		Mockito.doReturn(true).when(fetchServiceValues).updatePartnerActivity(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(PartnerActivityConfigDto.class), Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		boolean status = helper.validateAndUpdateEarnMultiplerForUpdate(offerCatalogDto, offerCatalog, resultResponse, offerReference);
		assertTrue(status);
	}
	
	@Test
	public void testGetImageUrlListForOfferListNull() throws MarketplaceException {
		
		List<MarketplaceImage> imageList = helper.getImageUrlListForOfferList(null);
		assertNull(imageList);	
	}
	
	@Test
	public void testGetOfferResponse() throws MarketplaceException {
		
		when(modelMapper.map(offerCatalog, OfferCatalogResultResponseDto.class)).thenReturn(offerCatalogResultResponseDto);
		offerCatalogResultResponseDto = helper.getOfferResponse(offerCatalog, purchasePaymentmethodList, conversionRateList, imageList);
		assertNotNull(offerCatalogResultResponseDto);	
	}
	
	@Test(expected=MarketplaceException.class)
	public void testGetOfferResponseException() throws MarketplaceException {
		
		when(modelMapper.map(offerCatalog, OfferCatalogResultResponseDto.class)).thenThrow(NullPointerException.class);
		offerCatalogResultResponseDto = helper.getOfferResponse(offerCatalog, purchasePaymentmethodList, conversionRateList, imageList);	
	}
	
	@Test
	public void testGetEligibleOfferList() throws MarketplaceException {
		
		eligibilityInfo.setMember(true);
		when(modelMapper.map(offerCatalog, OfferCatalogResultResponseDto.class)).thenReturn(offerCatalogResultResponseDto);
		offerResponseList = helper.getEligibleOfferList(eligibilityInfo, resultResponse, true);
		assertNotNull(offerResponseList);	
	}
	
	@Test
	public void testGetTransactionListPaymentMethods() throws MarketplaceException {
		
		List<PurchaseHistory> purchaseRecords = new ArrayList<>(1);
		PurchaseHistory purchaseHistory = new PurchaseHistory();
		purchaseHistory.setPurchaseItem(OfferConstants.CASHVOUCHER.get());
		purchaseHistory.setPaymentMethod(OfferConstants.FULLCREDITCARD.get());
		purchaseRecords.add(purchaseHistory);
		List<PaymentMethod> paymentMethodList = helper.getTransactionListPaymentMethods(purchaseRecords);
		assertNotNull(paymentMethodList);	
	}
	
	@Test
	public void testgetTransactionListPaymentMethodsNullPurchaseRecords() throws MarketplaceException {
		
		List<PaymentMethod> paymentMethodList = helper.getTransactionListPaymentMethods(null);
		assertNotNull(paymentMethodList);	
	}
	
	@Test
	public void testValidateOfferDetails() throws MarketplaceException {
		
		//Complete this.....promocodeDomain coming null
		offerCatalog.setStatus(OfferConstants.ACTIVE_STATUS.get());
		offerCatalog.getOfferValues().setPointsValue(null);
		purchaseRequest.setSelectedOption(OfferConstants.FULLPOINTS.get());
		when(repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(Mockito.any(), 
				Mockito.any())).thenReturn(conversionRateList);
		Mockito.doReturn(promocodeApply)
			   .when(promoCodeDomain)
			   .applyPromoCode(Mockito.any(String.class), 
				Mockito.any(String.class), Mockito.any(String.class), 
				Mockito.any(Double.class), Mockito.any(Integer.class), 
				Mockito.any(String.class), Mockito.any(Headers.class),
				Mockito.any(GetMemberResponse.class), Mockito.any(ResultResponse.class));
		when(repositoryHelper.findCounterForCurrentOffer(eligibilityInfo.getOffer().getOfferId()))
			.thenReturn(offerCounter);
		when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(timeLimit);
		when(repositoryHelper.saveAllOfferCounters(Mockito.any())).thenReturn(offerCounterList);
		Boolean status = helper.validateOfferDetails(eligibilityInfo, purchaseRequest, resultResponse);
		assertTrue(status);	
	}
	
	@Test
	public void testMakePaymentAndGetResponse() throws MarketplaceException {
		
		//PurchaseHistory coming null
		purchaseHistory = helper.makePaymentAndGetResponse(eligibilityInfo, purchaseRequest, purchaseResultResponse);	
		assertNull(purchaseHistory);
	}
	
	@Test
	public void testPurchaseItem() throws MarketplaceException {
		
		when(repositoryHelper.findBirthdayInfo()).thenReturn(birthdayInfo);
		eligibilityInfo.getOffer().setIsBirthdayGift(OfferConstants.FLAG_SET.get());
		Mockito.doReturn(savedPurchaseHistory).when(purchaseDomain).saveUpdatePurchaseDetails(Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any()); 
		Mockito.doReturn(paymentResponse).when(paymentService).paymentAndProvisioning(Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		helper.purchaseItem(purchaseRequest, eligibilityInfo, purchaseResultResponse);
		assertNotNull(birthdayInfo);
	}
	
	@Test
	public void testPerformAdditionalOperationsGoldCertficate() throws MarketplaceException {
		purchaseRequest.setSelectedPaymentItem(OfferConstants.GOLDCERTIFICATE.get());
		purchaseRequest.setAccountNumber("newAccountNumber");
		purchaseRequest.setMembershipCode("newMembershipCode");
		purchaseRequest.setVoucherDenomination(1);
		helper.performAdditionalOperation(purchaseRequest, eligibilityInfo, purchaseHistory, purchaseResultResponse);
		assertNotNull(purchaseHistory);
	}
	
	@Test
	public void testPerformAdditionalOperationsDiscountVoucher() throws MarketplaceException {
		purchaseRequest.setSelectedPaymentItem(OfferConstants.DISCOUNTVOUCHER.get());
		purchaseRequest.setVoucherDenomination(2);
		eligibilityInfo.getMemberDetails().setSubscribed(false);
		helper.performAdditionalOperation(purchaseRequest, eligibilityInfo, purchaseHistory, purchaseResultResponse);
		assertNotNull(purchaseHistory);
	}
	
	@Test
	public void testGetPaymentMethods() throws MarketplaceException {
		paymentMethods = helper.getPaymentMethods(offerCatalog.getOfferType().getOfferTypeId());
		assertNotNull(purchasePaymentMethod);
	}
	
	@Test
	public void testCheckMembershipEligbilityForOffers() throws MarketplaceException {
		eligibilityInfo.setMember(true);
		eligibilityInfo.setMemberDetails(null);
		Mockito.doReturn(memberDetails).when(fetchServiceValues).getMemberDetails(Mockito.any(String.class), Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		Mockito.doReturn(customerTypeList).when(fetchServiceValues).getAllCustomerTypes(Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		boolean Status = helper.checkMembershipEligbilityForOffers(eligibilityInfo, accountNumber, purchaseResultResponse);
		assertTrue(Status);
	}
	
	@Test
	public void testGetMemberEligibilityForDetailedEligibleOffer() throws MarketplaceException {
		
		eligibilityInfo.setMember(true);
		eligibilityInfo.setMemberDetails(null);
		Mockito.doReturn(memberDetails).when(fetchServiceValues).getMemberDetails(Mockito.any(String.class), Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		Mockito.doReturn(customerTypeList).when(fetchServiceValues).getAllCustomerTypes(Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		boolean Status = helper.getMemberEligibilityForDetailedEligibleOffer(eligibilityInfo, accountNumber, resultResponse);
		assertTrue(Status);
	}
	
	@Test
	public void testValidateAndGiftOffer() throws MarketplaceException {
		
		List<String> giftChannels = new ArrayList<>(1);
		giftChannels.add(channelId);
		offerCatalog.setGiftInfo(new GiftInfo(OfferConstants.FLAG_SET.get(), giftChannels, null));
		when(repositoryHelper.findByIsGift(Mockito.any(String.class))).thenReturn(offerList);
		Mockito.doReturn(savedPurchaseHistory).when(purchaseDomain).saveUpdatePurchaseDetails(Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any()); 
		Mockito.doReturn(paymentResponse).when(paymentService).paymentAndProvisioning(Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		String voucherCode = helper.validateAndGiftOffer(purchaseRequest, header, null, purchaseResultResponse);
		assertNotNull(voucherCode);
	}
	
	@Test
	public void testGetGoldCertificateAmountOrPoints() throws MarketplaceException {
		
		conversionRateList.get(0).setProductItem(OfferConstants.GOLD_CERTIFICATE_PRODUCT.get());
		when(repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(Mockito.any(), 
				Mockito.any())).thenReturn(conversionRateList);
		AmountPoints amountPoints = helper.getGoldCertificateAmountOrPoints(89.5, resultResponse);
		assertNotNull(amountPoints);
	}
	
	
	
	
	
	
	
		
	
	
	
	
}
	
	
		
	
		
		
		
	
	
