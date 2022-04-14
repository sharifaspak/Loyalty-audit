package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VoucherActionDomain.class)
@ActiveProfiles("unittest")
public class VoucherActionDomainTest {

	private VoucherActionDomain voucherActionDomain;

	@Before
	public void setUp() throws Exception {

		VoucherActionDomain.VoucherActionBuilder voucherActionBuilder = new VoucherActionDomain.VoucherActionBuilder("",
				"", "", "");
		voucherActionDomain = voucherActionBuilder.id("").createdDate(new Date()).createdUser("")
				.updatedDate(new Date()).updatedUser("").build();

		voucherActionDomain = new VoucherActionDomain(voucherActionBuilder);

	}

	@Test
	public void testGetters() {
		assertNotNull(voucherActionDomain.getAction());
		assertNotNull(voucherActionDomain.getCreatedDate());
		assertNotNull(voucherActionDomain.getCreatedUser());
		assertNotNull(voucherActionDomain.getId());
		assertNotNull(voucherActionDomain.getLabel());
		assertNotNull(voucherActionDomain.getProgram());
		assertNotNull(voucherActionDomain.getRedemptionMethod());
		assertNotNull(voucherActionDomain.getUpdatedDate());
		assertNotNull(voucherActionDomain.getUpdatedUser());

	}
	
	@Test
	public void testBuilder() {
		voucherActionDomain = new VoucherActionDomain();
		assertNotNull(voucherActionDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(voucherActionDomain.toString());
	}

}
