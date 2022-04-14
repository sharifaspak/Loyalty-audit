package com.loyalty.marketplace.gifting.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.inbound.dto.GiftingLimitRequest;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingLimit;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingLimitRepository;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftingLimitsResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftingLimitsResult;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class GiftingLimitDomain {

	private static final Logger LOG = LoggerFactory.getLogger(GiftingLimitDomain.class);
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	@Autowired
	GiftingLimitRepository giftingLimitRepository;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;
	
	private String id;
	private String programCode;
	private String giftType;
	private Double fee;
	private LimitsDomain accountLimits;
	private LimitsDomain membershipLimits;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;
	
	public GiftingLimitDomain(GiftingLimitBuilder giftingLimitBuilder) {
		this.id = giftingLimitBuilder.id;
		this.programCode = giftingLimitBuilder.programCode;
		this.giftType = giftingLimitBuilder.giftType;
		this.fee = giftingLimitBuilder.fee;
		this.accountLimits = giftingLimitBuilder.accountLimits;
		this.membershipLimits = giftingLimitBuilder.membershipLimits;
		this.createdUser = giftingLimitBuilder.createdUser;
		this.createdDate = giftingLimitBuilder.createdDate;
		this.updatedUser = giftingLimitBuilder.updatedUser;
		this.updatedDate = giftingLimitBuilder.updatedDate;
	}

	public static class GiftingLimitBuilder {

		private String id;
		private String programCode;
		private String giftType;
		private Double fee;
		private LimitsDomain accountLimits;
		private LimitsDomain membershipLimits;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;

		public GiftingLimitBuilder(String giftType, Double fee) {
			super();
			this.giftType = giftType;
			this.fee = fee;
		}

		public GiftingLimitBuilder id(String id) {
			this.id = id;
			return this;
		}

		public GiftingLimitBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public GiftingLimitBuilder accountLimits(LimitsDomain accountLimits) {
			this.accountLimits = accountLimits;
			return this;
		}
		
		public GiftingLimitBuilder membershipLimits(LimitsDomain membershipLimits) {
			this.membershipLimits = membershipLimits;
			return this;
		}
		
		public GiftingLimitBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public GiftingLimitBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public GiftingLimitBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public GiftingLimitBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public GiftingLimitDomain build() {
			return new GiftingLimitDomain(this);
		}
		
	}
	
	/**
	 * This method is used to configure gifting limits static table.
	 * @param giftingLimitRequest
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse configureLimits(GiftingLimitRequest giftingLimitRequest, Headers headers, ResultResponse resultResponse) {
		
		try {

			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			
			LimitsDomain account = new LimitsDomain.LimitsBuilder()
					.senderDayLimit(giftingLimitRequest.getAccountLimits().getSenderDayLimit())
					.senderWeekLimit(giftingLimitRequest.getAccountLimits().getSenderWeekLimit())
					.senderMonthLimit(giftingLimitRequest.getAccountLimits().getSenderMonthLimit())
					.receiverDayLimit(giftingLimitRequest.getAccountLimits().getReceiverDayLimit())
					.receiverWeekLimit(giftingLimitRequest.getAccountLimits().getReceiverWeekLimit())
					.receiverMonthLimit(giftingLimitRequest.getAccountLimits().getReceiverMonthLimit()).build();

			LimitsDomain membership = new LimitsDomain.LimitsBuilder()
					.senderDayLimit(giftingLimitRequest.getMembershipLimits().getSenderDayLimit())
					.senderWeekLimit(giftingLimitRequest.getMembershipLimits().getSenderWeekLimit())
					.senderMonthLimit(giftingLimitRequest.getMembershipLimits().getSenderMonthLimit())
					.receiverDayLimit(giftingLimitRequest.getMembershipLimits().getReceiverDayLimit())
					.receiverWeekLimit(giftingLimitRequest.getMembershipLimits().getReceiverWeekLimit())
					.receiverMonthLimit(giftingLimitRequest.getMembershipLimits().getReceiverMonthLimit()).build();

			GiftingLimitDomain giftingLimitDomain = new GiftingLimitDomain.GiftingLimitBuilder(
					giftingLimitRequest.getGiftType(), giftingLimitRequest.getFee()).programCode(headers.getProgram())
							.accountLimits(account).membershipLimits(membership)
							.createdUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken())
							.createdDate(new Date()).build();
			
			saveUpdateGiftingLimits(giftingLimitDomain, GiftingConstants.ACTION_INSERT, headers, null,
					GiftingConfigurationConstants.GIFT_LIMIT);
		
			resultResponse.setResult(GiftingCodes.GIFTING_LIMITS_INSERT_SUCCESS.getId(),
					GiftingCodes.GIFTING_LIMITS_INSERT_SUCCESS.getMsg());
			
		} catch (MarketplaceException e) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_LIMITS_EXCEPTION.getIntId(),
					GiftingCodes.GIFTING_LIMITS_EXCEPTION.getMsg());
			resultResponse.setResult(GiftingCodes.GIFTING_LIMITS_INSERT_FAILURE.getId(),
					GiftingCodes.GIFTING_LIMITS_INSERT_FAILURE.getMsg());
			e.printStackTrace();
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(GiftingCodes.GIFTING_LIMITS_INSERT_FAILURE.getId(),
					GiftingCodes.GIFTING_LIMITS_INSERT_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), GiftingConfigurationConstants.GIFTING_LIMIT_CONFIGURE,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
		
	}
	
	/**
	 * This method is used to update GiftingLimits static collection.
	 * @param giftingLimitRequest
	 * @param headers
	 * @param id
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse updateLimits(GiftingLimitRequest giftingLimitRequest, Headers headers, String id, ResultResponse resultResponse) {
		
		try {
			
			Optional<GiftingLimit> giftingLimit = giftingLimitRepository.findById(id);
			
			if(!giftingLimit.isPresent()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_LIMITS_UPDATE_NOT_FOUND.getIntId(),
						GiftingCodes.GIFTING_LIMITS_UPDATE_NOT_FOUND.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_LIMITS_UPDATE_FAILURE.getId(),
						GiftingCodes.GIFTING_LIMITS_UPDATE_FAILURE.getMsg());
				return resultResponse;
			}  
			
			GiftingLimit existingLimit = giftingLimit.get();
			
			LimitsDomain account = updateAccountLimits(giftingLimitRequest, existingLimit);
			LimitsDomain membership = updateMembershipLimits(giftingLimitRequest, existingLimit);

			GiftingLimitDomain giftingLimitDomain = new GiftingLimitDomain.GiftingLimitBuilder(
					existingLimit.getGiftType(), null != giftingLimitRequest.getFee() ? giftingLimitRequest.getFee() : existingLimit.getFee())
					.id(id)
					.programCode(headers.getProgram())
					.accountLimits(account)
					.membershipLimits(membership)
					.createdUser(existingLimit.getCreatedUser())
					.createdDate(existingLimit.getCreatedDate())
					.updatedUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken())
					.updatedDate(new Date())
					.build();

			saveUpdateGiftingLimits(giftingLimitDomain, GiftingConstants.ACTION_UPDATE, headers, existingLimit,
					GiftingConfigurationConstants.UPDATE_GIFT_LIMIT);
		
			resultResponse.setResult(GiftingCodes.GIFTING_LIMITS_UPDATE_SUCCESS.getId(),
					GiftingCodes.GIFTING_LIMITS_UPDATE_SUCCESS.getMsg());
			
		} catch (MarketplaceException e) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_LIMITS_EXCEPTION.getIntId(),
					GiftingCodes.GIFTING_LIMITS_EXCEPTION.getMsg());
			resultResponse.setResult(GiftingCodes.GIFTING_LIMITS_UPDATE_FAILURE.getId(),
					GiftingCodes.GIFTING_LIMITS_UPDATE_FAILURE.getMsg());
			e.printStackTrace();
		}
		
		return resultResponse;
		
	}
	
	/**
	 * This method is used to create domain for updating account level gifting limits.
	 * @param giftingLimitRequest
	 * @param existingLimit
	 * @return
	 */
	private LimitsDomain updateAccountLimits(GiftingLimitRequest giftingLimitRequest, GiftingLimit existingLimit) {
		
		return new LimitsDomain.LimitsBuilder()
				.senderDayLimit(null != giftingLimitRequest.getAccountLimits().getSenderDayLimit() ? giftingLimitRequest.getAccountLimits().getSenderDayLimit() : existingLimit.getAccountLimits().getSenderDayLimit())
				.senderWeekLimit(null != giftingLimitRequest.getAccountLimits().getSenderWeekLimit() ? giftingLimitRequest.getAccountLimits().getSenderWeekLimit() : existingLimit.getAccountLimits().getSenderWeekLimit())
				.senderMonthLimit(null != giftingLimitRequest.getAccountLimits().getSenderMonthLimit() ? giftingLimitRequest.getAccountLimits().getSenderMonthLimit() : existingLimit.getAccountLimits().getSenderMonthLimit())
				.receiverDayLimit(null != giftingLimitRequest.getAccountLimits().getReceiverDayLimit() ? giftingLimitRequest.getAccountLimits().getReceiverDayLimit() : existingLimit.getAccountLimits().getReceiverDayLimit())
				.receiverWeekLimit(null != giftingLimitRequest.getAccountLimits().getReceiverWeekLimit() ? giftingLimitRequest.getAccountLimits().getReceiverWeekLimit() : existingLimit.getAccountLimits().getReceiverWeekLimit())
				.receiverMonthLimit(null != giftingLimitRequest.getAccountLimits().getReceiverMonthLimit() ? giftingLimitRequest.getAccountLimits().getReceiverMonthLimit() : existingLimit.getAccountLimits().getReceiverMonthLimit())
				.build();
		
	}
	
	/**
	 * This method is used to create domain for updating membership level gifting limits.
	 * @param giftingLimitRequest
	 * @param existingLimit
	 * @return
	 */
	private LimitsDomain updateMembershipLimits(GiftingLimitRequest giftingLimitRequest, GiftingLimit existingLimit) {
		
		return new LimitsDomain.LimitsBuilder()
				.senderDayLimit(null != giftingLimitRequest.getMembershipLimits().getSenderDayLimit() ? giftingLimitRequest.getMembershipLimits().getSenderDayLimit() : existingLimit.getMembershipLimits().getSenderDayLimit())
				.senderWeekLimit(null != giftingLimitRequest.getMembershipLimits().getSenderWeekLimit() ? giftingLimitRequest.getMembershipLimits().getSenderWeekLimit() : existingLimit.getMembershipLimits().getSenderWeekLimit())
				.senderMonthLimit(null != giftingLimitRequest.getMembershipLimits().getSenderMonthLimit() ? giftingLimitRequest.getMembershipLimits().getSenderMonthLimit() : existingLimit.getMembershipLimits().getSenderMonthLimit())
				.receiverDayLimit(null != giftingLimitRequest.getMembershipLimits().getReceiverDayLimit() ? giftingLimitRequest.getMembershipLimits().getReceiverDayLimit() : existingLimit.getMembershipLimits().getReceiverDayLimit())
				.receiverWeekLimit(null != giftingLimitRequest.getMembershipLimits().getReceiverWeekLimit() ? giftingLimitRequest.getMembershipLimits().getReceiverWeekLimit() : existingLimit.getMembershipLimits().getReceiverWeekLimit())
				.receiverMonthLimit(null != giftingLimitRequest.getMembershipLimits().getReceiverMonthLimit() ? giftingLimitRequest.getMembershipLimits().getReceiverMonthLimit() : existingLimit.getMembershipLimits().getReceiverMonthLimit())
				.build();
		
	}
	
	/**
	 * This method is used to list gifting limits.
	 * @param listGiftingLimitsResponse
	 * @return
	 */
	public ListGiftingLimitsResponse listLimits(ListGiftingLimitsResponse listGiftingLimitsResponse) {
		
		try {

			List<GiftingLimit> giftingLimits = giftingLimitRepository.findAll();
			
			if(giftingLimits.isEmpty()) {
				listGiftingLimitsResponse.addErrorAPIResponse(GiftingCodes.NO_GIFTING_LIMITS_FOUND.getIntId(),
						GiftingCodes.NO_GIFTING_LIMITS_FOUND.getMsg());
				listGiftingLimitsResponse.setResult(GiftingCodes.GIFTING_LIMITS_LIST_FAILURE.getId(),
						GiftingCodes.GIFTING_LIMITS_LIST_FAILURE.getMsg());
				return listGiftingLimitsResponse;
			}  
			
			List<ListGiftingLimitsResult> giftingLimitsResult = new ArrayList<>();
		
			for(GiftingLimit limit : giftingLimits) {
				ListGiftingLimitsResult giftingLimit = modelMapper.map(limit, ListGiftingLimitsResult.class);
				giftingLimitsResult.add(giftingLimit);
			}
			
			listGiftingLimitsResponse.setGiftingLimits(giftingLimitsResult);
			listGiftingLimitsResponse.setResult(GiftingCodes.GIFTING_LIMITS_LIST_SUCCESS.getId(),
					GiftingCodes.GIFTING_LIMITS_LIST_SUCCESS.getMsg());
			
		} catch (Exception e) {
			listGiftingLimitsResponse.addErrorAPIResponse(GiftingCodes.GIFTING_LIMITS_LIST_EXCEPTION.getIntId(),
					GiftingCodes.GIFTING_LIMITS_LIST_EXCEPTION.getMsg());
			listGiftingLimitsResponse.setResult(GiftingCodes.GIFTING_LIMITS_LIST_FAILURE.getId(),
					GiftingCodes.GIFTING_LIMITS_LIST_FAILURE.getMsg());
			e.printStackTrace();
		}
		
		return listGiftingLimitsResponse;
		
	}
	
	/**
	 * This method is used to save/update GiftingLimits collection.
	 * @param giftingLimitDomain
	 * @param action
	 * @param headers
	 * @param existingGiftingLimit
	 * @param api
	 * @return
	 * @throws MarketplaceException
	 */
	public GiftingLimit saveUpdateGiftingLimits(GiftingLimitDomain giftingLimitDomain, String action,
			Headers headers, GiftingLimit existingGiftingLimit, String api) throws MarketplaceException {

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.DOMAIN_TO_SAVE,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_LIMIT_DOMAIN_SAVE_DB,
				giftingLimitDomain);

		try {

			GiftingLimit giftingLimit = modelMapper.map(giftingLimitDomain, GiftingLimit.class);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.ENTITY_TO_SAVE,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_LIMIT_DOMAIN_SAVE_DB, giftingLimit);

			GiftingLimit savedGiftingLimit = giftingLimitRepository.save(giftingLimit);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_LIMIT_DOMAIN_SAVE_DB,
					savedGiftingLimit);

			if (action.equalsIgnoreCase(GiftingConstants.ACTION_INSERT)) {

//				auditService.insertDataAudit(GiftingConfigurationConstants.GIFTING_LIMIT, savedGiftingLimit, api,
//						headers.getExternalTransactionId(), headers.getUserName());

			} else if (action.equalsIgnoreCase(GiftingConstants.ACTION_UPDATE)) {

				auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_LIMIT, savedGiftingLimit, api,
						existingGiftingLimit, headers.getExternalTransactionId(), headers.getUserName());

			}

			return savedGiftingLimit;

		} catch (MongoWriteException mongoException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_LIMIT_DOMAIN_SAVE_DB,
					mongoException.getClass() + mongoException.getMessage(), GiftingCodes.GIFTING_LIMITS_CONFIG_FAILED);

		} catch (ValidationException validationException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_LIMIT_DOMAIN_SAVE_DB,
					validationException.getClass() + validationException.getMessage(),
					GiftingCodes.VALIDATION_EXCEPTION);

		} catch (Exception e) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_LIMIT_DOMAIN_SAVE_DB, e.getClass() + e.getMessage(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION);

		}

	}
	
}
