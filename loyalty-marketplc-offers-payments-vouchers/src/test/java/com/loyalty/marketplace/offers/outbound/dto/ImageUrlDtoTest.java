package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=ImageUrlDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class ImageUrlDtoTest {
	
	private ImageUrlDto imageUrl;
		
	@Before
	public void setUp(){
		
		imageUrl = new ImageUrlDto();
		imageUrl.setAvailableInChannel("");
		imageUrl.setImageType("");
		imageUrl.setImageUrl("");
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		
		assertNotNull(imageUrl.getAvailableInChannel());
		assertNotNull(imageUrl.getImageType());
		assertNotNull(imageUrl.getImageUrl());	
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(imageUrl.toString());
	    
			
	}
		
}
