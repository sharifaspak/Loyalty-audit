package com.loyalty.marketplace.offers.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.equivalentpoints.outbound.database.repository.ConversionRateRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GoldCertificateRepository;
import com.loyalty.marketplace.image.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.repository.BirthdayGiftTrackerRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.BirthdayInfoRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferCountersRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRatingRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferTypeRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchasePaymentMethodRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.WishlistRepository;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.database.repository.DenominationRepository;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;

@SpringBootTest(classes=RepositoryHelper.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class RepositoryHelperTest {
	
	@Mock
	OfferTypeRepository offerTypeRepository;
	
	@Mock
	CategoryRepository categoryRepository;

	@Mock
	DenominationRepository denominationRepository;

	@Mock
	PaymentMethodRepository paymentMethodRepository;
	
	@Mock
	MerchantRepository merchantRepository;

	@Mock
	StoreRepository storeRepository;
		
	@Mock
	PurchaseRepository purchaseRepository;
	
	@Mock
	OfferRatingRepository offerRatingRepository;
	
	@Mock
	OfferRepository offerRepository;
	
	@Mock
	PurchasePaymentMethodRepository purchasePaymentMethodRepository;
		
	@Mock
	ConversionRateRepository conversionRateRepository;
	
	@Mock
	GoldCertificateRepository goldCertificateRepository;
	
	@Mock
	OfferCountersRepository offerCountersRepository;
	
	@Mock
	SubscriptionRepository subscriptionRepository;
	
	@Mock
	BirthdayInfoRepository birthdayInfoRepository;
	
	@Mock
	BirthdayGiftTrackerRepository birthdayGiftTrackerRepository;
	
	@Mock
	ImageRepository imageRepository;
	
	@Mock
	WishlistRepository wishlistRepository;
		
	@InjectMocks
	RepositoryHelper repositoryHelper;
	
	private String status;
	private String offerTypeId;
	private ResultResponse resultResponse;
	
	@Before
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
		status = "Active";
		offerTypeId = "1";
		resultResponse = new ResultResponse("");
   }
	
	@Test
	public void testGetOfferType(){
		
		String offerTypeId = "offerTypeId";
		OfferType offerType = new OfferType();
		when(offerTypeRepository.findByOfferTypeId(offerTypeId)).thenReturn(offerType);
		offerType = repositoryHelper.getOfferType(offerTypeId);
		assertNotNull(offerType);		
	}
	
	@Test
	public void testGetOfferTypeNull(){
		
		String offerTypeId = null;
		OfferType offerType = new OfferType();
		when(offerTypeRepository.findByOfferTypeId(offerTypeId)).thenReturn(offerType);
		offerType = repositoryHelper.getOfferType(offerTypeId);
		assertNull(offerType);		
	}
	
	@Test
	public void testGetMerchant(){
		
		String merchantCode = "merchantCode";
		Merchant merchant = new Merchant();
		when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);
		merchant = repositoryHelper.getMerchant(merchantCode);
		assertNotNull(merchant);		
	}
	
	@Test
	public void testGetMerchantNull(){
		
		String merchantCode = null;
		Merchant merchant = new Merchant();
		when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);
		merchant = repositoryHelper.getMerchant(merchantCode);
		assertNull(merchant);		
	}
	
	@Test
	public void testGetStoreList(){
		
		String merchantCode = "merchantCode";
		List<String> storeCodes = new ArrayList<>(1);
		storeCodes.add("storeCode");
		List<Store> stores = new ArrayList<>(1);
		when(storeRepository.findAllByStoreCodeAndMerchantCode(storeCodes, merchantCode)).thenReturn(stores);
		stores = repositoryHelper.getStoreList(merchantCode, storeCodes);
		assertNotNull(stores);		
	}
	
	@Test
	public void testGetStoreListNull(){
		
		String merchantCode = null;
		List<String> storeCodes = null;
		List<Store> stores = new ArrayList<>(1);
		when(storeRepository.findAllByStoreCodeAndMerchantCode(storeCodes, merchantCode)).thenReturn(stores);
		stores = repositoryHelper.getStoreList(merchantCode, storeCodes);
		assertNull(stores);		
	}
	
	@Test
	public void testGetCategory(){
		
		String categoryId = "categoryId";
		Category category = new Category();
		when(categoryRepository.findByCategoryId(categoryId)).thenReturn(category);
		category = repositoryHelper.getCategory(categoryId);
		assertNotNull(category);		
	}
	
	@Test
	public void testGetCategoryNull(){
		
		String categoryId = null;
		Category category = new Category();
		when(categoryRepository.findByCategoryId(categoryId)).thenReturn(category);
		category = repositoryHelper.getCategory(categoryId);
		assertNull(category);		
	}
	
	@Test
	public void testGetDenominationList(){
		
		List<Integer> denominationValues = new ArrayList<>();
		denominationValues.add(1);
		List<Denomination> denominationList = new ArrayList<>(1);
		when(denominationRepository.findAll()).thenReturn(denominationList);
		denominationList = repositoryHelper.getDenominationList(denominationValues);
		assertNotNull(denominationList);		
	}
	
	@Test
	public void testGetDenominationListNull(){
		
		List<Integer> denominationValues = new ArrayList<>();
		List<Denomination> denominationList = new ArrayList<>(1);
		when(denominationRepository.findAll()).thenReturn(denominationList);
		denominationList = repositoryHelper.getDenominationList(denominationValues);
		assertNull(denominationList);		
	}
	
	@Test
	public void testGetAllOfferCatalog(){
		
		List<OfferCatalog> offerCatalogList = new ArrayList<>(1);
		when(offerRepository.findAll()).thenReturn(offerCatalogList);
		offerCatalogList = repositoryHelper.getAllOfferCatalog();
		assertNotNull(offerCatalogList);		
	}
	
	@Test
	public void testFindOfferById(){
		
		String offerId = "offerId";
		OfferCatalog offerCatalog = new OfferCatalog();
		when(offerRepository.findByOfferId(offerId)).thenReturn(offerCatalog);
		offerCatalog = repositoryHelper.findByOfferId(offerId);
		assertNotNull(offerCatalog);		
	}
	
	@Test
	public void testFindOfferByIdNull(){
		
		String offerId = null;
		OfferCatalog offerCatalog = new OfferCatalog();
		when(offerRepository.findByOfferId(offerId)).thenReturn(offerCatalog);
		offerCatalog = repositoryHelper.findByOfferId(offerId);
		assertNull(offerCatalog);		
	}
	
	@Test
	public void testGetPaymentMethodsForPurchaseItem(){
		
		String item = "item";
		PurchasePaymentMethod purchasePaymentMethod = new PurchasePaymentMethod();
		when(purchasePaymentMethodRepository.findByPurchaseItem(item)).thenReturn(purchasePaymentMethod);
		purchasePaymentMethod = repositoryHelper.getPaymentMethodsForPurchaseItem(item);
		assertNotNull(purchasePaymentMethod);		
	}
	
	@Test
	public void testGetPaymentMethodsForPurchaseItemNull(){
		
		String item = null;
		PurchasePaymentMethod purchasePaymentMethod = new PurchasePaymentMethod();
		when(purchasePaymentMethodRepository.findByPurchaseItem(item)).thenReturn(purchasePaymentMethod);
		purchasePaymentMethod = repositoryHelper.getPaymentMethodsForPurchaseItem(item);
		assertNull(purchasePaymentMethod);		
	}
	
	@Test
	public void testGetActivePurchaseTransactionsNoAccountNumberOrTransactionType() throws ParseException{
		
		TransactionRequestDto transactionRequest = new TransactionRequestDto();
		transactionRequest.setToDate("12-03-1992");
		transactionRequest.setFromDate("12-03-1992");
		List<PurchaseHistory> purchaseRecords = repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest);
		assertNotNull(purchaseRecords);		
	}
	
	@Test
	public void testGetActivePurchaseTransactionsNoTransactionType() throws ParseException{
		
		TransactionRequestDto transactionRequest = new TransactionRequestDto();
		transactionRequest.setToDate("12-03-1992");
		transactionRequest.setFromDate("12-03-1992");
		transactionRequest.setAccountNumber("accountNumber");
		transactionRequest.setMembershipCode("membershipcode");
		List<PurchaseHistory> purchaseRecords = repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest);
		assertNotNull(purchaseRecords);		
	}
	
	@Test
	public void testGetActivePurchaseTransactionsLinkedAccountsNoTransactionType() throws ParseException{
		
		TransactionRequestDto transactionRequest = new TransactionRequestDto();
		transactionRequest.setToDate("12-03-1992");
		transactionRequest.setFromDate("12-03-1992");
		transactionRequest.setIncludeLinkedAccounts(true);
		transactionRequest.setAccountNumber("accountNumber");
		 List<PurchaseHistory> purchaseRecords = repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest);
		assertNotNull(purchaseRecords);		
	}
	
	@Test
	public void testGetActivePurchaseTransactionsNoAccountNumber() throws ParseException{
		
		TransactionRequestDto transactionRequest = new TransactionRequestDto();
		transactionRequest.setToDate("12-03-1992");
		transactionRequest.setFromDate("12-03-1992");
		transactionRequest.setTransactionType("transactionType");
		List<PurchaseHistory> purchaseRecords = repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest);
		assertNotNull(purchaseRecords);		
	}

	@Test
	public void testGetActivePurchaseTransactions() throws ParseException{
		
		TransactionRequestDto transactionRequest = new TransactionRequestDto();
		transactionRequest.setToDate("12-03-1992");
		transactionRequest.setFromDate("12-03-1992");
		transactionRequest.setTransactionType("transactionType");
		transactionRequest.setAccountNumber("accountNumber");
		transactionRequest.setMembershipCode("membershipcode");
		List<PurchaseHistory> purchaseRecords = repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest);
		assertNotNull(purchaseRecords);		
	}
	
	@Test
	public void testGetActivePurchaseTransactionsIncludeLinkedAccounts() throws ParseException{
		
		TransactionRequestDto transactionRequest = new TransactionRequestDto();
		transactionRequest.setToDate("12-03-1992");
		transactionRequest.setFromDate("12-03-1992");
		transactionRequest.setTransactionType("transactionType");
		transactionRequest.setAccountNumber("accountNumber");
		transactionRequest.setMembershipCode("membershipcode");
		transactionRequest.setIncludeLinkedAccounts(true);
		List<PurchaseHistory> purchaseRecords = repositoryHelper.findAllActivePurchaseTransactionsForMemberInDuration(transactionRequest);
		assertNotNull(purchaseRecords);		
	}
	
	@Test
	public void testSavePurchaseHistory(){
		
		PurchaseHistory result = repositoryHelper
				.savePurchaseHistory(new PurchaseHistory());
		assertNull(result);		
	}
	
	@Test
	public void testSavePurchaseHistoryNull(){
		
		PurchaseHistory result = repositoryHelper
				.savePurchaseHistory(null);
		assertNull(result);		
	}
	
	@Test
	public void testSaveOffer(){
		
		OfferCatalog result = repositoryHelper.saveOffer(new OfferCatalog());
		assertNull(result);		
	}
	
	@Test
	public void testSaveOfferNull(){
		
		OfferCatalog result = repositoryHelper.saveOffer(null);
		assertNull(result);		
	}
	
	@Test
	public void testFindByOfferCode(){
		
		OfferCatalog result = repositoryHelper.findByOfferCode(OffersRequestMappingConstants.OFFER_ID);
		assertNull(result);		
	}
	
	@Test
	public void testFindByOfferCodeNull(){
		
		OfferCatalog result = repositoryHelper.findByOfferCode(null);
		assertNull(result);		
	}
	
//	@Test
//	public void testFindAllAdminOffers(){
//		
//		List<OfferCatalog> result = repositoryHelper.findAllAdminOffers(0, 1, offerTypeId, status, resultResponse);
//		assertNotNull(result);		
//	}
//	
//	@Test
//	public void testFindAllAdminOffersNullPageAndLimit(){
//		
//		List<OfferCatalog> result = repositoryHelper.findAllAdminOffers(null, null, null, null, null);
//		assertNotNull(result);		
//	}
//	
//	@Test
//	public void testFindOffersByPartnerCode(){
//		
//		List<OfferCatalog> result = repositoryHelper.findOffersByPartnerCode(Mockito.any(String.class), Mockito.any(Integer.class), Mockito.any(Integer.class),
//				Mockito.any(String.class), Mockito.any(String.class), Mockito.any(ResultResponse.class));
//		assertNull(result);		
//	}
//	
//	@Test
//	public void testFindOffersByPartnerCodeNull(){
//		
//		List<OfferCatalog> result = repositoryHelper.findOffersByPartnerCode(null, null, null, null, null, null);
//		assertNull(result);		
//	}
//	
//	@Test
//	public void testFindOffersByMerchant(){
//		
//		List<OfferCatalog> result = repositoryHelper.findOfferByMerchant(Mockito.any(Merchant.class),  Mockito.any(Integer.class), Mockito.any(Integer.class),
//				Mockito.any(String.class), Mockito.any(String.class), Mockito.any(ResultResponse.class));
//		assertNull(result);		
//	}
//	
//	@Test
//	public void testFindOffersByMerchantNull(){
//		
//		List<OfferCatalog> result = repositoryHelper.findOfferByMerchant(null, null, null, null, null, null);
//		assertNull(result);		
//	}
	
	@Test
	public void testFindOfferTypeByDescription(){
		
		OfferType result = repositoryHelper.getOfferTypeByDescription(Mockito.any(String.class));
		assertNull(result);		
	}
	
	@Test
	public void testFindOfferTypeByDescriptionNull(){
		
		OfferType result = repositoryHelper.getOfferTypeByDescription(null);
		assertNull(result);		
	}
	
	
	
	
	
}
