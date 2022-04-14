
package com.loyalty.marketplace.voucher.maf.inbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class ApplicationHeader {

    private String transactionID;
    private String requestedSystem;
    private String retryLimit;
    private String requestedDate;

}
