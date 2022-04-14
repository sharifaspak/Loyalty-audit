
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
@JsonInclude(Include.NON_EMPTY)
public class VoucherReconciliationRequest {

    private VoucherReconciliationRequest.DataHeader DataHeader;

    @Getter
    @Setter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    public static class DataHeader {

       @NotEmpty
        private String TargetSystem;
       @NotEmpty
       private String FromDate;
       @NotEmpty
        private String ToDate;
        private Integer Page;
        private Integer Limit;
        private List<VoucherReconciliationRequest.DataHeader.AdditionalInfo> AdditionalInfo;

        @Data
        @JsonInclude(Include.NON_EMPTY)
        public static class AdditionalInfo {

        	 @NotEmpty
            private String Name;
        	 @NotEmpty
            private String Value;

        }

    }

}
