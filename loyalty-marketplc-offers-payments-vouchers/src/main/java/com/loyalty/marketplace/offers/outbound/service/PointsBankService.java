package com.loyalty.marketplace.offers.outbound.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.points.bank.outbound.dto.LifeTimeSavingsDetails;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.Requests;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;

/**
 * 
 * @author jaya.shukla
 *
 */
@Service
public class PointsBankService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PointsBankService.class);
	
	@Value(OffersConfigurationConstants.POINTS_BANK_URI)
    private String pointsBankUri;	
	
	@Value(OffersConfigurationConstants.LIFETIME_SAVINGS_FETCH_URL)
    private String lifetimeSavingsFetchUrl;
	
	@Autowired
	private ServiceHelper serviceHelper;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	/**
	 * 
	 * @param header
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 * @throws MarketplaceException
	 */
    public LifeTimeSavingsDetails getLifetimeSavingsDetails(String accountNumber, Headers header, ResultResponse resultResponse) throws MarketplaceException {
		
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), "getLifetimeSavingsDetails");
		LOG.info(log);
		String url = pointsBankUri + lifetimeSavingsFetchUrl;
		LifeTimeSavingsDetails lifetimeSavingsDetails = null;
		
		try {
			
			CommonApiStatus commonApiStatus = serviceHelper.getMethodPostObject(url, header, 
					Requests.getlifetimeSavingsRequest(accountNumber), OfferConstants.POINTS_BANK_SERVICE.get(), resultResponse);
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus), OfferErrorCodes.POINTS_BANK_RESPONSE_NOT_RECEIVED, resultResponse)
			&& Checks.checkValidResponseWithNoErrors(resultResponse, commonApiStatus.getApiStatus())) {
				
				lifetimeSavingsDetails = (LifeTimeSavingsDetails) serviceHelper.convertToObject(commonApiStatus.getResult(), LifeTimeSavingsDetails.class);
			
			}
			
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), "getLifetimeSavingsDetails",
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_LIFETIME_SAVINGS_DETAILS_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), "getLifetimeSavingsDetails",
					e.getClass() + e.getMessage(), OfferExceptionCodes.RUNTIME_EXCEPTION);
			
		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), "getLifetimeSavingsDetails");
		LOG.info(log);
		return lifetimeSavingsDetails;
		
	}
	    
}
