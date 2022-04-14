
package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarrefourConfirmGCRequestRequest {

    private ApplicationHeader ApplicationHeader;
    private CarrefourConfirmGCRequestRequest.DataHeader DataHeader;

   

    @Getter
    @Setter
    @ToString
    public static class DataHeader {

        protected String SessionID;
        protected String MerchantID;
        protected String TerminalID;
        protected String CashierID;
        protected String ReferenceNumber;
        protected String Note;
        protected List<AdditionalInfo> AdditionalInfo;

      
    }

}
