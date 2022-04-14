package com.loyalty.marketplace.offers.helper;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.constants.MarketplaceDBConstants;
import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.database.repository.ConversionRateRepository;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificate;
import com.loyalty.marketplace.gifting.outbound.database.repository.GoldCertificateRepository;
import com.loyalty.marketplace.image.constants.ImageConfigurationConstants;
import com.loyalty.marketplace.image.constants.ImageConstants;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.image.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersListConstants;
import com.loyalty.marketplace.offers.error.handler.ErrorRecords;
import com.loyalty.marketplace.offers.error.handler.ErrorRepository;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.OfferListAndCount;
import com.loyalty.marketplace.offers.helper.dto.OfferMerchantDto;
import com.loyalty.marketplace.offers.helper.dto.UpdateDate;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.inbound.dto.RefundTransactionRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CustomerTypeEntity;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.entity.SubscriptionValues;
import com.loyalty.marketplace.offers.outbound.database.entity.WishlistEntity;
import com.loyalty.marketplace.offers.outbound.database.repository.AccountOfferCounterRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.BirthdayGiftTrackerRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.BirthdayInfoRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.EligibleOfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.MemberOfferCounterRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferCounterRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferCountersRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRatingRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferTypeRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchasePaymentMethodRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.WishlistRepository;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.FilterValues;
import com.loyalty.marketplace.offers.utils.Predicates;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.database.repository.DenominationRepository;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.outbound.database.entity.RefundTransaction;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionWithInterestRequestDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionWithInterestResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionWithInterestResultResponse;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.StatusCode;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherAction;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherActionRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.result.UpdateResult;

/**
 * 
 * @author jaya.shukla
 *
 */
@Component
public class RepositoryHelper {

	private static final Logger LOG = LoggerFactory.getLogger(RepositoryHelper.class);

	@Autowired
	OfferTypeRepository offerTypeRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	DenominationRepository denominationRepository;

	@Autowired
	PaymentMethodRepository paymentMethodRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	StoreRepository storeRepository;

	@Autowired
	PurchaseRepository purchaseRepository;

	@Autowired
	OfferRatingRepository offerRatingRepository;

	@Autowired
	OfferRepository offerRepository;

	@Autowired
	PurchasePaymentMethodRepository purchasePaymentMethodRepository;

	@Autowired
	ConversionRateRepository conversionRateRepository;

	@Autowired
	GoldCertificateRepository goldCertificateRepository;

	@Autowired
	OfferCountersRepository offerCountersRepository;
	
	@Autowired
  	SubscriptionManagementController subscriptionController;

	@Autowired
	BirthdayInfoRepository birthdayInfoRepository;

	@Autowired
	BirthdayGiftTrackerRepository birthdayGiftTrackerRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	WishlistRepository wishlistRepository;
	
	@Autowired
	ErrorRepository errorRepository;
	
	@Autowired
	VoucherActionRepository voucherActionRepository;

	@Autowired
	MongoOperations mongoOperations;
	
	@Autowired
	EligibleOfferRepository eligibleOfferRepository;
	
	@Autowired
	OfferCounterRepository offerCounterRepository;
	
	@Autowired
	AccountOfferCounterRepository accountOfferCounterRepository;
	
	@Autowired
	MemberOfferCounterRepository memberOfferCounterRepository;
	
	@Value(OffersConfigurationConstants.MM_DB_URI)
	private String memberMangementDBURI;

	@Value(OffersConfigurationConstants.MM_DB)
	private String memberManagementDatabase;

	/**
	 * 
	 * @param offerTypeId
	 * @return offer type where id matches the offer type id
	 */
	public OfferType getOfferType(String offerTypeId) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		OfferType result = !StringUtils.isEmpty(offerTypeId) ? offerTypeRepository.findByOfferTypeId(offerTypeId)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param merchantCode
	 * @return merchant fetched with same merchantCode
	 */
	public Merchant getMerchant(String merchantCode) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.MERCHANT);
		LOG.info(log);
		Merchant merchant = !StringUtils.isEmpty(merchantCode) ? merchantRepository.findByMerchantCode(merchantCode)
				: null;

		log = Logs.logAfterHittingDB(MarketplaceDBConstants.MERCHANT);
		LOG.info(log);

		return merchant;
	}
	
	/**
	 * Loyalty as a service.
	 * @param merchantCode
	 * @return merchant fetched with same merchantCode
	 */
	public Merchant getMerchantByProgramCode(String merchantCode, Headers header) {

		//Loyalty as a service.
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.MERCHANT);
		LOG.info(log);
		Merchant merchant = !StringUtils.isEmpty(merchantCode) ? merchantRepository.findByMerchantCodeAndProgramIgnoreCase(merchantCode, header.getProgram())
				: null;

		log = Logs.logAfterHittingDB(MarketplaceDBConstants.MERCHANT);
		LOG.info(log);

		return merchant;
	}

	/**
	 * 
	 * @param merchantCode
	 * @param storeCodes
	 * @return list of stores fetched with same merchant code and store code in the
	 *         storeCodes
	 */
	public List<Store> getStoreList(String merchantCode, List<String> storeCodes) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.STORE);
		LOG.info(log);
		List<Store> result = !StringUtils.isEmpty(merchantCode) && !CollectionUtils.isEmpty(storeCodes)
				? storeRepository.findAllByStoreCodeAndMerchantCode(storeCodes, merchantCode)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.STORE);
		LOG.info(log);
		return result;

	}

	/**
	 * 
	 * @param categoryId
	 * @return category with same categoryId
	 */
	public Category getCategory(String categoryId) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		Category result = !StringUtils.isEmpty(categoryId) 
				? categoryRepository.findByCategoryId(categoryId) 
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param denominationValues
	 * @return denomination list with dirham value matching any value in
	 *         denominationValues
	 */
	public List<Denomination> getDenominationList(List<Integer> denominationValues) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		List<Denomination> result = CollectionUtils.isNotEmpty(denominationValues)
				? denominationRepository.findAllByDirhamValue(denominationValues)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		return result;

	}

	/**
	 * 
	 * @return all offerCatalog records present in repository
	 */
	public List<OfferCatalog> getAllOfferCatalog() {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		List<OfferCatalog> result = offerRepository.findAll();
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param item
	 * @return purchasePaymentMethod from repository with same purchase item as
	 *         input
	 */
	public PurchasePaymentMethod getPaymentMethodsForPurchaseItem(String item) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_PAYMENT_METHOD);
		LOG.info(log);
		PurchasePaymentMethod result = !StringUtils.isEmpty(item)
				? purchasePaymentMethodRepository.findByPurchaseItem(item)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_PAYMENT_METHOD);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param transactionRequest
	 * @param program 
	 * @return list of purchase history transactions based on filters
	 * @throws ParseException
	 */
	public List<PurchaseHistory> findAllActivePurchaseTransactionsWithFilters(
			TransactionRequestDto transactionRequest, Headers headers) throws ParseException {
		
		List<PurchaseHistory> purchaseList = null;
		
		if(!ObjectUtils.isEmpty(transactionRequest)) {
			
			Query transactionsQuery = new Query();
			transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.STATUS)
					.regex(ProcessValues.getRegexStartEndExpression(OfferConstants.SUCCESS.get()),
							OfferConstants.CASE_INSENSITIVE.get())
			        .and(OffersDBConstants.PROGRAM_CODE)
			        .regex(ProcessValues.getRegexStartEndExpression(headers.getProgram()),
							OfferConstants.CASE_INSENSITIVE.get()));
				
			setTransactionDateCriteriaInQuery(transactionRequest, transactionsQuery);
			setAccountNumberAndMembershipCodeCriteriaInQuery(transactionRequest, transactionsQuery);
			setPurchaseItemCriteriaInQuery(transactionRequest, transactionsQuery);
			setTransactionTypeCriteriaInQuery(transactionRequest, transactionsQuery);
			if(!StringUtils.isEmpty(headers.getChannelId()) && transactionRequest.isChannelCheck()) {
				transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.CHANNEL_ID)
						.regex(ProcessValues.getRegexStartEndExpression(headers.getChannelId()),
								OfferConstants.CASE_INSENSITIVE.get()));
			}
			
			String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
			LOG.info(log);
			
			purchaseList = mongoOperations.find(transactionsQuery.with(Sort.by(Sort.Direction.DESC, OfferConstants.SORT_DESC_CREATED_DATE.get())),
					PurchaseHistory.class, OffersDBConstants.PURCHASE_HISTORY);
			
			log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
			LOG.info(log);
		}
		
		return purchaseList;
	}
	
	/**
	 * Set transaction date criteria in the transactions query
	 * @param transactionRequest
	 * @param transactionsQuery
	 * @throws ParseException 
	 */
	private void setTransactionDateCriteriaInQuery(TransactionRequestDto transactionRequest, Query transactionsQuery) throws ParseException {
		
		if(!StringUtils.isEmpty(transactionRequest.getFromDate())
				&& !StringUtils.isEmpty(transactionRequest.getToDate())) {
					
					Date fromDate = Utilities.changeStringToDateWithTimeFormat(transactionRequest.getFromDate(),
							OfferConstants.FROM_DATE_TIME.get());
					Date toDate = Utilities.changeStringToDateWithTimeFormat(transactionRequest.getToDate(),
							OfferConstants.END_DATE_TIME.get());
					
					transactionsQuery.addCriteria(
							new Criteria().andOperator(
							    Criteria.where(OffersDBConstants.CREATED_DATE)
									.gte(fromDate),
							    Criteria.where(OffersDBConstants.CREATED_DATE)
									.lte(toDate)	
							));
				
				}
		
	}

	/**
	 * Set account number and membership code criteria in the transactions query
	 * @param transactionRequest
	 * @param transactionsQuery
	 */
	private void setAccountNumberAndMembershipCodeCriteriaInQuery(TransactionRequestDto transactionRequest,
			Query transactionsQuery) {
		
		if(!StringUtils.isEmpty(transactionRequest.getMembershipCode())) {
		    
			transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.MEMBERSHIP_CODE)
					.is(transactionRequest.getMembershipCode()));
			
			if(!StringUtils.isEmpty(transactionRequest.getAccountNumber())
			&& !transactionRequest.isIncludeLinkedAccounts()) {
				
				transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.ACCOUNT_NUMBER)
						.is(transactionRequest.getAccountNumber()));
				
			} else if(!CollectionUtils.isEmpty(transactionRequest.getAccountNumberList())
			&& !transactionRequest.isPrimaryAccountInList()) {
			
				transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.ACCOUNT_NUMBER)
						.in(transactionRequest.getAccountNumberList()));
				
			}
			
		}
		
	}

	/**
	 * Set criteria for transaction type in the transactions query
	 * @param transactionRequest
	 * @param transactionsQuery
	 */
    private void setTransactionTypeCriteriaInQuery(TransactionRequestDto transactionRequest, Query transactionsQuery) {
		
    	ProcessValues.setTransactionTypeInTransactionRequest(transactionRequest);
    	if(!StringUtils.isEmpty(transactionRequest.getTransactionType())) {
    		
    		if(OfferConstants.REDEMPTION_POINTS_TRANSACTION_TYPE.get().equalsIgnoreCase(transactionRequest.getTransactionType())) {
    			
    			transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.SPENT_POINTS).ne(0)
    					.orOperator(
    							Criteria.where(OffersDBConstants.PAYMENT_METHOD).regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.PAYMENT_METHOD_POINTS.get()),
    								OfferConstants.CASE_INSENSITIVE.get()),
    							Criteria.where(OffersDBConstants.PAYMENT_METHOD).regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.PAYMENT_METHOD_PARTIALPOINTS.get()),
    								OfferConstants.CASE_INSENSITIVE.get())));
    			
    		} else {
    			
    			transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.TRANSACTION_TYPE)
    					.regex(ProcessValues.getRegexStartEndExpression(transactionRequest.getTransactionType()),
    							OfferConstants.CASE_INSENSITIVE.get()));
    			
    		}
		}
		
	}
    
    /**
     * Set purchase item criteria in query
     * @param transactionRequest
     * @param transactionsQuery
     */
    private void setPurchaseItemCriteriaInQuery(TransactionRequestDto transactionRequest, Query transactionsQuery) {
		
    	ProcessValues.setOfferTypeInTransactionRequest(transactionRequest);
    	if(!StringUtils.isEmpty(transactionRequest.getOfferType())) {
			
			transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.PURCHASE_ITEM)
					.regex(ProcessValues.getRegexStartEndExpression(transactionRequest.getOfferType()),
							OfferConstants.CASE_INSENSITIVE.get()));
		}
		
	}
	
	/**
	 * 
	 * @param methodNames
	 * @return list of all payment methods where payment method present in input
	 *         list
	 */
	public List<PaymentMethod> findAllPaymentMethodsInSpecifiedList(List<String> methodNames) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.PAYMENT_METHOD);
		LOG.info(log);
		List<PaymentMethod> result = CollectionUtils.isNotEmpty(methodNames)
				? paymentMethodRepository.findAllPaymentMethodsInSpecifiedList(methodNames)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.PAYMENT_METHOD);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param asList
	 * @param productTypeValue
	 * @return list of conversion rate where partner code is in partnercodeList and
	 *         product type in productTypeValue
	 */
	public List<ConversionRate> findConversionRateListByPartnerCodeAndProductItem(List<String> partnerCodeList,
			List<String> productTypeValue) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CONVERSION_RATE);
		LOG.info(log);
		List<ConversionRate> result = CollectionUtils.isNotEmpty(partnerCodeList)
				&& CollectionUtils.isNotEmpty(productTypeValue)
				? conversionRateRepository.findByPartnerCodeAndProductItem(partnerCodeList, productTypeValue)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CONVERSION_RATE);
		LOG.info(log);
		return result;
	}
	
	/**
	 * Loyalty as a service.
	 * @param asList
	 * @param productTypeValue
	 * @return list of conversion rate where partner code is in partnercodeList and
	 *         product type in productTypeValue
	 */
	public List<ConversionRate> findConversionRateListByPartnerCodeAndProductItemAndProgramCode(List<String> partnerCodeList,
			List<String> productTypeValue, Headers header) {

		//Loyalty as a service.
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CONVERSION_RATE);
		LOG.info(log);
		List<ConversionRate> result = CollectionUtils.isNotEmpty(partnerCodeList)
				&& CollectionUtils.isNotEmpty(productTypeValue)
				? conversionRateRepository.findByPartnerCodeAndProductItemAndProgramCodeIgnoreCase(partnerCodeList, productTypeValue, header.getProgram())
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CONVERSION_RATE);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @return gold certificate record with same account number and membership code
	 *         in input
	 */
	public GoldCertificate findGoldCertificateRecordByAccountNumberAndMembershipCode(String accountNumber,
			String membershipCode) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.GOLD_CERTIFICATE);
		LOG.info(log);
		GoldCertificate result = !StringUtils.isEmpty(accountNumber) && !StringUtils.isEmpty(membershipCode)
				? goldCertificateRepository.findByAccountNumberAndMembershipCode(accountNumber, membershipCode)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.GOLD_CERTIFICATE);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @return list of purchase history records for the account number and
	 *         membership code as in input
	 */
	public List<PurchaseHistory> findPurchaseHistoryRecordsByAccountNumberAndMembershipCode(String accountNumber,
			String membershipCode) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		List<PurchaseHistory> result = !StringUtils.isEmpty(accountNumber) && !StringUtils.isEmpty(membershipCode)
				? purchaseRepository.findByAccountNumberAndMembershipCode(accountNumber, membershipCode)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param offerId
	 * @return offer counter for input offer id
	 */
	public OfferCounter findCounterForCurrentOffer(String offerId) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		OfferCounter result = !StringUtils.isEmpty(offerId) 
				? offerCountersRepository.findByOfferId(offerId) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param offerCountersList
	 * @return list of offer counter after saving the offer counter list
	 */
	public List<OfferCounter> saveAllOfferCounters(List<OfferCounter> offerCountersList) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		List<OfferCounter> result = !CollectionUtils.isEmpty(offerCountersList)
				? offerCountersRepository.saveAll(offerCountersList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		return result;

	}

	/**
	 * 
	 * @return list of all offer counters
	 */
	public List<OfferCounter> getAllCounters() {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		List<OfferCounter> result = offerCountersRepository.findAll();
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		return result;

	}

	/**
	 * 
	 * @param offerIdList
	 * @return list of all active offers from input offer id list for specific
	 *         channel id
	 */
	public List<OfferCatalog> findAllActiveOffersFromList(List<String> offerIdList, String channelId) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		List<OfferCatalog> result = offerRepository.findAllActiveOffersFromList(
				Arrays.asList(OfferConstants.ACTIVE_STATUS.get(), OfferConstants.ACTIVE_STATUS.get().toUpperCase(),
						OfferConstants.ACTIVE_STATUS.get().toLowerCase()),
				Arrays.asList(channelId), Arrays.asList(OfferConstants.FLAG_SET.get(),
						OfferConstants.FLAG_SET.get().toUpperCase(), OfferConstants.FLAG_SET.get().toLowerCase()),
				new Date(), offerIdList);
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param channelId
	 * @return list of all active birthday offers
	 */
	public List<OfferCatalog> listBirthdayOffers(String channelId) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		List<OfferCatalog> birthdayOffers = offerRepository.findAllActiveBirthdayOffers(
				Arrays.asList(OfferConstants.ACTIVE_STATUS.get(), OfferConstants.ACTIVE_STATUS.get().toUpperCase(),
						OfferConstants.ACTIVE_STATUS.get().toLowerCase()),
				Arrays.asList(channelId), Arrays.asList(OfferConstants.FLAG_SET.get(),
						OfferConstants.FLAG_SET.get().toUpperCase(), OfferConstants.FLAG_SET.get().toLowerCase()),
				new Date());
		List<OfferCatalog> result = !CollectionUtils.isEmpty(birthdayOffers)
				? FilterValues.filterOfferList(birthdayOffers, Predicates.activeMerchantAndStore())
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}

	/***
	 * 
	 * @param string
	 * @return list of all offers where gift offer value is equivalent to input flag
	 */
	public List<OfferCatalog> findByIsGift(String flag) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		List<OfferCatalog> result = !StringUtils.isEmpty(flag) 
				? offerRepository.findByIsGift(flag) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param asList
	 * @return list of all categories with category id in input list
	 */
	public List<Category> getCategoryListByIdList(List<String> categoryIdList) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		List<Category> result = CollectionUtils.isNotEmpty(categoryIdList)
				? categoryRepository.findCategoryByIdList(categoryIdList)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param date
	 * @return list of all bill payment/ recharge records in last 30 days
	 */
	public List<PurchaseHistory> findBillPaymentRechargeRecordsforCurrentAccountInLast30days(String accountNumber,
			String membershipCode, Date date) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		List<PurchaseHistory> result = !StringUtils.isEmpty(accountNumber) && !StringUtils.isEmpty(membershipCode)
				&& !ObjectUtils.isEmpty(date)
				? purchaseRepository.findSuccessfulRecordsByPurchaseItemsInSpecifiedDurationForSpecificAccount(
				  OfferConstants.SUCCESS.get(), Arrays.asList(OffersConfigurationConstants.billPaymentItem,
						  OffersConfigurationConstants.rechargeItem), date, accountNumber, membershipCode)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param counterOfferIdList
	 * @return list of all offer counters with offer id in the input list
	 */
	public List<OfferCounter> getAllCountersByIdList(List<String> offerIdList) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		List<OfferCounter> result = !CollectionUtils.isEmpty(offerIdList)
				? offerCountersRepository.findByOfferIdList(offerIdList, Arrays.asList(OffersConfigurationConstants.cinemaRule))
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param subOfferId
	 * @return offer with sub offer id same as input
	 */
	public OfferCatalog findOfferBySubOfferId(String subOfferId) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		OfferCatalog result = !ObjectUtils.isEmpty(subOfferId) 
				? offerRepository.findBySubOfferId(subOfferId) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param offerId
	 * @return offerCatalof fetched from repository with same offer Id as input
	 */
	public OfferCatalog findByOfferId(String offerId) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		OfferCatalog result = !StringUtils.isEmpty(offerId) 
				? offerRepository.findByOfferId(offerId) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}

	/**
	 * Loyalty as a service.
	 * @param offerId
	 * @return offerCatalof fetched from repository with same offer Id as input
	 */
	public OfferCatalog findByOfferIdAndProgramCode(String offerId, Headers header) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		OfferCatalog result = !StringUtils.isEmpty(offerId) 
				? offerRepository.findByOfferIdAndProgramCodeIgnoreCase(offerId, header.getProgram()) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}
	
	/**
	 * 
	 * @param purchaseToSave
	 * @return purchase history record after saving the purchase history
	 */
	public PurchaseHistory savePurchaseHistory(PurchaseHistory purchaseHistory) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		PurchaseHistory result = !ObjectUtils.isEmpty(purchaseHistory) 
				? purchaseRepository.save(purchaseHistory)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param offerCatalog
	 * @return offer record after saving the offer
	 */
	public OfferCatalog saveOffer(OfferCatalog offerCatalog) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		OfferCatalog result = !ObjectUtils.isEmpty(offerCatalog) 
				? offerRepository.save(offerCatalog) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param offerCode
	 * @return offer with same offer code
	 */
	public OfferCatalog findByOfferCode(String offerCode) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		OfferCatalog result = !StringUtils.isEmpty(offerCode) 
				? offerRepository.findByOfferCode(offerCode) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return result;
	}
	
	/**
	 * Loyalty as a service.
	 * @param validOfferCriteria
	 * @param page
	 * @param pageLimit
	 * @param offerType
	 * @param status
	 * @param resultResponse
	 * @return Filtered offer list for portal
	 */
	private OfferListAndCount filteredPortalOffersForProgramCode(Criteria validOfferCriteria, Integer page, Integer pageLimit,
			String offerType, String status, ResultResponse resultResponse, Headers header) {
		
		Query offerCatalogQuery = new Query();
		offerCatalogQuery.addCriteria(validOfferCriteria);
		
		//Loyalty as a service.
		Criteria programCodeCriteria = Criteria.where(MarketplaceDBConstants.PROGRAM_CODE)
				.regex(ProcessValues.getRegexStartEndExpression(header.getProgram()),
						OfferConstants.CASE_INSENSITIVE.get());
		offerCatalogQuery.addCriteria(programCodeCriteria);
		
		addFiltersToOfferCatalogQueryForProgramCode(status, offerType, offerCatalogQuery, resultResponse, header);
		includeShortResponseOfferFields(offerCatalogQuery);
		return Checks.checkNoErrors(resultResponse) ? getOfferListWithDynamicQuery(page, pageLimit, offerCatalogQuery) : null;
		
	}

	/**
	 * Loyalty as a service.
	 * Adds filters for the the dynamic query
	 * @param status
	 * @param offerType
	 * @param offerCatalogQuery 
	 * @param resultResponse 
	 */
	private void addFiltersToOfferCatalogQueryForProgramCode(String status, String offerType, Query offerCatalogQuery, ResultResponse resultResponse, Headers header) {
		
		if(!ObjectUtils.isEmpty(offerCatalogQuery)) {
			
			if(!StringUtils.isEmpty(status)) {
				
				Criteria statusCriteria = Criteria.where(OffersDBConstants.STATUS)
						.regex(ProcessValues.getRegexStartEndExpression(status),
								OfferConstants.CASE_INSENSITIVE.get());
				offerCatalogQuery.addCriteria(statusCriteria);
				
			}
			
			if(!StringUtils.isEmpty(offerType)) {
				
				OfferType fetchedOfferType = offerTypeRepository.findByOfferTypeIdAndProgramCodeIgnoreCase(offerType, header.getProgram());
				Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(fetchedOfferType), OfferErrorCodes.INVALID_OFFER_TYPE, resultResponse);
				Criteria offerTypeCriteria = Criteria.where(MarketplaceDBConstants.OFFER_TYPE)
						.is(fetchedOfferType);
				offerCatalogQuery.addCriteria(offerTypeCriteria);
				
			}
			
		}
		
	}
	
	/**
	 * Includes only the required fields for the record
	 * @param offerCatalogQuery
	 */
	private void includeShortResponseOfferFields(Query offerCatalogQuery) {
		
		offerCatalogQuery.fields()
		    .include(OffersDBConstants.OFFER_ID)
			.include(OffersDBConstants.OFFER_CODE)
			.include(OffersDBConstants.OFFER_TYPE)
			.include(OffersDBConstants.TITLE_EN)
			.include(OffersDBConstants.TITLE_AR)
			.include(OffersDBConstants.TITLE_DESCRIPTION_EN)
			.include(OffersDBConstants.TITLE_DESCRIPTION_AR)
			.include(OffersDBConstants.STATUS)
			.include(OffersDBConstants.AVAILABLE_IN_PORTAL)
			.include(OffersDBConstants.MERCHANT_CODE)
			.exclude(OffersDBConstants.ID);
	}


	/**
	 * 
	 * @param pageLimit 
	 * @param page 
	 * @param offerCatalogQuery
	 * @return list of offers fetched using dynamic query
	 */
	private OfferListAndCount getOfferListWithDynamicQuery(Integer page, Integer pageLimit, Query offerCatalogQuery) {
		
		OfferListAndCount result = null;
		
        if(!ObjectUtils.isEmpty(offerCatalogQuery)) {
			
        	result = new OfferListAndCount();
        	result.setCount(getRecordCountForQuery(offerCatalogQuery));
			
        	String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
			LOG.info(log);
			
			if(!ObjectUtils.isEmpty(page) 
			&& !ObjectUtils.isEmpty(pageLimit)) {
				
				Pageable pageable = PageRequest.of(page, pageLimit);
				result.setOfferList(mongoOperations.find(offerCatalogQuery.with(pageable),
						OfferCatalog.class, OffersDBConstants.OFFER_CATALOG));
			
			} else {
				
				result.setOfferList(mongoOperations.find(offerCatalogQuery, 
						OfferCatalog.class, OffersDBConstants.OFFER_CATALOG));
				
		   }
			
			log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
			LOG.info(log);
			
		}
		
		return result;
	}

	
	/**
	 * 
	 * @param offerCatalogQuery
	 * @return the number of total records for query
	 */
	private Integer getRecordCountForQuery(Query offerCatalogQuery) {
		
		String log = Logs.logBeforeGettingRecordCount(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		
		int count = (int) mongoOperations.count(offerCatalogQuery, OfferCatalog.class, OffersDBConstants.OFFER_CATALOG);
		
		log = Logs.logAfterGettingRecordCount(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);			
		
		return count;
	}
	
	/**
	 * Loyalty as a service.
	 * @param pageLimit
	 * @param page
	 * @param status 
	 * @param offerType 
	 * @param resultResponse 
	 * @return all offers with pagination
	 */
	public OfferListAndCount findAllAdminOffers(Integer page, Integer pageLimit, String offerType, String status, ResultResponse resultResponse, Headers header) {

		Criteria validOfferCriteria = Criteria.where(OffersDBConstants.OFFER_ID).exists(true);
		return filteredPortalOffersForProgramCode(validOfferCriteria, page, pageLimit, offerType, status, resultResponse, header);

	}

	/**
	 * Loyalty as a service.
	 * @param partnerCode
	 * @param pageLimit 
	 * @param page 
	 * @param offerCatalogResultResponse 
	 * @param status 
	 * @param offerType 
	 * @return all offers with same partner code as input
	 */
	public OfferListAndCount findOffersByPartnerCode(String partnerCode, Integer page, Integer pageLimit, String offerType, String status, ResultResponse resultResponse, Headers header) {

		Criteria validOfferCriteria = Criteria.where(OffersDBConstants.PARTNER_CODE).is(partnerCode);
		return filteredPortalOffersForProgramCode(validOfferCriteria, page, pageLimit, offerType, status, resultResponse, header);
		
	}

	/**
	 * Loyalty as a service.
	 * @param pageLimit 
	 * @param page 
	 * @param offerCatalogResultResponse 
	 * @param status 
	 * @param offerType 
	 * @param merchantCode
	 * @return all offers with same merchant as input
	 */
	public OfferListAndCount findOfferByMerchant(Merchant merchant, Integer page, Integer pageLimit, String offerType, String status, ResultResponse resultResponse, Headers header) {

		Criteria validOfferCriteria = Criteria.where(MarketplaceDBConstants.MERCHANT).is(merchant);
		return filteredPortalOffersForProgramCode(validOfferCriteria, page, pageLimit, offerType, status, resultResponse, header);
	
	}

	/**
	 * 
	 * @param offerType
	 * @return offer type with same description
	 */
	public OfferType getOfferTypeByDescription(String offerType) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		OfferType type = !StringUtils.isEmpty(offerType) 
				? offerTypeRepository.findByDescriptionEnglish(offerType)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		return type;
	}

	/**
	 * 
	 * @param offerTypeId
	 * @param status
	 * @return list of all offers with same offer type and status
	 */
	public List<OfferCatalog> getOffersByOfferTypeAndMerchant(String offerTypeId, String status) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		List<OfferCatalog> offerList = !StringUtils.isEmpty(offerTypeId) 
				&& !StringUtils.isEmpty(status)
				? offerRepository.findByOfferTypeMerchantCode(offerTypeId, status)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return offerList;
	}

	/**
	 * 
	 * @param purchaseItemList
	 * @return list of purchase payment methods with purchase item in input list
	 */
	public List<PurchasePaymentMethod> getPurchasePaymentMethodsByPurchaseItemList(List<String> purchaseItemList) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_PAYMENT_METHOD);
		LOG.info(log);
		List<PurchasePaymentMethod> purchasePaymentMethodList = !CollectionUtils.isEmpty(purchaseItemList)
				? purchasePaymentMethodRepository.findByPurchaseItemList(purchaseItemList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_PAYMENT_METHOD);
		LOG.info(log);
		return purchasePaymentMethodList;
	}
	
	/**
	 * Loyalty as a service.
	 * @param purchaseItemList
	 * @return list of purchase payment methods with purchase item in input list
	 */
	public List<PurchasePaymentMethod> getPurchasePaymentMethodsByPurchaseItemListByProgramCode(List<String> purchaseItemList, Headers header) {

		//Loyalty as a service.
		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_PAYMENT_METHOD);
		LOG.info(log);
		List<PurchasePaymentMethod> purchasePaymentMethodList = !CollectionUtils.isEmpty(purchaseItemList)
				? purchasePaymentMethodRepository.findByPurchaseItemListAndProgramCodeIgnoreCase(purchaseItemList, header.getProgram())
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_PAYMENT_METHOD);
		LOG.info(log);
		return purchasePaymentMethodList;
	}

	/**
	 * 
	 * @return birthday info
	 */
	public BirthdayInfo findBirthdayInfo() {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.BIRTHDAY_INFO);
		LOG.info(log);
		List<BirthdayInfo> birthdayInfoList = birthdayInfoRepository.findAll();
		log = Logs.logAfterHittingDB(OffersDBConstants.BIRTHDAY_INFO);
		LOG.info(log);
		return !CollectionUtils.isEmpty(birthdayInfoList) ? birthdayInfoList.get(0) : null;

	}
	
	/**
	 * Loyalty as a service.
	 * @return birthday info
	 */
	public BirthdayInfo findBirthdayInfoByProgramCode(Headers header) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.BIRTHDAY_INFO);
		LOG.info(log);
		List<BirthdayInfo> birthdayInfoList = birthdayInfoRepository.findByProgramCodeIgnoreCase(header.getProgram());
		log = Logs.logAfterHittingDB(OffersDBConstants.BIRTHDAY_INFO);
		LOG.info(log);
		return !CollectionUtils.isEmpty(birthdayInfoList) ? birthdayInfoList.get(0) : null;

	}

	/**
	 * 
	 * @param birthdayInfo
	 * @return birthday info after saving birthday info
	 */
	public BirthdayInfo saveBirthdayInfo(BirthdayInfo birthdayInfo) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.BIRTHDAY_INFO);
		LOG.info(log);
		BirthdayInfo savedInfo = !ObjectUtils.isEmpty(birthdayInfo) 
				? birthdayInfoRepository.save(birthdayInfo) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.BIRTHDAY_INFO);
		LOG.info(log);
		return savedInfo;

	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @return birthday gift tracker with same account number and membership code as
	 *         input
	 */
	public BirthdayGiftTracker getBirthdayGiftTrackerForCurrentAccount(String accountNumber, String membershipCode) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		BirthdayGiftTracker birthdayHelper = !StringUtils.isEmpty(accountNumber) 
				&& !StringUtils.isEmpty(membershipCode)
				? birthdayGiftTrackerRepository.findByAccountNumberAndMembershipCode(accountNumber, membershipCode)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		return birthdayHelper;
	}
	
	/**
	 * Loyalty as a service.
	 * @param accountNumber
	 * @param membershipCode
	 * @return birthday gift tracker with same account number and membership code as
	 *         input
	 */
	public BirthdayGiftTracker getBirthdayGiftTrackerForCurrentAccountByProgramCode(String accountNumber, String membershipCode, Headers header) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		BirthdayGiftTracker birthdayHelper = !StringUtils.isEmpty(accountNumber) 
				&& !StringUtils.isEmpty(membershipCode)
				? birthdayGiftTrackerRepository.findByAccountNumberAndMembershipCodeAndProgramCodeIgnoreCase(accountNumber, membershipCode, header.getProgram())
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		return birthdayHelper;
	}

	/**
	 * 
	 * @param offerIdList
	 * @return list of all images with domain id as in input list
	 */
	public List<MarketplaceImage> getImageForOfferList(List<String> offerIdList) {

		String log = Logs.logBeforeHittingDB(ImageConfigurationConstants.DB_MARKETPLACE_IMAGE);
		LOG.info(log);
		List<MarketplaceImage> imageList = !CollectionUtils.isEmpty(offerIdList)
				? imageRepository.findByImageCategoryAndDomainIdList(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER,
						offerIdList)
				: null;
		log = Logs.logAfterHittingDB(ImageConfigurationConstants.DB_MARKETPLACE_IMAGE);
		LOG.info(log);
		return imageList;
	}
	
	/**
	 * Loyalty as a service.
	 * @param offerIdList
	 * @return list of all images with domain id as in input list
	 */
	public List<MarketplaceImage> getImageForOfferListByProgramCode(List<String> offerIdList, Headers header) {

		//Loyalty as a service.
		String log = Logs.logBeforeHittingDB(ImageConfigurationConstants.DB_MARKETPLACE_IMAGE);
		LOG.info(log);
		List<MarketplaceImage> imageList = !CollectionUtils.isEmpty(offerIdList)
				? imageRepository.findByImageCategoryAndDomainIdListAndProgramCodeIgnoreCase(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER,
						offerIdList, header.getProgram())
				: null;
		log = Logs.logAfterHittingDB(ImageConfigurationConstants.DB_MARKETPLACE_IMAGE);
		LOG.info(log);
		return imageList;
	}

	/**
	 * 
	 * @param offersList
	 * @param channelId
	 * @return list of all offers with offer id as in offer list and available in
	 *         portal as input
	 */
	public List<OfferCatalog> getAllActiveOffersByOfferId(List<String> offersList, String channelId) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		List<OfferCatalog> offerCatalogList = !CollectionUtils.isEmpty(offersList) && !StringUtils.isEmpty(channelId)
				? offerRepository.findAllActiveOffersFromOfferIdList(
						        Arrays.asList(OfferConstants.ACTIVE_STATUS.get(),
								OfferConstants.ACTIVE_STATUS.get().toUpperCase(),
								OfferConstants.ACTIVE_STATUS.get().toLowerCase()),
						        Arrays.asList(channelId), new Date(), offersList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);

		return !CollectionUtils.isEmpty(offerCatalogList)
				? FilterValues.filterOfferList(offerCatalogList, Predicates.activeMerchantAndStore())
				: null;

	}

	/**
	 * 
	 * @param wishlist
	 * @return wishlist after saving wishlist
	 */
	public WishlistEntity saveWishlist(WishlistEntity wishlist) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.WISHLIST);
		LOG.info(log);
		WishlistEntity savedWishlist = !ObjectUtils.isEmpty(wishlist) 
				? wishlistRepository.save(wishlist) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.WISHLIST);
		LOG.info(log);
		return savedWishlist;
	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @return wishlist with same account number and membership code
	 */
	public WishlistEntity findWishlistForSpecificAccount(String accountNumber, String membershipCode) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.WISHLIST);
		LOG.info(log);
		WishlistEntity wishlist = !StringUtils.isEmpty(membershipCode)
				&& !StringUtils.isEmpty(accountNumber)
				? wishlistRepository.findByAccountNumberAndMembershipCode(accountNumber, membershipCode)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.WISHLIST);
		LOG.info(log);
		return wishlist;
	}

	/**
	 * 
	 * @param birthdayGiftTracker
	 * @return birthday gift tracker after saving birthday gift tracker
	 */
	public BirthdayGiftTracker saveBirthdayTracker(BirthdayGiftTracker birthdayGiftTracker) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		BirthdayGiftTracker birthdayHelper = !ObjectUtils.isEmpty(birthdayGiftTracker)
				? birthdayGiftTrackerRepository.save(birthdayGiftTracker)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		return birthdayHelper;
	}

	/**
	 * 
	 * @return count of offers in repository
	 */
	public Integer getOfferSize() {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		Integer size = (int) offerRepository.count();
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		return size;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerIdList
	 * @return List of all purchase history records with offer id in input list for
	 *         account number and membership code as in inut within specific
	 *         duration
	 */
	public List<PurchaseHistory> getPurchaseRecordsForGiftOffers(Date startDate, Date endDate, String accountNumber,
			String membershipCode, List<String> offerIdList) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		List<PurchaseHistory> purchaseRecords = !ObjectUtils.isEmpty(startDate) && !ObjectUtils.isEmpty(endDate)
				&& !ObjectUtils.isEmpty(accountNumber) && !ObjectUtils.isEmpty(membershipCode)
				&& !CollectionUtils.isEmpty(offerIdList)
						? purchaseRepository.findByStatusIgnoreCaseAndOfferIdInAndCreatedDateBetweenAndAccountNumberAndMembershipCode(
								OfferConstants.SUCCESS.get(), offerIdList, startDate, endDate, accountNumber,
								membershipCode)
						: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		return purchaseRecords;
	}

	/**
	 * 
	 * @param accountNumberList
	 * @param membershipCodeList
	 * @return List of birthday gift tracker with account number and membership code
	 *         as in input list
	 */
	public List<BirthdayGiftTracker> getBirthdayTrackerListForAccountList(List<String> accountNumberList,
			List<String> membershipCodeList) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		List<BirthdayGiftTracker> birthdayTrackerList = !CollectionUtils.isEmpty(accountNumberList)
				&& !CollectionUtils.isEmpty(membershipCodeList)
				? birthdayGiftTrackerRepository
				.findByAccountNumberInAndMembershipCodeIn(accountNumberList, membershipCodeList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.BIRTHDAY_HELPER);
		LOG.info(log);
		return birthdayTrackerList;
	}

	/**
	 * 
	 * @param offerCounterToSave
	 * @return offer counter after saving offer counter
	 */
	public OfferCounter saveOfferCounter(OfferCounter offerCounterToSave) {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		OfferCounter offerCounter = !ObjectUtils.isEmpty(offerCounterToSave)
				? offerCountersRepository.save(offerCounterToSave)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTER);
		LOG.info(log);
		return offerCounter;
	}

	/**
	 * 
	 * @param eligibleOffersRequest
	 * @param memberDetails
	 * @param channelId
	 * @param dayEligiblity 
	 * @return list of active and eligible offers after applying dynamic query
	 * @throws ParseException 
	 */
	public void getEligibleOffersList(EligibleOffersFiltersRequest eligibleOffersRequest,
				EligibilityInfo eligibilityInfo, String channelId) throws ParseException{

		Query offerCatalogQuery = new Query();

		Criteria merchantCriteria = !StringUtils.isEmpty(eligibleOffersRequest.getMerchantCode())
				? new Criteria().and(OffersDBConstants.STATUS)
						        .regex(ProcessValues.getRegexStartEndExpression(OfferConstants.ACTIVE_STATUS.get()),
						        	   OfferConstants.CASE_INSENSITIVE.get())
						        .and(OffersDBConstants.MERCHANT_CODE)
						        .is(eligibleOffersRequest.getMerchantCode())
				: new Criteria().and(OffersDBConstants.STATUS)
				                .regex(ProcessValues.getRegexStartEndExpression(OfferConstants.ACTIVE_STATUS.get()),
				                	   OfferConstants.CASE_INSENSITIVE.get());		
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.MERCHANT);
		LOG.info(log);
		
		Aggregation aggregation = newAggregation(project(from(field(OffersDBConstants.ID),
				field(OffersDBConstants.MERCHANT_CODE), field(OffersDBConstants.STATUS))),					
				match(merchantCriteria));
		
		AggregationResults<OfferMerchantDto> merchantResults = mongoOperations.aggregate(aggregation,
				MarketplaceDBConstants.MERCHANT, OfferMerchantDto.class);
        List<OfferMerchantDto> offerMerchantList = merchantResults.getMappedResults();
        log = Logs.logAfterHittingDB(MarketplaceDBConstants.MERCHANT);
		LOG.info(log);

		setActiveOfferCriteria(offerCatalogQuery, channelId, offerMerchantList);
		setCategorySubCategoryCriteria(eligibleOffersRequest, offerCatalogQuery);
		setTagsCriteria(eligibleOffersRequest, offerCatalogQuery);
		setGroupedFlagCriteria(eligibleOffersRequest, offerCatalogQuery);
		setNewOfferCriteria(eligibleOffersRequest, offerCatalogQuery);
		setMemberDetailsCriteria(eligibilityInfo.getMemberDetails(), offerCatalogQuery);
		
		eligibilityInfo.setRecordCount(getRecordCountForQuery(offerCatalogQuery));
		
    	log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		
		if(!ObjectUtils.isEmpty(eligibleOffersRequest.getPage()) 
		&& !ObjectUtils.isEmpty(eligibleOffersRequest.getPageLimit())) {
			
			eligibilityInfo.setOfferList(mongoOperations.find(offerCatalogQuery.with(PageRequest.of(eligibleOffersRequest.getPage(), eligibleOffersRequest.getPageLimit())),
					OfferCatalog.class, OffersDBConstants.OFFER_CATALOG));
		
		} else {
			
			eligibilityInfo.setOfferList(mongoOperations.find(offerCatalogQuery, 
					OfferCatalog.class, OffersDBConstants.OFFER_CATALOG));
			
	   }
		
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		
	}

	/**
	 * Setting criteria for active offer in offer catalog query
	 * 
	 * @param offerCatalogQuery
	 * @param flagList
	 * @param channelId
	 * @param merchantList
	 * @throws ParseException 
	 */
	private void setActiveOfferCriteria(Query offerCatalogQuery, String channelId, List<OfferMerchantDto> offerMerchantList) throws ParseException{

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.STORE);
		LOG.info(log);
		Aggregation aggregation = newAggregation(project(from(field(OffersDBConstants.ID),
				 field(OffersDBConstants.MERCHANT_CODE), field(OffersDBConstants.STATUS))),				
				match(new Criteria().and(OffersDBConstants.STATUS)
						.is(OfferConstants.ACTIVE_STATUS.get())
						.and(OffersDBConstants.MERCHANT_CODE)
						.in(offerMerchantList.stream().map(OfferMerchantDto::getMerchantCode).collect(Collectors.toList()))));
		
		AggregationResults<OfferMerchantDto> storeResults = mongoOperations.aggregate(aggregation,
				MarketplaceDBConstants.STORE, OfferMerchantDto.class);
        List<OfferMerchantDto> offerStoreList = storeResults.getMappedResults();
        
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.STORE);
		LOG.info(log);

		String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern(OfferConstants.TRANSACTIONS_DATE_FORMAT.get()));
		Date date = Utilities.changeStringToDateWithTimeFormatAndTimeZone(currentDate, OfferConstants.END_DATE_TIME.get());	
		
		Criteria activeOfferCriteria = Criteria
				.where(OffersDBConstants.MERCHANT_ID).in(offerMerchantList.stream().map(OfferMerchantDto::getId).collect(Collectors.toList()))
				.and(OffersDBConstants.STORE_ID).in(offerStoreList.stream().map(OfferMerchantDto::getId).collect(Collectors.toList()))
				.and(OffersDBConstants.STATUS)
				.regex(ProcessValues.getRegexStartEndExpression(OfferConstants.ACTIVE_STATUS.get()),
						OfferConstants.CASE_INSENSITIVE.get())
				.and(OffersDBConstants.AVAILABLE_IN_PORTAL)
				.regex(ProcessValues.getRegexStartEndExpression(channelId), OfferConstants.CASE_INSENSITIVE.get())
				.norOperator(ProcessValues.getRegexList(OffersDBConstants.IS_BIRTHDAY_GIFT,
						Arrays.asList(OfferConstants.FLAG_SET.get()), true));

		offerCatalogQuery.addCriteria(activeOfferCriteria);
		
		Criteria dateCriteria = Criteria.where(OffersDBConstants.OFFER_DATE).exists(true)
				.and(OffersDBConstants.OFFER_END_DATE).not().lte(date);
		
		offerCatalogQuery.addCriteria(dateCriteria);
	}

	/**
	 * Setting category and subcategory criteria in dynamic offer catalog query
	 * @param eligibleOffersRequest
	 * @param offerCatalogQuery
	 */
	private void setCategorySubCategoryCriteria(EligibleOffersFiltersRequest eligibleOffersRequest,
			Query offerCatalogQuery) {

		if (!ObjectUtils.isEmpty(eligibleOffersRequest.getCategoryId())
		 || !CollectionUtils.isEmpty(eligibleOffersRequest.getSubCategoryList())) {

			if (!ObjectUtils.isEmpty(eligibleOffersRequest.getCategoryId())) {

				Criteria categoryCriteria = Criteria.where(OffersDBConstants.OFFER_CATEGORY_ID)
						         .is(eligibleOffersRequest.getCategoryId());
				offerCatalogQuery.addCriteria(categoryCriteria);
			}

			if (!CollectionUtils.isEmpty(eligibleOffersRequest.getSubCategoryList())) {

				Criteria subCategoryCriteria = Criteria.where(OffersDBConstants.OFFER_SUBCATEGORY_ID)
						.in(eligibleOffersRequest.getSubCategoryList());
				offerCatalogQuery.addCriteria(subCategoryCriteria);
			}

		}

	}

	/**
	 * Setting tags criteria in dynamic offer catalog query
	 * 
	 * @param eligibleOffersRequest
	 * @param offerCatalogQuery
	 */
	private void setTagsCriteria(EligibleOffersFiltersRequest eligibleOffersRequest, Query offerCatalogQuery) {

		if (!ObjectUtils.isEmpty(eligibleOffersRequest.getKeywords())
		 || !ObjectUtils.isEmpty(eligibleOffersRequest.getEmirate())
		 || !CollectionUtils.isEmpty(eligibleOffersRequest.getAreas())) {

			Criteria tagsEnCriteria = null;
			Criteria tagsArCriteria = null;
		
			if (!ObjectUtils.isEmpty(eligibleOffersRequest.getKeywords())
			&& !ObjectUtils.isEmpty(eligibleOffersRequest.getEmirate())
			&& !CollectionUtils.isEmpty(eligibleOffersRequest.getAreas())) {

				tagsEnCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_EN,
											Arrays.asList(eligibleOffersRequest.getKeywords()), false)),
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_EN,
											Arrays.asList(eligibleOffersRequest.getEmirate()), false)),
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true).orOperator(ProcessValues
									.getRegexList(OffersDBConstants.TAGS_EN, eligibleOffersRequest.getAreas(), false)));
				
				tagsArCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_AR,
											Arrays.asList(eligibleOffersRequest.getKeywords()), false)),
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_AR,
											Arrays.asList(eligibleOffersRequest.getEmirate()), false)),
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true).orOperator(ProcessValues
									.getRegexList(OffersDBConstants.TAGS_AR, eligibleOffersRequest.getAreas(), false)));

			} else if (!ObjectUtils.isEmpty(eligibleOffersRequest.getKeywords())
					&& !ObjectUtils.isEmpty(eligibleOffersRequest.getEmirate())) {

				tagsEnCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_EN,
											Arrays.asList(eligibleOffersRequest.getKeywords()), false)),
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true).orOperator(ProcessValues.getRegexList(
									OffersDBConstants.TAGS_EN, Arrays.asList(eligibleOffersRequest.getEmirate()), false)));
				
				tagsArCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_AR,
											Arrays.asList(eligibleOffersRequest.getKeywords()), false)),
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true).orOperator(ProcessValues.getRegexList(
									OffersDBConstants.TAGS_AR, Arrays.asList(eligibleOffersRequest.getEmirate()), false)));

			} else if (!ObjectUtils.isEmpty(eligibleOffersRequest.getEmirate())
					&& !CollectionUtils.isEmpty(eligibleOffersRequest.getAreas())) {

				tagsEnCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_EN,
											Arrays.asList(eligibleOffersRequest.getEmirate()), false)),
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true).orOperator(ProcessValues
									.getRegexList(OffersDBConstants.TAGS_EN, eligibleOffersRequest.getAreas(), false)));
				
				tagsArCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_AR,
											Arrays.asList(eligibleOffersRequest.getEmirate()), false)),
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true).orOperator(ProcessValues
									.getRegexList(OffersDBConstants.TAGS_AR, eligibleOffersRequest.getAreas(), false)));

			} else if (!ObjectUtils.isEmpty(eligibleOffersRequest.getKeywords())
					&& !CollectionUtils.isEmpty(eligibleOffersRequest.getAreas())) {

				tagsEnCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_EN,
											Arrays.asList(eligibleOffersRequest.getKeywords()), false)),
							Criteria.where(OffersDBConstants.TAGS_EN).exists(true).orOperator(ProcessValues
									.getRegexList(OffersDBConstants.TAGS_EN, eligibleOffersRequest.getAreas(), false)));
				
				tagsArCriteria = new Criteria()
						.andOperator(
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true)
									.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_AR,
											Arrays.asList(eligibleOffersRequest.getKeywords()), false)),
							Criteria.where(OffersDBConstants.TAGS_AR).exists(true).orOperator(ProcessValues
									.getRegexList(OffersDBConstants.TAGS_AR, eligibleOffersRequest.getAreas(), false)));

			} else if (!ObjectUtils.isEmpty(eligibleOffersRequest.getKeywords())) {

				tagsEnCriteria = Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
						.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_EN,
								Arrays.asList(eligibleOffersRequest.getKeywords()), false));

				tagsArCriteria = Criteria.where(OffersDBConstants.TAGS).exists(true)
						.and(OffersDBConstants.TAGS_AR).exists(true)
						.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_AR,
								Arrays.asList(eligibleOffersRequest.getKeywords()), false));

			} else if (!ObjectUtils.isEmpty(eligibleOffersRequest.getEmirate())) {

				tagsEnCriteria = Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
						.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_EN,
								Arrays.asList(eligibleOffersRequest.getEmirate()), false));

				tagsArCriteria = Criteria.where(OffersDBConstants.TAGS_AR).exists(true)
						.orOperator(ProcessValues.getRegexList(OffersDBConstants.TAGS_AR,
								Arrays.asList(eligibleOffersRequest.getEmirate()), false));

			} else if (!CollectionUtils.isEmpty(eligibleOffersRequest.getAreas())) {

				tagsEnCriteria = Criteria.where(OffersDBConstants.TAGS_EN).exists(true)
						.orOperator(
								ProcessValues.getRegexList(OffersDBConstants.TAGS_EN, eligibleOffersRequest.getAreas(), false));

				tagsArCriteria = Criteria.where(OffersDBConstants.TAGS_AR).exists(true)
						.orOperator(
								ProcessValues.getRegexList(OffersDBConstants.TAGS_AR, eligibleOffersRequest.getAreas(), false));

			}

			Criteria tagsCriteria = Criteria.where(OffersDBConstants.TAGS).exists(true)
	                .orOperator(tagsEnCriteria, tagsArCriteria);
			offerCatalogQuery.addCriteria(tagsCriteria);

		}

	}

	/**
	 * Setting grouped flag criteria in dynamic offer catalog query
	 * 
	 * @param eligibleOffersRequest
	 * @param offerCatalogQuery
	 * @param flagList
	 */
	private void setGroupedFlagCriteria(EligibleOffersFiltersRequest eligibleOffersRequest, Query offerCatalogQuery) {

		if (eligibleOffersRequest.isGrouped()) {

			Criteria groupedCriteria = Criteria.where(OffersDBConstants.GROUPED_FLAG)
					.regex(ProcessValues.getRegexStartEndExpression(OfferConstants.FLAG_SET.get()),
					OfferConstants.CASE_INSENSITIVE.get());
			offerCatalogQuery.addCriteria(groupedCriteria);

		}

	}

	/**
	 * Setting new offer criteria in dynamic offer catalog query
	 * 
	 * @param eligibleOffersRequest
	 * @param offerCatalogQuery
	 * @param flagList
	 */
	private void setNewOfferCriteria(EligibleOffersFiltersRequest eligibleOffersRequest, Query offerCatalogQuery) {

		if (eligibleOffersRequest.isNewOffer()) {

			Criteria newOfferCriteria = Criteria.where(OffersDBConstants.NEW_OFFER)
					.regex(ProcessValues.getRegexStartEndExpression(OfferConstants.FLAG_SET.get()),
					OfferConstants.CASE_INSENSITIVE.get());
			offerCatalogQuery.addCriteria(newOfferCriteria);

		}

	}

	/**
	 * Setting customer type and customer segment criteria in dynamic offer catalog
	 * query
	 * 
	 * @param eligibleOffersRequest
	 * @param memberDetails
	 * @param offerCatalogQuery
	 * @param dayEligiblity 
	 */
	private void setMemberDetailsCriteria(GetMemberResponse memberDetails, Query offerCatalogQuery) {

		if (!ObjectUtils.isEmpty(memberDetails)
		&& !CollectionUtils.isEmpty(memberDetails.getCustomerType())
		&& !CollectionUtils.isEmpty(memberDetails.getCustomerSegment())) {

			Criteria exclusionTypeCriteria = new Criteria()
					.orOperator(Criteria.where(OffersDBConstants.CUSTOMER_TYPES_EXCLUSION).exists(false),
							new Criteria().norOperator(
									ProcessValues.getRegexList(OffersDBConstants.CUSTOMER_TYPES_EXCLUSION,
											memberDetails.getCustomerType(), true)));
			Criteria eligibleTypeCriteria = new Criteria().orOperator(ProcessValues.getRegexList(
					OffersDBConstants.CUSTOMER_TYPES_ELIGIBLE, memberDetails.getCustomerType(), true));

			Criteria customerTypeCriteria = Criteria.where(OffersDBConstants.CUSTOMER_TYPES).exists(true)
					.and(OffersDBConstants.CUSTOMER_TYPES_ELIGIBLE).exists(true)
					.andOperator(eligibleTypeCriteria, exclusionTypeCriteria);

			Criteria rulesCriteria = Criteria.where(OffersDBConstants.RULES)
					.regex(ProcessValues.getRegexStartEndExpression(OffersConfigurationConstants.cinemaRule),
						   OfferConstants.CASE_INSENSITIVE.get());
					                   
			Criteria eligibleSegmentCriteria = new Criteria().orOperator(
					Criteria.where(OffersDBConstants.CUSTOMER_SEGMENTS_ELIGIBLE).exists(false),
					Criteria.where(OffersDBConstants.CUSTOMER_SEGMENTS_ELIGIBLE).exists(true)
				   .orOperator(ProcessValues.getRegexList(OffersDBConstants.CUSTOMER_SEGMENTS_ELIGIBLE,
							memberDetails.getCustomerSegment(), true)));
			
			Criteria exclusionSegmentCriteria = new Criteria().orOperator(
					Criteria.where(OffersDBConstants.CUSTOMER_SEGMENTS_EXCLUSION).exists(false),
					Criteria.where(OffersDBConstants.CUSTOMER_SEGMENTS_EXCLUSION).exists(true)
				   .norOperator(ProcessValues.getRegexList(OffersDBConstants.CUSTOMER_SEGMENTS_EXCLUSION,
							memberDetails.getCustomerSegment(), true)));
			
			Criteria segmentPresentCriteria = Criteria.where(OffersDBConstants.CUSTOMER_SEGMENTS).exists(true)
									.andOperator(eligibleSegmentCriteria,
											     exclusionSegmentCriteria);
			Criteria customerSegmentCriteria = new Criteria().orOperator(
					 rulesCriteria, 
					 Criteria.where(OffersDBConstants.CUSTOMER_SEGMENTS).exists(false),
					 segmentPresentCriteria);
			
			Criteria memberCriteria = new Criteria().andOperator(customerTypeCriteria, customerSegmentCriteria);
			offerCatalogQuery.addCriteria(memberCriteria);
				
		}

	}

	/**
	 * 
	 * @param errorRecord
	 * @return saved error record
	 */
	public ErrorRecords saveErrorRecord(ErrorRecords errorRecord) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		ErrorRecords savedErrorRecord = !ObjectUtils.isEmpty(errorRecord)
				? errorRepository.save(errorRecord)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		return savedErrorRecord;
		
	}
	
	/**
	 * 
	 * @param offerId
	 * @return
	 */
	public List<ErrorRecords> findErrorRecordForOffer(String offerId) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		List<ErrorRecords> errorRecordList = !StringUtils.isEmpty(offerId)
				? errorRepository.findByOfferIdAndStatusIgnoreCase(offerId, OfferConstants.NEW_STATUS.get())
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		return errorRecordList;
		
	}
	
	/**
	 * Delete error record
	 * @param errorRecord
	 */
	public void deleteErrorRecord(List<ErrorRecords> errorRecordList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		
		if(!CollectionUtils.isEmpty(errorRecordList)) {
			
			errorRepository.deleteAll(errorRecordList);
		}
		
		log = Logs.logAfterHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		
	}

	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param startDate
	 * @param endDate
	 * @return list of birthday gift or bill payment/ recharge purchase history records 
	 */
	public List<PurchaseHistory> findBirthdayBillPaymentAndRechargePurchaseRecords(String accountNumber,
			String membershipCode, Date startDate, Date endDate) {
		
		List<PurchaseHistory> purchaseHistoryRecords = null;
		
		if(!ObjectUtils.isEmpty(accountNumber)
		&& !ObjectUtils.isEmpty(membershipCode)) {
			
			Query query = new Query();
			
			query.addCriteria(Criteria.where(OffersDBConstants.STATUS)
					.regex(ProcessValues.getRegexStartEndExpression(OfferConstants.SUCCESS.get()),
							OfferConstants.CASE_INSENSITIVE.get())
					.and(OffersDBConstants.ACCOUNT_NUMBER).is(accountNumber)
					.and(OffersDBConstants.MEMBERSHIP_CODE).is(membershipCode));
			
			Criteria birthdayCriteria = Criteria.where(OffersDBConstants.ADDITIONAL_DETAILS).exists(true)
					.and(OffersDBConstants.IS_BIRTHDAY).is(true)
					.andOperator(Criteria.where(OffersDBConstants.CREATED_DATE).gte(startDate),
							Criteria.where(OffersDBConstants.CREATED_DATE).lte(endDate));
			
			Date date = Utilities.getDate(OffersConfigurationConstants.dmDuration);
			Criteria billPaymentRechargeCriteria = Criteria.where(OffersDBConstants.CREATED_DATE).gte(date)
					.orOperator(ProcessValues.getRegexList(OffersDBConstants.PURCHASE_ITEM, 
							OffersListConstants.ELIGIBLE_TELECOM_SPEND_ITEMS, true));
			
			query.addCriteria(new Criteria().orOperator(birthdayCriteria, billPaymentRechargeCriteria));
			
			String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
			LOG.info(log);
			purchaseHistoryRecords = mongoOperations.find(query, PurchaseHistory.class, OffersDBConstants.PURCHASE_HISTORY);
			log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
			LOG.info(log);
			
		}
		
		return purchaseHistoryRecords; 
	}

	/**
	 * Loyalty as a service.
	 * @param accountNumber
	 * @param membershipCode
	 * @param startDate
	 * @param endDate
	 * @return list of birthday gift or bill payment/ recharge purchase history records 
	 */
	public List<PurchaseHistory> findBirthdayBillPaymentAndRechargePurchaseRecordsByProgramCode(String accountNumber,
			String membershipCode, Date startDate, Date endDate, Headers headers) {
		
		List<PurchaseHistory> purchaseHistoryRecords = null;
		
		if(!ObjectUtils.isEmpty(accountNumber)
		&& !ObjectUtils.isEmpty(membershipCode)) {
			
			Query query = new Query();
			
			query.addCriteria(Criteria.where(OffersDBConstants.STATUS)
					.regex(ProcessValues.getRegexStartEndExpression(OfferConstants.SUCCESS.get()),
							OfferConstants.CASE_INSENSITIVE.get())
					.and(OffersDBConstants.ACCOUNT_NUMBER).is(accountNumber)
					.and(OffersDBConstants.MEMBERSHIP_CODE).is(membershipCode)
					.and(OffersDBConstants.PROGRAM_CODE).regex(OfferConstants.REGEX_START.get()+headers.getProgram()+OfferConstants.REGEX_END.get(), OfferConstants.CASE_INSENSITIVE.get()));
			
			Criteria birthdayCriteria = Criteria.where(OffersDBConstants.ADDITIONAL_DETAILS).exists(true)
					.and(OffersDBConstants.IS_BIRTHDAY).is(true)
					.andOperator(Criteria.where(OffersDBConstants.CREATED_DATE).gte(startDate),
							Criteria.where(OffersDBConstants.CREATED_DATE).lte(endDate));
			
			Date date = Utilities.getDate(OffersConfigurationConstants.dmDuration);
			Criteria billPaymentRechargeCriteria = Criteria.where(OffersDBConstants.CREATED_DATE).gte(date)
					.orOperator(ProcessValues.getRegexList(OffersDBConstants.PURCHASE_ITEM, 
							OffersListConstants.ELIGIBLE_TELECOM_SPEND_ITEMS, true));
			
			query.addCriteria(new Criteria().orOperator(birthdayCriteria, billPaymentRechargeCriteria));
			
			String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
			LOG.info(log);
			purchaseHistoryRecords = mongoOperations.find(query, PurchaseHistory.class, OffersDBConstants.PURCHASE_HISTORY);
			log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
			LOG.info(log);
			
		}
		
		return purchaseHistoryRecords; 
	}
	
	/**
	 * 
	 * @param accountNumberList
	 * @param membershipCodeList
	 * @param startDate
	 * @param endDate
	 * @return purchase history records of birthday offers for list of accounts
	 */
	public List<PurchaseHistory> findBirthdayOfferPurchaseInDuration(List<String> accountNumberList,
			List<String> membershipCodeList, Date startDate, Date endDate) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		List<PurchaseHistory> purchaseRecords = !CollectionUtils.isEmpty(accountNumberList)
				&& !CollectionUtils.isEmpty(membershipCodeList)
				&& !ObjectUtils.isEmpty(startDate)
				&& !ObjectUtils.isEmpty(endDate)
				? purchaseRepository.findBirthdayOffersForAccountList(OfferConstants.SUCCESS.get(), true, accountNumberList, membershipCodeList, startDate, endDate)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		return purchaseRecords;
	}

	/**
	 * 
	 * @param voucherAction
	 * @return voucher action by id
	 */
	public VoucherAction getVoucherActionById(String voucherActionId) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.VOUCHER_ACTION);
		LOG.info(log);
		Optional<VoucherAction> voucherAction = !StringUtils.isEmpty(voucherActionId)
				? voucherActionRepository.findById(voucherActionId)
				: null;		
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.VOUCHER_ACTION);
		LOG.info(log);
		return !ObjectUtils.isEmpty(voucherAction) 
			&& voucherAction.isPresent() 
			 ? voucherAction.get() 
			 : null;
	}

	/**
	 * 
	 * @param channelId
	 * @return list of all active offers in db
	 */
	public List<OfferCatalog> findAllActiveEligibleOffers(String channelId) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		
		List<OfferCatalog> offerList = offerRepository.findAllActiveEligibleOffers(
				Arrays.asList(OfferConstants.ACTIVE_STATUS.get(), 
						OfferConstants.ACTIVE_STATUS.get().toUpperCase(),
						OfferConstants.ACTIVE_STATUS.get().toLowerCase()),
				Arrays.asList(channelId), 
				Arrays.asList(OfferConstants.FLAG_SET.get(), 
						OfferConstants.FLAG_SET.get().toUpperCase(),
						OfferConstants.FLAG_SET.get().toLowerCase()),
				new Date());	
		
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		
		return offerList;
	}
	
	/**
	 * 
	 * @param channelId
	 * @return list of all active offers in db
	 */
	public List<OfferCatalog> findAllActiveOffers() {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_CATALOG);
		LOG.info(log);
		
		return offerRepository.findAllActiveOffers(Arrays.asList(OfferConstants.ACTIVE_STATUS.get(), 
				OfferConstants.ACTIVE_STATUS.get().toUpperCase(),
				OfferConstants.ACTIVE_STATUS.get().toLowerCase()),
				Arrays.asList(OfferConstants.FLAG_SET.get(), 
						OfferConstants.FLAG_SET.get().toUpperCase(),
						OfferConstants.FLAG_SET.get().toLowerCase()),
				new Date());	
		
	}
	
	/**
	 * 
	 * @return list of all eligible offers from repository
	 */
	public List<EligibleOffers> findAllEligibleOffers() {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
		LOG.info(log);
		
		return eligibleOfferRepository.findAll();
		
	}
	
	/**
	 * 
	 * @param eligibleOfferList
	 * @return list of saved eligible offers
	 */
    public List<EligibleOffers> saveAllEligibleOffers(List<EligibleOffers> eligibleOfferList) {
		
    	String log = Logs.logBeforeHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
		LOG.info(log);
    	
		return !CollectionUtils.isEmpty(eligibleOfferList)
			 ? eligibleOfferRepository.saveAll(eligibleOfferList) 
			 : null;
	}

    /**
     * Removes all existing eligible offers
     */
	public void removeAllEligibleOffers() {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
		LOG.info(log);
		
		eligibleOfferRepository.deleteAll();
		
		log = Logs.logAfterHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
		LOG.info(log);
		
	}

	/**
	 * 
	 * @return latest update date for eligible offers
	 */
	public Date getLatestUpdateForEligibleOffers() {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
		LOG.info(log);
		
		Aggregation aggregation = newAggregation(project(from(field(OffersDBConstants.LAST_UPDATE_DATE),field(OffersDBConstants.OFFER_ID))),				
				match(Criteria.where(OffersDBConstants.OFFER_ID).exists(true)));
		
		AggregationResults<UpdateDate> dateResults = mongoOperations.aggregate(aggregation,
                OffersDBConstants.ELIGIBLE_OFFERS, UpdateDate.class);
        
        log = Logs.logAfterHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
		LOG.info(log);
		
		return !CollectionUtils.isEmpty(dateResults.getMappedResults()) ? dateResults.getMappedResults().get(0).getDate() : null;
		
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @return status of user subscription
	 */
	public boolean checkUserSubscribed(String accountNumber, Headers header) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.SUBSCRIPTION_CATALOG);
		LOG.info(log);
		
		SubscriptionWithInterestRequestDto request = new SubscriptionWithInterestRequestDto();
		request.setAccountNumber(Arrays.asList(accountNumber));
		request.setCallSubscription(true);
		
		SubscriptionWithInterestResultResponse subscriptionresponse = !StringUtils.isEmpty(accountNumber) 
			      && !ObjectUtils.isEmpty(header) 
			      ? subscriptionController.isSubscribedWithInterest(request,
			    		  header.getProgram(), header.getAuthorization(), 
			    		  header.getExternalTransactionId(), header.getUserName(), header.getSessionId(), header.getUserPrev(), 
			    		  header.getChannelId(), header.getSystemId(), header.getSystemPassword(), header.getToken(), header.getTransactionId())
			      : null;
		SubscriptionWithInterestResponseDto subscriptionWithInterestResponseDto = !ObjectUtils.isEmpty(subscriptionresponse)
				&& !CollectionUtils.isEmpty(subscriptionresponse.getSubscriptionWithInterest())	
				? subscriptionresponse.getSubscriptionWithInterest().stream().filter(s->s.getAccountNumber().equals(accountNumber)).findFirst().orElse(null)
				: null;
		boolean result = !ObjectUtils.isEmpty(subscriptionWithInterestResponseDto)
				      && subscriptionWithInterestResponseDto.getSubscriptionStatus().equalsIgnoreCase("true");
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.SUBSCRIPTION_CATALOG);
		LOG.info(log);
		return result;
	}
	
	/**
	 * 
	 * @param offerId
	 * @param accountNumber
	 * @return account counter for input account and offerId
	 */
	public AccountOfferCounts findCounterForCurrentOfferAndAccount(String offerId, String accountNumber, String membershipCode) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		
		AccountOfferCounts accountOfferCounter = !StringUtils.isEmpty(offerId) && !StringUtils.isEmpty(accountNumber) && !StringUtils.isEmpty(membershipCode)
				 ? accountOfferCounterRepository.findByOfferIdAndAccountNumberAndMembershipCode(offerId, accountNumber, membershipCode)
				 : null;
		
		log = Logs.logAfterHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		
		return accountOfferCounter;
	}

	/**
	 * 
	 * @param offerCountersList
	 * @return list of saved account offer counters
	 */
	public List<AccountOfferCounts> saveAllAccountOfferCounters(List<AccountOfferCounts> offerCountersList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		List<AccountOfferCounts> result = !CollectionUtils.isEmpty(offerCountersList)
				? accountOfferCounterRepository.saveAll(offerCountersList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		return result;
	}
	
	/**
	 * 
	 * @param offerCountersList
	 * @return list of saved member offer counters
	 */
	public List<MemberOfferCounts> saveAllMemberOfferCounters(List<MemberOfferCounts> offerCountersList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		List<MemberOfferCounts> result = !CollectionUtils.isEmpty(offerCountersList)
				? memberOfferCounterRepository.saveAll(offerCountersList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		return result;
	}
	
	/**
	 * 
	 * @param offerCountersList
	 * @return list of saved offer counters
	 */
	public List<OfferCounters> saveAllOfferCounter(List<OfferCounters> offerCountersList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		List<OfferCounters> result = !CollectionUtils.isEmpty(offerCountersList)
				? offerCounterRepository.saveAll(offerCountersList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param offerCounterToSave
	 * @return saved offer counter
	 */
	public OfferCounters saveOfferCounters(OfferCounters offerCounterToSave) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		OfferCounters offerCounter = !ObjectUtils.isEmpty(offerCounterToSave)
				? offerCounterRepository.save(offerCounterToSave)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return offerCounter;
	}

	/**
	 * 
	 * @param offerCounterToSave
	 * @return saved member offer counter
	 */
	public MemberOfferCounts saveMemberOfferCounters(MemberOfferCounts offerCounterToSave) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		MemberOfferCounts offerCounter = !ObjectUtils.isEmpty(offerCounterToSave)
				? memberOfferCounterRepository.save(offerCounterToSave)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		return offerCounter;
	}
	
	/**
	 * 
	 * @param offerCounterToSave
	 * @return saved account member counter
	 */
	public AccountOfferCounts saveAccountOfferCounters(AccountOfferCounts offerCounterToSave) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		AccountOfferCounts offerCounter = !ObjectUtils.isEmpty(offerCounterToSave)
				? accountOfferCounterRepository.save(offerCounterToSave)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		return offerCounter;
	}

	/**
	 * 
	 * @return list of offer counters for offer that are cinema offers
	 * 
	 */
	public List<OfferCounters> findCinemaOfferCounters() {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		List<OfferCounters> offerCounterList = offerCounterRepository.findByRulesIn(Arrays.asList(OffersConfigurationConstants.cinemaRule));
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return offerCounterList;
	}

	/**
	 * 
	 * @param membershipCode
	 * @param counterOfferIdList
	 * @return member counters for input membershipcode and input offer list
	 */
	public List<MemberOfferCounts> findCurrentMemberCountersForOfferList(String membershipCode,
			List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		List<MemberOfferCounts> memberOfferCounters = 
				!StringUtils.isEmpty(membershipCode) && !CollectionUtils.isEmpty(offerIdList)
				? memberOfferCounterRepository.findByMembershipCodeAndOfferIdIn(membershipCode, offerIdList) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);		
		return memberOfferCounters;		
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param offerIdList
	 * @return list of account offer counter for current account and offer
	 */
	public List<AccountOfferCounts> findCurrentAccountCountersForOfferList(String accountNumber, String membershipCode,
			List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		List<AccountOfferCounts> accountOfferCounters = 
				!StringUtils.isEmpty(accountNumber) &&!StringUtils.isEmpty(membershipCode) && !CollectionUtils.isEmpty(offerIdList)
				? accountOfferCounterRepository.findByAccountNumberAndMembershipCodeAndOfferIdIn(accountNumber,
						membershipCode, offerIdList) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);		
		return accountOfferCounters;		
	}

	/**
	 * 
	 * @param offerId
	 * @param membershipCode
	 * @return member offer counter for current member and offerId
	 */
	public MemberOfferCounts findCounterForCurrentOfferAndMember(String offerId, String membershipCode) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		MemberOfferCounts memberOfferCounter = 
				!StringUtils.isEmpty(membershipCode) && !StringUtils.isEmpty(offerId)
				? memberOfferCounterRepository.findByMembershipCodeAndOfferId(membershipCode, offerId) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);		
		return memberOfferCounter;	
	}

	/**
	 * 
	 * @param offerId
	 * @return counter for input offer id
	 */
	public OfferCounters findOfferCounterForCurrentOffer(String offerId) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		OfferCounters offerCounter = !StringUtils.isEmpty(offerId)
				? offerCounterRepository.findByOfferId(offerId)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return offerCounter;
		
	}

	/**
	 * 
	 * @param counterOfferIdList
	 * @return offer counter list for cinema and non cinema offers
	 */
	public List<OfferCounters> findCinemaAndNonCinemaOfferCounters(List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		List<OfferCounters> offerCounterList = !CollectionUtils.isEmpty(offerIdList) 
				? offerCounterRepository.findByRulesInOrOfferIdIn(Arrays.asList(OffersConfigurationConstants.cinemaRule), offerIdList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return offerCounterList;
	}

	/**
	 * 
	 * @param counterOfferIdList
	 * @return offer counter list for input offer list
	 */
	public List<OfferCounters> getCounterListForOfferList(List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		List<OfferCounters> offerCounterList = !CollectionUtils.isEmpty(offerIdList) 
				? offerCounterRepository.findByOfferIdIn(offerIdList)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return offerCounterList;
	}

	/**
	 * 
	 * @param offerId
	 * @param accountNumber
	 * @param membershipCode
	 * @return error records for cinema offers and current offer with current account number
	 */
	public List<ErrorRecords> getErrorRecordsForCurrentAndCinemaOffers(String offerId, String accountNumber,
			String membershipCode) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		List<ErrorRecords> errorRecordList = !StringUtils.isEmpty(offerId)
				&& !StringUtils.isEmpty(accountNumber)
				&& !StringUtils.isEmpty(membershipCode)
				? errorRepository.findByCinemaOffersOrCurrentOffer(Arrays.asList(offerId), accountNumber, membershipCode, Arrays.asList(OffersConfigurationConstants.cinemaRule))
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		return errorRecordList;
	}

	/**
	 * 
	 * @param offerId
	 * @param accountNumber
	 * @param membershipCode
	 * @return error records for current offer
	 */
	public List<ErrorRecords> getErrorRecordsForCurrentOffer(String offerId, String accountNumber,
			String membershipCode) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		List<ErrorRecords> errorRecordList = !StringUtils.isEmpty(offerId)
				&& !StringUtils.isEmpty(accountNumber)
				&& !StringUtils.isEmpty(membershipCode)
				? errorRepository.findByOfferIdAndAccountNumberAndMembershipCode(offerId, accountNumber, membershipCode)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ERROR_RECORDS);
		LOG.info(log);
		return errorRecordList;
	}
	
	/**
	 * 
	 * @return list of all offer counters
	 */
	public List<OfferCounters> getAllOfferCounters() {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		List<OfferCounters> result = offerCounterRepository.findAll();
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return result;

	}
	
	/**
	 * 
	 * @return list of all member offer counters
	 */
	public List<MemberOfferCounts> getAllMemberOfferCounters() {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		List<MemberOfferCounts> result = memberOfferCounterRepository.findAll();
		log = Logs.logAfterHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		return result;

	}
	
	/**
	 * 
	 * @return list of all account offer counters
	 */
	public List<AccountOfferCounts> getAllAccountOfferCounters() {

		String log = Logs.logBeforeHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		List<AccountOfferCounts> result = accountOfferCounterRepository.findAll();
		log = Logs.logAfterHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		return result;

	}
	
	/**
	 * 
	 * @param accountNumberList
	 * @param membershipCodeList
	 * @param offerIdList
	 * @return list of account offer counters for the account, member, offer combination
	 */
	public List<AccountOfferCounts> getAccountCountersForAllInErrorRecordsAndCurrent(List<String> accountNumberList,
			List<String> membershipCodeList, List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		List<AccountOfferCounts> result = !CollectionUtils.isEmpty(accountNumberList) 
				&& !CollectionUtils.isEmpty(membershipCodeList) 
				&& !CollectionUtils.isEmpty(offerIdList) 
				? accountOfferCounterRepository.findByAccountNumberInAndMembershipCodeInAndOfferIdIn(accountNumberList,
						membershipCodeList, offerIdList) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.ACCOUNT_OFFER_COUNTERS);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param membershipCodeList
	 * @param offerIdList
	 * @return list of member offer counters for the member, offer combination
	 */
	public List<MemberOfferCounts> getMemberCountersForAllInErrorRecordsAndCurrent(List<String> membershipCodeList,
			List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		List<MemberOfferCounts> result = !CollectionUtils.isEmpty(membershipCodeList) 
				&& !CollectionUtils.isEmpty(offerIdList) 
				? memberOfferCounterRepository.findByMembershipCodeInAndOfferIdIn(membershipCodeList, offerIdList) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		return result;
	}

	/***
	 * 
	 * @param offerIdList
	 * @return list of offer counters for the offer list
	 */
	public List<OfferCounters> getOfferCountersForAllInErrorRecordsAndCurrent(List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		List<OfferCounters> result = !CollectionUtils.isEmpty(offerIdList) 
				? offerCounterRepository.findByOfferIdIn(offerIdList) 
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.OFFER_COUNTERS);
		LOG.info(log);
		return result;
	}
	
	/**
	 * 
	 * @param noOfdaysToadd
	 * @return list of birthday accounts
	 * @throws MarketplaceException 
	 */
	@SuppressWarnings("deprecation")
	public List<BirthdayAccountsDto> getBirthdayAccountsInfo(int noOfdaysToadd) throws MarketplaceException {
		
		MongoClient mongoClient = getDBConnection();
		MongoTemplate template = new MongoTemplate(mongoClient, memberManagementDatabase);
		
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DATE, noOfdaysToadd);
		today = c.getTime();
		LOG.info("getting account with birthday date : {}", today);
		DateFormat df = new SimpleDateFormat("dd-MM");
		String datestrng = df.format(today);
		long startTime = System.currentTimeMillis();
		Aggregation aggregation = newAggregation(project(from(field("AccountNumber"), field("FirstName"),
				field("LastName"), field("EmailId"), field("Language"), field("UiLanguage"), field("MembershipCode"),
				field(("Status"), ("Status")), field(("Code"), ("Code")), field("DOB"))).and("DOB")
						.dateAsFormattedString("%d-%m").as("BirthDate"),
				match(new Criteria().and("BirthDate").is(datestrng).and("Status.Code")
						.in(Arrays.asList(StatusCode.SUS.toString(), StatusCode.ACT.toString()))));
		AggregationResults<BirthdayAccountsDto> groupResults = template.aggregate(aggregation, "Accounts",
				BirthdayAccountsDto.class);
		LOG.info("getBirthdayAccountsInfo query execution time : {}", System.currentTimeMillis() - startTime);
		return groupResults.getMappedResults();
		
	}
	
	/**
	 * 
	 * @param noOfdaysToadd
	 * @return list of birthday accounts
	 * @throws MarketplaceException 
	 */
	@SuppressWarnings("deprecation")
	public List<BirthdayAccountsDto> getBirthdayAccounts(int noOfdaysToadd) throws MarketplaceException {
		
		MongoClient mongoClient = getDBConnection();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, memberManagementDatabase);
		
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DATE, noOfdaysToadd);
		today = c.getTime();
		LOG.info("getting account with birthday date : {}", today);
		DateFormat df = new SimpleDateFormat("dd-MM");
		String datestrng = df.format(today);
		long startTime = System.currentTimeMillis();
		Aggregation aggregation = newAggregation(project(from(field("AccountNumber"), field("FirstName"),
				field("LastName"), field("EmailId"), field("Language"), field("UiLanguage"), field("MembershipCode"),
				field(("Status"), ("Status")), field(("Code"), ("Code")), field("DOB"))).and("DOB")
						.dateAsFormattedString("%d-%m").as("BirthDate"),
				match(new Criteria().and("BirthDate").is(datestrng).and("Status.Code")
						.in(Arrays.asList(StatusCode.SUS.toString(), StatusCode.ACT.toString()))));
		AggregationResults<BirthdayAccountsDto> groupResults = mongoTemplate.aggregate(aggregation, "Accounts",
				BirthdayAccountsDto.class);
		LOG.info("getBirthdayAccountsInfo query execution time : {}", System.currentTimeMillis() - startTime);
		return groupResults.getMappedResults();
	
	}

	
	/**
	 * 
	 * @return db connection url
	 * @throws MarketplaceException
	 */
	public MongoClient getDBConnection() throws MarketplaceException {
		
		try {
			return new MongoClient(new MongoClientURI(memberMangementDBURI));
		} catch (Exception e) {
		  throw new MarketplaceException("RepositoryHelper", "getDBConnection", "Failed to connect DB", OfferExceptionCodes.FAILED_TO_CONNECT_MM_DB);	 
		}
	}

	/**
	 * 
	 * @param transactionNo
	 * @return purchase transaction fetched by mongo db id
	 */
	public PurchaseHistory getPurchaseRecordById(String id) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		Optional<PurchaseHistory> result = !StringUtils.isEmpty(id) 
				?  purchaseRepository.findById(id)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		return !ObjectUtils.isEmpty(result) && result.isPresent() ? result.get() : null;
	}

	/**
	 * 
	 * @param subscriptionItem
	 * @param externalTransactionId
	 * @return purchase transaction fetched by purchase item and external transaction id
	 */
	public PurchaseHistory getPurchaseRecordByPurchaseItemAndTransactionId(String purchaseItem,
			String externalTransactionId) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		PurchaseHistory result = !StringUtils.isEmpty(purchaseItem)
				&& !StringUtils.isEmpty(externalTransactionId)
				?  purchaseRepository.findByPurchaseItemAndExtRefNo(purchaseItem, externalTransactionId)
				: null;
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		return result;
	}

	/**
	 * 
	 * @param subscriptionId
	 * @return subscription catalog fetched by id
	 */
	public SubscriptionValues getSubscriptionCatalogValues(String subscriptionId) {
		
		Aggregation aggregation = newAggregation(project(from(field(OffersDBConstants.ID),
				field(OffersDBConstants.POINTS_VALUE))),					
				match(new Criteria().and(OffersDBConstants.ID).is(subscriptionId)));
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.SUBSCRIPTION_CATALOG);
		LOG.info(log);
	
		AggregationResults<SubscriptionValues> subscriptionResults = mongoOperations.aggregate(aggregation,
				MarketplaceDBConstants.SUBSCRIPTION_CATALOG, SubscriptionValues.class);
		
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.SUBSCRIPTION_CATALOG);
		LOG.info(log);
		return !ObjectUtils.isEmpty(subscriptionResults) ? subscriptionResults.getUniqueMappedResult() : null;
	}

	/**
	 * 
	 * @param membershipCodeList
	 * @param offerIdList
	 * @return list of memberOfferCounter with input membership code and offer id
	 */
	public List<MemberOfferCounts> fetchMemberOfferCounterForMembershipListAndOffer(List<String> membershipCodeList,
			List<String> offerIdList) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		
		List<MemberOfferCounts> memberOfferCounters = memberOfferCounterRepository.findByMembershipCodeInAndOfferIdIn(membershipCodeList, offerIdList);
		
		log = Logs.logAfterHittingDB(OffersDBConstants.MEMBER_OFFER_COUNTERS);
		LOG.info(log);
		
		return memberOfferCounters;
	}

	/***
	 * Reset all offer counters
	 * @param classz
	 * @param unit
	 */
	public void resetCounters(Class<?> classz, String unit) {
		
		LOG.info("Resetting  {}", unit);
		UpdateResult denominationUpdate = pushDenominations(classz);
		LOG.info("Denomination Pushed : {}", denominationUpdate.getModifiedCount());
		
		Integer totalCount = getTotalCountForCounter(unit);
		UpdateResult updateResult = null;
		LOG.info("Total counters to update : {}", totalCount);
		if(!ObjectUtils.isEmpty(totalCount) && totalCount>0) {
			
			updateResult = updateCounter(classz, OffersConfigurationConstants.ANNUAL);
			LOG.info("Annual count updated");
			
			Integer remainingCount = ProcessValues.getRemainingCount(totalCount, updateResult);
			LOG.info("Total counters remaining after updating annual count : {}", remainingCount);
			
			if(!ObjectUtils.isEmpty(remainingCount) && remainingCount>0) {
				
				updateResult = updateCounter(classz, OffersConfigurationConstants.MONTHLY);
				LOG.info("Monthly count updated");
				remainingCount = ProcessValues.getRemainingCount(totalCount, updateResult);
				LOG.info("Total counters remaining after updating monthly count : {}", remainingCount);
				
				if(!ObjectUtils.isEmpty(remainingCount) && remainingCount>0) {
					
					updateResult = updateCounter(classz, OffersConfigurationConstants.WEEKLY);
					LOG.info("Weekly count updated");
					remainingCount = ProcessValues.getRemainingCount(totalCount, updateResult);
					LOG.info("Total counters remaining after updating weekly count : {}", remainingCount);
					
					if(!ObjectUtils.isEmpty(remainingCount) && remainingCount>0) {
						
						updateResult = updateCounter(classz, OffersConfigurationConstants.DAILY);
						LOG.info("Daily count updated");
						remainingCount = ProcessValues.getRemainingCount(totalCount, updateResult);
						LOG.info("Total counters remaining after updating weekly count : {}", remainingCount);
					}
				}
				
			}
			
		}
		
	}
	
	/***
	 * 
	 * @param unit
	 * @return
	 */
	public Integer getTotalCountForCounter(String unit) {
		
		Integer totalCount = null;
		switch(unit) {
		
			case OffersDBConstants.OFFER_COUNTERS :  totalCount = (int) offerCounterRepository.count();
		        								 	 break;
			case OffersDBConstants.MEMBER_OFFER_COUNTERS :  totalCount = (int) memberOfferCounterRepository.count();
		 	 										 break;
			case OffersDBConstants.ACCOUNT_OFFER_COUNTERS :  totalCount = (int) accountOfferCounterRepository.count();
													 break;
			default : totalCount = 0;										 
		}
		
		return totalCount;
	}
	
	/***
	 * 
	 * @param updateResult
	 */
	private UpdateResult updateCounter(Class<?> classz, String unit) {
		
		Date date = ProcessValues.getDateForCounterUpdateCheck(unit); 
		LOG.info("Comparison date for {} : {}", unit, date);
		Query query = new Query();
		query.addCriteria(
			Criteria.where(OffersDBConstants.LAST_PURCHASED_DATE).lt(date)
		);
		
		Update update = new Update();
		
		if(ProcessValues.updateField(unit, OffersConfigurationConstants.ANNUAL)) {
			
			update.set(OffersDBConstants.ANNUAL_COUNT, 0);
			update.set(OffersDBConstants.DENOMINATION_ANNUAL_COUNT, 0);
		}
		
		if(ProcessValues.updateField(unit, OffersConfigurationConstants.MONTHLY)) {
			update.set(OffersDBConstants.MONTHLY_COUNT, 0);
			update.set(OffersDBConstants.DENOMINATION_MONTHLY_COUNT, 0);
		}
		
		if(ProcessValues.updateField(unit, OffersConfigurationConstants.WEEKLY)) {
			update.set(OffersDBConstants.WEEKLY_COUNT, 0);
			update.set(OffersDBConstants.DENOMINATION_WEEKLY_COUNT, 0);
		}
		
		if(ProcessValues.updateField(unit, OffersConfigurationConstants.DAILY)) {
			update.set(OffersDBConstants.DAILY_COUNT, 0);
			update.set(OffersDBConstants.DENOMINATION_DAILY_COUNT, 0);
		}
		
		update.set(OffersDBConstants.LAST_PURCHASED_DATE, new Date());
		update.set(OffersDBConstants.DENOMINATION_LAST_PURCHASED_DATE, new Date());
		return mongoOperations.updateMulti(query, update, classz);
		
	}
	
	/***
	 * 
	 * @param classz
	 * @return
	 */
	public UpdateResult pushDenominations(Class<?> classz) {
		Query denominationQuery = new Query();
		denominationQuery.addCriteria(
			Criteria.where(OffersDBConstants.DENOMINATION_COUNT).exists(false)
		);
		
		Update denominationUpdate = new Update();
		denominationUpdate.set(OffersDBConstants.DENOMINATION_COUNT, new ArrayList<>());
		return mongoOperations.updateMulti(denominationQuery, denominationUpdate, classz);
		
	}
	
	/**
	 * 
	 * @param transactionRequest
	 * @param program
	 * @return
	 * @throws ParseException 
	 */
	public List<RefundTransaction> getRefundTransactions(RefundTransactionRequestDto transactionRequest, String program) throws ParseException {
		
		Query transactionsQuery = new Query();
		
		transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.PROGRAM_CODE)
				.regex(ProcessValues.getRegexStartEndExpression(program),
						OfferConstants.CASE_INSENSITIVE.get()));
		
		setFilterCriteria(transactionsQuery, transactionRequest);
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		
		List<RefundTransaction> transactionList = mongoOperations.find(transactionsQuery,
				RefundTransaction.class, MarketplaceDBConstants.REFUND_TRANSACTIONS);
		
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		return transactionList;
	}

	/***
	 * 
	 * @param transactionsQuery
	 * @param transactionRequest
	 * @throws ParseException 
	 */
	private void setFilterCriteria(Query transactionsQuery, RefundTransactionRequestDto transactionRequest) throws ParseException {
		
		if(!ObjectUtils.isEmpty(transactionRequest)) {
			
			if(!StringUtils.isEmpty(transactionRequest.getFromDate())
			&& StringUtils.isEmpty(transactionRequest.getToDate())) {
				
				Date fromDate = Utilities.changeStringToDateWithTimeFormat(transactionRequest.getFromDate(),
						OfferConstants.FROM_DATE_TIME.get());
				
				transactionsQuery.addCriteria(
						Criteria.where(OffersDBConstants.CREATED_DATE)
						.gte(fromDate));
				
			}
			
			if(!StringUtils.isEmpty(transactionRequest.getToDate())
			&& StringUtils.isEmpty(transactionRequest.getFromDate())) {
				
				Date toDate = Utilities.changeStringToDateWithTimeFormat(transactionRequest.getToDate(),
						OfferConstants.FROM_DATE_TIME.get());
				
				transactionsQuery.addCriteria(
						Criteria.where(OffersDBConstants.CREATED_DATE)
						.lte(toDate));
				
			}
			
			if(!StringUtils.isEmpty(transactionRequest.getFromDate())
			&& !StringUtils.isEmpty(transactionRequest.getToDate())) {
						
				Date fromDate = Utilities.changeStringToDateWithTimeFormat(transactionRequest.getFromDate(),
						OfferConstants.FROM_DATE_TIME.get());
				Date toDate = Utilities.changeStringToDateWithTimeFormat(transactionRequest.getToDate(),
						OfferConstants.FROM_DATE_TIME.get());
				
				transactionsQuery.addCriteria(new Criteria().andOperator(
					    Criteria.where(OffersDBConstants.CREATED_DATE)
							.gte(fromDate),
					    Criteria.where(OffersDBConstants.CREATED_DATE)
							.lte(toDate)));
						
			}
			
			if(!StringUtils.isEmpty(transactionRequest.getStatus())) {
				
				transactionsQuery.addCriteria(Criteria.where(OffersDBConstants.REFUND_STATUS)
						.regex(ProcessValues.getRegexStartEndExpression(transactionRequest.getStatus()),
								OfferConstants.CASE_INSENSITIVE.get()));
				
			}
		}
		
	}

	/***
	 * 
	 * @param transactionId
	 * @return
	 */
	public String getCustomerTypeFromTranscationDetails(String transactionId) {
		
		String log = Logs.logBeforeHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
		
		PurchaseHistory purchaseHistory = !StringUtils.isEmpty(transactionId)
				? purchaseRepository.findByMongoId(transactionId)
				: null;
		
		log = Logs.logAfterHittingDB(OffersDBConstants.PURCHASE_HISTORY);
		LOG.info(log);
	
		return !ObjectUtils.isEmpty(purchaseHistory)
			? purchaseHistory.getCustomerType()
			: null;
	}

	/**
	 * 
	 * @param paymentMethodsIds
	 * @return list of paymentMetods fetched in the paymentMetod collection
	 */
	public List<PaymentMethod> getPaymentMethodsList(List<String> paymentMethodsIds) {

		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.PAYMENT_METHOD);
		LOG.info(log);
		List<PaymentMethod> result = !CollectionUtils.isEmpty(paymentMethodsIds)
				? paymentMethodRepository.findAllPaymentMethodsInSpecifiedListOfIds(paymentMethodsIds)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.PAYMENT_METHOD);
		LOG.info(log);
		return result;

	}
	
	/***
	 * 
	 * @return list of all customer types from MM
	 */
	public List<CustomerTypeEntity> getAllCustomerTypes() {

		MongoClient mongoClient = null; 
		
		try {
			mongoClient = getDBConnection();
			
		} catch (Exception e) {
			LOG.info("Error in connecting to MM DB");
			e.printStackTrace();
		}
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, memberManagementDatabase);
		
		Query customerTypeQuery = new Query();
		customerTypeQuery.addCriteria(Criteria.where(OffersDBConstants.CUSTOMER_TYPE).exists(true));
		
		customerTypeQuery.fields()
	    	.include(OffersDBConstants.CUSTOMER_TYPE)
	    	.include(OffersDBConstants.ELIGIBILITY_MATRIX);
		
		return mongoTemplate.find(customerTypeQuery,
				CustomerTypeEntity.class, OffersDBConstants.CUSTOMER_TYPE);
	}
}
