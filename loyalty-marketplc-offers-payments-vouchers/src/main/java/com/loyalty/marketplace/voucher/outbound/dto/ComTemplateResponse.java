package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ComTemplateResponse {
	private int templateId;
	private String comType;
	private String msgSubject;
	private String description;
	private String template;
	private String language;
	private String active;
	private long notificationId;
	private String notificationCode;
	private String programCode;
	private Date updatedDate;
	private Date createdDate;
	private String createdUser;
	private String updatedUser;
}
