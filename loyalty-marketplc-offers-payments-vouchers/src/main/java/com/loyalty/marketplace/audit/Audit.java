package com.loyalty.marketplace.audit;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Audit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8286686683686932651L;

	private String collection;
	private String reason;
	private String auditData;
	private String existingData;
	private String action;
	private Date createdDate;
	private String status;
	private String statusDetail;
	private String transactionId;
	private String osUser;
	
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAuditData() {
		return auditData;
	}
	public void setAuditData(String auditData) {
		this.auditData = auditData;
	}
	public String getExistingData() {
		return existingData;
	}
	public void setExistingData(String existingData) {
		this.existingData = existingData;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDetail() {
		return statusDetail;
	}
	public void setStatusDetail(String statusDetail) {
		this.statusDetail = statusDetail;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOsUser() {
		return osUser;
	}
	public void setOsUser(String osUser) {
		this.osUser = osUser;
	}
	
	@Override
	public String toString() {
		return "Audit [collection=" + collection + ", reason=" + reason + ", auditData=" + auditData + ", existingData="
				+ existingData + ", action=" + action + ", createdDate=" + createdDate + ", status=" + status
				+ ", statusDetail=" + statusDetail + ", transactionId=" + transactionId + ", osUser=" + osUser + "]";
	}
	public Audit(@JsonProperty("collection")String collection, 
			@JsonProperty("reason")String reason, 
			@JsonProperty("auditData")String auditData, 
			@JsonProperty("existingData")String existingData, 
			@JsonProperty("action")String action,
			@JsonProperty("createdDate")Date createdDate, 
			@JsonProperty("status")String status, 
			@JsonProperty("statusDetail")String statusDetail,
			@JsonProperty("transactionId")String transactionId,
			@JsonProperty("osUser")String osUser) {
		super();
		this.collection = collection;
		this.reason = reason;
		this.auditData = auditData;
		this.existingData = existingData;
		this.action = action;
		this.createdDate = createdDate;
		this.status = status;
		this.statusDetail = statusDetail;
		this.transactionId = transactionId;
		this.osUser = osUser;
	}
	
	
		
}
