
package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeCarrefourAPIRequest {

    private ApplicationHeader ApplicationHeader;
    private InitializeCarrefourAPIRequest.DataHeader DataHeader;

    @Getter
    @Setter
    @ToString
    public static class DataHeader {

        private String MerchantID;
        private String Passphrase;
        private String TerminalID;
        private String CashierID;
        private List<AdditionalInfo> AdditionalInfo;
    }

}
