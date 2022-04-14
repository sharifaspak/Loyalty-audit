package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EventTypeCatalogueName.class)
@ActiveProfiles("unittest")
public class EventTypeCatalogueNameTest {

	private EventTypeCatalogueName eventTypeCatalogueName;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		eventTypeCatalogueName = new EventTypeCatalogueName();
		eventTypeCatalogueName.setEnglish("");
		eventTypeCatalogueName.setArabic("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(eventTypeCatalogueName.getEnglish());
		assertNotNull(eventTypeCatalogueName.getArabic());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(eventTypeCatalogueName.toString());
	}
	
}
