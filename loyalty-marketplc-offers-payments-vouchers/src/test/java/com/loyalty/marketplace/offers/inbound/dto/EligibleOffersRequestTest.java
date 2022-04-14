package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EligibleOffersRequest.class)
@ActiveProfiles("unittest")
public class EligibleOffersRequestTest {

	private EligibleOffersRequest eligibleOffersRequestDto;

	@Before
	public void setUp() {
		eligibleOffersRequestDto = new EligibleOffersRequest();
		eligibleOffersRequestDto.setFilterFlag("");
		eligibleOffersRequestDto.setAccountNumber("");
		eligibleOffersRequestDto.setCategoryId("");
		eligibleOffersRequestDto.setSubCategoryId("");
		eligibleOffersRequestDto.setKeywords("");
		eligibleOffersRequestDto.setMerchantCode("");
		eligibleOffersRequestDto.setPage(0);
		eligibleOffersRequestDto.setPageLimit(0);

	}

	@Test
	public void testGetters() {
		assertNotNull(eligibleOffersRequestDto.getFilterFlag());
		assertNotNull(eligibleOffersRequestDto.getAccountNumber());
		assertNotNull(eligibleOffersRequestDto.getCategoryId());
		assertNotNull(eligibleOffersRequestDto.getSubCategoryId());
		assertNotNull(eligibleOffersRequestDto.getKeywords());
		assertNotNull(eligibleOffersRequestDto.getMerchantCode());
		assertNotNull(eligibleOffersRequestDto.getPage());
		assertNotNull(eligibleOffersRequestDto.getPageLimit());
	}

	@Test
	public void testToString() {
		assertNotNull(eligibleOffersRequestDto.toString());
	}

}
