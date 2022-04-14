package com.loyalty.marketplace.payment.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorization {
	
		private String Currency;
		private String TransactionHint;
		private String OrderID;
		private String Channel;
		private Double Amount;
		private String Customer;
		private String TransactionID;
		private String OrderName;
		private String Terminal;
		private String Store;
		private String UserName;
		private String Password;
		
}
