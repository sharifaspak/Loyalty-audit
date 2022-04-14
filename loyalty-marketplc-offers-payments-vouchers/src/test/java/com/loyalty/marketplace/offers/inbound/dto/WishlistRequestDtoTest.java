package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = WishlistRequestDto.class)
@ActiveProfiles("unittest")
public class WishlistRequestDtoTest {

	private WishlistRequestDto wishlistDto;
	
	@Before
	public void setUp(){
		wishlistDto = new WishlistRequestDto();
		wishlistDto.setOfferId("");
		wishlistDto.setAction("");
		
	}
	
	@Test
	public void testGetters() {
		assertNotNull(wishlistDto.getOfferId());
		assertNotNull(wishlistDto.getAction());
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(wishlistDto.toString());
	}
	
}
