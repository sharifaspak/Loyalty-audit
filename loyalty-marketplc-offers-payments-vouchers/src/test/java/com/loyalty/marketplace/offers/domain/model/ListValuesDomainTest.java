package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = ListValuesDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ListValuesDomainTest {
	
	private ListValuesDomain listValues;
		
	@Before
	public void setUp(){
		listValues = new ListValuesDomain();
		listValues = new ListValuesDomain(new ArrayList<String>(), new ArrayList<String>());
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(listValues.getEligibleTypes());
	    assertNotNull(listValues.getExclusionTypes());
			
	}
		
}
