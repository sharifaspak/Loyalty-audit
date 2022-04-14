package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

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

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.OfferReferences;
import com.loyalty.marketplace.offers.inbound.dto.DenominationLimitDto;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersRequest;
import com.loyalty.marketplace.offers.inbound.dto.LimitDto;
import com.loyalty.marketplace.offers.inbound.dto.ListValuesDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.SubOfferDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.PaymentMethods;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.AddTAndC;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.BrandDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.DynamicDenominationValue;
import com.loyalty.marketplace.offers.outbound.database.entity.ListValues;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDate;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetailMobile;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetailWeb;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLabel;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLimit;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTitle;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTitleDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTypeDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferValues;
import com.loyalty.marketplace.offers.outbound.database.entity.ProvisioningAttributes;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.TAndC;
import com.loyalty.marketplace.offers.outbound.database.entity.Tags;
import com.loyalty.marketplace.offers.outbound.database.entity.TermsConditions;
import com.loyalty.marketplace.offers.outbound.database.entity.VoucherInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.WhatYouGet;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.StoreDto;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.StoreAddress;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.StoreDescription;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.DenominationDescription;
import com.loyalty.marketplace.outbound.database.entity.DenominationValue;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoException;

@SpringBootTest(classes=OfferCatalogDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferCatalogDomainTest {
	
	@Mock
	Validator validator;
	
	@Mock
	ModelMapper modelMapper;
	
	@Mock
	RepositoryHelper repositoryHelper;
	
	@Mock
	OffersHelper helper;
	
	@Mock
	ProgramManagement programManagement;	
	
	@Mock
	BirthdayGiftTrackerDomain birthdayGiftTrackerDomain;
	
	@Mock
	FetchServiceValues fetchServiceValues;
	
	@Mock
	AuditService auditService;
	
	@Value(OffersConfigurationConstants.ADMIN)
	protected String adminRole;
	
	@Value(OffersConfigurationConstants.OFFER_UPADTE_ROLES)
	protected List<String> roles;
	
	@InjectMocks
	OfferCatalogDomain offerCatalogDomain = new OfferCatalogDomain();
	
	private String program;
	private String status;
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
	private String offerId;
	private String externalTransactionId;
	private String token;
	private String partnerCode;
	private String merchantCode;
	private String accountNumber;
	private String channelId;
	private String purchaseItem;
	private Headers header;
	private OfferReferences offerReference;
	private List<OfferCatalog> offerList;
	private GetMemberResponse memberDetails;
	private PurchasePaymentMethod purchasePaymentMethod;
	private EligibilityInfo eligibilityInfo;
	private OfferRating offerRating;
	private Integer page;
	private Integer pageLimit;
	private String offerTypeId;
	private OfferCatalogResultResponseDto offerCatalogResultResponseDto; 
 		
	@Before
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
        program = "Smiles";
		status = OfferConstants.ACTIVE_STATUS.get();
		offerId="offerId";
		userName="userName";
		externalTransactionId="externalTransactionId";
		token = "token";
		partnerCode = "partnerCode";
		merchantCode="merchantCode";
		accountNumber="accountNumber";
		channelId="SWEB";
		purchaseItem="purchaseItem";
		page = 1;
		pageLimit = 20;
		offerTypeId = "1";
		
		header = new Headers(program, "", externalTransactionId, userName, "", "",
				channelId, "", "", token, "");
		
		offerRating = new OfferRating();
				
		offerCatalogDto = new OfferCatalogDto();
		offerCatalogDto.setOfferCode("offerCode");
		offerCatalogDto.setOfferTypeId(OfferConstants.DISCOUNT_OFFER.get());
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
		List<LimitDto> limitList = new ArrayList<>(1);
		LimitDto limitDto = new LimitDto();
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
		offerCatalogDto.setDynamicDenomination(OfferConstants.FLAG_SET.get());
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
		offerType.setPaymentMethods(paymentMethods);
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
		
		//Set gift info
		
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
		offerCatalog.setProvisioningChannel("provisioningChannel");
		provisioningAttributes = new ProvisioningAttributes();
		provisioningAttributes.setRatePlanCode("ratePlanCode");
		provisioningAttributes.setRtfProductCode("rtfProductCode");
		provisioningAttributes.setRtfProductType("rtfProductType");
		provisioningAttributes.setVasCode("vasCode");
		provisioningAttributes.setVasActionId("vasActionId");
		offerCatalog.setProvisioningAttributes(provisioningAttributes);
		offerCatalog.setEarnMultiplier(2.0);
		VoucherInfo voucherInfo = new VoucherInfo();
		voucherInfo.setVoucherAction("2");
		voucherInfo.setVoucherAmount(0.0);
		voucherInfo.setVoucherExpiryPeriod(0);
		voucherInfo.setVoucherExpiryDate(new Date());
		offerCatalog.setVoucherInfo(voucherInfo);
		
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
		memberDetails.setCustomerSegment(eligibleSegmentValues);
		
		AccountOfferCount accountOfferCount = new AccountOfferCount();
		accountOfferCount.setDailyCount(0);
		accountOfferCount.setWeeklyCount(0);
		accountOfferCount.setMonthlyCount(0);
		accountOfferCount.setAnnualCount(0);
		accountOfferCount.setTotalCount(0);
		accountOfferCount.setAccountNumber(memberDetails.getAccountNumber());
		accountOfferCount.setMembershipCode(memberDetails.getMembershipCode());
		accountOfferCount.setDenominationCount(null);
		List<AccountOfferCount> accountOfferCounterList = new ArrayList<>(1);
		accountOfferCounterList.add(accountOfferCount);
		
		MemberOfferCount memberOfferCount = new MemberOfferCount();
		memberOfferCount.setDailyCount(0);
		memberOfferCount.setWeeklyCount(0);
		memberOfferCount.setMonthlyCount(0);
		memberOfferCount.setAnnualCount(0);
		memberOfferCount.setTotalCount(0);
		memberOfferCount.setMembershipCode(memberDetails.getMembershipCode());
		memberOfferCount.setDenominationCount(null);
		List<MemberOfferCount> memberOfferCounterList = new ArrayList<>(1);
		memberOfferCounterList.add(memberOfferCount);
		
		OfferCounter offerCounter = new OfferCounter();
		offerCounter.setOfferId(offerCatalog.getOfferId());
		offerCounter.setDailyCount(0);
		offerCounter.setWeeklyCount(0);
		offerCounter.setMonthlyCount(0);
		offerCounter.setAnnualCount(0);
		offerCounter.setTotalCount(0);
		offerCounter.setLastPurchased(new Date());
		offerCounter.setDenominationCount(null);
		offerCounter.setAccountOfferCount(accountOfferCounterList);
		offerCounter.setMemberOfferCount(memberOfferCounterList);
		
		List<OfferCounter> offerCounterList = new ArrayList<>(1);
		offerCounterList.add(offerCounter);
		
		eligibilityInfo = new EligibilityInfo();
		eligibilityInfo.setMemberDetails(memberDetails);
		eligibilityInfo.setOfferCounterList(offerCounterList);
		
		purchasePaymentMethod = new PurchasePaymentMethod();
		purchasePaymentMethod.setPurchaseItem(purchaseItem);
		purchasePaymentMethod.setPaymentMethods(paymentMethods);
		
		offerCatalogResultResponseDto = new OfferCatalogResultResponseDto();
		offerCatalogResultResponseDto.setOfferId(offerCatalog.getOfferId());
		
		
		resultResponse = new ResultResponse(OfferConstants.EMPTY_CHARACTER.get());
	
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNull(offerCatalogDomain.getId());
	    assertNull(offerCatalogDomain.getOfferId());
	    assertNull(offerCatalogDomain.getProgramCode());
	    assertNull(offerCatalogDomain.getOfferCode());
	    assertNull(offerCatalogDomain.getOfferType());
	    assertNull(offerCatalogDomain.getOffer());
		assertNull(offerCatalogDomain.getBrandDescription());
		assertNull(offerCatalogDomain.getTermsAndConditions());
		assertNull(offerCatalogDomain.getTags());
		assertNull(offerCatalogDomain.getWhatYouGet());
		assertNull(offerCatalogDomain.getOfferDates());
		assertNull(offerCatalogDomain.getTrendingRank());
		assertNull(offerCatalogDomain.getStatus());
		assertNull(offerCatalogDomain.getAvailableInPortals());
		assertNull(offerCatalogDomain.getNewOffer());
		assertNull(offerCatalogDomain.getGiftInfo());
		assertNull(offerCatalogDomain.getIsDod());
		assertNull(offerCatalogDomain.getIsFeatured());
		assertNull(offerCatalogDomain.getOfferValues());
		assertNull(offerCatalogDomain.getDiscountPerc());
		assertNull(offerCatalogDomain.getEstSavings());
		assertNull(offerCatalogDomain.getSharing());
		assertNull(offerCatalogDomain.getSharingBonus());
		assertNull(offerCatalogDomain.getVatPercentage());
		assertNull(offerCatalogDomain.getLimit());
		assertNull(offerCatalogDomain.getPartnerCode());
		assertNull(offerCatalogDomain.getMerchant());
		assertNull(offerCatalogDomain.getOfferStores());
		assertNull(offerCatalogDomain.getCategory());
		assertNull(offerCatalogDomain.getSubCategory());
		assertNull(offerCatalogDomain.getDynamicDenomination());
		assertNull(offerCatalogDomain.getGroupedFlag());
		assertNull(offerCatalogDomain.getDynamicDenominationValue());
		assertNull(offerCatalogDomain.getIncrementalValue());
		assertNull(offerCatalogDomain.getCustomerTypes());
		assertNull(offerCatalogDomain.getDenominations());
		assertNull(offerCatalogDomain.getVoucherInfo());
		assertNull(offerCatalogDomain.getRules());
		assertNull(offerCatalogDomain.getProvisioningChannel());
		assertNull(offerCatalogDomain.getProvisioningAttributes());
		assertNull(offerCatalogDomain.getSubOffer());
		assertNull(offerCatalogDomain.getEarnMultiplier());
		assertNull(offerCatalogDomain.getActivityCode());
		assertNull(offerCatalogDomain.getCreatedDate());
		assertNull(offerCatalogDomain.getCreatedUser());
		assertNull(offerCatalogDomain.getUpdatedDate());
		assertNull(offerCatalogDomain.getUpdatedUser());
		
	}
	
	/**
	 * 
	 * Test toString method
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerCatalogDomain.toString());
	}	
	
	/////////////////////////////////////////CREATE OFFER/////////////////////////////////////////////
	@Test
	public void testValidateAndSaveOfferSuccess() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.getAllOfferCatalog()).thenReturn(new ArrayList<>(1));
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.DISCOUNT_OFFER_CREATED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testValidateAndSaveOfferMarketplaceException() throws MarketplaceException, ParseException {
		
		Mockito.doThrow(MarketplaceException.class).when(helper).validateAndGetActivityCodeForCreate(Mockito.any(OfferCatalogDto.class), 
				Mockito.any(ResultResponse.class), Mockito.any(OfferReferences.class));
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferErrorCodes.OFFER_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testValidateAndSaveOfferException() throws MarketplaceException, ParseException {
		
		Mockito.doThrow(NullPointerException.class).when(helper).validateAndGetActivityCodeForCreate(Mockito.any(OfferCatalogDto.class), 
				Mockito.any(ResultResponse.class), Mockito.any(OfferReferences.class));
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferErrorCodes.OFFER_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testValidateAndSaveOfferDiscountOffer() throws MarketplaceException, ParseException {
		offerCatalogDto.setOfferEndDate(null);
		offerCatalogDto.setVoucherExpiryDate(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(String.valueOf(OfferSuccessCodes.DISCOUNT_OFFER_CREATED.getIntId()), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferDiscountOfferWithoutRules() throws MarketplaceException, ParseException {
		offerCatalogDto.setRules(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.DISCOUNT_OFFER_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}

	@Test
	public void testValidateAndSaveOfferDealVoucher() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.DEAL_VOUCHER.get());
		offerCatalogDto.getCustomerSegments().setExclusionTypes(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.DEAL_VOUCHER_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferDealVoucherWithoutRules() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.DEAL_VOUCHER.get());
		offerCatalogDto.setRules(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.DEAL_VOUCHER_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}

	
	@Test
	public void testValidateAndSaveOfferCashVoucher() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.CASH_VOUCHER.get());
		offerCatalogDto.getCustomerSegments().setEligibleTypes(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.CASH_VOUCHER_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferCashVoucherWithoutRules() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.CASH_VOUCHER.get());
		offerCatalogDto.setRules(null);
		offerCatalogDto.getCustomerSegments().setEligibleTypes(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.CASH_VOUCHER_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferAddOnComs() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.ETISALT_ADDON.get());
		offerCatalogDto.setProvisioningChannel(OfferConstants.COMS_PROVISIONING_CHANNEL.get());
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.ETISALAT_COMS_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferAddOnComsWithoutRules() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.ETISALT_ADDON.get());
		offerCatalogDto.setProvisioningChannel(OfferConstants.COMS_PROVISIONING_CHANNEL.get());
		offerCatalogDto.setRules(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.ETISALAT_COMS_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferAddOnRtf() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.ETISALT_ADDON.get());
		offerCatalogDto.setProvisioningChannel(OfferConstants.RTF_PROVISIONING_CHANNEL.get());
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.ETISALAT_RTF_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferAddOnEmcais() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.ETISALT_ADDON.get());
		offerCatalogDto.setProvisioningChannel(OfferConstants.EMCAIS_PROVISIONING_CHANNEL.get());
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.ETISALAT_EMCAIS_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferAddOnPhonyTunes() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.ETISALT_ADDON.get());
		offerCatalogDto.setProvisioningChannel(OfferConstants.PHONYTUNES_PROVISIONING_CHANNEL.get());
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.ETISALAT_PHONEY_TUNES_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferAddOnRbt() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.ETISALT_ADDON.get());
		offerCatalogDto.setProvisioningChannel(OfferConstants.RBT_PROVISIONING_CHANNEL.get());
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.ETISALAT_RBT_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}

	@Test
	public void testValidateAndSaveGoldCertificate() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.GOLD_CERTIFICATE.get());
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.GOLD_CERTIFICATE_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveOfferGoldCertificateWithoutRules() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.GOLD_CERTIFICATE.get());
		offerCatalogDto.setRules(null);
		offerCatalogDto.getCustomerSegments().setEligibleTypes(null);
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferSuccessCodes.GOLD_CERTIFICATE_CREATED.getId(), resultResponse.getResult().getResponse());
        assertNotNull(domain);

	}
	
	@Test
	public void testValidateAndSaveFailureFull() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.GOLD_CERTIFICATE.get());
		Mockito.doReturn(true).when(helper).validateAndGetActivityCodeForCreate(
				 Mockito.any(OfferCatalogDto.class), Mockito.any(ResultResponse.class),
				 Mockito.any(OfferReferences.class));
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferErrorCodes.OFFER_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testValidateAndSaveFailureAbsentMandatoryDenominations() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferTypeId(OfferConstants.GOLD_CERTIFICATE.get());
		offerCatalogDto.setDenominations(null);
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferErrorCodes.OFFER_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testValidateAndSaveFailureWronfDateFormat() throws MarketplaceException, ParseException {
		
		offerCatalogDto.setOfferStartDate("abcd");
		resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogDto, header);
		assertEquals(OfferErrorCodes.OFFER_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());

	}
	
  /////////////////////////////////////////UPDATE OFFER/////////////////////////////////////////////
	
//	@Test
//	public void testValidateAndUpdateOfferSuccess() throws MarketplaceException, ParseException {
//		offerCatalogDto.setAction(OfferConstants.UPDATE_ACTION.get());
//		String userRole = "Internal/admin";
//		resultResponse = offerCatalogDomain.validateAndUpdateOffer(offerCatalogDto, offerId, header, userRole);
//		assertEquals(OfferErrorCodes.OFFER_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//
//	}
	
	
	
	
	
  /////////////////////////////////////////SAVE UPDATE OFFER/////////////////////////////////////////////
	
	@Test
	public void testSaveUpdateOfferInsertSuccess() throws MarketplaceException, ParseException {
		
		OfferCatalog savedOffer = offerCatalog;
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, null, offerCatalogDto, offerReference);
		when(modelMapper.map(domain, OfferCatalog.class)).thenReturn(offerCatalog);
		when(modelMapper.map(domain.getCategory(), Category.class)).thenReturn(category);
		when(modelMapper.map(domain.getSubCategory(),Category.class)).thenReturn(subCategory);
		when(repositoryHelper.saveOffer(offerCatalog)).thenReturn(savedOffer);
		savedOffer = offerCatalogDomain.saveUpdateOffer(domain, OfferConstants.INSERT_ACTION.get(), null, header);
		assertNotNull(savedOffer);

	}
	
	@Test
	public void testSaveUpdateOfferUpdateSuccess() throws MarketplaceException, ParseException {
		
		OfferCatalog savedOffer = offerCatalog;
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, offerCatalog, offerCatalogDto, offerReference);
		when(modelMapper.map(domain, OfferCatalog.class)).thenReturn(offerCatalog);
		when(modelMapper.map(domain.getCategory(), Category.class)).thenReturn(category);
		when(modelMapper.map(domain.getSubCategory(),Category.class)).thenReturn(subCategory);
		when(repositoryHelper.saveOffer(offerCatalog)).thenReturn(savedOffer);
		savedOffer = offerCatalogDomain.saveUpdateOffer(domain, OfferConstants.UPDATE_ACTION.get(), null, header);
		assertNotNull(savedOffer);

	}
	
	@Test(expected=MarketplaceException.class)
	public void testSaveUpdateOfferUpdateMongoException() throws MarketplaceException, ParseException {
		
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, offerCatalog, offerCatalogDto, offerReference);
		when(modelMapper.map(domain, OfferCatalog.class)).thenReturn(offerCatalog);
		when(modelMapper.map(domain.getCategory(), Category.class)).thenReturn(category);
		when(modelMapper.map(domain.getSubCategory(),Category.class)).thenReturn(subCategory);
		when(repositoryHelper.saveOffer(offerCatalog)).thenThrow(MongoException.class);
		offerCatalogDomain.saveUpdateOffer(domain, OfferConstants.UPDATE_ACTION.get(), null, header);

	}
	
	@Test(expected=MarketplaceException.class)
	public void testSaveUpdateOfferSaveException() throws MarketplaceException, ParseException {
		
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, offerCatalog, offerCatalogDto, offerReference);
		when(modelMapper.map(domain, OfferCatalog.class)).thenThrow(NullPointerException.class);
		offerCatalogDomain.saveUpdateOffer(domain, OfferConstants.INSERT_ACTION.get(), null, header);

	}
	
	@Test(expected=MarketplaceException.class)
	public void testSaveUpdateOfferUpdateException() throws MarketplaceException, ParseException {
		
		OfferCatalogDomain domain = DomainConfiguration.getOfferDomain(header, offerCatalog, offerCatalogDto, offerReference);
		offerCatalogDomain.saveUpdateOffer(domain, OfferConstants.UPDATE_ACTION.get(), null, header);
		
	}
	
	
   /////////////////////////////////////////UPDATE OFFER STATUS/////////////////////////////////////////////
	
	@Test
	public void testUpdateOfferStatusActivateSuccess() throws MarketplaceException, ParseException {
		
		OfferCatalog savedOffer = offerCatalog;
		when(repositoryHelper.saveOffer(offerCatalog)).thenReturn(savedOffer);
		savedOffer = offerCatalogDomain.updateOfferStatus(offerCatalog, status, userName, externalTransactionId);
		assertNotNull(savedOffer);

	}
	
	@Test
	public void testUpdateOfferStatusDeactivateSuccess() throws MarketplaceException, ParseException {
		
		status = OfferConstants.OFFER_DEFAULT_STATUS.get();
		OfferCatalog savedOffer = offerCatalog;
		when(repositoryHelper.saveOffer(offerCatalog)).thenReturn(savedOffer);
		savedOffer = offerCatalogDomain.updateOfferStatus(offerCatalog, status, userName, externalTransactionId);
		assertNotNull(savedOffer);

	}
	
	@Test(expected=MarketplaceException.class)
	public void testUpdateOfferStatusMongoException() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.saveOffer(offerCatalog)).thenThrow(MongoException.class);;
		offerCatalogDomain.updateOfferStatus(offerCatalog, status, userName, externalTransactionId);

	}
	
	@Test(expected=MarketplaceException.class)
	public void testUpdateOfferStatusException() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.saveOffer(offerCatalog)).thenThrow(NullPointerException.class);
		offerCatalogDomain.updateOfferStatus(offerCatalog, status, userName, externalTransactionId);

	}
	
	@Test
	public void testChangeOfferStatusInvalidAction() throws MarketplaceException, ParseException {
		
		resultResponse = offerCatalogDomain.changeOfferStatus(offerId, "status", userName, externalTransactionId, resultResponse);
		assertEquals(OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testChangeOfferStatusActivateSuccess() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		resultResponse = offerCatalogDomain.changeOfferStatus(offerId, status, userName, externalTransactionId, resultResponse);
		assertEquals(OfferSuccessCodes.OFFER_ACTIVATED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testChangeOfferStatusDeactivateSuccess() throws MarketplaceException, ParseException {
		
		offerCatalog.setStatus(status);
		status = OfferConstants.OFFER_DEFAULT_STATUS.get();
		when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		resultResponse = offerCatalogDomain.changeOfferStatus(offerId, status, userName, externalTransactionId, resultResponse);
		assertEquals(OfferSuccessCodes.OFFER_DEACTIVATED.getId(), resultResponse.getResult().getResponse());

	}
	
	@Test
	public void testChangeOfferStatusException() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.findByOfferId(offerId)).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.changeOfferStatus(offerId, status, userName, externalTransactionId, resultResponse);
		assertEquals(OfferErrorCodes.OFFER_STATUS_UPDATE_FAILED.getId(), resultResponse.getResult().getResponse());

	}
	
	
	
	
	
	 /////////////////////////////////////////GET ALL OFFERS FOR ADMINISTRATOR/////////////////////////////////////////////
	
//	@Test
//	public void testGetAllOffersForAdministratorSuccess() throws MarketplaceException, ParseException {
//		
//		List<OfferCatalog> offerList = new ArrayList<>(1);
//		offerList.add(offerCatalog);
//		when(repositoryHelper.findAllAdminOffers(page, pageLimit, status, offerTypeId, resultResponse)).thenReturn(offerList);
//		resultResponse = offerCatalogDomain.getAllOffersForAdministrator(header, page, pageLimit, status, offerTypeId);
//		assertEquals(OfferSuccessCodes.OFFERS_LISTED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
//		
//    }
	
	@Test
	public void testGetAllOffersForAdministratorException() throws MarketplaceException, ParseException {
		
		List<OfferCatalog> offerList = new ArrayList<>(1);
		offerList.add(offerCatalog);
		when(repositoryHelper.findAllAdminOffers(page, pageLimit, status, offerTypeId, resultResponse)).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.getAllOffersForAdministrator(header, page, pageLimit, status, offerTypeId);
		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
		
    }
	
//	@Test
//	public void testGetAllOffersForAdministratorMarketplaceException() throws MarketplaceException, ParseException {
//		
//		List<OfferCatalog> offerList = new ArrayList<>(1);
//		offerList.add(offerCatalog);
//		when(repositoryHelper.findAllAdminOffers(page, pageLimit, status, offerTypeId, resultResponse)).thenReturn(offerList);
//		Mockito.doThrow(MarketplaceException.class).when(helper).getOfferResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
//		resultResponse = offerCatalogDomain.getAllOffersForAdministrator(header, page, pageLimit, status, offerTypeId);
//		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
//		
//    }
	
	/////////////////////////////////////////GET OFFERS FOR PARTNER/////////////////////////////////////////////
		
//	@Test
//	public void testGetAllOffersForPartnerSuccess() throws MarketplaceException, ParseException {
//	
//		Mockito.doReturn(true).when(fetchServiceValues).checkPartnerExists(partnerCode, header);
//		when(repositoryHelper.findOffersByPartnerCode(partnerCode, page, pageLimit, status, offerTypeId, resultResponse)).thenReturn(offerList);
//		resultResponse = offerCatalogDomain.getAllOffersForPartner(header, partnerCode, page, pageLimit, status, offerTypeId);
//		assertEquals(OfferSuccessCodes.OFFERS_LISTED_FOR_PARTNER_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
//	
//	}
	
	@Test
	public void testGetAllOffersForPartnerException() throws MarketplaceException, ParseException {
	
		Mockito.doReturn(true).when(fetchServiceValues).checkPartnerExists(partnerCode, header);
		when(repositoryHelper.findOffersByPartnerCode(partnerCode, page, pageLimit, status, offerTypeId, resultResponse)).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.getAllOffersForPartner(header, partnerCode, page, pageLimit, status, offerTypeId);
		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
//	@Test
//	public void testGetAllOffersForPartnerMarketplaceException() throws MarketplaceException, ParseException {
//	
//		Mockito.doReturn(true).when(fetchServiceValues).checkPartnerExists(partnerCode, header);
//		when(repositoryHelper.findOffersByPartnerCode(partnerCode, page, pageLimit, status, offerTypeId, resultResponse)).thenReturn(offerList);
//		Mockito.doThrow(MarketplaceException.class).when(helper).getOfferResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
//		resultResponse = offerCatalogDomain.getAllOffersForPartner(header, partnerCode, page, pageLimit, status, offerTypeId);
//		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
//	
//	}
	
	/////////////////////////////////////////GET OFFERS FOR MERCHANT/////////////////////////////////////////////
		
//	@Test
//	public void testGetAllOffersForMerchantSuccess() throws MarketplaceException {
//	
//		when(repositoryHelper.getMerchant(merchantCode)).thenReturn(merchant);
//		when(repositoryHelper.findOfferByMerchant(merchant, page, pageLimit, status, offerTypeId, resultResponse)).thenReturn(offerList);
//		resultResponse = offerCatalogDomain.getAllOffersForMerchant(header, merchantCode, page, pageLimit, status, offerTypeId);
//		assertEquals(OfferSuccessCodes.OFFERS_LISTED_FOR_MERCHANT_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
//	
//	}
	
	@Test
	public void testGetAllOffersForMerchantException() throws MarketplaceException, ParseException {
	
		when(repositoryHelper.getMerchant(merchantCode)).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.getAllOffersForMerchant(header, merchantCode, page, pageLimit, status, offerTypeId);
		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
//	@Test
//	public void testGetAllOffersForMerchantMarketplaceException() throws MarketplaceException, ParseException {
//	
//		when(repositoryHelper.getMerchant(merchantCode)).thenReturn(merchant);
//		when(repositoryHelper.findOfferByMerchant(merchant, page, pageLimit, status, offerTypeId, resultResponse)).thenReturn(offerList);
//		Mockito.doThrow(MarketplaceException.class).when(helper).getOfferResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
//		resultResponse = offerCatalogDomain.getAllOffersForMerchant(header, merchantCode, page, pageLimit, status, offerTypeId);
//		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
//	
//	}
	
	/////////////////////////////////////////GET DETAIL OFFERS/////////////////////////////////////////////
		
	@Test
	public void testGetDetailedOffersSuccess() throws MarketplaceException {
	
		when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		resultResponse = offerCatalogDomain.getDetailedOfferPortal(header, offerId);
		assertEquals(OfferSuccessCodes.SPECIFIC_OFFER_PORTAL_LISTED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetDetailedOffersException() throws MarketplaceException, ParseException {
	
		when(repositoryHelper.findByOfferId(offerId)).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.getDetailedOfferPortal(header, offerId);
		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetDetailedOffersMarketplaceException() throws MarketplaceException, ParseException {
	
		when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		Mockito.doThrow(MarketplaceException.class).when(helper).getOfferResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		resultResponse = offerCatalogDomain.getDetailedOfferPortal(header, offerId);
		assertEquals(OfferErrorCodes.LISTING_OFFERS_PORTAL_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
	/////////////////////////////////////////ELIGIBLE PAYMENT METHODS/////////////////////////////////////////////
	
	@Test
	public void testGetEligiblePaymentMethodsWithPurchaseItemSuccess() throws MarketplaceException {
	    
		when(repositoryHelper.getPaymentMethodsForPurchaseItem(purchaseItem)).thenReturn(purchasePaymentMethod);
		Mockito.doReturn(memberDetails).when(fetchServiceValues).getMemberDetails(Mockito.any(), Mockito.any(), Mockito.any());
		resultResponse = offerCatalogDomain.getEligiblePaymentMethods(purchaseItem, accountNumber, header);
		assertEquals(OfferSuccessCodes.PAYMENT_METHODS_LISTED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetEligiblePaymentMethodsWithOfferIdSuccess() throws MarketplaceException {
	    offerCatalog.setOfferType(offerType);
		when(repositoryHelper.getPaymentMethodsForPurchaseItem(purchaseItem)).thenReturn(null);
		when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		Mockito.doReturn(purchasePaymentMethod.getPaymentMethods()).when(helper).getPaymentMethods(Mockito.any(String.class));
		Mockito.doReturn(memberDetails).when(fetchServiceValues).getMemberDetails(Mockito.any(), Mockito.any(), Mockito.any());
		resultResponse = offerCatalogDomain.getEligiblePaymentMethods(purchaseItem, accountNumber, header);
		assertEquals(OfferErrorCodes.LISTING_PAYMENT_METHODS_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetEligiblePaymentMethodsException() throws MarketplaceException {
	    
		when(repositoryHelper.getPaymentMethodsForPurchaseItem(purchaseItem)).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.getEligiblePaymentMethods(purchaseItem, accountNumber, header);
		assertEquals(OfferErrorCodes.LISTING_PAYMENT_METHODS_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetEligiblePaymentMethodsMarketplaceException() throws MarketplaceException {
	    
		when(repositoryHelper.getPaymentMethodsForPurchaseItem(purchaseItem)).thenReturn(purchasePaymentMethod);
		when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		when(fetchServiceValues.getMemberDetails(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(MarketplaceException.class);
		resultResponse = offerCatalogDomain.getEligiblePaymentMethods(purchaseItem, accountNumber, header);
		assertEquals(OfferErrorCodes.LISTING_PAYMENT_METHODS_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
//	/////////////////////////////////////////ELIGIBLE OFFER LIST/////////////////////////////////////////////
//	
//	@Test
//	public void testGetAllEligibleOffersWithAccountNumber() throws MarketplaceException {
//		resultResponse = offerCatalogDomain.getAllEligibleOffers(eligibleOffersRequest, header);
//		assertEquals(OfferErrorCodes.LISTING_OFFERS_MEMBER_FAILED.getId(), resultResponse.getResult().getResponse());
//	
//	}
//	
//	@Test
//	public void testGetAllEligibleOffersWithoutAccountNumber() throws MarketplaceException {
//	    eligibleOffersRequest.setAccountNumber(null);
//		resultResponse = offerCatalogDomain.getAllEligibleOffers(eligibleOffersRequest, header);
//		assertEquals(OfferErrorCodes.LISTING_OFFERS_ELIGIBLE_FAILED.getId(), resultResponse.getResult().getResponse());
//	
//	}
//	
//	@Test
//	public void testGetAllEligibleOffersMarketplaceException() throws MarketplaceException {
//		resultResponse = offerCatalogDomain.getAllEligibleOffers(eligibleOffersRequest, header);
//		Mockito.doReturn(offerList).when(helper).getOfferList(Mockito.any(EligibleOffersRequest.class), Mockito.any(String.class), Mockito.any(ResultResponse.class));
//		Mockito.doThrow(MarketplaceException.class).when(helper).checkMembershipEligbilityForOffers(Mockito.any(EligibilityInfo.class), Mockito.any(String.class), Mockito.any(ResultResponse.class));
//		assertEquals(OfferErrorCodes.LISTING_OFFERS_MEMBER_FAILED.getId(), resultResponse.getResult().getResponse());
//	
//	}
//	
//	@Test
//	public void testGetAllEligibleOffersException() throws MarketplaceException {
//		resultResponse = offerCatalogDomain.getAllEligibleOffers(eligibleOffersRequest, header);
//		Mockito.doThrow(NullPointerException.class).when(helper).getOfferList(Mockito.any(EligibleOffersRequest.class), Mockito.any(String.class), Mockito.any(ResultResponse.class));
//		assertEquals(OfferErrorCodes.LISTING_OFFERS_MEMBER_FAILED.getId(), resultResponse.getResult().getResponse());
//	
//	}

	/////////////////////////////////////////ELIGIBLE OFFER DETAIL/////////////////////////////////////////////
	
	@Test
	public void testGetDetailEligibleOffersWithAccountNumber() throws MarketplaceException {
		
		offerCatalog.setStatus(OfferConstants.ACTIVE_STATUS.get());
		when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		Mockito.doReturn(memberDetails).when(fetchServiceValues).getMemberDetails(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(true).when(helper).getMemberEligibilityForDetailedEligibleOffer(eligibilityInfo, accountNumber, resultResponse);
		Mockito.doReturn(offerCatalogResultResponseDto).when(helper).getOfferResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		resultResponse = offerCatalogDomain.getDetailedEligibleOffer(accountNumber, header, offerId);
		assertEquals(OfferErrorCodes.FETCHING_DETAILED_OFFER_MEMBER_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}

	
	@Test
	public void testGetDetailEligibleOffersSuccessWithoutAccountNumber() throws MarketplaceException {
	    offerCatalog.setStatus(OfferConstants.ACTIVE_STATUS.get());
	    when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
		resultResponse = offerCatalogDomain.getDetailedEligibleOffer(null, header, offerId);
		assertEquals(OfferSuccessCodes.DETAILED_ELIGIBLE_OFFER_DISPLAYED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetDetailEligibleOffersException() throws MarketplaceException {
	    when(repositoryHelper.findByOfferId(offerId)).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.getDetailedEligibleOffer(null, header, offerId);
		assertEquals(OfferErrorCodes.FETCHING_DETAILED_ELIGIBLE_OFFER_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetDetailEligibleOffersMarketplaceException() throws MarketplaceException {
		offerCatalog.setStatus(OfferConstants.ACTIVE_STATUS.get());
	    when(repositoryHelper.findByOfferId(offerId)).thenReturn(offerCatalog);
	    Mockito.doThrow(MarketplaceException.class).when(helper).getOfferResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
	    resultResponse = offerCatalogDomain.getDetailedEligibleOffer(null, header, offerId);
		assertEquals(OfferErrorCodes.FETCHING_DETAILED_ELIGIBLE_OFFER_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
    /////////////////////////////////////////GET ALL MERCHANT CODES FOR OFFER/////////////////////////////////////////////
	
	@Test
	public void testGetAllMerchantCodesForOfferSuccess() throws MarketplaceException {
		
		when(repositoryHelper.getOfferTypeByDescription(Mockito.anyString())).thenReturn(offerType);
		when(repositoryHelper.getOffersByOfferTypeAndMerchant(Mockito.any(), Mockito.any())).thenReturn(offerList);
		List<String> merchantIdList = offerCatalogDomain.getAllMerchantCodesForOffer(offerType.getOfferTypeId());
	    assertNotNull(merchantIdList);	
	}
	
	@Test
	public void testGetAllMerchantCodesForOfferException() throws MarketplaceException {
		
		when(repositoryHelper.getOfferTypeByDescription(Mockito.anyString())).thenThrow(NullPointerException.class);
		List<String> merchantIdList = offerCatalogDomain.getAllMerchantCodesForOffer(offerType.getOfferTypeId());
	    assertNull(merchantIdList);	
	}
	
	
	/////////////////////////////////////////SET OFFER RATING/////////////////////////////////////////////
	
	@Test
	public void testSetOfferRatingSuccess() throws MarketplaceException {
	
	   when(repositoryHelper.saveOffer(offerCatalog)).thenReturn(offerCatalog);	
	   offerCatalogDomain.setOfferRating(offerCatalog, offerRating, header);
	   assertNotNull(offerCatalog);
	   
	}
	
	@Test(expected=MarketplaceException.class)
	public void testSetOfferRatingMongoException() throws MarketplaceException {
	
	    when(repositoryHelper.saveOffer(offerCatalog)).thenThrow(MongoException.class);	
	    offerCatalogDomain.setOfferRating(offerCatalog, offerRating, header);
	}
	
	@Test(expected=MarketplaceException.class)
	public void testSetOfferRatingException() throws MarketplaceException {
	
		when(repositoryHelper.saveOffer(offerCatalog)).thenThrow(NullPointerException.class);	
		offerCatalogDomain.setOfferRating(offerCatalog, offerRating, header);
	
	}
	
	
	/////////////////////////////////////////SEND BIRTHDAY ALERTS/////////////////////////////////////////////
		
	@Test
	public void testSendBirthdayGiftAlertsSuccess() throws MarketplaceException {
	
		BirthdayAccountsDto birthdayAccountsDto = new BirthdayAccountsDto();
		birthdayAccountsDto.setAccountNumber(accountNumber);
		birthdayAccountsDto.setDob(new Date());
		birthdayAccountsDto.setMembershipCode("membershipCode");
		List<BirthdayAccountsDto> birthdayAccountList = new ArrayList<>(1);
		birthdayAccountList.add(birthdayAccountsDto);
		when(fetchServiceValues.getBirthdayAccountDetails(Mockito.any(String.class), Mockito.any(Headers.class))).thenReturn(birthdayAccountList);
		resultResponse = offerCatalogDomain.sendBirthdayGiftAlerts(header);
		assertEquals(OfferSuccessCodes.BATCH_PROCESS_FOR_BIRTHDAY_OFFERS_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testSendBirthdayGiftAlertsMarketplaceException() throws MarketplaceException {
	
		when(fetchServiceValues.getBirthdayAccountDetails(Mockito.any(String.class), Mockito.any(Headers.class))).thenThrow(MarketplaceException.class);
		resultResponse = offerCatalogDomain.sendBirthdayGiftAlerts(header);
		assertEquals(OfferSuccessCodes.BATCH_PROCESS_FOR_BIRTHDAY_OFFERS_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
	}
	
	
	/////////////////////////////////////////GET ALL ELIGIBLE BIRTHDAY OFFERS/////////////////////////////////////////////
		
	@Test
	public void testGetAllEligibleBirthdayOffersSuccess() throws MarketplaceException {
		
		when(repositoryHelper.listBirthdayOffers(header.getChannelId())).thenReturn(offerList);
		Mockito.doReturn(true).when(helper).checkMembershipEligbilityForOffers(Mockito.any(), Mockito.any(), Mockito.any());
		when(repositoryHelper.getBirthdayGiftTrackerForCurrentAccount(accountNumber, eligibilityInfo.getMemberDetails().getMembershipCode()))
        .thenReturn(new BirthdayGiftTracker());		
		resultResponse = offerCatalogDomain.getAllEligibleBirthdayOffers(accountNumber, header);
		assertEquals(OfferErrorCodes.LISTING_BIRTHDAYGIFT_OFFERS_ACCOUNT_FAILED.getId(), resultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetAllEligibleBirthdayOffersMarketplaceException() throws MarketplaceException {
	
		when(repositoryHelper.listBirthdayOffers(header.getChannelId())).thenReturn(offerList);
		Mockito.doThrow(MarketplaceException.class).when(helper).checkMembershipEligbilityForOffers(Mockito.any(), Mockito.any(), Mockito.any());
        resultResponse = offerCatalogDomain.getAllEligibleBirthdayOffers(accountNumber, header);
		assertEquals(OfferErrorCodes.LISTING_BIRTHDAYGIFT_OFFERS_ACCOUNT_FAILED.getId(), resultResponse.getResult().getResponse());
	}

	@Test
	public void testGetAllEligibleBirthdayOffersException() throws MarketplaceException {
	
		when(repositoryHelper.listBirthdayOffers(header.getChannelId())).thenThrow(NullPointerException.class);
		resultResponse = offerCatalogDomain.getAllEligibleBirthdayOffers(accountNumber, header);
		assertEquals(OfferErrorCodes.LISTING_BIRTHDAYGIFT_OFFERS_ACCOUNT_FAILED.getId(), resultResponse.getResult().getResponse());
	}
	
	
}
	
	
		
	
		
		
		
	
	
