package com.loyalty.marketplace.voucher.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountsDto {

	private List<String> accountNumber;
}
