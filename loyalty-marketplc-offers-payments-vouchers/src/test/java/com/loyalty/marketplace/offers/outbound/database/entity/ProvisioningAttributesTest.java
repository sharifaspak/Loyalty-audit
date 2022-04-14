package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = ProvisioningAttributes.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ProvisioningAttributesTest {
	
	@InjectMocks
	ProvisioningAttributes provisioningAttributesMock;
	
	private ProvisioningAttributes provisioningAttributes;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		provisioningAttributes = new ProvisioningAttributes();
		provisioningAttributes.setRatePlanCode("");
		provisioningAttributes.setRtfProductCode("");
		provisioningAttributes.setRtfProductType("");
		provisioningAttributes.setVasCode("");
		provisioningAttributes.setVasActionId("");
		provisioningAttributes.setPromotionalPeriod(0);
		provisioningAttributes.setActivityId("");
		provisioningAttributes.setPackName("");
		provisioningAttributes.setServiceId("");
		provisioningAttributes.setFeature("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(provisioningAttributes.getRatePlanCode());
	    assertNotNull(provisioningAttributes.getRtfProductCode());
	    assertNotNull(provisioningAttributes.getRtfProductType());
	    assertNotNull(provisioningAttributes.getVasCode());
	    assertNotNull(provisioningAttributes.getVasActionId());
	    assertNotNull(provisioningAttributes.getVasActionId());
		assertNotNull(provisioningAttributes.getPromotionalPeriod());
		assertNotNull(provisioningAttributes.getActivityId());
		assertNotNull(provisioningAttributes.getPackName());
		assertNotNull(provisioningAttributes.getServiceId());
		assertNotNull(provisioningAttributes.getFeature());
			
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
