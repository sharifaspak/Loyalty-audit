package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CashVoucherBurntInfo {

	@Field("Action")
	private String action;
	
	@Field("ExternalTransactionId")
	private String externalTransactionId;
	
	@Field("OrderId")
	private String orderId;
	
	@Field("VoucherBurntDate")	
	private Date voucherBurntDate;
	
	@Field("VoucherBurntUser")
	private String voucherBurntUser;
	
	@Field("VoucherNewBalance")
	private Double voucherNewBalance;
	
	@Field("VoucherCurrentBalance")
	private Double voucherCurrentBalance;
	
	@Field("RollBackDate")	
	private Date rollBackDate;
	
	@Field("RollBackUser")
	private String rollBackUser;
	
	@Field("RollBackAmount")
	private Double rollbackAmount;
	
}
