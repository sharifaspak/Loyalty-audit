package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.OfferRatingDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@SpringBootTest(classes = OfferRatingDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferRatingDomainTest {
	
	@Mock
	Validator validator;

	@Mock
	ProgramManagement programManagement;

	@Mock
	ModelMapper modelMapper;

	@Mock
	AuditService auditService;
	
	@Mock
	FetchServiceValues fetchServiceValues;

	@Mock
	RepositoryHelper repositoryHelper;
		
	@Mock
	OfferCatalogDomain offerCatalogDomain;
	
	@InjectMocks
	private OfferRatingDomain ratingDomain = new OfferRatingDomain();
	
	private OfferRatingDomain offerRatingDomain;
	private ResultResponse resultResponse;
	private Headers header;
	private GetMemberResponse memberDetails;
	private String accountNumber;
//	private OfferRating offerRating;
	private OfferRatingDto offerRatingDto;
	
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		accountNumber = "accountNumber";
		
		offerRatingDomain = new OfferRatingDomain
				.OfferRatingBuilder("")
				.build();
		
		offerRatingDomain = new OfferRatingDomain
				.OfferRatingBuilder("", new ArrayList<>(), 0.0)
				.id("id")
				.programCode("")
				.createdDate(new Date())
				.createdUser("")
				.updatedDate(new Date())
				.updatedUser("")
				.ratingCount(0)
				.commentCount(0)
				.build();
		
		memberDetails = new GetMemberResponse();
		memberDetails.setAccountNumber(accountNumber);
		memberDetails.setMembershipCode("membershipCode");
		memberDetails.setFirstName("firstName");
		memberDetails.setLastName("lastName");
		memberDetails.setDob(new Date());
		
		offerRatingDto = new OfferRatingDto();
		offerRatingDto.setOfferId("");
		offerRatingDto.setAccountNumber("");
		offerRatingDto.setRating(0);
		offerRatingDto.setComment("");
		
		header = new Headers("", "", "", "", "", "", "", "", "", "", "");
		resultResponse = new ResultResponse("");
//		offerRating = new OfferRating();
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(offerRatingDomain.getAverageRating());
		assertNotNull(offerRatingDomain.getOfferId());
		assertNotNull(offerRatingDomain.getCommentCount());
		assertNotNull(offerRatingDomain.getCreatedDate());
		assertNotNull(offerRatingDomain.getCreatedUser());
		assertNotNull(offerRatingDomain.getUpdatedDate());
		assertNotNull(offerRatingDomain.getUpdatedUser());
		assertNotNull(offerRatingDomain.getId());
		assertNotNull(offerRatingDomain.getRatingCount());
		assertNotNull(offerRatingDomain.getCommentCount());
		assertNotNull(offerRatingDomain.getMemberRatings());
			
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(offerRatingDomain.toString());
	    
	}
	
	@Test
	public void testValidateAndSaveOfferRating() {
		
		resultResponse = ratingDomain.validateAndSaveOfferRating(offerRatingDto, header);
		assertNotNull(resultResponse);
	}
		
}
