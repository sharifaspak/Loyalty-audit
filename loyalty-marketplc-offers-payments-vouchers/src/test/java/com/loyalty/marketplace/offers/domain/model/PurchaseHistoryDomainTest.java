package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.dto.TransactionListDto;
import com.loyalty.marketplace.offers.outbound.dto.TransactionsResultResponse;
import com.loyalty.marketplace.offers.utils.MapValues;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherControllerHelper;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherListResult;

import lombok.AccessLevel;
import lombok.Getter;

@SpringBootTest(classes = PurchaseDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PurchaseHistoryDomainTest {

	@InjectMocks
	private PurchaseDomain offerPurchaseHistory = new PurchaseDomain();
	
	@Mock	
	ModelMapper modelMapper;
	
	@Mock
	AuditService auditService;
	
	@Mock
	Validator validator;
	
	@Mock
	RepositoryHelper repositoryHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;
	
	@Getter(AccessLevel.NONE)
	@Autowired
    VoucherControllerHelper voucherControllerHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	OffersHelper helper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	SubscriptionManagementController subscriptionManagementController;
	
	@Autowired
	FetchServiceValues fetchServiceValues;

	TransactionRequestDto transactionRequest;
	Headers headers;
	List<TransactionListDto> transactionList;
	List<PurchaseHistory> purchaseList;
	PurchaseHistory purchaseHistory;
	List<PaymentMethod> paymentMethods;
	PaymentMethod paymentMethod;
	List<VoucherListResult> voucherDetails;
	VoucherListResult voucherDetail;
	TransactionsResultResponse  transactionsResultResponse;
	
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		headers = new Headers("", "", "", "", "", "", "", "", "", "", "");
		transactionRequest = new TransactionRequestDto();
		transactionRequest.setAccountNumber("accountNumber");
		transactionRequest.setAccountNumberList(new ArrayList<>());
		transactionRequest.setMembershipCode("membershipCode");
		transactionRequest.setTransactionType("transactionType");	
		transactionRequest.setFromDate("fromDate");
		transactionRequest.setToDate("toDate");
		
		purchaseList = new ArrayList<>(1);
		purchaseHistory = new PurchaseHistory();
		purchaseList.add(purchaseHistory);
		
		paymentMethods = new ArrayList<>(1);
		paymentMethod = new PaymentMethod();
		paymentMethods.add(paymentMethod);
		
		voucherDetails = new ArrayList<>(1);
		voucherDetail = new VoucherListResult();
		voucherDetails.add(voucherDetail);
		
		transactionsResultResponse = new TransactionsResultResponse("");
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNull(offerPurchaseHistory.getProgramCode());
		assertNull(offerPurchaseHistory.getPartnerCode());
		assertNull(offerPurchaseHistory.getMerchantCode());
		assertNull(offerPurchaseHistory.getMerchantName());
		assertNull(offerPurchaseHistory.getMembershipCode());
		assertNull(offerPurchaseHistory.getAccountNumber());
		assertNull(offerPurchaseHistory.getOfferId());
		assertNull(offerPurchaseHistory.getSubOfferId());
		assertNull(offerPurchaseHistory.getPromoCode());
		assertNull(offerPurchaseHistory.getExtRefNo());
		assertNull(offerPurchaseHistory.getEpgTransactionId());
		assertNull(offerPurchaseHistory.getCouponQuantity());
		assertNull(offerPurchaseHistory.getVoucherCode());
		assertNull(offerPurchaseHistory.getSubscriptionId());
		assertNull(offerPurchaseHistory.getPaymentMethod());
		assertNull(offerPurchaseHistory.getSpentPoints());
		assertNull(offerPurchaseHistory.getPartnerActivity());
		assertNull(offerPurchaseHistory.getPurchaseAmount());
		assertNull(offerPurchaseHistory.getStatus());
		assertNull(offerPurchaseHistory.getStatusReason());
		assertNull(offerPurchaseHistory.getCreatedDate());
		assertNull(offerPurchaseHistory.getCreatedUser());
		assertNull(offerPurchaseHistory.getUpdatedDate());
		assertNull(offerPurchaseHistory.getUpdatedUser());
		assertNull(offerPurchaseHistory.getChannelId());
		assertNull(offerPurchaseHistory.getLanguage());
		assertNull(offerPurchaseHistory.getOfferType());
		assertNull(offerPurchaseHistory.getPurchaseAmount());
		assertNull(offerPurchaseHistory.getSpentAmount());
		assertNull(offerPurchaseHistory.getTransactionNo());
		assertNull(offerPurchaseHistory.getTransactionType());
		assertNull(offerPurchaseHistory.getPurchaseItem());
		assertNull(offerPurchaseHistory.getAdditionalDetails());
		assertNull(offerPurchaseHistory.getCost());
		assertNull(offerPurchaseHistory.getPointsTransactionId());
		assertNull(offerPurchaseHistory.getReferralAccountNumber());
		assertNull(offerPurchaseHistory.getReferralBonusCode());
		assertNull(offerPurchaseHistory.getReferralBonus());
		assertNull(offerPurchaseHistory.getVatAmount());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(offerPurchaseHistory.toString());

	}
		
	@Test
	public void testGetAllPurchaseTransactionsWithVoucherDetailsSuccess() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest)).thenReturn(purchaseList);
		when(helper.getTransactionListPaymentMethods(purchaseList)).thenReturn(paymentMethods);
		when(voucherControllerHelper.getVoucherListByBusinessId(MapValues.mapPurchaseIdFromTransactionList(purchaseList),
     						transactionsResultResponse)).thenReturn(voucherDetails);
		transactionsResultResponse = offerPurchaseHistory.getAllPurchaseTransactionsWithVoucherDetails(transactionRequest, headers);
		assertEquals(OfferSuccessCodes.TRANSACTIONS_FETCHED_SUCCESSFULLY.getId(), transactionsResultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetAllPurchaseTransactionsWithVoucherDetailsException() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest)).thenThrow(NullPointerException.class);
		transactionsResultResponse = offerPurchaseHistory.getAllPurchaseTransactionsWithVoucherDetails(transactionRequest, headers);
		assertEquals(OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED.getId(), transactionsResultResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testGetAllPurchaseTransactionsWithVoucherDetailsMarketplaceException() throws MarketplaceException, ParseException {
		
		when(repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest)).thenReturn(purchaseList);
		when(helper.getTransactionListPaymentMethods(purchaseList)).thenReturn(paymentMethods);
		when(voucherControllerHelper.getVoucherListByBusinessId(MapValues.mapPurchaseIdFromTransactionList(purchaseList),
     						transactionsResultResponse)).thenThrow(NullPointerException.class);
		transactionsResultResponse = offerPurchaseHistory.getAllPurchaseTransactionsWithVoucherDetails(transactionRequest, headers);
		assertEquals(OfferErrorCodes.FETCHING_PURCHASE_TRANSACTONS_FAILED.getId(), transactionsResultResponse.getResult().getResponse());
	
	}

}
