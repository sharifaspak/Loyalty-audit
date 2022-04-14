package com.loyalty.marketplace.inbound.restcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketPlaceConstants;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.inbound.dto.CategoryDto;
import com.loyalty.marketplace.inbound.dto.CategoryRequestDto;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.merchants.domain.model.MerchantContactPersonDomain;
import com.loyalty.marketplace.merchants.domain.model.MerchantDomain;
import com.loyalty.marketplace.merchants.inbound.dto.MarketPlaceContactPersonDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.merchants.outbound.service.OfferService;
import com.loyalty.marketplace.merchants.outbound.service.dto.Headers;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.dto.CategoryResultResponse;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.UserManagementService;
import com.loyalty.marketplace.stores.constants.StoreCodes;
import com.loyalty.marketplace.stores.domain.model.StoreContactPersonDomain;
import com.loyalty.marketplace.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MarketplaceValidator;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Marketplace")
@RequestMapping("/marketplace")
public class MarketplaceController {

	private static final Logger LOG = LoggerFactory.getLogger(MarketplaceController.class);
	@Autowired
	MerchantDomain merchantDomain;

	@Autowired
	StoreDomain storeDomain;

	@Autowired
	Validator validator;
	
	@Autowired
	UserManagementService userManagementService;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	OfferService offerService;
	
	@Value("${programManagement.defaultProgramCode}")
    private String defaultProgramCode;

	@PostMapping(value = RequestMappingConstants.CONFIGURE_CONATACT_PERSON, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureContactPerson(@RequestBody MarketPlaceContactPersonDto addContactPersonDto,
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

		LOG.info("Request Parameters: {}", addContactPersonDto);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;

		if (StringUtils.isEmpty(addContactPersonDto.getMerchantCode())) {
			resultResponse.addErrorAPIResponse(MerchantCodes.EITHER_STORECODE_MERCHANTCODE_MANDATORY.getIntId(),
					MerchantCodes.EITHER_STORECODE_MERCHANTCODE_MANDATORY.getMsg());
			resultResponse.setResult(MerchantCodes.CONTACT_CREATION_FAILED.getId(),
					MerchantCodes.CONTACT_CREATION_FAILED.getMsg());
			return resultResponse;
		}

		if (StringUtils.isEmpty(addContactPersonDto.getStoreCode())) {

			if (!MarketplaceValidator.validateDto(addContactPersonDto, validator, resultResponse) || !MarketplaceValidator
					.validateContactPersons(addContactPersonDto.getContactPersons(), resultResponse)) {
				resultResponse.setResult(MerchantCodes.CONTACT_CREATION_FAILED.getId(),
						MerchantCodes.CONTACT_CREATION_FAILED.getMsg());
				return resultResponse;
			}

			try {

				List<ContactPersonDto> invalidContactPersons = new ArrayList<>();
				Merchant merchant = merchantRepository.findByMerchantCode(addContactPersonDto.getMerchantCode());

				if(!configureContactPersonForMerchant(addContactPersonDto, userName, token, resultResponse,
						invalidContactPersons, merchant, program,RequestMappingConstants.CONFIGURE_CONATACT_PERSON)) {
					return resultResponse;
				}
				
				offerService.updateEligibleOffers(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
				
			} catch (Exception e) {
				resultResponse.setResult(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getId(),
						MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
				LOG.error(new MarketplaceException(this.getClass().toString(), "configureContactPerson",
						e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
			}
		} else {

			if (!MarketplaceValidator.validateDto(addContactPersonDto, validator, resultResponse) || !MarketplaceValidator
					.validateContactPersons(addContactPersonDto.getContactPersons(), resultResponse)) {
				resultResponse.setResult(StoreCodes.CONTACT_PERSON_CREATION_FAILED.getId(),
						StoreCodes.CONTACT_PERSON_CREATION_FAILED.getMsg());
				return resultResponse;
			}
			
			Store store = storeRepository.findByStoreCodeAndMerchantCode(addContactPersonDto.getStoreCode(),
					addContactPersonDto.getMerchantCode());

			if(!configureContactPersonForStore(addContactPersonDto, userName, token, resultResponse, store, program)) {
				return resultResponse;
			}
			
			offerService.updateEligibleOffers(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
			
		}

		LOG.info("Response Parameters: {}", resultResponse);

		return resultResponse;

	}

	private boolean configureContactPersonForStore(MarketPlaceContactPersonDto addContactPersonDto, String userName,
			String token, ResultResponse resultResponse, Store store, String program) {
		if (null == store) {
			resultResponse.addErrorAPIResponse(StoreCodes.STORE_NOT_AVAILABLE.getIntId(), StoreCodes.STORE_NOT_AVAILABLE.getMsg());
			resultResponse.setResult(StoreCodes.CONTACT_PERSON_CREATION_FAILED.getId(),
					StoreCodes.CONTACT_PERSON_CREATION_FAILED.getMsg());
			return false;

		}
		List<StoreContactPersonDomain> contactPersonDomainToSave = storeDomain.createContactPersonDomain(
				addContactPersonDto.getContactPersons(), addContactPersonDto.getStoreCode(),
				addContactPersonDto.getMerchantCode(),addContactPersonDto.isOptInOrOut(), resultResponse);
		if (null == contactPersonDomainToSave || contactPersonDomainToSave.isEmpty()) {
			resultResponse.setResult(StoreCodes.CONTACT_PERSON_CREATION_FAILED.getId(),
					StoreCodes.CONTACT_PERSON_CREATION_FAILED.getMsg());
			return false;
		}
		storeDomain.configureContactPerson(store, contactPersonDomainToSave, null != userName ? userName : token, program,RequestMappingConstants.CONFIGURE_CONATACT_PERSON,resultResponse.getApiStatus().getExternalTransactionId());
		resultResponse.setResult(StoreCodes.CONTACT_PERSON_CREATED.getId(),
				StoreCodes.CONTACT_PERSON_CREATED.getMsg());
		userManagementService.saveStore(contactPersonDomainToSave,addContactPersonDto.isOptInOrOut(),resultResponse,token, program);
		resultResponse.setSuccessAPIResponse();
		return true;
	}

	private boolean configureContactPersonForMerchant(MarketPlaceContactPersonDto addContactPersonDto, String userName,
			String token, ResultResponse resultResponse, List<ContactPersonDto> invalidContactPersons,
			Merchant merchant, String program, String configureConatactPerson) {
		if (null == merchant) {
			resultResponse.addErrorAPIResponse(MerchantCodes.MERCHANT_NOT_AVAIABLE.getIntId(), MerchantCodes.MERCHANT_NOT_AVAIABLE.getMsg());
			resultResponse.setResult(MerchantCodes.CONTACT_CREATION_FAILED.getId(),
					MerchantCodes.CONTACT_CREATION_FAILED.getMsg());
			return false;

		}
		
		List<MerchantContactPersonDomain> contactPersonDomainToSave = merchantDomain.createContactPersonDomain(
				addContactPersonDto.getContactPersons(), invalidContactPersons,addContactPersonDto.isOptInOrOut(),
				addContactPersonDto.getMerchantCode());

		if (null == contactPersonDomainToSave || contactPersonDomainToSave.isEmpty()) {
			createInvalidContactPersonResponseMerchant(resultResponse, invalidContactPersons);
			resultResponse.setResult(MerchantCodes.CONTACT_CREATION_FAILED.getId(),
					MerchantCodes.CONTACT_CREATION_FAILED.getMsg());
			return false;
		}
		merchantDomain.configureContactPerson(merchant, contactPersonDomainToSave,
				null != userName ? userName : token, program,configureConatactPerson,resultResponse.getApiStatus().getExternalTransactionId());
		resultResponse.setResult(MerchantCodes.CONTACT_PERSON_CREATED.getId(),
				MerchantCodes.CONTACT_PERSON_CREATED.getMsg());
		userManagementService.saveMerchant(contactPersonDomainToSave,addContactPersonDto.isOptInOrOut(),resultResponse,token, program);

		createInvalidContactPersonResponseMerchant(resultResponse, invalidContactPersons);
		resultResponse.setSuccessAPIResponse();
		return true;
	}

	private void createInvalidContactPersonResponseMerchant(ResultResponse resultResponse,
			List<ContactPersonDto> invalidContactPersons) {
		if (!invalidContactPersons.isEmpty()) {
			for (final ContactPersonDto contactPersonDto : invalidContactPersons) {
				resultResponse.addErrorAPIResponse(MerchantCodes.CONTACT_PERSON_EXISTS.getIntId(),
						contactPersonDto.getEmailId() + ":" + MerchantCodes.CONTACT_PERSON_EXISTS.getMsg());
			}
			resultResponse.setResult(MerchantCodes.CONTACT_PERSON_CREATED.getId(),
					MerchantCodes.CONTACT_PERSON_CREATED.getMsg());
		}
	}
	
	/**
	 * Inserts a new category or list of categories. This is an internal API not to
	 * be published.
	 * 
	 * @param categoryRequest List of categories. Should include the category
	 *                        description.
	 * @return ResultResponse Final status after operation
	 */
	@PostMapping(value = "/category", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureCategories(@RequestBody CategoryRequestDto categoryRequestDto,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName) {

		LOG.info(MarketPlaceConstants.LOG_CONSTANTS.get(), MarketPlaceConstants.REQUEST_PARAMS.get(), categoryRequestDto);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		if (!MarketplaceValidator.validateDto(categoryRequestDto, validator, resultResponse)) {
            resultResponse.setResult(MerchantCodes.CATEGORY_ADDITION_FAILED.getId(),
            		MerchantCodes.CATEGORY_ADDITION_FAILED.getMsg());
            return resultResponse;
        }
        List<CategoryDto> categoryDto = categoryRequestDto.getCategories();
        for(CategoryDto catDto : categoryDto) {
            if (!MarketplaceValidator.validateDto(catDto, validator, resultResponse)) {
                resultResponse.setResult(MerchantCodes.CATEGORY_ADDITION_FAILED.getId(),
                		MerchantCodes.CATEGORY_ADDITION_FAILED.getMsg());
                return resultResponse;
            }
        }   

		List<Category> categoriesToSave = new ArrayList<>();
		List<Category> existingCategories = categoryRepository.findAll();
		List<String> categoryDescriptionEn = new ArrayList<>();
		List<Errors> errorList = new ArrayList<>();

		for (Category category : existingCategories) {
			categoryDescriptionEn.add(category.getCategoryName().getCategoryNameEn());
		}

		if (null != categoryRequestDto.getCategories()
				&& !categoryRequestDto.getCategories().isEmpty()) {

			for (CategoryDto category : categoryRequestDto.getCategories()) {

				if (!categoryDescriptionEn.contains(category.getCategoryNameEn())) {
					Category categoryToSave = new Category();
					categoryToSave.setCategoryId(category.getCategoryId()); 
					CategoryName name = new CategoryName();
					name.setCategoryNameEn(category.getCategoryNameEn());
					name.setCategoryNameAr(category.getCategoryNameAr());
					categoryToSave.setCategoryName(name);
					categoryToSave.setDtCreated(new Date());
					categoryToSave.setUsrCreated(userName);
					categoriesToSave.add(categoryToSave);

				} else {

					Errors error = new Errors();
					error.setCode(MerchantCodes.DUPLICATE_CATEGORY.getIntId());
					error.setMessage("Duplicate category : " + category.getCategoryNameEn());
					errorList.add(error);
				}
			}

			if (errorList.isEmpty()) {
				try {

					categoryRepository.saveAll(categoriesToSave);
					resultResponse.setResult(MerchantCodes.CATEGORY_ADDED_SUSSESSFULY.getId(),
							MerchantCodes.CATEGORY_ADDED_SUSSESSFULY.getMsg());

				} catch (Exception e) {
					resultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
							MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
					resultResponse.setResult(MerchantCodes.CATEGORY_ADDITION_FAILED.getId(),
							MerchantCodes.CATEGORY_ADDITION_FAILED.getMsg());
					LOG.error(new MarketplaceException(this.getClass().toString(), "configureCategories",
							e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
				}
			} else {

				resultResponse.setResult(MerchantCodes.CATEGORY_WITH_DUPLICATES.getId(),
						MerchantCodes.CATEGORY_WITH_DUPLICATES.getMsg());
				resultResponse.setBulkErrorAPIResponse(errorList);
			}

		}

		LOG.info(MarketPlaceConstants.LOG_CONSTANTS.get(), MarketPlaceConstants.RESPONSE_PARAMS.get(), resultResponse);
		return resultResponse;
	}

	/**
	 * Gets the list of all categories from static table.This will be used for offer creation.
	 * This is an internal API not to be published.
	 */
	
	@GetMapping(value = RequestMappingConstants.LIST_CATEGORY, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
			
			List<Category> categories = categoryRepository.findAll();
			List<CategoryDto> categoryList = new ArrayList<>();

			if (!categories.isEmpty()) {

				for (final Category category : categories) {
					if (category.getParentCategory()==null) {
					CategoryDto categoryDto = modelMapper.map(category,CategoryDto.class);
					categoryList.add(categoryDto);
					}
				}
				categoryResultResponse.setResult(MerchantCodes.CATEGORY_LISTED_SUCCESSFULLY.getId(),
						MerchantCodes.CATEGORY_LISTED_SUCCESSFULLY.getMsg());
			} else {
				
				categoryResultResponse.addErrorAPIResponse(MerchantCodes.NO_CATEGORY_TO_DISPLAY.getIntId(),
						MerchantCodes.NO_CATEGORY_TO_DISPLAY.getMsg());
				categoryResultResponse.setResult(MerchantCodes.CATEGORY_LISTING_FAILED.getId(),
						MerchantCodes.CATEGORY_LISTING_FAILED.getMsg());
				LOG.error("listCategory : {}", MerchantCodes.NO_CATEGORY_TO_DISPLAY.getMsg());
			}
			categoryResultResponse.setCategories(categoryList);
			
		} catch (Exception e) {
			categoryResultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			categoryResultResponse.setResult(MerchantCodes.CATEGORY_LISTING_FAILED.getId(),
					MerchantCodes.CATEGORY_LISTING_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "listCategory",
					e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("listCategory response parameters : {}", categoryResultResponse);
		return categoryResultResponse;
	}

	
	@PostMapping(value = RequestMappingConstants.RESET_CATEGORY, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void resetCategory() {
		categoryRepository.deleteAll();
		
	}
}
