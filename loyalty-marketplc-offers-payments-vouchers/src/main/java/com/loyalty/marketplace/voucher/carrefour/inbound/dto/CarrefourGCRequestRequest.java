
package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarrefourGCRequestRequest {

    private ApplicationHeader ApplicationHeader;
    private CarrefourGCRequestRequest.DataHeader DataHeader;

    @Getter
    @Setter
    @ToString
    public static class DataHeader {

        private String SessionID;
        private String MerchantID;
        private String TerminalID;
        private String CashierID;
        private String TransactionNumber;
        private String MsgType;
        private String GenCode;
        private String CardNumber;
        private String TrackToData;
        private BigDecimal Amount;
        private String Note;
        private String Reason;
        private String ValidityDate;
        private String MobileNo;
        private BigDecimal BasketAmount;
        private String BasketCategories;
        private String PinCode;
        private String Ean128;
        private List<AdditionalInfo> AdditionalInfo;


    }

}
