package com.loyalty.marketplace.subscriptionmanagement.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResponseDto;


@SpringBootTest(classes = SubscriptionResponseDto.class)
@ActiveProfiles("unittest")
public class SubscriptionResponseDtoTest {

	private SubscriptionResponseDto subscriptionResponseDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		subscriptionResponseDto = new SubscriptionResponseDto();
		subscriptionResponseDto.setId("");
		subscriptionResponseDto.setProgramCode("");
		subscriptionResponseDto.setCost(0.0);
		subscriptionResponseDto.setFreeDuration(0);
		subscriptionResponseDto.setSubscriptionCatalogId("");
		subscriptionResponseDto.setAccountNumber("");
		subscriptionResponseDto.setMembershipCode("");
		subscriptionResponseDto.setPromoCode("");
		subscriptionResponseDto.setPhoneyTunesPackageId("");
		subscriptionResponseDto.setPointsValue(0);
		subscriptionResponseDto.setStartDate(new Date());
		subscriptionResponseDto.setEndDate(new Date());
		subscriptionResponseDto.setValidityPeriod(0);
		subscriptionResponseDto.setStatus("");
		subscriptionResponseDto.setPaymentMethod("");
		subscriptionResponseDto.setTransactionId("");
		subscriptionResponseDto.setCreatedDate(new Date());
		subscriptionResponseDto.setUpdatedDate(new Date());
		subscriptionResponseDto.setCreatedUser("");
		subscriptionResponseDto.setUpdatedUser("");
		subscriptionResponseDto.setStartDate(new Date());
		subscriptionResponseDto.setEndDate(new Date());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(subscriptionResponseDto.getProgramCode());
		assertNotNull(subscriptionResponseDto.getCost());
		assertNotNull(subscriptionResponseDto.getFreeDuration());
		assertNotNull(subscriptionResponseDto.getId());
		assertNotNull(subscriptionResponseDto.getSubscriptionCatalogId());
		assertNotNull(subscriptionResponseDto.getAccountNumber());
		assertNotNull(subscriptionResponseDto.getMembershipCode());
		assertNotNull(subscriptionResponseDto.getPromoCode());
		assertNotNull(subscriptionResponseDto.getPhoneyTunesPackageId());
		assertNotNull(subscriptionResponseDto.getPointsValue());
		assertNotNull(subscriptionResponseDto.getStartDate());
		assertNotNull(subscriptionResponseDto.getEndDate());
		assertNotNull(subscriptionResponseDto.getValidityPeriod());
		assertNotNull(subscriptionResponseDto.getStatus());
		assertNotNull(subscriptionResponseDto.getPaymentMethod());
		assertNotNull(subscriptionResponseDto.getTransactionId());
		assertNotNull(subscriptionResponseDto.getCreatedDate());
		assertNotNull(subscriptionResponseDto.getUpdatedDate());
		assertNotNull(subscriptionResponseDto.getCreatedUser());
		assertNotNull(subscriptionResponseDto.getUpdatedUser());
		assertNotNull(subscriptionResponseDto.getStartDate());
		assertNotNull(subscriptionResponseDto.getEndDate());
	}
	
	@Test
	public void testToString() {
		assertNotNull(subscriptionResponseDto.toString());
	}
	
}