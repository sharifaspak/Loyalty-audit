//package com.loyalty.marketplace.offers.inbound.restcontroller;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
//import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
//import com.loyalty.marketplace.offers.inbound.dto.OfferTypeDto;
//import com.loyalty.marketplace.offers.inbound.dto.OfferTypeRequestDto;
//import com.loyalty.marketplace.offers.inbound.dto.SubCategoryDto;
//import com.loyalty.marketplace.offers.inbound.dto.SubCategoryRequestDto;
//import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
//import com.loyalty.marketplace.offers.outbound.database.entity.OfferTypeDescription;
//import com.loyalty.marketplace.offers.outbound.database.repository.OfferTypeRepository;
//import com.loyalty.marketplace.offers.outbound.dto.OfferTypeResultResponse;
//import com.loyalty.marketplace.offers.outbound.dto.SubCategoryResultResponse;
//import com.loyalty.marketplace.outbound.database.entity.Category;
//import com.loyalty.marketplace.outbound.database.entity.CategoryName;
//import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
//import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
//import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
//
//
//@SpringBootTest(classes=OffersConfigurationController.class)
//@AutoConfigureMockMvc
//@ActiveProfiles("unittest")
//@EnableWebMvc
//public class OffersConfigurationControllerTest {
//	
//	@Mock
//	ModelMapper modelMapper;
//	
//	@Mock
//	OfferTypeRepository offerTypeRepository;
//
//	@Mock
//	CategoryRepository categoryRepository;
//
//	@Mock
//	PaymentMethodRepository paymentMethodRepository;
//	
//	@InjectMocks
//	OffersConfigurationController offersConfigurationController;
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
//	
//	private OfferTypeRequestDto offerTypeRequest;
//	private List<OfferTypeDto> offerTypes;
//	private OfferTypeDto offerTypeDto;
//	private List<String> pMethods;
//	
//	private SubCategoryRequestDto subCategoryRequest;
//	private List<SubCategoryDto> subCategories;
//	private SubCategoryDto subCategoryDto;
//	
//	private List<String> existingOfferType;
//	
//	private List<OfferType> offerTypeList;
//	private OfferType offerType;
//	private OfferTypeDescription offerDescription;
//	
//	private PaymentMethod paymentMethod;
//	private List<PaymentMethod> paymentMethods;
//	
//	private SimpleDateFormat simpleDateFormat;
//	private Date dtCreated;
//	private Date dtUpdated;
//	
//	private Category category;
//	private CategoryName categoryName;
//	private Category subCategory;
//	private List<Category> categoryList;
//	private List<Category> subCategoryList;
//		
//	@Before
//	public void setUp() throws ParseException {
//		MockitoAnnotations.initMocks(this);
//
//		program = "Smiles";
//		authorization = "authorization";
//		externalTransactionId = "externalTransactionId";
//		userName = "userName";
//		sessionId = "sessionId";
//		userPrev = "userPrev";
//		channelId = "channelId";
//		systemId = "systemId";
//		systemPassword = "systemPassword";
//		token = "token";
//		transactionId = "transactionId";
//		
//		String pattern = "yyyy-MM-dd HH:MM:ss";
//		simpleDateFormat = new SimpleDateFormat(pattern);
//		dtCreated = simpleDateFormat.parse("2018-12-23 17:33:55");
//		dtUpdated = simpleDateFormat.parse("2018-12-23 17:38:55");
//				
//		pMethods = new ArrayList<>();
//		pMethods.add("1");
//		pMethods.add("2");
//		pMethods.add("3");
//		
//		offerTypeDto = new OfferTypeDto();
//		offerTypeDto.setOfferTypeId("5");
//		offerTypeDto.setDescriptionEn("offerTypeDtoDescriptionEn");
//		offerTypeDto.setDescriptionAr("offerTypeDtoDescriptionAr");
//		offerTypeDto.setPaymentMethods(pMethods);
//		
//		offerTypes = new ArrayList<>();
//		offerTypes.add(offerTypeDto);
//		
//		offerTypeRequest = new OfferTypeRequestDto();
//		offerTypeRequest.setOfferTypes(offerTypes);
//		
//		subCategories = new ArrayList<>();
//		subCategoryDto = new SubCategoryDto();
//		subCategories.add(subCategoryDto);
//		
//		subCategoryRequest = new SubCategoryRequestDto();
//		subCategoryRequest.setSubCategories(subCategories);
//		
//		
//		paymentMethod = new PaymentMethod();
//		paymentMethod.setPaymentMethodId("1");
//		paymentMethod.setDescription("methodDescription1");
//		paymentMethod.setUsrCreated("");
//		paymentMethod.setUsrUpdated("");
//		paymentMethod.setDtCreated(dtCreated);
//		paymentMethod.setDtUpdated(dtUpdated);
//		
//		paymentMethods = new ArrayList<>();
//		paymentMethods.add(paymentMethod);
//		
//		
//		offerType = new OfferType();
//		offerType.setOfferTypeId("2");
//		offerDescription = new OfferTypeDescription();
//		offerDescription.setTypeDescriptionEn("typeDescriptionEn");
//		offerDescription.setTypeDescriptionAr("typeDescriptionAr");
//		offerType.setOfferDescription(offerDescription);
//		offerType.setPaymentMethods(paymentMethods);
//		offerType.setUsrCreated("John");
//		offerType.setUsrUpdated("John");
//		offerType.setDtCreated(dtCreated);
//		offerType.setDtUpdated(dtUpdated);
//		
//		offerTypeList = new ArrayList<>();
//		offerTypeList.add(offerType);
//		
//		existingOfferType = new ArrayList<>();
//		existingOfferType.add("typeDescriptionEn1");
//		
//		category = new Category();
//		category.setCategoryId("categoryId");
//		categoryName = new CategoryName();
//		categoryName.setCategoryNameEn("categoryNameEn");
//		categoryName.setCategoryNameAr("categoryNameAr");
//		category.setCategoryName(categoryName);
//		category.setUsrCreated("usrCreated");
//		category.setUsrUpdated("usrUpdated");
//		category.setDtCreated(dtCreated);
//		category.setDtUpdated(dtUpdated);
//		
//		subCategory = new Category();
//		subCategory.setCategoryId("1");
//		subCategory.setCategoryName(new CategoryName());
//		subCategory.setParentCategory(category);
//		subCategory.setUsrCreated("usrCreated");
//		subCategory.setUsrUpdated("usrUpdated");
//		subCategory.setDtCreated(dtCreated);
//		subCategory.setDtUpdated(dtUpdated);
//		subCategory.setCategoryId("SubCategoryId");
//		
//		categoryList = new ArrayList<>();
//		categoryList.add(category);
//		
//		subCategoryList = new ArrayList<>();
//		subCategoryList.add(subCategory);
//		
//
//	}
//	
//	/**
//	 * Testing configureOfferType
//	 */
//	
////	@Test
////	public void testconfigureOfferTypeSuccess(){
////		
////		when(offerTypeRepository.findAll()).thenReturn(offerTypeList);
////		
////		resultResponse = offersConfigurationController.configureOfferType(offerTypeRequest, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
////		assertEquals(OfferSuccessCodes.OFFERTYPE_ADDED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
////	}
//	
////	@Test
////	public void testconfigureOfferTypeDuplicateType(){
////		
////		Errors error = new Errors();
////		error.setCode(1);
////		error.setMessage("Message");
////		errorList.add(error);
////		
////		when(offerTypeRepository.findAll()).thenReturn(offerTypeList);
////		when(offerControllerHelper.setOfferTypeToSave(program, existingOfferType, offerTypeDto, userName, resultResponse)).thenReturn(offerType);
////		when(offerTypeRepository.saveAll(offerTypeList)).thenThrow(NullPointerException.class);
////		
////		resultResponse = offersConfigurationController.configureOfferType(offerTypeRequest, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
////		assertEquals(OfferSuccessCodes.OFFERTYPE_ADDED_SUCCESSFULLY.getId(), resultResponse.getResult().getResponse());
////	}
//	
////	@Test
////	public void testconfigureOfferTypeEmptyRequest(){
////		
////		offerTypeRequest = new OfferTypeRequestDto();
////		
////		resultResponse = offersConfigurationController.configureOfferType(offerTypeRequest, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
////		assertEquals(OfferErrorCodes.EMPTY_OFFERTYPE_LIST.getId(), resultResponse.getResult().getResponse());
////	}
//	
//	
//	/**
//	 * Testing listOfferType
//	 */
//	
////	@Test
////	public void testListOfferTypeSuccess(){
////		
////		when(offerTypeRepository.findAll()).thenReturn(offerTypeList);
////		
////		OfferTypeResultResponse offerTypeResponse = offersConfigurationController.listOfferType(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
////		assertEquals(OfferSuccessCodes.OFFER_TYPE_LISTED_SUCCESSFULLY.getId(), offerTypeResponse.getResult().getResponse());
////	}
//	
//	@Test
//	public void testListOfferTypeEmptyList(){
//		
//		offerTypeList = new ArrayList<>();
//		when(offerTypeRepository.findAll()).thenReturn(offerTypeList);
//		
//		OfferTypeResultResponse offerTypeResponse = offersConfigurationController.listOfferType(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(OfferErrorCodes.OFFER_TYPE_LISTING_FAILED.getId(), offerTypeResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListOfferTypeException(){
//		
//		when(offerTypeRepository.findAll()).thenThrow(NullPointerException.class);
//		
//		OfferTypeResultResponse offerTypeResponse = offersConfigurationController.listOfferType(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
//		assertEquals(OfferErrorCodes.OFFER_TYPE_LISTING_FAILED.getId(), offerTypeResponse.getResult().getResponse());
//	}
//	
//	/**
//	 * Testing configureSubCategories
//	 */
//	
////	@Test
////	public void testConfigureSubCategoriesSuccess(){
////		
////		String categoryId = subCategory.getParentCategory().getCategoryId();
////		
////		when(categoryRepository.findAll()).thenReturn(categoryList);
////		when(categoryRepository.findByCategoryId(categoryId)).thenReturn(category);
////		when(categoryRepository.saveAll(subCategoryList)).thenReturn(subCategoryList);
////		
////		resultResponse = offersConfigurationController.configureSubCategories(subCategoryRequest, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
////		assertEquals(OfferSuccessCodes.SUBCATEGORY_ADDED_SUSSESSFULY.getId(), resultResponse.getResult().getResponse());
////	}
//	
////	@Test
////	public void testConfigureSubCategoriesDuplicateType(){
////		
////		Errors error = new Errors();
////		error.setCode(1);
////		error.setMessage("message");
////		errorList.add(error);
////		
////		String categoryId = subCategory.getParentCategory().getCategoryId();
////		
////		when(categoryRepository.findAll()).thenReturn(subCategoryList);
////		when(categoryRepository.findByCategoryId(categoryId)).thenReturn(category);
////		when(categoryRepository.saveAll(subCategoryList)).thenThrow(NullPointerException.class);
////		
////		resultResponse = offersConfigurationController.configureSubCategories(subCategoryRequest, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
////		assertEquals(OfferErrorCodes.SUBCATEGORY_WITH_DUPLICATES.getId(), resultResponse.getResult().getResponse());
////	}
//	
////	@Test
////	public void testConfigureSubCategoriesEmptyRequest(){
////		
////		subCategoryRequest = new SubCategoryRequestDto();
////		
////		resultResponse = offersConfigurationController.configureSubCategories(subCategoryRequest, program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId);
////		assertEquals(OfferErrorCodes.EMPTY_SUBCATEGORY_LIST.getId(), resultResponse.getResult().getResponse());
////	}
//	
//	
//	/**
//	 * Testing listSubCategory
//	 */
//	
//	@Test
//	public void testListSubCategorySuccess(){
//		
//		when(categoryRepository.findByCategoryId(category.getCategoryId())).thenReturn(category);
//		when(categoryRepository.findByParentCategory(category)).thenReturn(subCategoryList);
//		
//		SubCategoryResultResponse subCategoryResultResponse = offersConfigurationController.listSubCategory(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, category.getCategoryId());
//		assertEquals(OfferSuccessCodes.SUBCATEGORY_LISTED_SUCCESSFULLY.getId(), subCategoryResultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubCategoryEmptyList(){
//		
//		
//		when(categoryRepository.findByParentCategory(category)).thenReturn(categoryList);
//		
//		SubCategoryResultResponse subCategoryResultResponse = offersConfigurationController.listSubCategory(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, category.getCategoryId());
//		assertEquals(OfferErrorCodes.SUBCATEGORY_LISTING_FAILED.getId(), subCategoryResultResponse.getResult().getResponse());
//	}
//	
//	@Test
//	public void testListSubCategoryException(){
//		
//		
//		when(categoryRepository.findByCategoryId(category.getCategoryId())).thenThrow(NullPointerException.class);
//		
//		
//		SubCategoryResultResponse subCategoryResultResponse = offersConfigurationController.listSubCategory(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, externalTransactionId, category.getCategoryId());
//		assertEquals(OfferErrorCodes.SUBCATEGORY_LISTING_FAILED.getId(), subCategoryResultResponse.getResult().getResponse());
//	}
//	
//}
