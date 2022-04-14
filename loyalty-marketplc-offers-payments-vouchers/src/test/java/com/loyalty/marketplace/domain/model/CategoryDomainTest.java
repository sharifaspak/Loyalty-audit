package com.loyalty.marketplace.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=CategoryDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class CategoryDomainTest {
	
	private CategoryDomain category;
		
	@Before
	public void setUp(){
		
		category = new CategoryDomain();
		category = new CategoryDomain
				.CategoryBuilder("", "", "")
				.categoryId("")
				.categoryName(new CategoryNameDomain("", ""))
				.parentCategory(new CategoryDomain())
				.programCode("")
				.dtCreated(new Date())
				.usrCreated("")
				.dtUpdated(new Date())
				.usrUpdated("")
				.build();
		
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(category.getProgramCode());
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
