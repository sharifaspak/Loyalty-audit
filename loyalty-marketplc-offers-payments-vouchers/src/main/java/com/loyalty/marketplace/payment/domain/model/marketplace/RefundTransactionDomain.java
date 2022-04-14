package com.loyalty.marketplace.payment.domain.model.marketplace;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.payment.outbound.database.entity.RefundTransaction;
import com.loyalty.marketplace.payment.outbound.database.repository.RefundTransactionRepository;
import com.loyalty.marketplace.service.dto.PaymentReversalResponseDto;
import com.loyalty.marketplace.service.dto.PaymentReversalResultDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class RefundTransactionDomain {
	private static final Logger LOG = LoggerFactory.getLogger(RefundTransactionDomain.class);
	

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	RefundTransactionRepository repository;
	
	@Value("#{'${smiles.payment.failed.epgReversal.codes}'.split(',')}")
	private List<String> failedReversalCodes;
	
	@Value("${smiles.payment.success.epgReversal.code}")
	private String successEPGReversalCode;
	
	@Value("${smiles.payment.success.epgRefund.code}")
	private String successEPGRefundCode;
	
	@Autowired
	MongoOperations mongoOperations;
	
	private static final String SUCCESS_RESPONSE_CODE = "0";
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";

	private String id;
	private String transactionId;
	private String accountNumber;
	private String amount;
	private String epgTransactionId;
	private String refundStatus;
	private String responseMsg;
	private String responseCode;
	private String epgRerversalDesc;
	private String epgRerversalCode;
	private String epgRefundCode;
	private String epgRefundMsg;
	private String errorMessage;
	private Boolean isNotificationTrigger;
	private String cardNumber;
	private String cardToken;
	private String cardSubType;
	private String cardType;
	private String authorizationCode;
	private String orderId;
	private String msisdn;
	private String language;
	private String selectedPaymentItem;
	private String selectedPaymentOption;
	private String dirhamValue;
	private String pointsValue;
	//private Boolean isFreeDelivery;
	private String offerId;
	private String voucherDenomination;
	private String accountType;
	private Integer quantity;
	private String programCode;
	private String externalTransactionId;
	private String partialTransactionId;
	private Boolean reprocessedFlag;
	private String transactionDescription;
	private Date createdDate;
	private String createdUser;
	private String updatedUser;
	private Date dateLastUpdated;

	private RefundTransactionDomain(RefundTransactionDomainBuilder builder) {
		super();
		this.id = builder.id;
		this.transactionId = builder.transactionId;
		this.accountNumber = builder.accountNumber;
		this.amount = builder.amount;
		this.epgTransactionId = builder.epgTransactionId;
		this.refundStatus = builder.refundStatus;
		this.responseMsg = builder.responseMsg;
		this.responseCode = builder.responseCode;
		this.epgRerversalDesc = builder.epgRerversalDesc;
		this.epgRerversalCode = builder.epgRerversalCode;
		this.epgRefundCode = builder.epgRefundCode;
		this.epgRefundMsg = builder.epgRefundMsg;
		this.errorMessage = builder.errorMessage;
		this.isNotificationTrigger = builder.isNotificationTrigger;
		this.cardNumber = builder.cardNumber;
		this.cardToken = builder.cardToken;
		this.cardSubType = builder.cardSubType;
		this.cardType = builder.cardType;
		this.authorizationCode = builder.authorizationCode;
		this.orderId = builder.orderId;
		this.msisdn = builder.msisdn;
		this.language = builder.language;
		this.selectedPaymentItem = builder.selectedPaymentItem;
		this.selectedPaymentOption = builder.selectedPaymentOption;
		this.dirhamValue = builder.dirhamValue;
		this.pointsValue = builder.pointsValue;
		//this.isFreeDelivery = builder.isFreeDelivery;
		this.offerId = builder.offerId;
		this.voucherDenomination = builder.voucherDenomination;
		this.accountType = builder.accountType;
		this.quantity = builder.quantity;
		this.programCode = builder.programCode;
		this.externalTransactionId = builder.externalTransactionId;
		this.partialTransactionId = builder.partialTransactionId;
		this.reprocessedFlag = builder.reprocessedFlag;
		this.transactionDescription = builder.transactionDescription;
		this.createdDate = builder.createdDate;
		this.createdUser = builder.createdUser;
		this.updatedUser = builder.updatedUser;
		this.dateLastUpdated = builder.dateLastUpdated;
	}

	@Data
	public static class RefundTransactionDomainBuilder {

		private String id;
		private String transactionId;
		private String accountNumber;
		private String amount;
		private String epgTransactionId;
		private String refundStatus;
		private String responseMsg;
		private String responseCode;
		private String epgRerversalDesc;
		private String epgRerversalCode;
		private String epgRefundCode;
		private String epgRefundMsg;
		private String errorMessage;
		private Boolean isNotificationTrigger;
		private String cardNumber;
		private String cardToken;
		private String cardSubType;
		private String cardType;
		private String authorizationCode;
		private String orderId;
		private String msisdn;
		private String language;
		private String selectedPaymentItem;
		private String selectedPaymentOption;
		private String dirhamValue;
		private String pointsValue;
		//private Boolean isFreeDelivery;
		private String offerId;
		private String voucherDenomination;
		private String accountType;
		private Integer quantity;
		private String programCode;
		private String externalTransactionId;
		private String partialTransactionId;
		private Boolean reprocessedFlag;
		private String transactionDescription;
		private Date createdDate;
		private String createdUser;
		private String updatedUser;
		private Date dateLastUpdated;

		public RefundTransactionDomainBuilder(String transactionId, String accountNumber, String amount, String epgTransactionId,
				String refundStatus, String responseMsg, String responseCode, String epgRerversalDesc, String epgRerversalCode, String epgRefundCode,
				String epgRefundMsg, String errorMessage) {
			super();
			this.transactionId = transactionId;
			this.accountNumber = accountNumber;
			this.amount = amount;
			this.epgTransactionId = epgTransactionId;
			this.refundStatus = refundStatus;
			this.responseMsg = responseMsg;
			this.responseCode = responseCode;
			this.epgRerversalDesc = epgRerversalDesc;
			this.epgRerversalCode = epgRerversalCode;
			this.epgRefundCode = epgRefundCode;
			this.epgRefundMsg = epgRefundMsg;
			this.errorMessage = errorMessage;
		}

		public RefundTransactionDomainBuilder transactionId(String transactionId) {
			this.transactionId = transactionId;
			return this;
		}
		
		public RefundTransactionDomainBuilder id(String id) {
			this.id = id;
			return this;
		}

		public RefundTransactionDomainBuilder orderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public RefundTransactionDomainBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public RefundTransactionDomainBuilder amount(String amount) {
			this.amount = amount;
			return this;
		}

		public RefundTransactionDomainBuilder refundStatus(String refundStatus) {
			this.refundStatus = refundStatus;
			return this;
		}

		public RefundTransactionDomainBuilder responseMsg(String responseMsg) {
			this.responseMsg = responseMsg;
			return this;
		}

		public RefundTransactionDomainBuilder responseCode(String responseCode) {
			this.responseCode = responseCode;
			return this;
		}

		public RefundTransactionDomainBuilder isNotificationTrigger(Boolean isNotificationTrigger) {
			this.isNotificationTrigger = isNotificationTrigger;
			return this;
		}

		public RefundTransactionDomainBuilder epgRerversalDesc(String epgRerversalDesc) {
			this.epgRerversalDesc = epgRerversalDesc;
			return this;
		}

		public RefundTransactionDomainBuilder epgRerversalCode(String epgRerversalCode) {
			this.epgRerversalCode = epgRerversalCode;
			return this;
		}
		public RefundTransactionDomainBuilder epgRefundCode(String epgRefundCode) {
			this.epgRefundCode = epgRefundCode;
			return this;
		}

		public RefundTransactionDomainBuilder epgRefundMsg(String epgRefundMsg) {
			this.epgRefundMsg = epgRefundMsg;
			return this;
		}
		
		public RefundTransactionDomainBuilder errorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
			return this;
		}
		
		public RefundTransactionDomainBuilder epgTransactionId(String epgTransactionId) {
			this.epgTransactionId = epgTransactionId;
			return this;
		}

		public RefundTransactionDomainBuilder msisdn(String msisdn) {
			this.msisdn = msisdn;
			return this;
		}

		public RefundTransactionDomainBuilder language(String language) {
			this.language = language;
			return this;
		}

		public RefundTransactionDomainBuilder selectedPaymentItem(String selectedPaymentItem) {
			this.selectedPaymentItem = selectedPaymentItem;
			return this;
		}

		public RefundTransactionDomainBuilder selectedPaymentOption(String selectedPaymentOption) {
			this.selectedPaymentOption = selectedPaymentOption;
			return this;
		}

		public RefundTransactionDomainBuilder dirhamValue(String dirhamValue) {
			this.dirhamValue = dirhamValue;
			return this;
		}
		
		public RefundTransactionDomainBuilder pointsValue(String pointsValue) {
			this.pointsValue = pointsValue;
			return this;
		}

//		public RefundTransactionDomainBuilder isFreeDelivery(Boolean isFreeDelivery) {
//			this.isFreeDelivery = isFreeDelivery;
//			return this;
//		}

		public RefundTransactionDomainBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}

		public RefundTransactionDomainBuilder accountType(String accountType) {
			this.accountType = accountType;
			return this;
		}

		public RefundTransactionDomainBuilder quantity(Integer quantity) {
			this.quantity = quantity;
			return this;
		}

		public RefundTransactionDomainBuilder cardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
			return this;
		}

		public RefundTransactionDomainBuilder cardToken(String cardToken) {
			this.cardToken = cardToken;
			return this;
		}

		public RefundTransactionDomainBuilder cardSubType(String cardSubType) {
			this.cardSubType = cardSubType;
			return this;
		}

		public RefundTransactionDomainBuilder cardType(String cardType) {
			this.cardType = cardType;
			return this;
		}

		public RefundTransactionDomainBuilder authorizationCode(String authorizationCode) {
			this.authorizationCode = authorizationCode;
			return this;
		}

		public RefundTransactionDomainBuilder voucherDenomination(String voucherDenomination) {
			this.voucherDenomination = voucherDenomination;
			return this;
		}

		public RefundTransactionDomainBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public RefundTransactionDomainBuilder externalTransactionId(String externalTransactionId) {
			this.externalTransactionId = externalTransactionId;
			return this;
		}

		public RefundTransactionDomainBuilder partialTransactionId(String partialTransactionId) {
			this.partialTransactionId = partialTransactionId;
			return this;
		}
		
		public RefundTransactionDomainBuilder transactionDescription(String transactionDescription) {
			this.transactionDescription = transactionDescription;
			return this;
		}
		
		public RefundTransactionDomainBuilder reprocessedFlag(Boolean reprocessedFlag) {
			this.reprocessedFlag = reprocessedFlag;
			return this;
		}
		
		public RefundTransactionDomainBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public RefundTransactionDomainBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public RefundTransactionDomainBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public RefundTransactionDomainBuilder dateLastUpdated(Date dateLastUpdated) {
			this.dateLastUpdated = dateLastUpdated;
			return this;
		}

		public RefundTransactionDomain build() {
			return new RefundTransactionDomain(this);
		}

	}
	
	/**
	 * 
	 * Save the new  RefundTransaction document using Domain class
	 * 
	 * @param refundTransactionDomain
	 * @return refundTransaction
	 */
	public RefundTransaction saveRefundTransaction(RefundTransactionDomain refundTransactionDomain) {
		RefundTransaction refundTransaction = modelMapper.map(refundTransactionDomain, RefundTransaction.class);
		LOG.info("saveRefundTransaction RefundTransactionDomain: {}", refundTransactionDomain);
		repository.save(refundTransaction);
		LOG.info("saved RefundTransaction row");		
		return refundTransaction;
	}
	
	public List<RefundTransaction> fetchRecentFailedTransactionsList(){
		LOG.info("fetching list of failed payment reversal for last 24 hours");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		Date period = c.getTime();
		LOG.info("Date to fetch refund transactions: {}", period);
		List<RefundTransaction> transactions = repository.findAllFailedTransactions(failedReversalCodes, successEPGRefundCode, successEPGReversalCode, false, period );
		LOG.info("List of failed payment reversal transactions: {}", transactions);
		LOG.info("count of failed records fetched {}", transactions.size());
		return transactions;
	}
	
	public void updateRetriedRefundTransaction(PaymentReversalResponseDto response, Headers header) {
		LOG.info("updating RefundTransaction collection for retried transactions.");
		if (!ObjectUtils.isEmpty(response) && !CollectionUtils.isEmpty(response.getResult())) {
			for (PaymentReversalResultDto result : response.getResult()) {
				Query refunTransactionQuery = new Query();
				
				refunTransactionQuery.addCriteria(
					Criteria.where("TransactionId").is(result.getTransactionId()));
				
				Update update = new Update();
				if (!(failedReversalCodes.contains(result.getEpgRerversalCode()))) {
					update.set("ReprocessedFlag", true);
				}
				update.set("RefundStatus", result.getResponseCode().equalsIgnoreCase(SUCCESS_RESPONSE_CODE) ? SUCCESS : FAILURE);
				update.set("ResponseCode", result.getResponseCode());
				update.set("ResponseMsg", result.getResponseMsg());
				update.set("Msisdn", result.getMsisdn());
				update.set("EPGReversalCode", result.getEpgRerversalCode());
				update.set("EPGReversalDesc", result.getEpgRerversalDesc());
				update.set("EPGRefundCode", result.getEpgRefundCode());
				update.set("EPGRefundMsg", result.getEpgRefundMsg());
				update.set("FailureReason", null != result.getErrorMessage() ? result.getErrorMessage() : "NA");
				update.set("ExternalTransactionId", header.getExternalTransactionId());
				update.set("UpdatedDate", new Date());
				update.set("UpdatedUser", header.getUserName());
				
				mongoOperations.updateMulti(refunTransactionQuery, update, RefundTransaction.class);
			}
		}
		LOG.info("completed updating refundTransaction collection for retried transaction.");
    }

}
