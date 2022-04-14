package com.loyalty.marketplace.service.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipNotes {
	private String noteID;
	private String note;
	private String createdUser;
	private Date createdDate;
}
