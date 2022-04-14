package com.loyalty.marketplace.payment.domain.model.marketplace;

import org.springframework.stereotype.Component;

@Component
public class SmilesPaymentDomain {
	/*
	 * 
	 * private static final Logger LOG =
	 * LoggerFactory.getLogger(SmilesPaymentDomain.class.getName());
	 * 
	 * @Autowired ModelMapper modelMapper = new ModelMapper();
	 * 
	 * @Autowired SmilesPaymentRepository paymentRepository;
	 * 
	 * private String orderId;
	 * 
	 * private String accountNumber;
	 * 
	 * private String channelId;
	 * 
	 * private String offerId;
	 * 
	 * private String language;
	 * 
	 * private Double pointsValue;
	 * 
	 * private Double dirhamValue;
	 * 
	 * private String paymentItem;
	 * 
	 * private String typeOfPayment;
	 * 
	 * private String epgTransactionId;
	 * 
	 * private Integer reserveTxnId;
	 * 
	 * private String ccNumber;
	 * 
	 * private String ccExpDate;
	 * 
	 * private String approvalCode;
	 * 
	 * private Integer voucherDenomination;
	 * 
	 * private String promoCode;
	 * 
	 * private String ccToken;
	 * 
	 * private String atgUsername;
	 * 
	 * private String ccBrand;
	 * 
	 * private String ccTxnId;
	 * 
	 * private String token;
	 * 
	 * private String subOfferId;
	 * 
	 * private String count;
	 * 
	 * private Date createdDate;
	 * 
	 * private String createdUser;
	 * 
	 * private String updatedUser;
	 * 
	 * private Date dateLastUpdated;
	 * 
	 * public String getOrderId() { return orderId; }
	 * 
	 * public String getAccountNumber() { return accountNumber; }
	 * 
	 * public String getChannelId() { return channelId; }
	 * 
	 * public String getOfferId() { return offerId; }
	 * 
	 * public String getLanguage() { return language; }
	 * 
	 * public Double getPointsValue() { return pointsValue; }
	 * 
	 * public Double getDirhamValue() { return dirhamValue; }
	 * 
	 * public String getPaymentItem() { return paymentItem; }
	 * 
	 * public String getTypeOfPayment() { return typeOfPayment; }
	 * 
	 * public String getEpgTransactionId() { return epgTransactionId; }
	 * 
	 * public Integer getReserveTxnId() { return reserveTxnId; }
	 * 
	 * public String getCcNumber() { return ccNumber; }
	 * 
	 * public String getCcExpDate() { return ccExpDate; }
	 * 
	 * public String getApprovalCode() { return approvalCode; }
	 * 
	 * public Integer getVoucherDenomination() { return voucherDenomination; }
	 * 
	 * public String getPromoCode() { return promoCode; }
	 * 
	 * public String getCcToken() { return ccToken; }
	 * 
	 * public String getAtgUsername() { return atgUsername; }
	 * 
	 * public String getCcBrand() { return ccBrand; }
	 * 
	 * public String getCcTxnId() { return ccTxnId; }
	 * 
	 * public String getToken() { return token; }
	 * 
	 * public String getSubOfferId() { return subOfferId; }
	 * 
	 * public String getCount() { return count; }
	 * 
	 * public Date getCreatedDate() { return createdDate; }
	 * 
	 * public String getCreatedUser() { return createdUser; }
	 * 
	 * public String getUpdatedUser() { return updatedUser; }
	 * 
	 * public Date getDateLastUpdated() { return dateLastUpdated; }
	 * 
	 * @Override public String toString() { return
	 * "SmilesPaymentDomain [modelMapper=" + modelMapper + ", paymentRepository=" +
	 * paymentRepository + ", orderId=" + orderId + ", accountNumber=" +
	 * accountNumber + ", channelId=" + channelId + ", offerId=" + offerId +
	 * ", language=" + language + ", pointsValue=" + pointsValue + ", dirhamValue="
	 * + dirhamValue + ", paymentItem=" + paymentItem + ", typeOfPayment=" +
	 * typeOfPayment + ", epgTransactionId=" + epgTransactionId + ", reserveTxnId="
	 * + reserveTxnId + ", ccNumber=" + ccNumber + ", ccExpDate=" + ccExpDate +
	 * ", approvalCode=" + approvalCode + ", voucherDenomination=" +
	 * voucherDenomination + ", promoCode=" + promoCode + ", ccToken=" + ccToken +
	 * ", atgUsername=" + atgUsername + ", ccBrand=" + ccBrand + ", ccTxnId=" +
	 * ccTxnId + ", token=" + token + ", subOfferId=" + subOfferId + ", count=" +
	 * count + ", createdDate=" + createdDate + ", createdUser=" + createdUser +
	 * ", updatedUser=" + updatedUser + ", dateLastUpdated=" + dateLastUpdated +
	 * "]"; }
	 * 
	 * public SmilesPaymentDomain() {
	 * 
	 * }
	 * 
	 * public SmilesPaymentDomain(SmilesPaymentBuilder paymentBuilder) { super();
	 * this.orderId = paymentBuilder.orderId; this.accountNumber =
	 * paymentBuilder.accountNumber; this.channelId = paymentBuilder.channelId;
	 * this.offerId = paymentBuilder.offerId; this.language =
	 * paymentBuilder.language; this.pointsValue = paymentBuilder.pointsValue;
	 * this.dirhamValue = paymentBuilder.dirhamValue; this.paymentItem =
	 * paymentBuilder.paymentItem; this.typeOfPayment =
	 * paymentBuilder.typeOfPayment; this.epgTransactionId =
	 * paymentBuilder.epgTransactionId; this.reserveTxnId =
	 * paymentBuilder.reserveTxnId; this.ccNumber = paymentBuilder.ccNumber;
	 * this.ccExpDate = paymentBuilder.ccExpDate; this.approvalCode =
	 * paymentBuilder.approvalCode; this.voucherDenomination =
	 * paymentBuilder.voucherDenomination; this.promoCode =
	 * paymentBuilder.promoCode; this.ccToken = paymentBuilder.ccToken;
	 * this.atgUsername = paymentBuilder.atgUsername; this.ccBrand =
	 * paymentBuilder.ccBrand; this.ccTxnId = paymentBuilder.ccTxnId; this.token =
	 * paymentBuilder.token; this.subOfferId = paymentBuilder.subOfferId; this.count
	 * = paymentBuilder.count; this.createdDate = paymentBuilder.createdDate;
	 * this.createdUser = paymentBuilder.createdUser; this.updatedUser =
	 * paymentBuilder.updatedUser; this.dateLastUpdated =
	 * paymentBuilder.dateLastUpdated; this.paymentRepository =
	 * paymentBuilder.paymentRepository; }
	 * 
	 * public static class SmilesPaymentBuilder {
	 * 
	 * private String orderId;
	 * 
	 * private String accountNumber;
	 * 
	 * private String channelId;
	 * 
	 * private String offerId;
	 * 
	 * private String language;
	 * 
	 * private Double pointsValue;
	 * 
	 * private Double dirhamValue;
	 * 
	 * private String paymentItem;
	 * 
	 * private String typeOfPayment;
	 * 
	 * private String epgTransactionId;
	 * 
	 * private Integer reserveTxnId;
	 * 
	 * private String ccNumber;
	 * 
	 * private String ccExpDate;
	 * 
	 * private String approvalCode;
	 * 
	 * private Integer voucherDenomination;
	 * 
	 * private String promoCode;
	 * 
	 * private String ccToken;
	 * 
	 * private String atgUsername;
	 * 
	 * private String ccBrand;
	 * 
	 * private String ccTxnId;
	 * 
	 * private String token;
	 * 
	 * private String subOfferId;
	 * 
	 * private String count;
	 * 
	 * private Date createdDate;
	 * 
	 * private String createdUser;
	 * 
	 * private String updatedUser;
	 * 
	 * private Date dateLastUpdated;
	 * 
	 * private SmilesPaymentRepository paymentRepository;
	 * 
	 * public SmilesPaymentBuilder(String orderId, String accountNumber, String
	 * channelId, String offerId, String language, Double pointsValue, Double
	 * dirhamValue, String paymentItem, String typeOfPayment, String
	 * epgTransactionId, Integer reserveTxnId, String ccNumber, String ccExpDate,
	 * String approvalCode, Integer voucherDenomination, String promoCode, String
	 * ccToken, String atgUsername, String ccBrand, String ccTxnId, String token,
	 * String subOfferId, String count, Date createdDate, String createdUser, String
	 * updatedUser, Date dateLastUpdated,SmilesPaymentRepository paymentRepository)
	 * { super(); this.orderId = orderId; this.accountNumber = accountNumber;
	 * this.channelId = channelId; this.offerId = offerId; this.language = language;
	 * this.pointsValue = pointsValue; this.dirhamValue = dirhamValue;
	 * this.paymentItem = paymentItem; this.typeOfPayment = typeOfPayment;
	 * this.epgTransactionId = epgTransactionId; this.reserveTxnId = reserveTxnId;
	 * this.ccNumber = ccNumber; this.ccExpDate = ccExpDate; this.approvalCode =
	 * approvalCode; this.voucherDenomination = voucherDenomination; this.promoCode
	 * = promoCode; this.ccToken = ccToken; this.atgUsername = atgUsername;
	 * this.ccBrand = ccBrand; this.ccTxnId = ccTxnId; this.token = token;
	 * this.subOfferId = subOfferId; this.count = count; this.createdDate =
	 * createdDate; this.createdUser = createdUser; this.updatedUser = updatedUser;
	 * this.dateLastUpdated = dateLastUpdated; this.paymentRepository =
	 * paymentRepository; }
	 * 
	 * public SmilesPaymentDomain build() { SmilesPaymentDomain payment = new
	 * SmilesPaymentDomain(this); return payment; } }
	 * 
	 * public void saveSmilesPayment(SmilesPaymentDomain paymentDomain) {
	 * LOG.info("persisted object: " + paymentDomain.toString()); SmilesPayment
	 * payment = modelMapper.map(paymentDomain, SmilesPayment.class);
	 * this.paymentRepository.insert(payment); LOG.info("Persisted object: " +
	 * payment.toString()); }
	 */}
