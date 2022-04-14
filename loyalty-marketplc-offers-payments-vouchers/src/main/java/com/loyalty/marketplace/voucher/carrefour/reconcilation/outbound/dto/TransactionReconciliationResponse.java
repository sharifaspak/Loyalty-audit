package com.loyalty.marketplace.voucher.carrefour.reconcilation.outbound.dto;



import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.carrefour.reconcilation.inbound.dto.ResponseData;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionReconciliationResponse {

private ResponseData ResponseData;
private AckMessage AckMessage;
}