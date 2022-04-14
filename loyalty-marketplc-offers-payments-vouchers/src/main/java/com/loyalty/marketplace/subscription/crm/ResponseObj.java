package com.loyalty.marketplace.subscription.crm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObj {
	private String responseCode;
	private String responseDesc;
	private Result result;
	private boolean lastPage;
	private ResponseAdditionalInfo responseAdditionalInfo ;
	private List<ResponseLineItems> responseLineItems;
	private String orderId; 

}
