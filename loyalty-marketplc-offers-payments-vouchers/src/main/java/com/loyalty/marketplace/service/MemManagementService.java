package com.loyalty.marketplace.service;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.service.dto.CommonApiStatus;
import com.loyalty.marketplace.service.dto.EnrollmentResult;
import com.loyalty.marketplace.service.dto.EnrollmentResultResponse;
import com.loyalty.marketplace.service.dto.GetListMemberResponse;
import com.loyalty.marketplace.service.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.service.dto.GetMemberResponseDto;
import com.loyalty.marketplace.service.dto.ListMemberDetailRequestDto;
import com.loyalty.marketplace.service.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

@Service
@RefreshScope
public class MemManagementService {

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

	private static final Logger LOG = LoggerFactory.getLogger(MemManagementService.class);
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
			
            HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(getMemberRequest, serviceHelper.getHeader(header));
			ResponseEntity<CommonApiStatus> response = retryCallForMemberDetails(url, requestEntity);		
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

	public GetMemberResponseDto getMemberDetails(String validCode, String typeOfId, Headers header) throws VoucherManagementException {
		LOG.info("Inside getMemberDetails in VoucherMemberManagementService");

		String url = memberManagementUri + memberPath;

		LOG.info(FETCH_MEM_DET, url);
		GetMemberResponseDto memberResponse = null;
		try {
			MemberDetailRequestDto getMemberRequest = new MemberDetailRequestDto();
			String code ="";
			if (typeOfId.equalsIgnoreCase("accountNumber")) {
				LOG.info("accountNumber code : {}",validCode);
				code  = validCode.replaceAll("[^0-9]", "");	
				LOG.info("accountNumber code1 : {}",code);
				getMemberRequest.setAccountNumber(code);
			} else if (typeOfId.equalsIgnoreCase("membershipCode")) {
				LOG.info("membershipCode code : {}",validCode);
				code  = validCode.replace(EXTRA_CHAR, "");
				code = code.replace(EXTRA_CHAR_QUESTION, "");
				LOG.info("membershipCode code1 : {}",code);
				getMemberRequest.setMembershipCode(code);
			} else if (typeOfId.equalsIgnoreCase("loyaltyId")) {
				LOG.info("loyaltyId code : {}",validCode);
				code  = validCode.replace(EXTRA_CHAR, "");
				code = code.replace(EXTRA_CHAR_QUESTION, "");
				LOG.info("loyaltyId code1 : {}",code);
				getMemberRequest.setLoyaltyId(code);
			}

			LOG.info( "{} : {}",OfferConstants.REQUEST_PARAMS_FOR.get(), getMemberRequest);
			
            HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(getMemberRequest, serviceHelper.getHeader(header));
            
			ResponseEntity<CommonApiStatus> response = retryCallForMemberDetails(url, requestEntity);
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
	
	private ResponseEntity<CommonApiStatus> retryCallForMemberDetails(String url, HttpEntity<MemberDetailRequestDto> requestEntity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForMemberDetails method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return getRestTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
		});		
	}

	public GetListMemberResponse getListMemberDetails(List<String> accounts, String status, Headers header) throws VoucherManagementException {

		LOG.info("Inside getListMemberDetails in VoucherMemberManagementService");

		String url = memberManagementUri + listAllMemberPath;

		LOG.info(FETCH_MEM_DET, url);
		GetListMemberResponse memberResponse = null;

		try {

			ListMemberDetailRequestDto listMemberRequest = new ListMemberDetailRequestDto();
			listMemberRequest.setAccounts(accounts);
			listMemberRequest.setStatus(status);

			LOG.info(OfferConstants.REQUEST_PARAMS_FOR.get(), "{} get Member details, {}{}, {} ",
					OfferConstants.MESSAGE_SEPARATOR.get(), OfferConstants.SINGLE_MESSAGE.get(), listMemberRequest);
						
            HttpEntity<ListMemberDetailRequestDto> requestEntity = new HttpEntity<>(listMemberRequest, serviceHelper.getHeader(header));
            
			ResponseEntity<CommonApiStatus> response = retryCallForListMemberDetails(url, requestEntity);
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

	private ResponseEntity<CommonApiStatus> retryCallForListMemberDetails(String url,  HttpEntity<ListMemberDetailRequestDto> requestEntity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForListMemberDetails method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return getRestTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
		});		
	}
	public EnrollmentResultResponse memberEnroll(String accountNumber, Headers header) throws VoucherManagementException {

		LOG.info("Inside memberEnroll in VoucherMemberManagementService");

		String url = memberManagementUri + enrollMemberPath;

		LOG.info("Member Management URL to enroll Member : {} ", url);
		EnrollmentResultResponse enrollmentResultResponse = new EnrollmentResultResponse();
		try {
			
			MemberDetailRequestDto getMemberRequest = new MemberDetailRequestDto();
			String code  = accountNumber.replaceAll("[^0-9]", "");				
			getMemberRequest.setAccountNumber(code);
			
			//header.setChannelId(GiftingConstants.CHANNEL_ID_CCPORTAL);
            HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(getMemberRequest,serviceHelper.getHeader(header));           
            LOG.info( "{} : {}",OfferConstants.REQUEST_PARAMS_FOR.get(), getMemberRequest);
						
			ResponseEntity<CommonApiStatus> response = retryCallForMemberEnroll(url, requestEntity);
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

	private ResponseEntity<CommonApiStatus> retryCallForMemberEnroll(String url,  HttpEntity<MemberDetailRequestDto> requestEntity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForMemberEnroll method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return restTemplate.postForEntity(url, requestEntity, CommonApiStatus.class);
		});		
	}
	private GetMemberResponseDto extractMemberDetails(CommonApiStatus commonApiStatus) {

		GetMemberResponseDto getMemberResponse = null;
		LinkedHashMap<String, GetMemberResponseDto> resultLinkedHashMap = (LinkedHashMap<String, GetMemberResponseDto>) commonApiStatus
				.getResult();

		if (null != resultLinkedHashMap) {
			Object result = resultLinkedHashMap.get(OfferConstants.MEMBER_HASH_MAP_NAME.get());

			if (null != result) {
				getMemberResponse = (GetMemberResponseDto) serviceHelper.convertToObject(result,
						GetMemberResponseDto.class);

			}
		}

		return getMemberResponse;
	}
	
	public boolean checkListMemberExistsAndActive(String accountNumber, Headers header) throws VoucherManagementException {
				
		GetListMemberResponse getListMemberResponse = getListMemberDetails(Arrays.asList(accountNumber), MarketplaceConfigurationConstants.STATUS_ACT, header);
		
		return null != getListMemberResponse.getListMember() && !getListMemberResponse.getListMember().isEmpty();
		
	}
	
}
