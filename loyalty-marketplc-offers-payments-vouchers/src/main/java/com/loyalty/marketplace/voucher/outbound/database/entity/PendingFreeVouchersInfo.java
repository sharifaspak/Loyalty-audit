package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.voucher.constants.DBConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = DBConstants.PENDING_FREE_VOUCHER_INFO)
public class PendingFreeVouchersInfo {

	@Id	
	private String id;
	@Field("ProgramCode")
	private String programCode;	
	@Field("AccountNumber")
	private String accountNumber;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("OfferId")
	private String offerId;
	@Field("PartyId")
	private String partyId;
	@Field("VoucherType")
	private String voucherType;
	@Field("Denomination")
	private Integer denomination;
	@Field("ExternalTransactionId")
	private String externalTransactionId;
	@Field("ExpiryDate")
	private Date expiryDate;
	@Field("TransactionId")
	private String transactionId;
	@Field("VoucherCode")
	private String voucherCode;
	@Field("Status")
	private String status;
	@Field("Reason")
	private String reason;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	
}
