package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.WishlistRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@SpringBootTest(classes = WishlistDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class WishlistDomainTest {
	
	
	private WishlistDomain wlDomain = new WishlistDomain();
	
	private WishlistDomain wishlistDomain;
	private ResultResponse resultResponse;
	private Headers header;
	private GetMemberResponse memberDetails;
	private String accountNumber;
	private WishlistRequestDto wishlistDto;
	//private WishlistEntity wishlist;
	
	@Before
	public void setUp(){
		
		wishlistDomain = new WishlistDomain
				.WishlistBuilder("", "", new ArrayList<>())
				.accountNumber("")
				.membershipCode("")
				.offers(new ArrayList<>())
				.dtCreated(new Date())
				.usrCreated("")
				.dtUpdated(new Date())
				.usrUpdated("")
				.build();
		
		memberDetails = new GetMemberResponse();
		memberDetails.setAccountNumber(accountNumber);
		memberDetails.setMembershipCode("membershipCode");
		memberDetails.setFirstName("firstName");
		memberDetails.setLastName("lastName");
		memberDetails.setDob(new Date());
		
		wishlistDto = new WishlistRequestDto();
		wishlistDto.setOfferId("");
		wishlistDto.setAction("");
		
		header = new Headers("", "", "", "", "", "", "", "", "", "", "");
		resultResponse = new ResultResponse("");
		//wishlist = new WishlistEntity();
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(wishlistDomain.getAccountNumber());
		assertNotNull(wishlistDomain.getMembershipCode());
		assertNotNull(wishlistDomain.getOffers());
		assertNotNull(wishlistDomain.getDtCreated());
		assertNotNull(wishlistDomain.getDtUpdated());
		assertNotNull(wishlistDomain.getUsrCreated());
		assertNotNull(wishlistDomain.getUsrUpdated());
		assertNotNull(wishlistDomain.getProgram());
		assertNull(wishlistDomain.getId());
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(wishlistDomain.toString());
	    
	}
	
	@Test
	public void testValidateAndSaveOfferToWishlist() {
		
		resultResponse = wlDomain.validateAndSaveOfferToWishlist(wishlistDto, accountNumber, header);
		assertNotNull(resultResponse);
	    
	}
		
}
