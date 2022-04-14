package com.loyalty.marketplace.voucher.outbound.service;
import java.text.SimpleDateFormat;
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
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.maf.inbound.dto.PlaceVoucherOrderRequest;
import com.loyalty.marketplace.voucher.maf.inbound.dto.VoucherReconciliationRequest;
import com.loyalty.marketplace.voucher.maf.outbound.dto.PlaceVoucherOrderResponse;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Service
public class MafService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MafService.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Value("${maf.placeVoucher.uri}")
	private String mafPlaceVoucherUrl;
	
	@Value("${maf.voucherReconciliation.uri}")
	private String mafVoucherReconciliationUrl;
	
	@Value("${tibco.requested.system}")
	private String requestedSystem;
	
	@Value("${tibco.maf.transactionID}")
	private String mafTransactionId;
	
	@Value("${tibco.maf.productId}")
	private String productId;
	
	@Value("${tibco.maf.deliveryMethodID}")
	private String deliveryMethodID;
	
	public static final String LOYALTY= "loyalty";
	
	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		LOG.info("Requestand response saved : {}",callLog);
		jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
	}
	
	public void logServiceRequest(String jsonReq, String responseString, long start, long end, String externalTransactionId) {
		ServiceCallLogsDto serviceCallLogsDto = new ServiceCallLogsDto();
        serviceCallLogsDto.setCreatedDate(new Date());
        serviceCallLogsDto.setAction("MAF");
        serviceCallLogsDto.setTransactionId(externalTransactionId);
        serviceCallLogsDto.setServiceType("Outbound");
        serviceCallLogsDto.setRequest(jsonReq);
        serviceCallLogsDto.setResponse(responseString);
        serviceCallLogsDto.setCreatedUser(""); 
        serviceCallLogsDto.setResponseTime((end-start));
        
        saveRequestResponse(serviceCallLogsDto);
	}
	

	public PlaceVoucherOrderResponse getMafVoucherDetails(Integer voucherDenomination, String externalTransactionId) throws VoucherManagementException {
			 
		SimpleDateFormat requestedDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
		headers.add("X-TIB-TransactionID", mafTransactionId);
	    headers.add("X-TIB-RequestedSystem", requestedSystem);
	    headers.add("X-TIB-RetryLimit", "");
	    headers.add("X-TIB-RequestedDate", requestedDateFormat.format(new Date()));
	    		 
        PlaceVoucherOrderRequest placeVoucherOrderRequest = new PlaceVoucherOrderRequest();
        PlaceVoucherOrderRequest.DataHeader placeVoucherdataHeader = new PlaceVoucherOrderRequest.DataHeader();
        placeVoucherdataHeader.setTargetSystem(VoucherConstants.MAF);
        placeVoucherdataHeader.setProductID(productId);
        placeVoucherdataHeader.setDeliveryMethodID(deliveryMethodID);          
        placeVoucherdataHeader.setLoadAmount(String.valueOf(String.valueOf(voucherDenomination.intValue())));
		
        placeVoucherOrderRequest.setDataHeader(placeVoucherdataHeader);
        
        String jsonReq = new Gson().toJson(placeVoucherOrderRequest);
        LOG.info("MAF PlaceVoucherOrder Request Object {}",jsonReq);
        
        try {
	        Client client = Client.create();
		    client.addFilter(new HTTPBasicAuthFilter(LOYALTY, LOYALTY));
		    WebResource.Builder builder = client.resource(mafPlaceVoucherUrl).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		    for (String key : headers.keySet()) {
		    	builder = builder.header(key, headers.getFirst(key));
		    }
		    long start = System.currentTimeMillis();
		    Gson g = new Gson();
		    ClientResponse response = builder.post(ClientResponse.class, g.toJson(placeVoucherOrderRequest).getBytes());
			String responseString = response.getEntity(String.class);
		    PlaceVoucherOrderResponse responseBody = g.fromJson(responseString, PlaceVoucherOrderResponse.class);
		    long end = System.currentTimeMillis();
	    	
			LOG.info("getMafVoucherCode response : {}", responseString);
			LOG.info("mafresponse response : {}", responseBody);
			logServiceRequest(jsonReq, responseString, start, end, externalTransactionId);
				    
		    return responseBody;
		}
		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "getMafVoucherDetails", e.getMessage(),
					VoucherManagementCode.MAF_EXTERNAL_EXCEPTION);
		}

	}
	
	
	public VoucherReconciliationResponse getMafVoucherReconciliation(String date, Integer page, Integer limit) throws VoucherManagementException {
		 
		MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
		headers.add("X-TIB-TransactionID", mafTransactionId);
	    headers.add("X-TIB-RequestedSystem", requestedSystem);
	    headers.add("X-TIB-RetryLimit", "");
	    headers.add("X-TIB-RequestedDate", date);
	    		         
        VoucherReconciliationRequest voucherReconciliationRequest = new VoucherReconciliationRequest();
		VoucherReconciliationRequest.DataHeader voucherReconciliationdataHeader = new VoucherReconciliationRequest.DataHeader();
		voucherReconciliationdataHeader.setTargetSystem(VoucherConstants.MAF);
		voucherReconciliationdataHeader.setToDate(date);
		voucherReconciliationdataHeader.setFromDate(date);
		voucherReconciliationdataHeader.setPage(page);
		voucherReconciliationdataHeader.setLimit(limit);
                	
        voucherReconciliationRequest.setDataHeader(voucherReconciliationdataHeader);
        
        String jsonReq = new Gson().toJson(voucherReconciliationRequest);
       LOG.info("MAF VoucherReconciliation Request Object {}",jsonReq);
               
        try {
	        Client client = Client.create();
		    client.addFilter(new HTTPBasicAuthFilter(LOYALTY, LOYALTY));
		    WebResource.Builder builder = client.resource(mafVoucherReconciliationUrl).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		    for (String key : headers.keySet()) {
		    	builder = builder.header(key, headers.getFirst(key));
		    }
		    Gson g = new Gson();
		    ClientResponse response = builder.post(ClientResponse.class, g.toJson(voucherReconciliationRequest).getBytes());
			String responseString = response.getEntity(String.class);
			VoucherReconciliationResponse responseBody = g.fromJson(responseString, VoucherReconciliationResponse.class);
	    	
			LOG.info("MAF getMafVoucherReconciliation Response String : {}", responseString);
			LOG.info("MAF getMafVoucherReconciliation Response Body : {}", responseBody);
		    
		    return responseBody;
		}
		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "getMafVoucherReconciliation", e.getMessage(),
					VoucherManagementCode.MAF_EXTERNAL_EXCEPTION);
		}

	}

}
