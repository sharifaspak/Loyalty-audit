package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherValues.class)
@ActiveProfiles("unittest")
public class VoucherValuesTest {

	private VoucherValues voucherValues;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherValues = new VoucherValues();
		voucherValues.setCost(0.0);
		voucherValues.setPointsValue(0l);
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherValues.getCost());
		assertNotNull(voucherValues.getPointsValue());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherValues.toString());
	}

}