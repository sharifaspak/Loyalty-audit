package com.loyalty.marketplace.gifting.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.helper.Utility;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingHistoryRepository;
import com.loyalty.marketplace.gifting.outbound.dto.GoldCertificateTransactionResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftingHistoryResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGiftingHistoryResult;
import com.loyalty.marketplace.gifting.outbound.dto.MemberInfoResponse;
import com.loyalty.marketplace.gifting.outbound.dto.PointsGiftTransactionResponse;
import com.loyalty.marketplace.gifting.outbound.dto.PurchaseDetailsResponse;
import com.loyalty.marketplace.gifting.outbound.dto.TransactionResponse;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class GiftingHistoryDomain {

	private static final Logger LOG = LoggerFactory.getLogger(GiftingHistoryDomain.class);

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	GiftingHistoryRepository giftingHistoryRepository;
	
	private String id;
	private String programCode;
	private String giftType;
	private MemberInfoDomain senderInfo;
	private MemberInfoDomain receiverInfo;
	private String voucherCode;
	private PointsGiftedDetailsDomain pointsGifted;
	private List<GoldGiftedDetails> goldGifted;
	private Integer imageId;	
	private String imageUrl;
	private String message;	
	private Date scheduledDate;	
	private String transactionNo;
	private Date transactionDate;
	private String extRefNo;	
	private PurchaseDetailsDomain purchaseDetailsDomain;
	private String receiverConsumption;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;

	public GiftingHistoryDomain(GiftingHistoryBuilder giftingHistory) {

		this.id = giftingHistory.id;
		this.programCode = giftingHistory.programCode;
		this.giftType = giftingHistory.giftType;
		this.senderInfo = giftingHistory.senderInfo;
		this.receiverInfo = giftingHistory.receiverInfo;
		this.voucherCode = giftingHistory.voucherCode;
		this.pointsGifted = giftingHistory.pointsGifted;
		this.goldGifted = giftingHistory.goldGifted;
		this.imageId = giftingHistory.imageId;		
		this.imageUrl = giftingHistory.imageUrl;
		this.message = giftingHistory.message;		
		this.scheduledDate = giftingHistory.scheduledDate;		
		this.transactionNo = giftingHistory.transactionNo;
		this.transactionDate = giftingHistory.transactionDate;
		this.extRefNo = giftingHistory.extRefNo;		
		this.purchaseDetailsDomain = giftingHistory.purchaseDetailsDomain;
		this.receiverConsumption = giftingHistory.receiverConsumption;
		this.createdUser = giftingHistory.createdUser;
		this.createdDate = giftingHistory.createdDate;
		this.updatedUser = giftingHistory.updatedUser;
		this.updatedDate = giftingHistory.updatedDate;

	}

	public static class GiftingHistoryBuilder {

		private String id;
		private String programCode;
		private String giftType;
		private MemberInfoDomain senderInfo;
		private MemberInfoDomain receiverInfo;
		private String voucherCode;
		private PointsGiftedDetailsDomain pointsGifted;
		private List<GoldGiftedDetails> goldGifted;
		private Integer imageId;		
		private String imageUrl;
		private String message;		
		private Date scheduledDate;		
		private String transactionNo;
		private Date transactionDate;
		private String extRefNo;	
		private PurchaseDetailsDomain purchaseDetailsDomain;
		private String receiverConsumption;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;

		public GiftingHistoryBuilder(String giftType) {
			this.giftType = giftType;
		}

		public GiftingHistoryBuilder id(String id) {
			this.id = id;
			return this;
		}

		public GiftingHistoryBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public GiftingHistoryBuilder senderInfo(MemberInfoDomain senderInfo) {
			this.senderInfo = senderInfo;
			return this;
		}

		public GiftingHistoryBuilder receiverInfo(MemberInfoDomain receiverInfo) {
			this.receiverInfo = receiverInfo;
			return this;
		}

		public GiftingHistoryBuilder voucherCode(String voucherCode) {
			this.voucherCode = voucherCode;
			return this;
		}

		public GiftingHistoryBuilder pointsGifted(PointsGiftedDetailsDomain pointsGifted) {
			this.pointsGifted = pointsGifted;
			return this;
		}

		public GiftingHistoryBuilder goldGifted(List<GoldGiftedDetails> goldGifted) {
			this.goldGifted = goldGifted;
			return this;
		}

		public GiftingHistoryBuilder imageId(Integer imageId) {
			this.imageId = imageId;
			return this;
		}

		public GiftingHistoryBuilder message(String message) {
			this.message = message;
			return this;
		}		

		public GiftingHistoryBuilder imageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}
		
		public GiftingHistoryBuilder scheduledDate(Date scheduledDate) {
			this.scheduledDate = scheduledDate;
			return this;
		}

		public GiftingHistoryBuilder transactionNo(String transactionNo) {
			this.transactionNo = transactionNo;
			return this;
		}

		public GiftingHistoryBuilder transactionDate(Date transactionDate) {
			this.transactionDate = transactionDate;
			return this;
		}

		public GiftingHistoryBuilder extRefNo(String extRefNo) {
			this.extRefNo = extRefNo;
			return this;
		}
		
		public GiftingHistoryBuilder purchaseDetailsDomain(PurchaseDetailsDomain purchaseDetailsDomain) {
			this.purchaseDetailsDomain = purchaseDetailsDomain;
			return this;
		}

		public GiftingHistoryBuilder receiverConsumption(String receiverConsumption) {
			this.receiverConsumption = receiverConsumption;
			return this;
		}

		public GiftingHistoryBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public GiftingHistoryBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public GiftingHistoryBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public GiftingHistoryBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public GiftingHistoryDomain build() {
			return new GiftingHistoryDomain(this);
		}

	}

	/**
	 * This method is used to save/update to GiftingHistory collection.
	 * @param giftingHistoryDomain
	 * @param action
	 * @param headers
	 * @param existingGiftingHistory
	 * @param api
	 * @return
	 * @throws MarketplaceException
	 */
	public GiftingHistory saveUpdateGiftingHistory(GiftingHistoryDomain giftingHistoryDomain, String action,
			Headers headers, GiftingHistory existingGiftingHistory, String api) throws MarketplaceException {

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.DOMAIN_TO_SAVE,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_SAVE_DB,
				giftingHistoryDomain);

		try {

			GiftingHistory giftingHistory = modelMapper.map(giftingHistoryDomain, GiftingHistory.class);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.ENTITY_TO_SAVE,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_SAVE_DB, giftingHistory);

			GiftingHistory savedGiftingHistory = giftingHistoryRepository.save(giftingHistory);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_SAVE_DB,
					savedGiftingHistory);

			if (action.equalsIgnoreCase(GiftingConstants.ACTION_INSERT)) {

//				auditService.insertDataAudit(GiftingConfigurationConstants.GIFTING_HISTORY, savedGiftingHistory, api,
//						headers.getExternalTransactionId(), headers.getUserName());

			} else if (action.equalsIgnoreCase(GiftingConstants.ACTION_UPDATE)) {

				auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_HISTORY, savedGiftingHistory, api,
						existingGiftingHistory, headers.getExternalTransactionId(), headers.getUserName());

			}

			return savedGiftingHistory;

		} catch (MongoWriteException mongoException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_SAVE_DB,
					mongoException.getClass() + mongoException.getMessage(), GiftingCodes.MARKETPLACE_GIFTING_FAILURE);

		} catch (ValidationException validationException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_SAVE_DB,
					validationException.getClass() + validationException.getMessage(),
					GiftingCodes.VALIDATION_EXCEPTION);

		} catch (Exception e) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_SAVE_DB, e.getClass() + e.getMessage(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION);

		}

	}

	/**
	 * This method is used to mark if receiver has view voucher gift.
	 * @param voucherGift
	 * @param userName
	 * @param externalTransactionId
	 * @throws VoucherManagementException
	 */
	public void markReceiverConsumption(GiftingHistory voucherGift, String userName, String externalTransactionId) throws VoucherManagementException {
		
		try {
			Gson gson = new Gson();
			GiftingHistory originalVoucher = gson.fromJson(gson.toJson(voucherGift), GiftingHistory.class);			
			voucherGift.setReceiverConsumption("YES");				
			giftingHistoryRepository.save(voucherGift);
			auditService.updateDataAudit(DBConstants.VOUCHER_GIFT, voucherGift,VoucherRequestMappingConstants.GIFT_VOUCHER, originalVoucher,
			externalTransactionId, userName);

			LOG.info("gift Voucher : {} ",voucherGift);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new VoucherManagementException(this.getClass().toString(), "giftVoucher", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}
		
	}

	/**
	 * This method is used to list gifting History.
	 * @param accountNumber
	 * @param giftType
	 * @param filter
	 * @param headers
	 * @param listGiftingHistoryResponse
	 * @return
	 */
	public ListGiftingHistoryResponse listGiftingHistory(String accountNumber, String giftType, String filter, Headers headers, ListGiftingHistoryResponse listGiftingHistoryResponse) {
		
		try {

			if(!Utility.validateListGiftingHistoryRequest(accountNumber, giftType, filter, listGiftingHistoryResponse)) {
				listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getId(),
						GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getMsg());
				return listGiftingHistoryResponse; 
			}
			
			if(giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_VOUCHER)) giftType = GiftingConstants.GIFT_TYPE_VOUCHER;
			if(giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_POINTS)) giftType = GiftingConstants.GIFT_TYPE_POINTS;
			if(giftType.equalsIgnoreCase(GiftingConstants.GIFT_TYPE_GOLD)) giftType = GiftingConstants.GIFT_TYPE_GOLD;
			if(null == filter || filter.isEmpty()) filter = GiftingConstants.FILTER_ADMIN;
			
			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, listGiftingHistoryResponse, headers);  //Add headers instead of null
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS,
					this.getClass(), GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_LIST, memberDetails);

			if (null == memberDetails) {
				listGiftingHistoryResponse.addErrorAPIResponse(
						GiftingCodes.LISTING_GIFTING_HISTORY_MEMBER_DOES_NOT_EXIST.getIntId(),
						GiftingCodes.LISTING_GIFTING_HISTORY_MEMBER_DOES_NOT_EXIST.getMsg() + accountNumber);
				listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getId(),
						GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getMsg());
				return listGiftingHistoryResponse;
			}
			
			if (filter.equalsIgnoreCase(GiftingConstants.FILTER_ALL))
				populateAllListGiftResponse(accountNumber, memberDetails.getMembershipCode(), giftType, filter,
						listGiftingHistoryResponse);

			if (filter.equalsIgnoreCase(GiftingConstants.FILTER_SENT))
				populateSentListGiftResponse(accountNumber, memberDetails.getMembershipCode(), giftType,
						listGiftingHistoryResponse);

			if (filter.equalsIgnoreCase(GiftingConstants.FILTER_RECEIVED))
				populateReceivedListGiftResponse(accountNumber, memberDetails.getMembershipCode(), giftType,
						listGiftingHistoryResponse);

			if (filter.equalsIgnoreCase(GiftingConstants.FILTER_ADMIN))
				populateAllListGiftResponse(accountNumber, memberDetails.getMembershipCode(), giftType, filter,
						listGiftingHistoryResponse);
			
		} 
		catch (MarketplaceException me) {
			listGiftingHistoryResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getId(),
					GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		}
		
		return listGiftingHistoryResponse;
		
	}
	
	/**
	 * This method is used populate senders' gifting history response.
	 * @param accountNumber
	 * @param membershipCode
	 * @param giftType
	 * @param listGiftingHistoryResponse
	 * @return
	 */
	private ListGiftingHistoryResponse populateSentListGiftResponse(String accountNumber, String membershipCode,
			String giftType, ListGiftingHistoryResponse listGiftingHistoryResponse) {
	
		Sort sort = Sort.by(Sort.Direction.DESC, GiftingConstants.ORDER_DESC_TRANSACTION_DATE);
		
		List<GiftingHistory> giftingHistory = giftingHistoryRepository.findBySenderAccountNumberAndMembershipCodeAndGiftType(accountNumber,
						membershipCode, giftType, sort);

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_ENTITY,
				this.getClass(), GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_POPULATE_SENT, giftingHistory);
		
		if (giftingHistory.isEmpty()) {
			listGiftingHistoryResponse.addErrorAPIResponse(GiftingCodes.LIST_NO_SENT_GIFTING_HISTORY_AVAILABLE.getIntId(),
					GiftingCodes.LIST_NO_SENT_GIFTING_HISTORY_AVAILABLE.getMsg());
			listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getId(),
					GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getMsg());
			return listGiftingHistoryResponse;
		}

		ListGiftingHistoryResult retrievedHistory = createResponseObject(giftingHistory, GiftingConstants.FILTER_SENT);
			
		listGiftingHistoryResponse.setGiftingHistory(retrievedHistory);
		listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_SUCCESS.getId(),
				GiftingCodes.GIFTING_HISTORY_LIST_SUCCESS.getMsg());
		
		return listGiftingHistoryResponse;
		
	}
	
	/**
	 * This method is used populate receivers' gifting history response.
	 * @param accountNumber
	 * @param membershipCode
	 * @param giftType
	 * @param listGiftingHistoryResponse
	 * @return
	 */
	private ListGiftingHistoryResponse populateReceivedListGiftResponse(String accountNumber, String membershipCode,
			String giftType, ListGiftingHistoryResponse listGiftingHistoryResponse) {

		Sort sort = Sort.by(Sort.Direction.DESC, GiftingConstants.ORDER_DESC_TRANSACTION_DATE);
		
		List<GiftingHistory> giftingHistory = giftingHistoryRepository
				.findByReceiverAccountNumberAndMembershipCodeAndGiftType(accountNumber,
						membershipCode, giftType, new Date(), sort);

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_ENTITY,
				this.getClass(), GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_POPULATE_RECEIVED, giftingHistory);
		
		if (giftingHistory.isEmpty()) {
			listGiftingHistoryResponse.addErrorAPIResponse(GiftingCodes.LIST_NO_RECEIVED_GIFTING_HISTORY_AVAILABLE.getIntId(),
					GiftingCodes.LIST_NO_RECEIVED_GIFTING_HISTORY_AVAILABLE.getMsg());
			listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getId(),
					GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getMsg());
			return listGiftingHistoryResponse;
		}

		ListGiftingHistoryResult retrievedHistory = createResponseObject(giftingHistory, GiftingConstants.FILTER_RECEIVED);
		
		listGiftingHistoryResponse.setGiftingHistory(retrievedHistory);
		listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_SUCCESS.getId(),
				GiftingCodes.GIFTING_HISTORY_LIST_SUCCESS.getMsg());
		
		return listGiftingHistoryResponse;
		
	}
	
	/**
	 * This method is used populate senders' & receivers' gifting history response.
	 * @param accountNumber
	 * @param membershipCode
	 * @param giftType
	 * @param listGiftingHistoryResponse
	 * @return
	 */
	private ListGiftingHistoryResponse populateAllListGiftResponse(String accountNumber, String membershipCode,
			String giftType, String filter, ListGiftingHistoryResponse listGiftingHistoryResponse) {

		ListGiftingHistoryResult retrievedHistory = new ListGiftingHistoryResult();
		
		Sort sort = Sort.by(Sort.Direction.DESC, GiftingConstants.ORDER_DESC_TRANSACTION_DATE);
		
		List<GiftingHistory> giftingHistory = giftingHistoryRepository
				.findByAllAccountNumberAndMembershipCodeAndGiftType(accountNumber,
						membershipCode, giftType, sort);

		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_ENTITY,
				this.getClass(), GiftingConfigurationConstants.GIFTING_HISTORY_DOMAIN_POPULATE_ALL, giftingHistory);
		
		if (giftingHistory.isEmpty()) {
			listGiftingHistoryResponse.addErrorAPIResponse(GiftingCodes.LIST_NO_ALL_GIFTING_HISTORY_AVAILABLE.getIntId(),
					GiftingCodes.LIST_NO_ALL_GIFTING_HISTORY_AVAILABLE.getMsg());
			listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getId(),
					GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getMsg());
			return listGiftingHistoryResponse;
		}

		if(!checkIfAllScheduled(giftingHistory, filter, listGiftingHistoryResponse)) return listGiftingHistoryResponse; 
		
		List<TransactionResponse> transactions = new ArrayList<>();

		retrievedHistory.setProgramCode(giftingHistory.get(0).getProgramCode());
		retrievedHistory.setGiftType(giftingHistory.get(0).getGiftType());
		retrievedHistory.setType(GiftingConstants.FILTER_ALL);
		retrievedHistory.setAccountNumber(accountNumber);
		retrievedHistory.setMembershipCode(membershipCode);

		for (GiftingHistory history : giftingHistory) {

			TransactionResponse transaction = new TransactionResponse();
			
			transaction.setId(history.getId());
			
			if ((accountNumber.equals(history.getSenderInfo().getAccountNumber())
					&& membershipCode.equals(history.getSenderInfo().getMembershipCode()))) {

				retrievedHistory.setFirstName(giftingHistory.get(0).getSenderInfo().getFirstName());
				retrievedHistory.setLastName(giftingHistory.get(0).getSenderInfo().getLastName());
				retrievedHistory.setEmail(giftingHistory.get(0).getSenderInfo().getEmail());

				MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
				memberInfoResponse.setAccountNumber(history.getReceiverInfo().getAccountNumber());
				memberInfoResponse.setMembershipCode(history.getReceiverInfo().getMembershipCode());
				memberInfoResponse.setFirstName(history.getReceiverInfo().getFirstName());
				memberInfoResponse.setLastName(history.getReceiverInfo().getLastName());
				memberInfoResponse.setEmail(history.getReceiverInfo().getEmail());
				transaction.setRecipientInfo(memberInfoResponse);
				
				transaction.setTransactionType(GiftingConstants.FILTER_SENT);
				
			}
			
			if ((accountNumber.equals(history.getReceiverInfo().getAccountNumber())
					&& membershipCode.equals(history.getReceiverInfo().getMembershipCode()))) {

				if (history.getScheduledDate().after(new Date())
						&& !filter.equalsIgnoreCase(GiftingConstants.FILTER_ADMIN))
					continue;
	
				retrievedHistory.setFirstName(giftingHistory.get(0).getReceiverInfo().getFirstName());
				retrievedHistory.setLastName(giftingHistory.get(0).getReceiverInfo().getLastName());
				retrievedHistory.setEmail(giftingHistory.get(0).getReceiverInfo().getEmail());

				MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
				memberInfoResponse.setAccountNumber(history.getSenderInfo().getAccountNumber());
				memberInfoResponse.setMembershipCode(history.getSenderInfo().getMembershipCode());
				memberInfoResponse.setFirstName(history.getSenderInfo().getFirstName());
				memberInfoResponse.setLastName(history.getSenderInfo().getLastName());
				memberInfoResponse.setEmail(history.getSenderInfo().getEmail());
				transaction.setRecipientInfo(memberInfoResponse);
			
				transaction.setTransactionType(GiftingConstants.FILTER_RECEIVED);
				
			}

			transaction.setVoucherCode(history.getVoucherCode());
			
			populatePointsGoldAttributes(history, transaction);
			
			transaction.setImageId(history.getImageId());
			transaction.setImageUrl(history.getImageUrl());
			transaction.setMessage(history.getMessage());
			transaction.setScheduledDate(history.getScheduledDate());
			transaction.setReceiverConsumption(history.getReceiverConsumption());

			PurchaseDetailsResponse purchaseDetailsResponse = new PurchaseDetailsResponse();
			if (null != history.getPurchaseDetails()) {

				purchaseDetailsResponse.setCardNumber(history.getPurchaseDetails().getCardNumber());
				purchaseDetailsResponse.setCardType(history.getPurchaseDetails().getCardType());
				purchaseDetailsResponse.setCardSubType(history.getPurchaseDetails().getCardSubType());
				purchaseDetailsResponse.setCardToken(history.getPurchaseDetails().getCardToken());
				purchaseDetailsResponse.setCardExpiryDate(history.getPurchaseDetails().getCardExpiryDate());
				purchaseDetailsResponse.setAuthorizationCode(history.getPurchaseDetails().getAuthorizationCode());
				purchaseDetailsResponse.setSpentAmount(history.getPurchaseDetails().getSpentAmount());
				purchaseDetailsResponse.setEpgTransactionId(history.getPurchaseDetails().getEpgTransactionId());
				purchaseDetailsResponse.setUiLanguage(history.getPurchaseDetails().getUiLanguage());
				
			}
			
			purchaseDetailsResponse.setPaymentTransactionNo(history.getTransactionNo());
			purchaseDetailsResponse.setPaymentTransactionDate(history.getTransactionDate());
			purchaseDetailsResponse.setExtRefNo(history.getExtRefNo());
			transaction.setPurchaseDetails(purchaseDetailsResponse);
			
			transactions.add(transaction);

		}
			
		retrievedHistory.setTransactions(transactions);
		listGiftingHistoryResponse.setGiftingHistory(retrievedHistory);
		listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_SUCCESS.getId(),
				GiftingCodes.GIFTING_HISTORY_LIST_SUCCESS.getMsg());
		
		return listGiftingHistoryResponse;
		
	}
	
	/**
	 * This method checks if all retrieved gifting history are scheduled for a future date.
	 * @param giftingHistory
	 * @param listGiftingHistoryResponse
	 * @return
	 */
	private boolean checkIfAllScheduled(List<GiftingHistory> giftingHistory, String filter, ListGiftingHistoryResponse listGiftingHistoryResponse) {
		
		if(filter.equalsIgnoreCase(GiftingConstants.FILTER_ADMIN)) return true;
		
		int actual = 0;
		int scheduled = 0;
		for (GiftingHistory history : giftingHistory) {
			if (history.getScheduledDate().after(new Date())) {
				scheduled++;
			}
			actual++;
		}
		
		if(actual == scheduled) {
			listGiftingHistoryResponse.addErrorAPIResponse(GiftingCodes.LIST_NO_ALL_GIFTING_HISTORY_AVAILABLE.getIntId(),
					GiftingCodes.LIST_NO_ALL_GIFTING_HISTORY_AVAILABLE.getMsg());
			listGiftingHistoryResponse.setResult(GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getId(),
					GiftingCodes.GIFTING_HISTORY_LIST_FAILURE.getMsg());
			return false;
		}
		
		return true;
	}
	
	/**
	 * This method is used to populate list gifting history response.
	 * @param giftingHistory
	 * @param filter
	 * @return
	 */
	private ListGiftingHistoryResult createResponseObject(List<GiftingHistory> giftingHistory, String filter) {
		
		List<TransactionResponse> transactions = new ArrayList<>();

		ListGiftingHistoryResult retrievedHistory = new ListGiftingHistoryResult();
		retrievedHistory.setProgramCode(giftingHistory.get(0).getProgramCode());
		retrievedHistory.setGiftType(giftingHistory.get(0).getGiftType());
		
		if(filter.equalsIgnoreCase(GiftingConstants.FILTER_SENT)) {
			retrievedHistory.setType(GiftingConstants.FILTER_SENT);
			retrievedHistory.setAccountNumber(giftingHistory.get(0).getSenderInfo().getAccountNumber());
			retrievedHistory.setMembershipCode(giftingHistory.get(0).getSenderInfo().getMembershipCode());
			retrievedHistory.setFirstName(giftingHistory.get(0).getSenderInfo().getFirstName());
			retrievedHistory.setLastName(giftingHistory.get(0).getSenderInfo().getLastName());
			retrievedHistory.setEmail(giftingHistory.get(0).getSenderInfo().getEmail());
		}
		
		if(filter.equalsIgnoreCase(GiftingConstants.FILTER_RECEIVED)) {
			retrievedHistory.setType(GiftingConstants.FILTER_RECEIVED);
			retrievedHistory.setAccountNumber(giftingHistory.get(0).getReceiverInfo().getAccountNumber());
			retrievedHistory.setMembershipCode(giftingHistory.get(0).getReceiverInfo().getMembershipCode());
			retrievedHistory.setFirstName(giftingHistory.get(0).getReceiverInfo().getFirstName());
			retrievedHistory.setLastName(giftingHistory.get(0).getReceiverInfo().getLastName());
			retrievedHistory.setEmail(giftingHistory.get(0).getReceiverInfo().getEmail());
		}
		
		for (GiftingHistory history : giftingHistory) {

			TransactionResponse transaction = new TransactionResponse();
			
			MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
			
			if(filter.equalsIgnoreCase(GiftingConstants.FILTER_SENT)) {
				memberInfoResponse.setAccountNumber(history.getReceiverInfo().getAccountNumber());
				memberInfoResponse.setMembershipCode(history.getReceiverInfo().getMembershipCode());
				memberInfoResponse.setFirstName(history.getReceiverInfo().getFirstName());
				memberInfoResponse.setLastName(history.getReceiverInfo().getLastName());
				memberInfoResponse.setEmail(history.getReceiverInfo().getEmail());
				transaction.setRecipientInfo(memberInfoResponse);	
			}
			
			if(filter.equalsIgnoreCase(GiftingConstants.FILTER_RECEIVED)) {
				memberInfoResponse.setAccountNumber(history.getSenderInfo().getAccountNumber());
				memberInfoResponse.setMembershipCode(history.getSenderInfo().getMembershipCode());
				memberInfoResponse.setFirstName(history.getSenderInfo().getFirstName());
				memberInfoResponse.setLastName(history.getSenderInfo().getLastName());
				memberInfoResponse.setEmail(history.getSenderInfo().getEmail());
				transaction.setRecipientInfo(memberInfoResponse);
			}
			
			transaction.setVoucherCode(history.getVoucherCode());
			
			populatePointsGoldAttributes(history, transaction);

			transaction.setImageId(history.getImageId());
			transaction.setImageUrl(history.getImageUrl());
			transaction.setMessage(history.getMessage());
			transaction.setScheduledDate(history.getScheduledDate());
			transaction.setReceiverConsumption(history.getReceiverConsumption());

			PurchaseDetailsResponse purchaseDetailsResponse = new PurchaseDetailsResponse();
			if (null != history.getPurchaseDetails()) {
				
				purchaseDetailsResponse.setCardNumber(history.getPurchaseDetails().getCardNumber());
				purchaseDetailsResponse.setCardType(history.getPurchaseDetails().getCardType());
				purchaseDetailsResponse.setCardSubType(history.getPurchaseDetails().getCardSubType());
				purchaseDetailsResponse.setCardToken(history.getPurchaseDetails().getCardToken());
				purchaseDetailsResponse.setCardExpiryDate(history.getPurchaseDetails().getCardExpiryDate());
				purchaseDetailsResponse.setAuthorizationCode(history.getPurchaseDetails().getAuthorizationCode());
				purchaseDetailsResponse.setSpentAmount(history.getPurchaseDetails().getSpentAmount());
				purchaseDetailsResponse.setEpgTransactionId(history.getPurchaseDetails().getEpgTransactionId());
				purchaseDetailsResponse.setUiLanguage(history.getPurchaseDetails().getUiLanguage());
			
			}
			
			purchaseDetailsResponse.setPaymentTransactionNo(history.getTransactionNo());
			purchaseDetailsResponse.setPaymentTransactionDate(history.getTransactionDate());
			purchaseDetailsResponse.setExtRefNo(history.getExtRefNo());
			transaction.setPurchaseDetails(purchaseDetailsResponse);
			
			transactions.add(transaction);

		}
			
		retrievedHistory.setTransactions(transactions);
		
		return retrievedHistory;
		
	}
	
	/**
	 * This method is used to populate points and gold specific attributes in response.
	 * @param history
	 * @param transaction
	 */
	private void populatePointsGoldAttributes(GiftingHistory history, TransactionResponse transaction) {
		
		PointsGiftTransactionResponse pointsGiftTransactionResponse = new PointsGiftTransactionResponse();
		
		if(null != history.getPointsGifted()) {
			pointsGiftTransactionResponse.setPointsGifted(history.getPointsGifted().getPointsGifted());
			pointsGiftTransactionResponse.setSenderTransactionRefId(history.getPointsGifted().getSenderTransactionRefId());
			pointsGiftTransactionResponse.setReceiverTransactionRefId(history.getPointsGifted().getReceiverTransactionRefId());
			transaction.setPointsGift(pointsGiftTransactionResponse);
		}

		List<GoldCertificateTransactionResponse> goldGifts = new ArrayList<>();
		if (null != history.getGoldGifted() && !history.getGoldGifted().isEmpty()) {
			for (GoldGiftedDetails goldGift : history.getGoldGifted()) {
				GoldCertificateTransactionResponse goldTransaction = new GoldCertificateTransactionResponse();
				goldTransaction.setCertificateId(goldGift.getCertificateId());
				goldTransaction.setGoldGifted(goldGift.getGoldGifted());
				goldGifts.add(goldTransaction);
			}
			transaction.setGoldGift(goldGifts);
		}
		
	}
	
}
