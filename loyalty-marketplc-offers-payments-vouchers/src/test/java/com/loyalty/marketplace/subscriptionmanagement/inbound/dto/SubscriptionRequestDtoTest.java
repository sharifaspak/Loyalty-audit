package com.loyalty.marketplace.subscriptionmanagement.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionRequestDto;

@SpringBootTest(classes = SubscriptionRequestDto.class)
@ActiveProfiles("unittest")
public class SubscriptionRequestDtoTest {

	private SubscriptionRequestDto subscriptionRequestDtoTest;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		subscriptionRequestDtoTest = new SubscriptionRequestDto();
		subscriptionRequestDtoTest.setId("");
		subscriptionRequestDtoTest.setSubscriptionCatalogId("");
		subscriptionRequestDtoTest.setAccountNumber("");
		subscriptionRequestDtoTest.setMembershipCode("");
		subscriptionRequestDtoTest.setPromoCode("");
		subscriptionRequestDtoTest.setStartDate("");
		subscriptionRequestDtoTest.setPaymentMethod("");
	}
	
	@Test
	public void testGetters() {
		assertNotNull(subscriptionRequestDtoTest.getId());
		assertNotNull(subscriptionRequestDtoTest.getSubscriptionCatalogId());
		assertNotNull(subscriptionRequestDtoTest.getAccountNumber());
		assertNotNull(subscriptionRequestDtoTest.getMembershipCode());
		assertNotNull(subscriptionRequestDtoTest.getPromoCode());
		assertNotNull(subscriptionRequestDtoTest.getStartDate());
		assertNotNull(subscriptionRequestDtoTest.getPaymentMethod());
	}
	
	@Test
	public void testToString() {
		assertNotNull(subscriptionRequestDtoTest.toString());
	}
	
}