
package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationHeader {

    private  String TransactionID;
    private String RequestedSystem;
    private String RetryLimit;
    private String RequestedDate;

}
