package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.EventTypeCatalogueDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.EventTypeCatalogueName;

@SpringBootTest(classes = EventTypeCatalogueDTO.class)
@ActiveProfiles("unittest")
public class EventTypeCatalogueDTOTest {

	private EventTypeCatalogueDTO eventTypeCatalogueDTO;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		eventTypeCatalogueDTO = new EventTypeCatalogueDTO();
		eventTypeCatalogueDTO.setId("");
		eventTypeCatalogueDTO.setName(new EventTypeCatalogueName());
		eventTypeCatalogueDTO.setDescription(new EventTypeCatalogueDescription());
		EventTypeCatalogueDTO eventType = new EventTypeCatalogueDTO("", new EventTypeCatalogueName(), new EventTypeCatalogueDescription());
		eventType.setId("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(eventTypeCatalogueDTO.getId());
		assertNotNull(eventTypeCatalogueDTO.getName());
		assertNotNull(eventTypeCatalogueDTO.getDescription());
	}
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(eventTypeCatalogueDTO.toString());
	}
	
}