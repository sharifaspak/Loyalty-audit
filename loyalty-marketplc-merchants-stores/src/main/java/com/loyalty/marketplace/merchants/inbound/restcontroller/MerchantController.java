package com.loyalty.marketplace.merchants.inbound.restcontroller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.Validator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.inbound.dto.BarcodeDto;
import com.loyalty.marketplace.inbound.dto.BarcodeRequestDto;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.constants.DBConstants;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.merchants.constants.MerchantConstants;
import com.loyalty.marketplace.merchants.constants.MerchantRequestMappingConstants;
import com.loyalty.marketplace.merchants.domain.model.BarcodeDomain;
import com.loyalty.marketplace.merchants.domain.model.CategoryDomain;
import com.loyalty.marketplace.merchants.domain.model.MerchantBillingRateDomain;
import com.loyalty.marketplace.merchants.domain.model.MerchantContactPersonDomain;
import com.loyalty.marketplace.merchants.domain.model.MerchantDescriptionDomain;
import com.loyalty.marketplace.merchants.domain.model.MerchantDomain;
import com.loyalty.marketplace.merchants.domain.model.MerchantNameDomain;
import com.loyalty.marketplace.merchants.domain.model.TAndCDomain;
import com.loyalty.marketplace.merchants.domain.model.WhatYouGetDomain;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantImageRequestDto;
import com.loyalty.marketplace.merchants.inbound.dto.RateTypeDto;
import com.loyalty.marketplace.merchants.inbound.dto.RateTypeRequestDto;
import com.loyalty.marketplace.merchants.inbound.restcontroller.helper.MerchantControllerHelper;
import com.loyalty.marketplace.merchants.mapping.utility.ObjectMapperUtils;
import com.loyalty.marketplace.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.merchants.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantBillingRate;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantImage;
import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;
import com.loyalty.marketplace.merchants.outbound.database.repository.BarcodeRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantImageRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.RateTypeRepository;
import com.loyalty.marketplace.merchants.outbound.dto.GetMerchantNameResponse;
import com.loyalty.marketplace.merchants.outbound.dto.GetMerchantNameResult;
import com.loyalty.marketplace.merchants.outbound.dto.ImageInfo;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantImagesResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsDropdownResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsStoreOfferResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsWithTotal;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantDropdownResult;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantImagesResult;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantOffers;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantResult;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantStoreOfferResult;
import com.loyalty.marketplace.merchants.outbound.service.OfferService;
import com.loyalty.marketplace.merchants.outbound.service.dto.Headers;
import com.loyalty.marketplace.outbound.database.entity.image.MarketplaceImage;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.Result;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.PartnerService;
import com.loyalty.marketplace.outbound.service.UserManagementService;
import com.loyalty.marketplace.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.stores.outbound.dto.StoreResult;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MerchantValidator;
import com.loyalty.marketplace.utils.Utils;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Marketplace")
@RequestMapping("/marketplace")
@RefreshScope
public class MerchantController {

	private static final Logger LOG = LoggerFactory.getLogger(MerchantController.class);

	@Value("${centos.server.address}")
	private String serverAddress;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	MerchantDomain merchantDomain;

	@Autowired
	StoreDomain storeDomain;

	@Autowired
	Validator validator;

	@Autowired
	PartnerService partnerService;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	MerchantImageRepository merchantImageRepository;

	@Autowired
	StoreRepository storeRepository;

	@Autowired
	RateTypeRepository rateTypeRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	BarcodeRepository barcodeRepository;

	@Autowired
	MerchantControllerHelper merchantControllerHelper;

	@Autowired
	UserManagementService userManagementService;

	@Autowired
	OfferService offerService;

	@Autowired
	ProgramManagement programManagement;

	@Autowired
	AuditService auditService;
	
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	MongoOperations mongoOperations;
	
	@Value("${programManagement.defaultProgramCode}")
    private String defaultProgramCode;
	
	@PostMapping(value = MerchantRequestMappingConstants.CREATE_MERCHANT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureMerchant(@RequestBody MerchantDto merchantDto,
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

		LOG.info("configureMerchant request parameters: {}", merchantDto);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		if (!MerchantValidator.validateDto(merchantDto, validator, resultResponse)
				|| !MerchantValidator.validateRequest(merchantDto, validator, resultResponse)) {
			resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
					MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
			return resultResponse;
		}

		String merchantCodeRemovedSpecialChar = Utils.removeSpecialChars(merchantDto.getMerchantCode());
		try {
			program = programManagement.getProgramCode(program);
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			if (merchantCodeRemovedSpecialChar.isEmpty()) {

				resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_MERCHANT_CODE.getIntId(),
						MerchantCodes.INVALID_MERCHANT_CODE.getMsg());
				resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
						MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
				return resultResponse;

			}

			boolean exists = partnerService.getPartnerDetails(merchantDto.getPartnerCode(),token,externalTransactionId);

			if (!exists) {
				resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_PARTERCODE.getIntId(),
						MerchantCodes.INVALID_PARTERCODE.getMsg());
				resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
						MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
				return resultResponse;
			}

			if (!merchantDomain.validateCategoryBarcode(merchantDto.getCategory(), merchantDto.getBarcodeType(),
					resultResponse)) {
				resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
						MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
				return resultResponse;
			}

			Merchant merchantDetails = merchantRepository.findByMerchantCode(merchantDto.getMerchantCode());

			if (null != merchantDetails) {
				resultResponse.addErrorAPIResponse(MerchantCodes.MERCHANT_EXISTS.getIntId(),
						MerchantCodes.MERCHANT_EXISTS.getMsg());
				resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
						MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
				return resultResponse;
			}

			if (null != merchantDto.getDiscountBillingRates()
					&& !merchantDomain.validateBillingRateType(merchantDto.getDiscountBillingRates(), resultResponse)) {
				resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
						MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
				return resultResponse;
			}

			List<MerchantBillingRateDto> invalidBillingRateDtoList = new ArrayList<>();
			List<MerchantBillingRateDomain> billingRateDomainToSave = null;

			MerchantNameDomain merchantNameDoaminToSave = new MerchantNameDomain.MerchantNameBuilder(
					merchantDto.getMerchantNameEn(), merchantDto.getMerchantNameAr()).build();

			WhatYouGetDomain whatYouGetDoaminToSave = new WhatYouGetDomain.WhatYouGetBuilder(
					merchantDto.getWhatYouGetEn(), merchantDto.getWhatYouGetAr()).build();

			TAndCDomain tnCDoaminToSave = new TAndCDomain.TAndCBuilder(merchantDto.getTnCEn(), merchantDto.getTnCAr())
					.build();

			MerchantDescriptionDomain merchantDescriptionDoaminToSave = new MerchantDescriptionDomain.MerchantDescriptionBuilder()
					.merchantDescEn(merchantDto.getMerchantDescEn()).merchantDescAr(merchantDto.getMerchantDescAr())
					.build();

			List<ContactPersonDto> invalidContactPersons = new ArrayList<>();
			List<MerchantContactPersonDomain> contactPersonDomainToSave = merchantDomain.createContactPersonDomain(
					merchantDto.getContactPersons(), invalidContactPersons, merchantDto.isOptInOrOut(),
					merchantDto.getMerchantCode());
			if (null != merchantDto.getDiscountBillingRates()) {
				billingRateDomainToSave = merchantDomain.createBillingRateDomain(merchantDto.getDiscountBillingRates(),
						invalidBillingRateDtoList);
				if(billingRateDomainToSave.isEmpty()) billingRateDomainToSave = null;
			}

			BarcodeDomain barcode = new BarcodeDomain.BarcodeBuilder(merchantDto.getBarcodeType())
					.id(merchantDto.getBarcodeType()).build();
			CategoryDomain categoryDomain = new CategoryDomain.CategoryBuilder(merchantDto.getCategory()).build();

			MerchantDomain merchantDomainToSave = new MerchantDomain.MerchantBuilder(merchantDto.getMerchantCode(),
					merchantNameDoaminToSave, merchantDto.getPartnerCode(), categoryDomain, barcode,
					contactPersonDomainToSave, billingRateDomainToSave, MerchantConstants.MERCHANT_DEFAULT_STATUS.get(),0)
							.whatYouGet(whatYouGetDoaminToSave).tnC(tnCDoaminToSave).programCode(program)
							.merchantDescription(merchantDescriptionDoaminToSave)
							.externalName(merchantDto.getExternalName()).usrCreated(null != userName ? userName : token)
							.dtCreated(new Date()).build();

			merchantDomain.saveMerchant(merchantDomainToSave,externalTransactionId);
			resultResponse.setResult(MerchantCodes.MERCHANT_CREATED.getId(), MerchantCodes.MERCHANT_CREATED.getMsg());
			userManagementService.saveMerchant(contactPersonDomainToSave, merchantDto.isOptInOrOut(), resultResponse,
					token, program);
			/*
			 * for (MerchantContactPersonDomain singleMerchantToSave :
			 * contactPersonDomainToSave) {
			 * userManagementService.updateMerchantPassword(singleMerchantToSave,
			 * merchantDto.isOptInOrOut(), resultResponse, token); }
			 */
			
			for (MerchantBillingRateDto merchantBillingRateDto : invalidBillingRateDtoList) {
				resultResponse.addErrorAPIResponse(MerchantCodes.DUPLICATE_DISCOUNT_BILLINGRATE.getIntId(),
						"Duplicate Discount billing Rate of RateType : " + merchantBillingRateDto.getRateType()
								+ " and Rate : " + merchantBillingRateDto.getRate());
			}

			createInvalidContactPersonResponse(resultResponse, invalidContactPersons);
			resultResponse.setSuccessAPIResponse();

		} catch (MarketplaceException e) {
			resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg());
			resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
					MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "configureMerchant",
					e.getClass() + e.getMessage(), MerchantCodes.MERCHANT_CREATION_FAILED).printMessage());
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(),
					MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "configureMerchant",
					e.getClass() + e.getMessage(), MerchantCodes.MERCHANT_CREATION_FAILED).printMessage());
		}

		LOG.info("configureMerchant response parameters: {}", resultResponse);

		return resultResponse;

	}

	@PostMapping(value = MerchantRequestMappingConstants.UPDATE_MERCHANT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse updateMerchant(@RequestBody MerchantDto merchantDto,
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
			@PathVariable(value = MerchantRequestMappingConstants.MERCHANT_CODE, required = true) String merchantCode) {

		LOG.info("updateMerchant request parameters: {}", merchantDto);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		if (!MerchantValidator.validateDto(merchantDto, validator, resultResponse)
		|| !MerchantValidator.validateRequest(merchantDto, validator, resultResponse)) {
			resultResponse.setResult(MerchantCodes.MERCHANT_UPDATION_FAILED.getId(),
					MerchantCodes.MERCHANT_UPDATION_FAILED.getMsg());
			return resultResponse;
		}
		try {
			program = programManagement.getProgramCode(program);
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			boolean exist = partnerService.getPartnerDetails(merchantDto.getPartnerCode(),token,externalTransactionId);

			if (!exist) {
				resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_PARTERCODE.getIntId(),
						MerchantCodes.INVALID_PARTERCODE.getMsg());
				resultResponse.setResult(MerchantCodes.MERCHANT_UPDATION_FAILED.getId(),
						MerchantCodes.MERCHANT_UPDATION_FAILED.getMsg());
				return resultResponse;
			}

			if (!merchantDomain.validateCategoryBarcode(merchantDto.getCategory(), merchantDto.getBarcodeType(),
					resultResponse)) {
				resultResponse.setResult(MerchantCodes.MERCHANT_UPDATION_FAILED.getId(),
						MerchantCodes.MERCHANT_UPDATION_FAILED.getMsg());
				return resultResponse;
			}
			if (null != merchantDto.getDiscountBillingRates() && !merchantDomain.validateBillingRateType(merchantDto.getDiscountBillingRates(), resultResponse)) {
				resultResponse.setResult(MerchantCodes.MERCHANT_UPDATION_FAILED.getId(),
						MerchantCodes.MERCHANT_UPDATION_FAILED.getMsg());
				return resultResponse;
			}
			List<MerchantBillingRateDto> invalidBillingRateDtoList = new ArrayList<>();

			Merchant merchantDetails = merchantRepository.findByMerchantCode(merchantCode);

			if (null == merchantDetails) {
				resultResponse.addErrorAPIResponse(MerchantCodes.MERCHANT_NOT_AVAIABLE_UPDATE.getIntId(),
						MerchantCodes.MERCHANT_NOT_AVAIABLE_UPDATE.getMsg());
				resultResponse.setResult(MerchantCodes.MERCHANT_UPDATION_FAILED.getId(),
						MerchantCodes.MERCHANT_UPDATION_FAILED.getMsg());
				return resultResponse;
			}

			MerchantNameDomain merchantNameDoaminToUpdate = new MerchantNameDomain.MerchantNameBuilder(
					merchantDto.getMerchantNameEn(), merchantDto.getMerchantNameAr()).build();

			WhatYouGetDomain whatYouGetDoaminToUpdate = new WhatYouGetDomain.WhatYouGetBuilder(
					merchantDto.getWhatYouGetEn(), merchantDto.getWhatYouGetAr()).build();

			TAndCDomain tnCDoaminToUpdate = new TAndCDomain.TAndCBuilder(merchantDto.getTnCEn(), merchantDto.getTnCAr())
					.build();

			MerchantDescriptionDomain merchantDescriptionDoaminToUpdate = new MerchantDescriptionDomain.MerchantDescriptionBuilder()
					.merchantDescEn(merchantDto.getMerchantDescEn()).merchantDescAr(merchantDto.getMerchantDescAr())
					.build();

			List<MerchantContactPersonDomain> contactPersonDomainListToUpdate = prepareContactPersonDomainList(
					merchantDto, merchantDetails);

			List<MerchantBillingRateDomain> billingRateDomainListToUpdate = new ArrayList<>();
			
//				billingRateDomainListToUpdate = merchantDomain
//						.createBillingRateDomain(merchantDto.getDiscountBillingRates(), invalidBillingRateDtoList);
				
			billingRateDomainListToUpdate = updateBillingRates(merchantDto.getDiscountBillingRates(),
					invalidBillingRateDtoList, merchantDetails.getBillingRates());

			BarcodeDomain barcode = new BarcodeDomain.BarcodeBuilder(merchantDto.getBarcodeType())
					.id(merchantDto.getBarcodeType()).build();
			CategoryDomain categoryDomain = new CategoryDomain.CategoryBuilder(merchantDto.getCategory()).build();

			MerchantDomain merchantDomainToUpdate = new MerchantDomain.MerchantBuilder(
					merchantDetails.getMerchantCode(), merchantNameDoaminToUpdate, merchantDto.getPartnerCode(),
					categoryDomain, barcode, contactPersonDomainListToUpdate, billingRateDomainListToUpdate,
					merchantDetails.getStatus(),merchantDetails.getOfferCount()).id(merchantDetails.getId()).whatYouGet(whatYouGetDoaminToUpdate)
							.tnC(tnCDoaminToUpdate).merchantDescription(merchantDescriptionDoaminToUpdate)
							.externalName(merchantDto.getExternalName()).usrCreated(merchantDetails.getUsrCreated())
							.dtCreated(merchantDetails.getDtCreated()).usrUpdated(null != userName ? userName : token)
							.dtUpdated(new Date())
							.programCode(null != merchantDetails.getProgramCode() ? merchantDetails.getProgramCode()
									: program)
							.build();

			merchantDomain.updateMerchant(merchantDomainToUpdate, merchantDetails,
					MerchantRequestMappingConstants.UPDATE_MERCHANT,externalTransactionId);
			merchantControllerHelper.validateUsernameExists(merchantDto, resultResponse, merchantDetails);

			for (MerchantBillingRateDto merchantBillingRateDto : invalidBillingRateDtoList) {
				resultResponse.addErrorAPIResponse(MerchantCodes.DUPLICATE_DISCOUNT_BILLINGRATE.getIntId(),
						"Duplicate Discount billing Rate of RateType : " + merchantBillingRateDto.getRateType()
								+ " and Rate : " + merchantBillingRateDto.getRate());
			}

			resultResponse.setResult(MerchantCodes.MERCHANT_UPDATED_SUCCESSFULLY.getId(),
					MerchantCodes.MERCHANT_UPDATED_SUCCESSFULLY.getMsg());
			resultResponse.setSuccessAPIResponse();
			
			offerService.updateEligibleOffers(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
			

		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg());
			resultResponse.setResult(MerchantCodes.MERCHANT_UPDATION_FAILED.getId(),
					MerchantCodes.MERCHANT_UPDATION_FAILED.getMsg());
			LOG.error(e.printMessage());
		}

		catch (Exception e) {
			
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(MerchantCodes.MERCHANT_UPDATION_FAILED.getId(),
					MerchantCodes.MERCHANT_UPDATION_FAILED.getMsg());
			LOG.error(e.getMessage());

		}
		LOG.info("updateMerchant response parameters: {}", merchantDto);

		return resultResponse;

	}

	private List<MerchantContactPersonDomain> prepareContactPersonDomainList(MerchantDto merchantDto,
			Merchant merchantDetails) {
		List<MerchantContactPersonDomain> contactPersonDomainListToUpdate = new ArrayList<>();
		for (final ContactPerson ContactPerson : merchantDetails.getContactPersons()) {
			boolean exists = false;
			for (final ContactPersonDto contactPersonDto : merchantDto.getContactPersons()) {
				if (ContactPerson.getUserName().equals(contactPersonDto.getUserName())) {
					exists = true;
					contactPersonDomainListToUpdate
							.add(new MerchantContactPersonDomain.ContactPersonBuilder(contactPersonDto.getEmailId(),
									contactPersonDto.getFirstName(), contactPersonDto.getLastName(),
									contactPersonDto.getMobileNumber()).faxNumber(contactPersonDto.getFaxNumber())
											.userName(contactPersonDto.getUserName()).build());
				}

			}
			if (!exists) {
				contactPersonDomainListToUpdate
						.add(new MerchantContactPersonDomain.ContactPersonBuilder(ContactPerson.getEmailId(),
								ContactPerson.getFirstName(), ContactPerson.getLastName(),
								ContactPerson.getMobileNumber()).faxNumber(ContactPerson.getFaxNumber())
										.userName(ContactPerson.getUserName()).build());
			}

		}
		return contactPersonDomainListToUpdate;
	}

	public List<MerchantBillingRateDomain> updateBillingRates(List<MerchantBillingRateDto> newBillingRateDtoList,
			List<MerchantBillingRateDto> invalidBillingRateDtoList, List<MerchantBillingRate> existingBillingRates)
			throws ParseException {
	
		List<MerchantBillingRateDomain> billingRateList = new ArrayList<>();
		
		if (null != newBillingRateDtoList && !newBillingRateDtoList.isEmpty()) {
			for (final MerchantBillingRateDto newBillingRate : newBillingRateDtoList) {

				boolean existingBill = false;

				if (null != existingBillingRates && !existingBillingRates.isEmpty()) {
					for (MerchantBillingRate existingBillingRate : existingBillingRates) {

						if (null != newBillingRate && null != newBillingRate.getRateType()
								&& newBillingRate.getRateType().equals(existingBillingRate.getRateType())) {

							existingBill = true;

							Date billingRateStartDate = null;
							Date billingRateEndDate = null;

							if (null != newBillingRate.getStartDate()) {
								billingRateStartDate = (!StringUtils.isEmpty(newBillingRate.getStartDate()))
										? new SimpleDateFormat("dd-MM-yyyy").parse(newBillingRate.getStartDate())
										: null;
							}

							if (null != newBillingRate.getEndDate()) {
								billingRateEndDate = (!StringUtils.isEmpty(newBillingRate.getEndDate()))
										? new SimpleDateFormat("dd-MM-yyyy").parse(newBillingRate.getEndDate())
										: null;
							}

							boolean addToBillingRate = true;

							for (final MerchantBillingRateDomain billingRateDto : billingRateList) {

								if (null != newBillingRate.getRateType()
										&& newBillingRate.getRateType().equals(billingRateDto.getRateType())) {

									addToBillingRate = false;
								}

							}

							if (null != newBillingRate.getRateType() && addToBillingRate) {
								billingRateList.add(new MerchantBillingRateDomain.MerchantBillingRateBuilder(
										null != newBillingRate.getRate() ? newBillingRate.getRate()
												: existingBillingRate.getRate(),
										null != newBillingRate.getStartDate() ? billingRateStartDate
												: existingBillingRate.getStartDate(),
										null != newBillingRate.getEndDate() ? billingRateEndDate
												: existingBillingRate.getEndDate(),
										null != newBillingRate.getRateType() ? newBillingRate.getRateType()
												: existingBillingRate.getRateType(),
										null != newBillingRate.getCurrency() ? newBillingRate.getCurrency()
												: existingBillingRate.getCurrency()).build());
							} else if (null != newBillingRate.getRateType() && !addToBillingRate) {
								invalidBillingRateDtoList.add(newBillingRate);
							}

						}

					}
				}

				if (!existingBill) {

					Date billingRateStartDate = null;
					Date billingRateEndDate = null;

					if (null != newBillingRate.getStartDate()) {
						billingRateStartDate = (!StringUtils.isEmpty(newBillingRate.getStartDate()))
								? new SimpleDateFormat("dd-MM-yyyy").parse(newBillingRate.getStartDate())
								: null;
					}

					if (null != newBillingRate.getEndDate()) {
						billingRateEndDate = (!StringUtils.isEmpty(newBillingRate.getEndDate()))
								? new SimpleDateFormat("dd-MM-yyyy").parse(newBillingRate.getEndDate())
								: null;
					}

					boolean addToBillingRate = true;

					for (final MerchantBillingRateDomain billingRateDto : billingRateList) {

						if (null != newBillingRate.getRateType()
								&& newBillingRate.getRateType().equals(billingRateDto.getRateType())) {

							addToBillingRate = false;
						}

					}

					if (null != newBillingRate.getRateType() && addToBillingRate) {
						billingRateList
								.add(new MerchantBillingRateDomain.MerchantBillingRateBuilder(newBillingRate.getRate(),
										billingRateStartDate, billingRateEndDate, newBillingRate.getRateType(),
										newBillingRate.getCurrency()).build());
					} else if (null != newBillingRate.getRateType() && !addToBillingRate) {
						invalidBillingRateDtoList.add(newBillingRate);
					}

				}

			}

		}
		if (null != existingBillingRates && !existingBillingRates.isEmpty()) {
			for (MerchantBillingRate existingBillingRate : existingBillingRates) {

				MerchantBillingRateDomain billingRateAdded = null;

				if (!billingRateList.isEmpty()) {
					billingRateAdded = billingRateList.stream()
							.filter(m -> null != existingBillingRate.getRateType()
									&& existingBillingRate.getRateType().equals(m.getRateType()))
							.findAny().orElse(null);
				}

				if (null == billingRateAdded) {

					billingRateList.add(new MerchantBillingRateDomain.MerchantBillingRateBuilder(existingBillingRate.getRate(),
							existingBillingRate.getStartDate(), existingBillingRate.getEndDate(), existingBillingRate.getRateType(),
							existingBillingRate.getCurrency()).build());

				}

			}
		}
	
		return billingRateList;
	}
	
	@PostMapping(value = MerchantRequestMappingConstants.ACTIVATE_DEACTIVATE_MERCHANT)
	public ResultResponse activateDeactivateMerchant(
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
			@RequestHeader(value = MerchantRequestMappingConstants.MERCHANT_STATUS, required = true) String status,
			@PathVariable(value = MerchantRequestMappingConstants.MERCHANT_CODE, required = true) String merchantCode) {

		LOG.info("activateDeactivateMerchant request parameters :");

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		try {

			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			if (!MerchantConstants.MERCHANT_ACTIVE_STATUS.get().equalsIgnoreCase(status)
					&& !MerchantConstants.MERCHANT_DEFAULT_STATUS.get().equalsIgnoreCase(status)) {
				resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_MERCHANT_STATUS.getIntId(),
						MerchantCodes.INVALID_MERCHANT_STATUS.getMsg());
				resultResponse.setResult(MerchantCodes.MERCHANT_STATUS_UPDATE_FAILED.getId(),
						MerchantCodes.MERCHANT_STATUS_UPDATE_FAILED.getMsg());
				return resultResponse;
			}

			Merchant merchant = merchantRepository.findByMerchantCode(merchantCode);

			if (null == merchant) {
				resultResponse.setResult(MerchantCodes.MERCHANT_NOT_AVAIABLE_UPDATE.getId(),
						MerchantCodes.MERCHANT_NOT_AVAIABLE_UPDATE.getMsg());
				return resultResponse;
			}

			if (!merchant.getStatus().equalsIgnoreCase(status)) {

				Merchant updatedMerchantStatus = merchantDomain.updateMerchantStatus(merchant, status,
						null != userName ? userName : token, program,
						MerchantRequestMappingConstants.ACTIVATE_DEACTIVATE_MERCHANT,externalTransactionId);

				if (updatedMerchantStatus.getStatus()
						.equalsIgnoreCase(MerchantConstants.MERCHANT_DEFAULT_STATUS.get())) {
					resultResponse.setResult(MerchantCodes.MERCHANT_DEACTIVATED.getId(),
							MerchantCodes.MERCHANT_DEACTIVATED.getMsg());

				} else {
					resultResponse.setResult(MerchantCodes.MERCHANT_ACTIVATED.getId(),
							MerchantCodes.MERCHANT_ACTIVATED.getMsg());
				}

			} else {

				if (status.equalsIgnoreCase(MerchantConstants.MERCHANT_DEFAULT_STATUS.get())) {
					resultResponse.setResult(MerchantCodes.MERCHANT_DEACTIVATED_ALREADY.getId(),
							MerchantCodes.MERCHANT_DEACTIVATED_ALREADY.getMsg());

				} else {
					resultResponse.setResult(MerchantCodes.MERCHANT_ACTIVATED_ALREADY.getId(),
							MerchantCodes.MERCHANT_ACTIVATED_ALREADY.getMsg());
				}

			}
			
			offerService.updateEligibleOffers(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
			
			
		} catch (MarketplaceException markerplaceException) {
			resultResponse.setResult(markerplaceException.getErrorCode(), markerplaceException.getErrorMsg());
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(MerchantCodes.MERCHANT_STATUS_UPDATE_FAILED.getId(),
					MerchantCodes.MERCHANT_STATUS_UPDATE_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "activateDeactivateMerchant",
					e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}
		LOG.info("activateDeactivateMerchant response parameters: {}", resultResponse);

		return resultResponse;

	}

	@GetMapping(value = MerchantRequestMappingConstants.LIST_ALL_MERCHANTS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListMerchantsResponse listAllMerchants(
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
			@RequestHeader(value = RequestMappingConstants.MERCHANT_STATUS, required = false) String merchantStatus,
			@RequestHeader(value = RequestMappingConstants.FILTER_FLAG, required = false) String filterFlag,
			@RequestHeader(value = RequestMappingConstants.FILTER_VALUE, required = false) String filterValue,
			@RequestHeader(value = RequestMappingConstants.CATEGORY, required = false) String category,
			@RequestHeader(value = RequestMappingConstants.DROPDOWN, required = false) String dropDown,
			@RequestParam(value = MerchantRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = MerchantRequestMappingConstants.LIMIT, required = false) Integer limit) {

		LOG.info("Entering listAllMerchants in MerchantController.");
		ListMerchantsResponse merchantResultResponse = new ListMerchantsResponse(externalTransactionId);

		final String domain = MerchantRequestMappingConstants.MERCHANT_DOMAIN;

		try {
			
			if(null == program) program = defaultProgramCode;
			
			if(limit==null && page==null) {
				limit=Integer.MAX_VALUE;
				page=0;
			}
			else if(limit==null) {
				limit = Integer.MAX_VALUE;
			}
			else if(page == null) {
				page = 0;
			}
			Pageable pageNumberWithElements = PageRequest.of(page,limit);
			ListMerchantsWithTotal merchantOutputRes = new ListMerchantsWithTotal();
			List<Merchant> merchants = new ArrayList<>();
			
			if(null != dropDown && dropDown.equalsIgnoreCase("YES")) {
				
//				Aggregation aggregation = newAggregation(project(from(field("Name.English"),field("MerchantCode"))),				
//						match(Criteria.where("MerchantCode").exists(true)));
				
				Aggregation aggregation = newAggregation(project(from(field("Name.English"),field("Name.Arabic"),field("MerchantCode"),field("ProgramCode"))),				
						match(Criteria.where("MerchantCode").exists(true).and("ProgramCode")
								.regex("^"+program+"$","i")));
				
				AggregationResults<MerchantDropdownResult> merchantResults = mongoOperations.aggregate(aggregation,
						DBConstants.MERCHANT, MerchantDropdownResult.class);
				
				List<MerchantDropdownResult> merchantList = !CollectionUtils.isEmpty(merchantResults.getMappedResults()) ? merchantResults.getMappedResults() : null;
				
				if(null == merchantList) {
					merchantResultResponse.addErrorAPIResponse(MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getIntId(),
							MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getMsg());
					merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_DROPDOWN_FAILED.getId(),
							MerchantCodes.LISTING_MERCHANTS_DROPDOWN_FAILED.getMsg());
					return merchantResultResponse;
				}
				
				List<MerchantResult> merchantResponse = new ArrayList<>();
				merchantList.stream().forEach(c -> {
					merchantResponse.add(modelMapper.map(c, MerchantResult.class));
				});
					
				merchantResultResponse.setTotalRecords(!CollectionUtils.isEmpty(merchantResponse)?merchantResponse.size():0);
				merchantResultResponse.setListMerchants(merchantResponse);
				merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_DROPDOWN_SUCCESS.getId(),
						MerchantCodes.LISTING_MERCHANTS_DROPDOWN_SUCCESS.getMsg());
				
				LOG.info("Exiting listAllMerchants in MerchantController.");
				
				return merchantResultResponse;
				
			} else if (!StringUtils.isEmpty(channelId)
					&& channelId.equalsIgnoreCase(MerchantConstants.CHANNELID_ADMIN.get())) {
				merchantOutputRes = merchantControllerHelper.listMerchantsForAdmin(merchantStatus, pageNumberWithElements, program);
				merchants = merchantOutputRes.getListMerchants();
			} else {
				if (!StringUtils.isEmpty(filterFlag)
						&& filterFlag.equalsIgnoreCase(MerchantConstants.FILTER_FLAG_KEYWORD.get())
						&& StringUtils.isEmpty(filterValue)) {
					merchantResultResponse.setResult(MerchantCodes.KEYWORD_VALUE_REQUIRED.getId(),
							MerchantCodes.KEYWORD_VALUE_REQUIRED.getMsg());
					return merchantResultResponse;
				} else {
					merchantOutputRes = merchantControllerHelper.listMerchantsForNonAdmin(filterFlag, filterValue,
							category, token, pageNumberWithElements, externalTransactionId, program);
				}
				merchants = merchantOutputRes.getListMerchants();
				listMerchantsForWebChannel(program, authorization, externalTransactionId, userName, sessionId, userPrev,
						channelId, systemId, systemPassword, token, transactionId, domain, merchants);
			}

			if (null == merchants || merchants.isEmpty()) {
				merchantResultResponse.addErrorAPIResponse(MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getIntId(),
						MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getMsg());
				merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
						MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
				LOG.info("listAllMerchants response parameters: {}", merchantResultResponse);
				return merchantResultResponse;
			}
			List<MerchantResult> merchantList = prepareMerchantList(merchants);

			merchantResultResponse.setListMerchants(merchantList);
			merchantResultResponse.setTotalRecords(merchantOutputRes.getTotalRecords());
			merchantResultResponse.setResult(MerchantCodes.MERCHANTS_LISTED_SUCCESSFULLY.getId(),
					MerchantCodes.MERCHANTS_LISTED_SUCCESSFULLY.getMsg());

		} catch (Exception e) {
			merchantResultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					MarketPlaceCode.LIST_ALL_MERCHANTS.getConstant(), e.getClass() + e.getMessage(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}
		LOG.info("Exiting listAllMerchants in MerchantController.");
		return merchantResultResponse;
	}

	private void listMerchantsForWebChannel(String program, String authorization, String externalTransactionId,
			String userName, String sessionId, String userPrev, String channelId, String systemId,
			String systemPassword, String token, String transactionId, final String domain, List<Merchant> merchants) {
//		if (null != merchants && !merchants.isEmpty()) {
//			ListMerchantImagesResponse listMerchantImagesResponse = listMerchantImages(program, authorization,
//					externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
//					transactionId, domain);
//
//			if (null != listMerchantImagesResponse) {
//				List<MerchantImagesResult> merchantImagesResult = listMerchantImagesResponse.getListMerchantImages();
//				Map<String, String> imageByDomainId = new HashMap<>();
//				if (null != merchantImagesResult) {
//					for (MerchantImagesResult merchantImages : merchantImagesResult) {
//						imageByDomainId.computeIfAbsent(merchantImages.getDomainId(),
//								k -> merchantImages.getImageUrl());
//					}
//					for (Merchant merchant : merchants) {
//						merchant.setImageUrl(imageByDomainId.get(merchant.getMerchantCode()));
//					}
//				}
//			}
//		}
	}

	private List<MerchantResult> prepareMerchantList(List<Merchant> merchants) {
		
		List<MerchantResult> merchantList = new ArrayList<>();
		List<String> listMerchant = merchants.stream().map(Merchant::getMerchantCode).collect(Collectors.toList());
		
		List<MarketplaceImage> listMerchantImages = imageRepository.findByImageCategoryAndMerchantOfferImageDomainIdIn(
				MerchantConstants.IMAGE_CATEGORY_MERCHANT_OFFER.get(), listMerchant);

		for (Merchant merchant : merchants) {
			List<ImageInfo> imageInfoList = new ArrayList<>();

			MerchantResult merchantResult = modelMapper.map(merchant, MerchantResult.class);
			if (null != merchant.getBarcodeType()) {
				merchantResult.setBarcodeType(merchant.getBarcodeType().getName());
				merchantResult.setBarcodeId(merchant.getBarcodeType().getId());
			}
			if (null != merchant.getCategory() && null != merchant.getCategory().getCategoryName()) {
				merchantResult.setCategory(merchant.getCategory().getCategoryName().getCategoryNameEn());
			}
			List<MarketplaceImage> images = listMerchantImages.stream()
					.filter(c -> merchant.getMerchantCode().equals(c.getMerchantOfferImage().getDomainId()))
					.collect(Collectors.toList());
			
			if (!images.isEmpty()) {
				for (MarketplaceImage image : images) {
					ImageInfo imageInfo = new ImageInfo();
					if (null != image.getMerchantOfferImage()) {
						imageInfo.setImageType(image.getMerchantOfferImage().getImageType());
						imageInfo.setAvailableInChannel(image.getMerchantOfferImage().getAvailableInChannel());
					}
					imageInfo.setImageUrl(image.getImageUrl());
					imageInfo.setImageUrlDr(image.getImageUrlDr());
					imageInfo.setImageUrlProd(image.getImageUrlProd());
					imageInfoList.add(imageInfo);
				}

				merchantResult.setImageInfo(imageInfoList);
			}
			
			if(!ObjectUtils.isEmpty(merchant.getOfferCount())) {
				merchantResult.setOfferCount(merchant.getOfferCount());
			}

			merchantList.add(merchantResult);
		}
		
		
		return merchantList;
	
	}

	@GetMapping(value = MerchantRequestMappingConstants.VIEW_PARTNER_MERCHANTS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListMerchantsResponse viewPartnerMerchants(
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
			@PathVariable(value = MerchantRequestMappingConstants.PARTNER_CODE, required = true) String partnerCode,
			@RequestParam(value = MerchantRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = MerchantRequestMappingConstants.LIMIT, required = false) Integer limit) {

		LOG.info("Entering viewPartnerMerchants in MerchantController. request parameters: {}", partnerCode);

		ListMerchantsResponse merchantResultResponse = new ListMerchantsResponse(externalTransactionId);

		try {

			if(null == program) program = defaultProgramCode;
			
			List<Merchant> merchants = listPartnerMerchantsPagination(page, limit, partnerCode,
					merchantResultResponse, program);

			if(merchants.isEmpty() && !merchantResultResponse.getApiStatus().getErrors().isEmpty()) return merchantResultResponse;
			
			if (null == merchants || merchants.isEmpty()) {
				merchantResultResponse.addErrorAPIResponse(MerchantCodes.NO_MERCHANTS_FOR_PARTNER_TO_DISPLAY.getIntId(),
						MerchantCodes.NO_MERCHANTS_FOR_PARTNER_TO_DISPLAY.getMsg());
				merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
						MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
				return merchantResultResponse;
			}
				
			List<MerchantResult> merchantList = new ArrayList<>();
			
			for (Merchant merchant : merchants) {
				
				MerchantResult merchantResult = modelMapper.map(merchant, MerchantResult.class);
				if (null != merchant.getBarcodeType()) {
					merchantResult.setBarcodeType(merchant.getBarcodeType().getName());
					merchantResult.setBarcodeId(merchant.getBarcodeType().getId());
				}
				if (null != merchant.getCategory() && null != merchant.getCategory().getCategoryName()) {
					merchantResult.setCategory(merchant.getCategory().getCategoryName().getCategoryNameEn());
				}
				
				merchantResult = merchantControllerHelper.populateImageAttributes(merchant, merchantResult, program);
				merchantList.add(merchantResult);
			
			}

			merchantResultResponse.setListMerchants(merchantList);
			merchantResultResponse.setResult(MerchantCodes.MERCHANTS_LISTED_FOR_PARTNER_SUCCESSFULLY.getId(),
					MerchantCodes.MERCHANTS_LISTED_FOR_PARTNER_SUCCESSFULLY.getMsg());
			
		} catch (Exception e) {

			merchantResultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "viewPartnerMerchants",
					e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}

		LOG.info("Exiting viewPartnerMerchants in MerchantController.");

		return merchantResultResponse;

	}

	@GetMapping(value = MerchantRequestMappingConstants.VIEW_SPECIFIC_MERCHANT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListMerchantsStoreOfferResponse viewSpecificMerchant(
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
			@PathVariable(value = MerchantRequestMappingConstants.MERCHANT_CODE, required = true) String merchantCode,
			@RequestParam(value = MerchantRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = MerchantRequestMappingConstants.LIMIT, required = false) Integer limit) {

		LOG.info("Entering viewSpecificMerchant in MerchantController. request parameters: {}", merchantCode);

		ListMerchantsStoreOfferResponse merchantStoreOfferResultResponse = new ListMerchantsStoreOfferResponse(
				externalTransactionId);

		try {

			if(null == program) program = defaultProgramCode;
			
			Merchant merchant = listSpecificMerchantsPagination(page, limit, channelId, merchantCode, merchantStoreOfferResultResponse, program);
			
			if(null == merchant && !merchantStoreOfferResultResponse.getApiStatus().getErrors().isEmpty()) return merchantStoreOfferResultResponse;
			
			if (null == merchant) {
				merchantStoreOfferResultResponse.addErrorAPIResponse(
						MerchantCodes.NO_SPECIFIC_MERCHANT_TO_DISPLAY.getIntId(),
						MerchantCodes.NO_SPECIFIC_MERCHANT_TO_DISPLAY.getMsg());
				merchantStoreOfferResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
						MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
				return merchantStoreOfferResultResponse;
			}
			
			MerchantStoreOfferResult merchantList = modelMapper.map(merchant, MerchantStoreOfferResult.class);
			merchantList = merchantControllerHelper.populateMerchantAttributes(merchant, merchantList);
			
			if (!StringUtils.isEmpty(channelId) && channelId.equalsIgnoreCase(MerchantConstants.CHANNELID_WEB.get())) {
				//List<Store> stores = storeRepository.findByMerchantCode(merchantCode);
				List<Store> stores = storeRepository.findByMerchantCodeAndProgramCodeIgnoreCase(merchantCode, program);
				List<StoreResult> listStores = new ArrayList<>();

				for (Store store : stores) {
					StoreResult storeResult = modelMapper.map(store, StoreResult.class);
					listStores.add(storeResult);
				}
				merchantList.setStores(listStores);

				List<MerchantOffers> listOffers = offerService.getOfferDetails(merchantCode, token);
				merchantList.setOffers(listOffers);
			}
			
		//	List<MarketplaceImage> images = imageRepository.findByImageCategoryAndDomainId(MerchantConstants.IMAGE_CATEGORY_MERCHANT_OFFER.get(), merchant.getMerchantCode());
		List<MarketplaceImage> images = imageRepository.findByImageCategoryAndDomainIdAndProgramCodeIgnoreCase(
				MerchantConstants.IMAGE_CATEGORY_MERCHANT_OFFER.get(), merchant.getMerchantCode(), program);
		List<ImageInfo> imageInfoList = new ArrayList<>();
			if(!images.isEmpty()) {
				for(MarketplaceImage image : images) {
					ImageInfo imageInfo = new ImageInfo();
					if(null != image.getMerchantOfferImage()) {
						imageInfo.setImageType(image.getMerchantOfferImage().getImageType());
						imageInfo.setAvailableInChannel(image.getMerchantOfferImage().getAvailableInChannel());
					}
					imageInfo.setImageUrl(image.getImageUrl());
					imageInfo.setImageUrlDr(image.getImageUrlDr());
					imageInfo.setImageUrlProd(image.getImageUrlProd());
					imageInfoList.add(imageInfo);
				}
				merchantList.setImageInfo(imageInfoList);
			}
			
			merchantStoreOfferResultResponse.setListMerchants(merchantList);

			merchantStoreOfferResultResponse.setResult(MerchantCodes.SPECIFIC_MERCHANT_LISTED_SUCCESSFULLY.getId(),
					MerchantCodes.SPECIFIC_MERCHANT_LISTED_SUCCESSFULLY.getMsg());

		} catch (Exception e) {

			merchantStoreOfferResultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			merchantStoreOfferResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "viewSpecificMerchant",
					e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}

		LOG.info("Exiting viewSpecificMerchant in MerchantController.");

		return merchantStoreOfferResultResponse;

	}
	
	@GetMapping(value = MerchantRequestMappingConstants.LIST_DROPDOWN_MERCHANTS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListMerchantsDropdownResponse listDropdownMerchants(
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

		LOG.info("Entering listDropdownMerchants in MerchantController");

		ListMerchantsDropdownResponse merchantStoreOfferResultResponse = new ListMerchantsDropdownResponse(
				externalTransactionId);

		try {

			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			Aggregation aggregation = null;
			
			if (!StringUtils.isEmpty(channelId)
					&& channelId.equalsIgnoreCase(MerchantConstants.CHANNELID_ADMIN.get())) {
				
				aggregation = newAggregation(project(from(field("Name.English"),field("MerchantCode"))),				
						match(Criteria.where("MerchantCode").exists(true)));
				
			} else {
				
				aggregation = newAggregation(project(from(field("Name.English"),field("MerchantCode"), field("Status"))),				
						match(Criteria.where("Status").is("Active")));
				
			}
			
			AggregationResults<MerchantDropdownResult> dateResults = mongoOperations.aggregate(aggregation,
					DBConstants.MERCHANT, MerchantDropdownResult.class);
			
			merchantStoreOfferResultResponse.setMerchantsList(!CollectionUtils.isEmpty(dateResults.getMappedResults()) ? dateResults.getMappedResults() : null);
			
			merchantStoreOfferResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_DROPDOWN_SUCCESS.getId(),
					MerchantCodes.LISTING_MERCHANTS_DROPDOWN_SUCCESS.getMsg());

		} catch (Exception e) {

			merchantStoreOfferResultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			merchantStoreOfferResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_DROPDOWN_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_DROPDOWN_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "listDropdownMerchants",
					e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}

		LOG.info("Exiting listDropdownMerchants in MerchantController");

		return merchantStoreOfferResultResponse;

	}
		
	@PostMapping(value = MerchantRequestMappingConstants.CONFIGURE_RATETYPE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureRateTypes(@RequestBody RateTypeRequestDto rateTypeRequest,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId)
			throws Exception {

		LOG.info("rateType request parameters: {}", rateTypeRequest);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		List<RateType> rateTypesToSave = new ArrayList<>();
		List<RateTypeDto> rateTypesToSaveInDB = new ArrayList<>();
		List<RateType> existingRates = rateTypeRepository.findAll();

		int existingSize = 0;

		if (!existingRates.isEmpty()) {
			existingSize = existingRates.size();
		}
		if (null == rateTypeRequest || null == rateTypeRequest.getRateTypes()
				|| rateTypeRequest.getRateTypes().isEmpty()) {
			resultResponse.addErrorAPIResponse(MerchantCodes.RATE_TYPE_LIST_EMPTY.getIntId(),
					MerchantCodes.RATE_TYPE_LIST_EMPTY.getMsg());
			resultResponse.setResult(MerchantCodes.RATE_TYPE_ADDITION_FAILED.getId(),
					MerchantCodes.RATE_TYPE_ADDITION_FAILED.getMsg());
			return resultResponse;
		}
		List<Errors> errorList = new ArrayList<>();
		for (RateTypeDto rateTypeDto : rateTypeRequest.getRateTypes()) {

			if (!checkIfRateTypeExistis(existingRates, rateTypeDto)) {
				rateTypesToSaveInDB.add(rateTypeDto);
			}

			else {
				Errors error = new Errors();
				error.setCode(MerchantCodes.DUPLICATE_RATE_TYPE.getIntId());
				error.setMessage(rateTypeDto.getTypeRate() + " :" + MerchantCodes.DUPLICATE_RATE_TYPE.getMsg());
				errorList.add(error);
			}

		}
		resultResponse.setBulkErrorAPIResponse(errorList);
		if (rateTypesToSaveInDB.isEmpty()) {
			resultResponse.setResult(MerchantCodes.RATE_TYPE_ADDITION_FAILED.getId(),
					MerchantCodes.RATE_TYPE_ADDITION_FAILED.getMsg());
			return resultResponse;
		}
		program = programManagement.getProgramCode(program);
		if (merchantControllerHelper.validateRateTypeRequest(rateTypesToSaveInDB, resultResponse, rateTypesToSave,
				existingSize, null != userName ? userName : token, program)) {
			try {
				rateTypeRepository.saveAll(rateTypesToSave);
//				auditService.insertDataAudit(DBConstants.RATE_TYPE, rateTypesToSave,
//						MerchantRequestMappingConstants.CONFIGURE_RATETYPE,externalTransactionId,userName);
				resultResponse.setResult(MerchantCodes.RATE_TYPE_ADDED_SUSSESSFULY.getId(),
						MerchantCodes.RATE_TYPE_ADDED_SUSSESSFULY.getMsg());
			} catch (Exception e) {
				resultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
						MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
				resultResponse.setResult(MerchantCodes.RATE_TYPE_ADDITION_FAILED.getId(),
						MerchantCodes.RATE_TYPE_ADDITION_FAILED.getMsg());
				LOG.error(new MarketplaceException(this.getClass().toString(), "configureRateTypes",
						e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
			}
		}

		LOG.info("rateType response parameters: {}", resultResponse);

		return resultResponse;
	}

	private boolean checkIfRateTypeExistis(List<RateType> existingRates, RateTypeDto rateTypeDto) {
		boolean exists = false;
		for (RateType rateType : existingRates) {
			if (rateTypeDto.getTypeRate().equalsIgnoreCase(rateType.getTypeRate())) {
				exists = true;
			}
		}
		return exists;
	}

	@PostMapping(value = MerchantRequestMappingConstants.CONFIGURE_BARCODE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureBarcodeType(@Valid @RequestBody BarcodeRequestDto barcodeRequest,
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

		LOG.info("configureBarcodeType request parameters: {}", barcodeRequest);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		List<Barcode> barcodesToSave = new ArrayList<>();
		//List<Barcode> existingBarcodes = barcodeRepository.findAll();
		//int existingSize = 0;
		//if (!existingBarcodes.isEmpty()) {
			//existingSize = existingBarcodes.size();
		//}
		try {
			program = programManagement.getProgramCode(program);
			if (null != barcodeRequest && null != barcodeRequest.getBarcodes()
					&& !barcodeRequest.getBarcodes().isEmpty()) {

				for (BarcodeDto barcode : barcodeRequest.getBarcodes()) {
					Barcode barcodeToSave = new Barcode();
					modelMapper.map(barcode, barcodeToSave);
					//barcodeToSave.setId(String.valueOf(++existingSize));
					barcodeToSave.setDtCreated(new Date());
					barcodeToSave.setUsrCreated(userName);
					barcodeToSave.setDtUpdated(new Date());
					barcodeToSave.setUsrUpdated(userName);
					barcodeToSave.setProgram(program);
					barcodesToSave.add(barcodeToSave);
				}

				barcodeRepository.saveAll(barcodesToSave);
//				auditService.insertDataAudit(DBConstants.BARCODE, barcodesToSave,
//						MerchantRequestMappingConstants.CONFIGURE_BARCODE,externalTransactionId,userName);

				resultResponse.setResult(MerchantCodes.BARCODE_ADDED_SUSSESSFULY.getId(),
						MerchantCodes.BARCODE_ADDED_SUSSESSFULY.getMsg());
			}
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(MerchantCodes.BARCODE_ADDITION_FAILED.getId(),
					MerchantCodes.BARCODE_ADDITION_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "configureBarcodes",
					e.getClass() + e.getMessage(), MerchantCodes.BARCODE_ADDITION_FAILED).printMessage());

		}

		LOG.info("configureBarcodeType response parameters: {}", resultResponse);

		return resultResponse;
	}

	@PostMapping(value = MerchantRequestMappingConstants.CONFIGURE_IMAGE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureImage(@Valid @RequestBody MerchantImageRequestDto merchantImageRequestDto,
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

		LOG.info("configureImage request parameters: {}", merchantImageRequestDto);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		MerchantImage merchantImageRequestDtoToSave = new MerchantImage();

		try {
			modelMapper.map(merchantImageRequestDto, merchantImageRequestDtoToSave);
			merchantImageRepository.save(merchantImageRequestDtoToSave);
//			auditService.insertDataAudit(DBConstants.MERCHANT_IMAGE, merchantImageRequestDtoToSave,
//					MerchantRequestMappingConstants.CONFIGURE_IMAGE,externalTransactionId,userName);
			resultResponse.setResult(MerchantCodes.RATE_TYPE_ADDED_SUSSESSFULY.getId(),
					MerchantCodes.RATE_TYPE_ADDED_SUSSESSFULY.getMsg());
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(MerchantCodes.RATE_TYPE_ADDITION_FAILED.getId(),
					MerchantCodes.RATE_TYPE_ADDITION_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "configureRateTypes",
					e.getClass() + e.getMessage(), MerchantCodes.RATE_TYPE_ADDITION_FAILED).printMessage());
		}

		LOG.info("configureImage response parameters: {}", resultResponse);

		return resultResponse;
	}

	@GetMapping(value = MerchantRequestMappingConstants.LIST_MERCHANT_THUMDNAILS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListMerchantImagesResponse listMerchantThumbnails(
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

		ListMerchantImagesResponse merchantImagesResultResponse = new ListMerchantImagesResponse();
		Result result = new Result();

		try {
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			List<MerchantImage> merchantImageList = merchantImageRepository
					.findByDomain(MerchantRequestMappingConstants.MERCHANT_DOMAIN);
			List<MerchantImagesResult> merchantImageResultList;

			if (null != merchantImageList && !merchantImageList.isEmpty()) {
				merchantImageResultList = ObjectMapperUtils.mapAll(merchantImageList, MerchantImagesResult.class);

				for (MerchantImagesResult mi : merchantImageResultList) {
					mi.setImageUrl(serverAddress + "/" + mi.getImageUrl() + "/" + mi.getImageName());
				}

				merchantImagesResultResponse.setListMerchantImages(merchantImageResultList);
				result.setResponse(MerchantCodes.MERCHANTS_LISTED_SUCCESSFULLY.getId());
				result.setDescription(MerchantCodes.MERCHANTS_LISTED_SUCCESSFULLY.getMsg());
			} else {

				result.setResponse(MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getId());
				result.setDescription(MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getMsg());

			}

			merchantImagesResultResponse.setResult(result);
		} catch (Exception e) {

			result.setResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getId());
			result.setDescription(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			merchantImagesResultResponse.setResult(result);

			LOG.error(new MarketplaceException(this.getClass().toString(),
					MarketPlaceCode.LIST_ALL_MERCHANTS.getConstant(), e.getClass() + e.getMessage(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}
		return merchantImagesResultResponse;
	}

	@GetMapping(value = MerchantRequestMappingConstants.LIST_MERCHANT_IMAGES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListMerchantImagesResponse listMerchantImages(
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
			@PathVariable(value = MerchantRequestMappingConstants.MERCHANT_DOMAIN, required = true) String domain) {

		ListMerchantImagesResponse merchantImagesResultResponse = new ListMerchantImagesResponse();
		Result result = new Result();

		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			List<MerchantImage> merchantImageList = merchantImageRepository.findByDomain(domain);
			List<MerchantImagesResult> merchantImageResultList;

			if (!merchantImageList.isEmpty()) {
				merchantImageResultList = ObjectMapperUtils.mapAll(merchantImageList, MerchantImagesResult.class);

				for (MerchantImagesResult mi : merchantImageResultList) {
					mi.setImageUrl(serverAddress + "/" + mi.getImageUrl() + "/" + mi.getImageName());
				}

				merchantImagesResultResponse.setListMerchantImages(merchantImageResultList);
				result.setResponse(MerchantCodes.MERCHANTS_LISTED_SUCCESSFULLY.getId());
				result.setDescription(MerchantCodes.MERCHANTS_LISTED_SUCCESSFULLY.getMsg());
			} else {

				result.setResponse(MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getId());
				result.setDescription(MerchantCodes.NO_MERCHANTS_TO_DISPLAY.getMsg());

			}

			merchantImagesResultResponse.setResult(result);
		} catch (Exception e) {

			result.setResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getId());
			result.setDescription(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			merchantImagesResultResponse.setResult(result);

			LOG.error(new MarketplaceException(this.getClass().toString(),
					MarketPlaceCode.LIST_ALL_MERCHANTS.getConstant(), e.getClass() + e.getMessage(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}
		return merchantImagesResultResponse;
	}

	@GetMapping(value = MerchantRequestMappingConstants.LIST_BARCODES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Barcode> listBarcodes(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
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
		
		List<Barcode> listBarcode = new ArrayList<>();
		
		try {
			
			if(null == program) program = defaultProgramCode;
			return barcodeRepository.findByProgram(program);
		
			//listBarcode = barcodeRepository.findAll();
			//return listBarcode;
			
		} catch (Exception e) {
			return listBarcode;
		}
	
	}
	
	@GetMapping(value = MerchantRequestMappingConstants.GET_MERCHANT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String[] getMerchantCode(@PathVariable String userName) {
		LOG.info("getMerchantCode request parameters: {}", userName);
		String[] merpar = new  String[3];
		
		try {
			Optional<Merchant> merchant = merchantRepository.findByUserNameAndActive(userName,MerchantConstants.MERCHANT_ACTIVE_STATUS.get());
			if(merchant.isPresent() && null!= merchant.get().getMerchantName() ) {
				merpar[0] = merchant.get().getPartnerCode();
				merpar[1] = merchant.get().getMerchantCode();
				if(null!= merchant.get().getMerchantName()) {
				merpar[2] = merchant.get().getMerchantName().getMerchantNameEn();
				}
				return merpar;
			}
		} catch (Exception e) {
			return new String[0];
		}
		return new String[0];
	}

	@GetMapping(value = MerchantRequestMappingConstants.GET_STORE_MERCHANT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String[] getStoreMerchantCode(@PathVariable String userName) {
		LOG.info("getMerchantCode request parameters: {}", userName);
		String[] merstr = new  String[3];
		
		try {
			Optional<Store> store = storeRepository.findByUserName(userName);
			if(store.isPresent()) {
				Merchant merchant = merchantRepository.findByMerchantCode(store.get().getMerchantCode());
				merstr[0] = store.get().getStoreCode();
				merstr[1] = store.get().getMerchantCode();
				 if(null!= merchant && null!= merchant.getMerchantName()) {
					 merstr[2] = merchant.getMerchantName().getMerchantNameEn();
				 }
				return merstr;
			}
		} catch (Exception e) {
			return new String[0];
		}
		return merstr;
	}

	public List<Merchant> listPartnerMerchantsPagination(Integer page, Integer limit, String partnerCode,
			ListMerchantsResponse merchantResultResponse, String program) {

		Integer paginationValidation = Utils.validatePagination(page, limit);

		List<Merchant> merchants = null;
		Page<Merchant> retrievedMerchants = null;
		long totalElements = 0;
		
		if(1 == paginationValidation) {
			merchantResultResponse.addErrorAPIResponse(MerchantCodes.PAGINATION_LIMIT_EMPTY.getIntId(),
					MerchantCodes.PAGINATION_LIMIT_EMPTY.getMsg());
			merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
			return Collections.emptyList();
		}
		
		if(2 == paginationValidation) {
			merchantResultResponse.addErrorAPIResponse(MerchantCodes.PAGINATION_PAGE_EMPTY.getIntId(),
					MerchantCodes.PAGINATION_PAGE_EMPTY.getMsg());
			merchantResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
			return Collections.emptyList();
		}
		
		if(3 == paginationValidation) {
			//return merchantRepository.findByPartnerCode(partnerCode);
			return merchantRepository.findByPartnerCodeAndProgramCodeIgnoreCase(partnerCode, program);
		}
		
		if(4 == paginationValidation) {
			Pageable pageNumberWithElements = PageRequest.of(page, limit);	
			//retrievedMerchants = merchantRepository.findByPartnerCodePagination(partnerCode, pageNumberWithElements);
			retrievedMerchants = merchantRepository.findByPartnerCodeAndProgramCodeIgnoreCasePagination(partnerCode, program, pageNumberWithElements);
			merchants = retrievedMerchants.getContent();
			totalElements = retrievedMerchants.getTotalElements();
			merchantResultResponse.setTotalRecords(totalElements);
			return merchants;
		}		
		
		return Collections.emptyList();
	}
	
	public Merchant listSpecificMerchantsPagination(Integer page, Integer limit, String channelId, String merchantCode,
			ListMerchantsStoreOfferResponse merchantStoreOfferResultResponse, String program) {

		Integer paginationValidation = Utils.validatePagination(page, limit);
		long totalElements = 0;
		
		if(1 == paginationValidation) {
			merchantStoreOfferResultResponse.addErrorAPIResponse(MerchantCodes.PAGINATION_LIMIT_EMPTY.getIntId(),
					MerchantCodes.PAGINATION_LIMIT_EMPTY.getMsg());
			merchantStoreOfferResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
			return null;
		}
		
		if(2 == paginationValidation) {
			merchantStoreOfferResultResponse.addErrorAPIResponse(MerchantCodes.PAGINATION_PAGE_EMPTY.getIntId(),
					MerchantCodes.PAGINATION_PAGE_EMPTY.getMsg());
			merchantStoreOfferResultResponse.setResult(MerchantCodes.LISTING_MERCHANTS_FAILED.getId(),
					MerchantCodes.LISTING_MERCHANTS_FAILED.getMsg());
			return null;
		}
		
		if(3 == paginationValidation) {
			if (!StringUtils.isEmpty(channelId) && channelId.equalsIgnoreCase(MerchantConstants.CHANNELID_ADMIN.get())) {
				//return merchantRepository.findByMerchantCode(merchantCode);
				return merchantRepository.findByMerchantCodeAndProgramCodeIgnoreCase(merchantCode, program);
			} else {
//				return merchantRepository.findByMerchantCodeAndStatus(merchantCode,
//						MerchantConstants.MERCHANT_ACTIVE_STATUS.get());
				return merchantRepository.findByMerchantCodeAndStatusAndProgramCodeIgnoreCase(merchantCode,
						MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program);
			}
		}
		
		Page<Merchant> merchantRetrieved;
		if(4 == paginationValidation) {
			Pageable pageNumberWithElements = PageRequest.of(page, limit);	
			if (!StringUtils.isEmpty(channelId) && channelId.equalsIgnoreCase(MerchantConstants.CHANNELID_ADMIN.get())) {
				//merchantRetrieved = merchantRepository.findByMerchantCodePagination(merchantCode, pageNumberWithElements);
				merchantRetrieved = merchantRepository.findByMerchantCodeAndProgramCodeIgnoreCasePagination(merchantCode, program, pageNumberWithElements);
				totalElements = merchantRetrieved.getTotalElements();
				merchantStoreOfferResultResponse.setTotalRecords(totalElements);
			} else {
				//merchantRetrieved =  merchantRepository.findByMerchantCodeAndStatusPagination(merchantCode, MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), pageNumberWithElements);
				merchantRetrieved = merchantRepository.findByMerchantCodeAndStatusAndProgramCodeIgnoreCasePagination(merchantCode,
						MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
				totalElements = merchantRetrieved.getTotalElements();
				merchantStoreOfferResultResponse.setTotalRecords(totalElements);
			}
			
			if(!merchantRetrieved.getContent().isEmpty()) {
				return merchantRetrieved.getContent().get(0);
			} else {
				return null;
			}
		}		
		
		return null;
	}
		
	@GetMapping(value = MerchantRequestMappingConstants.LIST_RATETYPES, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RateType> listRateTypes(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
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
		List<RateType> listRateType = new ArrayList<>();
		try {
			
			if(null == program) program = defaultProgramCode;
			
			//listRateType = rateTypeRepository.findAll();
			listRateType = rateTypeRepository.findByProgramIgnoreCase(program);
			return listRateType;
		} catch (Exception e) {
			return listRateType;
		}
	}

	@PostMapping(value = MerchantRequestMappingConstants.RESSET_RATETYPE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void resetRateTypes() {
		rateTypeRepository.deleteAll();
	}

	@PostMapping(value = MerchantRequestMappingConstants.RESSET_BARCODE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void resetBarcodes() {
		barcodeRepository.deleteAll();

	}
	
	@GetMapping(value = "merchant/contactPersons", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ContactPerson> getContactPersons() {
		List<Merchant> merchants = merchantRepository.findAll();
		List<ContactPerson> contactpersons = new  ArrayList<>();
		for(final Merchant merchant :merchants ) {
			for(ContactPerson contactPerson : merchant.getContactPersons() ) {
				contactpersons.add(contactPerson);
			}
		}
		return contactpersons;
	}
	
	@PostMapping(value = "/keycloak/createMerchantContact", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse createContactPerson(
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestBody List<ContactPerson> contactPersons) {
		ResultResponse resultResponse = new ResultResponse(null);
		LOG.info("started migrating merchant usernames to RHSSO");
		List<MerchantContactPersonDomain> contactPersonDomainToSave = new ArrayList<>();
		for(final ContactPerson contactPerson :contactPersons ) {
			MerchantContactPersonDomain storeContactPersonDomain = new MerchantContactPersonDomain.ContactPersonBuilder(contactPerson.getEmailId(), contactPerson.getMobileNumber(), contactPerson.getFirstName(), contactPerson.getLastName()).password("Welcome@1111").userName(contactPerson.getUserName()).build();
			contactPersonDomainToSave.add(storeContactPersonDomain);
			}
		userManagementService.saveMerchant(contactPersonDomainToSave,false,resultResponse, token, program);
	return resultResponse;
	}

	private void createInvalidContactPersonResponse(ResultResponse resultResponse,
			List<ContactPersonDto> invalidContactPersons) {
		if (!invalidContactPersons.isEmpty()) {
			for (final ContactPersonDto contactPersonDto : invalidContactPersons) {
				resultResponse.addErrorAPIResponse(MerchantCodes.CONTACT_PERSON_EXISTS.getIntId(),
						contactPersonDto.getEmailId() + ":" + MerchantCodes.CONTACT_PERSON_EXISTS.getMsg());
			}
		}
	}

	@GetMapping(value = MerchantRequestMappingConstants.GET_MERCHANT_NAME, consumes = MediaType.ALL_VALUE)
	public GetMerchantNameResponse getMerchantName(
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
			@PathVariable(value = MerchantRequestMappingConstants.MERCHANT_CODE, required = true) String merchantCode) {

		LOG.info("Entering getMerchantName in MerchantController. Request Parameters: {}", merchantCode);

		GetMerchantNameResponse getMerchantNameResponse = new GetMerchantNameResponse(externalTransactionId);

		try {

			Merchant merchant = merchantRepository.findByMerchantCode(merchantCode);
			
			if(null == merchant) {
				getMerchantNameResponse.addErrorAPIResponse(MerchantCodes.NO_MERCHANT_FOUND.getIntId(),
						MerchantCodes.NO_MERCHANT_FOUND.getMsg() + merchantCode);
				getMerchantNameResponse.setResult(MerchantCodes.GET_MERCHANT_NAME_FAILED.getId(),
						MerchantCodes.GET_MERCHANT_NAME_FAILED.getMsg());
				LOG.info("Exiting getMerchantName in MerchantController.");
				return getMerchantNameResponse;
			}
			
			if(null == merchant.getMerchantName()) {
				getMerchantNameResponse.addErrorAPIResponse(MerchantCodes.NO_MERCHANT_NAME_POPULATED.getIntId(),
						MerchantCodes.NO_MERCHANT_NAME_POPULATED.getMsg() + merchantCode);
				getMerchantNameResponse.setResult(MerchantCodes.GET_MERCHANT_NAME_FAILED.getId(),
						MerchantCodes.GET_MERCHANT_NAME_FAILED.getMsg());
				LOG.info("Exiting getMerchantName in MerchantController.");
				return getMerchantNameResponse;
			}
			
			GetMerchantNameResult getMerchantNameResult = new GetMerchantNameResult();
			getMerchantNameResult.setMerchantCode(merchantCode);
			getMerchantNameResult.setMerchantNameEn(merchant.getMerchantName().getMerchantNameEn());
			getMerchantNameResult.setMerchantNameAr(merchant.getMerchantName().getMerchantNameAr());
			getMerchantNameResponse.setMerchantName(getMerchantNameResult);
			getMerchantNameResponse.setResult(MerchantCodes.GET_MERCHANT_NAME_SUCCESS.getId(),
					MerchantCodes.GET_MERCHANT_NAME_SUCCESS.getMsg());

		} catch (Exception e) {

			getMerchantNameResponse.addErrorAPIResponse(MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MerchantCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			getMerchantNameResponse.setResult(MerchantCodes.GET_MERCHANT_NAME_FAILED.getId(),
					MerchantCodes.GET_MERCHANT_NAME_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "getMerchantName",
					e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}

		LOG.info("Exiting getMerchantName in MerchantController.");
		
		return getMerchantNameResponse;

	}
	
}
