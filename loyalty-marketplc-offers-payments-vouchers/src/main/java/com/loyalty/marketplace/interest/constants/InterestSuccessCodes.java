package com.loyalty.marketplace.interest.constants;

public enum InterestSuccessCodes {

	        // API Status Codes
			STATUS_SUCCESS(0, "Success");
			
			
			private final int id;

			private final String msg;

			InterestSuccessCodes(int id, String msg) {
				this.id = id;
				this.msg = msg;
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
	
}
