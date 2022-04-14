package com.loyalty.marketplace.voucher.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.maf.inbound.dto.AdditionalInfo;
import com.loyalty.marketplace.voucher.maf.outbound.dto.PlaceVoucherOrderResponse;
import com.loyalty.marketplace.voucher.maf.outbound.dto.PlaceVoucherOrderResponse.ResponseData;
import com.loyalty.marketplace.voucher.maf.outbound.dto.PlaceVoucherOrderResponse.ResponseData.VoucherDetail;

@SpringBootTest(classes = PlaceVoucherOrderResponse.class)
@ActiveProfiles("unittest")
public class PlaceVoucherOrderResponseTest {

	private PlaceVoucherOrderResponse placeVoucherOrderResponse;
	private ResponseData responseData;
	private VoucherDetail voucherDetail;

	@Before
	public void setUp() throws Exception {
		placeVoucherOrderResponse = new PlaceVoucherOrderResponse();
		responseData = new PlaceVoucherOrderResponse.ResponseData();
		voucherDetail = new PlaceVoucherOrderResponse.ResponseData.VoucherDetail();

		placeVoucherOrderResponse.setAckMessage(new AckMessage());
		placeVoucherOrderResponse.setResponseData(responseData);

		responseData.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		responseData.setTransactionID("");
		responseData.setVoucherDetail(voucherDetail);

		voucherDetail.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		voucherDetail.setCountryID("");
		voucherDetail.setCreatedAt("");
		voucherDetail.setCustomerID("");
		voucherDetail.setExpiredAfter("");
		voucherDetail.setIPAddress("");
		voucherDetail.setNumberOfItems("");
		voucherDetail.setOrderID("");
		voucherDetail.setOrderReferenceNumber("");
		voucherDetail.setPartnerID("");
		voucherDetail.setPaymentMethodID("");
		voucherDetail.setRecipientID("");
		voucherDetail.setSource("");
		voucherDetail.setStatusID("");
		voucherDetail.setTotalActivationFee("");
		voucherDetail.setTotalAmount("");
		voucherDetail.setTotalDeliveryFee("");
		voucherDetail.setTotalFeeAmount("");
		voucherDetail.setTotalPaid("");
		voucherDetail.setTotalLoadAmount("");
		voucherDetail.setTotalVatableFee("");
		voucherDetail.setVAT("");
		voucherDetail.setVoucherID("");

	}

	@Test
	public void test() {
		assertNotNull(placeVoucherOrderResponse.getAckMessage());
		assertNotNull(placeVoucherOrderResponse.getResponseData());

		assertNotNull(responseData.getAdditionalInfo());
		assertNotNull(responseData.getTransactionID());
		assertNotNull(responseData.getVoucherDetail());

		assertNotNull(voucherDetail.getAdditionalInfo());
		assertNotNull(voucherDetail.getCountryID());
		assertNotNull(voucherDetail.getCreatedAt());
		assertNotNull(voucherDetail.getCustomerID());
		assertNotNull(voucherDetail.getExpiredAfter());
		assertNotNull(voucherDetail.getIPAddress());
		assertNotNull(voucherDetail.getNumberOfItems());
		assertNotNull(voucherDetail.getOrderID());
		assertNotNull(voucherDetail.getOrderReferenceNumber());
		assertNotNull(voucherDetail.getPartnerID());
		assertNotNull(voucherDetail.getPaymentMethodID());
		assertNotNull(voucherDetail.getRecipientID());
		assertNotNull(voucherDetail.getSource());
		assertNotNull(voucherDetail.getStatusID());
		assertNotNull(voucherDetail.getTotalActivationFee());
		assertNotNull(voucherDetail.getTotalAmount());
		assertNotNull(voucherDetail.getTotalDeliveryFee());
		assertNotNull(voucherDetail.getTotalFeeAmount());
		assertNotNull(voucherDetail.getTotalLoadAmount());
		assertNotNull(voucherDetail.getTotalPaid());
		assertNotNull(voucherDetail.getTotalVatableFee());
		assertNotNull(voucherDetail.getVAT());
		assertNotNull(voucherDetail.getVoucherID());

	}

	@Test
	public void testToString() {
		assertNotNull(placeVoucherOrderResponse.toString());
		assertNotNull(voucherDetail.toString());
		assertNotNull(responseData.toString());
	}

}