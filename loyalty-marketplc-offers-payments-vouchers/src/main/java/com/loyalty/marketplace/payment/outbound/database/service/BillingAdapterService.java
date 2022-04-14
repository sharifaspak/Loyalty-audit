package com.loyalty.marketplace.payment.outbound.database.service;

import java.math.BigInteger;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Date;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jms.core.JmsTemplate;

import com.loyalty.marketplace.payment.constants.MarketplaceCode;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.outbound.database.entity.ThirdPartyCallLog;
import com.loyalty.marketplace.payment.outbound.database.repository.ThirdPartyCallLogRepository;
import com.loyalty.marketplace.payment.utils.Utils;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;

import ae.billingadapter.etisalat._1.AdditionInfo;
import ae.billingadapter.etisalat._1.ChargeImmediateRequest;
import ae.billingadapter.etisalat._1.ChargeImmediatelyResponse;
import ae.billingadapter.etisalat._1.ExtraPrams;
import ae.billingadapter.etisalat._1_0.BillingAdapterWS4;
import ae.billingadapter.etisalat._1_0.BillingAdapterWSv4;

@Service
public class BillingAdapterService {

	private static final Logger LOG = LoggerFactory.getLogger(BillingAdapterService.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${payment.billingadapter.uri}")
	private String billingAdapterUri;
	
	@Value("${payment.billingadapter.username}")
	private String billingAdapterUsername;
	
	@Value("${payment.billingadapter.password}")
	private String billingAdapterPassword;
	
	@Value("${payment.billingadapter.transactionId}")
	private String billingAdapterTransactionId;
	
	@Value("${payment.billingadapter.contentProviderId}")
	private String billingAdapterContentProviderId;
	
	@Autowired
	ThirdPartyCallLogRepository callLogRepository;
	
	/**
	 * Rechange or Bill payment, calling billing adapter service
	 * @param accountNbr
	 * @param amount
	 * @return
	 * @throws MarketplaceException
	 */
	public ChargeImmediatelyResponse billingAdapterCall(String accountNbr, int amount, String externalRef) throws MarketplaceException {
		URL wsdlUrl = null;
		 try {
			 wsdlUrl = new URL(billingAdapterUri+"?wsdl");
	        } catch (MalformedURLException e) {
	        	LOG.info("Cannot create URL from : " + billingAdapterUri+"?wsdl");
	        }
		 LOG.info("URL for BillingAdapter : " + billingAdapterUri);
		 LOG.info("BillingAdapter User Name : " + billingAdapterUsername);
		
		BillingAdapterWSv4 service = new BillingAdapterWSv4(wsdlUrl);
		BillingAdapterWS4 port = service.getPort(BillingAdapterWS4.class);
		BindingProvider provider = (BindingProvider)port;
		provider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, billingAdapterUsername);
		provider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, billingAdapterPassword);
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, billingAdapterUri);
		ChargeImmediatelyResponse response = new ChargeImmediatelyResponse();
		ChargeImmediateRequest chargeReq = new ChargeImmediateRequest();
		chargeReq.setSubscriberID(accountNbr);
		chargeReq.setAmount(amount);
		String channel = "WEB";
		BigInteger barcode = new BigInteger("0");
		BigInteger revPer = new BigInteger("100");
		AdditionInfo additionalInfo = new AdditionInfo();
		additionalInfo.setContentId(new BigInteger("579"));
		additionalInfo.setContentName(MarketplaceConstants.CONTENT_NAME.getConstant());
		additionalInfo.setChannel(channel);
		additionalInfo.setTransactionId(new BigInteger(billingAdapterTransactionId));
		additionalInfo.setSubscriptionType(new BigInteger("3"));
		additionalInfo.setBarCode(barcode);
		additionalInfo.setContentProviderId(new BigInteger(billingAdapterContentProviderId));
		additionalInfo.setRevenuePercent(revPer);
		ExtraPrams extraParam = new ExtraPrams();
		extraParam.setParamName("VAT_FLAG");
		extraParam.setParamValue("Zero");
		additionalInfo.getAdditionalParams().add(extraParam);
		chargeReq.setAdditionalInformation(additionalInfo);
		String reqXml = Utils.toXML(chargeReq);
		ThirdPartyCallLog callLog = new ThirdPartyCallLog();
		LOG.info("-----------Billing Adapter Request XML Start--------------");
		LOG.info(reqXml);
		LOG.info("-----------Billing Adapter Request XML End--------------");
		ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
		
			long start = System.currentTimeMillis();
			try {
			response = port.chargeImmediate(chargeReq);
			}
			catch(Exception e) {
				LOG.error("Billing Adapter Service Call failed" + e.getMessage());
				throw new MarketplaceException(this.getClass().toString(), "billingAdapterCall",
						e.getClass() + e.getMessage(), MarketplaceCode.BILLING_ADAPTER_FAILED);
			}
			long end = System.currentTimeMillis();
			if(null != response)
			{
			LOG.info("-----------Billing Adapter Response XML Start--------------");
			LOG.info(Utils.toXML(response));
			LOG.info("-----------Billing Adapter Response XML End--------------");
		
			serviceCallLogsDto.setAccountNumber(accountNbr);
			serviceCallLogsDto.setCreatedDate(new Date());
			serviceCallLogsDto.setAction("Billing Adapter");
			serviceCallLogsDto.setTransactionId(externalRef);
			serviceCallLogsDto.setServiceType("Outbound");
			serviceCallLogsDto.setRequest(reqXml);
			serviceCallLogsDto.setResponse(Utils.toXML(response));
			if (response.getStatusCode() == 0) {
				serviceCallLogsDto.setStatus("Success");
			} else {
				serviceCallLogsDto.setStatus("Failed");
			}
			serviceCallLogsDto.setResponseTime((end-start));
			
			
			saveRequestResponse(serviceCallLogsDto);
			}
		
		return response;
	}
	
	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		try{
			LOG.info("Requestand response saved :" + callLog.toString());
			jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
		} catch(Exception e){
			LOG.info("JMS Exception Occured");
		}
	}


	public BillingAdapterService() {
		super();
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(billingAdapterUsername,
						billingAdapterPassword.toCharArray());
			}
		});
	}
}
