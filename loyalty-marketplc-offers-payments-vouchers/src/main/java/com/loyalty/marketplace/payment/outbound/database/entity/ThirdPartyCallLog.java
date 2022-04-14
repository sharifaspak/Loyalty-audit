package com.loyalty.marketplace.payment.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "ThirdPartyCallLog")
public class ThirdPartyCallLog {

	@Id
	private String id;	
	
	@Field("Service_Name")
	private String serviceName;
	@Field("Response_Transaction_id")
	private String responseTransactionId;
	@Field("Status")
	private String status;
	@Field("Failed_Reason")
	private String failedRreason;
	@Field("Account_Number")
	private String accountNumber;
	@Field("Request_Payload")
	private String request_Payload;
	@Field("Response_Payload")
	private String responsePayload;
	@Field("CreatedDate")
	private Date createdDate;
	
	
	
}
