package com.loyalty.marketplace.voucher.member.management.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.maf.inbound.dto.VoucherReconciliationRequest;
import com.loyalty.marketplace.voucher.maf.inbound.dto.VoucherReconciliationRequest.DataHeader;
import com.loyalty.marketplace.voucher.maf.inbound.dto.VoucherReconciliationRequest.DataHeader.AdditionalInfo;

@SpringBootTest(classes = VoucherReconciliationRequest.class)
@ActiveProfiles("unittest")
public class VoucherReconciliationRequestTest {

	private VoucherReconciliationRequest voucherReconciliationRequest;
	private DataHeader dataHeader;
	private AdditionalInfo additionalInfo;

	@Before
	public void setUp() throws Exception {
		voucherReconciliationRequest = new VoucherReconciliationRequest();

		dataHeader = new VoucherReconciliationRequest.DataHeader();
		voucherReconciliationRequest.setDataHeader(dataHeader);

		dataHeader.setAdditionalInfo(new ArrayList<VoucherReconciliationRequest.DataHeader.AdditionalInfo>());
		dataHeader.setFromDate("");
		dataHeader.setLimit(0);
		dataHeader.setPage(0);
		dataHeader.setTargetSystem("");
		dataHeader.setToDate("");

		additionalInfo = new VoucherReconciliationRequest.DataHeader.AdditionalInfo();
		additionalInfo.setName("");
		additionalInfo.setValue("");
	}

	@Test
	public void test() {
		assertNotNull(voucherReconciliationRequest.getDataHeader());

		assertNotNull(dataHeader.getAdditionalInfo());
		assertNotNull(dataHeader.getFromDate());
		assertNotNull(dataHeader.getLimit());
		assertNotNull(dataHeader.getPage());
		assertNotNull(dataHeader.getTargetSystem());
		assertNotNull(dataHeader.getToDate());

		assertNotNull(additionalInfo.getName());
		assertNotNull(additionalInfo.getValue());
	}

	@Test
	public void testToString() {
		assertNotNull(voucherReconciliationRequest.toString());
		assertNotNull(dataHeader.toString());
		assertNotNull(additionalInfo.toString());
	}

}