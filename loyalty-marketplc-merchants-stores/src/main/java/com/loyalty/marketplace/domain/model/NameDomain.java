package com.loyalty.marketplace.domain.model;

import org.springframework.stereotype.Component;

@Component
public class NameDomain {

	private String english;

	private String arabic;

	public String getEnglish() {
		return english;
	}

	public String getArabic() {
		return arabic;
	}

	public NameDomain() {

	}

	public NameDomain(NameBuilder name) {
		super();
		this.english = name.english;
		this.arabic = name.arabic;
	}
	

	public NameDomain(String english, String arabic) {
		super();
		this.english = english;
		this.arabic = arabic;
	}



	public static class NameBuilder {

		private String english;

		private String arabic;

		public NameBuilder(String english, String arabic) {
			super();
			this.english = english;
			this.arabic = arabic;
		}

		public NameBuilder english(String english) {
			this.english = english;
			return this;

		}

		public NameBuilder arabic(String arabic) {
			this.arabic = arabic;
			return this;

		}

		public NameDomain build() {
			return new NameDomain(this);
		}
	}

}
