package com.loyalty.marketplace.voucher.inbound.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.gifting.domain.GiftingHistoryDomain;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingHistoryRepository;
import com.loyalty.marketplace.gifting.outbound.dto.GiftingHistoryResult;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.offers.promocode.domain.PromoCodeDomain;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PromoCodeDetails;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PromoCodeRequest;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.carrefour.outbound.dto.CarrefourGCRequestResponse;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.constants.VoucherStatus;
import com.loyalty.marketplace.voucher.domain.PendingFreeVouchersInfoDomain;
import com.loyalty.marketplace.voucher.domain.ReconciliationDataInfoDomain;
import com.loyalty.marketplace.voucher.domain.ReconciliationInfoDomain;
import com.loyalty.marketplace.voucher.domain.VoucherDomain;
import com.loyalty.marketplace.voucher.domain.VoucherReconciliationDataDomain;
import com.loyalty.marketplace.voucher.domain.VoucherReconciliationDomain;
import com.loyalty.marketplace.voucher.domain.VoucherUploadFileDomain;
import com.loyalty.marketplace.voucher.inbound.dto.ListVoucherRequest;
import com.loyalty.marketplace.voucher.inbound.dto.RollBackVoucherBurnRequest;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherRequestDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherUploadDto;
import com.loyalty.marketplace.voucher.maf.outbound.dto.PlaceVoucherOrderResponse;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.EnrollmentResultResponse;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.GetListMemberResponse;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.MemberResponseDto;
import com.loyalty.marketplace.voucher.outbound.database.entity.BurntInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.CashVoucherBurntInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.OfferInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.PendingFreeVouchersInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.ReconciliationDataInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherReconciliation;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherReconciliationData;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherUploadFile;
import com.loyalty.marketplace.voucher.outbound.database.repository.PendingFreeVouchersInfoRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherReconciliationDataRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherReconciliationRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepositoryHelper;
import com.loyalty.marketplace.voucher.outbound.dto.BurnVoucher;
import com.loyalty.marketplace.voucher.outbound.dto.FileContentDto;
import com.loyalty.marketplace.voucher.outbound.dto.FreeVoucherHandbackFileDto;
import com.loyalty.marketplace.voucher.outbound.dto.HandbackFileDto;
import com.loyalty.marketplace.voucher.outbound.dto.ListVoucherStatusResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherGiftDetailsResult;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherListResult;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherReconciliationDtoResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResult;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResultResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherUploadResponse;
import com.loyalty.marketplace.voucher.outbound.service.CarreFourService;
import com.loyalty.marketplace.voucher.outbound.service.MafService;
import com.loyalty.marketplace.voucher.outbound.service.SMLSService;
import com.loyalty.marketplace.voucher.outbound.service.VoucherMemberManagementService;
import com.loyalty.marketplace.voucher.outbound.service.VoucherPartnerService;
import com.loyalty.marketplace.voucher.outbound.service.VoucherPointsBankService;
import com.loyalty.marketplace.voucher.outbound.service.YgagService;
import com.loyalty.marketplace.voucher.service.VoucherService;
import com.loyalty.marketplace.voucher.smls.outbound.dto.DownloadSMLSVoucherResponse;
import com.loyalty.marketplace.voucher.utils.OfferRules;
import com.loyalty.marketplace.voucher.utils.Utils;
import com.loyalty.marketplace.voucher.utils.VoucherDetails;
import com.loyalty.marketplace.voucher.utils.VoucherIDGenerator;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.loyalty.marketplace.voucher.ygag.outbound.dto.DownloadYGAGVoucherResponse;

@Component
@RefreshScope
public class VoucherControllerHelper {

	private static final Logger LOG = LoggerFactory.getLogger(VoucherControllerHelper.class);
	private static final String OK_NO_ERROR = "OK;None";
	private static final String LOG_VOUCHER = "voucher : {}";
	
	@Autowired
	VoucherService voucherService;

	@Autowired
	MemberManagementService memberManagementService;

	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	PendingFreeVouchersInfoRepository pendingFreeVouchersInfoRepository;

	@Autowired
	VoucherPartnerService partnerService;

	@Autowired
	FetchServiceValues fetchServiceValues;

	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired
	VoucherRepositoryHelper voucherRepositoryHelper;

	@Autowired
	PurchaseRepository purchaseRepository;

	@Autowired
	VoucherReconciliationDomain voucherReconciliationDomain;

	@Autowired
	VoucherReconciliationDataDomain voucherReconciliationDataDomain;

	@Autowired
	VoucherReconciliationRepository voucherReconciliationRepository;

	@Autowired
	VoucherReconciliationDataRepository voucherReconciliationDataRepository;

	@Autowired
	private GiftingHistoryRepository giftingHistoryRepository;

	@Autowired
	VoucherPointsBankService voucherPointsBankService;

	@Autowired
	private VoucherMemberManagementService voucherMemberManagementService;

	@Autowired
	VoucherUploadFileDomain voucherUploadFileDomain;

	@Autowired
	VoucherDomain vouDomain;

	@Autowired
	GiftingHistoryDomain giftingHistoryDomain;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	MongoOperations mongoOperations;

	@Autowired
	private MafService mafService;

	@Autowired
	private CarreFourService carreFourService;

	@Autowired
	private YgagService ygagService;

	@Autowired
	StoreRepository storeRepository;

	@Autowired
	private OffersHelper offersHelper;
	
	@Autowired
	private PendingFreeVouchersInfoDomain pendingFreeVouchersInfoDomain;
	
	@Autowired
	private SMLSService smlsService;
	
	@Autowired
	PromoCodeDomain promoCodeDomain;
	
	@Autowired
	ExceptionLogsService exceptionLogService;

	@Value("${cashVoucher.period}")
	private Integer cashVoucherPeriod;

	@Value("${discountVoucher.period}")
	private Integer discountVoucherPeriod;

	@Value("${dealVoucher.period}")
	private Integer dealVoucherPeriod;

	@Value("${role.admin}")
	private String adminRole;

	@Value("#{'${voucher.statistics.contact.numbers}'.split(',')}")
	private List<String> voucherStatsContacts;

	@Value("#{'${voucher.count.contact.numbers}'.split(',')}")
	private List<String> voucherCountContacts;

	@Value("${voucher.count.threshold.percentage}")
	private String voucherCountThreshold;

	@Value("${tibco.crfr.genCode}")
	private String genCode;

	@Value("${free.voucher.login.limit}")
	private Integer freeVoucherLoginLimit;
	
	@Value("${free.voucher.expiry.limit}")
	private Integer expiryForVoucher;
	
	@Value("${freeVoucher.fileUpload.location}")
	private String freeVoucherFileUploadLocation;
	
	/**
	 * This method generates a voucher code
	 * @param offerType
	 * @param merchantCode
	 * @return voucher code
	 */
	public String getVoucherCode(String offerType, String merchantCode) {
		VoucherIDGenerator voucherIDGenerator = new VoucherIDGenerator(36, 6);
		String resultStr = String.valueOf(voucherIDGenerator.generateOid());
		String resultStr1 = String.valueOf(voucherIDGenerator.generateOid());
		if (offerType.contains(VoucherConstants.DISCOUNT)) {
			return merchantCode.substring(0, 2) + "C" + resultStr;
		} else if (offerType.contains("Deal")) {
			return merchantCode.substring(0, 2) + "D" + resultStr;
		}

		return resultStr + resultStr1.substring(2);
	}

	/**
	 * This method gets voucher expiry date for a specific offer type
	 * @param offerType
	 * @param expiryPeriod
	 * @return expiry date
	 */
	public Date getVoucherExpirydate(String offerType, Integer expiryPeriod) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if (null != expiryPeriod && expiryPeriod > 0) {
			cal.add(Calendar.DAY_OF_YEAR, expiryPeriod);
			return cal.getTime();
		}
		if (offerType.equals(VoucherConstants.CASH_OFFER_ID)) {
			cal.add(Calendar.DAY_OF_YEAR, cashVoucherPeriod);
		} else if (offerType.equals(VoucherConstants.DISCOUNT_OFFER_ID)) {
			cal.add(Calendar.DAY_OF_YEAR, discountVoucherPeriod);
		} else if (offerType.equals(VoucherConstants.DEAL_OFFER_ID)) {
			cal.add(Calendar.DAY_OF_YEAR, dealVoucherPeriod);
		}
		return cal.getTime();

	}

	/**
	 * This method gets membership code for a given account number
	 * @param accountNumber
	 * @param headers 
	 * @return membership code
	 * @throws MarketplaceException 
	 */
	public String getMemberShipCode(String accountNumber, Headers headers) throws MarketplaceException {
		ResultResponse result = new ResultResponse(null);
		GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, result, headers); //Add headers instead of null
		if (null == memberDetails) {
			return null;
		}
		return memberDetails.getMembershipCode();
	}

	/**
	 * This method generates a pdf with voucher code
	 * @param voucher
	 * @param offerTitleDescription
	 * @return pdf data
	 * @throws VoucherManagementException
	 */
	public ByteArrayInputStream generatePDF(Voucher voucher, String offerTitleDescription)
			throws VoucherManagementException {

		Font graySmallFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.GRAY);
		Font grayVerySmallFont1 = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);

		Font catFont2 = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);

		try {
			Document document = new Document();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, out);
			document.open();
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

			// Get the date
			java.util.Date today = Calendar.getInstance().getTime();
			String reportDate = df.format(today);
			Paragraph paragraph = new Paragraph(reportDate, graySmallFont);

			document = addParagraphData(document, voucher, offerTitleDescription, graySmallFont, paragraph);
			document = addBarcodeData(document, voucher);

			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			String paraGraph = "This is an e-voucher. Simply present it at the" + System.lineSeparator()
					+ " selected Etisalat's redeeming partner";
			Paragraph paragraph5 = new Paragraph(paraGraph, graySmallFont);
			paragraph5.setAlignment(Element.ALIGN_CENTER);

			document.add(paragraph5);
			document.newPage();

			DottedLineSeparator dottedline2 = new DottedLineSeparator();
			dottedline2.setOffset(-27);
			dottedline2.setGap(0.02f);
			dottedline2.setLineColor(BaseColor.GRAY);
			paragraph.add(dottedline2);
			document.add(paragraph);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			Paragraph coupon2 = new Paragraph("TERMS & CONDITIONS", catFont2);
			coupon2.setAlignment(Element.ALIGN_CENTER);
			document.add(coupon2);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			paragraph = new Paragraph(VoucherConstants.TANDC, grayVerySmallFont1);
			paragraph.setIndentationLeft(100);
			paragraph.setIndentationRight(150);

			PdfPTable table = new PdfPTable(1);
			PdfPCell cell = new PdfPCell(paragraph);
			cell.setBorder(Rectangle.NO_BORDER);
			table.setTotalWidth(288);
			table.addCell(cell);
			document.add(paragraph);
			document.close();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "generatePDF", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}

	}

	private Document addBarcodeData(Document document, Voucher voucher)
			throws IOException, VoucherManagementException, DocumentException {
		Image barCode = null;
		if (voucher.getBarcodeType() != null && voucher.getBarcodeType().getName().trim().equalsIgnoreCase("Code128")) {
			if (voucher.getType() != null && voucher.getType().contains(VoucherConstants.DISCOUNT)) {
				barCode = Image.getInstance(
						Utils.generateAlphaNumericBarCode128(voucher.getMembershipCode(), voucher.getVoucherCode()));
				barCode.setAbsolutePosition(150f, 280f);
				barCode.scaleToFit(330f, 150f);
			} else {
				barCode = Image
						.getInstance(Utils.generateBarCode128(voucher.getMembershipCode(), voucher.getVoucherCode()));
				barCode.setAbsolutePosition(180f, 280f);
				barCode.scaleToFit(330f, 150f);
			}
			document.add(barCode);
		} else if (voucher.getBarcodeType() != null
				&& voucher.getBarcodeType().getName().trim().equalsIgnoreCase("EAN128")) {
			barCode = Image
					.getInstance(Utils.generateBarCodeEAN128(voucher.getMembershipCode(), voucher.getVoucherCode()));
			barCode.setAbsolutePosition(150f, 300f);
			barCode.scaleToFit(330f, 150f);
			document.add(barCode);
		} else {
			if (voucher.getType() != null && voucher.getType().contains(VoucherConstants.DISCOUNT)) {
				barCode = Image.getInstance(
						Utils.generateAlphaNumericBarCode128(voucher.getMembershipCode(), voucher.getVoucherCode()));
				barCode.setAbsolutePosition(180f, 280f);
				barCode.scaleToFit(330f, 150f);
			} else {
				barCode = Image
						.getInstance(Utils.generateBarCode39(voucher.getMembershipCode(), voucher.getVoucherCode()));
				barCode.setAbsolutePosition(150f, 300f);
				barCode.scaleToFit(330f, 150f);
			}

			document.add(barCode);
		}
		return document;
	}

	private Document addParagraphData(Document document, Voucher voucher, String offerTitleDescription,
			Font graySmallFont, Paragraph paragraph) throws DocumentException {
		Font catFont = new Font(Font.FontFamily.HELVETICA, 32, Font.NORMAL);
		Font smallBold = new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL);
		Font orangeFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.ORANGE);
		DateFormat df2 = new SimpleDateFormat("dd MMM yyyy");

		String voucherOrCoupon = "";

		paragraph.setAlignment(Element.ALIGN_RIGHT);
		DottedLineSeparator dottedline = new DottedLineSeparator();
		dottedline.setOffset(-27);
		dottedline.setGap(0.02f);
		dottedline.setLineColor(BaseColor.GRAY);
		paragraph.add(dottedline);

		document.add(paragraph);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);

		if (voucher.getType().contains("discount")) {
			voucherOrCoupon = "COUPON";
		} else {
			voucherOrCoupon = "VOUCHER";
		}
		Paragraph coupon = new Paragraph("YOUR " + voucherOrCoupon, catFont);
		coupon.setAlignment(Element.ALIGN_CENTER);
		document.add(coupon);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);

		Paragraph paragraph1 = new Paragraph(String.valueOf(voucher.getVoucherAmount()) + " AED", smallBold);
		paragraph1.setAlignment(Element.ALIGN_CENTER);

		document.add(paragraph1);
		document.add(Chunk.NEWLINE);
		String para = null;
		if (voucher.getType().equalsIgnoreCase("Deal Offer") && offerTitleDescription != null) {
			para = offerTitleDescription;
		} else {
			para = "This " + voucher.getVoucherAmount() + " AED voucher can be used with any " + System.lineSeparator()
					+ "purchase";
		}
		Paragraph paragraph2 = new Paragraph(para, graySmallFont);
		paragraph2.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph2);
		document.add(Chunk.NEWLINE);
		String voucherExpiryDate = df2.format(voucher.getExpiryDate());
		Paragraph paragraph3 = new Paragraph("Valid till " + voucherExpiryDate, orangeFont);
		paragraph3.setAlignment(Element.ALIGN_CENTER);

		document.add(paragraph3);
		return document;
	}

	/**
	 * This method validates the uploaded voucher file content
	 * @param uploadFileContent
	 * @param token
	 * @return data feedback file
	 * @throws ParseException
	 * @throws VoucherManagementException
	 */
	public StringBuilder validateVoucherFile(String uploadFileContent, OfferCatalog offerCatalog, Headers header)
			throws ParseException, VoucherManagementException {

		boolean valid = true;
		StringBuilder dataFeedback = new StringBuilder().append(uploadFileContent).append(";");
		String[] contentStringArray = new String[9];
		String[] intermediate = uploadFileContent.split(";");
		System.arraycopy(intermediate, 0, contentStringArray, 0, intermediate.length);

		dataFeedback.append(VoucherConstants.HANDBACK_FILE_ERROR);
		
		//Commented for Time Out by Arindam
//		LOG.info("OfferId in the upload file : {}", contentStringArray[5]);
//		OfferCatalog off = offerRepository.findByOfferId(contentStringArray[5]);
//		if(off==null ) {
//			dataFeedback.append("Offer id does not exist,");
//			valid = false;
//		}
//		else {
			valid = validateDatesInVoucherFile(contentStringArray, dataFeedback);
//			valid = validatePartnerInVoucherFile(contentStringArray, dataFeedback, valid, off,header);
			valid = validateMerchantCodeInVoucherFile(contentStringArray, dataFeedback, valid, header);
			valid = validateOfferSubOfferPartnerInVoucherFile(contentStringArray, dataFeedback, valid, offerCatalog);
//			valid = validateVoucherCodeInVoucherFile(contentStringArray, dataFeedback, valid);
//		}
		if (valid) {
			dataFeedback.delete(dataFeedback.indexOf(VoucherConstants.HANDBACK_FILE_ERROR), dataFeedback.length());
			dataFeedback.append(OK_NO_ERROR);
		}
		return dataFeedback;
	}

	private boolean validateVoucherCodeInVoucherFile(String[] contentStringArray, StringBuilder dataFeedback,
			boolean valid) {
		Voucher vou = voucherRepository.findByVoucherCode(contentStringArray[0]);
		if (null != vou) {
			dataFeedback.append("Voucher code is not unique,");
			valid = false;
		}

		if (contentStringArray[0] == null || contentStringArray[0].isEmpty()) {
			dataFeedback.append("Voucher code is required,");
			valid = false;
		}		
		return valid;
	}

	private boolean validatePartnerInVoucherFile(String[] contentStringArray, StringBuilder dataFeedback, boolean valid,
			OfferCatalog off, Headers header) throws VoucherManagementException {
		if (contentStringArray[3] == null || contentStringArray[3].isEmpty()) {
			dataFeedback.append("Merchant code is required,");
			valid = false;
		}

		boolean exists;
		exists = partnerService.checkPartnerTypeExists(off.getPartnerCode(),header);
		if (!exists) {
			dataFeedback.append("Partner is not a redemption type,");
			valid = false;
		}
		return valid;
	}
	
	private boolean validateMerchantCodeInVoucherFile(String[] contentStringArray, StringBuilder dataFeedback, boolean valid,
			Headers header) throws VoucherManagementException {
		if (contentStringArray[3] == null || contentStringArray[3].isEmpty()) {
			dataFeedback.append("Merchant code is required,");
			valid = false;
		}

		return valid;
	}

	private boolean validateOfferSubOfferPartnerInVoucherFile(String[] contentStringArray, StringBuilder dataFeedback,
			boolean valid, OfferCatalog off) {

		if (contentStringArray[5] == null || contentStringArray[5].isEmpty()) {
			dataFeedback.append("Offer id is required,");
			valid = false;
		}

		if (!off.getVoucherInfo().getVoucherAction().equalsIgnoreCase(VoucherConstants.SEARCH)) {
			dataFeedback.append("Offer id is not flagged for pre loaded codes,");
			valid = false;
		}

		if (!off.getMerchant().getMerchantCode().equalsIgnoreCase(contentStringArray[3])) {
			dataFeedback.append("Offer does not belong to merchant,");
			valid = false;
		}

		if (off.getOfferType().getOfferTypeId()
				.equalsIgnoreCase(VoucherConstants.DEAL_OFFER_ID)) {
			if (contentStringArray[8] == null) {
				dataFeedback.append("Sub Offer is required,");
				valid = false;
			} else if ((off.getSubOffer() == null) || ((off.getSubOffer() != null) && (off.getSubOffer().stream()
					.noneMatch(e -> e.getSubOfferId().equalsIgnoreCase(contentStringArray[8]))))) {
				dataFeedback.append("Sub Offer does not belong to offer,");
				valid = false;

			}
		} else if ((off.getOfferType().getOfferTypeId()
				.equalsIgnoreCase(VoucherConstants.CASH_OFFER_ID))
				&& (contentStringArray[2] == null || contentStringArray[2].isEmpty())) {
			dataFeedback.append("Denomination is required for cash vouchers,");
			valid = false;
		}
		return valid;
	}

	private boolean validateDatesInVoucherFile(String[] contentStringArray, StringBuilder dataFeedback)
			throws ParseException {
		boolean valid = true;
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.set(Calendar.HOUR_OF_DAY, 0);  
		now.set(Calendar.MINUTE, 0);  
		now.set(Calendar.SECOND, 0);  
		now.set(Calendar.MILLISECOND, 0);  
		LOG.info("Date now : {}", now);		
		
		Date startDate=null;
		if (contentStringArray[6] != null && !contentStringArray[6].isEmpty()) {
			startDate = new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(contentStringArray[6]);
		}
		Date endDate = null;
		if (contentStringArray[7] != null && !contentStringArray[7].isEmpty()) {
			endDate = new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(contentStringArray[7]);
		}

		Date expiryDate = null;
		if (contentStringArray[4] != null && !contentStringArray[4].isEmpty()) {
			expiryDate = new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(contentStringArray[4]);
		}		

		LOG.info("Start date  : {}", startDate);
		if ((startDate != null) && (startDate.before(now.getTime()))) {
			dataFeedback.append("Start date is past date,");
			valid = false;
		}
		
		LOG.info("End date  : {}", endDate);
		if ((endDate != null) && (endDate.before(now.getTime()))) {
			dataFeedback.append("End date is past date,");
			valid = false;
		}
		
		LOG.info("Expiry date  : {}", expiryDate);
		if (expiryDate != null && expiryDate.before(now.getTime())) {
			dataFeedback.append("Expiry date is past date,");
			valid = false;
		}
		return valid;
	}

	private Function<String, VoucherUploadDto> mapToItem = line -> {
		String[] intermediate = line.split(";");
		String[] contentStringArray = new String[9];

		System.arraycopy(intermediate, 0, contentStringArray, 0, intermediate.length);

		VoucherUploadDto item = new VoucherUploadDto();
		item.setVoucherCode(contentStringArray[0]);

		try {
			item.setUploadDate((contentStringArray[1] == null || contentStringArray[1].isEmpty()) ? null
					: new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(contentStringArray[1]));
			
			item.setStartDate((contentStringArray[6] == null || contentStringArray[6].isEmpty())
					? new Date()
					: new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(contentStringArray[6]));
			item.setEndDate((contentStringArray[7] == null || contentStringArray[7].isEmpty()) ? null
					: new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(contentStringArray[7]));
			item.setExpiryDate((contentStringArray[4] == null || contentStringArray[4].isEmpty()) ? null
					: new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(contentStringArray[4]));
			item.setSubOfferId(contentStringArray[8]);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("ParseException");
		}
		if (contentStringArray[2] == null || contentStringArray[2].isEmpty()) {
			item.setDenomination(null);
		} else {
			item.setDenomination(Double.parseDouble(contentStringArray[2]));
		}
		item.setMerchantCode(contentStringArray[3]);
		item.setOfferId(contentStringArray[5]);

		LOG.info("Voucher upload input list : {}",item);
		return item;
	};

	/**
	 * This method validates date to check if it is in the right format
	 * @param fromDate
	 * @param toDate
	 * @param resultResponse
	 * @return true or false
	 */
	public boolean validateBurnReportDates(String fromDate, String toDate, VoucherResultResponse resultResponse) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.parse(fromDate);
			format.parse(toDate);
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.DATE_FORMAT_EXCEPTION.getIntId(),
					VoucherManagementCode.DATE_FORMAT_EXCEPTION.getMsg());
			return false;
		}
		return true;
	}

	/**
	 * This method converts burnt voucher to file content
	 * @param fileContent
	 * @param vouchersList
	 */
	public void convertBurntVoucherToFileContent(StringBuilder fileContent, List<Voucher> vouchersList) {
		LOG.info("Before formatting file content");
		
		final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
		final DateTimeFormatter outputFormat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		vouchersList.stream().forEach(c -> {			
			String str = null != c.getDownloadedDate() ? c.getDownloadedDate().toString() : c.getCreatedDate().toString();			
			String str1 = c.getBurntInfo() != null ? c.getBurntInfo().getVoucherBurntDate().toString() : null;
			String str2 = c.getExpiryDate().toString();		
			final ZonedDateTime cretaedDate = ZonedDateTime.parse(str, inputFormat);
			final ZonedDateTime burntDate = ZonedDateTime.parse(str1, inputFormat);
			final ZonedDateTime expiredDate = ZonedDateTime.parse(str2, inputFormat);
			
			fileContent.append("\r\n");
			fileContent.append(c.getVoucherCode() + VoucherConstants.COMMA + c.getStatus() + VoucherConstants.COMMA
					+ c.getPartnerCode() + VoucherConstants.COMMA + c.getMerchantCode() + VoucherConstants.COMMA
					+ c.getOfferInfo().getOffer() + VoucherConstants.COMMA + c.getBurntInfo().getVoucherBurntUser()
					+ VoucherConstants.COMMA + c.getMerchantInvoiceId() + VoucherConstants.COMMA + c.getVoucherAmount()
					+ VoucherConstants.COMMA + outputFormat1.format(cretaedDate) + VoucherConstants.COMMA
					+ outputFormat1.format(burntDate) + VoucherConstants.COMMA + "Y" + VoucherConstants.COMMA
					+ c.getBurntInfo().getVoucherBurntId() + VoucherConstants.COMMA + outputFormat1.format(expiredDate)
					+ VoucherConstants.COMMA + "N");
		});
		LOG.info("After formatting file content");
	}

	/**
	 * This method gets voucher data for a specific partner for a time period
	 * @param fromDate
	 * @param toDate
	 * @return list of vouchers
	 * @throws ParseException
	 */
	public List<Voucher> getVoucherDataFromLoyaltyForPartner(Date fromDate, Date toDate) throws ParseException {
		return voucherRepository.findByPartnerCodeAndCreatedDate(VoucherConstants.MAF, fromDate, toDate);
	}

	/**
	 * This method saves voucher reconciliation information
	 * @param orders
	 * @param totalAmountFromPartner
	 * @param voucherListInLoyalty
	 * @param totalAmountInLoyalty
	 * @param program
	 * @param fromDate
	 * @param toDate
	 * @param userName
	 * @param reconciliationRefId
	 * @param dateToSave
	 * @param externalTransactionId
	 * @return id from saved collection
	 * @throws ParseException
	 * @throws VoucherManagementException
	 */
	public String saveVoucherToReconcile(List<Orders> orders, double totalAmountFromPartner,
			List<Voucher> voucherListInLoyalty, double totalAmountInLoyalty, String program, Date fromDate,
			Date toDate, String userName, String reconciliationRefId, Date dateToSave, String externalTransactionId)
			throws VoucherManagementException {

		VoucherReconciliationDataDomain refId = null;
		if (reconciliationRefId != null) {
			refId = new VoucherReconciliationDataDomain.VoucherReconciliationDataBuilder(null, null, null, null, null,
					null, null, null, null, reconciliationRefId).build();

		}

		ReconciliationInfoDomain loyalty = new ReconciliationInfoDomain.ReconciliationInfoBuilder(
				voucherListInLoyalty.size(), totalAmountInLoyalty).build();

		ReconciliationInfoDomain partner = new ReconciliationInfoDomain.ReconciliationInfoBuilder(
				orders.get(0)==null? 0:Integer.valueOf(orders.size()), Double.valueOf(totalAmountFromPartner)).build();
		
		VoucherReconciliationDomain voucherReconcileDomain;
		voucherReconcileDomain = new VoucherReconciliationDomain.VoucherReconciliationBuilder(program,
				VoucherConstants.MAF, fromDate, toDate, loyalty, partner, refId, dateToSave,
				userName, dateToSave, userName).build();
		return voucherReconciliationDomain.saveVoucherReconcile(voucherReconcileDomain, externalTransactionId,
				userName);

	}

	/**
	 * This method saves voucher difference in reconciliation collection
	 * @param orders
	 * @param voucherCodesFromPartner
	 * @param voucherListInLoyalty
	 * @param voucherCodesFromLoyalty
	 * @param program
	 * @param userName
	 * @param partnerTransactionId
	 * @param dateToSave
	 * @param externalTransactionId
	 * @returns the collection id
	 * @throws VoucherManagementException
	 */
	public String saveVoucherIndifferenceToReconcileData(List<Orders> orders, Set<String> voucherCodesFromPartner,
			List<Voucher> voucherListInLoyalty, Set<String> voucherCodesFromLoyalty, String program, String userName,
			String partnerTransactionId, Date dateToSave, String externalTransactionId)
			throws VoucherManagementException {
		SimpleDateFormat format = new SimpleDateFormat(VoucherConstants.RECONCILE_DATE_FORMAT_FROM_PARTNER);
		List<Orders> partnerReconciliationData;
		partnerReconciliationData = orders.get(0) == null ? new ArrayList<>()
				: orders.stream().filter(e -> !voucherCodesFromLoyalty.contains(e.getVoucherID()))
						.collect(Collectors.toList());

		List<Voucher> loyaltyReconciliationData;
		loyaltyReconciliationData = voucherListInLoyalty == null ? new ArrayList<>()
				: voucherListInLoyalty.stream().filter(e -> !voucherCodesFromPartner.contains(e.getVoucherCode()))
						.collect(Collectors.toList());

		String reconciliationLevel = getReconciliationLevel(partnerReconciliationData, loyaltyReconciliationData);

		List<ReconciliationDataInfoDomain> loyaltyData = new ArrayList<>();
		if (!loyaltyReconciliationData.isEmpty() && loyaltyReconciliationData.size() > 0) {
			loyaltyReconciliationData.stream().forEach(lData -> {
				ReconciliationDataInfoDomain reconcile = new ReconciliationDataInfoDomain.ReconciliationDataInfoBuilder(
						lData.getPartnerReferNumber(), null, lData.getVoucherCode(), VoucherConstants.MAF,
						lData.getPartnerTransactionId(), partnerTransactionId, lData.getVoucherAmount(),
						lData.getDownloadedDate()).build();
				loyaltyData.add(reconcile);
			});
		}

		List<ReconciliationDataInfoDomain> partnerData = new ArrayList<>();
		if (!partnerReconciliationData.isEmpty() && partnerReconciliationData.size() > 0) {
			partnerReconciliationData.stream().forEach(p -> {
				ReconciliationDataInfoDomain reconcile;
				try {
					reconcile = new ReconciliationDataInfoDomain.ReconciliationDataInfoBuilder(null,
							p.getOrderReferenceNumber(), p.getVoucherID(), VoucherConstants.MAF, null,
							partnerTransactionId, Double.parseDouble(p.getTotalAmount()),
							format.parse(p.getCreatedAt())).build();
					partnerData.add(reconcile);
				} catch (NumberFormatException e1) {
					throw new NumberFormatException(e1.getMessage());
				} catch (ParseException e2) {
					throw new RuntimeException(e2.getMessage());
				}
			});
		}
		VoucherReconciliationDataDomain voucherReconcileDataDomain;
		voucherReconcileDataDomain = new VoucherReconciliationDataDomain.VoucherReconciliationDataBuilder(program,
				reconciliationLevel, loyaltyData, partnerData, orders, dateToSave, userName, dateToSave, userName, null)
						.build();
		return voucherReconciliationDataDomain.saveVoucherReconcileData(voucherReconcileDataDomain,
				externalTransactionId, userName);

	}

	private String getReconciliationLevel(List<Orders> partnerReconciliationData,
			List<Voucher> loyaltyReconciliationData) {
		String level = "";
		if ((!partnerReconciliationData.isEmpty() && partnerReconciliationData.size() > 0)
				&& (!loyaltyReconciliationData.isEmpty() && loyaltyReconciliationData.size() > 0)) {
			level = VoucherConstants.RECONCILE_LOYALTY + "," + VoucherConstants.RECONCILE_PARTNER;
		} else {
			if (!partnerReconciliationData.isEmpty() && partnerReconciliationData.size() > 0) {
				level = VoucherConstants.RECONCILE_PARTNER;
			} else {
				level = VoucherConstants.RECONCILE_LOYALTY;
			}
		}
		return level;

	}

	/**
	 * This method sends an email after reconciliation
	 * @param voucherReconcileId
	 * @param dbRefIdReconcile
	 * @param externalTransactionId 
	 * @throws VoucherManagementException
	 */
	public void sendReconcileEmail(String voucherReconcileId, String dbRefIdReconcile, String externalTransactionId)
			throws VoucherManagementException {
		Optional<VoucherReconciliation> voucherReconcilation = voucherReconciliationRepository
				.findById(voucherReconcileId);
		Optional<VoucherReconciliationData> voucherDataReconcilation = voucherReconciliationDataRepository
				.findById(dbRefIdReconcile);

		if (voucherReconcilation.isPresent() && voucherDataReconcilation.isPresent()) {
			Integer loyaltyCount = voucherReconcilation.get().getLoyalty().getNoofCountTransaction();
			Double loyaltyAmount = voucherReconcilation.get().getLoyalty().getTotalAmount();

			Integer partnerCount = voucherReconcilation.get().getPartner().getNoofCountTransaction();
			Double partnerAmount = voucherReconcilation.get().getPartner().getTotalAmount();

			List<ReconciliationDataInfo> data = new ArrayList<>();
			if (voucherDataReconcilation.get().getReconciliationLevel()
					.equalsIgnoreCase(VoucherConstants.RECONCILE_LOYALTY)) {
				data.addAll(voucherDataReconcilation.get().getLoyaltyReconciliationData());
			} else if (voucherDataReconcilation.get().getReconciliationLevel()
					.equalsIgnoreCase(VoucherConstants.RECONCILE_PARTNER)) {
				data.addAll(voucherDataReconcilation.get().getPartnerReconciliationData());
			} else {
				data.addAll(voucherDataReconcilation.get().getLoyaltyReconciliationData());
				data.addAll(voucherDataReconcilation.get().getPartnerReconciliationData());
			}
			voucherService.sendEmailVoucherReconcile(loyaltyCount, loyaltyAmount, partnerCount, partnerAmount, data, externalTransactionId);
		} else {
			throw new VoucherManagementException(this.getClass().toString(), "voucherReconcile",
					VoucherManagementCode.VOUCHER_RECONCILIATION_NO_RECORDS.getMsg(),
					VoucherManagementCode.VOUCHER_RECONCILIATION_NO_RECORDS);
		}
	}

	/**
	 * This method checks if the voucher is invoiced
	 * @param voucherCode
	 * @param token
	 * @return true or false
	 * @throws VoucherManagementException
	 */
	public boolean checkIfInvoiced(String voucherCode, Headers header) throws VoucherManagementException {
		return voucherPointsBankService.checkIfInvoiced(voucherCode, header);

	}

	/**
	 * This method is used to list voucher by status.
	 * @param voucherStatus
	 * @param accountNumber
	 * @param voucherListResponse
	 * @param channelCheck 
	 * @return
	 * @throws VoucherManagementException 
	 */
	public ListVoucherStatusResponse listVoucherByStatus(String voucherStatus, String accountNumber, Headers headers,
			ListVoucherStatusResponse voucherListResponse, String offerId, boolean channelCheck, Integer page, Integer limit)
			throws VoucherManagementException {
	
		List<VoucherListResult> voucherList = new ArrayList<>();
		Date currentDate = Utils.getCurrentDatePlusOne();
		Sort sort = Sort.by(Sort.Direction.DESC, VoucherConstants.SORT_DESC_DOWNLOADED_DATE);
		Pageable pageNumberWithElements = (limit != null && page != null) ? PageRequest.of(page,limit,sort) : PageRequest.of(0,Integer.MAX_VALUE,sort);
		
		if (voucherStatus.equalsIgnoreCase(VoucherStatus.ACTIVE)) {
			
			List<Voucher> vouchers = new ArrayList<>();

			if (null != headers.getChannelId() && headers.getChannelId().equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
				
				if(channelCheck) {
					
					vouchers = !StringUtils.isEmpty(offerId)
							? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndOfferIdAndChannelIgnoreCase(
									accountNumber, VoucherStatus.ACTIVE, currentDate, offerId, headers.getChannelId(), sort)
							: voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndChannelIgnoreCase(accountNumber,
									VoucherStatus.ACTIVE, currentDate, headers.getChannelId(), sort);
					
				} else {
					
					vouchers = !StringUtils.isEmpty(offerId)
							? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndOfferId(
								accountNumber, VoucherStatus.ACTIVE, currentDate, offerId, pageNumberWithElements)
							: voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDate(accountNumber,
								VoucherStatus.ACTIVE, currentDate, pageNumberWithElements);
				}

			} else {
				if(channelCheck) {
					
					vouchers = !StringUtils.isEmpty(offerId) 
							? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListedAndOfferIdAndChannelIgnoreCase(
									accountNumber, VoucherStatus.ACTIVE, currentDate, offerId, headers.getChannelId(), sort)
							: voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListedAndChannelIgnoreCase(
							                accountNumber, VoucherStatus.ACTIVE, currentDate, headers.getChannelId(), sort);
					
				} else {
					
					vouchers = !StringUtils.isEmpty(offerId) 
							? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListedAndOfferId(
									accountNumber, VoucherStatus.ACTIVE, currentDate, offerId, pageNumberWithElements)
							: voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListed(
									accountNumber, VoucherStatus.ACTIVE, currentDate, pageNumberWithElements);
				}

				
			}

			if (vouchers.isEmpty()) {
				voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_ACTIVE_VOUCHERS_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_ACTIVE_VOUCHERS_AVAILABLE.getMsg());
				voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
				voucherListResponse.getResult().setVouchers(voucherList);
				return voucherListResponse;
			}

			voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, accountNumber, voucherStatus, headers,
					VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_STATUS, headers.getProgram());

			if (voucherList.isEmpty()) {
				voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_ACTIVE_VOUCHERS_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_ACTIVE_VOUCHERS_AVAILABLE.getMsg());
				voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
				voucherListResponse.getResult().setVouchers(voucherList);
				return voucherListResponse;
			}

			voucherListResponse.getApiStatus()
					.setMessage(VoucherManagementCode.LISTING_ACTIVE_VOUCHERS_SUCCESS.getMsg());

		} else if (voucherStatus.equalsIgnoreCase(VoucherStatus.EXPIRED)) {

			List<Voucher> vouchers = new ArrayList<>();

			if (null != headers.getChannelId() && headers.getChannelId().equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
				
				vouchers = channelCheck
						? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatusAndChannelIgnoreCase(
								accountNumber, currentDate, VoucherStatus.EXPIRED, headers.getChannelId(), sort)
						: voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatus(
								accountNumber, currentDate, VoucherStatus.EXPIRED, pageNumberWithElements);
			} else {
		
				vouchers = channelCheck
						? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatusAndIsBlackListedAndChannelIgnoreCase(accountNumber,
						                currentDate, VoucherStatus.EXPIRED, headers.getChannelId(), sort)
						: voucherRepository.findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatusAndIsBlackListed(accountNumber,
								currentDate, VoucherStatus.EXPIRED, pageNumberWithElements);
			}

			if (vouchers.isEmpty()) {
				voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_EXPIRED_VOUCHERS_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_EXPIRED_VOUCHERS_AVAILABLE.getMsg());
				voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
				voucherListResponse.getResult().setVouchers(voucherList);
				return voucherListResponse;
			}

			voucherList = getListOfVouchersForExpired(vouchers, accountNumber, voucherStatus, headers.getProgram());

			if (voucherList.isEmpty()) {
				voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_EXPIRED_VOUCHERS_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_EXPIRED_VOUCHERS_AVAILABLE.getMsg());
				voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
				voucherListResponse.getResult().setVouchers(voucherList);
				return voucherListResponse;
			} 
			
			voucherListResponse.getApiStatus()
					.setMessage(VoucherManagementCode.LISTING_EXPIRED_VOUCHERS_SUCCESS.getMsg());

		} else if (voucherStatus.equalsIgnoreCase(VoucherStatus.REDEEMED)) {
						
			List<Voucher> vouchers = new ArrayList<>();
			
			if(null != headers.getChannelId() && headers.getChannelId().equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
			
					vouchers = channelCheck
							? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndChannelIgnoreCase(accountNumber, headers.getChannelId(), sort)
							: voucherRepository.findByAccountNumberOrGiftedAccountNumber(accountNumber, pageNumberWithElements);
				
			} else {
					vouchers = channelCheck
						? voucherRepository.findByAccountNumberOrGiftedAccountNumberAndIsBlackListedAndChannelIgnoreCase(accountNumber, headers.getChannelId(), sort)
						: voucherRepository.findByAccountNumberOrGiftedAccountNumberAndIsBlackListed(accountNumber, pageNumberWithElements);
			
			}
			
			voucherList = getListOfRedeeedVouchers(vouchers, accountNumber, headers.getProgram());

			if (voucherList.isEmpty()) {
				voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_REDEEMED_VOUCHERS_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_REDEEMED_VOUCHERS_AVAILABLE.getMsg());
				voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
				voucherListResponse.getResult().setVouchers(voucherList);
				return voucherListResponse;

			}
			
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_REDEEMED_VOUCHERS_SUCCESS.getMsg());
			
		} else {
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_VOUCHER_STATUS.getIntId(),
					VoucherManagementCode.INVALID_VOUCHER_STATUS.getMsg());
		}

		voucherListResponse.getResult().setVouchers(voucherList);

		return voucherListResponse;
		
	}
	
	/**
	 * This method is used to list voucher by business id.
	 * @param businessId
	 * @param voucherListResponse
	 * @return
	 * @throws VoucherManagementException 
	 */
	public VoucherResultResponse listVoucherByBusinessId(String businessId, VoucherResultResponse voucherListResponse,
			Headers headers, String url, String channelId, String program) throws VoucherManagementException {
	
		Date currentDate = Utils.getCurrentDatePlusOne();
		List<VoucherListResult> voucherList = new ArrayList<>();
		Sort sort = Sort.by(Sort.Direction.DESC, VoucherConstants.SORT_DESC_DOWNLOADED_DATE);
		List<Voucher> vouchers = new ArrayList<>();	

		if(null != channelId && channelId.equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
			//vouchers = voucherRepository.findByUuidAndStartDate(businessId, currentDate, sort);
			vouchers = voucherRepository.findByUuidAndStartDateAndProgramCodeIgnoreCase(businessId, currentDate, program, sort);
		} else {
			//vouchers = voucherRepository.findByUuidAndStartDateAndIsBlackListed(businessId, currentDate, sort);
			vouchers = voucherRepository.findByUuidAndStartDateAndIsBlackListed(businessId, currentDate, program, sort);
		}
		
		if (vouchers.isEmpty()) {
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getIntId(),
					VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getMsg());
			return voucherListResponse;
		}

		voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, null, null, headers,
				VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID, program);
		voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_SUCCESS.getMsg());
		voucherListResponse.setResult(voucherList);
		
		return voucherListResponse;
		
	}
	
	/**
	 * This method is used to list voucher by code and account number as an optional parameter.
	 * @param accountNumber
	 * @param voucherCode
	 * @param storeCode
	 * @param voucherListResponse
	 * @param headers
	 * @param channelCheck 
	 * @return
	 * @throws VoucherManagementException
	 */
	public VoucherResultResponse listVoucherByCode(String accountNumber, String voucherCode, String storeCode,
			VoucherResultResponse voucherListResponse, Headers headers, String userName, String channelId, boolean channelCheck) throws VoucherManagementException {
		
		Voucher voucher = populateListByCodeVoucher(accountNumber, voucherCode, channelId, userName, headers, channelCheck);		
		if (voucher == null) {
			return Utils.listVoucherByCodeSetResponse(accountNumber, voucherCode, voucherListResponse);
		} else {
			if (null != channelId && !channelId.equalsIgnoreCase("SAPP") 
					&& voucher.getStatus().equalsIgnoreCase(VoucherStatus.BURNT))
				vouDomain.updateEnquiryDetails(Arrays.asList(voucherCode), userName);
			if (null != channelId && channelId.equalsIgnoreCase("EMAX")
					&& !voucher.getPartnerCode().equalsIgnoreCase("EMAX")) {
				voucherListResponse.getApiStatus()
						.setMessage(VoucherManagementCode.LISTING_SPECIFIC_VOUCHERS_FAILURE.getMsg());
				voucherListResponse.addErrorAPIResponse(
						VoucherManagementCode.VOUCHER_CODE_DOES_NOT_BELONG_TO_PARTNER.getIntId(),
						VoucherManagementCode.VOUCHER_CODE_DOES_NOT_BELONG_TO_PARTNER.getMsg());
				return voucherListResponse;
			}

			if (null != storeCode && null == storeRepository.findByStoreCodeAndMerchantCodeAndProgramCode(storeCode,
					voucher.getMerchantCode(), headers.getProgram())) {
				voucherListResponse.getApiStatus()
						.setMessage(VoucherManagementCode.LISTING_SPECIFIC_VOUCHERS_FAILURE.getMsg());
				voucherListResponse.addErrorAPIResponse(
						VoucherManagementCode.VOUCHER_CODE_DOES_NOT_BELONG_TO_MERCHANT.getIntId(),
						VoucherManagementCode.VOUCHER_CODE_DOES_NOT_BELONG_TO_MERCHANT.getMsg());
				return voucherListResponse;
			}
			
			VoucherListResult listResult = populateVoucherByCodeResponse(headers, voucher, accountNumber);
			if (null == listResult) {
				voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHER_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_VOUCHER_AVAILABLE.getMsg());
				voucherListResponse.getApiStatus()
						.setMessage(VoucherManagementCode.LISTING_SPECIFIC_VOUCHERS_FAILURE.getMsg());
			} else {

				voucherListResponse.getApiStatus()
						.setMessage(VoucherManagementCode.LISTING_SPECIFIC_VOUCHER_SUCCESS.getMsg());
			}
						
			voucherListResponse.setResult(listResult);
			return voucherListResponse;
		}
	}
	
	public Voucher populateListByCodeVoucher(String accountNumber, String voucherCode, String channelId, String userName, Headers headers, boolean channelCheck) {
		if(null != accountNumber && !accountNumber.isEmpty()) {		
			if(null != channelId && channelId.equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
				return channelCheck 
					 ? voucherRepository.findByAccountNumberAndVoucherCodeAndProgramCodeIgnoreCaseAndChannel(accountNumber, voucherCode, headers.getProgram(), channelId)
					 : voucherRepository.findByAccountNumberAndVoucherCodeAndProgramCodeIgnoreCase(accountNumber, voucherCode, headers.getProgram());
			} 
			return channelCheck 
				 ? voucherRepository.findByAccountNumberAndVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCaseAndChannel(accountNumber, voucherCode, headers.getProgram(), channelId)
				 : voucherRepository.findByAccountNumberAndVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCase(accountNumber, voucherCode, headers.getProgram());
		} 
		
		Store store = null != userName ? storeRepository.findByUserName(userName) : null;
		if(null != store) {			
			if(null != channelId && channelId.equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
				return channelCheck
					 ? voucherRepository.findByCodeAndMerchantCodeAndProgramCodeIgnoreCaseAndChannel(voucherCode, store.getMerchantCode(), headers.getProgram(), channelId)
					 : voucherRepository.findByCodeAndMerchantCodeAndProgramCodeIgnoreCase(voucherCode, store.getMerchantCode(), headers.getProgram());
			} 
			return channelCheck
				 ? voucherRepository.findByCodeAndMerchantCodeAndIsBlackListedAndProgramCodeIgnoreCaseAndChannel(voucherCode, store.getMerchantCode(), headers.getProgram(), channelId)
				 : voucherRepository.findByCodeAndMerchantCodeAndIsBlackListedAndProgramCodeIgnoreCase(voucherCode, store.getMerchantCode(), headers.getProgram());
		} else {				
			if(null != channelId && channelId.equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
				return channelCheck
					? voucherRepository.findByVoucherCodeAndProgramCodeIgnoreCaseAndChannelIgnoreCase(voucherCode, headers.getProgram(), channelId)
					: voucherRepository.findByVoucherCodeAndProgramCodeIgnoreCase(voucherCode, headers.getProgram());
			} 
			return channelCheck
				? voucherRepository.findByVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCaseAndChannel(voucherCode, headers.getProgram(), channelId)
				: voucherRepository.findByVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCase(voucherCode, headers.getProgram());
		}			
		
	}

	/**
	 * This method is used to validate voucher's start date and account details.
	 * @param listResult
	 * @param voucher
	 * @param accountNumber
	 * @return
	 * @throws VoucherManagementException 
	 */
	private VoucherListResult populateVoucherByCodeResponse(Headers headers, Voucher voucher, String accountNumber) throws VoucherManagementException {
		
		VoucherListResult voucherListResult = null;
		Date currentDate = Utils.getCurrentDatePlusOne();
		
		// If voucher is gifted  & accountNumber is of sender's.
		if (null != accountNumber && null != voucher.getGiftDetails()
				&& accountNumber.equals(voucher.getGiftDetails().getGiftedAccountNumber())) {	
			voucherListResult = getListOfVoucherForStoreCodeAPI(voucher, accountNumber, headers.getProgram());
		}
		
		// If accountNumber is receiver's or of the account calling this API.
		if (null != accountNumber && accountNumber.equals(voucher.getAccountNumber()) && null == voucher.getStartDate()
				|| (null != voucher.getStartDate() && voucher.getStartDate().before(currentDate))) {	
			voucherListResult = getListOfVoucherForStoreCodeAPI(voucher, accountNumber, headers.getProgram());
		}
		
		// If no accountNumber is passed to API.
		if (null == accountNumber && (null == voucher.getStartDate()
				|| (null != voucher.getStartDate() && voucher.getStartDate().before(currentDate)))) {	
			voucherListResult = getListOfVoucherForStoreCodeAPI(voucher, accountNumber, headers.getProgram());
		}
		
		// If CRFR partner and accountNumber is of receiver's or the account calling this API.
		if (null != voucher.getPartnerCode() && voucher.getPartnerCode().equals(VoucherConstants.CARREFOUR)
				&& null != accountNumber && accountNumber.equals(voucher.getAccountNumber())) {
	
			voucherListResult = listByCodeForCarrefour(headers, voucher, accountNumber);

		}
		if (voucherListResult != null && !voucher.getStatus().equalsIgnoreCase(VoucherStatus.READY)
				&& VoucherConstants.ADMID_PORTAL_CHANNEL_ID.equalsIgnoreCase(headers.getChannelId())) {
			if (voucher.getUuid() != null && voucher.getUuid().getEpgTransactionId() != null)
				voucherListResult.setEpgTransactionId(voucher.getUuid().getEpgTransactionId());

			// following fields should only be set for vouchers with ready status
			voucherListResult.setFileName(null);
			voucherListResult.setCreatedUser(null);
			voucherListResult.setCreatedDate(null);
		}
		return voucherListResult;
		
	}
	
	private VoucherListResult listByCodeForCarrefour(Headers headers, Voucher voucher, String accountNumber)
			throws VoucherManagementException {
		
		Date expiryDate = Utils.getExpiredVoucherDate();
		
		VoucherListResult voucherListResult = getListOfVoucherForStoreCodeAPI(voucher, accountNumber, headers.getProgram());
		
		if (null != voucher.getStatus() && voucher.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
				&& null != voucher.getExpiryDate() && voucher.getExpiryDate().after(expiryDate)) {

			List<Voucher> voucherRedeemedCarrefour = new ArrayList<>();

			BigDecimal balance = getCarrefourBalance(voucher, accountNumber);

			if (null != balance && balance.compareTo(BigDecimal.ONE) == 1) {
				if (null != voucherListResult) voucherListResult.setVoucherBalance(balance.doubleValue());
				vouDomain.updateCarrefourBalance(voucher, balance, headers.getUserName());
				
				return voucherListResult;
			}
			
			if (null != balance && (balance.compareTo(BigDecimal.ONE) == -1 || balance.compareTo(BigDecimal.ONE) == 0)) {
				
				voucherRedeemedCarrefour.add(voucher);
				List<Voucher> savedVoucher = vouDomain.updateCarrefourRedeemedVouchers(voucherRedeemedCarrefour, balance,
						headers.getExternalTransactionId(), headers.getUserName(),
						VoucherRequestMappingConstants.VOUCHER_LIST_BY_CODE);
			
				VoucherListResult savedV = null;
				for(Voucher v : savedVoucher) {
					savedV = getListOfVoucherForStoreCodeAPI(v, accountNumber, headers.getProgram());
					if (null != savedV) {
						savedV.setVoucherBalance(null != balance ? balance.doubleValue() : 0.0);
					}
				}
				
				return savedV;
			
			}
			
			return voucherListResult;
			
		}

		if (null != voucher.getStatus() && (voucher.getStatus().equalsIgnoreCase(VoucherStatus.EXPIRED)
				|| (voucher.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE) && null != voucher.getExpiryDate()
						&& voucher.getExpiryDate().before(expiryDate)))) {

			if (null != voucher.getVoucherBalance()) {
				if (null != voucherListResult) {
					voucherListResult.setVoucherBalance(voucher.getVoucherBalance());
					voucherListResult.setStatus(VoucherStatus.EXPIRED);
				}
				return voucherListResult;
			} else {
				
				List<Voucher> voucherRedeemedCarrefour = new ArrayList<>();
				
				BigDecimal balance = getCarrefourBalance(voucher, accountNumber);
				
				voucherRedeemedCarrefour.add(voucher);
				List<Voucher> savedVoucher = vouDomain.updateCarrefourRedeemedVouchers(voucherRedeemedCarrefour,
						balance, headers.getExternalTransactionId(), headers.getUserName(),
						VoucherRequestMappingConstants.VOUCHER_LIST_BY_CODE);

				VoucherListResult savedV = null;
				for(Voucher v : savedVoucher) {
					savedV = getListOfVoucherForStoreCodeAPI(v, accountNumber, headers.getProgram());
					if (null != savedV) {
						savedV.setVoucherBalance(null != balance ? balance.doubleValue() : 0.0);
						savedV.setStatus(VoucherStatus.EXPIRED);
					}
				}

				return savedV;

			}

		}

		return voucherListResult;

	}

	public BigDecimal getCarrefourBalance(Voucher voucher, String accountNumber) throws VoucherManagementException {

		int voucherAmt = null != voucher.getVoucherAmount() ? voucher.getVoucherAmount().intValue() : 0;
		String uuid = null != voucher.getUuid() ? voucher.getUuid().getId() : "";
		String cardNumber = null;
		if (voucher.getVoucherCode().startsWith(genCode)) {
			cardNumber = voucher.getVoucherCode().substring(genCode.length());
		}

		return carreFourService.getEvoucherBalanceFromCarrefour(voucher.getPartnerTransactionId(),
				cardNumber, accountNumber, voucherAmt, uuid);
		
	}

	/**
	 * This method is used to list voucher by id.
	 * @param voucherId
	 * @param voucherListResponse
	 * @param channelCheck 
	 */
	public VoucherResultResponse listVoucherById(String voucherId, VoucherResultResponse voucherListResponse, String channelId, String program, boolean channelCheck) {
		
		Date currentDate = Utils.getCurrentDatePlusOne();
		
		VoucherListResult voucherResult = null;
		Optional<Voucher> voucher = null;
		
		if(null != channelId && channelId.equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
			voucher = channelCheck
					? voucherRepository.findByIdAndStartDateAndChannel(voucherId, currentDate, channelId)
					: voucherRepository.findByIdAndStartDate(voucherId, currentDate);
		} else {
			voucher = channelCheck
					? voucherRepository.findByIdAndStartDateAndIsBlackListedAndChannel(voucherId, currentDate, channelId)
					: voucherRepository.findByIdAndStartDateAndIsBlackListed(voucherId, currentDate);
		}
		
		if (null == voucher || !voucher.isPresent()) {
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_VOUCHER_ID_FAILURE.getMsg());
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_VOUCHER_ID.getIntId(),
					VoucherManagementCode.NO_VOUCHERS_FOR_VOUCHER_ID.getMsg());
			return voucherListResponse;
		}
		
		voucherResult = getListOfVoucherForStoreCodeAPI(voucher.get(), null, program);
		voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_VOUCHER_ID_SUCCESS.getMsg());
		voucherListResponse.setResult(voucherResult);
		
		return voucherListResponse;

	}
	
	/**
	 * This method is used to list voucher by list of business id.
	 * @param listVoucherRequest
	 * @param voucherListResponse
	 * @return
	 * @throws VoucherManagementException 
	 */
	public VoucherResultResponse listVoucherByBusinessIdList(ListVoucherRequest listVoucherRequest,
			VoucherResultResponse voucherListResponse, Headers headers, String url, String channelId, String program) throws VoucherManagementException {

		List<VoucherListResult> voucherList = new ArrayList<>();
		Sort sort = Sort.by(Sort.Direction.DESC, VoucherConstants.SORT_DESC_DOWNLOADED_DATE);
		
		if (null == listVoucherRequest.getBusinessIds() || listVoucherRequest.getBusinessIds().isEmpty()) {
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_BUSINESS_ID_EMPTY.getIntId(),
					VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_BUSINESS_ID_EMPTY.getMsg());
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			voucherListResponse.setResult(voucherList);
			return voucherListResponse;
		}
		
		List<Voucher> vouchers = new ArrayList<>();
		
		if(null != channelId && channelId.equalsIgnoreCase(VoucherConstants.ADMID_PORTAL_CHANNEL_ID)) {
			vouchers = voucherRepository.findByUuid(listVoucherRequest.getBusinessIds(), sort);
		} else {
			vouchers = voucherRepository.findByUuidAndIsBlackListed(listVoucherRequest.getBusinessIds(), sort);
		}
		
		if (vouchers.isEmpty()) {
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getIntId(),
					VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getMsg());
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			return voucherListResponse;
		}

		voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, null, null, headers,
				VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID_LIST, program);
		voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_SUCCESS.getMsg());
		voucherListResponse.setResult(voucherList);

		return voucherListResponse;

	}
	
	public List<VoucherListResult> listVouchersByPartnerMerchant(String partnerCode, String merchantCode, Headers headers, VoucherResultResponse voucherResultResponse) throws VoucherManagementException {
		
		Sort sort = Sort.by(Sort.Direction.DESC, VoucherConstants.SORT_DESC_DOWNLOADED_DATE);
		List<VoucherListResult> voucherList = new ArrayList<>();
				
		if(partnerCode != null) {
			LOG.info("listVouchersByPartnerMerchant :: PartnerCode : {}",partnerCode);
			if(merchantCode != null) {
				LOG.info("listVouchersByPartnerMerchant :: MerchantCode : {}",merchantCode);
				List<Voucher> vouchers = voucherRepository.findByPartnerCodeAndMerchantCode(partnerCode, merchantCode, sort);
				if (!vouchers.isEmpty()) {
					voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, null, null, headers, VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_PARTNER_MERCHANT, headers.getProgram());
					voucherResultResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_SUCCESS.getMsg());
				} else {
					voucherResultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_PARTNER_MERCHANT.getIntId(), VoucherManagementCode.NO_VOUCHERS_FOR_PARTNER_MERCHANT.getMsg());
				}
			} else {
				List<Voucher> vouchers = voucherRepository.findByPartnerCode(partnerCode, sort);
				if (!vouchers.isEmpty()) {
					voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, null, null, headers, VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_PARTNER_MERCHANT, headers.getProgram());
					voucherResultResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_SUCCESS.getMsg());
				} else {
					voucherResultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_PARTNER.getIntId(),	VoucherManagementCode.NO_VOUCHERS_FOR_PARTNER.getMsg());
					voucherResultResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
					voucherResultResponse.setResult(voucherList);
				}
			}
		} else if (merchantCode != null) {
			LOG.info("listVouchersByPartnerMerchant :: MerchantCode : {}",merchantCode);
			List<Voucher> vouchers = voucherRepository.findByMerchantCode(merchantCode, sort);
			if (!vouchers.isEmpty()) {
				voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, null, null, headers, VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_PARTNER_MERCHANT, headers.getProgram());
				voucherResultResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_SUCCESS.getMsg());

			} else {
				voucherResultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_MERCHANT.getIntId(), VoucherManagementCode.NO_VOUCHERS_FOR_MERCHANT.getMsg());
			}
		} else {
			voucherResultResponse.addErrorAPIResponse(VoucherManagementCode.PARTER_MERCHANT_CODE_MANDATORY.getIntId(),
					VoucherManagementCode.PARTER_MERCHANT_CODE_MANDATORY.getMsg());
		}
		return voucherList;
	}
	
	public List<VoucherListResult> listVouchersByVoucherCodes(List<String> voucherCodes, Headers headers, VoucherResultResponse voucherResultResponse) throws VoucherManagementException {
		
		Sort sort = Sort.by(Sort.Direction.DESC, VoucherConstants.SORT_DESC_DOWNLOADED_DATE);
		List<VoucherListResult> voucherList = new ArrayList<>();
				
		if(voucherCodes != null) {
			LOG.info("listVouchersByVoucherCodes :: VoucherCodes : {}",voucherCodes);		
			List<Voucher> vouchers = voucherRepository.findByVoucherCodeIn(voucherCodes, sort);
			if (!vouchers.isEmpty()) {
				voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, null, null, headers, VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_PARTNER_MERCHANT, headers.getProgram());
				voucherResultResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_SUCCESS.getMsg());
			} else {
				voucherResultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_PARTNER.getIntId(),	VoucherManagementCode.NO_VOUCHERS_FOR_PARTNER.getMsg());
				voucherResultResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
				voucherResultResponse.setResult(voucherList);
			}
			
		} else {
			voucherResultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_REQUEST.getIntId(),
					VoucherManagementCode.INVALID_REQUEST.getMsg());
		}
		return voucherList;
	}
	
	/**
	 * This method returns voucher details for the list of vouchers specified
	 * @param vouchers
	 * @return list of voucher details
	 * @throws VoucherManagementException 
	 */
	public List<VoucherListResult> getListOfVouchersForPartnerCodeAPI(List<Voucher> vouchers, String accountNumber,
			String voucherStatus, Headers headers, String api, String program) throws VoucherManagementException {
	
        List<OfferCatalog> offerCatalogs = getOfferCatalogs(vouchers, program);
        List<GiftingHistory> giftingHistory = getGiftingHistory(vouchers);
		List<VoucherListResult> voucherList = new ArrayList<>();
		for (Voucher voucher : vouchers) {
			//Skips the loop and voucher not returned in response when 
			//account number is of gift sender's and status is active or expired.
//			if (!Utils.validateIsGift(voucher, accountNumber, voucherStatus)) continue;

			VoucherListResult voucherResult = modelMapper.map(voucher, VoucherListResult.class);
			populateVoucherResponse(voucher, voucherResult, accountNumber);
			
			Optional<OfferCatalog> offer = offerCatalogs.stream().filter(o -> o.getOfferId().equals(voucher.getOfferInfo().getOffer())).findFirst();
			if(offer.isPresent()) {
				populateOfferResponse(offer.get(), voucher, voucherResult);
			}
			
			if (null != voucher.getGiftDetails() && null != voucher.getGiftDetails().getGiftId()) {
				Optional<GiftingHistory> giftHistory = giftingHistory.stream().filter(g -> g.getId().equals(voucher.getGiftDetails().getGiftId())).findFirst();
				if(giftHistory.isPresent()) {
					populateGiftResponse(giftHistory.get(), voucher, accountNumber, voucherResult);
				}
			}
			
			if (null != api && VoucherConstants.LIST_VOUCHER_ELIGIBLE_API.contains(api)
					&& null != voucher.getPartnerCode()
					&& voucher.getPartnerCode().equals(VoucherConstants.CARREFOUR)) {
	
				getVoucherValidateCarrefour(api, voucher, voucherStatus,
						accountNumber, voucherList, voucherResult, headers);

			} else {
				voucherList.add(voucherResult);			
			}
			
		}
		return voucherList;
		
	}

	private List<Voucher> getVoucherValidateCarrefour(String api, Voucher voucher, String voucherStatus, String accountNumber,
			List<VoucherListResult> voucherList, VoucherListResult voucherResult, Headers headers) throws VoucherManagementException {

		List<Voucher> voucherRedeemedCarrefour = new ArrayList<>();		
		BigDecimal balance = getCarrefourBalance(voucher, accountNumber);
		
		if(api.equalsIgnoreCase(VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_STATUS)) {
			listVoucherByStatusCarrefour(voucherStatus, balance, voucher, voucherRedeemedCarrefour, voucherList,
					voucherResult, headers);
		} else if (api.equalsIgnoreCase(VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID)
				|| api.equalsIgnoreCase(VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID_LIST)) {
			listVoucherByBusinessIdCarrefour(balance, voucher, voucherRedeemedCarrefour, voucherList, voucherResult, headers);
		}
		
		return voucherRedeemedCarrefour;

	}
	
	private List<Voucher> listVoucherByStatusCarrefour(String voucherStatus, BigDecimal balance, Voucher voucher,
			List<Voucher> voucherRedeemedCarrefour, List<VoucherListResult> voucherList,
			VoucherListResult voucherResult, Headers headers) {

		if (voucherStatus.equalsIgnoreCase(VoucherStatus.ACTIVE)) {

			LOG.info("Balance: {}", balance);
			
			if (null != balance && balance.compareTo(BigDecimal.ONE) == 1) {
				voucherResult.setVoucherBalance(balance.doubleValue());
				voucherList.add(voucherResult);
			} else if (null != balance && (balance.compareTo(BigDecimal.ONE) == -1 || balance.compareTo(BigDecimal.ONE) == 0)) {
				voucherRedeemedCarrefour.add(voucher);
				vouDomain.updateCarrefourRedeemedVouchers(voucherRedeemedCarrefour, balance, headers.getExternalTransactionId(),
						headers.getUserName(), VoucherRequestMappingConstants.VOUCHER_LIST_BY_STATUS);
			}

		}
		
		return voucherRedeemedCarrefour;

	}

	private List<Voucher> listVoucherByBusinessIdCarrefour(BigDecimal balance, Voucher voucher,
			List<Voucher> voucherRedeemedCarrefour, List<VoucherListResult> voucherList,
			VoucherListResult voucherResult, Headers headers) {

		Date expiryDate = Utils.getExpiredVoucherDate();
		
		if (null != voucher.getStatus() && voucher.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
				&& null != voucher.getExpiryDate() && voucher.getExpiryDate().after(expiryDate)) {

			if (null != balance && balance.compareTo(BigDecimal.ONE) == 1) {
				if (null != voucherResult) voucherResult.setVoucherBalance(balance.doubleValue());
				voucherList.add(voucherResult);
			}
			
			if (null != balance && (balance.compareTo(BigDecimal.ONE) == -1 || balance.compareTo(BigDecimal.ONE) == 0)) {
				
				voucherRedeemedCarrefour.add(voucher);
				List<Voucher> savedVoucher = vouDomain.updateCarrefourRedeemedVouchers(voucherRedeemedCarrefour, balance,
						headers.getExternalTransactionId(), headers.getUserName(),
						VoucherRequestMappingConstants.VOUCHER_LIST_BY_STATUS);
			
				VoucherListResult savedV = null;
				for(Voucher v : savedVoucher) {
					savedV = getListOfVoucherForStoreCodeAPI(v, null, headers.getProgram());
					if (null != savedV) {
						savedV.setVoucherBalance(null != balance ? balance.doubleValue() : 0.0);
					}
				}
				
				voucherList.add(savedV);
			
			}
			
			return null;

		} else if (null != voucher.getStatus() && (voucher.getStatus().equalsIgnoreCase(VoucherStatus.EXPIRED)
				|| (voucher.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE) && null != voucher.getExpiryDate()
						&& voucher.getExpiryDate().before(expiryDate)))) {

			if (null != voucher.getVoucherBalance()) {
				if (null != voucherResult)
					voucherResult.setVoucherBalance(voucher.getVoucherBalance());
				voucherList.add(voucherResult);
			} else {

				voucherRedeemedCarrefour.add(voucher);
				List<Voucher> savedVoucher = vouDomain.updateCarrefourRedeemedVouchers(voucherRedeemedCarrefour,
						balance, headers.getExternalTransactionId(), headers.getUserName(),
						VoucherRequestMappingConstants.VOUCHER_LIST_BY_STATUS);

				VoucherListResult savedV = null;
				for(Voucher v : savedVoucher) {
					savedV = getListOfVoucherForStoreCodeAPI(v, null, headers.getProgram());
					if (null != savedV) {
						savedV.setVoucherBalance(null != balance ? balance.doubleValue() : 0.0);
					}
				}

				voucherList.add(savedV);

			}

		} else {
			voucherList.add(voucherResult);
		}

		return voucherRedeemedCarrefour;

	}

	/**
	 * This method lists the expired voucher details
	 * @param vouchers
	 * @param accountNumber
	 * @param voucherStatus
	 * @param headers
	 * @return
	 */
	public List<VoucherListResult> getListOfVouchersForExpired(List<Voucher> vouchers, String accountNumber,
			String voucherStatus, String program) {
		
		List<VoucherListResult> voucherList = new ArrayList<>();
		List<OfferCatalog> offerCatalogs = getOfferCatalogs(vouchers, program);
		List<GiftingHistory> giftingHistory = getGiftingHistory(vouchers);
		
		for (Voucher voucher : vouchers) {
			// Skips the loop and voucher not returned in response when
			// account number is of gift sender's and status is active or expired.
//			if (!Utils.validateIsGift(voucher, accountNumber, voucherStatus)) continue;

			voucherList.add(getListOfVoucherForListStatusAPI(offerCatalogs, giftingHistory, voucher, accountNumber));

		}
		
		return voucherList;
		
	}
	
	/**
	 * This method is used to get list of redeemed vouchers.
	 * @param vouchers
	 * @param accountNumber
	 * @return
	 * @throws VoucherManagementException 
	 */
	public List<VoucherListResult> getListOfRedeeedVouchers(List<Voucher> vouchers, String accountNumber, String program) {
		
		List<VoucherListResult> voucherList = new ArrayList<>();
		//Date expiryDateCheck = Utils.getExpiredVoucherDate();
		List<OfferCatalog> offerCatalogs = getOfferCatalogs(vouchers, program);
		List<GiftingHistory> giftingHistory = getGiftingHistory(vouchers);
		
		for (Voucher voucher : vouchers) {
				
//			if ((voucher.getStatus().equalsIgnoreCase(VoucherStatus.BURNT) && accountNumber.equals(voucher.getAccountNumber()))
//					|| (null != voucher.getGiftDetails()
//							&& accountNumber.equals(voucher.getGiftDetails().getGiftedAccountNumber()))) {
				
				VoucherListResult voucherListResult = getListOfVoucherForListStatusAPI(offerCatalogs, giftingHistory, voucher, accountNumber);
				voucherList.add(voucherListResult);
				
//			}
		}
		
		return voucherList;
		
	}

	/**
	 * This method gets details for the voucher specified 
	 * @param voucher
	 * 
	 */
	public VoucherListResult getListOfVoucherForStoreCodeAPI(Voucher voucher, String accountNumber, String program) {
		
		//OfferCatalog offerCatalog = offerRepository.findByOfferId(voucher.getOfferInfo().getOffer());
		OfferCatalog offerCatalog = offerRepository.findByOfferIdAndProgramCodeIgnoreCase(voucher.getOfferInfo().getOffer(), program);
		VoucherListResult listResult = modelMapper.map(voucher, VoucherListResult.class);
		
		populateVoucherResponse(voucher, listResult, accountNumber);
		
		if(null != offerCatalog) {
			populateOfferResponse(offerCatalog, voucher, listResult);
			populateStoreResponse(offerCatalog, listResult);
		}
		
		if(null != voucher.getGiftDetails() && null != voucher.getGiftDetails().getIsGift()
				&& voucher.getGiftDetails().getIsGift().equalsIgnoreCase(VoucherConstants.YES)) {
			//Optional<GiftingHistory> giftHistory = giftingHistoryRepository.findById(voucher.getGiftDetails().getGiftId());
			Optional<GiftingHistory> giftHistory = giftingHistoryRepository.findByIdAndProgramCodeIgnoreCase(voucher.getGiftDetails().getGiftId(), program);
			if(giftHistory.isPresent()) {
				GiftingHistory gift = giftHistory.get();
				populateGiftResponse(gift, voucher, accountNumber, listResult);
			}
		}
		
		return listResult;
	}
	
	public VoucherListResult getListOfVoucherForListStatusAPI(List<OfferCatalog> offerCatalogs, List<GiftingHistory> giftingHistory, Voucher voucher, String accountNumber) {
		
		OfferCatalog offerCatalog = null;
		
		for (OfferCatalog offer : offerCatalogs) {
			if (null != voucher.getOfferInfo() && null != voucher.getOfferInfo().getOffer()
					&& voucher.getOfferInfo().getOffer().equals(offer.getOfferId())) {
				offerCatalog = offer;
			}
		}
		
		VoucherListResult listResult = modelMapper.map(voucher, VoucherListResult.class);
		
		populateVoucherResponse(voucher, listResult, accountNumber);
		
		if(null != offerCatalog) {
			populateOfferResponse(offerCatalog, voucher, listResult);
			populateStoreResponse(offerCatalog, listResult);
		}
		
		for (GiftingHistory giftHistory : giftingHistory) {
			if(null != voucher.getGiftDetails() && null != voucher.getGiftDetails().getIsGift()
					&& voucher.getGiftDetails().getIsGift().equalsIgnoreCase(VoucherConstants.YES) && null != voucher.getGiftDetails().getGiftId()
							&& voucher.getGiftDetails().getGiftId().equals(giftHistory.getId())) {
					populateGiftResponse(giftHistory, voucher, accountNumber, listResult);
				
			}
		}	
		
		
		return listResult;
	}

	/**
	 * This method is used to populate voucher attributes in GET voucher API responses.
	 * @param voucher
	 * @param voucherResult
	 * @return
	 */
	private VoucherListResult populateVoucherResponse(Voucher voucher, VoucherListResult voucherResult, String accountNumber) {
		
		Date currentDate = Utils.getExpiredVoucherDate();
        
		if (null != voucher.getExpiryDate() && voucher.getExpiryDate().before(currentDate)) {
			voucherResult.setStatus(VoucherStatus.EXPIRED);
		} 
		
		if (null != voucher.getStatus() && voucher.getStatus().equalsIgnoreCase(VoucherStatus.BURNT)) {
			voucherResult.setStatus(VoucherStatus.REDEEMED);
		}
		
		if (null != accountNumber && null != voucher.getGiftDetails() && null != voucher.getGiftDetails().getIsGift()
				&& voucher.getGiftDetails().getIsGift().equalsIgnoreCase(VoucherConstants.YES)
				&& accountNumber.equals(voucher.getGiftDetails().getGiftedAccountNumber())) {
			voucherResult.setStatus(VoucherStatus.REDEEMED);
		}
		if (null != voucher.getVoucherValues()) {
			voucherResult.setCost(voucher.getVoucherValues().getCost());
			voucherResult.setPointsValue(voucher.getVoucherValues().getPointsValue());
		}
		if (null != voucher.getUuid() && null != voucher.getUuid().getId()) {
			voucherResult.setUuid(voucher.getUuid().getId());
		}
		if (null != voucher.getBarcodeType() && null != voucher.getBarcodeType().getName()) {
			voucherResult.setBarcodeType(voucher.getBarcodeType().getName());
		}
		PurchaseHistory purchaseHistory = voucher.getUuid();
		if (null != purchaseHistory && null != purchaseHistory.getExtRefNo()) {
			voucherResult.setExtRefNo(purchaseHistory.getExtRefNo());
		}
		
		return voucherResult;
		
	}
	
	/**
	 * This method is used to populate offer attributes in GET voucher API responses.
	 * @param offer
	 * @param voucher
	 * @param voucherResult
	 * @return
	 */
	private VoucherListResult populateOfferResponse(OfferCatalog offer, Voucher voucher,
			VoucherListResult voucherResult) {

		voucherResult.setEstSavings(offer.getEstSavings());
		voucherResult.setSharingBonus(offer.getSharingBonus());
		voucherResult.setSharing(offer.getSharing());
		
		if(!ObjectUtils.isEmpty(offer.getVoucherInfo())) {
			
			voucherResult.setVoucherRedeemType(offer.getVoucherInfo().getVoucherRedeemType());
			voucherResult.setPartnerRedeemURL(offer.getVoucherInfo().getPartnerRedeemURL());
			
			if(!ObjectUtils.isEmpty(offer.getVoucherInfo().getInstructionsToRedeemTitle())) {
				
				voucherResult.setInstructionsToRedeemTitleAr(offer.getVoucherInfo().getInstructionsToRedeemTitle().getInstructionsToRedeemTitleAr());
				voucherResult.setInstructionsToRedeemTitleEn(offer.getVoucherInfo().getInstructionsToRedeemTitle().getInstructionsToRedeemTitleEn());
				
			}
			
			if(!ObjectUtils.isEmpty(offer.getVoucherInfo().getInstructionsToRedeem())) {
				
				voucherResult.setInstructionsToRedeemAr(offer.getVoucherInfo().getInstructionsToRedeem().getInstructionsToRedeemAr());
				voucherResult.setInstructionsToRedeemEn(offer.getVoucherInfo().getInstructionsToRedeem().getInstructionsToRedeemEn());
			}
			
		}
		
		populateStoreResponse(offer, voucherResult);
		
		if (Utils.isNotNull(offer.getOffer())) {
			if (null != offer.getOffer().getOfferTitle()) {
				voucherResult.setVoucherNameEn(offer.getOffer().getOfferTitle().getOfferTitleEn());
				voucherResult.setVoucherNameAr(offer.getOffer().getOfferTitle().getOfferTitleAr());
			}
			if (null != offer.getOffer().getOfferTitleDescription()) {
				voucherResult.setVoucherDescriptionEn(
						offer.getOffer().getOfferTitleDescription().getOfferTitleDescriptionEn());
				voucherResult.setVoucherDescriptionAr(
						offer.getOffer().getOfferTitleDescription().getOfferTitleDescriptionAr());
			}
		}

		if (null != offer.getMerchant() && null != offer.getMerchant().getMerchantName()) {
			voucherResult.setMerchantNameAr(offer.getMerchant().getMerchantName().getMerchantNameAr());
			voucherResult.setMerchantName(offer.getMerchant().getMerchantName().getMerchantNameEn());
		}

		if (Utils.isNotNull(offer.getTermsAndConditions())
				&& Utils.isNotNull(offer.getTermsAndConditions().getTermsAndConditions())) {
			voucherResult.setTermsAndConditionsEn(
					offer.getTermsAndConditions().getTermsAndConditions().getTermsAndConditionsEn());
			voucherResult.setTermsAndConditionsAr(
					offer.getTermsAndConditions().getTermsAndConditions().getTermsAndConditionsAr());
		}

		if(null != offer.getOfferType() && null != offer.getOfferType().getOfferDescription()) {
			voucherResult.setOfferTypeAr(offer.getOfferType().getOfferDescription().getTypeDescriptionAr());
			voucherResult.setOfferTypeEn(offer.getOfferType().getOfferDescription().getTypeDescriptionEn());
		}
		
		if ((null != voucher.getType() && voucher.getType().equalsIgnoreCase(VoucherConstants.DISOUNT_OFFER))
				&& (null != offer.getDiscountPerc())) {
			voucherResult.setDiscountPerc(offer.getDiscountPerc().toString() + VoucherConstants.PERCENTAGE);
		}
		
		if(null != offer.getCategory()) {
			voucherResult.setCategoryId(offer.getCategory().getCategoryId());
		}

		return voucherResult;

	}
	
	/**
	 * This method is used to populate store attributes in GET voucher API responses.
	 * @param offer
	 * @param voucherResult
	 * @return
	 */
	private VoucherListResult populateStoreResponse(OfferCatalog offer, VoucherListResult voucherResult) {
		if (Utils.isNotNull(offer.getOfferStores())) {

			List<String> storeAddressEn = new ArrayList<>();
			List<String> storeAddressAr = new ArrayList<>();

			for (Store store : offer.getOfferStores()) {
				if (Utils.isNotNull(store) && Utils.isNotNull(store.getAddress()) && Utils.isNotNull(store.getDescription())) {
					storeAddressEn.add(store.getDescription().getStoreDescriptionEn() + ", " + store.getAddress().getAddressEn());
					storeAddressAr.add(store.getDescription().getStoreDescriptionAr() + ", " + store.getAddress().getAddressAr());
				}
			}

			voucherResult.setStoreAddressEn(storeAddressEn);
			voucherResult.setStoreAddressAr(storeAddressAr);
		}
		
		return voucherResult;
	}

	/**
	 * This method is used to populate gifting attributes in GET voucher API responses.
	 * @param voucher
	 * @param voucherResult
	 * @return
	 */
	private VoucherListResult populateGiftResponse(GiftingHistory giftingHistory, Voucher voucher, String accountNumber, VoucherListResult voucherResult) {
		
		VoucherGiftDetailsResult giftDetailsResult = new VoucherGiftDetailsResult();
		
		String transactionType = null;
		
		if(null != accountNumber) {
			
			if(accountNumber.equals(voucher.getGiftDetails().getGiftedAccountNumber())) {
				transactionType = VoucherConstants.VOUCHER_GIFT_SENT;
			}
			
			if(accountNumber.equals(voucher.getAccountNumber())) {
				transactionType = VoucherConstants.VOUCHER_GIFT_RECEIVED;
			}

		}

		if (null != voucher.getGiftDetails()) {
			giftDetailsResult.setIsGift(voucher.getGiftDetails().getIsGift());
			giftDetailsResult.setTransactionType(transactionType);
		}
		
		giftDetailsResult.setGiftId(giftingHistory.getId());
		
		if(null != giftingHistory.getSenderInfo()) {
			giftDetailsResult.setSenderFirstName(giftingHistory.getSenderInfo().getFirstName());
			giftDetailsResult.setSenderLastName(giftingHistory.getSenderInfo().getLastName());
			giftDetailsResult.setSenderAccountNumber(giftingHistory.getSenderInfo().getAccountNumber());
		}
		
		if(null != giftingHistory.getReceiverInfo()) {
			giftDetailsResult.setReceiverFirstName(giftingHistory.getReceiverInfo().getFirstName());
			giftDetailsResult.setReceiverLastName(giftingHistory.getReceiverInfo().getLastName());
			giftDetailsResult.setReceiverAccountNumber(giftingHistory.getReceiverInfo().getAccountNumber());
		}
		
		giftDetailsResult.setReceiverConsumption(giftingHistory.getReceiverConsumption());
		if(null != giftingHistory.getTransactionDate()) {
			giftDetailsResult.setGiftedDate(Utils.convertDateUAETimezone(giftingHistory.getTransactionDate()));
		}
		
		voucherResult.setGiftDetails(giftDetailsResult);
		
		return voucherResult;
	}
	
	/**
	 * This method is used to retrieve offer details for voucher in GET voucher API responses.
	 * @param vouchers
	 * @return
	 */
	private List<OfferCatalog> getOfferCatalogs(List<Voucher> vouchers, String program) {
		
		List<String> offerIdList = new ArrayList<>();
		Set<String> uniqueOfferIds = new HashSet<String>();
		
		for(Voucher voucher : vouchers) {
			if(null != voucher.getOfferInfo()) {
				uniqueOfferIds.add(voucher.getOfferInfo().getOffer());
			}
		}
		
		for(String id : uniqueOfferIds) {
			offerIdList.add(id);
		}
		
		return offerRepository.findByOfferIdListAndProgramCodeIgnoreCase(offerIdList, program);
		//return offerRepository.findByOfferIdList(offerIdList);
		
	}

	private List<GiftingHistory> getGiftingHistory(List<Voucher> vouchers) {
		
		List<String> giftIdList = new ArrayList<>();
		
		for(Voucher voucher : vouchers) {
			if(null != voucher.getGiftDetails() && null != voucher.getGiftDetails().getIsGift()
					&& voucher.getGiftDetails().getIsGift().equalsIgnoreCase(VoucherConstants.YES)) {
				giftIdList.add(voucher.getGiftDetails().getGiftId());
			}
		}
		
		return giftingHistoryRepository.findByGiftIdList(giftIdList);
		
	}
	
	/**
	 * This method is used to retrieve member details for a list of account numbers.
	 * @param vouchers
	 * @param header
	 * @return
	 * @throws VoucherManagementException
	 */
	private List<GetListMemberResponseDto> getMemberAccounts(List<Voucher> vouchers, Headers header) throws VoucherManagementException {
		
		List<String> accountsList = new ArrayList<>();
		Set<String> uniqueAccounts = new HashSet<>();
		
		for(Voucher voucher : vouchers) {
			if(null != voucher.getAccountNumber()) {
				uniqueAccounts.add(voucher.getAccountNumber());
			}
		}
		
		for(String id : uniqueAccounts) {
			accountsList.add(id);
		}
		
		GetListMemberResponse listMemberResponse = voucherMemberManagementService.getListMemberDetails(accountsList, header);
		
		return listMemberResponse.getListMember();
		
	}
	
	
	/**
	 * This method generates voucher and saves it in db
	 * @param voucherRequestDto
	 * @param program
	 * @param offerInfo
	 * @param points
	 * @param expiryDate
	 * @param userName
	 * @param token
	 * @param cost
	 * @param voucherDomain
	 * @param resultResponse
	 * @param externalTransactionId
	 * @param channelId 
	 * @return VoucherResponse
	 * @throws VoucherManagementException
	 */
	
	public VoucherResponse voucherGenerate(VoucherRequestDto voucherRequestDto, String program, OfferInfo offerInfo,
			long points, Date expiryDate, String userName, String token, double cost, VoucherDomain voucherDomain,
			VoucherResponse resultResponse, String externalTransactionId, String channelId) throws VoucherManagementException {
		
		for (int i = 1; i <= voucherRequestDto.getNumberOfVoucher(); i++) {
			String voucherCode = getVoucherCode(voucherRequestDto.getOfferType(), voucherRequestDto.getMerchantCode());
			LOG.info("voucherCode : {}", voucherCode);
			Voucher vou = voucherRepository.findByVoucherCode(voucherCode);
			if (null != vou) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHERS_CODE_NOT_UNIQUE.getIntId(),
						VoucherManagementCode.VOUCHERS_CODE_NOT_UNIQUE.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
				return resultResponse;
			}
			VoucherDomain vouDom = new VoucherDomain.VoucherBuilder(program, voucherCode, offerInfo,
					voucherRequestDto.getMerchantCode(), voucherRequestDto.getMerchantName(),
					voucherRequestDto.getMembershipCode(), String.valueOf(voucherRequestDto.getAccountNumber()),
					voucherRequestDto.getBarcodeType()).uuid(voucherRequestDto.getUuid()).pointsValue(points)
							.voucherAmount(voucherRequestDto.getVoucherAmount()).status(VoucherStatus.ACTIVE)
							.partnerCode(voucherRequestDto.getPartnerCode()).expiryDate(expiryDate)
							.type(voucherRequestDto.getOfferType()).createdDate(new Date())
							.createdUser(null != userName ? userName : token).cost(cost)
							.channel(channelId).build();
			String isBirthdayGift = (voucherRequestDto.getIsBirthdayGift() == null
					|| voucherRequestDto.getIsBirthdayGift().equalsIgnoreCase(OfferConstants.FLAG_NOT_SET.get())) ? "NO"
							: "BIRTHDAY";
			isBirthdayGift = voucherRequestDto.isMambaFoodVoucher() ? "MAMBA" : isBirthdayGift;

			Voucher voucher = voucherDomain.saveVoucher(vouDom, externalTransactionId, userName, isBirthdayGift, channelId);
			LOG.info(LOG_VOUCHER, voucher);
			resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getId(),
					VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getMsg());
			VoucherResult vouRes = new VoucherResult();
			vouRes.setVoucherCode(voucher.getVoucherCode());
			vouRes.setStatus(voucher.getStatus());
			vouRes.setVoucherId(voucher.getId());
			resultResponse.getVoucherResult().add(vouRes);

		}
		return resultResponse;
	}

	/**
	 * This method searches for vouchers that match the specified constraints from the uploaded pool of vouchers
	 * @param voucherRequestDto
	 * @param resultResponse
	 * @param voucherDomain
	 * @param userName
	 * @param token
	 * @param expiryDate
	 * @param cost
	 * @param points
	 * @param externalTransactionId
	 * @return vouchers that match the criteria 
	 * @throws VoucherManagementException
	 */
	public VoucherResponse voucherSearch(VoucherRequestDto voucherRequestDto, VoucherResponse resultResponse,
			VoucherDomain voucherDomain, String userName, String token, Date expiryDate, double cost, long points,
			String externalTransactionId, String channelId) throws VoucherManagementException {
			
		List<Voucher> result = new ArrayList<>();
		int retry = 0;
		boolean updateStatus = false;
		
		while(++retry<=2 
		   && Checks.checkNoErrors(resultResponse)
		   && !updateStatus) {
			
			LOG.info("Retry for searching voucher : attempt {}", retry);
			result = getListVoucherByOfferType(voucherRequestDto, result);
			LOG.info("result : {}", result);
			if (result.isEmpty()) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHERS_NOT_AVAILABLE.getIntId(),
						voucherRequestDto.getOfferId() + ":" + VoucherManagementCode.VOUCHERS_NOT_AVAILABLE.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
				sendEmailForVoucherNotAvailable(voucherRequestDto.getAccountNumber(), voucherRequestDto.getMerchantName(),
						voucherRequestDto.getMerchantEmail(),voucherRequestDto.getOfferId(),externalTransactionId);			
				return resultResponse;
			} else if (result.size() < voucherRequestDto.getNumberOfVoucher()) {			
				resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_VOUCHER_QUANTITY.getIntId(),
						voucherRequestDto.getOfferId() + ":" + VoucherManagementCode.INVALID_VOUCHER_QUANTITY.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
				sendEmailForVoucherNotAvailable(voucherRequestDto.getAccountNumber(), voucherRequestDto.getMerchantName(),
						voucherRequestDto.getMerchantEmail(),voucherRequestDto.getOfferId(),externalTransactionId);			
				return resultResponse;
			
			} else if (result.size() >= voucherRequestDto.getNumberOfVoucher()) {
				
				List<VoucherResult> voucherResult = result.stream().map(u -> {
					VoucherResult ur = new VoucherResult();
					ur.setStatus(VoucherStatus.ACTIVE);
					ur.setVoucherCode(u.getVoucherCode());
					ur.setVoucherId(u.getId());
					return ur;
				}).collect(Collectors.toList());

				updateStatus = voucherDomain.updateVoucherWithStatus(result, voucherRequestDto, null != userName ? userName : token,
						expiryDate, cost, points, externalTransactionId, retry, resultResponse, result.size() == voucherRequestDto.getNumberOfVoucher(),
						channelId);
				
				if(updateStatus) {
					
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getId(),
							VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getMsg());
					LOG.info("voucherResult : {}", voucherResult);
					resultResponse.getVoucherResult().addAll(voucherResult);
				
				} else if(retry==2) {
					
					if(result.size() == voucherRequestDto.getNumberOfVoucher()) {
						
						resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHERS_NOT_AVAILABLE.getIntId(),
							voucherRequestDto.getOfferId() + ":" + VoucherManagementCode.VOUCHERS_NOT_AVAILABLE.getMsg());
						resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
							VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
						sendEmailForVoucherNotAvailable(voucherRequestDto.getAccountNumber(), voucherRequestDto.getMerchantName(),
							voucherRequestDto.getMerchantEmail(),voucherRequestDto.getOfferId(),externalTransactionId);			
					} else {
						
						resultResponse.addErrorAPIResponse(VoucherManagementCode.COULD_NOT_GENERATE_VOUCHER.getIntId(),
								voucherRequestDto.getOfferId() + ":" + VoucherManagementCode.COULD_NOT_GENERATE_VOUCHER.getMsg());
						resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
								VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
					}
					
				}
				
			}

		}
				
		return resultResponse;
	}

	private void sendEmailForVoucherNotAvailable(String accountNumber, String merchantName, List<String> email,
			String offerId, String externalTransactionId) throws VoucherManagementException {
		try {
			LOG.info("Send email : {}", email);
			if (email != null && !email.isEmpty() && !email.get(0).equalsIgnoreCase("null")) {
				voucherService.sendEmail(accountNumber, merchantName, email.get(0), offerId, externalTransactionId);
			}

		} catch (VoucherManagementException e) {
			throw new VoucherManagementException(this.getClass().toString(), "voucherSearch",
					VoucherManagementCode.VOUCHER_EMAIL_FAILED.getMsg(), VoucherManagementCode.VOUCHER_EMAIL_FAILED);
		}
	}

	private List<Voucher> getListVoucherByOfferType(VoucherRequestDto voucherRequestDto, List<Voucher> result) {

		Calendar todayBeginning = Calendar.getInstance();
		todayBeginning.setTime(new Date());
		todayBeginning.set(Calendar.HOUR_OF_DAY, 0);  
		todayBeginning.set(Calendar.MINUTE, 0);  
		todayBeginning.set(Calendar.SECOND, 0);  
		todayBeginning.set(Calendar.MILLISECOND, 0); 
		
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(new Date());
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);  
		todayEnd.set(Calendar.MINUTE, 59);  
		todayEnd.set(Calendar.SECOND, 59);  
		todayEnd.set(Calendar.MILLISECOND, 999); 
		
		
		if (voucherRequestDto.getOfferTypeId().equalsIgnoreCase(VoucherConstants.DEAL_OFFER_ID)) {
			result = voucherRepository.findBySubOfferId(voucherRequestDto.getMerchantCode(),
					voucherRequestDto.getOfferId(), voucherRequestDto.getSubOfferId(), todayBeginning.getTime(),
					todayEnd.getTime(), VoucherStatus.READY, VoucherConstants.VOUCHER_TYPE,
					PageRequest.of(0, voucherRequestDto.getNumberOfVoucher()));
		} else if (voucherRequestDto.getOfferTypeId().equalsIgnoreCase(VoucherConstants.DISCOUNT_OFFER_ID)) {
			result = voucherRepository.findByOfferId(voucherRequestDto.getMerchantCode(),
					voucherRequestDto.getOfferId(), todayBeginning.getTime(), todayEnd.getTime(), VoucherStatus.READY,
					VoucherConstants.VOUCHER_TYPE, PageRequest.of(0, voucherRequestDto.getNumberOfVoucher()));
		} else if (voucherRequestDto.getOfferTypeId().equalsIgnoreCase(VoucherConstants.CASH_OFFER_ID)) {
			result = voucherRepository.findByOfferIdCashVoucher(voucherRequestDto.getMerchantCode(),
					voucherRequestDto.getVoucherAmount(), voucherRequestDto.getOfferId(), todayBeginning.getTime(),
					todayEnd.getTime(), VoucherStatus.READY, VoucherConstants.VOUCHER_TYPE,
					PageRequest.of(0, voucherRequestDto.getNumberOfVoucher()));
		}
		return result;
	}

	/**
	 * This method gets vouchers from the specified partner
	 * @param voucherRequestDto
	 * @param resultResponse
	 * @param program
	 * @param offerInfo
	 * @param points
	 * @param expiryDate
	 * @param userName
	 * @param token
	 * @param cost
	 * @param voucherDomain
	 * @param externalTransactionId
	 * @throws VoucherManagementException
	 */
	public VoucherResponse voucherAsk(VoucherRequestDto voucherRequestDto, VoucherResponse resultResponse,
			String program, OfferInfo offerInfo, long points, Date expiryDate, String userName, String token,
			double cost, VoucherDomain voucherDomain, String externalTransactionId, String channelId) throws VoucherManagementException {
						
		String isBirthdayGift = voucherDomain.birthdayGiftValue(voucherRequestDto);

		for (int i = 1; i <= voucherRequestDto.getNumberOfVoucher(); i++) {

			PlaceVoucherOrderResponse placeVoucherOrderResponse;
			if (voucherRequestDto.getPartnerCode().equalsIgnoreCase(VoucherConstants.MAF)) {
				LOG.info("Inside MAF Partner");
				placeVoucherOrderResponse = mafService.getMafVoucherDetails(voucherRequestDto.getVoucherDenomination(),
						externalTransactionId);
				if (placeVoucherOrderResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS)) {
					VoucherDomain vouDom = new VoucherDomain.VoucherBuilder(program, placeVoucherOrderResponse.getResponseData().getVoucherDetail().getOrderReferenceNumber(), offerInfo,
							voucherRequestDto.getMerchantCode(), voucherRequestDto.getMerchantName(),
							voucherRequestDto.getMembershipCode(), String.valueOf(voucherRequestDto.getAccountNumber()),
							voucherRequestDto.getBarcodeType()).uuid(voucherRequestDto.getUuid()).pointsValue(points)
									.voucherAmount(voucherRequestDto.getVoucherAmount()).status(VoucherStatus.ACTIVE)
									.partnerCode(VoucherConstants.MAF).expiryDate(expiryDate)
									.type(voucherRequestDto.getOfferType())
									.partnerReferNumber(placeVoucherOrderResponse.getResponseData().getVoucherDetail().getVoucherID())
									.partnerTransactionId(placeVoucherOrderResponse.getResponseData().getTransactionID())
									.createdDate(new Date()).createdUser(null != userName ? userName : token)
									.cost(cost).channel(channelId).build();
					
					Voucher voucher = voucherDomain.saveVoucher(vouDom, externalTransactionId, userName, isBirthdayGift, channelId);
					LOG.info(LOG_VOUCHER, voucher);
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getId(),
							VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getMsg());
					VoucherResult vouRes = new VoucherResult();
					vouRes.setVoucherCode(voucher.getVoucherCode());
					vouRes.setStatus(voucher.getStatus());
					vouRes.setVoucherId(voucher.getId());
					resultResponse.getVoucherResult().add(vouRes);

				} else {
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
							VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
					resultResponse.addErrorAPIResponse(VoucherManagementCode.MAF_SERVICE_ERROR.getIntId(),
							VoucherManagementCode.MAF_SERVICE_ERROR.getMsg());
					return resultResponse;
				}

			} else if (voucherRequestDto.getPartnerCode().equalsIgnoreCase(VoucherConstants.CARREFOUR)) {
				LOG.info("Inside CARREFOUR Partner");
				CarrefourGCRequestResponse carrefourGCResponse = carreFourService.getCarreFourVoucherDetails(voucherRequestDto.getVoucherDenomination(), externalTransactionId);
				if(null != carrefourGCResponse) {
					LOG.info("Saving Voucher");				 
					VoucherDomain vouDom = new VoucherDomain.VoucherBuilder(program, carrefourGCResponse.getResponseData().getBarCodeNumber(), offerInfo,
							voucherRequestDto.getMerchantCode(), voucherRequestDto.getMerchantName(),
							voucherRequestDto.getMembershipCode(), String.valueOf(voucherRequestDto.getAccountNumber()),
							voucherRequestDto.getBarcodeType()).uuid(voucherRequestDto.getUuid()).pointsValue(points)
									.voucherAmount(voucherRequestDto.getVoucherAmount()).status(VoucherStatus.ACTIVE)
									.partnerCode(VoucherConstants.CARREFOUR).expiryDate(expiryDate)
									.type(voucherRequestDto.getOfferType())
									.partnerReferNumber(carrefourGCResponse.getResponseData().getReferenceNumber())
									.partnerTransactionId(carrefourGCResponse.getResponseData().getTransactionID())
									.createdDate(new Date()).createdUser(null != userName ? userName : token)
									.cost(cost).channel(channelId).build();
					Voucher voucher = voucherDomain.saveVoucher(vouDom, externalTransactionId, userName, isBirthdayGift, channelId);
					LOG.info(LOG_VOUCHER, voucher);
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getId(),
							VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getMsg());
					VoucherResult vouRes = new VoucherResult();
					vouRes.setVoucherCode(voucher.getVoucherCode());
					vouRes.setStatus(voucher.getStatus());
					vouRes.setVoucherId(voucher.getId());
					resultResponse.getVoucherResult().add(vouRes);
				} else {
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
							VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
					resultResponse.addErrorAPIResponse(VoucherManagementCode.CARREFOUR_SERVICE_ERROR.getIntId(),
							VoucherManagementCode.CARREFOUR_SERVICE_ERROR.getMsg());
					return resultResponse;
				}
				
			} else if (voucherRequestDto.getPartnerCode().equalsIgnoreCase(VoucherConstants.YGAG)) {
				LOG.info("Inside YGAG Partner");
				DownloadYGAGVoucherResponse ygagResponse = ygagService.getYGAGVoucherDetails(voucherRequestDto, externalTransactionId);
				if (ygagResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS)) {
					LOG.info("Saving YGAG Voucher");
					VoucherDomain vouDom = new VoucherDomain.VoucherBuilder(program, ygagResponse.getGiftVoucher().getCode(), offerInfo,
							voucherRequestDto.getMerchantCode(), voucherRequestDto.getMerchantName(),
							voucherRequestDto.getMembershipCode(), String.valueOf(voucherRequestDto.getAccountNumber()),
							voucherRequestDto.getBarcodeType()).uuid(voucherRequestDto.getUuid()).pointsValue(points)
									.voucherAmount(voucherRequestDto.getVoucherAmount()).status(VoucherStatus.ACTIVE)
									.partnerCode(VoucherConstants.YGAG).expiryDate(expiryDate)
									.type(voucherRequestDto.getOfferType())
									.partnerReferNumber(ygagResponse.getReferenceId())
									.partnerTransactionId(ygagResponse.getTransactionID())
									.createdDate(new Date()).createdUser(null != userName ? userName : token)
									.cost(cost).channel(channelId).voucherPin(ygagResponse.getGiftVoucher().getPin())
									.build();
					Voucher voucher = voucherDomain.saveVoucher(vouDom, externalTransactionId, userName, isBirthdayGift, channelId);
					LOG.info(LOG_VOUCHER, voucher);
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getId(),
							VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getMsg());
					VoucherResult vouRes = new VoucherResult();
					vouRes.setVoucherCode(voucher.getVoucherCode());
					vouRes.setStatus(voucher.getStatus());
					vouRes.setVoucherId(voucher.getId());
					resultResponse.getVoucherResult().add(vouRes);

				} else {
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
							VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
					resultResponse.addErrorAPIResponse(VoucherManagementCode.YGAG_SERVICE_ERROR.getIntId(),
							VoucherManagementCode.YGAG_SERVICE_ERROR.getMsg());
					return resultResponse;
				}

			} else if (voucherRequestDto.getPartnerCode().equalsIgnoreCase(VoucherConstants.SMLS)) {
				LOG.info("Inside SMLS Partner");
				DownloadSMLSVoucherResponse smlsResponse = smlsService.fetchSMLSVoucher(voucherRequestDto, externalTransactionId, expiryDate);
				if (null != smlsResponse && smlsResponse.getStatusCode() == VoucherConstants.SMLS_RESPOSNE_CODE) {
					LOG.info("Saving SMLS Voucher");
					VoucherDomain vouDom = new VoucherDomain.VoucherBuilder(program, smlsResponse.getPromoCode(), offerInfo,
							voucherRequestDto.getMerchantCode(), voucherRequestDto.getMerchantName(),
							voucherRequestDto.getMembershipCode(), String.valueOf(voucherRequestDto.getAccountNumber()),
							voucherRequestDto.getBarcodeType()).uuid(voucherRequestDto.getUuid()).pointsValue(points)
									.voucherAmount(voucherRequestDto.getVoucherAmount()).status(VoucherStatus.ACTIVE)
									.partnerCode(VoucherConstants.SMLS).expiryDate(expiryDate)
									.type(voucherRequestDto.getOfferType())
									.partnerTransactionId(smlsResponse.getExternalTransactionId())
									.createdDate(new Date()).createdUser(null != userName ? userName : token).cost(cost).build();
					Voucher voucher = voucherDomain.saveVoucher(vouDom, externalTransactionId, userName, isBirthdayGift, channelId);
					LOG.info(LOG_VOUCHER, voucher);
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getId(),
							VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getMsg());
					VoucherResult vouRes = new VoucherResult();
					vouRes.setVoucherCode(voucher.getVoucherCode());
					vouRes.setStatus(voucher.getStatus());
					vouRes.setVoucherId(voucher.getId());
					resultResponse.getVoucherResult().add(vouRes);

				} else {
					resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
							VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
					resultResponse.addErrorAPIResponse(VoucherManagementCode.SMLS_SERVICE_ERROR.getIntId(),
							VoucherManagementCode.SMLS_SERVICE_ERROR.getMsg());
					return resultResponse;
				}

			} else {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
				resultResponse.addErrorAPIResponse(VoucherManagementCode.PARTNER_NOTCONFIGURED.getIntId(),
						VoucherManagementCode.PARTNER_NOTCONFIGURED.getMsg());
				return resultResponse;
			}

		}
		return resultResponse;
	}

	/**
	 * This method uploads vouchers
	 * @param fileContent
	 * @param file
	 * @param voucherDomain
	 * @param resultResponse
	 * @param header
	 * @throws ParseException
	 * @throws VoucherManagementException
	 */
	public VoucherUploadResponse prepareAndUploadVoucherData(List<String> fileContent, String fileName,
			VoucherDomain voucherDomain, VoucherUploadResponse resultResponse, Headers header)
			throws ParseException, VoucherManagementException {
		List<VoucherUploadDto> inputList;
		StringBuilder dataFileContent = new StringBuilder();
		List<StringBuilder> dataFeedbackFile = new ArrayList<>();
		
		List<String> voucherCodeList = fileContent.stream().map(e -> e.substring(0, e.indexOf(';'))).collect(Collectors.toList());
		Set<String> voucherCodeSet = new HashSet<>(voucherCodeList);
		if(voucherCodeSet.size()<voucherCodeList.size()) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getIntId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getMsg());
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getMsg());
			return resultResponse;
		}
		
		//Updated for Time Out by Arindam
		
		String[] contentStringArray = new String[9];
		String merchantCode = null;
		String offerCode = null;
		for (String s : fileContent) {
			//System.out.println("file content String : {}"+ s);
			String[] intermediate = s.split(";");
			System.arraycopy(intermediate, 0, contentStringArray, 0, intermediate.length);
			if(null == merchantCode || merchantCode.equalsIgnoreCase(contentStringArray[3])) {
				merchantCode = contentStringArray[3];
			} else {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FILE_MERCHANT_CODE_MULTIPLE.getIntId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FILE_MERCHANT_CODE_MULTIPLE.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_MERCHANT_CODE_MULTIPLE.getId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FILE_MERCHANT_CODE_MULTIPLE.getMsg());
				return resultResponse;				
			}
			
			if(null == offerCode || offerCode.equalsIgnoreCase(contentStringArray[5])) {
				offerCode = contentStringArray[5];
			} else {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_MULTIPLE.getIntId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_MULTIPLE.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_MULTIPLE.getId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_MULTIPLE.getMsg());
				return resultResponse;				
			}
		}
		
		List<Voucher> vouchersList = voucherRepository.findByVoucherCodeIn(voucherCodeList);
		LOG.info("prepareAndUploadVoucherData :: vouchersList : {}",vouchersList);
		if(!vouchersList.isEmpty()) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getIntId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getMsg());
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE.getMsg());
			return resultResponse;	
		}

		// Validate Offer and Partner code added by Arindam
		OfferCatalog offerCatalog = offerRepository.findByOfferId(offerCode);
		if(null != offerCatalog) {
			LOG.info("voucherControllerHelper :: checkPartnerTypeExists : {}",offerCatalog.getPartnerCode());
			boolean exists = partnerService.checkPartnerTypeExists(offerCatalog.getPartnerCode(),header);
			if (!exists) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FILE_PARTNER_NOT_REDEMPTION.getIntId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FILE_PARTNER_NOT_REDEMPTION.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_PARTNER_NOT_REDEMPTION.getId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FILE_PARTNER_NOT_REDEMPTION.getMsg());
				return resultResponse;
			}
		} else {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_NOT_EXIST.getIntId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_NOT_EXIST.getMsg());
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_NOT_EXIST.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_OFFER_ID_NOT_EXIST.getMsg());
			return resultResponse;
		}
		
		for (String s : fileContent) {
			//LOG.info("upload voucher file content : {}", s);
			dataFileContent = dataFileContent.append(s).append("\n");
			dataFeedbackFile.add(validateVoucherFile(s,offerCatalog,header));			
		}

		StringBuilder handBack = new StringBuilder();
		for (StringBuilder s : dataFeedbackFile) {
			handBack = handBack.append(s).append("\n");
		}
		inputList = fileContent.stream().map(mapToItem).collect(Collectors.toList());

		inputList.stream().forEach(e -> {
			if(offerCatalog.getMerchant()!=null && offerCatalog.getMerchant().getMerchantName()!=null) {
				e.setMerchantName(offerCatalog.getMerchant().getMerchantName().getMerchantNameEn());
			} else {
				e.setMerchantName("");
			}			
			if(offerCatalog.getOfferType() !=null && offerCatalog.getOfferType().getOfferDescription()!=null) {
				e.setOfferType(offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn());
			} else {
				e.setOfferType("");
			}
			e.setPartnerCode(offerCatalog.getPartnerCode());
		});
		String uploadFilestatus;
		if (dataFeedbackFile.toString().contains(VoucherConstants.HANDBACK_FILE_ERROR)) {
			uploadFilestatus = "Fail";
		} else {
			uploadFilestatus = "Success";
		}
		
		VoucherUploadFileDomain uploadDomain = new VoucherUploadFileDomain.VoucherUploadFileBuilder(header.getProgram(),
				fileName, inputList.get(0).getMerchantCode(), null, new Date(), "Upload Voucher",
				dataFileContent.toString(), uploadFilestatus, handBack.toString(), new Date(), header.getUserName()).build();
		voucherUploadFileDomain.saveVoucherUpload(uploadDomain, header.getExternalTransactionId(), header.getUserName());

		if (!dataFeedbackFile.toString().contains(VoucherConstants.HANDBACK_FILE_ERROR)) {
			for (VoucherUploadDto i : inputList) {
				OfferInfo offerInfo = new OfferInfo();
				offerInfo.setOffer(i.getOfferId());
				offerInfo.setSubOffer(i.getSubOfferId());
				VoucherDomain vouDom = new VoucherDomain.VoucherBuilder(header.getProgram(), i.getVoucherCode(), offerInfo,
						i.getMerchantCode(), i.getMerchantName(), null, null, null).uuid(null).pointsValue(0)
								.voucherAmount(i.getDenomination()).status(VoucherStatus.READY)
								.expiryDate(i.getExpiryDate()).createdDate(new Date()).createdUser(header.getUserName()).cost(null)
								.startDate(i.getStartDate()).endDate(i.getEndDate()).uploadDate(i.getUploadDate()).partnerCode(i.getPartnerCode())
								.type(i.getOfferType()).fileName(fileName)
								.build();
				voucherDomain.uploadVoucher(vouDom, header.getExternalTransactionId(), header.getUserName());
			}

			resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
					VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());
			return resultResponse;
		} else {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_CONTENT.getIntId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_CONTENT.getMsg());
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_CONTENT.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_CONTENT.getMsg());
			return resultResponse;
		}

	}

	/**
	 * This method prepares data for generating handback file after voucher upload
	 * @param voucherUploadFile
	 * @param fileContentList
	 * @param res
	 * @throws ParseException
	 */
	public List<VoucherUploadDto> prepareDataForHandbackDownload(VoucherUploadFile voucherUploadFile, List<String[]> fileContentList,
			List<VoucherUploadDto> res) throws ParseException {
		String[] handbackFileSplitByLine = voucherUploadFile.getHandbackFile().split("\n");
		for (String content : handbackFileSplitByLine) {
			fileContentList.add(content.split(";"));
		}

		for (int i = 0; i < fileContentList.size(); i++) {
			VoucherUploadDto voucherUploadDto = new VoucherUploadDto();
			voucherUploadDto.setVoucherCode(fileContentList.get(i)[0]);
			if (fileContentList.get(i)[1] != null) 	{
				voucherUploadDto.setUploadDate(new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(fileContentList.get(i)[1]));
			}
			
			if (fileContentList.get(i)[2] != null && !fileContentList.get(i)[2].isEmpty()) {
				voucherUploadDto.setDenomination(Double.parseDouble(fileContentList.get(i)[2]));
			} else {
				voucherUploadDto.setDenomination(null);
			}
			
			voucherUploadDto.setMerchantCode(fileContentList.get(i)[3]);
			if (fileContentList.get(i)[4] != null && !fileContentList.get(i)[4].isEmpty()) {
				voucherUploadDto.setExpiryDate(new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(fileContentList.get(i)[4]));
			}
			voucherUploadDto.setOfferId(fileContentList.get(i)[5]);
			if (fileContentList.get(i)[6] != null) {
				voucherUploadDto.setStartDate(new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(fileContentList.get(i)[6]));
			}
			if (fileContentList.get(i)[7] != null && !fileContentList.get(i)[7].isEmpty()) {
				voucherUploadDto.setEndDate(new SimpleDateFormat(VoucherConstants.DATE_FORMAT).parse(fileContentList.get(i)[7]));
			}
			voucherUploadDto.setSubOfferId(fileContentList.get(i)[8]);
			voucherUploadDto.setStatus(fileContentList.get(i)[9]);
			voucherUploadDto.setError(fileContentList.get(i)[10]);
			res.add(voucherUploadDto);
		}

		return res;
	}

	/**
	 * This method generates free voucher for the specified account numbers/loyalty id/membership code
	 * @param file
	 * @param fileContent
	 * @param offerId
	 * @param voucherDomain
	 * @param resultResponse
	 * @param header
	 * @throws VoucherManagementException
	 */
	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_FREE_VOUCHER)
	public VoucherUploadResponse prepareAndGenerateFreeVoucher(MultipartFile file, List<String> fileContent,
			String offerId, VoucherUploadResponse resultResponse, Headers header) throws VoucherManagementException {

		StringBuilder dataFileContent = new StringBuilder();
		List<StringBuilder> dataFeedbackFile = new ArrayList<>();

		OfferCatalog off = offerRepository.findByOfferIdStatusFree(offerId, "Active");

		if (off == null) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_VALID.getIntId(),
					VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_VALID.getMsg());
			resultResponse.setResult(VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_VALID.getId(),
					VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_VALID.getMsg());
			return resultResponse;
		}
		if (!off.getOfferType().getOfferTypeId().equalsIgnoreCase(VoucherConstants.DISCOUNT_OFFER_ID)
		&& !off.getOfferType().getOfferTypeId().equalsIgnoreCase(VoucherConstants.CASH_OFFER_ID)) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_DEAL.getIntId(),
					VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_DEAL.getMsg());
			resultResponse.setResult(VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_DEAL.getId(),
					VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_DEAL.getMsg());
			return resultResponse;
		}
		
		if(fileContent.get(0)!=null) {
			LOG.info("fileContent.get(0) : {}",fileContent.get(0));
			String[] firstLine = fileContent.get(0).split(";");
			if(firstLine[0].length()==1) {
				fileContent.set(0,fileContent.get(0).substring(1));			
			}
			
		}
		for (String s : fileContent) {
			dataFileContent = dataFileContent.append(s).append("\n");
			LOG.info("File content to be validated is : {} ",s);
			dataFeedbackFile.add(validateAccountFreeVoucherFile(s, off, header));
		}
		StringBuilder handBack = new StringBuilder();
		for (StringBuilder s : dataFeedbackFile) {
			handBack = handBack.append(s).append("\n");
		}
		String uploadFilestatus = "";
		if (dataFeedbackFile.toString().contains(VoucherConstants.HANDBACK_FILE_ERROR)) {
			uploadFilestatus = "Fail";
		} else {
			uploadFilestatus = "Success";
		}
		
		VoucherUploadFileDomain uploadDomain = new VoucherUploadFileDomain.VoucherUploadFileBuilder(header.getProgram(),
				file.getOriginalFilename(), null, offerId, new Date(), VoucherConstants.GENERATE_FREE_VOUCHER,
				dataFileContent.toString(), uploadFilestatus, handBack.toString(), new Date(), header.getUserName()).build();
		voucherUploadFileDomain.saveVoucherUpload(uploadDomain, header.getExternalTransactionId(), header.getUserName());
		
		if (dataFeedbackFile.toString().contains(VoucherConstants.HANDBACK_FILE_ERROR_EXT_TRANS_ID)) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_EXT_TRANSACTIONID.getIntId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_EXT_TRANSACTIONID.getMsg());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_EXT_TRANSACTIONID.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_EXT_TRANSACTIONID.getMsg());
			return resultResponse;

		} else if (dataFeedbackFile.toString().contains(VoucherConstants.HANDBACK_FILE_ERROR_DENOMINATION)) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_DENOMINATION.getIntId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_DENOMINATION.getMsg());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_DENOMINATION.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_DENOMINATION.getMsg());
			return resultResponse;

		} else if (dataFeedbackFile.toString().contains(VoucherConstants.HANDBACK_FILE_ERROR)) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_CONTENT.getIntId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_CONTENT.getMsg());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_CONTENT.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FAILED_INVALID_CONTENT.getMsg());
			return resultResponse;

		} else {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
					VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());
			return resultResponse;
		}

	}

	private StringBuilder validateAccountFreeVoucherFile(String uploadFileContent, OfferCatalog off, Headers header) {

		String result = "";
		StringBuilder dataFeedback = new StringBuilder().append(uploadFileContent).append(";");
		String[] intermediate = uploadFileContent.split(";");
		LOG.info("intermediate[0] : {}",intermediate[0]);
		String concatIntermediate = intermediate[0].concat(",");
		LOG.info("concatIntermediate : {}",concatIntermediate);
		String[] contentStringArray = new String[5];
		contentStringArray = concatIntermediate.split(",");
//		String[] contentStringArray = new String[5];
//		String[] intermediate = uploadFileContent.split(";");
//		System.arraycopy(intermediate, 0, contentStringArray, 0, intermediate.length);
		
		LOG.info("Array size : {}", contentStringArray.length);
		Integer denomination = null;
//		System.arraycopy(intermediate, 0, contentStringArray, 0, intermediate.length);
		if (contentStringArray.length>=1 
		&&  contentStringArray[0] != null 
		&& !contentStringArray[0].isEmpty()) {
			contentStringArray[0] = contentStringArray[0].replaceAll("[^0-9]", "");
			LOG.info("contentStringArray[0] accountNumber : {}",contentStringArray[0]);
		}
		
		if (contentStringArray[3] != null && !contentStringArray[3].isEmpty()) {
			LOG.info("contentStringArray[3] extTransactionId : {}",contentStringArray[3]);
			header.setExternalTransactionId(contentStringArray[3]);
		} else {
			dataFeedback.append(VoucherConstants.HANDBACK_FILE_ERROR_EXT_TRANS_ID);
		}
		
		if(off.getOfferType().getOfferTypeId().equalsIgnoreCase(VoucherConstants.CASH_OFFER_ID)) {
			if (contentStringArray.length>=4 
			&&  contentStringArray[4] != null && !contentStringArray[4].isEmpty()) {
				contentStringArray[4] = contentStringArray[4].replaceAll("[^0-9]", "");
				LOG.info("contentStringArray[4] denomination : {}",contentStringArray[4]);
				denomination = Integer.parseInt(contentStringArray[4]);
				LOG.info("denomination : {}",denomination);
			} else {
				dataFeedback.append(VoucherConstants.HANDBACK_FILE_ERROR_DENOMINATION);		
			}
		}
		
		if (contentStringArray.length>=1 
		&&  contentStringArray[0] != null && !contentStringArray[0].isEmpty()) {				
			result = checkMemberAndEnroll(contentStringArray[0], contentStringArray[0], "accountNumber", off, header, denomination);
			if (result.contains("fail")) {
				dataFeedback.append(VoucherConstants.HANDBACK_FILE_ERROR).append(result);
			} else {
				dataFeedback.append(OK_NO_ERROR);
			}

		} else if (contentStringArray.length>=2 
			   &&  contentStringArray[1] != null && !contentStringArray[1].isEmpty()) {
			result = checkMemberAndEnroll(contentStringArray[0],contentStringArray[1], "membershipCode", off, header, denomination);
			if (result.contains("fail")) {
				dataFeedback.append(VoucherConstants.HANDBACK_FILE_ERROR).append(result);
			} else {
				dataFeedback.append(OK_NO_ERROR);
			}

		} else if (contentStringArray.length>=3 
			   &&  contentStringArray[2] != null && !contentStringArray[2].isEmpty()) {
			result = checkMemberAndEnroll(contentStringArray[0], contentStringArray[2], "loyaltyId",  off, header, denomination);
			if (result.contains("fail")) {
				dataFeedback.append(VoucherConstants.HANDBACK_FILE_ERROR).append(result);
			} else {
				dataFeedback.append(OK_NO_ERROR);
			}
		} 

		return dataFeedback;
	}

	private String checkMemberAndEnroll(String accNumber, String code, String typeOfId, 
			OfferCatalog offCatalog, Headers header, Integer denomination) {	
		LOG.info("Inside checkMemberAndEnroll");
				
		MemberResponseDto getMemberResponse;
		try {
			getMemberResponse = voucherMemberManagementService.getMemberDetails(code, typeOfId,header);
		} catch (VoucherManagementException e1) {
			return e1.getMessage() + "fail";
		}
		ResultResponse resResponse = new ResultResponse(header.getExternalTransactionId());
		EnrollmentResultResponse enrollmentResultResponse;

		try {
			if (null != getMemberResponse) {				
				if (getMemberResponse.getMemberInfo().getStatus().equalsIgnoreCase("Active") || getMemberResponse.getMemberInfo().getStatus().equalsIgnoreCase("Suspend")  ) {					
					
					return generateVouchers(getMemberResponse.getAccountsInfo().get(0).getAccountNumber(), getMemberResponse.getMemberInfo().getMembershipCode(), 
							denomination, header, resResponse,offCatalog);
				}
				else{
					return "fail, Account is not in active or suspended state";
				}									 
			} else {				
				if(accNumber!=null  && !accNumber.isEmpty()) {
					enrollmentResultResponse = voucherMemberManagementService.memberEnroll(accNumber, header);					
					if (!enrollmentResultResponse.getEnrollmentResult().getMessage().contains("fail")) {
						
						return generateVouchers(accNumber, enrollmentResultResponse.getEnrollmentResult().getMembershipCode(), 
								denomination,header,resResponse,offCatalog);
					} else {
						return enrollmentResultResponse.getEnrollmentResult().getMessage();
					}
				} else {
					return "fail, Account number is required for enrolling a member";
				}
			}
		} catch (Exception e) {		
			return e.getMessage() + "fail";
		}
	}

	private String generateVouchers( String accountNum, String memCode, Integer denomination, Headers header, ResultResponse resResponse, OfferCatalog offCatalog) {

		LOG.info("Generating vouchers for account number {} and membership code {}",accountNum,memCode);
		String response ="";		
		PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setSelectedPaymentItem("coupon");
		purchaseRequestDto.setSelectedOption("fullPoints");
		purchaseRequestDto.setOfferId(offCatalog.getOfferId());
		purchaseRequestDto.setSpentAmount(0.0);
		purchaseRequestDto.setSpentPoints(0);
		purchaseRequestDto.setCouponQuantity(1);
		purchaseRequestDto.setAccountNumber(accountNum);		
		purchaseRequestDto.setMembershipCode(memCode);
		purchaseRequestDto.setVoucherDenomination(denomination);
		purchaseRequestDto.setExtTransactionId(header.getExternalTransactionId());
		
		try {
			String voucherId = offersHelper.generateFreeVoucher(offCatalog, purchaseRequestDto, header, null, resResponse);
			LOG.info("Voucher id {}", voucherId);
			LOG.info("Response is {}", resResponse);

			if (voucherId == null || voucherId.isEmpty()) {
				response = "fail, could not generate a voucher," + resResponse.getApiStatus().getErrors().toString();
			} else {
				response = "Success";
			}
		} catch (Exception e) {
			response = "fail, " + e.getMessage();
		}
		return response;
	}
	private OfferInfo createOfferInfoDto(String offerId) {
		OfferInfo offerInfo = new OfferInfo();
		offerInfo.setOffer(offerId);
		offerInfo.setSubOffer(null);
		return offerInfo;
	}

	private String calculateExpiryDate(Date startDate, OfferCatalog offCatalog) {
		String expiryDate = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(VoucherConstants.DATE_FORMAT);
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		if (offCatalog.getVoucherInfo().getVoucherExpiryPeriod() != null) {
			expiryDate = (startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
					.plusDays(offCatalog.getVoucherInfo().getVoucherExpiryPeriod()).format(formatter);
		} else {
			if (offCatalog.getOfferType().getOfferTypeId().equalsIgnoreCase(VoucherConstants.DISCOUNT_OFFER_ID)) {
				expiryDate = (startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.plusDays(discountVoucherPeriod).format(formatter);

			} else if (offCatalog.getOfferType().getOfferTypeId().equalsIgnoreCase(VoucherConstants.CASH_OFFER_ID)) {
				expiryDate = (startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
						.plusDays(cashVoucherPeriod).format(formatter);
			}
		}
		return expiryDate;
	}

	private VoucherRequestDto createVoucherRequestDto(OfferCatalog offCatalog, String offerId, Date setExpiryDate ) {
		VoucherRequestDto voucherRequestDto = new VoucherRequestDto();
		voucherRequestDto.setCost(0.0);
		voucherRequestDto.setNumberOfVoucher(1);
		voucherRequestDto.setOfferId(offerId);
		voucherRequestDto.setPointsValue(0);
		voucherRequestDto.setVoucherAmount(offCatalog.getVoucherInfo().getVoucherAmount());
		voucherRequestDto.setMerchantName(offCatalog.getMerchant().getMerchantName().getMerchantNameEn());
		voucherRequestDto.setMerchantCode(offCatalog.getMerchant().getMerchantCode());
		voucherRequestDto.setOfferType(offCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn());
		voucherRequestDto.setPartnerCode(offCatalog.getPartnerCode());
		voucherRequestDto.setVoucherExpiryDate(setExpiryDate);
		voucherRequestDto.setBarcodeType(offCatalog.getMerchant().getBarcodeType());
		voucherRequestDto.setUuid(null);
		return voucherRequestDto;
	}

	/**
	 * This method returns the details about a voucher that has been gifted, for the specified gift id
	 * @param userName
	 * @param giftId
	 * @param externalTransactionId
	 * @throws VoucherManagementException
	 */
	public GiftingHistoryResult getVoucherGiftDetails(String userName, String giftId, String externalTransactionId) throws VoucherManagementException{
		Optional<GiftingHistory> voucherGiftDetails;
		GiftingHistoryResult voucherGiftResult =null ;
		voucherGiftDetails = giftingHistoryRepository.findById(giftId);
		if(voucherGiftDetails.isPresent()) {
			voucherGiftResult = modelMapper.map(voucherGiftDetails.get(), GiftingHistoryResult.class);			
			giftingHistoryDomain.markReceiverConsumption(voucherGiftDetails.get(),userName, externalTransactionId);
		}		
		else {
			throw new VoucherManagementException(VoucherManagementCode.VOUCHER_GIFT_NOT_PRESENT);
		}
		return voucherGiftResult;
	}

	private List<OfferRules> getOfferCatalogsVoucherExpiry(List<VoucherDetails> vouchers) {
		
		List<String> offerIdList = new ArrayList<>();
		Set<String> uniqueOfferIds = new HashSet<String>();
		
		vouchers.stream().forEach(v -> {
			if(null != v.getOffer()) {
				uniqueOfferIds.add(v.getOffer());
			}
		});
		
		offerIdList.addAll(uniqueOfferIds);
		
		Aggregation aggregation = newAggregation(
				project(from(field("Offer"), field("Merchant"), field(OffersDBConstants.OFFER_ID))),
				match(Criteria.where(OffersDBConstants.OFFER_ID).in(offerIdList)));

		AggregationResults<OfferRules> offerResults = mongoOperations.aggregate(aggregation,
                OffersDBConstants.OFFER_CATALOG, OfferRules.class);
			
		return !CollectionUtils.isEmpty(offerResults.getMappedResults()) ? offerResults.getMappedResults() : null;
		
	}
	
	private List<GetListMemberResponseDto> getMemberAccountsVoucherExpiry(List<VoucherDetails> vouchers, Headers header) throws VoucherManagementException {
		
		List<String> accountsList = new ArrayList<>();
		Set<String> uniqueAccounts = new HashSet<>();
		
		vouchers.stream().forEach(v -> {
			if(null != v.getAccountNumber()) {
				uniqueAccounts.add(v.getAccountNumber());
			}
		});
	
		accountsList.addAll(uniqueAccounts);
			
		GetListMemberResponse listMemberResponse = voucherMemberManagementService.getListMemberDetails(accountsList, header);
	
		return listMemberResponse.getListMember();
		
	}
	
	private GetListMemberResponseDto checkMemberActive(List<GetListMemberResponseDto> memberAccounts, VoucherDetails voucher) {		
		
		return memberAccounts.stream().filter(a -> (null != voucher.getAccountNumber() && voucher.getAccountNumber().equals(a.getAccountNumber()))
				&& (null != voucher.getMembershipCode() && voucher.getMembershipCode().equals(a.getMembershipCode()))
				&& (null != a.getAccountStatus() && a.getAccountStatus().equalsIgnoreCase(VoucherConstants.MEMBER_ACTIVE_STATUS))).findAny().orElse(null);
				
	}
	
	/**
	 * This method sends notification for vouchers that are going to be expired soon
	 * @param resultResponse
	 * @param header
	 * 
	 */
	public ResultResponse notifyExpiredVouchers(ResultResponse resultResponse, Headers header) {

		try {

			Date datePlusSix = Utils.getDatePlusSixDays();
			Date datePlusEight = Utils.getDatePlusEightDays();

			Criteria voucherCriteria = Criteria.where("Status").is(VoucherStatus.ACTIVE)
					.andOperator(Criteria.where("ExpiryDate").gt(datePlusSix), Criteria.where("ExpiryDate").lt(datePlusEight));
					
			Aggregation aggregation = newAggregation(
					project(from(field("AccountNumber"), field("MembershipCode"), field("ExpiryDate"), field("VoucherCode"),
							field("OfferInfo.OfferId"), field("MerchantName"), field("Status"))),
					match(voucherCriteria));

			AggregationResults<VoucherDetails> voucherResults = mongoOperations.aggregate(aggregation, "Voucher",
					VoucherDetails.class);
			
			List<VoucherDetails> vouchers = !CollectionUtils.isEmpty(voucherResults.getMappedResults()) ? voucherResults.getMappedResults() : null;
			
			if (null == vouchers) {
				resultResponse.setResult(VoucherManagementCode.NO_VOUCHERS_EXPIRING_SOON.getId(),
						VoucherManagementCode.NO_VOUCHERS_EXPIRING_SOON.getMsg());
				return resultResponse;
			}
				
			List<OfferRules> offers = getOfferCatalogsVoucherExpiry(vouchers);
			List<GetListMemberResponseDto> memberAccounts = getMemberAccountsVoucherExpiry(vouchers, header);
			
			boolean notificationSent = false;
			List<String> vouchersNotified = new ArrayList<>();
			List<String> vouchersNotNotified = new ArrayList<>();
			
			for (VoucherDetails voucher : vouchers) {
	
				if(!Utils.isAfterSevenDays(voucher.getExpiryDate())) continue;
				
				OfferRules offerCatalog = null;
				GetListMemberResponseDto memberDetails = null;
				
				String expiryDate = Utils.getFormattedDate(voucher.getExpiryDate());
				
				if(null != offers && !offers.isEmpty()) offerCatalog = checkOfferExists(offers, voucher);
				if(null != memberAccounts && !memberAccounts.isEmpty()) memberDetails = checkMemberActive(memberAccounts, voucher);
				
				if(!validateVoucherExpiryParameters(voucher, offerCatalog, memberDetails, resultResponse)) {
					vouchersNotNotified.add(voucher.getVoucherCode());
					continue;
				}

				String language = VoucherConstants.NOTIFICATION_LANGUAGE_EN;
				
				if (Utils.validateMemberLanguagePreference(memberDetails) == 1)
					language = VoucherConstants.NOTIFICATION_LANGUAGE_EN;

				if (Utils.validateMemberLanguagePreference(memberDetails) == 2)
					language = VoucherConstants.NOTIFICATION_LANGUAGE_AR;

				Map<String, String> additionalParams = Utils.setVoucherExpiryNotificationParameters(memberDetails,
						voucher, offerCatalog, expiryDate);

				voucherService.pushNotificationVoucherExpiry(header.getExternalTransactionId(), language,
						voucher.getAccountNumber(), voucher.getMembershipCode(), additionalParams);

				vouchersNotified.add(voucher.getVoucherCode());
				
				notificationSent = true;
			
			}

			if(!notificationSent) {
				
				if(vouchersNotNotified.isEmpty()) {
					resultResponse.setResult(VoucherManagementCode.NO_VOUCHERS_EXPIRING_SOON.getId(),
							VoucherManagementCode.NO_VOUCHERS_EXPIRING_SOON.getMsg());
				} else {
					resultResponse.setResult(VoucherManagementCode.NO_NOTIFICATION_SENT_VOUCHER_EXPIRY.getId(),
							VoucherManagementCode.NO_NOTIFICATION_SENT_VOUCHER_EXPIRY.getMsg() + vouchersNotNotified);
				}
				
			} else {
				if(!vouchersNotNotified.isEmpty()) {
				String response = ". Voucher codes to expire in 7 days but no notificaton sent: " + vouchersNotNotified;
					resultResponse.setResult(VoucherManagementCode.VOUCHER_EXPIRY_NOTIFICATION_SUCCESS.getId(),
							VoucherManagementCode.VOUCHER_EXPIRY_NOTIFICATION_SUCCESS.getMsg() + vouchersNotified + response);
				} else {
					resultResponse.setResult(VoucherManagementCode.VOUCHER_EXPIRY_NOTIFICATION_SUCCESS.getId(),
							VoucherManagementCode.VOUCHER_EXPIRY_NOTIFICATION_SUCCESS.getMsg() + vouchersNotified);
				}
			}
			
		} catch (VoucherManagementException vme) {
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			resultResponse.setResult(VoucherManagementCode.NO_NOTIFICATION_SENT_VOUCHER_EXPIRY.getId(),
					VoucherManagementCode.NO_NOTIFICATION_SENT_VOUCHER_EXPIRY.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());
		}

		return resultResponse;

	}

	/**
	 * This method validate voucher expiry API related parameters.
	 * @param voucher
	 * @param offers
	 * @param memberAccounts
	 * @param offerCatalog
	 * @param memberDetails
	 * @param resultResponse
	 * @return
	 */
	private boolean validateVoucherExpiryParameters(VoucherDetails voucher, OfferRules offerCatalog,
			GetListMemberResponseDto memberDetails, ResultResponse resultResponse) {

		if(null == voucher.getOffer()) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_OFFER_ID_FOR_VOUCHER.getIntId(),
					VoucherManagementCode.NO_OFFER_ID_FOR_VOUCHER.getMsg() + voucher.getVoucherCode());
			return false;
		}
		
		if(null == offerCatalog) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_OFFER_ASSOCIATED_FOR_VOUCHER.getIntId(),
					VoucherManagementCode.NO_OFFER_ASSOCIATED_FOR_VOUCHER.getMsg() + voucher.getVoucherCode() + VoucherConstants.COMMA_SEPARATOR + voucher.getOffer());
			return false;
		}
		
		if (null == memberDetails) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_MEMBER_ACCOUNT_NUMBER.getIntId(),
					VoucherManagementCode.NO_MEMBER_ACCOUNT_NUMBER.getMsg() + voucher.getAccountNumber());
			return false;
		}
		
		return true;
		
	}

	/**
	 * This method checks if the offer exists for the given offerId.
	 * @param offers
	 * @param voucher
	 * @return
	 */
	private OfferRules checkOfferExists(List<OfferRules> offers, VoucherDetails voucher) {

		return offers.stream().filter(o -> null != o.getOfferId() && o.getOfferId().equals(voucher.getOffer()))
				.findAny().orElse(null);

	}

	/**
	 * This method checks if member is active.
	 * @param memberAccounts
	 * @param voucher
	 * @return
	 */
	private GetListMemberResponseDto checkMemberActive(List<GetListMemberResponseDto> memberAccounts, Voucher voucher) {		
		for(GetListMemberResponseDto account : memberAccounts) {			
			if ((null != voucher.getAccountNumber() && voucher.getAccountNumber().equals(account.getAccountNumber()))
					&& (null != voucher.getMembershipCode() && voucher.getMembershipCode().equals(account.getMembershipCode()))
					&& (null != account.getAccountStatus() && account.getAccountStatus().equalsIgnoreCase(VoucherConstants.MEMBER_ACTIVE_STATUS))) {				
					return account;	
			}			
		}		
		return null;		
	}
	
	/**
	 * This method provides the number of vouchers sold for customer tiers : rewards, gold, silver and co-branded card.
	 * @param resultResponse
	 * @param header
	 * 
	 */
	public ResultResponse notifyVoucherStatistics(ResultResponse resultResponse, Headers header) {

		try {
			
			int voucherCount = 0;
			int voucherCountSpecial = 0;
			List<String> accounts = new ArrayList<>();
			
			Date todayDate = Utils.getCurrentDate();
			Date tomorrowDate = Utils.getCurrentDatePlusOne();
			
			List<Voucher> activeVouchers = voucherRepository.findByDownloadedDateVoucherStats(todayDate, tomorrowDate);
			if (activeVouchers.isEmpty()) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_DOWNLOADED_TODAY.getIntId(),
						VoucherManagementCode.NO_VOUCHERS_DOWNLOADED_TODAY.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_STATISTICS_NOTIFICATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_STATISTICS_NOTIFICATION_FAILED.getMsg());
				return resultResponse;
			}
			
			List<String> offerIdList = new ArrayList<>();
			Set<String> uniqueOfferIds = activeVouchers.stream().map(voucher -> voucher.getOfferInfo().getOffer()).collect(Collectors.toCollection(HashSet::new));
			uniqueOfferIds.forEach(i -> offerIdList.add(i)); 
			List<OfferRules> offerAndRules = getOffersAndRules(offerIdList);
			
			for (Voucher voucher : activeVouchers) accounts.add(voucher.getAccountNumber());
			
			GetListMemberResponse listMemberResponse = voucherMemberManagementService.getListMemberDetails(accounts, header);
			List<GetListMemberResponseDto> memberList = listMemberResponse.getListMember();
			
			for (Voucher voucher : activeVouchers) {
				
				if(!checkCinemaVoucher(offerAndRules, voucher.getOfferInfo().getOffer())) continue;
				
				for(GetListMemberResponseDto member : memberList) {

					if((null != voucher.getAccountNumber()
							&& voucher.getAccountNumber().equals(member.getAccountNumber()))
							&& (null != voucher.getMembershipCode()
									&& voucher.getMembershipCode().equals(member.getMembershipCode()))) {
						if(Utils.checkMemberTier(member)) {
							voucherCountSpecial++;
						} else {
							voucherCount++;
						}
					}
					
				}
				
			}
				
			voucherService.sendSMSVoucherStatistics(voucherCount, voucherCountSpecial, voucherStatsContacts, header);

			resultResponse.setResult(VoucherManagementCode.VOUCHER_STATISTICS_NOTIFICATION_SUCCESS.getId(),
					VoucherManagementCode.VOUCHER_STATISTICS_NOTIFICATION_SUCCESS.getMsg());

		} catch (VoucherManagementException vme) {
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			resultResponse.setResult(VoucherManagementCode.VOUCHER_STATISTICS_NOTIFICATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_STATISTICS_NOTIFICATION_FAILED.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());
		}

		return resultResponse;

	}
	
	private List<OfferRules> getOffersAndRules(List<String> offerIdList) {
		
		Aggregation aggregation = newAggregation(
				project(from(field(OffersDBConstants.RULES), field("Offer"), field("Merchant"),
						field(OffersDBConstants.OFFER_ID))),
				match(Criteria.where(OffersDBConstants.OFFER_ID).in(offerIdList)));

		AggregationResults<OfferRules> offerResults = mongoOperations.aggregate(aggregation,
				OffersDBConstants.OFFER_CATALOG, OfferRules.class);

		return !CollectionUtils.isEmpty(offerResults.getMappedResults()) ? offerResults.getMappedResults() : null;

	}

	private List<OfferRules> getOffersAndRulesVoucherCount(List<String> offerIdList) {

		Criteria voucherCriteria = Criteria.where(OffersDBConstants.OFFER_ID).in(offerIdList)
				.andOperator(Criteria.where("Status").is(OfferConstants.ACTIVE_STATUS.get()));
				
		Aggregation aggregation = newAggregation(
				project(from(field(OffersDBConstants.RULES), field("Offer"), field("Status"),
						field("Merchant"), field(OffersDBConstants.OFFER_ID))),
				match(voucherCriteria));

		AggregationResults<OfferRules> offerResults = mongoOperations.aggregate(aggregation,
                OffersDBConstants.OFFER_CATALOG, OfferRules.class);
		
		LOG.info("Retrieved Offers: {}", !CollectionUtils.isEmpty(offerResults.getMappedResults()) ? offerResults.getMappedResults() : "none");
		
		return !CollectionUtils.isEmpty(offerResults.getMappedResults()) ? offerResults.getMappedResults() : null;
		
	}
	
	private boolean checkCinemaVoucher(List<OfferRules> offerAndRules, String offerId) {
		
		for(OfferRules rules : offerAndRules) {
			
			if (null != rules.getOfferId() && rules.getOfferId().equals(offerId) && null != rules.getRules()
					&& !rules.getRules().isEmpty()) {
				return rules.getRules().stream().anyMatch(i -> i.equals(OffersConfigurationConstants.cinemaRule));
			}
		}
		return false;
		
	}
	
	/**
	 * This method checks if available voucher count is less than 15% or 0
	 * @param resultResponse
	 * 
	 */
	public ResultResponse notifyVoucherCount(ResultResponse resultResponse, Headers headers) {

		LOG.info("Voucher Count Threshold: {}", voucherCountThreshold);
		
		List<VoucherDetails> merchantTotalVouchers = new ArrayList<>();
		List<VoucherDetails> merchantReadyVouchers = new ArrayList<>();
		String merchantName = null;
		String offerTitle = null;
		OfferRules offerRules = null;
		DecimalFormat decimalFormat = new DecimalFormat(VoucherConstants.DECIMAL_FORMAT);
		int actualCount = 0;
		List<String> notifSentToCodesZeroPerc = new ArrayList<>();
		List<String> notifSentToCodesFifteenPerc = new ArrayList<>();
		
		Date currentDate = Utils.getCurrentDateVoucherCount();
		LOG.info("Current Date GST: {}", currentDate);

		Criteria offerCriteria = Criteria.where(OffersDBConstants.VOUCHER_ACTION).is("3")
				.andOperator(Criteria.where("Status").regex(ProcessValues.getRegexStartEndExpression(OfferConstants.ACTIVE_STATUS.get()),
						OfferConstants.CASE_INSENSITIVE.get()), Criteria.where("OfferDate.EndDate").not().lte(currentDate));

		Aggregation aggregation = newAggregation(
				project(from(field(OffersDBConstants.RULES), field("Offer"), field("Status"),
						field("Merchant"), field(OffersDBConstants.OFFER_ID), field("VoucherInfo"), field("OfferDate"))),
				match(offerCriteria));

		AggregationResults<OfferRules> offerResults = mongoOperations.aggregate(aggregation,
				OffersDBConstants.OFFER_CATALOG, OfferRules.class);

		List<OfferRules> retrievedOffers = !CollectionUtils.isEmpty(offerResults.getMappedResults()) ? offerResults.getMappedResults() : null;

		if (null == retrievedOffers) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_OFFERS_VOUCHER_ACTION_THREE.getIntId(),
					VoucherManagementCode.NO_OFFERS_VOUCHER_ACTION_THREE.getMsg());
			resultResponse.setResult(VoucherManagementCode.VOUCHER_COUNT_NOTIFICATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_COUNT_NOTIFICATION_FAILED.getMsg());
			return resultResponse;
		}

		Set<String> offerIds = new HashSet<>();
		Set<String> merchantCodes = new HashSet<>();
		for (OfferRules offer : retrievedOffers) {
			if(null != offer.getOfferId()) offerIds.add(offer.getOfferId());
			if(null != offer.getMerchant()) merchantCodes.add(offer.getMerchant().getMerchantCode());
		}

		LOG.info("Offers Ids: {}", offerIds);

		Criteria voucherCriteria = Criteria.where("BulkId").exists(true)
		.andOperator(Criteria.where("StartDate").not().gte(currentDate), Criteria.where("ExpiryDate").not().lte(currentDate));
		
		Aggregation aggregationV = newAggregation(
				project(from(field("BulkId"), field("StartDate"), field("ExpiryDate"), field("VoucherCode"),
						field("OfferInfo.OfferId"), field("MerchantCode"), field("MerchantName"), field("Status"))),
				match(voucherCriteria));

		AggregationResults<VoucherDetails> voucherResults = mongoOperations.aggregate(aggregationV,
				"Voucher", VoucherDetails.class);

		List<VoucherDetails> uploadedVouchers = !CollectionUtils.isEmpty(voucherResults.getMappedResults()) ? voucherResults.getMappedResults() : null;

		if(null != uploadedVouchers) {
			LOG.info(
					VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME
							+ VoucherConstants.UPLOADED_VOUCHER_COUNT,
					this.getClass(), VoucherConstants.CONTROLLER_HELPER_VOUCHER_COUNT, String.valueOf(uploadedVouchers.size()));
		}

		List<String> allOfferIds = offerIds.stream().collect(Collectors.toList());
		for (String code : merchantCodes) {
			for (String offerId : offerIds) {

				double availableVoucherPercentage = 0.0;

				if (null != retrievedOffers)
					offerRules = getOfferCatalogVoucherCount(retrievedOffers, offerId, code);
				if (null == offerRules)
					continue;

				merchantTotalVouchers = getTotalVouchers(uploadedVouchers, code, offerId);
				merchantReadyVouchers = getReadyVouchers(uploadedVouchers, code, offerId);

				if (merchantTotalVouchers.size() != 0) {
					availableVoucherPercentage = Double.valueOf(decimalFormat.format(
							Double.valueOf(merchantReadyVouchers.size()) / Double.valueOf(merchantTotalVouchers.size())));
				}

				if(null != offerRules && null != offerRules.getOffer() && null != offerRules.getOffer().getOfferTitle()) offerTitle = offerRules.getOffer().getOfferTitle().getOfferTitleEn();
				
				if(null != merchantTotalVouchers &&  !merchantTotalVouchers.isEmpty() && null != merchantTotalVouchers.get(0) && null != merchantTotalVouchers.get(0).getMerchantName()) {
					merchantName = merchantTotalVouchers.get(0).getMerchantName();
				} else if(null != offerRules && null != offerRules.getMerchant() && null != offerRules.getMerchant().getMerchantName()) {
					merchantName = offerRules.getMerchant().getMerchantName().getMerchantNameEn();
				}

				int notificationType = sendSMSVoucherCountStatistics(availableVoucherPercentage,
						merchantReadyVouchers, merchantName, offerTitle, code, resultResponse, headers, offerId);

				if (1 == notificationType) {
					actualCount++;
					notifSentToCodesZeroPerc.add(offerId);
				}

				if (2 == notificationType) {
					actualCount++;
					notifSentToCodesFifteenPerc.add(offerId);
				}

			}
			
		}
		
		if(actualCount == 0) {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_COUNT_NOTIFICATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_COUNT_NOTIFICATION_FAILED.getMsg());
		} else {
						
			double threshold = Double.parseDouble(voucherCountThreshold);
			
			String zeroPercVoucher = "Zero ready vouchers available for offerIds: " + notifSentToCodesZeroPerc.toString();
			String fifteenPercVoucher = ", Less than " + threshold*100.00 + "% ready vouchers available for offerIds: " + notifSentToCodesFifteenPerc.toString();
			
			String voucherCountStats = zeroPercVoucher + fifteenPercVoucher;
			
			resultResponse.setResult(VoucherManagementCode.VOUCHER_COUNT_NOTIFICATION_SUCCESS.getId(),
					VoucherManagementCode.VOUCHER_COUNT_NOTIFICATION_SUCCESS.getMsg() + voucherCountStats);
			
		}

		return resultResponse;

	}
	
	/**
	 * Method to get total vouchers - voucher statistics API.
	 * @param uploadedVouchers
	 * @param merchantCode
	 * @param offerId
	 * @return
	 */
	private List<VoucherDetails> getTotalVouchers(List<VoucherDetails> uploadedVouchers, String merchantCode, String offerId) {
		
		return uploadedVouchers.stream()
				.filter(m -> m.getMerchantCode().equals(merchantCode) && null != m.getOffer() && m.getOffer().equals(offerId))
				.collect(Collectors.toList());

	}
	
	/**
	 * Method to get ready vouchers - Method to get total vouchers - voucher statistics API.
	 * @param uploadedVouchers
	 * @param merchantCode
	 * @param offerId
	 * @return
	 */
	private List<VoucherDetails> getReadyVouchers(List<VoucherDetails> uploadedVouchers, String merchantCode, String offerId) {
		
		return uploadedVouchers.stream()
				.filter(m -> null != m.getStatus() && m.getStatus().equalsIgnoreCase(VoucherStatus.READY)
						&& m.getMerchantCode().equals(merchantCode) && null != m.getOffer() && m.getOffer().equals(offerId))
				.collect(Collectors.toList());

	}
	
	/**
	 * Method to get an offer from list of offers - Method to get total vouchers - voucher statistics API.
	 * @param offersList
	 * @param offerId
	 * @return
	 */
	private OfferRules getOfferCatalog(List<OfferRules> offersList, String offerId) {
		
		return offersList.stream()
				.filter(m -> null != m.getOfferId() && m.getOfferId().equals(offerId)).findAny()
				.orElse(null);
		
	}

	/**
	 * Method to get an offer from list of offers - Method to get total vouchers - voucher count API.
	 * @param offersList
	 * @param offerId
	 * @return
	 */
	private OfferRules getOfferCatalogVoucherCount(List<OfferRules> offersList, String offerId, String merchantCode) {

		return offersList.stream()
				.filter(m -> null != m.getOfferId() && m.getOfferId().equals(offerId) && null != m.getMerchant() && m.getMerchant().getMerchantCode().equals(merchantCode)).findAny()
				.orElse(null);

	}
	
	/**
	 * This method is used to send email for available voucher count.
	 * Called from Cron API - voucherCountNotification method in VoucherController.
	 * @param availableVoucherPercentage
	 * @param merchantReadyVouchers
	 * @param merchantName
	 * @param offerTitle
	 * @param code
	 * @param resultResponse
	 */
	private int sendSMSVoucherCountStatistics(double availableVoucherPercentage, List<VoucherDetails> merchantReadyVouchers,
			String merchantName, String offerTitle, String code, ResultResponse resultResponse, Headers headers, String offerId) {
		try {
			
			if (availableVoucherPercentage == VoucherConstants.ZERO_PERCENT_VOUCHER_COUNT) {
				voucherService.sendSMSVoucherCount(merchantName, offerTitle,
						String.valueOf(VoucherConstants.ZERO_PERCENT_VOUCHER_COUNT), voucherCountContacts, headers,
						VoucherConstants.VOUCHER_COUNT_TYPE_ZERO);
				return 1;
			} else if (availableVoucherPercentage < Double.parseDouble(voucherCountThreshold)
					&& availableVoucherPercentage != VoucherConstants.ZERO_PERCENT_VOUCHER_COUNT) {
				voucherService.sendSMSVoucherCount(merchantName, offerTitle,
						String.valueOf(merchantReadyVouchers.size()), voucherCountContacts, headers, VoucherConstants.VOUCHER_COUNT_TYPE_15_PERC);
				return 2;
			} 
		
		} catch (VoucherManagementException vme) {
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());
		}
		
		return 0;
		
	}
	
	/**
	 * This method lists vouchers for specified list of business ids
	 * @param businessIds
	 * @param resultResponse
	 * @return voucher list
	 */
	public List<VoucherListResult> getVoucherListByBusinessId(List<String> businessIds, ResultResponse resultResponse, String program) {
		
		List<VoucherListResult> voucherList = new ArrayList<>();
		Sort sort = Sort.by(Sort.Direction.DESC, VoucherConstants.SORT_DESC_DOWNLOADED_DATE);
		
		try {	
			
			if (null == businessIds || businessIds.isEmpty()) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_BUSINESS_ID_EMPTY.getIntId(),
						VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_BUSINESS_ID_EMPTY.getMsg());
				resultResponse.setResult(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getId(),
						VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			}
			
			List<Voucher> vouchers = voucherRepository.findByUuid(businessIds, sort);
			if (!vouchers.isEmpty()) {
			
				voucherList = getListOfVouchersForPartnerCodeAPI(vouchers, null, null, null, null, program);
				resultResponse.setResult(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_SUCCESS.getId(),
						VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_SUCCESS.getMsg());
			
			} else {
				
				resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getIntId(),
						VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getMsg());
				resultResponse.setResult(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getId(),
						VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			
			}
		
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getId(),
					VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			LOG.error(e.getMessage());
		}
		
		return voucherList;
		
	}

	/**
	 * This method checks if the user is an admin
	 * @param userRole
	 * @return true if admin, false if not 
	 */
	public boolean validateUser(String userRole) {
		return com.loyalty.marketplace.utils.Utils.checkRoleExists(userRole, adminRole);
			
	}
	
	public int carrefourExpiryAlertHelper(ResultResponse resultResponse) {
		List<PurchaseHistory> purchaseHistory = new ArrayList<>();
		try {		
			Date currentDate = Utils.currentDate();
			Date pastDate = Utils.addMinutesToDate(currentDate, -30);
			purchaseHistory = purchaseRepository.findByPartnerCodeAndStatusIgnoreCaseAndStatusReasonAndCreatedDateBetween(VoucherConstants.CARREFOUR, VoucherConstants.FAILED,
					VoucherManagementCode.CARREFOUR_SERVICE_ERROR.getMsg(), pastDate, currentDate);
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(VoucherManagementCode.CRFR_ALERT_FAILED.getId(),
					VoucherManagementCode.CRFR_ALERT_FAILED.getMsg());
		}
		return purchaseHistory.size();
	}
	
	public int ygagExpiryAlertHelper(ResultResponse resultResponse) {
		List<PurchaseHistory> purchaseHistory = new ArrayList<>();
		try {		
			Date currentDate = Utils.currentDate();
			Date pastDate = Utils.addMinutesToDate(currentDate, -60);
			purchaseHistory = purchaseRepository.findByPartnerCodeAndStatusIgnoreCaseAndStatusReasonAndCreatedDateBetween(VoucherConstants.YGAG, VoucherConstants.FAILED,
					VoucherManagementCode.YGAG_SERVICE_ERROR.getMsg(), pastDate, currentDate);
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(VoucherManagementCode.YGAG_ALERT_FAILED.getId(),
					VoucherManagementCode.YGAG_ALERT_FAILED.getMsg());
		}
		return purchaseHistory.size();
	}
	
	public SMSRequestDto createSmsEvent(String transactionId, String templateId, String notificationId,
            String notificationCode, Map<String, String> additionalParameters, String language,
            List<String> destinationNumbers) {
        return new SMSRequestDto(transactionId, templateId, notificationId, notificationCode, null,
                additionalParameters, language, destinationNumbers);
    }

	public void processReconciliation(VoucherReconciliationResponse mafResponse, SimpleDateFormat dateFormat, String date, String userName, String program, String externalTransactionId, VoucherReconciliationDtoResponse voucherReconciliationResponse ) throws ParseException, VoucherManagementException {
		List<Orders> orders = mafResponse.getResponseData().getVoucherDetails().getData().getOrders();
		String partnerTransactionId = mafResponse.getResponseData().getTransactionID();
		
		List<Voucher> voucherListInLoyalty;
				
		Date fromDatePassToDb = Date.from(dateFormat.parse(date).toInstant());
			
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(date));	
		cal.add(Calendar.DAY_OF_MONTH, +1);
					
		String toDateDb = dateFormat.format(cal.getTime());
		Date toDatePassToDb = Date.from(dateFormat.parse(toDateDb).toInstant());
		
		voucherListInLoyalty = getVoucherDataFromLoyaltyForPartner(fromDatePassToDb,toDatePassToDb);
		
		LOG.info("MAF results : {} with size : {} ", orders, orders.size());
		LOG.info("Voucher results : {} with size : {} ", voucherListInLoyalty, voucherListInLoyalty.size() );
		
		String result = (orders.get(0)==null && voucherListInLoyalty.isEmpty()) ?  "Matching": "";				

		double totalAmountInLoyalty = Optional.ofNullable(voucherListInLoyalty)
				.orElseGet(Collections::emptyList).stream().mapToDouble(Voucher::getVoucherAmount).sum();

		Set<String> voucherCodesFromLoyalty = Optional.ofNullable(voucherListInLoyalty)
				.orElseGet(Collections::emptyList).stream().map(Voucher::getVoucherCode).collect(Collectors.toSet());

		Set<String> voucherCodesFromPartner = orders.get(0) == null ? new HashSet<>()
				: Optional.ofNullable(orders).orElseGet(Collections::emptyList).stream().map(Orders::getVoucherID)
						.collect(Collectors.toSet());

		double totalAmountFromPartner = orders.get(0) == null ? 0.0
				: Optional.ofNullable(orders).orElseGet(Collections::emptyList).stream()
						.mapToDouble(o -> Double.parseDouble(o.getTotalAmount())).sum();

		boolean voucherCondition = orders.get(0) == null ? false
				: (orders.stream().map(Orders::getVoucherID).allMatch(voucherCodesFromLoyalty::contains));
		boolean amountCondition = (totalAmountInLoyalty == totalAmountFromPartner);
		
		LOG.info("MAF total vouchers : {} with total amount : {} ", voucherCodesFromPartner, totalAmountFromPartner);
		LOG.info("Loyalty total vouchers : {} with total amount : {} ", voucherCodesFromLoyalty, totalAmountInLoyalty );
		
		Date dateToSave = new Date();
		if ((voucherCondition && amountCondition) || result.equalsIgnoreCase("Matching")) {
			saveVoucherToReconcile(orders, totalAmountFromPartner,
					Optional.ofNullable(voucherListInLoyalty).orElseGet(Collections::emptyList), totalAmountInLoyalty,
					program, fromDatePassToDb, toDatePassToDb, userName, null, dateToSave, externalTransactionId);
		} else {

			String dbRefIdReconcile = saveVoucherIndifferenceToReconcileData(orders, voucherCodesFromPartner,
					voucherListInLoyalty, voucherCodesFromLoyalty, program, userName, partnerTransactionId, dateToSave,
					externalTransactionId);
			String voucherReconcileId = saveVoucherToReconcile(orders, totalAmountFromPartner, voucherListInLoyalty,
					totalAmountInLoyalty, program, fromDatePassToDb, toDatePassToDb, userName, dbRefIdReconcile,
					dateToSave, externalTransactionId);
			sendReconcileEmail(voucherReconcileId, dbRefIdReconcile, externalTransactionId);
		}
		voucherReconciliationResponse.setResult(VoucherManagementCode.VOUCHER_RECONCILIATION_SUCCESS.getId(),
				VoucherManagementCode.VOUCHER_RECONCILIATION_SUCCESS.getMsg());

	}
	
	
	
	public boolean rollBackVoucherBurn(RollBackVoucherBurnRequest rollBackVoucherBurnRequest, Headers headers, BurnVoucher burnVoucher, VoucherResultResponse resultResponse) {
		List<Voucher> voucherList = voucherRepository.findByVoucherCodeAndExternalTransactionId(rollBackVoucherBurnRequest.getVoucherCode(), rollBackVoucherBurnRequest.getTransactionId());
		LOG.info("rollBackVoucherBurn :: voucherList : {} ", voucherList);
		if(voucherList.isEmpty()) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHER_AVAILABLE.getIntId(),
					VoucherManagementCode.NO_VOUCHER_AVAILABLE.getMsg());
		} else {
			if(voucherList.size() == 1) {
				if (voucherList.get(0).getCashVoucherBurntInfo()
						.stream().anyMatch(r -> rollBackVoucherBurnRequest.getTransactionId().equals(r.getOrderId()) && r.getAction().equalsIgnoreCase("rollback"))) {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_ALREADY_ROLLBACK.getIntId(),
							VoucherManagementCode.VOUCHER_ALREADY_ROLLBACK.getMsg());
				} else {
					CashVoucherBurntInfo cashVoucherBurntInfo = voucherList.get(0).getCashVoucherBurntInfo()
						.stream().filter(c -> rollBackVoucherBurnRequest.getTransactionId().equals(c.getExternalTransactionId()) && c.getAction().equalsIgnoreCase("burn"))
						.findAny().orElse(null);
					if(null != cashVoucherBurntInfo) {
						LOG.info("rollBackVoucherBurn :: cashVoucherBurntInfo : {} ", cashVoucherBurntInfo);
						Double rollBackAmount = cashVoucherBurntInfo.getVoucherCurrentBalance() - cashVoucherBurntInfo.getVoucherNewBalance();
						Double newBalance = voucherList.get(0).getVoucherBalance() + rollBackAmount;
						
						CashVoucherBurntInfo rollbackCashVoucherBurntInfo = new CashVoucherBurntInfo();
						rollbackCashVoucherBurntInfo.setAction("rollback");
						rollbackCashVoucherBurntInfo.setExternalTransactionId(headers.getExternalTransactionId());
						rollbackCashVoucherBurntInfo.setOrderId(rollBackVoucherBurnRequest.getTransactionId());
						rollbackCashVoucherBurntInfo.setRollbackAmount(rollBackAmount);
						rollbackCashVoucherBurntInfo.setVoucherCurrentBalance(voucherList.get(0).getVoucherBalance());
						rollbackCashVoucherBurntInfo.setVoucherNewBalance(newBalance);
						rollbackCashVoucherBurntInfo.setRollBackDate(new Date());
						rollbackCashVoucherBurntInfo.setRollBackUser(headers.getUserName());
								
						voucherList.get(0).getCashVoucherBurntInfo().add(rollbackCashVoucherBurntInfo);
						voucherList.get(0).setVoucherBalance(newBalance);
						
						if(null != voucherList.get(0).getBurntInfo() && voucherList.get(0).getBurntInfo().isVoucherBurnt()) {
							BurntInfo burntInfo = voucherList.get(0).getBurntInfo();
							burntInfo.setVoucherBurnt(false);
							voucherList.get(0).setBurntInfo(burntInfo);
						}
						
						burnVoucher.setRollBackId(headers.getExternalTransactionId());
						burnVoucher.setRollBackAmount(rollBackAmount);
						return voucherRepositoryHelper.updateCashVoucherBurntInfo(voucherList.get(0), headers).wasAcknowledged();
						
					} else {
						resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getIntId(),
								VoucherManagementCode.NO_VOUCHERS_FOR_TRANSACTION.getMsg());
					}
				}
			} else {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.MULTIPLE_VOUCHERS_FOUND.getIntId(),
						VoucherManagementCode.MULTIPLE_VOUCHERS_FOUND.getMsg());
			}
		}
		return false;
	}
	
	/***
	 * Set file contents in handback file response
	 * @param handbackFileDto
	 * @param file
	 */
	public void setFileContents(FreeVoucherHandbackFileDto handbackFileDto, VoucherUploadFile file) {
		
		if(!ObjectUtils.isEmpty(file)) {
			
			setUploadedFileContents(file.getFileContent(), handbackFileDto);
			setHandbackFileContent(file.getHandbackFile(), handbackFileDto);
			
		}
		
	}

	/**
	 * 
	 * @param fileContent
	 * @param handbackFileDto
	 */
	private void setUploadedFileContents(String fileContent, FreeVoucherHandbackFileDto handbackFileDto) {
		
		if(!StringUtils.isEmpty(fileContent)) {
			
			String[] contentList = fileContent.split(VoucherConstants.NEWLINE_CHARACTER);
						
			if(null!=contentList) {
				
				handbackFileDto.setFileUploadedContent(new ArrayList<>(contentList.length));
				for(String record : contentList) {
				
					String [] recordContent = record.split(VoucherConstants.COMMA_CHARACTER);
					handbackFileDto.getFileUploadedContent().add(getRecordContentInResponse(recordContent));
				}
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * @param recordContent
	 * @return
	 */
	private FileContentDto getRecordContentInResponse(String[] recordContent) {
		
		FileContentDto fileContentDto = new FileContentDto();
		
		if(recordContent.length>1 && !StringUtils.isEmpty(recordContent[0])) {
			
			fileContentDto.setAccountNumber(recordContent[0]);
		}
		
		if(recordContent.length>2 && !StringUtils.isEmpty(recordContent[1])) {
			
			fileContentDto.setMembershipCode(recordContent[1]);
		}
		
		if(recordContent.length>3 && !StringUtils.isEmpty(recordContent[2])) {
			
			fileContentDto.setLoyaltyId(recordContent[2]);
		}
		
		if(recordContent.length>4  && !StringUtils.isEmpty(recordContent[3])) {
			
			fileContentDto.setExternalTransactionId(recordContent[3]);
		}
		
		if(recordContent.length>=5 && !StringUtils.isEmpty(recordContent[4])) {
			
			fileContentDto.setDenomination(recordContent[4]);
		}

		return fileContentDto;
	}

	/**
	 * 
	 * @param handbackFile
	 * @param handbackFileDto
	 */
	private void setHandbackFileContent(String handbackFile, FreeVoucherHandbackFileDto handbackFileDto) {
		
		if(!StringUtils.isEmpty(handbackFile)) {
			
			String[] contentList = handbackFile.split(VoucherConstants.NEWLINE_CHARACTER);
						
			HandbackFileDto fileContentDto = null;
			
			if(null!=contentList) {
				
				handbackFileDto.setHandbackFileContent(new ArrayList<>(contentList.length));
				for(String record : contentList) {
				
					String [] recordContent = record.split(VoucherConstants.SEMICOLON_CHARACTER);
					
					if(null!=recordContent) {
						
						fileContentDto = new HandbackFileDto();
						
						String [] fileContent = recordContent[0].split(VoucherConstants.COMMA_CHARACTER);
						setFileContentInHandbackResponse(fileContent, fileContentDto);
						setStatusContentInHandbackResponse(recordContent, fileContentDto);
						handbackFileDto.getHandbackFileContent().add(fileContentDto);
					}
				}
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * @param fileContent
	 * @param fileContentDto
	 */
	private void setFileContentInHandbackResponse(String[] fileContent, HandbackFileDto fileContentDto) {
		
		if(fileContent.length>1 && !StringUtils.isEmpty(fileContent[0])) {
			
			fileContentDto.setAccountNumber(fileContent[0]);
		}
		
		if(fileContent.length>2 && !StringUtils.isEmpty(fileContent[1])) {
			
			fileContentDto.setMembershipCode(fileContent[1]);
		}
		
		if(fileContent.length>3 && !StringUtils.isEmpty(fileContent[2])) {
			
			fileContentDto.setLoyaltyId(fileContent[2]);
		}
		
		if(fileContent.length>4  && !StringUtils.isEmpty(fileContent[3])) {
			
			fileContentDto.setExternalTransactionId(fileContent[3]);
		}
		
		if(fileContent.length>=5 && !StringUtils.isEmpty(fileContent[4])) {
			
			fileContentDto.setDenomination(fileContent[4]);
		}
		
	}
	
	/**
	 * 
	 * @param recordContent
	 * @param fileContentDto
	 */
	private void setStatusContentInHandbackResponse(String[] recordContent, HandbackFileDto fileContentDto) {
		
		if(recordContent.length>2 && !StringUtils.isEmpty(recordContent[1])) {
			
			fileContentDto.setStatus(recordContent[1]);
		}
		
		if(recordContent.length>=3 && !StringUtils.isEmpty(recordContent[2])) {
			
			fileContentDto.setError(recordContent[2]);
		}
		
	}

	/**
	 * 
	 * @param file
	 * @param resultResponse
	 * @return
	 */
	public boolean validateFreeVoucherUploadFile(MultipartFile file, VoucherUploadResponse resultResponse) {
		
		//Checking File Format
		if (file == null || file.isEmpty() || file.getContentType() == null
		|| !(file.getContentType().contains(VoucherConstants.TEXT_CSV) 
		|| file.getContentType().contains(VoucherConstants.APP_OCTET_STREAM)
		|| file.getContentType().contains(VoucherConstants.APP_MS_EXCEL))) {

			resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_FILE.getIntId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_FILE.getMsg());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
		}  else {
		
			//Checking for Duplicate File Name
			String fileName = file.getOriginalFilename();
			boolean isExists = mongoOperations.exists(query(where(VoucherConstants.FILENAME).is(fileName)), VoucherUploadFile.class);
			
			if (isExists) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.UPLOADED_FILE_EXIST.getIntId(),
						VoucherManagementCode.UPLOADED_FILE_EXIST.getMsg());
				resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
						VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
			}
		}
		
		return Checks.checkNoErrors(resultResponse);

	}
	
	/**
	 * 
	 * @param offerDetails
	 * @param resultResponse
	 * @return
	 */
	public boolean validateOfferForFreeVoucherGifting(OfferCatalog offerDetails, VoucherUploadResponse resultResponse) {
	
		if (ObjectUtils.isEmpty(offerDetails)) {
			
			//Check for non-empty offer
			resultResponse.addErrorAPIResponse(VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_VALID.getIntId(),
					VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_VALID.getMsg());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
			
		} else if (!offerDetails.getOfferType().getOfferTypeId().equalsIgnoreCase(VoucherConstants.DISCOUNT_OFFER_ID)
		&& !offerDetails.getOfferType().getOfferTypeId().equalsIgnoreCase(VoucherConstants.CASH_OFFER_ID)) {
			
			//Check for valid offer type - discount or cash offer
			resultResponse.addErrorAPIResponse(VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_DEAL.getIntId(),
					VoucherManagementCode.FREE_VOUCHER_OFFER_NOT_DEAL.getMsg());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
			
		}
		
		return Checks.checkNoErrors(resultResponse);
	}
	
	/***
	 * 
	 * @param file
	 * @param resultResponse
	 * @return
	 * @throws IOException 
	 */
	public List<FileContentDto> readDataFromVoucherUploadFile(MultipartFile file,
			VoucherUploadResponse resultResponse) throws IOException {
		
		List<FileContentDto> fileContentList = new ArrayList<>();
		List<String> fileContent = new ArrayList<>();
	    
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			while ((line = br.readLine()) != null) {
				fileContent.add(line);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			LOG.error("Error occured in reading the file for free voucher : {} ", e.getMessage());
		}
		
		if(!CollectionUtils.isEmpty(fileContent)) {
			for (String s : fileContent) {
				FileContentDto fileContentDto = new FileContentDto();
				
				String[] contentStringArray = new String[5];
				
				String[] intermediateContentStringArray = s.split(VoucherConstants.COMMA_CHARACTER);
				
				for(int i = 0; i < intermediateContentStringArray.length; i++) {
					contentStringArray[i] = intermediateContentStringArray[i];
				}
				
				fileContentDto.setAccountNumber(contentStringArray[0]);
				fileContentDto.setMembershipCode(contentStringArray[1]);
				fileContentDto.setLoyaltyId(contentStringArray[2]);
				fileContentDto.setExternalTransactionId(contentStringArray[3]);
				fileContentDto.setDenomination(contentStringArray[4]);
				fileContentList.add(fileContentDto);
				
			}

		} else {
			
			resultResponse.addErrorAPIResponse(VoucherManagementCode.UPLOADED_FILE_EMPTY.getIntId(),
					VoucherManagementCode.UPLOADED_FILE_EMPTY.getMsg());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());

		}
		
		return fileContentList;

	}

	/**
	 * 
	 * @param file
	 * @param fileContentList
	 * @param headers 
	 * @param resultResponse
	 * @return 
	 */
	public VoucherUploadFile saveFileContentsForFreeVoucherUpload(MultipartFile file, List<FileContentDto> fileContentList, OfferCatalog offerDetails, 
			Headers headers, VoucherUploadResponse resultResponse) throws IOException, VoucherManagementException {
		
		StringBuilder dataFileContent = new StringBuilder();
	    
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			while ((line = br.readLine()) != null) {
				dataFileContent = dataFileContent.append(line).append(VoucherConstants.NEWLINE_CHARACTER);
			}
			
			//Saving File in Physical Location
			Files.copy(file.getInputStream(), Paths.get(freeVoucherFileUploadLocation + file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			LOG.error("Error occured in saving the file for free voucher : {} ", e.getMessage());
		}
		
		//Saving File in Database
		VoucherUploadFileDomain uploadDomain = new VoucherUploadFileDomain.VoucherUploadFileBuilder(headers.getProgram(),
				file.getOriginalFilename(), offerDetails.getMerchant().getMerchantCode(), offerDetails.getOfferId(), new Date(), VoucherConstants.GENERATE_FREE_VOUCHER,
				dataFileContent.toString(), VoucherConstants.PROCESSING, null, new Date(), headers.getUserName())
				.createdDate(new Date()).createdUser(headers.getUserName())
				.transactionId(headers.getExternalTransactionId()).build();
		
		return voucherUploadFileDomain.saveVoucherUpload(uploadDomain, headers.getExternalTransactionId(), headers.getUserName());

	}

	/**
	 * 
	 * @param fileContentList
	 * @param offerDetails
	 * @param headers 
	 * @param uploadedFile 
	 * @param resultResponse
	 * @throws VoucherManagementException 
	 */
	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_FREE_VOUCHER)
	public void giftFreeVoucherToEligibleAccounts(List<FileContentDto> fileContentList, OfferCatalog offerDetails,
			Headers headers, VoucherUploadFile uploadedFile, VoucherUploadResponse resultResponse) throws VoucherManagementException {
		
		List<HandbackFileDto> handbackFileDtoList = null;
		
		try {
			
			if(!CollectionUtils.isEmpty(fileContentList)) {
				
				List<PendingFreeVouchersInfoDomain> pendingVoucherInfoDomainList = new ArrayList<>(1);
				handbackFileDtoList = new ArrayList<>(fileContentList.size());
				List<String> externalTransactionIdList = fileContentList.stream()
						.filter(f->!StringUtils.isEmpty(f.getExternalTransactionId()))
						.map(FileContentDto::getExternalTransactionId)
						.collect(Collectors.toList());
				
				for(FileContentDto fileContent : fileContentList) {
                    
					LOG.info("~~~~~~~~~~Starting processing for account {} at time : {} ~~~~~~~~~~", fileContent.getAccountNumber(), new Date());
					headers.setExternalTransactionId(fileContent.getExternalTransactionId());
					HandbackFileDto handbackFileDto =  modelMapper.map(fileContent, HandbackFileDto.class); 
					validateInputFileDetailsForVoucherUpload(handbackFileDto, offerDetails.getOfferType().getOfferTypeId(), externalTransactionIdList);
					
					if(StringUtils.isEmpty(handbackFileDto.getError())) {
						validateAndGiftVoucher(handbackFileDto, offerDetails, headers, pendingVoucherInfoDomainList, resultResponse);						
					}
					
					handbackFileDtoList.add(handbackFileDto);
					Responses.removeAllErrors(resultResponse);
					LOG.info("~~~~~~~~~~Ended processing for account {} at time : {} ~~~~~~~~~~", fileContent.getAccountNumber(), new Date());
				}
				
				//Update status of processed file and save handback file contents
				updateUploadedFileInfoStatus(uploadedFile, null, handbackFileDtoList, headers);
				//Save details of vouchers parked for issuing in future
				pendingFreeVouchersInfoDomain.saveParkedVouchersInfoList(pendingVoucherInfoDomainList, headers);
				
			}
			
		} catch (Exception e) {
			
			updateUploadedFileInfoStatus(uploadedFile, e.getMessage(), handbackFileDtoList, headers);
			LOG.error("Error occured in processing uplaoded file for free voucher at {} : {} ", new Date(), e.getMessage());
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @param handbackFileDto
	 * @param offerTypeId
	 * @param externalTransactionIdList 
	 */
	private void validateInputFileDetailsForVoucherUpload(HandbackFileDto handbackFileDto, String offerTypeId, List<String> externalTransactionIdList) {
		
		if(StringUtils.isEmpty(handbackFileDto.getExternalTransactionId())) {
			
			//Checking for mandatory external transaction id
			handbackFileDto.setStatus(VoucherStatus.ERROR.toUpperCase());
			handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_EXT_TRANS_ID);
		
		} else if(Checks.checkIsCashVoucher(offerTypeId)
		&& ObjectUtils.isEmpty(handbackFileDto.getDenomination())) {
			
			//Checking for mandatory denomination for cash voucher
			handbackFileDto.setStatus(VoucherStatus.ERROR.toUpperCase());
			handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_DENOMINATION);
		
		} else {
			
			//Checking for duplicate external transaction id in file/db
			long count = externalTransactionIdList.stream()
	                .filter(e->e.equals(handbackFileDto.getExternalTransactionId()))
	                .collect(Collectors.counting());
			
			if(count>1) {
				
				handbackFileDto.setStatus(VoucherStatus.ERROR.toUpperCase());
				handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_DUPLICATE_EXT_TRANS_ID_FILE);
				
			} else {
				
				boolean isExists = mongoOperations.exists(
						 query(where(VoucherConstants.EXTERNAL_REF_NO)
						.is(handbackFileDto.getExternalTransactionId())
						.and(VoucherConstants.STATUS)
						.regex(VoucherConstants.SUCCESS, VoucherConstants.IGNORE_CASE)), 
						 PurchaseHistory.class);
				
				if (isExists) {
					
					handbackFileDto.setStatus(VoucherStatus.ERROR.toUpperCase());
					handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_DUPLICATE_EXT_TRANS_ID_DB);
				
				}
			}
			
		}
		
	}

	/**
	 * 
	 * @param handbackFileDto
	 * @param offerDetails 
	 * @param headers 
	 * @param pendingVoucherInfoDomainList 
	 * @param resultResponse
	 * @throws MarketplaceException 
	 * @throws VoucherManagementException 
	 */
	private void validateAndGiftVoucher(HandbackFileDto handbackFileDto, OfferCatalog offerDetails, 
			Headers headers, List<PendingFreeVouchersInfoDomain> pendingVoucherInfoDomainList,
			VoucherUploadResponse resultResponse) throws MarketplaceException, VoucherManagementException {
		
		MemberResponseDto member;
		try {
			
			String code = null;
			String typeOfId = null; 
			Date expiryDate = Utilities.getDateFromSpecificDate(expiryForVoucher, new Date());
			LOG.info("expiryForVoucher : {}, expiryDate: {}",expiryForVoucher,expiryDate);
			
			if(!StringUtils.isEmpty(handbackFileDto.getAccountNumber())) {
				
				code = handbackFileDto.getAccountNumber();
				typeOfId = VoucherConstants.ACCOUNT_NUMBER_CODE;
				
			} else if(!StringUtils.isEmpty(handbackFileDto.getMembershipCode())) {
				
				code = handbackFileDto.getMembershipCode();
				typeOfId = VoucherConstants.MEMBERSHIP_CODE_CODE;
				
			} else if(!StringUtils.isEmpty(handbackFileDto.getLoyaltyId())) {
				
				code = handbackFileDto.getLoyaltyId();
				typeOfId = VoucherConstants.LOYALTY_ID_CODE;
			}
			
			LOG.info("code : {}, typeOfId: {}",code,typeOfId);
			
			member = voucherMemberManagementService.getMemberDetails(code, typeOfId, headers);
		    GetMemberResponse memberDetails = Utils.getMemberInfo(member, handbackFileDto.getAccountNumber());
			
			if(!ObjectUtils.isEmpty(memberDetails)) {
				
				processForAccountPresent(memberDetails, member, handbackFileDto, offerDetails, headers, pendingVoucherInfoDomainList, expiryDate, resultResponse);
				
			} else {
				
				handbackFileDto.setStatus(VoucherStatus.PARKED);
				handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_NOT_ENROLLED);
				addToParkedDomainList(handbackFileDto, null, headers, offerDetails.getOfferId(), expiryDate, pendingVoucherInfoDomainList);
			}
		
		} catch (Exception e) {
			
			e.printStackTrace();
			handbackFileDto.setStatus(VoucherStatus.ERROR);
			handbackFileDto.setError(e.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @param memberDetails
	 * @param member 
	 * @param handbackFileDto
	 * @param offerDetails
	 * @param headers
	 * @param pendingVoucherInfoDomainList 
	 * @param expiryDate 
	 * @param resultResponse
	 * @throws MarketplaceException 
	 */
	private void processForAccountPresent(GetMemberResponse memberDetails, MemberResponseDto member, HandbackFileDto handbackFileDto,
			OfferCatalog offerDetails, Headers headers, List<PendingFreeVouchersInfoDomain> pendingVoucherInfoDomainList, 
			Date expiryDate, VoucherUploadResponse resultResponse) throws MarketplaceException {
		
		if(memberDetails.getAccountStatus().equalsIgnoreCase(VoucherConstants.MM_STATUS_ACTIVE)) {
			
			//Gifting voucher to an active account
			if(checkAccountValidForFreeVoucher(memberDetails)) {

				getVoucherCodeAfterGifting(offerDetails, memberDetails, member, headers, handbackFileDto, resultResponse);
			
				if(Checks.checkErrorsPresent(resultResponse)) {
					handbackFileDto.setStatus(VoucherStatus.ERROR.toUpperCase());
					handbackFileDto.setError(resultResponse.getApiStatus().getErrors().get(0).getMessage());
				
				} else {
					
					handbackFileDto.setStatus(VoucherStatus.SUCCESS);
				}
				
			} else {
				
				handbackFileDto.setStatus(VoucherStatus.PARKED);
				handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_INACTIVE+ freeVoucherLoginLimit +VoucherConstants.DAYS);
			}
		
		} else if(memberDetails.getAccountStatus().equalsIgnoreCase(VoucherConstants.MM_STATUS_SUSPENDED)) {
			
			handbackFileDto.setStatus(VoucherStatus.PARKED);
			handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_SUSPENDED_MEMBER);
			
		} else {
		
			handbackFileDto.setStatus(VoucherStatus.ERROR.toUpperCase());
			handbackFileDto.setError(VoucherConstants.CANCELLED_MEMBER);
		}

		addToParkedDomainList(handbackFileDto, memberDetails, headers, offerDetails.getOfferId(), expiryDate, pendingVoucherInfoDomainList);
	}
	
	/**
	 * 
	 * @param handbackFileDto
	 * @param memberDetails
	 * @param headers
	 * @param offerId
	 * @param expiryDate 
	 * @param pendingVoucherInfoDomainList 
	 */
	private void addToParkedDomainList(HandbackFileDto handbackFileDto, GetMemberResponse memberDetails,
			Headers headers, String offerId, Date expiryDate, List<PendingFreeVouchersInfoDomain> pendingVoucherInfoDomainList) {
		
		if(!ObjectUtils.isEmpty(handbackFileDto)
		&& handbackFileDto.getStatus().equalsIgnoreCase(VoucherStatus.PARKED)) {
			
			PendingFreeVouchersInfoDomain pendingFreeVouchersDomain
			   = new PendingFreeVouchersInfoDomain.PendingFreeVouchersInfoBuilder(handbackFileDto.getAccountNumber(), 
					   !ObjectUtils.isEmpty(memberDetails)
					&& !StringUtils.isEmpty(memberDetails.getMembershipCode())
					   ?memberDetails.getMembershipCode():null, 
					   offerId, 
					   !ObjectUtils.isEmpty(memberDetails)
					&& !StringUtils.isEmpty(memberDetails.getPartyId())?
							   memberDetails.getPartyId():null, 
					   VoucherConstants.GENERATE_FREE_VOUCHER, 
					   handbackFileDto.getExternalTransactionId())
			       .programCode(headers.getProgram())
			       .denomination(!StringUtils.isEmpty(handbackFileDto.getDenomination())
			    		   ? Integer.valueOf(handbackFileDto.getDenomination())
			    		   : null)
			       .expiryDate(expiryDate)
			       .status(handbackFileDto.getStatus())
			       .build();
			pendingVoucherInfoDomainList.add(pendingFreeVouchersDomain);
		}
		
	}

	/**
	 * 
	 * @param handbackFileDto
	 * @param headers
	 * @param offerId 
	 * @param expiryDate 
	 * @param pendingVoucherInfoDomainList 
	 * @throws VoucherManagementException 
	 */
	private void enrollMemberForGiftingFreeVoucher(HandbackFileDto handbackFileDto,
			Headers headers, String offerId, Date expiryDate, List<PendingFreeVouchersInfoDomain> pendingVoucherInfoDomainList) throws VoucherManagementException {
		
		EnrollmentResultResponse enrollmentResultResponse = voucherMemberManagementService.memberEnroll(handbackFileDto.getAccountNumber(), headers);					
		
		if (!enrollmentResultResponse.getEnrollmentResult().getMessage().contains(VoucherConstants.FAIL)) {
			
			handbackFileDto.setStatus(VoucherStatus.PARKED);
			handbackFileDto.setError(VoucherConstants.HANDBACK_FILE_ERROR_CURRENTLY_ENROLLED+enrollmentResultResponse.getEnrollmentResult().getMembershipCode());
			addToParkedDomainList(handbackFileDto, null, headers, offerId, expiryDate, pendingVoucherInfoDomainList);
			
		} else {
			
			handbackFileDto.setStatus(VoucherStatus.ERROR.toUpperCase());
			handbackFileDto.setError(enrollmentResultResponse.getEnrollmentResult().getMessage());
		}
		
	}

	/**
	 * 
	 * @param memberDetails
	 * @return
	 */
	private boolean checkAccountValidForFreeVoucher(GetMemberResponse memberDetails) {
		
		Date loginDateLimit = Utilities.getDateFromSpecificDate(Utilities.getNegativeIntegerValue(freeVoucherLoginLimit), new Date());
		LOG.info("freeVoucherLoginLimit : {}, loginDateLimit : {}", freeVoucherLoginLimit, loginDateLimit);
		LOG.info("Member last login date : {}", memberDetails.getLastLoginDate());
		return Checks.checkDateFirstGreaterThanSecond(memberDetails.getLastLoginDate(), loginDateLimit);
			
	}

	/**
	 * 
	 * @param offerDetails
	 * @param memberDetails
	 * @param member 
	 * @param header
	 * @param pendingGift
	 * @param purchaseResultResponse 
	 * @return
	 * @throws MarketplaceException 
	 */
	private String getVoucherCodeAfterGifting(OfferCatalog offerDetails, GetMemberResponse memberDetails,
			MemberResponseDto member, Headers header, Object giftDetails, ResultResponse resultResponse) throws MarketplaceException {
		
		PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setSelectedPaymentItem(ProcessValues.getPurchaseItemFromOfferType(offerDetails.getOfferType().getOfferTypeId()));
		purchaseRequestDto.setSelectedOption(OffersConfigurationConstants.fullPoints);
		purchaseRequestDto.setOfferId(offerDetails.getOfferId());
		purchaseRequestDto.setAccountNumber(memberDetails.getAccountNumber());
		purchaseRequestDto.setMembershipCode(memberDetails.getMembershipCode());
		purchaseRequestDto.setSpentAmount(0.0);
		purchaseRequestDto.setSpentPoints(0);
		purchaseRequestDto.setCouponQuantity(1);
		
		if(giftDetails instanceof PendingFreeVouchersInfo) {
			
			PendingFreeVouchersInfo pendingGift = (PendingFreeVouchersInfo) giftDetails; 
			purchaseRequestDto.setVoucherDenomination(pendingGift.getDenomination());
			purchaseRequestDto.setExtTransactionId(pendingGift.getExternalTransactionId());
			
		
		} else if(giftDetails instanceof HandbackFileDto) {
			
			HandbackFileDto freeVoucherDetails = (HandbackFileDto) giftDetails; 
			if(!StringUtils.isEmpty(freeVoucherDetails.getDenomination())) {
				purchaseRequestDto.setVoucherDenomination(Integer.valueOf(freeVoucherDetails.getDenomination()));
			}
			purchaseRequestDto.setExtTransactionId(freeVoucherDetails.getExternalTransactionId());
			
		}
		
		header.setExternalTransactionId(purchaseRequestDto.getExtTransactionId());
		
		GetMemberResponseDto memberConverted = null;
				
		try {
			
			memberConverted = !ObjectUtils.isEmpty(member)
					? modelMapper.map(member, GetMemberResponseDto.class)
					: null;
			
		} catch (Exception e) {
			
			LOG.error("Failed to convert member object");
		}		
		
		return offersHelper.generateFreeVoucher(offerDetails, purchaseRequestDto, header, memberConverted, resultResponse);
		
	}

	/**
	 * 
	 * @param uploadedFile
	 * @param handbackFileDtoList
	 * @param reason
	 * @param headers
	 */
	private void updateUploadedFileInfoStatus(VoucherUploadFile uploadedFile, String reason, List<HandbackFileDto> handbackFileDtoList,
			Headers headers) throws VoucherManagementException {
		
		LOG.info("Inside updateUploadedFileInfoStatus in VoucherControllerHelper");
		int flag = !CollectionUtils.isEmpty(handbackFileDtoList) ? 1 : 0;
		String uploadedFileStatus;
		StringBuilder handBackFileContent = new StringBuilder();
		for(HandbackFileDto handBackFileDto: handbackFileDtoList) {
			handBackFileContent = handBackFileContent.append(handBackFileDto.getAccountNumber()).append(VoucherConstants.COMMA_CHARACTER)
					.append(handBackFileDto.getLoyaltyId()).append(VoucherConstants.COMMA_CHARACTER)
					.append(handBackFileDto.getMembershipCode()).append(VoucherConstants.COMMA_CHARACTER)
					.append(handBackFileDto.getExternalTransactionId()).append(VoucherConstants.COMMA_CHARACTER)
					.append(handBackFileDto.getDenomination()).append(VoucherConstants.SEMICOLON_CHARACTER)
					.append(handBackFileDto.getStatus()).append(VoucherConstants.SEMICOLON_CHARACTER)
					.append(handBackFileDto.getError()).append(VoucherConstants.NEWLINE_CHARACTER);
			
			if(handBackFileDto.getStatus().equalsIgnoreCase(VoucherStatus.ERROR)){
				flag = 0;
			}
		}
		if(flag == 0) {
			uploadedFileStatus = VoucherConstants.FAILED;
		} else {
			uploadedFileStatus = VoucherConstants.SUCCESS;
		}
		
		Gson gson = new Gson();
	        VoucherUploadFile originalVoucherUploadFile = gson.fromJson(gson.toJson(uploadedFile), VoucherUploadFile.class);	
		VoucherUploadFileDomain uploadDomain = new VoucherUploadFileDomain.VoucherUploadFileBuilder(headers.getProgram(),
				uploadedFile.getFileName(), uploadedFile.getMerchantCode(), uploadedFile.getOfferId(), uploadedFile.getUploadedDate(), uploadedFile.getFileType(),
				uploadedFile.getFileContent(), uploadedFileStatus, handBackFileContent.toString(), new Date(), headers.getUserName())
				.id(uploadedFile.getId())
				.reason(!StringUtils.isEmpty(reason)?reason:null)
				.transactionId(uploadedFile.getTransactionId())
				.createdDate(uploadedFile.getCreatedDate())
				.createdUser(uploadedFile.getCreatedUser())
				.build();
		voucherUploadFileDomain.updateVoucherUpload(uploadDomain, originalVoucherUploadFile, headers.getExternalTransactionId(), headers.getUserName());
		LOG.info("Updated handback file for {} st : {} ", uploadedFile.getFileName(), new Date());
		LOG.info("Leaving updateUploadedFileInfoStatus in VoucherControllerHelper");
	}
	
	/**
	 * 
	 * @param memberDetails
	 * @param header
	 */
	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_FREE_VOUCHER_AFTER_LOGIN)
	public void giftFreeVoucherToCurrentAccount(GetMemberResponse memberDetails, Headers header) {
	
		LOG.info("==============Inside giftFreeVoucherToCurrentAccount in VoucherControllerHelper=============");
		
		if(!ObjectUtils.isEmpty(memberDetails)
		&& memberDetails.getAccountStatus().equalsIgnoreCase(VoucherConstants.ACTIVE)		
		&& !ObjectUtils.isEmpty(header)) {
	
			List<PendingFreeVouchersInfo> pendingVouchers = pendingFreeVouchersInfoRepository.findByAccountNumberAndStatusIgnoreCaseAndVoucherTypeIgnoreCaseAndProgramCodeIgnoreCase(memberDetails.getAccountNumber(),
					VoucherStatus.PARKED, VoucherConstants.GENERATE_FREE_VOUCHER, header.getProgram());
	
			if(!CollectionUtils.isEmpty(pendingVouchers)) {
				
				List<Voucher> originalPendingVoucherList = new ArrayList<>(pendingVouchers.size());
				
				pendingVouchers.stream().forEach(c -> {
					Gson gson = new Gson();
				    Voucher originalVoucher = gson.fromJson(gson.toJson(c), Voucher.class);	
				    originalPendingVoucherList.add(originalVoucher);
				});
				
				processAndGiftVoucher(memberDetails, originalPendingVoucherList, pendingVouchers, header);				
				
			}
			
		}
		LOG.info("==============Exiting giftFreeVoucherToCurrentAccount in VoucherControllerHelper=============");
	 }

	/**
	 * 
	 * @param memberDetails
	 * @param originalPendingVoucherList
	 * @param pendingVouchers
	 * @param header
	 */
	private void processAndGiftVoucher(GetMemberResponse memberDetails, List<Voucher> originalPendingVoucherList,
			List<PendingFreeVouchersInfo> pendingVouchers, Headers header) {
		
		try {
			
			List<OfferCatalog> offerCatalogList = offerRepository.findByActiveOfferList(
						pendingVouchers.stream().map(PendingFreeVouchersInfo::getOfferId).collect(Collectors.toSet()).stream().collect(Collectors.toList()),
						OfferConstants.ACTIVE_STATUS.get());
				
			for(PendingFreeVouchersInfo pendingGift : pendingVouchers) {
					
				giftVoucherAndUpdateStatus(offerCatalogList, pendingGift, header, memberDetails);
					
			}
			
			pendingFreeVouchersInfoRepository.saveAll(pendingVouchers);
			auditService.updateDataAudit(DBConstants.PENDING_FREE_VOUCHER_INFO, pendingVouchers, OffersRequestMappingConstants.RETRIEVE_BIRTHDAY_INFORMATION_ACCOUNT, originalPendingVoucherList, header.getExternalTransactionId(), header.getUserName());
			
		} catch (Exception e) {
			
			pendingFreeVouchersInfoRepository.saveAll(pendingVouchers);
			auditService.updateDataAudit(DBConstants.PENDING_FREE_VOUCHER_INFO, pendingVouchers, OffersRequestMappingConstants.RETRIEVE_BIRTHDAY_INFORMATION_ACCOUNT, originalPendingVoucherList, header.getExternalTransactionId(), header.getUserName());
			e.printStackTrace();
			LOG.error("Error occured in gifting pending vouchers to account {} : {} ", memberDetails.getAccountNumber(), e.getMessage());
		}

	}

	/**
	 * 
	 * @param offerCatalogList
	 * @param pendingGift
	 * @param header
	 * @param memberDetails
	 * @throws MarketplaceException
	 */
	private void giftVoucherAndUpdateStatus(List<OfferCatalog> offerCatalogList, PendingFreeVouchersInfo pendingGift, Headers header, GetMemberResponse memberDetails) throws MarketplaceException {
		
		OfferCatalog offerDetails = offerCatalogList.stream().filter(o->o.getOfferId().equals(pendingGift.getOfferId()))
				.findFirst().orElse(null);
		
		if(!ObjectUtils.isEmpty(offerDetails)
		&& Checks.checkDateFirstGreaterThanSecond(pendingGift.getExpiryDate(), new Date())) {
					
			PurchaseResultResponse resultResponse = new PurchaseResultResponse(header.getExternalTransactionId());
			pendingGift.setMembershipCode(memberDetails.getMembershipCode());
			pendingGift.setVoucherCode(getVoucherCodeAfterGifting(offerDetails, memberDetails, null, header, pendingGift, resultResponse));
			pendingGift.setTransactionId(!ObjectUtils.isEmpty(resultResponse)
					&& !ObjectUtils.isEmpty(resultResponse.getPurchaseResponseDto())
					? resultResponse.getPurchaseResponseDto().getTransactionNo()
					: null);
			pendingGift.setStatus(!StringUtils.isEmpty(pendingGift.getVoucherCode())
					? VoucherStatus.SUCCESS
					: VoucherStatus.FAILED);
			
			if(Checks.checkErrorsPresent(resultResponse)) {
				
				pendingGift.setReason(resultResponse.getApiStatus().getErrors().get(0).getMessage());
			
			} 
			
		} else {
			
			pendingGift.setStatus(ObjectUtils.isEmpty(offerDetails)
				? VoucherStatus.FAILED
				: VoucherStatus.EXPIRED.toUpperCase());
			pendingGift.setReason(ObjectUtils.isEmpty(offerDetails)
				? VoucherConstants.OFFER_NOT_PRESENT
				: VoucherConstants.VOUCHER_EXPIRED);
		}
		
	}

	public void voucherPromoCode(VoucherRequestDto voucherRequestDto,Headers headers, VoucherResponse resultResponse) {
		if(!ObjectUtils.isEmpty(voucherRequestDto) 
				&& !ObjectUtils.isEmpty(voucherRequestDto.getOfferTypeId())) {
			if(Checks.checkIsDiscountVoucher(voucherRequestDto.getOfferTypeId())) {
				LOG.info("Before Entering savePromoCodeDetails");
				saveSubscPromocodeDetails(voucherRequestDto,headers,resultResponse);
			}
		}
	}
	
	private void saveSubscPromocodeDetails(VoucherRequestDto voucherRequestDto,Headers headers,VoucherResponse voucherResponse) {
		LOG.info("Entering savePromocodeDetails method");
		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
		try {
			if(!ObjectUtils.isEmpty(voucherRequestDto)) {
				for (int i = 0; i < voucherRequestDto.getNumberOfVoucher(); i++) {
					Boolean response = false;
					PromoCodeRequest promocodeRequest = new PromoCodeRequest();
					
					List<String> accountno=new ArrayList<String>();
					accountno.add(voucherRequestDto.getAccountNumber());
					promocodeRequest.setAccountNumber(accountno);
					OfferCatalog offer = offerRepository.findByOfferId(voucherRequestDto.getOfferId());
					if(!ObjectUtils.isEmpty(offer.getSubscriptionDetails().getSubscriptionCatalogId())) {
						promocodeRequest.setSubscriptionCatalogId(offer.getSubscriptionDetails().getSubscriptionCatalogId());		
					}
					Voucher voucher = new Voucher();
					PromoCodeDetails promoCodeDetails = new PromoCodeDetails();
					if(!ObjectUtils.isEmpty(voucherResponse)
							&& !ObjectUtils.isEmpty(voucherResponse.getVoucherResult())
							&& !ObjectUtils.isEmpty(voucherResponse.getVoucherResult().get(i).getVoucherCode())) {
						promoCodeDetails.setPromoCode(voucherResponse.getVoucherResult().get(i).getVoucherCode());
						voucher = voucherRepository.findByVoucherCode(voucherResponse.getVoucherResult().get(i).getVoucherCode());
					}
					promoCodeDetails.setPromoCodeDescription("MarketPlace SubscPromo Purchase");
					promoCodeDetails.setPromoCodeTotalCount(1);
					promoCodeDetails.setPromoCodeType("Free Duration");
					promoCodeDetails.setPromoCodeLevel("Instance");
					promoCodeDetails.setPromoUserRedeemCountLimit(1);
					promoCodeDetails.setValue(0);
					if(!ObjectUtils.isEmpty(voucher)) {
						String endDate =  new SimpleDateFormat("dd-MM-yyyy").format(voucher.getExpiryDate());
						String startDate =  new SimpleDateFormat("dd-MM-yyyy").format(voucher.getDownloadedDate());
						promoCodeDetails.setEndDate(endDate);
						promoCodeDetails.setStartDate(startDate);
						int duration  = ((int) ((voucher.getExpiryDate().getTime() - voucher.getDownloadedDate().getTime())/ 1000/60/60/24))+1;
						promoCodeDetails.setPromoCodeDuration(duration);
					}
	//				String endDate =  new SimpleDateFormat("dd-MM-yyyy").format(eligibilityInfo.getOffer().getVoucherInfo().getVoucherExpiryDate());
	//				String startDate =  new SimpleDateFormat("dd-MM-yyyy").format(new Date());
					promocodeRequest.setPromoCodeDetails(promoCodeDetails);
					LOG.info("promoCodeRequest:{} ", promocodeRequest.toString());
					resultResponse = promoCodeDomain.createPromoCode(promocodeRequest, resultResponse, headers.getProgram(), headers.getUserName());
					LOG.info("resultResponse :{}",resultResponse);
					if(resultResponse.getResult().getResponse().equals("2800")) {
						response = true;
					}
					if(!response) {
						voucher.setBlackListed(true);
						voucher.setBlacklistedDate(new Date());
						voucher.setBlackListedUser("");
					}
				}
			}
			LOG.info("Exiting savePromocodeDetails method");
			
		} catch(Exception e) {
			e.printStackTrace();
			exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), voucherRequestDto.getAccountNumber(), headers.getUserName());
		}
	}

}

