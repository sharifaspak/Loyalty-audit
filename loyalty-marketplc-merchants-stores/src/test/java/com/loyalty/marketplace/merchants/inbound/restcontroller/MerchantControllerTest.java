package com.loyalty.marketplace.merchants.inbound.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.inbound.dto.BarcodeDto;
import com.loyalty.marketplace.inbound.dto.BarcodeRequestDto;
import com.loyalty.marketplace.inbound.dto.CategoryDto;
import com.loyalty.marketplace.inbound.dto.CategoryRequestDto;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.constants.MerchantRequestMappingConstants;
import com.loyalty.marketplace.merchants.domain.model.MerchantDomain;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantImageRequestDto;
import com.loyalty.marketplace.merchants.inbound.dto.RateTypeDto;
import com.loyalty.marketplace.merchants.inbound.dto.RateTypeRequestDto;
import com.loyalty.marketplace.merchants.inbound.restcontroller.helper.MerchantControllerHelper;
import com.loyalty.marketplace.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.merchants.outbound.database.entity.Name;
import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantDescription;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantImage;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.merchants.outbound.database.repository.BarcodeRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantImageRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.RateTypeRepository;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantImagesResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsStoreOfferResponse;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantResult;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantStoreOfferResult;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.PartnerService;
import com.loyalty.marketplace.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes=MerchantController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MerchantControllerTest {

	@Mock
	MongoDbFactory mongoDbFactory;
	
	@Mock
	PartnerService merchantService;

	@Mock
	MongoMappingContext mongoMappingContext;

	@Mock
	ModelMapper modelMapper;

	@Mock
	MerchantControllerHelper merchantControllerHelper;

	@Mock
	MerchantRepository merchantRepository;

	@Mock
	StoreRepository storeRepository;
	
	@Mock
	CategoryRepository categoryRepository;
	
	@Mock
	BarcodeRepository barcodeRepository;
	
	@Mock
	MerchantImageRepository merchantImageRepository;

	@Mock
	RateTypeRepository rateTypeRepository;

	@Mock
	MerchantDomain merchantDomain;
	
	@Mock
	StoreDomain storeDomain;

	@Mock
	Validator validator;

	@InjectMocks
	MerchantController merchantController;

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
	private String merchantCode;
	ResultResponse resultResponse;
	Merchant merchant;
	MerchantDto merchantDto ;
	List<ContactPersonDto> contactPersonsDto;

	@Before
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		
		externalTransactionId = "";
		
		 merchantDto = new MerchantDto();
		 merchantDto.setBarcodeType("1");
			merchantDto.setBarcodeType("1");
			merchantDto.setMerchantCode("1234");
			merchantDto.setMerchantDescAr("merchantDescAr");
			merchantDto.setMerchantDescEn("merchantDescEn");
			merchantDto.setMerchantNameAr("merchantNameAr");
			merchantDto.setMerchantNameAr("merchantNameAr");
			merchantDto.setPartnerCode("12344");
		    contactPersonsDto = new ArrayList<ContactPersonDto>();
	        ContactPersonDto contactPersonDto = new ContactPersonDto();
	        contactPersonDto.setEmailId("test@mail.com");
	        contactPersonDto.setMobileNumber("+971987654321");
	        contactPersonDto.setFaxNumber("016781095");
	        contactPersonDto.setFirstName("John");
	        contactPersonDto.setLastName("Doe");
	        contactPersonDto.setUserName(userName);
	        contactPersonsDto.add(contactPersonDto);
	        merchantDto.setContactPersons(contactPersonsDto);
			MerchantBillingRateDto billingRateDto = new MerchantBillingRateDto();
			billingRateDto.setCurrency("AED");
			billingRateDto.setRate(12.4);
			billingRateDto.setStartDate("09-09-2018");
			billingRateDto.setEndDate("09-09-2020");
			billingRateDto.setRateType("Accrual");
			List<MerchantBillingRateDto> discountBillingRates = new ArrayList<MerchantBillingRateDto>();
			discountBillingRates.add(billingRateDto);
			merchantDto.setDiscountBillingRates(discountBillingRates);

		
		merchantCode = "1235";
		resultResponse = new ResultResponse(externalTransactionId);
		merchant = new Merchant();
		merchant.setProgramCode("Smiles");
		merchant.setMerchantCode("Mer01");
		merchant.setMerchantCode("P1234");
		merchant.setStatus("Active");
		Barcode barcodeType = new Barcode();
		barcodeType.setName("BarcodeName");
		merchant.setBarcodeType(barcodeType);
		Category category = new Category();
		CategoryName categoryName = new CategoryName();
		categoryName.setCategoryNameEn("english");
		category.setCategoryName(categoryName);
		merchant.setCategory(category);
		MerchantName merchantName = new MerchantName();
		merchantName.setMerchantNameEn("Merchant English");
		merchantName.setMerchantNameAr("Merchant Arabic");
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescEn("Merchant Description English");
		merchantDescription.setMerchantDescAr("Merchant Description Arabic");
		merchant.setMerchantName(merchantName);
		merchant.setMerchantDescription(merchantDescription);

	}
	
	
	@Test
	public void testConfigureMerchantSuccess() throws MarketplaceException {
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		List<Errors> errorList = new ArrayList<>();
		resultResponse.setBulkErrorAPIResponse(errorList );
		merchantDto.setBarcodeType("1");
		merchantDto.setCategory("2");
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(null);
		when(merchantService.getPartnerDetails(Mockito.anyString())).thenReturn(true);
		when(merchantDomain.validateCategoryBarcode(Mockito.anyString(), Mockito.anyString(),Mockito.any(ResultResponse.class))).thenReturn(true);
	  when(merchantDomain.validateBillingRateType(Matchers.eq(merchantDto.getDiscountBillingRates()), Matchers.any(ResultResponse.class))).thenReturn(true);

		//when(merchantDomain.validateBillingRateType((List<MerchantBillingRateDto>) Mockito.any(MerchantBillingRateDto.class),Mockito.any(ResultResponse.class) )).thenReturn(true);
		resultResponse = merchantController.configureMerchant(merchantDto, program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		
		assertNotNull(resultResponse);
	}
	
	@Test
	public void testConfigureMerchantMerchantExists() throws MarketplaceException {
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		List<Errors> errorList = new ArrayList<>();
		resultResponse.setBulkErrorAPIResponse(errorList );
		
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		when(merchantDomain.validateBillingRateType(merchantDto.getDiscountBillingRates(),resultResponse)).thenReturn(true);
		//when(merchantDomain.validateBillingRateType((List<MerchantBillingRateDto>) Mockito.any(MerchantBillingRateDto.class),Mockito.any(ResultResponse.class) )).thenReturn(true);
		resultResponse = merchantController.configureMerchant(merchantDto, program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
		
	}
	@Test
	public void testConfigureMerchantValidationFail() throws MarketplaceException {
		
		merchantDto.setMerchantCode("123456");
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		when(merchantDomain.validateBillingRateType(merchantDto.getDiscountBillingRates(),resultResponse)).thenReturn(true);
		//when(merchantDomain.validateBillingRateType((List<MerchantBillingRateDto>) Mockito.any(MerchantBillingRateDto.class),Mockito.any(ResultResponse.class) )).thenReturn(true);
		resultResponse = merchantController.configureMerchant(merchantDto, program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
		
	}
	
	@Test
	public void testConfigureMerchantException() throws MarketplaceException {
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		when(merchantService.getPartnerDetails(Mockito.anyString())).thenReturn(true);
		//when(merchantDomain.validateBillingRateType(merchantDto.getDiscountBillingRates(),resultResponse)).thenThrow(RuntimeException.class);
		resultResponse = merchantController.configureMerchant(merchantDto, program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
		
	}
	   
	@Test
	public void testConfigureMerchantFailure() throws MarketplaceException {
		merchantDto.setMerchantCode("{{{{");
		
		//when(merchantDomain.validateBillingRateType(merchantDto.getDiscountBillingRates(), resultResponse)).thenReturn(true);
		resultResponse = merchantController.configureMerchant(merchantDto, program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertEquals("2141", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testupdateMerchantSuccess() throws MarketplaceException {
		merchantDto.setBarcodeType("1");
		merchantDto.setCategory("2");
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		when(merchantService.getPartnerDetails(Mockito.anyString())).thenReturn(true);
		when(merchantDomain.validateCategoryBarcode(Mockito.anyString(), Mockito.anyString(),Mockito.any(ResultResponse.class))).thenReturn(true);
	    when(merchantDomain.validateBillingRateType(Matchers.eq(merchantDto.getDiscountBillingRates()), Matchers.any(ResultResponse.class))).thenReturn(true);
       resultResponse = merchantController.updateMerchant(merchantDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, merchantCode);
		assertNotNull(resultResponse);
	}
	
	@Test
	public void testConfigureImageFailure() throws MarketplaceException {
		MerchantImage merchantImageRequestDtoToSave = new MerchantImage();
		MerchantImageRequestDto merchantImageRequestDto = null;
		when(merchantImageRepository.save(merchantImageRequestDtoToSave)).thenThrow(RuntimeException.class);
		resultResponse = merchantController.configureImage(merchantImageRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
		
	}
	
	
	@Test
	public void testConfigureImageException() throws MarketplaceException {
		MerchantImage merchantImageRequestDtoToSave = new MerchantImage();
		MerchantImageRequestDto merchantImageRequestDto = null;
		when(merchantImageRepository.save(merchantImageRequestDtoToSave)).thenThrow(RuntimeException.class);
		resultResponse = merchantController.configureImage(merchantImageRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
		
	}
	@Test
	public void testConfigureImageSuccess() throws MarketplaceException {
		MerchantImageRequestDto merchantImageRequestDto = new MerchantImageRequestDto();
		when(modelMapper.map(merchantImageRequestDto, MerchantImage.class)).thenReturn(null);
		resultResponse = merchantController.configureImage(merchantImageRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
		
	}
	
	
	@Test
	public void testListMerchantThumbnailsSuccess() throws MarketplaceException {
		MerchantImage merchantImage = new MerchantImage();
		merchantImage.setDomain("Domain");
		List<MerchantImage> merchantImageList = new ArrayList<MerchantImage>();
		merchantImageList.add(merchantImage);
		when(merchantImageRepository.findByDomain(Mockito.anyString())).thenReturn(merchantImageList);
		
		ListMerchantImagesResponse listMerchantImagesResponse = merchantController.listMerchantThumbnails(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);	
	
		assertNotNull(listMerchantImagesResponse);
	}
	
	@Test
	public void testListMerchantThumbnailsFailure() throws MarketplaceException {
		
		List<MerchantImage> merchantImageList = new ArrayList<MerchantImage>();
		
		when(merchantImageRepository.findByDomain(Mockito.anyString())).thenReturn(merchantImageList);
		
		ListMerchantImagesResponse listMerchantImagesResponse = merchantController.listMerchantThumbnails(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);	
	
		assertNotNull(listMerchantImagesResponse);
	}
	
	@Test
	public void testListMerchantThumbnailsException() throws MarketplaceException {
		
		when(merchantImageRepository.findByDomain(Mockito.anyString())).thenThrow(RuntimeException.class);
		
		ListMerchantImagesResponse listMerchantImagesResponse = merchantController.listMerchantThumbnails(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);	
	
		assertNotNull(listMerchantImagesResponse);
	}
	
	
	@Test
	public void testListMerchantImagesException() throws MarketplaceException {
		String domain = "";
		when(merchantImageRepository.findByDomain(Mockito.anyString())).thenThrow(RuntimeException.class);
		ListMerchantImagesResponse listMerchantImagesResponse= merchantController.listMerchantImages(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, domain );
	assertNotNull(listMerchantImagesResponse);
	}
	
	
	
	@Test
	public void testListMerchantImagesFailure() throws MarketplaceException {
		String domain = "";
		ListMerchantImagesResponse listMerchantImagesResponse= merchantController.listMerchantImages(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, domain );
		assertNotNull(listMerchantImagesResponse);
	}
	

	
	@Test
	public void testListBarcodesSuccess() throws MarketplaceException {
		List<Barcode> listBarcodes = new ArrayList<Barcode>();
		listBarcodes = merchantController.listBarcodes();
		assertNotNull(listBarcodes);
	}
	
	@Test
	public void testListBarcodesFailure() throws MarketplaceException {
		when(barcodeRepository.findAll()).thenThrow(RuntimeException.class);
		List<Barcode> listBarcodes = new ArrayList<Barcode>();
		listBarcodes = merchantController.listBarcodes();
		assertNotNull(listBarcodes);
	}
	
	
	@Test
	public void testListRateTypesSuccess() throws MarketplaceException {
		List<RateType> listRateTypes = new ArrayList<RateType>();
		listRateTypes = merchantController.listRateTypes();
		assertNotNull(listRateTypes);
	}
	
	@Test
	public void testListRateTypesFailure() throws MarketplaceException {
		List<RateType> listRateTypes = new ArrayList<RateType>();
		when(rateTypeRepository.findAll()).thenThrow(RuntimeException.class);
		listRateTypes = merchantController.listRateTypes();
		assertNotNull(listRateTypes);
	}
	

	@Test
	public void testListMerchantImagesSuccess() throws MarketplaceException {
		String domain = "";
		MerchantImage merchantImage = new MerchantImage();
		merchantImage.setDomain(domain);
		List<MerchantImage> merchantImageList = new ArrayList<MerchantImage>();
		merchantImageList.add(merchantImage);
		when(merchantImageRepository.findByDomain(Mockito.anyString())).thenReturn(merchantImageList);
		ListMerchantImagesResponse listMerchantImagesResponse= merchantController.listMerchantImages(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, domain );
		assertNotNull(listMerchantImagesResponse);
	}
	
	
	
	
	
	

	

	@Test
	public void testlistAllMerchantsSuccess() {

		List<Merchant> merchantList = new ArrayList<Merchant>();
		MerchantResult merchantResult = new MerchantResult();	
		merchantList.add(merchant);
		
		when(merchantRepository.findAll()).thenReturn(merchantList);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		ListMerchantsResponse listMerchantsResponse = merchantController.listAllMerchants(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId);
		assertEquals("2160", listMerchantsResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testlistAllMerchantsFailure() {

		List<Merchant> merchantList = new ArrayList<Merchant>();
		MerchantResult merchantResult = new MerchantResult();	
		merchantList.add(merchant);
		
		when(merchantRepository.findAll()).thenThrow(RuntimeException.class);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		ListMerchantsResponse listMerchantsResponse = merchantController.listAllMerchants(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId);
		assertEquals("2166", listMerchantsResponse.getResult().getResponse());
	
	}
	
	@Test
	public void testlistAllMerchantsSuccess2() {

		List<Merchant> merchantList = new ArrayList<Merchant>();
		MerchantResult merchantResult = new MerchantResult();	
		merchantList.add(merchant);
		when(merchantRepository.findByStatus(Mockito.anyString())).thenReturn(merchantList);
		when(merchantRepository.findAll()).thenReturn(merchantList);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		ListMerchantsResponse listMerchantsResponse = merchantController.listAllMerchants(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, "Web", systemId, systemPassword, token,
				externalTransactionId);
		assertNotNull(listMerchantsResponse);
	
	}
	
	
	@Test
	public void testlistAllPartnersEmptyList() {
		
		List<Merchant> merchantList = new ArrayList<Merchant>();
		MerchantResult merchantResult = new MerchantResult();	
		
		when(merchantRepository.findAll()).thenReturn(merchantList);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		
		ListMerchantsResponse listMerchantsResponse = merchantController.listAllMerchants(program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(listMerchantsResponse.getResult().getResponse());
	
	}

	@Test
	public void testlistAllPartnersException() {
	
		List<Merchant> merchantList = null;
		MerchantResult merchantResult = new MerchantResult();	
		
		when(merchantRepository.findAll()).thenReturn(merchantList);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);  
		
		ListMerchantsResponse listMerchantsResponse = merchantController.listAllMerchants(program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertEquals("2166", listMerchantsResponse.getResult().getResponse());
	
	}

	@Test
	public void testViewPartnerMerchantsSuccess() {
		String partnerCode = "1234";
		List<Merchant> merchantList = new ArrayList<Merchant>();
		MerchantResult merchantResult = new MerchantResult();
		merchantList.add(merchant);
		when(merchantRepository.findByPartnerCode(partnerCode)).thenReturn(merchantList);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		ListMerchantsResponse listMerchantsResponse = merchantController.viewPartnerMerchants(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId, partnerCode);
		assertEquals("2162", listMerchantsResponse.getResult().getResponse());
	}

	@Test
	public void testViewPartnerMerchantsFailure() {
		
		String partnerCode = "1234";
		
		List<Merchant> merchantList = new ArrayList<Merchant>();
		MerchantResult merchantResult = new MerchantResult(); 
		
		when(merchantRepository.findByPartnerCode(partnerCode)).thenReturn(merchantList);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		ListMerchantsResponse listMerchantsResponse = merchantController.viewPartnerMerchants(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId, partnerCode); 
		assertNotNull(listMerchantsResponse);
	
	}

	@Test
	public void testViewPartnerMerchantsException() {
		
		String partnerCode = "1234";
		
		List<Merchant> merchantList = null;
		MerchantResult merchantResult = new MerchantResult();
		
		when(merchantRepository.findByPartnerCode(partnerCode)).thenReturn(merchantList);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		ListMerchantsResponse listMerchantsResponse = merchantController.viewPartnerMerchants(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId, partnerCode);
		assertEquals("2166", listMerchantsResponse.getResult().getResponse());

	}
	
	@Test
	public void testViewPartnerMerchantsException2() {
		
		String partnerCode = "1234";
	
		MerchantResult merchantResult = new MerchantResult();
		
		when(merchantRepository.findByPartnerCode(Mockito.anyString())).thenThrow(RuntimeException.class);
		when(modelMapper.map(merchant, MerchantResult.class)).thenReturn(merchantResult);
		ListMerchantsResponse listMerchantsResponse = merchantController.viewPartnerMerchants(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId, partnerCode);
		assertNotNull(listMerchantsResponse);

	}

	@Test
	public void testViewSpecificMerchantException() {
		Merchant merchant = new Merchant();
		String merchantCode = "Mer01";
		merchant.setProgramCode("Smiles");
		merchant.setMerchantCode("Mer01");
		merchant.setMerchantCode("P1234");
		merchant.setStatus("Active");

		MerchantName merchantName = new MerchantName();
		merchantName.setMerchantNameEn("Merchant English");
		merchantName.setMerchantNameAr("Merchant Arabic");

		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescEn("Merchant Description English");
		merchantDescription.setMerchantDescAr("Merchant Description Arabic");

		merchant.setMerchantName(merchantName);
		merchant.setMerchantDescription(merchantDescription);

		List<Merchant> merchantList = new ArrayList<Merchant>();
		merchantList.add(merchant);

		when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);
		when(storeRepository.findByMerchantCode(merchantCode)).thenReturn(null);
		ListMerchantsStoreOfferResponse listMerchantsResponse = merchantController.viewSpecificMerchant(program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId, "Mer01");
		assertNotNull(listMerchantsResponse);
	}

	@Test
	public void testViewSpecificMerchantSuccess() {
		Merchant merchant = new Merchant();
		List<Store> stores = new ArrayList<>();
		Store store = new Store();
		store.setStoreCode("storeCode");
		stores.add(store);
		String merchantCode = "Mer01";
		merchant.setProgramCode("Smiles");
		merchant.setMerchantCode("Mer01");
		merchant.setMerchantCode("P1234");
		merchant.setStatus("Active");
		Barcode barcodeType = new Barcode();
		barcodeType.setName("BarcodeName");
		merchant.setBarcodeType(barcodeType);
		Category category = new Category();
		CategoryName categoryName = new CategoryName();
		categoryName.setCategoryNameEn("english");
		category.setCategoryName(categoryName);
		merchant.setCategory(category);
		MerchantName merchantName = new MerchantName();
		merchantName.setMerchantNameEn("Merchant English");
		merchantName.setMerchantNameAr("Merchant Arabic");

		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescEn("Merchant Description English");
		merchantDescription.setMerchantDescAr("Merchant Description Arabic");
		MerchantStoreOfferResult merchantofferList = new MerchantStoreOfferResult();

		merchant.setMerchantName(merchantName);
		merchant.setMerchantDescription(merchantDescription);
		List<Merchant> merchantList = new ArrayList<Merchant>();
		merchantList.add(merchant);
		when(modelMapper.map(merchant, MerchantStoreOfferResult.class)).thenReturn(merchantofferList);
		when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);
		when(storeRepository.findByMerchantCode(merchantCode)).thenReturn(stores);
		ListMerchantsStoreOfferResponse listMerchantsResponse = merchantController.viewSpecificMerchant(program,
				authorization, externalTransactionId, userName, sessionId, userPrev, "WEB", systemId, systemPassword,
				token, externalTransactionId, "Mer01");
		assertNotNull(listMerchantsResponse);
	}

	@Test
	public void testViewSpecificMerchantFailure() {

		when(merchantRepository.findByMerchantCode("123")).thenReturn(null);
		ListMerchantsStoreOfferResponse listMerchantsResponse = merchantController.viewSpecificMerchant(program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId, "Mer01");
		assertEquals("2166", listMerchantsResponse.getResult().getResponse());
	}

	@Test
	public void testActivateDeactivateMerchantSuccess() {
		String status = "Active";
		Merchant merchant = new Merchant();
		merchant.setStatus(status);
		when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);

		resultResponse = merchantController.activateDeactivateMerchant(program, authorization, externalTransactionId,
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId,
				status, merchantCode);
		assertEquals("2152", resultResponse.getResult().getResponse());
	}

	@Test
	public void testActivateDeactivateMerchantSuccessTestcase2() throws MarketplaceException {
		String status = "Inactive";
		Merchant merchant = new Merchant();
		merchant.setStatus("Active");
		when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);
		when(merchantDomain.updateMerchantStatus(merchant, status, userName,program, MerchantRequestMappingConstants.ACTIVATE_DEACTIVATE_MERCHANT)).thenReturn(merchant);
		//when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);
		when(merchantDomain.validateBillingRateType(Matchers.eq(merchantDto.getDiscountBillingRates()), Matchers.any(ResultResponse.class))).thenReturn(true);

		ResultResponse resultResponse = merchantController.activateDeactivateMerchant(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId, status, merchantCode);
		assertNotNull(resultResponse);
	}

	@Test
	public void testActivateDeactivateMerchantFailure() {
		String status = "failure";
		Merchant merchant = new Merchant();
		merchant.setStatus(status);
		when(merchantRepository.findByMerchantCode(merchantCode)).thenReturn(merchant);

		ResultResponse resultResponse = merchantController.activateDeactivateMerchant(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId, status, merchantCode);
		assertEquals("2155", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testActivateDeactivateMerchantException() {
		String status = "Active";
		Merchant merchant = new Merchant();
		merchant.setStatus(status);
		when(merchantRepository.findByMerchantCode(merchantCode)).thenThrow(RuntimeException.class);

		ResultResponse resultResponse = merchantController.activateDeactivateMerchant(program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				externalTransactionId, status, merchantCode);
		assertEquals("2155", resultResponse.getResult().getResponse());
	}
	
	

	@Test
	public void testRateTypeConfigurationSuccess() throws Exception {

		RateTypeDto rateTypeDto = new RateTypeDto();
		rateTypeDto.setType("Discount");
		rateTypeDto.setTypeId("B");
		rateTypeDto.setTypeRate("C");
		rateTypeDto.setTypeRateDesc("D");

		List<RateTypeDto> rateTypeList = new ArrayList<RateTypeDto>();
		rateTypeList.add(rateTypeDto);
		RateTypeRequestDto rateTypeRequest = new RateTypeRequestDto();
		rateTypeRequest.setRateTypes(rateTypeList);

		List<RateType> existingRates = new ArrayList<RateType>();
		RateType rateType = new RateType();
		rateType.setType("A");
		rateType.setTypeId("B");
		rateType.setTypeRate("C");
		rateType.setTypeRateDesc("D");
		existingRates.add(rateType);

		when(rateTypeRepository.findAll()).thenReturn(existingRates);
		when(rateTypeRepository.saveAll(new ArrayList<RateType>())).thenReturn(existingRates);
		when(merchantControllerHelper.validateRateTypeRequest(rateTypeList, resultResponse, existingRates, 3, userName, program))
				.thenReturn(true);
 
		resultResponse = merchantController.configureRateTypes(rateTypeRequest, program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				transactionId);

		assertNotNull(resultResponse);
	}
	
	@Test
	public void testRateTypeConfigurationFailure() throws Exception {
		
		RateTypeRequestDto rateTypeRequest = null;;
		resultResponse = merchantController.configureRateTypes(rateTypeRequest , program, authorization,
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
				transactionId);

		assertNotNull(resultResponse);
		
	}

	@Test(expected = NullPointerException.class)
	public void testRateTypeConfigurationException() throws Exception {

		RateTypeDto rateTypeDto = new RateTypeDto();
		rateTypeDto.setType("ACCRUAL");
		rateTypeDto.setTypeId("B");
		rateTypeDto.setTypeRate("C");
		rateTypeDto.setTypeRateDesc("D");

		List<RateTypeDto> rateTypeList = new ArrayList<RateTypeDto>();
		rateTypeList.add(rateTypeDto);
		RateTypeRequestDto rateTypeRequest = new RateTypeRequestDto();
		rateTypeRequest.setRateTypes(rateTypeList);

		List<RateType> existingRates = new ArrayList<RateType>(); 
		RateType rateType = new RateType();
		rateType.setType("A");
		rateType.setTypeId("B");
		rateType.setTypeRate("C");
		rateType.setTypeRateDesc("D");
		existingRates.add(rateType);

		when(rateTypeRepository.findAll()).thenReturn(existingRates);

		existingRates.add(null);
		when(rateTypeRepository.saveAll(existingRates)).thenReturn(new ArrayList<RateType>());

		merchantController.configureRateTypes(rateTypeRequest, program, authorization, externalTransactionId, userName,
				sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);

	}
	

    
	
	    @Test
	    public void testConfigureBarcodeSuccess() {
	        
	        BarcodeRequestDto barcodeRequest = new BarcodeRequestDto();
	        List<BarcodeDto> barcodes = new ArrayList<BarcodeDto>();
	        BarcodeDto barcode = new BarcodeDto();
	        barcode.setId("BarcodeId");
	        barcode.setName("BarcodeName");
	        barcode.setDescription("Description");
	        barcodes.add(barcode);
	        barcodeRequest.setBarcodes(barcodes);
	        
	        List<Barcode> existingBarcodes = new ArrayList<Barcode>();
	        Barcode existingBarcode = new Barcode();  
	        
	        existingBarcode.setId("BarcodeId");
	        existingBarcode.setName("BarcodeName");
	        existingBarcode.setDescription("Description");
	        existingBarcodes.add(existingBarcode);
	        
	        when(barcodeRepository.findAll()).thenReturn(existingBarcodes);
	        when(barcodeRepository.saveAll(new ArrayList<Barcode>())).thenReturn(existingBarcodes);
	        
	        resultResponse = merchantController.configureBarcodeType(barcodeRequest, program, authorization,
	                externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
	                transactionId);


	        assertNotNull(resultResponse.getResult().getResponse());
	        
	    }

	    
	    @Test(expected = RuntimeException.class)
	    public void testConfigureBarcodeException() {
	        
	        BarcodeRequestDto barcodeRequest = new BarcodeRequestDto();
	        List<BarcodeDto> barcodes = new ArrayList<BarcodeDto>();
	        BarcodeDto barcode = new BarcodeDto();
	        barcode.setId("BarcodeId");
	        barcode.setName("BarcodeName");
	        barcode.setDescription("Description");
	        barcodes.add(barcode);
	        barcodeRequest.setBarcodes(barcodes);  
	        
	        List<Barcode> existingBarcodes = new ArrayList<Barcode>();
	        Barcode existingBarcode = new Barcode();
	        
	        existingBarcode.setId("BarcodeId");
	        existingBarcode.setName("BarcodeName");
	        existingBarcode.setDescription("Description");
	        existingBarcodes.add(existingBarcode);
	        
	        when(barcodeRepository.findAll()).thenThrow(RuntimeException.class);
	        when(barcodeRepository.saveAll(new ArrayList<Barcode>())).thenThrow(RuntimeException.class);
	        
	        resultResponse = merchantController.configureBarcodeType(barcodeRequest, program, authorization,
	                externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token,
	                transactionId);

	 

	      assertNotNull(resultResponse);
	        
	    }






}
