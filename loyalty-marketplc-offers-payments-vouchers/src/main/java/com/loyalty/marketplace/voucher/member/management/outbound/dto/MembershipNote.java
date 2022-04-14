package com.loyalty.marketplace.voucher.member.management.outbound.dto;

import java.util.Date;

public class MembershipNote {

	private String note;
	private String createdUser;
	private Date createdDate;

	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
