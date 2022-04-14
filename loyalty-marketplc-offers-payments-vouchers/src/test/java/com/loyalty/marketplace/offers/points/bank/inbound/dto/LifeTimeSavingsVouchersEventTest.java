package com.loyalty.marketplace.offers.points.bank.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = LifeTimeSavingsVouchersEvent.class)
@ActiveProfiles("unittest")
public class LifeTimeSavingsVouchersEventTest {

	private LifeTimeSavingsVouchersEvent lifeTimeSavingsVouchersEvent;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		lifeTimeSavingsVouchersEvent = new LifeTimeSavingsVouchersEvent();
		lifeTimeSavingsVouchersEvent.setUsername("");
		lifeTimeSavingsVouchersEvent.setMembershipCode("");
		lifeTimeSavingsVouchersEvent.setAccountNumber("");
		lifeTimeSavingsVouchersEvent.setQuantity(10);
		lifeTimeSavingsVouchersEvent.setHasEstimatedSavings(true);
		lifeTimeSavingsVouchersEvent.setEstimatedSavings(2900.08);
		lifeTimeSavingsVouchersEvent.setSpendValue(3000.09);
		lifeTimeSavingsVouchersEvent.setType("");
		lifeTimeSavingsVouchersEvent.setExternalTransactionId("");

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(lifeTimeSavingsVouchersEvent.getAccountNumber());
		assertNotNull(lifeTimeSavingsVouchersEvent.getMembershipCode());
		assertNotNull(lifeTimeSavingsVouchersEvent.getAccountNumber());
		assertNotNull(lifeTimeSavingsVouchersEvent.getQuantity());
		assertNotNull(lifeTimeSavingsVouchersEvent.isHasEstimatedSavings());
		assertNotNull(lifeTimeSavingsVouchersEvent.getEstimatedSavings());
		assertNotNull(lifeTimeSavingsVouchersEvent.getSpendValue());
		assertNotNull(lifeTimeSavingsVouchersEvent.getType());
		assertNotNull(lifeTimeSavingsVouchersEvent.getExternalTransactionId());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(lifeTimeSavingsVouchersEvent.toString());
	}

}
