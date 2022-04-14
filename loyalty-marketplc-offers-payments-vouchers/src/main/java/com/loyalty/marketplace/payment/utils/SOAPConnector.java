package com.loyalty.marketplace.payment.utils;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.loyalty.marketplace.BillingAdapterMessageSenderWithAuth;

public class SOAPConnector extends WebServiceGatewaySupport {

	public Object callWebService(String url, Object request) {
		//SoapActionCallback soapCallBack = new SoapActionCallback("http://10.32.248.166:7103/BProcessSMSSender/SubmitSMServiceService");
		return getWebServiceTemplate().marshalSendAndReceive(url, request);
	}
	
	public Object callAuthWebService(String url, Object request) {
		getWebServiceTemplate().setMessageSender(new BillingAdapterMessageSenderWithAuth());
		return getWebServiceTemplate().marshalSendAndReceive(url, request);
	}
}

