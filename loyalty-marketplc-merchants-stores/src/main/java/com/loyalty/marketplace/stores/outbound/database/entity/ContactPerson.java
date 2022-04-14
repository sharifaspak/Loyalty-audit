package com.loyalty.marketplace.stores.outbound.database.entity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

public class ContactPerson {
	
	@Field("EmailId")
    private String emailId;
 
	@Field("MobileNumber")
	private String mobileNumber;
 
	@Field("FirstName")
	private String firstName;
 
	@Field("LastName")
	private String lastName;
    
	@Field("Fax")
    private String faxNumber;     
    
	@Field("UserName")
    private String userName;
    
	@Field("Pin")
    private Integer pin;
    
    @Transient
    private String password;

	public Integer getPin() {
		return pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "ContactPerson [emailId=" + emailId + ", mobileNumber=" + mobileNumber + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", faxNumber=" + faxNumber + ", userName=" + userName + ", pin=" + pin
				+ "]";
	}
 
    
}
