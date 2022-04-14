package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BurntInfo {

	@Field("VoucherBurnt")
	private boolean voucherBurnt;
	
	@Field("VoucherBurntId")
	private String voucherBurntId;
	
	@Field("VoucherBurntDate")
	@Indexed
	private Date voucherBurntDate;
	
	@Field("VoucherBurntUser")
	private String voucherBurntUser;
	
	@Field("BurnNotes")
	private String burnNotes;
	
	@Field("StoreCode")
	private String storeCode;
	
}
