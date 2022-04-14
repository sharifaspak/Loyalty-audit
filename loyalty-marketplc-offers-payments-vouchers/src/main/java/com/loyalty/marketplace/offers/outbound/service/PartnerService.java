package com.loyalty.marketplace.offers.outbound.service;

import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.GET_PARTNER_PATH;
import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.PARTNER_MANAGEMENT_URI;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.outbound.dto.ComTemplateResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;

/**
 * 
 * @author jaya.shukla
 *
 */
@Service
public class PartnerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PartnerService.class);
	
	@Value(PARTNER_MANAGEMENT_URI)
    private String partnerManagementUri;
	
	@Value(GET_PARTNER_PATH)
	private String partnerPath;	
	
	@Autowired
	private ServiceHelper serviceHelper;
	
	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;

	/**
	 * 
	 * @param partnerCode
	 * @param resultResponse
	 * @return status to indicate if partner exists in partner management service
	 * @throws MarketplaceException
	 */
	public Boolean checkPartnerExists(String partnerCode, Headers header) throws MarketplaceException {
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), OfferConstants.CHECK_PARTNER_EXISTS_METHOD.get());
		LOG.info(log);
		String url = partnerManagementUri + partnerPath + partnerCode;
		Boolean exists = false;
		
		try {
			
			log = Logs.logForServiceUrl(url);
			LOG.info(log);
			
			HttpEntity<?> entity = new HttpEntity<>(serviceHelper.getHeader(header));
			log = Logs.logForServiceHeaders(entity);
			LOG.info(log);
			
			log = Logs.logBeforeHittingService(OfferConstants.PARTNER_MANAGEMENT_SERVICE.get());
			LOG.info(log);
			ResponseEntity<Boolean> responseEntity = retryCallForCheckPartnerExists(url, entity);
			log = Logs.logAfterHittingService(OfferConstants.PARTNER_MANAGEMENT_SERVICE.get());
			LOG.info(log);
			
			if(!ObjectUtils.isEmpty(responseEntity)) {
				
				exists = responseEntity.getBody();
			}
			
			log = Logs.logForServiceResponse(exists);
			LOG.info(log);
		
		
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CHECK_PARTNER_EXISTS_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.PARTNER_CHECK_REST_CLIENT_EXCEPTION);

		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CHECK_PARTNER_EXISTS_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.PARTNER_CHECK_RUNTIME_EXCEPTION);
			
		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), OfferConstants.CHECK_PARTNER_EXISTS_METHOD.get());
		LOG.info(log);
		return exists;
	}
	
	private ResponseEntity<Boolean> retryCallForCheckPartnerExists(String url, HttpEntity<?> entity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForCheckPartnerExists method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return getRestTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class);
		});		
	}

}		
