package com.ecse321.team10.riderz.model;

public class DriverPerformance {
	private String username;
	private int tripCount;
	
	public DriverPerformance(String username, int tripCount) {
		this.username = username;
		this.tripCount = tripCount;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getTripCount() {
		return this.tripCount;
	}
}
