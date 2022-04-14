package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = GiftInfo.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class GiftInfoTest {
	
	private GiftInfo giftInfo;
	
	@Before
	public void setUp(){
		
		giftInfo = new GiftInfo();
		giftInfo.setIsGift("");
		giftInfo.setGiftChannels(new ArrayList<>());
		giftInfo.setGiftSubCardTypes(new ArrayList<>());
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(giftInfo.getIsGift());
		assertNotNull(giftInfo.getGiftChannels());
		assertNotNull(giftInfo.getGiftSubCardTypes());
		
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(giftInfo.toString());
	    
	}
		
}
