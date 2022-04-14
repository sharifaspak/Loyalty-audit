//package com.loyalty.marketplace.subscriptionmanagement.inbound.dto;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogRequestDto;
//
//@SpringBootTest(classes = SubscriptionCatalogRequestDto.class)
//@ActiveProfiles("unittest")
//public class SubscriptionCatalogRequestDtoTest {
//
//	private SubscriptionCatalogRequestDto subscriptionCatalogRequestDto;
//	
//	@Before
//	public void setUp(){
//		MockitoAnnotations.initMocks(this);
//		subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setId("");
//		subscriptionCatalogRequestDto.setSubscriptionTitle("");
//		subscriptionCatalogRequestDto.setSubscriptionDescription("");
//		subscriptionCatalogRequestDto.setCost(0);
//		subscriptionCatalogRequestDto.setFreeDuration(0);
//		subscriptionCatalogRequestDto.setPointsValue(0);
//		subscriptionCatalogRequestDto.setValidityPeriod(0);
//		subscriptionCatalogRequestDto.setStartDate("");
//		subscriptionCatalogRequestDto.setEndDate("");
//		subscriptionCatalogRequestDto.setStatus("");
//		subscriptionCatalogRequestDto.setChargeabilityType("");
//		//subscriptionCatalogRequestDto.setPaymentMethod(new ArrayList<String>());
//		subscriptionCatalogRequestDto.setCustomerSegment(new ArrayList<String>());
//		subscriptionCatalogRequestDto.setLinkedOfferId(new ArrayList<String>());
//	}
//	
//	@Test
//	public void testGetters() {
//		assertNotNull(subscriptionCatalogRequestDto.getId());
//		assertNotNull(subscriptionCatalogRequestDto.getSubscriptionTitle());
//		assertNotNull(subscriptionCatalogRequestDto.getSubscriptionDescription());
//		assertNotNull(subscriptionCatalogRequestDto.getPointsValue());
//		assertNotNull(subscriptionCatalogRequestDto.getCost());
//		assertNotNull(subscriptionCatalogRequestDto.getFreeDuration());
//		assertNotNull(subscriptionCatalogRequestDto.getValidityPeriod());
//		assertNotNull(subscriptionCatalogRequestDto.getStartDate());
//		assertNotNull(subscriptionCatalogRequestDto.getEndDate());
//		assertNotNull(subscriptionCatalogRequestDto.getStatus());
//		assertNotNull(subscriptionCatalogRequestDto.getChargeabilityType());
//		//assertNotNull(subscriptionCatalogRequestDto.getPaymentMethod());
//		assertNotNull(subscriptionCatalogRequestDto.getCustomerSegment());
//		assertNotNull(subscriptionCatalogRequestDto.getLinkedOfferId());
//	}
//	
//	@Test
//	public void testToString() {
//		assertNotNull(subscriptionCatalogRequestDto.toString());
//	}
//	
//}