package com.loyalty.marketplace.interest.restcontroller;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.interest.domain.model.InterestDomain;
import com.loyalty.marketplace.interest.inbound.dto.InterestIdRequestDto;
import com.loyalty.marketplace.interest.inbound.dto.InterestRequestDto;
import com.loyalty.marketplace.interest.inbound.restcontroller.LoyaltyCustomerInterestController;
import com.loyalty.marketplace.interest.outbound.dto.CategoriesInterestDto;
import com.loyalty.marketplace.interest.outbound.dto.InterestResponseResult;
import com.loyalty.marketplace.interest.repository.CategotyRepository;
import com.loyalty.marketplace.interest.repository.CustomerInterest;
import com.loyalty.marketplace.interest.repository.InterestRepository;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.CategoryDto;
import com.loyalty.marketplace.offers.inbound.dto.CategoryRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.DenominationDto;
import com.loyalty.marketplace.offers.inbound.dto.DenominationRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodRequestDto;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.DenominationDescription;
import com.loyalty.marketplace.outbound.database.entity.DenominationValue;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

@SpringBootTest(classes = LoyaltyCustomerInterestController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class LoyaltyinterestCalculatorControllerTest {
	@Mock
	ModelMapper modelMapper;
    @Mock
 	InterestRepository interestRepo;
    @Mock
 	CustomerInterest customerInterestRepo;
    @Mock
 	CategotyRepository categotyRepo;
    @Mock
    InterestDomain interestDomain;
    @InjectMocks
    LoyaltyCustomerInterestController loyaltyinterestCalculatorController;
    
    private String program;
	private String authorization;
	private String externalTransactionId;
	private String userName;
	private String sessionId;
	private String userPrev;
	private String channelId;
	private String systemId;
	private String systemPassword;
	private String token;
	private String transactionId;
	@Before
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
		
		program = "Smiles";
		authorization = "authorization";
		externalTransactionId = "externalTransactionId";
		userName = "userName";
		sessionId = "sessionId";
		userPrev = "userPrev";
		channelId = "channelId";
		systemId = "systemId";
		systemPassword = "systemPassword";
		token = "token";
		transactionId = "transactionId";
	}
    @Test
    public void testGetInterest() throws Exception{
    	List<CategoriesInterestDto> categoriesInterestList = new ArrayList<>();
    	CategoriesInterestDto categoriesInterestDto = new CategoriesInterestDto();
    	categoriesInterestDto.setInterestId("12345");
    	categoriesInterestDto.setInterestImageUrl("Test");
    	categoriesInterestDto.setInterestName("Test");
    	categoriesInterestDto.setInterestSelected(true);
    	CategoriesInterestDto categoriesInterestDto1 = new CategoriesInterestDto();
    	categoriesInterestDto.setInterestId("123456");
    	categoriesInterestDto.setInterestImageUrl("Test");
    	categoriesInterestDto.setInterestName("Test");
    	categoriesInterestDto.setInterestSelected(true);
    	categoriesInterestList.add(categoriesInterestDto);
    	categoriesInterestList.add(categoriesInterestDto1);
    	String accountNumber = "12345";
    	String externalTransactionId = "12345";
    	InterestResponseResult interestResultResponse = new InterestResponseResult(externalTransactionId);
    	interestResultResponse.setResult(categoriesInterestList);
//    	Mockito.when(interestDomain.getInterestDetails(Mockito.any(String.class),Mockito.any(String.class))).thenReturn(interestResultResponse);
    	interestResultResponse = loyaltyinterestCalculatorController.getInterest(program,authorization,externalTransactionId,channelId,systemId,systemPassword,token,userName,accountNumber);
		assertNotNull(interestResultResponse);
    }
    @Test
    public void testUpdateInterest() throws Exception{
    	
    	  String accountNumber = "12345";
    	  InterestIdRequestDto interestIdRequestDto = new InterestIdRequestDto();
    	  interestIdRequestDto.setInterestId("123");
    	  List<InterestIdRequestDto> selectedInterests = new ArrayList<>();
    	  InterestRequestDto interestRequestDto = new InterestRequestDto();
    	  interestRequestDto.setSelectedInterests(selectedInterests);
    	  InterestResponseResult interestResultResponse = new InterestResponseResult(externalTransactionId);
		  Mockito.when(interestDomain.updateInterest(Mockito.any(String.class),Mockito.any(InterestRequestDto.class),Mockito.any(String.class),Mockito.any(String.class),
				  Mockito.any(Headers.class))).thenReturn(interestResultResponse);
		  interestResultResponse = loyaltyinterestCalculatorController.updateInterest(program,authorization,externalTransactionId,channelId,systemId,systemPassword,token,userName,accountNumber,interestRequestDto);
		  assertNotNull(interestResultResponse);
    }
    
    
}
