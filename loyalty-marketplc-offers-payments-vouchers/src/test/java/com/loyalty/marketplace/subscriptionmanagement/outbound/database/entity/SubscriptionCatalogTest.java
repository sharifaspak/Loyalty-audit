//package com.loyalty.marketplace.subscriptionmanagement.outbound.database.entity;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
//
//@SpringBootTest(classes = SubscriptionCatalog.class)
//@ActiveProfiles("unittest")
//public class SubscriptionCatalogTest {
//
//	private SubscriptionCatalog subscriptionCatalog;
//	
//	@Before
//	public void setUp(){
//		MockitoAnnotations.initMocks(this);
//		subscriptionCatalog = new SubscriptionCatalog();
//		subscriptionCatalog.setId("");
//		subscriptionCatalog.setProgramCode("");
//		subscriptionCatalog.setCost(0);
//		subscriptionCatalog.setFreeDuration(0);
//		subscriptionCatalog.setSubscriptionLogo("");
//		subscriptionCatalog.setSubscriptionTitle("");
//		subscriptionCatalog.setSubscriptionDescription("");
//		subscriptionCatalog.setCustomerSegment(new ArrayList<String>());
//		subscriptionCatalog.setChargeabilityType("");
//		subscriptionCatalog.setPointsValue(0);
//		subscriptionCatalog.setValidityPeriod(0);
//		subscriptionCatalog.setStatus("");
//		//subscriptionCatalog.setPaymentMethod(new ArrayList<String>());
//		subscriptionCatalog.setLinkedOfferId(new ArrayList<String>());
//		subscriptionCatalog.setCreatedDate(new Date());
//		subscriptionCatalog.setUpdatedDate(new Date());
//		subscriptionCatalog.setCreatedUser("");
//		subscriptionCatalog.setUpdatedUser("");
//		subscriptionCatalog.setStartDate(new Date());
//		subscriptionCatalog.setEndDate(new Date());
//	}
//	
//	@Test
//	public void testGetters() {
//		assertNotNull(subscriptionCatalog.getProgramCode());
//		assertNotNull(subscriptionCatalog.getCost());
//		assertNotNull(subscriptionCatalog.getFreeDuration());
//		assertNotNull(subscriptionCatalog.getId());
//		assertNotNull(subscriptionCatalog.getSubscriptionLogo());
//		assertNotNull(subscriptionCatalog.getSubscriptionTitle());
//		assertNotNull(subscriptionCatalog.getSubscriptionDescription());
//		assertNotNull(subscriptionCatalog.getCustomerSegment());
//		assertNotNull(subscriptionCatalog.getChargeabilityType());
//		assertNotNull(subscriptionCatalog.getPointsValue());
//		assertNotNull(subscriptionCatalog.getStartDate());
//		assertNotNull(subscriptionCatalog.getEndDate());
//		assertNotNull(subscriptionCatalog.getValidityPeriod());
//		assertNotNull(subscriptionCatalog.getStatus());
//		//assertNotNull(subscriptionCatalog.getPaymentMethod());
//		assertNotNull(subscriptionCatalog.getLinkedOfferId());
//		assertNotNull(subscriptionCatalog.getCreatedDate());
//		assertNotNull(subscriptionCatalog.getUpdatedDate());
//		assertNotNull(subscriptionCatalog.getCreatedUser());
//		assertNotNull(subscriptionCatalog.getUpdatedUser());
//		assertNotNull(subscriptionCatalog.getStartDate());
//		assertNotNull(subscriptionCatalog.getEndDate());
//	}
//	
//	@Test
//	public void testToString() {
//		assertNotNull(subscriptionCatalog.toString());
//	}
//	
//}