package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.domain.ReconciliationInfoDomain.ReconciliationInfoBuilder;

@SpringBootTest(classes = VoucherReconciliationDataDomain.class)
@ActiveProfiles("unittest")
public class ReconciliationInfoDomainTest {

	private ReconciliationInfoDomain reconciliationInfoDomain;

	@Before
	public void setUp() throws Exception {		
		ReconciliationInfoBuilder reconciliationInfoBuilder = new ReconciliationInfoDomain.ReconciliationInfoBuilder(0, 0.0);

		reconciliationInfoBuilder.noofCountTransaction(0).totalAmount(0.0).build();
		
		reconciliationInfoDomain = new ReconciliationInfoDomain(reconciliationInfoBuilder);
	}

	@Test
	public void test() {
		assertNotNull(reconciliationInfoDomain.getNoofCountTransaction());
		assertNotNull(reconciliationInfoDomain.getTotalAmount());		
	}

	@Test
	public void testBuilder() {
		reconciliationInfoDomain = new ReconciliationInfoDomain();
		assertNotNull(reconciliationInfoDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(reconciliationInfoDomain.toString());
	}

}
