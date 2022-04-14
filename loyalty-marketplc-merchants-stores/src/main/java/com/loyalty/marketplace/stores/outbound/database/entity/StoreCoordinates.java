package com.loyalty.marketplace.stores.outbound.database.entity;

public class StoreCoordinates {
	
	private String coordinatesLat;
	
	private String coordinatesLong;

	public String getCoordinatesLat() {
		return coordinatesLat;
	}

	public void setCoordinatesLat(String coordinatesLat) {
		this.coordinatesLat = coordinatesLat;
	}

	public String getCoordinatesLong() {
		return coordinatesLong;
	}

	public void setCoordinatesLong(String coordinatesLong) {
		this.coordinatesLong = coordinatesLong;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinatesLat == null) ? 0 : coordinatesLat.hashCode());
		result = prime * result + ((coordinatesLong == null) ? 0 : coordinatesLong.hashCode());
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
		StoreCoordinates other = (StoreCoordinates) obj;
		if (coordinatesLat == null) {
			if (other.coordinatesLat != null)
				return false;
		} else if (!coordinatesLat.equals(other.coordinatesLat))
					return false;
		if (coordinatesLong == null) {
			if (other.coordinatesLong != null)
				return false;
		} else if (!coordinatesLong.equals(other.coordinatesLong))
					return false;
		return true;
	}

	@Override
	public String toString() {
		return "StoreCoordinates [coordinatesLat=" + coordinatesLat + ", coordinatesLong=" + coordinatesLong + "]";
	}
	
}
