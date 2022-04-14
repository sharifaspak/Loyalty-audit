package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

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
public class VoucherInfo {
	
	@Field("VoucherExpiryPeriod")
	private Integer voucherExpiryPeriod;
	@Field("VoucherAmount")
	private Double voucherAmount;
	@Field("VoucherAction")
	private String voucherAction;
	@Field("VoucherExpiryDate")
	private Date voucherExpiryDate;
	@Field("VoucherRedeemType")
	private String voucherRedeemType;
	@Field("PartnerRedeemURL")
    private String partnerRedeemURL ;
	@Field("InstructionsToRedeemTitle")
    private RedeemTitleInstructions instructionsToRedeemTitle;
	@Field("InstructionsToRedeem")
    private RedeemInstructions instructionsToRedeem;
		
}
