package com.loyalty.marketplace.voucher.member.management.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.maf.inbound.dto.AdditionalInfo;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails.Data;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Filters;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders;

@SpringBootTest(classes = VoucherReconciliationResponse.class)
@ActiveProfiles("unittest")
public class VoucherReconciliationResponseTest {

	private VoucherReconciliationResponse voucherReconciliationResponse;
	private Orders orders;
	private Data data;
	private VoucherDetails voucherDetails;
	private ResponseData responseData;
	private Filters filters;

	@Before
	public void setUp() throws Exception {
		voucherReconciliationResponse = new VoucherReconciliationResponse();
		responseData = new VoucherReconciliationResponse.ResponseData();
		voucherDetails = new VoucherReconciliationResponse.ResponseData.VoucherDetails();
		data = new VoucherReconciliationResponse.ResponseData.VoucherDetails.Data();
		filters = new VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Filters();
		orders = new VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders();

		voucherReconciliationResponse.setAckMessage(new AckMessage());
		voucherReconciliationResponse.setResponseData(responseData);

		responseData.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		responseData.setTransactionID("");
		responseData.setVoucherDetails(voucherDetails);

		voucherDetails.setData(data);

		data.setOrders(new ArrayList<VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders>());
		data.setFilters(filters);

		filters.setLimit("");
		filters.setPage("");
		filters.setTotalOrders("");

		orders.setAdditionalInfo(new ArrayList<AdditionalInfo>());
		orders.setCountryID("");
		orders.setCreatedAt("");
		orders.setCustomerID("");
		orders.setExpiredAfter("");
		orders.setIPAddress("");
		orders.setNumberOfItems("");
		orders.setOrderID("");
		orders.setOrderReferenceNumber("");
		orders.setPartnerID("");
		orders.setPaymentMethodID("");
		orders.setRecipientID("");
		orders.setSource("");
		orders.setStatusID("");
		orders.setTotalActivationFee("");
		orders.setTotalAmount("");
		orders.setTotalDeliveryFee("");
		orders.setTotalFeeAmount("");
		orders.setTotalLoadAmount("");
		orders.setTotalPaid("");
		orders.setTotalVatableFee("");
		orders.setVAT("");
		orders.setVoucherID("");

	}

	@Test
	public void test() {
		assertNotNull(voucherReconciliationResponse.getAckMessage());
		assertNotNull(voucherReconciliationResponse.getResponseData());

		assertNotNull(responseData.getAdditionalInfo());
		assertNotNull(responseData.getTransactionID());
		assertNotNull(responseData.getVoucherDetails());

		assertNotNull(voucherDetails.getData());

		assertNotNull(data.getFilters());
		assertNotNull(data.getOrders());

		assertNotNull(filters.getLimit());
		assertNotNull(filters.getPage());
		assertNotNull(filters.getTotalOrders());

		assertNotNull(orders.getAdditionalInfo());
		assertNotNull(orders.getCountryID());
		assertNotNull(orders.getCreatedAt());
		assertNotNull(orders.getCustomerID());
		assertNotNull(orders.getExpiredAfter());
		assertNotNull(orders.getIPAddress());
		assertNotNull(orders.getNumberOfItems());
		assertNotNull(orders.getOrderID());
		assertNotNull(orders.getOrderReferenceNumber());
		assertNotNull(orders.getPartnerID());
		assertNotNull(orders.getPaymentMethodID());
		assertNotNull(orders.getRecipientID());
		assertNotNull(orders.getSource());
		assertNotNull(orders.getStatusID());
		assertNotNull(orders.getTotalActivationFee());
		assertNotNull(orders.getTotalAmount());
		assertNotNull(orders.getTotalDeliveryFee());
		assertNotNull(orders.getTotalFeeAmount());
		assertNotNull(orders.getTotalLoadAmount());
		assertNotNull(orders.getTotalPaid());
		assertNotNull(orders.getTotalVatableFee());
		assertNotNull(orders.getVAT());
		assertNotNull(orders.getVoucherID());

	}

	@Test
	public void testToString() {
		assertNotNull(voucherReconciliationResponse.toString());
		assertNotNull(responseData.toString());
		assertNotNull(voucherDetails.toString());
		assertNotNull(data.toString());
		assertNotNull(filters.toString());
		assertNotNull(orders.toString());

	}

}