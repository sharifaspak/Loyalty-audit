//package com.loyalty.marketplace.subscriptionmanagement.outbound.dto;
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
//import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResponseDto;
//
//@SpringBootTest(classes = SubscriptionCatalogResponseDto.class)
//@ActiveProfiles("unittest")
//public class SubscriptionCatalogResponseDtoTest {
//
//	private SubscriptionCatalogResponseDto subscriptionCatalogResponseDto;
//	
//	@Before
//	public void setUp(){
//		MockitoAnnotations.initMocks(this);
//		subscriptionCatalogResponseDto = new SubscriptionCatalogResponseDto();
//		subscriptionCatalogResponseDto.setId("");
//		subscriptionCatalogResponseDto.setProgramCode("");
//		subscriptionCatalogResponseDto.setCost(0);
//		subscriptionCatalogResponseDto.setFreeDuration(0);
//		subscriptionCatalogResponseDto.setSubscriptionLogo("");
//		subscriptionCatalogResponseDto.setSubscriptionTitle("");
//		subscriptionCatalogResponseDto.setSubscriptionDescription("");
//		subscriptionCatalogResponseDto.setCustomerSegment(new ArrayList<String>());
//		subscriptionCatalogResponseDto.setChargeabilityType("");
//		subscriptionCatalogResponseDto.setPointsValue(0);
//		subscriptionCatalogResponseDto.setValidityPeriod(0);
//		subscriptionCatalogResponseDto.setStatus("");
//		//subscriptionCatalogResponseDto.setPaymentMethod(new ArrayList<String>());
//		subscriptionCatalogResponseDto.setLinkedOfferId(new ArrayList<String>());
//		subscriptionCatalogResponseDto.setCreatedDate(new Date());
//		subscriptionCatalogResponseDto.setUpdatedDate(new Date());
//		subscriptionCatalogResponseDto.setCreatedUser("");
//		subscriptionCatalogResponseDto.setUpdatedUser("");
//		subscriptionCatalogResponseDto.setStartDate(new Date());
//		subscriptionCatalogResponseDto.setEndDate(new Date());
//	}
//	
//	@Test
//	public void testGetters() {
//		assertNotNull(subscriptionCatalogResponseDto.getProgramCode());
//		assertNotNull(subscriptionCatalogResponseDto.getCost());
//		assertNotNull(subscriptionCatalogResponseDto.getFreeDuration());
//		assertNotNull(subscriptionCatalogResponseDto.getId());
//		assertNotNull(subscriptionCatalogResponseDto.getSubscriptionLogo());
//		assertNotNull(subscriptionCatalogResponseDto.getSubscriptionTitle());
//		assertNotNull(subscriptionCatalogResponseDto.getSubscriptionDescription());
//		assertNotNull(subscriptionCatalogResponseDto.getCustomerSegment());
//		assertNotNull(subscriptionCatalogResponseDto.getChargeabilityType());
//		assertNotNull(subscriptionCatalogResponseDto.getPointsValue());
//		assertNotNull(subscriptionCatalogResponseDto.getStartDate());
//		assertNotNull(subscriptionCatalogResponseDto.getEndDate());
//		assertNotNull(subscriptionCatalogResponseDto.getValidityPeriod());
//		assertNotNull(subscriptionCatalogResponseDto.getStatus());
//		//assertNotNull(subscriptionCatalogResponseDto.getPaymentMethod());
//		assertNotNull(subscriptionCatalogResponseDto.getLinkedOfferId());
//		assertNotNull(subscriptionCatalogResponseDto.getCreatedDate());
//		assertNotNull(subscriptionCatalogResponseDto.getUpdatedDate());
//		assertNotNull(subscriptionCatalogResponseDto.getCreatedUser());
//		assertNotNull(subscriptionCatalogResponseDto.getUpdatedUser());
//		assertNotNull(subscriptionCatalogResponseDto.getStartDate());
//		assertNotNull(subscriptionCatalogResponseDto.getEndDate());
//	}
//	
//	@Test
//	public void testToString() {
//		assertNotNull(subscriptionCatalogResponseDto.toString());
//	}
//	
//}