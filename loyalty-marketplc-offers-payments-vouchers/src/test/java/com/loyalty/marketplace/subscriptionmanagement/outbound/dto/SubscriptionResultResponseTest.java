//package com.loyalty.marketplace.subscriptionmanagement.outbound.dto;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResponseDto;
//import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResponseDto;
//import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResultResponse;
//
//@SpringBootTest(classes = SubscriptionResultResponse.class)
//@ActiveProfiles("unittest")
//public class SubscriptionResultResponseTest {
//
//	private SubscriptionResultResponse subscriptionResultResponse;
//	
//	@Before
//	public void setUp(){
//		MockitoAnnotations.initMocks(this);
//		subscriptionResultResponse = new SubscriptionResultResponse("");
//		subscriptionResultResponse.setSubscription(new ArrayList<SubscriptionResponseDto>());
//		subscriptionResultResponse.setSubscriptionCatalog(new ArrayList<SubscriptionCatalogResponseDto>());
//	}
//	
//	@Test
//	public void testGetters() {
//		assertNotNull(subscriptionResultResponse.getSubscription());
//		assertNotNull(subscriptionResultResponse.getSubscriptionCatalog());
//	}
//	
//	@Test
//	public void testToString() {
//		assertNotNull(subscriptionResultResponse.toString());
//	}
//	
//}