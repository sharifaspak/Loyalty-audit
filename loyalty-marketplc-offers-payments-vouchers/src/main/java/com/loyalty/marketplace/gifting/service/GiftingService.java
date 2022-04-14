package com.loyalty.marketplace.gifting.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.helper.Utility;
import com.loyalty.marketplace.gifting.helper.dto.NotificationHelperDto;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;
import com.loyalty.marketplace.gifting.service.dto.ApiStatus;
import com.loyalty.marketplace.gifting.service.dto.CommonApiStatus;
import com.loyalty.marketplace.gifting.service.dto.EnrollmentResult;
import com.loyalty.marketplace.gifting.service.dto.EnrollmentResultResponse;
import com.loyalty.marketplace.gifting.service.dto.GetListMemberResponse;
import com.loyalty.marketplace.gifting.service.dto.ListMemberDetailRequestDto;
import com.loyalty.marketplace.gifting.service.dto.MemberActivityPaymentDto;
import com.loyalty.marketplace.gifting.service.dto.MemberActivityResponse;
import com.loyalty.marketplace.gifting.service.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.image.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.dto.PushNotificationRequestDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;

@RefreshScope
@Service
public class GiftingService {

	private static final Logger LOG = LoggerFactory.getLogger(GiftingService.class);

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("getTemplateBean")
	private RestTemplate getRestTemplate;

	@Value("${memberManagement.uri}")
	private String memberManagementUri;

	@Value("${memberManagement.listAllMember.path}")
	private String listAllMemberPath;

	@Value("${memberManagement.enrollMember.path}")
	private String enrollMemberPath;

	@Value("${points.bank.uri}")
	private String pointsBankUri;

	@Value("${points.bank.memberPoints.path}")
	private String memberPoints;

	@Value("${memberActivity.uri}")
	private String memberActivityUri;

	@Value("${memberActivity.payment.path}")
	private String memberAccrualRedemption;

	@Value("${voucher.gift.url}")
	private String voucherGiftUrl;

	@Autowired
	private ServiceHelper serviceHelper;

	@Autowired
	EventHandler eventHandler;

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private RetryTemplate retryTemplate;

	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;

	public static final String SMS_QUEUE = "smsQueue";
	public static final String SCHEDULED_TIME = "scheduledTime";

	/* MEMBER MANAGEMENT METHODS */

	/**
	 * This method makes a call to member management microservice to enroll a
	 * member.
	 * 
	 * @param accountNumber
	 * @param header
	 * @return
	 * @throws MarketplaceException
	 */
	public EnrollmentResultResponse enrollMember(String accountNumber, Headers header) throws MarketplaceException {

		String url = memberManagementUri + enrollMemberPath;

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.MM_URL,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_ENROLL_MEMBER, url);

		EnrollmentResultResponse enrollmentResultResponse = new EnrollmentResultResponse();
		try {

			MemberDetailRequestDto getMemberRequest = new MemberDetailRequestDto();
			getMemberRequest.setAccountNumber(accountNumber.replace("\ufeff", ""));
			header.setChannelId(GiftingConstants.CHANNEL_ID_CCPORTAL);

			HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(getMemberRequest,
					serviceHelper.getHeader(header));
			ResponseEntity<CommonApiStatus> response = retryCallForEnrollMember(url, requestEntity, header);
			CommonApiStatus commonApiStatus = response.getBody();

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.MEMBER_ENROLL_RESPONSE,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_ENROLL_MEMBER,
					commonApiStatus);

			if (commonApiStatus != null && commonApiStatus.getApiStatus().getStatusCode() == 0) {
				Object result = commonApiStatus.getResult();
				enrollmentResultResponse = modelMapper.map(result, EnrollmentResultResponse.class);
				enrollmentResultResponse.getEnrollmentResult().setMessage(commonApiStatus.getApiStatus().getMessage());
			} else {
				EnrollmentResult enrollmentResult = new EnrollmentResult();
				enrollmentResult.setMessage(GiftingConstants.MEMBER_ENROLL_EXCEPTION
						+ ((commonApiStatus != null) ? commonApiStatus.getApiStatus().getMessage() : ""));
				enrollmentResultResponse.setEnrollmentResult(enrollmentResult);
			}

		} catch (RestClientException e) {
			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_ENROLL_MEMBER, e.getClass() + e.getMessage(),
					GiftingCodes.MEMBER_MANAGEMENT_EXCEPTION);
		} catch (Exception e) {
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_ENROLL_MEMBER, e.getClass() + e.getMessage(),
					GiftingCodes.MEMBER_MANAGEMENT_EXCEPTION).printMessage());
		}

		return enrollmentResultResponse;
	}

	/***
	 * 
	 * @param requestEntity
	 * @param url
	 * @return
	 */
	private ResponseEntity<CommonApiStatus> retryCallForEnrollMember(String url,
			HttpEntity<MemberDetailRequestDto> requestEntity, Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForEnrollMember method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				return restTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;

	}

	/**
	 * This method is used to retrieve member details by passing a list of account
	 * numbers.
	 * 
	 * @param accounts
	 * @param header
	 * @return
	 * @throws MarketplaceException
	 */
	public GetListMemberResponse getListMemberDetails(List<String> accounts, Headers header)
			throws MarketplaceException {

		String url = memberManagementUri + listAllMemberPath;

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.MM_URL,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_LIST_MEMBER_DETAILS, url);
		GetListMemberResponse memberResponse = null;
		try {
			ListMemberDetailRequestDto listMemberRequest = new ListMemberDetailRequestDto();
			listMemberRequest.setAccounts(accounts);

			LOG.info(OfferConstants.REQUEST_PARAMS_FOR.get(), "{} get Member details, {}{}, {} ",
					OfferConstants.MESSAGE_SEPARATOR.get(), OfferConstants.SINGLE_MESSAGE.get(), listMemberRequest);

			HttpEntity<ListMemberDetailRequestDto> requestEntity = new HttpEntity<>(listMemberRequest,
					serviceHelper.getHeader(header));

			ResponseEntity<CommonApiStatus> response = retryCallForGetListMemberDetails(url, requestEntity, header);
			CommonApiStatus commonApiStatus = response.getBody();

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.LIST_MEMBER_BY_ACCOUNT_LIST_RESPONSE,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_LIST_MEMBER_DETAILS,
					commonApiStatus);

			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {

				Object result = commonApiStatus.getResult();

				if (null != result) {
					memberResponse = (GetListMemberResponse) serviceHelper.convertToObject(result,
							GetListMemberResponse.class);
				}

				LOG.info(
						GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
								+ GiftingConstants.LIST_MEMBER_BY_ACCOUNT_LIST_RESPONSE,
						this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_LIST_MEMBER_DETAILS,
						memberResponse);

			}

		} catch (RestClientException e) {
			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_LIST_MEMBER_DETAILS, e.getClass() + e.getMessage(),
					GiftingCodes.MEMBER_MANAGEMENT_EXCEPTION);
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_LIST_MEMBER_DETAILS, e.getClass() + e.getMessage(),
					GiftingCodes.MEMBER_MANAGEMENT_EXCEPTION).printMessage());
		}

		return memberResponse;
	}

	private ResponseEntity<CommonApiStatus> retryCallForGetListMemberDetails(String url,
			HttpEntity<ListMemberDetailRequestDto> requestEntity, Headers header) {

		LOG.info("inside Retry block using retryTemplate of retryCallForGetListMemberDetails method of class {}",
				this.getClass().getName());
		return retryTemplate.execute(context -> {
			retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
					requestEntity.toString(), header.getUserName());
			return getRestTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
		});
	}
	/* MEMBER ACTIVITY & POINTS BANK METHODS */

	/**
	 * This method makes a call to points bank microservice to retrieve amount of
	 * points an account has.
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param resultResponse
	 * @param header
	 * @return
	 * @throws MarketplaceException
	 */
	public int retrieveMemberPoints(String accountNumber, String membershipCode, ResultResponse resultResponse,
			Headers header) throws MarketplaceException {

		String url = pointsBankUri + memberPoints;

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.PB_URL,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_RETRIEVE_MEMBER_POINTS, url);
		int result = 0;
		try {

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam(GiftingConstants.MEMBERSHIP_CODE, membershipCode)
					.queryParam(GiftingConstants.ACCOUNT_NUMBER, accountNumber);

			HttpHeaders headers = serviceHelper.getHeader(header);
			headers.set(GiftingConstants.CHANNEL_ID, GiftingConstants.CHANNEL_ID_CCPORTAL);
			HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<CommonApiStatus> response = retryCallForRetrieveMemberPoints(builder, requestEntity, header);

			CommonApiStatus commonApiStatus = response.getBody();

			if (null != commonApiStatus) {

				ApiStatus apiStatus = commonApiStatus.getApiStatus();

				LOG.info(
						GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
								+ GiftingConstants.POINTS_BANK_RESPONSE,
						this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_RETRIEVE_MEMBER_POINTS,
						commonApiStatus);

				if (apiStatus.getStatusCode() == 0 && commonApiStatus.getResult() != null) {
					result = (int) commonApiStatus.getResult();
				} else if (apiStatus.getStatusCode() != 0) {
					resultResponse.setErrorAPIResponse(apiStatus.getStatusCode(), apiStatus.getMessage());
				}

			} else {
				resultResponse.addErrorAPIResponse(GiftingCodes.POINTS_BANK_RESPONSE_NOT_RECEIVED.getIntId(),
						GiftingCodes.POINTS_BANK_RESPONSE_NOT_RECEIVED.getMsg());
			}

		} catch (RestClientException e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), accountNumber,
					header.getUserName(), url.toString());
			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_RETRIEVE_MEMBER_POINTS, e.getClass() + e.getMessage(),
					GiftingCodes.POINTS_BANK_EXCEPTION);
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), accountNumber,
					header.getUserName(), url.toString());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_RETRIEVE_MEMBER_POINTS, e.getClass() + e.getMessage(),
					GiftingCodes.POINTS_BANK_EXCEPTION).printMessage());
		}

		return result;
	}

	private ResponseEntity<CommonApiStatus> retryCallForRetrieveMemberPoints(UriComponentsBuilder builder,
			HttpEntity<HttpHeaders> requestEntity, Headers header) {

		LOG.info("inside Retry block using retryTemplate of retryCallForRetrieveMemberPoints method of class {}",
				this.getClass().getName());
		return retryTemplate.execute(context -> {
			retryLogsService.saveRestRetrytoRetryLogs(builder.toUriString().toString(),
					header.getExternalTransactionId(), requestEntity.toString(), header.getUserName());
			return getRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity,
					CommonApiStatus.class);
		});
	}

	/**
	 * This method makes a call to member activity microservice for
	 * accrual/redemption activity.
	 * 
	 * @param memberActivityPaymentDto
	 * @param header
	 * @param resultResponse
	 * @return
	 * @throws MarketplaceException
	 */
	public MemberActivityResponse memberAccrualRedemption(MemberActivityPaymentDto memberActivityPaymentDto,
			Headers header, ResultResponse resultResponse) throws MarketplaceException {

		String url = memberActivityUri + memberAccrualRedemption;

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.MA_URL,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_ACCRUAL_REDEMPTION, url);
		MemberActivityResponse memberActivityResponse = new MemberActivityResponse();
		try {

			HttpHeaders headers = serviceHelper.getHeader(header);
			HttpEntity<MemberActivityPaymentDto> requestEntity = new HttpEntity<>(memberActivityPaymentDto, headers);
			ResponseEntity<CommonApiStatus> response = retryCallForMemberAccrualRedemption(url, requestEntity, header);
			CommonApiStatus commonApiStatus = response.getBody();

			if (commonApiStatus != null && commonApiStatus.getApiStatus().getStatusCode() == 0) {
				Object result = commonApiStatus.getResult();
				memberActivityResponse = modelMapper.map(result, MemberActivityResponse.class);
			} else {
				resultResponse.addErrorAPIResponse(GiftingCodes.MEMBER_ACTIVTY_SERVICE_CALL_ERROR.getIntId(),
						GiftingCodes.MEMBER_ACTIVTY_SERVICE_CALL_ERROR.getMsg()
								+ ((commonApiStatus != null) ? commonApiStatus.getApiStatus().getErrors() : ""));
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			}

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.MEMBER_ACTIVITY_RESPONSE,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_ACCRUAL_REDEMPTION,
					commonApiStatus);

		} catch (RestClientException e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(),
					memberActivityPaymentDto.getAccountNumber(), header.getUserName(), url.toString());
			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_ACCRUAL_REDEMPTION, e.getClass() + e.getMessage(),
					GiftingCodes.MEMBER_ACTIVITY_EXCEPTION);
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(),
					memberActivityPaymentDto.getAccountNumber(), header.getUserName(), url.toString());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_ACCRUAL_REDEMPTION, e.getClass() + e.getMessage(),
					GiftingCodes.MEMBER_ACTIVITY_EXCEPTION).printMessage());
		}

		return memberActivityResponse;
	}

	private ResponseEntity<CommonApiStatus> retryCallForMemberAccrualRedemption(String url,
			HttpEntity<MemberActivityPaymentDto> requestEntity, Headers header) {

		LOG.info("inside Retry block using retryTemplate of retryCallForMemberAccrualRedemption method of class {}",
				this.getClass().getName());
		return retryTemplate.execute(context -> {
			retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
					requestEntity.toString(), header.getUserName());
			return restTemplate.postForEntity(url, requestEntity, CommonApiStatus.class);
		});
	}

	/* SMS, EMAIL & PUSH NOTIFICATION METHODS */

	/**
	 * This method is used to send push notification for configured birthday
	 * reminder list.
	 * 
	 * @param notificationDto
	 * @param accountNumber
	 * @throws MarketplaceException
	 */
	public void pushNotificationBirthdayReminder(PushNotificationRequestDto notificationDto)
			throws MarketplaceException {

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.NOTIFICATION_DTO,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_BIRTHDAY_PUSH_NOTIFICATION,
				notificationDto);

		try {

			eventHandler.publishPushNotification(notificationDto);

		} catch (JmsException jme) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_BIRTHDAY_PUSH_NOTIFICATION,
					jme.getClass() + jme.getMessage(), GiftingCodes.BIRTHDAY_REMINDER_PUSH_NOTIFICATION_FAILURE);

		} catch (Exception e) {
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_BIRTHDAY_PUSH_NOTIFICATION,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

	}

	/**
	 * This method is used to send SMS to sender & receiver for gift.
	 * 
	 * @param notificationHelperDto
	 * @param headers
	 */
	public void sendSMS(NotificationHelperDto notificationHelperDto, Headers headers) {

		SMSRequestDto smsRequestDto = new SMSRequestDto();

		smsRequestDto.setTransactionId(headers.getExternalTransactionId());
		smsRequestDto.setTemplateId(notificationHelperDto.getTemplateId());
		smsRequestDto.setNotificationId(notificationHelperDto.getNotificationId());
		smsRequestDto.setNotificationCode(notificationHelperDto.getNotificationCode());

		smsRequestDto.setLanguage(notificationHelperDto.getType().equalsIgnoreCase(GiftingConstants.TYPE_SENDER)
				? notificationHelperDto.getSenderLanguage()
				: notificationHelperDto.getReceiverLanguage());

		List<String> destinationNumber = new ArrayList<>();
		destinationNumber.add(notificationHelperDto.getType().equalsIgnoreCase(GiftingConstants.TYPE_SENDER)
				? notificationHelperDto.getSenderContactNumber()
				: notificationHelperDto.getReceiverContactNumber());
		smsRequestDto.setDestinationNumber(destinationNumber);

		Map<String, String> additionalParam = new HashMap<>();

		if (notificationHelperDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			additionalParam.put(GiftingConstants.SMS_FIRST_NAME_SENDER, notificationHelperDto.getSenderFirstName());
			additionalParam.put(GiftingConstants.SMS_GIFT_ID, notificationHelperDto.getGiftingHistory().getId());
			additionalParam.put(GiftingConstants.SMS_POINTS_GIFTED,
					String.valueOf(notificationHelperDto.getPointsGifted()));
		}

		if (notificationHelperDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
			additionalParam.put(GiftingConstants.SMS_FIRST_NAME_SENDER, notificationHelperDto.getSenderFirstName());
			additionalParam.put(GiftingConstants.SMS_GIFT_ID, notificationHelperDto.getGiftingHistory().getId());
			additionalParam.put(GiftingConstants.SMS_GOLD_GIFTED,
					String.valueOf(notificationHelperDto.getGoldGifted()));
		}

		if (notificationHelperDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
			additionalParam.put(GiftingConstants.SMS_CUSTOMER_NAME, notificationHelperDto.getSenderFirstName());
			additionalParam.put(GiftingConstants.SMS_URL,
					voucherGiftUrl + notificationHelperDto.getGiftingHistory().getId());
		}

		if (notificationHelperDto.getGiftingHistory().getScheduledDate().after(new Date())) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timeIs = sdf.format(notificationHelperDto.getGiftingHistory().getScheduledDate());
			additionalParam.put(SCHEDULED_TIME, timeIs);
		}
		smsRequestDto.setAdditionalParameters(additionalParam);

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.NOTIFICATION_DTO,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_SMS, smsRequestDto);

		try {

			eventHandler.publishSms(smsRequestDto);

		} catch (JmsException jme) {

			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_SMS, jme.getClass() + jme.getMessage(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		}

	}

	/**
	 * This method is used to send email to sender & receiver for gift.
	 * 
	 * @param emailId
	 */
	public void sendEmail(NotificationHelperDto notificationHelperDto, Headers headers) {

		try {
			EmailRequestDto emailRequestDto = new EmailRequestDto();

			emailRequestDto.setTransactionId(headers.getExternalTransactionId());
			emailRequestDto.setTemplateId(notificationHelperDto.getTemplateId());
			emailRequestDto.setNotificationId(notificationHelperDto.getNotificationId());
			emailRequestDto.setNotificationCode(notificationHelperDto.getNotificationCode());

			emailRequestDto.setLanguage(notificationHelperDto.getType().equalsIgnoreCase(GiftingConstants.TYPE_SENDER)
					? notificationHelperDto.getSenderLanguage()
					: notificationHelperDto.getReceiverLanguage());

			emailRequestDto.setEmailId(notificationHelperDto.getType().equalsIgnoreCase(GiftingConstants.TYPE_SENDER)
					? notificationHelperDto.getSenderEmailId()
					: notificationHelperDto.getReceiverEmailId());

			emailRequestDto
					.setAccountNumber(notificationHelperDto.getType().equalsIgnoreCase(GiftingConstants.TYPE_SENDER)
							? notificationHelperDto.getSenderAccountNumber()
							: notificationHelperDto.getReceiverAccountNumber());

			Map<String, String> additionalParam = new HashMap<>();
			additionalParam.put(GiftingConstants.EMAIL_SENDER_NAME, notificationHelperDto.getSenderFirstName());
			additionalParam.put(GiftingConstants.EMAIL_SENDER_NUMBER, notificationHelperDto.getSenderAccountNumber());
			additionalParam.put(GiftingConstants.EMAIL_GIFT_ID, notificationHelperDto.getGiftingHistory().getId());
			additionalParam.put(GiftingConstants.EMAIL_RECEIVER_NAME, notificationHelperDto.getReceiverFirstName());
			additionalParam.put(GiftingConstants.EMAIL_RECEIVER_NUMBER,
					notificationHelperDto.getReceiverAccountNumber());
			additionalParam.put(GiftingConstants.EMAIL_BACKGROUND_URL,
					notificationHelperDto.getGiftingHistory().getImageUrl());
			additionalParam.put(GiftingConstants.EMAIL_BACKGROUND_DESCRIPTION,
					URLDecoder.decode(notificationHelperDto.getGiftingHistory().getMessage(), "UTF-8"));
			additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_MSG,
					URLDecoder.decode(notificationHelperDto.getGiftingHistory().getMessage(), "UTF-8"));
			if (notificationHelperDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				additionalParam.put(GiftingConstants.EMAIL_POINTS_GIFTED,
						String.valueOf(notificationHelperDto.getPointsGifted()));
			}

			if (notificationHelperDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				additionalParam.put(GiftingConstants.EMAIL_GOLD_GIFTED,
						String.valueOf(notificationHelperDto.getGoldGifted()));
			}

			if (notificationHelperDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				configureVoucherParameters(additionalParam, notificationHelperDto, emailRequestDto.getLanguage());
			}

			if (notificationHelperDto.getGiftingHistory().getScheduledDate().after(new Date())) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String timeIs = sdf.format(notificationHelperDto.getGiftingHistory().getScheduledDate());
				additionalParam.put(SCHEDULED_TIME, timeIs);
			}
			emailRequestDto.setAdditionalParameters(additionalParam);

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.NOTIFICATION_DTO,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_EMAIL, emailRequestDto);

			eventHandler.publishEmail(emailRequestDto);

		} catch (JmsException jme) {

			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_EMAIL, jme.getClass() + jme.getMessage(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());

		} catch (UnsupportedEncodingException uee) {
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_EMAIL, uee.getClass() + uee.getMessage(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

	}

	private void configureVoucherParameters(Map<String, String> additionalParam,
			NotificationHelperDto notificationHelperDto, String language) {
		Voucher voucher = voucherRepository
				.findByVoucherCode(notificationHelperDto.getGiftingHistory().getVoucherCode());
		LOG.info("Voucher is : {}", voucher);
		if (voucher != null && voucher.getOfferInfo() != null) {
			additionalParam.put(GiftingConstants.EMAIL_VOUCER_EXPIRY_DATE, voucher.getExpiryDate().toString());
			additionalParam.put(GiftingConstants.EMAIL_TRANSAC_ID,
					voucher.getUuid() != null ? voucher.getUuid().getId() : "");
			additionalParam.put(GiftingConstants.EMAIL_AMOUNT_PAID,
					voucher.getVoucherValues() != null ? Double.toString(voucher.getVoucherValues().getCost()) : "");
			additionalParam.put(GiftingConstants.EMAIL_PAYMENT_METHOD,
					voucher.getUuid() != null ? voucher.getUuid().getPaymentMethod() : "");
			additionalParam.put(GiftingConstants.EMAIL_PAYMENT_DATE,
					voucher.getUuid() != null ? voucher.getUuid().getCreatedDate().toString() : "");

			OfferCatalog offerCatalog = offerRepository.findByOfferId(voucher.getOfferInfo().getOffer());
			String lang = (null == language || GiftingConstants.LANGUAGE_ENGLISH_LIST.contains(language.toUpperCase()))
					? offerCatalog.getOffer().getOfferTitle().getOfferTitleEn()
					: offerCatalog.getOffer().getOfferTitle().getOfferTitleAr();

			additionalParam.put(GiftingConstants.EMAIL_OFFER_DESC, lang);

			MarketplaceImage image = imageRepository.findByDomainIdAndDomainNameAndChannelAndImageType(
					voucher.getMerchantCode(), "SAPP", "MERCHANT", "LISTING");
			if (image != null) {
				additionalParam.put(GiftingConstants.EMAIL_OFFER_URL, image.getImageUrl());
			}
		}
		additionalParam.put(GiftingConstants.EMAIL_VOUCER_DELV_DATE,
				notificationHelperDto.getGiftingHistory().getScheduledDate().toString());
	}

	/**
	 * This method is used to send a push notification to the sender once received
	 * has viewed the gift.
	 * 
	 * @param senderAccountNumber
	 * @param senderFirstName
	 * @param receiverFirstName
	 * @param language
	 * @throws MarketplaceException
	 */
	public void pushNotificationViewedGift(String senderAccountNumber, String senderMembershipCode,
			String senderFirstName, String receiverFirstName, String language, String externalTransactionId) {

		try {

			PushNotificationRequestDto notificationDto = new PushNotificationRequestDto();

			if (null == language || 1 == Utility.checkLanguage(language))
				notificationDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_EN);

			if (null != language && 2 == Utility.checkLanguage(language))
				notificationDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_AR);

			notificationDto.setTransactionId(externalTransactionId);
			notificationDto.setNotificationId(GiftingConstants.LIST_GIFT_HISTORY_NOTIFICATION_ID);
			notificationDto.setNotificationCode(GiftingConstants.NOTIFICATION_CODE);
			notificationDto.setAccountNumber(senderAccountNumber);
			notificationDto.setMembershipCode(senderMembershipCode);

			Map<String, String> additionalParam = new HashMap<>();
			additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_NOTIFICATION_SENDER_NAME, senderFirstName);
			additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_NOTIFICATION_RECEIVER_NAME, receiverFirstName);
			notificationDto.setAdditionalParameters(additionalParam);

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.NOTIFICATION_DTO,
					this.getClass().getName(),
					GiftingConfigurationConstants.GIFTING_SERVICE_LIST_GIFT_HISTORY_PUSH_NOTIFICATION, notificationDto);

			eventHandler.publishPushNotification(notificationDto);

		} catch (JmsException jme) {

			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_LIST_GIFT_HISTORY_PUSH_NOTIFICATION,
					jme.getClass() + jme.getMessage(), GiftingCodes.GIFTING_SPECIFIC_HISTORY_PUSH_NOTIFICATION_FAILURE)
							.printMessage());

		} catch (Exception e) {
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_LIST_GIFT_HISTORY_PUSH_NOTIFICATION,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

	}

	/**
	 * This method is used to send an email to the sender once received has viewed
	 * the gift.
	 * 
	 * @param giftingHistory
	 * @param offerDesc
	 * @param language
	 * @param headers
	 */
	public void emailViewedGift(GiftingHistory giftingHistory, String offerDesc, String language, Headers headers) {

		try {
			LOG.info("emailViewedGift :: giftingHistory : {}", giftingHistory);
			EmailRequestDto emailRequestDto = new EmailRequestDto();

			emailRequestDto.setTransactionId(headers.getExternalTransactionId());
			emailRequestDto.setNotificationId(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_NOTIFICATION_ID);
			emailRequestDto.setNotificationCode(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_NOTIFICATION_CODE);

			if (null == language || (GiftingConstants.LANGUAGE_ENGLISH_LIST.contains(language.toUpperCase()))) {

				emailRequestDto.setTemplateId(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_TEMPLATE_ID_ENGLISH);
				emailRequestDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_EN);

			} else {

				emailRequestDto.setTemplateId(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_TEMPLATE_ID_ARABIC);
				emailRequestDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_AR);

			}

			Map<String, String> additionalParam = new HashMap<>();
			if (null != giftingHistory.getSenderInfo()) {
				emailRequestDto.setAccountNumber(giftingHistory.getSenderInfo().getAccountNumber());
				emailRequestDto.setEmailId(giftingHistory.getSenderInfo().getEmail());
				additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_SENDER_NAME,
						giftingHistory.getSenderInfo().getFirstName());
			}

			if (null != giftingHistory.getReceiverInfo())
				additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_RECIEVER_NAME,
						giftingHistory.getReceiverInfo().getFirstName());

			additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_IMG_URL, giftingHistory.getImageUrl());
			if (null != offerDesc)
				additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_OFFER_DESC, offerDesc);
			additionalParam.put(GiftingConstants.LIST_GIFT_HISTORY_EMAIL_MSG,
					URLDecoder.decode(giftingHistory.getMessage(), "UTF-8"));

			emailRequestDto.setAdditionalParameters(additionalParam);

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.NOTIFICATION_DTO,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_SERVICE_LIST_GIFT_HISTORY_EMAIL,
					emailRequestDto);

			eventHandler.publishEmail(emailRequestDto);

		} catch (JmsException jme) {

			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_LIST_GIFT_HISTORY_EMAIL,
					jme.getClass() + jme.getMessage(), GiftingCodes.GIFTING_SPECIFIC_HISTORY_EMAIL_FAILURE)
							.printMessage());

		} catch (Exception e) {
			LOG.error(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_SERVICE_LIST_GIFT_HISTORY_EMAIL,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

	}

}