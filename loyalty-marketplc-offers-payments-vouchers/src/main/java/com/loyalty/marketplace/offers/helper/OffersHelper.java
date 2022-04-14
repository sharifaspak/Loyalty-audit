package com.loyalty.marketplace.offers.helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.equivalentpoints.inbound.restcontroller.EquivalentPointsController;
import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.gifting.domain.GoldCertificateDomain;
import com.loyalty.marketplace.gifting.helper.dto.GoldCertificateDto;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificate;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.gifting.utils.GetValues;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersListConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CustomerSegmentResult;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.domain.model.BirthdayGiftTrackerDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCounterDomain;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.error.handler.ErrorRecords;
import com.loyalty.marketplace.offers.helper.dto.AmountPoints;
import com.loyalty.marketplace.offers.helper.dto.BirthdayDurationInfoDto;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.EligibleOfferHelperDto;
import com.loyalty.marketplace.offers.helper.dto.ExceptionInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.OfferReferences;
import com.loyalty.marketplace.offers.helper.dto.PromotionalGiftHelper;
import com.loyalty.marketplace.offers.helper.dto.PurchaseCount;
import com.loyalty.marketplace.offers.helper.dto.TimeLimits;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCountDto;
import com.loyalty.marketplace.offers.inbound.dto.PromotionalGiftRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CustomerTypeEntity;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.AddTAndC;
import com.loyalty.marketplace.offers.outbound.database.entity.AdditionalDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.BrandDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.DenominationCount;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOfferList;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffersContextAware;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCount;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDate;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetailMobile;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetailWeb;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferLabel;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTitle;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTitleDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.SubscriptionValues;
import com.loyalty.marketplace.offers.outbound.database.entity.TAndC;
import com.loyalty.marketplace.offers.outbound.database.entity.Tags;
import com.loyalty.marketplace.offers.outbound.database.entity.TermsConditions;
import com.loyalty.marketplace.offers.outbound.database.entity.WhatYouGet;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.dto.EligibleOffersResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogShortResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.TransactionListDto;
import com.loyalty.marketplace.offers.outbound.dto.TransactionsResultResponse;
import com.loyalty.marketplace.offers.outbound.service.SAPPServiceImpl;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.points.bank.inbound.dto.LifeTimeSavingsVouchersEvent;
import com.loyalty.marketplace.offers.promocode.domain.PromoCodeDomain;
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
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.dto.PushNotificationRequestDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApplyAndValidate;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherListResult;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftRequestDto;
import com.mongodb.MongoException;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 
 * @author jaya.shukla
 *
 */
@Component
public class OffersHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(OffersHelper.class);
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	EventHandler eventHandler;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	PurchaseDomain purchaseDomain;
	
	@Autowired
	OfferCounterDomain offerCounterDomain;
	
	@Autowired
	ServiceHelper serviceHelper;
	
	@Autowired
	GoldCertificateDomain goldCertificateDomain;
	
	@Autowired 
	BirthdayGiftTrackerDomain birthdayGiftTrackerDomain;
	
	@Autowired
	PromoCodeDomain promoCodeDomain;
	
	@Autowired
	SAPPServiceImpl sappServiceImpl;
	
	@Autowired
	EquivalentPointsController equivalentPointsController;
	
	@Autowired
	EligibleOffersContextAware eligibleOffersContextAware;
	
	@Autowired
	CounterHelper counterHelper;
	

	@Autowired
	OfferRepository offerRepository;
		
	@Value(OffersConfigurationConstants.OFFER_COUNTER_FLAG_ENABLED)
	private boolean offerCounterFlagEnabled;
	
	@Autowired
	private SubscriptionManagementController subscriptionManagementController;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	EligibleOfferList eligibleOfferList;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	/**
	 * Checks offer request references are valid and then creates activity code for the offer
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @param offerReference
	 * @return if references valid and activity code configured successfully
	 * @throws MarketplaceException
	 * @throws ParseException
	 */
	public boolean validateAndGetActivityCodeForCreate(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse, OfferReferences offerReference) throws MarketplaceException {
		
		List<Integer> denominations = Utilities.convertFromStringToIntegerList(offerCatalogRequest.getDenominations());
		boolean dynamicDenominationForCashVoucher = Checks.checkIsCashVoucher(offerCatalogRequest.getOfferTypeId()) && Checks.checkFlagSet(offerCatalogRequest.getDynamicDenomination());
		List<String> paymentMethodList = ProcessValues.getPaymentMethodList(offerCatalogRequest.getPaymentMethods(), offerCatalogRequest.getAccrualPaymentMethods());
		
		return OfferValidator.validateOfferType(offerReference, repositoryHelper.getOfferType(offerCatalogRequest.getOfferTypeId()), resultResponse)
			&& (!Checks.checkVoucherActionOfferType(offerCatalogRequest.getOfferTypeId())
			|| OfferValidator.validateVoucherAction(repositoryHelper.getVoucherActionById(offerCatalogRequest.getVoucherAction()), resultResponse))	
			&& OfferValidator.validateCategoryAndSubCategory(offerReference, repositoryHelper.getCategoryListByIdList(Arrays.asList(offerCatalogRequest.getCategoryId(), offerCatalogRequest.getSubCategoryId())),
			   offerCatalogRequest.getCategoryId(), offerCatalogRequest.getSubCategoryId(), resultResponse)
			&& OfferValidator.validateMerchant(offerReference, repositoryHelper.getMerchant(offerCatalogRequest.getMerchantCode()), offerCatalogRequest, resultResponse)
			&& OfferValidator.validateStores(offerReference, offerCatalogRequest.getStoreCodes(), repositoryHelper.getStoreList(offerCatalogRequest.getMerchantCode(), offerCatalogRequest.getStoreCodes()), resultResponse)
		    && OfferValidator.validateDenominationsAndDenominationLimit(offerReference, repositoryHelper.getDenominationList(denominations), denominations, offerCatalogRequest.getLimit(), dynamicDenominationForCashVoucher, resultResponse)
		    && (CollectionUtils.isEmpty(paymentMethodList)
		    || OfferValidator.validatePaymentMethods(offerReference, paymentMethodList, repositoryHelper.getPaymentMethodsList(paymentMethodList), resultResponse))
		    && OfferValidator.validateCustomerTypes(fetchServiceValues.getAllCustomerTypes(resultResponse, offerReference.getHeader()), offerCatalogRequest, resultResponse)
		    && configureActivityCode(offerCatalogRequest, resultResponse, offerReference.getHeader());
		    
	}
	
	/**
	 * Configures activity code for the offer
	 * @param offerCatalogRequest
	 * @param resultResponse
	 * @param header
	 * @return if activity code configured successfully
	 * @throws MarketplaceException
	 * @throws ParseException
	 */
	private boolean configureActivityCode(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse, Headers header) throws MarketplaceException{
		
    		offerCatalogRequest.setRedemptionActivityCode(OfferConstants.OFFER_CODE_PREF.get() + offerCatalogRequest.getOfferCode() + OfferConstants.UNDERSCORE_SEPARATOR.get() + OfferConstants.ACTIVITY_REDEMPTION_CODE.get());
    	
		ProgramActivityWithIdDto programActivityDto = null;
		List<ProgramActivityWithIdDto> programActivityList = fetchServiceValues.getAllProgramActivity(header, resultResponse);
		
		if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(programActivityList), OfferErrorCodes.PROGRAM_ACTIVITY_NOT_CONFIGURED, resultResponse)) {
			
			programActivityDto = programActivityList.stream()
                    .filter(p -> (OfferConstants.MARKETPLACE_PROGRAM_ACTIVITY.get()).equalsIgnoreCase(p.getActivityName().getEnglish()))
                    .findAny().orElse(null);
			
			PartnerActivityDto partnerActivityDto = Requests.getPartnerActivityRequest(OfferConstants.ACTIVITY_REDEMPTION.get(), offerCatalogRequest.getRedemptionActivityCode(), offerCatalogRequest.getRedemptionCodeDescriptionEn(), offerCatalogRequest.getRedemptionCodeDescriptionAr(), programActivityDto, OffersConfigurationConstants.ZERO_DOUBLE, offerCatalogRequest.getCustomerTypes());
			String redemptionId = fetchServiceValues.partnerActivity(offerCatalogRequest.getPartnerCode(), partnerActivityDto, resultResponse, header);
			offerCatalogRequest.setRedemptionId(!StringUtils.isEmpty(redemptionId)?redemptionId:null);
						
			if(Checks.checkNoErrors(resultResponse) 
			&& Checks.checkIsCashVoucher(offerCatalogRequest.getOfferTypeId()) ){
						
				if(!ObjectUtils.isEmpty(offerCatalogRequest.getEarnMultiplier()) 
				&& offerCatalogRequest.getEarnMultiplier()>0.0) {
					
					configureAccrualCode(offerCatalogRequest, resultResponse, header, programActivityDto);
				}
				
				if(!ObjectUtils.isEmpty(offerCatalogRequest.getPointsEarnMultiplier()) 
				&& offerCatalogRequest.getPointsEarnMultiplier()>0.0) {
					
					configurePointsAccrualCode(offerCatalogRequest, resultResponse, header, programActivityDto);
				}
			}
			
		
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * Validates offer references in offer request and updates earn multiplier 
	 * @param offerCatalogRequest
	 * @param existingOffer
	 * @param resultResponse
	 * @param offerReference
	 * @return if all the references valid and earn multiplier updated successfully 
	 * @throws MarketplaceException
	 * @throws ParseException 
	 */
	public boolean validateAndUpdateEarnMultiplerForUpdate(OfferCatalogDto offerCatalogRequest, OfferCatalog existingOffer,ResultResponse resultResponse, OfferReferences offerReference, OfferCountDto offerCountDto) throws MarketplaceException{
		
		offerReference.setOfferType(existingOffer.getOfferType());
		List<Integer> denominations = Utilities.convertFromStringToIntegerList(offerCatalogRequest.getDenominations());
		boolean dynamicDenominationForCashVoucher = Checks.checkIsCashVoucher(offerCatalogRequest.getOfferTypeId()) && Checks.checkFlagSet(offerCatalogRequest.getDynamicDenomination());
		
		List<String> paymentMethodIdList = ProcessValues.getPaymentMethodList(offerCatalogRequest.getPaymentMethods(), offerCatalogRequest.getAccrualPaymentMethods());
		ProcessValues.setOfferReferencesForUpdate(offerCatalogRequest, offerReference, existingOffer, resultResponse);
		ProcessValues.setOfferCountFlag(offerCatalogRequest, existingOffer, offerCountDto);
				
		return ((!Utilities.isEqual(offerCatalogRequest.getCategoryId(), existingOffer.getCategory().getCategoryId())
			&& !Utilities.isEqual(offerCatalogRequest.getSubCategoryId(), existingOffer.getSubCategory().getCategoryId()))
			|| OfferValidator.validateCategoryAndSubCategory(offerReference, repositoryHelper.getCategoryListByIdList(Arrays.asList(offerCatalogRequest.getCategoryId(), offerCatalogRequest.getSubCategoryId())),
			   offerCatalogRequest.getCategoryId(), offerCatalogRequest.getSubCategoryId(), resultResponse))
			&& (!Checks.checkVoucherActionOfferType(offerCatalogRequest.getOfferTypeId())
			|| Utilities.isEqual(offerCatalogRequest.getVoucherAction(), existingOffer.getVoucherInfo().getVoucherAction())
			|| OfferValidator.validateVoucherAction(repositoryHelper.getVoucherActionById(offerCatalogRequest.getVoucherAction()), resultResponse))	
			&& (Utilities.isEqual(offerCatalogRequest.getMerchantCode(), existingOffer.getMerchant().getMerchantCode())
			|| OfferValidator.validateMerchant(offerReference, repositoryHelper.getMerchant(offerCatalogRequest.getMerchantCode()), offerCatalogRequest, resultResponse))
			&& (Checks.checkNoDifferentValues(MapValues.mapAllStoreCodes(existingOffer.getOfferStores()), offerCatalogRequest.getStoreCodes())
			|| OfferValidator.validateStores(offerReference, offerCatalogRequest.getStoreCodes(), repositoryHelper.getStoreList(offerCatalogRequest.getMerchantCode(), offerCatalogRequest.getStoreCodes()), resultResponse))
			&& (Checks.checkNoDifferentValues(MapValues.mapAllDirhamValues(existingOffer.getDenominations()), offerCatalogRequest.getDenominations()) 
			|| OfferValidator.validateDenominationsAndDenominationLimit(offerReference, repositoryHelper.getDenominationList(denominations), denominations, offerCatalogRequest.getLimit(), dynamicDenominationForCashVoucher, resultResponse))
			&& (CollectionUtils.isEmpty(paymentMethodIdList)
			|| Checks.checkNoDifferentValues(ProcessValues.getCombinedOfferPaymentIds(existingOffer), paymentMethodIdList)
			|| OfferValidator.validatePaymentMethods(offerReference, paymentMethodIdList, repositoryHelper.getPaymentMethodsList(paymentMethodIdList), resultResponse))
			&& (ObjectUtils.isEmpty(existingOffer.getCustomerTypes())
		    	|| Checks.checkSameCustomerTypesInRequest(offerCatalogRequest, existingOffer)			
		    	|| OfferValidator.validateCustomerTypes(fetchServiceValues.getAllCustomerTypes(resultResponse, offerReference.getHeader()), offerCatalogRequest, resultResponse))
		   	&& updateActivityCode(offerCatalogRequest, existingOffer, resultResponse, offerReference.getHeader());
		
	}
	
	/**
	 * Updates earn multiplier in Member activity earn multiplier updated for cash voucher 
	 * @param offerCatalogRequest
	 * @param existingOffer
	 * @param resultResponse
	 * @param header
	 * @return if earn multiplier updated successfully
	 * @throws MarketplaceException
	 * @throws ParseException 
	 */
	public boolean updateActivityCode(OfferCatalogDto offerCatalogRequest, OfferCatalog existingOffer, 
			ResultResponse resultResponse, Headers header) throws MarketplaceException {
		
	boolean activityStatus = true;
		boolean customerTypeSame = Checks.checkExactlySameEligibleCustomerTypesInRequest(offerCatalogRequest, existingOffer);
		
		if(!customerTypeSame) {
			activityStatus = fetchServiceValues.updatePartnerActivity(offerCatalogRequest.getPartnerCode(), offerCatalogRequest.getRedemptionId(), Requests.getUpdatePartnerActivityRequest(null, offerCatalogRequest.getCustomerTypes()), resultResponse, header);
		}
		
		return activityStatus && getStatusForAccrualCodeUpdate(offerCatalogRequest, existingOffer, customerTypeSame, header, resultResponse); 
					
   }
	
	/**
    * 	
    * @param offerCatalogRequest
    * @param existingOffer
    * @param customerTypeSame
    * @param header
    * @param resultResponse
    * @return status for accrual update
    * @throws MarketplaceException 
    */
   private boolean getStatusForAccrualCodeUpdate(OfferCatalogDto offerCatalogRequest, OfferCatalog existingOffer,
			boolean customerTypeSame, Headers header, ResultResponse resultResponse) throws MarketplaceException {
		
	   boolean isCashVoucher = Checks.checkIsCashVoucher(offerCatalogRequest.getOfferTypeId());
	   boolean activityStatus = true;
	   
	   if(isCashVoucher) {
		   
		   boolean differentEarnMultiplier = !ObjectUtils.isEmpty(offerCatalogRequest.getEarnMultiplier())
				   && !Utilities.isEqual(offerCatalogRequest.getEarnMultiplier(), existingOffer.getEarnMultiplier());
		   boolean accrualCodeExists = Checks.checkAccrualCodeExists(existingOffer);
		   boolean accrualCodeUpdateRequired = accrualCodeExists && (!customerTypeSame
				   || differentEarnMultiplier);
		   boolean accrualCodeCreateRequired = !ObjectUtils.isEmpty(offerCatalogRequest.getEarnMultiplier()) && !accrualCodeExists;
		   
		   boolean differentPointsEarnMultiplier = null!=offerCatalogRequest.getPointsEarnMultiplier()
				   && (null==existingOffer.getAccrualDetails()
				   || (null!=existingOffer.getAccrualDetails().getPointsEarnMultiplier()
				   && !Utilities.isEqual(offerCatalogRequest.getPointsEarnMultiplier(), existingOffer.getAccrualDetails().getPointsEarnMultiplier())));
		   boolean pointsAccrualCodeExists = Checks.checkPointsAccrualCodeExists(existingOffer);
		   boolean pointsAccrualCodeUpdateRequired =  pointsAccrualCodeExists && (!customerTypeSame 
				   || differentPointsEarnMultiplier);
		   boolean pointsAccrualCodeCreateRequired = !ObjectUtils.isEmpty(offerCatalogRequest.getPointsEarnMultiplier()) && !pointsAccrualCodeExists;
		   
		   if(accrualCodeCreateRequired || pointsAccrualCodeCreateRequired) {
			
			 activityStatus = configureAccrualCodes(offerCatalogRequest, accrualCodeCreateRequired, pointsAccrualCodeCreateRequired, resultResponse, header);  
			   
		   }
		   
		   if(activityStatus && accrualCodeUpdateRequired){
			   
				activityStatus = fetchServiceValues.updatePartnerActivity(offerCatalogRequest.getPartnerCode(), offerCatalogRequest.getAccrualId(), Requests.getUpdatePartnerActivityRequest(offerCatalogRequest.getEarnMultiplier(), offerCatalogRequest.getCustomerTypes()), resultResponse, header); 
		   }
		   
		   if(activityStatus && pointsAccrualCodeUpdateRequired){
			   
				activityStatus = fetchServiceValues.updatePartnerActivity(offerCatalogRequest.getPartnerCode(), offerCatalogRequest.getPointsAccrualId(), Requests.getUpdatePartnerActivityRequest(offerCatalogRequest.getPointsEarnMultiplier(), offerCatalogRequest.getCustomerTypes()), resultResponse, header); 
		   }
	   
	   }
	   
	   return activityStatus;
	}
   
   /**
    * 	
    * @param offerCatalogRequest
    * @param resultResponse
    * @param header
    * @return
    * @throws MarketplaceException
    * @throws ParseException
    */
   private boolean configureAccrualCodes(OfferCatalogDto offerCatalogRequest, boolean accrualCodeConfigRequired, boolean pointAccrualCodeConfigRequired, ResultResponse resultResponse, Headers header) throws MarketplaceException{
		
    	ProgramActivityWithIdDto programActivityDto = null;
		List<ProgramActivityWithIdDto> programActivityList = fetchServiceValues.getAllProgramActivity(header, resultResponse);
		
		if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(programActivityList), OfferErrorCodes.PROGRAM_ACTIVITY_NOT_CONFIGURED, resultResponse)) {
			
			programActivityDto = programActivityList.stream()
                    .filter(p -> (OfferConstants.MARKETPLACE_PROGRAM_ACTIVITY.get()).equalsIgnoreCase(p.getActivityName().getEnglish()))
                    .findAny().orElse(null);
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(programActivityDto), OfferErrorCodes.PROGRAM_ACTIVITY_NOT_CONFIGURED, resultResponse)) {
				
				if(accrualCodeConfigRequired) {
					
					configureAccrualCode(offerCatalogRequest, resultResponse, header, programActivityDto);
					
				}
				
				if(pointAccrualCodeConfigRequired) {
					
					configurePointsAccrualCode(offerCatalogRequest, resultResponse, header, programActivityDto);
					
				}
				
			}
			
		}
		
		return Checks.checkNoErrors(resultResponse);
		
		
   }	
	
   /**
    * 
    * @param offerCatalogRequest
    * @param resultResponse
    * @param header
    * @param programActivityDto
    * @throws MarketplaceException
    */
   private void configurePointsAccrualCode(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse,
		Headers header, ProgramActivityWithIdDto programActivityDto) throws MarketplaceException {
	
	   	offerCatalogRequest.setPointsAccrualActivityCode(OfferConstants.OFFER_CODE_PREF.get() + offerCatalogRequest.getOfferCode()+ OfferConstants.UNDERSCORE_SEPARATOR.get() + OfferConstants.ACTIVITY_ACCRUAL_CODE_FOR_POINTS.get());
	   	PartnerActivityDto partnerActivityDto = Requests.getPartnerActivityRequest(OfferConstants.ACTIVITY_ACCRUAL.get(), offerCatalogRequest.getPointsAccrualActivityCode(), offerCatalogRequest.getPointsAccrualCodeDescriptionEn(), offerCatalogRequest.getPointsAccrualCodeDescriptionAr(), programActivityDto, offerCatalogRequest.getPointsEarnMultiplier(), offerCatalogRequest.getCustomerTypes());
	   	String accrualId = fetchServiceValues.partnerActivity(offerCatalogRequest.getPartnerCode(), partnerActivityDto, resultResponse, header);
		offerCatalogRequest.setPointsAccrualId(!StringUtils.isEmpty(accrualId)?accrualId:null);
	
   }
   
   /**
    * 
    * @param offerCatalogRequest
    * @param resultResponse
    * @param header
    * @param programActivityDto
    * @throws MarketplaceException
    */
   private void configureAccrualCode(OfferCatalogDto offerCatalogRequest, ResultResponse resultResponse, Headers header, ProgramActivityWithIdDto programActivityDto) throws MarketplaceException {
	
	   offerCatalogRequest.setAccrualActivityCode(OfferConstants.OFFER_CODE_PREF.get() + offerCatalogRequest.getOfferCode()+ OfferConstants.UNDERSCORE_SEPARATOR.get() + OfferConstants.ACTIVITY_ACCRUAL_CODE.get());
	   PartnerActivityDto partnerActivityDto  = Requests.getPartnerActivityRequest(OfferConstants.ACTIVITY_ACCRUAL.get(), offerCatalogRequest.getAccrualActivityCode(), offerCatalogRequest.getAccrualCodeDescriptionEn(), offerCatalogRequest.getAccrualCodeDescriptionAr(), programActivityDto, offerCatalogRequest.getEarnMultiplier(), offerCatalogRequest.getCustomerTypes());
	   String accrualId = fetchServiceValues.partnerActivity(offerCatalogRequest.getPartnerCode(), partnerActivityDto, resultResponse, header);
	   offerCatalogRequest.setAccrualId(!StringUtils.isEmpty(accrualId)?accrualId:null);
	
   }
	
   /**
    * Fetches the applicable conversion rate list for that product type and partner code combination	
    * @param offerCatalogList
    * @return list of applicable conversion rate
    */
   public List<ConversionRate> getConversionRateListForOfferList(List<OfferCatalog> offerCatalogList, String channelId){
	   
	   List<ConversionRate> conversionRateList = null;
	   
	   if(!CollectionUtils.isEmpty(offerCatalogList)) {
		   
		   List<String> partnerCodeList = MapValues.mapPartnerCodesFromOfferList(offerCatalogList, Predicates.notEmptyPartnerCode());	 
		   List<String> offerTypeList = MapValues.mapOfferTypeIdFromOfferList(offerCatalogList, Predicates.notEmptyOfferTypeId());
		   List<String> productTypeList = ProcessValues.getProductTypeList(offerTypeList);
		   conversionRateList = repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(partnerCodeList, productTypeList);
		   conversionRateList = FilterValues.filterConversionRateList(conversionRateList, Predicates.applicableRateForPartnerListAndChannelId(partnerCodeList, channelId));
	   }
	   
	   return conversionRateList;
	   
   }
      
   /**
    * Loyalty as a service.
    * Fetches the applicable conversion rate list for that product type and partner code combination	
    * @param offerCatalogList
    * @return list of applicable conversion rate
    */
   public List<ConversionRate> getConversionRateListForOfferListByProgramCode(List<OfferCatalog> offerCatalogList, String channelId, Headers header){
	   
	   //Loyalty as a service.
	   List<ConversionRate> conversionRateList = null;
	   
	   if(!CollectionUtils.isEmpty(offerCatalogList)) {
		   
		   List<String> partnerCodeList = MapValues.mapPartnerCodesFromOfferList(offerCatalogList, Predicates.notEmptyPartnerCode());	 
		   List<String> offerTypeList = MapValues.mapOfferTypeIdFromOfferList(offerCatalogList, Predicates.notEmptyOfferTypeId());
		   List<String> productTypeList = ProcessValues.getProductTypeList(offerTypeList);
		   //Loyalty as a service.
		   conversionRateList = repositoryHelper.findConversionRateListByPartnerCodeAndProductItemAndProgramCode(partnerCodeList, productTypeList, header);
		   conversionRateList = FilterValues.filterConversionRateList(conversionRateList, Predicates.applicableRateForPartnerListAndChannelId(partnerCodeList, channelId));
	   }
	   
	   return conversionRateList;
	   
   }
   
   /**
    * Fetches the applicable conversion rate list for that product type and partner code combination	
    * @param offerCatalogList
    * @return list of applicable conversion rate
    */
   public List<ConversionRate> getConversionRateListForBillPaymentRechargeRecords(List<PurchaseHistory> purchaseRecords, String channelId){
	   
	   List<ConversionRate> conversionRateList = null;
	   
	   if(!CollectionUtils.isEmpty(purchaseRecords)) {
		   
		   List<String> partnerCodeList = MapValues.mapAllPartnerCodesFromPurchaseHistory(purchaseRecords);	 
		   List<String> offerTypeList = Arrays.asList(OffersConfigurationConstants.billPaymentItem, OffersConfigurationConstants.rechargeItem); 
		   List<String> productTypeList = ProcessValues.getProductTypeList(offerTypeList);
		   conversionRateList = repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(partnerCodeList, productTypeList);
		   conversionRateList = !CollectionUtils.isEmpty(FilterValues.filterConversionRateList(conversionRateList, Predicates.sameChannelId(channelId)))
			   ? FilterValues.filterConversionRateList(conversionRateList, Predicates.sameChannelId(channelId))
			   : FilterValues.filterConversionRateList(conversionRateList, Predicates.noChannelSet());
		   
	   }
	   
	   return conversionRateList;
	   
   }
   
   /**
    * Fetches list of purchase payment methods for list of offers
    * @param offerCatalogList
    * @return list of purchase payment methods
    */
   public List<PurchasePaymentMethod> getPurchasePaymentMethodForOfferList(List<OfferCatalog> offerCatalogList){
	   
	   List<PurchasePaymentMethod> purchasePaymentMethodList = null;
	   
	   if(!CollectionUtils.isEmpty(offerCatalogList)) {
		   
		   List<String> offerTypeList = MapValues.mapOfferTypeIdFromOfferList(offerCatalogList, Predicates.notEmptyOfferTypeId());
		   List<String> purchaseItemList = ProcessValues.getPurchaseItemListFromOfferTypeList(offerTypeList);
		   purchasePaymentMethodList = repositoryHelper.getPurchasePaymentMethodsByPurchaseItemList(purchaseItemList);
	   }
	   
	   return purchasePaymentMethodList;
	   
   }
   
   /**
    * Loyalty as a service.
    * Fetches list of purchase payment methods for list of offers
    * @param offerCatalogList
    * @return list of purchase payment methods
    */
   public List<PurchasePaymentMethod> getPurchasePaymentMethodForOfferListByProgramCode(List<OfferCatalog> offerCatalogList, Headers header){
	   
	   //Loyalty as a service.
	   List<PurchasePaymentMethod> purchasePaymentMethodList = null;
	   
	   if(!CollectionUtils.isEmpty(offerCatalogList)) {
		   
		   List<String> offerTypeList = MapValues.mapOfferTypeIdFromOfferList(offerCatalogList, Predicates.notEmptyOfferTypeId());
		   List<String> purchaseItemList = ProcessValues.getPurchaseItemListFromOfferTypeList(offerTypeList);
		   purchasePaymentMethodList = repositoryHelper.getPurchasePaymentMethodsByPurchaseItemListByProgramCode(purchaseItemList, header);
	   }
	   
	   return purchasePaymentMethodList;
	   
   }
   
   /**
    * Fetches list of image urls for list of offers
    * @param offerCatalogList
    * @return list of image urls
    */
   public List<MarketplaceImage> getImageUrlListForOfferList(List<OfferCatalog> offerCatalogList){
	   
	   return !CollectionUtils.isEmpty(offerCatalogList)
			? repositoryHelper.getImageForOfferList(MapValues.mapOfferIdFromOfferList(offerCatalogList))
			: null;
	   
   }
   
   /**
    * Loyalty as a service.
    * Fetches list of image urls for list of offers
    * @param offerCatalogList
    * @return list of image urls
    */
   public List<MarketplaceImage> getImageUrlListForOfferListByProgramCode(List<OfferCatalog> offerCatalogList, Headers header){
	   
	   //Loyalty as a service.
	   return !CollectionUtils.isEmpty(offerCatalogList)
			? repositoryHelper.getImageForOfferListByProgramCode(MapValues.mapOfferIdFromOfferList(offerCatalogList), header)
			: null;
	   
   }
   
   /**
   	 * Maps OfferCatalog entity to OfferCatalogResultResponseDto
   	 * @param offerCatalog
   	 * @param purchasePaymentMethodList
   	 * @param conversionRateList
   	 * @param imageList
   	 * @return mapped response from entity
   	 * @throws MarketplaceException
   	 */
	public OfferCatalogResultResponseDto getOfferResponse(OfferCatalog offerCatalog, List<PurchasePaymentMethod> purchasePaymentMethodList, List<ConversionRate> conversionRateList, List<MarketplaceImage> imageList, boolean includeOfferPaymentMethods) throws MarketplaceException {
		
		OfferCatalogResultResponseDto offerCatalogDto = null;
    	
    	try {
    		
    		if(!ObjectUtils.isEmpty(offerCatalog)) {
    			
	    		 offerCatalogDto = modelMapper.map(offerCatalog, OfferCatalogResultResponseDto.class);
	    		 String purchaseItem = ProcessValues.getPurchaseItemFromOfferType(offerCatalog.getOfferType().getOfferTypeId());
	    		 PurchasePaymentMethod purchasePaymentMethod = !CollectionUtils.isEmpty(purchasePaymentMethodList) 
	       				 ? FilterValues.findAnyPurchasePaymentMethodWithinList(purchasePaymentMethodList, Predicates.matchesPurchaseItem(purchaseItem))
	       				 : null;
	       		 List<PaymentMethod> paymentMethods = !ObjectUtils.isEmpty(purchasePaymentMethod)
	       				 ? ProcessValues.getPaymentMethodsForPurchases(purchasePaymentMethod, offerCatalog.getPaymentMethods(), includeOfferPaymentMethods)
	       				 : null;
	       		 List<MarketplaceImage> filteredImages = !CollectionUtils.isEmpty(imageList)
	       				? FilterValues.filterImageList(imageList, Predicates.sameDomainId(offerCatalog.getOfferId()))
	       				: null;		 
	       		 ProcessValues.setUnmappedOfferValues(offerCatalogDto, offerCatalog, paymentMethods, conversionRateList, filteredImages);
    		 	
    		}
    		
			
		} catch (MongoException mongoException) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_OFFER_RESPONSE_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_OFFER_RESPONSE_METHOD.get(),
					e.getClass() + e.getMessage(),
					OfferExceptionCodes.OFFER_RESPONSE_RUNTIME_EXCEPTION);
			
		}
		
		return offerCatalogDto;
	}
	
	/**
	 * List of conversion rate for partner code in input and product item
	 * @param partnerCode
	 * @param item
	 * @param resultResponse
	 * @return
	 */
	public List<ConversionRate> fetchConversionRateList(String partnerCode, String item, String channelId, ResultResponse resultResponse) {
		
		List<String> productTypeList = Arrays.asList(ProcessValues.getProductTypeValue(item));		 
		List<ConversionRate> conversionRateList = 
				repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(Arrays.asList(partnerCode, OfferConstants.SMILES.get()), 
						productTypeList);
		
		if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(conversionRateList), OfferErrorCodes.RATE_NOT_SET_FOR_PARTNER, resultResponse)) {
			
			conversionRateList = FilterValues.filterConversionRateList(conversionRateList, Predicates.applicableRateForPartnerListAndChannelId(Arrays.asList(partnerCode), channelId));
		}
		
		return conversionRateList;
	}
	
	/**
	 * Maps eligible offers entity to response list
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 * @param isEligibilityRequired
	 * @return list of eligible offers response
	 * @throws MarketplaceException
	 */
	public List<OfferCatalogResultResponseDto> getEligibleOfferList(EligibilityInfo eligibilityInfo,
			ResultResponse offerCatalogResultResponse, boolean isEligibilityRequired, boolean subscriptionCheckRequired) throws MarketplaceException {
		
	  List<OfferCatalogResultResponseDto> offerCatalogList = null;
	
	  if(!ObjectUtils.isEmpty(eligibilityInfo) 
	  && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
		 
		  offerCatalogList = new ArrayList<>(eligibilityInfo.getOfferList().size());
		  List<ConversionRate> conversionRateList = getConversionRateListForOfferList(eligibilityInfo.getOfferList(), eligibilityInfo.getHeaders().getChannelId());
		  List<PurchasePaymentMethod> purchasePaymentMethodList = getPurchasePaymentMethodForOfferList(eligibilityInfo.getOfferList());
		  List<MarketplaceImage> imageList = getImageUrlListForOfferList(eligibilityInfo.getOfferList());	
		  PurchaseCount purchaseCount = ProcessValues.getPurchaseCountForCinemaOffers(eligibilityInfo);
		  
		  for(OfferCatalog offerCatalog : eligibilityInfo.getOfferList()) {
			  
			  //true for including offer level payment methods for selecting eligible payment methods
			  OfferCatalogResultResponseDto offerCatalogDto = getOfferResponse(offerCatalog, purchasePaymentMethodList, conversionRateList, imageList, true);
			  ProcessValues.setFreeOfferResponse(offerCatalogDto, eligibilityInfo, subscriptionCheckRequired, conversionRateList);
			  if(eligibilityInfo.isMember()) {
				 
				  eligibilityInfo.setOffer(offerCatalog);
				  eligibilityInfo.setCommonSegmentNames(Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						  MapValues.mapCustomerSegmentInOfferLimits(offerCatalog.getLimit(), Predicates.isCustomerSegmentInLimits())));
				  eligibilityInfo.setOfferCounters(FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offerCatalog.getOfferId())));
				  ProcessValues.setMemberAttributesInOffer(offerCatalogDto, offerCatalog, eligibilityInfo);
				  ProcessValues.setExtraInformation(offerCatalog, offerCatalogDto, eligibilityInfo, isEligibilityRequired, purchaseCount, offerCatalogResultResponse);
			  } 
			
			  offerCatalogList.add(offerCatalogDto);
			  
		}
		  
	  }
	  
      return offerCatalogList;
		
	}
	
	/**
	 * 
	 * @param purchaseList
	 * @return list of payment methods filtered from purchase history records
	 */
	public List<PaymentMethod> getTransactionListPaymentMethods(List<PurchaseHistory> purchaseList) {
		
		Set<String> paymentMethodNames = MapValues.mapAllPaymentMethodsFromPurchaseList(purchaseList);
		return repositoryHelper.findAllPaymentMethodsInSpecifiedList(Utilities.convertSetToList(paymentMethodNames));
    }
	
	/**
	 * 
	 * @param offerTypeId
	 * @param offerPaymentList 
	 * @return list of payment methods for input offer type
	 */
	public List<PaymentMethod> getPaymentMethods(String offerTypeId, List<PaymentMethod> offerPaymentList, boolean includeOfferPaymentMethods) {
      
	   String discountCashVoucher = Checks.checkIsDiscountVoucher(offerTypeId)
               ? OffersConfigurationConstants.discountVoucherItem 
               : OffersConfigurationConstants.cashVoucherItem;
       String dealEtisalatVoucher = Checks.checkIsDealVoucher(offerTypeId)
               ? OffersConfigurationConstants.dealVoucherItem 
               : OffersConfigurationConstants.addOnItem;  
       String goldTelecomVoucher = Checks.checkIsGoldCertificate(offerTypeId)
               ? OffersConfigurationConstants.goldCertificateItem 
               : OffersConfigurationConstants.billPaymentItem;
       String offerType = (Checks.checkIsDiscountVoucher(offerTypeId)
    		       || Checks.checkIsCashVoucher(offerTypeId))
                   ?  discountCashVoucher : dealEtisalatVoucher;
       String type = (Checks.checkIsGoldCertificate(offerTypeId)
		       || Checks.checkIsTelcomType(offerTypeId))
    		   ? goldTelecomVoucher : offerType;
       PurchasePaymentMethod purchasePaymentMethod = repositoryHelper.getPaymentMethodsForPurchaseItem(type);
       LOG.info("Offer type payment methods from offer : {}", purchasePaymentMethod);	
       LOG.info("offerPaymentList : {}", offerPaymentList);	
       //Changes for payment method introduced at offer level.
       return ProcessValues.getPaymentMethodsForPurchases(purchasePaymentMethod, offerPaymentList, includeOfferPaymentMethods);
      //return null!=purchasePaymentMethod? purchasePaymentMethod.getPaymentMethods() : null;
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 * @return status after validating whether all offer details are valid to make the purchase
	 */
	public boolean validateOfferDetails(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest,
			ResultResponse purchaseResultResponse) {
		
		if(Checks.checkOfferDetails(eligibilityInfo, purchaseRequest, purchaseResultResponse)) {
			
		   Integer points = ProcessValues.getOfferPointValue(purchaseRequest, eligibilityInfo.getOffer(), eligibilityInfo.getSubOffer(), eligibilityInfo.getDenomination());
		   List<ConversionRate> conversionRateList = null;
		   
		   if(Checks.checkPartialPaymentPaymentMethod(purchaseRequest.getSelectedOption()) 
		   || Checks.checkIsGoldCertificate(purchaseRequest.getSelectedPaymentItem())
		   ||(Checks.checkPaymentMethodFullPoints(purchaseRequest.getSelectedOption())
			&& ObjectUtils.isEmpty(points))) {
				
			   conversionRateList = fetchConversionRateList(eligibilityInfo.getOffer().getPartnerCode(), purchaseRequest.getSelectedPaymentItem(), eligibilityInfo.getHeaders().getChannelId(), purchaseResultResponse);
			   
		   }	   
				     	
		   if(Checks.checkNoErrors(purchaseResultResponse)) {
			   
			   eligibilityInfo.setAmountInfo(ProcessValues.setAmountInfoValues(purchaseRequest, eligibilityInfo, conversionRateList, points, purchaseResultResponse));
			   
			   if(Checks.checkNoErrors(purchaseResultResponse)) {
				   
				   eligibilityInfo.getAdditionalDetails().setFree(Checks.checkOfferFree(eligibilityInfo.getAmountInfo()));
				   resetSpecificOfferCounter(eligibilityInfo);
			   }
			   
		   }
		   
		}
		
		return Checks.checkNoErrors(purchaseResultResponse);
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 * @return status after validating whether all offer details are valid to make the purchase
	 */
	public boolean validateOfferSpecificDetails(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest,
			ResultResponse purchaseResultResponse) {
		
		if(Checks.checkOfferDetails(eligibilityInfo, purchaseRequest, purchaseResultResponse)) {
			
		   Integer points = ProcessValues.getOfferPointValue(purchaseRequest, eligibilityInfo.getOffer(), eligibilityInfo.getSubOffer(), eligibilityInfo.getDenomination());
		   
		   if(Checks.checkPartialPaymentPaymentMethod(purchaseRequest.getSelectedOption()) 
		   || Checks.checkIsGoldCertificate(purchaseRequest.getSelectedPaymentItem())
		   || Checks.checkPaymentMethodFullPoints(purchaseRequest.getSelectedOption())) {
				
			   eligibilityInfo.setConversionRateList(fetchConversionRateList(eligibilityInfo.getOffer().getPartnerCode(), purchaseRequest.getSelectedPaymentItem(), eligibilityInfo.getHeaders().getChannelId(), purchaseResultResponse));
		   }	   
				     	
		   if(Checks.checkNoErrors(purchaseResultResponse)) {
			   
			   eligibilityInfo.setAmountInfo(ProcessValues.setAmountInfoValues(purchaseRequest, eligibilityInfo, eligibilityInfo.getConversionRateList(), points, purchaseResultResponse));
			   LOG.info("AmountInfo : {}", eligibilityInfo.getAmountInfo());
			   
			   if(Checks.checkNoErrors(purchaseResultResponse)) {
				   
				   eligibilityInfo.getAdditionalDetails().setFree(Checks.checkOfferFree(eligibilityInfo.getAmountInfo()));
				  
			   }
			   
		   }
		   
		}
		
		return Checks.checkNoErrors(purchaseResultResponse);
	}
	
	/**
	 * Resets the limits of counter for current offer being purchase if not already reset in required time
	 * @param eligibilityInfo 
	 * 
	 */
	private void resetSpecificOfferCounter(EligibilityInfo eligibilityInfo) {
		
		OfferCounter offerCounter = repositoryHelper.findCounterForCurrentOffer(eligibilityInfo.getOffer().getOfferId());
		List<ErrorRecords> errorRecordList = repositoryHelper.findErrorRecordForOffer(eligibilityInfo.getOffer().getOfferId());
		ProcessValues.checkErrorRecordAndSetCounter(errorRecordList, offerCounter);
		eligibilityInfo.setOfferCounters(offerCounter);
		eligibilityInfo.setErrorRecordsList(errorRecordList);
		
		if(!ObjectUtils.isEmpty(offerCounter)) {
			   
		   List<OfferCounter> offerCounterList = Arrays.asList(offerCounter);
		   resetOfferCounter(offerCounterList, OffersRequestMappingConstants.PURCHASE, eligibilityInfo.getHeaders());
		   	   
		 }
		
	}

	/**
	 * Apply promotion code if promotion code received in input and change the price of product according to that
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseResultResponse
	 */
	public void applyPromoCode(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo, ResultResponse purchaseResultResponse) {
		
		if(!StringUtils.isEmpty(purchaseRequest.getPromoCode())) {
			
			PromoCodeApplyAndValidate promoCodeApply = promoCodeDomain.applyPromoCode(purchaseRequest.getPromoCode(), null, purchaseRequest.getOfferId(), eligibilityInfo.getAmountInfo().getOfferCost(),
	                eligibilityInfo.getAmountInfo().getOfferPoints(), purchaseRequest.getAccountNumber(), purchaseRequest.getVoucherDenomination(), eligibilityInfo.getHeaders(), eligibilityInfo.getMemberDetails(), purchaseResultResponse);
	        String log = Logs.logForVariable(OfferConstants.PROMO_CODE_VARIABLE.get(), promoCodeApply.getPromoCodeApply());
	        LOG.info(log);
	        
			if(Checks.checkNoErrors(purchaseResultResponse)
			&& !ObjectUtils.isEmpty(promoCodeApply)) {
				
				ProcessValues.setAmountInfoAfterApplyingPromoCode(promoCodeApply.getPromoCodeApply(),eligibilityInfo, purchaseRequest);
				
			}
			
		}

	}

	/**
	 * Reset different limits for a list of offer counters
	 * @param offerCounterList
	 * @param api
	 * @param headers
	 */
	public void resetOfferCounter(List<OfferCounter> offerCounterList, String api, Headers headers) {
		
		if(!CollectionUtils.isEmpty(offerCounterList)) {
			
			List<OfferCounter> resetList = getResetOfferCounterList(offerCounterList);
			List<OfferCounter> savedList =  repositoryHelper.saveAllOfferCounters(resetList);
			
			if(!CollectionUtils.isEmpty(savedList)) {
				
				auditService.updateDataAudit(api, savedList, OffersDBConstants.OFFER_COUNTER, offerCounterList,  headers.getExternalTransactionId(), headers.getUserName());
			}
		}
		
	}
	
	/**
 	 *  
 	 * @param offerCounterList
 	 * @return list of offer counters after modifying limits
 	 */
	public List<OfferCounter> getResetOfferCounterList(List<OfferCounter> offerCounterList) {
		
		List<OfferCounter> offerCounterListModified =  new ArrayList<>(offerCounterList.size());
		if(!offerCounterList.isEmpty()) {
			for(OfferCounter offerCounter : offerCounterList) {
			
				TimeLimits timeLimit = modelMapper.map(offerCounter, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				offerCounter.setId(offerCounter.getId());
				offerCounter.setRules(offerCounter.getRules());
				offerCounter.setDailyCount(timeLimit.getDailyCount());
				offerCounter.setWeeklyCount(timeLimit.getWeeklyCount());
				offerCounter.setMonthlyCount(timeLimit.getMonthlyCount());
				offerCounter.setAnnualCount(timeLimit.getAnnualCount());
				
				offerCounter.setLastPurchased(new Date());
				 
				if(!CollectionUtils.isEmpty(offerCounter.getDenominationCount())) {
					
					offerCounter.setDenominationCount(getResetDenominationCounterList(offerCounter.getDenominationCount()));
				}
				
				offerCounter.setAccountOfferCount(getResetAccountOfferCountList(offerCounter.getAccountOfferCount()));
				offerCounter.setMemberOfferCount(getResetMemberOfferCountList(offerCounter.getMemberOfferCount()));
				offerCounterListModified.add(offerCounter);
			}
		
		}
		
		return offerCounterListModified;
	}
	
	/**
	 * 
	 * @param accountOfferCountList
	 * @return list of account offer counts after modifying limits
	 */
	private List<AccountOfferCount> getResetAccountOfferCountList(List<AccountOfferCount> accountOfferCountList) {
		
		List<AccountOfferCount> accountOfferCountListModified =  null;
		if(!CollectionUtils.isEmpty(accountOfferCountList)) {
			
			accountOfferCountListModified =  new ArrayList<>(accountOfferCountList.size());
			
			for(AccountOfferCount accountOffer : accountOfferCountList) {
			
				TimeLimits timeLimit = modelMapper.map(accountOffer, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				accountOffer.setDailyCount(timeLimit.getDailyCount());
				accountOffer.setWeeklyCount(timeLimit.getWeeklyCount());
				accountOffer.setMonthlyCount(timeLimit.getMonthlyCount());
				accountOffer.setAnnualCount(timeLimit.getAnnualCount());
				accountOffer.setLastPurchased(new Date());
			
				if(!CollectionUtils.isEmpty(accountOffer.getDenominationCount())) {
					
					accountOffer.setDenominationCount(getResetDenominationCounterList(accountOffer.getDenominationCount()));
				}
				accountOfferCountListModified.add(accountOffer);
			}
		
		}
		
		return accountOfferCountListModified;
		
	}
	
	/**
	 * 
	 * @param memberOfferCountList
	 * @return list of member offer counts after modifying limits
	 */
	private List<MemberOfferCount> getResetMemberOfferCountList(List<MemberOfferCount> memberOfferCountList) {
		
		List<MemberOfferCount> memberOfferCountListModified =  null;

		if(!CollectionUtils.isEmpty(memberOfferCountList)) {
			
			memberOfferCountListModified =  new ArrayList<>(memberOfferCountList.size());
			
			for(MemberOfferCount memberOffer : memberOfferCountList) {
			
				TimeLimits timeLimit = modelMapper.map(memberOffer, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				memberOffer.setDailyCount(timeLimit.getDailyCount());
				memberOffer.setWeeklyCount(timeLimit.getWeeklyCount());
				memberOffer.setMonthlyCount(timeLimit.getMonthlyCount());
				memberOffer.setAnnualCount(timeLimit.getAnnualCount());
				memberOffer.setLastPurchased(new Date());
			
				if(!CollectionUtils.isEmpty(memberOffer.getDenominationCount())) {
					
					memberOffer.setDenominationCount(getResetDenominationCounterList(memberOffer.getDenominationCount()));
				}
				memberOfferCountListModified.add(memberOffer);
			}
		
		}
		
		return memberOfferCountListModified;
		
	}
	
	/**
	 * 
	 * @param denominationCountList
	 * @return list of denomination count after modifying limits
	 */
	private List<DenominationCount> getResetDenominationCounterList(List<DenominationCount> denominationCountList){
		
		List<DenominationCount> denominationCountListModified = null;
		
		if(!CollectionUtils.isEmpty(denominationCountList)) {
			
			denominationCountListModified = new ArrayList<>(denominationCountList.size());
			
			for(DenominationCount denominationCount : denominationCountList) {
				
				TimeLimits timeLimitDenom = modelMapper.map(denominationCount, TimeLimits.class);
				ProcessValues.resetCounter(timeLimitDenom);
				denominationCount.setDailyCount(timeLimitDenom.getDailyCount());
				denominationCount.setWeeklyCount(timeLimitDenom.getWeeklyCount());
				denominationCount.setMonthlyCount(timeLimitDenom.getMonthlyCount());
				denominationCount.setAnnualCount(timeLimitDenom.getAnnualCount());
				denominationCount.setLastPurchased(new Date());
				denominationCountListModified.add(denominationCount);
	       }
			
			
		}
		
		return denominationCountListModified;
	}
	
	/**
	 * Validates member eligibility for purchase flow
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseResultResponse
	 * @throws MarketplaceException
	 */
	public void purchaseItem(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo,
			PurchaseResultResponse purchaseResultResponse) throws MarketplaceException {
		
		if(Checks.checkNoErrors(purchaseResultResponse)
        && Checks.checkCustomerTypeEligibility(eligibilityInfo.getOffer(), eligibilityInfo.getMemberDetails(), purchaseResultResponse)) {
		        	
			if(StringUtils.equalsIgnoreCase(eligibilityInfo.getOffer().getIsBirthdayGift(), OfferConstants.FLAG_SET.get())) {
			    
				BirthdayDurationInfoDto birthdayDurationInfoDto = ProcessValues.getBirthdayDurationInfoDto(repositoryHelper.findBirthdayInfo(), eligibilityInfo.getMemberDetails());
				eligibilityInfo.setPurchaseHistoryList(repositoryHelper.findBirthdayBillPaymentAndRechargePurchaseRecords(eligibilityInfo.getMemberDetails().getAccountNumber(), 
						eligibilityInfo.getMemberDetails().getMembershipCode(), birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate()));
				
				Checks.checkBirthdayEligibility(birthdayDurationInfoDto,
						repositoryHelper.getBirthdayGiftTrackerForCurrentAccount(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode()),
						purchaseResultResponse, eligibilityInfo.getPurchaseHistoryList(), 
						eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode());
			}
			
			if(Checks.checkNoErrors(purchaseResultResponse)) {
				
				setCustomerSegmentDetails(eligibilityInfo, eligibilityInfo.getMemberDetails().getAccountNumber(), purchaseResultResponse);
				
				if(Checks.checkNoErrors(purchaseResultResponse)
				&& (ObjectUtils.isEmpty(eligibilityInfo.getOffer().getCustomerSegments())
				|| Checks.checkCustomerSegment(eligibilityInfo.getRuleResult(), eligibilityInfo.getOffer().getCustomerSegments(), eligibilityInfo.getMemberDetails().getCustomerSegment(), purchaseResultResponse))) {
					
					checkDownloadLimit(eligibilityInfo, purchaseRequest, purchaseResultResponse);
					
					if(Checks.checkNoErrors(purchaseResultResponse)) {
			    		
			    		PurchaseHistory purchaseDetails = makePaymentAndGetResponse(eligibilityInfo, purchaseRequest,purchaseResultResponse,null);
			        	performAdditionalOperation(purchaseRequest, eligibilityInfo, purchaseDetails, purchaseResultResponse);		
			    		
			    	}
					
				}
				
			}
        			
		        		
		}
		
	}
	
	/**
	 * Validates member eligibility for purchase flow
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseResultResponse
	 * @throws Exception 
	 */
	public void purchaseMarketplaceItem(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo,
			PurchaseResultResponse purchaseResultResponse, Headers headers) throws MarketplaceException {
		
		if(Checks.checkNoErrors(purchaseResultResponse)
        && Checks.checkCustomerTypeEligibility(eligibilityInfo.getOffer(), eligibilityInfo.getMemberDetails(), purchaseResultResponse)) {
		        	
			if(StringUtils.equalsIgnoreCase(eligibilityInfo.getOffer().getIsBirthdayGift(), OfferConstants.FLAG_SET.get())) {
			    
				BirthdayDurationInfoDto birthdayDurationInfoDto = ProcessValues.getBirthdayDurationInfoDto(repositoryHelper.findBirthdayInfo(), eligibilityInfo.getMemberDetails());
				eligibilityInfo.setPurchaseHistoryList(repositoryHelper.findBirthdayBillPaymentAndRechargePurchaseRecords(eligibilityInfo.getMemberDetails().getAccountNumber(), 
						eligibilityInfo.getMemberDetails().getMembershipCode(), birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate()));
				
				Checks.checkBirthdayEligibility(birthdayDurationInfoDto,
						repositoryHelper.getBirthdayGiftTrackerForCurrentAccount(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode()),
						purchaseResultResponse, eligibilityInfo.getPurchaseHistoryList(), 
						eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode());
			}
			
			if(Checks.checkNoErrors(purchaseResultResponse)) {
				
				setCustomerSegmentDetails(eligibilityInfo, eligibilityInfo.getMemberDetails().getAccountNumber(), purchaseResultResponse);
				
				if(Checks.checkNoErrors(purchaseResultResponse)
				&& (ObjectUtils.isEmpty(eligibilityInfo.getOffer().getCustomerSegments())
				|| Checks.checkCustomerSegment(eligibilityInfo.getRuleResult(), eligibilityInfo.getOffer().getCustomerSegments(), eligibilityInfo.getMemberDetails().getCustomerSegment(), purchaseResultResponse))) {
					
					checkNewDownloadLimit(eligibilityInfo, purchaseRequest, purchaseResultResponse);
					
					if(Checks.checkNoErrors(purchaseResultResponse)) {
			    		
			    		PurchaseHistory purchaseDetails = makePaymentAndGetResponse(eligibilityInfo, purchaseRequest,purchaseResultResponse, headers);
			    		performAdditionalOperationsForPurchase(purchaseRequest, eligibilityInfo, purchaseDetails, purchaseResultResponse);		
			    		
			    	}
					
				}
				
			}
        			
		        		
		}
		
	}
	
	/***
	 * Check download limit before purchasing an item 
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 */
	private void checkNewDownloadLimit(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest,
			PurchaseResultResponse purchaseResultResponse) {
		
		counterHelper.resetSpecificOfferCounters(eligibilityInfo);
		if(!ObjectUtils.isEmpty(eligibilityInfo.getOffer().getLimit())) {
		    
			List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
					MapValues.mapCustomerSegmentInOfferLimits(eligibilityInfo.getOffer().getLimit(), Predicates.isCustomerSegmentInLimits()));
	    	Checks.checkAllDownloadLimitForOffer(eligibilityInfo, purchaseRequest, null, customerSegmentNames, purchaseResultResponse);
			
		}
		
	}

	/**
	 * Check if download limit is left
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 */
	private void checkDownloadLimit(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest, PurchaseResultResponse purchaseResultResponse) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo.getOffer().getLimit())) {
		    
			setCounterDetails(eligibilityInfo);
			eligibilityInfo.setOfferCounters(FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(eligibilityInfo.getOffer().getOfferId())));
	    	List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
					MapValues.mapCustomerSegmentInOfferLimits(eligibilityInfo.getOffer().getLimit(), Predicates.isCustomerSegmentInLimits()));
	    	Checks.checkDownloadLimitLeft(eligibilityInfo.getOfferCounters(), eligibilityInfo.getOffer(), purchaseRequest.getCouponQuantity(), eligibilityInfo, purchaseRequest.getVoucherDenomination(), purchaseResultResponse, customerSegmentNames);
			
		}
		
	}

	/**
	 * Make payment and complete purchase flow
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 * @return purchase history record saved after completing the purchase
	 * @throws MarketplaceException
	 */
	public PurchaseHistory makePaymentAndGetResponse(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest,
			PurchaseResultResponse purchaseResultResponse, Headers headers) throws MarketplaceException {
		
		PurchaseDomain purchaseHistoryDomain = DomainConfiguration.getPurchaseDomain(eligibilityInfo, 
				purchaseRequest, null, null, eligibilityInfo.getHeaders(), null);
		PurchaseHistory savedPurchaseDetails = 
				purchaseDomain.saveUpdatePurchaseDetails(purchaseHistoryDomain, null, OfferConstants.INSERT_ACTION.get(), eligibilityInfo.getHeaders()); 
		String uuid = !ObjectUtils.isEmpty(savedPurchaseDetails) ? savedPurchaseDetails.getId(): null;
		boolean isPaymentRequired = !eligibilityInfo.getAdditionalDetails().isFree();
		
		PaymentResponse paymentResponse = paymentService.paymentAndProvisioning(purchaseRequest, eligibilityInfo.getMemberDetailsDto(), eligibilityInfo.getOffer(), null, Requests.getPaymentAdditionalRequest(eligibilityInfo.getHeaders(), eligibilityInfo.getOffer().getActivityCode().getRedemptionActivityCode().getActivityCode(), uuid, isPaymentRequired)); 
		
		String log = Logs.logForVariable(OfferConstants.PAYMENT_RESPONSE_VARIABLE.get(), paymentResponse);
		LOG.info(log);
		
		PurchaseHistory purchaseDetails = !ObjectUtils.isEmpty(savedPurchaseDetails)
				? purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, purchaseRequest, paymentResponse, savedPurchaseDetails, eligibilityInfo.getHeaders(), null), savedPurchaseDetails, OfferConstants.UPDATE_ACTION.get(), eligibilityInfo.getHeaders())
				: purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, purchaseRequest, paymentResponse, null, eligibilityInfo.getHeaders(), null), null, OfferConstants.INSERT_ACTION.get(), eligibilityInfo.getHeaders());
		
		PurchaseResponseDto purchaseResponse = ProcessValues.getPurchaseResponse(paymentResponse, purchaseDetails, ProcessValues.getEarnMultiplierForPurchaseResponse(purchaseRequest.getSelectedPaymentItem(), eligibilityInfo.getOffer().getEarnMultiplier()), purchaseRequest, purchaseResultResponse);
		purchaseResultResponse.setPurchaseResponseDto(purchaseResponse);
		
		return purchaseDetails;
		
	}
	
	/**
	 * Additional operations to be performed after payment is successfully done
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseDetails
	 * @param purchaseResultResponse 
	 * @throws MarketplaceException
	 */
	public void performAdditionalOperation(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo,
			PurchaseHistory purchaseDetails, PurchaseResultResponse purchaseResultResponse) throws MarketplaceException {
		
		if(Checks.checkNoErrors(purchaseResultResponse)) {
			
			setOfferCounter(eligibilityInfo.getOfferCounters(), eligibilityInfo.getMemberDetails(), eligibilityInfo.getOffer(), purchaseRequest.getCouponQuantity(), purchaseRequest.getVoucherDenomination(), eligibilityInfo.getHeaders(), eligibilityInfo.getErrorRecordsList());      
			setGoldCertificateDetails(purchaseRequest, eligibilityInfo, purchaseDetails);
			sendLifeTimeSavingsForDiscountCoupon(purchaseRequest, eligibilityInfo, purchaseDetails);
	        sendLifeTimeSavingsForPoints(purchaseRequest, eligibilityInfo,
	        		ProcessValues.getSpentAmount(eligibilityInfo.getAmountInfo(),
	    	        		eligibilityInfo.getOffer().getVatPercentage(), purchaseRequest.getSelectedPaymentItem(), 
	    	        		purchaseRequest.getSelectedOption(), purchaseRequest.getCouponQuantity()),
	        		purchaseResultResponse, purchaseDetails.getPointsTransactionId());
	        sendPushNotificationToNonSubscribedUser(purchaseRequest, eligibilityInfo);
			
		}
  	
	}
	
	/**
	 * Additional operations to be performed after payment is successfully done
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseDetails
	 * @param purchaseResultResponse 
	 * @throws MarketplaceException 
	 * @throws Exception 
	 */
	public void performAdditionalOperationsForPurchase(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo,
			PurchaseHistory purchaseDetails, PurchaseResultResponse purchaseResultResponse) throws MarketplaceException{
		
		if(Checks.checkNoErrors(purchaseResultResponse)) {
			
			setGoldCertificateDetails(purchaseRequest, eligibilityInfo, purchaseDetails);
			sendLifeTimeSavingsForDiscountCoupon(purchaseRequest, eligibilityInfo, purchaseDetails);
	        sendLifeTimeSavingsForPoints(purchaseRequest, eligibilityInfo,
	        		ProcessValues.getSpentAmount(eligibilityInfo.getAmountInfo(),
	    	        		eligibilityInfo.getOffer().getVatPercentage(), purchaseRequest.getSelectedPaymentItem(), 
	    	        		purchaseRequest.getSelectedOption(), purchaseRequest.getCouponQuantity()),
	        		purchaseResultResponse, purchaseDetails.getPointsTransactionId());
	        sendPushNotificationToNonSubscribedUser(purchaseRequest, eligibilityInfo);
			setCountersForCurrentPurchase(eligibilityInfo, purchaseRequest);
		} 
  	
	}
	
	/**
	 * Sets the counter values after the purchase is successful
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @throws Exception 
	 */
	public void setCountersForCurrentPurchase(EligibilityInfo eligibilityInfo, PurchaseRequestDto purchaseRequest){
		
		try {
			
			ErrorRecords errorRecord = new ErrorRecords();
			errorRecord.setAccountNumber(purchaseRequest.getAccountNumber());
			errorRecord.setMembershipCode(purchaseRequest.getMembershipCode());
			errorRecord.setOfferId(purchaseRequest.getOfferId());
			errorRecord.setRules(eligibilityInfo.getOffer().getRules());
			errorRecord.setDenomination(purchaseRequest.getVoucherDenomination());
			errorRecord.setCouponQuantity(purchaseRequest.getCouponQuantity());
			errorRecord.setStatus(OfferConstants.NEW_STATUS.get());
			errorRecord.setCreatedDate(new Date());
			
			if(CollectionUtils.isEmpty(eligibilityInfo.getErrorRecordsList())) {
				
				eligibilityInfo.setErrorRecordsList(new ArrayList<>(1));
				
			}
			
			eligibilityInfo.getErrorRecordsList().add(repositoryHelper.saveErrorRecord(errorRecord));
//			auditService.insertDataAudit(OffersDBConstants.ERROR_RECORDS, eligibilityInfo.getErrorRecordsList().get(eligibilityInfo.getErrorRecordsList().size()-1), OffersRequestMappingConstants.PURCHASE, eligibilityInfo.getHeaders().getExternalTransactionId(), eligibilityInfo.getHeaders().getUserName());
			counterHelper.saveCounterDetails(eligibilityInfo, purchaseRequest);
			repositoryHelper.deleteErrorRecord(eligibilityInfo.getErrorRecordsList());
			auditService.deleteDataAudit(OffersDBConstants.ERROR_RECORDS, eligibilityInfo.getErrorRecordsList(), OffersRequestMappingConstants.PURCHASE, eligibilityInfo.getHeaders().getExternalTransactionId(), eligibilityInfo.getHeaders().getUserName());
			
		} catch (Exception e) {
			
			exceptionLogService.saveExceptionsToExceptionLogs(e, eligibilityInfo.getHeaders().getExternalTransactionId(), purchaseRequest.getAccountNumber(), eligibilityInfo.getHeaders().getUserName());
			String log = Logs.logForConstantStatment(OfferConstants.COUNTER_ROLLBACK_PERFORMED.get());
			LOG.info(log);
		}
				
	}

	/**
	 * Set the offer counter after current purchase
	 * @param offerCounter
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerId
	 * @param couponQuantity
	 * @param denomination
	 * @param header
	 * @throws MarketplaceException
	 */
	public void setOfferCounter(OfferCounter offerCounter, GetMemberResponse memberDetails, OfferCatalog offer, Integer couponQuantity, Integer denomination, Headers header, List<ErrorRecords> errorRecordList){
		
		OfferCounterDomain offerCountersDomain = DomainConfiguration.getOfferCounterDomain(offerCounter, couponQuantity, offer.getOfferId(), memberDetails.getAccountNumber(), memberDetails.getMembershipCode(), denomination, offer.getRules());
		OfferCounter savedOfferCounter = offerCounterDomain.saveUpdateOfferCounter(offerCountersDomain, offerCounter, ProcessValues.getAction(offerCounter), header, OffersRequestMappingConstants.PURCHASE);
		
		if(ObjectUtils.isEmpty(savedOfferCounter)) {
			
			ErrorRecords errorRecord = ProcessValues.getErrorRecord(memberDetails.getAccountNumber(), memberDetails.getMembershipCode(), offer.getOfferId(), couponQuantity, denomination, offer.getRules());
			repositoryHelper.saveErrorRecord(errorRecord);
			
		} else {
			
			repositoryHelper.deleteErrorRecord(errorRecordList);
		}
		
    }
	
	/**
	 * Create gold certificate details after purchase is successfully complete
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @param purchaseDetails
	 * @throws MarketplaceException
	 */
	public void setGoldCertificateDetails(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo, PurchaseHistory purchaseDetails) throws MarketplaceException {
		
       if(Checks.checkIsGoldCertificate(purchaseRequest.getSelectedPaymentItem())) {
			
			GoldCertificate existingGoldCertificate = repositoryHelper.findGoldCertificateRecordByAccountNumberAndMembershipCode(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode());
			String action = ProcessValues.getAction(existingGoldCertificate);
			GoldCertificateDto goldCertificateDto = ProcessValues.getGoldCertificateDto(purchaseRequest, eligibilityInfo, purchaseDetails);
			GoldCertificateDomain goldCertificate = GetValues.getGoldCertificateDomain(goldCertificateDto, existingGoldCertificate, eligibilityInfo.getHeaders(), action);
			goldCertificateDomain.saveUpdateGoldCertificate(goldCertificate, action, eligibilityInfo.getHeaders(), existingGoldCertificate, OffersRequestMappingConstants.PURCHASE);
			
		}
		
	}

	/**
	 * Send lifetime saving details to points bank for discount coupon
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @throws MarketplaceException
	 */
	public void sendLifeTimeSavingsForDiscountCoupon(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo, PurchaseHistory purchaseDetails){
		
		if(Checks.checkIsDiscountVoucher(purchaseRequest.getSelectedPaymentItem())) {
			
			LifeTimeSavingsVouchersEvent lifeTimeSavingsVoucherEvent = 
					Requests.createLifetimeSavingsVoucherEvent(eligibilityInfo.getHeaders(), 
							purchaseRequest, ProcessValues.createLifetimeSavingsHelperDto(true, eligibilityInfo, eligibilityInfo.getOffer().getEstSavings(), purchaseDetails.getPointsTransactionId()));
			eventHandler.publishLifetimeSavings(lifeTimeSavingsVoucherEvent);
		}

    }
	
	/**
	 * Send lifetime savings to points bank for full Points
	 * @param purchaseRequest
	 * @param spentPoints
	 * @param partnerCode
	 * @param header
	 */
	public void sendLifeTimeSavingsForPoints(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo,
			Double spentAmount, ResultResponse resultResponse, String pointsTransactionId){
		
		if(Checks.checkPointsMethod(purchaseRequest.getSelectedOption())
		&& purchaseRequest.getSpentPoints()>0) {
			
			if(CollectionUtils.isEmpty(eligibilityInfo.getConversionRateList())) {
				eligibilityInfo.setConversionRateList(fetchConversionRateList(eligibilityInfo.getOffer().getPartnerCode(),
						purchaseRequest.getSelectedPaymentItem(), eligibilityInfo.getHeaders().getChannelId(),
						resultResponse));
			}
			double singleVoucherPoints = Math.ceil((double) purchaseRequest.getSpentPoints() / (double) purchaseRequest.getCouponQuantity());
			Double eqAmount = ProcessValues.getEquivalentAmount(eligibilityInfo.getConversionRateList(), (int) singleVoucherPoints, resultResponse);
			
			if(!ObjectUtils.isEmpty(eqAmount)) {
				eqAmount = eqAmount*purchaseRequest.getCouponQuantity();
			}
			
			if(Checks.checkErrorsPresent(resultResponse)) {
				
				Responses.removeAllErrors(resultResponse);
				
				if(ObjectUtils.isEmpty(spentAmount)
				|| Checks.checkIsPartialPointsPaymentMethod(purchaseRequest.getSelectedOption())) {
					spentAmount = OffersConfigurationConstants.defaultConversionRate * purchaseRequest.getSpentPoints();
				}
				
				
			} else {
				spentAmount = eqAmount;
			}
			
			LifeTimeSavingsVouchersEvent lifeTimeSavingsVoucherEvent = 
					Requests.createLifetimeSavingsVoucherEvent(eligibilityInfo.getHeaders(), 
							purchaseRequest, ProcessValues.createLifetimeSavingsHelperDto(false, eligibilityInfo, spentAmount, pointsTransactionId));
			eventHandler.publishLifetimeSavings(lifeTimeSavingsVoucherEvent);
		}
		
    }	
	
	/**
	 * Send push notification to member if he is non-subscribed and purchases a discount offer 
	 * @param purchaseRequest
	 * @param eligibilityInfo
	 * @throws MarketplaceException
	 */
	public void sendPushNotificationToNonSubscribedUser(PurchaseRequestDto purchaseRequest, EligibilityInfo eligibilityInfo){
		
		if(Checks.checkIsDiscountVoucher(purchaseRequest.getSelectedPaymentItem()) && !eligibilityInfo.getAdditionalDetails().isSubscribed()) {
			
			PushNotificationRequestDto pushNotificationMessage = Requests.getPushNotificationRequestForBogoSubscription(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(),
					eligibilityInfo.getMemberDetails().getFirstName(), eligibilityInfo.getHeaders().getExternalTransactionId(), eligibilityInfo.getMemberDetails().getUiLanguage());
			eventHandler.publishPushNotification(pushNotificationMessage);
			
		} 
		
	}
	
 	/**
	 * Set member related details for an account number
	 * @param eligibilityInfo
	 * @param accountNumber
	 * @param resultResponse
	 * @throws MarketplaceException
	 */
	public boolean checkMembershipEligbilityForOffers(EligibilityInfo eligibilityInfo, String accountNumber, ResultResponse resultResponse) throws MarketplaceException {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo) && eligibilityInfo.isMember()) {
			
			if(ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())) {
			
				GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, eligibilityInfo.getHeaders());
				eligibilityInfo.setMemberDetails(memberDetails);    
				if(!ObjectUtils.isEmpty(eligibilityInfo.getAdditionalDetails())) {
					
					eligibilityInfo.getAdditionalDetails().setSubscribed(memberDetails.isSubscribed());
					
				}

				eligibilityInfo.setCustomerSegmentCheckRequired(Checks.checkIsCustomerSegmentCheckRequired(eligibilityInfo.getOfferList()));
			}
			
			if(Checks.checkNoErrors(resultResponse)) {
				
				if(eligibilityInfo.isBirthdayInfoRequired()) {
					
					BirthdayDurationInfoDto birthdayDurationInfoDto = ProcessValues.getBirthdayDurationInfoDto(repositoryHelper.findBirthdayInfo(), eligibilityInfo.getMemberDetails());
					eligibilityInfo.setBirthdayDurationInfoDto(birthdayDurationInfoDto);
					eligibilityInfo.setPurchaseHistoryList(repositoryHelper.findBirthdayBillPaymentAndRechargePurchaseRecords(eligibilityInfo.getMemberDetails().getAccountNumber(), 
								eligibilityInfo.getMemberDetails().getMembershipCode(), birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate()));
					
				}
				
				setCustomerTypeDetails(eligibilityInfo, resultResponse);
				setCustomerSegmentDetails(eligibilityInfo, accountNumber, resultResponse);
				
				if(Checks.checkNoErrors(resultResponse)) {
					setCounterDetails(eligibilityInfo);
				}
					
			}
			
		}
		return Checks.checkNoErrors(resultResponse);
	}
	
   /**
	 * 
	 * @param eligibilityInfo
	 * @param accountNumber
	 * @param resultResponse
	 * @return status after checking membership related eligibilty
	 * @throws MarketplaceException
	 */
	public boolean getMemberEligibilityForDetailedEligibleOffer(EligibilityInfo eligibilityInfo, String accountNumber,
			ResultResponse resultResponse) throws MarketplaceException {
		
      if(!ObjectUtils.isEmpty(eligibilityInfo) && eligibilityInfo.isMember()) {
			
			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, eligibilityInfo.getHeaders());
			eligibilityInfo.setMemberDetails(memberDetails);
			
			if(!ObjectUtils.isEmpty(eligibilityInfo.getAdditionalDetails())) {
				
				eligibilityInfo.getAdditionalDetails().setSubscribed(memberDetails.isSubscribed());
				
			}

			eligibilityInfo.setCustomerSegmentCheckRequired(Checks.checkIsCustomerSegmentCheckRequired(eligibilityInfo.getOfferList()));
			
			if(Checks.checkNoErrors(resultResponse)
			&& Responses.setResponseAfterConditionCheck(Checks.checkListValuesInList(eligibilityInfo.getOffer().getCustomerTypes(), memberDetails.getCustomerType()),
					OfferErrorCodes.CUSTOMER_TYPE_INELIGIBLE, resultResponse)) {
				
				setCustomerTypeDetails(eligibilityInfo, resultResponse);
				setCustomerSegmentDetails(eligibilityInfo, accountNumber, resultResponse);
				
			}
			
			if(Checks.checkNoErrors(resultResponse)
			&& !ObjectUtils.isEmpty(eligibilityInfo.getOffer().getLimit())) {
	                	
				setOfferCounterValues(eligibilityInfo);
	        }
		
		}
		
       return Checks.checkNoErrors(resultResponse);
	}
	  	
   /**
    * 	
    * @param offerCatalogDto 
    * @param eligibilityInfo
    */
   public void setBirthdayAvailedStatus(OfferCatalogResultResponseDto offerCatalogDto, EligibilityInfo eligibilityInfo) {
	
	   if(!ObjectUtils.isEmpty(offerCatalogDto)
		&& StringUtils.equalsIgnoreCase(offerCatalogDto.getIsBirthdayGift(), OfferConstants.FLAG_SET.get())) {
		   
		   BirthdayInfo birthdayInfo = repositoryHelper.findBirthdayInfo();
		   BirthdayDurationInfoDto birthdayDurationInfoDto = ProcessValues.getBirthdayDurationInfo(birthdayInfo, eligibilityInfo.getMemberDetails().getDob());
		   List<PurchaseHistory> purchaseRecords = repositoryHelper.getPurchaseRecordsForGiftOffers(birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate(), eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), MapValues.mapOfferIdFromOfferList(eligibilityInfo.getOfferList()));
		   eligibilityInfo.setPurchaseHistoryList(purchaseRecords);
		   ProcessValues.setBirthdayAvailedStatus(eligibilityInfo, offerCatalogDto);
	   }
	
   }

   /***
    * Checks all conditions corresponding to welcome gift and purchase the associated gift to generate voucher
    * @param purchaseRequest
    * @param headers
    * @param subCardType
    * @param resultResponse
    * @return voucher id generated corresponding to welcome gift 
    * @throws MarketplaceException
    */
	public String validateAndGiftOffer(PurchaseRequestDto purchaseRequest, Headers headers, String subCardType, ResultResponse resultResponse) throws MarketplaceException {
	
		
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.VALIDATE_AND_GIFT_OFFER_METHOD.get());
		LOG.info(log);
		log = Logs.logForVariable(OfferConstants.PURCHASE_REQUEST.get(), purchaseRequest);
		LOG.info(log);
		log = Logs.logForVariable(OfferConstants.HEADER.get(), headers);
		LOG.info(log);
		log = Logs.logForVariable(OfferConstants.SUB_CARD_TYPE.get(), subCardType);
		LOG.info(log);
		
		String voucherId = null;
		
		List<OfferCatalog> offerList = repositoryHelper.findByIsGift(OfferConstants.FLAG_SET.get());
		List<OfferCatalog> giftOffers = null;
		LOG.info("offerList : {}", offerList);
		if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offerList), OfferErrorCodes.NO_GIFT_OFFERS, resultResponse)) {
			
			giftOffers = FilterValues.filterOfferList(offerList, Predicates.giftOffer(headers.getChannelId(), subCardType));
			LOG.info("GiftOffers : {}", giftOffers);
			if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(giftOffers), OfferErrorCodes.NO_GIFT_OFFERS, resultResponse)
			&& Responses.setResponseAfterConditionCheck(Utilities.isListWithOneElement(giftOffers), OfferErrorCodes.MORE_THAN_ONE_GIFT_OFFER, resultResponse)) {
				
				OfferCatalog offerDetails = giftOffers.get(0);
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerDetails), OfferErrorCodes.NO_GIFT_OFFERS, resultResponse)) {
				
					log = Logs.logForVariable(OffersRequestMappingConstants.OFFER_ID, offerDetails.getOfferId());
					LOG.info(log);
					purchaseRequest.setOfferId(offerDetails.getOfferId());
					voucherId =  giftOfferAndGetVoucherId(offerDetails, purchaseRequest, headers, resultResponse);
					log = Logs.logForVariable(VoucherRequestMappingConstants.VOUCHER_ID, voucherId);
					LOG.info(log);
					
				}
							
			}
		   	
		} 
		LOG.info("ResultResponse : {}", resultResponse );;
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.VALIDATE_AND_GIFT_OFFER_METHOD.get());
		LOG.info(log);
		
		return voucherId;
	}
	
	
	public String validateAndGiftOffer(PurchaseRequestDto purchaseRequest, Headers headers, WelcomeGiftRequestDto welcomeGiftRequest, ResultResponse resultResponse) throws MarketplaceException {
	
		
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.VALIDATE_AND_GIFT_OFFER_METHOD.get());
		LOG.info(log);
		log = Logs.logForVariable(OfferConstants.PURCHASE_REQUEST.get(), purchaseRequest);
		LOG.info(log);
		log = Logs.logForVariable(OfferConstants.HEADER.get(), headers);
		LOG.info(log);
//		log = Logs.logForVariable(OfferConstants.SUB_CARD_TYPE.get(), subCardType);
//		LOG.info(log);
		
		String voucherId = null;
		
		OfferCatalog giftOffer = offerRepository.findByOfferIdAndIsGift(welcomeGiftRequest.getGiftId() ,OfferConstants.FLAG_SET.get());
		if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(giftOffer), OfferErrorCodes.NO_GIFT_OFFERS, resultResponse)) {
			log = Logs.logForVariable(OffersRequestMappingConstants.OFFER_ID, giftOffer.getOfferId());
			LOG.info(log);
			purchaseRequest.setOfferId(giftOffer.getOfferId());
			voucherId =  giftOfferAndGetVoucherId(giftOffer, purchaseRequest, headers, resultResponse);
			log = Logs.logForVariable(VoucherRequestMappingConstants.VOUCHER_ID, voucherId);
			LOG.info(log);
		}
						
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.VALIDATE_AND_GIFT_OFFER_METHOD.get());
		LOG.info(log);
		
		return voucherId;
	}
	
	/**
	 * 
	 * @param offerDetails
	 * @param purchaseRequest
	 * @param headers
	 * @param resultResponse 
	 * @param resultResponse
	 * @return the voucher id generated after the offer is successfully gifted 
	 * @throws MarketplaceException
	 */
	public String giftOfferAndGetVoucherId(OfferCatalog offerDetails, PurchaseRequestDto purchaseRequest, Headers headers, ResultResponse resultResponse) throws MarketplaceException {
	
		String voucherId = null;
        List<ConversionRate> conversionRateList = Checks.checkPointsMethod(purchaseRequest.getSelectedPaymentItem())
	    		? repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(Arrays.asList(offerDetails.getPartnerCode(), OfferConstants.SMILES.get()), Arrays.asList(ProcessValues.getProductTypeValue(purchaseRequest.getSelectedPaymentItem())))
	    		: null;
	    		
	    EligibilityInfo eligibilityInfo = ProcessValues.getEligibilityInfoForWelcomeGift(purchaseRequest, headers, offerDetails, conversionRateList, resultResponse);
		
		PurchaseHistory savedPurchaseDetails = purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, 
				purchaseRequest, null, null, eligibilityInfo.getHeaders(), null), null, OfferConstants.INSERT_ACTION.get(), headers); 
		
		String uuid = !ObjectUtils.isEmpty(savedPurchaseDetails) ? savedPurchaseDetails.getId(): null;
		GetMemberResponseDto getMemberResponseDto = fetchServiceValues.getMemberDetailsForPayment(purchaseRequest.getAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(), headers, resultResponse);
		eligibilityInfo.setMemberDetailsDto(getMemberResponseDto);
		eligibilityInfo.setMemberDetails(ProcessValues.getMemberInfo(getMemberResponseDto, purchaseRequest.getAccountNumber()));
		checkNewDownloadLimit(eligibilityInfo, purchaseRequest, null);
		
		if(Checks.checkNoErrors(resultResponse)) {
			
			PaymentResponse paymentResponse = paymentService.paymentAndProvisioning(purchaseRequest, getMemberResponseDto, offerDetails, null, Requests.getPaymentAdditionalRequest(headers, offerDetails.getActivityCode().getRedemptionActivityCode().getActivityCode(), uuid, !eligibilityInfo.getAdditionalDetails().isFree())); 
			
			String log = Logs.logForVariable(OfferConstants.PAYMENT_RESPONSE_VARIABLE.get(), paymentResponse);
			LOG.info(log);
			
			PurchaseHistory purchaseDetails = !ObjectUtils.isEmpty(savedPurchaseDetails)
					? purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, purchaseRequest, paymentResponse, savedPurchaseDetails, eligibilityInfo.getHeaders(), null), savedPurchaseDetails, OfferConstants.UPDATE_ACTION.get(), eligibilityInfo.getHeaders())
					: purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, purchaseRequest, paymentResponse, null, eligibilityInfo.getHeaders(), null), null, OfferConstants.INSERT_ACTION.get(), eligibilityInfo.getHeaders());	
		    
			sendLifeTimeSavingsForDiscountCoupon(purchaseRequest, eligibilityInfo, purchaseDetails);
			sendLifeTimeSavingsForPoints(purchaseRequest, eligibilityInfo,
					ProcessValues.getSpentAmount(eligibilityInfo.getAmountInfo(),
	        		offerDetails.getVatPercentage(), purchaseRequest.getSelectedPaymentItem(), 
	        		purchaseRequest.getSelectedOption(), purchaseRequest.getCouponQuantity()), resultResponse, purchaseDetails.getPointsTransactionId());
			
			setCountersForCurrentPurchase(eligibilityInfo, purchaseRequest);
			
			voucherId = !CollectionUtils.isEmpty(purchaseDetails.getVoucherCode())
					    ? purchaseDetails.getVoucherCode().get(0)
					    : null; 	

		}

		return voucherId;
	}
		
	

	/**
	 * 
	 * @param offerDetails
	 * @param purchaseRequest
	 * @param headers
	 * @param resultResponse 
	 * @param resultResponse
	 * @return the voucher id generated after a free voucher is generated 
	 * @throws MarketplaceException
	 */
	public String generateFreeVoucher(OfferCatalog offerDetails, PurchaseRequestDto purchaseRequest, Headers headers, GetMemberResponseDto member, ResultResponse resultResponse) throws MarketplaceException {
		
		String voucherId = null;
		
		LOG.info("Purchase request is {}", purchaseRequest);
		
		Denomination denomination = Checks.checkIsDenomination(purchaseRequest.getVoucherDenomination())
	            && !ObjectUtils.isEmpty(offerDetails)
	            ? FilterValues.findAnyDenominationInDenominationList(offerDetails.getDenominations(), Predicates.sameDirhamValueForDenomination(purchaseRequest.getVoucherDenomination()))
	            : null;
		
		if(Responses.setResponseAfterConditionCheck(Checks.checkDenominationPresent(purchaseRequest.getSelectedPaymentItem(), purchaseRequest.getVoucherDenomination(), denomination, offerDetails), 
				OfferErrorCodes.DENOMINATION_NOT_EXISTING_FOR_OFFER, resultResponse)) {
		
			List<ConversionRate> conversionRateList = Checks.checkPointsMethod(purchaseRequest.getSelectedPaymentItem())
		    		? repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(Arrays.asList(offerDetails.getPartnerCode(), OfferConstants.SMILES.get()), Arrays.asList(ProcessValues.getProductTypeValue(purchaseRequest.getSelectedPaymentItem())))
		    		: null;
			EligibilityInfo eligibilityInfo = ProcessValues.getEligibilityInfoForFreeVoucherGeneration(purchaseRequest, headers, offerDetails, conversionRateList, denomination);
			
		    PurchaseHistory savedPurchaseDetails = purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, 
					purchaseRequest, null, null, eligibilityInfo.getHeaders(), null), null, OfferConstants.INSERT_ACTION.get(), headers); 
			
			String uuid = !ObjectUtils.isEmpty(savedPurchaseDetails) ? savedPurchaseDetails.getId(): null;
			GetMemberResponseDto getMemberResponseDto = ObjectUtils.isEmpty(member)
					? fetchServiceValues.getMemberDetailsForPayment(purchaseRequest.getAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(), headers, resultResponse)
					: member;
			eligibilityInfo.setMemberDetails(ProcessValues.getMemberInfo(getMemberResponseDto, purchaseRequest.getAccountNumber()));		
			eligibilityInfo.setMemberDetailsDto(getMemberResponseDto);
			
			checkNewDownloadLimit(eligibilityInfo, purchaseRequest, null);
			
			if(Checks.checkNoErrors(resultResponse)) {
				
				PaymentResponse paymentResponse = paymentService.paymentAndProvisioning(purchaseRequest, getMemberResponseDto, offerDetails, null, Requests.getPaymentAdditionalRequest(headers, offerDetails.getActivityCode().getRedemptionActivityCode().getActivityCode(), uuid, !eligibilityInfo.getAdditionalDetails().isFree())); 
				
				String log = Logs.logForVariable(OfferConstants.PAYMENT_RESPONSE_VARIABLE.get(), paymentResponse);
				LOG.info(log);
				ProcessValues.addErrorMessageForPayment(paymentResponse,resultResponse);
				PurchaseHistory purchaseDetails = !ObjectUtils.isEmpty(savedPurchaseDetails)
						? purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, purchaseRequest, paymentResponse, savedPurchaseDetails, eligibilityInfo.getHeaders(), null), savedPurchaseDetails, OfferConstants.UPDATE_ACTION.get(), eligibilityInfo.getHeaders())
						: purchaseDomain.saveUpdatePurchaseDetails(DomainConfiguration.getPurchaseDomain(eligibilityInfo, purchaseRequest, paymentResponse, null, eligibilityInfo.getHeaders(), null), null, OfferConstants.INSERT_ACTION.get(), eligibilityInfo.getHeaders());	
			    
				setCountersForCurrentPurchase(eligibilityInfo, purchaseRequest);
				
				sendLifeTimeSavingsForDiscountCoupon(purchaseRequest, eligibilityInfo, purchaseDetails);
				sendLifeTimeSavingsForPoints(purchaseRequest, eligibilityInfo,
						ProcessValues.getSpentAmount(eligibilityInfo.getAmountInfo(),
		        		offerDetails.getVatPercentage(), purchaseRequest.getSelectedPaymentItem(), 
		        		purchaseRequest.getSelectedOption(), purchaseRequest.getCouponQuantity()), resultResponse, purchaseDetails.getPointsTransactionId());
				
			
				voucherId = !CollectionUtils.isEmpty(purchaseDetails.getVoucherCode())
						   ? purchaseDetails.getVoucherCode().get(0)
								   : null;
				
			}			
			LOG.info("Voucher Id : {}", voucherId);		   
		
		}
		
		return voucherId; 	

	}

	/**
	 * 
	 * @param eligibilityInfo
	 * @param resultResponse
	 * @return status after successfully setting all member details
	 * @throws MarketplaceException
	 */
	public boolean setAllAccountDetails(EligibilityInfo eligibilityInfo, ResultResponse resultResponse) 
			throws MarketplaceException {
		
		return setMemberDetails(eligibilityInfo, resultResponse)
			&& setCustomerTypeDetails(eligibilityInfo, resultResponse)
//			&& setSubscriptionDetails(eligibilityInfo, resultResponse)
			&& setCustomerSegmentDetails(eligibilityInfo, eligibilityInfo.getAccountNumber(), resultResponse);
		
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param resultResponse
	 * @return status after setting member details for specific account number
	 * @throws MarketplaceException
	 */
	public boolean setMemberDetails(EligibilityInfo eligibilityInfo, ResultResponse resultResponse) throws MarketplaceException{
		
		GetMemberResponse memberDetails = 
				   ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())
				&& !StringUtils.isEmpty(eligibilityInfo.getAccountNumber())
				? fetchServiceValues.getMemberDetails(eligibilityInfo.getAccountNumber(), resultResponse, eligibilityInfo.getHeaders())
				: eligibilityInfo.getMemberDetails();
		eligibilityInfo.setMemberDetails(memberDetails);
		
		if(!ObjectUtils.isEmpty(eligibilityInfo.getAdditionalDetails())) {
			
			eligibilityInfo.getAdditionalDetails().setSubscribed(memberDetails.isSubscribed());
			
		}

		eligibilityInfo.setCustomerSegmentCheckRequired(Checks.checkIsCustomerSegmentCheckRequired(eligibilityInfo.getOfferList()));
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param resultResponse
	 * @return status after setting all customer  type details
	 * @throws MarketplaceException
	 */
	public boolean setCustomerTypeDetails(EligibilityInfo eligibilityInfo, ResultResponse resultResponse) throws MarketplaceException {
		
		if(Checks.checkNoErrors(resultResponse)) {
			
			List<ParentChlidCustomer> customerTypeList = fetchServiceValues.getAllCustomerTypes(resultResponse, eligibilityInfo.getHeaders());
			
			if(!CollectionUtils.isEmpty(customerTypeList)) {
				
				eligibilityInfo.getMemberDetails().setCustomerType(ProcessValues
						.getAllParentTypes(customerTypeList, eligibilityInfo.getMemberDetails().getCustomerType()));
				
			}
			
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
    /**
	 * 
	 * @param eligibilityInfo
	 * @param accountNumber
	 * @param resultResponse
	 * @return Status afer setting customer segment details fetched from Decision Manager
	 * @throws MarketplaceException
	 */
	public boolean setCustomerSegmentDetails(EligibilityInfo eligibilityInfo, String accountNumber, ResultResponse resultResponse) throws MarketplaceException {
		
		if(eligibilityInfo.isCustomerSegmentCheckRequired()) {
           
			List<String> customerSegments = null;
			Date date = Utilities.getDate(OffersConfigurationConstants.dmDuration);
			List<PurchaseHistory> purchaseRecords = !CollectionUtils.isEmpty(eligibilityInfo.getPurchaseHistoryList())
					? eligibilityInfo.getPurchaseHistoryList()					
					: repositoryHelper.findBillPaymentRechargeRecordsforCurrentAccountInLast30days(
					        accountNumber, eligibilityInfo.getMemberDetails().getMembershipCode(), date);
				
			eligibilityInfo.setPurchaseHistoryList(purchaseRecords);
			
			List<PurchaseHistory> records = FilterValues.filterPurchaseRecords(purchaseRecords, Predicates.samePurchaseItemListInAnInclusiveDateRange(OffersListConstants.ELIGIBLE_TELECOM_SPEND_ITEMS, 
					date, new Date()));
			
			CustomerSegmentResult decisionManagerResult = fetchServiceValues.checkCustomerSegment(Requests.getDecisionManagerRequestDto(Arrays.asList(eligibilityInfo.getMemberDetails()), records), eligibilityInfo.getHeaders(), resultResponse);
			
			if(Checks.checkNoErrors(resultResponse)
			&& !ObjectUtils.isEmpty(decisionManagerResult)) {
				
				RuleResult ruleResult = FilterValues.findAnyRuleResultWithinRuleList(decisionManagerResult.getRulesResult(), Predicates.sameAccountNumberForRule(eligibilityInfo.getMemberDetails().getAccountNumber()));
				eligibilityInfo.setRuleResult(ruleResult);
				if(!ObjectUtils.isEmpty(ruleResult)) {
			    	
			       customerSegments = ruleResult.getCustomerSegments();
			    } 
			
			}
			
			eligibilityInfo.getMemberDetails().setCustomerSegment(customerSegments);	
        
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * Set counter for list of offers
	 * @param eligibilityInfo
	 */
	public void setCounterDetails(EligibilityInfo eligibilityInfo) {
		
		if(eligibilityInfo.isMember()) {
			
			eligibilityInfo.setCounterOfferIdList(MapValues
	        		.mapOfferIdList(eligibilityInfo.getOfferList(), Predicates.limitPresent()));
			eligibilityInfo.setOfferCounterList(repositoryHelper.getAllCountersByIdList(eligibilityInfo.getCounterOfferIdList()));
			
		}
		
	}
	
	/**
	 * Set counter for list of eligible offers
	 * @param eligibilityInfo
	 */
	public void setCounterDetailsForEligibileOffers(EligibleOfferHelperDto eligibilityInfo) {
		
		if(eligibilityInfo.isMember()) {
			
			eligibilityInfo.setCounterOfferIdList(MapValues
	        		.mapEligibleOfferIdList(eligibilityInfo.getOfferList(), Predicates.limitPresentForEligibleOffers()));
			eligibilityInfo.setOfferCounterList(repositoryHelper.getAllCountersByIdList(eligibilityInfo.getCounterOfferIdList()));
			
		}
		
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param resultResponse
	 * @return status after successfully setting all member details
	 * @throws MarketplaceException
	 */
	public boolean setAccountDetailsForEligibleOffers(EligibleOfferHelperDto eligibilityInfo, ResultResponse resultResponse) 
			throws MarketplaceException {
		
		return setMemberDetailsForEligibleOffer(eligibilityInfo, resultResponse)
			&& setCustomerTypeDetailsForEligibleOffer(eligibilityInfo, resultResponse)
//			&& setSubscriptionDetailsForEligibleOffer(eligibilityInfo, resultResponse)
			&& setCustomerSegmentDetailsForEligibleOffer(eligibilityInfo, eligibilityInfo.getAccountNumber(), resultResponse);
		
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param resultResponse
	 * @return status after setting member details for specific account number
	 * @throws MarketplaceException
	 */
	public boolean setMemberDetailsForEligibleOffer(EligibleOfferHelperDto eligibilityInfo, ResultResponse resultResponse) throws MarketplaceException{
		
		GetMemberResponse memberDetails = ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())
				&& !StringUtils.isEmpty(eligibilityInfo.getAccountNumber())
				? fetchServiceValues.getMemberDetails(eligibilityInfo.getAccountNumber(), resultResponse, eligibilityInfo.getHeaders())
				: eligibilityInfo.getMemberDetails();
		eligibilityInfo.setMemberDetails(memberDetails);
		eligibilityInfo.setCustomerSegmentCheckRequired(Checks.checkIsCustomerSegmentCheckRequiredForEligibleOffers(eligibilityInfo.getOfferList()));
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param eligibilityInfo
	 * @param resultResponse
	 * @return status after setting all customer  type details
	 * @throws MarketplaceException
	 */
	public boolean setCustomerTypeDetailsForEligibleOffer(EligibleOfferHelperDto eligibilityInfo, ResultResponse resultResponse) throws MarketplaceException {
		
		if(Checks.checkNoErrors(resultResponse)) {
			
			List<ParentChlidCustomer> customerTypeList = fetchServiceValues.getAllCustomerTypes(resultResponse, eligibilityInfo.getHeaders());
			
			if(!CollectionUtils.isEmpty(customerTypeList)) {
				
				eligibilityInfo.getMemberDetails().setCustomerType(ProcessValues
						.getAllChildTypes(customerTypeList, eligibilityInfo.getMemberDetails().getCustomerType()));
				
			}
			
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
    /**
	 * 
	 * @param eligibilityInfo
	 * @param accountNumber
	 * @param resultResponse
	 * @return Status after setting customer segment details fetched from Decision Manager
	 * @throws MarketplaceException
	 */
	public boolean setCustomerSegmentDetailsForEligibleOffer(EligibleOfferHelperDto eligibilityInfo, String accountNumber, ResultResponse resultResponse) throws MarketplaceException {
		
		if(eligibilityInfo.isCustomerSegmentCheckRequired()) {
           
			List<String> customerSegments = null;
			Date date = Utilities.getDate(OffersConfigurationConstants.dmDuration);
			
			List<PurchaseHistory> purchaseRecords = repositoryHelper.findBillPaymentRechargeRecordsforCurrentAccountInLast30days(
					        accountNumber, eligibilityInfo.getMemberDetails().getMembershipCode(), date);
				
			List<PurchaseHistory> records = FilterValues.filterPurchaseRecords(purchaseRecords, Predicates.samePurchaseItemListInAnInclusiveDateRange(OffersListConstants.ELIGIBLE_TELECOM_SPEND_ITEMS, 
					date, new Date()));
				
			CustomerSegmentResult decisionManagerResult = fetchServiceValues.checkCustomerSegment(Requests.getDecisionManagerRequestDto(Arrays.asList(eligibilityInfo.getMemberDetails()), records), eligibilityInfo.getHeaders(), resultResponse);
			
			if(Checks.checkNoErrors(resultResponse)
			&& !ObjectUtils.isEmpty(decisionManagerResult)) {
				
				RuleResult ruleResult = FilterValues.findAnyRuleResultWithinRuleList(decisionManagerResult.getRulesResult(), Predicates.sameAccountNumberForRule(eligibilityInfo.getMemberDetails().getAccountNumber()));
				eligibilityInfo.setRuleResult(ruleResult);
				if(!ObjectUtils.isEmpty(ruleResult)) {
			    	
			       customerSegments = ruleResult.getCustomerSegments();
			    } 
			
			}
			
			eligibilityInfo.getMemberDetails().setCustomerSegment(customerSegments);	
        
		}
		
		return Checks.checkNoErrors(resultResponse);
		
	}
	
	/**
	 * 
	 * @param balance
	 * @param resultResponse
	 * @return equivalent aed and points value for a specific gold certificate denomination
	 */
	public AmountPoints getGoldCertificateAmountOrPoints(Double balance, String channelId, ResultResponse resultResponse) {
		
		AmountPoints amountPoints = null;
		
		if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(balance), OfferErrorCodes.BALANCE_EMPTY, resultResponse)) {
			
			amountPoints = new AmountPoints();
			if(Checks.checkNoErrors(resultResponse)) {
				amountPoints.setAmount(balance/OffersConfigurationConstants.BALANCE_TO_AMOUNT_RATE);
			}
			
			List<ConversionRate> conversionRateList = fetchConversionRateList(OffersConfigurationConstants.POINT_CONVERSION_PARTNER_CODE, OffersListConstants.GOLD_CERTIFICATE_ID_LIST.get(0), channelId, resultResponse);
			
			if(Checks.checkNoErrors(resultResponse)) {
				amountPoints.setPoints(ProcessValues.getEquivalentPoints(conversionRateList, amountPoints.getAmount()));
			}
			
		}
		
		return amountPoints;
		
	}

	/**
	 * 
	 * @param offerCatalogs
	 * @return offer entity list converted to response list
	 * @throws MarketplaceException
	 */
	public List<OfferCatalogResultResponseDto> getConvertedOfferResponseList(List<OfferCatalog> offerCatalogs, String channelId, Headers header, boolean includeOfferPaymentMethods) throws MarketplaceException {
		
		List<OfferCatalogResultResponseDto> offerCatalogList = null;
		
		if(!CollectionUtils.isEmpty(offerCatalogs)) {
			
			offerCatalogList = new ArrayList<>(offerCatalogs.size());
			List<ConversionRate> conversionRateList = getConversionRateListForOfferListByProgramCode(offerCatalogs, channelId, header);
			List<PurchasePaymentMethod> purchasePaymentMethodList = getPurchasePaymentMethodForOfferListByProgramCode(offerCatalogs, header);
			List<MarketplaceImage> imageList = getImageUrlListForOfferListByProgramCode(offerCatalogs, header);
			
			for (OfferCatalog offerCatalog : offerCatalogs) {
				
				OfferCatalogResultResponseDto offerCatalogDto = getOfferResponse(offerCatalog, purchasePaymentMethodList, conversionRateList, imageList, includeOfferPaymentMethods);
				offerCatalogList.add(offerCatalogDto);
			}
			
		}
		
		return offerCatalogList;
	}

	/**
	 * 
	 * @param offerCatalogs
	 * @return Fetched offer catalog list converted to short response object
	 * @throws MarketplaceException 
	 */
	public List<OfferCatalogShortResponseDto> getConvertedOfferShortResponseList(List<OfferCatalog> offerCatalogList) throws MarketplaceException {
		
		List<OfferCatalogShortResponseDto> offerCatalogResponseList = null;
		
		if(!CollectionUtils.isEmpty(offerCatalogList)) {
			
			offerCatalogResponseList = new ArrayList<>(offerCatalogList.size());
						
			for (OfferCatalog offerCatalog : offerCatalogList) {
				
				OfferCatalogShortResponseDto offerCatalogDto = getShortOfferResponse(offerCatalog);
				
				
				if(!ObjectUtils.isEmpty(offerCatalogDto)
	    			&& !ObjectUtils.isEmpty(offerCatalog.getOfferType())){
	    			 	
					offerCatalogDto.setOfferTypeId(offerCatalog.getOfferType().getOfferTypeId());
	
	    				if(!ObjectUtils.isEmpty(offerCatalog.getOfferType().getOfferDescription())){
	    				 
	    					offerCatalogDto.setTypeDescriptionEn(offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn());
	    					offerCatalogDto.setTypeDescriptionAr(offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionAr());
	    				}
	    			 
	    	   		}
		   		offerCatalogResponseList.add(offerCatalogDto);
			}
			
		}
		
		return offerCatalogResponseList;
	}

	/**
	 * 
	 * @param offerCatalog
	 * @return Maps fetched offer to offerShortResponseDto
	 * @throws MarketplaceException 
	 */
	private OfferCatalogShortResponseDto getShortOfferResponse(OfferCatalog offerCatalog) throws MarketplaceException {
		
		OfferCatalogShortResponseDto offerCatalogDto = null;
    	
    	try {
    		
    		if(!ObjectUtils.isEmpty(offerCatalog)) {
    			
	    		 offerCatalogDto = modelMapper.map(offerCatalog, OfferCatalogShortResponseDto.class);
	    		 
    		}
    		
			
		} catch (MongoException mongoException) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_SHORT_OFFER_RESPONSE_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_SHORT_OFFER_RESPONSE_METHOD.get(),
					e.getClass() + e.getMessage(),
					OfferExceptionCodes.OFFER_RESPONSE_RUNTIME_EXCEPTION);
			
		}
		
		return offerCatalogDto;
	}

	/**
	 * Sets the value of eligible offers and saves it
	 * @param eligibilityInfo
	 */
	public void populateEligibleOffers(EligibleOfferHelperDto eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			List<OfferCatalog> offerList = repositoryHelper.findAllActiveOffers();
			String log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
			LOG.info(log);
			
			if(!CollectionUtils.isEmpty(offerList)) {
				
				offerList = FilterValues.filterOfferList(offerList, Predicates.activeMerchantAndStore());
				eligibilityInfo.setOfferList(new ArrayList<>(1));
				offerList.forEach(o->eligibilityInfo.getOfferList().add(modelMapper.map(o, EligibleOffers.class)));
				List<String> offerTypeList = MapValues.mapOfferTypeIdFromEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.notEmptyOfferTypeIdForEligibleOffer());
				List<PurchasePaymentMethod>  purchasePaymentMethodList = repositoryHelper.getPurchasePaymentMethodsByPurchaseItemList(
						   ProcessValues.getPurchaseItemListFromOfferTypeList(offerTypeList));
				List<MarketplaceImage> imageList = repositoryHelper.getImageForOfferList(MapValues.mapEligibleOfferIdList(eligibilityInfo.getOfferList(), Predicates.notEmptyOfferIdForEligibleOffer()));	
				List<CustomerTypeEntity> customerTypeList = repositoryHelper.getAllCustomerTypes();
				LOG.info("Customer types fetched successfully from MM");
				
				for(EligibleOffers offer : eligibilityInfo.getOfferList()) {
			      
					ProcessValues.setPaymentMethodsForEligibleOffer(offer, purchasePaymentMethodList);
					ProcessValues.setImageListForEligibleOffer(offer, imageList);
					offer.setLastUpdateDate(new Date());
					
					if(!ObjectUtils.isEmpty(offer.getCustomerTypes())
					&& !CollectionUtils.isEmpty(offer.getCustomerTypes().getEligibleTypes())) {
						
						offer.getCustomerTypes().setEligibleTypes(FilterValues.filterStringListWithCondition(offer.getCustomerTypes().getEligibleTypes(), 
								Predicates.eligiblePaymentMethodsAvailable(customerTypeList, offer.getPaymentMethods())));;
					}					;
				}
				
				eligibilityInfo.setOfferList(repositoryHelper.saveAllEligibleOffers(eligibilityInfo.getOfferList()));
				eligibleOffersContextAware.addEligibleOfferListToContext(eligibilityInfo.getOfferList());
				log = Logs.logAfterHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
				LOG.info(log);
				sappServiceImpl.refreshEligibleOffersCache();
				
			}
			
		}
	}
		
	/**
	 * Maps eligible offer response to output response format
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 * @param conversionRateList 
	 * @param isEligibilityRequired
	 * @throws MarketplaceException
	 */
	public void mapToEligibleOfferResponse(EligibleOfferHelperDto eligibilityInfo,
			OfferCatalogResultResponse offerCatalogResultResponse, List<ConversionRate> conversionRateList,
			boolean subscriptionCheckRequired){
		
	  offerCatalogResultResponse.setOfferCatalogs(new ArrayList<>(1));	
		
	  if(!ObjectUtils.isEmpty(eligibilityInfo) 
	  && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
		 
		  PurchaseCount purchaseCount = ProcessValues.getPurchaseCountForCinemaOffersInEligibleOffers(eligibilityInfo);
		 		  
		  for(EligibleOffers offer : eligibilityInfo.getOfferList()) {
			  
			  OfferCatalogResultResponseDto offerCatalogDto = modelMapper.map(offer, OfferCatalogResultResponseDto.class);
			  ProcessValues.setUnmappedValuesForEligibleOffers(offerCatalogDto, offer, conversionRateList);
			  ProcessValues.setFreeOfferResponseInEligibleOffer(eligibilityInfo, offerCatalogDto, conversionRateList, subscriptionCheckRequired);
			  eligibilityInfo.setOffer(offer);
			  
			  if(eligibilityInfo.isMember()) {
				 
				  eligibilityInfo.setOffer(offer);
				  eligibilityInfo.setCommonSegmentNames(Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						  MapValues.mapCustomerSegmentInOfferLimits(offer.getLimit(), Predicates.isCustomerSegmentInLimits())));
				  eligibilityInfo.setOfferCounters(FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offer.getOfferId())));
				  ProcessValues.setMemberAttributesInEligibleOffer(offerCatalogDto, offer, eligibilityInfo);
				  ProcessValues.getMemberEligibilityForEligibleOffer(purchaseCount, eligibilityInfo, offerCatalogResultResponse, offerCatalogDto);
				  ProcessValues.getEligibleOfferDenominationLimitFailureResult(offerCatalogDto, offer.getDenominations());
			  } 
			
			  ProcessValues.setFreeOfferResponseInEligibleOffer(eligibilityInfo, offerCatalogDto, conversionRateList, subscriptionCheckRequired);
			  offerCatalogResultResponse.getOfferCatalogs().add(offerCatalogDto);	 
			  
		}
		  
	  }
	  
    }

	/**
	 * Sets member details for detailed eligible offer
	 * @param eligibilityInfo
	 * @param isMember
	 * @param offerCatalog 
	 * @param accountNumber 
	 * @param header 
	 * @param offerCatalogResultResponse
	 * @throws MarketplaceException 
	 */
	public void getMemberDetailsForDetailedEligibleOffer(EligibilityInfo eligibilityInfo, boolean isMember,
			OfferCatalog offerCatalog, Headers header, String accountNumber, OfferCatalogResultResponse offerCatalogResultResponse) throws MarketplaceException {
		
		if(isMember && !ObjectUtils.isEmpty(eligibilityInfo)) {
			  
			  eligibilityInfo.setOffer(offerCatalog);
			  ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, header, Arrays.asList(offerCatalog), isMember);
			  getMemberEligibilityForDetailedEligibleOffer(eligibilityInfo, accountNumber, offerCatalogResultResponse);
			  eligibilityInfo.setOfferCounters(FilterValues.findAnyOfferCounterinCounterList(eligibilityInfo.getOfferCounterList(), Predicates.sameOfferIdForLimit(offerCatalog.getOfferId())));
			  
			  if(Checks.checkNoErrors(offerCatalogResultResponse)
			 && !Checks.checkCinemaOffer(offerCatalog.getRules())
			 && !ObjectUtils.isEmpty(eligibilityInfo.getOffer().getCustomerSegments())) {
				 
				  Checks.checkCustomerSegment(eligibilityInfo.getRuleResult(), eligibilityInfo.getOffer().getCustomerSegments(), eligibilityInfo.getMemberDetails().getCustomerSegment(), offerCatalogResultResponse);
				  List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						  MapValues.mapCustomerSegmentInOfferLimits(offerCatalog.getLimit(), Predicates.isCustomerSegmentInLimits())); 
				  Checks.checkDownloadLimitLeft(eligibilityInfo.getOfferCounters(), offerCatalog, 0, eligibilityInfo, null, offerCatalogResultResponse, customerSegmentNames);
				  
			  }
			  
		  }
		
	}
	
	/**
	 * Sets member details for detailed eligible offer
	 * @param eligibilityInfo
	 * @param isMember
	 * @param offerCatalog 
	 * @param accountNumber 
	 * @param header 
	 * @param offerCatalogResultResponse
	 * @throws MarketplaceException 
	 */
	public void getMemberDetailsForEligibleOfferDetail(EligibilityInfo eligibilityInfo, boolean isMember,
			OfferCatalog offerCatalog, Headers header, String accountNumber, OfferCatalogResultResponse offerCatalogResultResponse) throws MarketplaceException {
		
		if(isMember && !ObjectUtils.isEmpty(eligibilityInfo)) {
			  
			  eligibilityInfo.setOffer(offerCatalog);
			  ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, header, Arrays.asList(offerCatalog), isMember);
			  getMemberEligibilityForDetailedEligibleOffer(eligibilityInfo, accountNumber, offerCatalogResultResponse);
			  eligibilityInfo.setAccountOfferCounter(FilterValues.findAnyAccountOfferCounterInList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
			  eligibilityInfo.setMemberOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
					? eligibilityInfo.getAccountOfferCounter().getMemberOfferCounter()
					: FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getMemberDetails().getMembershipCode())));
			  eligibilityInfo.setOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
					? eligibilityInfo.getAccountOfferCounter().getOfferCounter()
					: FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getOffer().getOfferId())));
			  

			  if(Checks.checkNoErrors(offerCatalogResultResponse)
			 && !Checks.checkCinemaOffer(offerCatalog.getRules())
			 && !ObjectUtils.isEmpty(eligibilityInfo.getOffer().getCustomerSegments())) {
				 
				  Checks.checkCustomerSegment(eligibilityInfo.getRuleResult(), eligibilityInfo.getOffer().getCustomerSegments(), eligibilityInfo.getMemberDetails().getCustomerSegment(), offerCatalogResultResponse);
				  List<String> customerSegmentNames = Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						  MapValues.mapCustomerSegmentInOfferLimits(offerCatalog.getLimit(), Predicates.isCustomerSegmentInLimits())); 
				  Checks.checkDownloadLimitLeft(null, offerCatalog, 0, eligibilityInfo, null, offerCatalogResultResponse, customerSegmentNames);
			  	  
			  }
			  
		  }
		
	}

	/**
	 * Sets response specific to member in detailed eligible offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 */
	public void setDetailedOfferResponseForMember(OfferCatalogResultResponseDto offerCatalogDto,
			OfferCatalog offerCatalog, EligibilityInfo eligibilityInfo,
			OfferCatalogResultResponse offerCatalogResultResponse) {
		
		  ProcessValues.setMemberAttributesInOffer(offerCatalogDto, offerCatalog, eligibilityInfo);
		  setBirthdayAvailedStatus(offerCatalogDto, eligibilityInfo);
		 
		  if(Checks.checkCinemaOffer(offerCatalog.getRules())) {
			  
			  PurchaseCount purchaseCount = ProcessValues.getPurchaseCountForCinemaOffers(eligibilityInfo);
			  offerCatalogDto.setEligibility(Responses.getMemberEligibility(purchaseCount, eligibilityInfo, offerCatalogResultResponse));
			  
		  }
			  
	}
	
	/**
	 * Sets response specific to member in detailed eligible offer response
	 * @param offerCatalogDto
	 * @param offerCatalog
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 */
	public void setDetailedEligibleOfferResponse(OfferCatalogResultResponseDto offerCatalogDto,
			OfferCatalog offerCatalog, EligibilityInfo eligibilityInfo,
			OfferCatalogResultResponse offerCatalogResultResponse) {
		
	  ProcessValues.setMemberAttributesInOffer(offerCatalogDto, offerCatalog, eligibilityInfo, eligibilityInfo.getConversionRateList());
	  setBirthdayAvailedStatus(offerCatalogDto, eligibilityInfo);
	  PurchaseCount purchaseCount =  Checks.checkCinemaOffer(offerCatalog.getRules())
			  ? ProcessValues.getValuesForPurchaseCount(eligibilityInfo.getOfferCountersList(), eligibilityInfo.getMemberOfferCounterList(), eligibilityInfo.getAccountOfferCounterList())
			  : null;	  
	  Responses.getMemberEligibility(purchaseCount, offerCatalogDto, eligibilityInfo, offerCatalogResultResponse);
				  
	}

	/**
	 * Filters list of eligible offers and converts to response list
	 * @param eligibleOffersRequest
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 */
	public void getFilteredEligibleOfferResponse(EligibleOffersFiltersRequest eligibleOffersRequest, EligibleOfferHelperDto eligibilityInfo,
			OfferCatalogResultResponse offerCatalogResultResponse, boolean subscriptionCheckRequired) {
		
		if(Checks.checkNoErrors(offerCatalogResultResponse)
		&& ProcessValues.filterAllEligibleOffers(eligibilityInfo, eligibleOffersRequest, offerCatalogResultResponse)) {
			
		    setCounterDetailsForEligibileOffers(eligibilityInfo);
		    offerCatalogResultResponse.setTotalRecordCount(eligibilityInfo.getOfferList().size());
		    List<String> partnerCodeList = MapValues.mapPartnerCodesFromEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.notEmptyPartnerCodeForEligibleOffer());	 
			List<String> offerTypeList = MapValues.mapOfferTypeIdFromEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.notEmptyOfferTypeIdForEligibleOffer());
			List<String> productTypeList = ProcessValues.getProductTypeList(offerTypeList);
			List<ConversionRate> conversionRateList = repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(partnerCodeList, productTypeList);
			mapToEligibleOfferResponse(eligibilityInfo, offerCatalogResultResponse,  conversionRateList, subscriptionCheckRequired);
			ProcessValues.sortEligibleOffers(offerCatalogResultResponse, eligibleOffersRequest);
            ProcessValues.applyPagination(eligibleOffersRequest, offerCatalogResultResponse);
         			
		}
			
		
	}
	
	/**
	 * Filters list of eligible offers and converts to response list
	 * @param eligibleOffersRequest
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 */
	public void filterAndGetEligibleOfferResponse(EligibleOffersFiltersRequest eligibleOffersRequest, EligibleOfferHelperDto eligibilityInfo,
			OfferCatalogResultResponse offerCatalogResultResponse, boolean subscriptionCheckRequired) {
		
		if(Checks.checkNoErrors(offerCatalogResultResponse)
		&& ProcessValues.filterAllEligibleOffers(eligibilityInfo, eligibleOffersRequest, offerCatalogResultResponse)) {
			
			if(eligibilityInfo.isMember()) {
				setOfferCounterValuesForEligibleOffers(eligibilityInfo);
			}
		    offerCatalogResultResponse.setTotalRecordCount(eligibilityInfo.getOfferList().size());
		    List<String> partnerCodeList = MapValues.mapPartnerCodesFromEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.notEmptyPartnerCodeForEligibleOffer());	 
			List<String> offerTypeList = MapValues.mapOfferTypeIdFromEligibleOfferList(eligibilityInfo.getOfferList(), Predicates.notEmptyOfferTypeIdForEligibleOffer());
			List<String> productTypeList = ProcessValues.getProductTypeList(offerTypeList);
			List<ConversionRate> conversionRateList = repositoryHelper.findConversionRateListByPartnerCodeAndProductItem(partnerCodeList, productTypeList);
			conversionRateList = FilterValues.filterConversionRateList(conversionRateList, Predicates.applicableRateForPartnerListAndChannelId(partnerCodeList, eligibilityInfo.getHeaders().getChannelId()));		
			mapEligibleOfferResponse(eligibilityInfo, offerCatalogResultResponse,  conversionRateList, subscriptionCheckRequired);
			ProcessValues.sortEligibleOffers(offerCatalogResultResponse, eligibleOffersRequest);
            ProcessValues.applyPagination(eligibleOffersRequest, offerCatalogResultResponse);
         			
		}
			
	}
	
	/**
	 * Maps eligible offer response to output response format
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 * @param conversionRateList 
	 * @param isEligibilityRequired
	 * @throws MarketplaceException
	 */
	public void mapEligibleOfferResponse(EligibleOfferHelperDto eligibilityInfo,
			OfferCatalogResultResponse offerCatalogResultResponse, List<ConversionRate> conversionRateList,
			boolean subscriptionCheckRequired){
		
	  offerCatalogResultResponse.setOfferCatalogs(new ArrayList<>(1));	
		
	  if(!ObjectUtils.isEmpty(eligibilityInfo) 
	  && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
		 
		  PurchaseCount purchaseCount = ProcessValues.getValuesForPurchaseCount(eligibilityInfo.getOfferCountersList(), eligibilityInfo.getMemberOfferCounterList(), eligibilityInfo.getAccountOfferCounterList());
		  
		  for(EligibleOffers offer : eligibilityInfo.getOfferList()) {
			  
			  List<ConversionRate> rateList = FilterValues.filterConversionRateList(conversionRateList, Predicates.applicableRateForPartnerListAndChannelId(Arrays.asList(offer.getPartnerCode()), eligibilityInfo.getHeaders().getChannelId()));
			  OfferCatalogResultResponseDto offerCatalogDto = modelMapper.map(offer, OfferCatalogResultResponseDto.class);
			  ProcessValues.setUnmappedValuesForEligibleOffers(offerCatalogDto, offer, rateList);
			  
			  eligibilityInfo.setOffer(offer);
			  if(eligibilityInfo.isMember()) {
				  
				  ProcessValues.setFreeOfferResponseInEligibleOffer(eligibilityInfo, offerCatalogDto, conversionRateList, subscriptionCheckRequired);
				  eligibilityInfo.setOffer(offer);
				  eligibilityInfo.setCommonSegmentNames(Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						  MapValues.mapCustomerSegmentInOfferLimits(offer.getLimit(), Predicates.isCustomerSegmentInLimits())));
				  eligibilityInfo.setAccountOfferCounter(FilterValues.findAnyAccountOfferCounterInList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
				  eligibilityInfo.setMemberOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
							? eligibilityInfo.getAccountOfferCounter().getMemberOfferCounter()
							: FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
				  eligibilityInfo.setOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
							? eligibilityInfo.getAccountOfferCounter().getOfferCounter()
							: FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getOffer().getOfferId())));
				  ProcessValues.setMemberAttributesInEligibleOffer(offerCatalogDto, offer, eligibilityInfo, rateList);
				  ProcessValues.getEligibilityForEligibleOffer(purchaseCount, eligibilityInfo, offerCatalogResultResponse, offerCatalogDto);
				  ProcessValues.getEligibleOfferDenominationLimitFailureResult(offerCatalogDto, offer.getDenominations());
			  } 
			
			  ProcessValues.setFreeOfferResponseInEligibleOffer(eligibilityInfo, offerCatalogDto, conversionRateList, subscriptionCheckRequired);
			  offerCatalogResultResponse.getOfferCatalogs().add(offerCatalogDto);	 
			  
		}
		  
	  }
	  
    }
	
	/**
	 * 
	 * @param headers
	 * @return response after setting all the eligible offers to the collection
	 */
	public EligibleOffersResultResponse getAndSaveEligibleOffersSync(Headers headers) {
		
		EligibleOffersResultResponse resultResponse = new EligibleOffersResultResponse(headers.getExternalTransactionId());
				
		try {
			    repositoryHelper.removeAllEligibleOffers();
			    EligibleOfferHelperDto eligibilityInfo = new EligibleOfferHelperDto();
				eligibilityInfo.setHeaders(headers);
				populateEligibleOffers(eligibilityInfo);
				resultResponse.setEligibleOfferList(eligibilityInfo.getOfferList());
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_AND_SAVE_ELIGIBLE_OFFERS.get(), e, null, OfferErrorCodes.ELIGIBLE_OFFERS_CONFIGURATION_FAILED,
					OfferExceptionCodes.CONFIGURE_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), null, headers.getUserName())));
			
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.ELIGIBLE_OFFERS_CONFIGURATION_FAILED, OfferSuccessCodes.ELIGIBLE_OFFERS_CONFIGURATION_SUCCESSFULL);
		return resultResponse;
	}
	
	/**
	 * 
	 * @param headers
	 * @return eligible offers in context variable
	 */
	public EligibleOffersResultResponse getEligibleOfferListInContext(Headers headers) {
		
		EligibleOffersResultResponse resultResponse = new EligibleOffersResultResponse(headers.getExternalTransactionId());
		resultResponse.setEligibleOfferList(eligibleOffersContextAware.fetchEligibleOfferList());
		resultResponse.setTotalRecordCount(!CollectionUtils.isEmpty(resultResponse.getEligibleOfferList()) ? resultResponse.getEligibleOfferList().size(): 0);
		Responses.setResponse(resultResponse, OfferErrorCodes.SINGLETON_RECORDS_RETRIEVAL_FAILED, OfferSuccessCodes.SINGLETON_RECORDS_RETRIEVED_SUCCESSFULLY);
		return resultResponse;
	}
	
	/**
	 * Resets list of all offer counters
	 * @param headers
	 * @param string
	 */
	public void resetCounterList(Headers headers, String api) {
		
		resetOfferCounterList(repositoryHelper.getAllOfferCounters(), headers, api);
		resetMemberOfferCounterList(repositoryHelper.getAllMemberOfferCounters(), headers, api);
		resetAccountOfferCounterList(repositoryHelper.getAllAccountOfferCounters(), headers, api);
		
	}
	
	/**
	 * Resets all offer counters in the batch process
	 * @param counters
	 * @param headers
	 * @param api
	 */
	private void resetOfferCounterList(List<OfferCounters> counters, Headers headers, String api) {
	
		List<OfferCounters> originalCounterList = new ArrayList<>(counters.size());
		originalCounterList.addAll(counters);
		
		if(!CollectionUtils.isEmpty(counters)) {
			
			for(OfferCounters counter : counters) {
				
				TimeLimits timeLimit = modelMapper.map(counter, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				counter.setDailyCount(timeLimit.getDailyCount());
				counter.setWeeklyCount(timeLimit.getWeeklyCount());
				counter.setMonthlyCount(timeLimit.getMonthlyCount());
				counter.setAnnualCount(timeLimit.getAnnualCount());
				counter.setLastPurchased(new Date());
				 
				if(!CollectionUtils.isEmpty(counter.getDenominationCount())) {
					
					counter.setDenominationCount(getResetDenominationCounterList(counter.getDenominationCount()));
				}

			}
			
			List<OfferCounters> savedCounters = repositoryHelper.saveAllOfferCounter(counters);
			auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedCounters, api, originalCounterList,  headers.getExternalTransactionId(), headers.getUserName());
		}
		
	}
	
	/**
	 * Resets all member offer counters in the batch process
	 * @param counters
	 * @param headers
	 * @param api
	 */
	private void resetMemberOfferCounterList(List<MemberOfferCounts> counters, Headers headers, String api) {
	
		List<MemberOfferCounts> originalCounterList = new ArrayList<>(counters.size());
		originalCounterList.addAll(counters);
		
		if(!CollectionUtils.isEmpty(counters)) {
			
			for(MemberOfferCounts counter : counters) {
				
				TimeLimits timeLimit = modelMapper.map(counter, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				counter.setDailyCount(timeLimit.getDailyCount());
				counter.setWeeklyCount(timeLimit.getWeeklyCount());
				counter.setMonthlyCount(timeLimit.getMonthlyCount());
				counter.setAnnualCount(timeLimit.getAnnualCount());
				counter.setLastPurchased(new Date());
				 
				if(!CollectionUtils.isEmpty(counter.getDenominationCount())) {
					
					counter.setDenominationCount(getResetDenominationCounterList(counter.getDenominationCount()));
				}

			}
			
			List<MemberOfferCounts> savedCounters = repositoryHelper.saveAllMemberOfferCounters(counters);
			auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedCounters, api, originalCounterList,  headers.getExternalTransactionId(), headers.getUserName());
		}
		
	}
	
	/**
	 * Resets all account offer counters in the batch process
	 * @param counters
	 * @param headers
	 * @param api
	 */
	private void resetAccountOfferCounterList(List<AccountOfferCounts> counters, Headers headers, String api) {
	
		List<AccountOfferCounts> originalCounterList = new ArrayList<>(counters.size());
		originalCounterList.addAll(counters);
		
		if(!CollectionUtils.isEmpty(counters)) {
			
			for(AccountOfferCounts counter : counters) {
				
				TimeLimits timeLimit = modelMapper.map(counter, TimeLimits.class);
				ProcessValues.resetCounter(timeLimit);
				counter.setDailyCount(timeLimit.getDailyCount());
				counter.setWeeklyCount(timeLimit.getWeeklyCount());
				counter.setMonthlyCount(timeLimit.getMonthlyCount());
				counter.setAnnualCount(timeLimit.getAnnualCount());
				counter.setLastPurchased(new Date());
				 
				if(!CollectionUtils.isEmpty(counter.getDenominationCount())) {
					
					counter.setDenominationCount(getResetDenominationCounterList(counter.getDenominationCount()));
				}

			}
			
			List<AccountOfferCounts> savedCounters = repositoryHelper.saveAllAccountOfferCounters(counters);
			auditService.updateDataAudit(OffersDBConstants.OFFER_COUNTERS, savedCounters, api, originalCounterList,  headers.getExternalTransactionId(), headers.getUserName());
		}
		
	}
	
	/**
	 * Sets the values of counters for list of offers 
	 * @param eligibilityInfo
	 */
	private void setOfferCounterValuesForEligibleOffers(EligibleOfferHelperDto eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			if(Checks.checkAllCinemaOfferInEligibleOffers(eligibilityInfo.getOfferList())) {
				
				eligibilityInfo.setOfferCountersList(repositoryHelper.findCinemaOfferCounters());
				eligibilityInfo.setCounterOfferIdList(MapValues.mapOfferIdFromOfferCounterList(eligibilityInfo.getOfferCountersList(), Predicates.nonEmptyOfferIdInOfferCounter()));
				eligibilityInfo.setMemberOfferCounterList(repositoryHelper.findCurrentMemberCountersForOfferList(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				eligibilityInfo.setAccountOfferCounterList(repositoryHelper.findCurrentAccountCountersForOfferList(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				
			} else if(Checks.checkCinemaAndNonCinemaOfferInEligibleOffers(eligibilityInfo.getOfferList())){
			
				eligibilityInfo.setCounterOfferIdList(MapValues.mapEligibleOfferIdList(eligibilityInfo.getOfferList(), Predicates.isNotCinemaOfferEligibleOffer()));
				eligibilityInfo.setOfferCountersList(repositoryHelper.findCinemaAndNonCinemaOfferCounters(eligibilityInfo.getCounterOfferIdList()));
				eligibilityInfo.setCounterOfferIdList(MapValues.mapOfferIdFromOfferCounterList(eligibilityInfo.getOfferCountersList(), Predicates.nonEmptyOfferIdInOfferCounter()));
				eligibilityInfo.setMemberOfferCounterList(repositoryHelper.findCurrentMemberCountersForOfferList(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				eligibilityInfo.setAccountOfferCounterList(repositoryHelper.findCurrentAccountCountersForOfferList(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
			
			
			} else {
				
				eligibilityInfo.setCounterOfferIdList(MapValues.mapEligibleOfferIdList(eligibilityInfo.getOfferList(), Predicates.isOfferIdPresentInEligibleOffer()));
				eligibilityInfo.setAccountOfferCounterList(repositoryHelper.findCurrentAccountCountersForOfferList(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				
				if(ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())) {
					
					eligibilityInfo.setCounterOfferIdList(FilterValues.filterStringListWithCondition(eligibilityInfo.getCounterOfferIdList(), Predicates.accountCounterNotPresent(eligibilityInfo.getAccountOfferCounterList(), eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode())));
					eligibilityInfo.setMemberOfferCounterList(repositoryHelper.findCurrentMemberCountersForOfferList(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
					eligibilityInfo.setOfferCountersList(repositoryHelper.getCounterListForOfferList(eligibilityInfo.getCounterOfferIdList()));
				}
				
			}
			
		}
		
	}
	
	/**
	 * Set member related details for an account number
	 * @param eligibilityInfo
	 * @param accountNumber
	 * @param resultResponse
	 * @throws MarketplaceException
	 */
	public boolean checkMemberEligbilityForOffers(EligibilityInfo eligibilityInfo, String accountNumber, ResultResponse resultResponse) throws MarketplaceException {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo) && eligibilityInfo.isMember()) {
			
			if(ObjectUtils.isEmpty(eligibilityInfo.getMemberDetails())) {
			
				GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, eligibilityInfo.getHeaders());
				eligibilityInfo.setMemberDetails(memberDetails);
				if(!ObjectUtils.isEmpty(eligibilityInfo.getAdditionalDetails())) {
					
					eligibilityInfo.getAdditionalDetails().setSubscribed(memberDetails.isSubscribed());
					
				}

				eligibilityInfo.setCustomerSegmentCheckRequired(Checks.checkIsCustomerSegmentCheckRequired(eligibilityInfo.getOfferList()));
			}
			
			if(Checks.checkNoErrors(resultResponse)) {
				
				if(eligibilityInfo.isBirthdayInfoRequired()) {
					
					BirthdayDurationInfoDto birthdayDurationInfoDto = ProcessValues.getBirthdayDurationInfoDto(repositoryHelper.findBirthdayInfo(), eligibilityInfo.getMemberDetails());
					eligibilityInfo.setBirthdayDurationInfoDto(birthdayDurationInfoDto);
					eligibilityInfo.setPurchaseHistoryList(repositoryHelper.findBirthdayBillPaymentAndRechargePurchaseRecords(eligibilityInfo.getMemberDetails().getAccountNumber(), 
								eligibilityInfo.getMemberDetails().getMembershipCode(), birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate()));
					
				}
				
				setCustomerTypeDetails(eligibilityInfo, resultResponse);
				setCustomerSegmentDetails(eligibilityInfo, accountNumber, resultResponse);
				
				if(Checks.checkNoErrors(resultResponse)) {
					setOfferCounterValues(eligibilityInfo);
				}
					
			}
			
		}
		return Checks.checkNoErrors(resultResponse);
	}
	
	/**
	 * Sets the values of counters for list of offers 
	 * @param eligibilityInfo
	 */
	public void setOfferCounterValues(EligibilityInfo eligibilityInfo) {
		
		if(!ObjectUtils.isEmpty(eligibilityInfo)) {
			
			if(Checks.checkAllCinemaOffer(eligibilityInfo.getOfferList())) {
				
				eligibilityInfo.setOfferCountersList(repositoryHelper.findCinemaOfferCounters());
				eligibilityInfo.setCounterOfferIdList(MapValues.mapOfferIdFromOfferCounterList(eligibilityInfo.getOfferCountersList(), Predicates.nonEmptyOfferIdInOfferCounter()));
				eligibilityInfo.setMemberOfferCounterList(repositoryHelper.findCurrentMemberCountersForOfferList(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				eligibilityInfo.setAccountOfferCounterList(repositoryHelper.findCurrentAccountCountersForOfferList(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				
			} else if(Checks.checkCinemaAndNonCinemaOffer(eligibilityInfo.getOfferList())){
			
				eligibilityInfo.setCounterOfferIdList(MapValues.mapOfferIdList(eligibilityInfo.getOfferList(), Predicates.isNotCinemaOffer()));
				eligibilityInfo.setOfferCountersList(repositoryHelper.findCinemaAndNonCinemaOfferCounters(eligibilityInfo.getCounterOfferIdList()));
				eligibilityInfo.setCounterOfferIdList(MapValues.mapOfferIdFromOfferCounterList(eligibilityInfo.getOfferCountersList(), Predicates.nonEmptyOfferIdInOfferCounter()));
				eligibilityInfo.setMemberOfferCounterList(repositoryHelper.findCurrentMemberCountersForOfferList(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				eligibilityInfo.setAccountOfferCounterList(repositoryHelper.findCurrentAccountCountersForOfferList(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
			
			} else if(!CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
				
				eligibilityInfo.setCounterOfferIdList(MapValues.mapOfferIdFromOfferList(eligibilityInfo.getOfferList()));
				eligibilityInfo.setAccountOfferCounterList(repositoryHelper.findCurrentAccountCountersForOfferList(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
				
				if(!ObjectUtils.isEmpty(eligibilityInfo.getOffer())) {
					
					eligibilityInfo.setAccountOfferCounter(FilterValues.findAnyAccountOfferCounterInList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getOffer().getOfferId())));
					
				}
				
				if(ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())) {
					
					eligibilityInfo.setCounterOfferIdList(FilterValues.filterStringListWithCondition(eligibilityInfo.getCounterOfferIdList(), Predicates.accountCounterNotPresent(eligibilityInfo.getAccountOfferCounterList(), eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode())));
					eligibilityInfo.setMemberOfferCounterList(repositoryHelper.findCurrentMemberCountersForOfferList(eligibilityInfo.getMemberDetails().getMembershipCode(), eligibilityInfo.getCounterOfferIdList()));
					eligibilityInfo.setOfferCountersList(repositoryHelper.getCounterListForOfferList(eligibilityInfo.getCounterOfferIdList()));
				}
				
			}
			
		}
		
	}
	
	/**
	 * Maps eligible offers entity to response list
	 * @param eligibilityInfo
	 * @param offerCatalogResultResponse
	 * @param isEligibilityRequired
	 * @return list of eligible offers 
	 * @throws MarketplaceException
	 */
	public List<OfferCatalogResultResponseDto> listOfEligibleOffers(EligibilityInfo eligibilityInfo,
			ResultResponse offerCatalogResultResponse, boolean isEligibilityRequired, boolean subscriptionCheckRequired) throws MarketplaceException {
		
	  List<OfferCatalogResultResponseDto> offerCatalogList = null;
	
	  if(!ObjectUtils.isEmpty(eligibilityInfo) 
	  && !CollectionUtils.isEmpty(eligibilityInfo.getOfferList())) {
		 
		  offerCatalogList = new ArrayList<>(eligibilityInfo.getOfferList().size());
		  List<ConversionRate> conversionRateList = getConversionRateListForOfferList(eligibilityInfo.getOfferList(), eligibilityInfo.getHeaders().getChannelId());
		  List<PurchasePaymentMethod> purchasePaymentMethodList = getPurchasePaymentMethodForOfferList(eligibilityInfo.getOfferList());
		  List<MarketplaceImage> imageList = getImageUrlListForOfferList(eligibilityInfo.getOfferList());	
		  PurchaseCount purchaseCount = ProcessValues.getValuesForPurchaseCount(eligibilityInfo.getOfferCountersList(), eligibilityInfo.getMemberOfferCounterList(), eligibilityInfo.getAccountOfferCounterList());
		  		  
		  for(OfferCatalog offerCatalog : eligibilityInfo.getOfferList()) {
			  
			  List<ConversionRate> rateList = FilterValues.filterConversionRateList(conversionRateList, Predicates.applicableRateForPartnerListAndChannelId(Arrays.asList(offerCatalog.getPartnerCode()), eligibilityInfo.getHeaders().getChannelId()));		
			  //true for including offer level payment methods for selecting eligible payment methods
			  OfferCatalogResultResponseDto offerCatalogDto = getOfferResponse(offerCatalog, purchasePaymentMethodList, rateList, imageList, true);
	       	  
	       	  ProcessValues.setFreeOfferResponse(offerCatalogDto, eligibilityInfo, subscriptionCheckRequired, conversionRateList);
			  
			  if(eligibilityInfo.isMember()) {
				 
				  eligibilityInfo.setOffer(offerCatalog);
				  eligibilityInfo.setCommonSegmentNames(Utilities.intersection(eligibilityInfo.getMemberDetails().getCustomerSegment(),
						  MapValues.mapCustomerSegmentInOfferLimits(offerCatalog.getLimit(), Predicates.isCustomerSegmentInLimits())));
				  eligibilityInfo.setAccountOfferCounter(FilterValues.findAnyAccountOfferCounterInList(eligibilityInfo.getAccountOfferCounterList(), Predicates.isCounterWithAccountNumberAndOfferId(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getMemberDetails().getAccountNumber(), eligibilityInfo.getMemberDetails().getMembershipCode())));
				  eligibilityInfo.setMemberOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
						? eligibilityInfo.getAccountOfferCounter().getMemberOfferCounter()
						: FilterValues.findAnyMemberOfferCounterInList(eligibilityInfo.getMemberOfferCounterList(), Predicates.isCounterWithMembershipCodeAndOfferId(eligibilityInfo.getOffer().getOfferId(), eligibilityInfo.getMemberDetails().getMembershipCode())));
				  eligibilityInfo.setOfferCounter(!ObjectUtils.isEmpty(eligibilityInfo.getAccountOfferCounter())
						? eligibilityInfo.getAccountOfferCounter().getOfferCounter()
						: FilterValues.findAnyOfferCounterInList(eligibilityInfo.getOfferCountersList(), Predicates.isCounterWithOfferId(eligibilityInfo.getOffer().getOfferId())));
				  ProcessValues.setMemberAttributesInOffer(offerCatalogDto, offerCatalog, eligibilityInfo, rateList);
				  ProcessValues.setExtraInformation(offerCatalog, offerCatalogDto, eligibilityInfo, isEligibilityRequired, purchaseCount, offerCatalogResultResponse);
			  } 
			
			  offerCatalogList.add(offerCatalogDto);
			  
		}
		  
	  }
	  
      return offerCatalogList;
		
	}

	/**
	 * Gift offer as part of promotional gift
	 * @param promotionalGiftRequest
	 * @param headers
	 * @param promotionalGiftResult
	 */
	public PurchaseResultResponse giftPromotionalOffer(PromotionalGiftRequestDto promotionalGiftRequest, Headers headers,
			OfferGiftValues offerGiftValues) {
		
		PurchaseResultResponse purchaseResultResponse = new PurchaseResultResponse(headers.getExternalTransactionId());
		
		if(!StringUtils.isEmpty(offerGiftValues.getOfferId())) {
			
			LOG.info("Gifting {} vouchers for offerId: {}", offerGiftValues.getCouponQuantity(), offerGiftValues.getOfferId());
			PurchaseRequestDto purchaseRequest = ProcessValues.getPurchaseRequestForPromotionalGiftOffer(promotionalGiftRequest, offerGiftValues, headers);
			purchaseResultResponse = purchaseDomain.validateAndSavePurchaseHistory(purchaseRequest, purchaseResultResponse, headers);
			LOG.info("Voucher Response : {}", purchaseResultResponse);;
			
			if(!ObjectUtils.isEmpty(purchaseResultResponse) 
			&& !ObjectUtils.isEmpty(purchaseResultResponse.getPurchaseResponseDto())
			&& !StringUtils.isEmpty(purchaseResultResponse.getPurchaseResponseDto().getTransactionNo())) {
				
				PurchaseHistory purchaseHistory = repositoryHelper.getPurchaseRecordById(purchaseResultResponse.getPurchaseResponseDto().getTransactionNo());
				
				if(!ObjectUtils.isEmpty(purchaseHistory)) {
					
					Gson gson = new Gson();

					PurchaseHistory originalPurchaseHistory = gson.fromJson(gson.toJson(purchaseHistory), PurchaseHistory.class);
					
					if(ObjectUtils.isEmpty(purchaseHistory.getAdditionalDetails())) {
						
						purchaseHistory.setAdditionalDetails(new AdditionalDetails());
					}
					
					purchaseHistory.getAdditionalDetails().setFree(true);
					purchaseHistory.getAdditionalDetails().setPromotionalGift(true);
					purchaseHistory.getAdditionalDetails().setPromoGiftId(promotionalGiftRequest.getPromotionalGiftId());
					purchaseHistory = repositoryHelper.savePurchaseHistory(purchaseHistory);
					
					if(!ObjectUtils.isEmpty(purchaseHistory)) {
					
						auditService.updateDataAudit(OffersRequestMappingConstants.PROMOTIONAL_GIFT, purchaseHistory, OffersDBConstants.PURCHASE_HISTORY, originalPurchaseHistory,  headers.getExternalTransactionId(), headers.getUserName());
					
					}
				}
				
			}
		}
		
		return purchaseResultResponse;

	}
	
	
	/**
	 * Subscribe account as part of promotional gift
	 * @param isSubscribed
	 * @param promotionalGiftRequest
	 * @param headers
	 * @param promotionalGiftResult
	 */
	public PurchaseResultResponse giftPromotionalSubscription(PromotionalGiftHelper promotionalGiftHelper, PromotionalGiftRequestDto promotionalGiftRequest, Headers headers,
			String subscriptionCatalogId) {
		
		PurchaseResultResponse purchaseResultResponse = new PurchaseResultResponse(headers.getExternalTransactionId());
		
		if(!StringUtils.isEmpty(subscriptionCatalogId)) {
			
			LOG.info("Gifting Subscription Id : {}", subscriptionCatalogId);
			SubscriptionValues subscriptionValues = repositoryHelper.getSubscriptionCatalogValues(subscriptionCatalogId);
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(subscriptionValues), OfferErrorCodes.SUBSCRIPTION_CATALOG_NOT_FOUND, purchaseResultResponse)) {
				
				PurchaseRequestDto purchaseRequestDto = ProcessValues.getPurchaseRequestForPromotionalGiftSubscription(promotionalGiftRequest, subscriptionCatalogId, subscriptionValues, headers);
				purchaseResultResponse = subscriptionManagementController.createSubscription(purchaseRequestDto, headers);
				LOG.info("Subscription Response : {}", purchaseResultResponse);
				
				if(Checks.checkNoErrors(purchaseResultResponse)) {
					
					updatePurchaseHistoryForPromotionalSubscription(promotionalGiftRequest, purchaseResultResponse, headers);
				}
			}			
			
		}
		
		return purchaseResultResponse;
    }

	
	/**
	 * 
	 * @param isSubscribed
	 * @param promotionalGiftRequest 
	 * @param purchaseResultResponse
	 * @param headers 
	 */
	private void updatePurchaseHistoryForPromotionalSubscription(PromotionalGiftRequestDto promotionalGiftRequest, PurchaseResultResponse purchaseResultResponse, Headers headers) {
		
		if(!Checks.checkNoErrors(purchaseResultResponse)) {
			
			PurchaseHistory purchaseHistory = repositoryHelper.getPurchaseRecordByPurchaseItemAndTransactionId(OffersConfigurationConstants.subscriptionItem, headers.getExternalTransactionId());
			
			if(!ObjectUtils.isEmpty(purchaseHistory)) {
				
				Gson gson = new Gson();

				PurchaseHistory originalPurchaseHistory = gson.fromJson(gson.toJson(purchaseHistory), PurchaseHistory.class);
				
				if(ObjectUtils.isEmpty(purchaseHistory.getAdditionalDetails())) {
					
					purchaseHistory.setAdditionalDetails(new AdditionalDetails());
				}
				
				purchaseHistory.getAdditionalDetails().setFree(true);
				purchaseHistory.getAdditionalDetails().setPromotionalGift(true);
				purchaseHistory.getAdditionalDetails().setPromoGiftId(promotionalGiftRequest.getPromotionalGiftId());
				purchaseHistory = repositoryHelper.savePurchaseHistory(purchaseHistory);
				
				if(!ObjectUtils.isEmpty(purchaseHistory)) {
				
					auditService.updateDataAudit(OffersRequestMappingConstants.PROMOTIONAL_GIFT, purchaseHistory, OffersDBConstants.PURCHASE_HISTORY, originalPurchaseHistory,  headers.getExternalTransactionId(), headers.getUserName());
				
				}
			}
			
		}

		
	}
	
	/**
	 * Validates member details and purchases an item
	 * @param eligibilityInfo
	 * @param purchaseRequest
	 * @param headers
	 * @param purchaseResultResponse
	 * @throws MarketplaceException 
	 */
	public void validateMemberDetailsAndPurchaseItem(EligibilityInfo eligibilityInfo,
			PurchaseRequestDto purchaseRequest, PurchaseResultResponse purchaseResultResponse, Headers headers) throws MarketplaceException {
		
		if(Checks.checkMemberDetails(purchaseRequest, eligibilityInfo.getMemberDetails(), eligibilityInfo.getPurchasepaymentMethod(), eligibilityInfo.getOffer().getPaymentMethods(), purchaseResultResponse)) {
    		
    		applyPromoCode(purchaseRequest, eligibilityInfo, purchaseResultResponse);
    		purchaseRequest.setMembershipCode(eligibilityInfo.getMemberDetails().getMembershipCode());
    		eligibilityInfo.setAccountNumber(purchaseRequest.getAccountNumber());
    		eligibilityInfo.getAdditionalDetails().setFree(ProcessValues.getIsFree(eligibilityInfo, purchaseRequest));
    		Checks.checkValidAmountPaid(purchaseRequest, eligibilityInfo, purchaseResultResponse);
    		ProcessValues.setValuesForFullPointsMethod(purchaseRequest, eligibilityInfo, purchaseResultResponse);
    		setCustomerTypeDetails(eligibilityInfo, purchaseResultResponse);
    		purchaseMarketplaceItem(purchaseRequest, eligibilityInfo, purchaseResultResponse, headers);
    		
    	}
		
	}

	/**
	 * 
	 * @param record
	 * @param transactionList
	 * @param paymentMethods
	 * @param voucherDetails
	 * @param transactionsResultResponse
	 */
	public void setTransactionValuesFromPurchaseRecord(PurchaseHistory purchaseRecord, List<TransactionListDto> transactionList,
			List<PaymentMethod> paymentMethods, List<VoucherListResult> voucherDetails,
			TransactionsResultResponse transactionsResultResponse) {
		
		TransactionListDto transaction = modelMapper.map(purchaseRecord, TransactionListDto.class);
			transaction.setProgramActivity(ProcessValues.getProgramActivity(purchaseRecord.getPurchaseItem()));	
			transaction.setSpentPoints(ProcessValues.getNegativeValuesForPositiveValues(purchaseRecord.getSpentPoints()));
			transaction.setPreferredNumber(purchaseRecord.getPreferredNumber());
			transaction.setRollBackFlag(purchaseRecord.isRollBackFlag());
			
			PaymentMethod currentPaymentMethod = FilterValues.findAnyPaymentMethodinMethodList(paymentMethods, Predicates.sameDescriptionForPaymentMethod(transaction.getPaymentMethod()));
			
			if(!ObjectUtils.isEmpty(currentPaymentMethod)) {
			   
			   transaction.setPaymentMethodId(currentPaymentMethod.getPaymentMethodId());
		    }
			
			if(!CollectionUtils.isEmpty(voucherDetails) && !purchaseRecord.isRollBackFlag()) {
				
				transaction.setRollBackFlag(Checks.checkRollBackStatusForTransaction(FilterValues.filterVoucherDetailsList(voucherDetails, Predicates.sameIdAndVoucherCodeForTransaction(transaction.getId(), transaction.getVoucherCode()))));
			
			} else {
				
				transaction.setRollBackFlag(purchaseRecord.isRollBackFlag());
			}
		   
		    transaction.setVoucherDetails(FilterValues.filterVoucherDetailsList(voucherDetails, Predicates.sameIdAndVoucherCodeForTransaction(transaction.getId(), transaction.getVoucherCode())));
		    transactionList.add(transaction);
		    Responses.removeLastError(transactionsResultResponse);
		
	}

	/***
	 * Reset all offer related counters
	 * @param headers
	 * @param string
	 */
	 public void resetCounters(ResultResponse resultResponse) {
		
			try {
			repositoryHelper.resetCounters(OfferCounters.class, OffersDBConstants.OFFER_COUNTERS);
			repositoryHelper.resetCounters(MemberOfferCounts.class, OffersDBConstants.MEMBER_OFFER_COUNTERS);
			repositoryHelper.resetCounters(AccountOfferCounts.class, OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
			
			resultResponse.setResult(OfferSuccessCodes.RESET_ALL_COUNTER_SUCCESS.getId(),
					OfferSuccessCodes.RESET_ALL_COUNTER_SUCCESS.getMsg());
			
		} catch(Exception e) {
			resultResponse.addErrorAPIResponse(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(OfferErrorCodes.RESET_ALL_COUNTER_FAILURE.getId(),
					OfferErrorCodes.RESET_ALL_COUNTER_FAILURE.getMsg());
		}
	}


	
	public void populateOfferDetails(OfferCatalog offerCatalog, String[] splittedContent) throws MarketplaceException{
		
		try {
		
			OfferDetails offerDetails = !ObjectUtils.isEmpty(offerCatalog.getOffer()) ? offerCatalog.getOffer() : new OfferDetails();
			
			OfferLabel offerLabel = !ObjectUtils.isEmpty(offerDetails.getOfferLabel()) ? offerDetails.getOfferLabel() : new OfferLabel();
			if(!StringUtils.isEmpty(splittedContent[7])) offerLabel.setOfferLabelEn(splittedContent[7]);
			if(!StringUtils.isEmpty(splittedContent[8])) offerLabel.setOfferLabelAr(splittedContent[8]);
			if(!(StringUtils.isEmpty(offerLabel.getOfferLabelEn()) && StringUtils.isEmpty(offerLabel.getOfferLabelAr()))) offerDetails.setOfferLabel(offerLabel);
			
			OfferTitle offerTitle = !ObjectUtils.isEmpty(offerDetails.getOfferTitle()) ? offerDetails.getOfferTitle() : new OfferTitle();
			if(!StringUtils.isEmpty(splittedContent[9])) offerTitle.setOfferTitleEn(splittedContent[9]);
			if(!StringUtils.isEmpty(splittedContent[10])) offerTitle.setOfferTitleAr(splittedContent[10]);
			if(!(StringUtils.isEmpty(offerTitle.getOfferTitleEn()) && StringUtils.isEmpty(offerTitle.getOfferTitleAr()))) offerDetails.setOfferTitle(offerTitle);
			
			OfferTitleDescription offerTitleDescription  = !ObjectUtils.isEmpty(offerDetails.getOfferTitleDescription()) ? offerDetails.getOfferTitleDescription() : new OfferTitleDescription();
			if(!StringUtils.isEmpty(splittedContent[11])) offerTitleDescription.setOfferTitleDescriptionEn(splittedContent[11]);
			if(!StringUtils.isEmpty(splittedContent[12])) offerTitleDescription.setOfferTitleDescriptionAr(splittedContent[12]);
			if(!(StringUtils.isEmpty(offerTitleDescription.getOfferTitleDescriptionEn()) && StringUtils.isEmpty(offerTitleDescription.getOfferTitleDescriptionAr()))) offerDetails.setOfferTitleDescription(offerTitleDescription);
			
			OfferDetailMobile offerDetailMobile = !ObjectUtils.isEmpty(offerDetails.getOfferMobile()) ? offerDetails.getOfferMobile() : new OfferDetailMobile();
			if(!StringUtils.isEmpty(splittedContent[13])) offerDetailMobile.setOfferDetailMobileEn(splittedContent[13]);
			if(!StringUtils.isEmpty(splittedContent[14])) offerDetailMobile.setOfferDetailMobileAr(splittedContent[14]);
			if(!(StringUtils.isEmpty(offerDetailMobile.getOfferDetailMobileEn()) && StringUtils.isEmpty(offerDetailMobile.getOfferDetailMobileAr()))) offerDetails.setOfferMobile(offerDetailMobile);
			
			OfferDetailWeb offerWeb = !ObjectUtils.isEmpty(offerDetails.getOfferWeb()) ? offerDetails.getOfferWeb() : new OfferDetailWeb();
			if(!StringUtils.isEmpty(splittedContent[15])) offerWeb.setOfferDetailWebEn(splittedContent[15]);
			if(!StringUtils.isEmpty(splittedContent[16])) offerWeb.setOfferDetailWebAr(splittedContent[16]);
			if(!(StringUtils.isEmpty(offerWeb.getOfferDetailWebEn()) && StringUtils.isEmpty(offerWeb.getOfferDetailWebAr()))) offerDetails.setOfferWeb(offerWeb);
	
			if(!(ObjectUtils.isEmpty(offerDetails.getOfferLabel()) && ObjectUtils.isEmpty(offerDetails.getOfferMobile()) && ObjectUtils.isEmpty(offerDetails.getOfferTitle())
					&& ObjectUtils.isEmpty(offerDetails.getOfferWeb()) && ObjectUtils.isEmpty(offerDetails.getOfferTitleDescription()))) offerCatalog.setOffer(offerDetails);
		} catch(Exception e) {
			LOG.info("Inside populateOfferDetails catch block");
			throw new MarketplaceException(this.getClass().toString(), "populateOfferDetails",
					e.getClass() + e.getMessage(), OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION);
		}
	}
	
	public void populateOtherOfferCatalogDetails(OfferCatalog offerCatalog, String[] splittedContent) throws MarketplaceException{
		try {
			TermsConditions termsAndConditions = !ObjectUtils.isEmpty(offerCatalog.getTermsAndConditions()) ? offerCatalog.getTermsAndConditions() : new TermsConditions();
			
			TAndC tAndC = !ObjectUtils.isEmpty(termsAndConditions.getTermsAndConditions()) ? termsAndConditions.getTermsAndConditions() : new TAndC();
			if(!StringUtils.isEmpty(splittedContent[19])) tAndC.setTermsAndConditionsEn(splittedContent[19]);
			if(!StringUtils.isEmpty(splittedContent[20])) tAndC.setTermsAndConditionsAr(splittedContent[20]);
			if(!(StringUtils.isEmpty(tAndC.getTermsAndConditionsEn()) && StringUtils.isEmpty(tAndC.getTermsAndConditionsAr()))) termsAndConditions.setTermsAndConditions(tAndC);
			
			AddTAndC addTAndC = !ObjectUtils.isEmpty(termsAndConditions.getAddTermsAndConditions()) ? termsAndConditions.getAddTermsAndConditions() : new AddTAndC();
			if(!StringUtils.isEmpty(splittedContent[21])) addTAndC.setAdditionalTermsAndConditionsEn(splittedContent[21]);
			if(!StringUtils.isEmpty(splittedContent[22])) addTAndC.setAdditionalTermsAndConditionsAr(splittedContent[22]);
			if(!(StringUtils.isEmpty(addTAndC.getAdditionalTermsAndConditionsEn()) && StringUtils.isEmpty(addTAndC.getAdditionalTermsAndConditionsAr()))) termsAndConditions.setAddTermsAndConditions(addTAndC);
			if(!(ObjectUtils.isEmpty(termsAndConditions.getTermsAndConditions()) && ObjectUtils.isEmpty(termsAndConditions.getAddTermsAndConditions()))) offerCatalog.setTermsAndConditions(termsAndConditions);
			
			BrandDescription brandDescription = !ObjectUtils.isEmpty(offerCatalog.getBrandDescription()) ? offerCatalog.getBrandDescription() : new BrandDescription();
			if(!StringUtils.isEmpty(splittedContent[17])) brandDescription.setBrandDescriptionEn(splittedContent[17]);
			if(!StringUtils.isEmpty(splittedContent[18])) brandDescription.setBrandDescriptionAr(splittedContent[18]);
			if(!(StringUtils.isEmpty(brandDescription.getBrandDescriptionEn()) && StringUtils.isEmpty(brandDescription.getBrandDescriptionAr()))) offerCatalog.setBrandDescription(brandDescription);		
						
			Tags tags = !ObjectUtils.isEmpty(offerCatalog.getTags()) ? offerCatalog.getTags() : new Tags();
			if(!StringUtils.isEmpty(splittedContent[23])) tags.setTagsEn(splittedContent[23]);
			if(!StringUtils.isEmpty(splittedContent[24])) tags.setTagsAr(splittedContent[24]);
			if(!(StringUtils.isEmpty(tags.getTagsEn()) && StringUtils.isEmpty(tags.getTagsAr()))) offerCatalog.setTags(tags);
			
			WhatYouGet whatYouGet = !ObjectUtils.isEmpty(offerCatalog.getWhatYouGet()) ? offerCatalog.getWhatYouGet() : new WhatYouGet();
			if(!StringUtils.isEmpty(splittedContent[5])) whatYouGet.setWhatYouGetEn(splittedContent[5]);
			if(!StringUtils.isEmpty(splittedContent[6])) whatYouGet.setWhatYouGetAr(splittedContent[6]);
			if(!(StringUtils.isEmpty(whatYouGet.getWhatYouGetEn()) && StringUtils.isEmpty(whatYouGet.getWhatYouGetAr()))) offerCatalog.setWhatYouGet(whatYouGet);
			
			String status = !StringUtils.isEmpty(splittedContent[4]) ? splittedContent[4]: offerCatalog.getStatus();
			if(!StringUtils.isEmpty(status)) offerCatalog.setStatus(status);
			
			OfferDate offerDate = !ObjectUtils.isEmpty(offerCatalog.getOfferDates()) ? offerCatalog.getOfferDates() : new OfferDate();
			if(!StringUtils.isEmpty(splittedContent[2])) offerDate.setOfferEndDate(Utilities.changeStringToDate(splittedContent[2], OfferConstants.DATE_FORMAT.get()));
			if(!StringUtils.isEmpty(splittedContent[3])) offerDate.setOfferStartDate(Utilities.changeStringToDate(splittedContent[3], OfferConstants.DATE_FORMAT.get()));			
			if(!(ObjectUtils.isEmpty(offerDate.getOfferEndDate()) && ObjectUtils.isEmpty(offerDate.getOfferStartDate()))) offerCatalog.setOfferDates(offerDate);
			
		} catch(Exception e) {
			LOG.info("Inside populateOtherOfferCatalogDetails catch block");
			throw new MarketplaceException(this.getClass().toString(), "populateOtherOfferCatalogDetails",
					e.getClass() + e.getMessage(), OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION);
		}
	}
	
	public void updateOfferCount(Merchant merchant, String action, boolean inc) {
		LOG.info("Entering updateOfferCount");
		long offerCount = 0;
		if(!ObjectUtils.isEmpty(merchant)) {
			if(!ObjectUtils.isEmpty(merchant.getOfferCount())) {
				offerCount = merchant.getOfferCount();
				LOG.info("OfferCount from merchant : {}",merchant.getOfferCount());
				LOG.info("OfferCount before modifying : {}",offerCount);
				if((action.equalsIgnoreCase(OfferConstants.ACTIVATE_ACTION.get()) && inc) || (action.equalsIgnoreCase(OfferConstants.UPDATE_ACTION.get()) && inc)) {
					offerCount = offerCount+1;
				} else if((action.equalsIgnoreCase(OfferConstants.DEACTIVATE_ACTION.get()) && !inc) || (action.equalsIgnoreCase(OfferConstants.UPDATE_ACTION.get()) && !inc)) {
					offerCount = offerCount-1;
				}
				LOG.info("offerCount after modifying: {}",offerCount);
				merchant.setOfferCount(offerCount);
			} else {
				LOG.info("OfferCount Empty flow");
				List<OfferCatalog> merchantOfferCount = offerRepository.findByMerchantAndStatusIn(merchant, Arrays.asList("Active","ACTIVE"));
				LOG.info("merchantOfferCount : {}",merchantOfferCount);
				if(!ObjectUtils.isEmpty(merchantOfferCount)) {
					merchant.setOfferCount(merchantOfferCount.size());
				} else {
					merchant.setOfferCount(0);				
				}
			}
			merchantRepository.save(merchant);
		}
		LOG.info("Exiting updateOfferCount");
	}


}


