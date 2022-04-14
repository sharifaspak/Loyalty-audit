package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = OffersDBConstants.PURCHASE_HISTORY)
public class PurchaseHistory {
	
	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("PurchaseItem")
	private String purchaseItem;
	@Field("PartnerCode")
	private String partnerCode;
	@Field("MerchantCode")
	private String merchantCode;
	@Field("MerchantName")
	private String merchantName;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("OfferId")
	private String offerId;
	@Field("OfferType")
	private String offerType;
	@Field("SubOfferId")
	private String subOfferId;
	@Field("TransactionType")
	private String transactionType;
	@Field("PromoCode")
	private String promoCode;
	@Field("TransactionNo")
	private String transactionNo;
	@Field("ExtRefNo")
	private String extRefNo;
	@Field("EpgTransactionId")
	private String epgTransactionId;
	@Field("CouponQuantity")
	private Integer couponQuantity;
	@Field("VoucherCode")
	private List<String> voucherCode;
	@Field("SubscriptionId")
	private String subscriptionId;
	@Field("PaymentMethod")
	private String paymentMethod;
	@Field("SpentAmount")
	private Double spentAmount;
	@Field("SpentPoints")
	private Integer spentPoints;
	@Field("Cost")
	private Double cost;
	@Field("Vat")
	private Double vatAmount;
	@Field("PurchaseAmount")
	private Double purchaseAmount;
	@Field("PartnerActivity")
	private MarketplaceActivity partnerActivity;
	@Field("ReferralAccountNumber")
	private String referralAccountNumber;
	@Field("ReferralBonusCode")
	private String referralBonusCode;
	@Field("ReferralBonus")
	private Double referralBonus;
	@Field("Status")
	private String status;
	@Field("StatusReason")
	private String statusReason;
	@Field("AdditionalDetails")
	private AdditionalDetails additionalDetails;
	@Field("Language")
	private String language;
	@Field("ChannelId")
	private String channelId;
	@Field("PointsTransactionId")
	private String pointsTransactionId;
	@Field("CustomerType")
	private String customerType;
	@Field("RollBackFlag")
	private boolean rollBackFlag;
	@Field("SubscriptionSegment")
	private String subscriptionSegment;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("PreferredNumber")
	private String preferredNumber;

}