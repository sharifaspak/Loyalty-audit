package com.loyalty.marketplace.gifting.helper.dto;

import java.util.Map;

import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NotificationHelperDto {

	private String type;
	
	private GiftingHistory giftingHistory;
	
	private String giftType;
	
	private int pointsGifted;
	
	private Double goldGifted;
	
	private String senderEmailId;
	
	private String senderAccountNumber;
	
	private String senderContactNumber;
	
	private String senderFirstName;
	
	private String senderLanguage;
	
	private String receiverEmailId;
	
	private String receiverAccountNumber;
	
	private String receiverContactNumber;
	
	private String receiverFirstName;
	
	private String receiverLanguage;
	
	private String templateId;
	
	private String notificationId;
	
	private String notificationCode;
	
	private Map<String, String> additionalParam;
		
}
