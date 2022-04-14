package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OfferInfo.class)
@ActiveProfiles("unittest")
public class OfferInfoTest {
	
	private  OfferInfo  offerInfo;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		offerInfo = new OfferInfo();
		offerInfo.setOffer("");
		offerInfo.setSubOffer("");
		
	}

	@Test
	public void testGetters() {
		assertNotNull(offerInfo.getOffer());
		assertNotNull(offerInfo.getSubOffer());
	}
	
	@Test
	public void testToString() {
		assertNotNull(offerInfo.toString());
	}

}