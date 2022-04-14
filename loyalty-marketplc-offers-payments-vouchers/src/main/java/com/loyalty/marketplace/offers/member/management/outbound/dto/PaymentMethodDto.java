package com.loyalty.marketplace.offers.member.management.outbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {
	
	@JsonProperty("paymentMethodId")
	private String methodId;
	@JsonProperty("description")
	private String methodDescription;

}
