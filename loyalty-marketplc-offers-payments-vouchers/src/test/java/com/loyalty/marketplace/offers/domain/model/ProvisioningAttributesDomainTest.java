package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = ProvisioningAttributesDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ProvisioningAttributesDomainTest {
	
	private ProvisioningAttributesDomain provisioningAttributes;
		
	@Before
	public void setUp(){
	
		provisioningAttributes = new ProvisioningAttributesDomain();
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNull(provisioningAttributes.getRatePlanCode());
		assertNull(provisioningAttributes.getRtfProductCode());
		assertNull(provisioningAttributes.getRtfProductType());
		assertNull(provisioningAttributes.getVasCode());
		assertNull(provisioningAttributes.getVasActionId());
		assertNull(provisioningAttributes.getPromotionalPeriod());
		assertNull(provisioningAttributes.getFeature());
		assertNull(provisioningAttributes.getActivityId());
		assertNull(provisioningAttributes.getPackName());
		assertNull(provisioningAttributes.getServiceId());
		
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(provisioningAttributes.toString());
			
	}
		
}
