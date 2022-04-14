package com.loyalty.marketplace.stores.inbound.restcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.merchants.outbound.service.OfferService;
import com.loyalty.marketplace.merchants.outbound.service.dto.Headers;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.Result;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.PartnerService;
import com.loyalty.marketplace.outbound.service.UserManagementService;
import com.loyalty.marketplace.stores.constants.StoreCodes;
import com.loyalty.marketplace.stores.constants.StoreConstants;
import com.loyalty.marketplace.stores.constants.StoreRequestMappingConstants;
import com.loyalty.marketplace.stores.domain.model.StoreAddressDomain;
import com.loyalty.marketplace.stores.domain.model.StoreContactPersonDomain;
import com.loyalty.marketplace.stores.domain.model.StoreDescriptionDomain;
import com.loyalty.marketplace.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.stores.inbound.dto.StoreDto;
import com.loyalty.marketplace.stores.inbound.restcontroller.helper.StoreControllerHelper;
import com.loyalty.marketplace.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.stores.outbound.dto.ListSpecificStoreResponse;
import com.loyalty.marketplace.stores.outbound.dto.ListStoresResponse;
import com.loyalty.marketplace.stores.outbound.dto.StoreResult;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.StoreValidator;
import com.loyalty.marketplace.utils.Utils;

import io.swagger.annotations.Api;

@Api(value = "Marketplace")
@RestController
@RequestMapping("/marketplace")
@RefreshScope
public class StoreController {

	private static final Logger LOG = LoggerFactory.getLogger(StoreController.class);

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StoreRepository storeRepository;

	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	UserManagementService userManagementService;

	@Autowired
	Validator validator;

	@Autowired
	StoreDomain storeDomain;
	
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	StoreControllerHelper storeControllerHelper;
	
	@Autowired
	OfferService offerService;
	
	@Value("${programManagement.defaultProgramCode}")
    private String defaultProgramCode;

	@PostMapping(value = StoreRequestMappingConstants.CONFIGURE_STORE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureStore(@RequestBody StoreDto storeDto,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId) {

		LOG.info("configureStore request parameters: {}", storeDto);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		if (!StoreValidator.validateDto(storeDto, validator, resultResponse)||
				!StoreValidator.validateRequest(storeDto, validator, resultResponse)) {			
			resultResponse.setResult(StoreCodes.STORE_CREATION_FAILED.getId(),
						StoreCodes.STORE_CREATION_FAILED.getMsg());
			return resultResponse;
		}

			try {
				
				Merchant merchant = merchantRepository.findByMerchantCode(storeDto.getMerchantCode());

				if (null == merchant) {
					resultResponse.setResult(StoreCodes.STORE_CREATION_FAILED.getId(),
							StoreCodes.STORE_CREATION_FAILED.getMsg());
					resultResponse.addErrorAPIResponse(StoreCodes.MERCHANT_NOT_AVAILABLE.getIntId(),
							StoreCodes.MERCHANT_NOT_AVAILABLE.getMsg());
					return resultResponse;
				}

				Store storeDetails = storeRepository.findByStoreCodeAndMerchantCode(storeDto.getStoreCode(),
						storeDto.getMerchantCode());

				if (null == storeDetails) {

					StoreDescriptionDomain storeDescriptionDomainToSave = new StoreDescriptionDomain.StoreDescriptionBuilder(
							storeDto.getDescription(), storeDto.getDescriptionAr()).build();

					StoreAddressDomain storeAddressDomainToSave = new StoreAddressDomain.StoreAddressBuilder(
							storeDto.getAddress(), storeDto.getAddressAr()).build();

					List<StoreContactPersonDomain> contactPersonDomainToSave = storeDomain.createContactPersonDomain(
							storeDto.getContactPersons(), storeDto.getStoreCode(), storeDto.getMerchantCode(),
							storeDto.isOptInOrOut(), resultResponse);

					StoreDomain storeDomainToSave = new StoreDomain.StoreBuilder(storeDto.getStoreCode(),
							storeDescriptionDomainToSave, storeDto.getMerchantCode(), contactPersonDomainToSave,
							StoreConstants.STORE_DEFAULT_STATUS.get()).latitude(storeDto.getLatitude())
									.longitude(storeDto.getLongitude()).address(storeAddressDomainToSave)
									.programCode(program).usrCreated(null != userName ? userName : token)
									.dtCreated(new Date()).build();

					storeDomain.saveStore(storeDomainToSave,externalTransactionId);

					resultResponse.setResult(StoreCodes.STORE_CREATED.getId(), StoreCodes.STORE_CREATED.getMsg());
					userManagementService.saveStore(contactPersonDomainToSave,storeDto.isOptInOrOut(),resultResponse,token, program);
					resultResponse.setSuccessAPIResponse();
				
				} else {
					
					resultResponse.addErrorAPIResponse(StoreCodes.STORE_EXISTS.getIntId(),
							StoreCodes.STORE_EXISTS.getMsg());
					resultResponse.setResult(StoreCodes.STORE_CREATION_FAILED.getId(),
							StoreCodes.STORE_CREATION_FAILED.getMsg());

				}

			} catch (MarketplaceException e) {
				resultResponse.setResult(e.getErrorCode(), e.getErrorMsg());
				resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg());
				LOG.error(e.printMessage());
			}

			catch (Exception e) {
				resultResponse.addErrorAPIResponse(StoreCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
						StoreCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
				resultResponse.setResult(StoreCodes.STORE_CREATION_FAILED.getId(),
						StoreCodes.STORE_CREATION_FAILED.getMsg());

				LOG.error(e.getMessage());
			}
			
		
		LOG.info("configureStore response parameters: {}", resultResponse);

		return resultResponse;

	}

	@PostMapping(value = StoreRequestMappingConstants.UPDATE_STORE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse updateStore(@RequestBody StoreDto storeDto,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = StoreRequestMappingConstants.MERCHANT_CODE, required = true) String merchantCode,
			@PathVariable(value = StoreRequestMappingConstants.STORE_CODE, required = true) String storeCode) {

		LOG.info("updateStore request parameters: {}", storeDto);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		if (!StoreValidator.validateDto(storeDto, validator, resultResponse)||
				!StoreValidator.validateRequest(storeDto, validator, resultResponse)) {
			resultResponse.setResult(StoreCodes.STORE_UPDATION_FAILED.getId(),
					StoreCodes.STORE_UPDATION_FAILED.getMsg());
			return resultResponse;
		}

			List<Store> storeDetail = storeRepository.findByStoreCodeAndMerchantCodeList(storeCode, merchantCode);

			if (null == storeDetail) {
				resultResponse.addErrorAPIResponse((StoreCodes.STORE_UNAVAILABLE_TO_UPDATE.getIntId()),
						StoreCodes.STORE_UNAVAILABLE_TO_UPDATE.getMsg());
				resultResponse.setResult(StoreCodes.STORE_UPDATION_FAILED.getId(),
						StoreCodes.STORE_UPDATION_FAILED.getMsg());
				return resultResponse;
			}

			if(storeDetail.size() > 1) {

				resultResponse.addErrorAPIResponse((StoreCodes.DUPLICATE_RECORDS_RETRIEVED_STORES.getIntId()),
						StoreCodes.DUPLICATE_RECORDS_RETRIEVED_STORES.getMsg());
				resultResponse.setResult(StoreCodes.STORE_UPDATION_FAILED.getId(),
						StoreCodes.STORE_UPDATION_FAILED.getMsg());
				return resultResponse;

			}

				try {

					Store storeDetails = storeDetail.get(0);
					Merchant merchant = merchantRepository.findByMerchantCode(storeDto.getMerchantCode());

					if (null == merchant) {
						resultResponse.setResult(StoreCodes.STORE_UPDATION_FAILED.getId(),
								StoreCodes.STORE_UPDATION_FAILED.getMsg());
						resultResponse.addErrorAPIResponse(StoreCodes.MERCHANT_NOT_AVAILABLE_UPDATE.getIntId(),
								StoreCodes.MERCHANT_NOT_AVAILABLE_UPDATE.getMsg());
						return resultResponse;
					}
					
					Store store = null;
					if(!merchantCode.equalsIgnoreCase(storeDto.getMerchantCode())) {
						store = storeRepository.findByStoreCodeAndMerchantCode(storeCode,
								storeDto.getMerchantCode());
					}
					
					if(store != null) {
						resultResponse.addErrorAPIResponse(StoreCodes.STORE_EXISTS.getIntId(),
								StoreCodes.STORE_EXISTS.getMsg());
						resultResponse.setResult(StoreCodes.STORE_UPDATION_FAILED.getId(),
								StoreCodes.STORE_UPDATION_FAILED.getMsg());
						return resultResponse;
					}

						StoreDescriptionDomain storeDescriptionDomainToSave = new StoreDescriptionDomain.StoreDescriptionBuilder(
								storeDto.getDescription(), storeDto.getDescriptionAr()).build();

						StoreAddressDomain storeAddressDomainToSave = new StoreAddressDomain.StoreAddressBuilder(
								storeDto.getAddress(), storeDto.getAddressAr()).build();

						List<StoreContactPersonDomain> contactPersonDomainToUpdate = storeControllerHelper.prepareContactPersonDomainList(storeDto, storeCode, resultResponse, storeDetails);

						StoreDomain storeDomainToUpdate = new StoreDomain.StoreBuilder(storeCode,
								storeDescriptionDomainToSave, storeDto.getMerchantCode(), contactPersonDomainToUpdate,
								StoreConstants.STORE_DEFAULT_STATUS.get()).latitude(storeDto.getLatitude())
										.longitude(storeDto.getLongitude()).address(storeAddressDomainToSave)
										.id(storeDetails.getId()).usrCreated(storeDetails.getUsrCreated())
										.dtCreated(storeDetails.getDtCreated()).usrUpdated(null != userName ? userName : token)
										.dtUpdated(new Date())
										.programCode(null != storeDetails.getProgramCode() ? storeDetails.getProgramCode() : program)
										.build();
						
						LOG.info(" storeDomainToUpdate :: {} ", storeDomainToUpdate);
						
						storeControllerHelper.validateUsernameExists(storeDto, resultResponse, storeDetails);
						storeDomain.updateStore(storeDomainToUpdate,storeDetails,externalTransactionId);

						resultResponse.setResult(StoreCodes.STORE_UPDATED_SUCCESSFULLY.getId(),
								StoreCodes.STORE_UPDATED_SUCCESSFULLY.getMsg());
						resultResponse.setSuccessAPIResponse();

			offerService.updateEligibleOffers(new Headers(program, authorization, externalTransactionId, userName,
					sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));

				} catch (MarketplaceException e) {
					resultResponse.setResult(e.getErrorCode(), e.getErrorMsg());
					resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getMessage());
					LOG.error(e.printMessage());
				}

				catch (Exception e) {
					resultResponse.addErrorAPIResponse(StoreCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
							StoreCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
					resultResponse.setResult(StoreCodes.STORE_UPDATION_FAILED.getId(),
							StoreCodes.STORE_UPDATION_FAILED.getMsg());

					LOG.error(e.getMessage());
				}
		LOG.info("updateStore response parameters: {}", resultResponse);

		return resultResponse;

	}

	@GetMapping(value = StoreRequestMappingConstants.LIST_ALL_STORES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListStoresResponse listAllStores(
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
			@RequestParam(value = StoreRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = StoreRequestMappingConstants.LIMIT, required = false) Integer limit) {

		LOG.info("Entering listAllStores in StoreController");

		ListStoresResponse storeResultResponse = new ListStoresResponse(externalTransactionId);
		Result result = new Result();

		try {
			
			if(null == program) program = defaultProgramCode;

			List<Store> stores = null;
			Page<Store> retrievedStores = null;
			long totalElements = 0;
			
			Integer paginationValidation = Utils.validatePagination(page, limit);
			
			if(1 == paginationValidation) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.PAGINATION_LIMIT_EMPTY.getIntId(),
						StoreCodes.PAGINATION_LIMIT_EMPTY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}
			
			if(2 == paginationValidation) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.PAGINATION_PAGE_EMPTY.getIntId(),
						StoreCodes.PAGINATION_PAGE_EMPTY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}
			
			if(3 == paginationValidation) {
				//stores = storeRepository.findAll();
				stores = storeRepository.findByProgramCodeIgnoreCase(program);
			}
			
			if(4 == paginationValidation) {
				Pageable pageNumberWithElements = PageRequest.of(page, limit);	
				//retrievedStores = storeRepository.findAll(pageNumberWithElements);
				retrievedStores = storeRepository.findByProgramCodeIgnoreCasePagination(program, pageNumberWithElements);
				stores = retrievedStores.getContent();
				totalElements = retrievedStores.getTotalElements();
			}		

			if (null == stores || stores.isEmpty()) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.NO_STORES_TO_DISPLAY.getIntId(),
						StoreCodes.NO_STORES_TO_DISPLAY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}
			
			List<StoreResult> storeList = new ArrayList<>();

			List<Merchant> merchantsList = storeControllerHelper.getMerchants(stores, program);
			
			for (Store store : stores) {
				StoreResult storeResult = modelMapper.map(store, StoreResult.class);
				Merchant merchant = merchantsList.stream()
						.filter(m -> null != store && null != store.getMerchantCode() && store.getMerchantCode().equals(m.getMerchantCode())).findAny()
						.orElse(null);
				if (null != merchant && null != merchant.getMerchantName()) {
					storeResult.setMerchantName(merchant.getMerchantName().getMerchantNameEn());
				}
				
				storeList.add(storeResult);
			}

			storeResultResponse.setStores(storeList);
			result.setResponse(StoreCodes.STORE_LISTED_SUCCESSFULLY.getId());
			result.setDescription(StoreCodes.STORE_LISTED_SUCCESSFULLY.getMsg());
			storeResultResponse.setResult(result);
			storeResultResponse.setTotalRecords(totalElements);

		} catch (Exception e) {
			storeResultResponse.addErrorAPIResponse(StoreCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					StoreCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
					StoreCodes.LISTING_STORES_FAILED.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info("Exiting listAllStores in StoreController");

		return storeResultResponse;

	}

	@GetMapping(value = StoreRequestMappingConstants.LIST_MERCHANT_STORES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListStoresResponse listMerchantStores(
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
			@PathVariable(value = StoreRequestMappingConstants.MERCHANT_CODE, required = true) String merchantCode,
			@RequestParam(value = StoreRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = StoreRequestMappingConstants.LIMIT, required = false) Integer limit) {

		LOG.info("Entering listMerchantStores in StoreController. request parameters: {}", merchantCode);

		ListStoresResponse storeResultResponse = new ListStoresResponse(externalTransactionId);
		Result result = new Result();

		try {

			if(null == program) program = defaultProgramCode;
			
			List<Store> stores = null;
			Page<Store> retrievedStores = null;
			long totalElements = 0;
			Integer paginationValidation = Utils.validatePagination(page, limit);
			
			if(1 == paginationValidation) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.PAGINATION_LIMIT_EMPTY.getIntId(),
						StoreCodes.PAGINATION_LIMIT_EMPTY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}
			
			if(2 == paginationValidation) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.PAGINATION_PAGE_EMPTY.getIntId(),
						StoreCodes.PAGINATION_PAGE_EMPTY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}	
			
			//Merchant merchantExists = merchantRepository.findByMerchantCode(merchantCode);
			Merchant merchantExists = merchantRepository.findByMerchantCodeAndProgramCodeIgnoreCase(merchantCode, program);
			if (null == merchantExists) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.MERCHANT_UNAVAILABLE.getIntId(),
						StoreCodes.MERCHANT_UNAVAILABLE.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}
			
			if (3 == paginationValidation) {
				//stores = storeRepository.findByMerchantCode(merchantCode);
				stores = storeRepository.findByMerchantCodeAndProgramCodeIgnoreCase(merchantCode, program);
			}

			if (4 == paginationValidation) {
				Pageable pageNumberWithElements = PageRequest.of(page, limit);
				//retrievedStores = storeRepository.findByMerchantCodePagination(merchantCode, pageNumberWithElements);
				retrievedStores = storeRepository.findByMerchantCodeAndProgramCodeIgnoreCasePagination(merchantCode, program, pageNumberWithElements);
				stores = retrievedStores.getContent();
				totalElements = retrievedStores.getTotalElements();
			}

			if (null == stores || stores.isEmpty()) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.NO_STORES_FOR_MERCHANT_TO_DISPLAY.getIntId(),
						StoreCodes.NO_STORES_FOR_MERCHANT_TO_DISPLAY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}

			List<StoreResult> storeList = new ArrayList<>();
			List<Merchant> merchantsList = storeControllerHelper.getMerchants(stores, program);
			
			for (Store store : stores) {
				StoreResult storeResult = modelMapper.map(store, StoreResult.class);
				Merchant merchant = merchantsList.stream()
						.filter(m -> null != store && null != store.getMerchantCode() && store.getMerchantCode().equals(m.getMerchantCode())).findAny()
						.orElse(null);
				if (null != merchant && null != merchant.getMerchantName()) {
					storeResult.setMerchantName(merchant.getMerchantName().getMerchantNameEn());
				}
				storeList.add(storeResult);
			}

			storeResultResponse.setStores(storeList);
			result.setResponse(StoreCodes.STORES_FOR_MERCHANT_LISTED_SUCCESSFULLY.getId());
			result.setDescription(StoreCodes.STORES_FOR_MERCHANT_LISTED_SUCCESSFULLY.getMsg());
			storeResultResponse.setResult(result);
			storeResultResponse.setTotalRecords(totalElements);
			
		} catch (Exception e) {

			storeResultResponse.addErrorAPIResponse(StoreCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					StoreCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
					StoreCodes.LISTING_STORES_FAILED.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info("Exiting listMerchantStores in StoreController.");

		return storeResultResponse;

	}

	@GetMapping(value = StoreRequestMappingConstants.LIST_PARTNER_STORES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListStoresResponse listPartnerStores(
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
			@PathVariable(value = StoreRequestMappingConstants.PARTNER_CODE, required = true) String partnerCode,
			@RequestParam(value = StoreRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = StoreRequestMappingConstants.LIMIT, required = false) Integer limit) {

		LOG.info("Entering listPartnerStores in StoreController. request parameters: {}", partnerCode);

		ListStoresResponse storeResultResponse = new ListStoresResponse(externalTransactionId);
		Result result = new Result();

		try {

			if(null == program) program = defaultProgramCode;
			
			List<Errors> errorList = new ArrayList<>();
			Integer paginationValidation = Utils.validatePagination(page, limit);
			
			if(1 == paginationValidation) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.PAGINATION_LIMIT_EMPTY.getIntId(),
						StoreCodes.PAGINATION_LIMIT_EMPTY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}
			
			if(2 == paginationValidation) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.PAGINATION_PAGE_EMPTY.getIntId(),
						StoreCodes.PAGINATION_PAGE_EMPTY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}
			
			boolean partnerExists = partnerService.getPartnerDetails(partnerCode, token, externalTransactionId);
			
			if (!partnerExists) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.PARTNER_UNAVAILABLE.getIntId(),
						StoreCodes.PARTNER_UNAVAILABLE.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}

			//List<Merchant> merchants = merchantRepository.findByPartnerCode(partnerCode);
			List<Merchant> merchants = merchantRepository.findByPartnerCodeAndProgramCodeIgnoreCase(partnerCode, program);

			if (null == merchants || merchants.isEmpty()) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.NO_STORES_FOR_PARTNER_TO_DISPLAY.getIntId(),
						StoreCodes.NO_STORES_FOR_PARTNER_TO_DISPLAY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}

			List<Store> allStores = new ArrayList<>();
			findAllStoresForMerchant(storeResultResponse, errorList, merchants, allStores, paginationValidation, page, limit, program);

			if (allStores.isEmpty() || null == allStores) {
				storeResultResponse.addErrorAPIResponse(StoreCodes.NO_STORES_FOR_PARTNER_TO_DISPLAY.getIntId(),
						StoreCodes.NO_STORES_FOR_PARTNER_TO_DISPLAY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
				return storeResultResponse;
			}

			List<StoreResult> storeList = new ArrayList<>();
			List<Merchant> merchantsList = storeControllerHelper.getMerchants(allStores, program);
			
			for (Store store : allStores) {

				StoreResult storeResult = modelMapper.map(store, StoreResult.class);
				Merchant merchant = merchantsList.stream()
						.filter(m -> null != store && null != store.getMerchantCode() && store.getMerchantCode().equals(m.getMerchantCode())).findAny()
						.orElse(null);
				if (null != merchant && null != merchant.getMerchantName()) {
					storeResult.setMerchantName(merchant.getMerchantName().getMerchantNameEn());
				}
				
				storeList.add(storeResult);

			}

			storeResultResponse.setStores(storeList);
			result.setResponse(StoreCodes.STORE_LISTED_FOR_PARTNER_SUCCESSFULLY.getId());
			result.setDescription(StoreCodes.STORE_LISTED_FOR_PARTNER_SUCCESSFULLY.getMsg());
			storeResultResponse.setResult(result);
			
		} catch (Exception e) {

			storeResultResponse.addErrorAPIResponse(StoreCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					StoreCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
					StoreCodes.LISTING_STORES_FAILED.getMsg());
			LOG.error(e.getMessage());

		}

		LOG.info("Exiting listPartnerStores in StoreController.");

		return storeResultResponse;

	}

	private void findAllStoresForMerchant(ListStoresResponse storeResultResponse, List<Errors> errorList,
			List<Merchant> merchants, List<Store> allStores, Integer paginationValidation, Integer page, Integer limit, String program) {
		
		List<String> merchantcodes = merchants.stream().map(Merchant::getMerchantCode).collect(Collectors.toList());
		
		List<Store> stores = new ArrayList<>();
		Page<Store> retrievedStores = null;
		long totalElements = 0;
		
		if(3 == paginationValidation) {
			//stores = storeRepository.findByMerchantCodes(merchantcodes);
			stores = storeRepository.findByMerchantCodesAndProgramCodeIgnoreCase(merchantcodes, program);
		}
		
		if(4 == paginationValidation) {
			Pageable pageNumberWithElements = PageRequest.of(page, limit);	
			//retrievedStores = storeRepository.findByMerchantCodesPagination(merchantcodes, pageNumberWithElements);
			retrievedStores = storeRepository.findByMerchantCodesPaginationAndProgramCodeIgnoreCase(merchantcodes, program, pageNumberWithElements);
			stores = retrievedStores.getContent();
			totalElements = retrievedStores.getTotalElements();
		}		
		
		allStores.addAll(stores);
		storeResultResponse.setSuccessAPIResponse();
		storeResultResponse.setTotalRecords(totalElements);
		
	}
	
//	private void findAllStoresForMerchant(ListStoresResponse storeResultResponse, List<Errors> errorList,
//			List<Merchant> merchants, List<Store> allStores) {
//		for (Merchant merchant : merchants) {
//
//			List<Store> stores = storeRepository.findByMerchantCode(merchant.getMerchantCode());
//
//			if (null != stores && !stores.isEmpty()) {
//				for (Store store : stores) {
//					allStores.add(store);
//				}
//			} else {
//				Errors error = new Errors();
//				error.setCode(StoreCodes.NO_STORES_FOR_MERCHANT_TO_DISPLAY.getIntId());
//				error.setMessage(
//						merchant.getMerchantCode() + ": " + StoreCodes.NO_STORES_FOR_MERCHANT_TO_DISPLAY.getMsg());
//				errorList.add(error);
//			}
//
//			storeResultResponse.setBulkErrorAPIResponse(errorList);
//			storeResultResponse.setSuccessAPIResponse();
//		}
//	}

	@GetMapping(value = StoreRequestMappingConstants.VIEW_SPECIFIC_STORE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListSpecificStoreResponse viewSpecificStore(
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
			@RequestHeader(value = StoreRequestMappingConstants.MERCHANT_CODE, required = true) String merchantCode,
			@PathVariable(value = StoreRequestMappingConstants.STORE_CODE, required = true) String storeCode) {

		LOG.info("Entering viewSpecificStore in StoreController. request parameters: {}", storeCode);

		ListSpecificStoreResponse storeResultResponse = new ListSpecificStoreResponse(externalTransactionId);
		Result result = new Result();

		try {

			if(null == program) program = defaultProgramCode;
			
			//Store store = storeRepository.findByStoreCodeAndMerchantCode(storeCode, merchantCode);
			Store store = storeRepository.findByStoreCodeAndMerchantCodeAndProgramIgnoreCase(storeCode, merchantCode, program);
		
			if (null != store) {

				List<StoreResult> storeList = new ArrayList<>();

				StoreResult storeResult = modelMapper.map(store, StoreResult.class);
				Merchant merchant = merchantRepository.findByMerchantCode(store.getMerchantCode());
				if (null != merchant && null != merchant.getMerchantName()) {
					storeResult.setMerchantName(merchant.getMerchantName().getMerchantNameEn());
				}
				storeList.add(storeResult);
				storeResultResponse.setStores(storeList);
				result.setResponse(StoreCodes.SPECIFIC_STORE_LISTED_SUCCESSFULLY.getId());
				result.setDescription(StoreCodes.SPECIFIC_STORE_LISTED_SUCCESSFULLY.getMsg());
				storeResultResponse.setResult(result);

			} else {
				storeResultResponse.addErrorAPIResponse(StoreCodes.NO_SPECIFIC_STORE_TO_DISPLAY.getIntId(),
						StoreCodes.NO_SPECIFIC_STORE_TO_DISPLAY.getMsg());
				storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
						StoreCodes.LISTING_STORES_FAILED.getMsg());
			}

		} catch (Exception e) {

			storeResultResponse.addErrorAPIResponse(StoreCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					StoreCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			storeResultResponse.setResult(StoreCodes.LISTING_STORES_FAILED.getId(),
					StoreCodes.LISTING_STORES_FAILED.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info("Exiting viewSpecificStore in StoreController.");

		return storeResultResponse;

	}
	
	@GetMapping(value = "store/contactPersons", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ContactPerson> getContactPersons() {
		List<Store> stores = storeRepository.findAll();
		List<ContactPerson> contactpersons = new  ArrayList<>();
		for(final Store store :stores ) {
			for(ContactPerson contactPerson : store.getContactPersons() ) {
				contactpersons.add(contactPerson);
			}
		}
		return contactpersons;
	}
	
	@PostMapping(value = "/keycloak/createStoreContact", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse createContactPerson(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestBody List<ContactPerson> contactPersons) {
		ResultResponse resultResponse = new ResultResponse("1234");
		List<StoreContactPersonDomain> contactPersonDomainToSave = new ArrayList<>();
		for(final ContactPerson contactPerson :contactPersons ) {
			StoreContactPersonDomain storeContactPersonDomain = new StoreContactPersonDomain.ContactPersonBuilder(contactPerson.getEmailId(), contactPerson.getMobileNumber(), contactPerson.getFirstName(), contactPerson.getLastName()).password("Welcome@1111").userName(contactPerson.getUserName()).build();
			contactPersonDomainToSave.add(storeContactPersonDomain);
			}
		//userManagementService.saveStore(contactPersonDomainToSave ,storeDto.isOptInorOut(),resultResponse,token);
		userManagementService.saveStore(contactPersonDomainToSave ,false,resultResponse,token, program);
	return resultResponse;
	}

}
