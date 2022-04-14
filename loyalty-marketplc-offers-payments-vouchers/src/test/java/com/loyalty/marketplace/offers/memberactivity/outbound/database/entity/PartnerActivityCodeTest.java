package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PartnerActivityCode.class)
@ActiveProfiles("unittest")
public class PartnerActivityCodeTest {

	private PartnerActivityCode partnerActivityCode;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		partnerActivityCode = new PartnerActivityCode();
		partnerActivityCode.setCode("");
		partnerActivityCode.setDescription(new ActivityDescription());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(partnerActivityCode.getCode());
		assertNotNull(partnerActivityCode.getDescription());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(partnerActivityCode.toString());
	}
	
}
