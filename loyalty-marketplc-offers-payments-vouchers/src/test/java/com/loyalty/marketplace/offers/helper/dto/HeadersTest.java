package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Headers.class)
@ActiveProfiles("unittest")
public class HeadersTest {

	private Headers headers;
	
	@Before
	public void setUp(){
		headers = new Headers();
		headers.setProgram("");
		headers.setAuthorization("");
		headers.setToken("");
		headers.setTransactionId("");
		headers.setChannelId("");
		headers.setExternalTransactionId("");
		headers.setSessionId("");
		headers.setSystemId("");
		headers.setSystemPassword("");
		headers.setUserName("");
		headers.setUserPrev("");
	}
	
	@Test
	public void testGetters() {
		assertNotNull(headers.getProgram());
		assertNotNull(headers.getAuthorization());
		assertNotNull(headers.getToken());
		assertNotNull(headers.getTransactionId());
		assertNotNull(headers.getChannelId());
		assertNotNull(headers.getExternalTransactionId());
		assertNotNull(headers.getSessionId());
		assertNotNull(headers.getSystemId());
		assertNotNull(headers.getSystemPassword());
		assertNotNull(headers.getUserName());
		assertNotNull(headers.getUserPrev());
	}
	
	@Test
	public void testToString() {
		assertNotNull(headers.toString());
	}
	
}
