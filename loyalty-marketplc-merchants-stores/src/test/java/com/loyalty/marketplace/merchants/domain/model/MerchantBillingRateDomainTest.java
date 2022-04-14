package com.loyalty.marketplace.merchants.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;
import com.loyalty.marketplace.merchants.outbound.database.repository.RateTypeRepository;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes = MerchantBillingRateDomain.class)
@ActiveProfiles("unittest")
public class MerchantBillingRateDomainTest {
	
	@Mock
	RateTypeRepository rateTypeRepository;
	
	@InjectMocks
	MerchantBillingRateDomain merchantBillingRateDomain = new MerchantBillingRateDomain();
	
	@Before
	public void setUp() throws ParseException {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void TestCreateBillingRatesSuccess() throws ParseException {
		
		MerchantBillingRateDto merchantBillingRateDto = new MerchantBillingRateDto();
		merchantBillingRateDto.setCurrency("IND");
		merchantBillingRateDto.setRate(1.0);
		merchantBillingRateDto.setRateType("POINTS");
		merchantBillingRateDto.setStartDate("10-10-2019");
		merchantBillingRateDto.setEndDate("10-10-2019");
		List<MerchantBillingRateDto> billingRateDtoList =  new ArrayList<>();
		billingRateDtoList.add(merchantBillingRateDto);
		List<MerchantBillingRateDto> invalidBillingRateDtoList =  new ArrayList<>();
		List<MerchantBillingRateDomain> merchantBillingRateDomainList =  new ArrayList<>();
		merchantBillingRateDomainList = merchantBillingRateDomain.createBillingRates(billingRateDtoList, invalidBillingRateDtoList);
			assertNotNull(merchantBillingRateDomainList);
	}
	
	
	@Test
	public void TestGetTypeByRateType() throws MarketplaceException {
		RateType rateType = new RateType();
		when(rateTypeRepository.findByTypeRate(Mockito.anyString())).thenReturn(rateType);
         rateType = merchantBillingRateDomain.getTypeByRateType("POINTS");
         assertNotNull(rateType);
	}
	
	@Test(expected = MarketplaceException.class )
	public void TestGetTypeByRateTypeException() throws MarketplaceException {
		when(rateTypeRepository.findByTypeRate(Mockito.anyString())).thenThrow(RuntimeException.class);
        merchantBillingRateDomain.getTypeByRateType("POINTS");
	}

}
