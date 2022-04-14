package com.loyalty.marketplace.offers.points.bank.inbound.dto;

import java.io.Serializable;

import com.loyalty.marketplace.outbound.events.eventobject.Event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LifeTimeSavingsVouchersEvent extends Event implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String membershipCode;
	private String accountNumber;
	private int quantity;
	private boolean hasEstimatedSavings;
	private double estimatedSavings;
	private double spendValue;
	private String type;
	private String externalTransactionId;
	private boolean subscriptionStatus;
	private String merchantName;
	private double subscriptionWaiveOff;
	private String pointsTransactionId;
	
}
