package com.loyalty.marketplace.equivalentpoints.utils;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {
	
	NOT_FOUND(404, HttpStatus.NOT_FOUND),
	GENERIC_RUNTIME_EXCEPTION(3000, HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_PARAMETERS(3001, HttpStatus.BAD_REQUEST),
	PARTNER_INVALID_PARAMETERS_CONVERSIONRATE_PRESENT(3002, HttpStatus.INTERNAL_SERVER_ERROR),
	PARTNER_INVALID_PARAMETERS_PARTNERCODE(3003, HttpStatus.INTERNAL_SERVER_ERROR),
	PARTNER_NULL_PARAMETERS_PARTNERCODE(3004, HttpStatus.INTERNAL_SERVER_ERROR),
	PARTNER_INVALID_PARAMETERS_CONVERSIONRATE(3005, HttpStatus.BAD_REQUEST),
	PARTNER_INVALID_PARAMETERS_DENOMINATIONRANGE(3006, HttpStatus.INTERNAL_SERVER_ERROR),
	PARTNER_CONVERSIONRATE_EMPTY(3007, HttpStatus.BAD_REQUEST),
	PARTNER_ACTIVITY_GET_ACTIVITY_DETAILS_ERROR(3008, HttpStatus.BAD_REQUEST),
	MEMBER_MANAGEMENT_GET_MEMBER_DETAILS_ERROR(3009, HttpStatus.BAD_REQUEST),;
	
	private final int code;
	private final HttpStatus httpStatus;

	private ErrorCodes(int code) {
		this.code = code;
		this.httpStatus = HttpStatus.BAD_REQUEST;
	}

	private ErrorCodes(int code, HttpStatus httpStatus) {
		this.code = code;
		this.httpStatus = httpStatus;
	}

	public int getCode() {
		return code;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public static String getEnumNameForValue(int code1) {
		for (ErrorCodes e : ErrorCodes.values()) {
			if (code1 == e.code)
				return e.name();
		}
		return null;
	}

}
