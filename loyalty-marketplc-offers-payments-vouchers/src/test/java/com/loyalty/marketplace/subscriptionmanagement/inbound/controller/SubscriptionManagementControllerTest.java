//package com.loyalty.marketplace.subscriptionmanagement.inbound.controller;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import javax.validation.Validator;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
//import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
//import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
//import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
//import com.loyalty.marketplace.outbound.dto.ResultResponse;
//import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
//import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
//import com.loyalty.marketplace.subscription.domain.SubscriptionCatalogDomain;
//import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
//import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
//import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogRequestDto;
//import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionRequestDto;
//import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
//import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
//import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
//import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;
//import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResultResponse;
//import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResponseDto;
//import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResultResponse;
//import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
//import com.loyalty.marketplace.utils.MarketplaceException;
//
//@SpringBootTest(classes = SubscriptionManagementController.class)
//@AutoConfigureMockMvc
//@ActiveProfiles("unittest")
//@EnableWebMvc
//public class SubscriptionManagementControllerTest {
//
//	@Mock
//	ModelMapper modelMapper;
//	
//	@Mock
//	SubscriptionCatalogRepository subscriptionCatalogRepository;
//	
//	@Mock
//	SubscriptionCatalogDomain subscriptionCatalogDomain;
//	
//	@Mock
//	SubscriptionRepository subscriptionRepository;
//	
//	@Mock
//	SubscriptionDomain subscriptionDomain;
//	
//	@Mock
//	MemberManagementService membermngmtService;
//	
//	@Mock
//	Validator validator;
//	
//	@InjectMocks
//	SubscriptionManagementController subscriptionManagementController;
//	
//	private String program;
//	private String authorization;
//	private String externalTransactionId;
//	private String userName;
//	private String sessionId;
//	private String userPrev;
//	private String channelId;
//	private String systemId;
//	private String systemPassword;
//	private String token;
//	private String transactionId;
//	ResultResponse resultResponse;
//	
//	private SubscriptionCatalogRequestDto subscriptionCatalogRequestDto;
//	List<PaymentMethodDto> paymentMethods;
//	List<String> linkedOfferId;
//	List<String> customerSegment;
//	
//	@Before
//	public void setUp() {
//		
//		MockitoAnnotations.initMocks(this);
//		program = "Smiles";
//		authorization = "authorization";
//		externalTransactionId = "6521";
//		userName = "userName";
//		sessionId = "sessionId";
//		userPrev = "userPrev";
//		channelId = "channelId";
//		systemId = "systemId";
//		systemPassword = "systemPassword";
//		token = "token";
//		transactionId = "transactionId";
//		
//		resultResponse = new ResultResponse(externalTransactionId);
//		
//		subscriptionCatalogRequestDto =  new SubscriptionCatalogRequestDto();	
//		subscriptionCatalogRequestDto.setId("Subscription ID");
//		subscriptionCatalogRequestDto.setValidityPeriod(2);
//		subscriptionCatalogRequestDto.setPointsValue(65);
//		subscriptionCatalogRequestDto.setStartDate("Start Date");
//		subscriptionCatalogRequestDto.setEndDate("End Date");
//		subscriptionCatalogRequestDto.setSubscriptionTitle("Subscription Title");
//		subscriptionCatalogRequestDto.setSubscriptionDescription("Subscription Description");
//		subscriptionCatalogRequestDto.setFreeDuration(23);
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Status");
//		
//		customerSegment = new ArrayList<>();
//		customerSegment.add("Seg 1");
//		customerSegment.add("Seg 2");
//		subscriptionCatalogRequestDto.setCustomerSegment(customerSegment);
//		
//		paymentMethods = new ArrayList<>();
////		paymentMethod.add("card");
////		paymentMethod.add("dcb");
////		subscriptionCatalogRequestDto.setPaymentMethod(paymentMethods);
//		
//		linkedOfferId = new ArrayList<>();
//		linkedOfferId.add("Linked 1");
//		linkedOfferId.add("Linked 2");
//		subscriptionCatalogRequestDto.setLinkedOfferId(linkedOfferId);
//		
//	}
//	
//	@Test
//	public void testCreateSubscriptionCatalogValidatioFail() {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setChargeabilityType("Charge");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		//List<String> payment = new ArrayList<>();
//		List<PaymentMethodDto> paymentMethods = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		resultResponse = subscriptionManagementController.createSubscriptionCatalog(subscriptionCatalogRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionCatalogDateValidation() throws Exception {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		subscriptionCatalogRequestDto.setStartDate("Start");
//		subscriptionCatalogRequestDto.setEndDate("End");
//		List<String> payment = new ArrayList<>();
//		List<PaymentMethodDto> paymentMethods = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(null)).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.createSubscriptionCatalog(subscriptionCatalogRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionCatalogCustomerTypeAvailable() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("qwer");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> availableCustomerType = new ArrayList<>();
//		availableCustomerType.add("Prepaid");
//		when(membermngmtService.getAvailableCustomerType(resultResponse)).thenReturn(availableCustomerType);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.createSubscriptionCatalog(subscriptionCatalogRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	
//	}
//	
//	@Test
//	public void testCreateSubscriptionCatalogCustomerTypeContainsAll() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscriptionCatalog = Optional.of(subscription);
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.createSubscriptionCatalog(subscriptionCatalogRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionCatalogUnavailable() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog = Optional.empty();
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.createSubscriptionCatalog(subscriptionCatalogRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionCatalogSMException() throws MarketplaceException, IOException, SubscriptionManagementException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog = Optional.empty();
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptionCatalog);
//		
//		doThrow(SubscriptionManagementException.class).when(subscriptionCatalogDomain)
//				.saveSubscriptionCatalog(Mockito.any(SubscriptionCatalogDomain.class));
//
//		resultResponse = subscriptionManagementController.createSubscriptionCatalog(subscriptionCatalogRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionCatalogGenericException() throws Exception {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(null)).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.createSubscriptionCatalog(subscriptionCatalogRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogValidatioFail() {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setChargeabilityType("Charge");
//		subscriptionCatalogRequestDto.setStatus("Active");
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		subscriptionCatalogRequestDto.setChargeabilityType("Charge");
//		String subscriptionCatalogId = "Subscription ID";
//		String action = "Update";
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogCustomerTypeAvailable() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("qwer");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> availableCustomerType = new ArrayList<>();
//		availableCustomerType.add("Prepaid");
//		when(membermngmtService.getAvailableCustomerType(resultResponse)).thenReturn(availableCustomerType);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = "Update";
//		
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogDateValidation() throws Exception {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		subscriptionCatalogRequestDto.setStartDate("Start");
//		subscriptionCatalogRequestDto.setEndDate("End");
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = "Update";
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(null)).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogCustomerTypeContainsAll() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscription.setProgramCode("Program");
//		subscriptionCatalog = Optional.of(subscription);
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = "Update";
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findById(subscriptionCatalogId)).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogExists() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog = Optional.empty();
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = "Update";
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findById(subscriptionCatalogId)).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogActionUpdateFail() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscription.setProgramCode("Smiles");
//		subscriptionCatalog = Optional.of(subscription);
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_UPDATE.get();
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findById(subscriptionCatalogId)).thenReturn(subscriptionCatalog);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogActionUpdate() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscription.setProgramCode("Smiles");
//		subscriptionCatalog = Optional.of(subscription);
//		
//		Optional<SubscriptionCatalog> subscriptions = Optional.empty();
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_UPDATE.get();
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findById(subscriptionCatalogId)).thenReturn(subscriptionCatalog);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptions);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogActionDelete() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscription.setProgramCode("Smiles");
//		subscriptionCatalog = Optional.of(subscription);
//		
//		Optional<SubscriptionCatalog> subscriptions = Optional.empty();
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_DELETE.get();
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findById(subscriptionCatalogId)).thenReturn(subscriptionCatalog);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptions);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_DELETED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogInvalidAction() throws MarketplaceException, IOException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscription.setProgramCode("Smiles");
//		subscriptionCatalog = Optional.of(subscription);
//		
//		Optional<SubscriptionCatalog> subscriptions = Optional.empty();
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = "Invalid";
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findById(subscriptionCatalogId)).thenReturn(subscriptionCatalog);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptions);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.INVALID_ACTION.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogSMException() throws MarketplaceException, IOException, SubscriptionManagementException {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setStartDate("20/12/2019");
//		subscriptionCatalogRequestDto.setEndDate("30/12/2019");
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
//		List<String> custType = new ArrayList<>();
//		custType.add("Prepaid");
//		subscriptionCatalogRequestDto.setCustomerSegment(custType);
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscription.setProgramCode("Smiles");
//		subscriptionCatalog = Optional.of(subscription);
//		
//		Optional<SubscriptionCatalog> subscriptions = Optional.empty();
//		
//		String subscriptionCatalogId = "Subscription ID";
//		String action = SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_UPDATE.get();
//		
//		when(membermngmtService.getAvailableCustomerType(Mockito.any(ResultResponse.class))).thenReturn(customerType);
//		when(subscriptionCatalogRepository.findById(subscriptionCatalogId)).thenReturn(subscriptionCatalog);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(subscriptionCatalogRequestDto.getSubscriptionTitle())).thenReturn(subscriptions);
//		
//		doThrow(SubscriptionManagementException.class).when(subscriptionCatalogDomain)
//		.updateSubscriptionCatalog(Mockito.any(SubscriptionCatalogDomain.class), Mockito.any(SubscriptionCatalog.class));
//		
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUpdateSubscriptionCatalogGenericException() throws Exception {
//		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = new SubscriptionCatalogRequestDto();
//		subscriptionCatalogRequestDto.setChargeabilityType("one-time");
//		subscriptionCatalogRequestDto.setStatus("Active");
////		List<String> payment = new ArrayList<>();
////		payment.add("card");
////		payment.add("dcb");
////		payment.add("points");
////		subscriptionCatalogRequestDto.setPaymentMethod(payment);
//		String subscriptionCatalogId = "Subscription ID";
//		String action = "Update";
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findBySubscriptionTitle(null)).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.updateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionCatalogValidationFail() {
//		SubscriptionCatalogResultResponse subscriptionCatalogResultResponse = new SubscriptionCatalogResultResponse(externalTransactionId);
//		subscriptionCatalogResultResponse = subscriptionManagementController.listSubscriptionCatalog("", authorization, externalTransactionId, "", sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(), subscriptionCatalogResultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionCatalogEmpty() {
//		SubscriptionCatalogResultResponse subscriptionCatalogResultResponse = new SubscriptionCatalogResultResponse(externalTransactionId);
//		List<SubscriptionCatalog> availableSubscriptionCatalog = new ArrayList<>();
//		when(subscriptionCatalogRepository.findAll()).thenReturn(availableSubscriptionCatalog);
//		subscriptionCatalogResultResponse = subscriptionManagementController.listSubscriptionCatalog(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(), subscriptionCatalogResultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionCatalogNotEmpty() {
//		SubscriptionCatalogResultResponse subscriptionCatalogResultResponse = new SubscriptionCatalogResultResponse(externalTransactionId);
//		List<SubscriptionCatalog> availableSubscriptionCatalog = new ArrayList<>();
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setEndDate(new Date());
//		availableSubscriptionCatalog.add(subscription);
////		when(subscriptionCatalogRepository.findByProgramCode(program.toLowerCase())).thenReturn(availableSubscriptionCatalog);
//		subscriptionCatalogResultResponse = subscriptionManagementController.listSubscriptionCatalog(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY.getId(), subscriptionCatalogResultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionCatalogException() throws Exception {
//		SubscriptionCatalogResultResponse subscriptionCatalogResultResponse = new SubscriptionCatalogResultResponse(externalTransactionId);
////		when(subscriptionCatalogRepository.findByProgramCode(program.toLowerCase())).thenThrow(NullPointerException.class);
//		subscriptionCatalogResultResponse = subscriptionManagementController.listSubscriptionCatalog(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(), subscriptionCatalogResultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionValidatioFail() {
//		PurchaseRequestDto subscriptionRequestDto = new PurchaseRequestDto();
//		String payment = "card";
//		subscriptionRequestDto.setSelectedOption(payment);
////		resultResponse = subscriptionManagementController.createSubscription(subscriptionRequestDto, null, authorization, externalTransactionId, null, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionSubscriptionCatalogNotFound() throws MarketplaceException, IOException {
//		PurchaseRequestDto subscriptionRequestDto = new PurchaseRequestDto();
//		subscriptionRequestDto.setStartDate("20/12/2019");
//		subscriptionRequestDto.setAccountNumber("Acc Number");
//		subscriptionRequestDto.setMembershipCode("Membership Code");
//		subscriptionRequestDto.setPromoCode("Promo Code");
//		String payment = "card";
//		subscriptionRequestDto.setSelectedOption(payment);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog = Optional.empty(); 
//		
//		when(subscriptionCatalogRepository.findByIdAndStatusAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get(), program.toLowerCase())).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.createSubscription(subscriptionRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionSubscriptionExists() throws MarketplaceException, IOException, SubscriptionManagementException {
//		PurchaseRequestDto subscriptionRequestDto = new PurchaseRequestDto();
//		subscriptionRequestDto.setStartDate("20/12/2019");
//		subscriptionRequestDto.setAccountNumber("Acc Number");
//		subscriptionRequestDto.setMembershipCode("Membership Code");
//		subscriptionRequestDto.setPromoCode("Promo Code");
//		String payment = "card";
//		subscriptionRequestDto.setSelectedOption(payment);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findByIdAndStatusAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get(), program.toLowerCase())).thenReturn(subscriptionCatalog);
//		
//		Optional<Subscription> subscriptions; 
//		Subscription subs = new Subscription();
//		subs.setId("ID");
//		subscriptions = Optional.of(subs);
//		when(subscriptionDomain.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), subscriptionRequestDto.getAccountNumber(), program.toLowerCase())).thenReturn(subscriptions);
//		
//		resultResponse = subscriptionManagementController.createSubscription(subscriptionRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionSubscriptionDoesNotExists() throws MarketplaceException, IOException, SubscriptionManagementException {
//		PurchaseRequestDto subscriptionRequestDto = new PurchaseRequestDto();
//		subscriptionRequestDto.setStartDate("20/12/2019");
//		subscriptionRequestDto.setAccountNumber("Acc Number");
//		subscriptionRequestDto.setMembershipCode("Membership Code");
//		subscriptionRequestDto.setPromoCode("Promo Code");
//		String payment = "card";
//		subscriptionRequestDto.setSelectedOption(payment);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findByIdAndStatusAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get(), program.toLowerCase())).thenReturn(subscriptionCatalog);
//		
//		Optional<Subscription> subscriptions = Optional.empty();
//		when(subscriptionDomain.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), subscriptionRequestDto.getAccountNumber(), program.toLowerCase())).thenReturn(subscriptions);
//		
//		resultResponse = subscriptionManagementController.createSubscription(subscriptionRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionEligibleCustomerSegment() throws MarketplaceException, IOException, SubscriptionManagementException {
//		PurchaseRequestDto subscriptionRequestDto = new PurchaseRequestDto();
//		subscriptionRequestDto.setStartDate("20/12/2019");
//		subscriptionRequestDto.setAccountNumber("Acc Number");
//		subscriptionRequestDto.setMembershipCode("Membership Code");
//		subscriptionRequestDto.setPromoCode("Promo Code");
//		String payment = "card";
//		subscriptionRequestDto.setSelectedOption(payment);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		List<String> customer = new ArrayList<>();
//		customer.add("Postpaid");
//		subscription.setCustomerSegment(customer);
//		subscriptionCatalog = Optional.of(subscription);
//		when(subscriptionCatalogRepository.findByIdAndStatusAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get(), program.toLowerCase())).thenReturn(subscriptionCatalog);
//		
//		Optional<Subscription> subscriptions = Optional.empty();
//		when(subscriptionDomain.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), subscriptionRequestDto.getAccountNumber(), program.toLowerCase())).thenReturn(subscriptions);
//		
//		GetMemberResponse getMemberResponse = new GetMemberResponse();
//		getMemberResponse.setMembershipCode("1000L");
//		getMemberResponse.setTierLevelName("Tier");
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		customerType.add("Postpaid");
//		getMemberResponse.setCustomerType(customerType);
//		
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenReturn(getMemberResponse);
//		
//		resultResponse = subscriptionManagementController.createSubscription(subscriptionRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testCreateSubscriptionEligiblePaymentMethods() throws MarketplaceException, IOException, SubscriptionManagementException {
//		PurchaseRequestDto subscriptionRequestDto = new PurchaseRequestDto();
//		subscriptionRequestDto.setStartDate("20/12/2019");
//		subscriptionRequestDto.setAccountNumber("Acc Number");
//		subscriptionRequestDto.setMembershipCode("Membership Code");
//		subscriptionRequestDto.setPromoCode("Promo Code");
//		String payment = "card";
//		subscriptionRequestDto.setSelectedOption(payment);
//		
//		Optional<SubscriptionCatalog> subscriptionCatalog; 
//		SubscriptionCatalog subscription = new SubscriptionCatalog();
//		subscription.setId("ID");
//		subscription.setSubscriptionLogo("Logo");
//		List<String> customer = new ArrayList<>();
//		customer.add("Prepaid");
//		subscription.setCustomerSegment(customer);
////		List<String> paymentMethods = new ArrayList<>();
////		paymentMethods.add("card");
////		paymentMethods.add("dcb");
////		paymentMethods.add("points");
////		subscription.setPaymentMethod(paymentMethods);
//		subscriptionCatalog = Optional.of(subscription);
////		when(subscriptionCatalogRepository.findByIdAndStatusAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get(), program.toLowerCase())).thenReturn(subscriptionCatalog);
//		
//		Optional<Subscription> subscriptions = Optional.empty();
//		when(subscriptionDomain.findBySubscriptionCatalogIdAndAccountNumberAndProgramCode(subscriptionRequestDto.getSubscriptionCatalogId(), subscriptionRequestDto.getAccountNumber(), program.toLowerCase())).thenReturn(subscriptions);
//		
//		GetMemberResponse getMemberResponse = new GetMemberResponse();
//		getMemberResponse.setMembershipCode("1000L");
//		getMemberResponse.setTierLevelName("Tier");
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		getMemberResponse.setCustomerType(customerType);
//		
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenReturn(getMemberResponse);
//		
//		resultResponse = subscriptionManagementController.createSubscription(subscriptionRequestDto, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUnsubscribeSubscriptionValidationFail() {
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.unsubscribeSubscription("id", "status", "", authorization, externalTransactionId, "", sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUnsubscribeSubscriptionPresent() {
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		Optional<Subscription> subscription;
//		Subscription subs = new Subscription();
//		subs.setId("Id");
//		subscription = Optional.of(subs);
//		when(subscriptionRepository.findByIdAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(subscription);
//		resultResponse = subscriptionManagementController.unsubscribeSubscription("id", "status", "string", authorization, externalTransactionId, "string", sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUnsubscribeSubscriptionCatalogFoundInvalid() {
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		Optional<Subscription> subscription;
//		Subscription subs = new Subscription();
//		subs.setId("Id");
//		subs.setSubscriptionCatalogId("CatalogId");
//		subscription = Optional.of(subs);
//		Optional<SubscriptionCatalog> subscriptionCatalog;
//		SubscriptionCatalog catalog = new SubscriptionCatalog();
//		catalog.setChargeabilityType("Charge");
//		subscriptionCatalog = Optional.of(catalog);
//		when(subscriptionRepository.findByIdAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(),
//				Mockito.anyString())).thenReturn(subscription);
//		when(subscriptionCatalogRepository.findByIdAndChargeabilityType("CatalogId", "auto-renewable")).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.unsubscribeSubscription("id", "status", "string",
//				authorization, externalTransactionId, "string", sessionId, userPrev, channelId, systemId,
//				systemPassword, token, transactionId);
//		assertNull(resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUnsubscribeSubscriptionCatalogFoundValid() {
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		Optional<Subscription> subscription;
//		Subscription subs = new Subscription();
//		subs.setId("Id");
//		subs.setSubscriptionCatalogId("CatalogId");
//		subs.setStatus("Subscribed");
//		subscription = Optional.of(subs);
//		Optional<SubscriptionCatalog> subscriptionCatalog;
//		SubscriptionCatalog catalog = new SubscriptionCatalog();
//		catalog.setChargeabilityType("Charge");
//		subscriptionCatalog = Optional.of(catalog);
//		when(subscriptionRepository.findByIdAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(),
//				Mockito.anyString())).thenReturn(subscription);
//		when(subscriptionCatalogRepository.findByIdAndChargeabilityType("CatalogId", "auto-renewable")).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.unsubscribeSubscription("id", "Unsubscribed", "string",
//				authorization, externalTransactionId, "string", sessionId, userPrev, channelId, systemId,
//				systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUnsubscribeSubscriptionException() {
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		Optional<Subscription> subscription;
//		Subscription subs = new Subscription();
//		subs.setId("Id");
//		subs.setSubscriptionCatalogId("CatalogId");
//		subs.setStatus("Subscribed");
//		subscription = Optional.of(subs);
//		Optional<SubscriptionCatalog> subscriptionCatalog;
//		SubscriptionCatalog catalog = new SubscriptionCatalog();
//		catalog.setChargeabilityType("Charge");
//		subscriptionCatalog = Optional.of(catalog);
//		when(subscriptionRepository.findByIdAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(),
//				Mockito.anyString())).thenThrow(NullPointerException.class);
//		when(subscriptionCatalogRepository.findByIdAndChargeabilityType("CatalogId", "auto-renewable")).thenReturn(subscriptionCatalog);
//		resultResponse = subscriptionManagementController.unsubscribeSubscription("id", "Unsubscribed", "string",
//				authorization, externalTransactionId, "string", sessionId, userPrev, channelId, systemId,
//				systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testUnsubscribeSubscriptionEmpty() {
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		Optional<Subscription> subscription = Optional.empty();
//		when(subscriptionRepository.findByIdAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(subscription);
//		resultResponse = subscriptionManagementController.unsubscribeSubscription("id", "status", "string", authorization, externalTransactionId, "string", sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionsValidationFail() {
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.listSubscriptions("", authorization, externalTransactionId, "", sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, "");
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionsNotFound() throws MarketplaceException, IOException, SubscriptionManagementException {
//		GetMemberResponse getMemberResponse = new GetMemberResponse();
//		getMemberResponse.setMembershipCode("1000L");
//		getMemberResponse.setTierLevelName("Tier");
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		customerType.add("Postpaid");
//		getMemberResponse.setCustomerType(customerType);
//		
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenReturn(getMemberResponse);
//		
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.listSubscriptions(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, "accountId");
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionsFound() throws MarketplaceException, IOException, SubscriptionManagementException {
//		GetMemberResponse getMemberResponse = new GetMemberResponse();
//		getMemberResponse.setMembershipCode("1000L");
//		getMemberResponse.setTierLevelName("Tier");
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		customerType.add("Postpaid");
//		getMemberResponse.setCustomerType(customerType);
//		
//		List<SubscriptionCatalog> activeSubscriptionCatalog = new ArrayList<>();
//		SubscriptionCatalog catalog = new SubscriptionCatalog();
//		catalog.setCustomerSegment(customerType);
//		activeSubscriptionCatalog.add(catalog);
//		
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenReturn(getMemberResponse);
//		when(subscriptionCatalogRepository.findByStatusAndProgramCode(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get(), program.toLowerCase())).thenReturn(activeSubscriptionCatalog);
//		
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.listSubscriptions(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, "accountId");
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionsNotEmpty() throws MarketplaceException, IOException, SubscriptionManagementException {
//		GetMemberResponse getMemberResponse = new GetMemberResponse();
//		getMemberResponse.setMembershipCode("1000L");
//		getMemberResponse.setTierLevelName("Tier");
//		List<String> customerType = new ArrayList<>();
//		customerType.add("Prepaid");
//		customerType.add("Postpaid");
//		getMemberResponse.setCustomerType(customerType);
//		
//		List<SubscriptionCatalog> activeSubscriptionCatalog = new ArrayList<>();
//		SubscriptionCatalog catalog = new SubscriptionCatalog();
//		catalog.setCustomerSegment(customerType);
//		activeSubscriptionCatalog.add(catalog);
//		
//		List<Subscription> subscribedSubscription = new ArrayList<>();
//		Subscription sub = new Subscription();
//		sub.setAccountNumber("Account");
//		sub.setCost(100);
//		sub.setId("Id");
//		sub.setSubscriptionCatalogId("Catalog ID");
//		subscribedSubscription.add(sub);
//		
//		SubscriptionResponseDto subscriptionResponseDto = new SubscriptionResponseDto();
//		subscriptionResponseDto.setSubscriptionCatalogId("Catalog ID");
//		subscriptionResponseDto.setAccountNumber("Account Number");
//		
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenReturn(getMemberResponse);
//		when(subscriptionCatalogRepository.findByStatusAndProgramCode(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get(), program.toLowerCase())).thenReturn(activeSubscriptionCatalog);
//		when(subscriptionRepository.findByAccountNumberAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(subscribedSubscription);
//		when(modelMapper.map(sub, SubscriptionResponseDto.class)).thenReturn(subscriptionResponseDto);
//		
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.listSubscriptions(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, "accountId");
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionsMemberUnavailable() throws MarketplaceException, IOException, SubscriptionManagementException {
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenReturn(null);
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.listSubscriptions(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, "accountId");
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionsMarketplaceException() throws MarketplaceException, IOException, SubscriptionManagementException {
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenThrow(MarketplaceException.class);
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.listSubscriptions(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, "accountId");
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubscriptionsException() throws MarketplaceException, IOException, SubscriptionManagementException {
//		when(membermngmtService.getMemberDetails(Mockito.anyString(), Mockito.any(ResultResponse.class))).thenThrow(NullPointerException.class);
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.listSubscriptions(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId, "accountId");
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testIsSubscriptionsValidationFail() {
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.subscribed("Account Number", "", authorization, externalTransactionId, "", sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testIsSubscriptionsInvalidParameters() {
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.subscribed(null, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertNotNull(resultResponse.getApiStatus().getErrors());
//	}
//	
//	@Test
//	public void testIsSubscriptionsNotEmpty() throws MarketplaceException, IOException, SubscriptionManagementException {
//		List<Subscription> subscribedSubscription = new ArrayList<>();
//		Subscription sub = new Subscription();
//		sub.setAccountNumber("Account");
//		sub.setCost(100);
//		sub.setId("Id");
//		sub.setSubscriptionCatalogId("Catalog ID");
//		subscribedSubscription.add(sub);
//		
//		SubscriptionResponseDto subscriptionResponseDto = new SubscriptionResponseDto();
//		subscriptionResponseDto.setSubscriptionCatalogId("Catalog ID");
//		subscriptionResponseDto.setAccountNumber("Account Number");
//		
//		when(subscriptionRepository.findByAccountNumberAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(subscribedSubscription);
//		when(modelMapper.map(sub, SubscriptionResponseDto.class)).thenReturn(subscriptionResponseDto);
//		
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.subscribed("Account", program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testIsSubscriptionsEmpty() throws MarketplaceException, IOException, SubscriptionManagementException {
//		List<Subscription> subscribedSubscription = new ArrayList<>();
//		when(subscriptionRepository.findByAccountNumberAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(subscribedSubscription);
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.subscribed("Account", program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getId(), resultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testIsSubscriptionsException() throws MarketplaceException, IOException, SubscriptionManagementException {
//		List<Subscription> subscribedSubscription = new ArrayList<>();
//		Subscription sub = new Subscription();
//		sub.setAccountNumber("Account");
//		sub.setCost(100);
//		sub.setId("Id");
//		sub.setSubscriptionCatalogId("Catalog ID");
//		subscribedSubscription.add(sub);
//		when(subscriptionRepository.findByAccountNumberAndStatusAndProgramCode(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(subscribedSubscription);
//		when(modelMapper.map(sub, SubscriptionResponseDto.class)).thenThrow(NullPointerException.class);
//		SubscriptionResultResponse resultResponse = new SubscriptionResultResponse(externalTransactionId);
//		resultResponse = subscriptionManagementController.subscribed("Account", program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(), resultResponse.getResult().getResponse());
//	}
//	
//}
