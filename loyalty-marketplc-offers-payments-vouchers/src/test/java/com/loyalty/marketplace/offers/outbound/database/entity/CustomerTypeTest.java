package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = CustomerType.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class CustomerTypeTest {
	
	@InjectMocks
	CustomerType customerTypeMock;
	
	private CustomerType customerType;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		customerType = new CustomerType();
		customerType.setEligibleCustomerTypes(new ArrayList<String>());
		customerType.setExclusionTypes(new ArrayList<String>());
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(customerType.getEligibleCustomerTypes());
	    assertNotNull(customerType.getExclusionTypes());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(customerType.toString());
	    
			
	}
		
}
