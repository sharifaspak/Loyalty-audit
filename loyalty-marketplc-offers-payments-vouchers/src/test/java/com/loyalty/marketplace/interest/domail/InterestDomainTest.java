package com.loyalty.marketplace.interest.domail;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.loyalty.marketplace.interest.domain.model.InterestDomain;
import com.loyalty.marketplace.interest.inbound.dto.InterestIdRequestDto;
import com.loyalty.marketplace.interest.inbound.dto.InterestRequestDto;
import com.loyalty.marketplace.interest.outbound.dto.CategoriesInterestDto;
import com.loyalty.marketplace.interest.outbound.dto.InterestResponseResult;
import com.loyalty.marketplace.interest.outbound.entity.CustomerInterestEntity;
import com.loyalty.marketplace.interest.outbound.entity.InterestEntity;
import com.loyalty.marketplace.interest.repository.CategotyRepository;
import com.loyalty.marketplace.interest.repository.CustomerInterest;
import com.loyalty.marketplace.interest.repository.InterestRepository;
import com.loyalty.marketplace.offers.domain.model.ActivityCodeDomain;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;



@SpringBootTest(classes=ActivityCodeDomain.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class InterestDomainTest {
	@Mock
	ModelMapper modelMapper;
    @Mock
 	InterestRepository interestRepo;
    @Mock
 	CustomerInterest customerInterestRepo;
    @Mock
 	CategotyRepository categotyRepo;
	/*
	 * @Mock InterestDomain interestDomain;
	 */
    @InjectMocks
    InterestDomain interestDomain;
    
    
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
	private String accountNumber;

	 Optional<CustomerInterestEntity> customerInterestEntity;
	 Optional<Category> categoryEntity;
	 Optional<Category> subCategoryEntity;
	 InterestResponseResult interestResultResponse = new InterestResponseResult(externalTransactionId);
	 List<InterestEntity> interestEntityList = new ArrayList<InterestEntity>();
	 InterestIdRequestDto interestIdRequestDto = new InterestIdRequestDto();
	 InterestRequestDto interestRequestDto = new InterestRequestDto();
	
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
		transactionId = "transactionId";
		accountNumber = "123456";
		
		Category category = new Category();
    	category.setCategoryId("1234");
    	category.setDtCreated(new Date());
    	
    	Category subCategory = new Category();
    	subCategory.setCategoryId("1234");
    	subCategory.setDtCreated(new Date());
    	
    	CategoryName name = new CategoryName();
        name.setCategoryNameAr("Ar");
        name.setCategoryNameEn("Eng");
    	category.setCategoryName(name);
        subCategory.setCategoryName(name);
        
    	InterestEntity entity = new InterestEntity();
    	entity.setId("1234");
    	entity.setCategoryId(category);
    	entity.setSubCategoryId(subCategory);
    	entity.setImageUrl("Test");
    	entity.setProgramCode("Test");
    	entity.setCategoryName(name);
    	
    	InterestEntity entity1 = new InterestEntity();
    	entity1.setId("12345");
    	entity1.setCategoryId(category);
    	entity1.setSubCategoryId(subCategory);
    	entity1.setImageUrl("Test");
    	entity1.setProgramCode("Test");
    	entity1.setCategoryName(name);
    	
    	
    	interestEntityList.add(entity);
    	interestEntityList.add(entity1);
    	
    	Mockito.when(interestRepo.findAll()).thenReturn(interestEntityList); 
    	CustomerInterestEntity customerEntity = new CustomerInterestEntity();
    	customerEntity.setAccountNumber(accountNumber);
    	customerEntity.setId("123");
    	customerEntity.setCreatedUser("Test");
    	List<String> interestIds = new ArrayList<>();
    	interestIds.add("123");
    	interestIds.add("124");
    	customerEntity.setInterestId(interestIds);
//    	customerEntity.setCretatedDate(new Date());
    	
    	customerInterestEntity = Optional.of(customerEntity);
    
    	
    	
    	
    	categoryEntity = Optional.of(category);
    	subCategoryEntity = Optional.of(subCategory);
    	
    	List<CategoriesInterestDto> categoriesInterestList = new ArrayList<>();
    	CategoriesInterestDto categoriesInterestDto = new CategoriesInterestDto();
    	categoriesInterestDto.setInterestId("12345");
    	categoriesInterestDto.setInterestImageUrl("Test");
    	categoriesInterestDto.setInterestName(entity.getCategoryName().getCategoryNameEn());
    	categoriesInterestDto.setInterestNameAr(entity.getCategoryName().getCategoryNameAr());
    	categoriesInterestDto.setInterestSelected(true);
    	CategoriesInterestDto categoriesInterestDto1 = new CategoriesInterestDto();
    	categoriesInterestDto.setInterestId("123456");
    	categoriesInterestDto.setInterestImageUrl("Test");
    	categoriesInterestDto.setInterestName(entity1.getCategoryName().getCategoryNameEn());
    	categoriesInterestDto.setInterestNameAr(entity1.getCategoryName().getCategoryNameAr());
    	categoriesInterestDto.setInterestSelected(customerEntity.getInterestId().contains(entity.getId()));
    	categoriesInterestList.add(categoriesInterestDto);
    	categoriesInterestList.add(categoriesInterestDto1);
    	
     	interestResultResponse.setResult(categoriesInterestList);
     	
     	
  	    interestIdRequestDto.setInterestId("123");
  	    List<InterestIdRequestDto> selectedInterests = new ArrayList<>();
  	    
  	    interestRequestDto.setSelectedInterests(selectedInterests);
     	
	}
	/*
	 * @Test public void testGetInterestDetails() throws Exception{
	 * Mockito.when(interestRepo.findAll()).thenReturn(interestEntityList);
	 * Mockito.when(customerInterestRepo.findByAccountNumber(Mockito.anyString())).
	 * thenReturn(customerInterestEntity);
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * categoryEntity);
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * subCategoryEntity); interestResultResponse =
	 * interestDomain.getInterestDetails(accountNumber,externalTransactionId);
	 * assertNotNull(interestResultResponse); }
	 * 
	 * @Test public void testUpdateInterest() throws Exception{
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * categoryEntity);
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * subCategoryEntity);
	 * Mockito.when(customerInterestRepo.findByAccountNumber(Mockito.anyString())).
	 * thenReturn(customerInterestEntity); interestResultResponse =
	 * interestDomain.updateInterest(accountNumber,interestRequestDto,
	 * externalTransactionId,userName); assertNotNull(interestResultResponse); }
	 * 
	 * @Test public void testUpdateInterestAcc() throws Exception{
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * categoryEntity);
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * subCategoryEntity); interestResultResponse =
	 * interestDomain.updateInterest(accountNumber,interestRequestDto,
	 * externalTransactionId,userName); assertNotNull(interestResultResponse); }
	 * 
	 * @Test public void testUpdateInterestEx() throws Exception{
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * categoryEntity);
	 * Mockito.when(categotyRepo.findByCategoryId(Mockito.anyString())).thenReturn(
	 * subCategoryEntity);
	 * Mockito.when(customerInterestRepo.findByAccountNumber(Mockito.anyString())).
	 * thenReturn(customerInterestEntity); interestResultResponse =
	 * interestDomain.updateInterest(accountNumber,interestRequestDto,
	 * externalTransactionId,userName); assertNotNull(interestResultResponse); }
	 */
}