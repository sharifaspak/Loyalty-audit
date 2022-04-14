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

@SpringBootTest(classes = BrandDescription.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BrandDescriptionTest {
	
	@InjectMocks
	BrandDescription brandDescriptionMock;
	
	private BrandDescription brandDescription;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		brandDescription = new BrandDescription();
		brandDescription.setBrandDescriptionEn("");
		brandDescription.setBrandDescriptionAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(brandDescription.getBrandDescriptionEn());
	    assertNotNull(brandDescription.getBrandDescriptionAr());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(brandDescription.toString());
	    
			
	}
		
}
