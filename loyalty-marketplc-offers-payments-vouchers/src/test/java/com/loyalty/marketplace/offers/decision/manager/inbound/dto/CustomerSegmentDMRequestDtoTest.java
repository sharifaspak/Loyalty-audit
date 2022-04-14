package com.loyalty.marketplace.offers.decision.manager.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerSegmentDMRequestDto.class)
@ActiveProfiles("unittest")
public class CustomerSegmentDMRequestDtoTest {

	private CustomerSegmentDMRequestDto decisionManagerRequestDto;

	@Before
	public void setUp() {
		decisionManagerRequestDto = new CustomerSegmentDMRequestDto();
		decisionManagerRequestDto.setMemberDetailsList(new ArrayList<>());
	}

	/**
	 * Test Getters
	 */
	@Test
	public void testGetters() {
		assertNotNull(decisionManagerRequestDto.getMemberDetailsList());
			}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		assertNotNull(decisionManagerRequestDto.toString());
	}
}
