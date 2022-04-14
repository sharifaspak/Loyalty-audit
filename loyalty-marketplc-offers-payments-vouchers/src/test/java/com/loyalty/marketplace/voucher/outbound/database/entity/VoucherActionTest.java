package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherAction.class)
@ActiveProfiles("unittest")
public class VoucherActionTest {

	private VoucherAction voucherAction;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherAction = new VoucherAction();
		voucherAction.setAction("");
		voucherAction.setCreatedDate(new Date());
		voucherAction.setCreatedUser("");
		voucherAction.setId("");
		voucherAction.setLabel("");
		voucherAction.setProgram("");
		voucherAction.setRedemptionMethod("");
		voucherAction.setUpdatedDate(new Date());
		voucherAction.setUpdatedUser("");
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherAction.getAction());
		assertNotNull(voucherAction.getCreatedDate());
		assertNotNull(voucherAction.getCreatedUser());
		assertNotNull(voucherAction.getId());
		assertNotNull(voucherAction.getLabel());
		assertNotNull(voucherAction.getProgram());
		assertNotNull(voucherAction.getRedemptionMethod());
		assertNotNull(voucherAction.getUpdatedDate());
		assertNotNull(voucherAction.getUpdatedUser());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherAction.toString());
	}

}