package com.loyalty.marketplace.voucher.outbound.database.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BurntInfo.class)
@ActiveProfiles("unittest")
public class BurntInfoTest {
	
	private BurntInfo burntInfo;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		burntInfo = new BurntInfo();
		burntInfo.setBurnNotes("");
		burntInfo.setStoreCode("");
		burntInfo.setVoucherBurnt(false);
		burntInfo.setVoucherBurntDate(new Date());
		burntInfo.setVoucherBurntId("");
		burntInfo.setVoucherBurntUser("");
	}

	@Test
	public void testGetters() {
		assertNotNull(burntInfo.getBurnNotes());
		assertNotNull(burntInfo.getStoreCode());
		assertNotNull(burntInfo.getVoucherBurntDate());
		assertNotNull(burntInfo.getVoucherBurntId());
		assertNotNull(burntInfo.getVoucherBurntUser());
		assertNotNull(burntInfo.isVoucherBurnt());
	}
	
	@Test
	public void testToString() {
		assertNotNull(burntInfo.toString());
	}

}