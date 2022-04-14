package com.loyalty.marketplace.voucher.member.management.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.maf.inbound.dto.PlaceVoucherOrderRequest;
import com.loyalty.marketplace.voucher.maf.inbound.dto.PlaceVoucherOrderRequest.DataHeader;
import com.loyalty.marketplace.voucher.maf.inbound.dto.PlaceVoucherOrderRequest.DataHeader.AdditionalInfo;

@SpringBootTest(classes = PlaceVoucherOrderRequest.class)
@ActiveProfiles("unittest")
public class PlaceVoucherOrderRequestTest {
	private PlaceVoucherOrderRequest placeVoucherOrderRequest;
	private DataHeader dataHeader;
	private AdditionalInfo additionalInfo;

	@Before
	public void setUp() throws Exception {
		placeVoucherOrderRequest = new PlaceVoucherOrderRequest();

		dataHeader = new PlaceVoucherOrderRequest.DataHeader();
		placeVoucherOrderRequest.setDataHeader(dataHeader);

		dataHeader.setAdditionalInfo(new ArrayList<PlaceVoucherOrderRequest.DataHeader.AdditionalInfo>());
		dataHeader.setAddress1("");
		dataHeader.setAddress2("");
		dataHeader.setCity("");
		dataHeader.setDeliveryMethodID("");
		dataHeader.setEmail("");
		dataHeader.setFirstName("");
		dataHeader.setGender("");
		dataHeader.setLastName("");
		dataHeader.setLoadAmount("");
		dataHeader.setMobileCode("");
		dataHeader.setMobileNumber("");
		dataHeader.setPaymentMethodID("");
		dataHeader.setProductID("");
		dataHeader.setTargetSystem("");

		additionalInfo = new PlaceVoucherOrderRequest.DataHeader.AdditionalInfo();
		additionalInfo.setName("");
		additionalInfo.setValue("");
	}

	@Test
	public void test() {
		assertNotNull(placeVoucherOrderRequest.getDataHeader());

		assertNotNull(dataHeader.getAdditionalInfo());
		assertNotNull(dataHeader.getAddress1());
		assertNotNull(dataHeader.getAddress2());
		assertNotNull(dataHeader.getCity());
		assertNotNull(dataHeader.getDeliveryMethodID());
		assertNotNull(dataHeader.getEmail());
		assertNotNull(dataHeader.getFirstName());
		assertNotNull(dataHeader.getGender());
		assertNotNull(dataHeader.getLastName());
		assertNotNull(dataHeader.getLoadAmount());
		assertNotNull(dataHeader.getMobileCode());
		assertNotNull(dataHeader.getMobileNumber());
		assertNotNull(dataHeader.getPaymentMethodID());
		assertNotNull(dataHeader.getProductID());
		assertNotNull(dataHeader.getTargetSystem());

		assertNotNull(additionalInfo.getName());
		assertNotNull(additionalInfo.getValue());
	}

	@Test
	public void testToString() {
		assertNotNull(placeVoucherOrderRequest.toString());
		assertNotNull(dataHeader.toString());
		assertNotNull(additionalInfo.toString());
	}

}