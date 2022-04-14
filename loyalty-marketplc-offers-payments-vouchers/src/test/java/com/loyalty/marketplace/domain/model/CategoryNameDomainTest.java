package com.loyalty.marketplace.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=CategoryNameDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class CategoryNameDomainTest {
	
	private CategoryNameDomain categoryName;
		
	@Before
	public void setUp(){
		
		categoryName = new CategoryNameDomain("", "");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(categoryName.getCategoryNameEn());
	    assertNotNull(categoryName.getCategoryNameAr());
	    
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
	   
		assertNotNull(categoryName.toString());
	}	
}
