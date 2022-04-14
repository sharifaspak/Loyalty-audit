package com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;

import lombok.Data;

@Data
public class PhoneyTunesResponse {
	
	protected String TransactionID;
	protected String response;
	protected AckMessage AckMessage;
}
