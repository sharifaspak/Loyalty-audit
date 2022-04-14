package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.domain.VoucherReconciliationDataDomain.VoucherReconciliationDataBuilder;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders;
import com.loyalty.marketplace.voucher.outbound.database.entity.ReconciliationDataInfo;

@SpringBootTest(classes = VoucherReconciliationDataDomain.class)
@ActiveProfiles("unittest")
public class VoucherReconciliationDataDomainTest {

	private VoucherReconciliationDataDomain voucherReconciliationDataDomain;

	@Before
	public void setUp() throws Exception {
		List<ReconciliationDataInfo> partnerReconciliationData = new ArrayList<ReconciliationDataInfo>();
		List<Orders> partnerContent = new ArrayList<Orders>();
		Date createdDate = new Date();
		String createdUser = "";
		Date updatedDate = new Date();
		String updatedUser = "";
		ArrayList<ReconciliationDataInfo> loyaltyReconciliationData = new ArrayList<ReconciliationDataInfo>();
		String reconciliationLevel = "";
		String programCode = "";
		VoucherReconciliationDataBuilder voucherReconciliationDataBuilder = new VoucherReconciliationDataDomain.VoucherReconciliationDataBuilder(
				programCode, reconciliationLevel, new ArrayList<ReconciliationDataInfoDomain>(), new ArrayList<ReconciliationDataInfoDomain>(), partnerContent,
				createdDate, createdUser, updatedDate, updatedUser, "");

		voucherReconciliationDataBuilder.createdDate(new Date()).createdUser("")
				.loyaltyReconciliationData(new ArrayList<ReconciliationDataInfoDomain>()).partnerCode("").partnerContent(partnerContent)
				.partnerReconciliationData(new ArrayList<ReconciliationDataInfoDomain>()).programCode(programCode).updatedDate(updatedDate)
				.updatedUser(updatedUser).build();

		voucherReconciliationDataDomain = new VoucherReconciliationDataDomain(voucherReconciliationDataBuilder);
	}

	@Test
	public void test() {
		assertNotNull(voucherReconciliationDataDomain.getCreatedDate());
		assertNotNull(voucherReconciliationDataDomain.getCreatedUser());
		assertNotNull(voucherReconciliationDataDomain.getId());
		assertNotNull(voucherReconciliationDataDomain.getLoyaltyReconciliationData());
		assertNotNull(voucherReconciliationDataDomain.getPartnerContent());
		assertNotNull(voucherReconciliationDataDomain.getPartnerReconciliationData());
		assertNotNull(voucherReconciliationDataDomain.getProgramCode());
		assertNotNull(voucherReconciliationDataDomain.getReconciliationLevel());
		assertNotNull(voucherReconciliationDataDomain.getUpdatedDate());
		assertNotNull(voucherReconciliationDataDomain.getUpdatedUser());

	}

	@Test
	public void testBuilder() {
		voucherReconciliationDataDomain = new VoucherReconciliationDataDomain();
		assertNotNull(voucherReconciliationDataDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(voucherReconciliationDataDomain.toString());
	}

}
