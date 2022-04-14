package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherInfoDomain {
	
	private Integer voucherExpiryPeriod;
	private Double voucherAmount;
	private String voucherAction;
	private Date voucherExpiryDate;
	private String voucherRedeemType;
	private String partnerRedeemURL ;
	private RedeemTitleInstructionsDomain instrutionsToRedeemTitle;
	private RedeemInstructionsDomain instrutionsToRedeem;
		
}
