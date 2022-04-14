package com.loyalty.marketplace.stores.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Store")
public class Store {

	@Id
	private String id;

	@Field("ProgramCode")
	private String programCode;

	@Indexed
	@Field("Code")
	private String storeCode;

	@Field("Description")
	private StoreDescription description;

	@Field("Address")
	private StoreAddress address;

	@Field("CoordinatesLatitude")
	private String latitude;

	@Field("CoordinatesLongitude")
	private String longitude;

	@Field("MerchantCode")
	private String merchantCode;

	@Field("ContactPersons")
	private List<ContactPerson> contactPersons;

	@Field("Status")
	private String status;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;

	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public StoreDescription getDescription() {
		return description;
	}

	public void setDescription(StoreDescription description) {
		this.description = description;
	}

	public StoreAddress getAddress() {
		return address;
	}

	public void setAddress(StoreAddress address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public List<ContactPerson> getContactPersons() {
		return contactPersons;
	}

	public void setContactPersons(List<ContactPerson> contactPersons) {
		this.contactPersons = contactPersons;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDtCreated() {
		return dtCreated;
	}

	public void setDtCreated(Date dtCreated) {
		this.dtCreated = dtCreated;
	}

	public Date getDtUpdated() {
		return dtUpdated;
	}

	public void setDtUpdated(Date dtUpdated) {
		this.dtUpdated = dtUpdated;
	}

	public String getUsrCreated() {
		return usrCreated;
	}

	public void setUsrCreated(String usrCreated) {
		this.usrCreated = usrCreated;
	}

	public String getUsrUpdated() {
		return usrUpdated;
	}

	public void setUsrUpdated(String usrUpdated) {
		this.usrUpdated = usrUpdated;
	}

	@Override
	public String toString() {
		return "Store [id=" + id + ", programCode=" + programCode + ", storeCode=" + storeCode + ", description="
				+ description + ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", merchantCode=" + merchantCode + ", contactPersons=" + contactPersons + ", status=" + status
				+ ", dtCreated=" + dtCreated + ", dtUpdated=" + dtUpdated + ", usrCreated=" + usrCreated
				+ ", usrUpdated=" + usrUpdated + "]";
	}

}
