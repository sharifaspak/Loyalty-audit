package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListVoucherStatusResult {

	List<VoucherListResult> vouchers;
	
}
