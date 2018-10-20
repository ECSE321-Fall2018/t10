package com.ecse321.team10.riderz.dto;

public class LocationDto {
	private String operator;
	private double longitude;
	private double latitude;
	
	public LocationDto() {
	}

	public LocationDto(String operator, double longitude, double latitude) {
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

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
