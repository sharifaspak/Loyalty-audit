package com.loyalty.marketplace.payment.domain.model.marketplace;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.payment.outbound.database.entity.EPGTransaction;
import com.loyalty.marketplace.payment.outbound.database.repository.EPGTransactionRepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class EPGTransactionDomain {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(EPGTransactionDomain.class);
	@Autowired
	EPGTransactionRepository epgTransactionRepository;
	

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	AuditService auditService;

	
	private String id;	
	private String orderId;
	private String accountNumber;
	private String amount;
	private String purchaseItem;
	private String subscriptionId;
	private String subscriptionCatalogId;
	private String epgStatus;
	private String masterEPGTransactionId;
	private String responseCode;
	private String responseClass;
	private String responseDescription;
	private String responseClassDescription;
	private String language;
	private String epgTransactionId;
	private String approvalCode;
	private String account;
	private String balance;
	private String fees;
	private String cardNumber;
	private String payer;
	private String cardToken;
	private String cardBrand;
	private String cardType;
	private String uniqueId;
	private String paymentType;
	private String programCode;
	private String externalTransactionId;
	private Date createdDate;
	private String createdUser;
	private String updatedUser;
	private Date dateLastUpdated;
	
	
	private EPGTransactionDomain(EPGTransactionDomainBuilder builder) {
		super();
		this.id = builder.id;	
		this.orderId = builder.orderId;
		this.accountNumber = builder.accountNumber;
		this.amount = builder.amount;
		this.purchaseItem = builder.purchaseItem;
		this.subscriptionId = builder.subscriptionId;
		this.subscriptionCatalogId = builder.subscriptionCatalogId;
		this.epgStatus = builder.epgStatus;
		this.masterEPGTransactionId = builder.masterEPGTransactionId;
		this.responseCode = builder.responseCode;
		this.responseClass = builder.responseClass;
		this.responseDescription = builder.responseDescription;
		this.responseClassDescription = builder.responseClassDescription;
		this.language = builder.language;
		this.epgTransactionId = builder.epgTransactionId;
		this.approvalCode = builder.approvalCode;
		this.account = builder.account;
		this.balance = builder.balance;
		this.fees = builder.fees;
		this.cardNumber = builder.cardNumber;
		this.payer = builder.payer;
		this.cardToken = builder.cardToken;
		this.cardBrand = builder.cardBrand;
		this.cardType = builder.cardType;
		this.uniqueId = builder.uniqueId;
		this.paymentType = builder.paymentType;
		this.programCode = builder.programCode;
		this.externalTransactionId = builder.externalTransactionId;
		this.createdDate = builder.createdDate;
		this.createdUser = builder.createdUser;
		this.updatedUser = builder.updatedUser;
		this.dateLastUpdated = builder.dateLastUpdated;
	
	}
	@NoArgsConstructor
	public static class EPGTransactionDomainBuilder {
		
		private String id;	
		private String orderId;
		private String accountNumber;
		private String amount;
		private String purchaseItem;
		private String subscriptionId;
		private String subscriptionCatalogId;
		private String epgStatus;
		private String masterEPGTransactionId;
		private String responseCode;
		private String responseClass;
		private String responseDescription;
		private String responseClassDescription;
		private String language;
		private String epgTransactionId;
		private String approvalCode;
		private String account;
		private String balance;
		private String fees;
		private String cardNumber;
		private String payer;
		private String cardToken;
		private String cardBrand;
		private String cardType;
		private String uniqueId;
		private String paymentType;
		private String programCode;
		private String externalTransactionId;
		private Date createdDate;
		private String createdUser;
		private String updatedUser;
		private Date dateLastUpdated;
		
		public EPGTransactionDomainBuilder(String responseCode, String responseClass, String responseDescription, String responseClassDescription,
				String balance, String amount, String fees, String uniqueId) {
			super();
			this.responseCode = responseCode;
			this.responseClass = responseClass;
			this.responseDescription = responseDescription;
			this.responseClassDescription = responseClassDescription;
			this.balance = balance;
			this.amount = amount;
			this.fees = fees;
			this.uniqueId = uniqueId;
		}
		
		public EPGTransactionDomainBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public EPGTransactionDomainBuilder orderId(String orderId) {
			this.orderId = orderId;
			return this;
		}
		
		public EPGTransactionDomainBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}
		
		public EPGTransactionDomainBuilder amount(String amount) {
			this.amount = amount;
			return this;
		}
		
		public EPGTransactionDomainBuilder purchaseItem(String purchaseItem) {
			this.purchaseItem = purchaseItem;
			return this;
		}
		
		public EPGTransactionDomainBuilder subscriptionId(String subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		public EPGTransactionDomainBuilder subscriptionCatalogId(String subscriptionCatalogId) {
			this.subscriptionCatalogId = subscriptionCatalogId;
			return this;
		}
		
		public EPGTransactionDomainBuilder epgStatus(String epgStatus) {
			this.epgStatus = epgStatus;
			return this;
		}
		
		public EPGTransactionDomainBuilder masterEPGTransactionId(String masterEPGTransactionId) {
			this.masterEPGTransactionId = masterEPGTransactionId;
			return this;
		}
		
		public EPGTransactionDomainBuilder responseCode(String responseCode) {
			this.responseCode = responseCode;
			return this;
		}
		
		public EPGTransactionDomainBuilder responseClass(String responseClass) {
			this.responseClass = responseClass;
			return this;
		}
		
		public EPGTransactionDomainBuilder responseDescription(String responseDescription) {
			this.responseDescription = responseDescription;
			return this;
		}
		
		public EPGTransactionDomainBuilder responseClassDescription(String responseClassDescription) {
			this.responseClassDescription = responseClassDescription;
			return this;
		}
		
		public EPGTransactionDomainBuilder language(String language) {
			this.language = language;
			return this;
		}
		
		public EPGTransactionDomainBuilder epgTransactionId(String epgTransactionId) {
			this.epgTransactionId = epgTransactionId;
			return this;
		}
		
		public EPGTransactionDomainBuilder approvalCode(String approvalCode) {
			this.approvalCode = approvalCode;
			return this;
		}
		
		public EPGTransactionDomainBuilder account(String account) {
			this.account = account;
			return this;
		}
		
		public EPGTransactionDomainBuilder balance(String balance) {
			this.balance = balance;
			return this;
		}
		
		public EPGTransactionDomainBuilder fees(String fees) {
			this.fees = fees;
			return this;
		}
		
		public EPGTransactionDomainBuilder cardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
			return this;
		}
		
		public EPGTransactionDomainBuilder payer(String payer) {
			this.payer = payer;
			return this;
		}
		public EPGTransactionDomainBuilder cardToken(String cardToken) {
			this.cardToken = cardToken;
			return this;
		}
		
		public EPGTransactionDomainBuilder cardBrand(String cardBrand) {
			this.cardBrand = cardBrand;
			return this;
		}
		
		public EPGTransactionDomainBuilder cardType(String cardType) {
			this.cardType = cardType;
			return this;
		}
		
		public EPGTransactionDomainBuilder uniqueId(String uniqueId) {
			this.uniqueId = uniqueId;
			return this;
		}
		
		public EPGTransactionDomainBuilder paymentType(String paymentType) {
			this.paymentType = paymentType;
			return this;
		}
		
		public EPGTransactionDomainBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public EPGTransactionDomainBuilder externalTransactionId(String externalTransactionId) {
			this.externalTransactionId = externalTransactionId;
			return this;
		}
		
		public EPGTransactionDomainBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		
		public EPGTransactionDomainBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public EPGTransactionDomainBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public EPGTransactionDomainBuilder dateLastUpdated(Date dateLastUpdated) {
			this.dateLastUpdated = dateLastUpdated;
			return this;
		}
		
		public EPGTransactionDomain build() {
			return new EPGTransactionDomain(this);
		}
	}
	
	
	/**
	 * 
	 * Save the new  EPGTransaction document using Domain class
	 * 
	 * @param epgTransactionDomain
	 * @param externalTransactionId
	 * @param userName
	 * @return
	 */
	public EPGTransaction saveEPGTransaction(EPGTransactionDomain epgTransactionDomain, String externalTransactionId, String userName) {
		EPGTransaction epgTransaction = modelMapper.map(epgTransactionDomain,
				EPGTransaction.class);
		LOG.info("saveEPGTransaction EPGTransactionDomain: {}", epgTransactionDomain);
		epgTransactionRepository.save(epgTransaction);
		
//		auditService.insertDataAudit("EPGTransaction", epgTransaction,
//				"/subscriptionRenewal", externalTransactionId, userName);
		LOG.info("saved EPGTransaction row");		
		return epgTransaction;
	}

}
