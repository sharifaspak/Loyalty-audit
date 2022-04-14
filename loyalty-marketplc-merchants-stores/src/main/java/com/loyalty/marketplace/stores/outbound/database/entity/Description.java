package com.loyalty.marketplace.stores.outbound.database.entity;

public class Description {
	
	private String description;
	
	private String descriptionArb;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionArb() {
		return descriptionArb;
	}

	public void setDescriptionArb(String descriptionArb) {
		this.descriptionArb = descriptionArb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptionArb == null) ? 0 : descriptionArb.hashCode());
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
		Description other = (Description) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
					return false;
		if (descriptionArb == null) {
			if (other.descriptionArb != null)
				return false;
		} else if (!descriptionArb.equals(other.descriptionArb))
					return false;
		return true;
	}

	@Override
	public String toString() {
		return "Description [description=" + description + ", descriptionArb=" + descriptionArb + "]";
	}
	
}
