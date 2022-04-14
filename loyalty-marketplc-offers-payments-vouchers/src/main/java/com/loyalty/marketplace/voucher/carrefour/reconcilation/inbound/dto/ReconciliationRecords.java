package com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReconciliationRecords {

    private String TerminalTxNo;
    private String LineCount;
    private String TerminalId;
    private String CashierId;
    private String TransactionNumber;
    private String ReferenceNumber;
    private String TransactionType;
    private String GenCode;
    private String CardNumber;
    private String Currency;
    private String Amount;
    private String Balance;
    private String FinalStatus;


}