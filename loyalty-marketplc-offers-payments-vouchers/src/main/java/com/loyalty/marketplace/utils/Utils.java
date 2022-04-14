package com.loyalty.marketplace.utils;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;


public class Utils {
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	private static final String NUMBER = "0123456789";
	private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";
	private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
	private static final String PASSWORD_ALLOW_BASE_SHUFFLE = shuffleString(PASSWORD_ALLOW_BASE);
	private static final String PASSWORD_ALLOW = PASSWORD_ALLOW_BASE_SHUFFLE;
	
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	
	private static Random rnd = new Random();

	public static String removeSpecialChars(String input) {
		if (input != null) {
			input = input.replaceAll("[\\x00-\\x09]", "");
			input = input.replaceAll("[\\x11-\\x19]", "");
			input = input.replaceAll("[<>\\\\={}^]", "");
		}
		return input;
	}

	public static String generateRandomPassword(int length) {
		SecureRandom random = new SecureRandom();
		if (length < 1)
			throw new IllegalArgumentException();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {

			int rndCharAt = random.nextInt(PASSWORD_ALLOW.length());
			char rndChar = PASSWORD_ALLOW.charAt(rndCharAt);
			sb.append(rndChar);

		}

		return sb.toString();

	}

	public static String shuffleString(String string) {
		List<String> letters = Arrays.asList(string.split(""));
		Collections.shuffle(letters);
		return letters.stream().collect(Collectors.joining());
	}
	
	
	
	public static Integer generateRandomNumber() {
		return 100000 + rnd.nextInt(900000);
	}
	
	

	public static void callServiceLog(String externalTransactionId, String json, String json2, String action, String accountNumber,
			String status, String userName) {
		// TODO Auto-generated method stub
		ServiceCallLogsDto callLog = new ServiceCallLogsDto();
		callLog.setAccountNumber(accountNumber);
		callLog.setAction(action);
		callLog.setCreatedDate(new Date());
		callLog.setCreatedUser(userName);
		callLog.setRequest(json2);
		callLog.setResponse(json);
		callLog.setStatus(status);
		callLog.setTransactionId(externalTransactionId);
		JmsTemplate template  = new JmsTemplate();
		LOG.info("Saving Service Log");
		template.convertAndSend("serviceCallLogQueue", callLog);
	}
	
	public static boolean checkRoleExists(String userRole, String role) {
		try {
			if (!StringUtils.isEmpty(userRole)) {
				String test = (userRole.replaceAll("\\\\", "")).replaceAll("\"", "");
				test = test.substring(1, test.length() - 1);
				String[] roles = test.split(",");
				for (String string : roles) {
					if (string.trim().equals(role)) {
						LOG.info("Role exists : {}", string);
						return true;
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Error while checking the role exists",e.getMessage());
		}
		return false;
	}
	
	public static boolean checkRolesExists(String userRole, List<String> roles) {
		try {
			if (!StringUtils.isEmpty(userRole)) {
				String test = (userRole.replaceAll("\\\\", "")).replaceAll("\"", "");
				test = test.substring(1, test.length() - 1);
				String[] userRoles = test.split(",");
				for (String role : roles) {
					for (String tokenRole : userRoles) {
					if (tokenRole.trim().equals(role)) {
						LOG.info("Role exists{}", role);
						return true;
					}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Error while checking the role exists",e.getMessage());
		}
		return false;
	}
	
	public static boolean validateFileFormat(MultipartFile file) {
		return file != null && !file.isEmpty() && file.getContentType() != null
				&& (file.getContentType().contains(VoucherConstants.TEXT_CSV) 
						|| file.getContentType().contains(VoucherConstants.APP_OCTET_STREAM)
						|| file.getContentType().contains(VoucherConstants.APP_MS_EXCEL));
	}


	public static PurchaseRequestDto getFreePurchaseRequest(OfferGiftValues offerValues, Headers headers, String accountNumber) {
		
		PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setUiLanguage(OffersConfigurationConstants.LANGUAGE_EN);
		purchaseRequestDto.setCouponQuantity(!ObjectUtils.isEmpty(offerValues.getCouponQuantity())
				? offerValues.getCouponQuantity():1);
		purchaseRequestDto.setOfferId(offerValues.getOfferId());
		purchaseRequestDto.setAccountNumber(accountNumber);
		purchaseRequestDto.setSelectedPaymentItem(ProcessValues.getPurchaseItemFromOfferType(offerValues.getOfferType()));
		purchaseRequestDto.setSelectedOption(OffersConfigurationConstants.fullPoints);
		purchaseRequestDto.setPaymentType(OffersConfigurationConstants.fullPoints);
		purchaseRequestDto.setSpentAmount(0.0);
		purchaseRequestDto.setSpentPoints(0);
		purchaseRequestDto.setExtTransactionId(headers.getExternalTransactionId());
		
		if(Checks.checkIsCashVoucher(offerValues.getOfferType())) {
			
			purchaseRequestDto.setVoucherDenomination(offerValues.getDenomination());
		}
		
		if(Checks.checkIsDealVoucher(offerValues.getOfferType())) {
			purchaseRequestDto.setSubOfferId(offerValues.getSubOfferId());
		}
		
		return purchaseRequestDto;
	}

	/***
	 * 
	 * @param headers
	 * @param action
	 * @param responseTime
	 * @param isOutbound
	 * @param status
	 * @param request
	 * @param response
	 */
	public static ServiceCallLogsDto createServiceCallLogsDto(Headers headers, String action, Long responseTime, boolean isOutbound, boolean isSuccess,
			Object request, Object response) {
		
		ServiceCallLogsDto serviceCallLogsDto = null;
		
		try {
			serviceCallLogsDto = new ServiceCallLogsDto();
			serviceCallLogsDto.setCreatedDate(new Date());
			serviceCallLogsDto.setAction(action);
			serviceCallLogsDto.setServiceType(isOutbound 
					? MarketplaceConfigurationConstants.OUTBOUND : MarketplaceConfigurationConstants.INBOUND);
			serviceCallLogsDto.setRequest(request);
			serviceCallLogsDto.setResponse(response);
			serviceCallLogsDto.setStatus(isSuccess
					? MarketplaceConfigurationConstants.STATUS_SUCCESS
					: MarketplaceConfigurationConstants.STATUS_FAILED);
			serviceCallLogsDto.setResponseTime(responseTime);
			
			if(!ObjectUtils.isEmpty(headers)) {
				
				serviceCallLogsDto.setTransactionId(headers.getExternalTransactionId());
				serviceCallLogsDto.setCreatedUser(headers.getUserName());
			}
			
		} catch(Exception e){
			
			LOG.error(MarketPlaceCode.FAILED_TO_CREATE_SERVICE_CALL_LOG_OBJECT.getId(),
					MarketPlaceCode.FAILED_TO_CREATE_SERVICE_CALL_LOG_OBJECT.getMsg());
			
		}
		
		return serviceCallLogsDto;
	}


	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String changeDateToString(Date date) {
		
		if(ObjectUtils.isEmpty(date)) {
			date = Calendar.getInstance().getTime();
		}	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		return dateFormat.format(date);
	}
	
}

