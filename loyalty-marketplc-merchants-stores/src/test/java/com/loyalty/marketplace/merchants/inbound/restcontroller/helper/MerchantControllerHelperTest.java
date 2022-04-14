package com.loyalty.marketplace.merchants.inbound.restcontroller.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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

import com.loyalty.marketplace.merchants.inbound.dto.RateTypeDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;

import com.loyalty.marketplace.outbound.dto.ResultResponse;


@SpringBootTest(classes = MerchantControllerHelper.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class MerchantControllerHelperTest {
	
	@Mock
	ModelMapper modelMapper;
	
	@InjectMocks
	MerchantControllerHelper merchantControllerHelper;
	
	@Before
    public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidateRateTypeRequest() {
		
		List<RateTypeDto> rateTypes = new ArrayList<RateTypeDto>();
		RateTypeDto rateTypeDto = new RateTypeDto();
		rateTypeDto.setType("DISCOUNT");
		rateTypeDto.setTypeRate("AED");
		rateTypes.add(rateTypeDto);
		ResultResponse resultResponse = new ResultResponse(null);
		List<RateType> rateTypesToSave = new ArrayList<RateType>();
		int existingSize = 1;
	boolean valid =	merchantControllerHelper.validateRateTypeRequest(rateTypes, resultResponse, rateTypesToSave, existingSize, "" , "");
	assertTrue(valid);
	}
	@Test
	public void testValidateRateTypeRequestFlase() {
		
		List<RateTypeDto> rateTypes = new ArrayList<RateTypeDto>();
		RateTypeDto rateTypeDto = new RateTypeDto();
		rateTypeDto.setType("invalid");
		rateTypeDto.setTypeRate("AED");
		rateTypes.add(rateTypeDto);
		ResultResponse resultResponse = new ResultResponse(null);
		List<RateType> rateTypesToSave = new ArrayList<RateType>();
		int existingSize = 1;
	boolean valid =	merchantControllerHelper.validateRateTypeRequest(rateTypes, resultResponse, rateTypesToSave, existingSize, "", "");
	assertFalse(valid);
	}

}
