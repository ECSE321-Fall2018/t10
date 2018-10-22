package com.ecse321.team10.riderz.model;

/**
 * An object representing a location entry.
 * @version 1.0
 */
public class Location {
	private String operator;
	private double longitude;
	private double latitude;

	public Location(String operator, double longitude, double latitude) {
		this.operator = operator;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getOperator() {
		return this.operator;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * Obtains a String representation of this object.
	 * @return A String representing the object.
	 */
	public String toString() {
		return "[Location] Operator: " + this.operator + " Longitude: " + this.longitude +
			   " Latitude: " + this.latitude;
	}
}
