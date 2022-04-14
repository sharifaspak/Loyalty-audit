package com.loyalty.marketplace.promote.partner.inbound.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.inbound.dto.UpdateWelcomeGiftReceivedFlagRequest;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiError;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.promote.partner.inbound.dto.AddPartnerPromote;
import com.loyalty.marketplace.promote.partner.outbound.dto.CountriesListResponse;
import com.loyalty.marketplace.promote.partner.outbound.dto.CountriesResponse;
import com.loyalty.marketplace.promote.partner.outbound.dto.PartnerPromoteDetails;
import com.loyalty.marketplace.promote.partner.outbound.dto.PartnerPromoteResponse;
import com.loyalty.marketplace.promote.partner.service.PartnerPromoteService;
import com.loyalty.marketplace.utils.MarketplaceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "PatnerPromoteController")
@RequestMapping("/marketplace/partner")
public class MarketPlacePromoteController implements AbstractController {
	
	
	@Value(OffersConfigurationConstants.MEMBER_MANAGEMENT_URI)
    private String memberManagementUri;	
	
	@Value(OffersConfigurationConstants.GET_COUNTRIES_DETAILS_PATH)
    private String getcontriesdetailspath;

	@Autowired
	private PartnerPromoteService partnerPromoteService;
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ServiceHelper serviceHelper;
	
	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;
	
	private static final Logger LOG = LoggerFactory.getLogger(MarketPlacePromoteController.class);

	@ApiOperation(value = "Insert Customer Interest")
	@PostMapping(value = "/promote", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonApiStatus> addPartnerPromote(
			@RequestBody @Valid AddPartnerPromote addPatnerPromote, 
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId, BindingResult errors) {
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		if (errors.hasErrors()) {
			List<ApiError> beanValidionErrors = getBeanValidatorErrors(errors);
			return getBadRequestStatus(externalTransactionId, "BAD REQUEST", beanValidionErrors, null);
		}
		try {
			List<String> countrycode=new ArrayList<String>();
			countrycode=getNationalityDetails(headers);
			if(!countrycode.contains(addPatnerPromote.getNationality()))
			{
				List<ApiError> apierrors = new ArrayList<>();
				ApiError error = new ApiError(411, "Invalid Nationality code");
				apierrors.add(error);
				return getBadRequestStatus(externalTransactionId, "Invalid Nationality code", apierrors, null);
			}
			PartnerPromoteResponse partnerPromoteResponse = partnerPromoteService.insertPatnerPromote(addPatnerPromote, program);
			return getSuccessStatus(externalTransactionId, RequestMappingConstants.STATUS_SUCCESS,
					partnerPromoteResponse);
		} catch (MarketplaceException e) {
			return getErrorStatus(externalTransactionId, RequestMappingConstants.STATUS_ERROR, e.getErrorCodeInt(), null);
		}
	
	}
	
	@ApiOperation(value = "Return contact details for account number")
	@GetMapping(value = "/promote")
	public ResponseEntity<CommonApiStatus> getPromotePartnerContactDetail(
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
			@RequestParam String accountNumber) {
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
					
			Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
					channelId, systemId, systemPassword, token, transactionId);
			PartnerPromoteDetails partnerPromoteDetails = partnerPromoteService.getPartnerContactAccount(accountNumber,program);
			if(null==partnerPromoteDetails)
			{
				List<ApiError> errors = new ArrayList<>();
				ApiError error = new ApiError(2, "Invalid account details");
				errors.add(error);
				return getBadRequestStatus(externalTransactionId, "Invalid account details", errors, null);
			}
			return getSuccessStatus(externalTransactionId, RequestMappingConstants.STATUS_SUCCESS,
					partnerPromoteDetails);
		} catch (Exception re) {
			return getErrorStatus(externalTransactionId, re.getMessage(), 1, null);
		} 
	}

	@ApiOperation(value = "Return contact details for account number")
	@GetMapping(value = "/promote/report/{startDate}/{endDate}")
	public ResponseEntity<?> getPromotePartnerContactDetails(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			HttpServletResponse httpServletResponse,
			@PathVariable String startDate, @PathVariable String endDate) throws ParseException {
		try {
			
			//Loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			String responseString  = partnerPromoteService.getPatnerContactDetails(httpServletResponse,startDate,endDate, program);
			if(responseString != null && responseString.equalsIgnoreCase("RECORD_NOT_FOUND")) {
				return getSuccessStatus(externalTransactionId, RequestMappingConstants.STATUS_SUCCESS,
						"Record not found in the Database");
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (MarketplaceException re) {
			return getErrorStatus(externalTransactionId, re.getErrorMsg(), re.getErrorCodeInt(), null);
		} 
	}
	
	
	public List<String> getNationalityDetails(Headers header){
		String url = memberManagementUri + getcontriesdetailspath;
		LOG.info("url for memberManagement"+ url);
		
		List<String> countrycode=new ArrayList<String>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(!ObjectUtils.isEmpty(header)) {
			   
			headers.set(RequestMappingConstants.TOKEN, header.getToken());
		}
		HttpEntity<?> entity = new HttpEntity<>(null, headers);
		ResponseEntity<CommonApiStatus> response = retryCallForGetNationalityDetails(url, entity);
		CommonApiStatus commonApiStatus = response.getBody();
		LOG.info("commonApiStatus"+commonApiStatus.toString());
		
		if(null!=commonApiStatus) {
			
			ApiStatus apiStatus = commonApiStatus.getApiStatus();
			Object result=commonApiStatus.getResult();
			
			if (null!=result) {
			
				LOG.info("result is not null");
				LOG.info("result"+result.toString());
				CountriesListResponse countriesListResponse = (CountriesListResponse) serviceHelper.convertToObject(result,
						CountriesListResponse.class);
				for(CountriesResponse countriesResponse:countriesListResponse.getCountries())
				{
					countrycode.add(countriesResponse.getId());
				}
				
				LOG.info("countriesListResponse"+countriesListResponse.toString());
				LOG.info("countrycode"+countrycode);
				return countrycode;
				
			} else {
				
				for(ApiError apiError : apiStatus.getErrors())
				{
					LOG.info( "Error Info"+apiError.getCode(), apiError.getMessage());
				}	
				
			}
		}
		return countrycode;
	}
	
	private ResponseEntity<CommonApiStatus> retryCallForGetNationalityDetails(String url, HttpEntity<?> entity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForGetNationalityDetails method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return getRestTemplate.exchange(url, HttpMethod.GET, entity, CommonApiStatus.class);
		});		
	}

		
}
