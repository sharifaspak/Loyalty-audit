package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ReconciliationRecords.class)
@ActiveProfiles("unittest")
public class ReconciliationRecordsTest {

	private ReconciliationRecords reconciliationRecords;

	@Before
	public void setUp() throws Exception {
		reconciliationRecords = new ReconciliationRecords();
		reconciliationRecords.setAmount("");
		reconciliationRecords.setBalance("");
		reconciliationRecords.setCardNumber("");
		reconciliationRecords.setCashierId("");
		reconciliationRecords.setCurrency("");
		reconciliationRecords.setFinalStatus("");
		reconciliationRecords.setGenCode("");
		reconciliationRecords.setLineCount("");
		reconciliationRecords.setReferenceNumber("");
		reconciliationRecords.setTerminalId("");
		reconciliationRecords.setTerminalTxNo("");
		reconciliationRecords.setTransactionNumber("");
		reconciliationRecords.setTransactionType("");

	}

	@Test
	public void test() {
		assertNotNull(reconciliationRecords.getAmount());
		assertNotNull(reconciliationRecords.getBalance());
		assertNotNull(reconciliationRecords.getCardNumber());
		assertNotNull(reconciliationRecords.getCashierId());
		assertNotNull(reconciliationRecords.getCurrency());
		assertNotNull(reconciliationRecords.getFinalStatus());
		assertNotNull(reconciliationRecords.getGenCode());
		assertNotNull(reconciliationRecords.getLineCount());
		assertNotNull(reconciliationRecords.getReferenceNumber());
		assertNotNull(reconciliationRecords.getTerminalId());
		assertNotNull(reconciliationRecords.getTerminalTxNo());
		assertNotNull(reconciliationRecords.getTransactionNumber());
		assertNotNull(reconciliationRecords.getTransactionType());
	}

	@Test
	public void testToString() {
		assertNotNull(reconciliationRecords.toString());
	}

}
