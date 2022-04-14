package com.loyalty.marketplace.stores.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ListStoresResponse.class)
@ActiveProfiles("unittest")
public class ListStoresResponseTest {

	private ListStoresResponse storesResponse;
	private List<StoreResult> testObject;
	private String extTransactionId;
	
	@Before
	public void setUp(){
		extTransactionId = "123";
		storesResponse = new ListStoresResponse(extTransactionId);
		testObject = new ArrayList<StoreResult>();
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(storesResponse.toString());
	}
	
	@Test
	public void testGetStores()
	{
		storesResponse.setStores(testObject);
		assertEquals(storesResponse.getStores(), testObject);
	}
	
}
