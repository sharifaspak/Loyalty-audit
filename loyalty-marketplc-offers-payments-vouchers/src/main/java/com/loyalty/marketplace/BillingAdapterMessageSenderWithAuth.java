package com.loyalty.marketplace;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import sun.misc.BASE64Encoder;

public class BillingAdapterMessageSenderWithAuth extends HttpUrlConnectionMessageSender {

	@Override
	protected void prepareConnection(HttpURLConnection connection)
			throws IOException {
		
		  
		  @SuppressWarnings("restriction") BASE64Encoder enc = new
		  sun.misc.BASE64Encoder(); String userpassword = "loyaltyBAUser:Password@123";
		  
		  @SuppressWarnings("restriction") String encodedAuthorization = enc.encode(
		  userpassword.getBytes() ); connection.setRequestProperty("Authorization",
		  "Basic " + encodedAuthorization);
		  
		  super.prepareConnection(connection);
		 }
}
