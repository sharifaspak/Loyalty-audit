package com.loyalty.marketplace.gifting.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PointsGiftedDetails {
	
	@Field("PointsGifted")
	private Integer pointsGifted;
	
	@Field("SenderTransactionRefId")
	private String senderTransactionRefId;
	
	@Field("ReceiverTransactionRefId")
	private String receiverTransactionRefId;
	
}
