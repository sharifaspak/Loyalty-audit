package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=OfferInfoDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class StoreDtoTest {
	
	
	@InjectMocks
	StoreDto storeDto;
	
	private StoreDto store;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		store = new StoreDto();
		
		store.setStoreCode("");
		store.setStoreAddressEn("");
		store.setStoreAddressAr("");
		store.setStoreDescriptionEn("");
		store.setStoreDescriptionAr("");
		store.setMobileNumber(new ArrayList<String>());
		store.setStoreCoordinates(new ArrayList<String>());
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(store.getStoreCode());
	    assertNotNull(store.getStoreAddressEn());
	    assertNotNull(store.getStoreAddressAr());
	    assertNotNull(store.getStoreDescriptionEn());
	    assertNotNull(store.getStoreDescriptionAr());
	    assertNotNull(store.getMobileNumber());
	    assertNotNull(store.getStoreCoordinates());
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(store.toString());
	    
			
	}
		
}
