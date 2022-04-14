package com.loyalty.marketplace.gifting.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class EnrollmentResult {

	private String accountNumber;
	private String transactionId;
	private String smilesId;
	private String membershipCode;
	@JsonInclude(Include.NON_NULL)
	private String message;
	private List<String> customerType;
	private String tierLevelName;

}