package com.loyalty.marketplace.gifting.helper;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.domain.model.NameDomain;
import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.domain.BirthdayReminderDomain;
import com.loyalty.marketplace.gifting.domain.BirthdayReminderListDomain;
import com.loyalty.marketplace.gifting.domain.GiftingCounterDomain;
import com.loyalty.marketplace.gifting.domain.GiftingHistoryDomain;
import com.loyalty.marketplace.gifting.domain.GoldCertificateDetailsDomain;
import com.loyalty.marketplace.gifting.domain.GoldCertificateDomain;
import com.loyalty.marketplace.gifting.domain.GoldGiftedDetails;
import com.loyalty.marketplace.gifting.domain.MemberInfoDomain;
import com.loyalty.marketplace.gifting.domain.PointsGiftedDetailsDomain;
import com.loyalty.marketplace.gifting.domain.PurchaseDetailsDomain;
import com.loyalty.marketplace.gifting.helper.dto.CountCompare;
import com.loyalty.marketplace.gifting.helper.dto.NotificationHelperDto;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderDto;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderListDto;
import com.loyalty.marketplace.gifting.inbound.dto.CancelGiftRequest;
import com.loyalty.marketplace.gifting.inbound.dto.GiftConfigureRequestDto;
import com.loyalty.marketplace.gifting.inbound.dto.GiftingRequest;
import com.loyalty.marketplace.gifting.inbound.dto.GoldGiftRequest;
import com.loyalty.marketplace.gifting.inbound.dto.OfferValueDto;
import com.loyalty.marketplace.gifting.inbound.dto.PointsGiftRequest;
import com.loyalty.marketplace.gifting.inbound.dto.PremiumVoucherRequest;
import com.loyalty.marketplace.gifting.inbound.dto.VoucherGiftRequest;
import com.loyalty.marketplace.gifting.outbound.database.entity.BirthdayReminder;
import com.loyalty.marketplace.gifting.outbound.database.entity.BirthdayReminderList;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingCounter;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingLimit;
import com.loyalty.marketplace.gifting.outbound.database.entity.Gifts;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificate;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificateTransaction;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.gifting.outbound.database.repository.BirthdayReminderRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingCounterRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingHistoryRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingLimitRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GoldCertificateRepository;
import com.loyalty.marketplace.gifting.outbound.dto.GoldCertificateTransactionResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGoldTransactionResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListSpecificGiftingHistoryResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListSpecificGiftingHistoryResult;
import com.loyalty.marketplace.gifting.outbound.dto.PointsGiftTransactionResponse;
import com.loyalty.marketplace.gifting.service.GiftingService;
import com.loyalty.marketplace.gifting.service.dto.EnrollmentResultResponse;
import com.loyalty.marketplace.gifting.service.dto.GetListMemberResponse;
import com.loyalty.marketplace.gifting.service.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.gifting.service.dto.MemberActivityPaymentDto;
import com.loyalty.marketplace.gifting.service.dto.MemberActivityResponse;
import com.loyalty.marketplace.gifting.utils.GiftingResponses;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.image.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.decision.manager.inbound.dto.CustomerSegmentDMRequestDto;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CustomerSegmentResult;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.dto.AmountPoints;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Requests;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.dto.PushNotificationRequestDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.inbound.dto.PaymentAdditionalRequest;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherStatus;
import com.loyalty.marketplace.voucher.domain.VoucherDomain;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherControllerHelper;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherGiftDetails;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.mongodb.client.result.UpdateResult;

import lombok.AccessLevel;
import lombok.Getter;

@RefreshScope
@Component
public class GiftingControllerHelper {

	@Autowired
	Validator validator;

	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	@Autowired
	GiftingService giftingService;

	@Autowired
	FetchServiceValues fetchServiceValues;

	@Autowired
	BirthdayReminderDomain birthdayReminderDomain;

	@Autowired
	GoldCertificateRepository goldCertificateRepository;

	@Autowired
	BirthdayReminderRepository birthdayReminderRepository;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	VoucherDomain voucherDomain;

	@Autowired
	VoucherControllerHelper voucherControllerHelper;

	@Autowired
	EventHandler eventHandler;
	
	@Autowired
	GiftingHistoryDomain giftingHistoryDomain;

	@Autowired
	GiftingHistoryRepository giftingHistoryRepository;

	@Autowired
	OfferRepository offerRepository;

	@Autowired
	GiftingCounterRepository giftingCounterRepository;

	@Autowired
	GiftingLimitRepository giftingLimitRepository;

	@Autowired
	GiftingCounterDomain giftingCounterDomain;

	@Autowired
	GoldCertificateDomain goldCertificateDomain;

	@Getter(AccessLevel.NONE)
	@Autowired
	PaymentService paymentService;

	@Autowired
	OffersHelper helper;
	
	@Autowired
	MongoOperations mongoOperations;

	@Autowired
	ProgramManagement programManagement;
	
	@Value("${premium.voucher.gift.sms.notification.code}")
	private String premiumGiftSmsNotificationCode;

	@Value("${premium.voucher.gift.sms.template.id}")
	private String premiumGiftSmsTemplateId;

	@Value("${premium.voucher.gift.sms.notification.id}")
	private String premiumGiftSmsNotificationId;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	PurchaseDomain purchaseDomain;
	
	@Value("#{'${eligible.customerSegments.gifting.points.sender}'.split(',')}")
	private List<String> pointsSenderEligibleCustomerSegments;

	@Value("#{'${eligible.customerSegments.gifting.points.receiver}'.split(',')}")
	private List<String> pointsReceiverEligibleCustomerSegments;

	@Value("#{'${eligible.customerSegments.gifting.gold.sender}'.split(',')}")
	private List<String> goldSenderEligibleCustomerSegments;

	@Value("#{'${eligible.customerSegments.gifting.gold.receiver}'.split(',')}")
	private List<String> goldReceiverEligibleCustomerSegments;

	
	@Value("${premium.voucher.gift.type}")
	private String premiumGiftType;
	
	@Value("${promotional.gift.type}")
	private String promotionalGiftType;
	
	@Value("${subscription.purchase.gift.type}")
	private String subscriptionGiftType;
	
	@Value("#{'${valid.giftType.list}'.split(',')}")
	private List<String> validGiftTypes;
	
	@Value("#{'${non.eligible.notification.channelIds}'.split(',')}")
	private List<String> nonEligibleNotificationChannelIds;

	private static final Logger LOG = LoggerFactory.getLogger(GiftingControllerHelper.class);
	private static final String DB_OPERATION_UPDATE = "Update";
	private static final String DB_OPERATION_INSERT = "Insert";

	
	
	
	
	/**
	 * This method is used to validate request parameters for list gold transaction
	 * API.
	 * 
	 * @param accountNumber
	 * @param listGoldTransactionResponse
	 * @return
	 */
	public boolean validateListGoldTransactionRequestParameters(String accountNumber,
			ListGoldTransactionResponse listGoldTransactionResponse) {
		if (null == accountNumber || accountNumber.isEmpty()) {
			listGoldTransactionResponse.addErrorAPIResponse(GiftingCodes.ACCOUNT_NUMBER_MANDATORY_FIELD.getIntId(),
					GiftingCodes.ACCOUNT_NUMBER_MANDATORY_FIELD.getMsg());
			listGoldTransactionResponse.setResult(GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getId(),
					GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getMsg());
		}
		return listGoldTransactionResponse.getApiStatus().getErrors().isEmpty();
	}

	/**
	 * This method is used to retrieve member details for a list of account numbers.
	 *
	 * @param header
	 * @return
	 * @throws VoucherManagementException
	 * @throws MarketplaceException
	 */
	public List<GetListMemberResponseDto> getMemberAccounts(List<BirthdayReminderListDto> reminderList,
			List<BirthdayReminder> allBirthdays, List<BirthdayReminderList> reminderListEntity, Headers header)
			throws MarketplaceException {

		List<String> accountsList = new ArrayList<>();
		Set<String> uniqueAccounts = new HashSet<>();

		if (null != reminderList) {

			for (BirthdayReminderListDto remindAccount : reminderList) {
				if (null != remindAccount.getAccountNumber()) {
					uniqueAccounts.add(remindAccount.getAccountNumber());
				}
			}

		}

		if (null != allBirthdays) {

			for (BirthdayReminder birthday : allBirthdays) {
				if (null != birthday.getAccountNumber()) {
					uniqueAccounts.add(birthday.getAccountNumber());
				}
			}

		}

		uniqueAccounts = getAccountsListExistingMembers(reminderListEntity, uniqueAccounts);

		for (String id : uniqueAccounts) {
			accountsList.add(id);
		}

		GetListMemberResponse listMemberResponse = giftingService.getListMemberDetails(accountsList, header);

		return listMemberResponse.getListMember();

	}

	/**
	 * This method is used to get list of unique account numbers.
	 * 
	 * @param reminderListEntity
	 * @param uniqueAccounts
	 * @return
	 */
	private Set<String> getAccountsListExistingMembers(List<BirthdayReminderList> reminderListEntity,
			Set<String> uniqueAccounts) {

		if (null != reminderListEntity) {

			for (BirthdayReminderList birthdayEntity : reminderListEntity) {
				if (null != birthdayEntity.getAccountNumber()) {
					uniqueAccounts.add(birthdayEntity.getAccountNumber());
				}
			}

		}

		return uniqueAccounts;

	}

	/**
	 * This method checks if the member exists and is active.
	 * 
	 * @param allMemberDetails
	 * @param newMemberToRemind
	 * @return
	 */
	public GetListMemberResponseDto checkMemberExistsActive(List<GetListMemberResponseDto> allMemberDetails,
			BirthdayReminderListDto newMemberToRemind, BirthdayReminder birthday,
			BirthdayReminderList birthdayListEntity) {

		GetListMemberResponseDto accountDetails = null;
		for (GetListMemberResponseDto member : allMemberDetails) {

			if (null != newMemberToRemind && newMemberToRemind.getAccountNumber().equals(member.getAccountNumber())
					&& (member.getAccountStatus().equalsIgnoreCase(GiftingConstants.MEMBER_ACT_STATUS)
							|| (member.getAccountStatus().equalsIgnoreCase(GiftingConstants.MEMBER_ACTIVE_STATUS)))) {
				return member;
			}

			if (null != birthday && birthday.getAccountNumber().equals(member.getAccountNumber())
					&& birthday.getMembershipCode().equals(member.getMembershipCode())
					&& (member.getAccountStatus().equalsIgnoreCase(GiftingConstants.MEMBER_ACT_STATUS)
							|| (member.getAccountStatus().equalsIgnoreCase(GiftingConstants.MEMBER_ACTIVE_STATUS)))) {
				return member;
			}

			if (null != birthdayListEntity && birthdayListEntity.getAccountNumber().equals(member.getAccountNumber())
					&& birthdayListEntity.getMembershipCode().equals(member.getMembershipCode())
					&& (member.getAccountStatus().equalsIgnoreCase(GiftingConstants.MEMBER_ACT_STATUS)
							|| (member.getAccountStatus().equalsIgnoreCase(GiftingConstants.MEMBER_ACTIVE_STATUS)))) {
				return member;
			}

		}

		return accountDetails;

	}

	/**
	 * This method is used to create birthday reminder list for new member.
	 * 
	 * @param birthdayReminderDto
	 * @param headers
	 * @param memberDetails
	 * @param check
	 * @param resultResponse
	 * @throws MarketplaceException
	 */
	public void createBirthdayReminderListNewMember(BirthdayReminderDto birthdayReminderDto, Headers headers,
			GetMemberResponse memberDetails, int check, ResultResponse resultResponse) throws MarketplaceException {

		List<BirthdayReminderListDomain> memberReminderList = new ArrayList<>();

		List<GetListMemberResponseDto> allMemberDetails = getMemberAccounts(birthdayReminderDto.getReminderList(), null,
				null, headers);

		Map<String, String> memberLanguage = new HashMap<String, String>();

		for (BirthdayReminderListDto memberToRemind : birthdayReminderDto.getReminderList()) {

			GetListMemberResponseDto accountDetails = checkMemberExistsActive(allMemberDetails, memberToRemind, null,
					null);

			if (null == accountDetails) {
				resultResponse.addErrorAPIResponse(GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getMsg()
								+ memberToRemind.getAccountNumber());
				check++;
			} else {
				if (Utility.setRemindPriorAttribute(memberToRemind, resultResponse)) {
					BirthdayReminderListDomain birthdayReminderListDomain = new BirthdayReminderListDomain.BirthdayReminderListBuilder(
							memberToRemind.getAccountNumber(), accountDetails.getMembershipCode(),
							memberToRemind.getRemindPrior(), GiftingConstants.BIRTHDAY_REMINDER_PENDING)
									.firstName(accountDetails.getFirstName()).lastName(accountDetails.getLastName())
									.build();
					memberReminderList.add(birthdayReminderListDomain);

					memberLanguage.put(memberToRemind.getAccountNumber(), accountDetails.getUilanguage());

				} else {
					check++;
				}
			}

		}

		if (check == birthdayReminderDto.getReminderList().size()) {
			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getMsg());
		} else {

			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_REQUEST_SUCCESS.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_REQUEST_SUCCESS.getMsg());

			BirthdayReminderDomain birthdayDomain = new BirthdayReminderDomain.BirthdayReminderBuilder(
					memberDetails.getAccountNumber(), memberDetails.getMembershipCode(), birthdayReminderDto.getDob())
							.programCode(headers.getProgram()).firstName(memberDetails.getFirstName())
							.lastName(memberDetails.getLastName()).reminderList(memberReminderList)
							.createdUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken())
							.createdDate(new Date()).build();

			birthdayReminderDomain.saveUpdateBirthdayReminder(birthdayDomain, GiftingConstants.ACTION_INSERT, headers,
					null, GiftingConfigurationConstants.CREATE_BIRTHDAY_REMINDER_LIST);

			for (Map.Entry<String, String> element : memberLanguage.entrySet()) {

				sendPushNotificationBirthdayReminder(element.getKey(), getMembershipCode(allMemberDetails, element),
						memberDetails.getFirstName(), headers.getExternalTransactionId(), element.getValue());

			}

		}

	}

	/**
	 * This method returns the membership code for the given account.
	 * @param allMemberDetails
	 * @param element
	 * @return
	 */
	private String getMembershipCode(List<GetListMemberResponseDto> allMemberDetails, Map.Entry<String, String> element) {
		
		for(GetListMemberResponseDto member : allMemberDetails) {
			if(member.getAccountNumber().equals(element.getKey())) {
				return member.getMembershipCode();
			}
			
		}
		
		return "";
		
	}
	
	/**
	 * This method is used to update birthday reminder list for an existing member.
	 * 
	 * @param birthdayReminderDto
	 * @param headers
	 * @param reminderAccountExists
	 * @param check
	 * @param resultResponse
	 * @throws MarketplaceException
	 */
	public void createBirthdayReminderListExistingMember(BirthdayReminderDto birthdayReminderDto, Headers headers,
			BirthdayReminder reminderAccountExists, int check, ResultResponse resultResponse)
			throws MarketplaceException {

		int originalListSize = 0;

		List<BirthdayReminderListDomain> memberReminderList = new ArrayList<>();

		for (BirthdayReminderList existingMemberToRemind : reminderAccountExists.getReminderList()) {
			BirthdayReminderListDomain birthdayReminderListDomain = new BirthdayReminderListDomain.BirthdayReminderListBuilder(
					existingMemberToRemind.getAccountNumber(), existingMemberToRemind.getMembershipCode(),
					existingMemberToRemind.getRemindPrior(), existingMemberToRemind.getStatus())
							.firstName(existingMemberToRemind.getFirstName())
							.lastName(existingMemberToRemind.getLastName()).build();
			memberReminderList.add(birthdayReminderListDomain);
			originalListSize++;
		}

		List<GetListMemberResponseDto> allMemberDetails = getMemberAccounts(birthdayReminderDto.getReminderList(), null,
				null, headers);

		for (BirthdayReminderListDto newMemberToRemind : birthdayReminderDto.getReminderList()) {

			boolean reminderExists = checkReminderExists(reminderAccountExists, newMemberToRemind);

			if (reminderExists) {
				check++;
				resultResponse.addErrorAPIResponse(GiftingCodes.BIRTHDAY_REMINDER_MEMBER_ALREADY_EXIST.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_MEMBER_ALREADY_EXIST.getMsg()
								+ newMemberToRemind.getAccountNumber());
			} else {

				GetListMemberResponseDto accountDetails = checkMemberExistsActive(allMemberDetails, newMemberToRemind,
						null, null);

				if (null == accountDetails) {
					resultResponse.addErrorAPIResponse(GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getIntId(),
							GiftingCodes.BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST.getMsg()
									+ newMemberToRemind.getAccountNumber());
					check++;
				} else {
					if (Utility.setRemindPriorAttribute(newMemberToRemind, resultResponse)) {

						BirthdayReminderListDomain birthdayReminderListDomain = new BirthdayReminderListDomain.BirthdayReminderListBuilder(
								newMemberToRemind.getAccountNumber(), accountDetails.getMembershipCode(),
								newMemberToRemind.getRemindPrior(), GiftingConstants.BIRTHDAY_REMINDER_PENDING)
										.firstName(accountDetails.getFirstName()).lastName(accountDetails.getLastName())
										.build();
						memberReminderList.add(birthdayReminderListDomain);

						sendPushNotificationBirthdayReminder(newMemberToRemind.getAccountNumber(),
								accountDetails.getMembershipCode(), accountDetails.getFirstName(), headers.getExternalTransactionId(),
								accountDetails.getUilanguage());

					} else {
						check++;
					}
				}

			}

		}

		BirthdayReminderDomain birthdayDomain = createBirthdayReminderDomain(check, birthdayReminderDto, headers,
				reminderAccountExists, memberReminderList, originalListSize, resultResponse);

		birthdayReminderDomain.saveUpdateBirthdayReminder(birthdayDomain, GiftingConstants.ACTION_UPDATE, headers,
				reminderAccountExists, GiftingConfigurationConstants.CREATE_BIRTHDAY_REMINDER_LIST);

	}

	/**
	 * This method check if a birthday reminder already exists for the receiver
	 * account.
	 * 
	 * @param reminderAccountExists
	 * @param newMemberToRemind
	 * @return
	 */
	private boolean checkReminderExists(BirthdayReminder reminderAccountExists,
			BirthdayReminderListDto newMemberToRemind) {

		for (BirthdayReminderList existingMemberToRemind : reminderAccountExists.getReminderList()) {
			if (newMemberToRemind.getAccountNumber().equals(existingMemberToRemind.getAccountNumber()))
				return true;
		}

		return false;

	}

	/**
	 * This method is used to create Birthday Reminder Domain object.
	 * 
	 * @param check
	 * @param birthdayReminderDto
	 * @param headers
	 * @param reminderAccountExists
	 * @param memberReminderList
	 * @param originalListSize
	 * @param resultResponse
	 * @return
	 */
	private BirthdayReminderDomain createBirthdayReminderDomain(int check, BirthdayReminderDto birthdayReminderDto,
			Headers headers, BirthdayReminder reminderAccountExists,
			List<BirthdayReminderListDomain> memberReminderList, int originalListSize, ResultResponse resultResponse) {

		if (check == birthdayReminderDto.getReminderList().size()) {
			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_REQUEST_FAILURE.getMsg());
		} else {
			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_REQUEST_SUCCESS.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_REQUEST_SUCCESS.getMsg());
		}

		return new BirthdayReminderDomain.BirthdayReminderBuilder(reminderAccountExists.getAccountNumber(),
				reminderAccountExists.getMembershipCode(), reminderAccountExists.getDob())
						.id(reminderAccountExists.getId())
						.programCode(null != headers.getProgram() ? headers.getProgram()
								: reminderAccountExists.getProgramCode())
						.firstName(reminderAccountExists.getFirstName()).lastName(reminderAccountExists.getLastName())
						.reminderList(memberReminderList).createdUser(reminderAccountExists.getCreatedUser())
						.createdDate(reminderAccountExists.getCreatedDate())
						.updatedUser(originalListSize < memberReminderList.size()
								? null != headers.getUserName() ? headers.getUserName() : headers.getToken()
								: reminderAccountExists.getUpdatedUser())
						.updatedDate(originalListSize < memberReminderList.size() ? new Date()
								: reminderAccountExists.getUpdatedDate())
						.build();

	}

	/**
	 * This method is used to send push notification for configured birthday
	 * reminder list.
	 *
	 * @param senderFirstName
	 * @throws MarketplaceException
	 */
	private void sendPushNotificationBirthdayReminder(String accountNumber, String membershipCode, String senderFirstName,
			String externalTransactionId, String language) throws MarketplaceException {

		PushNotificationRequestDto notificationDto = new PushNotificationRequestDto();

		if (null == language || 1 == Utility.checkLanguage(language))
			notificationDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_EN);

		if (null != language && 2 == Utility.checkLanguage(language))
			notificationDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_AR);

		notificationDto.setTransactionId(externalTransactionId);
		notificationDto.setNotificationId(GiftingConstants.BIRTHDAY_REMINDER_LIST_NOTIFICATION_ID);
		notificationDto.setNotificationCode(GiftingConstants.NOTIFICATION_CODE);
		notificationDto.setAccountNumber(accountNumber);
		notificationDto.setMembershipCode(membershipCode);
		
		Map<String, String> additionalParam = new HashMap<>();		
		additionalParam.put("FIRST_NAME", senderFirstName);
		notificationDto.setAdditionalParameters(additionalParam);
		
		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.NOTIFICATION_DTO,
				this.getClass().getName(), GiftingConfigurationConstants.CONTROLLER_HELPER_BIRTHDAY_PUSH_NOTIFICATION,
				notificationDto);

		giftingService.pushNotificationBirthdayReminder(notificationDto);

	}

	/**
	 * This method is used to reminder the list of account of their friend's
	 * birthday.
	 *
	 * @param senderFirstName
	 * @throws MarketplaceException
	 */
	public void remindMemberBirthdayPushNotification(String account, String membershipCode, String senderFirstName,
			String externalTransactionId, String language) throws MarketplaceException {

		PushNotificationRequestDto notificationDto = new PushNotificationRequestDto();

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.BIRTHDAY_REMINDER_ACCOUNTS,
				this.getClass().getName(), GiftingConfigurationConstants.CONTROLLER_HELPER_BIRTHDAY_REMIND_DAY, account);

		if (null == language || 1 == Utility.checkLanguage(language))
			notificationDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_EN);

		if (null != language && 2 == Utility.checkLanguage(language))
			notificationDto.setLanguage(GiftingConstants.NOTIFICATION_LANG_AR);

		notificationDto.setTransactionId(externalTransactionId);
		notificationDto.setNotificationId(GiftingConstants.SEND_BIRTHDAY_REMINDER_NOTIFICATION_ID);
		notificationDto.setNotificationCode(GiftingConstants.NOTIFICATION_CODE);
		notificationDto.setAccountNumber(account);
		notificationDto.setMembershipCode(membershipCode);
		
		Map<String, String> additionalParam = new HashMap<>();		
		additionalParam.put("FIRST_NAME", senderFirstName);
		notificationDto.setAdditionalParameters(additionalParam);
		
		giftingService.pushNotificationBirthdayReminder(notificationDto);

	}

	/**
	 * This method is used to send push notification for birthday reminder list.
	 * 
	 * @param birthday
	 * @param resultResponse
	 * @return
	 * @throws MarketplaceException
	 */
	public ResultResponse sendBirthdayReminders(BirthdayReminder birthday, ResultResponse resultResponse,
			Headers headers) throws MarketplaceException {

		List<String> accountListDay = new ArrayList<>();
		List<String> accountListWeek = new ArrayList<>();
		List<String> accountListMonth = new ArrayList<>();

		List<GetListMemberResponseDto> allMemberDetails = getMemberAccounts(null, null, birthday.getReminderList(),
				headers);

		for (BirthdayReminderList accountToRemind : birthday.getReminderList()) {

			GetListMemberResponseDto accountDetails = checkMemberExistsActive(allMemberDetails, null, null,
					accountToRemind);

			if (null == accountDetails) {
				resultResponse.addErrorAPIResponse(
						GiftingCodes.BIRTHDAY_REMINDER_ACCOUNT_INACTIVE_NOT_EXISTS.getIntId(),
						GiftingCodes.BIRTHDAY_REMINDER_ACCOUNT_INACTIVE_NOT_EXISTS.getMsg()
								+ accountToRemind.getAccountNumber());
				continue;
			}

			if (accountToRemind.getRemindPrior().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_DAY)
					&& accountToRemind.getStatus().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_ACCEPTED)
					&& Utility.isAfterDays(birthday.getDob())) {

				accountListDay.add(accountToRemind.getAccountNumber());
				remindMemberBirthdayPushNotification(accountToRemind.getAccountNumber(),
						accountToRemind.getMembershipCode(), birthday.getFirstName(),
						headers.getExternalTransactionId(), accountDetails.getUilanguage());

			} else if (accountToRemind.getRemindPrior().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_WEEK)
					&& accountToRemind.getStatus().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_ACCEPTED)
					&& Utility.isAfterWeeks(birthday.getDob())) {

				accountListWeek.add(accountToRemind.getAccountNumber());
				remindMemberBirthdayPushNotification(accountToRemind.getAccountNumber(),
						accountToRemind.getMembershipCode(), birthday.getFirstName(),
						headers.getExternalTransactionId(), accountDetails.getUilanguage());

			} else if (accountToRemind.getRemindPrior().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_MONTH)
					&& accountToRemind.getStatus().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_ACCEPTED)
					&& Utility.isAfterMonths(birthday.getDob())) {

				accountListMonth.add(accountToRemind.getAccountNumber());
				remindMemberBirthdayPushNotification(accountToRemind.getAccountNumber(),
						accountToRemind.getMembershipCode(), birthday.getFirstName(),
						headers.getExternalTransactionId(), accountDetails.getUilanguage());

			}
		}

		if (accountListDay.isEmpty() && accountListWeek.isEmpty() && accountListMonth.isEmpty()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.NO_BIRTHDAY_REMINDER_SCHEDULED.getIntId(),
					GiftingCodes.NO_BIRTHDAY_REMINDER_SCHEDULED.getMsg() + birthday.getAccountNumber());
		}

		return resultResponse;

	}

	/**
	 * This method is used to gift a voucher
	 * 
	 * @param giftingVoucherDto
	 * @param headers
	 * @param resultResponse
	 * 
	 */
	public ResultResponse giftVoucher(GiftingRequest giftingVoucherDto, Headers headers,
			ResultResponse resultResponse) {
		try {
			
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			
			VoucherGiftRequest voucherGiftRequest = modelMapper.map(giftingVoucherDto, VoucherGiftRequest.class);

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.VOUCHER_GIFT_REQUEST_PARAMS,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_VOUCHER, voucherGiftRequest);
			if (!GiftingValidator.validateRequestParameters(voucherGiftRequest, validator, resultResponse)) {
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
				return resultResponse;
			}

			if(voucherGiftRequest.getIsScheduled()!=null && voucherGiftRequest.getIsScheduled().equalsIgnoreCase("Yes")  && (voucherGiftRequest.getScheduledDate()==null)) {
			
					resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_DATE_REQD.getIntId(),
							GiftingCodes.SCHEDULED_DATE_REQD.getMsg());
					resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
							GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
					return resultResponse;
				
			}
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			now.set(Calendar.HOUR_OF_DAY, 0);  
			now.set(Calendar.MINUTE, 0);  
			now.set(Calendar.SECOND, 0);  
			now.set(Calendar.MILLISECOND, 0);  
			voucherGiftRequest.setScheduledDate((voucherGiftRequest.getScheduledDate()!=null) ? voucherGiftRequest.getScheduledDate() : new Date());   
			if(voucherGiftRequest.getScheduledDate().before(now.getTime())) {
				resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_DATE_PAST.getIntId(),
						GiftingCodes.SCHEDULED_DATE_PAST.getMsg());
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
				return resultResponse;
			}
			
			Voucher voucher = voucherRepository.findByCodeAndStatusAndExpiryDateNotGift(
					voucherGiftRequest.getVoucherCode(), VoucherStatus.ACTIVE,
					voucherGiftRequest.getSenderAccountNumber(), new Date());
			LOG.info("Is voucher available : {}", voucher);
			if (null == voucher) {
				resultResponse.addErrorAPIResponse(GiftingCodes.NO_VOUCHERS_FOR_GIFTING_AVAILABLE.getIntId(),
						GiftingCodes.NO_VOUCHERS_FOR_GIFTING_AVAILABLE.getMsg());
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
				return resultResponse;
			}

			Double crfrBalance = null;
			String isCRFR = "NO";
			if(null != voucher.getPartnerCode() && voucher.getPartnerCode().equalsIgnoreCase(GiftingConstants.CARREFOUR)) {
				isCRFR = "YES";
				if(null == voucher.getVoucherAmount() || voucher.getVoucherAmount().equals(0.0)) {
					resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_NO_VOUCHER_AMOUNT.getIntId(),
							GiftingCodes.VOUCHER_GIFT_NO_VOUCHER_AMOUNT.getMsg());
					resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
							GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
					return resultResponse;
				}

				if(null != voucher.getVoucherBalance()) {

					if(voucher.getVoucherBalance() < voucher.getVoucherAmount()) {
						resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_PARTIAL_BALANCE.getIntId(),
								GiftingCodes.VOUCHER_GIFT_PARTIAL_BALANCE.getMsg());
						resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
								GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
						return resultResponse;
					} else {
						crfrBalance = retrieveCRFRBalance(voucher, resultResponse);
						if(null == crfrBalance) {
							resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
									GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
							return resultResponse;
						}
						if(crfrBalance < voucher.getVoucherAmount()) {
							updateCRFRBalaneDB(crfrBalance, voucher.getVoucherBalance(), voucher, headers);
							resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_PARTIAL_BALANCE.getIntId(),
									GiftingCodes.VOUCHER_GIFT_PARTIAL_BALANCE.getMsg());
							resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
									GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
							return resultResponse;
						}
					}

				} else {

					crfrBalance = retrieveCRFRBalance(voucher, resultResponse);
					if(null == crfrBalance) {
						resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
								GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
						return resultResponse;
					}
					if(crfrBalance < voucher.getVoucherAmount()) {
						updateCRFRBalaneDB(crfrBalance, voucher.getVoucherBalance(), voucher, headers);
						resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_PARTIAL_BALANCE.getIntId(),
								GiftingCodes.VOUCHER_GIFT_PARTIAL_BALANCE.getMsg());
						resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
								GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
						return resultResponse;
					}

				}

			}

			MarketplaceImage image = imageRepository.findByImageId(voucherGiftRequest.getImageId());
			if (image == null) {
				resultResponse.addErrorAPIResponse(GiftingCodes.NO_IMAGES_AVAILABLE.getIntId(),
						GiftingCodes.NO_IMAGES_AVAILABLE.getMsg());
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
				return resultResponse;
			}
			GetMemberResponseDto senderMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
					voucherGiftRequest.getSenderAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(),
					headers, resultResponse);
			GetMemberResponse senderDetails = ProcessValues.getMemberInfo(senderMemberDetailsDto,
					voucherGiftRequest.getSenderAccountNumber());

			GetMemberResponseDto receiverMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
					voucherGiftRequest.getReceiverAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(),
					headers, resultResponse);
			GetMemberResponse receiverDetails = ProcessValues.getMemberInfo(receiverMemberDetailsDto,
					voucherGiftRequest.getReceiverAccountNumber());

			if (!senderAccountValidation(senderDetails, resultResponse, giftingVoucherDto.getGiftType())) {
				return resultResponse;
			}

			if (null == receiverDetails) {
				resultResponse.setSuccessAPIResponse();
				resultResponse.getApiStatus().setErrors(new ArrayList<>());
				receiverDetails = enrollReceiverValidation(giftingVoucherDto, resultResponse, headers);
				if (receiverDetails == null)
					return resultResponse;
				
				LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS_AFTER_ENROLL,
						this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_VOUCHER, receiverDetails);
				
			}
			if(null != voucherGiftRequest.getReceiverEmail() && !voucherGiftRequest.getReceiverEmail().isEmpty()) {
				receiverDetails.setEmail(voucherGiftRequest.getReceiverEmail());
			}
			resultResponse.setSuccessAPIResponse();
			resultResponse.getApiStatus().setErrors(new ArrayList<>());
			GiftingHistory savedGift = saveGiftingHistory(giftingVoucherDto, senderDetails, receiverDetails, image,
					headers, giftingVoucherDto.getVoucherCode(), 0, null, null, null);

			voucherDomain.giftVoucher(voucher, voucherGiftRequest, headers.getUserName(),
					receiverDetails.getMembershipCode(), headers.getExternalTransactionId(), savedGift.getId(), crfrBalance, isCRFR);
			
			if(!nonEligibleNotificationChannelIds.contains(headers.getChannelId())) {
				LOG.info("ChannelId : {}", headers.getChannelId());
				giftingService.sendSMS(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, savedGift,
						GiftingConstants.GIFT_TYPE_VOUCHER, 0, null, senderDetails, receiverDetails,
						GiftingConstants.NOTIFICATION_TYPE_SMS), headers);
				giftingService.sendEmail(createNotificationHelperDto(GiftingConstants.TYPE_SENDER, savedGift,
						GiftingConstants.GIFT_TYPE_VOUCHER, 0, null, senderDetails,
						receiverDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL), headers);
				
			}
			
			if ((null != voucherGiftRequest.getReceiverEmail() && !voucherGiftRequest.getReceiverEmail().isEmpty())
					|| (null != receiverDetails.getEmail() && !receiverDetails.getEmail().isEmpty())) {
				if(!nonEligibleNotificationChannelIds.contains(headers.getChannelId())) {
					LOG.info("ChannelId : {}", headers.getChannelId());
					giftingService.sendEmail(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, savedGift,
							GiftingConstants.GIFT_TYPE_VOUCHER, 0, null, senderDetails,
							receiverDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL), headers);
				}
			}
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_SUCCESS.getId(),
					GiftingCodes.VOUCHER_GIFTING_SUCCESS.getMsg());
		} catch (MarketplaceException me) {
			me.printStackTrace();
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "giftVoucher",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		return resultResponse;
	}

	/**
	 * This method is used to gift points.
	 * 
	 * @param giftingPointsDto
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse giftPoints(GiftingRequest giftingPointsDto, Headers headers, ResultResponse resultResponse) {

		try {

			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			
			PointsGiftRequest pointsGiftRequest = modelMapper.map(giftingPointsDto, PointsGiftRequest.class);

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.POINTS_GIFT_REQUEST_PARAMS,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_POINTS, pointsGiftRequest);

			
			MarketplaceImage image = imageRepository.findByImageId(pointsGiftRequest.getImageId());

			if (!validatePointsGiftParameters(pointsGiftRequest, image, resultResponse)) {
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return resultResponse;
			}
			
			if(pointsGiftRequest.getIsScheduled()!=null && pointsGiftRequest.getIsScheduled().equalsIgnoreCase("Yes")) {
				if(pointsGiftRequest.getScheduledDate()==null) {
					resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_DATE_REQD.getIntId(),
							GiftingCodes.SCHEDULED_DATE_REQD.getMsg());
					resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
							GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
					return resultResponse;
				}
			}
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			now.set(Calendar.HOUR_OF_DAY, 0);  
			now.set(Calendar.MINUTE, 0);  
			now.set(Calendar.SECOND, 0);  
			now.set(Calendar.MILLISECOND, 0);  
			pointsGiftRequest.setScheduledDate((pointsGiftRequest.getScheduledDate()!=null) ? pointsGiftRequest.getScheduledDate() : new Date());   
			if(pointsGiftRequest.getScheduledDate().before(now.getTime())) {
				resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_DATE_PAST.getIntId(),
						GiftingCodes.SCHEDULED_DATE_PAST.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return resultResponse;
			}
			

			GetMemberResponseDto senderMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
					pointsGiftRequest.getSenderAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(),
					headers, resultResponse);
			GetMemberResponse senderAccountDetails = ProcessValues.getMemberInfo(senderMemberDetailsDto,
					pointsGiftRequest.getSenderAccountNumber());

			GetMemberResponseDto receiverMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
					pointsGiftRequest.getReceiverAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(),
					headers, resultResponse);
			GetMemberResponse receiverAccountDetails = ProcessValues.getMemberInfo(receiverMemberDetailsDto,
					pointsGiftRequest.getReceiverAccountNumber());

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.SENDER_RECEIVER_MEMBER_DETAILS,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_POINTS, senderAccountDetails,
					receiverAccountDetails);

			if (!senderAccountValidation(senderAccountDetails, resultResponse, giftingPointsDto.getGiftType()))
				return resultResponse;

			pointsGiftRequest.setSenderMembershipCode(senderAccountDetails.getMembershipCode());

			if (!Utility.checkMemberExists(receiverAccountDetails, giftingPointsDto.getReceiverAccountNumber(),
					resultResponse)) {
				
				resultResponse.setSuccessAPIResponse();
				resultResponse.getApiStatus().setErrors(new ArrayList<>());
				
				receiverAccountDetails = enrollReceiverValidation(giftingPointsDto, resultResponse, headers);
				if (receiverAccountDetails == null)
					return resultResponse;

				resultResponse.setSuccessAPIResponse();
				resultResponse.getApiStatus().setErrors(new ArrayList<>());
				
				LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS_AFTER_ENROLL,
						this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_POINTS, receiverAccountDetails);
				
				pointsGiftRequest.setReceiverMembershipCode(receiverAccountDetails.getMembershipCode());

				receiverMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
						giftingPointsDto.getReceiverAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(),
						headers, resultResponse);

			} else {

				if (!Utility.isMemberAccountActive(receiverAccountDetails, resultResponse)) {
					resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
							GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
					return resultResponse;
				}
				
				pointsGiftRequest.setReceiverMembershipCode(receiverAccountDetails.getMembershipCode());

			}

			List<GiftingCounter> giftTypeCounts = checkLimits(pointsGiftRequest.getSenderAccountNumber(),
					pointsGiftRequest.getReceiverAccountNumber(), pointsGiftRequest.getSenderMembershipCode(),
					pointsGiftRequest.getReceiverMembershipCode(), pointsGiftRequest.getGiftPoints(),
					GiftingConstants.GIFT_TYPE_POINTS, resultResponse);

			if (!resultResponse.getApiStatus().getErrors().isEmpty()) {
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return resultResponse;
			}

			if (!customerSegmentCheck(senderAccountDetails, receiverAccountDetails,
					pointsSenderEligibleCustomerSegments, pointsReceiverEligibleCustomerSegments, resultResponse,
					headers)) {
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return resultResponse;
			}

			if (!validateGiftPoints(pointsGiftRequest, senderAccountDetails, resultResponse, headers)) return resultResponse;

			GiftingHistory giftingHistoryBeforePayment = saveGiftingHistory(giftingPointsDto, senderAccountDetails,
					receiverAccountDetails, image, headers, null, pointsGiftRequest.getGiftPoints().intValue(), null,
					null, pointsGiftRequest);
			
			PaymentResponse paymentResponse = pointGiftPayment(pointsGiftRequest, giftingPointsDto,
					senderMemberDetailsDto, senderAccountDetails, headers, giftingHistoryBeforePayment.getId(), resultResponse);

			if (null == paymentResponse) {
				giftingHistoryRepository.delete(giftingHistoryBeforePayment);
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return resultResponse;
			}

			if (!memberActivityPointsGift(pointsGiftRequest, senderMemberDetailsDto, senderAccountDetails,
					receiverMemberDetailsDto, receiverAccountDetails, headers, resultResponse)) {
				giftingHistoryRepository.delete(giftingHistoryBeforePayment);
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return resultResponse;
			}

			resultResponse = updateGiftingCounter(giftTypeCounts, GiftingConstants.GIFT_TYPE_POINTS,
					pointsGiftRequest.getSenderAccountNumber(), pointsGiftRequest.getSenderMembershipCode(),
					pointsGiftRequest.getReceiverAccountNumber(), pointsGiftRequest.getReceiverMembershipCode(),
					pointsGiftRequest.getGiftPoints(), headers, resultResponse);

			if (null != pointsGiftRequest.getReceiverEmail() && !pointsGiftRequest.getReceiverEmail().isEmpty()) {
				receiverAccountDetails.setEmail(pointsGiftRequest.getReceiverEmail());
			}
			
		//	GiftingHistory giftingHistory = saveGiftingHistory(giftingPointsDto, senderAccountDetails,
			//		receiverAccountDetails, image, headers, null, pointsGiftRequest.getGiftPoints().intValue(), null,
				//	paymentResponse, pointsGiftRequest);
			//saveGiftingHistoryAfterPayment(giftingHistoryBeforePayment.getId(), paymentResponse, null != headers.getUserName() ? headers.getUserName() : headers.getToken()) ;
			
//			giftingService.sendSMS(
//					createNotificationHelperDto(GiftingConstants.TYPE_SENDER, giftingHistory,
//							GiftingConstants.GIFT_TYPE_POINTS, pointsGiftRequest.getGiftPoints().intValue(), null,
//							senderAccountDetails, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_SMS),
//					headers);
			giftingService.sendSMS(
					createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, giftingHistoryBeforePayment,
							GiftingConstants.GIFT_TYPE_POINTS, pointsGiftRequest.getGiftPoints().intValue(), null,
							senderAccountDetails, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_SMS),
					headers);
//			giftingService.sendEmail(
//					createNotificationHelperDto(GiftingConstants.TYPE_SENDER, giftingHistory,
//							GiftingConstants.GIFT_TYPE_POINTS, pointsGiftRequest.getGiftPoints().intValue(), null,
//							senderAccountDetails, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL),
//					headers);
//			
//			if((null != pointsGiftRequest.getReceiverEmail() && !pointsGiftRequest.getReceiverEmail().isEmpty())
//					|| (null != receiverAccountDetails.getEmail() && !receiverAccountDetails.getEmail().isEmpty())) {
//				giftingService.sendEmail(
//						createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, giftingHistory,
//								GiftingConstants.GIFT_TYPE_POINTS, pointsGiftRequest.getGiftPoints().intValue(), null,
//								senderAccountDetails, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL),
//						headers);
//			}
			
			resultResponse.setResult(GiftingCodes.GIFTING_POINTS_SUCCESS.getId(),
					GiftingCodes.GIFTING_POINTS_SUCCESS.getMsg());

		} catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
					GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
					GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_POINTS,
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		return resultResponse;

	}

	/**
	 * This method is used to create notification helper DTO.
	 * 
	 * @param type
	 * @param giftingHistory
	 * @param giftType
	 * @param pointsGifted
	 * @param goldGifted
	 * @param senderAccountDetails
	 * @param receiverAccountDetails
	 * @return
	 */
	private NotificationHelperDto createNotificationHelperDto(String type, GiftingHistory giftingHistory,
			String giftType, int pointsGifted, Double goldGifted, GetMemberResponse senderAccountDetails,
			GetMemberResponse receiverAccountDetails, String notificationType) {

		NotificationHelperDto notificationDto = new NotificationHelperDto();

		notificationDto.setType(type);
		notificationDto.setGiftingHistory(giftingHistory);
		notificationDto.setGiftType(giftType);
		notificationDto.setPointsGifted(pointsGifted);
		notificationDto.setGoldGifted(goldGifted);

		notificationDto.setSenderFirstName(senderAccountDetails.getFirstName());
		notificationDto.setSenderContactNumber(senderAccountDetails.getAccountNumber());
	
		notificationDto.setSenderLanguage(GiftingConstants.LANGUAGE_ENGLISH_LIST.contains(configureLanguage(senderAccountDetails))?GiftingConstants.NOTIFICATION_LANG_EN : GiftingConstants.NOTIFICATION_LANG_AR);
		notificationDto.setSenderEmailId(senderAccountDetails.getEmail());
		notificationDto.setSenderAccountNumber(senderAccountDetails.getAccountNumber());

		notificationDto.setReceiverFirstName(receiverAccountDetails.getFirstName());
		notificationDto.setReceiverContactNumber(receiverAccountDetails.getAccountNumber());
		
		notificationDto.setReceiverLanguage(GiftingConstants.LANGUAGE_ENGLISH_LIST.contains(configureLanguage(receiverAccountDetails))?GiftingConstants.NOTIFICATION_LANG_EN : GiftingConstants.NOTIFICATION_LANG_AR);
		notificationDto.setReceiverEmailId(receiverAccountDetails.getEmail());
		notificationDto.setReceiverAccountNumber(receiverAccountDetails.getAccountNumber());
	
		switch (notificationType) {

		case GiftingConstants.NOTIFICATION_TYPE_SMS:

			if (type.equalsIgnoreCase(GiftingConstants.TYPE_SENDER))
				setSenderSMSAttributes(giftType, senderAccountDetails, notificationDto);
			if (type.equalsIgnoreCase(GiftingConstants.TYPE_RECEIVER))
				setReceiverSMSAttributes(giftType, receiverAccountDetails, notificationDto);

			break;

		case GiftingConstants.NOTIFICATION_TYPE_EMAIL:

			if (type.equalsIgnoreCase(GiftingConstants.TYPE_SENDER))
				setSenderEmailAttributes(giftType, senderAccountDetails, notificationDto);
			if (type.equalsIgnoreCase(GiftingConstants.TYPE_RECEIVER))
				setReceiverEmailAttributes(giftType, receiverAccountDetails, notificationDto);

			break;

		}

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.NOTIFICATION_HELPER_DTO,
				this.getClass().getName(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_CREATE_NOTIFICATION_DTO,
				notificationDto);

		return notificationDto;

	}

	/**
	 * This method is used to set sms sender attributes in notification helper DTO.
	 * 
	 * @param giftType
	 * @param senderAccountDetails
	 * @param notificationDto
	 * @return
	 */
	private NotificationHelperDto setSenderSMSAttributes(String giftType, GetMemberResponse senderAccountDetails,
			NotificationHelperDto notificationDto) {

		if (1 == Utility.validateLanguage(notificationDto.getSenderLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_SMS_TEMPLATE_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationId(GiftingConstants.SENDER_SMS_NOTIFICATION_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_SMS_NOTIFICATION_CODE_GIFT_POINTS_EN);
				setSenderSMSGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_SMS_TEMPLATE_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationId(GiftingConstants.SENDER_SMS_NOTIFICATION_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_SMS_NOTIFICATION_CODE_GIFT_GOLD_EN);
				setSenderSMSGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_SMS_TEMPLATE_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationId(GiftingConstants.SENDER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_EN);
				setSenderSMSGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, senderAccountDetails, notificationDto);
			}

		}

		if (2 == Utility.validateLanguage(notificationDto.getSenderLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_SMS_TEMPLATE_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationId(GiftingConstants.SENDER_SMS_NOTIFICATION_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_SMS_NOTIFICATION_CODE_GIFT_POINTS_AR);
				setSenderSMSGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_SMS_TEMPLATE_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationId(GiftingConstants.SENDER_SMS_NOTIFICATION_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_SMS_NOTIFICATION_CODE_GIFT_GOLD_AR);
				setSenderSMSGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_SMS_TEMPLATE_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationId(GiftingConstants.SENDER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_AR);
				setSenderSMSGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, senderAccountDetails, notificationDto);
			}

		}

		return notificationDto;

	}

	/**
	 * This method is used to set sms receiver attributes in notification helper
	 * DTO.
	 * 
	 * @param giftType
	 * @param receiverAccountDetails
	 * @param notificationDto
	 * @return
	 */
	private NotificationHelperDto setReceiverSMSAttributes(String giftType, GetMemberResponse receiverAccountDetails,
			NotificationHelperDto notificationDto) {

		if (1 == Utility.validateLanguage(notificationDto.getReceiverLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_SMS_TEMPLATE_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_SMS_NOTIFICATION_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_SMS_NOTIFICATION_CODE_GIFT_POINTS_EN);
				setReceiverSMSGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_SMS_TEMPLATE_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_SMS_NOTIFICATION_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_SMS_NOTIFICATION_CODE_GIFT_GOLD_EN);
				setReceiverSMSGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_SMS_TEMPLATE_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_EN);
				setReceiverSMSGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, receiverAccountDetails, notificationDto);
			}

		}

		if (2 == Utility.validateLanguage(notificationDto.getReceiverLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_SMS_TEMPLATE_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_SMS_NOTIFICATION_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_SMS_NOTIFICATION_CODE_GIFT_POINTS_AR);
				setReceiverSMSGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_SMS_TEMPLATE_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_SMS_NOTIFICATION_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_SMS_NOTIFICATION_CODE_GIFT_GOLD_AR);
				setReceiverSMSGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_SMS_TEMPLATE_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_AR);
				setReceiverSMSGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, receiverAccountDetails, notificationDto);
			}

		}

		return notificationDto;

	}

	/**
	 * This method is used to set email sender attributes in notification helper
	 * DTO.
	 * 
	 * @param giftType
	 * @param senderAccountDetails
	 * @param notificationDto
	 * @return
	 */
	private NotificationHelperDto setSenderEmailAttributes(String giftType, GetMemberResponse senderAccountDetails,
			NotificationHelperDto notificationDto) {

		if (1 == Utility.validateLanguage(notificationDto.getSenderLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_EMAIL_TEMPLATE_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationId(GiftingConstants.SENDER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_EN);
				setSenderEmailGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_EMAIL_TEMPLATE_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationId(GiftingConstants.SENDER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_EN);
				setSenderEmailGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationId(GiftingConstants.SENDER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_EN);
				setSenderEmailGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, senderAccountDetails, notificationDto);
			}

		}

		if (2 == Utility.validateLanguage(notificationDto.getSenderLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_EMAIL_TEMPLATE_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationId(GiftingConstants.SENDER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_AR);
				setSenderEmailGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_EMAIL_TEMPLATE_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationId(GiftingConstants.SENDER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_AR);
				setSenderEmailGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, senderAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.SENDER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationId(GiftingConstants.SENDER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationCode(GiftingConstants.SENDER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_AR);
				setSenderEmailGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, senderAccountDetails, notificationDto);
			}

		}

		return notificationDto;

	}

	/**
	 * This method is used to set email receiver attributes in notification helper
	 * DTO.
	 * 
	 * @param giftType
	 * @param receiverAccountDetails
	 * @param notificationDto
	 * @return
	 */
	private NotificationHelperDto setReceiverEmailAttributes(String giftType, GetMemberResponse receiverAccountDetails,
			NotificationHelperDto notificationDto) {

		if (1 == Utility.validateLanguage(notificationDto.getReceiverLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_EMAIL_TEMPLATE_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_EN);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_EN);
				setReceiverEmailGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_EMAIL_TEMPLATE_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_EN);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_EN);
				setReceiverEmailGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_EN);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_EN);
				setReceiverEmailGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, receiverAccountDetails, notificationDto);
			}

		}

		if (2 == Utility.validateLanguage(notificationDto.getReceiverLanguage())) {

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_EMAIL_TEMPLATE_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_AR);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_AR);
				setReceiverEmailGiftAttributes(GiftingConstants.GIFT_TYPE_POINTS, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_EMAIL_TEMPLATE_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_AR);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_AR);
				setReceiverEmailGiftAttributes(GiftingConstants.GIFT_TYPE_GOLD, receiverAccountDetails, notificationDto);
			}

			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
				notificationDto.setTemplateId(GiftingConstants.RECEIVER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationId(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_AR);
				notificationDto.setNotificationCode(GiftingConstants.RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_AR);
				setReceiverEmailGiftAttributes(GiftingConstants.GIFT_TYPE_VOUCHER, receiverAccountDetails, notificationDto);
			}

		}

		return notificationDto;

	}

	private NotificationHelperDto setSenderSMSGiftAttributes(String giftType, GetMemberResponse senderAccountDetails,
			NotificationHelperDto notificationDto) {

		Map<String, String> additionalParam = new HashMap<>();
		
		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		return notificationDto;

	}
	
	private NotificationHelperDto setReceiverSMSGiftAttributes(String giftType, GetMemberResponse receiverAccountDetails,
			NotificationHelperDto notificationDto) {

		Map<String, String> additionalParam = new HashMap<>();
		
		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			additionalParam.put(GiftingConstants.SMS_FIRST_NAME_SENDER, notificationDto.getSenderFirstName());
			additionalParam.put(GiftingConstants.SMS_GIFT_ID, notificationDto.getGiftingHistory().getId());
			additionalParam.put(GiftingConstants.SMS_POINTS_GIFTED, String.valueOf(notificationDto.getPointsGifted()));
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		return notificationDto;

	}
	
	private NotificationHelperDto setSenderEmailGiftAttributes(String giftType, GetMemberResponse senderAccountDetails,
			NotificationHelperDto notificationDto) {

		Map<String, String> additionalParam = new HashMap<>();
		
		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		return notificationDto;

	}
	
	private NotificationHelperDto setReceiverEmailGiftAttributes(String giftType, GetMemberResponse receiverAccountDetails,
			NotificationHelperDto notificationDto) {

		Map<String, String> additionalParam = new HashMap<>();
		
		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
			notificationDto.setAdditionalParam(additionalParam);
		}

		return notificationDto;

	}

	/**
	 * This method handles payment while gifting points.
	 * 
	 * @param pointsGiftRequest
	 * @param giftingPointsDto
	 * @param senderMemberDetailsDto
	 * @param senderAccountDetails
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	private PaymentResponse pointGiftPayment(PointsGiftRequest pointsGiftRequest, GiftingRequest giftingPointsDto,
			GetMemberResponseDto senderMemberDetailsDto, GetMemberResponse senderAccountDetails, Headers headers,
			String id, ResultResponse resultResponse) {

		PaymentResponse paymentResponse = null;
		if (pointsGiftRequest.getPurchaseDetails().getSpentAmount() > 0.0) {
			paymentResponse = giftingPayment(giftingPointsDto, MarketplaceConstants.FULLCREDITCARD.getConstant(),
					senderMemberDetailsDto, senderAccountDetails, headers, id, resultResponse);
		} else {
			pointsGiftRequest.getPurchaseDetails().setSpentAmount(0.0);
			paymentResponse = giftingPayment(giftingPointsDto, GiftingConstants.PARTIAL_POINTS_CC,
					senderMemberDetailsDto, senderAccountDetails, headers, id, resultResponse);
		}

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.PAYMENT_RESPONSE,
				this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_POINTS, paymentResponse);

		return paymentResponse;

	}

	/**
	 * This method handles redemption and accrual activities for points gifting.
	 * 
	 * @param pointsGiftRequest
	 * @param senderMemberDetailsDto
	 * @param senderAccountDetails
	 * @param receiverMemberDetailsDto
	 * @param receiverAccountDetails
	 * @param headers
	 * @param resultResponse
	 * @return
	 * @throws MarketplaceException
	 */
	private boolean memberActivityPointsGift(PointsGiftRequest pointsGiftRequest,
			GetMemberResponseDto senderMemberDetailsDto, GetMemberResponse senderAccountDetails,
			GetMemberResponseDto receiverMemberDetailsDto, GetMemberResponse receiverAccountDetails, Headers headers,
			ResultResponse resultResponse) throws MarketplaceException {

		MemberActivityPaymentDto redemptionActivityDto = createMemberActivityRequest(pointsGiftRequest,
				senderMemberDetailsDto, senderAccountDetails, receiverMemberDetailsDto, receiverAccountDetails, headers,
				GiftingConstants.TRANSACTION_TYPE_RDM);

		if (!memberAccrualRedemption(redemptionActivityDto, resultResponse, headers, pointsGiftRequest,
				GiftingConstants.TRANSACTION_TYPE_RDM))
			return false;

		if (null == pointsGiftRequest.getScheduledDate() || (null != pointsGiftRequest.getScheduledDate()
				&& Utility.isTodayDate(pointsGiftRequest.getScheduledDate()))) {

			MemberActivityPaymentDto accrualActivityDto = createMemberActivityRequest(pointsGiftRequest,
					senderMemberDetailsDto, senderAccountDetails, receiverMemberDetailsDto, receiverAccountDetails,
					headers, GiftingConstants.TRANSACTION_TYPE_ACR);

			if (!memberAccrualRedemption(accrualActivityDto, resultResponse, headers, pointsGiftRequest,
					GiftingConstants.TRANSACTION_TYPE_ACR))
				return false;

		}

		return true;

	}

	/**
	 * This method is used to validate points gift parameters.
	 * 
	 * @param pointsGiftRequest
	 * @param image
	 * @param resultResponse
	 * @return
	 */
	private boolean validatePointsGiftParameters(PointsGiftRequest pointsGiftRequest, MarketplaceImage image,
			ResultResponse resultResponse) {

		if (!GiftingValidator.validateRequestParameters(pointsGiftRequest, validator, resultResponse)) {
			resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
					GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
		}

		GiftingLimit giftingLimit = giftingLimitRepository.findByGiftType(GiftingConstants.GIFT_TYPE_POINTS);
		if (Double.compare(giftingLimit.getFee(), pointsGiftRequest.getFee()) != 0) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_POINTS_FAILURE_WRONG_FEE.getIntId(),
					GiftingCodes.GIFTING_POINTS_FAILURE_WRONG_FEE.getMsg() + String.valueOf(giftingLimit.getFee()));
		}

		if (image == null) {
			resultResponse.addErrorAPIResponse(GiftingCodes.NO_IMAGES_AVAILABLE.getIntId(),
					GiftingCodes.NO_IMAGES_AVAILABLE.getMsg());
		}

		if (null != pointsGiftRequest.getScheduledDate()
				&& Utility.checkPastDate(pointsGiftRequest.getScheduledDate())) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_DATE_IS_PAST.getIntId(),
					GiftingCodes.SCHEDULED_DATE_IS_PAST.getMsg());
		}

		if (null != pointsGiftRequest.getPurchaseDetails()
				&& null == pointsGiftRequest.getPurchaseDetails().getSpentAmount())
			pointsGiftRequest.getPurchaseDetails().setSpentAmount(0.0);

		return resultResponse.getApiStatus().getErrors().isEmpty();

	}

	/**
	 * This method is used to validate customer segments for sender and receiver
	 * accounts - gifting points/gold.
	 * 
	 * @param senderAccountDetails
	 * @param receiverAccountDetails
	 * @param customerSegmentsSender
	 * @param customerSegmentsReceiver
	 * @param resultResponse
	 * @return
	 * @throws MarketplaceException
	 */
	private boolean customerSegmentCheck(GetMemberResponse senderAccountDetails,
			GetMemberResponse receiverAccountDetails, List<String> customerSegmentsSender,
			List<String> customerSegmentsReceiver, ResultResponse resultResponse, Headers headers)
			throws MarketplaceException {

		List<GetMemberResponse> memberInfoList = new ArrayList<>();
		memberInfoList.add(senderAccountDetails);
		memberInfoList.add(receiverAccountDetails);

		CustomerSegmentDMRequestDto decisionManagerRequestDto = Requests.getDecisionManagerRequestDto(memberInfoList,
				null);

		CustomerSegmentResult decisionManagerResult = fetchServiceValues.checkCustomerSegment(decisionManagerRequestDto,
				headers, resultResponse);

		if(null == decisionManagerResult) {
			resultResponse.addErrorAPIResponse(GiftingCodes.DECISION_MANAGER_ERROR_RESPONSE.getIntId(),
					GiftingCodes.DECISION_MANAGER_ERROR_RESPONSE.getMsg());
			return false;
		}
		
		boolean senderSegmentCheck = false;
		boolean receiverSegmentCheck = false;

		for (RuleResult ruleResult : decisionManagerResult.getRulesResult()) {
			if ((ruleResult.getAccountNumber().equals(senderAccountDetails.getAccountNumber()))
					&& (null != ruleResult.getCustomerSegments() && !ruleResult.getCustomerSegments().isEmpty())) {
				List<String> result = ruleResult.getCustomerSegments().stream().map(String::toUpperCase)
						.collect(Collectors.toList());
				List<String> cusSegSender = customerSegmentsSender.stream().map(String::toUpperCase)
						.collect(Collectors.toList());
				LOG.info("Customer segments from DM for the sender account : {}", result);
				LOG.info("Gifting customer segments configured for sender : {} ", cusSegSender);
				senderSegmentCheck = CollectionUtils.containsAny(result, cusSegSender);
			}

			if ((ruleResult.getAccountNumber().equals(receiverAccountDetails.getAccountNumber()))
					&& (null != ruleResult.getCustomerSegments() && !ruleResult.getCustomerSegments().isEmpty())) {
				List<String> result = ruleResult.getCustomerSegments().stream().map(String::toUpperCase)
						.collect(Collectors.toList());
				List<String> cusSegReceiver = customerSegmentsReceiver.stream().map(String::toUpperCase)
						.collect(Collectors.toList());
				LOG.info("Customer segments from DM for the receiver account : {}", result);
				LOG.info("Gifting customer segments configured for receiver : {} ", cusSegReceiver);
				receiverSegmentCheck = CollectionUtils.containsAny(result, cusSegReceiver);
			}
		}

		if (!senderSegmentCheck) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_CUSTOMER_SEGMENT_CHECK_FAILED.getIntId(),
					GiftingCodes.SENDER_CUSTOMER_SEGMENT_CHECK_FAILED.getMsg());
		}

		if (!receiverSegmentCheck) {
			resultResponse.addErrorAPIResponse(GiftingCodes.RECEIVER_CUSTOMER_SEGMENT_CHECK_FAILED.getIntId(),
					GiftingCodes.RECEIVER_CUSTOMER_SEGMENT_CHECK_FAILED.getMsg());
		}
		return resultResponse.getApiStatus().getErrors().isEmpty();
	}

	/**
	 * This method is to create request object for Member Activity Accrual &
	 * Redemption.
	 * 
	 * @param pointsGiftRequest
	 * @param senderMemberDetailsDto
	 * @param senderAccountDetails
	 * @param receiverMemberDetailsDto
	 * @param receiverAccountDetails
	 * @param headers
	 * @param transactionType
	 * @return
	 */
	private MemberActivityPaymentDto createMemberActivityRequest(PointsGiftRequest pointsGiftRequest,
			GetMemberResponseDto senderMemberDetailsDto, GetMemberResponse senderAccountDetails,
			GetMemberResponseDto receiverMemberDetailsDto, GetMemberResponse receiverAccountDetails, Headers headers,
			String transactionType) {

		DateFormat dateFormat = new SimpleDateFormat(GiftingConstants.STRING_DATE_FORMAT);
		String currentDate = dateFormat.format(new Date());

		MemberActivityPaymentDto memberActivityDto = new MemberActivityPaymentDto();
		memberActivityDto.setAccountNumber(transactionType.equalsIgnoreCase(GiftingConstants.TRANSACTION_TYPE_RDM)
				? senderAccountDetails.getAccountNumber()
				: receiverAccountDetails.getAccountNumber());
		memberActivityDto.setMembershipCode(transactionType.equalsIgnoreCase(GiftingConstants.TRANSACTION_TYPE_RDM)
				? senderAccountDetails.getMembershipCode()
				: receiverAccountDetails.getMembershipCode());
		memberActivityDto.setPartnerCode(GiftingConstants.POINT_CONVERSION_PARTNER_CODE);
		memberActivityDto.setActivityCode(transactionType.equalsIgnoreCase(GiftingConstants.TRANSACTION_TYPE_RDM)
				? GiftingConstants.MA_ACTIVITY_CODE_REDEMPTION
				: GiftingConstants.MA_ACTIVITY_CODE_ACCRUAL);
		memberActivityDto.setEventDate(currentDate);
		memberActivityDto.setSpendValue(!pointsGiftRequest.getPurchaseDetails().getSpentAmount().equals(0.0)
				? pointsGiftRequest.getPurchaseDetails().getSpentAmount()
				: pointsGiftRequest.getGiftPoints());
		memberActivityDto.setExternalReferenceNumber(headers.getExternalTransactionId());
		memberActivityDto.setRedemptionType(transactionType.equalsIgnoreCase(GiftingConstants.TRANSACTION_TYPE_RDM)
				? GiftingConstants.REDEMPTION_TYPE
				: null);
		memberActivityDto.setPointsValue(pointsGiftRequest.getGiftPoints().intValue());
		memberActivityDto.setMemberResponse(
				transactionType.equalsIgnoreCase(GiftingConstants.TRANSACTION_TYPE_RDM) ? senderMemberDetailsDto
						: receiverMemberDetailsDto);

		return memberActivityDto;

	}

	/**
	 * This method is used to make a call to Member Activity microservice for
	 * Accrual & Redemption activity.
	 * 
	 * @param memberActivityPaymentDto
	 * @param resultResponse
	 * @param header
	 * @param pointsGiftRequest
	 * @param transactionType
	 * @return
	 * @throws MarketplaceException
	 */
	public boolean memberAccrualRedemption(MemberActivityPaymentDto memberActivityPaymentDto,
			ResultResponse resultResponse, Headers header, PointsGiftRequest pointsGiftRequest, String transactionType)
			throws MarketplaceException {

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.MEMBER_ACTIVITY_REQUEST,
				this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_MEMBER_ACCRUAL_REDEMTION,
				memberActivityPaymentDto);

		MemberActivityResponse memberActivityResponse = giftingService.memberAccrualRedemption(memberActivityPaymentDto,
				header, resultResponse);

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.MEMBER_ACTIVITY_RESPONSE,
				this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_MEMBER_ACCRUAL_REDEMTION,
				memberActivityResponse);

		if (!resultResponse.getApiStatus().getErrors().isEmpty())
			return false;

		if (transactionType.equalsIgnoreCase(GiftingConstants.TRANSACTION_TYPE_RDM)) {
			pointsGiftRequest.setSenderTransactionRefId(memberActivityResponse.getTransactionRefId());
		}

		if (transactionType.equalsIgnoreCase(GiftingConstants.TRANSACTION_TYPE_ACR)) {
			pointsGiftRequest.setReceiverTransactionRefId(memberActivityResponse.getTransactionRefId());
		}

		return true;
	}

	/**
	 * This method is used to call payment service for gold/points gifting
	 * transactions.
	 * 
	 * @param giftRequest
	 * @param paymentType
	 * @param senderMemberDetailsDto
	 * @param senderAccountDetails
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	private PaymentResponse giftingPayment(GiftingRequest giftRequest, String paymentType,
			GetMemberResponseDto senderMemberDetailsDto, GetMemberResponse senderAccountDetails, Headers headers,
			String id, ResultResponse resultResponse) {

		PurchaseRequestDto purchaseRequest = new PurchaseRequestDto();
		purchaseRequest.setAccountNumber(senderAccountDetails.getAccountNumber());
		purchaseRequest.setMembershipCode(senderAccountDetails.getMembershipCode());
		purchaseRequest.setSelectedOption(paymentType);

		if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			purchaseRequest.setSpentPoints(giftRequest.getGiftPoints().intValue());
		} else {
			purchaseRequest.setSpentPoints(0);
		}
		if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			purchaseRequest.setSpentAmount(giftRequest.getPurchaseDetails().getSpentAmount() + giftRequest.getFee());
		} else {
			purchaseRequest.setSpentAmount(giftRequest.getFee());
		}

		purchaseRequest.setAuthorizationCode(giftRequest.getPurchaseDetails().getAuthorizationCode());
		purchaseRequest.setCardExpiryDate(giftRequest.getPurchaseDetails().getCardExpiryDate());
		purchaseRequest.setCardNumber(giftRequest.getPurchaseDetails().getCardNumber());
		purchaseRequest.setCardSubType(giftRequest.getPurchaseDetails().getCardSubType());
		purchaseRequest.setCardToken(giftRequest.getPurchaseDetails().getCardToken());
		purchaseRequest.setCardType(giftRequest.getPurchaseDetails().getCardType());
		purchaseRequest.setExtTransactionId(headers.getExternalTransactionId());
		purchaseRequest.setAtgUserName(headers.getUserName());

		if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			purchaseRequest.setSelectedPaymentItem(GiftingConstants.POINT_GIFTING_PAYMENT_ITEM);
		} else {
			purchaseRequest.setSelectedPaymentItem(GiftingConstants.GOLD_GIFTING_PRODUCT_ITEM);
		}
		purchaseRequest.setEpgTransactionId(giftRequest.getPurchaseDetails().getEpgTransactionId());
		purchaseRequest.setUiLanguage(giftRequest.getPurchaseDetails().getUiLanguage());

		PaymentAdditionalRequest paymentAdditionalRequest = new PaymentAdditionalRequest();
		paymentAdditionalRequest.setChannelId(headers.getChannelId());
		paymentAdditionalRequest.setActivityCode(null);
		paymentAdditionalRequest.setUuid(id);
		paymentAdditionalRequest.setPaymentRequired(true);
		paymentAdditionalRequest.setHeader(headers);
		
		LOG.info("Payment Request: {}", purchaseRequest);
		LOG.info("Payment Additional Request: {}", paymentAdditionalRequest);
		
		PaymentResponse paymentResponse = paymentService.paymentAndProvisioning(purchaseRequest, senderMemberDetailsDto,
				null, null, paymentAdditionalRequest);

		if (null == paymentResponse || null == paymentResponse.getPaymentStatus()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.PAYMENT_FAIL.getIntId(),
					GiftingCodes.PAYMENT_FAIL.getMsg());
			if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			} else {
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			}
			return null;
		} else if (paymentResponse.getPaymentStatus().equalsIgnoreCase(GiftingConstants.PAYMENT_FAILED_STATUS)) {
			resultResponse.addErrorAPIResponse(GiftingCodes.PAYMENT_FAILED.getIntId(),
					GiftingCodes.PAYMENT_FAILED.getMsg() + paymentResponse.getFailedreason());
			if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			} else {
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			}
			return null;
		}

		return paymentResponse;

	}

	/**
	 * This method is used to validate points and spent amount received in request
	 * and check sender's points availability for redemption.
	 * 
	 * @param pointsGiftRequest
	 * @param senderAccountDetails
	 * @param resultResponse
	 * @param header
	 * @return
	 * @throws MarketplaceException
	 */
	private boolean validateGiftPoints(PointsGiftRequest pointsGiftRequest, GetMemberResponse senderAccountDetails,
			ResultResponse resultResponse, Headers header) throws MarketplaceException {

		if (null != pointsGiftRequest.getPurchaseDetails()
				&& null != pointsGiftRequest.getPurchaseDetails().getSpentAmount()
				&& pointsGiftRequest.getPurchaseDetails().getSpentAmount() > 0.0) {

			List<ConversionRate> conversionRateList = helper.fetchConversionRateList(
					GiftingConstants.POINT_CONVERSION_PARTNER_CODE, GiftingConstants.POINT_GIFTING_PRODUCT_ITEM,
					header.getChannelId(), resultResponse);
			Integer equivalentPoints = ProcessValues.getEquivalentPoints(conversionRateList,
					pointsGiftRequest.getPurchaseDetails().getSpentAmount());

			if (null == equivalentPoints) {
				resultResponse.addErrorAPIResponse(GiftingCodes.EQUIVALENT_POINTS_CONVERSION_FAILED.getIntId(),
						GiftingCodes.EQUIVALENT_POINTS_CONVERSION_FAILED.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return false;
			}

			if (Integer.compare(pointsGiftRequest.getGiftPoints().intValue(), equivalentPoints) != 0) {
				resultResponse.addErrorAPIResponse(GiftingCodes.POINT_SPEND_AMOUNT_UNEQUAL.getIntId(),
						GiftingCodes.POINT_SPEND_AMOUNT_UNEQUAL.getMsg() + String.valueOf(equivalentPoints)
								+ GiftingConstants.COMMA_SEPARATOR
								+ String.valueOf(pointsGiftRequest.getGiftPoints().intValue()));
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return false;
			}

		} else {

			if (!checkMemberPointsAvailability(pointsGiftRequest.getSenderAccountNumber(),
					senderAccountDetails.getMembershipCode(), pointsGiftRequest.getGiftPoints(), resultResponse,
					header)) {
				resultResponse.addErrorAPIResponse(GiftingCodes.MEMBER_INSUFFICIENT_POINTS.getIntId(),
						GiftingCodes.MEMBER_INSUFFICIENT_POINTS.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
				return false;
			}

		}

		return true;

	}

	/**
	 * This method is used to save points/voucher/gold gifing transaction to
	 * GiftingHistory collection.
	 * 
	 * @param giftRequest
	 * @param senderAccountDetails
	 * @param receiverAccountDetails
	 * @param image
	 * @param headers
	 * @param voucherCode
	 * @param points
	 * @param goldCertificate
	 * @param paymentResponse
	 * @param pointsGiftRequest
	 * @return
	 * @throws MarketplaceException
	 */
	private GiftingHistory saveGiftingHistory(GiftingRequest giftRequest, GetMemberResponse senderAccountDetails,
			GetMemberResponse receiverAccountDetails, MarketplaceImage image, Headers headers, String voucherCode,
			int points, List<GoldGiftedDetails> goldCertificate, PaymentResponse paymentResponse,
			PointsGiftRequest pointsGiftRequest) throws MarketplaceException {

		PointsGiftedDetailsDomain pointsGiftedDetailsDomain = null;

		String giftType = "";
		String paymentTransactionNum = (paymentResponse == null) ? "" : paymentResponse.getTransactionNo();
		if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {
			giftType = GiftingConstants.GIFT_TYPE_VOUCHER;
		} else if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {
			giftType = GiftingConstants.GIFT_TYPE_POINTS;
			pointsGiftedDetailsDomain = new PointsGiftedDetailsDomain.PointsGiftedDetailsBuilder(points)
					.senderTransactionRefId(pointsGiftRequest.getSenderTransactionRefId())
					.receiverTransactionRefId(pointsGiftRequest.getReceiverTransactionRefId()).build();
		}
		if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
			giftType = GiftingConstants.GIFT_TYPE_GOLD;
		}
		MemberInfoDomain senderDomain = new MemberInfoDomain.MemberInfoBuilder(senderAccountDetails.getAccountNumber(),
				senderAccountDetails.getMembershipCode()).firstName(senderAccountDetails.getFirstName())
						.lastName(senderAccountDetails.getLastName()).email(senderAccountDetails.getEmail()).build();

		MemberInfoDomain receiverDomain = new MemberInfoDomain.MemberInfoBuilder(
				receiverAccountDetails.getAccountNumber(), receiverAccountDetails.getMembershipCode())
						.firstName(receiverAccountDetails.getFirstName()).lastName(receiverAccountDetails.getLastName())
						.email(receiverAccountDetails.getEmail()).build();
		PurchaseDetailsDomain purchaseDomain;
		if (giftRequest.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER) && (giftRequest.getPurchaseDetails()==null)) {
			purchaseDomain = null;
		}
		else {
			purchaseDomain = new PurchaseDetailsDomain.PurchaseDetailsBuilder(
				giftRequest.getPurchaseDetails().getCardNumber(), giftRequest.getPurchaseDetails().getCardType(),
				giftRequest.getPurchaseDetails().getCardSubType(), giftRequest.getPurchaseDetails().getCardToken(),
				giftRequest.getPurchaseDetails().getCardExpiryDate(),
				giftRequest.getPurchaseDetails().getAuthorizationCode(),
				giftRequest.getPurchaseDetails().getSpentAmount(),
				giftRequest.getPurchaseDetails().getEpgTransactionId(),
				giftRequest.getPurchaseDetails().getUiLanguage()).build();
		}
		GiftingHistoryDomain giftingHistoryDomainObj = new GiftingHistoryDomain.GiftingHistoryBuilder(giftType)
				.programCode(headers.getProgram()).voucherCode(voucherCode).senderInfo(senderDomain)
				.receiverInfo(receiverDomain).message(giftRequest.getMessage()).pointsGifted(pointsGiftedDetailsDomain)
				.goldGifted(goldCertificate).imageId(giftRequest.getImageId()).imageUrl(image.getImageUrl())
				.purchaseDetailsDomain(purchaseDomain)
				.scheduledDate(giftRequest.getScheduledDate() == null ? new Date() : giftRequest.getScheduledDate())
				.transactionNo(paymentTransactionNum)
				.transactionDate(new Date())
				.extRefNo(headers.getExternalTransactionId())
				.receiverConsumption(GiftingConstants.RECEIVER_CONSUMPTION_NO)
				.createdUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken())
				.createdDate(new Date()).build();

		return giftingHistoryDomain.saveUpdateGiftingHistory(giftingHistoryDomainObj, GiftingConstants.ACTION_INSERT,
				headers, null, GiftingConfigurationConstants.GIFT);

	}

	/**
	 * This method is used to gift gold
	 * 
	 * @param giftingGoldDto
	 * @param headers
	 * @param resultResponse
	 * 
	 */
	public ResultResponse giftGold(GiftingRequest giftingGoldDto, Headers headers, ResultResponse resultResponse) {

		try {
			
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			
			GoldGiftRequest goldGiftRequest = modelMapper.map(giftingGoldDto, GoldGiftRequest.class);

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.GOLD_GIFT_REQUEST_PARAMS,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_GOLD, goldGiftRequest);

			if (!GiftingValidator.validateRequestParameters(goldGiftRequest, validator, resultResponse)) {
				resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_GOLD_FAILURE.getIntId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
				return resultResponse;
			}

			if(goldGiftRequest.getIsScheduled()!=null && goldGiftRequest.getIsScheduled().equalsIgnoreCase("Yes")) {
				if(goldGiftRequest.getScheduledDate()==null) {
					resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_DATE_REQD.getIntId(),
							GiftingCodes.SCHEDULED_DATE_REQD.getMsg());
					resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
							GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
					return resultResponse;
				}
			}
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			now.set(Calendar.HOUR_OF_DAY, 0);  
			now.set(Calendar.MINUTE, 0);  
			now.set(Calendar.SECOND, 0);  
			now.set(Calendar.MILLISECOND, 0);  
			goldGiftRequest.setScheduledDate((goldGiftRequest.getScheduledDate()!=null) ? goldGiftRequest.getScheduledDate() : new Date());   
			if(goldGiftRequest.getScheduledDate().before(now.getTime())) {
				resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_DATE_PAST.getIntId(),
						GiftingCodes.SCHEDULED_DATE_PAST.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
				return resultResponse;
			}
			
			GiftingLimit findByGiftType = giftingLimitRepository.findByGiftType(GiftingConstants.GIFT_TYPE_GOLD);
			if (findByGiftType.getFee().compareTo(goldGiftRequest.getFee()) != 0) {
				resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_GOLD_FAILURE_WRONG_FEE.getIntId(),
						GiftingCodes.GIFTING_GOLD_FAILURE_WRONG_FEE.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE_WRONG_FEE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE_WRONG_FEE.getMsg());
				return resultResponse;
			}

			GetMemberResponseDto senderMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
					goldGiftRequest.getSenderAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(),
					headers, resultResponse);
			GetMemberResponse senderAccountDetails = ProcessValues.getMemberInfo(senderMemberDetailsDto,
					goldGiftRequest.getSenderAccountNumber());

			GetMemberResponseDto receiverMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
					goldGiftRequest.getReceiverAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(),
					headers, resultResponse);
			GetMemberResponse receiverAccountDetails = ProcessValues.getMemberInfo(receiverMemberDetailsDto,
					goldGiftRequest.getReceiverAccountNumber());

			LOG.info(
					GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
							+ GiftingConstants.SENDER_RECEIVER_MEMBER_DETAILS,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_GOLD, senderAccountDetails,
					receiverAccountDetails);

			if (!senderAccountValidation(senderAccountDetails, resultResponse, giftingGoldDto.getGiftType())) {
				return resultResponse;
			}
			
			GoldCertificate goldDetailsSender = goldCertificateRepository.findByAccountNumberAndMembershipCode(
					senderAccountDetails.getAccountNumber(), senderAccountDetails.getMembershipCode());
			if (goldDetailsSender == null || goldDetailsSender.getTotalGoldBalance() < goldGiftRequest.getGiftGold()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_GOLD_FAILURE_INSUF.getIntId(),
						GiftingCodes.GIFTING_GOLD_FAILURE_INSUF.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE_INSUF.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE_INSUF.getMsg());
				return resultResponse;
			}

			if (receiverAccountDetails == null) {
				resultResponse.setSuccessAPIResponse();
				resultResponse.getApiStatus().setErrors(new ArrayList<>());
				receiverAccountDetails = enrollReceiverValidation(giftingGoldDto, resultResponse, headers);
				if (receiverAccountDetails == null)
					return resultResponse;
				
				LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS_AFTER_ENROLL,
						this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_GOLD, receiverAccountDetails);
				
			}
			resultResponse.setSuccessAPIResponse();
			resultResponse.getApiStatus().setErrors(new ArrayList<>());

			MarketplaceImage image = imageRepository.findByImageId(goldGiftRequest.getImageId());
			if (image == null) {
				resultResponse.addErrorAPIResponse(GiftingCodes.NO_IMAGES_AVAILABLE.getIntId(),
						GiftingCodes.NO_IMAGES_AVAILABLE.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
				return resultResponse;
			}

			List<GiftingCounter> giftTypeCounts = checkLimits(senderAccountDetails.getAccountNumber(),
					receiverAccountDetails.getAccountNumber(), senderAccountDetails.getMembershipCode(),
					receiverAccountDetails.getMembershipCode(), goldGiftRequest.getGiftGold(),
					GiftingConstants.GIFT_TYPE_GOLD, resultResponse);

			if (!resultResponse.getApiStatus().getErrors().isEmpty()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_GOLD_FAILURE.getIntId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
				return resultResponse;
			}

			if (!customerSegmentCheck(senderAccountDetails, receiverAccountDetails, goldSenderEligibleCustomerSegments,
					goldReceiverEligibleCustomerSegments, resultResponse, headers)) {
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
				return resultResponse;
			}

			if(null != goldGiftRequest.getReceiverEmail() && !goldGiftRequest.getReceiverEmail().isEmpty()) {
				receiverAccountDetails.setEmail(goldGiftRequest.getReceiverEmail());
			}

			GiftingHistory giftingHistory = goldGiftingCalculationAndSave(goldGiftRequest, senderAccountDetails,
					receiverAccountDetails, goldDetailsSender, headers, giftingGoldDto, image, null, resultResponse,senderMemberDetailsDto);

			if (null == giftingHistory) {
				return resultResponse;
			}
			
			resultResponse = updateGiftingCounter(giftTypeCounts, GiftingConstants.GIFT_TYPE_GOLD,
					senderAccountDetails.getAccountNumber(), senderAccountDetails.getMembershipCode(),
					receiverAccountDetails.getAccountNumber(), receiverAccountDetails.getMembershipCode(),
					goldGiftRequest.getGiftGold(), headers, resultResponse);
//			giftingService.sendSMS(createNotificationHelperDto(GiftingConstants.TYPE_SENDER, giftingHistory,
//					GiftingConstants.GIFT_TYPE_GOLD, 0, giftingGoldDto.getGiftGold(), senderAccountDetails,
//					receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_SMS), headers);
//			giftingService.sendSMS(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, giftingHistory,
//					GiftingConstants.GIFT_TYPE_GOLD, 0, giftingGoldDto.getGiftGold(), senderAccountDetails,
//					receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_SMS), headers);
//			giftingService.sendEmail(createNotificationHelperDto(GiftingConstants.TYPE_SENDER, giftingHistory,
//					GiftingConstants.GIFT_TYPE_GOLD, 0, giftingGoldDto.getGiftGold(), senderAccountDetails,
//					receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL), headers);
//			
//			if((null != goldGiftRequest.getReceiverEmail() && !goldGiftRequest.getReceiverEmail().isEmpty())
//					|| (null != receiverAccountDetails.getEmail() && !receiverAccountDetails.getEmail().isEmpty())) {
//				giftingService.sendEmail(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, giftingHistory,
//						GiftingConstants.GIFT_TYPE_GOLD, 0, giftingGoldDto.getGiftGold(), senderAccountDetails,
//						receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL), headers);
//			}
//			
			resultResponse.setResult(GiftingCodes.GIFTING_GOLD_SUCCESS.getId(),
					GiftingCodes.GIFTING_GOLD_SUCCESS.getMsg());

		} catch (MarketplaceException me) {
			me.printMessage();
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
					GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		}

		catch (Exception me) {
			me.printStackTrace();
			resultResponse.addErrorAPIResponse(me.hashCode(), me.getMessage());
			resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
					GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			LOG.error(me.getMessage());
		}
		return resultResponse;
	}

	/**
	 * Enroll an account in member management
	 * 
	 * @param accountNumber
	 * @param header
	 * @return enrollment response
	 * @throws MarketplaceException
	 */
	public EnrollmentResultResponse enrollMember(String accountNumber, Headers header) throws MarketplaceException {
		return giftingService.enrollMember(accountNumber, header);
	}

	/**
	 * This method is used to check points availibility for redemption from sender's
	 * account - points gifting.
	 * 
	 * @param accountNumber
	 * @param membershipCode
	 * @param giftPoints
	 * @param resultResponse
	 * @param header
	 * @return
	 * @throws MarketplaceException
	 */
	public boolean checkMemberPointsAvailability(String accountNumber, String membershipCode, Double giftPoints,
			ResultResponse resultResponse, Headers header) throws MarketplaceException {

		int senderPoints = giftingService.retrieveMemberPoints(accountNumber, membershipCode, resultResponse, header);

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SENDER_AVAILABLE_POINTS,
				this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_POINTS_AVAILABILITY,
				String.valueOf(senderPoints));

		if (Double.compare(Double.valueOf(senderPoints), giftPoints) < 0) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_POINTS_UNAVAILABLE.getIntId(),
					GiftingCodes.SENDER_POINTS_UNAVAILABLE.getMsg() + String.valueOf(senderPoints));
			resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
					GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			return false;
		}

		return true;
	}

	/**
	 * This method is used to send email and sms after gifting a voucher
	 * 
	 * @param id
	 * @param senderEmailId
	 * @param receiverEmailId
	 * @param image
	 * @param externalTransactionId
	 * @throws VoucherManagementException
	 * @throws Exception
	 */
	public void sendEmailSms(String id, String senderEmailId, String receiverEmailId, MarketplaceImage image,
			String externalTransactionId) throws VoucherManagementException, Exception {
		Optional<GiftingHistory> voucherGiftDetails;
		voucherGiftDetails = giftingHistoryRepository.findById(id);
		if (voucherGiftDetails.isPresent()) {
			Voucher vou = voucherRepository.findByVoucherCode(voucherGiftDetails.get().getVoucherCode());
			OfferCatalog offCatalog = offerRepository.findByOfferId(vou.getOfferInfo().getOffer());
			// giftingService.sendAllDetails(id,
			// voucherGiftDetails.get().getSenderInfo().getAccountNumber(),voucherGiftDetails.get().getSenderInfo().getFirstName(),voucherGiftDetails.get().getSenderInfo().getLastName(),senderEmailId,
			// voucherGiftDetails.get().getReceiverInfo().getFirstName(),voucherGiftDetails.get().getReceiverInfo().getLastName(),receiverEmailId,voucherGiftDetails.get().getReceiverInfo().getAccountNumber(),voucherGiftDetails.get().getScheduledDate(),
			// voucherGiftDetails.get().getPurchaseDetails(), vou.getExpiryDate(),
			// vou.getVoucherAmount(),
			// vou.getVoucherValues().getCost(),offCatalog.getBrandDescription().getBrandDescriptionEn(),image.getImage(),externalTransactionId);
		}

	}

	/**
	 * This method checks if gifting points or gold is under the limits for the
	 * provided sender and receiver account numbers
	 * 
	 * @param senderAccountNumber
	 * @param receiverAccountNumber
	 * @param senderMembershipCode
	 * @param receiverMembershipCode
	 * @param gift
	 * @param giftType
	 * @param resultResponse
	 * 
	 */
	public List<GiftingCounter> checkLimits(String senderAccountNumber, String receiverAccountNumber,
			String senderMembershipCode, String receiverMembershipCode, Double gift, String giftType,
			ResultResponse resultResponse) {

		CountCompare accountLimits;
		CountCompare membershipLimits;

		List<String> memberships = new ArrayList<>();
		memberships.add(senderMembershipCode);
		memberships.add(receiverMembershipCode);

		List<GiftingCounter> giftTypeCounts = giftingCounterRepository.findByGiftTypeAndMembershipCode(giftType,
				memberships);

		if (giftTypeCounts.isEmpty())
			return giftTypeCounts;

		GiftingLimit giftingLimit = giftingLimitRepository.findByGiftType(giftType);

		if (null == giftingLimit) {
			resultResponse.addErrorAPIResponse(GiftingCodes.NO_GIFTING_LIMITS_AVAILABLE.getIntId(),
					GiftingCodes.NO_GIFTING_LIMITS_AVAILABLE.getMsg() + giftType);
			return giftTypeCounts;
		}

		accountLimits = Utility.populateLimitsObject(giftingLimit.getAccountLimits());
		membershipLimits = Utility.populateLimitsObject(giftingLimit.getMembershipLimits());

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.GIFTING_COUNTER_RESPONSE,
				this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_CHECK_LIMITS, giftTypeCounts);

		validateLimits(giftTypeCounts, senderAccountNumber, receiverAccountNumber, senderMembershipCode,
				receiverMembershipCode, gift, accountLimits, membershipLimits, resultResponse);

		return giftTypeCounts;

	}

	/**
	 * This method is used to check if sender/receiver has reached their
	 * account/membership level limits for the day/week/month - gifting points/gold.
	 * 
	 * @param giftTypeCounts
	 * @param senderAccountNumber
	 * @param receiverAccountNumber
	 * @param senderMembershipCode
	 * @param receiverMembershipCode
	 * @param gift
	 * @param accountLimits
	 * @param membershipLimits
	 * @param resultResponse
	 * @return
	 */
	private ResultResponse validateLimits(List<GiftingCounter> giftTypeCounts, String senderAccountNumber,
			String receiverAccountNumber, String senderMembershipCode, String receiverMembershipCode, Double gift,
			CountCompare accountLimits, CountCompare membershipLimits, ResultResponse resultResponse) {

		CountCompare senderAccountCount;
		CountCompare receiverAccountCount;
		CountCompare senderMembershipCount;
		CountCompare receiverMembershipCount;

		for (GiftingCounter counter : giftTypeCounts) {

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_ACCOUNT)
					&& counter.getAccountNumber().equals(senderAccountNumber)
					&& counter.getMembershipCode().equals(senderMembershipCode)) {

				senderAccountCount = Utility.populateCounterObject(counter);
				Utility.checkSenderAccountLimits(senderAccountCount, accountLimits, gift, resultResponse);

			}

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_ACCOUNT)
					&& counter.getAccountNumber().equals(receiverAccountNumber)
					&& counter.getMembershipCode().equals(receiverMembershipCode)) {

				receiverAccountCount = Utility.populateCounterObject(counter);
				Utility.checkReceiverAccountLimits(receiverAccountCount, accountLimits, gift, resultResponse);

			}

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_MEMBERSHIP)
					&& counter.getMembershipCode().equals(senderMembershipCode)) {

				senderMembershipCount = Utility.populateCounterObject(counter);
				Utility.checkSenderMembershipLimits(senderMembershipCount, membershipLimits, gift, resultResponse);

			}

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_MEMBERSHIP)
					&& counter.getMembershipCode().equals(receiverMembershipCode)) {

				receiverMembershipCount = Utility.populateCounterObject(counter);
				Utility.checkReceiverMembershipLimits(receiverMembershipCount, membershipLimits, gift, resultResponse);

			}
		}
		return resultResponse;
	}

	/**
	 * This method is used to update gifting counter collection after gifting
	 * gold/points related transaction.
	 * 
	 * @param giftTypeCounts
	 * @param giftType
	 * @param senderAccountNumber
	 * @param senderMembershipCode
	 * @param receiverAccountNumber
	 * @param receiverMembershipCode
	 * @param gift
	 * @param headers
	 * @param resultResponse
	 * @return
	 * @throws MarketplaceException
	 */
	private ResultResponse updateGiftingCounter(List<GiftingCounter> giftTypeCounts, String giftType,
			String senderAccountNumber, String senderMembershipCode, String receiverAccountNumber,
			String receiverMembershipCode, Double gift, Headers headers, ResultResponse resultResponse)
			throws MarketplaceException {

		boolean senderAccount = false;
		boolean receiverAccount = false;
		boolean senderMembership = false;
		boolean receiverMembership = false;

		GiftingCounter senderAccountCounter = null;
		GiftingCounter receiverAccountCounter = null;
		GiftingCounter senderMembershipCounter = null;
		GiftingCounter receiverMembershipCounter = null;

		boolean sameMembership = false;
		List<GiftingCounterDomain> counterDomains = null;

		if (giftTypeCounts.isEmpty()) {

			counterDomains = createCountDomainObjects(senderAccount, receiverAccount, senderMembership,
					receiverMembership, giftType, senderAccountNumber, senderMembershipCode, receiverAccountNumber,
					receiverMembershipCode, gift, headers, resultResponse, sameMembership, senderAccountCounter,
					receiverAccountCounter, senderMembershipCounter, receiverMembershipCounter);

			giftingCounterDomain.saveUpdateGiftingCounter(counterDomains, headers, giftTypeCounts,
					GiftingConfigurationConstants.GIFT);

			return resultResponse;

		}

		for (GiftingCounter counter : giftTypeCounts) {

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_ACCOUNT)
					&& counter.getAccountNumber().equals(senderAccountNumber)
					&& counter.getMembershipCode().equals(senderMembershipCode)) {
				senderAccount = true;
				senderAccountCounter = counter;
			}

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_ACCOUNT)
					&& counter.getAccountNumber().equals(receiverAccountNumber)
					&& counter.getMembershipCode().equals(receiverMembershipCode)) {
				receiverAccount = true;
				receiverAccountCounter = counter;
			}

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_MEMBERSHIP)
					&& counter.getMembershipCode().equals(senderMembershipCode)) {
				senderMembership = true;
				senderMembershipCounter = counter;
			}

			if (counter.getLevel().equalsIgnoreCase(GiftingConstants.LEVEL_MEMBERSHIP)
					&& counter.getMembershipCode().equals(receiverMembershipCode)) {
				receiverMembership = true;
				receiverMembershipCounter = counter;
			}

		}

		counterDomains = createCountDomainObjects(senderAccount, receiverAccount, senderMembership, receiverMembership,
				giftType, senderAccountNumber, senderMembershipCode, receiverAccountNumber, receiverMembershipCode,
				gift, headers, resultResponse, sameMembership, senderAccountCounter, receiverAccountCounter,
				senderMembershipCounter, receiverMembershipCounter);

		giftingCounterDomain.saveUpdateGiftingCounter(counterDomains, headers, giftTypeCounts,
				GiftingConfigurationConstants.GIFT);

		return resultResponse;

	}

	private List<GiftingCounterDomain> createCountDomainObjects(boolean senderAccount, boolean receiverAccount,
			boolean senderMembership, boolean receiverMembership, String giftType, String senderAccountNumber,
			String senderMembershipCode, String receiverAccountNumber, String receiverMembershipCode, Double gift,
			Headers headers, ResultResponse resultResponse, boolean sameMembership, GiftingCounter senderAccountCounter,
			GiftingCounter receiverAccountCounter, GiftingCounter senderMembershipCounter,
			GiftingCounter receiverMembershipCounter) {

		List<GiftingCounterDomain> counterDomains = new ArrayList<>();

		GiftingCounterDomain senderAccountDomain = null;
		GiftingCounterDomain receiverAccountDomain = null;
		GiftingCounterDomain senderMembershipDomain = null;
		GiftingCounterDomain receiverMembershipDomain = null;

		if (!senderAccount) {
			senderAccountDomain = insertCounterDomain(giftType, GiftingConstants.LEVEL_ACCOUNT, senderAccountNumber,
					senderMembershipCode, gift, true, headers);
		} else {
			senderAccountDomain = updateCounterDomain(giftType, GiftingConstants.LEVEL_ACCOUNT, senderAccountNumber,
					senderMembershipCode, gift, true, senderAccountCounter, headers);
		}

		if (!receiverAccount) {
			receiverAccountDomain = insertCounterDomain(giftType, GiftingConstants.LEVEL_ACCOUNT, receiverAccountNumber,
					receiverMembershipCode, gift, false, headers);
		} else {
			receiverAccountDomain = updateCounterDomain(giftType, GiftingConstants.LEVEL_ACCOUNT, receiverAccountNumber,
					receiverMembershipCode, gift, false, receiverAccountCounter, headers);
		}

		if (!senderMembership && !senderMembershipCode.equals(receiverMembershipCode)) {
			senderMembershipDomain = insertCounterDomain(giftType, GiftingConstants.LEVEL_MEMBERSHIP, null,
					senderMembershipCode, gift, true, headers);
		} else if (!senderMembership && senderMembershipCode.equals(receiverMembershipCode)) {
			senderMembershipDomain = insertCounterDomainMembership(giftType, GiftingConstants.LEVEL_MEMBERSHIP, null,
					senderMembershipCode, gift, true, headers);
			sameMembership = true;
		} else if (senderMembership && senderMembershipCode.equals(receiverMembershipCode)) {
			senderMembershipDomain = updateCounterDomainMembership(giftType, GiftingConstants.LEVEL_MEMBERSHIP, null,
					senderMembershipCode, gift, senderMembershipCounter, headers);
			sameMembership = true;
		} else {
			senderMembershipDomain = updateCounterDomain(giftType, GiftingConstants.LEVEL_MEMBERSHIP, null,
					senderMembershipCode, gift, true, senderMembershipCounter, headers);
		}

		if (!receiverMembership && !sameMembership) {
			receiverMembershipDomain = insertCounterDomain(giftType, GiftingConstants.LEVEL_MEMBERSHIP, null,
					receiverMembershipCode, gift, false, headers);
			counterDomains.add(receiverMembershipDomain);
		} else if (receiverMembership && !sameMembership) {
			receiverMembershipDomain = updateCounterDomain(giftType, GiftingConstants.LEVEL_MEMBERSHIP, null,
					receiverMembershipCode, gift, false, receiverMembershipCounter, headers);
			counterDomains.add(receiverMembershipDomain);
		}

		counterDomains.add(senderAccountDomain);
		counterDomains.add(receiverAccountDomain);
		counterDomains.add(senderMembershipDomain);

		return counterDomains;

	}

	/**
	 * This method is used to insert into GiftingCounter collection - account level.
	 * 
	 * @param giftType
	 * @param level
	 * @param accountNumber
	 * @param membershipCode
	 * @param gift
	 * @param sender
	 * @param headers
	 * @return
	 */
	private GiftingCounterDomain insertCounterDomain(String giftType, String level, String accountNumber,
			String membershipCode, Double gift, boolean sender, Headers headers) {

		return new GiftingCounterDomain.GiftingCounterBuilder(giftType, level).programCode(headers.getProgram())
				.accountNumber(accountNumber).membershipCode(membershipCode).sentDayCount(sender ? gift : 0.0)
				.sentWeekCount(sender ? gift : 0.0).sentMonthCount(sender ? gift : 0.0)
				.receivedDayCount(sender ? 0.0 : gift).receivedWeekCount(sender ? 0.0 : gift)
				.receivedMonthCount(sender ? 0.0 : gift).lastResetDate(new Date()).createdUser(headers.getUserName())
				.createdDate(new Date()).build();

	}

	/**
	 * This method is used to insert into GiftingCounter collection - membership
	 * level.
	 * 
	 * @param giftType
	 * @param level
	 * @param accountNumber
	 * @param membershipCode
	 * @param gift
	 * @param sender
	 * @param headers
	 * @return
	 */
	private GiftingCounterDomain insertCounterDomainMembership(String giftType, String level, String accountNumber,
			String membershipCode, Double gift, boolean sender, Headers headers) {

		return new GiftingCounterDomain.GiftingCounterBuilder(giftType, level).programCode(headers.getProgram())
				.accountNumber(accountNumber).membershipCode(membershipCode).sentDayCount(gift).sentWeekCount(gift)
				.sentMonthCount(gift).receivedDayCount(gift).receivedWeekCount(gift).receivedMonthCount(gift)
				.lastResetDate(new Date()).createdUser(headers.getUserName()).createdDate(new Date()).build();

	}

	/**
	 * This method is used to update GiftingCounter collection - account level.
	 * 
	 * @param giftType
	 * @param level
	 * @param accountNumber
	 * @param membershipCode
	 * @param gift
	 * @param sender
	 * @param existingCounter
	 * @param headers
	 * @return
	 */
	private GiftingCounterDomain updateCounterDomain(String giftType, String level, String accountNumber,
			String membershipCode, Double gift, boolean sender, GiftingCounter existingCounter, Headers headers) {

		return new GiftingCounterDomain.GiftingCounterBuilder(giftType, level).id(existingCounter.getId())
				.programCode(null != headers.getProgram() ? headers.getProgram() : existingCounter.getProgramCode())
				.accountNumber(accountNumber).membershipCode(membershipCode)
				.sentDayCount(sender ? existingCounter.getSentDayCount() + gift : existingCounter.getSentDayCount())
				.sentWeekCount(sender ? existingCounter.getSentWeekCount() + gift : existingCounter.getSentWeekCount())
				.sentMonthCount(
						sender ? existingCounter.getSentMonthCount() + gift : existingCounter.getSentMonthCount())
				.receivedDayCount(
						sender ? existingCounter.getReceivedDayCount() : existingCounter.getReceivedDayCount() + gift)
				.receivedWeekCount(
						sender ? existingCounter.getReceivedWeekCount() : existingCounter.getReceivedWeekCount() + gift)
				.receivedMonthCount(sender ? existingCounter.getReceivedMonthCount()
						: existingCounter.getReceivedMonthCount() + gift)
				.lastResetDate(existingCounter.getLastResetDate()).createdUser(existingCounter.getCreatedUser())
				.createdDate(existingCounter.getCreatedDate()).updatedUser(headers.getUserName())
				.updatedDate(new Date()).build();

	}

	/**
	 * This method is used to update GiftingCounter collection - membership level.
	 * 
	 * @param giftType
	 * @param level
	 * @param accountNumber
	 * @param membershipCode
	 * @param gift
	 * @param existingCounter
	 * @param headers
	 * @return
	 */
	private GiftingCounterDomain updateCounterDomainMembership(String giftType, String level, String accountNumber,
			String membershipCode, Double gift, GiftingCounter existingCounter, Headers headers) {

		return new GiftingCounterDomain.GiftingCounterBuilder(giftType, level).id(existingCounter.getId())
				.programCode(null != headers.getProgram() ? headers.getProgram() : existingCounter.getProgramCode())
				.accountNumber(accountNumber).membershipCode(membershipCode)
				.sentDayCount(existingCounter.getSentDayCount() + gift)
				.sentWeekCount(existingCounter.getSentDayCount() + gift)
				.sentMonthCount(existingCounter.getSentDayCount() + gift)
				.receivedDayCount(existingCounter.getReceivedDayCount() + gift)
				.receivedWeekCount(existingCounter.getReceivedWeekCount() + gift)
				.receivedMonthCount(existingCounter.getReceivedMonthCount() + gift)
				.lastResetDate(existingCounter.getLastResetDate()).createdUser(existingCounter.getCreatedUser())
				.createdDate(existingCounter.getCreatedDate()).updatedUser(headers.getUserName())
				.updatedDate(new Date()).build();

	}

	/**
	 * This method is called by API to reset GiftingCounter collection
	 * daily/weekly/monnthly. API configured in GiftingController - used when offers
	 * cron API fails to execute.
	 * 
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse resetGiftingCountersCron(Headers headers, ResultResponse resultResponse) {

		Calendar currentCalendar = Calendar.getInstance();
		List<GiftingCounter> countersToReset = new ArrayList<>();
		List<GiftingCounter> allCounters = giftingCounterRepository.findAll();

		if (allCounters.isEmpty()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.GIFTING_RESET_NO_COUNTERS_AVAILABLE.getIntId(),
					GiftingCodes.GIFTING_RESET_NO_COUNTERS_AVAILABLE.getMsg());
			resultResponse.setResult(GiftingCodes.GIFTING_COUNTER_RESET_FAILED.getId(),
					GiftingCodes.GIFTING_COUNTER_RESET_FAILED.getMsg());
			return resultResponse;
		}

		for (GiftingCounter counter : allCounters) {

			if (!Checks.checkIsDateInCurrentDay(counter.getLastResetDate(), currentCalendar)) {
				counter.setSentDayCount(0.0);
				counter.setReceivedDayCount(0.0);
			}

			if (!Checks.checkIsDateInCurrentWeek(counter.getLastResetDate(), currentCalendar)) {
				counter.setSentWeekCount(0.0);
				counter.setReceivedWeekCount(0.0);
			}

			if (!Checks.checkIsDateInCurrentMonth(counter.getLastResetDate(), currentCalendar)) {
				counter.setSentMonthCount(0.0);
				counter.setReceivedMonthCount(0.0);
			}

			counter.setLastResetDate(new Date());

			counter.setUpdatedUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken());
			counter.setUpdatedDate(new Date());

			countersToReset.add(counter);

		}

		List<GiftingCounter> resetGiftingCounters = giftingCounterRepository.saveAll(countersToReset);

		auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_COUNTER, resetGiftingCounters,
				OffersRequestMappingConstants.RESET_ALL_COUNTER, allCounters, headers.getExternalTransactionId(),
				headers.getUserName());

		resultResponse.setResult(GiftingCodes.GIFTING_COUNTER_RESET_SUCCESS.getId(),
				GiftingCodes.GIFTING_COUNTER_RESET_SUCCESS.getMsg());

		return resultResponse;

	}

	/**
	 * This method is used to reset GiftingCounters by cron API. Cron API configured
	 * under "Offers" - the same API used to reset offer counters.
	 * 
	 * @param headers
	 */
	 	public void resetGiftingCounters(Headers headers, ResultResponse resultResponse) {
		
		try {
			Calendar currentCalendar = Calendar.getInstance();
			List<GiftingCounter> countersToReset = new ArrayList<>();
			List<String> giftTypes = new ArrayList<>();
			giftTypes.add(GiftingConstants.GIFT_TYPE_POINTS);
			giftTypes.add(GiftingConstants.GIFT_TYPE_GOLD);
			List<GiftingCounter> allCounters = giftingCounterRepository.findByGiftTypeIn(giftTypes);
	
			if (!allCounters.isEmpty()) {
	
				for (GiftingCounter counter : allCounters) {
	
					if (!Checks.checkIsDateInCurrentDay(counter.getLastResetDate(), currentCalendar)) {
						counter.setSentDayCount(0.0);
						counter.setReceivedDayCount(0.0);
					}
	
					if (!Checks.checkIsDateInCurrentWeek(counter.getLastResetDate(), currentCalendar)) {
						counter.setSentWeekCount(0.0);
						counter.setReceivedWeekCount(0.0);
					}
	
					if (!Checks.checkIsDateInCurrentMonth(counter.getLastResetDate(), currentCalendar)) {
						counter.setSentMonthCount(0.0);
						counter.setReceivedMonthCount(0.0);
					}
	
					counter.setLastResetDate(new Date());
	
					counter.setUpdatedUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken());
					counter.setUpdatedDate(new Date());
	
					countersToReset.add(counter);
	
				}

				List<GiftingCounter> resetGiftingCounters = giftingCounterRepository.saveAll(countersToReset);
				
				auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_COUNTER, resetGiftingCounters,
						OffersRequestMappingConstants.RESET_ALL_COUNTER, allCounters, headers.getExternalTransactionId(),
						headers.getUserName());
				
				resultResponse.setResult(OfferSuccessCodes.RESET_ALL_COUNTER_SUCCESS.getId(),
						OfferSuccessCodes.RESET_ALL_COUNTER_SUCCESS.getMsg());
			}

			List<GiftingCounter> resetGiftingCounters = giftingCounterRepository.saveAll(countersToReset);

			auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_COUNTER, resetGiftingCounters,
					OffersRequestMappingConstants.RESET_ALL_COUNTER, allCounters, headers.getExternalTransactionId(),
					headers.getUserName());

	
		} catch(Exception e) {
			resultResponse.addErrorAPIResponse(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(OfferErrorCodes.RESET_ALL_COUNTER_FAILURE.getId(),
					OfferErrorCodes.RESET_ALL_COUNTER_FAILURE.getMsg());
		}

	}

	/**
	 * This method is used to validate if sender account is present and active.
	 * 
	 * @param senderDetails
	 * @param resultResponse
	 * @param giftType
	 * @return
	 */
	private boolean senderAccountValidation(GetMemberResponse senderDetails, ResultResponse resultResponse,
			String giftType) {
		if (senderDetails == null) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_ACCOUNT_NOT_PRESENT.getIntId(),
					GiftingCodes.SENDER_ACCOUNT_NOT_PRESENT.getMsg());
			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER))
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
			else if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS))
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			else
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			return false;
		}

		else if (!senderDetails.getAccountStatus().equalsIgnoreCase(GiftingConstants.ACCOUNT_STATUS_ACTIVE)
				&& !senderDetails.getAccountStatus().equalsIgnoreCase(GiftingConstants.ACCOUNT_STATUS_ACT)) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_ACCOUNT_NOT_ACTIVE.getIntId(),
					GiftingCodes.SENDER_ACCOUNT_NOT_ACTIVE.getMsg());
			if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER))
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
			else if (giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS))
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			else
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			return false;
		}
		return true;
	}

	/**
	 * This method is used to enroll the receiver member is it is not currently
	 * present.
	 * 
	 * @param giftingDto
	 * @param resultResponse
	 * @param header
	 * @return
	 * @throws MarketplaceException
	 */
	private GetMemberResponse enrollReceiverValidation(GiftingRequest giftingDto, ResultResponse resultResponse,
			Headers header) throws MarketplaceException {

		EnrollmentResultResponse enrollmentResultResponse;
		enrollmentResultResponse = enrollMember(giftingDto.getReceiverAccountNumber(), header);
		if (enrollmentResultResponse.getEnrollmentResult().getMessage()
				.contains(GiftingConstants.MEMBER_ENROLL_FAILED_STATUS)) {
			resultResponse.addErrorAPIResponse(GiftingCodes.MEMBER_ENROLL_FAILED.getIntId(),
					GiftingCodes.MEMBER_ENROLL_FAILED.getMsg()
							+ enrollmentResultResponse.getEnrollmentResult().getMessage());
			if (giftingDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER))
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
			else if (giftingDto.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS))
				resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
						GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			else
				resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
						GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			return null;
		}
		
		GetMemberResponseDto receiverMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
				giftingDto.getReceiverAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(), header,
				resultResponse);
		
		GetMemberResponse receiverAccountDetails = ProcessValues.getMemberInfo(receiverMemberDetailsDto,
				giftingDto.getReceiverAccountNumber());

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS_AFTER_ENROLL,
				this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_ENROLL_RECEIVER, receiverAccountDetails);
		
		if (!Utility.checkMemberExists(receiverAccountDetails, giftingDto.getReceiverAccountNumber(), resultResponse)) {
			GetMemberResponse receiverDetails = new GetMemberResponse();
			receiverDetails.setEmail(giftingDto.getReceiverEmail());
			receiverDetails.setFirstName(giftingDto.getReceiverFirstName());
			receiverDetails.setLastName(giftingDto.getReceiverLastName());
			receiverDetails.setMembershipCode(enrollmentResultResponse.getEnrollmentResult().getMembershipCode());
			receiverDetails.setAccountNumber(enrollmentResultResponse.getEnrollmentResult().getAccountNumber());
			receiverDetails.setTierLevelName(enrollmentResultResponse.getEnrollmentResult().getTierLevelName());
			receiverDetails.setCustomerType(enrollmentResultResponse.getEnrollmentResult().getCustomerType());
			return receiverDetails;
		}

		receiverAccountDetails.setEmail(giftingDto.getReceiverEmail());
		receiverAccountDetails.setFirstName(giftingDto.getReceiverFirstName());
		receiverAccountDetails.setLastName(giftingDto.getReceiverLastName());
		receiverAccountDetails.setMembershipCode(enrollmentResultResponse.getEnrollmentResult().getMembershipCode());
		receiverAccountDetails.setAccountNumber(enrollmentResultResponse.getEnrollmentResult().getAccountNumber());
		receiverAccountDetails.setTierLevelName(enrollmentResultResponse.getEnrollmentResult().getTierLevelName());
		receiverAccountDetails.setCustomerType(enrollmentResultResponse.getEnrollmentResult().getCustomerType());

		return receiverAccountDetails;

	}

	private GiftingHistory goldGiftingCalculationAndSave(GoldGiftRequest goldGiftRequest,
			GetMemberResponse senderAccountDetails, GetMemberResponse receiverAccountDetails,
			GoldCertificate senderGoldCertificateDetails, Headers headers, GiftingRequest giftingGoldDto,
			MarketplaceImage image, PaymentResponse paymentResponse, ResultResponse resultResponse, GetMemberResponseDto senderMemberDetailsDto)
			throws MarketplaceException {
		Double balanceGoldToBeGifted = goldGiftRequest.getGiftGold();
		List<GoldCertificateDetailsDomain> goldGiftedSender = new ArrayList<>();
		List<GoldCertificateDetailsDomain> goldGiftedReceiver = new ArrayList<>();
		List<GoldGiftedDetails> goldGiftedSenderDetails = new ArrayList<>();

		GoldCertificate receiverGoldCertificateDetails = goldCertificateRepository.findByAccountNumberAndMembershipCode(
				receiverAccountDetails.getAccountNumber(), receiverAccountDetails.getMembershipCode());

		Double totalGoldBalance;
		Double totalSpentAmount;
		Integer totalPointsAmount = 0;

		for (GoldCertificateTransaction g : senderGoldCertificateDetails.getCertificateDetails()) {
			if ((!g.getTransactionType().equalsIgnoreCase(GiftingConstants.TRANSAC_TYPE_GIFTED))
					&& (g.getCurrentGoldBalance() <= balanceGoldToBeGifted)) {
				balanceGoldToBeGifted = balanceGoldToBeGifted - g.getCurrentGoldBalance();
				
				goldGiftedReceiver.add(new GoldCertificateDetailsDomain(g.getCertificateId(), null,
						GiftingConstants.TRANSAC_TYPE_GIFT, g.getPartnerCode(), g.getMerchantCode(),
						new NameDomain(g.getMerchantName().getEnglish(), g.getMerchantName().getArabic()),
						g.getCurrentGoldBalance(), g.getCurrentGoldBalance(), g.getSpentAmount(), g.getPointAmount(),
						goldGiftRequest.getScheduledDate()));

				g.setTransactionType(GiftingConstants.TRANSAC_TYPE_GIFTED);
				g.setCurrentGoldBalance(0.0);
				g.setPointAmount(0);
				g.setSpentAmount(0.0);
				goldGiftedSenderDetails.add(new GoldGiftedDetails(g.getCertificateId(), g.getCurrentGoldBalance()));

			} else if ((!g.getTransactionType().equalsIgnoreCase(GiftingConstants.TRANSAC_TYPE_GIFTED))
					&& (g.getCurrentGoldBalance() > balanceGoldToBeGifted)) {
				Double currentBal = g.getCurrentGoldBalance() - balanceGoldToBeGifted;
				AmountPoints goldToAmtPnts = helper.getGoldCertificateAmountOrPoints(balanceGoldToBeGifted,
						headers.getChannelId(), resultResponse);
				Double receiverAmt = goldToAmtPnts.getAmount();
				Integer receiverPoints = goldToAmtPnts.getPoints();

				goldGiftedReceiver.add(new GoldCertificateDetailsDomain(g.getCertificateId(), null,
						GiftingConstants.TRANSAC_TYPE_GIFT, g.getPartnerCode(), g.getMerchantCode(),
						new NameDomain(g.getMerchantName().getEnglish(), g.getMerchantName().getArabic()),
						balanceGoldToBeGifted, balanceGoldToBeGifted, receiverAmt, receiverPoints,
						goldGiftRequest.getScheduledDate()));

				g.setCurrentGoldBalance(currentBal);
				g.setSpentAmount(g.getSpentAmount() - receiverAmt);
				g.setPointAmount(g.getPointAmount() - receiverPoints);
				goldGiftedSenderDetails.add(new GoldGiftedDetails(g.getCertificateId(), balanceGoldToBeGifted));

				balanceGoldToBeGifted = (double) 0;
			}
			if (balanceGoldToBeGifted == 0) {
				break;
			}
		}

		goldGiftedSender = prepareDataForGoldSenderReceiverPersist(goldGiftedSender, senderGoldCertificateDetails);
		totalGoldBalance = goldGiftedSender.stream().mapToDouble(GoldCertificateDetailsDomain::getCurrentGoldBalance)
				.sum();
		totalSpentAmount = goldGiftedSender.stream().mapToDouble(GoldCertificateDetailsDomain::getSpentAmount).sum();
		totalPointsAmount = goldGiftedSender.stream().mapToInt(GoldCertificateDetailsDomain::getPointAmount).sum();

		GiftingHistory savedGift = saveGiftingHistory(giftingGoldDto, senderAccountDetails, receiverAccountDetails,
				image, headers, null, 0, goldGiftedSenderDetails, paymentResponse, null);

		PaymentResponse payment = giftingPayment(giftingGoldDto,
				MarketplaceConstants.FULLCREDITCARD.getConstant(), senderMemberDetailsDto, senderAccountDetails,
				headers, savedGift.getId(), resultResponse);
		LOG.info("PaymentResponse : {}", payment);
		if (null == payment) {
			giftingHistoryRepository.delete(savedGift);
			resultResponse.setResult(GiftingCodes.GIFTING_GOLD_FAILURE.getId(),
					GiftingCodes.GIFTING_GOLD_FAILURE.getMsg());
			return null;
		}
		
		//saveGiftingHistoryAfterPayment(savedGift.getId(), payment, null != headers.getUserName() ? headers.getUserName() : headers.getToken()) ;
			
		
		saveGoldCertificate(senderGoldCertificateDetails, totalGoldBalance, totalSpentAmount, totalPointsAmount,
				goldGiftedSender, headers, DB_OPERATION_UPDATE);
		
		List<GoldCertificateDetailsDomain> goldGiftedReceiverPersist = goldGiftedReceiver.stream()
				.map(rec -> new GoldCertificateDetailsDomain(rec.getCertificateId(), savedGift.getId(),
						rec.getTransactionType(), rec.getPartnerCode(), rec.getMerchantCode(), rec.getMerchantName(),
						rec.getOriginalGoldBalance(), rec.getCurrentGoldBalance(), rec.getSpentAmount(),
						rec.getPointAmount(), rec.getStartDate()))
				.collect(Collectors.toList());

		if (receiverGoldCertificateDetails != null) {
			goldGiftedReceiverPersist = prepareDataForGoldSenderReceiverPersist(goldGiftedReceiverPersist,
					receiverGoldCertificateDetails);
			totalGoldBalance = goldGiftedReceiverPersist.stream()
					.mapToDouble(GoldCertificateDetailsDomain::getCurrentGoldBalance).sum();
			totalSpentAmount = goldGiftedReceiverPersist.stream()
					.mapToDouble(GoldCertificateDetailsDomain::getSpentAmount).sum();
			totalPointsAmount = goldGiftedReceiverPersist.stream()
					.mapToInt(GoldCertificateDetailsDomain::getPointAmount).sum();
			saveGoldCertificate(receiverGoldCertificateDetails, totalGoldBalance, totalSpentAmount, totalPointsAmount,
					goldGiftedReceiverPersist, headers, DB_OPERATION_UPDATE);

		} else {
			totalGoldBalance = goldGiftedReceiverPersist.stream()
					.mapToDouble(GoldCertificateDetailsDomain::getCurrentGoldBalance).sum();
			totalSpentAmount = goldGiftedReceiverPersist.stream()
					.mapToDouble(GoldCertificateDetailsDomain::getSpentAmount).sum();
			totalPointsAmount = goldGiftedReceiverPersist.stream()
					.mapToInt(GoldCertificateDetailsDomain::getPointAmount).sum();

			receiverGoldCertificateDetails = new GoldCertificate();
			receiverGoldCertificateDetails.setAccountNumber(receiverAccountDetails.getAccountNumber());
			receiverGoldCertificateDetails.setMembershipCode(receiverAccountDetails.getMembershipCode());
			saveGoldCertificate(receiverGoldCertificateDetails, totalGoldBalance, totalSpentAmount, totalPointsAmount,
					goldGiftedReceiverPersist, headers, DB_OPERATION_INSERT);
		}
		return savedGift;
	}

	private List<GoldCertificateDetailsDomain> prepareDataForGoldSenderReceiverPersist(
			List<GoldCertificateDetailsDomain> goldGiftedDetails, GoldCertificate goldCertificateDetails) {
		goldGiftedDetails.addAll(goldCertificateDetails.getCertificateDetails().stream()
				.map(gc -> new GoldCertificateDetailsDomain(gc.getCertificateId(), gc.getTransactionId(),
						gc.getTransactionType(), gc.getPartnerCode(), gc.getMerchantCode(),
						new NameDomain(gc.getMerchantName().getEnglish(), gc.getMerchantName().getArabic()),
						gc.getOriginalGoldBalance(), gc.getCurrentGoldBalance(), gc.getSpentAmount(),
						gc.getPointAmount(), gc.getStartDate()))
				.collect(Collectors.toList()));
		return goldGiftedDetails;
	}

	private void saveGoldCertificate(GoldCertificate goldCertificateDetails, Double totalGoldBalance,
			Double totalSpentAmount, Integer totalPointsAmount, List<GoldCertificateDetailsDomain> goldGifted,
			Headers headers, String action) throws MarketplaceException {
		GoldCertificateDomain dom;
		if (action.equalsIgnoreCase(DB_OPERATION_UPDATE)) {
			dom = new GoldCertificateDomain.GoldCertificateBuilder(goldCertificateDetails.getAccountNumber(),
					goldCertificateDetails.getMembershipCode()).id(goldCertificateDetails.getId())
							.programCode(headers.getProgram()).totalGoldBalance(totalGoldBalance)
							.totalSpentAmount(totalSpentAmount).totalPointAmount(totalPointsAmount)
							.certificateDetails(goldGifted).updatedUser(headers.getUserName()).updatedDate(new Date())
							.build();
		} else {
			dom = new GoldCertificateDomain.GoldCertificateBuilder(goldCertificateDetails.getAccountNumber(),
					goldCertificateDetails.getMembershipCode()).programCode(headers.getProgram())
							.totalGoldBalance(totalGoldBalance).totalSpentAmount(totalSpentAmount)
							.totalPointAmount(totalPointsAmount).certificateDetails(goldGifted)
							.createdUser(headers.getUserName()).createdDate(new Date()).build();
		}
		goldCertificateDomain.saveUpdateGoldCertificate(dom, action, headers, goldCertificateDetails,
				GiftingConfigurationConstants.GIFT);
	}

	/**
	 * This method is used to send the points gifting that is scheduled for a future
	 * date. Cron API configured in GiftingController - sendScheduledGift method.
	 * 
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse sendScheduledGift(Headers headers, ResultResponse resultResponse) {

		try {

			List<GiftingHistory> giftsToSend = giftingHistoryRepository
					.findByGiftType(GiftingConstants.GIFT_TYPE_POINTS);

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_ENTITY,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT, giftsToSend);

			DateFormat dateFormat = new SimpleDateFormat(GiftingConstants.STRING_DATE_FORMAT);
			String currentDate = dateFormat.format(new Date());

			if (giftsToSend.isEmpty()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.NO_POINTS_GIFT_SCHEDULED.getIntId(),
						GiftingCodes.NO_POINTS_GIFT_SCHEDULED.getMsg());
				resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getId(),
						GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getMsg());
				return resultResponse;
			}

			int actualCount = 0;
			for (GiftingHistory giftingHistory : giftsToSend) {

				if (Utility.isTodayDate(giftingHistory.getScheduledDate())
						&& null == giftingHistory.getPointsGifted().getReceiverTransactionRefId()) {

					GetMemberResponseDto receiverMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
							giftingHistory.getReceiverInfo().getAccountNumber(),
							ProcessValues.getIncludeMemberDetailsForPayment(), headers, resultResponse);

					GetMemberResponse receiverAccountDetails = ProcessValues.getMemberInfo(receiverMemberDetailsDto,
							giftingHistory.getReceiverInfo().getAccountNumber());

					LOG.info(
							GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
									+ GiftingConstants.RETRIEVED_MEMBER_DETAILS,
							this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT,
							receiverMemberDetailsDto);

					MemberActivityPaymentDto memberActivityDto = new MemberActivityPaymentDto();
					memberActivityDto.setAccountNumber(giftingHistory.getReceiverInfo().getAccountNumber());
					memberActivityDto.setMembershipCode(giftingHistory.getReceiverInfo().getMembershipCode());
					memberActivityDto.setPartnerCode(GiftingConstants.POINT_CONVERSION_PARTNER_CODE);
					memberActivityDto.setActivityCode(GiftingConstants.MA_ACTIVITY_CODE_ACCRUAL);
					memberActivityDto.setEventDate(currentDate);
					memberActivityDto.setSpendValue(giftingHistory.getPurchaseDetails().getSpentAmount());
					memberActivityDto.setExternalReferenceNumber(giftingHistory.getExtRefNo());
					memberActivityDto.setPointsValue(giftingHistory.getPointsGifted().getPointsGifted());
					memberActivityDto.setMemberResponse(receiverMemberDetailsDto);

					LOG.info(
							GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
									+ GiftingConstants.MEMBER_ACTIVITY_REQUEST,
							this.getClass().getName(),
							GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT, memberActivityDto);

					MemberActivityResponse memberActivityResponse = giftingService
							.memberAccrualRedemption(memberActivityDto, headers, resultResponse);

					LOG.info(
							GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME
									+ GiftingConstants.MEMBER_ACTIVITY_RESPONSE,
							this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT,
							memberActivityResponse);

					if (!resultResponse.getApiStatus().getErrors().isEmpty()) {
						resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_MEMBER_ACTIVTY_ERROR.getIntId(),
								GiftingCodes.SCHEDULED_MEMBER_ACTIVTY_ERROR.getMsg() + giftingHistory.getId());
						continue;
					}

					giftingHistory.getPointsGifted()
							.setReceiverTransactionRefId(memberActivityResponse.getTransactionRefId());
					giftingHistory
							.setUpdatedUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken());
					giftingHistory.setUpdatedDate(new Date());

					GiftingHistory savedGiftingHistory = giftingHistoryRepository.save(giftingHistory);
					LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
							this.getClass().getName(),
							GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT, savedGiftingHistory);

					auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_HISTORY, savedGiftingHistory,
							GiftingConfigurationConstants.SEND_GIFT, giftingHistory, headers.getExternalTransactionId(),
							headers.getUserName());

					giftingService.sendSMS(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, giftingHistory,
							GiftingConstants.GIFT_TYPE_POINTS, giftingHistory.getPointsGifted().getPointsGifted(), null,
							null, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_SMS), headers);
					giftingService.sendEmail(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, giftingHistory,
							GiftingConstants.GIFT_TYPE_POINTS, giftingHistory.getPointsGifted().getPointsGifted(), null,
							null, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL), headers);

					actualCount++;

				}

			}

			if (actualCount == 0) {
				resultResponse.addErrorAPIResponse(GiftingCodes.NO_POINTS_GIFT_SCHEDULED.getIntId(),
						GiftingCodes.NO_POINTS_GIFT_SCHEDULED.getMsg());
				resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getId(),
						GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getMsg());
			} else {
				resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_SUCCESS.getId(),
						GiftingCodes.SCHEDULED_POINTS_GIFT_SUCCESS.getMsg());
			}

		} catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getId(),
					GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		}

		return resultResponse;

	}
	
	/**
	 * This is a method called from the adhoc API - when the send scheduled batch API fails.
	 * This method is used to send points gift to receiver account for 1 transaction at a time.
	 * @param id
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse sendScheduledGiftAdhoc(String id, Headers headers, ResultResponse resultResponse) {

		try {

			Optional<GiftingHistory> giftToSend = giftingHistoryRepository.findById(id);

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_ENTITY,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT_ADHOC, giftToSend);

			DateFormat dateFormat = new SimpleDateFormat(GiftingConstants.STRING_DATE_FORMAT);
			String currentDate = dateFormat.format(new Date());

			if (!giftToSend.isPresent()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.NO_GIFT_HISTORY_FOR_ID.getIntId(),
						GiftingCodes.NO_GIFT_HISTORY_FOR_ID.getMsg() + id);
				resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getId(),
						GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getMsg());
				return resultResponse;
			}

			GiftingHistory scheduledGiftingHistory = giftToSend.get();
			
			if (null != scheduledGiftingHistory.getPointsGifted().getReceiverTransactionRefId()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.POINTS_GIFT_ALREADY_SENT.getIntId(),
						GiftingCodes.POINTS_GIFT_ALREADY_SENT.getMsg()
								+ scheduledGiftingHistory.getPointsGifted().getReceiverTransactionRefId());
				resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getId(),
						GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getMsg());
				return resultResponse;
			}

			GetMemberResponseDto receiverMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
					scheduledGiftingHistory.getReceiverInfo().getAccountNumber(),
					ProcessValues.getIncludeMemberDetailsForPayment(), headers, resultResponse);

			GetMemberResponse receiverAccountDetails = ProcessValues.getMemberInfo(receiverMemberDetailsDto,
					scheduledGiftingHistory.getReceiverInfo().getAccountNumber());

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT_ADHOC, receiverMemberDetailsDto);

			MemberActivityPaymentDto memberActivityDto = new MemberActivityPaymentDto();
			memberActivityDto.setAccountNumber(scheduledGiftingHistory.getReceiverInfo().getAccountNumber());
			memberActivityDto.setMembershipCode(scheduledGiftingHistory.getReceiverInfo().getMembershipCode());
			memberActivityDto.setPartnerCode(GiftingConstants.POINT_CONVERSION_PARTNER_CODE);
			memberActivityDto.setActivityCode(GiftingConstants.MA_ACTIVITY_CODE_ACCRUAL);
			memberActivityDto.setEventDate(currentDate);
			memberActivityDto.setSpendValue(scheduledGiftingHistory.getPurchaseDetails().getSpentAmount());
			memberActivityDto.setExternalReferenceNumber(scheduledGiftingHistory.getExtRefNo());
			memberActivityDto.setPointsValue(scheduledGiftingHistory.getPointsGifted().getPointsGifted());
			memberActivityDto.setMemberResponse(receiverMemberDetailsDto);

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME	+ GiftingConstants.MEMBER_ACTIVITY_REQUEST,
					this.getClass().getName(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT_ADHOC, memberActivityDto);

			MemberActivityResponse memberActivityResponse = giftingService
					.memberAccrualRedemption(memberActivityDto, headers, resultResponse);

			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME	+ GiftingConstants.MEMBER_ACTIVITY_RESPONSE,
					this.getClass(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT_ADHOC, memberActivityResponse);

			if (!resultResponse.getApiStatus().getErrors().isEmpty()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.SCHEDULED_MEMBER_ACTIVTY_ERROR.getIntId(),
						GiftingCodes.SCHEDULED_MEMBER_ACTIVTY_ERROR.getMsg() + scheduledGiftingHistory.getId());
				resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getId(),
						GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getMsg());
				return resultResponse;
			}

			scheduledGiftingHistory.getPointsGifted().setReceiverTransactionRefId(memberActivityResponse.getTransactionRefId());
			scheduledGiftingHistory.setUpdatedUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken());
			scheduledGiftingHistory.setUpdatedDate(new Date());

			GiftingHistory savedGiftingHistory = giftingHistoryRepository.save(scheduledGiftingHistory);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
					this.getClass().getName(), GiftingConfigurationConstants.CONTROLLER_HELPER_SEND_SCHEDULED_GIFT_ADHOC, savedGiftingHistory);

			auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_HISTORY, savedGiftingHistory,
					GiftingConfigurationConstants.SEND_GIFT, scheduledGiftingHistory, headers.getExternalTransactionId(),
					headers.getUserName());

			giftingService.sendSMS(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, scheduledGiftingHistory,
					GiftingConstants.GIFT_TYPE_POINTS, scheduledGiftingHistory.getPointsGifted().getPointsGifted(), null,
					null, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_SMS), headers);
			giftingService.sendEmail(createNotificationHelperDto(GiftingConstants.TYPE_RECEIVER, scheduledGiftingHistory,
					GiftingConstants.GIFT_TYPE_POINTS, scheduledGiftingHistory.getPointsGifted().getPointsGifted(), null,
					null, receiverAccountDetails, GiftingConstants.NOTIFICATION_TYPE_EMAIL), headers);

			resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_SUCCESS.getId(),
					GiftingCodes.SCHEDULED_POINTS_GIFT_SUCCESS.getMsg());

		} catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getId(),
					GiftingCodes.SCHEDULED_POINTS_GIFT_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		}

		return resultResponse;

	}

	/**
	 * This method is used to list a specific gift transaction history.
	 * 
	 * @param id
	 * @param headers
	 * @param listSpecificGiftingHistoryResponse
	 * @return
	 */
	public ListSpecificGiftingHistoryResponse listSpecificGiftingHistory(String id, Headers headers,
			ListSpecificGiftingHistoryResponse listSpecificGiftingHistoryResponse) {

		try {

			if (null == id) {
				listSpecificGiftingHistoryResponse.addErrorAPIResponse(
						GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_ID_MANDATORY_FIELD.getIntId(),
						GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_ID_MANDATORY_FIELD.getMsg());
				listSpecificGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_FAILURE.getId(),
						GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_FAILURE.getMsg());
				return listSpecificGiftingHistoryResponse;
			}

			Optional<GiftingHistory> giftHistory = giftingHistoryRepository.findById(id);

			if (!giftHistory.isPresent()) {
				listSpecificGiftingHistoryResponse.addErrorAPIResponse(
						GiftingCodes.GIFTING_SPECIFIC_HISTORY_NOT_AVAILABLE.getIntId(),
						GiftingCodes.GIFTING_SPECIFIC_HISTORY_NOT_AVAILABLE.getMsg() + id);
				listSpecificGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_FAILURE.getId(),
						GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_FAILURE.getMsg());
				return listSpecificGiftingHistoryResponse;
			}

			GiftingHistory existingGiftingHistory = giftHistory.get();

			if (modelMapper.getTypeMap(GiftingHistory.class, ListSpecificGiftingHistoryResult.class) == null) {
				modelMapper.addMappings(Utility.giftHistoryFieldMapping);
			}

			ListSpecificGiftingHistoryResult giftingHistoryResult = null;
			String offerDescription = null;
			
			giftingHistoryResult = modelMapper.map(existingGiftingHistory, ListSpecificGiftingHistoryResult.class);
			giftingHistoryResult = setListGiftingHistoryAttributes(existingGiftingHistory, giftingHistoryResult);
			listSpecificGiftingHistoryResponse.setGiftHistoryResult(giftingHistoryResult);

			if (null != existingGiftingHistory.getReceiverConsumption() && existingGiftingHistory
					.getReceiverConsumption().equalsIgnoreCase(GiftingConstants.RECEIVER_CONSUMPTION_NO)) {

				GiftingHistory giftingHistoryToUpdate = giftHistory.get();

				GetMemberResponseDto senderMemberDetailsDto = fetchServiceValues.getMemberDetailsForPayment(
						existingGiftingHistory.getSenderInfo().getAccountNumber(),
						ProcessValues.getIncludeMemberDetailsForPayment(), headers, listSpecificGiftingHistoryResponse);
				GetMemberResponse senderAccountDetails = ProcessValues.getMemberInfo(senderMemberDetailsDto,
						existingGiftingHistory.getSenderInfo().getAccountNumber());

				giftingHistoryToUpdate.setReceiverConsumption(GiftingConstants.RECEIVER_CONSUMPTION_YES);
				giftingHistoryToUpdate
						.setUpdatedUser(null != headers.getUserName() ? headers.getUserName() : headers.getToken());
				giftingHistoryToUpdate.setUpdatedDate(new Date());

				GiftingHistory savedGiftingHistory = giftingHistoryRepository.save(giftingHistoryToUpdate);
				LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
						this.getClass().getName(),
						GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_LIST_SPECIFIC_HISTORY,
						savedGiftingHistory);

				String lang = configureLanguage(senderAccountDetails);
				
				offerDescription = getOfferDescription(existingGiftingHistory, senderAccountDetails);
				
				if(!nonEligibleNotificationChannelIds.contains(headers.getChannelId())) {
					LOG.info("ChannelId: {}",headers.getChannelId());
					giftingService.pushNotificationViewedGift(existingGiftingHistory.getSenderInfo().getAccountNumber(),
							existingGiftingHistory.getSenderInfo().getMembershipCode(),
							existingGiftingHistory.getSenderInfo().getFirstName(),
							existingGiftingHistory.getReceiverInfo().getFirstName(),
							lang,
							headers.getExternalTransactionId());
					giftingService.emailViewedGift(existingGiftingHistory, offerDescription, lang, headers);
					
					listSpecificGiftingHistoryResponse.setResult(
							GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_NOTIFICATION_SENT_SUCCESS.getId(),
							GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_NOTIFICATION_SENT_SUCCESS.getMsg());

				}
	
				auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_HISTORY, savedGiftingHistory,
						GiftingConfigurationConstants.LIST_GIFT_HISTORY, existingGiftingHistory,
						headers.getExternalTransactionId(), headers.getUserName());
				
				if(nonEligibleNotificationChannelIds.contains(headers.getChannelId())) {
					listSpecificGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_SUCCESS.getId(),
							GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_SUCCESS.getMsg());
				}

				
			} else {

				listSpecificGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_SUCCESS.getId(),
						GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_SUCCESS.getMsg());

			}

		} catch (Exception e) {
			listSpecificGiftingHistoryResponse.addErrorAPIResponse(GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			listSpecificGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_FAILURE.getId(),
					GiftingCodes.GIFTING_SPECIFIC_HISTORY_LIST_FAILURE.getMsg());
			LOG.info(new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.CONTROLLER_HELPER_GIFT_LIST_SPECIFIC_HISTORY,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		return listSpecificGiftingHistoryResponse;

	}

	/**
	 * This method is used to set voucher/points/gold specific attributes in GET API
	 * response.
	 * 
	 * @param existingGiftingHistory
	 * @param giftingHistoryResult
	 * @return
	 */
	private ListSpecificGiftingHistoryResult setListGiftingHistoryAttributes(GiftingHistory existingGiftingHistory,
			ListSpecificGiftingHistoryResult giftingHistoryResult) {

		if (null != existingGiftingHistory.getGiftType()
				&& existingGiftingHistory.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {

			List<GoldCertificateTransactionResponse> goldTransactionsList = new ArrayList<>();
			for (GoldGiftedDetails goldGift : existingGiftingHistory.getGoldGifted()) {

				GoldCertificateTransactionResponse goldTransaction = new GoldCertificateTransactionResponse();
				goldTransaction.setCertificateId(goldGift.getCertificateId());
				goldTransaction.setGoldGifted(goldGift.getGoldGifted());
				goldTransactionsList.add(goldTransaction);

			}
			giftingHistoryResult.setGoldGift(goldTransactionsList);

		}

		if (null != existingGiftingHistory.getGiftType()
				&& existingGiftingHistory.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) {

			PointsGiftTransactionResponse pointsGift = new PointsGiftTransactionResponse();
			pointsGift.setPointsGifted(existingGiftingHistory.getPointsGifted().getPointsGifted());
			pointsGift.setSenderTransactionRefId(existingGiftingHistory.getPointsGifted().getSenderTransactionRefId());
			pointsGift.setReceiverTransactionRefId(
					existingGiftingHistory.getPointsGifted().getReceiverTransactionRefId());
			giftingHistoryResult.setPointsGift(pointsGift);

		}

		if (null != existingGiftingHistory.getGiftType()
				&& existingGiftingHistory.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) {

			giftingHistoryResult.setVoucherCode(existingGiftingHistory.getVoucherCode());

		}

		return giftingHistoryResult;

	}

	/**
	 * This method is used to retrieve the offer description for the voucher code.
	 * @param existingGiftingHistory
	 * @param senderAccountDetails
	 * @return
	 */
	private String getOfferDescription(GiftingHistory existingGiftingHistory, GetMemberResponse senderAccountDetails) {

		String offerDescription = null;
		String language = null != senderAccountDetails.getUiLanguage() ? senderAccountDetails.getUiLanguage()
				: senderAccountDetails.getLanguage();

		if (!existingGiftingHistory.getGiftType().equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) return null;

		Voucher voucher = voucherRepository.findByVoucherCode(existingGiftingHistory.getVoucherCode());
		if (null != voucher && null != voucher.getOfferInfo()) {

			OfferCatalog offerCatalog = offerRepository.findByOfferId(voucher.getOfferInfo().getOffer());

			if (null != offerCatalog && null != offerCatalog.getOffer()
					&& null != offerCatalog.getOffer().getOfferTitle()) {
				
				if (null == language || (null != language && GiftingConstants.LANGUAGE_ENGLISH_LIST.contains(language.toUpperCase()))) {
					return offerCatalog.getOffer().getOfferTitle().getOfferTitleEn();
				}

				if (GiftingConstants.LANGUAGE_ARABIC_LIST.contains(language.toUpperCase())) {
					return offerCatalog.getOffer().getOfferTitle().getOfferTitleAr();
				}

			}

		}

		return offerDescription;

	}
	
	public String configureLanguage(GetMemberResponse accountDetails) {
		String language = "";
		if(accountDetails.getUiLanguage()!=null){
			language = accountDetails.getUiLanguage();
		}
		else if(accountDetails.getLanguage()!=null) {
			language = accountDetails.getLanguage();
		}
		else {
			language = GiftingConstants.LANGUAGE_ENGLISH;
		}
		LOG.info("Language is {}", language);
		return language.toUpperCase();
	}

//	private void saveGiftingHistoryAfterPayment(String id, PaymentResponse paymentResponse, String userName) {
//		
//		String paymentTransactionNum = (paymentResponse == null) ? "" : paymentResponse.getTransactionNo();
//		
//		Query query = new Query(new Criteria("_id").is(id));			
//		Update update = new Update().set("PaymentTransactionNo", paymentTransactionNum)
//				.set("PaymentTransactionDate", new Date())
//				.set("UpdatedDate", new Date()).set("UpdatedUser", userName);
//		UpdateResult res =mongoOperations.updateFirst(query, update, "GiftingHistory");
//		LOG.info("Update gifting history {} ",res.getModifiedCount());
//
//	}


	/**	CANCEL VOUCHER GIFT METHODS **/

	/**
	 * This method is used to cancel/revert voucher gift.
	 * @param cancelGiftRequest
	 * @param headers
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse cancelVoucherGift(CancelGiftRequest cancelGiftRequest, Headers headers, ResultResponse resultResponse) {

		try {

			LOG.info("Entering cancelVoucherGift");

			Voucher voucher = voucherRepository.findByVoucherCodeAndStatus(cancelGiftRequest.getVoucherCode(), VoucherStatus.ACTIVE);

			if(!validateCancelVoucherParameters(voucher, resultResponse)) return resultResponse;

			if(null != voucher.getPartnerCode() && !voucher.getPartnerCode().equalsIgnoreCase(GiftingConstants.CARREFOUR)) {
				if(!cancelVoucherUpdateDB(voucher, null, GiftingConstants.NOT_CRFR_VOUCHER, headers, resultResponse)) return resultResponse;
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_SUCCESS.getId(),
						GiftingCodes.VOUCHER_GIFT_CANCEL_SUCCESS.getMsg());
				return resultResponse;
			}

			if(null == voucher.getVoucherAmount()) {
				resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_NO_VOUCHER_AMOUNT.getIntId(),
						GiftingCodes.VOUCHER_GIFT_NO_VOUCHER_AMOUNT.getMsg());
				resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
						GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
				return resultResponse;
			}

			Double crfrBalance = null;
			if(null != voucher.getVoucherBalance()) {

				if(voucher.getVoucherBalance() < voucher.getVoucherAmount()) {
					resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_ALREADY_USED.getIntId(),
							GiftingCodes.VOUCHER_GIFT_ALREADY_USED.getMsg());
					resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
							GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
					return resultResponse;
				}

				crfrBalance = retrieveCRFRBalance(voucher, resultResponse);
				if(null == crfrBalance) return resultResponse;

				if(crfrBalance < voucher.getVoucherAmount()) {
					updateCRFRBalaneDB(crfrBalance, voucher.getVoucherBalance(), voucher, headers);
					resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_ALREADY_USED.getIntId(),
							GiftingCodes.VOUCHER_GIFT_ALREADY_USED.getMsg());
					resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
							GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
					return resultResponse;
				}

			} else {

				crfrBalance = retrieveCRFRBalance(voucher, resultResponse);
				if(null == crfrBalance) return resultResponse;

				if (crfrBalance.doubleValue() < voucher.getVoucherAmount()) {
					updateCRFRBalaneDB(crfrBalance, voucher.getVoucherBalance(), voucher, headers);
					resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_ALREADY_USED.getIntId(),
							GiftingCodes.VOUCHER_GIFT_ALREADY_USED.getMsg());
					resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
							GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
					return resultResponse;
				}

			}

			if(!cancelVoucherUpdateDB(voucher, crfrBalance, GiftingConstants.IS_CRFR_VOUCHER, headers, resultResponse)) return resultResponse;
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_SUCCESS.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_SUCCESS.getMsg());

		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "giftVoucher",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		return resultResponse;

	}

	/**
	 * This method is used to update DB after cancelling voucher gift.
	 * @param voucher
	 * @param crfrBalance
	 * @param action
	 * @param headers
	 */
	private boolean cancelVoucherUpdateDB(Voucher voucher, Double crfrBalance, String action, Headers headers, ResultResponse resultResponse) throws MarketplaceException {

		Optional<GiftingHistory> oldGiftingHistory = giftingHistoryRepository.findById(voucher.getGiftDetails().getGiftId());

		if(!oldGiftingHistory.isPresent()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_NO_GIFT_HISTORY_FOUND.getIntId(),
					GiftingCodes.VOUCHER_GIFT_NO_GIFT_HISTORY_FOUND.getMsg() + voucher.getGiftDetails().getGiftId());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
			return false;
		}

		GiftingHistory giftingHistory = oldGiftingHistory.get();
		String senderAccountNumber = null;
		String senderMembershipCode = null;

		if(null != giftingHistory.getSenderInfo()) {
			senderAccountNumber = giftingHistory.getSenderInfo().getAccountNumber();
			senderMembershipCode = giftingHistory.getSenderInfo().getMembershipCode();
		}

		if(null == senderAccountNumber) {
			senderAccountNumber = null != voucher.getGiftDetails() ? voucher.getGiftDetails().getGiftedAccountNumber() : null;
		}

		if(null == senderAccountNumber) {
			resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_NO_SENDER_ACCOUNT_NUM_FOUND.getIntId(),
					GiftingCodes.VOUCHER_GIFT_NO_SENDER_ACCOUNT_NUM_FOUND.getMsg());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
			return false;
		}

		if(null != senderAccountNumber && null == senderMembershipCode) {
			List<String> accounts = new ArrayList<>();
			accounts.add(senderAccountNumber);
			GetListMemberResponse getListMemberResponse = giftingService.getListMemberDetails(accounts, headers);
			List<GetListMemberResponseDto> memberDetails = getListMemberResponse.getListMember();
			String finalSenderAccountNumber = senderAccountNumber;
			GetListMemberResponseDto getListMemberResponseDto = memberDetails.stream()
					.filter(m -> null != m.getAccountNumber() && m.getAccountNumber().equals(finalSenderAccountNumber)).findAny()
					.orElse(null);
			if(null != getListMemberResponseDto) senderMembershipCode = getListMemberResponseDto.getMembershipCode();
		}

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.ORIGINAL_ACCOUNT_DETAILS,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_HELPER_CANCEL_GIFT_DB,
				senderAccountNumber, senderMembershipCode);

		giftingHistoryRepository.deleteById(voucher.getGiftDetails().getGiftId());

		VoucherGiftDetails giftDetails = voucher.getGiftDetails();
		giftDetails.setGiftedAccountNumber(null);
		giftDetails.setGiftId(null);
		giftDetails.setIsGift(GiftingConstants.IS_GIFT_NO);
		voucher.setStartDate(new Date());
		voucher.setAccountNumber(senderAccountNumber);
		voucher.setMembershipCode(senderMembershipCode);
		voucher.setGiftDetails(giftDetails);
		voucher.setUpdatedUser(headers.getUserName());
		voucher.setUpdatedDate(new Date());

		if(action.equalsIgnoreCase(GiftingConstants.IS_CRFR_VOUCHER)) {
			voucher.setVoucherBalance(crfrBalance);
		}

		Voucher updatedVoucher = voucherRepository.save(voucher);

		auditService.deleteDataAudit(DBConstants.VOUCHERS, voucher, GiftingConfigurationConstants.CANCEL_GIFT, headers.getExternalTransactionId(), headers.getUserName());
		auditService.updateDataAudit(DBConstants.VOUCHERS, updatedVoucher, GiftingConfigurationConstants.CANCEL_GIFT, voucher, headers.getExternalTransactionId(), headers.getUserName());

		return true;

	}

	/**
	 * This method is used to retrieve balance from CRFR.
	 * @param voucher
	 * @param resultResponse
	 * @return
	 * @throws VoucherManagementException
	 */
	private Double retrieveCRFRBalance(Voucher voucher, ResultResponse resultResponse) throws VoucherManagementException {

		BigDecimal balance = voucherControllerHelper.getCarrefourBalance(voucher, voucher.getAccountNumber());
		if(null == balance) {
			resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_NO_CRFR_BALANCE_RETRIEVED.getIntId(),
					GiftingCodes.VOUCHER_GIFT_NO_CRFR_BALANCE_RETRIEVED.getMsg());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
			return null;
		}

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.CRFR_BALANCE,
				this.getClass().getSimpleName(), GiftingConfigurationConstants.CONTROLLER_HELPER_CANCEL_GIFT_RETRIVED_CRFR_BAL,
				balance.doubleValue());

		return balance.doubleValue();

	}

	/**
	 * Method to update CRFR balance in Voucher collection.
	 * @param updatedCRFRBalance
	 * @param currentCRFRBalance
	 * @param voucher
	 * @param headers
	 */
	private void updateCRFRBalaneDB(Double updatedCRFRBalance, Double currentCRFRBalance, Voucher voucher, Headers headers) {

		if (null == currentCRFRBalance || (null != currentCRFRBalance && currentCRFRBalance != updatedCRFRBalance)) {
			voucher.setVoucherBalance(updatedCRFRBalance);
			voucher.setUpdatedUser(headers.getUserName());
			voucher.setUpdatedDate(new Date());
			Voucher updatedVoucher = voucherRepository.save(voucher);
			auditService.updateDataAudit(DBConstants.VOUCHERS, updatedVoucher, GiftingConfigurationConstants.CANCEL_GIFT, voucher, headers.getExternalTransactionId(), headers.getUserName());
		}

	}

	/**
	 * This method is used to validate cancel voucher API parameters.
	 * @param voucher
	 * @param resultResponse
	 * @return
	 */
	private boolean validateCancelVoucherParameters(Voucher voucher, ResultResponse resultResponse) {

		if (null == voucher) {
			resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_VOUCHER_NOT_ACTIVE.getIntId(),
					GiftingCodes.VOUCHER_GIFT_VOUCHER_NOT_ACTIVE.getMsg());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
			return false;
		}

		if((null == voucher.getGiftDetails() || null == voucher.getGiftDetails().getIsGift()) ||
				(null != voucher.getGiftDetails() && (null == voucher.getGiftDetails().getIsGift() ||
						(voucher.getGiftDetails().getIsGift().equalsIgnoreCase(GiftingConstants.IS_GIFT_NO))))) {
			resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_VOUCHER_NOT_GIFTED.getIntId(),
					GiftingCodes.VOUCHER_GIFT_VOUCHER_NOT_GIFTED.getMsg());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
			return false;
		}

		if(null != voucher.getGiftDetails() && null != voucher.getGiftDetails().getIsGift() &&
				voucher.getGiftDetails().getIsGift().equalsIgnoreCase(GiftingConstants.IS_GIFT_YES) && null == voucher.getGiftDetails().getGiftId()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.VOUCHER_GIFT_NO_GIFT_ID.getIntId(),
					GiftingCodes.VOUCHER_GIFT_NO_GIFT_ID.getMsg());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFT_CANCEL_FAILED.getMsg());
			return false;
		}

		return true;

	}
	
	/***
	 * 
	 * @param offerQuantity
	 * @param premiumVoucherDto
	 * @param headers
	 * @param activity 
	 * @throws MarketplaceException 
	 */
	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_PREMIUM_VOUCHER_GIFT)
	public Boolean purchaseVoucher(OfferGiftValues offerValues, PremiumVoucherRequest premiumVoucherDto, Headers headers, ResultResponse resultResponse) throws MarketplaceException {
		
		if(!ObjectUtils.isEmpty(offerValues)
		&& !StringUtils.isEmpty(offerValues.getOfferId())
		&& !ObjectUtils.isEmpty(premiumVoucherDto)
		&& !StringUtils.isEmpty(premiumVoucherDto.getAccountNumber())) {
			
			LOG.info("Gifting offer : {}", offerValues.getOfferId());
			PurchaseRequestDto purchaseRequestDto = Utils.getFreePurchaseRequest(offerValues, headers, premiumVoucherDto.getAccountNumber());
			PurchaseResultResponse purchaseResult = new PurchaseResultResponse(headers.getExternalTransactionId());
			purchaseResult = purchaseDomain.validateAndSavePurchaseHistory(purchaseRequestDto, purchaseResult, headers);
			LOG.info("Offer Gifted");
			
			if(Checks.checkErrorsPresent(purchaseResult)) {
				purchaseResult.getApiStatus().getErrors().forEach(
						e->resultResponse.addErrorAPIResponse(e.getCode(), e.getMessage())); 
			}
			
			if(!ObjectUtils.isEmpty(purchaseResult)
			&& !ObjectUtils.isEmpty(purchaseResult.getPurchaseResponseDto())
			&& !CollectionUtils.isEmpty(purchaseResult.getPurchaseResponseDto().getVoucherCodes())) {
				
				purchaseResult = new PurchaseResultResponse(headers.getExternalTransactionId());
				GetMemberResponse member = fetchServiceValues.getMemberDetails(premiumVoucherDto.getAccountNumber(), purchaseResult, headers);
			
				if(!ObjectUtils.isEmpty(member)) {
					sendSmsForVoucherPurchase(purchaseRequestDto, headers, premiumVoucherDto, member);
				}
			}
			
		}
		
		return null != resultResponse && null != resultResponse.getApiStatus() && CollectionUtils.isEmpty(resultResponse.getApiStatus().getErrors());
	}

	/***
	 * 
	 * @param purchaseRequestDto
	 * @return cost of voucher from purchase history
	 */
	private String getVoucherCost(PurchaseRequestDto purchaseRequestDto) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("PurchaseItem")
				.is(purchaseRequestDto.getSelectedPaymentItem())
		        .and("ExtRefNo").is(purchaseRequestDto.getExtTransactionId()));
		query.fields().include("Cost");
		List<PurchaseHistory> purchaseHistoryList = mongoOperations.find(query, PurchaseHistory.class, OffersDBConstants.PURCHASE_HISTORY);
		return !CollectionUtils.isEmpty(purchaseHistoryList)
			&& !ObjectUtils.isEmpty(purchaseHistoryList.get(0))
			&& !ObjectUtils.isEmpty(purchaseHistoryList.get(0).getCost())
			? String.valueOf(purchaseHistoryList.get(0).getCost())
			: null;
	}

	/***
	 * 
	 * @param purchaseRequestDto
	 * @param purchaseResult
	 * @param headerDto
	 * @param activityDto 
	 * @param activity 
	 * @param member 
	 * @param string 
	 */
	private void sendSmsForVoucherPurchase(PurchaseRequestDto purchaseRequestDto, Headers headerDto, PremiumVoucherRequest premiumVoucherDto, 
			GetMemberResponse member) {
		
		LOG.info("Sending SMS");
		SMSRequestDto smsRequest = new SMSRequestDto();
		smsRequest.setMembershipCode(member.getMembershipCode());
		smsRequest.setDestinationNumber(Arrays.asList(member.getAccountNumber()));
		smsRequest.setNotificationCode(premiumGiftSmsNotificationCode);
		smsRequest.setNotificationId(premiumGiftSmsNotificationId);
		smsRequest.setTemplateId(premiumGiftSmsTemplateId);
		smsRequest.setTransactionId(headerDto.getExternalTransactionId());
		smsRequest.setLanguage(StringUtils.equalsIgnoreCase(member.getUiLanguage(), OfferConstants.ENGLISH.get())
				? OffersConfigurationConstants.LANGUAGE_EN 
				: OffersConfigurationConstants.LANGUAGE_AR);
		
		Map<String, String> additionalParameters = new HashMap<>();
		additionalParameters.put(GiftingConstants.FIRST_NAME_SMS_PARAMETER, !ObjectUtils.isEmpty(member)
				&& !StringUtils.isEmpty(member.getFirstName())
				? member.getFirstName()
				: null);
		additionalParameters.put(GiftingConstants.POINTS_REDEEMED_SMS_PARAMETER, !ObjectUtils.isEmpty(premiumVoucherDto.getPointsValue())
				? String.valueOf(premiumVoucherDto.getPointsValue())
				: null);
		if(!ObjectUtils.isEmpty(premiumVoucherDto.getActivityDescription())) {
			
			additionalParameters.put(GiftingConstants.ACTIVITY_DESCRIPTION, smsRequest.getLanguage().equalsIgnoreCase( OffersConfigurationConstants.LANGUAGE_EN)
					?premiumVoucherDto.getActivityDescription().getEnglish() 
					:premiumVoucherDto.getActivityDescription().getArabic());
			
		}
		additionalParameters.put(GiftingConstants.VOUCHER_DENOMINATION, !ObjectUtils.isEmpty(purchaseRequestDto.getVoucherDenomination())
				? String.valueOf(purchaseRequestDto.getVoucherDenomination())
				: getVoucherCost(purchaseRequestDto));
		smsRequest.setAdditionalParameters(additionalParameters);
		

		eventHandler.publishSms(smsRequest);
		LOG.info("SMS sent");
	}
	
	/***
	 * 
	 * @param giftRequestDto
	 * @param resultResponse 
	 * @return
	 */
	public boolean validateGiftConfigurationRequest(GiftConfigureRequestDto giftRequestDto, Headers headers, ResultResponse resultResponse) {
		
		return !ObjectUtils.isEmpty(giftRequestDto)
			&& !StringUtils.isEmpty(giftRequestDto.getGiftType())
			&& GiftingResponses.setResponseAfterConditionCheck(Utilities.presentInList(validGiftTypes, giftRequestDto.getGiftType()),
					GiftingCodes.INVALID_GIFT_TYPE_VALUE, resultResponse)
			&& (!premiumGiftType.equalsIgnoreCase(giftRequestDto.getGiftType())
			|| validatePremiumGiftRequest(giftRequestDto, resultResponse))
			&& (!promotionalGiftType.equalsIgnoreCase(giftRequestDto.getGiftType())
			|| validatePromotionalGiftRequest(giftRequestDto, headers, resultResponse))
			&& (!subscriptionGiftType.equalsIgnoreCase(giftRequestDto.getGiftType())
			|| validateSubscriptionPurchaseGiftRequest(giftRequestDto, resultResponse))
			&& validateOfferValues(giftRequestDto.getOfferValues(), resultResponse);
	}
	
	public boolean validateOfferValues(List<OfferValueDto> offerList, ResultResponse resultResponse) {
		
		if(!CollectionUtils.isEmpty(offerList)) {
			
			offerList.forEach(o->{
				
				OfferCatalog offer = offerRepository.findByOfferId(o.getOfferId());
				
				if(ObjectUtils.isEmpty(offer)) {
					
					resultResponse.addErrorAPIResponse(GiftingCodes.OFFER_DOES_NOT_EXIST_IN_DB.getIntId(),
							GiftingCodes.OFFER_DOES_NOT_EXIST_IN_DB.getMsg());
					
					
				} else {
					
					if(!offer.getOfferType().getOfferTypeId().equals(o.getOfferType())) {
						
						resultResponse.addErrorAPIResponse(GiftingCodes.OFFER_TYPE_INVALID.getIntId(),
								GiftingCodes.OFFER_TYPE_INVALID.getMsg());
						
					}
					
					if(Checks.checkIsCashVoucher(o.getOfferType())) {
						
						if(ObjectUtils.isEmpty(o.getDenomination())) {
						
							resultResponse.addErrorAPIResponse(GiftingCodes.DENOMINATION_MANDATORY_FOR_CASH_VOUCHER.getIntId(),
								GiftingCodes.DENOMINATION_MANDATORY_FOR_CASH_VOUCHER.getMsg());
						
						} else if(!ObjectUtils.isEmpty(offer.getDenominations())
							&& !offer.getDenominations().stream().anyMatch(d->d.getDenominationValue().getDirhamValue().equals(o.getDenomination()))) {
							
							resultResponse.addErrorAPIResponse(GiftingCodes.DENOMINATION_INVALID_FOR_CASH_VOUCHER.getIntId(),
									GiftingCodes.DENOMINATION_INVALID_FOR_CASH_VOUCHER.getMsg());
							
						}
						
					}
						
					if(Checks.checkIsDealVoucher(o.getOfferType())) {
						
						if(ObjectUtils.isEmpty(o.getSubOfferId())) {
							
							resultResponse.addErrorAPIResponse(GiftingCodes.SUB_OFFER_MANDATORY_FOR_DEAL_VOUCHER.getIntId(), 
								GiftingCodes.SUB_OFFER_MANDATORY_FOR_DEAL_VOUCHER.getMsg());
						
						} else if(!ObjectUtils.isEmpty(offer.getSubOffer())
						    && !offer.getSubOffer().stream().anyMatch(s->s.getSubOfferId().equals(o.getSubOfferId()))) {
							
							resultResponse.addErrorAPIResponse(GiftingCodes.SUB_OFFER_INVALID_FOR_DEAL_VOUCHER.getIntId(), 
									GiftingCodes.SUB_OFFER_INVALID_FOR_DEAL_VOUCHER.getMsg());
							
						}
					}
					
				}
					
			});
			
		}
		
		return Checks.checkNoErrors(resultResponse);
	}
	
	/***
	 * 
	 * @param giftRequestDto
	 * @param resultResponse 
	 * @return
	 */
	private boolean validatePremiumGiftRequest(GiftConfigureRequestDto giftRequestDto, ResultResponse resultResponse) {
		
		return !ObjectUtils.isEmpty(giftRequestDto)
			&& GiftingResponses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(giftRequestDto.getMinPointValue()),
					GiftingCodes.MIN_VALUE_NOT_PRESENT, resultResponse)
			&& GiftingResponses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(giftRequestDto.getMaxPointValue()),
					GiftingCodes.MAX_VALUE_NOT_PRESENT, resultResponse)
			&& GiftingResponses.setResponseAfterConditionCheck(!mongoOperations
					.exists(query(where(GiftingConfigurationConstants.MIN_POINT_VALUE)
					.lte(giftRequestDto.getMinPointValue())
					.and(GiftingConfigurationConstants.MAX_POINT_VALUE)
					.gte(giftRequestDto.getMaxPointValue())), Gifts.class),
					GiftingCodes.PREMIUM_GIFT_POINT_RANGE_EXISTING, resultResponse);
	}

	/***
	 * 
	 * @param giftRequestDto
	 * @param headers 
	 * @param resultResponse 
	 * @return
	 */
	private boolean validatePromotionalGiftRequest(GiftConfigureRequestDto giftRequestDto, Headers headers, ResultResponse resultResponse) {
		
		return !ObjectUtils.isEmpty(giftRequestDto)
				&& GiftingResponses.setResponseAfterConditionCheck(!StringUtils.isEmpty(giftRequestDto.getPromotionalGiftId()),
						GiftingCodes.GIFT_ID_NOT_PRESENT, resultResponse)
				&& GiftingResponses.setResponseAfterConditionCheck(!StringUtils.isEmpty(headers.getChannelId()),
						GiftingCodes.CHANNEL_ID_NOT_PRESENT, resultResponse)
				&& GiftingResponses.setResponseAfterConditionCheck(!mongoOperations.exists(query(where(GiftingConfigurationConstants.PROMOTIONAL_GIFT_ID).is(giftRequestDto.getPromotionalGiftId())), Gifts.class),
						GiftingCodes.PROMOTIONAL_GIFT_ID_EXISTING, resultResponse);
	}

	/***
	 * 
	 * @param giftRequestDto
	 * @param resultResponse 
	 * @return
	 */
	private boolean validateSubscriptionPurchaseGiftRequest(GiftConfigureRequestDto giftRequestDto, ResultResponse resultResponse) {
		
		return !ObjectUtils.isEmpty(giftRequestDto)
				&& GiftingResponses.setResponseAfterConditionCheck(!StringUtils.isEmpty(giftRequestDto.getSubscriptionCatalogId()),
						GiftingCodes.GIFT_ID_NOT_PRESENT, resultResponse)
				&& GiftingResponses.setResponseAfterConditionCheck(!mongoOperations.exists(query(where(GiftingConfigurationConstants.SUBSCRIPTION_CATALOG_ID).is(giftRequestDto.getSubscriptionCatalogId())), Gifts.class),
						GiftingCodes.SUBSCRIPTION_GIFT_ID_EXISTING, resultResponse);
	}

}
