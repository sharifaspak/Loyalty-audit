package com.loyalty.marketplace.voucher.member.management.outbound.dto;

import lombok.ToString;

@ToString
public class ApiError {
	
	private Integer code;
	private String message;

	public ApiError() {
		super();
	}

	public ApiError(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
