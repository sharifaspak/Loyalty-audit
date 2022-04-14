
package com.loyalty.marketplace.voucher.carrefour.outbound.dto;

import java.math.BigDecimal;
import java.util.List;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AdditionalInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarrefourConfirmGCRequestResponse {

    private CarrefourConfirmGCRequestResponse.ResponseData ResponseData;
    private AckMessage AckMessage;

    @Getter
    @Setter
    @ToString
    public static class ResponseData {

        protected String TransactionID;
        protected String IsSuccessful;
        protected String ReferenceNumber;
        protected BigDecimal Balance;
        protected String Note;
        protected String PinCode;
        protected BigDecimal PointBalance;
        protected List<AdditionalInfo> AdditionalInfo;

    }

}
