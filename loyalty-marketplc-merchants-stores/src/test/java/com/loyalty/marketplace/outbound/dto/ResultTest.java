package com.loyalty.marketplace.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Result.class)
@ActiveProfiles("unittest")
public class ResultTest {

	private Result result;
	
	@Before
	public void setUp(){
		result = new Result();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(result.toString());
	}
	
	@Test
	public void testGetResponse()
	{
		String response = "Result response";
		result.setResponse(response);;
		assertEquals(result.getResponse(), response);
	}
	
	@Test
	public void testGetDescription()
	{ 
		String desc = "Result description";
		result.setDescription(desc);
		assertEquals(result.getDescription(), desc);
	}
	
}
