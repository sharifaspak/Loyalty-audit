package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes=VoucherInfo.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class VoucherInfoTest {
	
	private VoucherInfo voucherInfo;
		
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		voucherInfo = new VoucherInfo();
		voucherInfo.setVoucherExpiryDate(new Date());
		voucherInfo.setVoucherAction("1");
		voucherInfo.setVoucherAmount(0.0);
		voucherInfo.setVoucherExpiryPeriod(0);
				
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		 assertNotNull(voucherInfo.getVoucherExpiryDate());
		assertNotNull(voucherInfo.getVoucherAction());
	    assertNotNull(voucherInfo.getVoucherAmount());
	    assertNotNull(voucherInfo.getVoucherExpiryPeriod());
			
	}
	
	/**
	 * 
	 * Test toString method
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(voucherInfo.toString());
	}
		
}
