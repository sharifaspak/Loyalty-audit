package com.loyalty.marketplace.subscriptionmanagement.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResultResponse;

@SpringBootTest(classes = SubscriptionCatalogResultResponse.class)
@ActiveProfiles("unittest")
public class SubscriptionCatalogResultResponseTest {

	private SubscriptionCatalogResultResponse subscriptionCatalogResultResponse;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		subscriptionCatalogResultResponse = new SubscriptionCatalogResultResponse("");
		subscriptionCatalogResultResponse.setSubscriptionCatalog(new ArrayList<SubscriptionCatalogResponseDto>());
	}
	
	@Test
	public void testGetters() {
		assertNotNull(subscriptionCatalogResultResponse.getSubscriptionCatalog());
	}
	
	@Test
	public void testToString() {
		assertNotNull(subscriptionCatalogResultResponse.toString());
	}
	
}