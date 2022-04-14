
package com.loyalty.marketplace.voucher.carrefour.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AdditionalInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeCarrefourAPIResponse {

    private InitializeCarrefourAPIResponse.ResponseData ResponseData;
    private AckMessage AckMessage;
    @Getter
    @Setter
    @ToString
    public static class ResponseData {

        private String TransactionID;
        private String IsSuccessful;
        private String SessionID;
        private List<AdditionalInfo> AdditionalInfo;
    }

}
