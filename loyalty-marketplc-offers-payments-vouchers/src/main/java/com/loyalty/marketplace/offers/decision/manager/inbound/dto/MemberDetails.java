package com.loyalty.marketplace.offers.decision.manager.inbound.dto;

import java.util.List;

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
@NoArgsConstructor
@ToString
public class MemberDetails {
	
	private String accountNumber;
	private String membershipCode;
	private List<String> customerType;
    private String tierLevelName;
    private boolean subscribed;
    private boolean hasCobranded;
    private List<String> coBrand;
  	private boolean activeCoBranded;
  	private double billPaymentAmount;
  	private double rechargeAmount;
	
}
