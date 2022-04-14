package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerTypeListResult.class)
@ActiveProfiles("unittest")
public class CustomerTypeListResultTest {
	
	private CustomerTypeListResult customerTypeListResult;
	
	@Before
	public void setUp() {
		customerTypeListResult= new CustomerTypeListResult();
		customerTypeListResult= new CustomerTypeListResult(new ArrayList<ParentChlidCustomer>());
		customerTypeListResult.setCustomerTypeList(new ArrayList<ParentChlidCustomer>());
	}
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(customerTypeListResult.getCustomerTypeList());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(customerTypeListResult.toString());
	}
}

