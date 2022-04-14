package com.loyalty.marketplace.voucher.ygag.outbound.dto;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;

import lombok.Data;

@Data
public class DownloadYGAGVoucherResponse {
	
	private String TransactionID;
	private String referenceId;
	private String orderId;
	private String state;
	private String deliveryType;
	private String isGeneric;
	private String notify;
	private String barCode;
	private String pdfLink;
	private String expiryDate;
	private String redemptionInstructions;
	private String country;
	private String receiverName;
	private String receiverEmail;
	private String receiverPhone;
	private String message;
	private String importantNotes;
	
	private DownloadYGAGVoucherResponse.OrderedAmount orderedAmount;
	private DownloadYGAGVoucherResponse.BrandAmount brandAcceptedAmount;
	private DownloadYGAGVoucherResponse.GiftVoucher giftVoucher;
	private DownloadYGAGVoucherResponse.BrandDetails brandDetails;
	
    private AckMessage AckMessage;

    
    @Data
    public static class OrderedAmount {
    	
    	private String currency;
    	private String amount;
    }
    
    @Data
    public static class BrandAmount {
    	
    	private String currency;
    	private String amount;
    }
	
	@Data
    public static class GiftVoucher {
    	
		private String code;
    	private String pin;
    	private String url;    	
    }
	
	@Data
    public static class BrandDetails {
		private String logo;
		private String productImage;
		private String code;
		private String name;
		private String pinRedeemable;

    }

}
