package com.loyalty.marketplace.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=CategoryName.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class CategoryNameTest {
	
	
	@InjectMocks
	CategoryName categoryNameMock;
	
	private CategoryName categoryName;
		
	@Before
	public void setUp(){
		
		categoryName = new CategoryName();
		
		categoryName.setCategoryNameEn("");
		categoryName.setCategoryNameAr("");
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(categoryName.getCategoryNameEn());
	    assertNotNull(categoryName.getCategoryNameAr());
	    
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
	   
		assertNotNull(categoryName.toString());
	}	
}
