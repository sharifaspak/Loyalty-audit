package com.loyalty.marketplace.outbound.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationRequestDto implements Serializable {

	private static final long serialVersionUID = 7361887449577737491L;	
	private String transactionId;
    private String notificationId;
    private String notificationCode;
    private Map<String,String> additionalParameters;
    private String language;
    private String accountNumber;
    private String membershipCode;
	
}
