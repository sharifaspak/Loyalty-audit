package com.loyalty.marketplace.voucher.outbound.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherRequestDto;
import com.loyalty.marketplace.voucher.smls.inbound.dto.DownloadSMLSVoucherRequest;
import com.loyalty.marketplace.voucher.smls.outbound.dto.DownloadSMLSVoucherResponse;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

@Service
public class SMLSService {
	private static final Logger LOG = LoggerFactory.getLogger(SMLSService.class);
	

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${smls.generatePromo.endpoint.apiKey}")
	private String apiKey;
	
	@Value("${smls.generatePromo.endpoint.apiSecrete}")
	private String apiSecrete;
	
	@Value("${smls.generatePromo.endpoint.url}")
	private String SMLSGeneratePromoEndpoint;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		LOG.info("Requestand response saved : {}",callLog);
		jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
	}
	public void logServiceRequestAndResponse(String jsonReq, String responseString, long start, long end, String externalTransactionId) {
		ServiceCallLogsDto serviceCallLogsDto = new ServiceCallLogsDto();
        serviceCallLogsDto.setCreatedDate(new Date());
        serviceCallLogsDto.setAction("SMLS");
        serviceCallLogsDto.setTransactionId(externalTransactionId);
        serviceCallLogsDto.setServiceType("Outbound");
        serviceCallLogsDto.setRequest(jsonReq);
        serviceCallLogsDto.setResponse(responseString);
        serviceCallLogsDto.setCreatedUser(""); 
        serviceCallLogsDto.setResponseTime((end-start));
        
        saveRequestResponse(serviceCallLogsDto);
	}
	
	public DownloadSMLSVoucherRequest getSMLSVoucherRequest(VoucherRequestDto voucherRequestDto, String externalTransactionId, Date expiryDate) {
		LOG.info("getSMLSVoucherRequest :: voucherRequestDto : {}", voucherRequestDto);  
		DownloadSMLSVoucherRequest request = new DownloadSMLSVoucherRequest();
		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
		request.setValidFrom(DateFormat.format(new Date()));
		request.setValidTill(DateFormat.format(expiryDate));
		request.setAccountNumber(voucherRequestDto.getAccountNumber());
		request.setApiKey(apiKey);
		request.setApiSecret(apiSecrete);
		request.setPromoId(voucherRequestDto.getOfferId());
		request.setTransactionId(externalTransactionId); 
		
        return request;
    } 
	public DownloadSMLSVoucherResponse fetchSMLSVoucher(VoucherRequestDto voucherRequestDto, String externalTransactionId, Date expiryDate) throws VoucherManagementException{
		LOG.info("SMLSService: DownloadSMLSVoucherResponse method start");
		DownloadSMLSVoucherRequest request = getSMLSVoucherRequest(voucherRequestDto, externalTransactionId, expiryDate);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(request, headers);
		try {
			
			LOG.info("Hitting url : {}", SMLSGeneratePromoEndpoint);
			LOG.info("SMLS generate voucher request : {}", entity);
			long start = System.currentTimeMillis();
			ResponseEntity<String> smlsResponse = retryGetSMLSVoucher(SMLSGeneratePromoEndpoint, entity);
			LOG.info("SMLS Response : {}", smlsResponse);
			long end = System.currentTimeMillis();
			logServiceRequestAndResponse(entity.toString(), smlsResponse.getBody(), start, end, externalTransactionId);
			Gson gsonObje = new Gson();
			String body = smlsResponse.getBody();
			DownloadSMLSVoucherResponse responseBody = gsonObje.fromJson(body, DownloadSMLSVoucherResponse.class);
			LOG.info("SMLSService: DownloadSMLSVoucherResponse method end");
			return responseBody;
			
		} catch (IllegalStateException exception) {
			throw new VoucherManagementException(this.getClass().toString(), "smls", VoucherManagementCode.SMLS_SERVICE_ERROR.getMsg(),
					VoucherManagementCode.SMLS_SERVICE_ERROR);
		}
		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "smls", VoucherManagementCode.SMLS_EXTERNAL_EXCEPTION.getMsg(),
					VoucherManagementCode.SMLS_EXTERNAL_EXCEPTION);
		}
	}
	
	private ResponseEntity<String> retryGetSMLSVoucher(String SMLSGeneratePromoEndpoint, HttpEntity<?> entity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallPostObject method of class {}", this.getClass().getName());
		return retryTemplate.execute(context ->{
			return restTemplate.exchange(SMLSGeneratePromoEndpoint, HttpMethod.POST, entity,
					String.class);
		});		
	}

}
