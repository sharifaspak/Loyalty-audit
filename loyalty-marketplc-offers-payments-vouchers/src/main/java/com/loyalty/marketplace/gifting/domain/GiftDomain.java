package com.loyalty.marketplace.gifting.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.helper.GiftingControllerHelper;
import com.loyalty.marketplace.gifting.inbound.dto.GiftConfigureRequestDto;
import com.loyalty.marketplace.gifting.inbound.dto.GiftUpdateRequestDto;
import com.loyalty.marketplace.gifting.inbound.dto.OfferValueDto;
import com.loyalty.marketplace.gifting.inbound.dto.PremiumVoucherRequest;
import com.loyalty.marketplace.gifting.outbound.database.entity.Gifts;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftRepository;
import com.loyalty.marketplace.gifting.outbound.dto.GiftResponseDto;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftConfigurationResponse;
import com.loyalty.marketplace.gifting.outbound.dto.OfferValueResponseDto;
import com.loyalty.marketplace.gifting.utils.GetValues;
import com.loyalty.marketplace.gifting.utils.GiftingResponses;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MarketplaceValidator;
import com.mongodb.MongoException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class GiftDomain {

	private static final Logger LOG = LoggerFactory.getLogger(GiftDomain.class);
	private static final String DELETE_GIFT_METHOD ="deleteGift";
	private static final String EXCEPTION_LOG_MESSAGE = "Refer ExceptionLog collection with id : {}";
	private static final String GIFTING_VOUCHER_ERROR = "Error occured while gifting voucher : {}";
	private static final String CONFIGURING_GIFT_ERROR = "Error occured while configuring gift : {} ";
	private static final String DELETING_GIFT_ERROR = "Error occured while deleting gift : {}";
	
	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;

	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	GiftingControllerHelper helper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	GiftRepository giftRepository;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	MongoOperations mongoOperations;
	
	@Value("${premium.voucher.gift.type}")
	private String premiumGiftType;
	
	private String id;
	private String programCode;
	private String giftType;
	private GiftValuesDomain giftDetails;
	private List<OfferGiftValuesDomain> offerValues;
	private Boolean isActive;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;
	
	public GiftDomain(GiftBuilder gift) {
		
		this.id = gift.id;
		this.programCode = gift.programCode;
		this.giftType = gift.giftType;
		this.giftDetails = gift.giftDetails;
		this.offerValues = gift.offerValues;
		this.isActive = gift.isActive;
		this.createdUser = gift.createdUser;
		this.createdDate = gift.createdDate;
		this.updatedUser = gift.updatedUser;
		this.updatedDate = gift.updatedDate;

	}
	
	public static class GiftBuilder {
		
		private String id;
		private String programCode;
		private String giftType;
		private GiftValuesDomain giftDetails;
		private List<OfferGiftValuesDomain> offerValues;
		private Boolean isActive;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;
				
		public GiftBuilder(String id) {
			this.id = id;
		}
		
		public GiftBuilder(String programCode, String giftType) {

			this.programCode = programCode;
			this.giftType = giftType;
			
		}

		public GiftBuilder id(String id) {
			this.id = id;
			return this;
		}

		public GiftBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public GiftBuilder giftType(String giftType) {
			this.giftType = giftType;
			return this;
		}
		
		public GiftBuilder giftDetails(GiftValuesDomain giftDetails) {
			this.giftDetails = giftDetails;
			return this;
		}
		
		public GiftBuilder offerValues(List<OfferGiftValuesDomain> offerValues) {
			this.offerValues = offerValues;
			return this;
		}
		
		public GiftBuilder isActive(Boolean isActive) {
			this.isActive = isActive;
			return this;
		}
		
		public GiftBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public GiftBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public GiftBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public GiftBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public GiftDomain build() {
			return new GiftDomain(this);
		}
	
	}

	/***
	 * 
	 * @param premiumVoucherDto
	 * @param headers
	 */
	public ResultResponse giftPremiumVoucher(PremiumVoucherRequest premiumVoucherDto, Headers headers) {
		
		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
		
		try {
			
			if(MarketplaceValidator.validateDto(premiumVoucherDto, validator, resultResponse)) {
				
				Gifts gift = giftRepository.findPremiumGift(premiumGiftType, premiumVoucherDto.getPointsValue(), premiumVoucherDto.getPointsValue(), true);
				
				if(!ObjectUtils.isEmpty(gift)
			    && !CollectionUtils.isEmpty(gift.getOfferValues())) {
					
					LOG.info("Gift retrieved");
					for(OfferGiftValues offerQuantity : gift.getOfferValues()) {
						
						helper.purchaseVoucher(offerQuantity, premiumVoucherDto, headers, resultResponse);
					}
					
				}
			}
			
		}  catch(Exception e) {
			
			LOG.error(GIFTING_VOUCHER_ERROR, e.getMessage());
			LOG.info(EXCEPTION_LOG_MESSAGE, exceptionLogService.saveExceptionsToExceptionLogs(e, 
					headers.getExternalTransactionId(),
					premiumVoucherDto.getAccountNumber(),
					headers.getUserName()));
			
		}
		
		return resultResponse;
		
	}

	/***
	 * 
	 * @param giftRequestDto
	 * @param headers
	 * @return
	 */
	public ResultResponse validateAndConfigureGift(GiftConfigureRequestDto giftRequestDto, Headers headers) {
		
		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
		
		try {
			
			if(MarketplaceValidator.validateDto(giftRequestDto, validator, resultResponse)
			&& helper.validateGiftConfigurationRequest(giftRequestDto, headers, resultResponse)) {
				
				Gifts savedGift = giftRepository.save(modelMapper.map(GetValues.getGiftDomain(giftRequestDto, headers), Gifts.class));
				
				if(!ObjectUtils.isEmpty(savedGift)) {
					
					id = savedGift.getId();
				}
			}
			
		}  catch(Exception e) {
			
			LOG.error(CONFIGURING_GIFT_ERROR, e.getMessage());
			LOG.info(EXCEPTION_LOG_MESSAGE, exceptionLogService.saveExceptionsToExceptionLogs(e, 
					headers.getExternalTransactionId(),
					null,
					headers.getUserName()));
			
		}
		
		GiftingResponses.setResultWithMessage(resultResponse, GiftingCodes.GIFT_CONFIGURED_SUCCESSFULLY, GiftingCodes.GIFT_CONFIGURATION_FAILED, id, null);
		return resultResponse;
	}
	
	/***
	 * 
	 * @param giftRequestDto
	 * @param headers
	 * @return
	 */
	public ResultResponse validateAndUpdateGift(String id, GiftUpdateRequestDto giftRequestDto, Headers headers) {
		
		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
		
		try {
			
			if(MarketplaceValidator.validateDto(giftRequestDto, validator, resultResponse)
			&& helper.validateOfferValues(giftRequestDto.getOfferValues(), resultResponse)) {
				
				Gifts gift = giftRepository.getGiftByIdAndStatus(id, true);
				
				if(ObjectUtils.isEmpty(gift)) {
					
					resultResponse.addErrorAPIResponse(GiftingCodes.GIFT_NOT_FOUND.getIntId(), GiftingCodes.GIFT_NOT_FOUND.getMsg());
				
				} else {
					
					Gson gson = new Gson();
					Gifts originalGift = gson.fromJson(gson.toJson(gift), Gifts.class);
					updateOfferValues(gift, giftRequestDto, resultResponse);
					
					if(Checks.checkNoErrors(resultResponse)) {
						LOG.info("Gift : {}", gift);
						giftRepository.save(gift);
						auditService.updateDataAudit(GiftingConfigurationConstants.UPDATE_GIFT, gift, OffersRequestMappingConstants.UPDATE_OFFER, originalGift, headers.getExternalTransactionId(), headers.getUserName());
					}
					
				}
			
			}
			
		}  catch(Exception e) {
			
			LOG.error(CONFIGURING_GIFT_ERROR, e.getMessage());
			LOG.info(EXCEPTION_LOG_MESSAGE, exceptionLogService.saveExceptionsToExceptionLogs(e, 
					headers.getExternalTransactionId(),
					null,
					headers.getUserName()));
			
		}
		GiftingResponses.setResponse(resultResponse, GiftingCodes.GIFT_UPDATION_FAILED, GiftingCodes.GIFT_UPDATED_SUCCESSFULLY);
		return resultResponse;
	}

	private void updateOfferValues(Gifts gift, GiftUpdateRequestDto giftRequestDto, ResultResponse resultResponse) {
		
		if(!CollectionUtils.isEmpty(giftRequestDto.getOfferValues())) {
			
			List<OfferGiftValues> offerValueList = new CopyOnWriteArrayList<>();
			
			if(!CollectionUtils.isEmpty(gift.getOfferValues())) {
				
				offerValueList.addAll(gift.getOfferValues());
			}
			
			for(OfferValueDto offerValue  : giftRequestDto.getOfferValues()) {
				
				boolean offerExists = !CollectionUtils.isEmpty(gift.getOfferValues())
					&& gift.getOfferValues().stream().anyMatch(o->o.getOfferId().equals(offerValue.getOfferId()));
				LOG.info("OfferId {} already exists in gift : {}", offerValue.getOfferId(), offerExists);
				switch(offerValue.getAction()) {
				
					case GiftingConfigurationConstants.INSERT :
									LOG.info("Adding offer to gift");
									insertNewOfferToGift(offerValue, offerExists, gift, resultResponse);
					       			break;
			
					case GiftingConfigurationConstants.UPDATE : 
									LOG.info("Updating offer in gift");	
						            updateExistingOfferInGift(offerValue, offerExists, gift, resultResponse);
						     		break;
					
					case GiftingConfigurationConstants.DELETE :
									LOG.info("Deleting offer from gift");	
									deleteExistingOfferInGift(offerValue, offerExists, gift, offerValueList, resultResponse);
	       						    gift.setOfferValues(offerValueList);
									break;
	       			
					default :	 resultResponse.addErrorAPIResponse(GiftingCodes.NOT_A_VALID_ACTION.getIntId(), GiftingCodes.NOT_A_VALID_ACTION.getMsg()+ offerValue.getOfferId());		
				}
				
			}
			
						
		}
		
	}

	private void deleteExistingOfferInGift(OfferValueDto offerValue, boolean offerExists, Gifts gift,
			List<OfferGiftValues> offerValueList, ResultResponse resultResponse) {
		
		if(offerExists) {
			
			Iterator<OfferGiftValues> iterator = offerValueList.iterator();
			while (iterator.hasNext()) {
				
				OfferGiftValues offerVal = iterator.next();
				if(offerVal.getOfferId().equals(offerValue.getOfferId())) {
					offerValueList.remove(offerVal);
				}
			}
			
		} else {
			
			resultResponse.addErrorAPIResponse(GiftingCodes.OFFER_DOES_NOT_EXIST_IN_GIFT.getIntId(), GiftingCodes.OFFER_DOES_NOT_EXIST_IN_GIFT.getMsg()+" "+offerValue.getOfferId());
		}
		
		
	}

	private void updateExistingOfferInGift(OfferValueDto offerValue, boolean offerExists, 
			Gifts gift, ResultResponse resultResponse) {
		
		if(offerExists) {
			
			gift.getOfferValues().forEach(o->{
				
				if(o.getOfferId().equals(offerValue.getOfferId())) {
					
					o.setOfferType(offerValue.getOfferType());
					o.setCouponQuantity(offerValue.getCouponQuantity());
					
					if(Checks.checkIsDealVoucher(offerValue.getOfferType())) {
						o.setSubOfferId(offerValue.getSubOfferId());
					}
					
					if(Checks.checkIsCashVoucher(offerValue.getOfferType())) {
						o.setDenomination(offerValue.getDenomination());
					}
					
				}
				
			});
			
		} else {
			
			resultResponse.addErrorAPIResponse(GiftingCodes.OFFER_DOES_NOT_EXIST_IN_GIFT.getIntId(), GiftingCodes.OFFER_DOES_NOT_EXIST_IN_GIFT.getMsg()+" "+offerValue.getOfferId());
		}
		
	}

	private void insertNewOfferToGift(OfferValueDto offerValue, boolean offerExists,
			Gifts gift, ResultResponse resultResponse) {
		
		if(!offerExists) {
			
			if(CollectionUtils.isEmpty(gift.getOfferValues())) {
				
				gift.setOfferValues(new ArrayList<>(1));
			}
			
			gift.getOfferValues().add(new OfferGiftValues(offerValue.getOfferId(),
					offerValue.getOfferType(), 
					offerValue.getSubOfferId(),
					offerValue.getDenomination(), 
					offerValue.getCouponQuantity()));
		} else {
			
			resultResponse.addErrorAPIResponse(GiftingCodes.OFFER_ALREADY_EXISTS_IN_GIFT.getIntId(), GiftingCodes.OFFER_ALREADY_EXISTS_IN_GIFT.getMsg()+" "+offerValue.getOfferId());
			
		}
		
	}

	public ResultResponse validateAndDeleteGift(String id, Headers headers) {
		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
		
		try {
			Gifts giftToDelete = giftRepository.getGiftByIdAndStatus(id, true);
			
			if(!ObjectUtils.isEmpty(giftToDelete)) {
				deleteGift(id, giftToDelete, headers);
				resultResponse.setResult(GiftingCodes.GIFT_DELETED_SUCCESSFULLY.getId(),GiftingCodes.GIFT_DELETED_SUCCESSFULLY.getMsg());
				resultResponse.setSuccessAPIResponse();
			} else {
				resultResponse.addErrorAPIResponse(GiftingCodes.GIFT_NOT_FOUND.getIntId(), GiftingCodes.GIFT_NOT_FOUND.getMsg());
			}
			
			
		}  catch(Exception e) {
			
			LOG.error(DELETING_GIFT_ERROR, e.getMessage());
			LOG.info(EXCEPTION_LOG_MESSAGE, exceptionLogService.saveExceptionsToExceptionLogs(e, 
					headers.getExternalTransactionId(),
					null,
					headers.getUserName()));
			
		}
		GiftingResponses.setResponse(resultResponse, GiftingCodes.GIFT_DELETION_FAILED, GiftingCodes.GIFT_DELETED_SUCCESSFULLY);
		return resultResponse;
	}
	
	private void deleteGift(String id, Gifts giftToDelete, Headers headers) throws MarketplaceException {
		
		try {
			giftToDelete.setIsActive(false);
			giftRepository.save(giftToDelete);
//			giftRepository.deleteById(id);
			auditService.deleteDataAudit(GiftingConfigurationConstants.GIFTS, giftToDelete, GiftingConfigurationConstants.DELETE_GIFT, headers.getExternalTransactionId(), headers.getUserName());			
		} catch (MongoException mongoException) {
			
			throw new MarketplaceException(this.getClass().toString(), DELETE_GIFT_METHOD,
					mongoException.getClass() + mongoException.getMessage(), GiftingCodes.MONGO_ERROR);
		
		}  catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), DELETE_GIFT_METHOD,
					e.getClass() + e.getMessage(),  GiftingCodes.RUNTIME_EXCEPTION);
		
		}
	}

	
	
	public ListGiftConfigurationResponse listGiftConfigurationDetail(String id, Headers headers) {
		
		ListGiftConfigurationResponse listGiftConfigurationResponse = new ListGiftConfigurationResponse(headers.getExternalTransactionId());

		try {
			Gifts gift = giftRepository.getGiftByIdAndStatus(id, true);

			if (ObjectUtils.isEmpty(gift)) {
				listGiftConfigurationResponse.addErrorAPIResponse(
						GiftingCodes.GIFTING_CONFIGURATION_NOT_AVAILABLE.getIntId(),
						GiftingCodes.GIFTING_CONFIGURATION_NOT_AVAILABLE.getMsg());
				listGiftConfigurationResponse.setResult(GiftingCodes.GIFTING_CONFIGURATION_LIST_FAILURE.getId(),
						GiftingCodes.GIFTING_CONFIGURATION_LIST_FAILURE.getMsg());
				return listGiftConfigurationResponse;
			}

			List<GiftResponseDto> giftResponse = new ArrayList<>();
			GiftResponseDto giftResponseDto = modelMapper.map(gift, GiftResponseDto.class);
			giftResponseDto = populateGiftDetails(gift, giftResponseDto);
			giftResponse.add(giftResponseDto);
			listGiftConfigurationResponse.setGiftConfigurationResponse(giftResponse);
			
			listGiftConfigurationResponse.setResult(GiftingCodes.GIFTING_CONFIGURATION_LIST_SUCCESS.getId(),
					GiftingCodes.GIFTING_CONFIGURATION_LIST_SUCCESS.getMsg());
			

		} catch(Exception e) {
			
			LOG.error(CONFIGURING_GIFT_ERROR, e.getMessage());
			LOG.info(EXCEPTION_LOG_MESSAGE, exceptionLogService.saveExceptionsToExceptionLogs(e, 
					headers.getExternalTransactionId(),
					null,
					headers.getUserName()));
			
		}
		
		return listGiftConfigurationResponse;
	}
	
	public ListGiftConfigurationResponse listGiftConfiguration(String giftType, Headers headers) {
		
		ListGiftConfigurationResponse listGiftConfigurationResponse = new ListGiftConfigurationResponse(headers.getExternalTransactionId());
		
		try {
			List<Gifts> gifts = null;
			if(!StringUtils.isEmpty(giftType)) {
				gifts = giftRepository.getGiftByGiftTypeAndStatus(giftType, true);
			} else {
				gifts = giftRepository.getGiftByStatus(true);
			}

			List<GiftResponseDto> giftResponse = new ArrayList<>();
		
			if(!ObjectUtils.isEmpty(gifts)) {
				
				for(Gifts gift : gifts) {
					GiftResponseDto giftResponseDto = new GiftResponseDto();
					giftResponseDto = populateGiftDetails(gift, giftResponseDto);
					giftResponse.add(giftResponseDto);
				}
				
				listGiftConfigurationResponse.setResult(GiftingCodes.GIFTING_CONFIGURATION_LIST_SUCCESS.getId(),
						GiftingCodes.GIFTING_CONFIGURATION_LIST_SUCCESS.getMsg());
				listGiftConfigurationResponse.setGiftConfigurationResponse(giftResponse);
				
			} else {
				
				listGiftConfigurationResponse.addErrorAPIResponse(
						GiftingCodes.GIFTING_CONFIGURATION_NOT_AVAILABLE.getIntId(),
						GiftingCodes.GIFTING_CONFIGURATION_NOT_AVAILABLE.getMsg());
				listGiftConfigurationResponse.setResult(GiftingCodes.GIFTING_CONFIGURATION_LIST_FAILURE.getId(),
						GiftingCodes.GIFTING_CONFIGURATION_LIST_FAILURE.getMsg());
				
			}
			
		} catch(Exception e) {
			
			LOG.error(CONFIGURING_GIFT_ERROR, e.getMessage());
			LOG.info(EXCEPTION_LOG_MESSAGE, exceptionLogService.saveExceptionsToExceptionLogs(e, 
					headers.getExternalTransactionId(),
					null,
					headers.getUserName()));
			
		}

		return listGiftConfigurationResponse;
	}
	
	public GiftResponseDto populateGiftDetails(Gifts gift, GiftResponseDto giftResponseDto) {
		if(!ObjectUtils.isEmpty(gift)) {
			giftResponseDto.setId(gift.getId());
			giftResponseDto.setGiftType(gift.getGiftType());
			
			if(!ObjectUtils.isEmpty(gift.getGiftDetails())) {
				giftResponseDto.setMaxPointValue(gift.getGiftDetails().getMaxPointValue());
				giftResponseDto.setMinPointValue(gift.getGiftDetails().getMinPointValue());
				giftResponseDto.setSubscriptionCatalogId(gift.getGiftDetails().getSubscriptionCatalogId());
				giftResponseDto.setPromotionalGiftId(gift.getGiftDetails().getPromotionalGiftId());
				giftResponseDto.setChannelId(gift.getGiftDetails().getChannelId());
			}
			
			List<OfferValueResponseDto> offerValues = new ArrayList<OfferValueResponseDto>();
			if(!ObjectUtils.isEmpty(gift.getOfferValues())) {
				for(OfferGiftValues offerGiftValues: gift.getOfferValues()) {
					OfferValueResponseDto offerValueResponseDto = new OfferValueResponseDto();
					offerValueResponseDto.setOfferId(offerGiftValues.getOfferId());
					offerValueResponseDto.setSubOfferId(offerGiftValues.getSubOfferId());
					offerValueResponseDto.setOfferType(offerGiftValues.getOfferType());
					offerValueResponseDto.setCouponQuantity(offerGiftValues.getCouponQuantity());
					if(!ObjectUtils.isEmpty(offerGiftValues.getDenomination())) {
						String denomination = Integer.toString(offerGiftValues.getDenomination());
						offerValueResponseDto.setDenomination(denomination);
					}
					offerValues.add(offerValueResponseDto);
				}
			}
			giftResponseDto.setOfferValues(offerValues);
		}
			
		return giftResponseDto;
	}
	
}


