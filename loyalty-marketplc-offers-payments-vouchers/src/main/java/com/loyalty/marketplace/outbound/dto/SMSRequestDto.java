package com.loyalty.marketplace.outbound.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSRequestDto implements Serializable {

	private static final long serialVersionUID = -9121061427387316609L;
	private String transactionId;
	private String templateId;
	private String notificationId;
	private String notificationCode;
	private String membershipCode;
	private Map<String, String> additionalParameters;
	private String language;
	private List<String> destinationNumber;
}
