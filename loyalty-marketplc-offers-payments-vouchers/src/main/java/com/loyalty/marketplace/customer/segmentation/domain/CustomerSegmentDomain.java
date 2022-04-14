//package com.loyalty.marketplace.customer.segmentation.domain;
//
//import java.util.Date;
//import java.util.List;
//
//import javax.validation.ValidationException;
//import javax.validation.Validator;
//
//import org.modelmapper.ModelMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
//import com.loyalty.marketplace.audit.AuditService;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentConfigurationConstants;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentConstants;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentErrorCodes;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentExceptionCodes;
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentSuccessCodes;
//import com.loyalty.marketplace.customer.segmentation.helper.CustomerSegmentRepositoryHelper;
//import com.loyalty.marketplace.customer.segmentation.helper.dto.CustomerSegmentExceptionInfo;
//import com.loyalty.marketplace.customer.segmentation.helper.dto.CustomerSegmentHeaders;
//import com.loyalty.marketplace.customer.segmentation.input.dto.CustomerSegmentRequestDto;
//import com.loyalty.marketplace.customer.segmentation.ouput.database.entity.CustomerSegment;
//import com.loyalty.marketplace.customer.segmentation.utils.CustomerSegmentDomainConfiguration;
//import com.loyalty.marketplace.customer.segmentation.utils.CustomerSegmentResponses;
//import com.loyalty.marketplace.customer.segmentation.utils.CustomerSegmentValidator;
//import com.loyalty.marketplace.outbound.dto.ResultResponse;
//import com.loyalty.marketplace.utils.MarketplaceException;
//import com.mongodb.MongoWriteException;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//@Getter
//@NoArgsConstructor
//@ToString
//@Component
//public class CustomerSegmentDomain {
//	
//	private static final Logger LOG = LoggerFactory.getLogger(CustomerSegmentDomain.class);
//
//	@Getter(AccessLevel.NONE)
//	@Autowired
//	ModelMapper modelMapper;
//
//	@Getter(AccessLevel.NONE)
//	@Autowired
//	AuditService auditService;
//	
//	@Getter(AccessLevel.NONE)
//	@Autowired
//	ProgramManagement programManagement;
//	
//	@Getter(AccessLevel.NONE)
//	@Autowired
//	Validator validator;
//	
//	@Getter(AccessLevel.NONE)
//	@Autowired
//	CustomerSegmentRepositoryHelper repositoryHelper;
//	
//	private String id;
//	private String name;
//	private ListValuesDomain channelId;
//	private ListValuesDomain tierLevel;
//	private ListValuesDomain gender;
//	private ListValuesDomain nationality;
//	private ListValuesDomain numberType;
//	private ListValuesDomain accountStatus;
//	private ListValuesDomain emailVerificationStatus;
//	private ListValuesDomain days;
//	private ListValuesDomain customerType;
//	private ListValuesDomain cobrandedCards;
//	private IntegerValuesDomain totalTierPoints;
//	private IntegerValuesDomain totalAccountPoints;
//	private DateValuesDomain dob;
//	private DateValuesDomain accountStartDate;
//	private boolean isFirstAccess;
//	private boolean isCoBranded;
//	private boolean isSubscribed;
//	private boolean isPrimaryAccount;
//	private boolean isTop3Account;
//	private List<PurchaseValuesDomain> purchaseItems;
//	private Date createdDate;
//	private String createdUser;
//	private Date updatedDate;
//	private String updatedUser;
//	
//	public CustomerSegmentDomain(CustomerSegmentDomainBuilder customerSegment) {
//		
//		this.id = customerSegment.id;
//		this.name = customerSegment.name;
//		this.channelId = customerSegment.channelId;
//		this.tierLevel = customerSegment.tierLevel;
//		this.gender = customerSegment.gender;
//		this.nationality = customerSegment.nationality;
//		this.numberType = customerSegment.numberType;
//		this.accountStatus = customerSegment.accountStatus;
//		this.emailVerificationStatus = customerSegment.emailVerificationStatus;
//		this.days = customerSegment.days;
//		this.customerType = customerSegment.customerType;
//		this.cobrandedCards = customerSegment.cobrandedCards;
//		this.totalTierPoints = customerSegment.totalTierPoints;
//		this.totalAccountPoints = customerSegment.totalAccountPoints;
//		this.dob = customerSegment.dob;
//		this.accountStartDate = customerSegment.accountStartDate;
//		this.isFirstAccess = customerSegment.isFirstAccess;
//		this.isCoBranded = customerSegment.isCoBranded;
//		this.isSubscribed = customerSegment.isSubscribed;
//		this.isPrimaryAccount = customerSegment.isPrimaryAccount;
//		this.isTop3Account = customerSegment.isTop3Account;
//		this.purchaseItems = customerSegment.purchaseItems;
//		this.createdDate = customerSegment.createdDate;
//		this.createdUser = customerSegment.createdUser;
//		this.updatedDate = customerSegment.updatedDate;
//		this.updatedUser = customerSegment.updatedUser;
//	}
//	
//	public static class CustomerSegmentDomainBuilder{
//		
//		private String id;
//		private String name;
//		private ListValuesDomain channelId;
//		private ListValuesDomain tierLevel;
//		private ListValuesDomain gender;
//		private ListValuesDomain nationality;
//		private ListValuesDomain numberType;
//		private ListValuesDomain accountStatus;
//		private ListValuesDomain emailVerificationStatus;
//		private ListValuesDomain days;
//		private ListValuesDomain customerType;
//		private ListValuesDomain cobrandedCards;
//		private IntegerValuesDomain totalTierPoints;
//		private IntegerValuesDomain totalAccountPoints;
//		private DateValuesDomain dob;
//		private DateValuesDomain accountStartDate;
//		private boolean isFirstAccess;
//		private boolean isCoBranded;
//		private boolean isSubscribed;
//		private boolean isPrimaryAccount;
//		private boolean isTop3Account;
//		private List<PurchaseValuesDomain> purchaseItems;
//		private Date createdDate;
//		private String createdUser;
//		private Date updatedDate;
//		private String updatedUser;
//		
//		public CustomerSegmentDomainBuilder(String name, Date createdDate, String createdUser) {
//			super();
//			this.name = name;
//			this.createdDate = createdDate;
//			this.createdUser = createdUser;
//		}
//		
//		public CustomerSegmentDomainBuilder id(String id) {
//			this.id = id;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder name(String name) {
//			this.name = name;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder channelId(ListValuesDomain channelId) {
//			this.channelId = channelId;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder tierLevel(ListValuesDomain tierLevel) {
//			this.tierLevel = tierLevel;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder gender(ListValuesDomain gender) {
//			this.gender = gender;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder nationality(ListValuesDomain nationality) {
//			this.nationality = nationality;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder numberType(ListValuesDomain numberType) {
//			this.numberType = numberType;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder accountStatus(ListValuesDomain accountStatus) {
//			this.accountStatus = accountStatus;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder emailVerificationStatus(ListValuesDomain emailVerificationStatus) {
//			this.emailVerificationStatus = emailVerificationStatus;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder days(ListValuesDomain days) {
//			this.days = days;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder customerType(ListValuesDomain customerType) {
//			this.customerType = customerType;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder cobrandedCards(ListValuesDomain cobrandedCards) {
//			this.cobrandedCards = cobrandedCards;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder totalTierPoints(IntegerValuesDomain totalTierPoints) {
//			this.totalTierPoints = totalTierPoints;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder totalAccountPoints(IntegerValuesDomain totalAccountPoints) {
//			this.totalAccountPoints = totalAccountPoints;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder accountStartDate(DateValuesDomain accountStartDate) {
//			this.accountStartDate = accountStartDate;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder dob(DateValuesDomain dob) {
//			this.dob = dob;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder isFirstAccess(boolean isFirstAccess) {
//			this.isFirstAccess = isFirstAccess;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder isCoBranded(boolean isCoBranded) {
//			this.isCoBranded = isCoBranded;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder isSubscribed(boolean isSubscribed) {
//			this.isSubscribed = isSubscribed;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder isPrimaryAccount(boolean isPrimaryAccount) {
//			this.isPrimaryAccount = isPrimaryAccount;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder isTop3Account(boolean isTop3Account) {
//			this.isTop3Account = isTop3Account;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder purchaseItems(List<PurchaseValuesDomain> purchaseItems) {
//			this.purchaseItems = purchaseItems;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder updatedDate(Date updatedDate) {
//			this.updatedDate = updatedDate;
//			return this;
//		}
//		
//		public CustomerSegmentDomainBuilder updatedUser(String updatedUser) {
//			this.updatedUser = updatedUser;
//			return this;
//		}
//		
//		public CustomerSegmentDomain build() {
//			return new CustomerSegmentDomain(this);
//		}
//		
//		
//	}
//	
//	/**
//	 * 
//	 * @param userName 
//	 * @param externalTransactionId 
//	 * @param offerPurchaseDomain
//	 * @param resultResponse
//	 * @return saves a new offer purchase history record
//	 * @throws MarketplaceException
//	 */
//	public CustomerSegment saveUpdateCustomerSegment(CustomerSegmentDomain customerSegmentDomain,
//			CustomerSegment existingCustomerSegment, String action, CustomerSegmentHeaders headers, String api) throws MarketplaceException {
//		
//		LOG.info(CustomerSegmentConstants.LOG_CONSTANTS.get() , CustomerSegmentConstants.DOMAIN_PERSIST.get(), customerSegmentDomain);
//		
//		try {			
//			
//			CustomerSegment customerSegmentToSave = modelMapper.map(customerSegmentDomain, CustomerSegment.class);
//			LOG.info(CustomerSegmentConstants.LOG_CONSTANTS.get(), CustomerSegmentConstants.CONVERTED_ENTITY.get(), customerSegmentToSave);
//			
//			CustomerSegment savedCustomerSegment = repositoryHelper.saveCustomerSegment(customerSegmentToSave);	
//			
//			if(action.equals(CustomerSegmentConstants.INSERT_ACTION.get())) {
//				
//				auditService.insertDataAudit(CustomerSegmentConfigurationConstants.CUSTOMER_SEGMENTATION, savedCustomerSegment, api, headers.getExternalTransactionId(), headers.getUserName());
//			
//			} else if(action.equals(CustomerSegmentConstants.UPDATE_ACTION.get())) {
//				
//				auditService.updateDataAudit(CustomerSegmentConfigurationConstants.CUSTOMER_SEGMENTATION, savedCustomerSegment, api, existingCustomerSegment,  headers.getExternalTransactionId(), headers.getUserName());
//			}
//			
//			LOG.info(CustomerSegmentConstants.LOG_CONSTANTS.get(), CustomerSegmentConstants.DOMAIN_PERSISTED.get(), savedCustomerSegment);
//			return  savedCustomerSegment;
//			
//		} catch (MongoWriteException mongoException) {
//			
//			
//			throw new MarketplaceException(this.getClass().toString(), CustomerSegmentConstants.CONFIGURE_CUSTOMER_SEGMENT.get(),
//					mongoException.getClass() + mongoException.getMessage(), CustomerSegmentExceptionCodes.MONGO_WRITE_EXCEPTION);
//			
//			
//		
//		} catch (ValidationException validationException) {
//		
//			throw new MarketplaceException(this.getClass().toString(), CustomerSegmentConstants.CONFIGURE_CUSTOMER_SEGMENT.get(),
//					validationException.getClass() + validationException.getMessage(),
//					CustomerSegmentExceptionCodes.VALIDATION_EXCEPTION);
//		
//		} catch (Exception e) {
//		
//			CustomerSegmentExceptionCodes exception = (null==customerSegmentDomain.getId())
//					? CustomerSegmentExceptionCodes.CUSTOMER_SEGMENT_ADDITION_RUNTIME_EXCEPTION
//					: CustomerSegmentExceptionCodes.CUSTOMER_SEGMENT_UPDATION_RUNTIME_EXCEPTION; 
//			
//			throw new MarketplaceException(this.getClass().toString(), CustomerSegmentConstants.CONFIGURE_CUSTOMER_SEGMENT.get(),
//					e.getClass() + e.getMessage(), exception);
//		
//		}
//	}
//	
//	/**
//	 * 
//	 * @param customerSegmentRequest
//	 * @param headers
//	 * @return
//	 */
//	public ResultResponse validateAndSaveCustomerSegment(CustomerSegmentRequestDto customerSegmentRequest,
//			CustomerSegmentHeaders header) {
//		
//		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
//		
//		try {
//			
//			header.setProgram(programManagement.getProgramCode(header.getProgram()));
//			
//			if(CustomerSegmentValidator.validateDto(customerSegmentRequest, validator, resultResponse)
//			&& CustomerSegmentValidator.validateCustomerSegment(customerSegmentRequest, resultResponse)) {
//				
//				CustomerSegment customerSegment = repositoryHelper.findCustomerSegmentByName(customerSegmentRequest.getName());
//				
//				if(CustomerSegmentResponses.setResponseAfterConditionCheck(ObjectUtils.isEmpty(customerSegment), CustomerSegmentErrorCodes.CUSTOMER_SEGMENT_EXISTS, resultResponse)) {
//				    
//					CustomerSegmentDomain customerSegmentToSave = CustomerSegmentDomainConfiguration.getCustomerSegmentDomain(true, customerSegmentRequest, customerSegment, header);
//					saveUpdateCustomerSegment(customerSegmentToSave, null, CustomerSegmentConstants.INSERT_ACTION.get(), header, CustomerSegmentConfigurationConstants.CREATE_CUSTOMER_SEGMENT);
//					
//				}
//				
//            }
//			
//		} catch (MarketplaceException e) {
//			
//			e.printStackTrace();
//			
//			CustomerSegmentResponses.setResponseAfterException(resultResponse, 
//					new CustomerSegmentExceptionInfo(this.getClass().toString(), 
//					CustomerSegmentConstants.VALIDATE_AND_SAVE_CUSTOMER_SEGMENT.get(), null, e, CustomerSegmentErrorCodes.CUSTOMER_SEGMENT_CREATION_FAILED, null,
//					CustomerSegmentErrorCodes.CUSTOMER_SEGMENT_CREATION_FAILED));
//			
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//			CustomerSegmentResponses.setResponseAfterException(resultResponse, 
//					new CustomerSegmentExceptionInfo(this.getClass().toString(), 
//					CustomerSegmentConstants.VALIDATE_AND_SAVE_CUSTOMER_SEGMENT.get(), e, null, CustomerSegmentErrorCodes.CUSTOMER_SEGMENT_CREATION_FAILED,
//					CustomerSegmentExceptionCodes.CUSTOMER_SEGMENT_ADDITION_RUNTIME_EXCEPTION, null));
//				
//		}
//		
//		CustomerSegmentResponses.setResponse(resultResponse, CustomerSegmentErrorCodes.CUSTOMER_SEGMENT_CREATION_FAILED, CustomerSegmentSuccessCodes.CUSTOMER_SEGMENT_CREATED_SUCCESSFULLY);
//		return resultResponse;
//		
//		
//	}
//	
//		
//}
