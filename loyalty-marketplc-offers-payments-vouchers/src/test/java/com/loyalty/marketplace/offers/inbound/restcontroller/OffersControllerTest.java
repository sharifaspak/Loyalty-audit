package com.loyalty.marketplace.offers.inbound.restcontroller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.gifting.helper.GiftingControllerHelper;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.domain.model.BirthdayInfoDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCatalogDomain;
import com.loyalty.marketplace.offers.domain.model.OfferRatingDomain;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.domain.model.WishlistDomain;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.BirthdayInfoRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersRequest;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferRatingDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.WishlistRequestDto;
import com.loyalty.marketplace.offers.outbound.dto.BirthdayInfoResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.PaymentMethodResponse;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.TransactionsResultResponse;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes=OffersController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OffersControllerTest {
	
	@Mock
	OfferCatalogDomain offerCatalogDomain;
	
	@Mock
	PurchaseDomain purchaseDomain;
	
	@Mock
	WishlistDomain wishlistDomain;
	
	@Mock
	BirthdayInfoDomain birthdayInfoDomain;
	
	@Mock
	OfferRatingDomain offerRatingDomain;
	
	@Mock
	SubscriptionManagementController subscriptionManagementController;
	
	@Mock
	Validator validator;
	
	@Mock
	RepositoryHelper repositoryHelper;
	
	@Mock
	GiftingControllerHelper giftingControllerHelper;
	
	@InjectMocks
	OffersController offersController;
	
	private String program;
	private String authorization;
	private String externalTransactionId;
	private String userName;
	private String sessionId;
	private String userPrev;
	private String channelId;
	private String systemId;
	private String systemPassword;
	private String token;
	private String partnerCode;
	private String merchantCode;
	private String role;
	private String offerId;
	private String transactionId;
	private EligibleOffersRequest offerRequest;
	private String accountNumber;
	private Headers headers;
	private Integer page;
	private Integer pageLimit;
	private String offerTypeId;
	private String status;
	private EligibleOffersFiltersRequest eligibleOffersParameters;
	
	@Before
	public void setUp() throws ParseException {
		MockitoAnnotations.initMocks(this);

		program = "Smiles";
		authorization = "authorization";
		externalTransactionId = "externalTransactionId";
		userName = "userName";
		sessionId = "sessionId";
		userPrev = "userPrev";
		channelId = "channelId";
		systemId = "systemId";
		systemPassword = "systemPassword";
		token = "token";
		transactionId = "transactionId";
		partnerCode = "partnerCode";
		merchantCode = "merchantCode";
		role = "role";
		offerId = "offerId";
		accountNumber = "accountNumber";
		offerTypeId = "offerTypeId";
		status = "status";
		
		new Headers(program, authorization, externalTransactionId, userName, 
				sessionId, userPrev, channelId, systemId, systemPassword, token, 
				externalTransactionId);
		
		page = 1;
		pageLimit = 20;
		offerRequest = new EligibleOffersRequest();
		offerRequest.setFilterFlag("filterFlag");
		
		eligibleOffersParameters = new EligibleOffersFiltersRequest();
	
	}
	
	/**
	 * Testing offer creation
	 * @throws ParseException 
	 * @throws MarketplaceException
	 */
	
	@Test
	public void testConfigureOffers() throws MarketplaceException, ParseException {
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		OfferCatalogDto offerCatalogRequest = new OfferCatalogDto();
		when(offerCatalogDomain.validateAndSaveOffer(offerCatalogRequest, 
				new Headers(program, authorization, externalTransactionId, userName, 
				sessionId, userPrev, channelId, systemId, systemPassword, token, 
				externalTransactionId))).thenReturn(resultResponse);
		
		resultResponse = offersController.createOffer(offerCatalogRequest, 
				program, authorization, externalTransactionId, userName, sessionId, 
				userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testUpdateOffers() throws MarketplaceException, ParseException {
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		OfferCatalogDto offerCatalogRequest = new OfferCatalogDto();
		when(offerCatalogDomain.validateAndUpdateOffer(offerCatalogRequest, offerId, 
				headers, role)).thenReturn(resultResponse);
		
		resultResponse = offersController.updateOffer(offerCatalogRequest, 
				program, authorization, externalTransactionId, userName, sessionId, 
				userPrev, channelId, systemId, systemPassword, token, externalTransactionId,
				offerId, role);
		
		assertNull(resultResponse);
		
	}
	
//	@Test
//    public void testGetAllOffers() throws MarketplaceException, ParseException {
//		
//		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
//		when(offerCatalogDomain.getAllOffersForAdministrator(headers, page, pageLimit, offerTypeId, status)).thenReturn(resultResponse);
//		
//		resultResponse = offersController.listAdminOffers(page, pageLimit, offerTypeId, status, program, authorization, externalTransactionId,
//				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, 
//				externalTransactionId);
//		
//		assertNull(resultResponse);
//		
//	}
//	
//	@Test
//    public void testGetOffersForPartnerCode() throws MarketplaceException, ParseException {
//		
//		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
//		when(offerCatalogDomain.getAllOffersForPartner(headers, partnerCode, page, pageLimit, offerTypeId, status)).thenReturn(resultResponse);
//		
//		resultResponse = offersController.listAllOffersForPartner(page, pageLimit, offerTypeId, status, program, authorization, externalTransactionId, 
//				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, 
//				externalTransactionId, partnerCode);
//		
//		assertNull(resultResponse);
//		
//	}
//	
//	@Test
//    public void testGetOffersForMerchantCode() throws MarketplaceException, ParseException {
//		
//		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
//		when(offerCatalogDomain.getAllOffersForMerchant(headers, merchantCode, page, pageLimit, offerTypeId, status)).thenReturn(resultResponse);
//		
//		resultResponse = offersController.listAllOffersForMerchant(page, pageLimit, offerTypeId, status, program, authorization, externalTransactionId, 
//				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, 
//				externalTransactionId, merchantCode);
//		
//		assertNull(resultResponse);
//		
//	}
	
	@Test
    public void testGetSpecificOffersDetail() throws MarketplaceException, ParseException {
		
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
		when(offerCatalogDomain.getDetailedOfferPortal(headers, offerId)).thenReturn(resultResponse);
		
		resultResponse = offersController.getSpecificOfferDetails(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, 
				systemPassword, token, externalTransactionId, offerId);
		
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testGetEligiblePaymentMethods() throws MarketplaceException, ParseException {
		
		PaymentMethodResponse resultResponse = new PaymentMethodResponse(externalTransactionId);
		when(offerCatalogDomain.getEligiblePaymentMethods(offerId, accountNumber, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.listEligiblePaymentMethods(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId, offerId, accountNumber);
		
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testGetEligibleOfferList() throws MarketplaceException, ParseException {
		
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
		when(offerCatalogDomain.getEligibleOfferList(eligibleOffersParameters, headers)).thenReturn(resultResponse);
		resultResponse = offersController.listEligibleOffers(eligibleOffersParameters, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testGetEligibleOfferListEmptyRequest() throws MarketplaceException, ParseException {
		
		EligibleOffersFiltersRequest offerRequest = null;
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
		when(offerCatalogDomain.getEligibleOfferList(offerRequest, headers)).thenReturn(resultResponse);
		resultResponse = offersController.listEligibleOffers(eligibleOffersParameters, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testGetEligibleOfferDetail() throws MarketplaceException, ParseException {
		
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
		when(offerCatalogDomain.getDetailedEligibleOffer(accountNumber, headers, offerId)).thenReturn(resultResponse);
		
		resultResponse = offersController.listAppOfferDetail(offerRequest, program, 
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, 
				systemPassword, token, externalTransactionId, offerId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testGetEligibleOfferDetailEmptyRequest() throws MarketplaceException, ParseException {
		
		EligibleOffersRequest offerRequest = null;
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
		when(offerCatalogDomain.getDetailedEligibleOffer(accountNumber, headers, offerId)).thenReturn(resultResponse);
		
		resultResponse = offersController.listAppOfferDetail(offerRequest, program, 
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, 
				systemPassword, token, externalTransactionId, offerId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testPurchase() throws MarketplaceException, ParseException {
		
		PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setSelectedPaymentItem(program);
		PurchaseResultResponse resultResponse = new PurchaseResultResponse(externalTransactionId);
		when(purchaseDomain.validateAndSavePurchaseHistory(purchaseRequestDto, resultResponse, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.purchaseItems(purchaseRequestDto, 
				program, authorization, externalTransactionId, userName, sessionId, userPrev, 
				channelId, systemId, systemPassword, token, externalTransactionId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testPurchaseSubscription() throws MarketplaceException, ParseException {
		
		PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setSelectedPaymentItem(OffersConfigurationConstants.subscriptionItem);
		PurchaseResultResponse resultResponse = new PurchaseResultResponse(externalTransactionId);
		when(subscriptionManagementController.createSubscription(purchaseRequestDto, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.purchaseItems(purchaseRequestDto, 
				program, authorization, externalTransactionId, userName, sessionId, userPrev, 
				channelId, systemId, systemPassword, token, externalTransactionId);
		assertNull(resultResponse);
		
	}
	
//	@Test
//    public void testResetCounters() throws MarketplaceException, ParseException {
//		
//		List<OfferCounter> offerCounterList = new ArrayList<OfferCounter>();
//		offerCounterList.add(new OfferCounter());
//		offerCounterList.get(0).setDailyCount(1);
//		when(repositoryHelper.getAllCounters()).thenReturn(offerCounterList);
//		Mockito.doNothing().when(giftingControllerHelper).resetGiftingCounters(headers);
//		offersController.resetAllOfferCounters(program, authorization, externalTransactionId, 
//				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, 
//				externalTransactionId);
//		assertNotNull(offerCounterList);
//	}
	
	@Test
    public void testListAllTransactions() throws MarketplaceException, ParseException {
		
		TransactionRequestDto transactionRequest = new TransactionRequestDto();
		TransactionsResultResponse resultResponse = new TransactionsResultResponse(externalTransactionId);
		when(purchaseDomain.getAllPurchaseTransactionsWithVoucherDetails(transactionRequest, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.listAllTransactions(transactionRequest, 
				program, authorization, externalTransactionId, userName, sessionId, userPrev, 
				channelId, systemId, systemPassword, token, externalTransactionId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testListMerchantsWithOfferType() throws MarketplaceException, ParseException {
		
		when(offerCatalogDomain.getAllMerchantCodesForOffer(offerId)).thenReturn(new ArrayList<String>());
		List<String> values = offersController.listMerchantsWithOfferType(offerId);
		assertNotNull(values);
		
	}
	
	
	@Test
    public void testRateOffer() throws MarketplaceException, ParseException {
		
		OfferRatingDto offerRatingRequest = new OfferRatingDto();
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		when(offerRatingDomain.validateAndSaveOfferRating(offerRatingRequest, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.rateOffer(offerRatingRequest, 
				program, authorization, externalTransactionId, userName, sessionId, userPrev, 
				channelId, systemId, systemPassword, token, externalTransactionId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testBatchProcessBirthDayOffers() throws MarketplaceException, ParseException {
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		when(offerCatalogDomain.sendBirthdayGiftAlerts(headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.batchProcessBirthDayOffers(program, authorization, externalTransactionId, userName, sessionId, userPrev, 
				channelId, systemId, systemPassword, token, externalTransactionId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testGetMemberBirthDayGiftOffers() throws MarketplaceException, ParseException {
		
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
		when(offerCatalogDomain.getAllEligibleBirthdayOffers(accountNumber, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.getMemberBirthDayGiftOffers(program, authorization, externalTransactionId, userName,
				sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, accountNumber);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testConfigureWishlistAdd() throws MarketplaceException, ParseException {
		
		WishlistRequestDto wishlistRequest = new WishlistRequestDto();
		wishlistRequest.setAction(OfferConstants.ADD_ACTION.get());
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		when(wishlistDomain.validateAndSaveOfferToWishlist(wishlistRequest ,accountNumber, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.configureWishlist(wishlistRequest, program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, accountNumber);
		assertNull(resultResponse);
	}
	
	@Test
    public void testConfigureWishlistRemove() throws MarketplaceException, ParseException {
		
		WishlistRequestDto wishlistRequest = new WishlistRequestDto();
		wishlistRequest.setAction(OfferConstants.REMOVE_ACTION.get());
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		when(wishlistDomain.validateAndRemoveOfferFromWishlist(wishlistRequest ,accountNumber, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.configureWishlist(wishlistRequest, program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, accountNumber);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testGetFromWishlist() throws MarketplaceException, ParseException {
		
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(externalTransactionId);
		when(wishlistDomain.getActiveOffersFromWishlist(accountNumber, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.getFromWishlist(program, authorization, 
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, 
				systemPassword, token, externalTransactionId, accountNumber);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testConfigureBirthdayInfo() throws MarketplaceException, ParseException {
		
		BirthdayInfoRequestDto birthayInfoRequest = new BirthdayInfoRequestDto();
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		when(birthdayInfoDomain.configureBirthdayInfo(birthayInfoRequest, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.configureBirthdayInfo(birthayInfoRequest, 
				program, authorization, externalTransactionId, userName, sessionId, userPrev, 
				channelId, systemId, systemPassword, token, externalTransactionId);
		assertNull(resultResponse);
		
	}
	
	@Test
    public void testRetrieveBirthdayInfo() throws MarketplaceException, ParseException {
		
		BirthdayInfoResultResponse resultResponse = new BirthdayInfoResultResponse(externalTransactionId);
		when(birthdayInfoDomain.getBirthdayInfoForAccount(accountNumber, headers)).thenReturn(resultResponse);
		
		resultResponse = offersController.retrieveBirthdayInfoAccount(program, authorization, externalTransactionId, userName, 
				sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, accountNumber);
		assertNull(resultResponse);
		
	}
	
	
	
	
	
	
}
