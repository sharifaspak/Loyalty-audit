package com.loyalty.marketplace.interest.domain.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.interest.constants.InterestErrorCodes;
import com.loyalty.marketplace.interest.constants.InterestExceptionCodes;
import com.loyalty.marketplace.interest.inbound.dto.InterestIdRequestDto;
import com.loyalty.marketplace.interest.inbound.dto.InterestRequestDto;
import com.loyalty.marketplace.interest.outbound.dto.CategoriesInterestDto;
import com.loyalty.marketplace.interest.outbound.dto.CategoryDto;
import com.loyalty.marketplace.interest.outbound.dto.GetCustomerInterestResponse;
import com.loyalty.marketplace.interest.outbound.dto.InterestResponseResult;
import com.loyalty.marketplace.interest.outbound.entity.CustomerInterestEntity;
import com.loyalty.marketplace.interest.outbound.entity.InterestEntity;
import com.loyalty.marketplace.interest.repository.CategotyRepository;
import com.loyalty.marketplace.interest.repository.CustomerInterest;
import com.loyalty.marketplace.interest.repository.InterestRepository;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.constants.VoucherStatus;

@Component
public class InterestDomain {
	private static final Logger log = LoggerFactory.getLogger(InterestDomain.class);

	@Autowired
	InterestRepository interestRepo;
	@Autowired
	CustomerInterest customerInterestRepo;
	@Autowired
	CategotyRepository categotyRepo;
	@Autowired
	MemberManagementService membermngmtService;
	@Autowired
	FetchServiceValues fetchServiceValues;
	@Autowired
	MongoOperations mongoOperations;

	private void persistCustomerInterest(String userName, String accountNumber,InterestRequestDto interestRequestDt,
			Optional<CustomerInterestEntity> interestEntity) {

		CustomerInterestEntity customerInterestEntity = null;

		if (null == userName) {
			userName = "Test";
		}
		if (interestEntity.isPresent()) {
			customerInterestEntity = interestEntity.get();
		} else {
			customerInterestEntity = new CustomerInterestEntity();
			customerInterestEntity.setAccountNumber(accountNumber);	
			customerInterestEntity.setCreatedUser(userName);
			customerInterestEntity.setCreatedDate(new Date());
			
		}
		List<String> inputInterest = new ArrayList<>();
		for (InterestIdRequestDto intId : interestRequestDt.getSelectedInterests()) {
			inputInterest.add(intId.getInterestId());
		}
		customerInterestEntity.setInterestId(inputInterest);
		customerInterestEntity.setUpdatedUser(userName);
		customerInterestEntity.setUpdatedDate(new Date());
		customerInterestRepo.save(customerInterestEntity);
	
	}
	public InterestResponseResult updateInterest(String accountNumber, InterestRequestDto interestRequestDt,
			String externalTransactionId, String userName, Headers headers) throws MarketplaceException, IOException {

		InterestResponseResult interestResponseResult = new InterestResponseResult(externalTransactionId);
		if (!validateSelectedInterests(interestRequestDt.getSelectedInterests())) {
			interestResponseResult.addErrorAPIResponse(InterestExceptionCodes.INTEREST_NOT_FOUND_EXCEPTION.getIntId(),
					InterestExceptionCodes.INTEREST_NOT_FOUND_EXCEPTION.getMsg());
			return interestResponseResult;
		}

		ResultResponse result = new ResultResponse(externalTransactionId);
		GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, result,headers);//Add headers instead of null
		if (null == memberDetails) {
			interestResponseResult.setErrorAPIResponse(InterestErrorCodes.ACCOUNT_NOT_FOUND.getIntId(),
					InterestErrorCodes.ACCOUNT_NOT_FOUND.getMsg());
			return interestResponseResult;
		} else {
			Optional<CustomerInterestEntity> interestEntity = customerInterestRepo.findByAccountNumber(accountNumber);
			if (null != interestRequestDt.getSelectedInterests() && !interestRequestDt.getSelectedInterests().isEmpty()) {
				persistCustomerInterest(userName, accountNumber, interestRequestDt, interestEntity);
			}else {
				if (interestEntity.isPresent()) {
					CustomerInterestEntity customerInterestEntity = interestEntity.get();
					customerInterestRepo.delete(customerInterestEntity);
				}
			}
			
		}

		try {
			interestResponseResult = getInterestDetails(accountNumber, externalTransactionId, false, headers,0, Integer.MAX_VALUE);
		} catch (Exception e) {
			interestResponseResult.addErrorAPIResponse(InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					InterestErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			interestResponseResult.addErrorAPIResponse(InterestErrorCodes.NO_INTEREST_TO_DISPLAY.getIntId(),
					InterestErrorCodes.NO_INTEREST_TO_DISPLAY.getMsg());
			log.error(new MarketplaceException(this.getClass().toString(), "getInterest", e.getClass() + e.getMessage(),
					OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		return interestResponseResult;
	}

	private boolean validateSelectedInterests(List<InterestIdRequestDto> selectedInterests) {
		boolean isInputValid = false;
		if (null == selectedInterests) {
			return false;
		}
		if (!selectedInterests.isEmpty()) {
			List<String> interestListInput = selectedInterests.stream().map(InterestIdRequestDto::getInterestId)
					.collect(Collectors.toList());
			List<InterestRepository> interestListFromDB = interestRepo.findByIdIn(interestListInput);
			if (interestListInput.size() == interestListFromDB.size()) {
				isInputValid = true;
			}
		}else {
			isInputValid = true;
		}
		return isInputValid;
	}
	public boolean insert() {
		CategoryName name = new CategoryName();
		name.setCategoryNameAr("arabic");
		name.setCategoryNameEn("english");
		Category category = new Category();
		category.setCategoryId("10");
		Category subCategory = new Category();
		subCategory.setCategoryId("20");
		InterestEntity interest = new InterestEntity();
		interest.setImageUrl("Test1");
		interest.setCategoryName(name);
		interest.setProgramCode("200");
		interest.setCategoryId(category);
		interest.setSubCategoryId(subCategory);
		interest.setUpdatedUser("Test2");
		interest.setCreatedUser("Test2");
		interest.setCreatedDate(new Date());
		interest.setUpdatedDate(new Date());

		interestRepo.save(interest);

		return true;
	}

	public boolean customerInterestInsert() {
		CustomerInterestEntity customerInterestEntity = new CustomerInterestEntity();
		List<String> list = new ArrayList<>();
		list.add("4");
		list.add("5");
		list.add("6");
		//customerInterestEntity.setId(1);
		customerInterestEntity.setAccountNumber("12367");
		customerInterestEntity.setCreatedUser("Test1");
		customerInterestEntity.setCreatedDate(new Date());
		customerInterestEntity.setUpdatedDate(new Date());
		customerInterestEntity.setUpdatedUser("Test1");
		customerInterestEntity.setInterestId(list);

		customerInterestRepo.save(customerInterestEntity);

		return true;
	}

	public boolean insertCategory() {
		Category cat = new Category();
		cat.setCategoryId("20");
		CategoryName name = new CategoryName();
		name.setCategoryNameAr("التسوق");
		name.setCategoryNameEn("Shopping Offers");
		Category entity = new Category();
		entity.setCategoryId("10");
		entity.setCategoryName(name);
		entity.setDtCreated(new Date());
		entity.setDtUpdated(new Date());
		entity.setUsrUpdated("Test");
		entity.setUsrCreated("Test");
		entity.setParentCategory(cat);
		categotyRepo.save(entity);
		return true;
	}

	public InterestResponseResult getInterestDetails(String accountNumber,String externalTransactionId,boolean isMemberCallRequired, Headers headers, Integer page, Integer limit) throws Exception {
		log.info("Entered getInterestDetails().");
		GetCustomerInterestResponse userCategoriesInterest = new GetCustomerInterestResponse();
		InterestResponseResult interestResponseResult = new InterestResponseResult(externalTransactionId);
		List<CategoriesInterestDto> categoriesInterestList = new ArrayList<>();
		ResultResponse result = new ResultResponse(externalTransactionId);
		if(isMemberCallRequired) {
		GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, result, headers);//Add headers here
		if(null==memberDetails) {
			interestResponseResult.setErrorAPIResponse(InterestErrorCodes.
					ACCOUNT_NOT_FOUND.getIntId(), InterestErrorCodes.ACCOUNT_NOT_FOUND.getMsg());
			return interestResponseResult;
		}
		}
		if(limit==null && page==null) {
			limit=Integer.MAX_VALUE;
			page=0;
		}
		else if(limit==null) {
			limit = Integer.MAX_VALUE;
		}
		else if(page == null) {
			page = 0;
		}
		Pageable pageNumberWithElements = PageRequest.of(page,limit);
		//Page<InterestEntity> interestEntityPage = interestRepo.findAll(pageNumberWithElements);
		//List<InterestEntity> interestEntityList=	interestEntityPage.getContent().isEmpty()?null:interestEntityPage.getContent();
		
		//Loyalty as a service.
		Criteria programCriteria = Criteria.where("ProgramCode").regex(headers.getProgram(), "i");
		
		Query query = new Query();
		query.with(Sort.by(Direction.ASC, "CategoryId.$id"));
		query.with(pageNumberWithElements);
		//Loyalty as a service.
		query.addCriteria(programCriteria);
		List<InterestEntity> interestEntityList=  mongoOperations.find(query, InterestEntity.class);		
		if(interestEntityList==null || interestEntityList.isEmpty()) {
			interestResponseResult.addErrorAPIResponse(InterestExceptionCodes.
					INTEREST_NOT_FOUND_EXCEPTION.getIntId(),
					InterestExceptionCodes.INTEREST_NOT_FOUND_EXCEPTION.getMsg()); 
			return interestResponseResult;
			}
		
		//List<Category> categoryEntity = categotyRepo.findAll();
		List<Category> categoryEntity = categotyRepo.findByProgramCodeIgnoreCase(headers.getProgram());
		
		//Optional<CustomerInterestEntity> customerInterestEntity = customerInterestRepo.findByAccountNumber(accountNumber);
		Optional<CustomerInterestEntity> customerInterestEntity = customerInterestRepo.findByAccountNumberAndProgramCodeIgnoreCase(accountNumber, headers.getProgram());

//			for (InterestEntity interestEntity : interestEntityList) {
//				Category categoryId = interestEntity.getCategoryId();
//				Category subCategoryId = interestEntity.getSubCategoryId();
//				Optional<Category> categoryEntity = categotyRepo.findByCategoryId(categoryId.getCategoryId());
//				Optional<Category> subCategoryEntity = categotyRepo.findByCategoryId(subCategoryId.getCategoryId());
//				CategoriesInterestDto categoriesInterest = populate(interestEntity,customerInterestEntity,categoryEntity,subCategoryEntity);
//				categoriesInterestList.add(categoriesInterest);
//				}
		
		for (InterestEntity interestEntity : interestEntityList) {				
			CategoriesInterestDto categoriesInterest = populate(interestEntity,customerInterestEntity,
					categoryEntity.stream().filter(c->c.getCategoryId().equalsIgnoreCase(interestEntity.getCategoryId().getCategoryId())).findAny(),
					categoryEntity.stream().filter(c->c.getCategoryId().equalsIgnoreCase(interestEntity.getSubCategoryId().getCategoryId())).findAny());
			
			categoriesInterestList.add(categoriesInterest);
		}
			
		userCategoriesInterest.setInterestList(categoriesInterestList);
		userCategoriesInterest.setTotalRecords(interestEntityList.size());
		interestResponseResult.setResult(userCategoriesInterest);
		
		return interestResponseResult;
	}

	public CategoriesInterestDto populate(InterestEntity interestEntity,
			Optional<CustomerInterestEntity> customerInterestEntity, Optional<Category> categoryEntity,
			Optional<Category> subCategoryEntity) {
		
		Category entity = categoryEntity.isPresent() ? categoryEntity.get() : null;
		Category subEntity = subCategoryEntity.isPresent() ? subCategoryEntity.get() : null;
		CategoriesInterestDto categoriesInterest = new CategoriesInterestDto();
		CategoryDto category = new CategoryDto();
		
		if(!ObjectUtils.isEmpty(entity)) {
			
			category.setCategoryId(entity.getCategoryId());
			category.setCategoryName(entity.getCategoryName().getCategoryNameEn());
			category.setCategoryNameAr(entity.getCategoryName().getCategoryNameAr());
		}	
		
		CategoryDto subCategory = new CategoryDto();
		if(!ObjectUtils.isEmpty(subEntity)) {
			
			subCategory.setCategoryId(subEntity.getCategoryId());
			subCategory.setCategoryName(subEntity.getCategoryName().getCategoryNameEn());
			subCategory.setCategoryNameAr(subEntity.getCategoryName().getCategoryNameAr());
		}	
		    
		categoriesInterest.setInterestId(interestEntity.getId());
		categoriesInterest.setInterestImageUrl(interestEntity.getImageUrl());
		categoriesInterest.setInterestNameAr(interestEntity.getCategoryName().getCategoryNameAr());
		categoriesInterest.setInterestName(interestEntity.getCategoryName().getCategoryNameEn());
			
		if(customerInterestEntity.isPresent()) {
		categoriesInterest.setInterestSelected(customerInterestEntity.get().getInterestId().contains(interestEntity.getId()));
		}else {
			categoriesInterest.setInterestSelected(false);
		}
		categoriesInterest.setCategory(category);
		categoriesInterest.setSubCategory(subCategory);

	   return categoriesInterest; 

	}
	
   public void resetDB() {
	   interestRepo.deleteAll();
   }
}
