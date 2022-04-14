package com.loyalty.marketplace.offers.domain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.ExceptionInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.RefundTransactionRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.RefundTransactionDto;
import com.loyalty.marketplace.offers.outbound.dto.RefundTransactionResponse;
import com.loyalty.marketplace.offers.outbound.dto.TransactionListDto;
import com.loyalty.marketplace.offers.outbound.dto.TransactionsResultResponse;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.offers.utils.MapValues;
import com.loyalty.marketplace.offers.utils.OfferValidator;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.outbound.database.entity.RefundTransaction;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.SmilesAppService;
import com.loyalty.marketplace.service.dto.PaymentReversalRequetsDto;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MarketplaceValidator;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherControllerHelper;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherListResult;
import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;

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
public class PurchaseDomain {
	private static final Logger LOG = LoggerFactory.getLogger(PurchaseDomain.class);
	private static final String REFUND_TRANSACTIONS_METHOD = "getAllRefundTransactions";
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;
	
	@Getter(AccessLevel.NONE)
	@Autowired
    VoucherControllerHelper voucherControllerHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	OffersHelper helper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	SubscriptionManagementController subscriptionManagementController;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	MongoOperations mongoOperations;
	

	@Autowired
	SmilesAppService smilesAppService;
	
	private String id;
	private String programCode;
	private String purchaseItem;
	private String partnerCode;
	private String merchantCode;
	private String merchantName;
	private String membershipCode;
	private String accountNumber;
	private String offerId;
	private String offerType;
	private String subOfferId;
	private String transactionType;
	private String promoCode;
	private String transactionNo;
	private String extRefNo;
	private String epgTransactionId;
	private Integer couponQuantity;
	private List<String> voucherCode;
	private String subscriptionId;
	private String paymentMethod;
	private Double spentAmount;
	private Integer spentPoints;
	private MarketplaceActivityDomain partnerActivity;
	private String activityCode;
	private Double cost;
	private Double vatAmount;
	private Double purchaseAmount;
	private String status;
	private String statusReason;
	private AdditionalDetailsDomain additionalDetails;
	private String referralAccountNumber;
	private String referralBonusCode;
	private Double referralBonus;
	private String pointsTransactionId;
	private String customerType;
	private String language;
	private String channelId;
	private String subscriptionSegment;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	private String preferredNumber;
	
	public PurchaseDomain(PurchaseDomainBuilder purchaseDomainBuilder) {
		super();
		this.id = purchaseDomainBuilder.id;
		this.programCode = purchaseDomainBuilder.programCode;
		this.purchaseItem = purchaseDomainBuilder.purchaseItem;
		this.partnerCode = purchaseDomainBuilder.partnerCode;
		this.merchantCode = purchaseDomainBuilder.merchantCode;
		this.merchantName = purchaseDomainBuilder.merchantName;
		this.membershipCode = purchaseDomainBuilder.membershipCode;
		this.accountNumber = purchaseDomainBuilder.accountNumber;
		this.offerId = purchaseDomainBuilder.offerId;
		this.offerType = purchaseDomainBuilder.offerType;
		this.subOfferId = purchaseDomainBuilder.subOfferId;
		this.promoCode = purchaseDomainBuilder.promoCode;
		this.transactionType = purchaseDomainBuilder.transactionType;
		this.transactionNo = purchaseDomainBuilder.transactionNo;
		this.extRefNo = purchaseDomainBuilder.extRefNo;
		this.epgTransactionId = purchaseDomainBuilder.epgTransactionId;
		this.couponQuantity = purchaseDomainBuilder.couponQuantity;
		this.voucherCode = purchaseDomainBuilder.voucherCode;
		this.subscriptionId = purchaseDomainBuilder.subscriptionId;
		this.paymentMethod = purchaseDomainBuilder.paymentMethod;
		this.spentAmount = purchaseDomainBuilder.spentAmount;
		this.spentPoints = purchaseDomainBuilder.spentPoints;
		this.partnerActivity = purchaseDomainBuilder.partnerActivity;
		this.cost = purchaseDomainBuilder.cost;
		this.vatAmount = purchaseDomainBuilder.vatAmount;
		this.purchaseAmount = purchaseDomainBuilder.purchaseAmount;
		this.status = purchaseDomainBuilder.status;
		this.statusReason = purchaseDomainBuilder.statusReason;
		this.language = purchaseDomainBuilder.language;
		this.channelId = purchaseDomainBuilder.channelId;
		this.additionalDetails = purchaseDomainBuilder.additionalDetails;
		this.referralAccountNumber = purchaseDomainBuilder.referralAccountNumber;
		this.referralBonusCode = purchaseDomainBuilder.referralBonusCode;
		this.referralBonus = purchaseDomainBuilder.referralBonus;
		this.pointsTransactionId = purchaseDomainBuilder.pointsTransactionId;
		this.customerType = purchaseDomainBuilder.customerType;
		this.subscriptionSegment = purchaseDomainBuilder.subscriptionSegment;
		this.createdDate = purchaseDomainBuilder.createdDate;
		this.createdUser = purchaseDomainBuilder.createdUser;
		this.updatedDate = purchaseDomainBuilder.updatedDate;
		this.updatedUser = purchaseDomainBuilder.updatedUser;
		this.preferredNumber = purchaseDomainBuilder.preferredNumber;
	}

	public static class PurchaseDomainBuilder{
		
		private String id;
		private String programCode;
		private String purchaseItem;
		private String partnerCode;
		private String merchantCode;
		private String merchantName;
		private String membershipCode;
		private String accountNumber;
		private String offerId;
		private String offerType;
		private String subOfferId;
		private String transactionType;
		private String promoCode;
		private String transactionNo;
		private String extRefNo;
		private String epgTransactionId;
		private Integer couponQuantity;
		private List<String> voucherCode;
		private String subscriptionId;
		private String paymentMethod;
		private Double spentAmount;
		private Integer spentPoints;
		private MarketplaceActivityDomain partnerActivity; 
		private Double cost;
		private Double vatAmount;
		private Double purchaseAmount;
		private String status;
		private String statusReason;
		private String language;
		private String channelId;
		private AdditionalDetailsDomain additionalDetails;
		private String referralAccountNumber;
		private String referralBonusCode;
		private Double referralBonus;
		private String pointsTransactionId;
		private String customerType;
		private String subscriptionSegment;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;
		private String preferredNumber;
		
		public PurchaseDomainBuilder(String purchaseItem, String membershipCode, String accountNumber, 
				String paymentMethod, String partnerCode) {
			super();
			this.purchaseItem = purchaseItem;
			this.membershipCode = membershipCode;
			this.accountNumber = accountNumber;
			this.paymentMethod = paymentMethod;
			this.partnerCode = partnerCode;
		}
				
		public PurchaseDomainBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public PurchaseDomainBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public PurchaseDomainBuilder purchaseItem(String purchaseItem) {
			this.purchaseItem = purchaseItem;
			return this;
		}
		
		public PurchaseDomainBuilder partnerCode(String partnerCode) {
			this.partnerCode = partnerCode;
			return this;
		}
		
		public PurchaseDomainBuilder merchantCode(String merchantCode) {
			this.merchantCode = merchantCode;
			return this;
		}
		
		public PurchaseDomainBuilder merchantName(String merchantName) {
			this.merchantName = merchantName;
			return this;
		}
		
		public PurchaseDomainBuilder membershipCode(String membershipCode) {
			this.membershipCode = membershipCode;
			return this;
		}
		
		public PurchaseDomainBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}
		
		public PurchaseDomainBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}
		
		public PurchaseDomainBuilder offerType(String offerType) {
			this.offerType = offerType;
			return this;
		}
		
		public PurchaseDomainBuilder subOfferId(String subOfferId) {
			this.subOfferId = subOfferId;
			return this;
		}
		
		public PurchaseDomainBuilder promoCode(String promoCode) {
			this.promoCode = promoCode;
			return this;
		}
		
		public PurchaseDomainBuilder transactionType(String transactionType) {
			this.transactionType = transactionType;
			return this;
		}
		
		public PurchaseDomainBuilder transactionNo(String transactionNo) {
			this.transactionNo = transactionNo;
			return this;
		}
		
		public PurchaseDomainBuilder extRefNo(String extRefNo) {
			this.extRefNo = extRefNo;
			return this;
		}
		
		public PurchaseDomainBuilder epgTransactionId(String epgTransactionId) {
			this.epgTransactionId = epgTransactionId;
			return this;
		}
		
		public PurchaseDomainBuilder couponQuantity(Integer couponQuantity) {
			this.couponQuantity = couponQuantity;
			return this;
		}
		
		public PurchaseDomainBuilder voucherCode(List<String> voucherCode) {
			this.voucherCode = voucherCode;
			return this;
		}
		
		public PurchaseDomainBuilder subscriptionId(String subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		public PurchaseDomainBuilder paymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
			return this;
		}
		
		public PurchaseDomainBuilder spentAmount(Double spentAmount) {
			this.spentAmount = spentAmount;
			return this;
		}
		
		public PurchaseDomainBuilder spentPoints(Integer spentPoints) {
			this.spentPoints = spentPoints;
			return this;
		}
		
		public PurchaseDomainBuilder partnerActivity(MarketplaceActivityDomain partnerActivity) {
			this.partnerActivity = partnerActivity;
			return this;
		}
		
		public PurchaseDomainBuilder cost(Double cost) {
			this.cost = cost;
			return this;
		}
		
		public PurchaseDomainBuilder vatAmount(Double vatAmount) {
			this.vatAmount = vatAmount;
			return this;
		}
		
		public PurchaseDomainBuilder purchaseAmount(Double purchaseAmount) {
			this.purchaseAmount = purchaseAmount;
			return this;
		}
		
		public PurchaseDomainBuilder status(String status) {
			this.status = status;
			return this;
		}
		
		public PurchaseDomainBuilder statusReason(String statusReason) {
			this.statusReason = statusReason;
			return this;
		}
		
		public PurchaseDomainBuilder language(String language) {
			this.language = language;
			return this;
		}
		
		public PurchaseDomainBuilder channelId(String channelId) {
			this.channelId = channelId;
			return this;
		}
		
		public PurchaseDomainBuilder subscriptionSegment(String subscriptionSegment) {
			this.subscriptionSegment = subscriptionSegment;
			return this;
		}
		
		public PurchaseDomainBuilder additionalDetails(AdditionalDetailsDomain additionalDetails) {
			this.additionalDetails = additionalDetails;
			return this;
		}
		
		public PurchaseDomainBuilder referralAccountNumber(String referralAccountNumber) {
			this.referralAccountNumber = referralAccountNumber;
			return this;
		}
		
		public PurchaseDomainBuilder referralBonusCode(String referralBonusCode) {
			this.referralBonusCode = referralBonusCode;
			return this;
		}
		
		public PurchaseDomainBuilder referralBonus(Double referralBonus) {
			this.referralBonus = referralBonus;
			return this;
		}
		
		public PurchaseDomainBuilder pointsTransactionId(String pointsTransactionId) {
			this.pointsTransactionId = pointsTransactionId;
			return this;
		}
		
		public PurchaseDomainBuilder customerType(String customerType) {
			this.customerType = customerType;
			return this;
		}
		
		public PurchaseDomainBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		
		public PurchaseDomainBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public PurchaseDomainBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public PurchaseDomainBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public PurchaseDomainBuilder preferredNumber(String preferredNumber) {
			this.preferredNumber = preferredNumber;
			return this;
		}
		
		public PurchaseDomain build() {
			return new PurchaseDomain(this);
		}
		
	}
	
	/**
	 * Save/Update the purchase details to repository
	 * @param purchaseDomain
	 * @param purchaseHistory
	 * @param action
	 * @param headers
	 * @return saved/updated purchase history
	 * @throws MarketplaceException
	 */
	public PurchaseHistory saveUpdatePurchaseDetails(PurchaseDomain purchaseDomain,
			PurchaseHistory purchaseHistory, String action, Headers headers) throws MarketplaceException {
		
		try {			
			
			PurchaseHistory purchaseToSave = modelMapper.map(purchaseDomain, PurchaseHistory.class);
			PurchaseHistory savedPurchaseHistory = repositoryHelper.savePurchaseHistory(purchaseToSave);	
			
			if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.PURCHASE_HISTORY, savedPurchaseHistory, OffersRequestMappingConstants.PURCHASE, purchaseHistory,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
			return  savedPurchaseHistory;
			
		} catch (MongoException mongoException) {
			
			exceptionLogService.saveExceptionsToExceptionLogs(mongoException, headers.getExternalTransactionId(), purchaseDomain.getAccountNumber(), headers.getUserName());
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_OFFER_PURCHASE_DOMAIN_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
			
		}  catch (Exception e) {
		
			exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), purchaseDomain.getAccountNumber(), headers.getUserName());
			OfferExceptionCodes exception = (purchaseDomain.getId()==null)
					? OfferExceptionCodes.OFFER_PURCHASE_HISTORY_ADDITION_RUNTIME_EXCEPTION
					: OfferExceptionCodes.OFFER_PURCHASE_HISTORY_UPDATION_RUNTIME_EXCEPTION; 
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_OFFER_PURCHASE_DOMAIN_METHOD.get(),
					e.getClass() + e.getMessage(), exception);
		
		}
	}
	
	/**
	 * Checks all necessary conditions and completes purchase with required operations
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 * @param headers
	 * @return purchase detail response for end user
	 */
	public PurchaseResultResponse validateAndSaveMarketplacePurchaseHistory(PurchaseRequestDto purchaseRequest, PurchaseResultResponse purchaseResultResponse, Headers headers) {
		
		OfferSuccessCodes successResult = ProcessValues.getPurchaseSuccessCode(purchaseRequest.getSelectedPaymentItem());
		OfferErrorCodes errorResult = ProcessValues.getPurchaseErrorCode(purchaseRequest.getSelectedPaymentItem());
		
		try {	
			
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			purchaseRequest.setExtTransactionId(headers.getExternalTransactionId());
			Responses.setDefaultPurchasesValues(purchaseRequest);
			
			PurchasePaymentMethod purchasePaymentMethod = repositoryHelper.getPaymentMethodsForPurchaseItem(purchaseRequest.getSelectedPaymentItem());
			
			if(Checks.checkPurchaseRequestValid(purchaseRequest, purchasePaymentMethod, purchaseResultResponse)) {
			
				OfferCatalog offerDetails = Checks.checkIsDealVoucher(purchaseRequest.getSelectedPaymentItem())
						? repositoryHelper.findOfferBySubOfferId(purchaseRequest.getSubOfferId())
						: repositoryHelper.findByOfferId(purchaseRequest.getOfferId());
			
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerDetails), OfferErrorCodes.OFFER_NOT_AVAILABLE, purchaseResultResponse)) {
					
					EligibilityInfo eligibilityInfo = new EligibilityInfo();		
					ProcessValues.setEligibilityInfoForPurchase(eligibilityInfo, purchaseRequest, offerDetails, purchasePaymentMethod, headers);		
					
			        if(helper.validateOfferDetails(eligibilityInfo, purchaseRequest,purchaseResultResponse)) {
			        
			        	purchaseRequest.setOfferId(offerDetails.getOfferId());	
			        	eligibilityInfo.setOffer(offerDetails);
			        	eligibilityInfo.setOfferList(Arrays.asList(offerDetails));
			        	
			        	GetMemberResponseDto getMemberResponse = fetchServiceValues.getMemberDetailsForPayment(purchaseRequest.getAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(), eligibilityInfo.getHeaders(), purchaseResultResponse);
						eligibilityInfo.setMemberDetailsDto(getMemberResponse);
						GetMemberResponse memberDetails = ProcessValues.getMemberInfo(getMemberResponse, purchaseRequest.getAccountNumber());
						eligibilityInfo.setMemberDetails(memberDetails);
						if(!ObjectUtils.isEmpty(eligibilityInfo.getAdditionalDetails())) {
							
							eligibilityInfo.getAdditionalDetails().setSubscribed(memberDetails.isSubscribed());
							eligibilityInfo.getAdditionalDetails().setSubscriptionBenefit(Checks
									.checkOfferEligibleForBenefits(offerDetails.getOfferType().getOfferTypeId(), 
											offerDetails.getCategory().getCategoryId(), 
											offerDetails.getSubCategory().getCategoryId(), 
											eligibilityInfo.getMemberDetails().getBenefits()));
						}

						eligibilityInfo.setCustomerSegmentCheckRequired(Checks.checkIsCustomerSegmentCheckRequired(eligibilityInfo.getOfferList()));
						helper.validateMemberDetailsAndPurchaseItem(eligibilityInfo, purchaseRequest, purchaseResultResponse,null);
			        	
			        }
					
					
				}
			    
			}			
			
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			Responses.setResponseAfterException(purchaseResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.MAKE_PURCHASE.get(), null, e, errorResult, null,
					errorResult,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), purchaseRequest.getAccountNumber(), headers.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			Responses.setResponseAfterException(purchaseResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.MAKE_PURCHASE.get(), e, null, errorResult,
					OfferExceptionCodes.PURCHASE_HISTORY_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), purchaseRequest.getAccountNumber(), headers.getUserName())));
			
		}
		
		Responses.setResponse(purchaseResultResponse, errorResult, successResult);
		return purchaseResultResponse;
		
	}
	
	/**
	 * Checks all necessary conditions and completes purchase with required operations
	 * @param purchaseRequest
	 * @param purchaseResultResponse
	 * @param headers
	 * @return purchase detail response for end user
	 */
	public PurchaseResultResponse validateAndSavePurchaseHistory(PurchaseRequestDto purchaseRequest, PurchaseResultResponse purchaseResultResponse, Headers headers) {
		
		OfferSuccessCodes successResult = ProcessValues.getPurchaseSuccessCode(purchaseRequest.getSelectedPaymentItem());
		OfferErrorCodes errorResult = ProcessValues.getPurchaseErrorCode(purchaseRequest.getSelectedPaymentItem());
		
		try {	
			
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			purchaseRequest.setExtTransactionId(headers.getExternalTransactionId());
			Responses.setDefaultPurchasesValues(purchaseRequest);
			
			PurchasePaymentMethod purchasePaymentMethod = repositoryHelper.getPaymentMethodsForPurchaseItem(purchaseRequest.getSelectedPaymentItem());
			LOG.info("paymentMethodList from db: {}", !ObjectUtils.isEmpty(purchasePaymentMethod) && !CollectionUtils.isEmpty(purchasePaymentMethod.getPaymentMethods())
					? purchasePaymentMethod.getPaymentMethods().stream().map(PaymentMethod::getDescription).collect(Collectors.toList())
					: null);
			
			if(Checks.checkPurchaseRequestValid(purchaseRequest, purchasePaymentMethod, purchaseResultResponse)) {
			
				OfferCatalog offerDetails = Checks.checkIsDealVoucher(purchaseRequest.getSelectedPaymentItem())
						? repositoryHelper.findOfferBySubOfferId(purchaseRequest.getSubOfferId())
						: repositoryHelper.findByOfferId(purchaseRequest.getOfferId());
			
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offerDetails), OfferErrorCodes.OFFER_NOT_AVAILABLE, purchaseResultResponse)) {
					
					EligibilityInfo eligibilityInfo = new EligibilityInfo();		
					ProcessValues.setEligibilityInfoForPurchase(eligibilityInfo, purchaseRequest, offerDetails, purchasePaymentMethod, headers);		
					
			        if(helper.validateOfferSpecificDetails(eligibilityInfo, purchaseRequest,purchaseResultResponse)) {
			        
			        	purchaseRequest.setOfferId(offerDetails.getOfferId());	
			        	eligibilityInfo.setOffer(offerDetails);
			        	eligibilityInfo.setOfferList(Arrays.asList(offerDetails));
			        	
			        	GetMemberResponseDto getMemberResponse = fetchServiceValues.getMemberDetailsForPayment(purchaseRequest.getAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(), eligibilityInfo.getHeaders(), purchaseResultResponse);
						
			        	if(Checks.checkNoErrors(purchaseResultResponse)) {
			        		
			        		eligibilityInfo.setMemberDetailsDto(getMemberResponse);
							GetMemberResponse memberDetails = ProcessValues.getMemberInfo(getMemberResponse, purchaseRequest.getAccountNumber());
							eligibilityInfo.setMemberDetails(memberDetails);
							if(!ObjectUtils.isEmpty(eligibilityInfo.getAdditionalDetails())) {
								
								eligibilityInfo.getAdditionalDetails().setSubscribed(
										!ObjectUtils.isEmpty(memberDetails)
										&& memberDetails.isSubscribed());
								eligibilityInfo.getAdditionalDetails().setSubscriptionBenefit(Checks
										.checkOfferEligibleForBenefits(offerDetails.getOfferType().getOfferTypeId(), 
												offerDetails.getCategory().getCategoryId(), 
												offerDetails.getSubCategory().getCategoryId(), 
												eligibilityInfo.getMemberDetails().getBenefits()));
								
							}

							eligibilityInfo.setCustomerSegmentCheckRequired(Checks.checkIsCustomerSegmentCheckRequired(eligibilityInfo.getOfferList()));
							helper.validateMemberDetailsAndPurchaseItem(eligibilityInfo, purchaseRequest, purchaseResultResponse, headers);

			        	}
			        }
					
				}
			    
			}			
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			Responses.setResponseAfterException(purchaseResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.MAKE_PURCHASE.get(), null, e, errorResult, null,
					errorResult,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), purchaseRequest.getAccountNumber(), headers.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			Responses.setResponseAfterException(purchaseResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.MAKE_PURCHASE.get(), e, null, errorResult,
					OfferExceptionCodes.PURCHASE_HISTORY_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), purchaseRequest.getAccountNumber(), headers.getUserName())));
		}
		
		addErrorToPurchaseHistory(purchaseResultResponse, purchaseRequest, headers);
		Responses.setResponse(purchaseResultResponse, errorResult, successResult);
		return purchaseResultResponse;
		
	}
	
	/**
	 * Validates and filters purchase transaction records as per the request
	 * @param transactionRequest
	 * @param headers
	 * @return filtered transaction list with voucher details
	 */
	public TransactionsResultResponse getAllPurchaseTransactionsWithVoucherDetails(
			TransactionRequestDto transactionRequest, Headers headers) {
		
         TransactionsResultResponse  transactionsResultResponse = new TransactionsResultResponse(headers.getExternalTransactionId());
         List<TransactionListDto> transactionList = null; 
        		 
         try {
        	 
        	 if(MarketplaceValidator.validateDto(transactionRequest, validator, transactionsResultResponse)
        	 &&	OfferValidator.validateTransactionRequest(transactionRequest, transactionsResultResponse)){
        		
        		 List<PurchaseHistory> purchaseList = repositoryHelper.findAllActivePurchaseTransactionsWithFilters(transactionRequest, headers);
        		 
        		 if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(purchaseList), OfferErrorCodes.NO_TRANSACTION_MATCHES_CRITERIA, transactionsResultResponse)) {
     				
        			transactionList = new ArrayList<>(purchaseList.size());
     			    List<PaymentMethod> paymentMethods = helper.getTransactionListPaymentMethods(purchaseList);
					List<VoucherListResult> voucherDetails = voucherControllerHelper.getVoucherListByBusinessId(
							MapValues.mapPurchaseIdFromTransactionList(purchaseList), transactionsResultResponse, headers.getProgram());

     				for(PurchaseHistory purchaseRecord : purchaseList) {
     					
     					helper.setTransactionValuesFromPurchaseRecord(purchaseRecord, transactionList, paymentMethods, voucherDetails, transactionsResultResponse);
     					
     				}
     				
     			}
        		 
        	}
        	 
 		 } catch(ValidationException e) {
  			
  			e.printStackTrace();
  			String accountNo = null;
  			if(!ObjectUtils.isEmpty(transactionRequest.getAccountNumber())) {
  				accountNo = transactionRequest.getAccountNumber();
  			} else if(!CollectionUtils.isEmpty(transactionRequest.getAccountNumberList())) {
  				accountNo = transactionRequest.getAccountNumberList().toString();
  			} 
						
  			Responses.setResponseAfterException(transactionsResultResponse, 
 					new ExceptionInfo(this.getClass().toString(), 
 					OfferConstants.FETCH_TRANSACTION_LIST_METHOD.get(), e, null, OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED,
 					OfferExceptionCodes.VALIDATION_EXCEPTION, null,
 					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), accountNo, headers.getUserName())));
  			
  		} catch (Exception e) {
 			
 			e.printStackTrace();
 			String accountNo = null;
  			if(!ObjectUtils.isEmpty(transactionRequest.getAccountNumber())) {
  				accountNo = transactionRequest.getAccountNumber();
  			} else if(!CollectionUtils.isEmpty(transactionRequest.getAccountNumberList())) {
  				accountNo = transactionRequest.getAccountNumberList().toString();
  			}
 			Responses.setResponseAfterException(transactionsResultResponse, 
 					new ExceptionInfo(this.getClass().toString(), 
 					OfferConstants.FETCH_TRANSACTION_LIST_METHOD.get(), e, null, OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED,
 					OfferExceptionCodes.FETCH_TRANSACTION_DOMAIN_EXCEPTION, null,
 					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), accountNo, headers.getUserName())));
 				
 		}
 		
 		Responses.setResponse(transactionsResultResponse, OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED, OfferSuccessCodes.TRANSACTIONS_FETCHED_SUCCESSFULLY);
 		transactionsResultResponse.setTransactionList(transactionList);
 		return transactionsResultResponse;
	}

	public UpdateResult updateFailedPurchase(List<String> purchaseId, String actionReason) {
		LOG.info("Inside updateFailedPurchase method to update purchased record status to Failed");
		Query purchaseHistoryQuery = new Query();
		purchaseHistoryQuery.addCriteria(
			Criteria.where("_id").in(purchaseId));
		
		Update update = new Update();
		update.set("Status", OfferConstants.FAILED.get());
		update.set("StatusReason", actionReason);
		update.set("UpdatedDate", new Date());
		
		return mongoOperations.updateMulti(purchaseHistoryQuery, update, PurchaseHistory.class);
	}
	
	/***
	 * 
	 * @param transactionRequest
	 * @param headers
	 * @return
	 */
	public RefundTransactionResponse getAllRefundTransactions(RefundTransactionRequestDto transactionRequest,
			Headers headers) {
		RefundTransactionResponse  transactionsResultResponse = new RefundTransactionResponse(headers.getExternalTransactionId());
        List<RefundTransactionDto> transactionList = null; 
       		 
        try {
       	 
	      if(MarketplaceValidator.validateDto(transactionRequest, validator, transactionsResultResponse))
	       	{
	       		
	       		List<RefundTransaction> refundTransactions = repositoryHelper.getRefundTransactions(transactionRequest, headers.getProgram());
	       		
	       		if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(refundTransactions), OfferErrorCodes.NO_TRANSACTION_MATCHES_CRITERIA, transactionsResultResponse)) {
	       			
	       			transactionList = new ArrayList<>(refundTransactions.size());
	       			for(RefundTransaction transaction : refundTransactions) {
	       				
	       				transactionList.add(modelMapper.map(transaction, RefundTransactionDto.class));
	       			}
	       			
	       		}
	       	}
       	 
		 } catch(ValidationException e) {
 			
 			e.printStackTrace();
 			Responses.setResponseAfterException(transactionsResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					REFUND_TRANSACTIONS_METHOD, e, null, OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED,
					OfferExceptionCodes.VALIDATION_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), null, headers.getUserName())));
 			
 		} catch (Exception e) {
			
			e.printStackTrace();
			Responses.setResponseAfterException(transactionsResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					REFUND_TRANSACTIONS_METHOD, e, null, OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED,
					OfferExceptionCodes.RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), null, headers.getUserName())));
				
		}
		
		Responses.setResponse(transactionsResultResponse, OfferErrorCodes.FETCHING_REFUND_TRANSACTONS_FAILED, OfferSuccessCodes.REFUND_TRANSACTIONS_FETCHED_SUCCESSFULLY);
		transactionsResultResponse.setRefundTransactionList(transactionList);
		return transactionsResultResponse;

	}
	
	/***
	 * 
	 * @param purchaseResultResponse
	 * @param purchaseRequestDto
	 * @param headers
	 */
	public void checkAndPerformRefund(PurchaseResultResponse purchaseResultResponse,
			PurchaseRequestDto purchaseRequestDto, Headers headers) {
		
		addErrorToPurchaseHistory(purchaseResultResponse, purchaseRequestDto, headers);
		if(!Checks.checkPurchaseResponsePresent(purchaseResultResponse)) {
			
			purchaseResultResponse.setPurchaseResponseDto(new PurchaseResponseDto());
			
		}
		
		if(Checks.checkEligibleForRefund(purchaseRequestDto, headers, purchaseResultResponse)) {
			
			purchaseResultResponse.getPurchaseResponseDto().setRefundFlag(true);
			
			try {
				
				    String accountType = repositoryHelper.getCustomerTypeFromTranscationDetails(purchaseResultResponse.getPurchaseResponseDto().getTransactionNo());
					
					if(StringUtils.isEmpty(accountType)) {
						
						GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(purchaseRequestDto.getAccountNumber(), new ResultResponse(headers.getExternalTransactionId()), headers);
						accountType = !ObjectUtils.isEmpty(memberDetails)
								&& !CollectionUtils.isEmpty(memberDetails.getCustomerType())
								? memberDetails.getCustomerType().get(0)
								: null;
					}
					PaymentReversalRequetsDto request = new PaymentReversalRequetsDto();
					request.setPurchaseRequestDto(purchaseRequestDto);
					request.setAccountType(accountType);
					smilesAppService.smilesPaymentReversal(Arrays.asList(request), false, headers);
					
			} catch (Exception e) {
				
				LOG.error("Error in processing refund. Refer ExceptionLogService collection with id : {}", 
						exceptionLogService.saveExceptionsToExceptionLogs(e, 
								headers.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), headers.getUserName()));
			}
		}
		
	}
	
	/**
	 * Add errors record to 
	 * @param purchaseResultResponse
	 * @param purchaseRequestDto
	 * @param headers
	 */
	public void addErrorToPurchaseHistory(PurchaseResultResponse purchaseResultResponse,
			PurchaseRequestDto purchaseRequestDto, Headers headers) {
		
		if(Checks.checkErrorsPresent(purchaseResultResponse)) {
			
			try {
				
				if(!Checks.checkPurchaseResponsePresent(purchaseResultResponse)) {
					
					PurchaseDomain purchaseHistoryDomain = DomainConfiguration.getPurchaseDomainForError(purchaseRequestDto, headers,
							purchaseResultResponse.getApiStatus().getErrors().get(0).getMessage());
					PurchaseHistory purchaseHistory = saveUpdatePurchaseDetails(purchaseHistoryDomain, null, OfferConstants.INSERT_ACTION.get(), headers);
					purchaseResultResponse.setPurchaseResponseDto(new PurchaseResponseDto());
					purchaseResultResponse.getPurchaseResponseDto().setTransactionNo(purchaseHistory.getId());				
				}
				
			} catch (MarketplaceException e) {
				LOG.error("Error in saving details to PurchaseHistory");
				e.printStackTrace();
			} 
			
		}	
	}
		
}

