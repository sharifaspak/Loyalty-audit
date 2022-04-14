package com.loyalty.marketplace.voucher.outbound.service;

import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.GET_PARTNER_TYPE_PATH;
import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.PARTNER_MANAGEMENT_URI;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;


@Service
@RefreshScope
public class VoucherPartnerService {

	@Value(PARTNER_MANAGEMENT_URI)
    private String partnerManagementUri;
	
	@Value(GET_PARTNER_TYPE_PATH)
	private String partnerTypePath;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	@Autowired
	private ServiceHelper serviceHelper;
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	private static final Logger LOG = LoggerFactory.getLogger(VoucherPartnerService.class);
	

	/**
	 * 
	 * @param partnerCode
	 * @return status to indicate if partner type exists in partner management service
	 * @throws MarketplaceException
	 * @throws VoucherManagementException 
	 */
	public boolean checkPartnerTypeExists(String partnerCode, Headers header) throws VoucherManagementException {

		String url = partnerManagementUri + partnerTypePath + partnerCode;
		
		LOG.info("Partner Management URL to check if partner type exists : {} ", url);
		return retryTemplate.execute(context ->{
			LOG.info("inside Retry block using retryTemplate of checkPartnerTypeExists method of class {}", this.getClass().getName());
			try {			
	            HttpEntity<?> entity = new HttpEntity<>(serviceHelper.getHeader(header));
				ResponseEntity<Boolean> response = getRestTemplate.exchange(url, HttpMethod.GET, entity,
						Boolean.class);
				
				return (response.getBody() !=null) ?response.getBody():false;			
				
			} catch (RestClientException e) {			
				throw new VoucherManagementException(this.getClass().toString(), VoucherConstants.CHECK_PARTNER_TYPE_EXISTS_METHOD,
						e.getClass() + e.getMessage(), VoucherManagementCode.PARTNER_TYPE_CHECK_REST_CLIENT_EXCEPTION);
	
			} catch (Exception e) {			
				throw new VoucherManagementException(this.getClass().toString(), VoucherConstants.CHECK_PARTNER_TYPE_EXISTS_METHOD,
						e.getClass() + e.getMessage(), VoucherManagementCode.PARTNER_TYPE_CHECK_RUNTIME_EXCEPTION);
				
			}
		});

	}
}
