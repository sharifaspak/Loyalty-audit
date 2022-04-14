package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.domain.VoucherReconciliationDomain.VoucherReconciliationBuilder;
import com.loyalty.marketplace.voucher.outbound.database.entity.ReconciliationInfo;

@SpringBootTest(classes = VoucherReconciliationDomain.class)
@ActiveProfiles("unittest")
public class VoucherReconciliationDomainTest {

	private VoucherReconciliationDomain voucherReconciliationDomain;

	@Before
	public void setUp() throws Exception {

		String programCode = "";
		String partnerCode = "";
		Date startDateTime = new Date();
		Date endDateTime = new Date();
		ReconciliationInfo loyalty = new ReconciliationInfo();
		ReconciliationInfo partner = new ReconciliationInfo();
		Date createdDate = new Date();
		String createdUser = "";
		Date updatedDate = new Date();
		String updatedUser = "";
		VoucherReconciliationBuilder voucherBuilder = new VoucherReconciliationDomain.VoucherReconciliationBuilder(
				programCode, partnerCode, startDateTime, endDateTime, new ReconciliationInfoDomain(),  new ReconciliationInfoDomain(), new VoucherReconciliationDataDomain(), createdDate, createdUser,
				updatedDate, updatedUser);
		voucherBuilder.createdDate(createdDate).createdUser(createdUser).endDateTime(endDateTime).loyalty(new ReconciliationInfoDomain())
				.partner(new ReconciliationInfoDomain()).partnerCode(partnerCode).programCode(programCode).startDateTime(startDateTime)
				.updatedDate(updatedDate).updatedUser(updatedUser).build();
		voucherReconciliationDomain = new VoucherReconciliationDomain(voucherBuilder);
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherReconciliationDomain.getCreatedDate());
		assertNotNull(voucherReconciliationDomain.getCreatedUser());
		assertNotNull(voucherReconciliationDomain.getEndDateTime());
		assertNull(voucherReconciliationDomain.getId());
		assertNotNull(voucherReconciliationDomain.getLoyalty());
		assertNotNull(voucherReconciliationDomain.getPartner());
		assertNotNull(voucherReconciliationDomain.getPartnerCode());
		assertNotNull(voucherReconciliationDomain.getProgramCode());
		assertNotNull(voucherReconciliationDomain.getStartDateTime());
		assertNotNull(voucherReconciliationDomain.getUpdatedDate());
		assertNotNull(voucherReconciliationDomain.getUpdatedUser());

	}

	@Test
	public void testBuilder() {
		voucherReconciliationDomain = new VoucherReconciliationDomain();
		assertNotNull(voucherReconciliationDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(voucherReconciliationDomain.toString());
	}

}
