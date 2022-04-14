package com.loyalty.marketplace.voucher.outbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AccountWithActiveVouchersResult.class)
@ActiveProfiles("unittest")
public class AccountWithActiveVouchersResultTest {

	private AccountWithActiveVouchersResult accountWithActiveVouchersResult;

	private AccountWithActiveVouchersResult accountWithActiveVouchersResult1 = new AccountWithActiveVouchersResult();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		accountWithActiveVouchersResult.setAccountNumber("");
		accountWithActiveVouchersResult.setActive(false);
		 
	}

	@Test
	public void testGetters() {
		assertNotNull(accountWithActiveVouchersResult.getAccountNumber());
		assertNotNull(accountWithActiveVouchersResult.isActive());
	}

	@Test
	public void testToString() {
		assertNotNull(accountWithActiveVouchersResult.toString());
	}
	
	@Test
	public void testNoArgsConstructor() {
		assertNotNull(accountWithActiveVouchersResult1);
	}

}