package com.loyalty.marketplace.offers.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.offers.outbound.database.entity.AdditionalDetails;
import com.loyalty.marketplace.offers.outbound.database.entity.MarketplaceActivity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TransactionListDto {
	
	private String id;
	private String programCode;
	private String purchaseItem;
	private String programActivity;
	private String partnerCode;
	private String merchantCode;
	private String merchantName;
	private String membershipCode;
	private String accountNumber;
	private String offerId;
	private String offerType;
	private String subOfferId;
	private String transactionType;
	private String promoCode;
	private String transactionNo;
	private String extRefNo;
	private String epgTransactionId;
	private Integer couponQuantity;
	private boolean rollBackFlag;
	private List<String> voucherCode;
	private String subscriptionId;
	private String paymentMethodId;
	private String paymentMethod;
	private Double spentAmount;
	private Integer spentPoints;
	private Double cost;
	private Double vatAmount;
	private Double purchaseAmount;
	private MarketplaceActivity partnerActivity;
	private AdditionalDetails additionalDetails;
	private String pointsTransactionId;
	private String status;
	private String statusReason;
	private String language;
	private String channelId;
	private Object voucherDetails;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;	
	private String preferredNumber;
	private String subscriptionSegment;
	
}
