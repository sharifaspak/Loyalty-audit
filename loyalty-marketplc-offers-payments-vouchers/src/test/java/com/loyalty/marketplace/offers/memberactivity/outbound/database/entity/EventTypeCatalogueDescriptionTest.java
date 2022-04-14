package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EventTypeCatalogueDescription.class)
@ActiveProfiles("unittest")
public class EventTypeCatalogueDescriptionTest {

	private EventTypeCatalogueDescription eventTypeCatalogueDescription;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		eventTypeCatalogueDescription = new EventTypeCatalogueDescription();
		eventTypeCatalogueDescription.setEnglish("");
		eventTypeCatalogueDescription.setArabic("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(eventTypeCatalogueDescription.getEnglish());
		assertNotNull(eventTypeCatalogueDescription.getArabic());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(eventTypeCatalogueDescription.toString());
	}
	
}
