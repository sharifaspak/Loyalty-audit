package com.loyalty.marketplace.equivalentpoints.inbound;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.loyalty.marketplace.equivalentpoints.domain.EquivalentPointsDomain;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.ConversionRateDto;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.EquivalentPointsDto;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.event.HeaderDto;
import com.loyalty.marketplace.equivalentpoints.inbound.restcontroller.EquivalentPointsController;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ListRedemptionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionPointsOverlapRangeList;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionPointsValueChangeList;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionRate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { EquivalentPointsController.class })
public class EquivalentPointsControllerTest {

	@Mock
	private ModelMapper modelMapper;
	@MockBean
	private EquivalentPointsController equivalentPointsController;
	@MockBean
	private EquivalentPointsDomain equivalentPointsDomain;
	@MockBean
	private Validator validator;

	ResponseEntity<CommonApiStatus> apiStatus;

	@Before
	public void setUp() {
		equivalentPointsController = new EquivalentPointsController(equivalentPointsDomain, validator);
	}

	@Test
	public void testInsertConversionRate() throws Exception {
		ConversionRateDto conversionRateDto = new ConversionRateDto("partnerCode", null, "productItem", null, 0.2);
		apiStatus = equivalentPointsController.insertConversionRate(conversionRateDto, "userName", null, null, null,
				null, null, null, null, "program");
		assertNotNull(apiStatus);
	}

	@Test
	public void testGetEquivalentPoints() throws Exception {
		RedemptionRate redemptionCalculatedValue = new RedemptionRate(23.0, 0.2);
		List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList = new ArrayList<>();
		redemptionPointsValueChangeList.add(new RedemptionPointsValueChangeList("1", 0.2, 5.0, 6.0, 34.0, 56.0,
				"productItem", "channel", "partnerCode", "userName", new Date()));
		List<RedemptionPointsOverlapRangeList> redemptionPointsOverlapRangeList = new ArrayList<>();
		redemptionPointsOverlapRangeList.add(new RedemptionPointsOverlapRangeList("2", 1.0, 2.0, 3.0, 5.0, 34.0, 89.0,
				"productItem", "channel", "partnerCode", "userName", new Date()));
		ListRedemptionRate listRedemptionRate = new ListRedemptionRate(redemptionCalculatedValue, null,
				redemptionPointsValueChangeList, redemptionPointsOverlapRangeList);
		Mockito.when(equivalentPointsDomain.getAllEquivalentPoints()).thenReturn(listRedemptionRate);
		apiStatus = equivalentPointsController.getEquivalentPoints("userName", null, null, null, null, null, null, null,
				"program");
		assertNotNull(apiStatus);
	}

	@Test
	public void testEquivalentPointsAmount() throws Exception {
		List<RedemptionRate> redemptionRateList = new ArrayList<>();
		redemptionRateList.add(new RedemptionRate(23.0, 0.2));
		EquivalentPointsDto equivalentPointsDto = new EquivalentPointsDto("redeeming", "12", 2.8, null, "activityCode",
				"partnerCode", "channel", "offerType");
		Mockito.when(equivalentPointsDomain.calculateEquivalentPoints(Mockito.any(EquivalentPointsDto.class),
				Mockito.any(HeaderDto.class), Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(redemptionRateList);
		apiStatus = equivalentPointsController.equivalentPoints(equivalentPointsDto, "userName", null, null, null, null,
				null, null, null, "program");
		assertNotNull(apiStatus);
	}

	@Test
	public void testEquivalentPointsPoints() throws Exception {
		List<RedemptionRate> redemptionRateList = new ArrayList<>();
		redemptionRateList.add(new RedemptionRate(23.0, 0.2));
		EquivalentPointsDto equivalentPointsDto = new EquivalentPointsDto("redeeming", "12", null, 4.5, "activityCode",
				"partnerCode", "channel", "offerType");
		Mockito.when(equivalentPointsDomain.calculateEquivalentAmount(Mockito.any(EquivalentPointsDto.class)))
				.thenReturn(redemptionRateList);
		apiStatus = equivalentPointsController.equivalentPoints(equivalentPointsDto, "userName", null, null, null, null,
				null, null, null, "program");
		assertNotNull(apiStatus);
	}

	@Test
	public void testEquivalentPoints() throws Exception {
		RedemptionRate redemptionCalculatedValue = new RedemptionRate(23.0, 0.2);
		List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList = new ArrayList<>();
		redemptionPointsValueChangeList.add(new RedemptionPointsValueChangeList("1", 0.2, 5.0, 6.0, 34.0, 56.0,
				"productItem", "channel", "partnerCode", "userName", new Date()));
		List<RedemptionPointsOverlapRangeList> redemptionPointsOverlapRangeList = new ArrayList<>();
		redemptionPointsOverlapRangeList.add(new RedemptionPointsOverlapRangeList("2", 1.0, 2.0, 3.0, 5.0, 34.0, 89.0,
				"productItem", "channel", "partnerCode", "userName", new Date()));
		ListRedemptionRate listRedemptionRate = new ListRedemptionRate(redemptionCalculatedValue, null,
				redemptionPointsValueChangeList, redemptionPointsOverlapRangeList);
		EquivalentPointsDto equivalentPointsDto = new EquivalentPointsDto("redeeming", "12", null, null, "activityCode",
				"partnerCode", "channel", "offerType");
		Mockito.when(equivalentPointsDomain.getConversionRates(Mockito.any(EquivalentPointsDto.class),
				Mockito.any(String.class), Mockito.any(String.class))).thenReturn(listRedemptionRate);
		apiStatus = equivalentPointsController.equivalentPoints(equivalentPointsDto, "userName", null, null, null, null,
				null, null, null, "program");
		assertNotNull(apiStatus);
	}

}
