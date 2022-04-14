
package com.loyalty.marketplace.voucher.carrefour.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AckMessage {

    private String Status;
    private String ErrorCode;
    private String ErrorType;
    private String ErrorDescription;

}
