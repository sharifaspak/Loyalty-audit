package com.loyalty.marketplace.merchants.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.merchants.constants.DBConstants;

@Document(collection = DBConstants.RATE_TYPE)
public class RateType {

	@Id
	private String typeId;
	
	@Field("ProgramCode")
	private String program;
	
	@Field("RateType")
	private String typeRate;
	@Field("RateDescription")
	private String typeRateDesc;
	@Field("Type")
	private String type;
	
	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;
	
	
	

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeRate() {
		return typeRate;
	}

	public void setTypeRate(String typeRate) {
		this.typeRate = typeRate;
	}

	public String getTypeRateDesc() {
		return typeRateDesc;
	}

	public void setTypeRateDesc(String typeRateDesc) {
		this.typeRateDesc = typeRateDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return "RateType [typeId=" + typeId + ", program=" + program + ", typeRate=" + typeRate + ", typeRateDesc="
				+ typeRateDesc + ", type=" + type + ", usrCreated=" + usrCreated + ", usrUpdated=" + usrUpdated
				+ ", dtCreated=" + dtCreated + ", dtUpdated=" + dtUpdated + "]";
	}
	
}
