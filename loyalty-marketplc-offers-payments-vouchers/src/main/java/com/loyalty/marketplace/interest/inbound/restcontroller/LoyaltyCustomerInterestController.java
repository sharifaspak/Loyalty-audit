package com.loyalty.marketplace.interest.inbound.restcontroller;

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
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.interest.constants.InterestErrorCodes;
import com.loyalty.marketplace.interest.constants.InterestRequestMappingConstant;
import com.loyalty.marketplace.interest.domain.model.InterestDomain;
import com.loyalty.marketplace.interest.inbound.dto.InterestRequestDto;
import com.loyalty.marketplace.interest.outbound.dto.InterestResponseResult;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;

import io.swagger.annotations.Api;

@Api(value = "marketplace")
@RestController
@RequestMapping("/marketplace")
public class LoyaltyCustomerInterestController {
	private static final Logger LOG = LoggerFactory.getLogger(LoyaltyCustomerInterestController.class);
	@Autowired 
	InterestDomain interestDomain;

	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;
	
	@PostMapping(value = InterestRequestMappingConstant.API_UPDATE_INTEREST)
	public InterestResponseResult updateInterest(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@PathVariable(value = InterestRequestMappingConstant.ACCOUNT_NUMBER, required = true) String accountNumber,
			@RequestBody InterestRequestDto interestRequestDto){
		
		InterestResponseResult interestResultResponse = null;
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers header = new Headers(program, authorization, externalTransactionId, userName, null, null, channelId, systemId, systemPassword, token, null);
		try {
			interestResultResponse = new InterestResponseResult(externalTransactionId);
			interestResultResponse = interestDomain.updateInterest(accountNumber,interestRequestDto,externalTransactionId,userName, header);
		}catch(Exception e) {
			interestResultResponse.addErrorAPIResponse(InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			interestResultResponse.addErrorAPIResponse(InterestErrorCodes.FAILED_TO_UPDATE_INTEREST_DETAILS.getIntId(),
					InterestErrorCodes.FAILED_TO_UPDATE_INTEREST_DETAILS.getMsg()); LOG.error(new MarketplaceException(this.getClass().toString(), "getInterest", e.getClass() + e.getMessage(),
							InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION).printMessage()); 
		}
		return interestResultResponse;
	}

	@PostMapping("/insert")
	public void updateInterest()
	{
		boolean val = interestDomain.insert();
	}
	@PostMapping("/customerInterestInsert")
	public void CustomerInterestInsert() {
		boolean val = interestDomain.customerInterestInsert();
	}
	@PostMapping("/insertCategory")
	public void insertCategory() {
		boolean val = interestDomain.insertCategory();
	}



	@GetMapping(value = InterestRequestMappingConstant.API_GET_INTEREST) 
	public InterestResponseResult getInterest(
			@RequestHeader(value = RequestMappingConstants.PROGRAM,required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value =RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false)String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL,required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@PathVariable(value = InterestRequestMappingConstant.ACCOUNT_NUMBER,required = true) String accountNumber,
			@RequestParam(value = VoucherRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = VoucherRequestMappingConstants.LIMIT, required = false) Integer limit) { 
		InterestResponseResult interestResultResponse = new InterestResponseResult(externalTransactionId);
		
		//Loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		Headers header = new Headers(program, authorization, externalTransactionId, userName, null, null, channelId, systemId, systemPassword, token, null);
		try {
			
			interestResultResponse = interestDomain.getInterestDetails(accountNumber,externalTransactionId, true, header,page,limit);
		}
		catch(Exception e) {
			e.printStackTrace();
			interestResultResponse.addErrorAPIResponse(InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			interestResultResponse.addErrorAPIResponse(InterestErrorCodes.NO_INTEREST_TO_DISPLAY.getIntId(),
					InterestErrorCodes.NO_INTEREST_TO_DISPLAY.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "getInterest", e.getClass() + e.getMessage(),InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION).printMessage()); 
					}
		return interestResultResponse;
		
	}
	
	@PostMapping(value = InterestRequestMappingConstant.RESET_DB, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void resetOfferCatalog() {
       
		interestDomain.resetDB();
       
    }

}