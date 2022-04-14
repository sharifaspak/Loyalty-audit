
package com.loyalty.marketplace.voucher.member.management.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResult {
 
	private String accountNumber; 
	private String transactionId; 
	private String smilesId;
	private String membershipCode;
	@JsonInclude(Include.NON_NULL)
	private String message;
}
