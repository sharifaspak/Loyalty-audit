package com.loyalty.marketplace.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ResultResponse.class)
@ActiveProfiles("unittest")
public class ResultResponseTest {

	private ResultResponse resultResponse;
	
	@Before
	public void setUp(){
		resultResponse = new ResultResponse(null);
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(resultResponse.toString());
	}
	
	@Test
	public void testGetResult()
	{ 
		Result result = new Result();
		resultResponse.setResult(result);
		assertEquals(resultResponse.getResult() , result);
	}
	
}
