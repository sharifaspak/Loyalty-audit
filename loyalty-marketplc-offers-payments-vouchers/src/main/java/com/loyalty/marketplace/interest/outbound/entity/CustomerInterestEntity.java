package com.loyalty.marketplace.interest.outbound.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.interest.inbound.dto.InterestIdRequestDto;

@Document(collection = "CustomerInterest")
public class CustomerInterestEntity {
	
	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field(value = "AccountNumber")
	private String accountNumber;
	
	@Field(value = "InterestId")
	private List<String> interestId;
	
	@Field(value = "CreatedDate")
	private Date createdDate;
	
	@Field(value = "CreatedUser")
	private String createdUser;
	
	

	

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getInterestId() {
		return interestId;
	}

	public void setInterestId(List<String> interestId) {
		this.interestId = interestId;
	}


	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	@Field(value = "UpdatedDate")
	private Date updatedDate;
	
	@Field(value = "UpdatedUser")
	private String updatedUser;


	
	
}
