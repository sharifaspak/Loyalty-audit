package com.loyalty.marketplace.constants;

public enum UserConstants {
	
	USERNAME("username"),
	JWT_ASSERTION("X-JWT-Assertion"),
	APPLICATION("application"),
	LOYALTY("loyalty"),
	UNATHOURIZED_USER("Unauthorized user"), 
	ROLE("role"),
;
	
	private String constant;
	
	UserConstants(String constant) {
			    this.constant = constant;
			  }
		
		public String get() {
		    return this.constant;
		  }
}
