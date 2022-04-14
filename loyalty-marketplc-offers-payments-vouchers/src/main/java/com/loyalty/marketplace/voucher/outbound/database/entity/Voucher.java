package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;

import lombok.Data;
import lombok.ToString;

@Document(collection = "Voucher")
@Data
@ToString
public class Voucher {
	
	@Field("ProgramCode")
	private String programCode;	
	
	@Id	
	private String id;
	
	@NotEmpty(message="Voucher Code cannot be empty")
	@Field("VoucherCode")
	@Indexed
	private String voucherCode;
	
	@Field("OfferInfo")
	private OfferInfo offerInfo;
	
	@Field("MerchantCode")
	@Indexed
	private String merchantCode;
	
	@Field("PartnerCode")
	@Indexed
	private String partnerCode;
	
	@Field("MerchantName")
	private String merchantName;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("AccountNumber")
	@Indexed
	private String accountNumber;
	
	@Field("GiftDetails")
	private VoucherGiftDetails giftDetails;
	
	@Field("Type")
	@Indexed
	private String type;
	
	@Field("VoucherType")
	private String voucherType;
	
	@Field("Status")
	@Indexed
	private String status;
	
	@Field("VoucherValues")
	private VoucherValues voucherValues;
	
	@Field("PartnerReferNumber")
	private String partnerReferNumber;
	
	@Field("PartnerTransactionId")
	private String partnerTransactionId;
	
	@Field("UUID")
	@DBRef
	private PurchaseHistory uuid;
	
	@Field("DownloadedDate")
	@Indexed
	private Date downloadedDate;
	
	@Field("ExpiryDate")
	@NotNull(message="Expiry Date cannot be null")
	@Indexed
	private Date expiryDate;
	
	@Field("IsBlackListed")
	@Indexed
	private boolean isBlackListed;
	
	@Field("BlackListedUser")
	private String blackListedUser;	
	
	@Field("BlackListedDate")
	private Date blacklistedDate;
	
	@Field("BulkId")
	private String bulkId;	
	
	@Field("StartDate")
	private Date startDate;
	
	@Field("EndDate")
	private Date endDate;
	
	@Field("VoucherAmount")
	private Double voucherAmount;
		
	@Field("Transfer")
	private VoucherTransfer transfer;
	
	@Field("MerchantInvoiceId")
	private String merchantInvoiceId;
	
	@Field("BurntInfo")
	private BurntInfo burntInfo;
	
	@Field("BarcodeType")
	@DBRef
	private Barcode barcodeType;
	
	@Field("VoucherBalance")
	private Double voucherBalance;
	
	@Field("Channel")
	private String channel;
	
	@Field("CreatedDate")
	@Indexed
	private Date createdDate;
	
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("CashVoucherBurntInfo")
	private List<CashVoucherBurntInfo> cashVoucherBurntInfo;
	
	@Field("BurnEnquiryCount")
	private int burnEnquiryCount;
	
	@Field("LastburnEnquiryUser")
	private String lastburnEnquiryUser;
	
	@Field("LastEnquiryDate")
	private Date lastEnquiryDate;
	
	@Field("VoucherPin")
	private String voucherPin;
	
	@Field("FileName")
	private String fileName;	
}
