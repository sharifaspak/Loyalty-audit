package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=GiftInfoDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class GiftInfoDomainTest {
	
	private GiftInfoDomain giftInfoDomain;
		
	@Before
	public void setUp(){
		
		giftInfoDomain = new GiftInfoDomain();
		giftInfoDomain = new GiftInfoDomain("",
				new ArrayList<>(), new ArrayList<>());
		
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(giftInfoDomain.getIsGift());
	    assertNotNull(giftInfoDomain.getGiftChannels());
	    assertNotNull(giftInfoDomain.getGiftSubCardTypes());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(giftInfoDomain.toString());
			
	}
		
}
