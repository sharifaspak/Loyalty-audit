package com.loyalty.marketplace.offers.helper.dto;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;

@SpringBootTest(classes = ExceptionInfo.class)
@ActiveProfiles("unittest")
public class ExceptionInfoTest {

	private ExceptionInfo exceptionInfo;
	
	@Before
	public void setUp(){
		exceptionInfo = new ExceptionInfo();
		exceptionInfo = new ExceptionInfo("", "", null, null, null, null, null);
		exceptionInfo.setCurrentClassName("");
		exceptionInfo.setCurrentMethodName("");
		exceptionInfo.setErrorResult(OfferErrorCodes.ACCOUNT_NOT_AVAILABLE);
		exceptionInfo.setException(new Exception());
		exceptionInfo.setExceptionError(OfferExceptionCodes.ADD_TO_WISHLIST_DOMAIN_RUNTIME_EXCEPTION);
		exceptionInfo.setMarketplaceError(OfferErrorCodes.ACCOUNT_NUMBER_REQUIRED);
		exceptionInfo.setMarketplaceException(null);
	}
	
	@Test
	public void testGetters() {
		
		assertNotNull(exceptionInfo.getCurrentClassName());
		assertNotNull(exceptionInfo.getCurrentMethodName());
		assertNotNull(exceptionInfo.getErrorResult());
		assertNotNull(exceptionInfo.getException());
		assertNotNull(exceptionInfo.getExceptionError());
		assertNotNull(exceptionInfo.getMarketplaceError());
		assertNull(exceptionInfo.getMarketplaceException());
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(exceptionInfo.toString());
	}
	
}
