package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ParentChlidCustomer.class)
@ActiveProfiles("unittest")
public class ParentChlidCustomerTest {

	private ParentChlidCustomer parentChlidCustomer;

	@Before
	public void setUp() {
		parentChlidCustomer = new ParentChlidCustomer();
		parentChlidCustomer = new ParentChlidCustomer("","");
		parentChlidCustomer.setChild("");
		parentChlidCustomer.setParent("");
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(parentChlidCustomer.getChild());
		assertNotNull(parentChlidCustomer.getParent());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(parentChlidCustomer.toString());
	}
}
