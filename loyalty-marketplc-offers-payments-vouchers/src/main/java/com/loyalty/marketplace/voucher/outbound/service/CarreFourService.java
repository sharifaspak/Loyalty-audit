package com.loyalty.marketplace.voucher.outbound.service;
import java.math.BigDecimal;
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
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.ApplicationHeader;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.CarrefourConfirmGCRequestRequest;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.CarrefourGCRequestRequest;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.InitializeCarrefourAPIRequest;
import com.loyalty.marketplace.voucher.carrefour.outbound.dto.CarrefourConfirmGCRequestResponse;
import com.loyalty.marketplace.voucher.carrefour.outbound.dto.CarrefourGCRequestResponse;
import com.loyalty.marketplace.voucher.carrefour.outbound.dto.InitializeCarrefourAPIResponse;
import com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.CarrefourTransactionReconcilationRequest;
import com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.ReconciliationRecords;
import com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.TransactionReconciliationRequest;
import com.loyalty.marketplace.voucher.carrefour.reconcilation.outbound.dto.CarrefourTransactionReconcilationResponse;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Service
public class CarreFourService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CarreFourService.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Value("${carreFour.uri}")
	private String carreFourUrl;
	
	@Value("${carreFour.initializecarrefourapi.path}")
	private String initializecarrefourapi;
	
	@Value("${carreFour.carrefourgcrequest.path}")
	private String carrefourgcrequest;
	
	@Value("${carreFour.carrefourconfirmgcrequest.path}")
	private String carrefourconfirmgcrequest;
	
	@Value("${carreFour.transactionreconciliation.path}")
	private String transactionreconciliation;
	
	@Value("${tibco.requested.system}")
	private String requestedSystem;
	
	@Value("${tibco.crfr.passphrase}")
	private String passphrase;
	
	@Value("${tibco.crfr.merchantID}")
	private String merchantID;
		
	@Value("${tibco.crfr.terminalID}")
	private String terminalID;
		
	@Value("${tibco.crfr.cashierID}")
	private String cashierID;
	
	@Value("${tibco.crfr.genCode}")
	private String genCode;
	
	@Value("${tibco.crfr.transactionType.a}")
	private String transactionTypeA;
	
	@Value("${tibco.crfr.finalStatus}")
	private String finalStatus;
	
	@Value("${tibco.crfr.currency}")
	private String currency;
	
	@Value("${tibco.crfr.lineCount}")
	private String lineCount;
	
	
	private SimpleDateFormat requestedDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	private static final String loyalty = "loyalty";
	
	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		LOG.info("Requestand response saved : {}",callLog);
		jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
	}
	
	public void logServiceRequest(String jsonReq, String responseString, long start, long end, String externalTransactionId) {
		ServiceCallLogsDto serviceCallLogsDto = new ServiceCallLogsDto();
        serviceCallLogsDto.setCreatedDate(new Date());
        serviceCallLogsDto.setAction("CRFR");
        serviceCallLogsDto.setTransactionId(externalTransactionId);
        serviceCallLogsDto.setServiceType("Outbound");
        serviceCallLogsDto.setRequest(jsonReq);
        serviceCallLogsDto.setResponse(responseString);
        serviceCallLogsDto.setCreatedUser(""); 
        serviceCallLogsDto.setResponseTime((end-start));
        
        saveRequestResponse(serviceCallLogsDto);
	}
	
	private ApplicationHeader createApplicationHeader(Date now, String transactionId) {
        ApplicationHeader applicationHeader = new ApplicationHeader();
        applicationHeader.setTransactionID(transactionId);
        applicationHeader.setRequestedDate(requestedDateFormat.format(now));
        applicationHeader.setRequestedSystem(requestedSystem);
        applicationHeader.setRetryLimit("");
        return applicationHeader;
    }
	
    public CarrefourGCRequestResponse getCarreFourVoucherDetails(Integer voucherDenomination, String externalTransactionId) throws VoucherManagementException {
		 
		Date now = new Date();
        String crfrTransId = String.valueOf(now.getTime()).substring(0, 10);
        ApplicationHeader appHeader = createApplicationHeader(now, crfrTransId);
        
        LOG.info("Calling InitializeCarrefourAPI");
        InitializeCarrefourAPIResponse initCrfrResponse = initCarrefour(appHeader, externalTransactionId);
        
        
        if(initCrfrResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS)) {
        	LOG.info("Calling CarrefourGC");
        	CarrefourGCRequestResponse carrefourGCResponse = carrefourGC(appHeader, initCrfrResponse.getResponseData().getSessionID(), initCrfrResponse.getResponseData().getTransactionID(),
        			voucherDenomination.intValue(), "A", "", externalTransactionId);
        	
        	if(carrefourGCResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS) 
        			&& carrefourGCResponse.getResponseData().getIsSuccessful().equalsIgnoreCase(VoucherConstants.TRUE)) {
        		LOG.info("Calling CarrefourConfirmGC");  
        		CarrefourConfirmGCRequestResponse carrefourConfirmGCRequestResponse = carrefourConfirmGC(appHeader, initCrfrResponse.getResponseData().getSessionID(), 
        				carrefourGCResponse.getResponseData().getReferenceNumber(), externalTransactionId);
        		    		
        		if (carrefourConfirmGCRequestResponse != null && carrefourConfirmGCRequestResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS) &&
        				 carrefourConfirmGCRequestResponse.getResponseData().getIsSuccessful().equalsIgnoreCase(VoucherConstants.TRUE)) {
        			 LOG.info("Voucher TransactionReconciliation");       			 
        			 if(transactionReconciliation(carrefourGCResponse, externalTransactionId)) {
        				 return carrefourGCResponse;
        			 }  
        		}
        	}       	        
        } 
        return null;
    }
	
	
	private InitializeCarrefourAPIResponse initCarrefour(ApplicationHeader applicationHeader, String externalTransactionId) throws VoucherManagementException{
        InitializeCarrefourAPIRequest initializeCarrefourAPIRequest = getInitCarrefourRequest(applicationHeader);
        MultivaluedMap<String, String> headers = new MultivaluedMapImpl();     
        
        String jsonReq = new Gson().toJson(initializeCarrefourAPIRequest);
		try {
		        Client client = Client.create();
			    client.addFilter(new HTTPBasicAuthFilter(loyalty, loyalty));
			    WebResource.Builder builder = client.resource(carreFourUrl+initializecarrefourapi).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
			    for (String key : headers.keySet()) {
			    	builder = builder.header(key, headers.getFirst(key));
			    }
	
			    long start = System.currentTimeMillis();
			    Gson g = new Gson();
			    LOG.info("initCarrefour request : {}", jsonReq);
			    ClientResponse response = builder.post(ClientResponse.class, g.toJson(initializeCarrefourAPIRequest).getBytes());			    
				String responseString = response.getEntity(String.class);
				LOG.info("initCarrefour responseString : {}", responseString);
				InitializeCarrefourAPIResponse responseBody = g.fromJson(responseString, InitializeCarrefourAPIResponse.class);	
				long end = System.currentTimeMillis();
				
				LOG.info("initCarrefour responseBody : {}", responseBody);
				logServiceRequest(jsonReq, responseString, start, end, externalTransactionId);
				
			    return responseBody;
			}
			catch (Exception e) {
				throw new VoucherManagementException(this.getClass().toString(), "initCarrefour", e.getMessage(),
						VoucherManagementCode.CARREFOUR_INIT_EXTERNAL_EXCEPTION);
			}
    }

    private InitializeCarrefourAPIRequest getInitCarrefourRequest(ApplicationHeader appHeader) {
        InitializeCarrefourAPIRequest initializeCarrefourAPIRequest = new InitializeCarrefourAPIRequest();
        InitializeCarrefourAPIRequest.DataHeader dataHeader = new InitializeCarrefourAPIRequest.DataHeader();
        dataHeader.setTerminalID(terminalID);
        dataHeader.setPassphrase(passphrase);
        dataHeader.setMerchantID(merchantID);
        dataHeader.setCashierID(cashierID);

        initializeCarrefourAPIRequest.setApplicationHeader(appHeader);
        initializeCarrefourAPIRequest.setDataHeader(dataHeader);
        return initializeCarrefourAPIRequest;
    }
    
    
    private CarrefourGCRequestResponse carrefourGC(ApplicationHeader applicationHeader, String sessionID, String transactionNumber, int amount, 
    		String queryType,String cardNumber, String externalTransactionId) throws VoucherManagementException {
    	CarrefourGCRequestRequest carrefourGCRequestRequest = getCarrefourGCRequest(applicationHeader, sessionID, transactionNumber, amount, queryType, cardNumber);
        MultivaluedMap<String, String> headers = new MultivaluedMapImpl();   
       
        String jsonReq = new Gson().toJson(carrefourGCRequestRequest);
        try {
                    
            Client client = Client.create();
		    client.addFilter(new HTTPBasicAuthFilter(loyalty, loyalty));
		    WebResource.Builder builder = client.resource(carreFourUrl+carrefourgcrequest).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		    for (String key : headers.keySet()) {
		    	builder = builder.header(key, headers.getFirst(key));
		    }
		    long start = System.currentTimeMillis();
            Gson g = new Gson();
            
		    LOG.info("Carrefour GC Request : {}", jsonReq);
		    ClientResponse response = builder.post(ClientResponse.class, g.toJson(carrefourGCRequestRequest).getBytes());			    
			String responseString = response.getEntity(String.class);
			LOG.info("Carrefour GC ResponseString : {}", responseString);
			CarrefourGCRequestResponse responseBody = g.fromJson(responseString, CarrefourGCRequestResponse.class);	
			long end = System.currentTimeMillis();
			LOG.info("Carrefour GC ResponseBody : {}", responseBody);
			logServiceRequest(jsonReq, responseString, start, end, externalTransactionId);
			
			return responseBody;
        }

		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "carrefourGC", e.getMessage(),
					VoucherManagementCode.CARREFOUR_GC_EXTERNAL_EXCEPTION);
		}

    }
    
    private CarrefourGCRequestRequest getCarrefourGCRequest(ApplicationHeader appHeader, String sessionID, String transactionNumber, int amount, String queryType,String cardNumber) {
    	CarrefourGCRequestRequest carrefourGCRequestRequest = new CarrefourGCRequestRequest();

        CarrefourGCRequestRequest.DataHeader dataHeader = new CarrefourGCRequestRequest.DataHeader();
        dataHeader.setSessionID(sessionID);
        dataHeader.setMerchantID(merchantID);
        dataHeader.setTerminalID(terminalID);
        dataHeader.setCashierID(cashierID);
        dataHeader.setGenCode(genCode);
        dataHeader.setTransactionNumber(transactionNumber);
        if(queryType.equals("B")) {
            dataHeader.setMsgType("B");
            dataHeader.setCardNumber(cardNumber);
        }
        else {
            dataHeader.setMsgType("A");
        }
        dataHeader.setAmount(new BigDecimal(amount));
        dataHeader.setAmount(new BigDecimal(amount));
        
        carrefourGCRequestRequest.setApplicationHeader(appHeader);
        carrefourGCRequestRequest.setDataHeader(dataHeader);
                
        return carrefourGCRequestRequest;
    } 
    
    
    private CarrefourConfirmGCRequestResponse carrefourConfirmGC(ApplicationHeader applicationHeader, String sessionID, 
    		String referenceNumber, String externalTransactionId) throws VoucherManagementException {     
        CarrefourConfirmGCRequestRequest carrefourConfirmGCRequestRequest = getCarrefourConfirmGCRequest(applicationHeader, sessionID, referenceNumber);
        MultivaluedMap<String, String> headers = new MultivaluedMapImpl();  
        
        String jsonReq = new Gson().toJson(carrefourConfirmGCRequestRequest);
        try {
                    
            Client client = Client.create();
		    client.addFilter(new HTTPBasicAuthFilter(loyalty, loyalty));
		    WebResource.Builder builder = client.resource(carreFourUrl+carrefourconfirmgcrequest).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		    for (String key : headers.keySet()) {
		    	builder = builder.header(key, headers.getFirst(key));
		    }
		    long start = System.currentTimeMillis();
            Gson g = new Gson();
		    LOG.info("CarrefourConfirm GC Request : {}", jsonReq);
		    ClientResponse response = builder.post(ClientResponse.class, g.toJson(carrefourConfirmGCRequestRequest).getBytes());			    
			String responseString = response.getEntity(String.class);
			LOG.info("CarrefourConfirm GC ResponseString : {}", responseString);
			CarrefourConfirmGCRequestResponse responseBody = g.fromJson(responseString, CarrefourConfirmGCRequestResponse.class);
			long end = System.currentTimeMillis();
			
			LOG.info("CarrefourConfirm GC ResponseBody : {}", responseBody);
			logServiceRequest(jsonReq, responseString, start, end, externalTransactionId);
			
			return responseBody;
        }

		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "carrefourConfirmGC", e.getMessage(),
					VoucherManagementCode.CARREFOUR_GC_CONFIRM_EXTERNAL_EXCEPTION);
		}
    }
    
    private CarrefourConfirmGCRequestRequest getCarrefourConfirmGCRequest(ApplicationHeader applicationHeader, String sessionID, String referenceNumber) {
    	CarrefourConfirmGCRequestRequest carrefourConfirmGCRequestRequest = new CarrefourConfirmGCRequestRequest();
        
        CarrefourConfirmGCRequestRequest.DataHeader dataHeader = new CarrefourConfirmGCRequestRequest.DataHeader();
        dataHeader.setSessionID(sessionID);
        dataHeader.setMerchantID(merchantID);
        dataHeader.setTerminalID(terminalID);
        dataHeader.setCashierID(cashierID);
        dataHeader.setReferenceNumber(referenceNumber);
        dataHeader.setNote("");

        carrefourConfirmGCRequestRequest.setDataHeader(dataHeader);
        carrefourConfirmGCRequestRequest.setApplicationHeader(applicationHeader);
                
        return carrefourConfirmGCRequestRequest;
    } 
    
    private boolean transactionReconciliation(CarrefourGCRequestResponse carrefourGCRequestResponse, String externalTransactionId) throws VoucherManagementException {        
    	CarrefourTransactionReconcilationRequest carrefourTransactionReconcilationRequest = getTransactionReconciliation(carrefourGCRequestResponse);
        MultivaluedMap<String, String> headers = new MultivaluedMapImpl();  
        boolean status = false;
        String jsonReq = new Gson().toJson(carrefourTransactionReconcilationRequest);
        try {
                    
            Client client = Client.create();
		    client.addFilter(new HTTPBasicAuthFilter(loyalty, loyalty));
		    WebResource.Builder builder = client.resource(carreFourUrl+transactionreconciliation).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		    for (String key : headers.keySet()) {
		    	builder = builder.header(key, headers.getFirst(key));
		    }
            Gson g = new Gson();
            long start = System.currentTimeMillis();
		    LOG.info("CarrefourTransactionReconcilation Request : {}", jsonReq);
		    ClientResponse response = builder.post(ClientResponse.class, g.toJson(carrefourTransactionReconcilationRequest).getBytes());
		    long end = System.currentTimeMillis();
			String responseString = response.getEntity(String.class);
			LOG.info("CarrefourTransactionReconcilation ResponseString : {}", responseString);
			CarrefourTransactionReconcilationResponse responseBody = g.fromJson(responseString, CarrefourTransactionReconcilationResponse.class);			
			LOG.info("CarrefourTransactionReconcilation ResponseBody : {}", responseBody);
			
			if(responseBody.getTransactionReconciliationResponse().getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS)) {
				status = true;
			}
			
			logServiceRequest(jsonReq, responseString, start, end, externalTransactionId);
			return status;
        }

		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "carrefourConfirmGC", e.getMessage(),
					VoucherManagementCode.CARREFOUR_GC_CONFIRM_EXTERNAL_EXCEPTION);
		}
    }
    
    private CarrefourTransactionReconcilationRequest getTransactionReconciliation(CarrefourGCRequestResponse carrefourGCRequestResponse) {
    	final SimpleDateFormat expiryDateFormat = new SimpleDateFormat("yyyyMMdd");
    	CarrefourTransactionReconcilationRequest reconciliationRequest = new CarrefourTransactionReconcilationRequest();
    	TransactionReconciliationRequest transactionReconciliationRequest = new TransactionReconciliationRequest();
    	ReconciliationRecords rcRecord = new ReconciliationRecords();
        
    	final CarrefourGCRequestResponse.ResponseData responseData = carrefourGCRequestResponse.getResponseData();
        rcRecord.setBalance(responseData.getBalance().toString());
        rcRecord.setReferenceNumber(responseData.getReferenceNumber());
        rcRecord.setTerminalTxNo(responseData.getTransactionID());
        rcRecord.setTerminalId(terminalID);
        rcRecord.setCashierId(cashierID);
        rcRecord.setTransactionNumber(responseData.getTransactionID());
        rcRecord.setAmount(String.valueOf(responseData.getAmount()));
        rcRecord.setGenCode(genCode);
        rcRecord.setCardNumber(responseData.getCardNumber());
        rcRecord.setTransactionType(transactionTypeA);
        rcRecord.setFinalStatus(finalStatus);
        rcRecord.setCurrency(currency);
        rcRecord.setLineCount(lineCount);
        
        final com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.ApplicationHeader applicationHeader = new com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.ApplicationHeader();
        applicationHeader.setTransactionID(responseData.getTransactionID());
        applicationHeader.setRequestedSystem(requestedSystem);
        
        final com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.DataHeader dataHeader = new com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.DataHeader();
        dataHeader.setReconciliationRecords(rcRecord);
        dataHeader.setBusinessDate(expiryDateFormat.format(new Date()));
        dataHeader.setPassphrase(passphrase);
        dataHeader.setMerchantID(merchantID);
        
        
        transactionReconciliationRequest.setDataHeader(dataHeader);
        transactionReconciliationRequest.setApplicationHeader(applicationHeader);
        
        reconciliationRequest.setTransactionReconciliationRequest(transactionReconciliationRequest);   
                        
        return reconciliationRequest;
    } 
    
    
    public BigDecimal getEvoucherBalanceFromCarrefour(String crfrTransId,String cardNumber,String accountNumber, 
    		int amount, String externalTransactionId) throws VoucherManagementException {
		
    	BigDecimal balance = null;  	
		Date now = new Date();
        ApplicationHeader appHeader = createApplicationHeader(now, crfrTransId);
        
        LOG.info("Calling InitializeCarrefourAPI");
        InitializeCarrefourAPIResponse initCrfrResponse = initCarrefour(appHeader, externalTransactionId);
               
        if(initCrfrResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS) 
        		&& initCrfrResponse.getResponseData().getIsSuccessful().equalsIgnoreCase(VoucherConstants.TRUE)) {
        	LOG.info("Calling CarrefourGC");
        	CarrefourGCRequestResponse carrefourGCResponse = carrefourGC(appHeader, initCrfrResponse.getResponseData().getSessionID(), initCrfrResponse.getResponseData().getTransactionID(),
        			amount, "B", cardNumber, externalTransactionId);
        	
        	if(carrefourGCResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS) 
        			&& carrefourGCResponse.getResponseData().getIsSuccessful().equalsIgnoreCase(VoucherConstants.TRUE)) {
        		LOG.info("[Carrefour ] GCRequest :: success  :: referenceNumber :{}", carrefourGCResponse.getResponseData().getReferenceNumber());
                LOG.info("[Carrefour ] GCRequest :: success  :: balance :{}", carrefourGCResponse.getResponseData().getBalance());
                balance = carrefourGCResponse.getResponseData().getBalance();       		               
            }     	        
        }
        
        return balance;
    }
    
}
