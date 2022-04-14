package com.loyalty.marketplace.voucher.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PurchaseDetailsDomain.class)
@ActiveProfiles("unittest")
public class PurchaseDetailsDomainTest {

	private PurchaseDetailsDomain purchaseDetailsDomain;

	@Before
	public void setUp() throws Exception {

		PurchaseDetailsDomain.PurchaseDetailsBuilder purchaseDetailsBuilder = new PurchaseDetailsDomain.PurchaseDetailsBuilder("",
				"", "", "", "", "", 0.0);
		purchaseDetailsDomain = purchaseDetailsBuilder.authorizationCode("").cardExpiryDate("").cardNumber("").cardSubType("").cardToken("").
				cardType("").spentAmount(0.0).build();

		purchaseDetailsDomain = new PurchaseDetailsDomain(purchaseDetailsBuilder);

	}

	@Test
	public void testGetters() {
		assertNotNull(purchaseDetailsDomain.getAuthorizationCode());
		assertNotNull(purchaseDetailsDomain.getCardExpiryDate());
		assertNotNull(purchaseDetailsDomain.getCardNumber());
		assertNotNull(purchaseDetailsDomain.getCardSubType());
		assertNotNull(purchaseDetailsDomain.getCardToken());
		assertNotNull(purchaseDetailsDomain.getCardType());
		assertNotNull(purchaseDetailsDomain.getSpentAmount());	

	}
	
	@Test
	public void testBuilder() {
		purchaseDetailsDomain = new PurchaseDetailsDomain();
		assertNotNull(purchaseDetailsDomain);
	}

	@Test
	public void testToString() {
		assertNotNull(purchaseDetailsDomain.toString());
	}

}
