package com.loyalty.marketplace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.loyalty.marketplace.payment.utils.SOAPConnector;

@Configuration
public class Config {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPaths("ae.billingadapter.etisalat._1");
		return marshaller;
	}
	
	@Bean
    public SOAPConnector soapConnector(Jaxb2Marshaller marshaller) throws Exception {
        SOAPConnector client = new SOAPConnector();
        //client.setDefaultUri("http://localhost:7205/NotificationService/SendNotificationService");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        //client.setMessageSender(new BillingAdapterMessageSenderWithAuth());
        return client;
    }
}
