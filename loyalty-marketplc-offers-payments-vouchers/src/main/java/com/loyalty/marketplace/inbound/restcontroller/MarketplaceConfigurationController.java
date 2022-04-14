package com.loyalty.marketplace.inbound.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.helper.MarketplaceRepositoryHelper;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.dto.ExceptionInfo;
import com.loyalty.marketplace.offers.inbound.dto.CategoryDto;
import com.loyalty.marketplace.offers.inbound.dto.DenominationDto;
import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.offers.inbound.dto.SubCategoryDto;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.dto.CategoryResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.DenominationResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferTypeResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferTypesDto;
import com.loyalty.marketplace.offers.outbound.dto.PaymentMethodResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.SubCategoryResultResponse;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;

import io.swagger.annotations.Api;

@RestController
@Api(value = RequestMappingConstants.MARKETPLACE)
@RequestMapping(RequestMappingConstants.MARKETPLACE_BASE)
public class MarketplaceConfigurationController {

	private static final Logger LOG = LoggerFactory.getLogger(MarketplaceConfigurationController.class);

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	MarketplaceRepositoryHelper repositoryHelper;
	
	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	/**
	 * Internal API to return list of all payment methods
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of all payment methods
	 */
	@GetMapping(value = RequestMappingConstants.LIST_ALL_PAYMENT_METHODS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PaymentMethodResultResponse listAllPaymentMethods(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		PaymentMethodResultResponse paymentMethodResultResponse = new PaymentMethodResultResponse(externalTransactionId);

		try {
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			 
			List<PaymentMethod> paymentMethods = repositoryHelper.findAllPaymentMethods(program);
			List<PaymentMethodDto> paymentMethodList = null;

			if (!CollectionUtils.isEmpty(paymentMethods)) {
				
				paymentMethodList = new ArrayList<>(paymentMethods.size());
				for (PaymentMethod paymentMethod : paymentMethods) {
					PaymentMethodDto paymentMethodDto = modelMapper.map(paymentMethod,PaymentMethodDto.class);
					paymentMethodList.add(paymentMethodDto);
				}
				paymentMethodResultResponse.setResult(OfferSuccessCodes.PAYMENT_METHOD_LISTED_SUCCESSFULLY.getId(),
						OfferSuccessCodes.PAYMENT_METHOD_LISTED_SUCCESSFULLY.getMsg());
			} else {
				paymentMethodResultResponse.addErrorAPIResponse(OfferErrorCodes.NO_PAYMENT_METHOD_TO_DISPLAY.getIntId(),
						OfferErrorCodes.NO_PAYMENT_METHOD_TO_DISPLAY.getMsg());
				paymentMethodResultResponse.setResult(OfferErrorCodes.PAYMENT_METHOD_LISTING_FAILED.getId(),
						OfferErrorCodes.PAYMENT_METHOD_LISTING_FAILED.getMsg());
			}
			
			paymentMethodResultResponse.setPaymentMethods(paymentMethodList);

		} catch (Exception e) {
			paymentMethodResultResponse.addErrorAPIResponse(OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			paymentMethodResultResponse.setResult(OfferErrorCodes.PAYMENT_METHOD_LISTING_FAILED.getId(),
					OfferErrorCodes.PAYMENT_METHOD_LISTING_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "listAllPaymentMethods",
					e.getClass() + e.getMessage(), OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return paymentMethodResultResponse;
	}
	
	/**
	 * API to return list of all categories
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of all categories
	 */
	@GetMapping(value = RequestMappingConstants.LIST_ALL_CATEGORIES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryResultResponse listCategory(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		CategoryResultResponse categoryResultResponse = new CategoryResultResponse(externalTransactionId);

		try {
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			List<Category> categories = repositoryHelper.findAllCategories(program);
			List<CategoryDto> categoryList = new ArrayList<>();

			if (!categories.isEmpty()) {

				for (final Category category : categories) {
					if (category.getParentCategory()==null) {
					CategoryDto categoryDto = modelMapper.map(category,CategoryDto.class);
					categoryList.add(categoryDto);
					}
				}
				categoryResultResponse.setResult(OfferSuccessCodes.CATEGORY_LISTED_SUCCESSFULLY.getId(),
						OfferSuccessCodes.CATEGORY_LISTED_SUCCESSFULLY.getMsg());
			} else {
				
				categoryResultResponse.addErrorAPIResponse(OfferErrorCodes.NO_CATEGORY_TO_DISPLAY.getIntId(),
						OfferErrorCodes.NO_CATEGORY_TO_DISPLAY.getMsg());
				categoryResultResponse.setResult(OfferErrorCodes.CATEGORY_LISTING_FAILED.getId(),
						OfferErrorCodes.CATEGORY_LISTING_FAILED.getMsg());
				LOG.error("listCategory : {}", OfferErrorCodes.NO_CATEGORY_TO_DISPLAY.getMsg());
			}
			categoryResultResponse.setCategories(categoryList);
			
		} catch (Exception e) {
			categoryResultResponse.addErrorAPIResponse(OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			categoryResultResponse.setResult(OfferErrorCodes.CATEGORY_LISTING_FAILED.getId(),
					OfferErrorCodes.CATEGORY_LISTING_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "listCategory",
					e.getClass() + e.getMessage(), OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return categoryResultResponse;
	}
	
	/**
	 * API to get the list of all denominations from the DB
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of all available denominations
	 */
	@GetMapping(value = RequestMappingConstants.LIST_ALL_DENOMINATIONS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public DenominationResultResponse listDenominations(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		DenominationResultResponse denominationResultResponse = new DenominationResultResponse(externalTransactionId);

		try {
			
			//Loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			//List<Denomination> denominations = repositoryHelper.getAllDenominationSorted();
			List<Denomination> denominations = repositoryHelper.getAllDenominationSortedForProgramCode(program);
			List<DenominationDto> denominationList = new ArrayList<>(denominations.size());

			if (!CollectionUtils.isEmpty(denominations)) {

				for (final Denomination denomination : denominations) {
					DenominationDto denominationDto = modelMapper.map(denomination,DenominationDto.class);
					denominationList.add(denominationDto);
				}
				denominationResultResponse.setResult(OfferSuccessCodes.DENOMINATION_LISTED_SUCCESSFULLY.getId(),
						OfferSuccessCodes.DENOMINATION_LISTED_SUCCESSFULLY.getMsg());
			} else {
				
				denominationResultResponse.addErrorAPIResponse(OfferErrorCodes.NO_DENOMINATION_TO_DISPLAY.getIntId(),
						OfferErrorCodes.NO_DENOMINATION_TO_DISPLAY.getMsg());
				denominationResultResponse.setResult(OfferErrorCodes.DENOMINATION_LISTING_FAILED.getId(),
						OfferErrorCodes.DENOMINATION_LISTING_FAILED.getMsg());
				LOG.error("listDenominations : {}", OfferErrorCodes.NO_DENOMINATION_TO_DISPLAY.getMsg());
			}
			denominationResultResponse.setDenominations(denominationList);
			
		} catch (Exception e) {
			denominationResultResponse.addErrorAPIResponse(OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			denominationResultResponse.setResult(OfferErrorCodes.DENOMINATION_LISTING_FAILED.getId(),
					OfferErrorCodes.DENOMINATION_LISTING_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "listDenominations",
					e.getClass() + e.getMessage(), OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return denominationResultResponse;
	}
	
	/**
	 * Gets the list of all offer types from static table.This will be used for offer creation.
	 * This is an internal API not to be published.
	 */
	@GetMapping(value = RequestMappingConstants.LIST_ALL_OFFER_TYPES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OfferTypeResultResponse listOfferType(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_OFFER_TYPE_METHOD.get());
		LOG.info(log);
		
		OfferTypeResultResponse offerTypeResultResponse = new OfferTypeResultResponse(externalTransactionId);

		try {
			
			//Loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			//List<OfferType> offerTypes = repositoryHelper.findAllOfferTypes();
			
			List<OfferType> offerTypes = repositoryHelper.findAllOfferTypesForProgramCode(program);
			List<OfferTypesDto> offerTypeList = new ArrayList<>();

			if (!offerTypes.isEmpty()) {

				for (final OfferType offerType : offerTypes) {
					OfferTypesDto offerTypesDto = modelMapper.map(offerType,OfferTypesDto.class);
					offerTypeList.add(offerTypesDto);
				}
				offerTypeResultResponse.setResult(OfferSuccessCodes.OFFER_TYPE_LISTED_SUCCESSFULLY.getId(),
						OfferSuccessCodes.OFFER_TYPE_LISTED_SUCCESSFULLY.getMsg());
			} else {
				
				offerTypeResultResponse.addErrorAPIResponse(OfferErrorCodes.NO_OFFER_TYPE_TO_DISPLAY.getIntId(),
						OfferErrorCodes.NO_OFFER_TYPE_TO_DISPLAY.getMsg());
				offerTypeResultResponse.setResult(OfferErrorCodes.OFFER_TYPE_LISTING_FAILED.getId(),
						OfferErrorCodes.OFFER_TYPE_LISTING_FAILED.getMsg());
				LOG.error(OfferConstants.LIST_OFFER_TYPE_METHOD.get(), OfferConstants.IN.get(),
						this .getClass(), OfferConstants.MESSAGE_SEPARATOR.get(),
						OfferErrorCodes.NO_OFFER_TYPE_TO_DISPLAY.getMsg());
			}
			offerTypeResultResponse.setOfferTypes(offerTypeList);
			
		} catch (Exception e) {
			
			offerTypeResultResponse.addErrorAPIResponse(OfferExceptionCodes.OFFER_TYPE_FETCH_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.OFFER_TYPE_FETCH_RUNTIME_EXCEPTION.getMsg());
			offerTypeResultResponse.setResult(OfferErrorCodes.OFFER_TYPE_LISTING_FAILED.getId(),
					OfferErrorCodes.OFFER_TYPE_LISTING_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.LIST_OFFER_TYPE_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.OFFER_TYPE_FETCH_RUNTIME_EXCEPTION).printMessage());
		}
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_OFFER_TYPE_METHOD.get());
		LOG.info(log);
		
		return offerTypeResultResponse;
	}
	
	/**
	 * 
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param categoryId
	 * @return
	 */
	@GetMapping(value = RequestMappingConstants.LIST_ALL_SUBCATEGORIES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SubCategoryResultResponse listSubCategory(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = OffersRequestMappingConstants.CATEGORY_ID, required = false) String categoryId,
			@RequestHeader(value = OffersRequestMappingConstants.CATEGORY_ID_LIST, required = false) List<String> categoryIdList) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_SUBCATEGORY_METHOD.get());
		LOG.info(log);
		
		SubCategoryResultResponse subCategoryResultResponse = new SubCategoryResultResponse(externalTransactionId);
		List<SubCategoryDto> subCategoryList = null;
		
		try {	
			
			List<String> categoryIdListRequest = null; 
					
			if(!StringUtils.isEmpty(categoryId)
			|| !CollectionUtils.isEmpty(categoryIdList)) {
				
				categoryIdListRequest = new ArrayList<String>(1);
				
				if(!StringUtils.isEmpty(categoryId)) {
					categoryIdListRequest.add(categoryId);
	            }

				if(!CollectionUtils.isEmpty(categoryIdList)) {
					categoryIdListRequest.addAll(categoryIdList);
	            }

			}
			
			if (null == program) program = defaultProgramCode;
			
			List<Category> subCategoryRecords = null;
			
			if(!CollectionUtils.isEmpty(categoryIdListRequest)) {
				
				List<Category> categoryList = repositoryHelper.findCategoryByIdListAndProgramCode(categoryIdListRequest, program);
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(categoryList), OfferErrorCodes.CATEGORY_NOT_EXISTING, subCategoryResultResponse)) {
					
					subCategoryRecords = repositoryHelper.getAllCategoriesWithParentCategoryListAndProgramCode(categoryList, program);
				}
				
			} else {
				
				subCategoryRecords = repositoryHelper.getAllCategories(program);
				
			}
			
			if (!CollectionUtils.isEmpty(subCategoryRecords)) {

				subCategoryList = new ArrayList<>(subCategoryRecords.size());
				
				for (Category category : subCategoryRecords) {
					
					SubCategoryDto subCategoryDto = new SubCategoryDto();
					subCategoryDto.setSubCategoryId(category.getCategoryId());
					subCategoryDto.setSubCategoryNameEn(!ObjectUtils.isEmpty(category.getCategoryName())
							? category.getCategoryName().getCategoryNameEn() : null);
					subCategoryDto.setSubCategoryNameAr(!ObjectUtils.isEmpty(category.getCategoryName())
							? category.getCategoryName().getCategoryNameAr() : null);
					subCategoryDto.setParentCategory(!ObjectUtils.isEmpty(category.getParentCategory())
							? category.getParentCategory().getCategoryId() : null);
					subCategoryList.add(subCategoryDto);
				}
				
			} 
			
			if(Checks.checkNoErrors(subCategoryResultResponse)) {
				
				Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(subCategoryList), 
						OfferErrorCodes.NO_SUBCATEGORY_TO_DISPLAY, subCategoryResultResponse);
				
			}
		
			
			
		} catch (Exception e) {
			
			Responses.setResponseAfterException(subCategoryResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.LIST_SUBCATEGORY_METHOD.get(), e, null, OfferErrorCodes.SUBCATEGORY_LISTING_FAILED,
					OfferExceptionCodes.SUBCATEGORY_FETCH_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, externalTransactionId, null, userName)));
			
		}
		
		subCategoryResultResponse.setSubCategories(subCategoryList);
		Responses.setResponse(subCategoryResultResponse, OfferErrorCodes.SUBCATEGORY_LISTING_FAILED, OfferSuccessCodes.SUBCATEGORY_LISTED_SUCCESSFULLY);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_SUBCATEGORY_METHOD.get());
		LOG.info(log);
		
		return subCategoryResultResponse;
	}
	
	

}	
	
