package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.voucher.domain.PurchaseDetailsDomain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VoucherGiftResult {

	private String programCode;
	private String id;
	private String voucherCode;
	private String senderMembershipCode;
	private String receiverMembershipCode;
	private String senderAccountNumber;
	private String senderFirstName;
	private String senderLastName;
	private String receiverAccountNumber;
	private String receiverFirstName;
	private String receiverLastName;
	private String imageName;
	private String imageCategory;
	private String imageUrl;
	private String message;
	private Date giftSendDate;
	private PurchaseDetailsDomain purchaseDetails;
	private String receiverConsumption;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;

}
