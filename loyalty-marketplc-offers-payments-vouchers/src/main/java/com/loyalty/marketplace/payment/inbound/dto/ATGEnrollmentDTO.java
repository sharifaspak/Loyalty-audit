package com.loyalty.marketplace.payment.inbound.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ATGEnrollmentDTO implements Serializable{
	
	private static final long serialVersionUID = -7461041673508163970L;
	private String accountNumber;
	private String atgUserName;
	private String channel;
	private String token;
	private String externalTransactionId;
}
