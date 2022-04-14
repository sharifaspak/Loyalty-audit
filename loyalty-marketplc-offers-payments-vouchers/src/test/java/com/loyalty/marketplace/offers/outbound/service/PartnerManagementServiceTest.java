package com.loyalty.marketplace.offers.outbound.service;

import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.GET_PARTNER_PATH;
import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.PARTNER_MANAGEMENT_URI;
import static org.junit.Assert.assertFalse;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.utils.MarketplaceException;

@SpringBootTest(classes=PartnerService.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class PartnerManagementServiceTest {
	
	@Value(PARTNER_MANAGEMENT_URI)
    private String partnerManagementUri;
	
	@Value(GET_PARTNER_PATH)
	private String partnerPath;	
	
	@Mock
	private ServiceHelper serviceHelper;
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	PartnerService partnerService;
	
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
	private String partnerCode;
	private Headers headers;
	private HttpEntity<?> entity;
	private boolean partnerStatus;
	private String url;
	private ResponseEntity<Boolean> response;
	
	
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
		partnerCode = "partnerCode";
		url = "sampleUrl"; 
		response = new ResponseEntity<Boolean>(true, HttpStatus.OK);
				
		new Headers(program, authorization, externalTransactionId, userName, 
				sessionId, userPrev, channelId, systemId, systemPassword, token, 
				externalTransactionId);
	}
	
	@Test
	public void testCheckPartnerExists() throws MarketplaceException, ParseException {
		
		Mockito.doReturn(response).when(restTemplate).exchange(url, HttpMethod.GET, entity, Boolean.class);
		partnerStatus = partnerService.checkPartnerExists(partnerCode, headers);
        assertFalse(partnerStatus);
	}
	
	@Test
	public void testCheckPartnerExistsRestClientException() throws MarketplaceException, ParseException {
		
		Mockito.doThrow(RestClientException.class).when(restTemplate).exchange(url, HttpMethod.GET, entity, Boolean.class);
		partnerStatus = partnerService.checkPartnerExists(partnerCode, headers);
        assertFalse(partnerStatus);
	}
	
	@Test
	public void testCheckPartnerExistsException() throws MarketplaceException, ParseException {
		
		Mockito.doThrow(NullPointerException.class).when(restTemplate).exchange(url, HttpMethod.GET, entity, Boolean.class);
		partnerStatus = partnerService.checkPartnerExists(partnerCode, headers);
		assertFalse(partnerStatus);
	}
	
}
