package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.outbound.database.entity.Category;

@SpringBootTest(classes = OfferReferences.class)
@ActiveProfiles("unittest")
public class OfferReferencesTest {

	private OfferReferences offerReferences;
	
	@Before
	public void setUp(){
		
		offerReferences = new OfferReferences(null, null, null, null, null, null, null, null);
		offerReferences = new OfferReferences();
		offerReferences.setCategory(new Category());
		offerReferences.setSubCategory(new Category());
		offerReferences.setOfferType(new OfferType());
		offerReferences.setMerchant(new Merchant());
		offerReferences.setDenominations(new ArrayList<>());
		offerReferences.setStore(new ArrayList<>());
		offerReferences.setSize(0);
		offerReferences.setHeader(new Headers());
	}
	
	@Test
	public void testGetters() {
		
		assertNotNull(offerReferences.getCategory());
		assertNotNull(offerReferences.getSubCategory());
		assertNotNull(offerReferences.getOfferType());
		assertNotNull(offerReferences.getMerchant());
		assertNotNull(offerReferences.getDenominations());
		assertNotNull(offerReferences.getStore());
		assertNotNull(offerReferences.getSize());
		assertNotNull(offerReferences.getHeader());
	}
	
	@Test
	public void testToString() {
		assertNotNull(offerReferences.toString());
	}
	
}
