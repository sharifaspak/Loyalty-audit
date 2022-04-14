package com.loyalty.marketplace.utils;

import com.loyalty.marketplace.constants.IMarketPlaceCode;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;

public class MarketplaceException extends Exception {

	private static final long serialVersionUID = -8460356990632230194L;

	private int errorCode;
	private String errorMsg;
	private String detailMessage;
	private Throwable cause;
	private String className;
	private String methodName;

	public MarketplaceException(IMarketPlaceCode code) {
		super();
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
	}

	public MarketplaceException(String message, Throwable cause, IMarketPlaceCode code) {
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
		this.cause = cause;
	}

	public MarketplaceException(String className, String methodName, String message, IMarketPlaceCode code) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
	}

	public MarketplaceException(Throwable cause, IMarketPlaceCode code) {
		super(cause);
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
	}
	
	public MarketplaceException(String className, String methodName, String message, OfferErrorCodes code) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
	}

	public MarketplaceException(String className, String methodName, String message, OfferExceptionCodes code) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = code.getMsg();
		this.errorCode = code.getIntId();
		this.detailMessage = message;
	}

	public MarketplaceException(String className, String methodName, String message, int code,String errorMsg) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = errorMsg;
		this.errorCode = code;
		this.detailMessage = message;
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
