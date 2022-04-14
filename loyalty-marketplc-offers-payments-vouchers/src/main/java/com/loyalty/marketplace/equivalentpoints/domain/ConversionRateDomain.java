package com.loyalty.marketplace.equivalentpoints.domain;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ConversionRateDomain {

	private String programCode;
	private String partnerCode;
	private String channel;
	private String productItem;
	private Double lowValue;
	private Double highValue;
	private Double pointStart;
	private Double pointEnd;
	private Double coefficientA;
	private Double coefficientB;
	private Double valuePerPoint;
	private Date createdDate;
	private Date lastUpdated;
	private String createdUser;
	private String updatedUser;

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

	public ConversionRateDomain() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConversionRateDomain(ConversionRateBuilder builder) {
		this.programCode = builder.programCode;
		this.partnerCode = builder.partnerCode;
		this.channel = builder.channel;
		this.productItem = builder.productItem;
		this.lowValue = builder.lowValue;
		this.highValue = builder.highValue;
		this.pointStart = builder.pointStart;
		this.pointEnd = builder.pointEnd;
		this.coefficientA = builder.coefficientA;
		this.coefficientB = builder.coefficientB;
		this.valuePerPoint = builder.valuePerPoint;
		this.createdDate = builder.createdDate;
		this.lastUpdated = builder.lastUpdated;
		this.createdUser = builder.createdUser;
		this.updatedUser = builder.updatedUser;
	}

	@Override
	public String toString() {
		return "ConversionRateDomain [programCode=" + programCode + ", partnerCode=" + partnerCode + ", channel="
				+ channel + ", productItem=" + productItem + ", lowValue=" + lowValue + ", highValue=" + highValue
				+ ", pointStart=" + pointStart + ", pointEnd=" + pointEnd + ", coefficientA=" + coefficientA
				+ ", coefficientB=" + coefficientB + ", valuePerPoint=" + valuePerPoint + ", createdDate=" + createdDate
				+ ", lastUpdated=" + lastUpdated + ", createdUser=" + createdUser + ", updatedUser=" + updatedUser
				+ "]";
	}

	public static class ConversionRateBuilder {
		private String programCode;
		private String partnerCode;
		private String channel;
		private String productItem;
		private Double lowValue;
		private Double highValue;
		private Double pointStart;
		private Double pointEnd;
		private Double coefficientA;
		private Double coefficientB;
		private Double valuePerPoint;
		private Date createdDate;
		private Date lastUpdated;
		private String createdUser;
		private String updatedUser;

		public ConversionRateBuilder(Double valuePerPoint) {
			this.valuePerPoint = valuePerPoint;
		}

		public ConversionRateBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public ConversionRateBuilder partnerCode(String partnerCode) {
			this.partnerCode = partnerCode;
			return this;
		}

		public ConversionRateBuilder channel(String channel) {
			this.channel = channel;
			return this;
		}

		public ConversionRateBuilder productItem(String productItem) {
			this.productItem = productItem;
			return this;
		}

		public ConversionRateBuilder lowValue(Double lowValue) {
			this.lowValue = lowValue;
			return this;
		}

		public ConversionRateBuilder highValue(Double highValue) {
			this.highValue = highValue;
			return this;
		}

		public ConversionRateBuilder pointStart(Double pointStart) {
			this.pointStart = pointStart;
			return this;
		}

		public ConversionRateBuilder pointEnd(Double pointEnd) {
			this.pointEnd = pointEnd;
			return this;
		}

		public ConversionRateBuilder coefficientA(Double coefficientA) {
			this.coefficientA = coefficientA;
			return this;
		}

		public ConversionRateBuilder coefficientB(Double coefficientB) {
			this.coefficientB = coefficientB;
			return this;
		}

		public ConversionRateBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public ConversionRateBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public ConversionRateBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public ConversionRateBuilder lastUpdated(Date lastUpdated) {
			this.lastUpdated = lastUpdated;
			return this;
		}

		public ConversionRateDomain build() {
			ConversionRateDomain conversionRateDomain = new ConversionRateDomain(this);
			return conversionRateDomain;
		}
	}

}