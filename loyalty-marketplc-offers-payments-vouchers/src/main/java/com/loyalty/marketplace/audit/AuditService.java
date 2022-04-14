package com.loyalty.marketplace.audit;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuditService {
	
	@Value("${audit.auditFlag}")
	private boolean auditFlag;
	
	@Value("${audit.enableAudit}")
	private List<String> enableAudit;
	
	@Autowired
	AuditRepository auditRepository;
	
	@Autowired
	AuditEvents auditEvent;

	private Audit setAuditData(String collectionName, Object object, String action,String changeType, Object oldObject,String transactionId,String osUser) {
		
		//return new Audit(collectionName, changeType, object, oldObject, action, new Date(), null, null);
		
		
		ObjectMapper obj = new ObjectMapper();
		String jsonStr = "[]";
		String jsonOldStr = "[]";
		try {
			if (null != object) {
				jsonStr = obj.writeValueAsString(object);
			}
			if (null != oldObject) {
				jsonOldStr = obj.writeValueAsString(oldObject);
			}

		} catch (JsonProcessingException e) { 
			e.printStackTrace();
		}
		return new Audit(collectionName, changeType, jsonStr, jsonOldStr, action, new Date(), null, null,transactionId,osUser);
		 
		
	}
//	public void insertDataAudit(String collectionName, Object object, String changeType,String transactionId,String osUser) {
//		if (auditFlag && enableAudit.contains(collectionName)) {
//			saveDataAudit(setAuditData(collectionName, object,"insert",changeType,null,transactionId,osUser));
//		}
//	}
	public void deleteDataAudit(String collectionName, Object object, String changeType,String transactionId,String osUser) {
		if (auditFlag && enableAudit.contains(collectionName)) {
		if (null!=object) {
			saveDataAudit(setAuditData(collectionName, object,"delete",changeType,null,transactionId,osUser));
		}else {
			saveDataAudit(setAuditData(collectionName, object,"deleteAll",changeType,null,transactionId,osUser));
		}
		}
	}
	public void updateDataAudit(String collectionName, Object modifiedObject, String changeType, Object existingObject,String transactionId,String osUser) {
		if (auditFlag && enableAudit.contains(collectionName)) {
			saveDataAudit(setAuditData(collectionName, modifiedObject,"update",changeType,existingObject,transactionId,osUser));
		}
	}
	private void saveDataAudit(Audit audit) {
		
		auditEvent.publishAuditEvent(audit);
			
	}
}
