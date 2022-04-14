package com.loyalty.marketplace.voucher.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.Code128Constants;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherStatus;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.AccountInfoDto;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.MemberResponseDto;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResultResponse;

public class Utils {

	private Utils() {

	}

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	public static byte[] generateBarCode128(String membershipCode, String barCodeValue) throws VoucherManagementException {
		LOG.debug("generateBarCode128 Start Here ");
		byte[] imageBytes = null;
		Code128Bean bean = new Code128Bean();
		final int dpi = 150;
		bean.setModuleWidth(UnitConv.in2mm(1.8f / dpi)); 
		bean.doQuietZone(false);
		OutputStream out = null;
		String path = membershipCode + "-" + barCodeValue + ".png";
		BitmapCanvasProvider canvas = null;
		canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
		try {
			bean.generateBarcode(canvas, barCodeValue);
			BufferedImage image = canvas.getBufferedImage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			imageBytes = baos.toByteArray();
		} catch (Exception e) {
			LOG.error("generateBarCode128 Exception: ", e);
		}
		LOG.debug("generateBarCode128 response Parameters {}",imageBytes);
		return imageBytes;
	}

	public static byte[] generateBarCode39(String membershipCode, String barCodeValue)
			throws VoucherManagementException {
		LOG.debug("generateBarCode39 Start Here ");
		byte[] imageBytes = null;
		Code39Bean bean = new Code39Bean();

		final int dpi = 300;

		bean.setModuleWidth(UnitConv.in2mm(3.0f / dpi)); // makes the narrow bar
		bean.setWideFactor(3);
		bean.doQuietZone(false);
		OutputStream out = null;
		String path = null;
		path = membershipCode + "-" + barCodeValue + ".png";
		BitmapCanvasProvider canvas = null;
		canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
		try {
			bean.generateBarcode(canvas, barCodeValue);
			BufferedImage image = canvas.getBufferedImage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			imageBytes = baos.toByteArray();
		} catch (Exception e) {
			LOG.error("generateBarCode39 Exception: ", e);
		}
		LOG.debug("generateBarCode39 response Parameters {}",imageBytes);
		return imageBytes;
	}

	public static byte[] generateBarCodeEAN128(String membershipCode, String barCodeValue) {
		if(barCodeValue.length()<12 || !barCodeValue.matches("[0-9]+")) {
			LOG.error("Barcode Value contains characters or length is less than 12!: {}", barCodeValue);
			return null;
		}
		EAN128Bean c = new EAN128Bean();
		  c.setChecksumMode(ChecksumMode.CP_AUTO);
		  c.setOmitBrackets(false);
		  c.setCodeset(Code128Constants.CODESET_C);
		  c.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
		  c.doQuietZone(true);
		  c.setQuietZone(5);
		  c.setFontSize(2d);
		  int dpi = 200;
		  boolean antiAlias = false;
		  int orientation = 0;
		  byte[] imagebyte = null;
		  BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);
		  try {
		  c.generateBarcode(canvas, "12345678912");
		  BufferedImage image = canvas.getBufferedImage();
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  ImageIO.write(image, "jpg", baos);
		  imagebyte = baos.toByteArray();
		  }
		  catch(Exception e) {
			  LOG.error("generateBarCodeAEN128 Exception: ", e);
		  }
		  LOG.debug("generateBarCodeAEN128 response Parameters {}",imagebyte);
		  return imagebyte;
	}

	public static byte[] generateAlphaNumericBarCode128(String membershipCode, String barCodeValue)
			throws VoucherManagementException {
		byte[] imageBytes = null;
		Code128Bean bean = new Code128Bean();
		final int dpi = 150;
		bean.setModuleWidth(UnitConv.in2mm(2.0f / dpi)); // makes the narrow bar
		bean.doQuietZone(false);
		OutputStream out = null;
		String path = membershipCode + "-" + barCodeValue + ".png";
		File outputFile = null;
		BitmapCanvasProvider canvas = null;
		canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
		try {
			bean.generateBarcode(canvas, barCodeValue);
			BufferedImage image = canvas.getBufferedImage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			imageBytes = baos.toByteArray();
		} catch (Exception e) {
			LOG.error("generateAlphaNumericBarCode128 Exception: ", e);
		}
		  LOG.debug("generateAlphaNumericBarCode128 response Parameters {}", imageBytes);
		return imageBytes;
	}

	public static PdfPCell createTextCell(String text, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(align);
		cell.addElement(p);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}

	public static PdfPCell createTextCell2(String[] text, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		for (String current : text) {
			Paragraph p = new Paragraph(current, font);
			p.setAlignment(align);
			cell.addElement(p);
		}
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}
	
	public static void copyFile(ByteArrayInputStream byteArrayInputStream, String outputFile) throws IOException {
		InputStream in = byteArrayInputStream;
		  try(FileOutputStream out = new FileOutputStream(outputFile)) {
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			in.close();			
		}
	}
	
	public static Date currentDate() {

		Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());	
        return currentDate.getTime();
        
	}
	
	public static Date addMinutesToDate(Date date, int minutes) {

		Calendar futureDate = Calendar.getInstance();
        futureDate.setTime(date);
        futureDate.add(Calendar.MINUTE, minutes);	
        return futureDate.getTime();
        
	}

	/**
	 * This method is used to get current date.
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
	
	public static Date getCurrentDateVoucherCountUTCDate() {
		
     	try {

     		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
     	    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
     	    SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
     	    return localDateFormat.parse( simpleDateFormat.format(new Date()) );

		} catch (ParseException e) {
			LOG.info("UTC Date Parse Error - Utils.getCurrentDateVoucherCountGSTOffset");
			e.printStackTrace();
		}
		
     	return null;
     	        
	}
	
	public static Date getCurrentDateVoucherCount() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDate.setTime(new Date());
        return currentDate.getTime();
        
	}
	
	/**
	 * This method is used to get current date + 1 day.
	 * @return
	 */
	public static Date getCurrentDatePlusOne() {

		Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        currentDate.add(Calendar.DATE, 1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
	
        return currentDate.getTime();
        
	}
	
	/**
	 * This method is used to get current date + 7 days.
	 * @return
	 */
	public static Date getDatePlusSevenDays() {

		Calendar futureDate = Calendar.getInstance();
        futureDate.setTime(new Date());
        futureDate.add(Calendar.DATE, VoucherConstants.VOUCHER_EXPIRY_PLUS_7_DAYS);
        futureDate.set(Calendar.HOUR_OF_DAY, 0);
        futureDate.set(Calendar.MINUTE, 0);
        futureDate.set(Calendar.SECOND, 0);
        futureDate.set(Calendar.MILLISECOND, 0);
	
        return futureDate.getTime();
        
	}
	
	/**
	 * This method is used to get current date + 6 days.
	 * @return
	 */
	public static Date getDatePlusSixDays() {
		Calendar futureDate = Calendar.getInstance();
        futureDate.setTime(new Date());
        futureDate.add(Calendar.DATE, 6);
        futureDate.set(Calendar.HOUR_OF_DAY, 0);
        futureDate.set(Calendar.MINUTE, 0);
        futureDate.set(Calendar.SECOND, 0);
        futureDate.set(Calendar.MILLISECOND, 0);
	
        return futureDate.getTime();
        
	}
	
	/**
	 * This method is used to get current date + 8 days.
	 * @return
	 */
	public static Date getDatePlusEightDays() {

		Calendar futureDate = Calendar.getInstance();
        futureDate.setTime(new Date());
        futureDate.add(Calendar.DATE, 8);
        futureDate.set(Calendar.HOUR_OF_DAY, 0);
        futureDate.set(Calendar.MINUTE, 0);
        futureDate.set(Calendar.SECOND, 0);
        futureDate.set(Calendar.MILLISECOND, 0);
	
        return futureDate.getTime();
        
	}
	
	/**
	 * This method is used to get date + minutes.
	 * @param minutes
	 * @return
	 */
	public static Date getDatePlusMinutes(int minutes) {

		Calendar futureDate = Calendar.getInstance();
        futureDate.setTime(new Date());
        futureDate.add(Calendar.DATE, 0);
        futureDate.set(Calendar.HOUR_OF_DAY, 0);
        futureDate.set(Calendar.MINUTE, minutes);
        futureDate.set(Calendar.SECOND, 0);
        futureDate.set(Calendar.MILLISECOND, 0);
	
        return futureDate.getTime();
        
	}
	
	/**
	 * This method is used to retrieve date in format: dd-MM-yyyy
	 * @param expiryDate
	 * @return
	 */
	public static String getFormattedDate(Date expiryDate) {

		SimpleDateFormat formatter = new SimpleDateFormat(VoucherConstants.VOUCHER_EXPIRY_DATE_FORMAT);
		return formatter.format(expiryDate);
        
	}
		
	/**
	 * This method is used to set addition params for voucher expiry push notification.
	 * @param memberDetails
	 * @param voucher
	 * @param offerCatalog
	 * @param expiryDate
	 * @return
	 */
	public static Map<String, String> setVoucherExpiryNotificationParameters(GetListMemberResponseDto memberDetails,
			VoucherDetails voucher, OfferRules offerCatalog, String expiryDate) {

		String offerTitle = null;
		String merchantName = null;
		
		if (validateMemberLanguagePreference(memberDetails) == 1) {
			
			if(null != voucher.getMerchantName()) {
				merchantName = voucher.getMerchantName();
			} else if(null != offerCatalog && null != offerCatalog.getMerchant() && null != offerCatalog.getMerchant().getMerchantName()) {
				merchantName = offerCatalog.getMerchant().getMerchantName().getMerchantNameEn();
			}
			
			if(null != offerCatalog && null != offerCatalog.getOffer() && null != offerCatalog.getOffer().getOfferTitle()) {
				offerTitle = offerCatalog.getOffer().getOfferTitle().getOfferTitleEn();
			}
		
		}
		
		if (validateMemberLanguagePreference(memberDetails) == 2) {
			
			if(null != offerCatalog && null != offerCatalog.getOffer() && null != offerCatalog.getOffer().getOfferTitle()) {
				offerTitle = offerCatalog.getOffer().getOfferTitle().getOfferTitleAr();
			}
				
			if(null != offerCatalog && null != offerCatalog.getMerchant() && null != offerCatalog.getMerchant().getMerchantName()) {
				merchantName = offerCatalog.getMerchant().getMerchantName().getMerchantNameAr();
			}
			
		}
		
		Map<String, String> additionalParam = new HashMap<>();		
		additionalParam.put(VoucherConstants.VOUCHER_EXPIRY_COUPON_NAME, offerTitle);
		additionalParam.put(VoucherConstants.VOUCHER_EXPIRY_VALUE, merchantName);
		additionalParam.put(VoucherConstants.VOUCHER_EXPIRY_EXP_DATE, expiryDate);
		
		return additionalParam;
	
}
	
	/**
	 * This method is used to check if parameter is null.
	 * @param value
	 * @return
	 */
	public static boolean isNotNull(Object value) {
		return null != value;
	}
	
	/**
	 * This method check if member tier is gold, silver or a cobranded card holder.
	 * @param member
	 * @return
	 */
	public static boolean checkMemberTier(GetListMemberResponseDto member) {

		return member.isCoBrandedCard()
				|| (null != member.getTier() && (member.getTier().equalsIgnoreCase(VoucherConstants.MEMBER_TIER_GOLD)
						|| member.getTier().equalsIgnoreCase(VoucherConstants.MEMBER_TIER_SILVER))) ? true : false;

	}
	
	/**
	 * This method check if the given date is exactly after 7 days from today.
	 * @param dateToCheck
	 * @return
	 */
	public static boolean isAfterSevenDays(Date dateToCheck) {
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());  
		currentDate.add(Calendar.DATE, VoucherConstants.VOUCHER_EXPIRY_PLUS_7_DAYS);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0);
		
		Calendar checkDate = Calendar.getInstance();
		checkDate.setTime(dateToCheck);
		checkDate.set(Calendar.HOUR_OF_DAY, 0);  
		checkDate.set(Calendar.MINUTE, 0);  
		checkDate.set(Calendar.SECOND, 0);  
		checkDate.set(Calendar.MILLISECOND, 0);
		
		return currentDate.getTime().equals(checkDate.getTime());
	
	}
	
	/**
	 * This method checks the preferred language for a member.
	 * @param memberDetails
	 * @return
	 */
	public static int validateMemberLanguagePreference(GetListMemberResponseDto memberDetails) {
		
		if (null == memberDetails.getUilanguage() || (null != memberDetails.getUilanguage()
				&& (memberDetails.getUilanguage().equalsIgnoreCase(VoucherConstants.MEMBER_UI_LANG_ENGLISH)
						|| memberDetails.getUilanguage()
								.equalsIgnoreCase(VoucherConstants.MEMBER_UI_LANG_EN)))) {
			return 1;
		}
		
		if (null != memberDetails.getUilanguage()
				&& (memberDetails.getUilanguage().equalsIgnoreCase(VoucherConstants.MEMBER_UI_LANG_ARABIC) || 
						memberDetails.getUilanguage().equalsIgnoreCase(VoucherConstants.MEMBER_UI_LANG_AR))) {
			return 2;
		}
		
		return 3;
		
	}
	
	/**
	 * This method is used to get date to check for expired voucher.
	 * @return
	 */
	public static Date getExpiredVoucherDate() {
		
		Calendar dateToday = Calendar.getInstance(); 
		dateToday.setTime(new Date());  
		dateToday.add(Calendar.DATE, -1);
        dateToday.set(Calendar.HOUR_OF_DAY, 23);  
        dateToday.set(Calendar.MINUTE, 59);  
        dateToday.set(Calendar.SECOND, 59);  
        dateToday.set(Calendar.MILLISECOND, 0);
        
        return dateToday.getTime();
		
	}
	
	/**
	 * This method validates if the given voucher is a gift.
	 * @param voucher
	 * @param accountNumber
	 * @param voucherStatus
	 * @return
	 */
	public static boolean validateIsGift(Voucher voucher, String accountNumber, String voucherStatus) {
		
		if (null != accountNumber && null != voucher.getGiftDetails() && null != voucher.getGiftDetails().getIsGift()
				&& voucher.getGiftDetails().getIsGift().equalsIgnoreCase(VoucherConstants.YES)
				&& accountNumber.equals(voucher.getGiftDetails().getGiftedAccountNumber())
				&& (null != voucherStatus && (voucherStatus.equalsIgnoreCase(VoucherStatus.ACTIVE)
					|| voucherStatus.equalsIgnoreCase(VoucherStatus.EXPIRED)))) {
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * This method is used to set response for list voucher by code API.
	 * @param accountNumber
	 * @param accountExists
	 * @param voucherCode
	 * @param voucherListResponse
	 * @return
	 */
	public static VoucherResultResponse listVoucherByCodeSetResponse(String accountNumber, String voucherCode,
			VoucherResultResponse voucherListResponse) {
	
		if (null != accountNumber && !accountNumber.isEmpty()) {
			voucherListResponse.addErrorAPIResponse(
					VoucherManagementCode.NO_VOUCHER_AVAILABLE_FOR_CODE_ACCOUNT.getIntId(),
					VoucherManagementCode.NO_VOUCHER_AVAILABLE_FOR_CODE_ACCOUNT.getMsg() + voucherCode
							+ VoucherConstants.COMMA_SEPARATOR + accountNumber);
		} else {
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHER_AVAILABLE.getIntId(),
					VoucherManagementCode.NO_VOUCHER_AVAILABLE.getMsg());
		}
		
		voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_SPECIFIC_VOUCHERS_FAILURE.getMsg());
		
		return voucherListResponse;
		
	}

	/**
	 * This method is used to get current date in UAE timezone.
	 * @return
	 */
	public static Date convertDateUAETimezone(Date date) {

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(date);
		currentDate.add(Calendar.HOUR_OF_DAY, 4);

		return currentDate.getTime();

	}
	
	/**
	 * 
	 * @param getMemberResponse
	 * @param accountNumber
	 * @return combined account and member info mapped from response received from member management
	 */
	public static GetMemberResponse getMemberInfo(MemberResponseDto getMemberResponse, String accountNumber) {
		
		GetMemberResponse getMemberDetails = null;
		
		if(null!=getMemberResponse && null!=getMemberResponse.getAccountsInfo()
				&& !getMemberResponse.getAccountsInfo().isEmpty()) {
			
			AccountInfoDto account = getMemberResponse.getAccountsInfo().stream()
			.filter(a->a.getAccountNumber().equals(accountNumber))
			.findAny().orElse(null);
			
			if(null!=account && null!=getMemberResponse.getMemberInfo()) {
				
				getMemberDetails = new GetMemberResponse();
				getMemberDetails.setCustomerType(account.getCustomerType());
				getMemberDetails.setTotalAccountPoints(account.getTotalPoints());
				getMemberDetails.setEligibleFeatures(account.getEligibleFeatures());
				getMemberDetails.setEligiblePaymentMethod(account.getEligiblePaymentMethod());
				getMemberDetails.setAccountId(account.getAccountId());
				getMemberDetails.setChannelId(account.getChannelId());
				getMemberDetails.setFirstName(account.getFirstName());
				getMemberDetails.setLastName(account.getLastName());
				getMemberDetails.setAccountStatus(account.getAccountStatus());
				getMemberDetails.setAgeEligibleFlag(account.isAgeEligibleFlag());
				getMemberDetails.setDob(account.getDob());
				getMemberDetails.setGender(account.getGender());
				getMemberDetails.setAccountNumber(accountNumber);
				getMemberDetails.setSubscribed(StringUtils.equalsIgnoreCase(OfferConstants.TRUE.get(), account.getSubscribtionStatus()));
				getMemberDetails.setNationality(account.getNationality());
				getMemberDetails.setNumberType(account.getNumberType());
				getMemberDetails.setPrimaryAccount(account.isPrimary());
				getMemberDetails.setFirstAccess(account.isFirstAccessFlag());
				getMemberDetails.setEmailVerificationStatus(!ObjectUtils.isEmpty(account.getEmailVerification()) ? Utilities.getStringValueOrNull(account.getEmailVerification().getVerificationStatus()) : null);
				getMemberDetails.setCobrandedCardDetails(account.getCobrandedCardDetails());
				getMemberDetails.setTotalTierPoints(getMemberResponse.getMemberInfo().getTotalPoints());
				getMemberDetails.setTop3Account(Utilities.presentInList(getMemberResponse.getMemberInfo().getTop3Account(), accountNumber));
				getMemberDetails.setMembershipCode(getMemberResponse.getMemberInfo().getMembershipCode());
				getMemberDetails.setTierLevelName(getMemberResponse.getMemberInfo().getTierLevel());
				getMemberDetails.setReferralAccountNumber(!ObjectUtils.isEmpty(account.getReferralBonusAccount())
						 ?account.getReferralBonusAccount().getAccountNumber()
						 : null);
				getMemberDetails.setReferralBonusCode(account.getReferralCode());
				getMemberDetails.setLanguage(account.getLanguage());
				getMemberDetails.setUiLanguage(account.getUilanguage());
				getMemberDetails.setEmail(account.getEmail());
				getMemberDetails.setLastLoginDate(account.getLoginDate());
				getMemberDetails.setPartyId(account.getPartyId());
			}
				
		}
				
		return getMemberDetails;
		
	}
	
	
}
