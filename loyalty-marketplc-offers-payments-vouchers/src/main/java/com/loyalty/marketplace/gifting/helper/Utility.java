package com.loyalty.marketplace.gifting.helper;

import java.util.Calendar;
import java.util.Date;

import org.modelmapper.PropertyMap;

import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.helper.dto.CountCompare;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderListDto;
import com.loyalty.marketplace.gifting.inbound.dto.BirthdayReminderStatusDto;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingCounter;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;
import com.loyalty.marketplace.gifting.outbound.database.entity.Limits;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftingHistoryResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListSpecificGiftingHistoryResult;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

public class Utility {
	
	private Utility() {}
	
	/**
	 * This is used to set remindPrior attribute - for data consistency in DB.
	 * @param memberToRemind
	 * @param resultResponse
	 * @return
	 */
	public static boolean setRemindPriorAttribute(BirthdayReminderListDto memberToRemind, ResultResponse resultResponse) {

		if(memberToRemind.getRemindPrior().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_DAY)) {
			memberToRemind.setRemindPrior(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_DAY);
		} else if(memberToRemind.getRemindPrior().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_WEEK)) {
			memberToRemind.setRemindPrior(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_WEEK);
		} else if(memberToRemind.getRemindPrior().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_MONTH)) {
			memberToRemind.setRemindPrior(GiftingConstants.BIRTHDAY_REMINDER_PRIOR_MONTH);
		} else {
			resultResponse.addErrorAPIResponse(
					GiftingCodes.BIRTHDAY_REMINDER_INVALID_REMIND_PRIOR.getIntId(),
					GiftingCodes.BIRTHDAY_REMINDER_INVALID_REMIND_PRIOR.getMsg());
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * This is used to set status attribute - for data consistency in DB.
	 * @param birthdayReminderStatusDto
	 * @param resultResponse
	 * @return
	 */
	public static boolean setStatusAttribute(BirthdayReminderStatusDto birthdayReminderStatusDto, ResultResponse resultResponse) {

		if(birthdayReminderStatusDto.getStatus().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_ACCEPTED)) {
			birthdayReminderStatusDto.setStatus(GiftingConstants.BIRTHDAY_REMINDER_ACCEPTED);
		} else if(birthdayReminderStatusDto.getStatus().equalsIgnoreCase(GiftingConstants.BIRTHDAY_REMINDER_REJECTED)) {
			birthdayReminderStatusDto.setStatus(GiftingConstants.BIRTHDAY_REMINDER_REJECTED);
		} else {
			resultResponse.addErrorAPIResponse(
					GiftingCodes.BIRTHDAY_REMINDER_INVALID_STATUS.getIntId(),
					GiftingCodes.BIRTHDAY_REMINDER_INVALID_STATUS.getMsg());
			resultResponse.setResult(GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getId(),
					GiftingCodes.BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE.getMsg());
			return false;
		}
		
		return true;
		
	}

	/**
	 * This method is used to get today's date.
	 * @return
	 */
	public static Date getCurrentDate() {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		return currentDate.getTime();
		
	}
	
	/**
	 * This method is used to check if given date is today's date.
	 * @param date
	 * @return
	 */
	public static boolean isTodayDate(Date date) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(date);
		checkDate.set(Calendar.MILLISECOND, 0);
		checkDate.set(Calendar.SECOND, 0);
		checkDate.set(Calendar.MINUTE, 0);
		checkDate.set(Calendar.HOUR, 0);

		return currentDate.getTime().equals(checkDate.getTime());
	
	}
	
	/**
	 * This method checks if given date is before the current date.
	 * @param date
	 * @return
	 */
	public static boolean checkPastDate(Date date) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(date);
		checkDate.set(Calendar.MILLISECOND, 0);
		checkDate.set(Calendar.SECOND, 0);
		checkDate.set(Calendar.MINUTE, 0);
		checkDate.set(Calendar.HOUR, 0);

		return checkDate.getTime().before(currentDate.getTime());
	
	}
	
	/**
	 * This method is used to check if given date is tomorrow's date.
	 * @param dateToCheck
	 * @return
	 */
	public static boolean isAfterDays(Date dateToCheck) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());  
		currentDate.add(Calendar.DATE, 1);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(dateToCheck);
		checkDate.set(Calendar.MILLISECOND, 0);
		checkDate.set(Calendar.SECOND, 0);
		checkDate.set(Calendar.MINUTE, 0);
		checkDate.set(Calendar.HOUR, 0);

		return currentDate.getTime().equals(checkDate.getTime());
	
	}
	
	/**
	 * This method is used to check if given date is 7 days from today.
	 * @param dateToCheck
	 * @return
	 */
	public static boolean isAfterWeeks(Date dateToCheck) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());  
		currentDate.add(Calendar.DATE, 7);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(dateToCheck);
		checkDate.set(Calendar.MILLISECOND, 0);
		checkDate.set(Calendar.SECOND, 0);
		checkDate.set(Calendar.MINUTE, 0);
		checkDate.set(Calendar.HOUR, 0);
	
		return currentDate.getTime().equals(checkDate.getTime());
	
	}

	/**
	 * This method is used to check if given date is one month from today.
	 * @param dateToCheck
	 * @return
	 */
	public static boolean isAfterMonths(Date dateToCheck) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());  
		currentDate.add(Calendar.MONTH, 1);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(dateToCheck);
		checkDate.set(Calendar.MILLISECOND, 0);
		checkDate.set(Calendar.SECOND, 0);
		checkDate.set(Calendar.MINUTE, 0);
		checkDate.set(Calendar.HOUR, 0);
		
		return currentDate.getTime().equals(checkDate.getTime());
	}

	/**
	 * This method is used to check if given date is 7 days ago.
	 * @param dateToCheck
	 * @return
	 */
	public static boolean isBeforeWeeks(Date dateToCheck) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());  
		currentDate.add(Calendar.DATE, -7);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(dateToCheck);
		checkDate.set(Calendar.MILLISECOND, 0);
		checkDate.set(Calendar.SECOND, 0);
		checkDate.set(Calendar.MINUTE, 0);
		checkDate.set(Calendar.HOUR, 0);
	
		return currentDate.getTime().equals(checkDate.getTime());
	
	}

	/**
	 * This method is used to check if given date is one month before.
	 * @param dateToCheck
	 * @return
	 */
	public static boolean isBeforeMonths(Date dateToCheck) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());  
		currentDate.add(Calendar.MONTH, -1);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(dateToCheck);
		checkDate.set(Calendar.MILLISECOND, 0);
		checkDate.set(Calendar.SECOND, 0);
		checkDate.set(Calendar.MINUTE, 0);
		checkDate.set(Calendar.HOUR, 0);
		
		return currentDate.getTime().equals(checkDate.getTime());
	}

	/**
	 * This method is used to check if member exists.
	 * @param member
	 * @param accountNumber
	 * @param resultResponse
	 * @return
	 */
	public static boolean checkMemberExists(GetMemberResponse member, String accountNumber, ResultResponse resultResponse) {
		if(null == member) {
			resultResponse.addErrorAPIResponse(
					GiftingCodes.MEMBER_DOES_NOT_EXIST.getIntId(),
					GiftingCodes.MEMBER_DOES_NOT_EXIST.getMsg()
							+ accountNumber);
			resultResponse.setResult(GiftingCodes.GIFTING_POINTS_FAILURE.getId(),
					GiftingCodes.GIFTING_POINTS_FAILURE.getMsg());
			return false;
		}
		return true;
	}
	
	/**
	 * This method is used to populate CountCompare object from GiftingCounter collection.
	 * CountCompare is used used to validate gifting limits for the account/memberhsip.
	 * @param giftingCounter
	 * @return
	 */
	public static CountCompare populateCounterObject(GiftingCounter giftingCounter) {
		
		CountCompare countCompare = new CountCompare();
		
		countCompare.setSentDayCount(giftingCounter.getSentDayCount());
		countCompare.setSentWeekCount(giftingCounter.getSentWeekCount());
		countCompare.setSentMonthCount(giftingCounter.getSentMonthCount());
		countCompare.setReceivedDayCount(giftingCounter.getReceivedDayCount());
		countCompare.setReceivedWeekCount(giftingCounter.getReceivedWeekCount());
		countCompare.setReceivedMonthCount(giftingCounter.getReceivedMonthCount());
		
		return countCompare;
	
	}
	
	/**
	 * This method is used to populate CountCompare object from GiftingLimits collection.
	 * @param limits
	 * @return
	 */
	public static CountCompare populateLimitsObject(Limits limits) {
		
		CountCompare countCompare = new CountCompare();
		
		countCompare.setSentDayCount(limits.getSenderDayLimit());
		countCompare.setSentWeekCount(limits.getSenderWeekLimit());
		countCompare.setSentMonthCount(limits.getSenderMonthLimit());
		countCompare.setReceivedDayCount(limits.getReceiverDayLimit());
		countCompare.setReceivedWeekCount(limits.getReceiverWeekLimit());
		countCompare.setReceivedMonthCount(limits.getReceiverMonthLimit());
		
		return countCompare;
	
	}
	
	/** 
	 * This method is used to check sender account limits.
	 * @param actualCount
	 * @param staticLimit
	 * @param gift
	 * @param resultResponse
	 * @return
	 */
	public static boolean checkSenderAccountLimits(CountCompare actualCount, CountCompare staticLimit, Double gift, ResultResponse resultResponse) {
		
		if((actualCount.getSentDayCount() + gift) > staticLimit.getSentDayCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_ACCOUNT_EXCEED_DAY.getIntId(),
					GiftingCodes.SENDER_ACCOUNT_EXCEED_DAY.getMsg() + String.valueOf(actualCount.getSentDayCount())
							+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getSentDayCount())
							+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getSentWeekCount() + gift) > staticLimit.getSentWeekCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_ACCOUNT_EXCEED_WEEK.getIntId(),
					GiftingCodes.SENDER_ACCOUNT_EXCEED_WEEK.getMsg() + String.valueOf(actualCount.getSentWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getSentWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getSentMonthCount() + gift) > staticLimit.getSentMonthCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_ACCOUNT_EXCEED_MONTH.getIntId(),
					GiftingCodes.SENDER_ACCOUNT_EXCEED_MONTH.getMsg() + String.valueOf(actualCount.getSentMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getSentMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty();
		
	}
	
	/**
	 * This method is used to check sender membership limits.
	 * @param actualCount
	 * @param staticLimit
	 * @param gift
	 * @param resultResponse
	 * @return
	 */
	public static boolean checkSenderMembershipLimits(CountCompare actualCount, CountCompare staticLimit, Double gift, ResultResponse resultResponse) {
		
		if((actualCount.getSentDayCount() + gift) > staticLimit.getSentDayCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_MEMBERSHIP_EXCEED_DAY.getIntId(),
					GiftingCodes.SENDER_MEMBERSHIP_EXCEED_DAY.getMsg() + String.valueOf(actualCount.getSentDayCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getSentDayCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getSentWeekCount() + gift) > staticLimit.getSentWeekCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_MEMBERSHIP_EXCEED_WEEK.getIntId(),
					GiftingCodes.SENDER_MEMBERSHIP_EXCEED_WEEK.getMsg() + String.valueOf(actualCount.getSentWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getSentWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getSentMonthCount() + gift) > staticLimit.getSentMonthCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.SENDER_MEMBERSHIP_EXCEED_MONTH.getIntId(),
					GiftingCodes.SENDER_MEMBERSHIP_EXCEED_MONTH.getMsg() + String.valueOf(actualCount.getSentMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getSentMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty();
		
	}
	
	/**
	 * This method is used to check receiver account limits.
	 * @param actualCount
	 * @param staticLimit
	 * @param gift
	 * @param resultResponse
	 * @return
	 */
	public static boolean checkReceiverAccountLimits(CountCompare actualCount, CountCompare staticLimit, Double gift, ResultResponse resultResponse) {
		
		if((actualCount.getReceivedDayCount() + gift) > staticLimit.getReceivedDayCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.RECEIVER_ACCOUNT_EXCEED_DAY.getIntId(),
					GiftingCodes.RECEIVER_ACCOUNT_EXCEED_DAY.getMsg() + String.valueOf(actualCount.getReceivedDayCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getReceivedDayCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getReceivedWeekCount() + gift) > staticLimit.getReceivedWeekCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.RECEIVER_ACCOUNT_EXCEED_DAY.getIntId(),
					GiftingCodes.RECEIVER_ACCOUNT_EXCEED_DAY.getMsg() + String.valueOf(actualCount.getReceivedWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getReceivedWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getReceivedMonthCount() + gift) > staticLimit.getReceivedMonthCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.RECEIVER_ACCOUNT_EXCEED_DAY.getIntId(),
					GiftingCodes.RECEIVER_ACCOUNT_EXCEED_DAY.getMsg() + String.valueOf(actualCount.getReceivedMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getReceivedMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty();
		
	}
	
	/**
	 * This method is used to check receiver membership limits.
	 * @param actualCount
	 * @param staticLimit
	 * @param gift
	 * @param resultResponse
	 * @return
	 */
	public static boolean checkReceiverMembershipLimits(CountCompare actualCount, CountCompare staticLimit, Double gift, ResultResponse resultResponse) {
		
		if((actualCount.getReceivedDayCount() + gift) > staticLimit.getReceivedDayCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.RECEIVER_MEMBERSHIP_EXCEED_DAY.getIntId(),
					GiftingCodes.RECEIVER_MEMBERSHIP_EXCEED_DAY.getMsg() + String.valueOf(actualCount.getReceivedDayCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getReceivedDayCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getReceivedWeekCount() + gift) > staticLimit.getReceivedWeekCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.RECEIVER_MEMBERSHIP_EXCEED_WEEK.getIntId(),
					GiftingCodes.RECEIVER_MEMBERSHIP_EXCEED_WEEK.getMsg() + String.valueOf(actualCount.getReceivedWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getReceivedWeekCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		if((actualCount.getReceivedMonthCount() + gift) > staticLimit.getReceivedMonthCount()) {
			resultResponse.addErrorAPIResponse(GiftingCodes.RECEIVER_MEMBERSHIP_EXCEED_MONTH.getIntId(),
					GiftingCodes.RECEIVER_MEMBERSHIP_EXCEED_MONTH.getMsg() + String.valueOf(actualCount.getReceivedMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(staticLimit.getReceivedMonthCount())
					+ GiftingConstants.COMMA_SEPARATOR + String.valueOf(gift));
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty();
		
	}

	/**
	 * This method is used to check if account is active.
	 * @param accountDetails
	 * @param resultResponse
	 * @return
	 */
	public static boolean isMemberAccountActive(GetMemberResponse accountDetails, ResultResponse resultResponse) {
		
		if (null != accountDetails.getAccountStatus()
				&& !accountDetails.getAccountStatus().equalsIgnoreCase(GiftingConstants.ACCOUNT_STATUS_ACTIVE)
				&& !accountDetails.getAccountStatus().equalsIgnoreCase(GiftingConstants.ACCOUNT_STATUS_ACT)) {
			resultResponse.addErrorAPIResponse(GiftingCodes.MEMBER_ACCOUNT_NOT_ACTIVE.getIntId(),
					GiftingCodes.MEMBER_ACCOUNT_NOT_ACTIVE.getMsg() + accountDetails.getAccountNumber());
			resultResponse.setResult(GiftingCodes.VOUCHER_GIFTING_FAILED.getId(),
					GiftingCodes.VOUCHER_GIFTING_FAILED.getMsg());
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * This method is used to check if the input language is English or Arabic.
	 * @param language
	 * @return
	 */
	public static int checkLanguage(String language) {
		
		if (language.equalsIgnoreCase(GiftingConstants.LANGUAGE_EN)
				|| language.equalsIgnoreCase(GiftingConstants.LANGUAGE_ENGLISH))
			return 1;
		
		if (language.equalsIgnoreCase(GiftingConstants.LANGUAGE_AR)
				|| language.equalsIgnoreCase(GiftingConstants.LANGUAGE_ARABIC))
			return 2;
		
		return 3;
		
	}
	
	/**
	 * This method is used to validate request parameters for list gifting history API.
	 * @param accountNumber
	 * @param giftType
	 * @param filter
	 * @param listGiftingHistoryResponse
	 * @return
	 */
	public static boolean validateListGiftingHistoryRequest(String accountNumber, String giftType, String filter, ListGiftingHistoryResponse listGiftingHistoryResponse) {
		
		boolean nullCheck = false;
		
		if(null == accountNumber) {
			listGiftingHistoryResponse.addErrorAPIResponse(
					GiftingCodes.ACCOUNT_NUMBER_MANDATORY_FIELD.getIntId(),
					GiftingCodes.ACCOUNT_NUMBER_MANDATORY_FIELD.getMsg());
			nullCheck = true;
		}
		
		if(null == giftType || giftType.isEmpty()) {
			listGiftingHistoryResponse.addErrorAPIResponse(
					GiftingCodes.GIFT_TYPE_MANDATORY_FIELD.getIntId(),
					GiftingCodes.GIFT_TYPE_MANDATORY_FIELD.getMsg());
			nullCheck = true;
		}
		
		if(nullCheck) return false;
		
		if(!giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)
				&& !giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)
				&& !giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) {
			listGiftingHistoryResponse.addErrorAPIResponse(
					GiftingCodes.INVALID_GIFT_TYPE.getIntId(),
					GiftingCodes.INVALID_GIFT_TYPE.getMsg());
			nullCheck = true;
		}
		
		if(null == filter || filter.isEmpty()) return true;
		
		if(!filter.equalsIgnoreCase(GiftingConstants.FILTER_ALL)
				&& !filter.equalsIgnoreCase(GiftingConstants.FILTER_SENT)
				&& !filter.equalsIgnoreCase(GiftingConstants.FILTER_RECEIVED)) {
			listGiftingHistoryResponse.addErrorAPIResponse(
					GiftingCodes.INVALID_FILTER_TYPE.getIntId(),
					GiftingCodes.INVALID_FILTER_TYPE.getMsg());
			nullCheck = true;
		}
		
		return nullCheck ? false : true;
		
	}
	
	/**
	 * This method is used to validate language for english or arabic.
	 * @param language
	 * @return
	 */
	public static int validateLanguage(String language) {
		
		if (null == language
				|| (null != language && GiftingConstants.LANGUAGE_ENGLISH_LIST.contains(language.toUpperCase()))) {
			return 1;
		}
		
		if (null != language && GiftingConstants.LANGUAGE_ARABIC_LIST
				.contains(language.toUpperCase())) {
			return 2;
		}
		
		return 3;
		
	}
	
	/**
	 * This property map is used to populate gift history attributes in GET API response object.
	 */
	public static final PropertyMap<GiftingHistory, ListSpecificGiftingHistoryResult> giftHistoryFieldMapping = new PropertyMap<GiftingHistory, ListSpecificGiftingHistoryResult>() {
		
		@Override
		protected void configure() {
			map().setId(source.getId());
			map().setProgramCode(source.getProgramCode());
			map().setGiftType(source.getGiftType());
			
			map().getSenderInfo().setAccountNumber(source.getSenderInfo().getAccountNumber());
			map().getSenderInfo().setMembershipCode(source.getSenderInfo().getMembershipCode());
			map().getSenderInfo().setFirstName(source.getSenderInfo().getFirstName());
			map().getSenderInfo().setLastName(source.getSenderInfo().getLastName());
			map().getSenderInfo().setEmail(source.getSenderInfo().getEmail());
		
			map().getReceiverInfo().setAccountNumber(source.getReceiverInfo().getAccountNumber());
			map().getReceiverInfo().setMembershipCode(source.getReceiverInfo().getMembershipCode());
			map().getReceiverInfo().setFirstName(source.getReceiverInfo().getFirstName());
			map().getReceiverInfo().setLastName(source.getReceiverInfo().getLastName());
			map().getReceiverInfo().setEmail(source.getReceiverInfo().getEmail());
			
			map().setReceiverConsumption(source.getReceiverConsumption());
					
			map().setImageId(source.getImageId());
			map().setImageUrl(source.getImageUrl());
			map().setMessage(source.getMessage());
			map().setScheduledDate(source.getScheduledDate());
			
			map().getPurchaseDetails().setCardNumber(source.getPurchaseDetails().getCardNumber());
			map().getPurchaseDetails().setCardType(source.getPurchaseDetails().getCardType());
			map().getPurchaseDetails().setCardSubType(source.getPurchaseDetails().getCardSubType());
			map().getPurchaseDetails().setCardToken(source.getPurchaseDetails().getCardToken());
			map().getPurchaseDetails().setCardExpiryDate(source.getPurchaseDetails().getCardExpiryDate());
			map().getPurchaseDetails().setAuthorizationCode(source.getPurchaseDetails().getAuthorizationCode());
			map().getPurchaseDetails().setSpentAmount(source.getPurchaseDetails().getSpentAmount());
			map().getPurchaseDetails().setEpgTransactionId(source.getPurchaseDetails().getEpgTransactionId());
			map().getPurchaseDetails().setUiLanguage(source.getPurchaseDetails().getUiLanguage());
			map().getPurchaseDetails().setPaymentTransactionNo(source.getTransactionNo());
			map().getPurchaseDetails().setPaymentTransactionDate(source.getTransactionDate());
			map().getPurchaseDetails().setExtRefNo(source.getExtRefNo());
			
			map().setCreatedDate(source.getCreatedDate());
			map().setCreatedUser(source.getCreatedUser());
			map().setUpdatedDate(source.getUpdatedDate());
			map().setUpdatedUser(source.getUpdatedUser());
			
		}
	};

	/**
	 * This method is used to get current date in UAE timezone.
	 * @return
	 */
	public static Date getCurrentDateUAETime() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		currentDate.add(Calendar.HOUR_OF_DAY, 4);

		return currentDate.getTime();

	}

}
