package com.loyalty.marketplace.stores.domain.model;

public class StoreDescriptionDomain {
	
	private String description;
	
	private String descriptionAr;
	
	public StoreDescriptionDomain(StoreDescriptionBuilder storeDescriptionBuilder) {
		super();
		this.description = storeDescriptionBuilder.description;
		this.descriptionAr = storeDescriptionBuilder.descriptionAr;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionAr() {
		return descriptionAr;
	}
	
	public static class StoreDescriptionBuilder{
		
		private String description;
		
		private String descriptionAr;

		public StoreDescriptionBuilder(String description, String descriptionAr) {
			super();
			this.description = description;
			this.descriptionAr = descriptionAr;
		}
		
		public StoreDescriptionDomain build(){
			return new StoreDescriptionDomain(this);
			
		}
		
		
		
	}

}
