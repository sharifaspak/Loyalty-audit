package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.stores.outbound.database.entity.Description;

@SpringBootTest(classes=Description.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class DescriptionTest {
	
	private Description description;
		
	@Before
	public void setUp(){
		
		description = new Description();
		description.setDescriptionEn("");
		description.setDescriptionAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		
		assertNotNull(description.getDescriptionEn());
	    assertNotNull(description.getDescriptionAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(description.toString());
	    
			
	}
		
}
