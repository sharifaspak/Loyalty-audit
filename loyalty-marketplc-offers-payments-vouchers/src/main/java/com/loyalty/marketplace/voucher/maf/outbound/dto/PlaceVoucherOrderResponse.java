
package com.loyalty.marketplace.voucher.maf.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.maf.inbound.dto.AdditionalInfo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class PlaceVoucherOrderResponse {

    private PlaceVoucherOrderResponse.ResponseData ResponseData;
    private AckMessage AckMessage;

    @Getter
    @Setter
    @ToString
    @JsonInclude(Include.NON_NULL)
    public static class ResponseData {

        private String TransactionID;
        private PlaceVoucherOrderResponse.ResponseData.VoucherDetail VoucherDetail;
        private List<AdditionalInfo> AdditionalInfo;

     @Data
     @JsonInclude(Include.NON_EMPTY)
        public static class VoucherDetail {

            private String OrderID;
            private String OrderReferenceNumber;
            private String NumberOfItems;
            private String CustomerID;
            private String RecipientID;
            private String IPAddress;
            private String VoucherID;
            private String PartnerID;
            private String CountryID;
            private String StatusID;
            private String PaymentMethodID;
            private String Source;
            private String TotalActivationFee;
            private String TotalDeliveryFee;
            private String TotalFeeAmount;
            private String VAT;
            private String TotalVatableFee;
            private String TotalLoadAmount;
            private String TotalAmount;
            private String TotalPaid;
            private String ExpiredAfter;
            private String CreatedAt;
            private List<AdditionalInfo> AdditionalInfo;
        }
        
    }
            
}
