package com.loyalty.marketplace.offers.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

@SpringBootTest(classes=PurchasePaymentMethod.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PurchasePaymentMethodTest {

	private String purchaseItemId;
	private String programCode;
	private String purchaseItem;
	private List<PaymentMethod> paymentMethods;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	@InjectMocks
	PurchasePaymentMethod PurchasePaymentMethodTest;
	
	private PurchasePaymentMethod purchasePaymentMethod;
		
	@Before
	public void setUp(){
		
		purchaseItemId = "purchaseItemId";
		programCode="programCode";
		purchaseItem = "purchaseItem";
		paymentMethods = new ArrayList<>();
		usrCreated = "usrCreated";
		usrUpdated = "usrUpdated";
		dtCreated = new Date();
		dtUpdated = new Date();
		
		MockitoAnnotations.initMocks(this);
		purchasePaymentMethod = new PurchasePaymentMethod();
		purchasePaymentMethod.setPurchaseItemId(purchaseItemId);
		purchasePaymentMethod.setProgramCode(programCode);
		purchasePaymentMethod.setPurchaseItem(purchaseItem);
		purchasePaymentMethod.setPaymentMethods(paymentMethods);
		purchasePaymentMethod.setDtCreated(dtCreated);
		purchasePaymentMethod.setDtUpdated(dtUpdated);
		purchasePaymentMethod.setUsrCreated(usrCreated);
		purchasePaymentMethod.setUsrUpdated(usrUpdated);
		
		
				
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNotNull(purchasePaymentMethod.getPurchaseItemId());
		assertNotNull(purchasePaymentMethod.getProgramCode());
		assertNotNull(purchasePaymentMethod.getPurchaseItem());
		assertNotNull(purchasePaymentMethod.getPaymentMethods());
		assertNotNull(purchasePaymentMethod.getDtCreated());
		assertNotNull(purchasePaymentMethod.getDtUpdated());
		assertNotNull(purchasePaymentMethod.getUsrCreated());
		assertNotNull(purchasePaymentMethod.getUsrUpdated());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(purchasePaymentMethod.toString());

	}
	
}
