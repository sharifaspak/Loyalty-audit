package com.loyalty.marketplace.merchants.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.merchants.constants.DBConstants;

@Document(collection = DBConstants.BARCODE)
public class Barcode {
	@Id	
	private String id;
	
	
	@Field("Name")
	private String name;
	
	@Field("Description")
	private String description;

	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;
	
	@Field("ProgramCode")
	private String program;

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Barcode [id=" + id + ", name=" + name + ", description=" + description + ", usrCreated=" + usrCreated
				+ ", usrUpdated=" + usrUpdated + ", dtCreated=" + dtCreated + ", dtUpdated=" + dtUpdated + ", program="
				+ program + "]";
	}
	
}
