package com.loyalty.marketplace.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.constants.MarketplaceDBConstants;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferTypeRepository;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.database.repository.DenominationRepository;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.utils.Logs;

@Component
public class MarketplaceRepositoryHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(MarketplaceRepositoryHelper.class);

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	DenominationRepository denominationRepository;

	@Autowired
	OfferTypeRepository offerTypeRepository;
	
	@Autowired
	PaymentMethodRepository paymentMethodRepository;

	/**
	 * 
	 * @param program 
	 * @return list of all categories
	 */
	public List<Category> findAllCategories(String program) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		List<Category> result = categoryRepository.findByProgramCodeIgnoreCase(program);
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		return result;
	}

	//Loyalty as a service.
	/**
	 * 
	 * @param categoryIdList
	 * @param program
	 * @return category that matches any of the category id and program
	 */
	public List<Category> findCategoryByIdListAndProgramCode(List<String> categoryIdList, String program) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		List<Category> categoryList = !CollectionUtils.isEmpty(categoryIdList)
			 ? categoryRepository.findByCategoryIdInAndProgramCodeIgnoreCase(categoryIdList, program)
			 : null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);	 
		return categoryList;	 
			 
	}
	
	/**
	 * 
	 * @param parentCategoryList
	 * @param program
	 * @return list of all categories with any of the input parent category and program code
	 */
	public List<Category> getAllCategoriesWithParentCategoryListAndProgramCode(List<Category> parentCategoryList, String program) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		List<Category> categoryList = !CollectionUtils.isEmpty(parentCategoryList)
				? categoryRepository.findByParentCategoryInAndProgramCodeIgnoreCase(parentCategoryList, program)
				: null;
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		return categoryList;
	}
	
	public List<Category> getAllCategories(String program) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		List<Category> categoryList = categoryRepository.findByParentCategoryNullAndProgramCodeIgnoreCase(program);
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.CATEGORY);
		LOG.info(log);
		return categoryList;
	}
	
	/**
	 * 
	 * @return list of all offer types
	 */
	public List<OfferType> findAllOfferTypes() {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		List<OfferType> result = offerTypeRepository.findAll();
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		return result;
		
	}
	
	//Loyalty as a service.
	public List<OfferType> findAllOfferTypesForProgramCode(String programCode) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		List<OfferType> result = offerTypeRepository.findByProgramCodeIgnoreCase(programCode);
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.OFFER_TYPE);
		LOG.info(log);
		return result;
		
	}

	/**
	 * 
	 * @param program 
	 * @return list of all payment methods
	 */
	public List<PaymentMethod> findAllPaymentMethods(String program) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.PAYMENT_METHOD);
		LOG.info(log);
		List<PaymentMethod> result = paymentMethodRepository.findByProgramCodeIgnoreCase(program);
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.PAYMENT_METHOD);
		LOG.info(log);
		return result;
	}

	public List<Denomination> getAllDenominationSorted() {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		List<Denomination> result = denominationRepository
					.findAll(Sort.by(Sort.Direction.ASC, MarketplaceConfigurationConstants.DENOMINATION_DIRHAM_VALUE));
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		return result;
	}
	
	//Loyalty as a service.
	public List<Denomination> getAllDenominationSortedForProgramCode(String program) {
		
		String log = Logs.logBeforeHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		List<Denomination> result = denominationRepository.findByProgramCodeIgnoreCase(program, Sort.by(Sort.Direction.ASC, MarketplaceConfigurationConstants.DENOMINATION_DIRHAM_VALUE));
		log = Logs.logAfterHittingDB(MarketplaceDBConstants.DENOMINATION);
		LOG.info(log);
		return result;
	}
	
}

