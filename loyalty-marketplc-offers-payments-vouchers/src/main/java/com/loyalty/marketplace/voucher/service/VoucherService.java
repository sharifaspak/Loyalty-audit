package com.loyalty.marketplace.voucher.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.dto.PushNotificationRequestDto;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.outbound.database.entity.ReconciliationDataInfo;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherUploadListResponse;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

@Service
@RefreshScope
public class VoucherService {

	@Autowired
	EventHandler eventHandler;

	@Value("${smilesPortal.uri}")
	private String smilesPortal;

	@Value("${userVerifyToken.path}")
	private String userVerifyToken;

	@Value("#{'${reconcile.emailId}'.split(',')}")
	private List<String> reconcileEmailId;

	@Value("${admin.portal.link.uri}")
	private String adminPortalLink;

	@Value("${partnerManagement.uri}")
	private String partnerManagement;

	@Value("${merchant.store.uri}")
	private String merchantStoreUri;

	@Value("${partnerCodeUrl}")
	private String partnerCodeUrl;

	@Value("${merchantsCodeUrl}")
	private String merchantsCodeUrl;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	@Autowired
	private ServiceHelper serviceHelper;
	
	@Autowired
	private RetryTemplate retryTemplate;

	ObjectMapper mapper = new ObjectMapper();

	private static final String VOUCHER_SERVICE = "VoucherService";

	private static final Logger LOG = LoggerFactory.getLogger(VoucherService.class);

	public void sendEmail(String accountNumber, String merchantName, String merchantEmail, String offerId, String externalTransactionId)
			throws VoucherManagementException {
		EmailRequestDto emailDto = new EmailRequestDto();

		Map<String, String> additionalParam = new HashMap<>();
		additionalParam.put(VoucherConstants.MERCHANT_NAME_NO_VOUCHER_CODE, merchantName);
		additionalParam.put(VoucherConstants.OFFER_ID_NO_VOUCHER_CODE, offerId);
		additionalParam.put(VoucherConstants.ADMIN_PORTAL_LINK, adminPortalLink);

		emailDto.setTransactionId(externalTransactionId);
		emailDto.setTemplateId(VoucherConstants.VOUCHER_GEN_TEMPLATE_ID);
		emailDto.setNotificationId(VoucherConstants.VOUCHER_GEN_NOTIFICATION_ID);
		emailDto.setNotificationCode(VoucherConstants.VOUCHER_GEN_NOTIFICATION_CODE);
		emailDto.setAdditionalParameters(additionalParam);
		emailDto.setLanguage(VoucherConstants.VOUCHER_GEN_LANGUAGE);
		emailDto.setEmailId(merchantEmail);
		emailDto.setAccountNumber(accountNumber);

		try {
			eventHandler.publishEmail(emailDto);
		} catch (JmsException jme) {
			throw new VoucherManagementException(VOUCHER_SERVICE, "sendEmail", jme.getClass() + jme.getMessage(),
					VoucherManagementCode.VOUCHER_EMAIL_FAILED);
		}

	}

	public void sendEmailVoucherReconcile(Integer loyaltyCount, Double loyaltyAmount, Integer partnerCount,
			Double partnerAmount, List<ReconciliationDataInfo> data, String externalTransactionId) throws VoucherManagementException {
		LOG.info("In reconciliation send email");
		EmailRequestDto emailDto = new EmailRequestDto();
		DateFormat dateFormat = new SimpleDateFormat(VoucherConstants.DATE_FORMAT);

		Map<String, String> additionalParam = new HashMap<>();
		additionalParam.put(VoucherConstants.COUNT_TRANSAC_PARTNER, "");
		additionalParam.put(VoucherConstants.COUNT_TRANSAC_PARTNER, partnerCount.toString());
		additionalParam.put(VoucherConstants.COUNT_TRANSAC_LOYALTY, loyaltyCount.toString());
		additionalParam.put(VoucherConstants.TOTAL_AMOUNT_PARTNER, partnerAmount.toString());
		additionalParam.put(VoucherConstants.TOTAL_AMOUNT_LOYALTY, loyaltyAmount.toString());

		additionalParam.put(VoucherConstants.RECONCILE_DETAIL, "");

		additionalParam.put(VoucherConstants.RECONCILE_DETAIL_VOUCHER_CODE, data.stream().map(e -> e.getVoucherCode())
				.collect(Collectors.toList()).stream().map(Object::toString).collect(Collectors.joining(",")));
		additionalParam.put(VoucherConstants.RECONCILE_DETAIL_PARTNER_CODE, data.stream().map(e -> e.getPartnerCode())
				.collect(Collectors.toList()).stream().map(Object::toString).collect(Collectors.joining(",")));
		additionalParam.put(VoucherConstants.RECONCILE_DETAIL_LOYALTY_ID,
				data.stream().map(e -> e.getLoyaltyTransactionId()!=null? e.getLoyaltyTransactionId(): "null").collect(Collectors.toList()).stream()
						.map(Object::toString).collect(Collectors.joining(",")));
		additionalParam.put(VoucherConstants.RECONCILE_DETAIL_PARTNER_ID,
				data.stream().map(e -> e.getPartnerTransactionId()!=null?e.getPartnerTransactionId():"null").collect(Collectors.toList()).stream()
						.map(Object::toString).collect(Collectors.joining(",")));
		additionalParam.put(VoucherConstants.RECONCILE_DETAIL_VOUCHER_AMT, data.stream().map(e -> e.getVoucherAmount())
				.collect(Collectors.toList()).stream().map(Object::toString).collect(Collectors.joining(",")));
		additionalParam.put(VoucherConstants.RECONCILE_DETAIL_TRANSAC_DATE,
				data.stream().map(e -> dateFormat.format(e.getTransactionDate())).collect(Collectors.toList()).stream()
						.map(Object::toString).collect(Collectors.joining(",")));
		
		emailDto.setTransactionId(externalTransactionId);
		emailDto.setTemplateId(VoucherConstants.VOUCHER_RECON_TEMPLATE_ID);
		emailDto.setNotificationId(VoucherConstants.VOUCHER_RECON_NOTIFICATION_ID);
		emailDto.setNotificationCode(VoucherConstants.VOUCHER_RECON_NOTIFICATION_CODE);
		emailDto.setAdditionalParameters(additionalParam);
		emailDto.setLanguage(VoucherConstants.VOUCHER_RECON_LANGUAGE);
		LOG.info("Email ids are : {}",reconcileEmailId);
		for (String email : reconcileEmailId) {
			emailDto.setEmailId(email);
			try {
				eventHandler.publishEmail(emailDto);
			} catch (JmsException jme) {
				throw new VoucherManagementException(VOUCHER_SERVICE, "sendReconcileMismatchEmail",
						jme.getClass() + jme.getMessage(), VoucherManagementCode.VOUCHER_EMAIL_FAILED);
			}
		}
	}

	public void pushNotificationVoucherExpiry(String externalTransactionId, String language, String accountNumber,
			String membershipCode, Map<String, String> additionalParams) throws VoucherManagementException {

		PushNotificationRequestDto notificationDto = new PushNotificationRequestDto();

		notificationDto.setTransactionId(externalTransactionId);
		notificationDto.setNotificationId(VoucherConstants.VOUCHER_EXPIRY_NOTIFICATION_ID);
		notificationDto.setNotificationCode(VoucherConstants.NOTIFICATION_CODE);
		notificationDto.setLanguage(language);
		notificationDto.setAccountNumber(accountNumber);
		notificationDto.setMembershipCode(membershipCode);
		notificationDto.setAdditionalParameters(additionalParams);

		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.NOTIFICATION_DTO,
				this.getClass().getName(), VoucherConstants.SERVICE_VOUCHER_EXPIRY, notificationDto);

		try {

			eventHandler.publishPushNotification(notificationDto);

		} catch (JmsException jme) {
			throw new VoucherManagementException(VOUCHER_SERVICE, VoucherConstants.SERVICE_VOUCHER_EXPIRY,
					jme.getClass() + jme.getMessage(), VoucherManagementCode.VOUCHER_EXPIRY_NOTIFICATION_FAILED);
		} catch (Exception e) {
			LOG.error(
					new VoucherManagementException(this.getClass().toString(), VoucherConstants.SERVICE_VOUCHER_EXPIRY,
							e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION)
									.printMessage());
		}

	}

	public void sendSMSVoucherStatistics(int voucherCount, int voucherCountSpecial, List<String> contactNumbers,
			Headers header) throws VoucherManagementException {

		SMSRequestDto smsRequestDto = new SMSRequestDto();

		Map<String, String> additionalParam = new HashMap<>();
		additionalParam.put(VoucherConstants.V_COUNT_NORMAL, String.valueOf(voucherCount));
		additionalParam.put(VoucherConstants.V_COUNT_SPECIAL, String.valueOf(voucherCountSpecial));

		smsRequestDto.setTransactionId(header.getExternalTransactionId());
		smsRequestDto.setAdditionalParameters(additionalParam);
		smsRequestDto.setDestinationNumber(contactNumbers);
		smsRequestDto.setTemplateId(VoucherConstants.VOUCHER_STATS_TEMPLATE_ID);
		smsRequestDto.setNotificationId(VoucherConstants.VOUCHER_STATS_NOTIFICATION_ID);
		smsRequestDto.setLanguage(VoucherConstants.NOTIFICATION_LANGUAGE_EN);
		smsRequestDto.setNotificationCode(VoucherConstants.NOTIFICATION_CODE);

		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.NOTIFICATION_DTO,
				this.getClass().getName(), VoucherConstants.SERVICE_VOUCHER_STATISTICS, smsRequestDto.toString());

		try {

			eventHandler.publishAlertSms(smsRequestDto);

		} catch (JmsException jme) {
			throw new VoucherManagementException(VOUCHER_SERVICE, VoucherConstants.SERVICE_VOUCHER_STATISTICS,
					jme.getClass() + jme.getMessage(), VoucherManagementCode.VOUCHER_STATISTICS_NOTIFICATION_FAILED);
		}

	}

	public void sendSMSVoucherCount(String merchantName, String offerTitle, String availableVoucherCount,
			List<String> contactNumbers, Headers header, String type) throws VoucherManagementException {

		SMSRequestDto smsRequestDto = new SMSRequestDto();

		smsRequestDto.setTransactionId(header.getExternalTransactionId());
		smsRequestDto.setDestinationNumber(contactNumbers);
		smsRequestDto.setLanguage(VoucherConstants.NOTIFICATION_LANGUAGE_EN);
		smsRequestDto.setNotificationCode(VoucherConstants.NOTIFICATION_CODE);

		Map<String, String> additionalParam = new HashMap<>();

		if (null != type && type.equalsIgnoreCase(VoucherConstants.VOUCHER_COUNT_TYPE_ZERO)) {

			additionalParam.put(VoucherConstants.MERCHANT_NAME, merchantName);
			additionalParam.put(VoucherConstants.OFFER_TITLE, offerTitle);
			smsRequestDto.setTemplateId(VoucherConstants.VOUCHER_COUNT_TYPE_ZERO_TEMPLATE_ID);
			smsRequestDto.setNotificationId(VoucherConstants.VOUCHER_COUNT_TYPE_ZERO_NOTIFICATION_ID);

		} else if (null != type && type.equalsIgnoreCase(VoucherConstants.VOUCHER_COUNT_TYPE_15_PERC)) {

			additionalParam.put(VoucherConstants.MERCHANT_NAME, merchantName);
			additionalParam.put(VoucherConstants.OFFER_TITLE, offerTitle);
			additionalParam.put(VoucherConstants.AVAILABLE_COUNT, availableVoucherCount);
			smsRequestDto.setTemplateId(VoucherConstants.VOUCHER_COUNT_TYPE_15_TEMPLATE_ID);
			smsRequestDto.setNotificationId(VoucherConstants.VOUCHER_COUNT_TYPE_15_NOTIFICATION_ID);

		}

		smsRequestDto.setAdditionalParameters(additionalParam);

		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.NOTIFICATION_DTO,
				this.getClass().getName(), VoucherConstants.SERVICE_VOUCHER_COUNT, smsRequestDto);

		try {

			eventHandler.publishAlertSms(smsRequestDto);

		} catch (JmsException jme) {
			throw new VoucherManagementException(VOUCHER_SERVICE, VoucherConstants.SERVICE_VOUCHER_COUNT,
					jme.getClass() + jme.getMessage(), VoucherManagementCode.VOUCHER_COUNT_NOTIFICATION_FAILED);
		}

	}

	public List<String> getMerchantsForPartner(String partnerCode, Headers headers,
		VoucherUploadListResponse resultResponse) throws VoucherManagementException {
		List<String> mechantCodes = new ArrayList<>();
		try {
			
			HttpEntity<?> entity = new HttpEntity<Object>(serviceHelper.getHeader(headers));
			final String url = merchantStoreUri + partnerCodeUrl + partnerCode + merchantsCodeUrl;
			LOG.info("Partner url is {}", url);
			String jsonStr = retryCallForGetMerchantsForPartner(url, entity);

			Map map = mapper.readValue(jsonStr, new TypeReference<HashMap>() {});
			Set<Entry> keys = map.entrySet();
			for (Entry key : keys) {
				LOG.info("Key {}", key.getKey());
				LOG.info("Value {}", key.getValue());
				if (((String) key.getKey()).equalsIgnoreCase("listMerchants") && null != key.getValue()) {
					Gson gson = new Gson();
					String listMerchantJsonStr = gson.toJson(key.getValue());
					LOG.info("List merchant JSON String : {}",listMerchantJsonStr);
					
					JSONArray jsonarray = new JSONArray(listMerchantJsonStr);
					for (int i = 0; i < jsonarray.length(); i++) {
					    JSONObject jsonobject = jsonarray.getJSONObject(i);					   			   
						mechantCodes.add(jsonobject.getString("merchantCode"));
					}
					
				}
			}
			return mechantCodes;
		} catch (RestClientException rce) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES_PARTNER_ERROR.getIntId(),
					VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES_PARTNER_ERROR.getMsg());
			throw new VoucherManagementException(VOUCHER_SERVICE, "getMerchantsForPartner",
					rce.getClass() + rce.getMessage(), VoucherManagementCode.VOUCHER_EMAIL_FAILED);

		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(e.hashCode(), e.getMessage());
			throw new VoucherManagementException(VOUCHER_SERVICE, "getMerchantsForPartner",
					e.getClass() + e.getMessage(), VoucherManagementCode.VOUCHER_EMAIL_FAILED);
		}
	}
	
	private String retryCallForGetMerchantsForPartner(String url, HttpEntity<?> entity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForGetMerchantsForPartner method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return getRestTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
		});		
	}

}
