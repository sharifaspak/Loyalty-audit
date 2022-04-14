package com.loyalty.marketplace.voucher.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VoucherStatus {
	
	private VoucherStatus(){}
	
	public static final String ACTIVE = "active";
	public static final String READY = "ready";
	public static final String ERROR = "error";
	public static final String BURNT = "burnt";
	public static final String CANCELED = "cancelled";
	public static final String INVOICED = "invoiced";
	public static final String VERIFIED = "verified";
	public static final String REDEEMED = "redeemed";
	public static final String EXPIRED = "expired";
	
	public static final String PENDING = "PENDING";
	public static final String GIFTED = "GIFTED";
	public static final String FAILED = "FAILED";
	public static final String PARKED = "PARKED";
	public static final String OK = "OK";
	public static final String SUCCESS = "SUCCESS";
	
	public static final List<String> VOUCHER_STATS_ELIGIBLE_STATUS = Collections.unmodifiableList(Arrays.asList(ACTIVE, READY));
	
	
}
