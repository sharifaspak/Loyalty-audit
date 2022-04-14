package com.loyalty.marketplace.inbound.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ContactPersonDto {

	@NotEmpty(message="{validation.ContactPersonDto.emailId.notEmpty.msg}")
	@Email(message="{validation.ContactPersonDto.emailId.format.msg}")
	private String emailId;	
	
	@NotEmpty(message="{validation.ContactPersonDto.firstName.notEmpty.msg}")
	private String firstName;	
	
	@NotEmpty(message="{validation.ContactPersonDto.lastName.notEmpty.msg}")
	private String lastName;
	

	@NotEmpty(message="{validation.ContactPersonDto.mobileNumber.notEmpty.msg}")
	private String mobileNumber;
	
	private String faxNumber;
	
	private String userName;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((faxNumber == null) ? 0 : faxNumber.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	
	@Override
    public boolean equals(Object o) {
        if (!(o instanceof ContactPersonDto)) {
            return false;
        }
        ContactPersonDto contcatPerson  = (ContactPersonDto) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getEmailId(), contcatPerson.getEmailId());
        builder.append(getFaxNumber(), contcatPerson.getFaxNumber());
        builder.append(getFirstName(), contcatPerson.getFirstName());
        builder.append(getLastName(), contcatPerson.getLastName());
        builder.append(getMobileNumber(), contcatPerson.getMobileNumber());
        builder.append(getUserName(), contcatPerson.getUserName());
        return builder.isEquals();
    }

	@Override
	public String toString() {
		return "ContactPersonDto [emailId=" + emailId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", mobileNumber=" + mobileNumber + ", faxNumber=" + faxNumber + ", userName=" + userName + "]";
	}
	
}
