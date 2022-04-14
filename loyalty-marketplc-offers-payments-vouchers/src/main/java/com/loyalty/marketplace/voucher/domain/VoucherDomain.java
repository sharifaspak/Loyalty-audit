package com.loyalty.marketplace.voucher.domain;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.gifting.inbound.dto.VoucherGiftRequest;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.constants.VoucherStatus;
import com.loyalty.marketplace.voucher.inbound.dto.BurnCashVoucher;
import com.loyalty.marketplace.voucher.inbound.dto.BurnVoucherRequest;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherRequestDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherTransferRequest;
import com.loyalty.marketplace.voucher.outbound.database.entity.BurntInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.CashVoucherBurntInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.OfferInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherGiftDetails;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherTransfer;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherValues;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.voucher.outbound.database.service.SequenceGeneratorService;
import com.loyalty.marketplace.voucher.outbound.dto.BurnVoucher;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResponse;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.loyalty.marketplace.voucher.utils.VoucherValidator;
import com.mongodb.DBRef;
import com.mongodb.client.result.UpdateResult;

import lombok.ToString;

@Component
@ToString
public class VoucherDomain {

		@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	Validator validator;

	@Autowired
	AuditService auditService;

	@Autowired
	MongoOperations mongoOperations;

	@Autowired
	PurchaseRepository purchaseRepository;

	private static final Logger LOG = LoggerFactory.getLogger(VoucherDomain.class);
	private static final String BURNID_SEQ_KEY = "BurnId";
	private static final String VOUCHERCOLLECTION = "Voucher";
	private static final String STATUS = "Status";
	private static final String VOUCHERCODE = "VoucherCode";
	private static final String BLACKLISTEDUSER = "BlackListedUser";
	private static final String BLACKLISTEDDATE = "BlackListedDate";
	private static final String ISBLACKLISTED = "IsBlackListed";
	private static final String UPDATEDUSER = "UpdatedUser";
	private static final String UPDATEDDATE = "UpdatedDate";
	private static final String EXPIRYDATE = "ExpiryDate";
	private static final String ACCOUNTNUMBER = "AccountNumber";
	private static final String MEMBERSHIPCODE = "MembershipCode";
	private static final String VOUCHERAMOUNT = "VoucherAmount";
	private static final String UUID = "UUID";
	private static final String BARCODETYPE = "BarcodeType";
	private static final String VOUCHERVALUES = "VoucherValues";
	private static final String PARTNERCODE = "PartnerCode";
	private static final String DOWNLOADEDDATE = "DownloadedDate";
	private static final String MERCHANTINVOICEID = "MerchantInvoiceId";
	private static final String BURNTINFO = "BurntInfo";
	private static final String TRANSFER = "Transfer";
	private static final String GIFTDETAILS = "GiftDetails";
	private static final String STARTDATE = "StartDate";
	private static final String VOUCHERBALANCE = "VoucherBalance";
	public static final String BURNENQUIRYCOUNT = "BurnEnquiryCount";
	public static final String LASTBURNENQUIRYUSER = "LastburnEnquiryUser";
	public static final String LASTENQUIRYDATE = "LastEnquiryDate";
	public static final String CHANNEL = "Channel";
	
	private String programCode;
	private String id;
	private String voucherCode;
	private OfferInfo offerInfo;
	private String merchantCode;
	private String partnerCode;
	private String merchantName;
	private String membershipCode;
	private String accountNumber;
	private String type;
	private String status;
	private Date expiryDate;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	private Double cost;
	private Double voucherAmount;
	private long pointsValue;
	private Barcode barcodeType;
	private String uuid;
	private Date uploadDate;
	private Date startDate;
	private Date endDate;
	private String partnerReferNumber;
	private String voucherType;
	private String partnerTransactionId;
	private Double voucherBalance;
	private int burnEnquiryCount;
	private String lastburnEnquiryUser;
	private Date lastEnquiryDate;
	private String channel;
	private String voucherPin;
	private String fileName;



	public String getPartnerCode() {
		return partnerCode;
	}

	public Double getVoucherBalance() {
		return voucherBalance;
	}

	public String getPartnerReferNumber() {
		return partnerReferNumber;
	}

	public String getVoucherType() {
		return voucherType;
	}

	public String getUuid() {
		return uuid;
	}

	public Barcode getBarcodeType() {
		return barcodeType;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public Double getCost() {
		return cost;
	}

	public String getProgramCode() {
		return programCode;
	}

	public String getId() {
		return id;
	}

	public OfferInfo getOfferInfo() {
		return offerInfo;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public String getMembershipCode() {
		return membershipCode;
	}

	public Double getVoucherAmount() {
		return voucherAmount;
	}

	public long getPointsValue() {
		return pointsValue;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getPartnerTransactionId() {
		return partnerTransactionId;
	}

	public int getBurnEnquiryCount() {
		return burnEnquiryCount;
	}

	public String getLastburnEnquiryUser() {
		return lastburnEnquiryUser;
	}

	public Date getLastEnquiryDate() {
		return lastEnquiryDate;
	}
	
	public String getVoucherPin() {
		return voucherPin;
	}

	public String getFileName() {
		return fileName;
	}
	
	public VoucherDomain() {

	}

	public VoucherDomain(VoucherBuilder voucherBuilder) {
		this.programCode = voucherBuilder.programCode;
		this.id = voucherBuilder.id;
		this.offerInfo = voucherBuilder.offerInfo;
		this.membershipCode = voucherBuilder.membershipCode;
		this.merchantCode = voucherBuilder.merchantCode;
		this.partnerCode = voucherBuilder.partnerCode;
		this.merchantName = voucherBuilder.merchantName;
		this.accountNumber = voucherBuilder.accountNumber;
		this.type = voucherBuilder.type;
		this.status = voucherBuilder.status;
		this.expiryDate = voucherBuilder.expiryDate;
		this.createdUser = voucherBuilder.createdUser;
		this.createdDate = voucherBuilder.createdDate;
		this.updatedUser = voucherBuilder.updatedUser;
		this.updatedDate = voucherBuilder.updatedDate;
		this.voucherCode = voucherBuilder.voucherCode;
		this.cost = voucherBuilder.cost;
		this.pointsValue = voucherBuilder.pointsValue;
		this.voucherAmount = voucherBuilder.voucherAmount;
		this.barcodeType = voucherBuilder.barcodeType;
		this.uuid = voucherBuilder.uuid;
		this.startDate = voucherBuilder.startDate;
		this.endDate = voucherBuilder.endDate;
		this.uploadDate = voucherBuilder.uploadDate;
		this.partnerReferNumber = voucherBuilder.partnerReferNumber;
		this.voucherType = VoucherConstants.VOUCHER_TYPE;
		this.partnerTransactionId = voucherBuilder.partnerTransactionId;
		this.voucherBalance = voucherBuilder.voucherBalance;
		this.burnEnquiryCount = voucherBuilder.burnEnquiryCount;
		this.lastburnEnquiryUser = voucherBuilder.lastburnEnquiryUser;
		this.lastEnquiryDate = voucherBuilder.lastEnquiryDate;
		this.voucherPin = voucherBuilder.voucherPin;
		this.channel = voucherBuilder.channel;
		this.fileName=voucherBuilder.fileName;		
	}

	public static class VoucherBuilder {

		private String programCode;
		private String id;
		private String partnerCode;
		private String voucherCode;
		private OfferInfo offerInfo;
		private String merchantCode;
		private String merchantName;
		private String membershipCode;
		private String accountNumber;
		private String status;
		private Date expiryDate;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;
		private Double cost;
		private Double voucherAmount;
		private long pointsValue;
		private Barcode barcodeType;
		private String uuid;
		private Date startDate;
		private Date endDate;
		private Date uploadDate;
		private String partnerReferNumber;
		private String type;
		private String partnerTransactionId;
		private Double voucherBalance;
		private int burnEnquiryCount;
		private String lastburnEnquiryUser;
		private Date lastEnquiryDate;
		private String voucherPin;
		private String channel;
		private String fileName;
		
		public VoucherBuilder(String programCode, String voucherCode, OfferInfo offerInfo, String merchantCode,
				String merchantName, String membershipCode, String accountNumber, Barcode barcode) {
			super();
			this.programCode = programCode;
			this.offerInfo = offerInfo;
			this.voucherCode = voucherCode;
			this.merchantCode = merchantCode;
			this.merchantName = merchantName;
			this.membershipCode = membershipCode;
			this.accountNumber = accountNumber;
			this.barcodeType = barcode;
		}
		
		public VoucherBuilder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}
		
		public VoucherBuilder voucherBalance(Double voucherBalance) {
			this.voucherBalance = voucherBalance;
			return this;
		}

		public VoucherBuilder expiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
			return this;
		}

		public VoucherBuilder voucherAmount(Double voucherAmount) {
			this.voucherAmount = voucherAmount;
			return this;
		}

		public VoucherBuilder pointsValue(long points) {
			this.pointsValue = points;
			return this;
		}

		public VoucherBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public VoucherBuilder cost(Double cost) {
			this.cost = cost;
			return this;
		}

		public VoucherBuilder id(String id) {
			this.id = id;
			return this;
		}

		public VoucherBuilder status(String status) {
			this.status = status;
			return this;
		}

		public VoucherBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public VoucherBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public VoucherBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public VoucherBuilder uuid(String uuid) {
			this.uuid = uuid;
			return this;
		}

		public VoucherBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public VoucherBuilder startDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}

		public VoucherBuilder endDate(Date endDate) {
			this.endDate = endDate;
			return this;
		}

		public VoucherBuilder uploadDate(Date uploadDate) {
			this.uploadDate = uploadDate;
			return this;
		}

		public VoucherBuilder partnerCode(String partnerCode) {
			this.partnerCode = partnerCode;
			return this;
		}

		public VoucherBuilder partnerReferNumber(String partnerReferNumber) {
			this.partnerReferNumber = partnerReferNumber;
			return this;
		}

		public VoucherBuilder partnerTransactionId(String partnerTransactionId) {
			this.partnerTransactionId = partnerTransactionId;
			return this;
		}

		public VoucherBuilder type(String type) {
			this.type = type;
			return this;
		}

		public VoucherBuilder burnEnquiryCount(int burnEnquiryCount) {
			this.burnEnquiryCount = burnEnquiryCount;
			return this;
		}

		public VoucherBuilder lastburnEnquiryUser(String lastburnEnquiryUser) {
			this.lastburnEnquiryUser = lastburnEnquiryUser;
			return this;
		}

		public VoucherBuilder lastEnquiryDate(Date lastEnquiryDate) {
			this.lastEnquiryDate = lastEnquiryDate;
			return this;
		}
		public VoucherBuilder voucherPin(String voucherPin) {
			this.voucherPin = voucherPin;
			return this;
		}
		
		public VoucherBuilder channel(String channel) {
			this.channel = channel;
			return this;
		}

		public VoucherDomain build() {
			return new VoucherDomain(this);
		}

	}


	public Voucher saveVoucher(VoucherDomain vouDom, String externalTransactionId, String userName,
			String isBirthdayGift, String channelId) throws VoucherManagementException {
		LOG.info("saveVoucher VoucherDomain: {}", vouDom);
		LOG.info("voucherDomain.voucherPin: {}", vouDom.getVoucherPin());
		try {
			Voucher voucherToSave = modelMapper.map(vouDom, Voucher.class);
			LOG.info("voucherToSave.voucherPin: {}", voucherToSave.getVoucherPin());
			VoucherValues voucherValues = new VoucherValues();
			voucherValues.setCost(vouDom.getCost());
			voucherValues.setPointsValue(vouDom.getPointsValue());
			if (null != vouDom.getUuid()) {
				PurchaseHistory offerPurchaseHistory = new PurchaseHistory();
				offerPurchaseHistory.setId(vouDom.getUuid());
				voucherToSave.setUuid(offerPurchaseHistory);
			}
			if (null != voucherToSave.getStartDate()) {
				voucherToSave.setStartDate(new Date());
			}
			if (isBirthdayGift.equalsIgnoreCase("MAMBA")) {
				voucherToSave.setVoucherBalance(vouDom.getVoucherAmount());
			}
			if(StringUtils.isEmpty(voucherToSave.getChannel())) {
				
				voucherToSave.setChannel(channelId);
			}
			voucherToSave.setVoucherValues(voucherValues);
			VoucherGiftDetails giftDetails = new VoucherGiftDetails();
			giftDetails.setIsGift(isBirthdayGift);
			voucherToSave.setGiftDetails(giftDetails);
			voucherToSave.setDownloadedDate(new Date());
			VoucherValidator.validateEntity(voucherToSave, validator);
			this.voucherRepository.insert(voucherToSave);
//			auditService.insertDataAudit(DBConstants.VOUCHERS, voucherToSave,
//					VoucherRequestMappingConstants.GENERATE_VOUCHERS, externalTransactionId, userName);
			LOG.info("saveVoucher : voucherToSave{}", voucherToSave);
			return voucherToSave;
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "saveVoucher", e.getMessage(),
					VoucherManagementCode.VOUCHER_GENERATION_FAILED);
		}
	}

	public void cancelVoucher(String userName, List<Voucher> voucher, String externalTransactionId)
			throws VoucherManagementException {
		LOG.info("cancelVoucher voucher: {}", voucher);
		try {

			List<Voucher> originalVoucherList = new ArrayList<>();

			voucher.stream().forEach(c -> {
				Gson gson = new Gson();
				Voucher originalVoucher = gson.fromJson(gson.toJson(c), Voucher.class);
				originalVoucherList.add(originalVoucher);
			});

			List<String> vCodes = voucher.stream().map(e -> e.getVoucherCode()).collect(Collectors.toList());

			Query query = new Query(new Criteria(VOUCHERCODE).in(vCodes));
			Update update = new Update().set(STATUS, VoucherStatus.CANCELED).set(BLACKLISTEDUSER, userName)
					.set(BLACKLISTEDDATE, new Date()).set(ISBLACKLISTED, true).set(UPDATEDUSER, userName)
					.set(UPDATEDDATE, new Date());
			UpdateResult res = mongoOperations.updateMulti(query, update, VOUCHERCOLLECTION);
			if (res.getModifiedCount() == 0) {
				throw new VoucherManagementException(this.getClass().toString(), "cancelVoucher",
						"Could not cancel vouchers", VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
			}

			auditService.updateDataAudit(DBConstants.VOUCHERS, voucher, VoucherRequestMappingConstants.VOUCHER_CANCEL,
					originalVoucherList, externalTransactionId, userName);
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "cancelVoucher", e.getMessage(),
					VoucherManagementCode.VOUCHER_CANCELLATION_FAILED);
		}
	}
	
	public boolean updateVoucherWithStatus(List<Voucher> voucher,VoucherRequestDto voucherRequestDto, String userName, Date expiryDate, 
			double cost, long points, String externalTransactionId, int retry, VoucherResponse resultResponse, boolean exactVouchersPresent, String channelId) throws VoucherManagementException {
		
		boolean updateStatus = false;
		try {
			List<Voucher> originalVoucherList = new ArrayList<>();
			
			voucher.stream().forEach(c -> {
				Gson gson = new Gson();
			    Voucher originalVoucher = gson.fromJson(gson.toJson(c), Voucher.class);	
			    originalVoucherList.add(originalVoucher);
			});
			
			List<String> vCodes=voucher.stream().map(e -> e.getVoucherCode()).collect(Collectors.toList());
			
			
			PurchaseHistory offerPurchaseHistory = new PurchaseHistory();
			offerPurchaseHistory.setId(voucherRequestDto.getUuid());
			
			
			VoucherValues voucherVal = new VoucherValues();
			voucherVal.setPointsValue(points);
			voucherVal.setCost(cost);
				
			
			VoucherGiftDetails voucherGiftDetails = new VoucherGiftDetails();
			String isGift = birthdayGiftValue(voucherRequestDto);
			voucherGiftDetails.setIsGift(isGift);
			
			boolean purchaseExistsById = purchaseRepository.existsById(voucherRequestDto.getUuid());
			
			DBRef dbRefPurchaseId=null;
			
			if(purchaseExistsById) {				
				dbRefPurchaseId = new DBRef(OffersDBConstants.PURCHASE_HISTORY, new ObjectId(voucherRequestDto.getUuid()));
			}
			else {				
				dbRefPurchaseId =new DBRef(OffersDBConstants.PURCHASE_HISTORY, voucherRequestDto.getUuid());
			}
		
			DBRef dbRefBarcodeType=new DBRef("Barcode", new ObjectId(voucherRequestDto.getBarcodeType().getId()));
			Query queryExpiryNotNull = new Query(new Criteria().andOperator(Criteria.where(VOUCHERCODE).in(vCodes),Criteria.where(EXPIRYDATE).ne(null)));			
			Update updateExpiryNotNull = new Update().set(STATUS, VoucherStatus.ACTIVE).set(ACCOUNTNUMBER, voucherRequestDto.getAccountNumber())
					.set(MEMBERSHIPCODE, voucherRequestDto.getMembershipCode()).set(VOUCHERAMOUNT, voucherRequestDto.getVoucherAmount()).set(UUID, dbRefPurchaseId)
					.set(BARCODETYPE,dbRefBarcodeType).set(VOUCHERVALUES,voucherVal).set(PARTNERCODE,voucherRequestDto.getPartnerCode())
					.set(DOWNLOADEDDATE,new Date()).set(UPDATEDUSER,userName).set(UPDATEDDATE, new Date())
					.set(GIFTDETAILS, voucherGiftDetails)
					.set(CHANNEL, channelId);
			
			UpdateResult resExpiryNotNull =mongoOperations.updateMulti(queryExpiryNotNull, updateExpiryNotNull, VOUCHERCOLLECTION);
			
			
			Query query = new Query(new Criteria().andOperator(Criteria.where(VOUCHERCODE).in(vCodes),Criteria.where(EXPIRYDATE).is(null)));			
			Update update = new Update().set(STATUS, VoucherStatus.ACTIVE).set(ACCOUNTNUMBER, voucherRequestDto.getAccountNumber())
					.set(MEMBERSHIPCODE, voucherRequestDto.getMembershipCode()).set(VOUCHERAMOUNT, voucherRequestDto.getVoucherAmount()).set(UUID, dbRefPurchaseId)
					.set(BARCODETYPE,dbRefBarcodeType).set(VOUCHERVALUES,voucherVal).set(PARTNERCODE,voucherRequestDto.getPartnerCode())
					.set(EXPIRYDATE,expiryDate).set(DOWNLOADEDDATE,new Date()).set(UPDATEDUSER,userName).set(UPDATEDDATE, new Date())
					.set(GIFTDETAILS, voucherGiftDetails)
					.set(CHANNEL, channelId);
			UpdateResult res =mongoOperations.updateMulti(query, update, VOUCHERCOLLECTION);
			
//			if(res.getModifiedCount()==0 
//			&& resExpiryNotNull.getModifiedCount()==0 
//			&& (retry==2 || exactVouchersPresent)) {
////				throw new VoucherManagementException(this.getClass().toString(), "generateVoucher", "Could not generate vouchers",
////						VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
//				
//				
//			
//			} else 
			
			if(res.getModifiedCount()!=0 || resExpiryNotNull.getModifiedCount()!=0) {
				
				updateStatus = true;
				auditService.updateDataAudit(DBConstants.VOUCHERS, voucher,VoucherRequestMappingConstants.GENERATE_VOUCHERS, originalVoucherList, externalTransactionId, userName);
			}	
			
		}
		catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "updateVoucherWithStatus", e.getMessage(),
					VoucherManagementCode.VOUCHER_STATUS_FAILED);
		}
		
		return updateStatus;
	}

	public void uploadVoucher(VoucherDomain voucher, String externalTransactionId, String userName)
			throws VoucherManagementException {
		try {
			Voucher voucherToSave = modelMapper.map(voucher, Voucher.class);
			voucherToSave.setBulkId(voucherToSave.getMerchantCode() + voucherToSave.getVoucherCode());
			voucherToSave.setVoucherType(VoucherConstants.VOUCHER_TYPE);
			VoucherValues voucherValues = new VoucherValues();
			voucherValues.setCost(0.0);
			voucherValues.setPointsValue(0);
			voucherToSave.setVoucherValues(voucherValues);
			voucherToSave.setVoucherAmount(voucher.getVoucherAmount());

			VoucherGiftDetails giftDetails = new VoucherGiftDetails();
			giftDetails.setIsGift("NO");
			voucherToSave.setGiftDetails(giftDetails);

			LOG.info("upload voucher voucher code : {} ", voucher.getVoucherCode());
			LOG.info("upload voucher domain object : {} ", voucherToSave);
			this.voucherRepository.save(voucherToSave);
//			auditService.insertDataAudit(DBConstants.VOUCHERS, voucherToSave,
//					VoucherRequestMappingConstants.VOUCHER_UPLOAD, externalTransactionId, userName);

		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "uploadVouchererror", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}
	}

	public void burnVoucher(Voucher voucher, BurnVoucherRequest burnVoucherRequest, String userName,
			BurnVoucher burnVoucher, String storeCode, String externalTransactionId) throws VoucherManagementException {

		try {
			Gson gson = new Gson();
			Voucher originalVoucher = gson.fromJson(gson.toJson(voucher), Voucher.class);

			BurntInfo burntInfo = new BurntInfo();
			String burnId = String.valueOf(sequenceGeneratorService.generateSequence(BURNID_SEQ_KEY));
			burntInfo.setVoucherBurntId(burnId);
			burntInfo.setBurnNotes(burnVoucherRequest.getRemarks());
			burntInfo.setStoreCode(storeCode);
			burntInfo.setVoucherBurnt(true);
			burntInfo.setVoucherBurntDate(new Date());
			burntInfo.setVoucherBurntUser(userName);
			voucher.setStatus(VoucherStatus.BURNT);
			voucher.setMerchantInvoiceId(burnVoucherRequest.getInvoiceId());
			voucher.setUpdatedDate(new Date());
			voucher.setUpdatedUser(userName);
			voucher.setBurntInfo(burntInfo);

			Query query = new Query(new Criteria(VOUCHERCODE).is(voucher.getVoucherCode()));
			Update update = new Update().set(STATUS, VoucherStatus.BURNT)
					.set(MERCHANTINVOICEID, burnVoucherRequest.getInvoiceId()).set(UPDATEDDATE, new Date())
					.set(UPDATEDUSER, userName).set(BURNTINFO, burntInfo);
			UpdateResult res = mongoOperations.updateFirst(query, update, VOUCHERCOLLECTION);

			if (res.getModifiedCount() != 1) {
				throw new VoucherManagementException(this.getClass().toString(), "burnVoucher",
						"Could not burn voucher", VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
			}
			auditService.updateDataAudit(DBConstants.VOUCHERS, voucher, VoucherRequestMappingConstants.VOUCHER_BURN,
					originalVoucher, externalTransactionId, userName);

			if (null != voucher.getUuid()) {
				burnVoucher.setTransactionRefId(voucher.getUuid().getId());
			}
			burnVoucher.setVoucherCode(voucher.getVoucherCode());
			burnVoucher.setBurnId(burnId);
			LOG.info("Burnt Voucher : {} ", voucher);
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "burnVoucher", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}

	}

	public void transferVoucher(Voucher voucher, VoucherTransferRequest voucherTransferRequest, String userName,
			String memberShipCode, String externalTransactionId) throws VoucherManagementException {

		try {
			Gson gson = new Gson();
			Voucher originalVoucher = gson.fromJson(gson.toJson(voucher), Voucher.class);
			VoucherTransfer transfer = new VoucherTransfer(true, voucher.getAccountNumber(),
					voucherTransferRequest.getAgentName());
			VoucherGiftDetails giftDetails = new VoucherGiftDetails();
			giftDetails.setIsGift("NO");

			Query query = new Query(new Criteria(VOUCHERCODE).is(voucher.getVoucherCode()));
			Update update = new Update().set(ACCOUNTNUMBER, voucherTransferRequest.getTargetAccountNumber())
					.set(MEMBERSHIPCODE, memberShipCode).set(UPDATEDDATE, new Date()).set(UPDATEDUSER, userName)
					.set(TRANSFER, transfer).set(GIFTDETAILS, giftDetails);
			UpdateResult res = mongoOperations.updateFirst(query, update, VOUCHERCOLLECTION);
			if (res.getModifiedCount() != 1) {
				throw new VoucherManagementException(this.getClass().toString(), "transfer",
						"Could not transfer voucher", VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
			}

			auditService.updateDataAudit(DBConstants.VOUCHERS, voucher, VoucherRequestMappingConstants.VOUCHER_TRANSFER,
					originalVoucher, externalTransactionId, userName);

			LOG.info("transfer Voucher : {} ", voucher);
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "transferVoucher", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}

	}

	/**
	 * This method saves the specified voucher to db
	 * 
	 * @param voucher
	 * @param voucherGiftRequest
	 * @param userName
	 * @param memberShipCode
	 * @param externalTransactionId
	 * @param giftId
	 * @throws VoucherManagementException
	 */
	public void giftVoucher(Voucher voucher, VoucherGiftRequest voucherGiftRequest, String userName,
			String memberShipCode, String externalTransactionId, String giftId, Double crfrBalance, String isCRFR)
			throws VoucherManagementException {
		try {
			Gson gson = new Gson();
			Voucher originalVoucher = gson.fromJson(gson.toJson(voucher), Voucher.class);

			VoucherGiftDetails giftDetails = new VoucherGiftDetails();
			giftDetails.setIsGift("YES");
			giftDetails.setGiftedAccountNumber(originalVoucher.getAccountNumber());
			giftDetails.setGiftId(giftId);

			Query query = new Query(new Criteria(VOUCHERCODE).is(voucher.getVoucherCode()));
			Update update = new Update();
			if (null != isCRFR && isCRFR.equalsIgnoreCase("YES")) {
				update.set(ACCOUNTNUMBER, voucherGiftRequest.getReceiverAccountNumber())
						.set(MEMBERSHIPCODE, memberShipCode).set(UPDATEDDATE, new Date()).set(UPDATEDUSER, userName)
						.set(STARTDATE, voucherGiftRequest.getScheduledDate()).set(GIFTDETAILS, giftDetails)
						.set(VOUCHERBALANCE, crfrBalance);
			} else {
				update.set(ACCOUNTNUMBER, voucherGiftRequest.getReceiverAccountNumber())
						.set(MEMBERSHIPCODE, memberShipCode).set(UPDATEDDATE, new Date()).set(UPDATEDUSER, userName)
						.set(STARTDATE, voucherGiftRequest.getScheduledDate()).set(GIFTDETAILS, giftDetails);
			}

			UpdateResult res = mongoOperations.updateFirst(query, update, VOUCHERCOLLECTION);
			if (res.getModifiedCount() != 1) {
				throw new VoucherManagementException(this.getClass().toString(), "giftVoucher",
						"Could not gift voucher", VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
			}
			auditService.updateDataAudit(DBConstants.VOUCHERS, voucher, VoucherRequestMappingConstants.GIFT_VOUCHER,
					originalVoucher, externalTransactionId, userName);

			LOG.info("gift Voucher : {} ", voucher);
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "giftVoucher", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}
	}

	public List<Voucher> updateCarrefourRedeemedVouchers(List<Voucher> voucherRedeemedCarrefour, BigDecimal balance,
			String externalTransactionId, String userName, String api) {

		List<Voucher> vouchersToSave = new ArrayList<>();
		List<String> vCodes = voucherRedeemedCarrefour.stream().map(e -> e.getVoucherCode())
				.collect(Collectors.toList());

		LOG.info("Updating CRFR voucher: {}, balance: {}, api: {}, ext trn id: {}, username: {}",
				voucherRedeemedCarrefour, balance, api, externalTransactionId, userName);
		BurntInfo burntInfo = new BurntInfo();
		burntInfo.setVoucherBurntDate(new Date());
		burntInfo.setVoucherBurnt(true);

		for (Voucher voucher : voucherRedeemedCarrefour) {

			voucher.setStatus(VoucherStatus.BURNT);
			voucher.setVoucherBalance(null != balance ? balance.doubleValue() : 0.0);

			if (null != voucher.getBurntInfo()) {
				voucher.getBurntInfo().setVoucherBurntDate(new Date());
				voucher.getBurntInfo().setVoucherBurnt(true);
			} else {
				voucher.setBurntInfo(burntInfo);
			}

			voucher.setUpdatedUser(userName);
			voucher.setUpdatedDate(new Date());

			vouchersToSave.add(voucher);

		}

		Query queryExpiryNotNull = new Query(new Criteria(VOUCHERCODE).in(vCodes));
		Update updateExpiryNotNull = new Update().set(STATUS, VoucherStatus.BURNT)
				.set(VOUCHERBALANCE, null != balance ? balance.doubleValue() : 0.0).set(BURNTINFO, burntInfo)
				.set(UPDATEDUSER, userName).set(UPDATEDDATE, new Date());
		mongoOperations.updateMulti(queryExpiryNotNull, updateExpiryNotNull, VOUCHERCOLLECTION);

		List<Voucher> savedVouchers = this.voucherRepository.findByVoucherCodeIn(vCodes);
		auditService.updateDataAudit(DBConstants.VOUCHERS, vouchersToSave, api, voucherRedeemedCarrefour,
				externalTransactionId, userName);

		return savedVouchers;

	}

	public void updateCarrefourBalance(Voucher voucher, BigDecimal balance, String userName) {

		LOG.info("Updating CRFR voucher: {}, balance: {}", voucher, balance);

		voucher.setVoucherBalance(null != balance ? balance.doubleValue() : 0.0);
		voucher.setUpdatedUser(userName);
		voucher.setUpdatedDate(new Date());

		voucherRepository.save(voucher);
	}

	public void burnCashVoucher(Voucher voucher, BurnCashVoucher burnVoucherRequest, String userName,
			BurnVoucher burnVoucher, String externalTransactionId) throws VoucherManagementException {

		try {
			Gson gson = new Gson();
			Voucher originalVoucher = gson.fromJson(gson.toJson(voucher), Voucher.class);

			DecimalFormat df = new DecimalFormat("###.##");
			Double newBalance = Double.parseDouble(df.format(voucher.getVoucherBalance()))
					- Double.parseDouble(df.format(burnVoucherRequest.getAmountToDeduct()));
			BurntInfo burntInfo = new BurntInfo();
			String burnId = "";
			if (newBalance.equals(0.0)) {
				burnId = String.valueOf(sequenceGeneratorService.generateSequence(BURNID_SEQ_KEY));

				burntInfo.setVoucherBurntId(burnId);
				burntInfo.setVoucherBurnt(true);
				burntInfo.setVoucherBurntDate(new Date());
				burntInfo.setVoucherBurntUser(userName);
				voucher.setStatus(VoucherStatus.BURNT);
				voucher.setUpdatedDate(new Date());
				voucher.setUpdatedUser(userName);
				voucher.setBurntInfo(burntInfo);
			}

			CashVoucherBurntInfo cashVoucherBurntInfo = new CashVoucherBurntInfo();
			// cashVoucherBurntInfo.setVoucherBurnt(true);
			cashVoucherBurntInfo.setAction("burn");
			cashVoucherBurntInfo.setVoucherBurntDate(new Date());
			cashVoucherBurntInfo.setExternalTransactionId(externalTransactionId);
			cashVoucherBurntInfo.setOrderId(burnVoucherRequest.getOrderId());
			cashVoucherBurntInfo.setVoucherBurntUser(userName);
			cashVoucherBurntInfo.setVoucherCurrentBalance(voucher.getVoucherBalance());
			cashVoucherBurntInfo.setVoucherNewBalance(newBalance);
			List<CashVoucherBurntInfo> cashVoucherBurntInfoList = new ArrayList<>();
			cashVoucherBurntInfoList.add(cashVoucherBurntInfo);
			if (voucher.getCashVoucherBurntInfo() != null) {
				voucher.getCashVoucherBurntInfo().add(cashVoucherBurntInfo);
			} else {
				voucher.setCashVoucherBurntInfo(cashVoucherBurntInfoList);
			}
			Query query = new Query(new Criteria(VOUCHERCODE).is(voucher.getVoucherCode()));
			Update update = new Update();
			if (newBalance.equals(0.0)) {
				update.set(STATUS, voucher.getStatus()).set("CashVoucherBurntInfo", voucher.getCashVoucherBurntInfo())
						.set(UPDATEDDATE, new Date()).set(UPDATEDUSER, userName).set(BURNTINFO, burntInfo)
						.set("VoucherBalance", newBalance);
			} else {
				update.set(STATUS, voucher.getStatus()).set("CashVoucherBurntInfo", voucher.getCashVoucherBurntInfo())
						.set(UPDATEDDATE, new Date()).set(UPDATEDUSER, userName).set("VoucherBalance", newBalance);
			}
			UpdateResult res = mongoOperations.updateFirst(query, update, VOUCHERCOLLECTION);

			if (res.getModifiedCount() != 1) {
				throw new VoucherManagementException(this.getClass().toString(), "burnCashVoucher",
						"Could not burn mamba voucher", VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
			}
			auditService.updateDataAudit(DBConstants.VOUCHERS, voucher,
					VoucherRequestMappingConstants.BURN_CASH_VOUCHER, originalVoucher, externalTransactionId, userName);

			if (null != voucher.getUuid()) {
				burnVoucher.setTransactionRefId(voucher.getUuid().getId());
			}
			burnVoucher.setVoucherCode(voucher.getVoucherCode());
			if (newBalance.equals(0.0)) {
				burnVoucher.setBurnId(burnId);
			} else {
				burnVoucher.setBurnId(externalTransactionId);
			}
			LOG.info("Mamba Burnt Voucher : {} ", voucher);
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "burnMambaVoucher", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}

	}

	public void updateEnquiryDetails(List<String> voucherCodeList, String userName) throws VoucherManagementException {

		try {
			
			Query query = new Query();
			query.addCriteria(
				Criteria.where(VOUCHERCODE).in(voucherCodeList)
			);
			
			Update update = new Update()
					.set(LASTBURNENQUIRYUSER, userName).set(LASTENQUIRYDATE, new Date()).inc(BURNENQUIRYCOUNT, 1);
			
			
			UpdateResult res = mongoOperations.updateMulti(query, update, VOUCHERCOLLECTION);

			LOG.info("Burnt voucher enquiry/burn update count:{}",res.getModifiedCount());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoucherManagementException(this.getClass().toString(), "updateVoucher", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}

	}
	
	public String birthdayGiftValue(VoucherRequestDto voucherRequestDto) {
		String isBirthdayGift = (voucherRequestDto.getIsBirthdayGift() == null
				|| voucherRequestDto.getIsBirthdayGift().equalsIgnoreCase(OfferConstants.FLAG_NOT_SET.get())) ? "NO"
						: "BIRTHDAY";
		isBirthdayGift = voucherRequestDto.isMambaFoodVoucher() ? "MAMBA" : isBirthdayGift;
		isBirthdayGift = voucherRequestDto.isSubscPromo() ? "YES" : isBirthdayGift;
		return isBirthdayGift;
	}



}

