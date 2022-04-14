package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.error.handler.ErrorRecords;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes = OfferCounterDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferCounterDomainTest {
	
	@Mock
	ModelMapper modelMapper;
	
	@Mock
	AuditService auditService;
	
	@Mock
	RepositoryHelper repositoryHelper;
	
	@Mock
	ErrorRecords errorRecords;

	@InjectMocks
	private OfferCounterDomain counterDomain = new OfferCounterDomain();

	private Headers header;
	private OfferCounter offerCounter;
	private OfferCounterDomain offerCounterDomain;
	
	@Before
	public void setUp() throws ParseException {

		MockitoAnnotations.initMocks(this);
		offerCounterDomain = new OfferCounterDomain.OfferCounterBuilder("", 0, 0, 0,
				0, 0).offerId("").dailyCount(0).weeklyCount(0)
						.monthlyCount(0).annualCount(0).totalCount(0)
						.denominationCount(new ArrayList<>())
						.accountOfferCount(new ArrayList<>())
						.memberOfferCount(new ArrayList<>())
						.lastPurchased(new Date())
						.build();
		offerCounter = new OfferCounter();
		header = new Headers("", "", "", "", "", "", "", "", "", "", "");
		
	}

	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {

		assertNull(offerCounterDomain.getId());
		assertNotNull(offerCounterDomain.getOfferId());
		assertNotNull(offerCounterDomain.getDailyCount());
		assertNotNull(offerCounterDomain.getWeeklyCount());
		assertNotNull(offerCounterDomain.getMonthlyCount());
		assertNotNull(offerCounterDomain.getAnnualCount());
		assertNotNull(offerCounterDomain.getTotalCount());
		assertNotNull(offerCounterDomain.getLastPurchased());
		assertNotNull(offerCounterDomain.getDenominationCount());
		assertNotNull(offerCounterDomain.getAccountOfferCount());
		assertNotNull(offerCounterDomain.getMemberOfferCount());

	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		assertNotNull(offerCounterDomain.toString());
	}
	
	
	@Test
	public void testSaveOfferCounter() throws MarketplaceException {
		
		when(modelMapper.map(offerCounterDomain, OfferCounter.class)).thenReturn(offerCounter);
		when(repositoryHelper.saveOfferCounter(Mockito.any(OfferCounter.class))).thenReturn(offerCounter);
		offerCounter = counterDomain.saveUpdateOfferCounter(offerCounterDomain, null, OfferConstants.INSERT_ACTION.get(), header, OffersRequestMappingConstants.PURCHASE);
		assertNotNull(offerCounter);
	}
	
	@Test
	public void testUpdateOfferCounter() throws MarketplaceException {
		
		when(modelMapper.map(offerCounterDomain, OfferCounter.class)).thenReturn(offerCounter);
		when(repositoryHelper.saveOfferCounter(Mockito.any(OfferCounter.class))).thenReturn(offerCounter);
		offerCounter = counterDomain.saveUpdateOfferCounter(offerCounterDomain, offerCounter, OfferConstants.UPDATE_ACTION.get(), header, OffersRequestMappingConstants.PURCHASE);
		assertNotNull(offerCounter);
	}

	@Test
	public void testSaveUpdateOfferCounterException() throws MarketplaceException {
		
		offerCounter = counterDomain.saveUpdateOfferCounter(offerCounterDomain, null, OfferConstants.INSERT_ACTION.get(), null, OffersRequestMappingConstants.PURCHASE);
		assertNull(offerCounter);
	}
	
	

}
