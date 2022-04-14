package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BurnVoucherSequence.class)
@ActiveProfiles("unittest")
public class BurnVoucherSequenceTest {

	private BurnVoucherSequence burnVoucherSequence;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		burnVoucherSequence = new BurnVoucherSequence();
		burnVoucherSequence.setId("");
		burnVoucherSequence.setSeq(0l);
	}

	@Test
	public void testGetters() {
		assertNotNull(burnVoucherSequence.getId());
		assertNotNull(burnVoucherSequence.getSeq());
	}

	@Test
	public void testToString() {
		assertNotNull(burnVoucherSequence.toString());
	}

}