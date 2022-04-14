package com.loyalty.marketplace.offers.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.validation.Validator;

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

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.BirthdayInfoRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.dto.BirthdayInfoResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.BirthdayInfoResultResponse;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoException;

@SpringBootTest(classes = BirthdayInfoDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class BirthdayInfoDomainTest {
	
	@Mock
	ModelMapper modelMapper;

	@Mock
	RepositoryHelper repositoryHelper;
	
	@Mock
	OffersHelper helper;
	
	@Mock
	AuditService auditService;
	
	@Mock
	ProgramManagement programManagement;
	
	@Mock
	Validator validator;
	
	@Mock
	FetchServiceValues fetchServiceValues;
	
	@InjectMocks
	private BirthdayInfoDomain bdayInfoDomain = new BirthdayInfoDomain();
	
	
	private BirthdayInfoDomain birthdayInfoDomain;
	private Headers header;
	private BirthdayInfo birthdayInfo;
	private ResultResponse resultResponse;
	private BirthdayInfoResultResponse birthdayResultResponse;
	private BirthdayInfoRequestDto birthdayInfoRequestDto;
	private GetMemberResponse memberDetails;
	private String accountNumber;
	
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		accountNumber = "accountNumber";
		birthdayInfoDomain = new BirthdayInfoDomain
				.BirthdayInfoBuilder(0, 0, 0, 0)
				.id("")
				.programCode("programCode")
				.title(new BirthdayTitleDomain())
				.subTitle(new BirthdaySubTitleDomain())
				.description(new BirthdayDescriptionDomain())
				.weekIcon(new BirthdayWeekIconDomain())
				.iconText(new BirthdayIconTextDomain())
				.createdDate(new Date())
				.createdUser("")
				.updatedDate(new Date())
				.updatedUser("")
				.build();
		

		birthdayInfoRequestDto = new BirthdayInfoRequestDto();
		birthdayInfoRequestDto.setTitleEn("");
		birthdayInfoRequestDto.setTitleAr("");
		birthdayInfoRequestDto.setSubTitleEn("");
		birthdayInfoRequestDto.setSubTitleAr("");
		birthdayInfoRequestDto.setDescriptionEn("");
		birthdayInfoRequestDto.setDescriptionAr("");
		birthdayInfoRequestDto.setIconTextEn("");
		birthdayInfoRequestDto.setIconTextAr("");
		birthdayInfoRequestDto.setWeekIconEn("");
		birthdayInfoRequestDto.setWeekIconAr("");
		birthdayInfoRequestDto.setPurchaseLimit(0);
		birthdayInfoRequestDto.setThresholdPlusValue(0);
		birthdayInfoRequestDto.setThresholdMinusValue(0);
		birthdayInfoRequestDto.setDisplayLimit(0);
		
		memberDetails = new GetMemberResponse();
		memberDetails.setAccountNumber(accountNumber);
		memberDetails.setMembershipCode("membershipCode");
		memberDetails.setFirstName("firstName");
		memberDetails.setLastName("lastName");
		memberDetails.setDob(new Date());
		
		birthdayInfo = new BirthdayInfo();
		header = new Headers("", "", "", "", "", "", "", "", "", "", "");
		resultResponse = new ResultResponse("");
		birthdayResultResponse = new BirthdayInfoResultResponse("");
	}
	
	/**
	 * 
	 * Test Getters
	 * 
	 */
	@Test
	public void testGetters() {
		
		assertNotNull(birthdayInfoDomain.getId());
		assertNotNull(birthdayInfoDomain.getProgramCode());
		assertNotNull(birthdayInfoDomain.getTitle());
		assertNotNull(birthdayInfoDomain.getSubTitle());
		assertNotNull(birthdayInfoDomain.getDescription());
		assertNotNull(birthdayInfoDomain.getIconText());
		assertNotNull(birthdayInfoDomain.getWeekIcon());
		assertNotNull(birthdayInfoDomain.getPurchaseLimit());
		assertNotNull(birthdayInfoDomain.getDisplayLimit());
		assertNotNull(birthdayInfoDomain.getThresholdMinusValue());
		assertNotNull(birthdayInfoDomain.getThresholdPlusValue());
		assertNotNull(birthdayInfoDomain.getCreatedDate());
		assertNotNull(birthdayInfoDomain.getUpdatedDate());
		assertNotNull(birthdayInfoDomain.getCreatedUser());
		assertNotNull(birthdayInfoDomain.getUpdatedUser());
		
	}
	
	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {
		
		assertNotNull(birthdayInfoDomain.toString());
	    
	}
	

	@Test(expected=MarketplaceException.class)
	public void testSaveUpdateBirthdayInfoException() throws MarketplaceException {
		
		birthdayInfo = bdayInfoDomain.saveUpdateBirthdayInfoDomain(birthdayInfoDomain, birthdayInfo, OfferConstants.UPDATE_ACTION.get(), null);
	}
	
	@Test(expected=MarketplaceException.class)
	public void testSaveUpdateBirthdayInfoExceptionInsert() throws MarketplaceException {
		
		birthdayInfoDomain = new BirthdayInfoDomain();
		birthdayInfo = bdayInfoDomain.saveUpdateBirthdayInfoDomain(birthdayInfoDomain, null, OfferConstants.INSERT_ACTION.get(), null);
	}
	
	@Test
	public void testConfigureBirthdayInfoSaveInfo() throws MarketplaceException {
		
		resultResponse = bdayInfoDomain.configureBirthdayInfo(birthdayInfoRequestDto, header);
		assertNotNull(resultResponse);
	}
	
	@Test
	public void testConfigureBirthdayInfoUpdateInfo() throws MarketplaceException {
	  
		when(repositoryHelper.findBirthdayInfo()).thenReturn(birthdayInfo);
		resultResponse = bdayInfoDomain.configureBirthdayInfo(birthdayInfoRequestDto, header);
		assertNotNull(resultResponse);
	}
	
	@Test
	public void testConfigureBirthdayInfoMarketplaceException() throws MarketplaceException {
	  
		when(repositoryHelper.findBirthdayInfo()).thenReturn(birthdayInfo);
		when(modelMapper.map(Mockito.any(BirthdayInfoDomain.class), Mockito.any())).thenThrow(MongoException.class);
		resultResponse = bdayInfoDomain.configureBirthdayInfo(birthdayInfoRequestDto, header);
		assertNotNull(resultResponse);
	}
	
	@Test
	public void testConfigureBirthdayInfoException() throws MarketplaceException {
	  
		when(repositoryHelper.findBirthdayInfo()).thenThrow(NullPointerException.class);
		resultResponse = bdayInfoDomain.configureBirthdayInfo(birthdayInfoRequestDto, header);
		assertNotNull(resultResponse);
	}
	
	@Test
	public void testGetBirthdayInfo() throws MarketplaceException {
        //Complete this....birthday eligibility not coming true 	  
		Mockito.doReturn(memberDetails).when(fetchServiceValues).getMemberDetails(Mockito.any(String.class), Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		when(repositoryHelper.findBirthdayInfo()).thenReturn(birthdayInfo);
		Mockito.doReturn(true).when(helper).checkMembershipEligbilityForOffers(Mockito.any(), Mockito.any(), Mockito.any());
		when(modelMapper.map(birthdayInfo, BirthdayInfoResponseDto.class)).thenReturn(new BirthdayInfoResponseDto());
		birthdayResultResponse = bdayInfoDomain.getBirthdayInfoForAccount(accountNumber, header);
		assertNotNull(birthdayResultResponse);
	}
	
	@Test
	public void testGetBirthdayInfoException() throws MarketplaceException {
         	  
		Mockito.doReturn(memberDetails).when(fetchServiceValues).getMemberDetails(Mockito.any(String.class), Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		when(repositoryHelper.findBirthdayInfo()).thenThrow(NullPointerException.class);
		birthdayResultResponse = bdayInfoDomain.getBirthdayInfoForAccount(accountNumber, header);
		assertNotNull(birthdayResultResponse);
	}
	
	@Test
	public void testGetBirthdayInfoMarketplaceException() throws MarketplaceException {
	  
		Mockito.doThrow(MarketplaceException.class).when(fetchServiceValues).getMemberDetails(Mockito.any(String.class), Mockito.any(ResultResponse.class), Mockito.any(Headers.class));
		birthdayResultResponse = bdayInfoDomain.getBirthdayInfoForAccount(accountNumber, header);
		assertNotNull(birthdayResultResponse);
	}
	
	
	
		
}
