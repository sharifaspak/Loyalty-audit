package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.BirthdayDurationInfoDto;
import com.loyalty.marketplace.offers.helper.dto.ExceptionInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.BirthdayInfoRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.dto.BirthdayInfoResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.BirthdayInfoResultResponse;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.offers.utils.OfferValidator;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherControllerHelper;
import com.mongodb.MongoException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@Component
public class BirthdayInfoDomain {

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	OffersHelper helper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	VoucherControllerHelper voucherControllerHelper;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	String refreshCrmInfoUri = "/refreshCrmInfo/{accountNumber}";
	
	private String id;
	private String programCode;
	private BirthdayTitleDomain title;
	private BirthdaySubTitleDomain subTitle;
	private BirthdayDescriptionDomain description;
	private BirthdayIconTextDomain iconText;
	private BirthdayWeekIconDomain weekIcon;
	private Integer purchaseLimit;
	private Integer thresholdPlusValue;
	private Integer thresholdMinusValue;
	private Integer displayLimit;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;
	
	public BirthdayInfoDomain(BirthdayInfoBuilder birthdayInfo) {
		super();
		this.id = birthdayInfo.id;
		this.programCode = birthdayInfo.programCode;
		this.title = birthdayInfo.title;
		this.subTitle = birthdayInfo.subTitle;
		this.description = birthdayInfo.description;
		this.iconText = birthdayInfo.iconText;
		this.weekIcon = birthdayInfo.weekIcon;
		this.purchaseLimit = birthdayInfo.purchaseLimit;
		this.thresholdPlusValue = birthdayInfo.thresholdPlusValue;
		this.thresholdMinusValue = birthdayInfo.thresholdMinusValue;
		this.displayLimit = birthdayInfo.displayLimit;
		this.createdUser = birthdayInfo.createdUser;
		this.createdDate = birthdayInfo.createdDate;
		this.updatedUser = birthdayInfo.updatedUser;
		this.updatedDate = birthdayInfo.updatedDate;
	}
	
	public static class BirthdayInfoBuilder{
		
		private String id;
		private String programCode;
		private BirthdayTitleDomain title;
		private BirthdaySubTitleDomain subTitle;
		private BirthdayDescriptionDomain description;
		private BirthdayIconTextDomain iconText;
		private BirthdayWeekIconDomain weekIcon;
		private Integer purchaseLimit;
		private Integer thresholdPlusValue;
		private Integer thresholdMinusValue;
		private Integer displayLimit;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;
		
		public BirthdayInfoBuilder(Integer purchaseLimit, Integer thresholdPlusValue, Integer thresholdMinusValue, Integer displayLimit) {
			super();
			this.purchaseLimit = purchaseLimit;
			this.thresholdPlusValue = thresholdPlusValue;
			this.thresholdMinusValue = thresholdMinusValue;
			this.displayLimit = displayLimit;
		}
		
		public BirthdayInfoBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public BirthdayInfoBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public BirthdayInfoBuilder title(BirthdayTitleDomain title) {
			this.title = title;
			return this;
		}
		
		public BirthdayInfoBuilder subTitle(BirthdaySubTitleDomain subTitle) {
			this.subTitle = subTitle;
			return this;
		}
		
		public BirthdayInfoBuilder description(BirthdayDescriptionDomain description) {
			this.description = description;
			return this;
		}
		
		public BirthdayInfoBuilder iconText(BirthdayIconTextDomain iconText) {
			this.iconText = iconText;
			return this;
		}
		
		public BirthdayInfoBuilder weekIcon(BirthdayWeekIconDomain weekIcon) {
			this.weekIcon = weekIcon;
			return this;
		}
		
		public BirthdayInfoBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		
		public BirthdayInfoBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public BirthdayInfoBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public BirthdayInfoBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public BirthdayInfoDomain build() {
			return new BirthdayInfoDomain(this);
		}
		
		
	}
	
	/**
	 * Domain method to save/update birthday information to repository
	 * @param birthdayInfoDomain
	 * @param birthdayInfo
	 * @param action
	 * @param headers
	 * @return saved/updated birthday information
	 * @throws MarketplaceException
	 */
	public BirthdayInfo saveUpdateBirthdayInfoDomain(BirthdayInfoDomain birthdayInfoDomain,
			BirthdayInfo birthdayInfo, String action, Headers headers) throws MarketplaceException {
		
		try {			
			
			BirthdayInfo birthdayInfoToSave = modelMapper.map(birthdayInfoDomain, BirthdayInfo.class);
			
			BirthdayInfo savedBirthdayInfo = repositoryHelper.saveBirthdayInfo(birthdayInfoToSave);	
			
			if(action.equals(OfferConstants.INSERT_ACTION.get())) {
				
//				auditService.insertDataAudit(OffersDBConstants.BIRTHDAY_INFO, savedBirthdayInfo, OffersRequestMappingConstants.CONFIGURE_BIRTHDAY_INFORMATION, headers.getExternalTransactionId(), headers.getUserName());
			
			} else if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.BIRTHDAY_INFO, savedBirthdayInfo, OffersRequestMappingConstants.CONFIGURE_BIRTHDAY_INFORMATION, birthdayInfo,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
			return  savedBirthdayInfo;
			
		} catch (MongoException mongoException) {
			
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_BIRTHDAY_INFO_DOMAIN_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
			
			
		
		} catch (Exception e) {
		
			OfferExceptionCodes exception = (birthdayInfoDomain.getId()==null)
					? OfferExceptionCodes.BIRTHDAY_INFO_ADDITION_RUNTIME_EXCEPTION
					: OfferExceptionCodes.BIRTHDAY_INFO_UPDATION_RUNTIME_EXCEPTION; 
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_BIRTHDAY_INFO_DOMAIN_METHOD.get(),
					e.getClass() + e.getMessage(), exception);
		
		}
	}

	/**
	 * Domain method to configure birthday information
	 * @param birthayInfoRequest
	 * @param headers
	 * @return status after configuring birthday information
	 */
	public ResultResponse configureBirthdayInfo(BirthdayInfoRequestDto birthayInfoRequest, Headers header) {
		
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		
		try {
			
			header.setProgram(programManagement.getProgramCode(header.getProgram()));
			
			if(OfferValidator.validateDto(birthayInfoRequest, validator, resultResponse)) {
				
				BirthdayInfo birthdayInfo = repositoryHelper.findBirthdayInfo();
				BirthdayInfoDomain birthdayInfoDomain = DomainConfiguration.getBirthdayInfoDomain(header, birthdayInfo, birthayInfoRequest);
				saveUpdateBirthdayInfoDomain(birthdayInfoDomain, birthdayInfo, ProcessValues.getAction(birthdayInfo), header);	
				
			}
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.VALIDATE_AND_CONFIGURE_BIRTHDAY_INFO.get(), null, e, 
					OfferErrorCodes.CONFIGURING_BIRTHDAY_INFO_FAILED, null,
					OfferErrorCodes.CONFIGURING_BIRTHDAY_INFO_FAILED,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.VALIDATE_AND_CONFIGURE_BIRTHDAY_INFO.get(), 
					e, null, OfferErrorCodes.CONFIGURING_BIRTHDAY_INFO_FAILED,
					OfferExceptionCodes.CONFIGURE_BIRTHDAY_INFO_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
				
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.CONFIGURING_BIRTHDAY_INFO_FAILED, OfferSuccessCodes.BIRTHDAY_INFO_CONFIGURED_SUCCESSFULLY);
		return resultResponse;
	}

	/**
	 * Domain method to retrieve birthday information for a member
	 * @param accountNumber
	 * @param headers
	 * @return Static birthday info for a member
	 */
	public BirthdayInfoResultResponse getBirthdayInfoForAccount(String accountNumber, Headers header) {
		
		BirthdayInfoResultResponse resultResponse = new BirthdayInfoResultResponse(header.getExternalTransactionId());
		
		try {
			fetchServiceValues.refreshCrmInfo(accountNumber, header);
			
			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, header);
			//Gift pending vouchers for current account
		    voucherControllerHelper.giftFreeVoucherToCurrentAccount(memberDetails, header);
		    
			if(!ObjectUtils.isEmpty(memberDetails)) {
				
				BirthdayInfo birthdayInfo = repositoryHelper.findBirthdayInfoByProgramCode(header);
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(birthdayInfo), OfferErrorCodes.BIRTHDAY_INFO_NOT_SET, resultResponse)) {

					BirthdayDurationInfoDto birthdayDurationInfoDto = ProcessValues.getBirthdayDurationInfoDto(repositoryHelper.findBirthdayInfoByProgramCode(header), memberDetails);
					
					List<PurchaseHistory> purchaseRecords = repositoryHelper.findBirthdayBillPaymentAndRechargePurchaseRecordsByProgramCode(memberDetails.getAccountNumber(), 
							memberDetails.getMembershipCode(), birthdayDurationInfoDto.getStartDate(), birthdayDurationInfoDto.getEndDate(), header);
					
					BirthdayGiftTracker birthdayGiftTracker = repositoryHelper.getBirthdayGiftTrackerForCurrentAccountByProgramCode(accountNumber, memberDetails.getMembershipCode(), header);
					
					if(Checks.checkBirthdayEligibility(birthdayDurationInfoDto, birthdayGiftTracker, resultResponse, 
							purchaseRecords, memberDetails.getAccountNumber(), memberDetails.getMembershipCode())) {
						
						BirthdayInfoResponseDto birthdayInfoResponseDto = modelMapper.map(birthdayInfo, BirthdayInfoResponseDto.class);	
						
						if(!ObjectUtils.isEmpty(birthdayInfoResponseDto)) {
							
							birthdayInfoResponseDto.setDob(memberDetails.getDob());
							birthdayInfoResponseDto.setFirstName(memberDetails.getFirstName());
							birthdayInfoResponseDto.setLastName(memberDetails.getLastName());
						    resultResponse.setBirthdayInfoResponseDto(birthdayInfoResponseDto);
						    
						}
						
						
					}	
						
				}
				
			}
			
		} 
		
		catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_BIRTHDAY_INFO_FOR_ACCOUNT.get(), null, e, 
					OfferErrorCodes.RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ACCOUNT, null,
					OfferErrorCodes.RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ACCOUNT,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
			
			
		} 
		catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_BIRTHDAY_INFO_FOR_ACCOUNT.get(), 
					e, null, OfferErrorCodes.RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ACCOUNT,
					OfferExceptionCodes.RETRIEVE_BIRTHDAY_INFO_ACCOUNT_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
				
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ACCOUNT, OfferSuccessCodes.BIRTHDAY_INFO_RETRIEVED_SUCCESSFULLY_FOR_ACCOUNT);
		return resultResponse;
	}
	
	/**
	 * Domain method to retrieve birthday information for an admin
	 * @param header
	 * @return static birthday info for admin
	 */
	public BirthdayInfoResultResponse getBirthdayInfoForAdmin(Headers header) {
		
		BirthdayInfoResultResponse resultResponse = new BirthdayInfoResultResponse(header.getExternalTransactionId());
		
		try {
			
				//Changes for loyalty as a service.
				//BirthdayInfo birthdayInfo = repositoryHelper.findBirthdayInfo();
				BirthdayInfo birthdayInfo = repositoryHelper.findBirthdayInfoByProgramCode(header);
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(birthdayInfo), OfferErrorCodes.BIRTHDAY_INFO_NOT_SET, resultResponse)) {
						
						BirthdayInfoResponseDto birthdayInfoResponseDto = modelMapper.map(birthdayInfo, BirthdayInfoResponseDto.class);	
						
						if(!ObjectUtils.isEmpty(birthdayInfoResponseDto)) {
							
							resultResponse.setBirthdayInfoResponseDto(birthdayInfoResponseDto);
						    
						}
						
				}
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(resultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_BIRTHDAY_INFO_FOR_ADMIN.get(), 
					e, null, OfferErrorCodes.RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ACCOUNT,
					OfferExceptionCodes.RETRIEVE_BIRTHDAY_INFO_ADMIN_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName())));
				
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ADMIN, OfferSuccessCodes.BIRTHDAY_INFO_RETRIEVED_SUCCESSFULLY_FOR_ADMIN);
		return resultResponse;
	}
	
	
	
	
}
