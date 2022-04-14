package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerTypeDto.class)
@ActiveProfiles("unittest")
public class CustomerTypeDtoTest {

	private CustomerTypeDto customerTypeDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		customerTypeDto = new CustomerTypeDto();
		customerTypeDto.setCustomerType("");
		CustomerTypeDto customerType = new CustomerTypeDto("");
		customerType.setCustomerType("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(customerTypeDto.getCustomerType());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(customerTypeDto.toString());
	}
	
}
