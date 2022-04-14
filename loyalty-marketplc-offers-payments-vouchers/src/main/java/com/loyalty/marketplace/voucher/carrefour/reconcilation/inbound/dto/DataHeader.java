package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataHeader {

    private String MerchantID;
    private String Passphrase;
    private String BusinessDate;
    private ReconciliationRecords ReconciliationRecords;
}