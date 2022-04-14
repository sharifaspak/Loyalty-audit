package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionReconciliationRequest {

    private ApplicationHeader ApplicationHeader;
    private DataHeader DataHeader;

}