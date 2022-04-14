package com.loyalty.marketplace.constants;

public enum CFMConstants {
	LIFESTYLEANDENTERTAINMENT("Lifestyle & Entertainment"),
	MOBILEAPP("Mobile App"),
	DATEFORMAT("dd/MM/yyyy HH:mm:ss"),
	SMS("SMS"),
	ETISALATOFFERS("Etisalat offers"),
	ENGLISH("ENGLISH"),
	ARABIC("ARABIC")
;
	
	private String constant;
	
	CFMConstants(String constant) {
			    this.constant = constant;
			  }
		
		public String get() {
		    return this.constant;
		  }
}
