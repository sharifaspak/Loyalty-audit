package com.loyalty.marketplace.utils;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.stores.constants.StoreCodes;

public class MarketplaceException extends Exception {

	private static final long serialVersionUID = -8460356990632230194L;

	private int errorCode;
	private String errorMsg;
	private String detailMessage;
	private Throwable cause;
	private String className;
	private String methodName;

	public MarketplaceException(MarketPlaceCode code) {
		super();
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
	}

	public MarketplaceException(String message, Throwable cause, MarketPlaceCode code) {
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
		this.cause = cause;
	}

	public MarketplaceException(String className, String methodName, String message, MarketPlaceCode code) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
	}
	
	public MarketplaceException(String className, String methodName, String message, MerchantCodes code) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
	}
	
	public MarketplaceException(String className, String methodName, String message, StoreCodes code) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
	}

	public MarketplaceException(Throwable cause, MarketPlaceCode code) {
		super(cause);
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
	}

	public int getErrorCodeInt() {
		return errorCode;
	}
	
	public String getErrorCode() {
		return Integer.toString(errorCode);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	public Throwable getCustomCause() {
		return this.cause;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String printMessage() {

		if (this.getErrorCode() != null) {

			StringBuilder printMessage = new StringBuilder();
			printMessage.append(" ERROR CODE: " + this.getErrorCode() + "| ERROR MESSAGE:" + this.getErrorMsg());
			if (this.getClassName() != null)
				printMessage.append("| SOURCE:" + this.getClassName());
			if (this.getMethodName() != null)
				printMessage.append("," + this.getMethodName());
			if (this.getDetailMessage() != null)
				printMessage.append("| CAUSE: " + this.getDetailMessage());

			return printMessage.toString();

		}

		if (this.getMessage() != null) {
			return this.getMessage();
		}

		return super.getMessage();
	}

}
