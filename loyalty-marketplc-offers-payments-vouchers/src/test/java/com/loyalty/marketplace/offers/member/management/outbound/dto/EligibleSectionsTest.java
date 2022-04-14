package com.loyalty.marketplace.offers.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EligibileSections.class)
@ActiveProfiles("unittest")
public class EligibleSectionsTest {

	private EligibileSections eligibileSections;

	@Before
	public void setUp() {

		eligibileSections = new EligibileSections();
		eligibileSections.setSectionId("");
		eligibileSections.setSectionName("");
		eligibileSections.setSectionDescription("");
		eligibileSections.setSectionType("");
		
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(eligibileSections.getSectionId());
		assertNotNull(eligibileSections.getSectionName());
		assertNotNull(eligibileSections.getSectionType());
		assertNotNull(eligibileSections.getSectionDescription());
		

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(eligibileSections.toString());
	}
}
