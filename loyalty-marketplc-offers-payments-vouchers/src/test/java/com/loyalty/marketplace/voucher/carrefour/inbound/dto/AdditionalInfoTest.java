package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AdditionalInfo.class)
@ActiveProfiles("unittest")
public class AdditionalInfoTest {

	private AdditionalInfo additionalInfo;

	@Before
	public void setUp() throws Exception {
		additionalInfo = new AdditionalInfo();
		additionalInfo.setName("");
		additionalInfo.setValue("");
	}

	@Test
	public void testGetters() {
		assertNotNull(additionalInfo.getName());
		assertNotNull(additionalInfo.getValue());
	}

	@Test
	public void testToString() {
		assertNotNull(additionalInfo.toString());
	}

}
