package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = LimitDto.class)
@ActiveProfiles("unittest")
public class LimitDtoTest {

	private LimitDto limitDto;

	@Before
	public void setUp() {
		limitDto = new LimitDto();
		limitDto.setCustomerSegment("");
		limitDto.setCouponQuantity(0);
		limitDto.setDailyLimit(0);
		limitDto.setWeeklyLimit(0);
		limitDto.setMonthlyLimit(0);
		limitDto.setAnnualLimit(0);
		limitDto.setDownloadLimit(0);
		limitDto.setDenominationLimit(new ArrayList<DenominationLimitDto>(1));
		limitDto.setAccountDailyLimit(0);
		limitDto.setAccountWeeklyLimit(0);
		limitDto.setAccountMonthlyLimit(0);
		limitDto.setAccountAnnualLimit(0);
		limitDto.setAccountTotalLimit(0);
		limitDto.setAccountDenominationLimit(new ArrayList<DenominationLimitDto>());
		limitDto.setMemberDailyLimit(0);
		limitDto.setMemberWeeklyLimit(0);
		limitDto.setMemberMonthlyLimit(0);
		limitDto.setMemberAnnualLimit(0);
		limitDto.setMemberTotalLimit(0);
		limitDto.setMemberDenominationLimit(new ArrayList<DenominationLimitDto>());

	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		assertNotNull(limitDto.getCustomerSegment());
		assertNotNull(limitDto.getCouponQuantity());
		assertNotNull(limitDto.getDailyLimit());
		assertNotNull(limitDto.getWeeklyLimit());
		assertNotNull(limitDto.getMonthlyLimit());
		assertNotNull(limitDto.getAnnualLimit());
		assertNotNull(limitDto.getDownloadLimit());
		assertNotNull(limitDto.getDenominationLimit());
		assertNotNull(limitDto.getAccountDailyLimit());
		assertNotNull(limitDto.getAccountWeeklyLimit());
		assertNotNull(limitDto.getAccountMonthlyLimit());
		assertNotNull(limitDto.getAccountAnnualLimit());
		assertNotNull(limitDto.getAccountTotalLimit());
		assertNotNull(limitDto.getAccountDenominationLimit());
		assertNotNull(limitDto.getMemberDailyLimit());
		assertNotNull(limitDto.getMemberWeeklyLimit());
		assertNotNull(limitDto.getMemberMonthlyLimit());
		assertNotNull(limitDto.getMemberAnnualLimit());
		assertNotNull(limitDto.getMemberTotalLimit());
		assertNotNull(limitDto.getMemberDenominationLimit());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */

	@Test
	public void testToString() {
		assertNotNull(limitDto.toString());
	}

}
