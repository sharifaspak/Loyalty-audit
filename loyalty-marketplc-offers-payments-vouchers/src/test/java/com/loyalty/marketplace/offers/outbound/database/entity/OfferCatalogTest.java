package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.outbound.database.entity.Category;


@SpringBootTest(classes=OfferCatalog.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferCatalogTest {
	
	private OfferCatalog offerCatalog;
		
	@Before
	public void setUp() throws ParseException {
		
		offerCatalog = new OfferCatalog();
		offerCatalog.setId("");
		offerCatalog.setOfferId("OF_A155_4");
		offerCatalog.setProgramCode("Smiles");
		offerCatalog.setOfferCode("A155");		
		offerCatalog.setOfferType(new OfferType());
		offerCatalog.setOffer(new OfferDetails());
		offerCatalog.setBrandDescription(new BrandDescription());
		offerCatalog.setTermsAndConditions(new TermsConditions());
		offerCatalog.setTags(new Tags());
		offerCatalog.setWhatYouGet(new WhatYouGet());
		offerCatalog.setOfferDates(new OfferDate());
		offerCatalog.setTrendingRank(1);
		offerCatalog.setStatus("Active");
		offerCatalog.setAvailableInPortals(new ArrayList<>());
		offerCatalog.setNewOffer("newOffer");
		offerCatalog.setGiftInfo(new GiftInfo());
		offerCatalog.setIsDod("IsDod");
		offerCatalog.setIsFeatured("isFeatured");
		offerCatalog.setOfferValues(new OfferValues());
		offerCatalog.setDiscountPerc(10);
		offerCatalog.setEstSavings(3.8);
		offerCatalog.setSharing("sharing");
		offerCatalog.setSharingBonus(12);
		offerCatalog.setVatPercentage(10.2);
		offerCatalog.setLimit(new ArrayList<>());
		offerCatalog.setPartnerCode("Dev23");
		offerCatalog.setMerchant(new Merchant());
		offerCatalog.setOfferStores(new ArrayList<>());
		offerCatalog.setCategory(new Category());
		offerCatalog.setSubCategory(new Category());
		offerCatalog.setDynamicDenomination("dynamicDenomination");
		offerCatalog.setGroupedFlag("groupedFlag");
		offerCatalog.setDynamicDenominationValue(new DynamicDenominationValue());
		offerCatalog.setIncrementalValue(5);
		offerCatalog.setCustomerTypes(new ListValues());
		offerCatalog.setDenominations(new ArrayList<>());
		offerCatalog.setRules(new ArrayList<>());
		offerCatalog.setProvisioningChannel("provisioningChannel");
		offerCatalog.setRules(new ArrayList<>());
		offerCatalog.setProvisioningAttributes(new ProvisioningAttributes());
		offerCatalog.setEarnMultiplier(2.0);
		offerCatalog.setSubOffer(new ArrayList<SubOffer>());
		offerCatalog.setActivityCode(new ActivityCode());
		offerCatalog.setCreatedDate(new Date());
		offerCatalog.setCreatedUser("");
		offerCatalog.setUpdatedDate(new Date());
		offerCatalog.setUpdatedUser("");
		offerCatalog.setCustomerSegments(new ListValues());
		offerCatalog.setIsBirthdayGift("");
		offerCatalog.setOfferRating(new OfferRating());
		offerCatalog.setStaticRating(0);
		offerCatalog.setVoucherInfo(new VoucherInfo());
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerCatalog.getOfferId());
	    assertNotNull(offerCatalog.getProgramCode());
	    assertNotNull(offerCatalog.getOfferCode());
	    assertNotNull(offerCatalog.getOfferType());
	    assertNotNull(offerCatalog.getOffer());
		assertNotNull(offerCatalog.getBrandDescription());
		assertNotNull(offerCatalog.getTermsAndConditions());
		assertNotNull(offerCatalog.getTags());
		assertNotNull(offerCatalog.getWhatYouGet());
		assertNotNull(offerCatalog.getOfferDates());
		assertNotNull(offerCatalog.getTrendingRank());
		assertNotNull(offerCatalog.getStatus());
		assertNotNull(offerCatalog.getAvailableInPortals());
		assertNotNull(offerCatalog.getNewOffer());
		assertNotNull(offerCatalog.getGiftInfo());
		assertNotNull(offerCatalog.getIsDod());
		assertNotNull(offerCatalog.getIsFeatured());
		assertNotNull(offerCatalog.getOfferValues());
		assertNotNull(offerCatalog.getDiscountPerc());
		assertNotNull(offerCatalog.getEstSavings());
		assertNotNull(offerCatalog.getSharing());
		assertNotNull(offerCatalog.getSharingBonus());
		assertNotNull(offerCatalog.getVatPercentage());
		assertNotNull(offerCatalog.getLimit());
		assertNotNull(offerCatalog.getPartnerCode());
		assertNotNull(offerCatalog.getMerchant());
		assertNotNull(offerCatalog.getOfferStores());
		assertNotNull(offerCatalog.getCategory());
		assertNotNull(offerCatalog.getSubCategory());
		assertNotNull(offerCatalog.getDynamicDenomination());
		assertNotNull(offerCatalog.getGroupedFlag());
		assertNotNull(offerCatalog.getDynamicDenominationValue());
		assertNotNull(offerCatalog.getIncrementalValue());
		assertNotNull(offerCatalog.getCustomerTypes());
		assertNotNull(offerCatalog.getDenominations());
		assertNotNull(offerCatalog.getRules());
		assertNotNull(offerCatalog.getProvisioningChannel());
		assertNotNull(offerCatalog.getProvisioningAttributes());
		assertNotNull(offerCatalog.getSubOffer());
		assertNotNull(offerCatalog.getEarnMultiplier());
		assertNotNull(offerCatalog.getActivityCode());
		assertNotNull(offerCatalog.getCreatedDate());
		assertNotNull(offerCatalog.getCreatedUser());
		assertNotNull(offerCatalog.getUpdatedDate());
		assertNotNull(offerCatalog.getUpdatedUser());
		assertNotNull(offerCatalog.getCustomerSegments());
		assertNotNull(offerCatalog.getIsBirthdayGift());
		assertNotNull(offerCatalog.getOfferRating());
		assertNotNull(offerCatalog.getStaticRating());
		assertNotNull(offerCatalog.getVoucherInfo());
		
	}
	
	/**
	 * 
	 * Test toString method
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerCatalog.toString());
	}	
	
	
	
}
