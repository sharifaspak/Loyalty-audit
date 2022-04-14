package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = WishlistEntity.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class WishlistEntityTest {
	
	private WishlistEntity wishlistEntity;
	
	@Before
	public void setUp(){
		
		wishlistEntity = new WishlistEntity();
		wishlistEntity.setAccountNumber("");
		wishlistEntity.setMembershipCode("");
		wishlistEntity.setId("");
		wishlistEntity.setOffers(new ArrayList<>());
		wishlistEntity.setDtCreated(new Date());
		wishlistEntity.setDtUpdated(new Date());
		wishlistEntity.setUsrCreated("");
		wishlistEntity.setUsrUpdated("");
		wishlistEntity.setProgram("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(wishlistEntity.getAccountNumber());
		assertNotNull(wishlistEntity.getMembershipCode());
		assertNotNull(wishlistEntity.getId());
		assertNotNull(wishlistEntity.getOffers());
		assertNotNull(wishlistEntity.getDtCreated());
		assertNotNull(wishlistEntity.getDtUpdated());
		assertNotNull(wishlistEntity.getUsrCreated());
		assertNotNull(wishlistEntity.getUsrUpdated());
		assertNotNull(wishlistEntity.getProgram());
		
		
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(wishlistEntity.toString());
	    
	}
		
}
