package com.loyalty.marketplace.stores.domain.model;

public class StoreAddressDomain {
	
    private String address;
	
	private String addressAr;

	
	public String getAddress() {
		return address;
	}


	public String getAddressAr() {
		return addressAr;
	}


	public StoreAddressDomain(StoreAddressBuilder storeAddressBuilder) {
		this.addressAr = storeAddressBuilder.addressAr;
		this.address  = storeAddressBuilder.address;
	}


	public static class StoreAddressBuilder{
		
		  private String address;
		  
		  private String addressAr;
			
			public StoreAddressBuilder(String address, String addressAr) {
			super();
			this.address = address;
			this.addressAr = addressAr;
		}
			
		public StoreAddressDomain build(){
			return new StoreAddressDomain(this);
			
		}
		
		
		
	}


}
