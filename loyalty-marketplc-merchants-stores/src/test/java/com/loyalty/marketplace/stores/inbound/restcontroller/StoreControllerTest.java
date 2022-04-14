package com.loyalty.marketplace.stores.inbound.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantDescription;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.UserManagementService;
import com.loyalty.marketplace.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.stores.inbound.dto.StoreDto;
import com.loyalty.marketplace.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.stores.outbound.dto.ListStoresResponse;
import com.loyalty.marketplace.stores.outbound.dto.StoreResult;

@SpringBootTest(classes=StoreController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class StoreControllerTest {

	@Mock
	UserManagementService userManagementService;
	
	@Mock
	ModelMapper modelMapper;

	@Mock
	StoreRepository storeRepository;
	
	@Mock
	MerchantRepository merchantRepository;
	
	@Mock
	Validator validator;
	
	@Mock
	StoreDomain storeDomain;
	
	@InjectMocks
	StoreController storeController;
	
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
	private StoreDto storeDto;
	List<ContactPersonDto> contactPersonsDto;
	List<ContactPerson> contactPersons;
	ResultResponse resultResponse;
	
	@Before
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		program = "Smiles";
		authorization = "authorization";
		externalTransactionId = "1234";
		userName = "userName";
		sessionId = "sessionId";
		userPrev = "userPrev";
		channelId = "channelId";
		systemId = "systemId";
		systemPassword = "systemPassword";
		token = "token";
		transactionId = "transactionId";
		resultResponse = new ResultResponse(externalTransactionId);
		storeDto =  new StoreDto();	
		storeDto.setStoreCode("1234");
		storeDto.setMerchantCode("1234");
		storeDto.setAddress("address");
		storeDto.setAddressAr("addressAr");
		storeDto.setDescriptionAr("descriptionAr");
		storeDto.setDescriptionEn("description");
		storeDto.setLatitude("1234");
		storeDto.setLongitude("1234");
		contactPersonsDto = new ArrayList<ContactPersonDto>();
        ContactPersonDto contactPersonDto = new ContactPersonDto();
        contactPersonDto.setEmailId("test@mail.com");
        contactPersonDto.setMobileNumber("+971987654321");
        contactPersonDto.setFaxNumber("016781095");
        contactPersonDto.setFirstName("John");
        contactPersonDto.setLastName("Doe");
        contactPersonDto.setUserName(userName);
        ContactPersonDto contactPersonDto1 = new ContactPersonDto();
        contactPersonDto1.setEmailId("test1@mail.com");
        contactPersonDto1.setMobileNumber("+971987654321");
        contactPersonDto1.setFaxNumber("016781095");
        contactPersonDto1.setFirstName("John");
        contactPersonDto1.setLastName("Doe");
        contactPersonDto1.setUserName("");
        contactPersonsDto.add(contactPersonDto1);
        storeDto.setContactPersons(contactPersonsDto);
	}
	
	@Test
	public void testConfigureStoreSuccess() {
		Merchant merchant = new Merchant();
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		resultResponse = storeController.configureStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId);
		assertEquals("2230", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testConfigureStoreExists() {
		Merchant merchant = new Merchant();
		Store store = new Store();
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		resultResponse = storeController.configureStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId);
		assertEquals("2231", resultResponse.getResult().getResponse());
	}
	@Test
	public void testConfigureStoreInvalidMerchantCode() {
		Store store = new Store();
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(null);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		resultResponse = storeController.configureStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId);
		assertEquals("2231", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testConfigureStoreException() {
		Store store = new Store();
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenThrow(RuntimeException.class);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		resultResponse = storeController.configureStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId);
		assertEquals("2231", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testConfigureStoreInvalidStoreCode() {
		storeDto.setStoreCode("123456");
		resultResponse = storeController.configureStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId);
		assertEquals("2231", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testupdateStoreSuccess() {
		Merchant merchant = new Merchant();
		Store store = new Store();
		List<ContactPerson> conperList = new ArrayList<ContactPerson>();
		ContactPerson conPer = new ContactPerson();		
		conPer.setEmailId("abc@gmail.com");
		conPer.setUserName("userName");
		conperList.add(conPer);
		ContactPerson conPer1 = new ContactPerson();		
		conPer1.setEmailId("abc@gmail.com");
		conPer1.setUserName("");
		conperList.add(conPer1);
		store.setContactPersons(conperList);
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		resultResponse = storeController.updateStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId, storeDto.getMerchantCode(), storeDto.getStoreCode());
		assertEquals("2241", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testUpdateStoreException() {
		Merchant merchant = new Merchant();
		Store store = new Store();		
		List<ContactPerson> conperList = new ArrayList<ContactPerson>();
		ContactPerson conPer = new ContactPerson();		
		conPer.setEmailId("abc@gmail.com");
		conperList.add(conPer);
		store.setContactPersons(conperList);
		when(storeRepository.findByStoreCode(Mockito.anyString())).thenReturn(store);
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		resultResponse = storeController.updateStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId, storeDto.getMerchantCode(), storeDto.getStoreCode());
		assertNotNull(resultResponse);
	}
	@Test
	public void testUpdateStoreInvalidMerchantCode() {
		Store store = new Store();
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(null);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		resultResponse = storeController.updateStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId, storeDto.getMerchantCode(), storeDto.getStoreCode());
		assertEquals("2241", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testUpdateStoreInvalidStoreCode() {
		storeDto.setStoreCode("123456");
		resultResponse = storeController.updateStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId, storeDto.getMerchantCode(), storeDto.getStoreCode());
		assertEquals("2241", resultResponse.getResult().getResponse());
	}
	@Test
	public void testUpdateStoreNotAvailable() {
		resultResponse = storeController.updateStore(storeDto, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, program, externalTransactionId, storeDto.getMerchantCode(), storeDto.getStoreCode());
		assertEquals("2241", resultResponse.getResult().getResponse());
	}
	
	@Test
	public void testListAllStoresUnAvailable() {		
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		
		when(storeRepository.findAll()).thenReturn(storeList );

		storeResponse = storeController.listAllStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(storeResponse);
	}
	
	@Test
	public void testListAllStoresException() {		
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		when(storeRepository.findAll()).thenThrow(RuntimeException.class);
		storeResponse = storeController.listAllStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(storeResponse);
	}
	
	@Test
	public void testListAllStores() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		when(storeRepository.findAll()).thenReturn(storeList );
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		storeResponse = storeController.listAllStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
		assertNotNull(storeResponse);
	}
	

	@Test
	public void testListMerchantStores() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		StoreResult storeResult = new StoreResult();
		when(modelMapper.map(store, StoreResult.class)).thenReturn(storeResult);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(storeList );
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		storeResponse = storeController.listMerchantStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, "1234");
				assertNotNull(storeResponse);
	}
	
	@Test
	public void testListMerchantStoresUnavailable() {		
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(null );

		storeResponse = storeController.listMerchantStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, "1234");
				assertNotNull(storeResponse);
	}
	
	@Test
	public void testListMerchantStoresExcpetion() {		
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenThrow(RuntimeException.class);

		storeResponse = storeController.listMerchantStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, "1234");
				assertNotNull(storeResponse);
	}
	
	@Test
	public void testViewSpecificStoreUnavailable() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		StoreResult storeResult = new StoreResult();
		when(modelMapper.map(store, StoreResult.class)).thenReturn(storeResult);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(storeList );
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		storeResponse = storeController.viewSpecificStore(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, storeDto.getMerchantCode(),storeDto.getStoreCode());
		assertEquals("2211", storeResponse.getResult().getResponse());
	}
	@Test
	public void testViewSpecificStore() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		StoreResult storeResult = new StoreResult();
		when(modelMapper.map(store, StoreResult.class)).thenReturn(storeResult);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(storeList );
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		storeResponse = storeController.viewSpecificStore(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, storeDto.getMerchantCode(),storeDto.getStoreCode());
		assertEquals("2225", storeResponse.getResult().getResponse());
	}
	@Test
	public void testViewSpecificStoreException() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		StoreResult storeResult = new StoreResult();
		when(modelMapper.map(store, StoreResult.class)).thenReturn(storeResult);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenThrow(RuntimeException.class);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(storeList );
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		storeResponse = storeController.viewSpecificStore(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, storeDto.getMerchantCode(),storeDto.getStoreCode());
		assertEquals("2211", storeResponse.getResult().getResponse());
	}
	
	
	@Test
	public void testListPartnerStoresFailure() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		StoreResult storeResult = new StoreResult();
		List<Merchant> merchantList = new ArrayList<Merchant>();
		merchantList.add(merchant);
		when(modelMapper.map(store, StoreResult.class)).thenReturn(storeResult);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(storeList );
		
		when(merchantRepository.findByPartnerCode(Mockito.anyString())).thenReturn(merchantList);
		storeResponse = storeController.listPartnerStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, "1234");
		assertEquals("2211", storeResponse.getResult().getResponse());
	}
	
	@Test
	public void testListPartnerStores() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		StoreResult storeResult = new StoreResult();
		List<Merchant> merchantList = new ArrayList<Merchant>();
		merchantList.add(merchant);
		when(modelMapper.map(store, StoreResult.class)).thenReturn(storeResult);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(null );		
		when(merchantRepository.findByPartnerCode(Mockito.anyString())).thenReturn(null);
		storeResponse = storeController.listPartnerStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, "1234");
		assertEquals("2211", storeResponse.getResult().getResponse());
	}
	
	@Test
	public void testListPartnerException() {	
		Merchant merchant = new Merchant();
		MerchantDescription merchantDescription = new MerchantDescription();
		merchantDescription.setMerchantDescAr("merchantDescAr");
		merchantDescription.setMerchantDescEn("merchantDescEn");
		merchant.setMerchantDescription(merchantDescription);
		ListStoresResponse storeResponse = new ListStoresResponse("1234");
		List<Store> storeList = new ArrayList<Store>();
		Store store = new Store();
		store.setStoreCode("1234");
		storeList.add(store);
		StoreResult storeResult = new StoreResult();
		List<Merchant> merchantList = new ArrayList<Merchant>();
		merchantList.add(merchant);
		when(modelMapper.map(store, StoreResult.class)).thenReturn(storeResult);
		when(storeRepository.findByStoreCodeAndMerchantCode(Mockito.anyString(),Mockito.anyString())).thenReturn(store);
		when(storeRepository.findByMerchantCode(Mockito.anyString())).thenReturn(null );		
		when(merchantRepository.findByPartnerCode(Mockito.anyString())).thenThrow(RuntimeException.class);
		storeResponse = storeController.listPartnerStores(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, "1234");
		assertEquals("2211", storeResponse.getResult().getResponse());
	}
	
	
	

	}
	
