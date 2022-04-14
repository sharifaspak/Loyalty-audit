package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.domain.ReconciliationDataInfoDomain.ReconciliationDataInfoBuilder;

@SpringBootTest(classes = VoucherReconciliationDataDomain.class)
@ActiveProfiles("unittest")
public class ReconciliationDataInfoDomainTest {

	private ReconciliationDataInfoDomain reconciliationDataInfoDomain;

	@Before
	public void setUp() throws Exception {		
		ReconciliationDataInfoBuilder reconciliationDataInfoBuilder = new ReconciliationDataInfoDomain.ReconciliationDataInfoBuilder("", "", "", "", "", "", 0.0, new Date());

		reconciliationDataInfoBuilder.loyaltyTransactionId("").orderReferenceNumber("").partnerCode("").partnerReferenceNumber("").partnerTransactionId("").
		transactionDate(new Date()).voucherAmount(0.0).voucherCode("").build();
		
		reconciliationDataInfoDomain = new ReconciliationDataInfoDomain(reconciliationDataInfoBuilder);
	}

	@Test
	public void test() {
		assertNotNull(reconciliationDataInfoDomain.getLoyaltyTransactionId());
		assertNotNull(reconciliationDataInfoDomain.getOrderReferenceNumber());
		assertNotNull(reconciliationDataInfoDomain.getPartnerCode());
		assertNotNull(reconciliationDataInfoDomain.getPartnerReferenceNumber());
		assertNotNull(reconciliationDataInfoDomain.getPartnerTransactionId());
		assertNotNull(reconciliationDataInfoDomain.getTransactionDate());
		assertNotNull(reconciliationDataInfoDomain.getVoucherAmount());
		assertNotNull(reconciliationDataInfoDomain.getVoucherCode());
	}

	@Test
	public void testBuilder() {
		reconciliationDataInfoDomain = new ReconciliationDataInfoDomain();
		assertNotNull(reconciliationDataInfoDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(reconciliationDataInfoDomain.toString());
	}

}
