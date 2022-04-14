package com.loyalty.marketplace.equivalentpoints.inbound.helper;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.event.HeaderDto;
import com.loyalty.marketplace.equivalentpoints.outbound.database.service.EquivalentPointsService;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ApiError;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ApiStatus;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.equivalentpoints.utils.EquivalentPointsException;
import com.loyalty.marketplace.equivalentpoints.utils.ErrorCodes;
import com.loyalty.marketplace.equivalentpoints.utils.ErrorMessages;

@Component
public class EquivalentPointsHelperBase extends ErrorMessages {
	
	private static final Logger LOG = LoggerFactory.getLogger(EquivalentPointsHelperBase.class);
	
	@Autowired
	private EquivalentPointsService memberActivityService;
	
	@Value("${memberManagement.uri}")
	private String memberMgmtUrl;
	
	@Value("${memberManagement.getMember.path}")
	private String getMemberUrl;
	
	/**
	 * @param createMemberDetailRequestDto
	 * @return
	 * @throws MemberActivityException
	 */
	public GetMemberResponseDto invokeMemberManagementforMemberDetails(MemberDetailRequestDto memberDetailRequestDto,
			HeaderDto headerDto) throws EquivalentPointsException {
		String mmRequestPayload = convertToJson(memberDetailRequestDto);
		String getMemberDetails = memberMgmtUrl + getMemberUrl;
		LOG.info("inside invokeMemberManagementforMemberDetails method of memberEligibilityDMRequestDto {} ",
				mmRequestPayload);
		ResponseEntity<CommonApiStatus> response = null;
		CommonApiStatus commonApiStatus = null;
		GetMemberResponseDto getMemberResponse = null;
		try {
			response = memberActivityService.buildWebClient(getMemberDetails, memberDetailRequestDto, HttpMethod.POST,
					CommonApiStatus.class, headerDto);
			commonApiStatus = response.getBody();
			LOG.info(
					"Exiting invokeMemberManagementforMemberDetails method of commonApiStatus {}   with member activity ",
					response);
			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
				LOG.info("Member Details Retrieved Successfully");
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, GetMemberResponseDto> resultLinkedHashMap = (LinkedHashMap<String, GetMemberResponseDto>) commonApiStatus
						.getResult();
				Object result = resultLinkedHashMap.get("memberResponse");
				ObjectMapper mapper = new ObjectMapper();
				getMemberResponse = mapper.convertValue(result, GetMemberResponseDto.class);
				LOG.info("Member Details Object Converted Successfully");
			} else {
				ApiStatus apiStatus = response.getBody().getApiStatus();
				ApiError apiError = apiStatus.getErrors().get(0);
				throw new EquivalentPointsException(ErrorCodes.MEMBER_MANAGEMENT_GET_MEMBER_DETAILS_ERROR,
						apiError.getMessage());
			}
		} catch (Exception ex) {
			LOG.error("Exiting invokeMemberManagementforMemberDetails method of exception {}  with member activity ",
					ex.getMessage());
			throw new EquivalentPointsException(ErrorCodes.MEMBER_MANAGEMENT_GET_MEMBER_DETAILS_ERROR,
					exceptionforMemberdetails);
		}
		LOG.info("Exiting :: invokeMemberManagementforMemberDetails()");
		return getMemberResponse;
	}
	
	private String convertToJson(Object object) {
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
		}
		return null;
	}

}
