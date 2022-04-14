package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VoucherListResult {

	private String id;
	
	private String voucherCode;
	
	private String offer;
	
	private String subOffer;
	
	private String denomination;
	
	private String merchantCode;
	
	private String partnerCode;
	
	private String merchantName;
	
	private String membershipCode;
	
	private String accountNumber;
	
	private String voucherNameEn;
	
	private String voucherNameAr;
	
	private String voucherDescriptionEn;
	
	private String voucherDescriptionAr;
	
	private String voucherType;
	
	private String status;
	
	private long pointsValue;
	
	private Double cost;
	
	private String partnerReferNumber;
	
	private String uuid;
	
	private Date downloadedDate;
	
	private Date expiryDate;
	
	private boolean isBlackListed;
	
	private String blackListedUser;
	
	private Date blacklistedDate;
	
	private String bulkId;
	
	private Date startDate;
	
	private Date endDate;
	
	private Double voucherAmount;
	
	private boolean isTransfer;
	
	private String oldAccountNumber;
	
	private String agentName;
	
	private String merchantInvoiceId;
	
	private boolean voucherBurnt;
	
	private String voucherBurntId;
	
	private Date voucherBurntDate;
	
	private String voucherBurntUser;
	
	private String burnNotes;
	
	private String storeCode;
	
	private String barcodeType;
	
	private Double estSavings;
	
	private String extRefNo;
	
	private String merchantNameAr;
	
	private Integer sharingBonus;
	
	private String sharing;
	
	private String termsAndConditionsEn;
	
	private String termsAndConditionsAr;
	
	private List<String> storeAddressEn;
	
	private List<String> storeAddressAr;
	
	private String offerTypeEn;
	
	private String offerTypeAr;
	
	private String discountPerc;
	
	private String categoryId;
	
	private Double voucherBalance;
	
	private String voucherRedeemType;
	
	private String partnerRedeemURL ;
	
	private String instructionsToRedeemTitleEn;
	
	private String instructionsToRedeemTitleAr;
	
	private String instructionsToRedeemEn;
	
	private String instructionsToRedeemAr;
	
	private VoucherGiftDetailsResult giftDetails;
	
	private String channel;
	
	private String createdUser;
	
	private Date createdDate;
	
	private String updatedUser;
	
	private Date updatedDate;
	
	private String voucherPin;

	private String fileName;
	
	private String epgTransactionId;

}
