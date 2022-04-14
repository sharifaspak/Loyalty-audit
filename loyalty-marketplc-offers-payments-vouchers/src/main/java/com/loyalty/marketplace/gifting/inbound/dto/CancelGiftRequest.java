package com.loyalty.marketplace.gifting.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@ToString
public class CancelGiftRequest {

    @NotEmpty(message = "{validation.cancelGiftRequest.voucherCode.notEmpty.msg}")
    private String voucherCode;

}
