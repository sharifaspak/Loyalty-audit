package com.loyalty.marketplace.voucher.outbound.service;

import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherRequestDto;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.loyalty.marketplace.voucher.ygag.inbound.dto.DownloadYGAGVoucherRequest;
import com.loyalty.marketplace.voucher.ygag.outbound.dto.DownloadYGAGVoucherResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Service
public class YgagService {

	private static final Logger LOG = LoggerFactory.getLogger(YgagService.class);

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Value("${ygag.supplierPartnerManagement.uri}")
	private String supplierPartnerManagementUrl;
	
	@Value("${tibco.requested.system}")
	private String requestedSystem;
	
	@Value("${tibco.ygag.transactionID}")
	private String ygagTransactionId;
	
	public static final String LOYALTY= "loyalty";
	
	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		LOG.info("Requestand response saved : {}",callLog);
		jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
	}
	
	public void logServiceRequest(String jsonReq, String responseString, long start, long end, String externalTransactionId) {
		ServiceCallLogsDto serviceCallLogsDto = new ServiceCallLogsDto();
        serviceCallLogsDto.setCreatedDate(new Date());
        serviceCallLogsDto.setAction("YGAG");
        serviceCallLogsDto.setTransactionId(externalTransactionId);
        serviceCallLogsDto.setServiceType("Outbound");
        serviceCallLogsDto.setRequest(jsonReq);
        serviceCallLogsDto.setResponse(responseString);
        serviceCallLogsDto.setCreatedUser(""); 
        serviceCallLogsDto.setResponseTime((end-start));
        
        saveRequestResponse(serviceCallLogsDto);
	}
	
	private static DownloadYGAGVoucherRequest getYGAGVoucherRequest(VoucherRequestDto voucherRequestDto) {
		LOG.info("getYGAGVoucherRequest :: voucherRequestDto : {}", voucherRequestDto);  
		DownloadYGAGVoucherRequest ygagVoucherRequest = new DownloadYGAGVoucherRequest();

		ygagVoucherRequest.setReferenceId(voucherRequestDto.getUuid());
		ygagVoucherRequest.setNotify("0");
		ygagVoucherRequest.setBrandCode(voucherRequestDto.getExternalName());
		ygagVoucherRequest.setCurrency("AED");
		ygagVoucherRequest.setAmount(voucherRequestDto.getVoucherDenomination().toString());
		ygagVoucherRequest.setCountry("AE");
		ygagVoucherRequest.setReceiverName(voucherRequestDto.getMerchantName());
		ygagVoucherRequest.setDeliveryType("1"); 
		
        return ygagVoucherRequest;
    } 
	
	public DownloadYGAGVoucherResponse getYGAGVoucherDetails(VoucherRequestDto voucherRequestDto, String externalTransactionId) throws VoucherManagementException {
		  
		MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
		headers.add("X-TIB-TransactionID", ygagTransactionId);
	    headers.add("X-TIB-RequestedSystem", requestedSystem);
	    
		DownloadYGAGVoucherRequest ygagRequest = getYGAGVoucherRequest(voucherRequestDto);
		String jsonReq = new Gson().toJson(ygagRequest);
        LOG.info("YGAG Request Object {}",jsonReq);
        
		try {        
	        Client client = Client.create();
		    client.addFilter(new HTTPBasicAuthFilter(LOYALTY, LOYALTY));
		    
		    WebResource.Builder builder = client.resource(supplierPartnerManagementUrl).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		    for (String key : headers.keySet()) {
		    	builder = builder.header(key, headers.getFirst(key));
		    }
		    long start = System.currentTimeMillis();
	        Gson g = new Gson();
		    LOG.info("YGAG Request : {}", jsonReq);
		    ClientResponse response = builder.post(ClientResponse.class, g.toJson(ygagRequest).getBytes());			    
			String responseString = response.getEntity(String.class);
			LOG.info("YGAG ResponseString : {}", responseString);
			long end = System.currentTimeMillis();
			logServiceRequest(jsonReq, responseString, start, end, externalTransactionId);
			
			DownloadYGAGVoucherResponse responseBody = g.fromJson(responseString, DownloadYGAGVoucherResponse.class);
			LOG.info("YGAG ResponseBody : {}", responseBody);
			
			return responseBody;
	    }
		catch (IllegalStateException exception) {
			throw new VoucherManagementException(this.getClass().toString(), "ygag", VoucherManagementCode.YGAG_SERVICE_ERROR.getMsg(),
					VoucherManagementCode.YGAG_SERVICE_ERROR);
		}
		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "ygag", VoucherManagementCode.YGAG_EXTERNAL_EXCEPTION.getMsg(),
					VoucherManagementCode.YGAG_EXTERNAL_EXCEPTION);
		}
	}
}
