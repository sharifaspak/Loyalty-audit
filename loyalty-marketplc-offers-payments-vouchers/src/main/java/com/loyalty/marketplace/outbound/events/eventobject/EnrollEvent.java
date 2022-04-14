package com.loyalty.marketplace.outbound.events.eventobject;

import java.io.Serializable;

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
@JsonInclude(Include.NON_NULL)
public class EnrollEvent extends Event implements Serializable {

	private static final long serialVersionUID = 1L;
	private String accountNumber;
	private String membershipCode;
	private String userName;
	private String externalTransactionId;
}
