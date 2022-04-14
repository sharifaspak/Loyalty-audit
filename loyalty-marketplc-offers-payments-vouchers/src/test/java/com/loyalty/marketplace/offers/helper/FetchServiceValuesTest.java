package com.loyalty.marketplace.offers.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.decision.manager.inbound.dto.CustomerSegmentDMRequestDto;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CustomerSegmentResult;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityConfigDto;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;
import com.loyalty.marketplace.offers.outbound.service.DecisionManagerService;
import com.loyalty.marketplace.offers.outbound.service.MemberActivityService;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.offers.outbound.service.PartnerService;
import com.loyalty.marketplace.offers.outbound.service.dto.IncludeMemberDetails;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes=FetchServiceValues.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class FetchServiceValuesTest {
	
	@Mock
	MemberManagementService memberManagementService;
	
	@Mock
	MemberActivityService memberActivityService;
	
	@Mock
	PartnerService partnerService;
	
	@Mock
	DecisionManagerService decisionManagerService;
		
	@InjectMocks
	FetchServiceValues fetchServiceValues;
	
	private ResultResponse resultResponse; 
	private Headers header;
	private String accountNumber;
	private String partnerCode;
	private String activityId;
	private boolean status;
	
	@Before
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
		resultResponse = new ResultResponse("");
		header = new Headers();
		accountNumber = "accountNumber";
		partnerCode = "partnerCode";
		activityId = "activityId";
		status = true;

   }
	
	@Test
	public void testGetAllCustomerTypes() throws MarketplaceException{
		
		List<ParentChlidCustomer> customerTypes = new ArrayList<>(1);
		when(memberManagementService.getAllCustomerTypesWithParent(resultResponse, header)).thenReturn(customerTypes);
		customerTypes = fetchServiceValues.getAllCustomerTypes(resultResponse, header);
		assertNotNull(customerTypes);		
	}
	
	@Test
	public void testGetMemberDetails() throws MarketplaceException{
		
		IncludeMemberDetails includeMemberDetails = new IncludeMemberDetails();
		GetMemberResponseDto memberResponseDto =  new GetMemberResponseDto();
		GetMemberResponse memberResponse = new GetMemberResponse();
		when(memberManagementService.getMemberDetails(accountNumber, includeMemberDetails, header, resultResponse)).thenReturn(memberResponseDto);
		memberResponse = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, header);
		assertNull(memberResponse);		
	}
	
	@Test
	public void testGetMemberDetailsForPayment() throws MarketplaceException{
		
		IncludeMemberDetails includeMemberDetails = new IncludeMemberDetails();
		GetMemberResponseDto memberResponseDto =  new GetMemberResponseDto();
		when(memberManagementService.getMemberDetails(accountNumber, includeMemberDetails, header, resultResponse)).thenReturn(memberResponseDto);
		memberResponseDto = fetchServiceValues.getMemberDetailsForPayment(accountNumber, includeMemberDetails, header, resultResponse);
		assertNotNull(memberResponseDto);		
	}
	
	@Test
	public void testCheckPartnerExists() throws MarketplaceException{
		
		when(partnerService.checkPartnerExists(partnerCode, header)).thenReturn(status);
		status = fetchServiceValues.checkPartnerExists(partnerCode, header);
		assertTrue(status);		
	}
	
	@Test
	public void testCheckCustomerSegment() throws MarketplaceException{
		
		CustomerSegmentDMRequestDto decisionManagerDto = new CustomerSegmentDMRequestDto();
		CustomerSegmentResult decisionManagerResult = new CustomerSegmentResult();
		when(decisionManagerService.checkCustomerSegment(decisionManagerDto, header, resultResponse)).thenReturn(decisionManagerResult);
		decisionManagerResult = fetchServiceValues.checkCustomerSegment(decisionManagerDto, header, resultResponse);
		assertNotNull(decisionManagerResult);		
	}
	
	@Test
	public void testPartnerActivity() throws MarketplaceException{
		
		PartnerActivityDto partnerActivityDto = new PartnerActivityDto();
		when(memberActivityService.createPartnerActivity(partnerCode, partnerActivityDto, header, resultResponse)).thenReturn(activityId);
		activityId = fetchServiceValues.partnerActivity(partnerCode, partnerActivityDto, resultResponse, header);
		assertNotNull(activityId);		
	}
	
	@Test
	public void testUpdatePartnerActivity() throws MarketplaceException{
		
		PartnerActivityConfigDto partnerActivityDto = new PartnerActivityConfigDto();
		when(memberActivityService.updatePartnerActivity(partnerCode, activityId, partnerActivityDto, header, resultResponse)).thenReturn(status);
		status = fetchServiceValues.updatePartnerActivity(partnerCode, activityId, partnerActivityDto, resultResponse, header);
		assertNotNull(status);		
	}
	
	@Test
	public void testGetAllProgramActivity() throws MarketplaceException{
		
		List<ProgramActivityWithIdDto> programActivityList = new ArrayList<>(1);
		when(memberActivityService.getProgramActivityList(header, resultResponse)).thenReturn(programActivityList);
		programActivityList = fetchServiceValues.getAllProgramActivity(header, resultResponse);
		assertNotNull(programActivityList);		
	}
	
	@Test
	public void testGetBirthdayAccountDetails() throws MarketplaceException{
		
		String days ="2";
		List<BirthdayAccountsDto> birthayAccounts = new ArrayList<>(1);
		when(memberManagementService.getBirthdayAccountDetails(days, header)).thenReturn(birthayAccounts);
		birthayAccounts = fetchServiceValues.getBirthdayAccountDetails(days, header);
		assertNotNull(birthayAccounts);		
	}
	
	
	
	
	
	
}
