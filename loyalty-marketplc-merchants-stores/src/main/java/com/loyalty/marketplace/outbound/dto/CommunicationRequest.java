package com.loyalty.marketplace.outbound.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommunicationRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5357238694787330241L;
	private String channelId;
	private String systemid;
	private String systempassword;
	private String token;
	private String receivertype;
	private String receiverid;
	private String templateid;
	private String communicationtype; 
	private String additionalParameters;
	private String commSubject;
	private String commText;
	private String smilesEmail;
	private String uiLanguage;
	private String attachmentContent;
	private String sticker;
	private String voucherDescription;
	//Additional parameters
	private boolean dbFlag;
	private String dbMethod;
	private String dataValidation;
	private String finalText;
	private String template;
	private String subject;
	private String notificationId;
	private String notificationCode;
	private String memCode;
	private String notificationLanguage;
	private Map<String,String> placeholders;
	private Map<String,String> dynamicParameters;
	
	
	public CommunicationRequest(
			@JsonProperty("channelId") String channelId, 
			@JsonProperty("systemid") String systemid, 
			@JsonProperty("systempassword") String systempassword, 
			@JsonProperty("token") String token,
			@JsonProperty("receivertype") String receivertype, 
			@JsonProperty("receiverid") String receiverid, 
			@JsonProperty("templateid") String templateid, 
			@JsonProperty("communicationtype") String communicationtype,
			@JsonProperty("additionalParameters") String additionalParameters, 
			@JsonProperty("commSubject") String commSubject, 
			@JsonProperty("commText") String commText, 
			@JsonProperty("smilesEmail") String smilesEmail, 
			@JsonProperty("uiLanguage") String uiLanguage,
			@JsonProperty("attachmentContent") String attachmentContent, 
			@JsonProperty("sticker") String sticker, 
			@JsonProperty("voucherDescription") String voucherDescription, 
			@JsonProperty("dbFlag") boolean dbFlag, 
			@JsonProperty("dbMethod") String dbMethod,
			@JsonProperty("dataValidation") String dataValidation, 
			@JsonProperty("finalText") String finalText, 
			@JsonProperty("template") String template, 
			@JsonProperty("subject") String subject, 
			@JsonProperty("notificationId") String notificationId,
			@JsonProperty("notificationCode") String notificationCode, 
			@JsonProperty("memCode") String memCode, 
			@JsonProperty("notificationLanguage") String notificationLanguage, 
			@JsonProperty("placeholders") Map<String, String> placeholders,
			@JsonProperty("dynamicParameters") Map<String, String> dynamicParameters) {
		super();
		this.channelId = channelId;
		this.systemid = systemid;
		this.systempassword = systempassword;
		this.token = token;
		this.receivertype = receivertype;
		this.receiverid = receiverid;
		this.templateid = templateid;
		this.communicationtype = communicationtype;
		this.additionalParameters = additionalParameters;
		this.commSubject = commSubject;
		this.commText = commText;
		this.smilesEmail = smilesEmail;
		this.uiLanguage = uiLanguage;
		this.attachmentContent = attachmentContent;
		this.sticker = sticker;
		this.voucherDescription = voucherDescription;
		this.dbFlag = dbFlag;
		this.dbMethod = dbMethod;
		this.dataValidation = dataValidation;
		this.finalText = finalText;
		this.template = template;
		this.subject = subject;
		this.notificationId = notificationId;
		this.notificationCode = notificationCode;
		this.memCode = memCode;
		this.notificationLanguage = notificationLanguage;
		this.placeholders = placeholders;
		this.dynamicParameters = dynamicParameters;
	}
	public CommunicationRequest() {
		super();
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getSystempassword() {
		return systempassword;
	}
	public void setSystempassword(String systempassword) {
		this.systempassword = systempassword;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getReceivertype() {
		return receivertype;
	}
	public void setReceivertype(String receivertype) {
		this.receivertype = receivertype;
	}
	public String getReceiverid() {
		return receiverid;
	}
	public void setReceiverid(String receiverid) {
		this.receiverid = receiverid;
	}
	public String getTemplateid() {
		return templateid;
	}
	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}
	public String getCommunicationtype() {
		return communicationtype;
	}
	public void setCommunicationtype(String communicationtype) {
		this.communicationtype = communicationtype;
	}
	public String getAdditionalParameters() {
		return additionalParameters;
	}
	public void setAdditionalParameters(String additionalParameters) {
		this.additionalParameters = additionalParameters;
	}
	public String getCommSubject() {
		return commSubject;
	}
	public void setCommSubject(String commSubject) {
		this.commSubject = commSubject;
	}
	public String getCommText() {
		return commText;
	}
	public void setCommText(String commText) {
		this.commText = commText;
	}
	public String getSmilesEmail() {
		return smilesEmail;
	}
	public void setSmilesEmail(String smilesEmail) {
		this.smilesEmail = smilesEmail;
	}
	public String getUiLanguage() {
		return uiLanguage;
	}
	public void setUiLanguage(String uiLanguage) {
		this.uiLanguage = uiLanguage;
	}
	public String getAttachmentContent() {
		return attachmentContent;
	}
	public void setAttachmentContent(String attachmentContent) {
		this.attachmentContent = attachmentContent;
	}
	public String getSticker() {
		return sticker;
	}
	public void setSticker(String sticker) {
		this.sticker = sticker;
	}
	public String getVoucherDescription() {
		return voucherDescription;
	}
	public void setVoucherDescription(String voucherDescription) {
		this.voucherDescription = voucherDescription;
	}
	public boolean isDbFlag() {
		return dbFlag;
	}
	public void setDbFlag(boolean dbFlag) {
		this.dbFlag = dbFlag;
	}
	public String getDbMethod() {
		return dbMethod;
	}
	public void setDbMethod(String dbMethod) {
		this.dbMethod = dbMethod;
	}
	public String getDataValidation() {
		return dataValidation;
	}
	public void setDataValidation(String dataValidation) {
		this.dataValidation = dataValidation;
	}
	public String getFinalText() {
		return finalText;
	}
	public void setFinalText(String finalText) {
		this.finalText = finalText;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	public String getMemCode() {
		return memCode;
	}
	public void setMemCode(String memCode) {
		this.memCode = memCode;
	}
	public String getNotificationLanguage() {
		return notificationLanguage;
	}
	public void setNotificationLanguage(String notificationLanguage) {
		this.notificationLanguage = notificationLanguage;
	}
	public Map<String, String> getPlaceholders() {
		return placeholders;
	}
	public void setPlaceholders(Map<String, String> placeholders) {
		this.placeholders = placeholders;
	}
	public Map<String, String> getDynamicParameters() {
		return dynamicParameters;
	}
	public void setDynamicParameters(Map<String, String> dynamicParameters) {
		this.dynamicParameters = dynamicParameters;
	}
	@Override
	public String toString() {
		return "CommunicationRequest [channelId=" + channelId + ", systemid=" + systemid + ", systempassword="
				+ systempassword + ", token=" + token + ", receivertype=" + receivertype + ", receiverid=" + receiverid
				+ ", templateid=" + templateid + ", communicationtype=" + communicationtype + ", additionalParameters="
				+ additionalParameters + ", commSubject=" + commSubject + ", commText=" + commText + ", smilesEmail="
				+ smilesEmail + ", uiLanguage=" + uiLanguage + ", attachmentContent=" + attachmentContent + ", sticker="
				+ sticker + ", voucherDescription=" + voucherDescription + ", dbFlag=" + dbFlag + ", dbMethod="
				+ dbMethod + ", dataValidation=" + dataValidation + ", finalText=" + finalText + ", template="
				+ template + ", subject=" + subject + ", notificationId=" + notificationId + ", notificationCode="
				+ notificationCode + ", memCode=" + memCode + ", notificationLanguage=" + notificationLanguage
				+ ", placeholders=" + placeholders + ", dynamicParameters=" + dynamicParameters + "]";
	}
	
	

}
