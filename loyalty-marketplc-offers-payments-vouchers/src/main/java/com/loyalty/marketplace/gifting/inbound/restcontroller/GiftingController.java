package com.loyalty.marketplace.gifting.inbound.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.domain.BirthdayReminderDomain;
import com.loyalty.marketplace.gifting.domain.GiftDomain;
import com.loyalty.marketplace.gifting.domain.GiftingHistoryDomain;
import com.loyalty.marketplace.gifting.domain.GiftingLimitDomain;
import com.loyalty.marketplace.gifting.domain.GoldCertificateDomain;
import com.loyalty.marketplace.gifting.helper.GiftingControllerHelper;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderDto;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderStatusDto;
import com.loyalty.marketplace.gifting.inbound.dto.CancelGiftRequest;
import com.loyalty.marketplace.gifting.inbound.dto.GiftConfigureRequestDto;
import com.loyalty.marketplace.gifting.inbound.dto.GiftUpdateRequestDto;
import com.loyalty.marketplace.gifting.inbound.dto.GiftingLimitRequest;
import com.loyalty.marketplace.gifting.inbound.dto.GiftingRequest;
import com.loyalty.marketplace.gifting.inbound.dto.PremiumVoucherRequest;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftConfigurationResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftingHistoryResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftingLimitsResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGoldTransactionResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListSpecificGiftingHistoryResponse;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.events.eventobject.AccountChangeEvent;
import com.loyalty.marketplace.outbound.events.helper.EventHelper;
import com.loyalty.marketplace.utils.Logs;

import io.swagger.annotations.Api;

@RestController
@Api(value = GiftingConfigurationConstants.MARKETPLACE)
@RequestMapping(GiftingConfigurationConstants.MARKETPLACE_BASE)
public class GiftingController {

	private static final Logger LOG = LoggerFactory.getLogger(GiftingController.class);

	@Autowired
	GoldCertificateDomain goldCertificateDomain;

	@Autowired
	BirthdayReminderDomain birthdayReminderDomain;

	@Autowired
	GiftingHistoryDomain giftingHistoryDomain;

	@Autowired
	GiftingLimitDomain giftingLimitDomain;

	@Autowired
	GiftingControllerHelper giftingControllerHelper;

	@Autowired
	EventHelper eventHelper;
	
	@Autowired
	GiftDomain giftDomain;
	
	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;

	/* GIFT POINTS & GOLD */

	/**
	 * This method is used to gift voucher, points or gold
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
	 * @param giftingRequest
	 * 
	 */
	@PostMapping(value = GiftingConfigurationConstants.GIFT, consumes = MediaType.ALL_VALUE)
	public ResultResponse marketplaceGifting(
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
			@RequestBody GiftingRequest giftingRequest) {

		LOG.info(
				GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_GIFTING, giftingRequest);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		if (null == giftingRequest.getGiftType()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GIFT_TYPE_FIELD_MANDATORY.getIntId(),
					GiftingCodes.GIFT_TYPE_FIELD_MANDATORY.getMsg());
			resultResponse.setResult(GiftingCodes.MARKETPLACE_GIFTING_FAILURE.getId(),
					GiftingCodes.MARKETPLACE_GIFTING_FAILURE.getMsg());
			return resultResponse;
		}

		switch (giftingRequest.getGiftType()) {

		case GiftingConstants.GIFT_TYPE_VOUCHER:
			giftingControllerHelper.giftVoucher(giftingRequest, headers, resultResponse);
			break;
		case GiftingConstants.GIFT_TYPE_POINTS:
			giftingControllerHelper.giftPoints(giftingRequest, headers, resultResponse);
			break;
		case GiftingConstants.GIFT_TYPE_GOLD:
			giftingControllerHelper.giftGold(giftingRequest, headers, resultResponse);
			break;

		default:
			resultResponse.addErrorAPIResponse(GiftingCodes.INVALID_GIFT_TYPE.getIntId(),
					GiftingCodes.INVALID_GIFT_TYPE.getMsg());
			resultResponse.setResult(GiftingCodes.MARKETPLACE_GIFTING_FAILURE.getId(),
					GiftingCodes.MARKETPLACE_GIFTING_FAILURE.getMsg());

		}

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_GIFTING, resultResponse);

		return resultResponse;

	}

	@PostMapping(value = GiftingConfigurationConstants.CANCEL_GIFT, consumes = MediaType.ALL_VALUE)
	public ResultResponse cancelVoucherGift(
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
			@RequestBody CancelGiftRequest cancelGiftRequest) {

		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CANCEL_GIFT,
				cancelGiftRequest);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;

		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		giftingControllerHelper.cancelVoucherGift(cancelGiftRequest, headers, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CANCEL_GIFT, resultResponse);

		return resultResponse;

	}

	/**
	 * This method is used to send gifting that are scheduled for a future date.
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
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.SEND_GIFT, consumes = MediaType.ALL_VALUE)
	public ResultResponse sendScheduledGift(
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

		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_SCHEDULED_GIFT);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		giftingControllerHelper.sendScheduledGift(headers, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_SCHEDULED_GIFT, resultResponse);

		return resultResponse;

	}

	/**
	 * This is an adhoc API to send a scheduled gift to receiver account manually
	 * for a single gifting history id in case the /sendGift batch API fails.
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
	 * @param id
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.SEND_GIFT_ADHOC, consumes = MediaType.ALL_VALUE)
	public ResultResponse sendScheduledGiftAdhoc(
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
			@PathVariable(value = GiftingConfigurationConstants.SEND_GIFT_ADHOC_GIFT_ID, required = true) String id) {

		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_SCHEDULED_GIFT_ADHOC);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		giftingControllerHelper.sendScheduledGiftAdhoc(id, headers, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_SCHEDULED_GIFT_ADHOC, resultResponse);

		return resultResponse;

	}

	/**
	 * This method is used to reset GiftingCounters daily, weekly or monthly. This
	 * API can be used in case the cron job fails.
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
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.RESET_COUNTER, consumes = MediaType.ALL_VALUE)
	public ResultResponse resetGiftingCounters(
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

		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_RESET_COUNTER);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		giftingControllerHelper.resetGiftingCountersCron(headers, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_RESET_COUNTER, resultResponse);

		return resultResponse;

	}

	/**
	 * This method is used to list gifting history for Points, Gold or Voucher.
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
	 * @param accountNumber
	 * @param giftType
	 * @param filter
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.LIST_GIFT_HISTORY, consumes = MediaType.ALL_VALUE)
	public ListGiftingHistoryResponse listGiftingHistory(
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
			@PathVariable(value = GiftingConfigurationConstants.ACCOUNT_NUMBER, required = false) String accountNumber,
			@RequestHeader(value = GiftingConfigurationConstants.GIFT_TYPE, required = false) String giftType,
			@RequestHeader(value = GiftingConfigurationConstants.FILTER, required = false) String filter) {

		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_GIFTING_HISTORY);

		ListGiftingHistoryResponse listGiftingHistoryResponse = new ListGiftingHistoryResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		giftingHistoryDomain.listGiftingHistory(accountNumber, giftType, filter, headers, listGiftingHistoryResponse);

		LOG.info(GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_GIFTING_HISTORY);

		return listGiftingHistoryResponse;

	}

	/**
	 * This method lists the details of a specific gifting transaction.
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
	 * @param giftId
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.GIFT, consumes = MediaType.ALL_VALUE)
	public ListSpecificGiftingHistoryResponse listSpecificGiftingHistory(
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
			@RequestParam(value = GiftingConfigurationConstants.GIFT_ID, required = false) String giftId) {

		LOG.info(
				GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_SPECIFIC_GIFTING_HISTORY, giftId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		ListSpecificGiftingHistoryResponse listSpecificGiftingHistoryResponse = new ListSpecificGiftingHistoryResponse(
				externalTransactionId);

		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		giftingControllerHelper.listSpecificGiftingHistory(giftId, headers, listSpecificGiftingHistoryResponse);

		LOG.info(GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_SPECIFIC_GIFTING_HISTORY);

		return listSpecificGiftingHistoryResponse;

	}

	/* CONFIGURE & LIST GIFTING LIMITS */

	/**
	 * This method is used to populate GiftingLimits static collection.
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
	 * @param giftingLimitRequest
	 * @return
	 */
	@PostMapping(value = GiftingConfigurationConstants.GIFT_LIMIT, consumes = MediaType.ALL_VALUE)
	public ResultResponse configureGiftingLimits(
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
			@RequestBody GiftingLimitRequest giftingLimitRequest) {

		LOG.info(
				GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CONFIGURE_LIMITS, giftingLimitRequest);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		giftingLimitDomain.configureLimits(giftingLimitRequest, headers, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CONFIGURE_LIMITS, resultResponse);

		return resultResponse;

	}

	/**
	 * This method is used to update GiftingLimits static collection.
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
	 * @param id
	 * @param giftingLimitRequest
	 * @return
	 */
	@PostMapping(value = GiftingConfigurationConstants.UPDATE_GIFT_LIMIT, consumes = MediaType.ALL_VALUE)
	public ResultResponse updateGiftingLimits(
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
			@PathVariable(value = GiftingConfigurationConstants.GIFTING_LIMIT_ID, required = true) String id,
			@RequestBody GiftingLimitRequest giftingLimitRequest) {

		LOG.info(
				GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_UPDATE_LIMITS, giftingLimitRequest);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		giftingLimitDomain.updateLimits(giftingLimitRequest, headers, id, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_UPDATE_LIMITS, resultResponse);

		return resultResponse;

	}

	/**
	 * This method is used to list configured gifting limits.
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
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.GIFT_LIMIT, consumes = MediaType.ALL_VALUE)
	public ListGiftingLimitsResponse listGiftingLimits(
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

		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_LIMITS);

		ListGiftingLimitsResponse listGiftingLimitsResponse = new ListGiftingLimitsResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		giftingLimitDomain.listLimits(listGiftingLimitsResponse);

		LOG.info(GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_LIMITS);

		return listGiftingLimitsResponse;

	}

//	@PostMapping(value = GiftingConfigurationConstants.PROMOTIONAL_GIFT, consumes = MediaType.ALL_VALUE)
//	public ResultResponse sendPromotionalGift(
//			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
//			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
//			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
//			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
//			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
//			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
//			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
//			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
//			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
//			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
//			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
//			@RequestBody CancelGiftRequest cancelGiftRequest) {
//
//		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.REQUEST_PARAMS,
//				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CANCEL_GIFT,
//				cancelGiftRequest);
//
//		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
//				channelId, systemId, systemPassword, token, transactionId);
//
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		//giftingControllerHelper.cancelVoucherGift(cancelGiftRequest, headers, resultResponse);
//
//		LOG.info(
//				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
//						+ GiftingConstants.RESPONSE_PARAMS,
//				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CANCEL_GIFT, resultResponse);
//
//		return resultResponse;
//
//	}

	/* CONFIGURE BIRTHDAY REMINDERS */

	/**
	 * This method is used to create a birthday reminder list for accounts.
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
	 * @param birthdayReminderDto
	 * @return
	 */
	@PostMapping(value = GiftingConfigurationConstants.CREATE_BIRTHDAY_REMINDER_LIST, consumes = MediaType.ALL_VALUE)
	public ResultResponse configureBirthdayReminderList(
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
			@RequestBody BirthdayReminderDto birthdayReminderDto) {

		LOG.info(
				GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CONFIGURE_BIRTHDAY_LIST, birthdayReminderDto);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		birthdayReminderDomain.configureBirthdayReminderList(birthdayReminderDto, headers, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_CONFIGURE_BIRTHDAY_LIST, resultResponse);

		return resultResponse;

	}

	/**
	 * This method is used to accept/reject being added to another account's
	 * reminder list.
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
	 * @param birthdayReminderStatusDto
	 * @return
	 */
	@PostMapping(value = GiftingConfigurationConstants.ACCEPT_REJECT_BIRTHDAY_REMINDER, consumes = MediaType.ALL_VALUE)
	public ResultResponse acceptRejectBirthdayReminder(
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
			@RequestBody BirthdayReminderStatusDto birthdayReminderStatusDto) {

		LOG.info(
				GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_ACCEPT_REJECT_REMINDER,
				birthdayReminderStatusDto);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		birthdayReminderDomain.acceptRejectBirthdayReminder(birthdayReminderStatusDto, headers, resultResponse);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_ACCEPT_REJECT_REMINDER, resultResponse);

		return resultResponse;

	}

	/* NOTIFY BIRTHDAY & LISTING GOLD TRANSACTIONS */

	/**
	 * This is a Cron API to notify accounts of their friends' birthday.
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
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.NOTIFY_MEMBER_BIRTHDAY, consumes = MediaType.ALL_VALUE)
	public ResultResponse notifyMemberBirthday(
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

		LOG.info(GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_NOTIFY_REMINDER);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		birthdayReminderDomain.notifyMemberBirthday(resultResponse, headers);

		LOG.info(
				GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_NOTIFY_REMINDER, resultResponse);

		return resultResponse;

	}

	/**
	 * This method is used to list gold transaction.
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
	 * @param accountNumber
	 * @return
	 */
	@GetMapping(value = GiftingConfigurationConstants.LIST_GOLD_TRANSACTIONS, consumes = MediaType.ALL_VALUE)
	public ListGoldTransactionResponse listGoldTransaction(
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
			@RequestParam(value = GiftingConfigurationConstants.ACCOUNT_NUMBER, required = false) String accountNumber) {

		LOG.info(
				GiftingConstants.LOG_ENTERING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
						+ GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_GOLD_TRANSACTION, accountNumber);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ListGoldTransactionResponse listGoldTransactionResponse = new ListGoldTransactionResponse(
				externalTransactionId);
		goldCertificateDomain.listGoldTransactions(listGoldTransactionResponse, accountNumber, headers);

		LOG.info(GiftingConstants.LOG_LEAVING + GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_LIST_GOLD_TRANSACTION);

		return listGoldTransactionResponse;

	}

	@PostMapping(value = GiftingConfigurationConstants.GIFT_ACCOUNT_CHANGE, consumes = MediaType.ALL_VALUE)
	public ResultResponse giftingAccountNumberChange(
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
			@RequestBody AccountChangeEvent accountChangeEvent) {

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), "testAccountNumberChange", accountChangeEvent);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		eventHelper.updateBirthdayReminderRecords(accountChangeEvent);
		eventHelper.updateGiftingCounterRecords(accountChangeEvent);
		eventHelper.updateGiftingHistoryRecords(accountChangeEvent);

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), "testAccountNumberChange", resultResponse);

		return resultResponse;

	}
	
	@PostMapping(value = GiftingConfigurationConstants.GIFT_PREMIUM_VOUCHER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse giftPremiumVoucherForPointRedemption(
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
			@RequestBody PremiumVoucherRequest premiumVoucherDto) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), GiftingConstants.GIFT_PREMIUM_VOUCHERS_METHOD);
		LOG.info(log);

		//Changes for loyalty as a service.
	    if (null == program) program = defaultProgramCode;
				
		ResultResponse resultResponse = giftDomain.giftPremiumVoucher(premiumVoucherDto, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), GiftingConstants.GIFT_PREMIUM_VOUCHERS_METHOD);
		LOG.info(log);
		
		return resultResponse;

	}
	
	@PostMapping(value = GiftingConfigurationConstants.CREATE_GIFT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureGift(
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
			@RequestBody GiftConfigureRequestDto giftRequestDto) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), GiftingConstants.GIFT_PREMIUM_VOUCHERS_METHOD);
		LOG.info(log);

		//Changes for loyalty as a service.
	    if (null == program) program = defaultProgramCode;
				
		ResultResponse resultResponse = giftDomain.validateAndConfigureGift(giftRequestDto, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), GiftingConstants.GIFT_PREMIUM_VOUCHERS_METHOD);
		LOG.info(log);
		
		return resultResponse;

	}
	
	@PostMapping(value = GiftingConfigurationConstants.UPDATE_GIFT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse updateGift(
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
			@RequestBody GiftUpdateRequestDto giftRequestDto,
			@PathVariable(value = GiftingConfigurationConstants.ID, required = true) String id) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), GiftingConstants.GIFT_PREMIUM_VOUCHERS_METHOD);
		LOG.info(log);

		//Changes for loyalty as a service.
	    if (null == program) program = defaultProgramCode;
				
		ResultResponse resultResponse = giftDomain.validateAndUpdateGift(id, giftRequestDto, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), GiftingConstants.GIFT_PREMIUM_VOUCHERS_METHOD);
		LOG.info(log);
		
		return resultResponse;

	}
	
		
	@DeleteMapping(value = GiftingConfigurationConstants.DELETE_GIFT, consumes = MediaType.ALL_VALUE)
	public ResultResponse deleteGift(
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
			@PathVariable(value = GiftingConfigurationConstants.ID, required = true) String id) {
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), GiftingConstants.DELETE_GIFT_CONFIGURATION);
		LOG.info(log);

		//Changes for loyalty as a service.
	    if (null == program) program = defaultProgramCode;
				
		ResultResponse resultResponse = giftDomain.validateAndDeleteGift(id, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), GiftingConstants.DELETE_GIFT_CONFIGURATION);
		LOG.info(log);
		
		return resultResponse;

	}
	
	@GetMapping(value = GiftingConfigurationConstants.LIST_GIFT, consumes = MediaType.ALL_VALUE)
	public ListGiftConfigurationResponse listGiftConfiguration(
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
			@RequestParam(value = GiftingConfigurationConstants.GIFT_TYPE, required = false) String giftType ) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), GiftingConstants.LIST_GIFT_CONFIGURATION_METHOD);
		LOG.info(log);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ListGiftConfigurationResponse listGiftConfigurationResponse = giftDomain.listGiftConfiguration(giftType, headers);

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), GiftingConstants.LIST_GIFT_CONFIGURATION_METHOD);
		LOG.info(log);

		return listGiftConfigurationResponse;

	}
	
	@GetMapping(value = GiftingConfigurationConstants.GIFT_DETAIL, consumes = MediaType.ALL_VALUE)
	public ListGiftConfigurationResponse listGiftConfigurationDetail(
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
		@PathVariable(value = GiftingConfigurationConstants.ID, required = true) String id) {
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), GiftingConstants.LIST_GIFT_CONFIGURATION_METHOD);
		LOG.info(log);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ListGiftConfigurationResponse listGiftConfigurationResponse = giftDomain.listGiftConfigurationDetail(id, headers);

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), GiftingConstants.LIST_GIFT_CONFIGURATION_METHOD);
		LOG.info(log);
		
		return listGiftConfigurationResponse;

	}

}
