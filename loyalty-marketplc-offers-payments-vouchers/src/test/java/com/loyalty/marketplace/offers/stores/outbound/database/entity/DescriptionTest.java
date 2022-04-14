package com.loyalty.marketplace.offers.stores.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@SpringBootTest(classes=Description.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DescriptionTest {
	
	Description description;
	
	private String testString;
		
	@Before
	public void setUp(){
		description = new Description();
		testString = "Test String";
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(description.toString());
	}
	@Test
	public void testGetDescriptionEn()
	{
		description.setDescriptionAr(testString);;
		assertEquals(description.getDescriptionAr(), testString);
	}
	
	@Test
	public void testGetDescriptionAr()
	{
		description.setDescriptionAr(testString);
		assertEquals(description.getDescriptionAr(), testString);
	}
		
}

