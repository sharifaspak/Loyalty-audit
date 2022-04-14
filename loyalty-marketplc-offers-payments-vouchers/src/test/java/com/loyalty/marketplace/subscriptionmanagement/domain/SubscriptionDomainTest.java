//package com.loyalty.marketplace.subscriptionmanagement.domain;
//
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//import java.util.Date;
//import java.util.Optional;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
//import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
//import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;
//import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
//import com.mongodb.MongoException;
//import com.mongodb.MongoWriteException;
//
//@SpringBootTest(classes = SubscriptionDomain.class)
//@ActiveProfiles("unittest")
//public class SubscriptionDomainTest {
//
//	@Mock
//	ModelMapper modelMapper;
//	
//	@Mock
//	SubscriptionRepository subscriptionRepository;
//	
//	@InjectMocks
//	SubscriptionDomain subscriptionDomain = new SubscriptionDomain();
//	
//	private String id;
//	private String programCode;
//	private String subscriptionCatalogId;
//	private String accountNumber;
//	private String membershipCode;
//	private String promoCode;
//	private Integer cost;
//	private Integer pointsValue;
//	private Integer freeDuration;
//	private Integer validityPeriod;
//	private Date startDate;
//	private Date endDate;
//	private String status;
//	private String paymentMethod;
//	private String phoneyTunesPackageId;
//	private String transactionId;
//	private Date createdDate;
//	private String createdUser;
//	private Date updatedDate;
//	private String updatedUser;
//	
//	private SubscriptionDomain subDomain;
//	private SubscriptionDomain subsDomain;
//
//	private String subscriptionStatus;
//	private String userName;
//	private String subscriptionId;
//	
//	Subscription subscription;
//	
//	@Before
//	public void setUp() {
//		
//		MockitoAnnotations.initMocks(this);
//
//		subscription = new Subscription();
//		subscription.setId("Sub ID");
//		subscription.setProgramCode("Smiles");
//		subscription.setSubscriptionCatalogId("Subscription Catalog ID");
//		subscription.setAccountNumber("Account Number");
//		subscription.setMembershipCode("Membership Code");
//		subscription.setPromoCode("Promo Code");
//		subscription.setCost(10);
//		subscription.setPointsValue(11);
//		subscription.setFreeDuration(12);
//		subscription.setValidityPeriod(13);
//		subscription.setStartDate(new Date());
//		subscription.setEndDate(new Date());
//		subscription.setStatus("Status");
//		subscription.setPaymentMethod("card");
//		subscription.setPhoneyTunesPackageId("Phony ID");
//		subscription.setTransactionId("Transaction ID");
//		subscription.setCreatedDate(new Date());
//		subscription.setCreatedUser("Created User");
//		subscription.setUpdatedDate(new Date());
//		subscription.setUpdatedUser("Updated User");
//		
//		id = "ID";
//		programCode = "Program";
//		subscriptionCatalogId = "Subscription ID";
//		accountNumber = "Account Number";
//		membershipCode = "Membership Code";
//		promoCode = "Promo Code";
//		cost = 20;
//		pointsValue = 21;
//		freeDuration = 22;
//		validityPeriod = 23;
//		startDate = new Date();
//		endDate = new Date();
//		status = "Active";
//		paymentMethod = "card";
//		phoneyTunesPackageId = "Phony ID";
//		transactionId = "Transaction ID";
//		createdDate = new Date();
//		createdUser = "Created User";
//		updatedDate = new Date();
//		updatedUser = "Updated User";
//		
//		subscriptionStatus = "Active";
//		userName = "Username";
//		subscriptionId = "Id";
//		
////		subDomain = new SubscriptionDomain.SubscriptionBuilder(id, programCode, subscriptionCatalogId, accountNumber, membershipCode,
////				promoCode, cost, pointsValue, freeDuration, validityPeriod,
////				startDate, endDate, status, paymentMethod,
////				phoneyTunesPackageId, transactionId, createdDate, createdUser,
////				updatedDate, updatedUser).build();
////		
////		subsDomain = new SubscriptionDomain.SubscriptionBuilder(programCode, subscriptionCatalogId, accountNumber, membershipCode,
////				promoCode, cost, pointsValue, freeDuration, validityPeriod,
////				startDate, endDate, status, paymentMethod,
////				phoneyTunesPackageId, transactionId, createdDate, createdUser,
////				updatedDate, updatedUser).build();
//
//	}
//	
//	@Test
//	public void testSaveSubscriptionSuccess() throws SubscriptionManagementException {
//		when(modelMapper.map(subDomain, Subscription.class)).thenReturn(subscription);
//		when(this.subscriptionRepository.insert(subscription)).thenReturn(subscription);
//		subscriptionDomain.saveSubscription(subDomain);
//		assertNotNull(subscription);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testSaveSubscriptionMongoException() throws SubscriptionManagementException {
//		when(modelMapper.map(subDomain, Subscription.class)).thenReturn(subscription);
//		when(this.subscriptionRepository.insert(subscription)).thenThrow(MongoWriteException.class);
//		subscriptionDomain.saveSubscription(subDomain);
//		assertNotNull(subscription);
//	}
//
//	@Test(expected = SubscriptionManagementException.class)
//	public void testSaveSubscriptionException() throws SubscriptionManagementException {
//		when(modelMapper.map(subDomain, Subscription.class)).thenThrow(NullPointerException.class);
//		when(this.subscriptionRepository.insert(subscription)).thenReturn(subscription);
//		subscriptionDomain.saveSubscription(subDomain);
//		assertNotNull(subscription);
//	}
//	
//	@Test
//	public void testUpdateSubscriptionSuccess() throws SubscriptionManagementException {
//		when(modelMapper.map(subDomain, Subscription.class)).thenReturn(subscription);
//		when(this.subscriptionRepository.save(subscription)).thenReturn(subscription);
//		subscriptionDomain.updateSubscription(subDomain, subscription);
//		assertNotNull(subscription);
//	}
//
//	@Test(expected = SubscriptionManagementException.class)
//	public void testUpdateSubscriptionCatalogException() throws SubscriptionManagementException {
//		when(modelMapper.map(subDomain, Subscription.class)).thenThrow(NullPointerException.class);
//		when(this.subscriptionRepository.save(subscription)).thenReturn(subscription);
//		subscriptionDomain.updateSubscription(subDomain, subscription);
//		assertNotNull(subscription);
//	}
//	
//	@Test
//	public void testCancelSubscriptionSuccess() throws SubscriptionManagementException {
//		when(this.subscriptionRepository.save(subscription)).thenReturn(subscription);
//		subscriptionDomain.cancelSubscription(subscription, subscriptionStatus, userName);
//		assertNotNull(subscription);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testCancelSubscriptionException() throws SubscriptionManagementException {
//		when(this.subscriptionRepository.save(subscription)).thenThrow(MongoException.class);
//		subscriptionDomain.cancelSubscription(subscription, subscriptionStatus, userName);
//		assertNotNull(subscription);
//	}
//	
//	@Test
//	public void testDeleteSubscriptionSuccess() throws SubscriptionManagementException {
//		doNothing().when(subscriptionRepository).deleteById(subscriptionId);
//		subscriptionDomain.deleteSubscription(subscriptionId);
//		assertNotNull(subscription);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testDeleteSubscriptionException() throws SubscriptionManagementException {
//		doThrow(NullPointerException.class).when(subscriptionRepository).deleteById(subscriptionId);
//		subscriptionDomain.deleteSubscription(subscriptionId);
//		assertNotNull(subscription);
//	}
//	
//	@Test
//	public void testFindSubscriptionSuccess() throws SubscriptionManagementException {
//		Subscription subs = new Subscription();
//		when(subscriptionRepository.findBySubscriptionCatalogId(subscriptionCatalogId)).thenReturn(Optional.of(subs));
//		subscriptionDomain.findSubscription(subscriptionCatalogId);
//		assertNotNull(subs);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testFindSubscriptionException() throws SubscriptionManagementException {
//		when(subscriptionRepository.findBySubscriptionCatalogId(subscriptionCatalogId)).thenThrow(MongoException.class);
//		subscriptionDomain.findSubscription(subscriptionCatalogId);
//		assertNotNull(subscription);
//	}
//	
////	@Test
////	public void testFindByIdAndAccountNumberAndProgramCodeSuccess() throws SubscriptionManagementException {
////		Subscription subs = new Subscription();
////		when(subscriptionRepository.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionCatalogId, accountNumber, programCode)).thenReturn(Optional.of(subs));
////		subscriptionDomain.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionCatalogId, accountNumber, programCode);
////		assertNotNull(subs);
////	}
////	
////	@Test(expected = SubscriptionManagementException.class)
////	public void testFindByIdAndAccountNumberAndProgramCodeException() throws SubscriptionManagementException {
////		when(subscriptionRepository.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionCatalogId, accountNumber, programCode)).thenThrow(MongoException.class);
////		subscriptionDomain.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionCatalogId, accountNumber, programCode);
////		assertNotNull(subscription);
////	}
//	
//}
