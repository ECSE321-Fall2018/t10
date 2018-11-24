package com.ecse321.team10.riderz.dto;

public class DriverPerformanceDto {
	private String username;
	private int tripCount;
	
	public DriverPerformanceDto() { }
	
	public DriverPerformanceDto(String username, int tripCount) {
		this.username = username;
		this.tripCount = tripCount;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getTripCount() {
		return this.tripCount;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setTripCount(int tripCount) {
		this.tripCount = tripCount;
	}
}

