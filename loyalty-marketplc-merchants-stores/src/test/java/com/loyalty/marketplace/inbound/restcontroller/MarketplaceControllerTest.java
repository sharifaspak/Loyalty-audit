package com.loyalty.marketplace.inbound.restcontroller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.domain.model.MerchantDomain;
import com.loyalty.marketplace.merchants.inbound.dto.MarketPlaceContactPersonDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantDescription;
import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.merchants.outbound.database.entity.Name;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.UserManagementService;
import com.loyalty.marketplace.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes = MarketplaceController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MarketplaceControllerTest {

	@Mock
	MerchantRepository merchantRepository;

	@Mock
	StoreRepository storeRepository;

	@Mock
	MerchantDomain merchantDomain;
	
	@Mock
	UserManagementService userManagementService;

	@Mock
	StoreDomain storeDomain;

	@Mock
	Validator validator;

	@InjectMocks
	MarketplaceController marketplaceController;

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
	MerchantDto merchantDto;
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
	public void testConfigureContactPersonMerchantSuccess() throws MarketplaceException {
		MarketPlaceContactPersonDto marketPlaceContactPersonDto = new MarketPlaceContactPersonDto();
		marketPlaceContactPersonDto.setMerchantCode(merchantCode);
		marketPlaceContactPersonDto.setContactPersons(contactPersonsDto);
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenReturn(merchant);
		resultResponse = marketplaceController.configureContactPerson(marketPlaceContactPersonDto, program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId);
		assertEquals("2131", resultResponse.getResult().getResponse());
	}

	@Test
	public void testConfigureContactPersonException() throws MarketplaceException {
		MarketPlaceContactPersonDto marketPlaceContactPersonDto = new MarketPlaceContactPersonDto();
		marketPlaceContactPersonDto.setMerchantCode(merchantCode);
		marketPlaceContactPersonDto.setContactPersons(contactPersonsDto);
		when(merchantRepository.findByMerchantCode(Mockito.anyString())).thenThrow(RuntimeException.class);
		resultResponse = marketplaceController.configureContactPerson(marketPlaceContactPersonDto, program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId);
		assertEquals("2190", resultResponse.getResult().getResponse());
	}

	@Test
	public void testConfigureContactPersonStoreSuccess() throws MarketplaceException {
		Store store = new Store();
		MarketPlaceContactPersonDto marketPlaceContactPersonDto = new MarketPlaceContactPersonDto();
		marketPlaceContactPersonDto.setStoreCode("1234");
		marketPlaceContactPersonDto.setContactPersons(contactPersonsDto);
		when(storeRepository.findByStoreCode((Mockito.anyString()))).thenReturn(store);
		resultResponse = marketplaceController.configureContactPerson(marketPlaceContactPersonDto, program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
	}

	@Test
	public void testConfigureContactPersonStoreFailure() throws MarketplaceException {
		MarketPlaceContactPersonDto marketPlaceContactPersonDto = new MarketPlaceContactPersonDto();
		marketPlaceContactPersonDto.setStoreCode("1234");
		marketPlaceContactPersonDto.setContactPersons(contactPersonsDto);
		when(storeRepository.findByStoreCode((Mockito.anyString()))).thenReturn(null);
		resultResponse = marketplaceController.configureContactPerson(marketPlaceContactPersonDto, program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId);
		assertNotNull(resultResponse);
	}

	@Test
	public void testConfigureContactPersonFailure() throws MarketplaceException {
		Store store = new Store();
		MarketPlaceContactPersonDto marketPlaceContactPersonDto = new MarketPlaceContactPersonDto();
		marketPlaceContactPersonDto.setContactPersons(contactPersonsDto);
		when(storeRepository.findByStoreCode((Mockito.anyString()))).thenReturn(store);
		resultResponse = marketplaceController.configureContactPerson(marketPlaceContactPersonDto, program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId);
		assertEquals("2131", resultResponse.getResult().getResponse());
	}

	@Test
	public void testConfigureContactPersonMerchantNotAvaible() throws MarketplaceException {
		MarketPlaceContactPersonDto marketPlaceContactPersonDto = new MarketPlaceContactPersonDto();
		marketPlaceContactPersonDto.setMerchantCode(merchantCode);
		marketPlaceContactPersonDto.setContactPersons(contactPersonsDto);
		resultResponse = marketplaceController.configureContactPerson(marketPlaceContactPersonDto, program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId,
				systemPassword, token, externalTransactionId);
		assertEquals("2131", resultResponse.getResult().getResponse());
	}

}
