package com.loyalty.marketplace.voucher.ygag.inbound.dto;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.ApplicationHeader;

import lombok.Data;

@Data
public class DownloadYGAGVoucherRequest {
	
	private ApplicationHeader ApplicationHeader;

	private String referenceId;
    private String notify;
    private String brandCode;
    private String currency;
    private String amount;
    private String country;
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;
    private String message;
    private String deliveryType;
    
}
