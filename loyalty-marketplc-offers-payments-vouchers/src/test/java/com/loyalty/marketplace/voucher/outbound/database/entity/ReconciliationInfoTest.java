package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ReconciliationInfo.class)
@ActiveProfiles("unittest")
public class ReconciliationInfoTest {

	private ReconciliationInfo reconciliationInfo;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		reconciliationInfo = new ReconciliationInfo();
		reconciliationInfo.setNoofCountTransaction(0);
		reconciliationInfo.setTotalAmount(0.0);
	}

	@Test
	public void testGetters() {
		assertNotNull(reconciliationInfo.getNoofCountTransaction());
		assertNotNull(reconciliationInfo.getTotalAmount());
	}

	@Test
	public void testToString() {
		assertNotNull(reconciliationInfo.toString());
	}

}