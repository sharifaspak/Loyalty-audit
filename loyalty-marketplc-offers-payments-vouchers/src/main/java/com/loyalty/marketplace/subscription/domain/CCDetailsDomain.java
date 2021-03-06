package com.loyalty.marketplace.subscription.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CCDetailsDomain {

	private String cardNumber;
	private String subtype;
}
