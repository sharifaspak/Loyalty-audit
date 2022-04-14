package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiStatus {
	private String externalTransactionId;
	private String message;
	private String overallStatus;
	private Integer statusCode;
	private List<ApiError> errors = new ArrayList<>();

	public ApiStatus(String externalTransactionId) {
		super();
		this.externalTransactionId = externalTransactionId;
	}
}
