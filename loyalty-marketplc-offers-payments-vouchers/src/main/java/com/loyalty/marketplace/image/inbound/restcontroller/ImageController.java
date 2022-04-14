package com.loyalty.marketplace.image.inbound.restcontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.image.constants.ImageConfigurationConstants;
import com.loyalty.marketplace.image.constants.ImageConstants;
import com.loyalty.marketplace.image.domain.BannerPropertiesDomain;
import com.loyalty.marketplace.image.domain.ImageDomain;
import com.loyalty.marketplace.image.helper.ImageControllerHelper;
import com.loyalty.marketplace.image.helper.ImageDimensions;
import com.loyalty.marketplace.image.inbound.dto.BannerParameters;
import com.loyalty.marketplace.image.inbound.dto.BannerPropertiesDto;
import com.loyalty.marketplace.image.inbound.dto.BannerPropertiesRequest;
import com.loyalty.marketplace.image.inbound.dto.GenerateMetaDataRequest;
import com.loyalty.marketplace.image.inbound.dto.GiftingParameters;
import com.loyalty.marketplace.image.inbound.dto.ImageParameters;
import com.loyalty.marketplace.image.inbound.dto.MerchantOfferParameters;
import com.loyalty.marketplace.image.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.image.outbound.dto.GenerateMetaDataResponse;
import com.loyalty.marketplace.image.outbound.dto.ListBannerPropertiesResponse;
import com.loyalty.marketplace.image.outbound.dto.ListImageResponse;
import com.loyalty.marketplace.image.outbound.dto.ResultResponse;
import com.loyalty.marketplace.offers.helper.dto.Headers;

import io.swagger.annotations.Api;

@RestController
@Api(value = ImageConfigurationConstants.MARKETPLACE)
@RequestMapping(ImageConfigurationConstants.MARKETPLACE_BASE)
public class ImageController {

	private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	ImageDomain imageDomain;
		
	@Autowired
	ImageControllerHelper imageControllerHelper;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	BannerPropertiesDomain bannerPropertiesDomain;

	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;
	
	/* POST: CONFIGURE MERCHANT OFFER BANNER GIFTING IMAGES & PROPERTIES */
	
	/**
	 * API to upload Merchant & Offer image.
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
	 * @param file
	 * @param imageType
	 * @param domainId
	 * @param domainName
	 * @param availableInChannel
	 * @return
	 */
	@PostMapping(value = ImageConfigurationConstants.UPLOAD_IMAGE_MERCHANT_OFFER, consumes = MediaType.ALL_VALUE)
	public ResultResponse configureMerchantOfferImage(
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
			@RequestParam(value = ImageConfigurationConstants.FILE, required = false) MultipartFile file,
			@RequestParam(value = ImageConfigurationConstants.IMAGE_TYPE, required = false) String imageType,
			@RequestParam(value = ImageConfigurationConstants.DOMAIN_ID, required = false) String domainId,
			@RequestParam(value = ImageConfigurationConstants.DOMAIN_NAME, required = false) String domainName,
			@RequestParam(value = ImageConfigurationConstants.AVAILABLE_IN_CHANNEL, required = false) String availableInChannel) {

		MerchantOfferParameters merchantOfferParameters = new MerchantOfferParameters(program, authorization, externalTransactionId, userName,
				sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, file, imageType,
				domainId, domainName, availableInChannel);

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_MERCHANT_OFFER_IMG);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		imageDomain.validateUploadMerchantOfferImage(merchantOfferParameters, resultResponse, headers);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_MERCHANT_OFFER_IMG);

		return resultResponse;

	}

	/**
	 * API to upload Banner image.
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
	 * @param file
	 * @param offerId
	 * @param offerType
	 * @param deepLink
	 * @param status
	 * @param bannerOrder
	 * @param isStaticBanner
	 * @param isBogoOffer
	 * @param coBrandedPartner
	 * @param bannerPosition
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@PostMapping(value = ImageConfigurationConstants.UPLOAD_IMAGE_BANNER, consumes = MediaType.ALL_VALUE)
	public ResultResponse configureBannerImage(
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
			@RequestParam(value = ImageConfigurationConstants.FILE, required = false) MultipartFile file,
			@RequestParam(value = ImageConfigurationConstants.OFFER_ID, required = false) String offerId,
			@RequestParam(value = ImageConfigurationConstants.OFFER_TYPE, required = false) String offerType,
			@RequestParam(value = ImageConfigurationConstants.DEEP_LINK, required = false) String deepLink,
			@RequestParam(value = ImageConfigurationConstants.STATUS, required = false) String status,
			@RequestParam(value = ImageConfigurationConstants.BANNER_ORDER, required = false) Integer bannerOrder,
			@RequestParam(value = ImageConfigurationConstants.IS_STATIC_BANNER, required = false) String isStaticBanner,
			@RequestParam(value = ImageConfigurationConstants.IS_BOGO_OFFER, required = false) String isBogoOffer,
			@RequestParam(value = ImageConfigurationConstants.CO_BRANDED_PARTNER, required = false) String coBrandedPartner,
			@RequestParam(value = ImageConfigurationConstants.BANNER_POSITION, required = false) String bannerPosition,
			@RequestParam(value = ImageConfigurationConstants.START_DATE, required = false) String startDate,
			@RequestParam(value = ImageConfigurationConstants.END_DATE, required = false) String endDate) {

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		BannerParameters bannerParamters = new BannerParameters(program, authorization, externalTransactionId, userName,
				sessionId, userPrev, channelId, systemId, systemPassword, token,
				transactionId, file, offerId, offerType, deepLink, status,
				bannerOrder, isStaticBanner, isBogoOffer, coBrandedPartner, bannerPosition,
				startDate, endDate);

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_BANNER_IMG);

		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		imageDomain.validateUploadBannerImage(bannerParamters, resultResponse, headers);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_BANNER_IMG);

		return resultResponse;

	}
	
	/**
	 * API to upload Gifting image.
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
	 * @param file
	 * @param type
	 * @param priority
	 * @param backgroundPriority
	 * @param status
	 * @param nameEn
	 * @param nameAr
	 * @param colorCode
	 * @param colorDirection
	 * @param greetingMessageEn
	 * @param greetingMessageAr
	 * @return
	 */
	@PostMapping(value = ImageConfigurationConstants.UPLOAD_IMAGE_GIFTING, consumes = MediaType.ALL_VALUE)
	public ResultResponse configureGiftingImage(
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
			@RequestParam(value = ImageConfigurationConstants.FILE, required = false) MultipartFile file,
			@RequestParam(value = ImageConfigurationConstants.TYPE, required = false) String type,
			@RequestParam(value = ImageConfigurationConstants.PRIORITY, required = false) Integer priority,
			@RequestParam(value = ImageConfigurationConstants.BACKGROUND_PRIORITY, required = false) Integer backgroundPriority,
			@RequestParam(value = ImageConfigurationConstants.STATUS, required = false) String status,
			@RequestParam(value = ImageConfigurationConstants.NAME_EN, required = false) String nameEn,
			@RequestParam(value = ImageConfigurationConstants.NAME_AR, required = false) String nameAr,
			@RequestParam(value = ImageConfigurationConstants.COLOR_CODE, required = false) String colorCode,
			@RequestParam(value = ImageConfigurationConstants.COLOR_DIRECTION, required = false) String colorDirection,
			@RequestParam(value = ImageConfigurationConstants.GREETING_MSG_EN, required = false) String greetingMessageEn,
			@RequestParam(value = ImageConfigurationConstants.GREETING_MSG_AR, required = false) String greetingMessageAr) {

		GiftingParameters giftingParamters = new GiftingParameters(program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, file, type,
				priority, backgroundPriority, status, nameEn, nameAr, colorCode, colorDirection, greetingMessageEn,
				greetingMessageAr);

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_GIFTING_IMG);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		imageDomain.validateUploadGiftingImage(giftingParamters, resultResponse, headers);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_GIFTING_IMG);

		return resultResponse;

	}

	/**
	 * API to configure banner properties.
	 * @param bannerPropertiesDto
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
	@PostMapping(value = ImageConfigurationConstants.ADD_BANNER_PROPERTY, consumes = MediaType.ALL_VALUE)
	public ResultResponse configureBannerProperties(
			@RequestBody BannerPropertiesDto bannerPropertiesDto,
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

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_BANNER_PROPERTIES, bannerPropertiesDto);

		BannerPropertiesRequest bannerPropertiesRequest = new BannerPropertiesRequest(bannerPropertiesDto.getTopBannerLimit(), bannerPropertiesDto.getMiddleBannerLimit(),
				bannerPropertiesDto.getBottomBannerLimit(), bannerPropertiesDto.getIncludeRedeemedOffers(),
				bannerPropertiesDto.getPersonalizeBannerCount(), bannerPropertiesDto.getFixedBannerCount(), program,
				userName, token, externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		bannerPropertiesDomain.configureBannerProperty(bannerPropertiesRequest, resultResponse, headers);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_CONFIGURE_BANNER_PROPERTIES);

		return resultResponse;

	}
	
	/* POST: UPDATE BANNER GIFTING IMAGES & PROPERTIES */
	
	/**
	 * API to update Banner & Gifting image.
	 * @param id
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
	 * @param file
	 * @param status
	 * @param offerId
	 * @param offerType
	 * @param deepLink
	 * @param bannerOrder
	 * @param isStaticBanner
	 * @param isBogoOffer
	 * @param coBrandedPartner
	 * @param bannerPosition
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param priority
	 * @param backgroundPriority
	 * @param nameEn
	 * @param nameAr
	 * @param colorCode
	 * @param colorDirection
	 * @param greetingMessageEn
	 * @param greetingMessageAr
	 * @return
	 */
	@PostMapping(value = ImageConfigurationConstants.UPDATE_IMAGE, consumes = MediaType.ALL_VALUE)
	public ResultResponse updateBannerGiftingImage(
			@PathVariable(value = ImageConfigurationConstants.IMAGE_ID, required = true) String id,
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
			@RequestParam(value = ImageConfigurationConstants.FILE, required = false) MultipartFile file,
			@RequestParam(value = ImageConfigurationConstants.STATUS, required = false) String status,
			@RequestParam(value = ImageConfigurationConstants.OFFER_ID, required = false) String offerId,
			@RequestParam(value = ImageConfigurationConstants.OFFER_TYPE, required = false) String offerType,
			@RequestParam(value = ImageConfigurationConstants.DEEP_LINK, required = false) String deepLink,
			@RequestParam(value = ImageConfigurationConstants.BANNER_ORDER, required = false) Integer bannerOrder,
			@RequestParam(value = ImageConfigurationConstants.IS_STATIC_BANNER, required = false) String isStaticBanner,
			@RequestParam(value = ImageConfigurationConstants.IS_BOGO_OFFER, required = false) String isBogoOffer,
			@RequestParam(value = ImageConfigurationConstants.CO_BRANDED_PARTNER, required = false) String coBrandedPartner,
			@RequestParam(value = ImageConfigurationConstants.BANNER_POSITION, required = false) String bannerPosition,
			@RequestParam(value = ImageConfigurationConstants.START_DATE, required = false) String startDate,
			@RequestParam(value = ImageConfigurationConstants.END_DATE, required = false) String endDate,
			@RequestParam(value = ImageConfigurationConstants.TYPE, required = false) String type,
			@RequestParam(value = ImageConfigurationConstants.PRIORITY, required = false) Integer priority,
			@RequestParam(value = ImageConfigurationConstants.BACKGROUND_PRIORITY, required = false) Integer backgroundPriority,
			@RequestParam(value = ImageConfigurationConstants.NAME_EN, required = false) String nameEn,
			@RequestParam(value = ImageConfigurationConstants.NAME_AR, required = false) String nameAr,
			@RequestParam(value = ImageConfigurationConstants.COLOR_CODE, required = false) String colorCode,
			@RequestParam(value = ImageConfigurationConstants.COLOR_DIRECTION, required = false) String colorDirection,
			@RequestParam(value = ImageConfigurationConstants.GREETING_MSG_EN, required = false) String greetingMessageEn,
			@RequestParam(value = ImageConfigurationConstants.GREETING_MSG_AR, required = false) String greetingMessageAr) {

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		ImageParameters imageRequest = new ImageParameters(program, authorization, externalTransactionId, userName,
				sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, file, status, offerId,
				offerType, deepLink, bannerOrder, isStaticBanner, isBogoOffer, coBrandedPartner, bannerPosition,
				startDate, endDate, type, priority, backgroundPriority, nameEn, nameAr, colorCode, colorDirection,
				greetingMessageEn, greetingMessageAr);
		
		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_UPDATE_IMG);

		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		imageDomain.updateBannerGiftingImage(imageRequest, id, resultResponse, headers);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_UPDATE_IMG);

		return resultResponse;

	}
	
	/**
	 * API to update banner properties.
	 * @param bannerPropertiesDto
	 * @param id
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
	@PostMapping(value = ImageConfigurationConstants.UPDATE_BANNER_PROPERTY, consumes = MediaType.ALL_VALUE)
	public ResultResponse updateBannerProperties(
			@RequestBody BannerPropertiesDto bannerPropertiesDto,
			@PathVariable(value = ImageConfigurationConstants.IMAGE_ID, required = true) String id,
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

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_UPDATE_PROPERTIES, bannerPropertiesDto);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		BannerPropertiesRequest bannerPropertiesRequest = new BannerPropertiesRequest(bannerPropertiesDto.getTopBannerLimit(), bannerPropertiesDto.getMiddleBannerLimit(),
				bannerPropertiesDto.getBottomBannerLimit(), bannerPropertiesDto.getIncludeRedeemedOffers(),
				bannerPropertiesDto.getPersonalizeBannerCount(), bannerPropertiesDto.getFixedBannerCount(), id, program,
				userName, token, externalTransactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		bannerPropertiesDomain.updateBannerProperty(bannerPropertiesRequest, resultResponse);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_UPDATE_PROPERTIES);

		return resultResponse;

	}
	
	/* GET: LIST BANNER GIFTING IMAGES & PROPERTIES */
	
	/**
	 * API to list images and its attributes.
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
	 * @param imageType
	 * @param status
	 * @return
	 */
	@GetMapping(value = ImageConfigurationConstants.RETRIEVE_IMAGES, consumes = MediaType.ALL_VALUE)
	public ListImageResponse listImages(
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
			@RequestParam(value = ImageConfigurationConstants.LISTING_IMAGE_TYPE, required = false) String imageType,
			@RequestParam(value = ImageConfigurationConstants.LISTING_STATUS, required = false) String status) {

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.LIST_IMAGE_REQUEST_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_LIST_IMG, imageType, status);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		ListImageResponse listImageResponse = new ListImageResponse(externalTransactionId);
		imageDomain.listAllImages(listImageResponse, imageType, status);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_LIST_IMG);

		return listImageResponse;

	}
	
	/**
	 * This API is used to display image based on id.
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
	 * @param httpServletResponse
	 */
	@GetMapping(value = ImageConfigurationConstants.DISPLAY_IMAGE, consumes = MediaType.ALL_VALUE)
	public void displayImage(
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
			@PathVariable(value = ImageConfigurationConstants.IMAGE_ID, required = true) String id,
			HttpServletResponse httpServletResponse) {

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_DISPLAY_IMG, id);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		imageDomain.displayImage(id, httpServletResponse, resultResponse);

		if (!resultResponse.getApiStatus().getErrors().isEmpty()) {
			LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
					this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_DISPLAY_IMG);
		} else {
			LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.DISPLAY_IMAGE,
					this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_DISPLAY_IMG);
		}

	}
	
	/**
	 * API to list banner properties.
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
	@GetMapping(value = ImageConfigurationConstants.LIST_BANNER_PROPERTY, consumes = MediaType.ALL_VALUE)
	public ListBannerPropertiesResponse listBannerProperties(
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

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME, this.getClass().getSimpleName(),
				ImageConfigurationConstants.CONTROLLER_LIST_PROPERTIES);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		ListBannerPropertiesResponse listBannerPropertiesResponse = new ListBannerPropertiesResponse(externalTransactionId);
		bannerPropertiesDomain.listBannerProperties(listBannerPropertiesResponse);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_LIST_PROPERTIES);

		return listBannerPropertiesResponse;

	}
	
	/**
	 * A helper API to list configured images categories and their dimensions.
	 * @return
	 */
	@GetMapping(value = ImageConfigurationConstants.LIST_IMAGE_DIMENSIONS, consumes = MediaType.ALL_VALUE)
	public List<String> listImageDimensions() {

		List<String> list = new ArrayList<>();
		list.add("MERCHANT_WEB_LISTING_L: " + ImageDimensions.MERCHANT_WEB_LISTING_L);
		list.add("MERCHANT_WEB_LISTING_H: " + ImageDimensions.MERCHANT_WEB_LISTING_H);
		list.add("MERCHANT_WEB_DETAIL_L: " + ImageDimensions.MERCHANT_WEB_DETAIL_L);
		list.add("MERCHANT_WEB_DETAIL_H: " + ImageDimensions.MERCHANT_WEB_DETAIL_H);
		list.add("MERCHANT_APP_COUPONS_L: " + ImageDimensions.MERCHANT_APP_COUPONS_L);
		list.add("MERCHANT_APP_COUPONS_H: " + ImageDimensions.MERCHANT_APP_COUPONS_H);
		list.add("MERCHANT_APP_DETAIL_L: " + ImageDimensions.MERCHANT_APP_DETAIL_L);
		list.add("MERCHANT_APP_DETAIL_H: " + ImageDimensions.MERCHANT_APP_DETAIL_H);
		list.add("MERCHANT_APP_LISTING_L: " + ImageDimensions.MERCHANT_APP_LISTING_L);
		list.add("MERCHANT_APP_LISTING_H: " + ImageDimensions.MERCHANT_APP_LISTING_H);
		list.add("MERCHANT_APP_LIST_LOGO_L: " + ImageDimensions.MERCHANT_APP_LIST_LOGO_L);
		list.add("MERCHANT_APP_LIST_LOGO_H: " + ImageDimensions.MERCHANT_APP_LIST_LOGO_H);
		list.add("OFFER_WEB_DETAIL_L: " + ImageDimensions.OFFER_WEB_DETAIL_L);
		list.add("OFFER_WEB_DETAIL_H: " + ImageDimensions.OFFER_WEB_DETAIL_H);
		list.add("OFFER_WEB_LISTING_L: " + ImageDimensions.OFFER_WEB_LISTING_L);
		list.add("OFFER_WEB_LISTING_H: " + ImageDimensions.OFFER_WEB_LISTING_H);
		list.add("OFFER_WEB_MY_COUPONS_L: " + ImageDimensions.OFFER_WEB_MY_COUPONS_L);
		list.add("OFFER_WEB_MY_COUPONS_H: " + ImageDimensions.OFFER_WEB_MY_COUPONS_H);
		list.add("OFFER_WEB_FEATURED_L: " + ImageDimensions.OFFER_WEB_FEATURED_L);
		list.add("OFFER_WEB_FEATURED_H: " + ImageDimensions.OFFER_WEB_FEATURED_H);
		list.add("OFFER_APP_DOD_LISTING_NEW_L: " + ImageDimensions.OFFER_APP_DOD_LISTING_NEW_L);
		list.add("OFFER_APP_DOD_LISTING_NEW_H: " + ImageDimensions.OFFER_APP_DOD_LISTING_NEW_H);
		list.add("OFFER_APP_SMALL_HORIZONTAL_NEW_L: " + ImageDimensions.OFFER_APP_SMALL_HORIZONTAL_NEW_L);
		list.add("OFFER_APP_SMALL_HORIZONTAL_NEW_H: " + ImageDimensions.OFFER_APP_SMALL_HORIZONTAL_NEW_H);
		list.add("OFFER_APP_DETAIL_L: " + ImageDimensions.OFFER_APP_DETAIL_L);
		list.add("OFFER_APP_DETAIL_H: " + ImageDimensions.OFFER_APP_DETAIL_H);
		list.add("OFFER_APP_LUCKY_OFFER_NEW_L: " + ImageDimensions.OFFER_APP_LUCKY_OFFER_NEW_L);
		list.add("OFFER_APP_LUCKY_OFFER_NEW_H: " + ImageDimensions.OFFER_APP_LUCKY_OFFER_NEW_H);
		list.add("BANNER_POSITION_TOP_L: " + ImageDimensions.BANNER_POSITION_TOP_L);
		list.add("BANNER_POSITION_TOP_H: " + ImageDimensions.BANNER_POSITION_TOP_H);
		list.add("BANNER_POSITION_MIDDLE_L: " + ImageDimensions.BANNER_POSITION_MIDDLE_L);
		list.add("BANNER_POSITION_MIDDLE_H: " + ImageDimensions.BANNER_POSITION_MIDDLE_H);
		list.add("BANNER_POSITION_BOTTOM_L: " + ImageDimensions.BANNER_POSITION_BOTTOM_L);
		list.add("BANNER_POSITION_BOTTOM_H: " + ImageDimensions.BANNER_POSITION_BOTTOM_H);
		list.add("GIFTING_IMAGE_L: " + ImageDimensions.GIFTING_IMAGE_L);
		list.add("GIFTING_IMAGE_H: " + ImageDimensions.GIFTING_IMAGE_H);
			
		return list;

	}
	
	/**
	 * This API is used to generate meta data for a list of images from the input category.
	 * Note - Generates meta data only for MERCHANT & OFFER images from WEB & APP folders.
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
	 * @param generateMetaDataRequest
	 * @return
	 */
	@PostMapping(value = ImageConfigurationConstants.GENERATE_META_DATA, consumes = MediaType.ALL_VALUE)
    public GenerateMetaDataResponse generateMetaDataForMerchantOfferImages(
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
			@RequestBody GenerateMetaDataRequest generateMetaDataRequest) {

		LOG.info(ImageConstants.LOG_ENTERING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_GENERATE_META_DATA, generateMetaDataRequest);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		GenerateMetaDataResponse generateMetaDataResponse = new GenerateMetaDataResponse(externalTransactionId);
		imageControllerHelper.generateMetaData(generateMetaDataRequest, generateMetaDataResponse, headers);

		LOG.info(ImageConstants.LOG_LEAVING + ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.RESPONSE_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_GENERATE_META_DATA, generateMetaDataResponse);

		return generateMetaDataResponse;
    
	}
	
}
