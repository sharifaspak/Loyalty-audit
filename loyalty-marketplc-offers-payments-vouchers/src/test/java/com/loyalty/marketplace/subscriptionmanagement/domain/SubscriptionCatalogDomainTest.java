//package com.loyalty.marketplace.subscriptionmanagement.domain;
//
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
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
//import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
//import com.loyalty.marketplace.outbound.dto.ResultResponse;
//import com.loyalty.marketplace.subscription.domain.SubscriptionCatalogDomain;
//import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
//import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
//import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
//import com.mongodb.MongoWriteException;
//
//@SpringBootTest(classes = SubscriptionCatalogDomain.class)
//@ActiveProfiles("unittest")
//public class SubscriptionCatalogDomainTest {
//
//	@Mock
//	ModelMapper modelMapper;
//	
//	@Mock
//	SubscriptionCatalogRepository subscriptionCatalogRepository;
//	
//	@InjectMocks
//	SubscriptionCatalogDomain subscriptionCatalogDomain = new SubscriptionCatalogDomain();
//	
//	private String id;
//	private String subscriptionTitle;
//	private String subscriptionDescription;
//	private Integer cost;
//	private Integer pointsValue;
//	private Integer freeDuration;
//	private Integer validityPeriod;
//	private Date startDate;
//	private Date endDate;
//	private String status;
//	private String chargeabilityType;
//	private List<PaymentMethodDto> paymentMethods;
//	private List<String> customerSegment;
//	private List<String> linkedOfferId;
//	private String subscriptionLogo;
//	private Date createdDate;
//	private String createdUser;
//	private Date updatedDate;
//	private String updatedUser;
//	private String programCode;
//	
//	private SubscriptionCatalogDomain catalogDomain;
//	private SubscriptionCatalogDomain subCatalogDomain;
//	
//	private String externalTransactionId;
//	ResultResponse resultResponse;
//	SubscriptionCatalog subscriptionCatalog;
//
//	@Before
//	public void setUp() {
//
//		externalTransactionId = "4728";
//		MockitoAnnotations.initMocks(this);
//		resultResponse = new ResultResponse(externalTransactionId);
//		subscriptionCatalog = new SubscriptionCatalog();
//
//		subscriptionCatalog.setId("Sub ID");
//		subscriptionCatalog.setSubscriptionTitle("Title");
//		subscriptionCatalog.setSubscriptionDescription("Description");
//		subscriptionCatalog.setCost(10);
//		subscriptionCatalog.setPointsValue(11);
//		subscriptionCatalog.setFreeDuration(12);
//		subscriptionCatalog.setValidityPeriod(13);
//		subscriptionCatalog.setStartDate(new Date());
//		subscriptionCatalog.setEndDate(new Date());
//		subscriptionCatalog.setStatus("Status");
//		subscriptionCatalog.setChargeabilityType("Chargability");
//		subscriptionCatalog.setSubscriptionLogo("Logo");
//		List<String> customerSegment = new ArrayList<>();
//		customerSegment.add("Seg 1");
//		customerSegment.add("Seg 2");
//		subscriptionCatalog.setCustomerSegment(customerSegment);
//		List<PaymentMethodDto> paymentMethods = new ArrayList<>();
////		paymentMethod.add("card");
////		paymentMethod.add("dcb");
//		//subscriptionCatalog.setPaymentMethod(paymentMethod);
//		List<String> linkedOfferId = new ArrayList<>();
//		linkedOfferId.add("Linked 1");
//		linkedOfferId.add("Linked 2");
//		subscriptionCatalog.setLinkedOfferId(linkedOfferId);
//		
//		id = "ID";
//		subscriptionTitle = "Title";
//		subscriptionDescription = "Description";
//		cost = 20;
//		pointsValue = 21;
//		freeDuration = 22;
//		validityPeriod = 23;
//		startDate = new Date();
//		endDate = new Date();
//		status = "Active";
//		chargeabilityType = "Chargability";
//		subscriptionLogo = "Subscription";
//		paymentMethods = new ArrayList<>();
////		paymentMethod.add("card");
////		paymentMethod.add("dcb");
//		customerSegment = new ArrayList<>();
//		customerSegment.add("Seg 1");
//		customerSegment.add("Seg 2");
//		linkedOfferId = new ArrayList<>();
//		linkedOfferId.add("Linked 1");
//		linkedOfferId.add("Linked 2");
//		
////		catalogDomain = new SubscriptionCatalogDomain.SubscriptionCatalogBuilder(id, subscriptionTitle, subscriptionDescription,
////				cost, pointsValue, freeDuration, validityPeriod, startDate,
////				endDate, status, chargeabilityType, paymentMethods,
////				customerSegment, linkedOfferId, createdDate,
////				createdUser, updatedDate, updatedUser, programCode).build();
////		
////		subCatalogDomain = new SubscriptionCatalogDomain.SubscriptionCatalogBuilder(subscriptionTitle, subscriptionDescription,
////				cost, pointsValue, freeDuration, validityPeriod, startDate,
////				endDate, status, chargeabilityType, paymentMethods,
////				customerSegment, linkedOfferId, createdDate,
////				createdUser, updatedDate, updatedUser, programCode).build();
//
//	}
//	
//	@Test
//	public void testSaveSubscriptionCatalogSuccess() throws SubscriptionManagementException {
//		when(modelMapper.map(catalogDomain, SubscriptionCatalog.class)).thenReturn(subscriptionCatalog);
//		when(this.subscriptionCatalogRepository.insert(subscriptionCatalog)).thenReturn(subscriptionCatalog);
//		subscriptionCatalogDomain.saveSubscriptionCatalog(catalogDomain);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testSaveSubscriptionCatalogMongoException() throws SubscriptionManagementException {
//		when(modelMapper.map(catalogDomain, SubscriptionCatalog.class)).thenReturn(subscriptionCatalog);
//		when(this.subscriptionCatalogRepository.insert(subscriptionCatalog)).thenThrow(MongoWriteException.class);
//		subscriptionCatalogDomain.saveSubscriptionCatalog(catalogDomain);
//		assertNotNull(subscriptionCatalog);
//	}
//
//	@Test(expected = SubscriptionManagementException.class)
//	public void testSaveSubscriptionCatalogException() throws SubscriptionManagementException {
//		when(modelMapper.map(catalogDomain, SubscriptionCatalog.class)).thenThrow(NullPointerException.class);
//		when(this.subscriptionCatalogRepository.insert(subscriptionCatalog)).thenReturn(subscriptionCatalog);
//		subscriptionCatalogDomain.saveSubscriptionCatalog(catalogDomain);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogSuccess() throws SubscriptionManagementException {
//		when(modelMapper.map(catalogDomain, SubscriptionCatalog.class)).thenReturn(subscriptionCatalog);
//		when(this.subscriptionCatalogRepository.save(subscriptionCatalog)).thenReturn(subscriptionCatalog);
//		subscriptionCatalogDomain.updateSubscriptionCatalog(catalogDomain, subscriptionCatalog);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testUpdateSubscriptionCatalogMongoException() throws SubscriptionManagementException {
//		when(modelMapper.map(catalogDomain, SubscriptionCatalog.class)).thenReturn(subscriptionCatalog);
//		when(this.subscriptionCatalogRepository.save(subscriptionCatalog)).thenThrow(MongoWriteException.class);
//		subscriptionCatalogDomain.updateSubscriptionCatalog(catalogDomain, subscriptionCatalog);
//		assertNotNull(subscriptionCatalog);
//	}
//
//	@Test(expected = SubscriptionManagementException.class)
//	public void testUpdateSubscriptionCatalogException() throws SubscriptionManagementException {
//		when(modelMapper.map(catalogDomain, SubscriptionCatalog.class)).thenThrow(NullPointerException.class);
//		when(this.subscriptionCatalogRepository.save(subscriptionCatalog)).thenReturn(subscriptionCatalog);
//		subscriptionCatalogDomain.updateSubscriptionCatalog(catalogDomain, subscriptionCatalog);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//	@Test
//	public void testDeleteSubscriptionCatalogSuccess() throws SubscriptionManagementException {
//		subscriptionCatalogDomain.deleteSubscriptionCatalog(id);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testDeleteSubscriptionCatalogException() throws SubscriptionManagementException {
//		doThrow(NullPointerException.class).doNothing().when(subscriptionCatalogRepository).deleteById(id);
//		subscriptionCatalogDomain.deleteSubscriptionCatalog(id);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//	@Test
//	public void testFindSubscriptionCatalogSuccess() throws SubscriptionManagementException {
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findById(id)).thenReturn(subscriptionCatalog);
//		subscriptionCatalogDomain.findSubscriptionCatalog(id);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//	@Test(expected = SubscriptionManagementException.class)
//	public void testFindSubscriptionCatalogException() throws SubscriptionManagementException {
//		when(subscriptionCatalogRepository.findById(id)).thenThrow(NullPointerException.class);
//		subscriptionCatalogDomain.findSubscriptionCatalog(id);
//		assertNotNull(subscriptionCatalog);
//	}
//	
//}
