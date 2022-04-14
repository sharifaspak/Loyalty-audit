package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.voucher.domain.VoucherDomain.VoucherBuilder;
import com.loyalty.marketplace.voucher.outbound.database.entity.OfferInfo;

@SpringBootTest(classes = VoucherDomain.class)
@ActiveProfiles("unittest")
public class VoucherDomainTest {

	private VoucherDomain voucherDomain;

	@Before
	public void setUp() throws Exception {
		String programCode = "";
		String voucherCode = "";
		OfferInfo offerInfo = new OfferInfo();
		String merchantCode = "";
		String merchantName = "";
		String membershipCode = "";
		String accountNumber = "";
		Barcode barcode = new Barcode();
		Double cost = 0.0;
		Date createdDate = new Date();
		String createdUser = "";
		Date endDate = new Date();
		Date expiryDate = new Date();
		String id = "";
		String partnerCode = "";
		String partnerReferNumber = "";
		long points = 0l;
		Date startDate = new Date();
		String status = "";
		String type = "";
		Date updatedDate = new Date();
		String updatedUser = "";
		Date uploadDate = new Date();
		String uuid = "";
		Double voucherAmount = 0.0;
		VoucherBuilder voucherBuilder = new VoucherDomain.VoucherBuilder(programCode, voucherCode, offerInfo,
				merchantCode, merchantName, membershipCode, accountNumber, barcode);
		voucherBuilder.accountNumber(accountNumber).cost(cost).createdDate(createdDate).createdUser(createdUser)
				.endDate(endDate).expiryDate(expiryDate).id(id).partnerCode(partnerCode)
				.partnerReferNumber(partnerReferNumber).pointsValue(points).startDate(startDate).status(status)
				.type(type).updatedDate(updatedDate).updatedUser(updatedUser).uploadDate(uploadDate).uuid(uuid)
				.voucherAmount(voucherAmount).build();
		voucherDomain = new VoucherDomain(voucherBuilder);
	}

	@Test
	public void testGetters() {
		assertNotNull(voucherDomain.getAccountNumber());
		assertNotNull(voucherDomain.getBarcodeType());
		assertNotNull(voucherDomain.getCost());
		assertNotNull(voucherDomain.getCreatedDate());
		assertNotNull(voucherDomain.getCreatedUser());
		assertNotNull(voucherDomain.getEndDate());
		assertNotNull(voucherDomain.getExpiryDate());
		assertNotNull(voucherDomain.getId());
		assertNotNull(voucherDomain.getMembershipCode());
		assertNotNull(voucherDomain.getMerchantCode());
		assertNotNull(voucherDomain.getMerchantName());
		assertNotNull(voucherDomain.getOfferInfo());
		assertNotNull(voucherDomain.getPartnerCode());
		assertNotNull(voucherDomain.getPartnerReferNumber());
		assertNotNull(voucherDomain.getPointsValue());
		assertNotNull(voucherDomain.getProgramCode());
		assertNotNull(voucherDomain.getStartDate());
		assertNotNull(voucherDomain.getStatus());
		assertNotNull(voucherDomain.getType());
		assertNotNull(voucherDomain.getUpdatedDate());
		assertNotNull(voucherDomain.getUpdatedUser());
		assertNotNull(voucherDomain.getUploadDate());
		assertNotNull(voucherDomain.getUuid());
		assertNotNull(voucherDomain.getVoucherAmount());
		assertNotNull(voucherDomain.getVoucherCode());
		assertNotNull(voucherDomain.getVoucherType());

	}

	@Test
	public void testBuilder() {
		voucherDomain = new VoucherDomain();
		assertNotNull(voucherDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(voucherDomain.toString());
	}

}
