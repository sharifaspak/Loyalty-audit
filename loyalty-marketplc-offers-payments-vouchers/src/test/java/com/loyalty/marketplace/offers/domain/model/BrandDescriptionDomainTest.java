package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = BrandDescriptionDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BrandDescriptionDomainTest {
	
	private BrandDescriptionDomain brandDescription;
		
	@Before
	public void setUp(){
		
		brandDescription = new BrandDescriptionDomain();
		brandDescription = new BrandDescriptionDomain("En","Ar");
				
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
		
}
