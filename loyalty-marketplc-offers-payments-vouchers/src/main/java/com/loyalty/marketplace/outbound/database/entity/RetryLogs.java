package com.loyalty.marketplace.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetryLogs {
	
	@Field("TransactionId")
	private String transactionId;
	@Field(value = "Url")
	private String url;
	@Field(value = "Request")
	private String request;
	@Field(value = "CreatedDate")
	private Date createdDate;
	@Field(value = "CreatedUser")
	private String createdUser;
}
