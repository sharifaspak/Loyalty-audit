package com.loyalty.marketplace.merchants.outbound.service.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.ToString;

@ToString
public class EmailRequestDto implements Serializable {

	private static final long serialVersionUID = -9023794945914975109L;
	
	private String transactionId;
	private String templateId;
	private String notificationId;
	private String notificationCode;
	private Map<String,String> additionalParameters;
	private String language;
	private String emailId;
	private String accountNumber;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}
	public String getNotificationCode() {
		return notificationCode;
	}
	public void setNotificationCode(String notificationCode) {
		this.notificationCode = notificationCode;
	}
	public Map<String, String> getAdditionalParameters() {
		return additionalParameters;
	}
	public void setAdditionalParameters(Map<String, String> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
