package com.loyalty.marketplace.outbound.database.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=Category.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class CategoryTest {
	
	
	@InjectMocks
	Category CategoryMock;
	
	private Category category;
		
	@Before
	public void setUp(){
		
		category = new Category();
		
		category.setCategoryId("");
		category.setCategoryName(new CategoryName());
		category.setParentCategory(new Category());
		category.setUsrUpdated("");
		category.setUsrCreated("");
		category.setDtCreated(new Date());
		category.setDtUpdated(new Date());
				
	}
	
	@Test
	public void testGetProgramCode()
	{
		category.setProgramCode("");
		assertEquals(category.getProgramCode(), "");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(category.getCategoryId());
	    assertNotNull(category.getCategoryName());
	    assertNotNull(category.getParentCategory());
	    assertNotNull(category.getUsrCreated());
	    assertNotNull(category.getUsrUpdated());
	    assertNotNull(category.getDtCreated());
	    assertNotNull(category.getDtUpdated());
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
	   
		assertNotNull(category.toString());
	}	
}
