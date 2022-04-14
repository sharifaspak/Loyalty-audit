package com.loyalty.marketplace.merchants.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TAndC.class)
@ActiveProfiles("unittest")
public class TAndCTest {

	private TAndC tAndC;
	private String testString;
	
	@Before
	public void setUp(){
		tAndC = new TAndC();
		testString = "testValue";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(tAndC.toString());
	}
	
	@Test
	public void testGetTnCEn()
	{
		tAndC.setTnCEn(testString);
		assertEquals(tAndC.getTnCEn(), testString);
	}
	
	@Test
	public void testGetTnCAr()
	{
		tAndC.setTnCAr(testString);
		assertEquals(tAndC.getTnCAr(), testString);
	}
	
}
