package com.loyalty.marketplace.equivalentpoints.utils;

public class EquivalentPointsException extends Exception {
	private static final long serialVersionUID = -8460356990632230194L;

	private Integer errorCode;
	private String errorMsg;
	private Throwable cause;
	private String className;
	private String methodName;
	private ErrorCodes errorCodes;

	public EquivalentPointsException() {
		super();
	}

	public EquivalentPointsException(ErrorCodes code, String message) {
		super();
		this.errorMsg = message;
		this.errorCode = code.getCode();
		this.errorCodes = code;
	}

	public EquivalentPointsException(String message, Throwable cause, ErrorCodes code) {
		this.errorMsg = message;
		this.errorCode = code.getCode();
		this.cause = cause;
	}

	public EquivalentPointsException(String className, String methodName, String message, ErrorCodes code) {
		super(message);
		this.className = className;
		this.methodName = methodName;
		this.errorMsg = message;
		this.errorCode = code.getCode();

	}

	public EquivalentPointsException(Throwable cause, ErrorCodes code, String message) {
		super(cause);
		this.errorMsg = message;
		this.errorCode = code.getCode();
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
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

	public ErrorCodes getErrorCodes() {
		return errorCodes;
	}

	public String printMessage() {

		if (this.getErrorCode() != null) {

			StringBuilder printMessage = new StringBuilder();
			printMessage.append(" ERROR CODE: " + this.getErrorCode() + "| ERROR MESSAGE:" + this.getErrorMsg());
			if (this.getClassName() != null)
				printMessage.append("| SOURCE:" + this.getClassName());
			if (this.getMethodName() != null)
				printMessage.append("," + this.getMethodName());
			return printMessage.toString();

		}

		if (this.getMessage() != null) {
			return this.getMessage();
		}

		return super.getMessage();
	}
}
