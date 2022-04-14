package com.loyalty.marketplace.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApiStatus.class)
@ActiveProfiles("unittest")
public class CommonApiStatusTest {

	private CommonApiStatus commonApiStatus;
	
	@Before
	public void setUp(){
		
		commonApiStatus = new CommonApiStatus("externalTransactionId");
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(commonApiStatus.toString());
	}
	
	@Test
	public void testGetExternalTransactionId()
	{
		String externalTransactionId = "externalTransactionId";
		commonApiStatus.setExternalTransactionId(externalTransactionId);
		assertEquals(commonApiStatus.getExternalTransactionId(), externalTransactionId);
	}
	
	@Test
	public void testGetMessage()
	{ 
		String msg = "msg";
		commonApiStatus.setMessage(msg);
		assertEquals(commonApiStatus.getMessage(), msg);
		
	}
	
	@Test
	public void testGetOverallStatus()
	{ 
		String status = "status";
		commonApiStatus.setOverallStatus(status);
		assertEquals(commonApiStatus.getOverallStatus(), status);
	}
	
	@Test
	public void testGetStatusCode()
	{ 
		Integer code = 123;
		commonApiStatus.setStatusCode(code);
		assertEquals(commonApiStatus.getStatusCode(), code);
	}
	
	@Test
	public void testGetErrors()
	{ 
		List<Errors> errors = new ArrayList<Errors>();
		commonApiStatus.setErrors(errors);
		assertEquals(commonApiStatus.getErrors(), errors);
	}
	
}
