
package com.loyalty.marketplace.voucher.maf.inbound.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class PlaceVoucherOrderRequest {

    @NotEmpty
    private PlaceVoucherOrderRequest.DataHeader DataHeader;

    @Getter
    @Setter
    @ToString
    @JsonInclude(Include.NON_NULL)
    public static class DataHeader {

    	@NotEmpty
    	private String TargetSystem;
    	@NotEmpty
        private String ProductID;
        private String PaymentMethodID;
        @NotEmpty
        private String DeliveryMethodID;
        @NotEmpty
        private String LoadAmount;
        private String FirstName;
        private String LastName;
        private String Email;
        private String MobileCode;
        private String MobileNumber;
        private String Address1;
        private String Address2;
        private String City;
        private String Gender;
        private List<PlaceVoucherOrderRequest.DataHeader.AdditionalInfo> AdditionalInfo;
        @Data
        @JsonInclude(Include.NON_NULL)
        public static class AdditionalInfo {

        	@NotEmpty
            private String Name;
        	@NotEmpty
            private String Value;

        }
        
    }
}
