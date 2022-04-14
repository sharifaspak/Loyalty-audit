package com.loyalty.marketplace.gifting.helper.dto;

import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.utils.MarketplaceException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GiftingExceptionInfo {
	
	private String currentClassName;
	private String currentMethodName;
	private Exception exception;
	private MarketplaceException marketplaceException;
	private GiftingCodes errorResult;
	private GiftingCodes exceptionError;
	private GiftingCodes marketplaceError;
	private String exceptionLogId;
	
}
