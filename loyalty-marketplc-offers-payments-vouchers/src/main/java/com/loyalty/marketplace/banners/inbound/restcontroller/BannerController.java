package com.loyalty.marketplace.banners.inbound.restcontroller;

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
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.banners.constants.BannerConstants;
import com.loyalty.marketplace.banners.constants.BannerRequestMappingConstants;
import com.loyalty.marketplace.banners.domain.model.BannerDomain;
import com.loyalty.marketplace.banners.inbound.dto.BannerListRequestDto;
import com.loyalty.marketplace.banners.inbound.dto.BannerRequestDto;
import com.loyalty.marketplace.banners.outbound.dto.BannerResultResponse;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.Logs;

import io.swagger.annotations.Api;

@RestController
@Api(value = RequestMappingConstants.MARKETPLACE)
@RequestMapping(RequestMappingConstants.MARKETPLACE_BASE)
public class BannerController {
	
	private static final Logger LOG = LoggerFactory.getLogger(BannerController.class);
	
	@Autowired
	BannerDomain bannerDomain;
	
	@Value(MarketplaceConfigurationConstants.DEFAULT_PROGRAM_CODE)
	private String defaultProgramCode;
	
	@PostMapping(value = BannerRequestMappingConstants.CREATE_BANNER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse createBanner(@RequestBody BannerListRequestDto bannersRequestListDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), BannerConstants.CREATE_BANNER_METHOD.get());
		LOG.info(log);

		if (null == program) program = defaultProgramCode;
		
		log = Logs.logForRequest(bannersRequestListDto);
		LOG.info(log);
		
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.HEADER, header);
		LOG.info(log);

		ResultResponse resultResponse = bannerDomain.addNewBanner(bannersRequestListDto, header);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), BannerConstants.CREATE_BANNER_METHOD.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	@PostMapping(value = BannerRequestMappingConstants.UPDATE_BANNER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse updateBanner(@RequestBody BannerRequestDto bannerRequestDto,
			@PathVariable(value = BannerRequestMappingConstants.ID, required = true) String bannerId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), BannerConstants.UPDATE_BANNER_METHOD.get());
		LOG.info(log);

		if (null == program) program = defaultProgramCode;
		
		log = Logs.logForRequest(bannerRequestDto);
		LOG.info(log);
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.BANNER_ID, bannerId);
		LOG.info(log);
		
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.HEADER, header);
		LOG.info(log);

		ResultResponse resultResponse = bannerDomain.updateExistingBanner(bannerId, bannerRequestDto, header);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), BannerConstants.UPDATE_BANNER_METHOD.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	@GetMapping(value = BannerRequestMappingConstants.FETCH_BANNER_LIST)
	public BannerResultResponse listBanners(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), BannerConstants.LIST_BANNERS_METHOD.get());
		LOG.info(log);

		if (null == program) program = defaultProgramCode;
		
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.HEADER, header);
		LOG.info(log);
		
		BannerResultResponse resultResponse = bannerDomain.listAllBanners(header);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), BannerConstants.LIST_BANNERS_METHOD.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	@GetMapping(value = BannerRequestMappingConstants.FETCH_SPECIFIC_BANNER)
	public BannerResultResponse listSpecificBanner(
			@PathVariable(value = BannerRequestMappingConstants.ID, required = true) String bannerId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), BannerConstants.LIST_SPECIFIC_BANNER_METHOD.get());
		LOG.info(log);

		if (null == program) program = defaultProgramCode;
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.BANNER_ID, bannerId);
		LOG.info(log);

		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.HEADER, header);
		LOG.info(log);
		
		BannerResultResponse resultResponse = bannerDomain.listSpecificBanner(bannerId, header);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), BannerConstants.LIST_SPECIFIC_BANNER_METHOD.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	@DeleteMapping(value = BannerRequestMappingConstants.DELETE_BANNER)
	public ResultResponse deleteBanner(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@PathVariable(value = BannerRequestMappingConstants.ID, required = true) String bannerId,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), BannerConstants.DELETE_BANNER_METHOD.get());
		LOG.info(log);

		if (null == program) program = defaultProgramCode;
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.BANNER_ID, bannerId);
		LOG.info(log);

		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		log = Logs.logForVariable(MarketplaceConfigurationConstants.HEADER, header);
		LOG.info(log);
		
		ResultResponse resultResponse = bannerDomain.deleteSpecificBanner(bannerId, header);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), BannerConstants.DELETE_BANNER_METHOD.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
}
