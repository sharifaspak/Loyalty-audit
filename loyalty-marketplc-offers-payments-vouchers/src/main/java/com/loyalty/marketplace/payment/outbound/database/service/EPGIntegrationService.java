package com.loyalty.marketplace.payment.outbound.database.service;

import java.util.Date;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.google.gson.Gson;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.constants.PaymentConstants;
import com.loyalty.marketplace.payment.domain.model.marketplace.EPGTransactionDomain;
import com.loyalty.marketplace.payment.outbound.database.repository.EPGTransactionRepository;
import com.loyalty.marketplace.payment.utils.Authorization;
import com.loyalty.marketplace.payment.utils.EPGPaymentRequest;
import com.loyalty.marketplace.payment.utils.EPGResponse;


@RefreshScope
@Service
public class EPGIntegrationService {

	private static final Logger LOG = LoggerFactory.getLogger(EPGIntegrationService.class);
	
	@Value("${epg.payerNotPresetScenario.requestParam.transactionHint}")
	private String transactionHint;
	
	@Value("${epg.payerNotPresetScenario.requestParam.channel}")
	private String channel;
	
	@Value("${epg.payerNotPresetScenario.requestParam.customer}")
	private String customer;
	
	@Value("${epg.payerNotPresetScenario.requestParam.orderName}")
	private String orderName;
	
	@Value("${epg.payerNotPresetScenario.endpoint.url}")
	private String epgPayerNotPresentEndpointURL;
	
	@Value("${epg.endpoint.username}")
	private String epgEndpointUsername;
	
	@Value("${epg.endpoint.password}")
	private String epgEndpointPassword;
	
	@Value("${epg.payerNotPresetScenario.requestParam.Store}")
	private String store;
	
	@Value("${epg.payerNotPresetScenario.requestParam.Terminal}")
	private String terminal;
			
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	EPGTransactionRepository epgTransactionRepository;
	
	@Autowired
	EPGTransactionDomain epgTransactionDomain;
	
    public EPGResponse callEPGPayerNotPresentPayment(PurchaseRequestDto request, Headers header, ResultResponse result
    ) throws Exception {

    	LOG.info("inside EPGIntegrationService.callEPG");
    	EPGResponse epgPaymentResponse = new EPGResponse();
    	 int randomNumber = (int)Math.floor(Math.random()*(546546546 -346546546 + 1) + 346546546);
    	String orderId = Integer.toString(randomNumber);
     	LOG.info("printing OrderId:{}", header.getExternalTransactionId().substring(8));
    	EPGPaymentRequest paymentRequest = new EPGPaymentRequest();
    	Authorization authorization = new Authorization();
    	authorization.setCurrency(PaymentConstants.CURRENCY_AED);
    	authorization.setTransactionHint(transactionHint);
    	authorization.setOrderID(orderId);
    	authorization.setChannel(channel);
    	authorization.setAmount(request.getSpentAmount());
    	authorization.setCustomer(customer);
    	authorization.setTransactionID(request.getMasterEPGTransactionId());
    	authorization.setOrderName(orderName);
    	authorization.setStore(store);
    	authorization.setTerminal(terminal);
    	authorization.setUserName(epgEndpointUsername);
    	authorization.setPassword(epgEndpointPassword);
    	
    	paymentRequest.setAuthorization(authorization);
		
    	ServiceCallLogsDto serviceCallLogsDto = new ServiceCallLogsDto();
    	
		String jsonEPGReq = new Gson().toJson(paymentRequest);
		try {
			MultivaluedMap<String, String> headers = new MultivaluedMapImpl();  
			String url = epgPayerNotPresentEndpointURL;
			  Client client = Client.create();
			    WebResource.Builder builder = client.resource(url).accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).type(javax.ws.rs.core.MediaType.APPLICATION_JSON);
			    for (String key : headers.keySet()) {
			    	builder = builder.header(key, headers.getFirst(key));
			    }
			    long start = System.currentTimeMillis();
	            Gson g = new Gson();
			    LOG.info("EPG Request : {}", jsonEPGReq);
			    ClientResponse response = builder.post(ClientResponse.class, g.toJson(paymentRequest).getBytes());	
			    long end = System.currentTimeMillis();
			    
				String responseString = response.getEntity(String.class);
				LOG.info("EPG ResponseString : {}", responseString);
				
				epgPaymentResponse = g.fromJson(responseString, EPGResponse.class);
				LOG.info("EPG Payment Response Object : {}", epgPaymentResponse);
			
				if (epgPaymentResponse.getTransaction().getResponseClass().equalsIgnoreCase("0")) {
					EPGTransactionDomain epgTransactionDom = new EPGTransactionDomain.EPGTransactionDomainBuilder(epgPaymentResponse.getTransaction().getResponseCode(),
							epgPaymentResponse.getTransaction().getResponseClass(), epgPaymentResponse.getTransaction().getResponseDescription(),
							epgPaymentResponse.getTransaction().getResponseClassDescription(), epgPaymentResponse.getTransaction().getBalance().getValue(),
							epgPaymentResponse.getTransaction().getAmount().getValue(), epgPaymentResponse.getTransaction().getFees().getValue(), 
							epgPaymentResponse.getTransaction().getUniqueID()).account(epgPaymentResponse.getTransaction().getAccount()).accountNumber(request.getAccountNumber())
							.approvalCode(epgPaymentResponse.getTransaction().getApprovalCode()).cardBrand(epgPaymentResponse.getTransaction().getCardBrand())
							.cardNumber(epgPaymentResponse.getTransaction().getCardNumber()).cardToken(epgPaymentResponse.getTransaction().getCardToken())
							.cardType(epgPaymentResponse.getTransaction().getCardType()).createdDate(new Date()).createdUser(header.getUserName())
							.epgStatus(epgPaymentResponse.getTransaction().getResponseClassDescription()).epgTransactionId(epgPaymentResponse.getTransaction().getTransactionID())
							.language(epgPaymentResponse.getTransaction().getLanguage()).masterEPGTransactionId(request.getMasterEPGTransactionId())
							.orderId(epgPaymentResponse.getTransaction().getOrderID()).payer(epgPaymentResponse.getTransaction().getPayer().getInformation())
							.paymentType(request.getPaymentType()).programCode(header.getProgram()).purchaseItem(request.getSelectedPaymentItem())
							.subscriptionCatalogId(request.getSubscriptionCatalogId()).subscriptionId(request.getAdditionalParams()).externalTransactionId(header.getExternalTransactionId()).build();
					epgTransactionDomain.saveEPGTransaction(epgTransactionDom, header.getExternalTransactionId(), header.getUserName());
				}
				else {
					EPGTransactionDomain epgTransactionDom = new EPGTransactionDomain.EPGTransactionDomainBuilder(epgPaymentResponse.getTransaction().getResponseCode(),
						epgPaymentResponse.getTransaction().getResponseClass(), epgPaymentResponse.getTransaction().getResponseDescription(),
						epgPaymentResponse.getTransaction().getResponseClassDescription(), epgPaymentResponse.getTransaction().getBalance().getValue(),
						epgPaymentResponse.getTransaction().getAmount().getValue(), epgPaymentResponse.getTransaction().getFees().getValue(), 
						epgPaymentResponse.getTransaction().getUniqueID()).externalTransactionId(header.getExternalTransactionId()).epgStatus("Failure")
						.accountNumber(request.getAccountNumber()).masterEPGTransactionId(request.getMasterEPGTransactionId())
						.subscriptionCatalogId(request.getSubscriptionCatalogId()).subscriptionId(request.getAdditionalParams())
						.createdDate(new Date()).createdUser(header.getUserName()).programCode(header.getProgram()).build();

					epgTransactionDomain.saveEPGTransaction(epgTransactionDom, header.getExternalTransactionId(), header.getUserName());
				}
				
				serviceCallLogsDto.setCreatedDate(new Date());
				serviceCallLogsDto.setAction("EPG Subscription Renewal");
				serviceCallLogsDto.setTransactionId(header.getExternalTransactionId());
				serviceCallLogsDto.setServiceType("Outbound");
				serviceCallLogsDto.setRequest(jsonEPGReq);
				serviceCallLogsDto.setResponse(responseString);
				if (epgPaymentResponse.getTransaction().getResponseClass().equalsIgnoreCase("0")) {
					serviceCallLogsDto.setStatus(epgPaymentResponse.getTransaction().getResponseClassDescription());
				} else {
					serviceCallLogsDto.setStatus("Failure");
				}
				serviceCallLogsDto.setCreatedUser("");
				serviceCallLogsDto.setResponseTime((end-start));
					
				saveRequestResponse(serviceCallLogsDto); 
			
			result.getApiStatus().setStatusCode(Integer.valueOf(epgPaymentResponse.getTransaction().getResponseCode()));
			result.getApiStatus().setMessage(epgPaymentResponse.getTransaction().getResponseDescription());
			result.getApiStatus().setOverallStatus(epgPaymentResponse.getTransaction().getResponseClassDescription());
			result.getResult().setDescription(epgPaymentResponse.getTransaction().getResponseDescription());
			result.getResult().setResponse(epgPaymentResponse.getTransaction().getResponseClassDescription());
		
			
		} catch (Exception e) {
			result.getResult().setDescription("Exception Ocuured while EPG integration");
			result.getResult().setResponse("Failure");
			
			serviceCallLogsDto.setCreatedDate(new Date());
			serviceCallLogsDto.setAction("EPG Subscription Renewal");
			serviceCallLogsDto.setTransactionId(header.getExternalTransactionId());
			serviceCallLogsDto.setServiceType("Outbound");
			serviceCallLogsDto.setRequest(jsonEPGReq);
			serviceCallLogsDto.setResponse("Exception Ocuured while EPG integration: " +  e.getMessage());
			serviceCallLogsDto.setStatus("Failure");
			serviceCallLogsDto.setCreatedUser("");
				
			saveRequestResponse(serviceCallLogsDto); 
			LOG.error("Exception Ocuured while EPG integration: {}",e.getMessage());
			e.printStackTrace();
		}
    	
    	return epgPaymentResponse;
	
    }

	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		try{
		jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
		LOG.info("EPG integartion Request and response saved to ServiceCallLogs :" + callLog.toString());
		} catch(Exception e){
		LOG.info("JMS Exception Occured while saving EPG Request and Response to ServiceCallLogs");
		}
	}
}
