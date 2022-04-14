package com.loyalty.marketplace.outbound.service.dto;

import java.util.ArrayList;
import java.util.List;

import com.loyalty.marketplace.outbound.dto.Errors;


public class ApiStatus {
	 @Override
	public String toString() {
		return "ApiStatus [message=" + message + ", overallStatus=" + overallStatus + ", statusCode=" + statusCode
				+ ", errors=" + errors + ", externalTransationId=" + externalTransationId + "]";
	}

	private String message;
	 private String overallStatus;
	 private float statusCode;
	 ArrayList < Errors > errors = new ArrayList < Errors > ();
	 private String externalTransationId;

	 public List<Errors> getErrors() {
			return errors;
		}
		public void setErrors(List<Errors> errors) {
			this.errors = (ArrayList<Errors>) errors;
		}

	 // Getter Methods 

	 public String getMessage() {
	  return message;
	 }

	 public String getOverallStatus() {
	  return overallStatus;
	 }

	 public float getStatusCode() {
	  return statusCode;
	 }

	 public String getExternalTransationId() {
	  return externalTransationId;
	 }

	 // Setter Methods 

	 public void setMessage(String message) {
	  this.message = message;
	 }

	 public void setOverallStatus(String overallStatus) {
	  this.overallStatus = overallStatus;
	 }

	 public void setStatusCode(float statusCode) {
	  this.statusCode = statusCode;
	 }

	 public void setExternalTransationId(String externalTransationId) {
	  this.externalTransationId = externalTransationId;
	 }
	
}
