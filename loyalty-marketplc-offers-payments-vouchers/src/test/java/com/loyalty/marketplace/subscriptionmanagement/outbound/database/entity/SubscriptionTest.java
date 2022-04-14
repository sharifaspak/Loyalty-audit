package com.loyalty.marketplace.subscriptionmanagement.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;

@SpringBootTest(classes = Subscription.class)
@ActiveProfiles("unittest")
public class SubscriptionTest {

	private Subscription subscription;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		subscription = new Subscription();
		subscription.setId("");
		subscription.setProgramCode("");
		subscription.setCost(0.0);
		subscription.setFreeDuration(0);
		subscription.setSubscriptionCatalogId("");
		subscription.setAccountNumber("");
		subscription.setMembershipCode("");
		subscription.setPromoCode("");
		subscription.setPhoneyTunesPackageId("");
		subscription.setPointsValue(0);
		subscription.setValidityPeriod(0);
		subscription.setStatus("");
		subscription.setPaymentMethod("");
		subscription.setTransactionId("");
		subscription.setCreatedDate(new Date());
		subscription.setUpdatedDate(new Date());
		subscription.setCreatedUser("");
		subscription.setUpdatedUser("");
		subscription.setStartDate(new Date());
		subscription.setEndDate(new Date());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(subscription.getProgramCode());
		assertNotNull(subscription.getCost());
		assertNotNull(subscription.getFreeDuration());
		assertNotNull(subscription.getId());
		assertNotNull(subscription.getSubscriptionCatalogId());
		assertNotNull(subscription.getAccountNumber());
		assertNotNull(subscription.getMembershipCode());
		assertNotNull(subscription.getPromoCode());
		assertNotNull(subscription.getPhoneyTunesPackageId());
		assertNotNull(subscription.getPointsValue());
		assertNotNull(subscription.getStartDate());
		assertNotNull(subscription.getEndDate());
		assertNotNull(subscription.getValidityPeriod());
		assertNotNull(subscription.getStatus());
		assertNotNull(subscription.getPaymentMethod());
		assertNotNull(subscription.getTransactionId());
		assertNotNull(subscription.getCreatedDate());
		assertNotNull(subscription.getUpdatedDate());
		assertNotNull(subscription.getCreatedUser());
		assertNotNull(subscription.getUpdatedUser());
		assertNotNull(subscription.getStartDate());
		assertNotNull(subscription.getEndDate());
	}
	
	@Test
	public void testToString() {
		assertNotNull(subscription.toString());
	}
	
}