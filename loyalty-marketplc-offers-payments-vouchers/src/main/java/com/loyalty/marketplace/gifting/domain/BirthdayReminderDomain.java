package com.loyalty.marketplace.gifting.domain;

import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.helper.GiftingControllerHelper;
import com.loyalty.marketplace.gifting.helper.GiftingValidator;
import com.loyalty.marketplace.gifting.helper.Utility;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderDto;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderStatusDto;
import com.loyalty.marketplace.gifting.outbound.database.entity.BirthdayReminder;
import com.loyalty.marketplace.gifting.outbound.database.entity.BirthdayReminderList;
import com.loyalty.marketplace.gifting.outbound.database.repository.BirthdayReminderRepository;
import com.loyalty.marketplace.gifting.service.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class BirthdayReminderDomain {

	private static final Logger LOG = LoggerFactory.getLogger(BirthdayReminderDomain.class);

	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;

	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;

	@Getter(AccessLevel.NONE)
	@Autowired
	BirthdayReminderRepository birthdayReminderRepository;
	
	@Autowired
	GiftingControllerHelper giftingControllerHelper;
	
	private String id;
	private String programCode;
	private String accountNumber;
	private String membershipCode;
	private String firstName;
	private String lastName;
	private Date dob;
	private List<BirthdayReminderListDomain> reminderList;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;
	
	public BirthdayReminderDomain(BirthdayReminderBuilder birthdayReminderBuilder) {
		this.id = birthdayReminderBuilder.id;
		this.programCode = birthdayReminderBuilder.programCode;
		this.accountNumber = birthdayReminderBuilder.accountNumber;
		this.membershipCode = birthdayReminderBuilder.membershipCode;
		this.firstName = birthdayReminderBuilder.firstName;
		this.lastName = birthdayReminderBuilder.lastName;
		this.dob = birthdayReminderBuilder.dob;
		this.reminderList = birthdayReminderBuilder.reminderList;
		this.createdUser = birthdayReminderBuilder.createdUser;
		this.createdDate = birthdayReminderBuilder.createdDate;
		this.updatedUser = birthdayReminderBuilder.updatedUser;
		this.updatedDate = birthdayReminderBuilder.updatedDate;
	}
	
	public static class BirthdayReminderBuilder {

		private String id;
		private String programCode;
		private String accountNumber;
		private String membershipCode;
		private String firstName;
		private String lastName;
		private Date dob;
		private List<BirthdayReminderListDomain> reminderList;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;

		public BirthdayReminderBuilder(String accountNumber,
				String membershipCode, Date dob) {
			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
			this.dob = dob;
		}

		public BirthdayReminderBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public BirthdayReminderBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public BirthdayReminderBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public BirthdayReminderBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public BirthdayReminderBuilder reminderList(List<BirthdayReminderListDomain> reminderList) {
			this.reminderList = reminderList;
			return this;
		}

		public BirthdayReminderBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public BirthdayReminderBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public BirthdayReminderBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public BirthdayReminderBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public BirthdayReminderDomain build() {
			return new BirthdayReminderDomain(this);
		}

	}
	
	/**
	 * This method is used to create a birthday reminder list for an account.
	 * @param birthdayReminderDto
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse configureBirthdayReminderList(BirthdayReminderDto birthdayReminderDto, Headers headers, ResultResponse resultResponse) {
		
		try {

			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			
			int check = 0;
			
			if(!GiftingValidator.validateRequestParameters(birthdayReminderDto, validator, resultResponse)) {
				resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getMsg());
				return resultResponse;
			}
			
			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(birthdayReminderDto.getAccountNumber(), resultResponse, headers);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS, this.getClass(),
					GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_CONFIGURE_LIST, memberDetails);
			
			if(null == memberDetails) {
				resultResponse.addErrorAPIResponse(
						GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getMsg()
								+ birthdayReminderDto.getAccountNumber());
				resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getMsg());
				return resultResponse;
			}
			
			BirthdayReminder reminderAccountExists = birthdayReminderRepository.findByAccountNumberAndMembershipCode(birthdayReminderDto.getAccountNumber(), memberDetails.getMembershipCode());
			
			if (null != reminderAccountExists) {
				giftingControllerHelper.createBirthdayReminderListExistingMember(birthdayReminderDto, headers,
						reminderAccountExists, check, resultResponse);
			} else {
				giftingControllerHelper.createBirthdayReminderListNewMember(birthdayReminderDto, headers, memberDetails,
						check, resultResponse);
			}
			
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_CONFIGURE_LIST,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
		
	}
	
	/**
	 * This method is used to accept/reject being added to another account's birthday reminder list.
	 * @param birthdayReminderStatusDto
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse acceptRejectBirthdayReminder(BirthdayReminderStatusDto birthdayReminderStatusDto, Headers headers, ResultResponse resultResponse) {
		
		try {
			
			boolean accountExists = false;
			
			if(!GiftingValidator.validateRequestParameters(birthdayReminderStatusDto, validator, resultResponse)) {
				resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getMsg());
				return resultResponse;
			}
			
			if(!Utility.setStatusAttribute(birthdayReminderStatusDto, resultResponse)) return resultResponse;
			
			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(birthdayReminderStatusDto.getSenderAccountNumber(), resultResponse, headers);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS, this.getClass(),
					GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_ACCEPT_REJECT, memberDetails);
			
			if(null == memberDetails) {
				resultResponse.addErrorAPIResponse(
						GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getMsg()
								+ birthdayReminderStatusDto.getSenderAccountNumber());
				resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getMsg());
				return resultResponse;
			}
			
			BirthdayReminder senderAccountExists = birthdayReminderRepository.findByAccountNumberAndMembershipCode(birthdayReminderStatusDto.getSenderAccountNumber(), memberDetails.getMembershipCode());
			
			if (null == senderAccountExists) {
				resultResponse.addErrorAPIResponse(
						GiftingCodes.BIRTHDAY_REMINDER_SENDER_ACCOUNT_DOES_NOT_EXIST.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_SENDER_ACCOUNT_DOES_NOT_EXIST.getMsg());
				resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getMsg());
				return resultResponse;
			} 
			
			for(BirthdayReminderList reminder : senderAccountExists.getReminderList()) {
				if(reminder.getAccountNumber().equals(birthdayReminderStatusDto.getReceiverAccountNumber())) {
					reminder.setStatus(birthdayReminderStatusDto.getStatus());
					senderAccountExists.setUpdatedUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken());
					senderAccountExists.setUpdatedDate(new Date());
					accountExists = true;
				}
			}
			
			if(!accountExists) {
				resultResponse.addErrorAPIResponse(
						GiftingCodes.BIRTHDAY_REMINDER_ACCOUNT_DOES_NOT_EXIST.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_ACCOUNT_DOES_NOT_EXIST.getMsg()
								+ birthdayReminderStatusDto.getSenderAccountNumber() + GiftingConstants.COMMA_SEPARATOR
								+ birthdayReminderStatusDto.getReceiverAccountNumber());
				resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getMsg());
				return resultResponse;
			}
			
			BirthdayReminder updatedStatusBirthdayReminder = birthdayReminderRepository.save(senderAccountExists);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
    				this.getClass().getName(), GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_ACCEPT_REJECT, updatedStatusBirthdayReminder);
			
			auditService.updateDataAudit(GiftingConfigurationConstants.BIRTHDAY_REMINDER, updatedStatusBirthdayReminder,
					GiftingConfigurationConstants.ACCEPT_REJECT_BIRTHDAY_REMINDER, senderAccountExists, headers.getExternalTransactionId(), headers.getUserName());
			
			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_SUCCESS.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_SUCCESS.getMsg());
			
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_ACCEPT_REJECT,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
		
	}
	
	/**
	 * This method is used to save the birthday reminder list to BirthdayReminder collection.
	 * @param birthdayReminderDomain
	 * @param action
	 * @param headers
	 * @param existingBirthdayReminder
	 * @param api
	 * @return
	 * @throws MarketplaceException
	 */
	public BirthdayReminder saveUpdateBirthdayReminder(BirthdayReminderDomain birthdayReminderDomain, String action,
			Headers headers, BirthdayReminder existingBirthdayReminder, String api) throws MarketplaceException {

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.DOMAIN_TO_SAVE,
				this.getClass().getName(), GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_SAVE_DB, birthdayReminderDomain);

		try {

			BirthdayReminder birthdayReminder = modelMapper.map(birthdayReminderDomain, BirthdayReminder.class);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.ENTITY_TO_SAVE,
    				this.getClass().getName(), GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_SAVE_DB, birthdayReminder);

			BirthdayReminder savedBirthdayReminder = birthdayReminderRepository.save(birthdayReminder);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
    				this.getClass().getName(), GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_SAVE_DB, savedBirthdayReminder);

			if (action.equalsIgnoreCase(GiftingConstants.ACTION_INSERT)) {

//				auditService.insertDataAudit(GiftingConfigurationConstants.BIRTHDAY_REMINDER, savedBirthdayReminder,
//						api, headers.getExternalTransactionId(), headers.getUserName());

			} else if (action.equalsIgnoreCase(GiftingConstants.ACTION_UPDATE)) {

				auditService.updateDataAudit(GiftingConfigurationConstants.BIRTHDAY_REMINDER, savedBirthdayReminder,
						api, existingBirthdayReminder, headers.getExternalTransactionId(), headers.getUserName());
			
			}
			
			return savedBirthdayReminder;

		} catch (MongoWriteException mongoException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_SAVE_DB,
					mongoException.getClass() + mongoException.getMessage(), GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE);

		} catch (ValidationException validationException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_SAVE_DB,
					validationException.getClass() + validationException.getMessage(),
					GiftingCodes.VALIDATION_EXCEPTION);

		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_SAVE_DB, e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION);

		}

	}
	
	/** 
	 * This method is used to notify configured account about their friend's birthday.
	 * Cron API in GiftingController - notifyMemberBirthday calls this method.
	 * @param resultResponse
	 * @param headers
	 * @return
	 */
	public ResultResponse notifyMemberBirthday(ResultResponse resultResponse, Headers headers) {
		
		try {

			List<BirthdayReminder> allBirthdays = birthdayReminderRepository.findAll();
			
			if(allBirthdays.isEmpty()) {
				resultResponse.addErrorAPIResponse(
						GiftingCodes.BIRTHDAY_REMINDER_LIST_EMPTY.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_LIST_EMPTY.getMsg());
				resultResponse.setResult(GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_FAILURE.getMsg());
				return resultResponse;
			}
			
			List<GetListMemberResponseDto> allMemberDetails = giftingControllerHelper.getMemberAccounts(null, allBirthdays, null, headers);
			
			for(BirthdayReminder birthday : allBirthdays) {
				
				GetListMemberResponseDto accountDetails = giftingControllerHelper.checkMemberExistsActive(allMemberDetails, null, birthday, null);
				
				if(null == accountDetails) {
					resultResponse.addErrorAPIResponse(
							GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getIntId(),
							GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getMsg()
									+ birthday.getAccountNumber());
				} else {				
					giftingControllerHelper.sendBirthdayReminders(birthday, resultResponse, headers);
				}
				
			}
			
			if(allBirthdays.size() == resultResponse.getApiStatus().getErrors().size()) {
				resultResponse.setResult(GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_FAILURE.getId(),
						GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_FAILURE.getMsg());
			} else {
				resultResponse.setResult(GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_SUCCESS.getId(),
						GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_SUCCESS.getMsg());
			}
			
			
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_FAILURE.getId(),
					GiftingCodes.BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), GiftingConfigurationConstants.BIRTHDAY_REMINDER_DOMAIN_NOTIFY_MEMBER,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
		
	}
	
}
