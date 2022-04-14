package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoException;

@SpringBootTest(classes = BirthdayGiftTrackerDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayGiftTrackerDomainTest {
	
	@Mock
	ModelMapper modelMapper;

	@Mock
	RepositoryHelper repositoryHelper;
	
	@Mock
	AuditService auditService;
	
	@InjectMocks
	private BirthdayGiftTrackerDomain bdayGiftTrackerDomain = new BirthdayGiftTrackerDomain();
	
	private BirthdayGiftTrackerDomain birthdayGiftTrackerDomain;
	private BirthdayGiftTracker birthdayGiftTracker;
	private Headers header;
	
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		birthdayGiftTrackerDomain = new BirthdayGiftTrackerDomain
				.BirthdayGiftTrackerDomainBuilder("", "", new Date(), new Date())
				.programCode("program")
				.id("id")
				.createdAt(new Date())
				.createdBy("")
				.updatedAt(new Date())
				.updatedBy("")
				.build();
		birthdayGiftTracker = new BirthdayGiftTracker();
		birthdayGiftTracker.setId(birthdayGiftTrackerDomain.getId());
		header = new Headers("", "", "", "", "", "", "", "", "", "", "");
		
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(birthdayGiftTrackerDomain.getAccountNumber());
		assertNotNull(birthdayGiftTrackerDomain.getMembershipCode());
		assertNotNull(birthdayGiftTrackerDomain.getProgramCode());
		assertNotNull(birthdayGiftTrackerDomain.getLastViewedDate());
		assertNotNull(birthdayGiftTrackerDomain.getBirthDate());
		assertNotNull(birthdayGiftTrackerDomain.getId());
		assertNotNull(birthdayGiftTrackerDomain.getCreatedAt());
		assertNotNull(birthdayGiftTrackerDomain.getUpdatedAt());
		assertNotNull(birthdayGiftTrackerDomain.getCreatedBy());
		assertNotNull(birthdayGiftTrackerDomain.getUpdatedBy());
		
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayGiftTrackerDomain.toString());
	    
	}
	
	@Test
	public void testSaveBirthdayGiftTracker() throws MarketplaceException {
		
		when(modelMapper.map(birthdayGiftTrackerDomain, BirthdayGiftTracker.class)).thenReturn(birthdayGiftTracker);
		when(repositoryHelper.saveBirthdayTracker(Mockito.any(BirthdayGiftTracker.class))).thenReturn(birthdayGiftTracker);
		birthdayGiftTracker = bdayGiftTrackerDomain.saveUpdateBirthdayTracker(birthdayGiftTrackerDomain, null, OfferConstants.INSERT_ACTION.get(), header);
		assertNotNull(birthdayGiftTracker);
	    
	}
	
	@Test
	public void testUpdateBirthdayGiftTracker() throws MarketplaceException {
		
		when(modelMapper.map(birthdayGiftTrackerDomain, BirthdayGiftTracker.class)).thenReturn(birthdayGiftTracker);
		when(repositoryHelper.saveBirthdayTracker(Mockito.any(BirthdayGiftTracker.class))).thenReturn(birthdayGiftTracker);
		birthdayGiftTracker = bdayGiftTrackerDomain.saveUpdateBirthdayTracker(birthdayGiftTrackerDomain, birthdayGiftTracker, OfferConstants.UPDATE_ACTION.get(), header);
		assertNotNull(birthdayGiftTracker);
	    
	}
	
	@Test(expected = MarketplaceException.class)
	public void testSaveBirthdayGiftTrackerException() throws MarketplaceException {
		
		birthdayGiftTrackerDomain = new BirthdayGiftTrackerDomain();
		when(modelMapper.map(birthdayGiftTrackerDomain, BirthdayGiftTracker.class)).thenThrow(NullPointerException.class);
		birthdayGiftTracker = bdayGiftTrackerDomain.saveUpdateBirthdayTracker(birthdayGiftTrackerDomain, birthdayGiftTracker, OfferConstants.INSERT_ACTION.get(), header);
	    
	}
	
	@Test(expected = MarketplaceException.class)
	public void testUpdateBirthdayGiftTrackerException() throws MarketplaceException {
		
		when(modelMapper.map(birthdayGiftTrackerDomain, BirthdayGiftTracker.class)).thenThrow(NullPointerException.class);
		birthdayGiftTracker = bdayGiftTrackerDomain.saveUpdateBirthdayTracker(birthdayGiftTrackerDomain, birthdayGiftTracker, OfferConstants.UPDATE_ACTION.get(), header);
	    
	}
	
	@Test(expected = MarketplaceException.class)
	public void testSaveUpdateBirthdayGiftTrackerMongoException() throws MarketplaceException {
		
		when(modelMapper.map(birthdayGiftTrackerDomain, BirthdayGiftTracker.class)).thenThrow(MongoException.class);
		birthdayGiftTracker = bdayGiftTrackerDomain.saveUpdateBirthdayTracker(birthdayGiftTrackerDomain, birthdayGiftTracker, OfferConstants.INSERT_ACTION.get(), header);
	}
		
}
