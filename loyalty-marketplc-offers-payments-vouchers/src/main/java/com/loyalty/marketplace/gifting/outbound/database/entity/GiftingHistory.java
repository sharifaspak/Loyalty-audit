package com.loyalty.marketplace.gifting.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.domain.GoldGiftedDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = GiftingConfigurationConstants.GIFTING_HISTORY)
public class GiftingHistory {

	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("GiftType")
	private String giftType;
	
	@Field("SenderInfo")
	private MemberInfo senderInfo;
	
	@Field("ReceiverInfo")
	private MemberInfo receiverInfo;
	
	@Field("VoucherCode")
	private String voucherCode;
	
	@Field("PointsGiftedTransaction")
	private PointsGiftedDetails pointsGifted;
	
	@Field("GoldGiftedTransaction")
	private List<GoldGiftedDetails> goldGifted;
	
	@Field("ImageId")
	private Integer imageId;	
	
	@Field("ImageUrl")
	private String imageUrl;
	
	@Field("Message")
	private String message;	
	
	@Field("ScheduledDate")
	private Date scheduledDate;
			
	@Field("ReceiverConsumption")
	private String receiverConsumption;
	
	@Field("PaymentTransactionNo")
	private String transactionNo;
	
	@Field("PaymentTransactionDate")
	private Date transactionDate;	
	
	@Field("ExtRefNo")
	private String extRefNo;
		
	@Field("PurchaseDetails")
	private PurchaseDetails purchaseDetails;
	
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("CreatedDate")
	private Date createdDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
