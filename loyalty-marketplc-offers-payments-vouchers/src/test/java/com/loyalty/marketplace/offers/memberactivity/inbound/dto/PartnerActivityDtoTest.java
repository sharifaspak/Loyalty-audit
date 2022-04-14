package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityTypes;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ChainedActivity;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.PartnerActivityCode;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.RateType;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ThresholdCap;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.TierBonus;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;

@SpringBootTest(classes = PartnerActivityDto.class)
@ActiveProfiles("unittest")
public class PartnerActivityDtoTest {

	private PartnerActivityDto partnerActivityDto;
	
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		partnerActivityDto = new PartnerActivityDto();
		partnerActivityDto.setLoyaltyActivity(new ProgramActivityWithIdDto());
		partnerActivityDto.setActivityCode(new PartnerActivityCode());
		partnerActivityDto.setBaseRate(100.25);
		partnerActivityDto.setStartDate(new Date());
		partnerActivityDto.setEndDate(new Date());
		partnerActivityDto.setTierPointsFlag(true);
		partnerActivityDto.setTierPointRate(100.25);
		partnerActivityDto.setActivityTypeCatalogue(new EventTypeCatalogueDTO());
		partnerActivityDto.setNumberType(new ArrayList<NumberTypeDto>());
		partnerActivityDto.setPointsExpiryPeriod(100L);
		partnerActivityDto.setActivityType(new ActivityTypes());
		partnerActivityDto.setRateType(new RateType());
		partnerActivityDto.setCustomerType(new ArrayList<CustomerTypeDto>());
		partnerActivityDto.setTierBonus(new ArrayList<TierBonus>());
		partnerActivityDto.setChainedActivity(new ArrayList<ChainedActivity>());
		partnerActivityDto.setThresholdCap(new ArrayList<ThresholdCap>());
		
	}
	

	@Test
	public void testGetters() {
		assertNotNull(partnerActivityDto.getLoyaltyActivity());
		assertNotNull(partnerActivityDto.getActivityCode());
		assertNotNull(partnerActivityDto.getBaseRate());
		assertNotNull(partnerActivityDto.getStartDate());
		assertNotNull(partnerActivityDto.getEndDate());
		assertNotNull(partnerActivityDto.isTierPointsFlag());
		assertNotNull(partnerActivityDto.getTierPointRate());
		assertNotNull(partnerActivityDto.getActivityTypeCatalogue());
		assertNotNull(partnerActivityDto.getNumberType());
		assertNotNull(partnerActivityDto.getPointsExpiryPeriod());
		assertNotNull(partnerActivityDto.getActivityType());
		assertNotNull(partnerActivityDto.getRateType());
		assertNotNull(partnerActivityDto.getCustomerType());
		assertNotNull(partnerActivityDto.getTierBonus());
		assertNotNull(partnerActivityDto.getChainedActivity());
		assertNotNull(partnerActivityDto.getThresholdCap());
		
	}

	
	@Test
	public void testToString() {
		assertNotNull(partnerActivityDto.toString());
	}
	
}