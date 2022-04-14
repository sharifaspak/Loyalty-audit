package com.loyalty.marketplace.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Accenture
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ExceptionLogs")
public class ExceptionLogs {

	@Id
	private String id;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("ExceptionDetails")
	private String exceptionDetails;
	@Field("TransactionId")
	private String transactionId;
	@Field(value = "Url")
	private String url;
	@Field(value = "CreatedDate")
	private Date createdDate;
	@Field(value = "UpdatedDate")
	private Date lastUpdated;
	@Field(value = "CreatedUser")
	private String createdUser;
	@Field(value = "UpdatedUser")
	private String updatedUser;

}
