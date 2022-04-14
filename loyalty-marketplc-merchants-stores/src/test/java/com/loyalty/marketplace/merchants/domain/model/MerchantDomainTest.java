package com.loyalty.marketplace.merchants.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.constants.MerchantRequestMappingConstants;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.merchants.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantDescription;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.merchants.outbound.database.entity.Name;
import com.loyalty.marketplace.merchants.outbound.database.repository.BarcodeRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoException;


@SpringBootTest(classes = MerchantDomain.class)
@ActiveProfiles("unittest")
public class MerchantDomainTest {

	//private static final MerchantBuilder merchantBuilder = new MerchantBuilder(null, null, null, null, null, null, null, null);

	@Mock
	ModelMapper modelMapper;
	
	@Mock
	MerchantBillingRateDomain billingRateDomain;
	
	@Mock
	Validator validator;
	
	@Mock
	MerchantRepository merchantRepository;
	
	@Mock
	CategoryRepository categoryRepository;
	
	@Mock
	BarcodeRepository barcodeRepository;
	
	
	@InjectMocks 
	MerchantDomain merchantDomain = new MerchantDomain();

	private String externalTransactionId;
	private String userName;
	private String program;
//	private String authorization;
//	private String sessionId;
//	private String userPrev;
//	private String channelId;
//	private String systemId;
//	private String systemPassword;
//	private String token;
//	private String transactionId;
//	private String merchantCode;
	ResultResponse resultResponse ;
	Merchant merchant ;

	@Before
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		
//		merchantCode = "1234";
		externalTransactionId = "123";
		userName = "TestUser";
		
		resultResponse = new ResultResponse(externalTransactionId);
		merchant = new Merchant();
		merchant.setProgramCode("Smiles");
		merchant.setMerchantCode("Mer01");
		merchant.setMerchantCode("P1234");
		merchant.setStatus("Active");
		Barcode barcodeType =new Barcode();
		barcodeType.setName("BarcodeName");
		merchant.setBarcodeType(barcodeType);
		Category category = new Category();
		CategoryName categoryName = new CategoryName() ;
		categoryName.setCategoryNameEn("english");
		category.setCategoryName(categoryName );
		merchant.setCategory(category);
		MerchantName merchantName = new MerchantName();
		merchantName.setMerchantNameEn("Merchant English");
		merchantName.setMerchantNameAr("Merchant Arabic");		
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescEn("Merchant Description English");
		merchantDescription.setMerchantDescAr("Merchant Description Arabic");		
		merchant.setMerchantName(merchantName);
		merchant.setMerchantDescription(merchantDescription);
		program = "program";
//		authorization = "Authorization";
//		externalTransactionId = "externalTransactionId";
//		userName = "userName";
//		sessionId = "sessionId";
//		userPrev = "userPrev";
//		channelId = "channelId";
//		systemId = "systemId";
//		systemPassword = "systemPassword";
//		token = "token";
//		transactionId = "transactionId";
//		MerchantCode = "MerchantCode";
//
	}
	
	
	@Test (expected = MarketplaceException.class)
	public void testSaveMerchantException() throws MarketplaceException {
		List<ContactPerson> contactPersons =  new ArrayList<>();
		merchant.setContactPersons(contactPersons );		
		when(modelMapper.map(merchantDomain , Merchant.class)).thenReturn(merchant);
		when(this.merchantRepository.insert(merchant)).thenReturn(merchant);
		merchantDomain.saveMerchant(merchantDomain,externalTransactionId);
		assertNotNull(merchant);
	}
	
	@Test 
	public void configureContactPersonSuccess() {		
		MerchantContactPersonDomain contactDomain = new MerchantContactPersonDomain();
		ContactPerson value = new ContactPerson();
		List<ContactPerson> contactPersons = new ArrayList<ContactPerson>();
		merchant.setContactPersons(contactPersons );
		when(modelMapper.map(contactDomain, ContactPerson.class)).thenReturn(value);
		when(this.merchantRepository.save(merchant)).thenReturn(merchant);
		List<MerchantContactPersonDomain> contactPersonDomain = new ArrayList<MerchantContactPersonDomain>();
		merchantDomain.configureContactPerson(merchant, contactPersonDomain , userName, program, MerchantRequestMappingConstants.CONFIGURE_CONATACT_PERSON,externalTransactionId);
		assertNotNull(merchant);
		
	}
	
	@Test
	public void testProgramCode() {
        MerchantDomain md = new MerchantDomain.MerchantBuilder().programCode("123")
                                .build();
        assertEquals("123", md.getProgramCode());
    }
	
	@Test
	public void testId() {
        MerchantDomain md = new MerchantDomain.MerchantBuilder().id("123")
                                .build();
        assertEquals("123", md.getId());
    }
	
	@Test
	public void testStatus() {
        MerchantDomain md = new MerchantDomain.MerchantBuilder().status("123")
                                .build();
        assertEquals("123", md.getStatus());
    }
	
	@Test
	public void testWhatYouGet() {
		WhatYouGetDomain wd = new WhatYouGetDomain.WhatYouGetBuilder("What you get in English", "What you get in Arabic")
				.build();		
        assertEquals("What you get in English", wd.getWhatYouGetEn());		
        assertEquals("What you get in Arabic", wd.getWhatYouGetAr());
                      
        MerchantDomain expected = new MerchantDomain.MerchantBuilder().whatYouGet(wd).build();       
                
        assertEquals("What you get in English",expected.getWhatYouGet().getWhatYouGetEn());
        assertEquals("What you get in Arabic",expected.getWhatYouGet().getWhatYouGetAr());
        
    }
	
	
	@Test
	public void testTnC() {
		TAndCDomain tnC = new TAndCDomain.TAndCBuilder("Terms And Conditions in English", "Terms And Conditions in Arabic")
				.build();  
        assertEquals("Terms And Conditions in English", tnC.getTnCEn());
        assertEquals("Terms And Conditions in Arabic", tnC.getTnCAr());
        
        MerchantDomain expected = new MerchantDomain.MerchantBuilder().tnC(tnC).build();                                 
        assertEquals("Terms And Conditions in English", expected.getTnC().getTnCEn());
        assertEquals("Terms And Conditions in Arabic", expected.getTnC().getTnCAr());
    }
	
	@Test
	public void testMerchantDescription() {
		MerchantDescriptionDomain merDesc = new MerchantDescriptionDomain.MerchantDescriptionBuilder()
				.merchantDescEn("Merchant 01 Description English")
				.merchantDescAr("Merchant 01 Description Arabic")
				.build();
        assertEquals("Merchant 01 Description English", merDesc.getMerchantDescEn());
        assertEquals("Merchant 01 Description Arabic", merDesc.getMerchantDescAr());
        
        MerchantDomain expected = new MerchantDomain.MerchantBuilder().merchantDescription(merDesc).build();
        assertEquals("Merchant 01 Description English", expected.getMerchantDescription().getMerchantDescEn());
        assertEquals("Merchant 01 Description Arabic", expected.getMerchantDescription().getMerchantDescAr());
    }
	
	@Test
	public void testExternalName() {
        MerchantDomain md = new MerchantDomain.MerchantBuilder().externalName("External Name")
                                .build();
        assertEquals("External Name", md.getExternalName());
    }
	
	@Test
	public void testDtCreated() throws ParseException {
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    String strDate= formatter.format(date);
	    
    	Date dobFDate = (Date)formatter.parse(strDate);
    	MerchantDomain md = new MerchantDomain.MerchantBuilder().dtCreated(dobFDate)
                            .build();
    	assertEquals(dobFDate, md.getDtCreated());
	    
    }
	
	@Test
	public void testDtUpdated() throws ParseException {
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    String strDate= formatter.format(date); 
	    	    
    	Date dobFDate = (Date)formatter.parse(strDate);  
    	MerchantDomain md = new MerchantDomain.MerchantBuilder().dtUpdated(dobFDate)
                .build();
    	assertEquals(dobFDate, md.getDtUpdated());
	       
    }
	
	@Test
	public void testUsrCreated() {
		MerchantDomain md = new MerchantDomain.MerchantBuilder().usrCreated("123")
                .build();
		assertEquals("123", md.getUsrCreated());
    }
	
	@Test
	public void testUsrUpdated() {
		MerchantDomain md = new MerchantDomain.MerchantBuilder().usrUpdated("123")
                .build();
		assertEquals("123", md.getUsrUpdated());
    }
	
	@Test
	public void TestValidateCategoryBarcodeSuccess() {
		
		Optional<Category> categry = Optional.of(new Category());
		Optional<Barcode> barcde = Optional.of(new Barcode());
		when(categoryRepository.findById(Mockito.anyString())).thenReturn(categry);
		when(barcodeRepository.findById(Mockito.anyString())).thenReturn(barcde);

		boolean valid = merchantDomain.validateCategoryBarcode("123", "123", resultResponse);
	 assertTrue(valid);
	}
	
	

	@Test
	public void TestCheckEmailExistsSuccess() {
		Optional<Merchant> merchnat = Optional.of(new Merchant());
		 when(merchantRepository.findByMerchantCodeAndEmailIdAndUserName(Mockito.anyString(), Mockito.anyString(),Mockito.anyString())).thenReturn(merchnat);
		boolean valid = merchantDomain.checkEmailExists("123", "123", resultResponse, "123");
		assertFalse(valid);
	}
	
	
	
	@Test
	public void TestValidateBillingRateTypeSuccess() throws MarketplaceException {
		List<MerchantBillingRateDto> discountBillingRates = new ArrayList<MerchantBillingRateDto>();
		boolean valid = merchantDomain.validateBillingRateType(discountBillingRates, resultResponse);	
		assertTrue(valid);
	}
	
	@Test
	public void TestValidateBillingRateTypeFailure() throws MarketplaceException {
		MerchantBillingRateDto merchantBillingRateDto = new MerchantBillingRateDto();
		merchantBillingRateDto.setCurrency("IND");
		merchantBillingRateDto.setRate(1.0);
		merchantBillingRateDto.setRateType("POINTS");
		merchantBillingRateDto.setStartDate("10-10-2019");
		merchantBillingRateDto.setEndDate("10-10-2019");
		List<MerchantBillingRateDto> discountBillingRates = new ArrayList<MerchantBillingRateDto>();
		discountBillingRates.add(merchantBillingRateDto);
		when(billingRateDomain.getTypeByRateType(Mockito.anyString())).thenReturn(null);
		merchantDomain.validateBillingRateType(discountBillingRates, resultResponse);	
	}
	
	@Test(expected = MarketplaceException.class)
	public void TestValidateBillingRateTypeException() throws MarketplaceException {
		MerchantBillingRateDto merchantBillingRateDto = new MerchantBillingRateDto();
		merchantBillingRateDto.setCurrency("IND");
		merchantBillingRateDto.setRate(1.0);
		merchantBillingRateDto.setRateType("POINTS");
		merchantBillingRateDto.setStartDate("10-10-2019");
		merchantBillingRateDto.setEndDate("10-10-2019");
		List<MerchantBillingRateDto> discountBillingRates = new ArrayList<MerchantBillingRateDto>();
		discountBillingRates.add(merchantBillingRateDto);
		when(billingRateDomain.getTypeByRateType(Mockito.anyString())).thenThrow(MarketplaceException.class);
		merchantDomain.validateBillingRateType(discountBillingRates, resultResponse);	
	}
	
	@Test()
	public void TestUpdateMerchant() throws MarketplaceException {
		merchant = merchantDomain.updateMerchantStatus(merchant, "active", userName, program,MerchantRequestMappingConstants.UPDATE_MERCHANT,externalTransactionId);
		assertNull(merchant);
	}
	
	@Test(expected = MarketplaceException.class)
	public void TestUpdateException() throws MarketplaceException {
		when(this.merchantRepository.save(merchant)).thenThrow(MongoException.class);
		merchantDomain.updateMerchantStatus(merchant, "active", userName,program,MerchantRequestMappingConstants.UPDATE_MERCHANT,externalTransactionId);
	}
	
	
	
}
	
