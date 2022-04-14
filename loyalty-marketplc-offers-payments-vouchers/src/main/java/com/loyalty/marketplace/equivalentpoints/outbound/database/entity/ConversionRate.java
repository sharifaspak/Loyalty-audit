package com.loyalty.marketplace.equivalentpoints.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ConversionRate")
public class ConversionRate {

	@Id
	private String id;
	@Field(value = "ProgramCode")
	private String programCode;
	@Field(value = "PartnerCode")
	private String partnerCode;
	@Field(value = "Channel")
	private String channel;
	@Field(value = "ProductItem")
	private String productItem;
	@Field(value = "LowValue")
	private Double lowValue;
	@Field(value = "HighValue")
	private Double highValue;
	@Field(value = "PointStart")
	private Double pointStart;
	@Field(value = "PointEnd")
	private Double pointEnd;
	@Field(value = "CoefficientA")
	private Double coefficientA;
	@Field(value = "CoefficientB")
	private Double coefficientB;
	@Field(value = "ValuePerPoint")
	private Double valuePerPoint;
	@Field(value = "CreatedDate")
	private Date createdDate;
	@Field(value = "UpdatedDate")
	private Date lastUpdated;
	@Field(value = "CreatedUser")
	private String createdUser;
	@Field(value = "UpdatedUser")
	private String updatedUser;

	public ConversionRate() {
		super();
	}

	public ConversionRate(String id, String programCode, String partnerCode, String channel, String productItem,
			Double lowValue, Double highValue, Double pointStart, Double pointEnd, Double coefficientA, Double coefficientB,
			Double valuePerPoint, Date createdDate, Date lastUpdated, String createdUser, String updatedUser) {
		super();
		this.id = id;
		this.programCode = programCode;
		this.partnerCode = partnerCode;
		this.channel = channel;
		this.productItem = productItem;
		this.lowValue = lowValue;
		this.highValue = highValue;
		this.pointStart = pointStart;
		this.pointEnd = pointEnd;
		this.coefficientA = coefficientA;
		this.coefficientB = coefficientB;
		this.valuePerPoint = valuePerPoint;
		this.createdDate = createdDate;
		this.lastUpdated = lastUpdated;
		this.createdUser = createdUser;
		this.updatedUser = updatedUser;
	}

	public String getId() {
		return id;
	}

	public String getProgramCode() {
		return programCode;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public String getChannel() {
		return channel;
	}

	public String getProductItem() {
		return productItem;
	}

	public Double getLowValue() {
		return lowValue;
	}

	public Double getHighValue() {
		return highValue;
	}

	public Double getPointStart() {
		return pointStart;
	}

	public Double getPointEnd() {
		return pointEnd;
	}

	public Double getCoefficientA() {
		return coefficientA;
	}

	public Double getCoefficientB() {
		return coefficientB;
	}

	public Double getValuePerPoint() {
		return valuePerPoint;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setProductItem(String productItem) {
		this.productItem = productItem;
	}

	public void setLowValue(Double lowValue) {
		this.lowValue = lowValue;
	}

	public void setHighValue(Double highValue) {
		this.highValue = highValue;
	}

	public void setPointStart(Double pointStart) {
		this.pointStart = pointStart;
	}

	public void setPointEnd(Double pointEnd) {
		this.pointEnd = pointEnd;
	}

	public void setCoefficientA(Double coefficientA) {
		this.coefficientA = coefficientA;
	}

	public void setCoefficientB(Double coefficientB) {
		this.coefficientB = coefficientB;
	}

	public void setValuePerPoint(Double valuePerPoint) {
		this.valuePerPoint = valuePerPoint;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	@Override
	public String toString() {
		return "ConversionRate [id=" + id + ", programCode=" + programCode + ", partnerCode=" + partnerCode
				+ ", channel=" + channel + ", productItem=" + productItem + ", lowValue=" + lowValue + ", highValue="
				+ highValue + ", pointStart=" + pointStart + ", pointEnd=" + pointEnd + ", coefficientA=" + coefficientA
				+ ", coefficientB=" + coefficientB + ", valuePerPoint=" + valuePerPoint + ", createdDate=" + createdDate
				+ ", lastUpdated=" + lastUpdated + ", createdUser=" + createdUser + ", updatedUser=" + updatedUser
				+ "]";
	}

}
