package com.loyalty.marketplace.inbound.dto;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Barcode")
public class BarcodeDto {

	private String name;
	private String description;
	
	private String id;
	
	
	private String usrCreated;

	private String usrUpdated;

	private Date dtCreated;

	private Date dtUpdated;

	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the usrCreated
	 */
	public String getUsrCreated() {
		return usrCreated;
	}

	/**
	 * @param usrCreated the usrCreated to set
	 */
	public void setUsrCreated(String usrCreated) {
		this.usrCreated = usrCreated;
	}

	/**
	 * @return the usrUpdated
	 */
	public String getUsrUpdated() {
		return usrUpdated;
	}

	/**
	 * @param usrUpdated the usrUpdated to set
	 */
	public void setUsrUpdated(String usrUpdated) {
		this.usrUpdated = usrUpdated;
	}

	/**
	 * @return the dtCreated
	 */
	public Date getDtCreated() {
		return dtCreated;
	}

	/**
	 * @param dtCreated the dtCreated to set
	 */
	public void setDtCreated(Date dtCreated) {
		this.dtCreated = dtCreated;
	}

	/**
	 * @return the dtUpdated
	 */
	public Date getDtUpdated() {
		return dtUpdated;
	}

	/**
	 * @param dtUpdated the dtUpdated to set
	 */
	public void setDtUpdated(Date dtUpdated) {
		this.dtUpdated = dtUpdated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BarcodeDto other = (BarcodeDto) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BarcodeDto [name = " + name + ", description=" + description + "]";
	}

	

}
