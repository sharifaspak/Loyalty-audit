package com.loyalty.marketplace.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ServiceCallLogsDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 178931989321298192L;
	private String transactionId;
	private transient Object request;
	private transient Object response;
	private String action;
	private String accountNumber;
	private String status;
	private Date createdDate;
	private String createdUser;
	private long responseTime;
	private Map<String,String> requestParam;
	private Map<String,String> header;
	private String method;
	
}