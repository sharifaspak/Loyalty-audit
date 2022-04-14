package com.loyalty.marketplace.banners.outbound.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.banners.constants.BannerCodes;
import com.loyalty.marketplace.banners.constants.BannerConstants;
import com.loyalty.marketplace.banners.constants.BannerRequestMappingConstants;
import com.loyalty.marketplace.banners.outbound.dto.AuthorizationRequest;
import com.loyalty.marketplace.banners.outbound.dto.AuthorizationResponse;
import com.loyalty.marketplace.banners.outbound.dto.BannerListResponseDto;
import com.loyalty.marketplace.banners.outbound.dto.BannerResultResponse;
import com.loyalty.marketplace.banners.outbound.dto.BannerServiceListResponse;
import com.loyalty.marketplace.banners.outbound.dto.BannerServiceResponse;
import com.loyalty.marketplace.banners.outbound.dto.BannerServiceWithListResponse;
import com.loyalty.marketplace.banners.outbound.dto.CreateBannerRequestDto;
import com.loyalty.marketplace.banners.outbound.dto.UpdateBannerRequestDto;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;

import sun.misc.BASE64Encoder;

@Component
public class BannerService {

	private static final Logger LOG = LoggerFactory.getLogger(BannerService.class);
	private static final String URL = "Url : {}";
	private static final String REQUEST = "Request : {}";
	private static final String RESPONSE = "Response : {}";

	@Value("${banners.authentication.username}")
	private String username;

	@Value("${banners.authentication.password}")
	private String password;

	@Value("${banners.authentication.channel}")
	private String channel;

	@Value("${banners.authenticate.url}")
	private String authenticateUrl;

	@Value("${banners.add.url}")
	private String addNewBannerUrl;

	@Value("${banners.update.url}")
	private String updateBannerUrl;

	@Value("${banners.delete.url}")
	private String deleteBannerUrl;

	@Value("${banners.list.url}")
	private String listBannersUrl;

	@Value("${banners.list.detail.url}")
	private String listBannerSpecificUrl;

	@Autowired
//	@Qualifier("customRestTemplateBean")
	private RestTemplate restTemplate;

	@Value("${aes.account.key}")
	public String accountPinKey;

	@Value("${aes.account.initVector}")
	public String accountPinVector;

	@Autowired
	EventHandler eventHandler;

	@Autowired
	BannerServiceHelper serviceHelper;

	@Autowired
	RetryTemplate retryTemplate;

	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;

	/***
	 * 
	 * @param value
	 * @return
	 */
	public String encrypt(String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(accountPinVector.getBytes(StandardCharsets.UTF_8));
			SecretKeySpec skeySpec = new SecretKeySpec(accountPinKey.getBytes(StandardCharsets.UTF_8), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return new BASE64Encoder().encode(encrypted);
		} catch (Exception ex) {
			LOG.error("Error", ex);
		}
		return null;
	}

	/**
	 * 
	 * @param resultResponse
	 * @return authentication token
	 * @throws MarketplaceException
	 */
	public String fetchBannerToken(ResultResponse resultResponse, Headers header) throws MarketplaceException {

		LOG.info("Inside fetchBannerToken in class {}", this.getClass().getName());
		String token = null;
		AuthorizationRequest request = null;
		try {
			request = new AuthorizationRequest(username, encrypt(password), channel);
			ResponseEntity<AuthorizationResponse> responseEntity = getTokenCallWithRetry(header, request);

			if (!ObjectUtils.isEmpty(responseEntity)) {

				AuthorizationResponse response = responseEntity.getBody();
				if (!ObjectUtils.isEmpty(response)) {

					if (!StringUtils.isEmpty(response.getToken())) {

						token = response.getToken();

					} else if (!StringUtils.isEmpty(response.getResponseMsg())) {

						resultResponse.addErrorAPIResponse(BannerCodes.ERROR_IN_GETTING_TOKEN.getIntId(),
								BannerCodes.ERROR_IN_GETTING_TOKEN.getMsg() + response.getResponseMsg());
					}
				}

			} else {

				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getIntId(),
						BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getMsg());
			}

		} catch (Exception e) {

			eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
					BannerConstants.BANNER_AUTHENTICATION.get(), null, true, false, request, e.getMessage()));
			throw new MarketplaceException(this.getClass().toString(), "fetchBannerToken",
					e.getClass() + e.getMessage(), BannerCodes.BANNER_SERVICE_EXCEPTION);

		}

		String log = Logs.logForVariable(RequestMappingConstants.TOKEN, token);
		LOG.info(log);
		LOG.info("Leaving fetchBannerToken in class {}", this.getClass().getName());
		return token;
	}

	/***
	 * 
	 * @param header
	 * @param header
	 * @param request
	 * @return token for authentication
	 */
	private ResponseEntity<AuthorizationResponse> getTokenCallWithRetry(Headers header, AuthorizationRequest request) {

		HttpEntity<?> requestEntity = new HttpEntity<>(request, serviceHelper.getBannerHeaders(header, false, null));
		LOG.info(URL, authenticateUrl);
		LOG.info(REQUEST, requestEntity);
		try {
			return retryTemplate.execute(context -> {
				LOG.info("inside Retry block using retryTemplate of getTokenCallWithRetry in class {}",
						this.getClass().getName());
				retryLogsService.saveRestRetrytoRetryLogs(authenticateUrl.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				long start = System.currentTimeMillis();
				ResponseEntity<AuthorizationResponse> responseEntity = restTemplate.exchange(authenticateUrl,
						HttpMethod.POST, requestEntity, AuthorizationResponse.class);
				LOG.info(RESPONSE, responseEntity);
				eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
						BannerConstants.BANNER_AUTHENTICATION.get(), System.currentTimeMillis() - start, true,
						!ObjectUtils.isEmpty(responseEntity) && !ObjectUtils.isEmpty(responseEntity.getBody())
								&& !StringUtils.isEmpty(responseEntity.getBody().getToken()),
						request, !ObjectUtils.isEmpty(responseEntity) ? responseEntity.getBody() : null));
				return responseEntity;
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), authenticateUrl.toString());
		}
		return null;

	}

	/**
	 * 
	 * @param token
	 * @param bannerListRequest
	 * @param resultResponse
	 * @return status after adding banner details
	 * @throws MarketplaceException
	 */
	public String addNewBanner(String token, CreateBannerRequestDto banners, ResultResponse resultResponse,
			Headers header) throws MarketplaceException {

		LOG.info("Inside addNewBanner in class {}", this.getClass().getName());
		String id = null;
		try {

			ResponseEntity<BannerServiceWithListResponse> responseEntity = createNewBannerWithRetry(header, banners,
					token);

			if (!ObjectUtils.isEmpty(responseEntity)) {

				BannerServiceWithListResponse response = responseEntity.getBody();

				if (!ObjectUtils.isEmpty(response) && (StringUtils.isEmpty(response.getResponseCode())
						|| !response.getResponseCode().equals(BannerConstants.BANNER_SUCCESS_CODE.get()))) {

					resultResponse.addErrorAPIResponse(BannerCodes.BANNER_CREATION_ERROR.getIntId(),
							response.getResponseMsg());

				} else if (!ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getBanners())
						&& !ObjectUtils.isEmpty(response.getBanners().get(0))
						&& !ObjectUtils.isEmpty(response.getBanners().get(0).getId())) {

					id = String.valueOf(response.getBanners().get(0).getId());

				}

			} else {

				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getIntId(),
						BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getMsg());
			}

		} catch (Exception e) {

			eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
					BannerConstants.BANNER_CREATION.get(), null, true, false, banners, e.getMessage()));
			throw new MarketplaceException(this.getClass().toString(), "addNewBanner", e.getClass() + e.getMessage(),
					BannerCodes.BANNER_SERVICE_EXCEPTION);
		}
		LOG.info("Leaving addNewBanner in class {}", this.getClass().getName());
		return id;
	}

	/**
	 * 
	 * @param header
	 * @param banners
	 * @param token
	 * @return create banner response
	 */
	private ResponseEntity<BannerServiceWithListResponse> createNewBannerWithRetry(Headers header,
			CreateBannerRequestDto banners, String token) {

		HttpEntity<?> requestEntity = new HttpEntity<>(banners, serviceHelper.getBannerHeaders(header, true, token));
		LOG.info(URL, addNewBannerUrl);
		LOG.info(REQUEST, requestEntity);
		try {
			return retryTemplate.execute(context -> {
				LOG.info("inside Retry block using retryTemplate of createNewBannerWithRetry in class {}",
						this.getClass().getName());
				retryLogsService.saveRestRetrytoRetryLogs(addNewBannerUrl.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				long start = System.currentTimeMillis();
				ResponseEntity<BannerServiceWithListResponse> responseEntity = restTemplate.exchange(addNewBannerUrl,
						HttpMethod.POST, requestEntity, BannerServiceWithListResponse.class);
				LOG.info(RESPONSE, responseEntity);
				eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
						BannerConstants.BANNER_CREATION.get(), System.currentTimeMillis() - start, true,
						!ObjectUtils.isEmpty(responseEntity) && !ObjectUtils.isEmpty(responseEntity.getBody())
								&& !StringUtils.isEmpty(responseEntity.getBody().getResponseCode())
								&& responseEntity.getBody().getResponseCode()
										.equals(BannerConstants.BANNER_SUCCESS_CODE.get()),
						requestEntity, !ObjectUtils.isEmpty(responseEntity) ? responseEntity.getBody() : null));
				return responseEntity;
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), addNewBannerUrl.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param token
	 * @param bannerRequest
	 * @param resultResponse
	 * @return status after updating banner details
	 * @throws MarketplaceException
	 */
	public void updateExistingBanner(String token, UpdateBannerRequestDto bannerRequest, ResultResponse resultResponse,
			Headers header) throws MarketplaceException {

		LOG.info("Inside updateExistingBanner in class {}", this.getClass().getName());
		try {

			ResponseEntity<BannerServiceResponse> responseEntity = updateBannerWithRetry(header, token, bannerRequest);

			if (!ObjectUtils.isEmpty(responseEntity)) {

				BannerServiceResponse response = responseEntity.getBody();

				if (!ObjectUtils.isEmpty(response) && (StringUtils.isEmpty(response.getResponseCode())
						|| !response.getResponseCode().equals(BannerConstants.BANNER_SUCCESS_CODE.get()))) {

					resultResponse.addErrorAPIResponse(BannerCodes.BANNER_UPDATION_ERROR.getIntId(),
							response.getResponseMsg());

				}

			} else {

				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getIntId(),
						BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getMsg());
			}

		} catch (Exception e) {

			eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
					BannerConstants.BANNER_UPDATION.get(), null, true, false, bannerRequest, e.getMessage()));
			throw new MarketplaceException(this.getClass().toString(), "updateExistingBanner",
					e.getClass() + e.getMessage(), BannerCodes.BANNER_SERVICE_EXCEPTION);
		}
		LOG.info("Leaving updateExistingBanner in class {}", this.getClass().getName());
	}

	/**
	 * 
	 * @param header
	 * @param token
	 * @param bannerRequest
	 * @return updated banner response
	 */
	private ResponseEntity<BannerServiceResponse> updateBannerWithRetry(Headers header, String token,
			UpdateBannerRequestDto bannerRequest) {

		HttpEntity<?> requestEntity = new HttpEntity<>(bannerRequest,
				serviceHelper.getBannerHeaders(header, true, token));
		LOG.info(URL, updateBannerUrl);
		LOG.info(REQUEST, requestEntity);
		try {
			return retryTemplate.execute(context -> {
				LOG.info("inside Retry block using retryTemplate of updateBannerWithRetry in class {}",
						this.getClass().getName());
				retryLogsService.saveRestRetrytoRetryLogs(updateBannerUrl.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				long start = System.currentTimeMillis();
				ResponseEntity<BannerServiceResponse> responseEntity = restTemplate.exchange(updateBannerUrl,
						HttpMethod.PUT, requestEntity, BannerServiceResponse.class);
				LOG.info(RESPONSE, responseEntity);
				eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
						BannerConstants.BANNER_UPDATION.get(), System.currentTimeMillis() - start, true,
						!ObjectUtils.isEmpty(responseEntity) && !ObjectUtils.isEmpty(responseEntity.getBody())
								&& !StringUtils.isEmpty(responseEntity.getBody().getResponseCode())
								&& responseEntity.getBody().getResponseCode()
										.equals(BannerConstants.BANNER_SUCCESS_CODE.get()),
						requestEntity, !ObjectUtils.isEmpty(responseEntity) ? responseEntity.getBody() : null));
				return responseEntity;
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), updateBannerUrl.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param token
	 * @param resultResponse
	 * @return list of all banners
	 * @throws MarketplaceException
	 */
	public List<BannerListResponseDto> fetchAllBanners(String token, BannerResultResponse resultResponse,
			Headers header) throws MarketplaceException {

		LOG.info("Inside fetchAllBanners in class {}", this.getClass().getName());
		List<BannerListResponseDto> bannerList = null;
		try {

			ResponseEntity<BannerServiceListResponse> responseEntity = listAllBannersWithRetry(header, token);

			if (!ObjectUtils.isEmpty(responseEntity.getBody())) {

				BannerServiceListResponse response = responseEntity.getBody();

				if (!ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getBanners())) {

					bannerList = new ArrayList<>(1);
					bannerList.addAll(response.getBanners());

				} else {

					resultResponse.addErrorAPIResponse(BannerCodes.BANNER_LISTING_ERROR.getIntId(),
							BannerCodes.BANNER_LISTING_ERROR.getMsg());

				}

			} else {

				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getIntId(),
						BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getMsg());
			}

		} catch (Exception e) {

			eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
					BannerConstants.BANNER_LISTING.get(), null, true, false, null, e.getMessage()));
			throw new MarketplaceException(this.getClass().toString(), "fetchAllBanners", e.getClass() + e.getMessage(),
					BannerCodes.BANNER_SERVICE_EXCEPTION);
		}
		LOG.info("Leaving fetchAllBanners in class {}", this.getClass().getName());
		return bannerList;
	}

	/**
	 * 
	 * @param header
	 * @param token
	 * @return
	 */
	private ResponseEntity<BannerServiceListResponse> listAllBannersWithRetry(Headers header, String token) {

		HttpEntity<?> requestEntity = new HttpEntity<>(serviceHelper.getBannerHeaders(header, true, token));
		LOG.info(URL, authenticateUrl);
		LOG.info(REQUEST, requestEntity);
		try {
			return retryTemplate.execute(context -> {
				LOG.info("inside Retry block using retryTemplate of listAllBannersWithRetry in class {}",
						this.getClass().getName());
				retryLogsService.saveRestRetrytoRetryLogs(listBannersUrl.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				long start = System.currentTimeMillis();
				ResponseEntity<BannerServiceListResponse> responseEntity = restTemplate.exchange(listBannersUrl,
						HttpMethod.GET, requestEntity, BannerServiceListResponse.class);
				LOG.info(RESPONSE, responseEntity);
				eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
						BannerConstants.BANNER_LISTING.get(), System.currentTimeMillis() - start, true,
						!ObjectUtils.isEmpty(responseEntity) && !ObjectUtils.isEmpty(responseEntity.getBody())
								&& !CollectionUtils.isEmpty(responseEntity.getBody().getBanners()),
						requestEntity, !ObjectUtils.isEmpty(responseEntity) ? responseEntity.getBody() : null));
				return responseEntity;
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), listBannersUrl.toString());
		}
		return null;

	}

	/**
	 * 
	 * @param token
	 * @param bannerId
	 * @param resultResponse
	 * @return details of specific banner
	 * @throws MarketplaceException
	 */
	public List<BannerListResponseDto> fetchSpecificBanner(String token, String bannerId, ResultResponse resultResponse,
			Headers header) throws MarketplaceException {

		LOG.info("Inside fetchSpecificBanner in class {}", this.getClass().getName());
		List<BannerListResponseDto> bannerList = null;
		try {

			ResponseEntity<BannerServiceWithListResponse> responseEntity = fetchBannerDetailWithRetry(header, token,
					bannerId);

			if (!ObjectUtils.isEmpty(responseEntity.getBody())) {

				BannerServiceWithListResponse response = responseEntity.getBody();

				if (!ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getBanners())) {

					bannerList = new ArrayList<>(1);
					bannerList.addAll(response.getBanners());

				} else {

					resultResponse.addErrorAPIResponse(BannerCodes.BANNER_LISTING_SPECIFIC_ERROR.getIntId(),
							!ObjectUtils.isEmpty(response) ? response.getResponseMsg()
									: BannerCodes.BANNER_LISTING_SPECIFIC_ERROR.getMsg());

				}

			} else {

				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getIntId(),
						BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getMsg());
			}

		} catch (Exception e) {

			eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
					BannerConstants.BANNER_DETAIL.get(), null, true, false, null, e.getMessage()));
			throw new MarketplaceException(this.getClass().toString(), "fetchSpecificBanner",
					e.getClass() + e.getMessage(), BannerCodes.BANNER_SERVICE_EXCEPTION);
		}
		LOG.info("Leaving fetchSpecificBanner in class {}", this.getClass().getName());
		return bannerList;
	}

	/**
	 * 
	 * @param header
	 * @param token
	 * @param bannerId
	 * @return
	 */
	private ResponseEntity<BannerServiceWithListResponse> fetchBannerDetailWithRetry(Headers header, String token,
			String bannerId) {

		Map<String, String> params = new HashMap<>();
		params.put(BannerRequestMappingConstants.ID, bannerId);
		HttpEntity<?> requestEntity = new HttpEntity<>(serviceHelper.getBannerHeaders(header, true, token));
		LOG.info(URL, listBannerSpecificUrl);
		LOG.info(REQUEST, requestEntity);
		try {
			return retryTemplate.execute(context -> {
				LOG.info("inside Retry block using retryTemplate of fetchBannerDetailWithRetry in class {}",
						this.getClass().getName());
				retryLogsService.saveRestRetrytoRetryLogs(listBannerSpecificUrl.toString(),
						header.getExternalTransactionId(), requestEntity.toString(), header.getUserName());
				long start = System.currentTimeMillis();
				ResponseEntity<BannerServiceWithListResponse> responseEntity = restTemplate.exchange(
						listBannerSpecificUrl, HttpMethod.GET, requestEntity, BannerServiceWithListResponse.class,
						params);
				LOG.info(RESPONSE, responseEntity);
				eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
						BannerConstants.BANNER_DETAIL.get(), System.currentTimeMillis() - start, true,
						!ObjectUtils.isEmpty(responseEntity) && !ObjectUtils.isEmpty(responseEntity.getBody())
								&& !StringUtils.isEmpty(responseEntity.getBody().getResponseCode())
								&& responseEntity.getBody().getResponseCode()
										.equals(BannerConstants.BANNER_SUCCESS_CODE.get()),
						requestEntity, !ObjectUtils.isEmpty(responseEntity) ? responseEntity.getBody() : null));
				return responseEntity;
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), listBannerSpecificUrl.toString());
		}
		return null;

	}

	/**
	 * 
	 * @param token
	 * @param bannerId
	 * @param resultResponse
	 * @return status after deleting banner
	 * @throws MarketplaceException
	 */
	public void deleteExistingBanner(String token, String bannerId, ResultResponse resultResponse, Headers header)
			throws MarketplaceException {

		LOG.info("Inside deleteExistingBanner in class {}", this.getClass().getName());
		try {

			ResponseEntity<BannerServiceResponse> responseEntity = deleteBannerWithRetry(header, token, bannerId);

			if (!ObjectUtils.isEmpty(responseEntity.getBody())) {

				BannerServiceResponse response = responseEntity.getBody();

				if (!ObjectUtils.isEmpty(response) && (StringUtils.isEmpty(response.getResponseCode())
						|| !response.getResponseCode().equals(BannerConstants.BANNER_SUCCESS_CODE.get()))) {

					resultResponse.addErrorAPIResponse(BannerCodes.BANNER_DELETION_ERROR.getIntId(),
							response.getResponseMsg());

				}

			} else {

				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getIntId(),
						BannerCodes.BANNER_SERVICE_IMPROPER_RESPONSE.getMsg());
			}

		} catch (Exception e) {

			eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
					BannerConstants.BANNER_DELETION.get(), null, true, false, null, e.getMessage()));
			throw new MarketplaceException(this.getClass().toString(), "deleteExistingBanner",
					e.getClass() + e.getMessage(), BannerCodes.BANNER_SERVICE_EXCEPTION);
		}
		LOG.info("Leaving deleteExistingBanner in class {}", this.getClass().getName());
	}

	/***
	 * 
	 * @param header
	 * @param token
	 * @param bannerId
	 * @return
	 */
	private ResponseEntity<BannerServiceResponse> deleteBannerWithRetry(Headers header, String token, String bannerId) {

		Map<String, String> params = new HashMap<>();
		params.put(BannerRequestMappingConstants.ID, bannerId);

		HttpEntity<?> requestEntity = new HttpEntity<>(serviceHelper.getBannerHeaders(header, true, token));
		LOG.info(URL, deleteBannerUrl);
		LOG.info(REQUEST, requestEntity);
		try {
			return retryTemplate.execute(context -> {
				LOG.info("inside Retry block using retryTemplate of deleteBannerWithRetry in class {}",
						this.getClass().getName());
				retryLogsService.saveRestRetrytoRetryLogs(deleteBannerUrl.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				long start = System.currentTimeMillis();
				ResponseEntity<BannerServiceResponse> responseEntity = restTemplate.exchange(deleteBannerUrl,
						HttpMethod.DELETE, requestEntity, BannerServiceResponse.class, params);
				LOG.info(RESPONSE, responseEntity);
				eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header,
						BannerConstants.BANNER_DELETION.get(), System.currentTimeMillis() - start, true,
						!ObjectUtils.isEmpty(responseEntity) && !ObjectUtils.isEmpty(responseEntity.getBody())
								&& !StringUtils.isEmpty(responseEntity.getBody().getResponseCode())
								&& responseEntity.getBody().getResponseCode()
										.equals(BannerConstants.BANNER_SUCCESS_CODE.get()),
						requestEntity, !ObjectUtils.isEmpty(responseEntity) ? responseEntity.getBody() : null));
				return responseEntity;
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), deleteBannerUrl.toString());
		}
		return null;

	}

}
