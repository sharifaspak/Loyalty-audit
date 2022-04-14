package com.loyalty.marketplace.voucher.constants;

public enum VoucherCode implements IVoucherCode{
	
	VALIDATOR_DELIMITOR(" "),
	INVALID_PARAMETER(100, "Invalid input parameters");
	
	private int id = 0;
	private String constant = "";
	  private String msg = "";

	  VoucherCode(int id, String msg) {
	    this.id = id;
	    this.msg = msg;
	  }
	  
	  VoucherCode(String constant) {
		    this.constant = constant;
		  }

	  public int getIntId() {
	    return this.id;
	  }
	  
	  public String getId() {
		 return Integer.toString(this.id);
	  }

	  public String getMsg() {
	    return this.msg;
	  }
	  
	  public String getConstant() {
		    return this.constant;
		  }

}
