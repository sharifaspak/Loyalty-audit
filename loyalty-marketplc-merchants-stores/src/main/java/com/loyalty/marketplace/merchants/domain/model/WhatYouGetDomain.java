package com.loyalty.marketplace.merchants.domain.model;

import org.springframework.stereotype.Component;

@Component
public class WhatYouGetDomain {

	private String whatYouGetEn;

	private String whatYouGetAr;
	
	public String getWhatYouGetEn() {
		return whatYouGetEn;
	}

	public String getWhatYouGetAr() {
		return whatYouGetAr;
	}

	public WhatYouGetDomain() {

	}

	public WhatYouGetDomain(WhatYouGetBuilder name) {
		super();
		this.whatYouGetEn = name.whatYouGetEn;
		this.whatYouGetAr = name.whatYouGetAr;
	}

	public static class WhatYouGetBuilder {

		private String whatYouGetEn;

		private String whatYouGetAr;

		public WhatYouGetBuilder(String whatYouGetEn, String whatYouGetAr) {
			super();
			this.whatYouGetEn = whatYouGetEn;
			this.whatYouGetAr = whatYouGetAr;
			
		}

		public WhatYouGetBuilder whatYouGetEn(String whatYouGetEn) {
			this.whatYouGetEn = whatYouGetEn;
			return this;

		}

		public WhatYouGetBuilder whatYouGetAr(String whatYouGetAr) {
			this.whatYouGetAr = whatYouGetAr;
			return this;

		}

		public WhatYouGetDomain build() {
			return new WhatYouGetDomain(this);
		}
	}
	
}
