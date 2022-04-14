package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=Name.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class NameTest {
	
	
	@InjectMocks
	Name nameMock;
	
	private Name name;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		name = new Name();
		name.setEnglish("");
		name.setArabic("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(name.getEnglish());
	    assertNotNull(name.getArabic());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(name.toString());
	    
			
	}
		
}
