package com.loyalty.marketplace.voucher.outbound.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.member.management.inbound.dto.ListMemberDetailRequestDto;
import com.loyalty.marketplace.voucher.member.management.inbound.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.EnrollmentResult;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.EnrollmentResultResponse;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.GetListMemberResponse;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.MemberResponseDto;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;

@Service
@RefreshScope
public class VoucherMemberManagementService {

	@Value("${memberManagement.uri}")
	private String memberManagementUri;

	@Value("${memberManagement.getMember.path}")
	private String memberPath;

	@Value("${memberManagement.listAllMember.path}")
	private String listAllMemberPath;

	@Value("${memberManagement.configureTier.path}")
	private String configureTierPath;

	@Value("${memberManagement.enrollMember.path}")
	private String enrollMemberPath;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("getTemplateBean")
	private RestTemplate getRestTemplate;

	@Autowired
	private ServiceHelper serviceHelper;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private RetryTemplate retryTemplate;
	
	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;

	private static final Logger LOG = LoggerFactory.getLogger(VoucherMemberManagementService.class);
	private static final String GET_MEMBER_RESP = "Respose from Member Management getMemberDetails API call: {}";
	private static final String FETCH_MEM_DET = "Member Management URL to fetch Member Details: {} ";
	private static final String EXTRA_CHAR = "\ufeff";
	private static final String EXTRA_CHAR_QUESTION = "?";

	public boolean checkMemberExists(String accountNumber, Headers header) throws VoucherManagementException {

		LOG.info("Inside checkMemberExists in VoucherMemberManagementService");

		String url = memberManagementUri + memberPath;

		LOG.info(FETCH_MEM_DET, url);
		try {
			MemberDetailRequestDto getMemberRequest = new MemberDetailRequestDto();
			getMemberRequest.setAccountNumber(accountNumber);

			HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(getMemberRequest,
					serviceHelper.getHeader(header));
			ResponseEntity<CommonApiStatus> response = retryCallForMemberDetails(url, requestEntity, header);
			CommonApiStatus commonApiStatus = response.getBody();

			LOG.info(GET_MEMBER_RESP, commonApiStatus);

			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
				return true;
			}

		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "checkMemberExists",
					e.getClass() + e.getMessage(), VoucherManagementCode.MEMBER_MANAGEMENT_EXCEPTION);
		}

		return false;
	}

	public MemberResponseDto getMemberDetails(String validCode, String typeOfId, Headers header)
			throws VoucherManagementException {
		LOG.info("Inside getMemberDetails in VoucherMemberManagementService");

		String url = memberManagementUri + memberPath;

		LOG.info(FETCH_MEM_DET, url);
		MemberResponseDto memberResponse = null;

		try {
			MemberDetailRequestDto getMemberRequest = new MemberDetailRequestDto();
			String code = "";
			if (typeOfId.equalsIgnoreCase("accountNumber")) {
				LOG.info("accountNumber code : {}", validCode);
				code = validCode.replaceAll("[^0-9]", "");
				LOG.info("accountNumber code1 : {}", code);
				getMemberRequest.setAccountNumber(code);
			} else if (typeOfId.equalsIgnoreCase("membershipCode")) {
				LOG.info("membershipCode code : {}", validCode);
				code = validCode.replace(EXTRA_CHAR, "");
				code = code.replace(EXTRA_CHAR_QUESTION, "");
				LOG.info("membershipCode code1 : {}", code);
				getMemberRequest.setMembershipCode(code);
			} else if (typeOfId.equalsIgnoreCase("loyaltyId")) {
				LOG.info("loyaltyId code : {}", validCode);
				code = validCode.replace(EXTRA_CHAR, "");
				code = code.replace(EXTRA_CHAR_QUESTION, "");
				LOG.info("loyaltyId code1 : {}", code);
				getMemberRequest.setLoyaltyId(code);
			}

			LOG.info("{} : {}", OfferConstants.REQUEST_PARAMS_FOR.get(), getMemberRequest);

			HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(getMemberRequest,
					serviceHelper.getHeader(header));

			ResponseEntity<CommonApiStatus> response = retryCallForMemberDetails(url, requestEntity, header);
			CommonApiStatus commonApiStatus = response.getBody();
			LOG.info(GET_MEMBER_RESP, commonApiStatus);

			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
				memberResponse = extractMemberDetails(commonApiStatus);
				LOG.info("Member Details from Member Management API: {} ", memberResponse);
			}

		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "getMemberDetails",
					e.getClass() + e.getMessage(), VoucherManagementCode.MEMBER_MANAGEMENT_EXCEPTION);
		}
		return memberResponse;

	}

	private ResponseEntity<CommonApiStatus> retryCallForMemberDetails(String url,
			HttpEntity<MemberDetailRequestDto> requestEntity, Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForMemberDetails method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				return getRestTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}

	public GetListMemberResponse getListMemberDetails(List<String> accounts, Headers header)
			throws VoucherManagementException {

		LOG.info("Inside getListMemberDetails in VoucherMemberManagementService");

		String url = memberManagementUri + listAllMemberPath;

		LOG.info(FETCH_MEM_DET, url);
		GetListMemberResponse memberResponse = null;

		try {

			ListMemberDetailRequestDto listMemberRequest = new ListMemberDetailRequestDto();
			listMemberRequest.setAccounts(accounts);

			LOG.info(OfferConstants.REQUEST_PARAMS_FOR.get(), "{} get Member details, {}{}, {} ",
					OfferConstants.MESSAGE_SEPARATOR.get(), OfferConstants.SINGLE_MESSAGE.get(), listMemberRequest);

			HttpEntity<ListMemberDetailRequestDto> requestEntity = new HttpEntity<>(listMemberRequest,
					serviceHelper.getHeader(header));

			ResponseEntity<CommonApiStatus> response = retryCallForListMemberDetails(url, requestEntity, header);
			CommonApiStatus commonApiStatus = response.getBody();
			LOG.info(GET_MEMBER_RESP, commonApiStatus);

			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
				Object result = commonApiStatus.getResult();
				if (null != result) {
					memberResponse = (GetListMemberResponse) serviceHelper.convertToObject(result,
							GetListMemberResponse.class);
				}
				LOG.info("List of Member Details from Member Management API: {} ", memberResponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new VoucherManagementException(this.getClass().toString(), "getMemberDetails",
					e.getClass() + e.getMessage(), VoucherManagementCode.MEMBER_MANAGEMENT_EXCEPTION);
		}
		return memberResponse;
	}

	private ResponseEntity<CommonApiStatus> retryCallForListMemberDetails(String url,
			HttpEntity<ListMemberDetailRequestDto> requestEntity, Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForListMemberDetails method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				return getRestTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}

	public EnrollmentResultResponse memberEnroll(String accountNumber, Headers header)
			throws VoucherManagementException {

		LOG.info("Inside memberEnroll in VoucherMemberManagementService");

		String url = memberManagementUri + enrollMemberPath;

		LOG.info("Member Management URL to enroll Member : {} ", url);
		EnrollmentResultResponse enrollmentResultResponse = new EnrollmentResultResponse();
		try {

			MemberDetailRequestDto getMemberRequest = new MemberDetailRequestDto();
			String code = accountNumber.replaceAll("[^0-9]", "");
			getMemberRequest.setAccountNumber(code);

			// header.setChannelId(GiftingConstants.CHANNEL_ID_CCPORTAL);
			HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(getMemberRequest,
					serviceHelper.getHeader(header));
			LOG.info("{} : {}", OfferConstants.REQUEST_PARAMS_FOR.get(), getMemberRequest);

			ResponseEntity<CommonApiStatus> response = retryCallForMemberEnroll(url, requestEntity, header);
			CommonApiStatus commonApiStatus = response.getBody();

			LOG.info("Response from Member Management enroll member API call: {}", commonApiStatus);
			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
				Object result = commonApiStatus.getResult();
				LOG.info("Member enroll from Member Management API: {} ", result);
				enrollmentResultResponse = modelMapper.map(result, EnrollmentResultResponse.class);
				enrollmentResultResponse.getEnrollmentResult().setMessage(commonApiStatus.getApiStatus().getMessage());
			} else {
				EnrollmentResult enrollmentResult = new EnrollmentResult();
				enrollmentResult.setMessage(
						"failed, exception in member management, " + commonApiStatus.getApiStatus().getMessage());
				enrollmentResultResponse.setEnrollmentResult(enrollmentResult);
			}

		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "memberEnroll",
					e.getClass() + e.getMessage(), VoucherManagementCode.MEMBER_MANAGEMENT_EXCEPTION);
		}
		return enrollmentResultResponse;
	}

	private ResponseEntity<CommonApiStatus> retryCallForMemberEnroll(String url,
			HttpEntity<MemberDetailRequestDto> requestEntity, Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForMemberEnroll method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				return restTemplate.postForEntity(url, requestEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}

	private MemberResponseDto extractMemberDetails(CommonApiStatus commonApiStatus) {

		MemberResponseDto getMemberResponse = null;
		LinkedHashMap<String, MemberResponseDto> resultLinkedHashMap = (LinkedHashMap<String, MemberResponseDto>) commonApiStatus
				.getResult();

		if (null != resultLinkedHashMap) {
			Object result = resultLinkedHashMap.get(OfferConstants.MEMBER_HASH_MAP_NAME.get());

			if (null != result) {
				getMemberResponse = (MemberResponseDto) serviceHelper.convertToObject(result, MemberResponseDto.class);

			}
		}

		return getMemberResponse;
	}

	public boolean checkListMemberExistsAndActive(String accountNumber, Headers header)
			throws VoucherManagementException {

		List<String> accounts = new ArrayList<>();
		accounts.add(accountNumber);

		GetListMemberResponse getListMemberResponse = getListMemberDetails(accounts, header);

		if (null != getListMemberResponse.getListMember() && !getListMemberResponse.getListMember().isEmpty()) {

			for (GetListMemberResponseDto getListMemberResponseDto : getListMemberResponse.getListMember()) {
				if (null != getListMemberResponseDto.getAccountStatus()
						&& (VoucherConstants.MM_STATUS_ACT.equalsIgnoreCase(getListMemberResponseDto.getAccountStatus())
								|| VoucherConstants.MM_STATUS_ACTIVE
										.equalsIgnoreCase(getListMemberResponseDto.getAccountStatus()))) {

					return true;

				}
			}

		}

		return false;

	}

}
