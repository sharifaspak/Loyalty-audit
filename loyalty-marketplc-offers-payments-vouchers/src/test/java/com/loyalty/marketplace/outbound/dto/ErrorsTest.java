package com.loyalty.marketplace.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Errors.class)
@ActiveProfiles("unittest")
public class ErrorsTest {

	private Errors errors;
	
	@Before
	public void setUp(){
		errors = new Errors();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(errors.toString());
	}
	
	@Test
	public void testGetCode()
	{
		Integer errorCode = 123;
		errors.setCode(errorCode);
		assertEquals(errors.getCode(), errorCode);
	}
	
	@Test
	public void testGetMessage()
	{ 
		String msg = "Error Message";
		errors.setMessage(msg);
		assertEquals(errors.getMessage(), msg);
	}
	
}
